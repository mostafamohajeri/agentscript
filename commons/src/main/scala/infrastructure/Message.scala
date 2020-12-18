package infrastructure

import akka.actor.typed.ActorRef

class Message(sender_ref: Option[ActorRef[IMessage]] = Option.empty) extends IMessage {
  override def c_sender: ActorRef[IMessage] = sender_ref.get
  override def c_sender_name: String = sender_ref.get.path.name
}
