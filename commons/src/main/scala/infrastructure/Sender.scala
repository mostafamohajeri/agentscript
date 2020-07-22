package infrastructure

import akka.actor.typed.ActorRef

case class Sender(p_name:String, p_ref : ActorRef[IMessage]) {
  def ref() = p_ref
  def name() = p_name
}
