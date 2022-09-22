import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.{IntTerm, ListTerm, StructTerm}
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike

class AgentSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  val mas = MAS()

  override def beforeAll(): Unit = {
    val m = testKit.spawn(mas(), "MAS")
    val prob = testKit.createTestProbe[IMessage]()
    m ! AgentRequestMessage(
      Seq(
//        AgentRequest(asl.talker.Agent, "talker", 1),
        AgentRequest(new asl.greeter().agentBuilder, "greeter", 1),
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

   "respond with a IntentionErrorMessage if it has no plan for it" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("something_else",Seq()),AkkaMessageSource(prob.ref))

    val message = prob.receiveMessage()

     assert(message.isInstanceOf[IntentionErrorMessage])
     assert(message.asInstanceOf[IntentionErrorMessage].cause equals NoPlanMessage())
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

  "A greeter agent" should {
    "say greetings in response to a hello" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address()  ! GoalMessage(StructTerm("hello"),AkkaMessageSource(prob.ref))
      assert(prob.receiveMessage().asInstanceOf[GoalMessage].content.toString  equals  "greetings")
    }

    "loop a list" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address()  ! GoalMessage(StructTerm("loop_this",Seq(ListTerm(Seq(IntTerm(1),IntTerm(4))))),AkkaMessageSource(prob.ref))
//      assert(prob.receiveMessage().asInstanceOf[GoalMessage].content.toString  equals  "greetings")
    }

    "loop" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("greeter").get.asInstanceOf[AkkaMessageSource].address()  ! GoalMessage(StructTerm("loop",Seq()),AkkaMessageSource(prob.ref))
      //      assert(prob.receiveMessage().asInstanceOf[GoalMessage].content.toString  equals  "greetings")
    }
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}