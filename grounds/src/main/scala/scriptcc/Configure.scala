package scriptcc

import agentfactory.FactoryManager
import akka.actor.ActorSystem
import infrastructure.{AgentRequest, IMessage}

import scala.io.Source

object Configure {
  def create(jsonFile: String) : Seq[AgentRequest] = {
    val file = jsonFile
    val source = Source.fromFile(file)

    val loc = file.replaceAll(file.split("/").last,"")


    val input = source.getLines().mkString

    val data = ujson.read(input)
    val r : Seq[AgentRequest] = data("agents").arr.map(
      o =>
        AgentRequest(FactoryManager.create(loc+o("script_file").str,o("name").str), o("name").str, o("number").num.toInt)
    ).toSeq

    r

  }
}
