package infrastructure

import akka.actor.typed.ActorRef

case class ActorMessage(
    destination_name: String,
    message: IMessage,
    p_sender_ref: ActorRef[IMessage]
) extends Message(Option(AkkaMessageSource(p_sender_ref))) {}
