package bb

import java.util.concurrent.ArrayBlockingQueue
import bb.expstyla.exp.{GenericTerm, StructTerm}
import infrastructure.{ExecutionContext, QueryResponse}
import prolog.LogicEngine
import prolog.builtins.{assert, retractall, true_}
import prolog.fluents.DataBase
import prolog.terms.{Clause, Conj, Trail}

import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

@Deprecated
class BeliefBaseStylaConcurrent() extends IBeliefBase[GenericTerm] {

  val db = new DataBase(null)

  var a : ArrayBlockingQueue[LogicEngine] = new ArrayBlockingQueue[LogicEngine](6)

  for(i <- 1 to 6) {
    a.put(new LogicEngine(db))
  }

  override def forceAssertOne(term: GenericTerm): Unit =
    this.synchronized {
      if(!term.isClause)
        db.add(List(term.getTermValue))
      else
        db.add(term.asInstanceOf[StructTerm].asTermList)
    }

  override def assertOne(term: GenericTerm) (implicit executionContext: ExecutionContext) : Boolean =
    this.synchronized
    {
      db.pushIfNotExists(List(term.getTermValue))
    }

  override def assert(terms: List[GenericTerm]) (implicit executionContext: ExecutionContext) : Unit =
    this.synchronized {
      terms.foreach(t => forceAssertOne(t))
    }

  override def retractOne(term: GenericTerm) (implicit executionContext: ExecutionContext) : Boolean =
    this.synchronized
    {
      db.delIfExists(term.getTermValue)
    }

  override def query(term: GenericTerm) (implicit executionContext: ExecutionContext) : QueryResponse =
//    this.synchronized
    {
      val logicEngine = a.take()
      logicEngine.set_query(List(Conj.build(true_(),term.getTermValue)))
//      println(logicEngine.query)
      val answer = logicEngine.askAnswer()
//      println(f"${logicEngine.query }: subs: ${logicEngine.substitutions()}")
      if (answer == null) {
        a.put(logicEngine)
        QueryResponse(result = false, Map[String, GenericTerm]())

      } else {
        val vars =
          logicEngine.substitutions().map(tuple => tuple._1 -> GenericTerm.create(tuple._2.ref))
        a.put(logicEngine)
        QueryResponse(result = true, vars)
      }
    }

  override def bufferedQuery(term: GenericTerm) (implicit executionContext: ExecutionContext) : Iterator[QueryResponse] =
//    this.synchronized
    {
      val logicEngine = a.take()
      val ret = new ListBuffer[QueryResponse]()

      logicEngine.setGoal(term.getTermValue)

      var more = true

      while (more) {
        val answer = logicEngine.askAnswer()
        if (null == answer)
          more = false
        else {
          val vars =
            logicEngine.substitutions().map(tuple => tuple._1 -> GenericTerm.create(tuple._2.ref))
          ret.append(QueryResponse(result = true, vars))
        }
      }
      a.put(logicEngine)
      ret.iterator
    }

  override def query() : QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

  override def matchTerms(term1: GenericTerm, term2: GenericTerm): QueryResponse = {
    val t1 = term1.getTermValue
    val t2 = term2.getTermValue

    val tr = new Trail()
    if (t1.unify(t2, tr)) {
      QueryResponse(
        result = true,
        tr.substitutions() map { v => v._1 -> GenericTerm.create(v._2.ref) }
      )
    } else {
      QueryResponse(result = false, Map[String, GenericTerm]())
    }
  }



  override def matchTerms(): QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

}

