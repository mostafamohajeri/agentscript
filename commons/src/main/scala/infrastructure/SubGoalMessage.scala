package infrastructure

import akka.actor.typed.ActorRef

case class SubGoalMessage(p_goal: IGoal, p_params: IParams, p_src: IMessageSource)
    extends Message(Option(p_src))
    with ISubGoalMessage {
  override def goal: IGoal     = p_goal
  override def params: IParams = p_params
}
