package agentfactory

import infrastructure.IAgent

trait IFactory {
  def getAgent(): IAgent

}
