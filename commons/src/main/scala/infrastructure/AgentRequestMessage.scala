package infrastructure

import akka.actor.typed.ActorRef

case class AgentRequestMessage(agentTypes: Seq[AgentRequest],respondTo: Option[ActorRef[IMessage]] = Option.empty) extends
  Message(respondTo) {
}
