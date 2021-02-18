package infrastructure

import akka.actor.typed.ActorRef

case class InitEndMessage(p_src: ActorRef[IMessage])
  extends Message[AkkaMessageSource](Option(AkkaMessageSource(p_src)))
    with ISubGoalMessage {
  override def goal: IGoal     = null
  override def params: IParams = null
}