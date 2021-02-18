package infrastructure

import akka.actor.typed.ActorRef

case class AgentRequestMessage(
    agentTypes: Seq[AgentRequest],
    respondTo: ActorRef[IMessage]
) extends Message(Option(AkkaMessageSource(respondTo))) {}
