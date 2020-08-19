package infrastructure
import akka.actor.typed.ActorRef

case class SystemExitMessage() extends IMessage {
  override def c_sender: ActorRef[IMessage] = null
  override def c_sender_name: String = null
}
