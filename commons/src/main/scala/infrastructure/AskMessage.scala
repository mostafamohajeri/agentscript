package infrastructure

case class AskMessage(p_content: Any, src: IMessageSource)
  extends Message(Option(src))
    with IAskMessage {
  override def content: Any = p_content
}
