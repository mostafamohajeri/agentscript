//package HandyTests
//
//import java.util
//
//import it.unibo.tuprolog.core.operators.OperatorSet
//import it.unibo.tuprolog.core.{Numeric, Struct, Term, Var}
//import it.unibo.tuprolog.solve.exception.error.TypeError
//import it.unibo.tuprolog.solve.library.Library
//import it.unibo.tuprolog.solve.primitive.Solve
//import it.unibo.tuprolog.solve._
//import it.unibo.tuprolog.theory.Theory
//import kotlin.jvm.functions.Function1
//import kotlin.sequences.Sequence
//
//import scala.jdk.CollectionConverters._
//
//object fun_creator {
//
//  // Workaround!, the replyFail methods have named args and other than that the javac can not determine the overloads between the two implementations if one
//  val null_pointer_to_a_side_effect_manager: kotlin.jvm.functions.Function1[_ >: it.unibo.tuprolog.solve.SideEffectsBuilder, kotlin.Unit] = (b: SideEffectsBuilder) => kotlin.Unit.INSTANCE
//
//  /*
//  A lot happening here,
//  1- scala lambdas or Java Functions are not accpetable for tuprolog, so we have to use {kotlin.jvm.functions.Function1}
//   */
//  val gt: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] =
//  // 2- the actual function starts here
//    ((request: Solve.Request[ExecutionContext]) => {
//      val arg1: Term = request.getArguments.get(0)
//      val arg2: Term = request.getArguments.get(1)
//
//      if (!arg1.isInstanceOf[Numeric]) {
//        throw (new TypeError.Companion()) forGoal(
//          request getContext,
//          request getSignature,
//          (new TypeError.Expected.Companion()) getNUMBER,
//          arg1
//        )
//      }
//
//      if (!arg2.isInstanceOf[Numeric]) {
//        // Oh the Companion :)
//        throw (new TypeError.Companion()) forGoal(
//          request getContext,
//          request getSignature,
//          (new TypeError.Expected.Companion()) getNUMBER,
//          arg2
//        )
//      }
//
//
//      if (arg1.asInstanceOf[Numeric].getDecimalValue.compareTo(arg2.asInstanceOf[Numeric].getDecimalValue) > 0)
//      // There is a big passage between scala.Seq and kotlin.Sequence, we can not simply return a Seq or even an java.util.Iterator
//      // The return type is an anonymous interface over java.util.Iterator (which is kotlin Sequence in a nutshell), which by the time it reaches the kotlin it is automatically converted to kotlin.Sequence
//      (() => Seq(request.replySuccess(request.getContext.getSubstitution, null)).asJava.iterator())
//      else
//      () => Seq(request.replyFail(null)).asJava.iterator()
//    })
//
//}
//
//object SolverRunner {
//
//  def apply() : Unit = {
//    val gtSignature = new Signature("gt", 2, false)
//    //Init the solver
//    val solver =
//      ClassicSolverFactory.INSTANCE.mutableSolverWithDefaultBuiltins(
//        ClassicSolverFactory.INSTANCE.getDefaultLibraries,
//        ClassicSolverFactory.INSTANCE.getDefaultFlags,
//        ClassicSolverFactory.INSTANCE.getDefaultStaticKb,
//        ClassicSolverFactory.INSTANCE.getDefaultDynamicKb,
//        ClassicSolverFactory.INSTANCE.getDefaultInputChannel,
//        ClassicSolverFactory.INSTANCE.getDefaultOutputChannel,
//        ClassicSolverFactory.INSTANCE.getDefaultErrorChannel,
//        ClassicSolverFactory.INSTANCE.getDefaultWarningsChannel
//      )
//
//
//    solver.loadLibrary(
//      Library.aliased(
//        new OperatorSet(),
//        Theory.empty(),
//        // Why were these "_ >:" and "_ <:" needed
//        // not sure have to look into the source code (probably the types are used as "X implements  ExecutionContext" and so on)
//        Map(gtSignature -> ((fun_creator.gt).asInstanceOf[Function1[_ >: Solve.Request[_ <: ExecutionContext], _ <: Sequence[Solve.Response]]])).asJava,
//        new util.HashMap(),
//        "it.unibo.lrizzato.myprimives"
//      )
//    )
//
//    // The vars
//    var A = Var.of("A")
//    var B = Var.of("B")
//
//    // Some pointless query : A is 20, B is 15 , gt(A,B)
//    val tt =  Struct.of(",", Struct.of(",",Struct.of("is",A,Numeric.of(20)),Struct.of("is",B,Numeric.of(15))),Struct.of("gt",A,B))
//
//    val sol1 = solver.solve(tt, Int.MaxValue).iterator()
//
//    if (sol1.hasNext) {
//      val sol = sol1.next().asInstanceOf[Solution]
//      println(sol.isYes) // prints true
//      if (sol.isYes)
//        println(sol.getSolvedQuery) // prints ((is(20, 20), is(15, 15)), gt(20, 15))
//    }
//  }
//}
//
//
//object GTTest {
//
//  def main(args: Array[String]): Unit = {
//    var N = Seq("a")
//    var B = Seq("baba","mama","bro")
////    val o = for {
////      l1 <- N
////      l2 <- B if l2.contains(l1)
////    } yield {
////      N = Seq(l1)
////      B = Seq(l2)
////    }
//
//
//
////    println( N zipWithFilter B )
//
////    SolverRunner()
//  }
//
//}