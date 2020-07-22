package infrastructure

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import infrastructure.access.ASHttpServer

object MAS {

  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context => {
      ASHttpServer.create(context)
      var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
      var agentsNotInitialized : Int = 0
      var t0 = System.nanoTime();
      var t1 : Long = System.nanoTime()
      var yellowPages: ActorRef[IMessage] = context.spawn(YellowPages.apply(), "yp");
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
                  yellowPages.tell(ActorSubscribeMessage(name, ref))
                }
            }
          case ReadyMessage(s) =>
            agentsNotInitialized -= 1
//            println(f"ageNt $s says ready, we have $agentsNotInitialized agents to go")
            if(agentsNotInitialized == 0) {
              println((System.nanoTime() - t0) / 10e9)
              agentsNotStarted.foreach(a => a ! StartMessage())
              agentsNotStarted = Seq[ActorRef[IMessage]]()
            }

          case _=> throw new RuntimeException(f"can no handle message of type $message")
//          case StartMessage() =>
//            agentsNotStarted.foreach(a => a ! StartMessage())
//            agentsNotStarted = Seq[ActorRef[IMessage]]()
        }
        Behaviors.same
      }
    }
    }
  }
}
