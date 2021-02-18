package infrastructure

import akka.actor.typed.ActorRef

case class IntentionErrorMessage (
    cause: FailureCause = UnknownCause(),
    src: IMessageSource)
  extends Message(Option(src)) {
}
