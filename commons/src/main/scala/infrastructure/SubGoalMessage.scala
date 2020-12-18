package infrastructure

import akka.actor.typed.ActorRef

case class SubGoalMessage(p_goal: IGoal,
                          p_params: IParams,
                          p_sender_ref: ActorRef[IMessage] )
  extends Message(Option(p_sender_ref))
    with ISubGoalMessage {
  override def goal: IGoal = p_goal
  override def params: IParams = p_params
}
