import akka.actor.testkit.typed.scaladsl.{ActorTestKit, ScalaTestWithActorTestKit}
import akka.actor.typed.ActorRef
import infrastructure.{AgentRequest, AgentRequestMessage, AgentRequestRespondMessage, IMessage, MAS, SystemExitMessage}
import org.scalatest.BeforeAndAfterAll

import collection.mutable.Stack
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class MASSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {



  "A MAS" must {
    "create agents from specs sent to it" in {
      val mas = testKit.spawn(MAS(), "MAS")
      val prob = testKit.createTestProbe[IMessage]()
      mas ! AgentRequestMessage(
        Seq(
          AgentRequest(asl.talker.Agent, "talker", 2),
        ),
        Option(prob.ref))

      val response = prob.receiveMessage()

      assert(response.isInstanceOf[AgentRequestRespondMessage])
      assert(response.asInstanceOf[AgentRequestRespondMessage].agents.size == 2)
    }

    "stop when asked to" in {
      val mas  = testKit.spawn(MAS(), "MAS")
      val prob = testKit.createTestProbe[IMessage]()

      mas ! SystemExitMessage()
      prob.expectTerminated(mas)

    }
  }



  override def afterAll(): Unit = testKit.shutdownTestKit()
}