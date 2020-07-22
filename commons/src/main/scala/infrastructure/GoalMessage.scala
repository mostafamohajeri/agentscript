package infrastructure

import akka.actor.typed.ActorRef

case class GoalMessage(p_belief: Any,
                       p_sender_name: String,
                       p_sender_ref: ActorRef[IMessage] )
  extends Message(Option(p_sender_name),Option(p_sender_ref))
    with IGoalMessage {
  override def belief: Any = p_belief
}
