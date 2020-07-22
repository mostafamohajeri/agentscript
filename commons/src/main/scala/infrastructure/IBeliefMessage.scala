package infrastructure

trait IBeliefMessage extends IMessage {
  def belief: Any
}
