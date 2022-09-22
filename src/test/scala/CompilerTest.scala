import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.StructTerm
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike

import scala.tools.nsc.ScriptRunner

class CompilerTest extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  override def beforeAll(): Unit = {

  }

 "An agent" should {


   "respond with a IntentionErrorMessage if it has no plan for it" in {
       val system = new scriptcc.ScriptRunner
       system.createMas("/home/msotafa/IdeaProjects/actor-playgrounds/src/test/asl/input.json",true)
       Thread.sleep(2000)
   }
 }



//  override def afterAll(): Unit = testKit.shutdownTestKit()
}