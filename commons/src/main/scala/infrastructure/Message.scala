package infrastructure

import akka.actor.typed.ActorRef

class Message[S <: IMessageSource](src: Option[S] = Option.empty) extends IMessage {
  override def source = src.get
}
