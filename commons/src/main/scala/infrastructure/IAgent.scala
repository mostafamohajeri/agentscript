package infrastructure

import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}

trait IAgent {
  def apply(
      name: String,
      yellowPages: IYellowPages,
      MAS: ActorRef[IMessage],
      parent: IMessageSource
  ): Behavior[IMessage]
  def agent_type: String
}
