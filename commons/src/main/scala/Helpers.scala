
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import alice.tuprolog.{Term, Var}

import scala.collection.mutable

trait IAgent {
  def initGoals(): List[IMessage]

  def apply(name: String, yellowPages: ActorRef[IMessage]): Behavior[IMessage]
}

trait IMessage {
  def sender(): ActorRef[IMessage]
}

class Message(s: ActorRef[IMessage]) extends IMessage {
  override def sender(): ActorRef[IMessage] = s
}




case class ActorSubscribeMessage(name: String, s: ActorRef[IMessage]) extends Message(s)

case class ActorMessage(actorName: String, m: IMessage, s: ActorRef[IMessage]) extends Message(s) {
  def message(): IMessage = m
}

trait IGoalMessage extends IMessage {
  def goal(): IGoal
  def params(): IParams
}


trait IBeliefMessage extends IMessage {
  def belief(): AnyVal

  def params(): IParams
}

trait IGoal {

}

trait IParams {

}


trait IPlan {

}

class CPlan[G, P](goal: G, params: P) extends IPlan

case class GoalMessage(p_goal: IGoal, p_params: IParams, s: ActorRef[IMessage]) extends Message(s) with IGoalMessage {
  override def goal(): IGoal = p_goal

  override def params(): IParams = p_params
}

case class BeliefMessage(p_belief: AnyVal, p_params: IParams, s: ActorRef[IMessage]) extends Message(s) with IBeliefMessage {
  override def belief(): AnyVal = p_belief

  override def params(): IParams = p_params
}

case class StartMessage() extends IMessage {
  override def sender(): ActorRef[IMessage] = null
}

object YellowPages {
  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context => {
      var agentsPersistent: mutable.HashMap[String, ActorRef[IMessage]] = mutable.HashMap()
      Behaviors.receive { (context, message) =>
        message match {
          case ActorSubscribeMessage(name, ref) =>
            agentsPersistent.put(name, ref)
          case ActorMessage(s, m, _) =>
            agentsPersistent(s) ! m
        }
        Behaviors.same
      }
    }
    }
  }
}

case class ExecutionContext(agent: ActorContext[IMessage],intention: ActorContext[IGoalMessage],yellowPages: ActorRef[IMessage],beliefBase: BeliefBase)



object MAS {

  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context => {
      var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
      var yellowPages: ActorRef[IMessage] = context.spawn(YellowPages.apply(), "yp");
      Behaviors.receive { (context, message) =>
        message match {
          case AgentRequest(_, _) =>
            for (a <- 1 to message.asInstanceOf[AgentRequest].count) {
              val name = "Agent_" + a
              val ref = context.spawn(message.asInstanceOf[AgentRequest].agent.apply(name, yellowPages), name)
              agentsNotStarted = agentsNotStarted :+ ref
              yellowPages.tell(ActorSubscribeMessage(name, ref))
              // message.agent.initGoals().foreach(ref.tell)
            }
          case StartMessage() =>
            agentsNotStarted.foreach(a => a ! StartMessage())
            agentsNotStarted = Seq[ActorRef[IMessage]]()
        }
        Behaviors.same
      }
    }
    }
  }
}


case class AgentRequest(agent: IAgent, count: Int) extends IMessage {
  override def sender(): ActorRef[IMessage] = null
}


object PrimitiveAction extends IGoal {

  case class Parameters(v1: () => Unit) extends IParams

  def execute(executionContext: ExecutionContext, params: Parameters): Unit = {
    params.v1()
  }
}

object VarManager {
  def bindVar(var_name: String, variables: mutable.HashMap[String, Term]): Term = variables.getOrElseUpdate(var_name, Var.of(var_name))
}

