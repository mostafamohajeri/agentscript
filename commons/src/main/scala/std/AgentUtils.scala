package std

import infrastructure.{ExecutionContext, IMessageSource}

trait AgentUtils {
  def exit()(implicit executionContext: ExecutionContext): Unit
}
