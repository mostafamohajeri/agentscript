package infrastructure

import akka.actor.typed.ActorRef

case class ReadyMessage(p_src: ActorRef[IMessage])
  extends Message[AkkaMessageSource](Option(AkkaMessageSource(p_src)))
