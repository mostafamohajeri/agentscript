//import java.util
//
//import TheParentPrologKT.{FinishMessage, IMessage, InitMessage}
//import akka.actor.typed.scaladsl.Behaviors
//import akka.actor.typed.{ActorRef, Behavior}
//import it.unibo.tuprolog.core.operators.OperatorSet
//import it.unibo.tuprolog.core.{Atom, Numeric, Struct, Substitution, Term, Truth, Var}
//import it.unibo.tuprolog.solve.library.{Libraries, Library}
//import it.unibo.tuprolog.solve.primitive.Solve
//import it.unibo.tuprolog.solve.{ClassicSolverFactory, ExecutionContext, SideEffectsBuilder, Signature, Solution}
//import it.unibo.tuprolog.theory.Theory
//import kotlin.sequences.Sequence
//import java.util.Map.entry
//
//import it.unibo.tuprolog.solve.exception.error.TypeError
//import kotlin.jvm.functions.Function1
//
//import scala.jdk.CollectionConverters._
//import scala.collection.mutable
//import kotlin.sequences.Sequence
//
//import scala.runtime.AbstractFunction1
//
//
//
//object fun_creator {
//
//  val null_pointer_to_a_side_effect_manager : kotlin.jvm.functions.Function1[_ >: it.unibo.tuprolog.solve.SideEffectsBuilder, kotlin.Unit] = (b: SideEffectsBuilder)  => kotlin.Unit.INSTANCE
//
//  val gt: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
//    val arg1: Term = request.getArguments.get(0)
//    val arg2: Term = request.getArguments.get(1)
//
//
//    if (!arg1.isInstanceOf[Numeric]) {
//      throw (new TypeError.Companion()) forGoal(
//        request getContext,
//        request getSignature,
//        (new TypeError.Expected.Companion()) getNUMBER,
//        arg1
//      )
//    }
//
//    if (!arg2.isInstanceOf[Numeric]) {
//      throw (new TypeError.Companion()) forGoal(
//        request getContext,
//        request getSignature,
//        (new TypeError.Expected.Companion()) getNUMBER,
//        arg2
//      )
//    }
//
//
//    if (arg1.asInstanceOf[Numeric].getDecimalValue.compareTo(arg2.asInstanceOf[Numeric].getDecimalValue) > 0)
//       (() => Seq(request.replySuccess(request.getContext.getSubstitution, null)).asJava.iterator())
//    else
//      () => Seq(request.replyFail(null)).asJava.iterator()
//
//  })
//
//  val replaceAll: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
//    val arg1: Term = request.getArguments.get(0)
//    val arg2: Term = request.getArguments.get(1)
//    val arg3: Term = request.getArguments.get(2)
//    val arg4: Term = request.getArguments.get(3)
//
//    val subs = Substitution.of(arg4.as[Var], Atom.of(arg1.as[Atom].toString.replaceAll(arg2.as[Atom].toString,arg3.as[Atom].toString)))
//
//      (() => Seq(request.replySuccess(subs, null)).asJava.iterator())
//
//  })
//
//}
//
//object SomeActorPrologKT {
//
//  def apply(): Behavior[IMessage] = {
//    Behaviors.setup {
//      context => {
//
//        Behaviors.receive { (context, message) =>
//
//          message match {
//            case InitMessage(actorRef, name) =>
//
//              val gtSignature = new Signature("gt", 2,false)
//              val replaceAllSignature = new Signature("replaceAll", 4,false)
//              //Init the solver
//              val solver =
//                ClassicSolverFactory.INSTANCE.mutableSolverWithDefaultBuiltins(
//                  ClassicSolverFactory.INSTANCE.getDefaultLibraries,
//                  ClassicSolverFactory.INSTANCE.getDefaultFlags,
//                  ClassicSolverFactory.INSTANCE.getDefaultStaticKb,
//                  ClassicSolverFactory.INSTANCE.getDefaultDynamicKb,
//                  ClassicSolverFactory.INSTANCE.getDefaultInputChannel,
//                  ClassicSolverFactory.INSTANCE.getDefaultOutputChannel,
//                  ClassicSolverFactory.INSTANCE.getDefaultErrorChannel,
//                  ClassicSolverFactory.INSTANCE.getDefaultWarningsChannel
//                )
//
//              // Insert something
//              solver.assertA( Struct.of("neighbor", Atom.of("tom"), Numeric.of(10), Atom.of(name)))
//
//              solver.loadLibrary(
//                Library.aliased(
//                  new OperatorSet(),
//                  Theory.empty(),
//                  Map(
//                    gtSignature -> ((fun_creator.gt).asInstanceOf[Function1[_ >: Solve.Request[ _ <: ExecutionContext], _ <: Sequence[Solve.Response]]]),
//                    replaceAllSignature -> ((fun_creator.replaceAll).asInstanceOf[Function1[_ >: Solve.Request[ _ <: ExecutionContext], _ <: Sequence[Solve.Response]]])
//                  ).asJava,
//                  new util.HashMap(),
//                  "it.unibo.lrizzato.myprimives"
//                )
//              )
//
//
//              // The vars
//              var N = Var.of("N")
//              var Distance = Var.of("Distance")
//              var X = Var.of("X")
//              var Neighbor = Var.of("Neighbor")
//              var Z = Var.of("Z")
//
//
//
//              for (i <- 1 to 100) {
//                // Some pointless query
//
////                val tt = Struct.of(",", Struct.of(",", Struct.of(",", Struct.of(",", Struct.of(",", Struct.of(",", Struct.of("=", N, Atom.of(name)), Truth.of(true)), Struct.of("neighbor", Neighbor, Distance, N)), Struct.of("is", X, Struct.of("+", Distance, Numeric.of(i)))), Struct.of("is", Z, Distance)), Struct.of(">", X, Z)), Struct.of("replaceAll", Atom.of("hello"), Atom.of("e"), Atom.of("a"), Var.of("Hallo")))
//
//                val tt = Struct.of(",",
//                  Struct.of("neighbor", Neighbor, Distance, Atom.of(name)),
//                  Struct.of("is",
//                    X,
//                    Struct.of("+",Distance,Numeric.of(i))))
//
//                val sol1 = solver.solve(tt, Int.MaxValue).iterator()
//
//                if (sol1.hasNext) {
//                  // Execption happens here
//                  val sol = sol1.next().asInstanceOf[Solution]
//                  println(sol.isYes)
////                  if (sol.isYes)
////                    println(sol.asInstanceOf[Solution].getSolvedQuery)
//                }
//              }
//
//              actorRef ! FinishMessage(context.self)
//          }
//          Behaviors.stopped
//        }
//      }
//    }
//  }
//}
//
//object TheParentPrologKT {
//
//  trait IMessage
//
//  case class InitMessage(actorRef: ActorRef[IMessage], agentName: String) extends IMessage
//
//  case class FinishMessage(actorRef: ActorRef[IMessage]) extends IMessage
//
//  object StartMessage extends IMessage
//
//  def apply(): Behavior[IMessage] = {
//    Behaviors.setup { context => {
//      var refs = new mutable.HashMap[String, ActorRef[IMessage]]()
//      var ends = 0
//
//
//      for (i <- 1 to 500) {
//        refs.put(i.toString, context.spawn(SomeActorPrologKT(), "a" + i.toString))
//      }
//
//      var t0 = System.nanoTime()
//
//      Behaviors.receive { (context, message) =>
//        message match {
//          case StartMessage =>
//            t0 = System.nanoTime()
//            println("start " + ((System.nanoTime() - t0) / 10e9).toString)
//            var n = 0
//
//            refs.foreach(k => {
//              k._2 ! InitMessage(context.self, f"a$n")
//              n += 1
//            })
//          case FinishMessage(a) =>
//            ends += 1
//            println(ends + ", actor: " + a.path.toStringWithoutAddress + " finished ")
//            if (ends == 500) {
//              println("end " + ((System.nanoTime() - t0) / 1e9).toString)
//            }
//        }
//        Behaviors.same
//      }
//    }
//    }
//  }
//}
