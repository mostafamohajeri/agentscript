package infrastructure

trait IGoalMessage extends IMessage {
  def belief: Any
}
