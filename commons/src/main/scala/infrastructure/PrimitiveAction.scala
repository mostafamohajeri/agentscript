package infrastructure

object PrimitiveAction extends IGoal {

  case class Parameters(v1: () => Unit) extends IParams

  def execute(params: Parameters)(implicit executionContext: ExecutionContext): Unit = {
    params.v1()
  }
}
