package infrastructure

import akka.actor.typed.ActorRef

trait IMessage {
  def c_sender: ActorRef[IMessage]
  def c_sender_name: String
}
