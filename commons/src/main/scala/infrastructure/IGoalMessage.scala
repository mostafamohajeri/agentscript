package infrastructure

trait IGoalMessage extends IMessage {
  def content: Any
}
