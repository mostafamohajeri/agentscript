package infrastructure

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}
import infrastructure.access.ASHttpServer
import scala.collection.parallel.CollectionConverters._

case class MAS(val yellowPages: YellowPages = YellowPages()) {

  def apply(name : String = "__MAS"): Behavior[IMessage] = {
    Behaviors.setup { context =>
      {
//      ASHttpServer.create(context)
        var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
        var agentsNotInitialized: Int                 = 0
        val resolver                                  = ActorRefResolver(context.system)
        Behaviors.receive { (context, message) =>
          message match {
            case AgentRequestMessage(types, respondTo) =>
              types foreach {
                case AgentRequest(agentType, name_pattern, count) =>
                  agentsNotInitialized += count
                  for (a <- 1 to count) {
                    val name = if (count == 1) name_pattern else name_pattern + a
                    val ref  = context.spawn(agentType.apply(name, yellowPages , context.self), name)
                    agentsNotStarted = agentsNotStarted :+ ref
                    yellowPages.putOne(AkkaMessageSource(ref))
                  }
                  if (respondTo != null)
                    respondTo ! AgentRequestRespondMessage(agentsNotStarted)
              }
              Behaviors.same
            case ReadyMessage(s) =>
              agentsNotInitialized -= 1
              if (agentsNotInitialized == 0) {
                agentsNotStarted.par.foreach(a => a ! StartMessage())
                agentsNotStarted = Seq[ActorRef[IMessage]]()
              }
              Behaviors.same
            case m : ActorSubscribeMessage =>
              yellowPages.putOne(AkkaMessageSource(m.actor_to_sub_ref))
              Behaviors.same
            case SystemExitMessage() =>
              Behaviors.stopped

            case _ => throw new RuntimeException(f"can no handle message of type $message")
          }

        }
      }
    }
  }
}
