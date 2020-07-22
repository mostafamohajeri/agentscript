package bb

import java.util

import infrastructure.QueryResponse
import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.core.{Atom, Struct, Term}
import it.unibo.tuprolog.dsl.unify.PrologWithUnificationImpl
import it.unibo.tuprolog.solve.library.{Libraries, Library}
import it.unibo.tuprolog.solve.primitive.Solve
import it.unibo.tuprolog.solve.{ClassicSolverFactory, ExecutionContext, MutableSolver, Solution, Solver}
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.unify.Unificator
import kotlin.jvm.functions.Function1
import kotlin.sequences.Sequence
import std.prolog
import std.prolog.fun_creator

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class BeliefBaseKT() {

  val unificator = new PrologWithUnificationImpl(Unificator.getDefault);

  val solver: MutableSolver =
    ClassicSolverFactory.INSTANCE.mutableSolverWithDefaultBuiltins(
      new Libraries(
        Library.aliased(
          new OperatorSet(),
          Theory.empty(),
          Map(
            prolog.fun_creator.replaceAllSignature -> ((fun_creator.replaceAll).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
            prolog.fun_creator.concatSignature -> ((fun_creator.concat).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
            prolog.fun_creator.num2strSignature -> ((fun_creator.num2str).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
            prolog.fun_creator.str2numSignature -> ((fun_creator.str2num).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
            prolog.fun_creator.betweenSignature -> ((fun_creator.between).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]])
          ).asJava,
          new util.HashMap(),
          "utils"
        )
      )
    )

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

  def assert(term: Struct): Boolean = this.synchronized {
//    query(Struct.of(",", Struct.of("not", term), Struct.of("asserta", term))).result

    solver.assertA(term)
//    println(solver.getDynamicKb)
    true
  }

  def assert(terms: List[Struct]): Unit = this.synchronized {
    terms.foreach(t => assert(t))
  }

  def retract(term: Struct): Boolean = this.synchronized {
//    query(Struct.of("retractAll", term)).result
    solver.retractAll(term)
    true
//    println(solver.getDynamicKb)
//    true
  }

  def query(term: Struct): QueryResponse = this.synchronized {

    var sol: Solution = null

    val sol1 = solver.solve(term, Int.MaxValue).iterator()
    if (sol1.hasNext)
      sol = sol1.next().asInstanceOf[Solution]


    if (sol.isYes) {
      val vars: Map[String, Term] = sol.getSubstitution.asScala map { v => v._1.getName -> v._2 } toMap

      QueryResponse(result = true, vars)
    }
    else {
      QueryResponse(result = false, Map[String, Term]())
    }
  }

  def rawQuery(term: Struct): Iterator[Solution] = this.synchronized {

    val ret = new ListBuffer[Solution]()
    solver.solve(term, Int.MaxValue).iterator().asInstanceOf[java.util.Iterator[Solution]].forEachRemaining(s => ret.append(s))
    ret.iterator

  }

  def query(): QueryResponse = QueryResponse(result = true, Map[String, Term]())

  def matchTerms(term1: Term, term2: Term): QueryResponse = {
    val ret = unificator.mgu(term1,term2)
    if(ret.isSuccess) {
      val vars: Map[String, Term] = ret.asScala map { v => v._1.getName -> v._2 } toMap

      QueryResponse(result = true, vars)
    }
    else {
      QueryResponse(result = false, Map[String, Term]())
    }
  }

  def matchTerms(): QueryResponse = QueryResponse(result = true, Map[String, Term]())

  def mgu(term1: Term, term2: Term): Unit = {
    println(unificator.mgu(term1, term2))
  }
}

