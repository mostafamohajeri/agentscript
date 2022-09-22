import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import asl.ihl
import bb.expstyla.exp.{IntTerm, ListTerm, StructTerm, VarTerm}
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike

class IHLSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  val mas = MAS()

  override def beforeAll(): Unit = {
    val m = testKit.spawn(mas(), "MAS")
    val prob = testKit.createTestProbe[IMessage]()
    m ! AgentRequestMessage(
      Seq(
//        AgentRequest(asl.talker.Agent, "talker", 1),
        AgentRequest(new ihl().agentBuilder, "mDevice", 1),
      ),prob.ref)
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

   "target" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("mDevice").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("target",Seq(VarTerm("D"))),AkkaMessageSource(prob.ref))

   }


   "print" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("mDevice").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("get_all_most_preferred_legal",Seq()),AkkaMessageSource(prob.ref))

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