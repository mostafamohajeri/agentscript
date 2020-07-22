package infrastructure

case class AgentRequestMessage(agentTypes: Seq[AgentRequest]) extends Message() {
}
