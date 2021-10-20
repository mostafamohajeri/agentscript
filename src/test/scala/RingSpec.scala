import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.StructTerm
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike

class RingSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  val mas = MAS()

  override def beforeAll(): Unit = {
    val m = testKit.spawn(mas(), "MAS")
    m ! AgentRequestMessage(
      Seq(
//        AgentRequest(asl.talker.Agent, "talker", 1),
        AgentRequest(new asl.master().agentBuilder, "master1", 1),
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


   "respond with a IntentionErrorMessage if it has no plan for it" in {
     val prob = testKit.createTestProbe[IMessage]()

     mas.yellowPages.getAgent("master1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("something_else",Seq()),AkkaMessageSource(prob.ref))

     val message = prob.receiveMessage()

     assert(message.isInstanceOf[IntentionErrorMessage])
     assert(message.asInstanceOf[IntentionErrorMessage].cause equals NoPlanMessage())
   }
 }



  override def afterAll(): Unit = testKit.shutdownTestKit()
}