package std

import infrastructure._

import scala.collection.parallel.CollectionConverters._

object utils extends AgentUtils {

  override def exit()(implicit executionContext: ExecutionContext): Unit = {
    executionContext.agent.system.terminate()
  }

}
