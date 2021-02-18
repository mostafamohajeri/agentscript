package serialize

case class SerializeException(message: String) extends Throwable {
  override def getMessage: String = message
}
