package infrastructure

import akka.actor.typed.ActorRef

case class GoalMessage(p_content: Any, src: IMessageSource)
    extends Message(Option(src))
    with IGoalMessage {
  override def content: Any = p_content
}
