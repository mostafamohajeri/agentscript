package agentfactory
import infrastructure.IAgent
import translation.Translator

object FactoryManager {





  def create(file: String,name: String) : IAgent = {
    val translator: Translator = new Translator()
    val code = translator.translate(file,name)
    val factory : IFactory = FactoryCompiler.createFactory(code,name)
    factory.getAgent()
  }

}
