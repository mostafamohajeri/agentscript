package infrastructure

case class UnBeliefMessage(p_content: Any, src: IMessageSource)
  extends Message(Option(src))
    with IBeliefMessage {
  override def content: Any = p_content
}
