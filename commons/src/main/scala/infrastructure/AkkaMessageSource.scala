package infrastructure

import akka.actor.typed.ActorRef

case class AkkaMessageSource(src: ActorRef[IMessage]) extends IMessageSource {
  override def address(): ActorRef[IMessage] = src
  override def name(): String = src.path.name
}