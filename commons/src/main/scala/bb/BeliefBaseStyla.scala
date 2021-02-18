package bb

import java.util

import bb.expstyla.exp.{GenericTerm, StringTerm, StructTerm}
import infrastructure.QueryResponse
import prolog.LogicEngine
import prolog.builtins.{assert, retractall}
import prolog.terms.{Fun, SmallInt, Trail}

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class BeliefBaseStyla() {

  val logicEngine = new LogicEngine()

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

  val lru = new LRUCache[StructTerm, QueryResponse](100)

  def assert(term: StructTerm): Boolean =
    this.synchronized {
      val assert = new assert()
      assert.args = Array(term.getTermValue)
      logicEngine.setGoal(assert)
      val answer = logicEngine.askAnswer()
      true
    }

  def assert(terms: List[StructTerm]): Unit =
    this.synchronized {
      terms.foreach(t => assert(t))
    }

  def retract(term: StructTerm): Boolean =
    this.synchronized {
      val retractAll = new retractall()
      retractAll.args = Array(term.getTermValue)
      logicEngine.setGoal(retractAll)
      logicEngine.askAnswer()
      true
    }

  def query(term: StructTerm): QueryResponse =
    this.synchronized {

      logicEngine.set_query(List(term.getTermValue))

      val answer = logicEngine.askAnswer()

      if (answer == null) {

        QueryResponse(result = false, Map[String, GenericTerm]())

      } else {
        val vars =
          logicEngine.substitutions().map(tuple => tuple._1 -> GenericTerm.create(tuple._2.ref))

        QueryResponse(result = true, vars)
      }
    }

  def bufferedQuery(term: StructTerm): Iterator[QueryResponse] =
    this.synchronized {

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

  def query(): QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

  def matchTerms(term1: StructTerm, term2: StructTerm): QueryResponse = {
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

  def matchTerms(): QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

}

import java.util.Collections.synchronizedMap

import scala.jdk.CollectionConverters._
import scala.collection.mutable

class LRUCache[K, V](maxEntries: Int) extends java.util.LinkedHashMap[K, V](100, .75f, true) {

  override def removeEldestEntry(eldest: java.util.Map.Entry[K, V]): Boolean = size > maxEntries

}

object LRUCache {
  def apply[K, V](maxEntries: Int): mutable.Map[K, V] =
    synchronizedMap(new LRUCache[K, V](maxEntries)).asScala
}
