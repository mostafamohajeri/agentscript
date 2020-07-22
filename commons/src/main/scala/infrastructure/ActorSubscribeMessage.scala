package infrastructure

import akka.actor.typed.ActorRef

case class ActorSubscribeMessage(actor_to_sub_name: String, actor_to_sub_ref: ActorRef[IMessage])extends Message()
