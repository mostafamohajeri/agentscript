package HandyTests

import akka.actor.typed
import akka.actor.typed.{ActorSystem, Scheduler}
import bb.expstyla.exp.{BooleanTerm, GenericTerm, IntTerm, StructTerm}
import infrastructure._

import _root_.scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.language.implicitConversions
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

object Test1 {



  def main(args: Array[String]): Unit = {

        import org.apache.log4j.BasicConfigurator
        BasicConfigurator.configure()




    val mas = MAS()
    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(), "MAS")

    implicit val timeout: Timeout = 5000.milliseconds
    implicit val ec: ExecutionContextExecutor = system.executionContext
    implicit val scheduler: Scheduler = system.scheduler

//    system ! AgentRequestMessage(
//      Seq(
//        //        AgentRequest(asl.talker.Agent, "talker", 1),
////        AgentRequest(new ring.master().agentBuilder, "master1", 1),
////        AgentRequest(new ring.thread().agentBuilder, "thread", 16),
//        AgentRequest(new ring.test().agentBuilder, "test", 1),
//        AgentRequest(new ring.scenario().agentBuilder, "scenario", 1),
//      ),null)

    val result: Future[IMessage] = system.ask(ref => AgentRequestMessage(
      Seq(
        //        AgentRequest(asl.talker.Agent, "talker", 1),
        //        AgentRequest(new ring.master().agentBuilder, "master1", 1),
        //        AgentRequest(new ring.thread().agentBuilder, "thread", 16),
        AgentRequest(new ring.test().agentBuilder, "test", 1),
        AgentRequest(new ring.scenario().agentBuilder, "scenario", 1),
      ),ref))


    val system_ready : Boolean = try {
      val response = Await.result(result, timeout.duration).asInstanceOf[ReadyMessage]
      true
    }
    catch {
      case _ =>
        false
    }


    if(system_ready) {
      mas.yellowPages.getAgent("test").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq(IntTerm(2))),mas.yellowPages.getAgent("scenario").get)
      mas.yellowPages.getAgent("test").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq(IntTerm(2))),mas.yellowPages.getAgent("scenario").get)
      mas.yellowPages.getAgent("test").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq(IntTerm(2))),mas.yellowPages.getAgent("scenario").get)
      mas.yellowPages.getAgent("test").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq(IntTerm(2))),mas.yellowPages.getAgent("scenario").get)
      mas.yellowPages.getAgent("test").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("init",Seq(IntTerm(2))),mas.yellowPages.getAgent("scenario").get)
    }
    else {
      println("Something went wrong")
    }

  }

}
