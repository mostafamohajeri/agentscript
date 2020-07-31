package listener_talker
import _root_.scala.collection.mutable.HashMap

import _root_.akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import _root_.akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
import _root_.it.unibo.tuprolog.core.{Atom, Numeric, Struct, Term, Truth}

import _root_.scala.util.Failure
import _root_.scala.util.Success
import _root_.akka.util.Timeout
import _root_.scala.concurrent.duration._
import _root_.akka.actor.typed.scaladsl.AskPattern._
import _root_.scala.language.implicitConversions
import _root_.scala.concurrent.{Await, Future}
import _root_.scala.jdk.CollectionConverters._
import std.coms._
import std.converters._

import scala.util.Random
import bb._
import infrastructure._
import infrastructure.termutils.ExTermFactoryKT._
import infrastructure.termutils.ExTermVisitorKT._
import bb.exp._

object talker {

  object Intention {

    def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


      Behaviors.receive {
        (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

          message match {
            case SubGoalMessage(_,_,_,r) =>
              message.goal match {

                case talker.talk =>
                  talker.talk.execute(message.params.asInstanceOf[talker.talk.Parameters])


                case _ =>
                  context.log.error("This actor can not handle goal of type {}", message.goal)
              }
              r ! IntentionDoneMessage(Option(executionContext.name),Option(executionContext.agent.self))
              Behaviors.same
            case InitEndMessage(s,r) =>
              //r ! message
              Behaviors.stopped
          }
      }
    }
  }

  object Agent extends IAgent {

    override def agent_type: String = "talker"

    def initGoals()(implicit executionContext: ExecutionContext) = List[StructTerm](
      StructTerm("talk",Seq[GenericTerm](IntTerm(10000)))

    )

    def initBeliefs()(implicit executionContext: ExecutionContext) = List[StructTerm](
    )

    def create_goal_message(t: StructTerm,r:String,ref:ActorRef[IMessage]) : Option[SubGoalMessage] = {
      if(t.functor=="talk" && t.terms.size == 1 ) {
        val args : talk.Parameters = if(t.terms.size == 0)  talk.Parameters(List( )) else talk.Parameters(t.terms.toList)
        Option(SubGoalMessage(talk, args,r,ref))
      } else  {
        println("no plan for this goal")
        Option.empty[SubGoalMessage]
      }
    }

    def apply(name: String,yellowPages: ActorRef[IMessage],MAS: ActorRef[IMessage]): Behavior[IMessage] = {
      Behaviors.setup { context =>
        val yp : ActorRef[IMessage] = yellowPages
        val bb : BeliefBaseKT = BeliefBaseFactory()
        val logger = AgentLogger()
        implicit val executionContext : ExecutionContext = ExecutionContext(name,agent_type,context,yp,bb,logger)
        bb.assert(initBeliefs)

        val initiator = context.spawn(Intention(executionContext),"initiator")

        MAS ! ReadyMessage(context.self)
        Behaviors.receive {
          (context, message) => message match {
            case StartMessage() =>
              logger.start()

              implicit val timeout: Timeout = 20.seconds
              implicit val ec = context.executionContext
              implicit val scheduler = context.system.scheduler


              //              initGoals.foreach( tuple => initiator ! SubGoalMessage(tuple._1,tuple._2,name,context.self))
              initGoals.foreach(struct => {


                val result: Future[IMessage] =  initiator.ask[IMessage](ref => {
                  val subGoal = create_goal_message(struct,name,ref)
                  if(subGoal.isDefined)
                    subGoal.get
                  else
                    throw new RuntimeException(s"No plan for initial goal $struct")
                }
                )


                result.onComplete {
                  case Success(IntentionDoneMessage(n, r)) => IntentionDoneMessage(n,r)
                  case Failure(_) => IntentionFailedMessage()
                }

                Await.result(result,timeout.duration)

                //                context.ask[ISubGoalMessage, IMessage](initiator, ref => SubGoalMessage(tuple._1, tuple._2, name, ref)) {
                //                  case Success(IntentionDoneMessage(_, _)) => IntentionDoneMessage()
                //                  case Failure(_) => IntentionErrorMessage()
                //                }
              }
              )

              initiator ! InitEndMessage(name,context.self)
              context.log.debug(f"$name: I have started, switching behavior")
              normal_behavior()

            //            case InitEndMessage(_,_) =>
            //              context.log.debug(f"$name: I have started, switching behavior")
            //              normal_behavior()
          }

        }
      }
    }

    def normal_behavior() (implicit executionContext: ExecutionContext): Behavior[IMessage] = {
      Behaviors.setup { context =>

        val pool = Routers.pool(poolSize = 8)(
          Behaviors.supervise(Intention(executionContext)).onFailure[Exception](SupervisorStrategy.restart))

        val router = context.spawn(pool, "intention-pool")

        Behaviors.receive {
          (context, message) => message match {
            case IntentionDoneMessage(s,r) =>
            //context.log.debug(f"${executionContext.name}: an intention was done by $s")
            case SubGoalMessage(_, _, _,_) =>
              router ! message.asInstanceOf[SubGoalMessage]
            case GoalMessage(m,r,ref) =>
              m match {
                case t: StructTerm =>
                  val subGoal = create_goal_message(t,r,ref)

                  if(subGoal.isDefined)
                    context.self ! subGoal.get


              }
          }
            Behaviors.same
        }
      }
    }
  }

  object talk extends IGoal {
    case class Parameters(l_params: List[GenericTerm]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var X = params.l_params(0)
      var vars = new HashMap[String,GenericTerm]
      //plan 0

      vars.clear()
      vars +=( "X" -> params.l_params(0) )

      val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("talk",Seq[GenericTerm](VarManager.bindVar("X", vars))) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query()

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: HashMap[String,GenericTerm])(implicit executionContext: ExecutionContext): Unit = {

      val ex_L56333 = executionContext.beliefBase.bufferedQuery( StructTerm("between",Seq[GenericTerm](IntTerm(0),VarManager.bindVar("X", vars),VarManager.bindVar("L56333", vars))) )
      while (ex_L56333.hasNext) {
        val sol_L56333 = ex_L56333.next
        if(sol_L56333.result) {
          vars += ("B" -> sol_L56333.bindings("L56333"))
          PrimitiveAction.execute(PrimitiveAction.Parameters(() => broadcast_achieve(StructTerm("listen",Seq[GenericTerm](VarManager.bindVar("B", vars))))))

        }
      }
      vars -= ("B")
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println(StringTerm("done"))))


    }


  }



  def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

}