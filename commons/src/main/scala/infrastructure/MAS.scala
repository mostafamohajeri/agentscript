package infrastructure

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}
import infrastructure.access.ASHttpServer
import scala.collection.parallel.CollectionConverters._

case class MAS(val yellowPages: YellowPages = YellowPages()) {

  private var _allReady = true
  def allReady : Boolean = _allReady

  def applyDefaults() : Behavior[IMessage] = {
    this.apply()
  }

  def apply(name : String = "__MAS",createHTTPServer: Boolean = false, HTTPPort : Int = 8585): Behavior[IMessage] = {
    Behaviors.setup { context =>
      {
        if(createHTTPServer)
          ASHttpServer.create(context,yellowPages, HTTPPort)
        var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
        var controllers: Seq[ActorRef[IMessage]] = Seq()
        var agentsNotInitialized: Int                 = 0
        val resolver                                  = ActorRefResolver(context.system)
        Behaviors.receive { (context, message) =>
          message match {
            case AgentRequestMessage(types, respondTo) =>
//              println("got request for" + types)
              types foreach {
                case AgentRequest(agentType, name_pattern, count) =>
                  agentsNotInitialized += count
                  _allReady = false;
                  for (a <- 1 to count) {
                    val name = if (count == 1) name_pattern else name_pattern + a
                    val ref  = context.spawn(agentType.apply(name, yellowPages , context.self,AkkaMessageSource(respondTo)), name)
                    agentsNotStarted = agentsNotStarted :+ ref
                    yellowPages.putOne(AkkaMessageSource(ref))
                  }
                  if (respondTo != null) {
//                    respondTo ! AgentRequestRespondMessage(agentsNotStarted)
                    if(!controllers.contains(respondTo))
                      controllers = controllers :+ respondTo
                  }
              }
              Behaviors.same
            case ReadyMessage(s) =>
              agentsNotInitialized -= 1
              if (agentsNotInitialized == 0) {
                agentsNotStarted.par.foreach(a => a ! StartMessage())
                agentsNotStarted = Seq[ActorRef[IMessage]]()
                _allReady = true
                controllers.foreach(a => a ! ReadyMessage(context.self))
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

object MAS {
  def build():MAS = new MAS()
}
