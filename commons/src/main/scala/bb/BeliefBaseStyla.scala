package bb

import java.util
import bb.expstyla.exp.{BooleanTerm, GenericTerm, StringTerm, StructTerm, VarMap}
import infrastructure.{ExecutionContext, IAgent, QueryResponse}
import prolog.LogicEngine
import prolog.builtins.{assert, retractall}
import prolog.terms.{Fun, SmallInt, Trail}

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class BeliefBaseStyla() extends IBeliefBase[GenericTerm] {

  val logicEngine = new LogicEngine()

  var vars = VarMap()
  this.forceAssertOne(
    StructTerm(":-",Seq[GenericTerm](StructTerm("most_preferred",Seq[GenericTerm](vars("Pred"))),StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("copy_term",Seq[GenericTerm](vars("Pred"),vars("Pred2"))),vars("Pred"))),StructTerm("forall",Seq[GenericTerm](vars("Pred2"),StructTerm(";",Seq[GenericTerm](StructTerm("->",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("dominates",Seq[GenericTerm](vars("Pred2"),vars("Pred"))),StructTerm("\\==",Seq[GenericTerm](vars("Pred"),vars("Pred2"))))),BooleanTerm(false))),BooleanTerm(true)))))))))
  )
  //  logicEngine.db.pushIfNotExists(logicEngine.parser.parse("most_preferred(G) :- copy_term(G, G2) , G ,  forall((G2, (G2 > G) -> fail; true))"))
  //  val e = new ExecutionContext

  //  solver.loadLibrary(
  //    Library.aliased(
  //      new OperatorSet(),
  //      Theory.empty(),
  //      Map(
  //        prolog.fun_creator.replaceAllSignature -> ((fun_creator.replaceAll).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
  //        prolog.fun_creator.concatSignature -> ((fun_creator.concat).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
  //        prolog.fun_creator.num2strSignature -> ((fun_creator.num2str).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
  //        prolog.fun_creator.str2numSignature -> ((fun_creator.str2num).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]])
  //      ).asJava,
  //      new util.HashMap(),
  //      "utils"
  //    )
  //  )


  override def forceAssertOne(term: GenericTerm) : Unit =
    this.synchronized {
      if(!term.isClause)
        logicEngine.db.add(List(term.getTermValue))
      else
        logicEngine.db.add(term.asInstanceOf[StructTerm].asTermList)
    }

  override def assertOne(term: GenericTerm) (implicit executionContext: ExecutionContext) : Boolean =
    this.synchronized {
      if(!term.isClause)
        logicEngine.db.pushIfNotExists(List(term.getTermValue))
      else
        logicEngine.db.pushIfNotExists(term.asInstanceOf[StructTerm].asTermList)
    }

  override def assert(terms: List[GenericTerm]) (implicit executionContext: ExecutionContext) : Unit =
    this.synchronized {
      terms.foreach(t => forceAssertOne(t))
    }

  override def retractOne(term: GenericTerm) (implicit executionContext: ExecutionContext) : Boolean =
    this.synchronized
    {
      logicEngine.db.delIfExists(term.getTermValue)
    }

  override def query(term: GenericTerm) (implicit executionContext: ExecutionContext) : QueryResponse =
    this.synchronized
    {

      val t = term.getTermValue
//      println(t.toString)
      logicEngine.set_query(List(term.getTermValue))

      val answer = logicEngine.askAnswer()
      if (answer == null) {

        QueryResponse(result = false, Map[String, GenericTerm]())

      } else {
        val vars =
          logicEngine.substitutions().map(tuple => tuple._1 -> GenericTerm.create(tuple._2.ref))

        QueryResponse(result = true, vars,Option(GenericTerm.create(answer.ref)))
      }
    }

  override def bufferedQuery(term: GenericTerm) (implicit executionContext: ExecutionContext) : Iterator[QueryResponse] =
    this.synchronized
    {

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

      ret.iterator
    }

  override def query() : QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

  override def matchTerms(term1: GenericTerm, term2: GenericTerm) : QueryResponse = {
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

  override def matchTerms() : QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

}

