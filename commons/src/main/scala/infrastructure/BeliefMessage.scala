package infrastructure

import akka.actor.typed.ActorRef

case class BeliefMessage(p_content: Any, src: IMessageSource)
  extends Message(Option(src))
    with IBeliefMessage {
  override def content: Any = p_content
}
