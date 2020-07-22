package infrastructure

import akka.actor.typed.ActorRef

case class ExecutionRequest(stepNo: Int ,step : () => Unit,executionContext: ExecutionContext,
p_sender_ref: ActorRef[IMessage] )
extends Message(Option.empty,Option(p_sender_ref)) {

}
