package infrastructure

import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}

trait IAgent {
  def apply(
      name: String,
      yellowPages: ActorRef[IMessage],
      MAS: ActorRef[IMessage]
  ): Behavior[IMessage]
  def agent_type: String
}
