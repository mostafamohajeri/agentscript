package HandyTests

import akka.actor.typed
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import bb.expstyla.exp.StructTerm
import bb.serialize.terms.JsonTermDeserializer
import com.google.gson.Gson
import infrastructure.access.AgentRegistry.deserializer
import infrastructure.{AgentRequest, AgentRequestMessage, AkkaMessageSource, GoalMessage, IMessage, MAS}

import scala.language.implicitConversions

object Ring {

  val deserializer = JsonTermDeserializer(new Gson())

  def main(args: Array[String]): Unit = {

        import org.apache.log4j.BasicConfigurator
        BasicConfigurator.configure()



    val mas = MAS()
    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(), "MAS")
    system ! AgentRequestMessage(
      Seq(
        //        AgentRequest(asl.talker.Agent, "talker", 1),
        AgentRequest(new ring.master().agentBuilder, "master1", 1),
        AgentRequest(new ring.thread().agentBuilder, "thread", 16),
      ),null)
    Thread.sleep(2000)

    mas.yellowPages.getAgent("master1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq()),mas.yellowPages.getAgent("master1").get)

  }

}
