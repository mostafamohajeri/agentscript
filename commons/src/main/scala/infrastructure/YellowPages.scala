package infrastructure

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.mutable

case class YellowPages() extends IYellowPages {

  private val _agents: mutable.HashMap[String, IMessageSource] = mutable.HashMap()

  override def putOne(iMessageSource: IMessageSource) = {
    iMessageSource match {
      case src : AkkaMessageSource => _agents.put(src.name(),src)
    }
  }

  override def putOne(name: String, iMessageSource: IMessageSource) = {
    iMessageSource match {
      case src : AkkaMessageSource => _agents.put(name,src)
    }
  }

  override def getAgent(name: String): Option[IMessageSource]  = _agents.get(name)

  override def getAll() : Map[String,IMessageSource] = _agents.toMap

}
