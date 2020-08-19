package infrastructure

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}
import infrastructure.access.ASHttpServer
import scala.collection.parallel.CollectionConverters._

object MAS {

  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context => {
//      ASHttpServer.create(context)
      var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
      var agentsNotInitialized : Int = 0
      val resolver = ActorRefResolver(context.system)
      var t0 = System.nanoTime();
      var t1 : Long = System.nanoTime()
      var yellowPages: ActorRef[IMessage] = context.spawn(YellowPages.apply(), "yp");
      yellowPages ! ActorSubscribeMessage("__MAS",context.self)
      Behaviors.receive { (context, message) =>
        message match {
          case AgentRequestMessage(types) =>
            types foreach {
              case AgentRequest(agentType,name_pattern, count) =>
                agentsNotInitialized += count
                for (a <- 1 to count) {
                  val name = name_pattern + a
                  t0 = System.nanoTime()
                  val ref = context.spawn(agentType.apply(name, yellowPages,context.self), name)
                  agentsNotStarted = agentsNotStarted :+ ref
                  yellowPages ! ActorSubscribeMessage(name, ref)
                }
            }
            Behaviors.same
          case ReadyMessage(s) =>

            agentsNotInitialized -= 1
//            println(f"ageNt $s says ready, we have $agentsNotInitialized agents to go")
            if(agentsNotInitialized == 0) {
              println((System.nanoTime() - t0) / 10e9)
              agentsNotStarted.par.foreach(a => a ! StartMessage())
              agentsNotStarted = Seq[ActorRef[IMessage]]()
            }
            Behaviors.same
          case SystemExitMessage() =>
            Behaviors.stopped

          case _=> throw new RuntimeException(f"can no handle message of type $message")
//          case StartMessage() =>
//            agentsNotStarted.foreach(a => a ! StartMessage())
//            agentsNotStarted = Seq[ActorRef[IMessage]]()
        }

      }
    }
    }
  }
}
