import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.StructTerm
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike

class AgentSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  override def beforeAll(): Unit = {
    val mas = testKit.spawn(MAS(), "MAS")
    mas ! AgentRequestMessage(
      Seq(
        AgentRequest(asl.talker.Agent, "talker", 1),
      ))
    Thread.sleep(3000)
  }

  "An agent" should {
    "exist in yellow pages if it was created before" in {
      eventually (
        assert(YellowPages.agentsPersistentCentral.contains("talker"))
      )
    }
  }

  "An agent" should {
    "respond with a IntentionDoneMessage if it has a plan for it" in {
      val prob = testKit.createTestProbe[IMessage]()
      assert(YellowPages.agentsPersistentCentral.contains("talker"))

      YellowPages.agentsPersistentCentral("talker") ! GoalMessage(StructTerm("say_hi",Seq()),prob.ref)

      val response = prob.receiveMessage()

      assert(response.isInstanceOf[IntentionDoneMessage])

    }

    "respond with a IntentionErrorMessage if it has no plan for it" in {
      val prob = testKit.createTestProbe[IMessage]()

      YellowPages.agentsPersistentCentral("talker") ! GoalMessage(StructTerm("say_bye",Seq()),prob.ref)

      prob.expectMessage(IntentionErrorMessage(NoPlanMessage()))
    }

    "send a GoalMessage if the plan says so" in {
      val prob = testKit.createTestProbe[IMessage]()

      YellowPages.agentsPersistentCentral("talker") ! GoalMessage(StructTerm("ask_me_something",Seq()),prob.ref)

      val response = prob.receiveMessage()

      assert(response.isInstanceOf[GoalMessage])
      assert(response.asInstanceOf[GoalMessage].p_belief.asInstanceOf[StructTerm].functor.equals("kill_me"))

    }
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}