package infrastructure

import akka.actor.typed.ActorRef

case class IntentionFailedMessage(sender_name: Option[String] = Option.empty, sender_ref: Option[ActorRef[IMessage]] = Option.empty) extends IMessage {
  override def c_sender: ActorRef[IMessage] = sender_ref.get
  override def c_sender_name: String = sender_name.get
}
