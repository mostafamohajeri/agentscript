package infrastructure

trait IYellowPages {

  def putOne(iMessageSource: IMessageSource)

  def getAgent(name: String): Option[IMessageSource]

  def getAll(): Map[String, IMessageSource]

  def putOne(name: String, iMessageSource: IMessageSource)
}
