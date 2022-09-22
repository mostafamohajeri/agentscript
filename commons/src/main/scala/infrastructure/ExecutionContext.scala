package infrastructure



import akka.actor.typed.{ActorRef, ActorRefResolver}
import akka.actor.typed.scaladsl.ActorContext
import bb.expstyla.exp.GenericTerm
import bb.{BeliefBaseStyla, IBeliefBase, IGenericTerm}
import org.slf4j.Logger

case class  ExecutionContext(
    name: String,
    agentType: String,
    agent: ActorContext[IMessage],
    yellowPages: IYellowPages,
    beliefBase: IBeliefBase[GenericTerm],
    logger: Logger,
    goalParser: IAgentGoalParser,
    parent: IMessageSource,
    intention: ActorContext[ISubGoalMessage],
    src: IMessageSource
)

object ExecutionContext {
  def apply(
      name: String,
      agentType: String,
      agent: ActorContext[IMessage],
      yellowPages: IYellowPages,
      beliefBase: IBeliefBase[GenericTerm],
      logger: Logger,
      goalParser: IAgentGoalParser,
      parent: IMessageSource
           ): ExecutionContext = {
    ExecutionContext(
      name,
      agentType,
      agent,
      yellowPages,
      beliefBase,
      logger,
      goalParser,
      parent,
      null,
      null
    )
  }

  def apply(
      name: String,
      agentType: String,
      agent: ActorContext[IMessage]
  ): ExecutionContext = {
    ExecutionContext(
      name,
      agentType,
      agent,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    )
  }

}
