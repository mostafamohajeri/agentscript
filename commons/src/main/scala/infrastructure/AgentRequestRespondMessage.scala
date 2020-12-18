package infrastructure

import akka.actor.typed.ActorRef

case class AgentRequestRespondMessage(agents: Seq[ActorRef[IMessage]]) extends Message() {
}
