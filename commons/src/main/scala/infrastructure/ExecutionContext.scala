package infrastructure

import akka.actor.typed.{ActorRef, ActorRefResolver}
import akka.actor.typed.scaladsl.ActorContext
import bb.BeliefBaseKT


case class ExecutionContext(
                             name: String,
                             agentType: String,
                             agent: ActorContext[IMessage],
                             yellowPages: ActorRef[IMessage],
                             beliefBase: BeliefBaseKT,
                             agentLogger: AgentLogger,
                             intention: ActorContext[ISubGoalMessage],
                             sender : Sender
                           )



object ExecutionContext {
  def apply (
              name: String,
              agentType: String,
              agent: ActorContext[IMessage],
              yellowPages: ActorRef[IMessage],
              beliefBase: BeliefBaseKT,
              agentLogger: AgentLogger
            ) : ExecutionContext = {
    ExecutionContext(
      name,agentType,agent,yellowPages,beliefBase,agentLogger,null,null
    )
  }
  def apply (
              name: String,
              agentType: String,
              agent: ActorContext[IMessage]
            ) : ExecutionContext = {
    ExecutionContext(
      name,agentType,agent,null,null,null,null,null
    )
  }

}

