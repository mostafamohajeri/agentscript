package infrastructure

trait FailureCause
case class UnknownCause(text: String = "unknown") extends FailureCause
case class NoApplicablePlanMessage()              extends FailureCause
case class NoPlanMessage()                        extends FailureCause
