package agentfactory
import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

object FactoryCompiler {

  def createFactory(code: String, name: String): IFactory = {
    val tb   = universe.runtimeMirror(this.getClass.getClassLoader).mkToolBox()
    val tree = tb.parse(f"""
         | import agentfactory.IFactory
         | import infrastructure.IAgent
         |
         | class Factory_$name extends IFactory{
         |
         | override def getAgent() = this.$name.Agent
         |
         | $code
         |
         | }
         |
         | new Factory_$name()
         |
    """.stripMargin)

    tb.compile(tree)().asInstanceOf[IFactory]
  }

}
