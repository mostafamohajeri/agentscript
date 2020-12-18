package infrastructure

import akka.actor.typed.ActorRef

case class InitEndMessage(p_sender_ref: ActorRef[IMessage])
  extends  Message(Option(p_sender_ref))
    with ISubGoalMessage {
  override def goal: IGoal = null
  override def params: IParams = null
}

