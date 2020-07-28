import _root_.scala.collection.mutable.HashMap

import _root_.akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import _root_.akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
import _root_.it.unibo.tuprolog.core.{Atom, Numeric, Struct, Term, Truth}

import _root_.scala.util.Failure
import _root_.scala.util.Success
import _root_.akka.util.Timeout
import _root_.scala.concurrent.duration._
import _root_.akka.actor.typed.scaladsl.AskPattern._

import _root_.scala.concurrent.{Await, Future}
import _root_.scala.jdk.CollectionConverters._
import std.coms._
import scala.util.Random
import bb._
import infrastructure._
import infrastructure.termutils.ExTermFactoryKT._
import infrastructure.termutils.ExTermVisitorKT._

object employee {

  object Intention {

    def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


      Behaviors.receive {
        (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

          message match {
            case SubGoalMessage(_,_,_,r) =>
              message.goal match {

                case employee.interview =>
                  employee.interview.execute(message.params.asInstanceOf[employee.interview.Parameters])

                case employee.info =>
                  employee.info.execute(message.params.asInstanceOf[employee.info.Parameters])

                case employee.consent =>
                  employee.consent.execute(message.params.asInstanceOf[employee.consent.Parameters])

                case employee.do_risk_analysis =>
                  employee.do_risk_analysis.execute(message.params.asInstanceOf[employee.do_risk_analysis.Parameters])


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

    override def agent_type: String = "employee"

    def initGoals()(implicit executionContext: ExecutionContext) = List[(IGoal,IParams)](
    )

    def initBeliefs()(implicit executionContext: ExecutionContext) = List[Struct](
      Struct.of("bank",toTerm(kyc.data_gen.employee_data(myName).bank))

    )

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
              initGoals.foreach(tuple => {

                val result: Future[IMessage] =  initiator.ask[IMessage](ref => SubGoalMessage(tuple._1, tuple._2, name, ref))

                result.onComplete {
                  case Success(IntentionDoneMessage(n, r)) => context.log.debug(f"$name: inital intention done")
                  case Failure(_) => context.log.error(f"$name: inital error")
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
              context.log.debug(f"${executionContext.name}: an intention was done by $s")
            case SubGoalMessage(_, _, _,_) =>
              router ! message.asInstanceOf[SubGoalMessage]
            case GoalMessage(m,r,ref) =>
              m match {
                case t: Struct =>
                  if(t.getFunctor=="interview" && t.getArity == 1 ) {
                    val args : interview.Parameters = if(t.getArity== 0)  interview.Parameters(List( )) else interview.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(interview, args,r,ref)
                  } else if(t.getFunctor=="info" && t.getArity == 3 ) {
                    val args : info.Parameters = if(t.getArity== 0)  info.Parameters(List( )) else info.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(info, args,r,ref)
                  } else if(t.getFunctor=="consent" && t.getArity == 3 ) {
                    val args : consent.Parameters = if(t.getArity== 0)  consent.Parameters(List( )) else consent.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(consent, args,r,ref)
                  } else if(t.getFunctor=="do_risk_analysis" && t.getArity == 2 ) {
                    val args : do_risk_analysis.Parameters = if(t.getArity== 0)  do_risk_analysis.Parameters(List( )) else do_risk_analysis.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(do_risk_analysis, args,r,ref)
                  } else  if(true) { println("no plan for this goal") }
              }
          }
            Behaviors.same
        }
      }
    }
  }

  object interview extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var Client = params.l_params(0)
      var vars = new HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "Client" -> params.l_params(0) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("interview",VarManager.bindVar("Client", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of("bank",VarManager.bindVar("B", vars)),Struct.of("==",VarManager.bindVar("B", vars),toTerm(executionContext.sender.name))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("Client"),Struct.of("give_info",VarManager.bindVar("B", vars)))))


    }


  }

  object info extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var Name = params.l_params(0)
      //var SBI = params.l_params(1)
      //var Country = params.l_params(2)
      var vars = new HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "Name" -> params.l_params(0), "SBI" -> params.l_params(1), "Country" -> params.l_params(2) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("info",VarManager.bindVar("Name", vars),VarManager.bindVar("SBI", vars),VarManager.bindVar("Country", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of("bank",VarManager.bindVar("B", vars)),Struct.of("=",VarManager.bindVar("Client", vars),toTerm(executionContext.sender.name))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("information",VarManager.bindVar("Client", vars),Struct.of("info",VarManager.bindVar("Name", vars),VarManager.bindVar("SBI", vars),VarManager.bindVar("Country", vars)))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("Client"),Struct.of("give_consent",VarManager.bindVar("B", vars),Atom.of("KYC")))))


    }


  }

  object consent extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var B = params.l_params(0)
      //var Purpose = params.l_params(1)
      //var  = params.l_params(2)
      var vars = new HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "B" -> params.l_params(0), "Purpose" -> params.l_params(1), "2" -> params.l_params(2) )

      val m0 = executionContext.beliefBase.matchTerms(Struct.of("consent",VarManager.bindVar("B", vars),VarManager.bindVar("Purpose", vars),Truth.of(true)),Struct.of("consent",params.l_params.asJava));

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of(",",Struct.of("=",VarManager.bindVar("Client", vars),toTerm(executionContext.sender.name)),Struct.of("bank",VarManager.bindVar("B", vars))),Struct.of("==",VarManager.bindVar("Purpose", vars),Atom.of("KYC"))),Struct.of("information",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      } //plan 1

      vars.clear()
      vars +=( "Bank" -> params.l_params(0), "Purpose" -> params.l_params(1), "2" -> params.l_params(2) )

      val m1 = executionContext.beliefBase.matchTerms(Struct.of("consent",VarManager.bindVar("Bank", vars),VarManager.bindVar("Purpose", vars),Truth.of(false)),Struct.of("consent",params.l_params.asJava));

      if(m1.result)
      {
        m1.bindings foreach { case (k, v) => vars += (k -> v) }

        val r1 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of("=",VarManager.bindVar("Client", vars),toTerm(executionContext.sender.name)),Struct.of("bank",VarManager.bindVar("B", vars))),Struct.of("information",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars))))

        if (r1.result) {
          r1.bindings foreach { case (k, v) => vars += (k -> v) }
          plan1(vars)
          return
        }

      }

    }


    def plan0(vars: HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("B"),Struct.of("interview_complete",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars),Truth.of(true)))))


    }
    def plan1(vars: HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("B"),Struct.of("interview_complete",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars),Truth.of(true)))))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("information",VarManager.bindVar("Client", vars),VarManager.bindVar("_", vars))))


    }


  }

  object do_risk_analysis extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var C = params.l_params(0)
      //var  = params.l_params(1)
      var vars = new HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "C" -> params.l_params(0), "1" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(Struct.of("do_risk_analysis",VarManager.bindVar("C", vars),Struct.of("info",VarManager.bindVar("Name", vars),VarManager.bindVar("SBI", vars),VarManager.bindVar("Country", vars))),Struct.of("do_risk_analysis",params.l_params.asJava));

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


    def plan0(vars: HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      vars += ("R" -> toTerm(kyc.algorithms.risk(executionContext.sender.name,vars("SBI"),vars("Country"))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(executionContext.sender.ref,Struct.of("assign_risk",VarManager.bindVar("C", vars),VarManager.bindVar("R", vars)))))


    }


  }



  def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

}