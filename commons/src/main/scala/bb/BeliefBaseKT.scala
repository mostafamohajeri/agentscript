package bb

import java.util

import bb.exp.{GenericTerm, StructTerm}
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

  def assert(term: StructTerm): Boolean = this.synchronized {
//    query(Struct.of(",", Struct.of("not", term), Struct.of("asserta", term))).result

    solver.assertA(term.getTermValue.as[Struct])
//    println(solver.getDynamicKb)
    true
  }

  def assert(terms: List[StructTerm]): Unit = this.synchronized {
    terms.foreach(t => assert(t))
  }

  def retract(term: StructTerm): Boolean = this.synchronized {
//    query(Struct.of("retractAll", term)).result
    solver.retractAll(term.getTermValue.as[Struct])
    true
//    println(solver.getDynamicKb)
//    true
  }

  def query(term: StructTerm): QueryResponse = this.synchronized {

    var sol: Solution = null

    val sol1 = solver.solve(term.getTermValue.as[Struct], Int.MaxValue).iterator()
    if (sol1.hasNext)
      sol = sol1.next().asInstanceOf[Solution]


    if (sol.isYes) {
      val vars: Map[String, GenericTerm] = sol.getSubstitution.asScala map { v => v._1.getName -> GenericTerm.create(v._2) } toMap

      QueryResponse(result = true, vars)
    }
    else {
      QueryResponse(result = false, Map[String, GenericTerm]())
    }
  }

  def bufferedQuery(term: StructTerm): Iterator[QueryResponse] = this.synchronized {

    val ret = new ListBuffer[QueryResponse]()
    solver.solve(term.getTermValue.as[Struct], Int.MaxValue).iterator().asInstanceOf[java.util.Iterator[Solution]].forEachRemaining(s => {
      val vars: Map[String, GenericTerm] = s.getSubstitution.asScala map { v => v._1.getName -> GenericTerm.create(v._2) } toMap

      ret.append(QueryResponse(result = true, vars))
    })
    ret.iterator

  }

  def query(): QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

  def matchTerms(term1: StructTerm, term2: StructTerm): QueryResponse = {
    val ret = unificator.mgu(term1.getTermValue.as[Struct],term2.getTermValue.as[Struct])
    if(ret.isSuccess) {
      val vars: Map[String, GenericTerm] = ret.asScala map { v => v._1.getName -> GenericTerm.create(v._2) } toMap

      QueryResponse(result = true, vars)
    }
    else {
      QueryResponse(result = false, Map[String, GenericTerm]())
    }
  }

  def matchTerms(): QueryResponse = QueryResponse(result = true, Map[String, GenericTerm]())

}

