
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import alice.tuprolog.{Term, Var}

import scala.collection.mutable

trait IAgent {
  def initGoals(): List[IMessage]

  def apply(name: String, yellowPages: ActorRef[IMessage],MAS: ActorRef[IMessage]): Behavior[IMessage]
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
  def belief(): Term
}

case class BeliefMessage(p_belief: Term,p_sender: ActorRef[IMessage]) extends IBeliefMessage {
  override def belief(): Term = p_belief
  override def sender(): ActorRef[IMessage] = p_sender
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

case class StartMessage() extends IMessage {
  override def sender(): ActorRef[IMessage] = null
}

case class ReadyMessage(s : ActorRef[IMessage]) extends IMessage {
  override def sender(): ActorRef[IMessage] = s
}

object YellowPages {
  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context => {
      var agentsPersistent: mutable.HashMap[String, ActorRef[IMessage]] = mutable.HashMap()
      Behaviors.receive { (context, message) =>
        message match {
          case ActorSubscribeMessage(name, ref) =>
            agentsPersistent.put(name, ref)
          case ActorMessage(d, m, s) =>
            m match {
              case BeliefMessage(b,_) =>
              println (f" $s wants to send belief $b to $d")
              if (agentsPersistent.contains (d) )
              agentsPersistent (d) ! m
            }
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
      var agentsNotInitialized : Int = 0
      var yellowPages: ActorRef[IMessage] = context.spawn(YellowPages.apply(), "yp");
      Behaviors.receive { (context, message) =>
        message match {
          case AgentRequest(types) =>
            types foreach {
              case (agentType, count) =>
                agentsNotInitialized = count
                for (a <- 1 to count) {
                  val name = "Agent_" + a
                  val ref = context.spawn(agentType.apply(name, yellowPages,context.self), name)
                  agentsNotStarted = agentsNotStarted :+ ref
                  yellowPages.tell(ActorSubscribeMessage(name, ref))
                }
            }
          case ReadyMessage(s) =>
            agentsNotInitialized -= 1
            println(f"ageNt $s says ready, we have $agentsNotInitialized agents to go")
            if(agentsNotInitialized == 0) {
              agentsNotStarted.foreach(a => a ! StartMessage())
              agentsNotStarted = Seq[ActorRef[IMessage]]()
            }

          case _=> throw new RuntimeException(f"can no handle message of type $message")
//          case StartMessage() =>
//            agentsNotStarted.foreach(a => a ! StartMessage())
//            agentsNotStarted = Seq[ActorRef[IMessage]]()
        }
        Behaviors.same
      }
    }
    }
  }
}


case class AgentRequest(agentTypes: Map[IAgent, Int]) extends IMessage {
  override def sender(): ActorRef[IMessage] = null
}


object PrimitiveAction extends IGoal {

  case class Parameters(v1: () => Unit) extends IParams

  def execute(params: Parameters) (implicit executionContext: ExecutionContext): Unit = {
    params.v1()
  }
}

object VarManager {
  def bindVar(var_name: String, variables: mutable.HashMap[String, Term]): Term = variables.getOrElseUpdate(var_name, Var.of(var_name))
}

