package infrastructure

import akka.actor.typed.ActorRef

case class BeliefMessage(p_belief: Any,
                         p_sender_ref: ActorRef[IMessage] )
  extends Message(Option(p_sender_ref))
    with IBeliefMessage {
  override def belief: Any = p_belief
}
