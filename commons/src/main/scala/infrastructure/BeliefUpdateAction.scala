package infrastructure

import bb.expstyla.exp.GenericTerm
import bb.expstyla.exp.StructTerm

object BeliefUpdateAction extends IGoal {

  case class Parameters(v1: String, v2: (GenericTerm)) extends IParams

  def execute(params: Parameters,goalParser: IAgentGoalParser)(implicit executionContext: ExecutionContext): Unit = {
    val op = params.v1
    op match {
      case "+"  =>
        if(executionContext.beliefBase.assertOne(params.v2.asInstanceOf[StructTerm])) {
          val subGoal = goalParser.create_belief_message(
            params.v2.asInstanceOf[StructTerm],
            AkkaMessageSource(executionContext.agent.self)
          )
          if(subGoal.isDefined)
            executionContext.agent.self ! subGoal.get
        }
      case "-"  => if(executionContext.beliefBase.retractOne(params.v2.asInstanceOf[StructTerm])) {
        val subGoal = goalParser.create_unbelief_message(
          params.v2.asInstanceOf[StructTerm],
          AkkaMessageSource(executionContext.agent.self)
        )
        if(subGoal.isDefined)
          executionContext.agent.self ! subGoal.get
      }
      case "-+" => throw new RuntimeException("not implemented")
    }
  }
}
