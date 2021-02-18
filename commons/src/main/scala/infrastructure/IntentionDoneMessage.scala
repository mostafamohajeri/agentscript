package infrastructure

import akka.actor.typed.ActorRef

case class IntentionDoneMessage(
                                 src: IMessageSource)
  extends Message(Option(src)) {
}
