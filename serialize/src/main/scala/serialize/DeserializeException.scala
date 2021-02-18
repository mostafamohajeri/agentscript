package serialize

case class DeserializeException(message: String) extends Throwable {
  override def getMessage: String = message
}
