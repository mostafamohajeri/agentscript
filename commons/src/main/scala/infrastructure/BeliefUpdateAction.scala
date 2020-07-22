package infrastructure

import it.unibo.tuprolog.core.{Struct, Term}


object BeliefUpdateAction extends IGoal {

  case class Parameters(v1: String, v2: Struct) extends IParams

  def execute(params: Parameters) (implicit executionContext: ExecutionContext): Boolean = {
    val op = params.v1
    op match {
      case "+" => executionContext.beliefBase.assert(params.v2)
      case "-" => executionContext.beliefBase.retract(params.v2)
      case "-+" => throw new RuntimeException
    }
  }
}
