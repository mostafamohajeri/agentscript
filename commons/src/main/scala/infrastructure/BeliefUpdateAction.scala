package infrastructure

import bb.IGenericTerm
import bb.expstyla.exp.StructTerm



object BeliefUpdateAction extends IGoal {

  case class Parameters(v1: String, v2: (IGenericTerm)) extends IParams

  def execute(params: Parameters) (implicit executionContext: ExecutionContext): Boolean = {
    val op = params.v1
    op match {
      case "+" => executionContext.beliefBase.assert(params.v2.asInstanceOf[StructTerm])
      case "-" => executionContext.beliefBase.retract(params.v2.asInstanceOf[StructTerm])
      case "-+" => throw new RuntimeException("not implemented")
    }
  }
}
