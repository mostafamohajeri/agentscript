package infrastructure

import akka.actor.typed.ActorRef

case class DummyMessageSource(p_name: String) extends IMessageSource {
  override def address(): String = "Dummy"
  override def name(): String = p_name
}