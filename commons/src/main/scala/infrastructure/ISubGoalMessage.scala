package infrastructure

trait ISubGoalMessage extends IMessage {
  def goal: IGoal
  def params: IParams
}
