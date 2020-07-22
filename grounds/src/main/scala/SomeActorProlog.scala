//
//import TheParentProlog.{FinishMessage, IMessage, InitMessage}
//import akka.actor.typed.scaladsl.Behaviors
//import akka.actor.typed.{ActorRef, Behavior}
//import alice.tuprolog.{Prolog, Struct, Var}
//
//import scala.collection.mutable
//
//object SomeActorProlog {
//
//  def apply(): Behavior[IMessage] = {
//    Behaviors.setup { context => {
//
//      Behaviors.receive { (context, message) =>
//
//        message match {
//          case InitMessage(actorRef, name) =>
//            val engine = new Prolog()
//            engine.solve(Struct.of("assertz", Struct.of("neighbor", Struct.atom("tom"), alice.tuprolog.Number.of(10), Struct.atom(name))))
//
//            var N = Var.of("N")
//            var Distance = Var.of("Distance")
//            var X = Var.of("X")
//            var Neighbor = Var.of("Neighbor")
//            var Z = Var.of("Z")
//
//            val tt = Struct.of(",", Struct.of(",", Struct.of(",", Struct.of(",", Struct.of(",", Struct.of("=", N, Struct.atom(name)), Struct.truth(true)), Struct.of("neighbor", Neighbor, Distance, N)), Struct.of("is", X, Struct.of("+", Distance, alice.tuprolog.Number.of(1)))), Struct.of("is", Z, Distance)), Struct.of(">", X, Z))
//
//            val sol1 = engine.solve(tt)
//            if (sol1.isSuccess)
//              println(sol1)
//            throw new RuntimeException
//            actorRef ! FinishMessage(context.self)
//        }
//        Behaviors.stopped
//      }
//    }
//    }
//  }
//}
//
//object TheParentProlog {
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
//      for (i <- 1 to 500) {
//        refs.put(i.toString, context.spawn(SomeActorProlog(), i.toString))
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
//            refs.foreach(k => {
//              k._2 ! InitMessage(context.self, f"agent_$n")
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
