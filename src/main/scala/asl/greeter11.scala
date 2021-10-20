
package asl
import _root_.scala.collection.mutable.HashMap

import _root_.akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import _root_.akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
import java.util.logging.Logger
import _root_.scala.util.Failure
import _root_.scala.util.Success
import _root_.akka.util.Timeout
import _root_.scala.concurrent.duration._
import _root_.akka.actor.typed.scaladsl.AskPattern._
import _root_.scala.language.implicitConversions
import _root_.scala.concurrent.{Await, Future}
import _root_.scala.jdk.CollectionConverters._
import std.converters._

import scala.util.Random
import bb._
import infrastructure._
import bb.expstyla.exp._
import std.{AgentCommunicationLayer, DefaultCommunications}

class greeter11  (coms: AgentCommunicationLayer = new  DefaultCommunications,
                  beliefBaseFactory: IBeliefBaseFactory = new StylaBeliefBaseFactory)
  extends IntentionalAgentFactory {


  object Intention {

    def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>

      Behaviors.receive {
        (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context, src = message.source)

          message match {
            case SubGoalMessage(_,_,r) =>
              message.goal match {

                case greeter11.this.hello_1 =>
                  greeter11.this.hello_1.execute(message.params.asInstanceOf[Parameters])


                case _ =>
                  context.log.error("This actor can not handle goal of type {}", message.goal)
              }
              r match {
                case a : AkkaMessageSource =>
                  a.src ! IntentionDoneMessage(AkkaMessageSource(executionContext.agent.self))
                case DummyMessageSource(_) =>
                  context.log.error("Intention Done!")
              }

              Behaviors.same
            case InitEndMessage(r) =>
              Behaviors.stopped
          }
      }
    }
  }

  override def agentBuilder: Agent = new Agent()
  class Agent extends IAgent {

    override def agent_type: String = "greeter11"

    var vars = VarMap()

    def initGoals()(implicit executionContext: ExecutionContext) = List[StructTerm](
    )

    def initBeliefs()(implicit executionContext: ExecutionContext) = List[StructTerm](
    )

    def create_goal_message(t: StructTerm, ref: IMessageSource) (implicit executionContext: ExecutionContext): Option[SubGoalMessage] = {
      if(t.matchOnlyFunctorAndArity("hello",1)) {
        //          if(t.functor=="hello" && t.terms.size == 1 ) {
        val args: Parameters = Parameters(t.terms.toList)
        Option(SubGoalMessage(hello_1, args, ref))
      } else  {
        Option.empty[SubGoalMessage]
      }
    }

    def apply(name: String, yellowPages: IYellowPages, MAS: ActorRef[IMessage]): Behavior[IMessage] = {
      Behaviors.setup { context =>
        val yp: IYellowPages = yellowPages
        val bb: IBeliefBase[GenericTerm] = beliefBaseFactory()
        implicit val executionContext: ExecutionContext = ExecutionContext(name, agent_type, context, yp, bb, context.log)
        bb.assert(initBeliefs)

        val initiator = context.spawn(Intention(executionContext), "initiator")

        MAS ! ReadyMessage(context.self)
        Behaviors.receive {
          (context, message) =>
            message match {
              case StartMessage() =>

                implicit val timeout: Timeout = 99999.seconds
                implicit val ec = context.executionContext
                implicit val scheduler = context.system.scheduler


                //              initGoals.foreach( tuple => initiator ! SubGoalMessage(tuple._1,tuple._2,context.self))
                initGoals.foreach(struct => {


                  val result: Future[IMessage] = initiator.ask[IMessage](ref => {
                    val subGoal = create_goal_message(struct, AkkaMessageSource(ref))
                    if (subGoal.isDefined)
                      subGoal.get
                    else
                      throw new RuntimeException(s"No plan for initial goal $struct")
                  }
                  )


                  result.onComplete {
                    case Success(IntentionDoneMessage(r)) => IntentionDoneMessage(r)
                    case Failure(_) => IntentionErrorMessage(src = null)
                  }

                  Await.result(result, timeout.duration)

                  //                context.ask[ISubGoalMessage, IMessage](initiator, ref => SubGoalMessage(tuple._1, tuple._2,name, ref)) {
                  //                  case Success(IntentionDoneMessage(_, _)) => IntentionDoneMessage()
                  //                  case Failure(_) => IntentionErrorMessage()
                  //                }
                }
                )

                initiator ! InitEndMessage(context.self)
                normal_behavior()

              //            case InitEndMessage(_) =>
              //              context.log.debug(f"$name: I have started, switching behavior")
              //              normal_behavior()
            }

        }
      }
    }

    def normal_behavior()(implicit executionContext: ExecutionContext): Behavior[IMessage] = {
      Behaviors.setup { context =>

        val pool = Routers.pool(poolSize = 8)(
          Behaviors.supervise(Intention(executionContext)).onFailure[Exception](SupervisorStrategy.restart))
        val router = context.spawn(pool, "intention-pool")

        Behaviors.receive {
          (context, message) =>
            message match {
              case IntentionDoneMessage(r) =>
              //context.log.debug(f"${executionContext.name}: an intention was done by $s")
              case SubGoalMessage(_, _, _) =>
                router ! message.asInstanceOf[SubGoalMessage]
              case GoalMessage(m, ref) =>
                m match {
                  case t: StructTerm =>
                    val subGoal = create_goal_message(t, ref)

                    if (subGoal.isDefined)
                      context.self ! subGoal.get
                    else
                      ref.asInstanceOf[AkkaMessageSource].src ! IntentionErrorMessage(NoPlanMessage(),AkkaMessageSource(executionContext.agent.self))


                }
            }
            Behaviors.same
        }
      }
    }
  }

  object hello_1 extends IGoal {

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      var vars = VarMap()
      //plan 0 start

      vars.clear()
      vars +=(   "Name" -> params.l_params(0))

      val r0 = executionContext.beliefBase.query(StructTerm("==",Seq[GenericTerm](asString(vars("Name")).contains(StringTerm("Mr")),BooleanTerm(true))))

      if (r0.result) {
        r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
        plan0(vars)
        return
      }

      // plan 0 end
      //plan 1 start

      vars.clear()
      vars +=(   "Name" -> params.l_params(0))

      val r1 = executionContext.beliefBase.query(StructTerm(";",Seq[GenericTerm](StructTerm("==",Seq[GenericTerm](asString(vars("Name")).contains(StringTerm("Ms")),BooleanTerm(true))),asString(vars("Name")).contains(StringTerm("Mrs")))))

      if (r1.result) {
        r1.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
        plan1(vars)
        return
      }

      // plan 1 end
      //plan 2 start

      vars.clear()
      vars +=( "0" -> params.l_params(0))

      val m2 = executionContext.beliefBase.matchTerms(StructTerm("hello",Seq[GenericTerm](StringTerm("John"))),StructTerm("hello",params.l_params));
      if(m2.result)
      {
        m2.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

        plan2(vars)
        return
      }
      // plan 2 end


      executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

    }


    def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.achieve(executionContext.src,StructTerm("greetings",Seq[GenericTerm](StringTerm("Sir"))))))


    }
    def plan1(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.achieve(executionContext.src,StructTerm("greetings",Seq[GenericTerm](StringTerm("Madam"))))))


    }
    def plan2(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.achieve(executionContext.src,StructTerm("greetings",Seq[GenericTerm](StringTerm("John"))))))


    }


  }





}