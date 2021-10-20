import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.{IntTerm, StringTerm, StructTerm, VarTerm}
import infrastructure._
import org.stringtemplate.v4.gui.JTreeScopeStackModel.StringTree
//import org.scalatest.tools.Durations.Duration
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration.Duration
import org.scalatest.time.SpanSugar._

class ConcurrencySpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  val mas = MAS()

  override def beforeAll(): Unit = {
    val m = testKit.spawn(mas(), "MAS")
    m ! AgentRequestMessage(
      Seq(
//        AgentRequest(asl.talker.Agent, "talker", 1),
        AgentRequest(new asl.simple_bb_access().agentBuilder, "greeter", 1),
        AgentRequest(new asl.asker().agentBuilder, "asker", 1),
        AgentRequest(new asl.responder().agentBuilder, "responder", 1),
      ),null)
    Thread.sleep(3000)
  }
//
//  "An agent" should {
//    "exist in yellow pages if it was created before" in {
//      eventually (
//        assert(YellowPages.agents.contains("talker"))
//      )
//    }
//  }

 "An agent" should {
  //  "respond with a IntentionDoneMessage if it has a plan for it" in {
  //    val prob = testKit.createTestProbe[IMessage]()
  //    assert(YellowPages.agents.contains("talker"))

  //    YellowPages.agents("talker") ! GoalMessage(StructTerm("say_hi",Seq()),AkkaMessageSource(prob.ref))

  //    val response = prob.receiveMessage()

  //    assert(response.isInstanceOf[IntentionDoneMessage])

  //  }

   "respond with twice the number of messages it recieves" in {
     val prob = testKit.createTestProbe[IMessage]()

     for(w <-1 to 4) {
       mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("read", Seq(IntTerm(w))), AkkaMessageSource(prob.ref))
     }
     val response = prob.receiveMessages(8,10 seconds)

//     assert(response.last.asInstanceOf[GoalMessage].content.toString  equals  "greeter")

   }

   "respond" in {
     val prob = testKit.createTestProbe[IMessage]()


     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))

     val response = prob.receiveMessage(10 seconds)

     println(response.asInstanceOf[AskMessage].content.toString)
     assert(response.asInstanceOf[AskMessage].content.toString  contains "name")
     response.asInstanceOf[AskMessage].source.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))


   }


   "respond2" in {
     val prob = testKit.createTestProbe[IMessage]()


     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("newinfo",Seq(StringTerm("wow"))), AkkaMessageSource(prob.ref))

//     val response = prob.receiveMessage(10 seconds)

//     println(response.asInstanceOf[AskMessage].content.toString)
//     assert(response.asInstanceOf[AskMessage].content.toString  contains "name")
//     response.asInstanceOf[AskMessage].source.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))


   }

   "respond3" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("newinfo",Seq(StringTerm("wow"))), AkkaMessageSource(prob.ref))
     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("test",Seq()), AkkaMessageSource(prob.ref))

     val response = prob.receiveMessage(10 seconds)
     println(response)
     //     println(response.asInstanceOf[AskMessage].content.toString)
     //     assert(response.asInstanceOf[AskMessage].content.toString  contains "name")
     //     response.asInstanceOf[AskMessage].source.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))


   }

   "test messages" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("asker").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))
//     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("test",Seq()), AkkaMessageSource(prob.ref))

//     val response = prob.receiveMessage(10 seconds)
//     println(response)
     //     println(response.asInstanceOf[AskMessage].content.toString)
     //     assert(response.asInstanceOf[AskMessage].content.toString  contains "name")
     //     response.asInstanceOf[AskMessage].source.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(StructTerm("do",Seq()), AkkaMessageSource(prob.ref))


   }




   //  "send a GoalMessage if the plan says so" in {
  //    val prob = testKit.createTestProbe[IMessage]()

  //    YellowPages.agents("talker") tell GoalMessage(StructTerm("hi",Seq()),prob.ref)

  //    val response = prob.receiveMessage()

  //    assert(response.asInstanceOf[GoalMessage].p_belief.toString  equals  "hello")
  //    assert(response.isInstanceOf[GoalMessage])
  //    assert(response.asInstanceOf[GoalMessage].p_belief.asInstanceOf[StructTerm].functor.equals("hello"))

  //  }


 }


  override def afterAll(): Unit = testKit.shutdownTestKit()
}