package infrastructure

import akka.actor.typed.ActorRef

case class ReadyMessage(p_actor_ref : ActorRef[IMessage]) extends Message(sender_ref = Option(p_actor_ref)) {
}
