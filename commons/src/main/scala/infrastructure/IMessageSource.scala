package infrastructure
import akka.actor.typed.ActorRef

trait IMessageSource {
  def address(): Any
  def name(): String
}
