package agentfactory
import infrastructure.{IAgent, IntentionalAgentFactory}

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

object FactoryCompiler {

  def createFactory(code: String, name: String): IAgent = {
    val tb   = universe.runtimeMirror(this.getClass.getClassLoader).mkToolBox()
    val tree = tb.parse(f"""
         | import infrastructure.IntentionalAgentFactory
         | import infrastructure.IAgent
         |
         | $code
         |
         | new $name().agentBuilder
         |
    """.stripMargin)

//    val tree = tb.parse(  """
//                            |import agentfactory.IFactory
//                            |import infrastructure.IAgent
//                            |class AgentHelloWorld extends IFactory{
//                            |  def getAgent(): asl.asker.agentBuilder
//                            |}
//                            |
//                            |
//                            |new AgentHelloWorld()
//                            |""".stripMargin)

//    println(tree)

    tb.compile(tree)().asInstanceOf[IAgent]
  }

}
