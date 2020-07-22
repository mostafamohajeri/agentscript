package infrastructure

import akka.actor.typed.ActorRef

case class ActorMessage(destination_name: String,
                        message: IMessage,
                        p_sender_name: String,
                        p_sender_ref: ActorRef[IMessage] )
  extends Message(Option(p_sender_name),Option(p_sender_ref)) {

}
