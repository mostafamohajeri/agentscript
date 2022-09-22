package agentfactory
import infrastructure.{IAgent, IntentionalAgentFactory}
import translation.Translator

object FactoryManager {

  def create(file: String, name: String): IAgent = {
    println(f"transpiling file $file ...")
    val translator: Translator = new Translator()
    val code                   = translator.translate(file, name)

    println(f"creating agent $name ...")

//    println(code)

    val factory: IAgent = FactoryCompiler.createFactory(code, name)
    factory
  }

}
