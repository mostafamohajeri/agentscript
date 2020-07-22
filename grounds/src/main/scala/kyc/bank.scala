import scala.collection.mutable

import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
import it.unibo.tuprolog.core.{Atom, Numeric, Struct, Term, Truth}

import scala.jdk.CollectionConverters._
import std.coms._
import scala.util.Random
import bb._
import infrastructure._
import infrastructure.termutils.ExTermFactoryKT._
import infrastructure.termutils.ExTermVisitorKT._

object bank {

  object Intention {

    def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


      Behaviors.receive {
        (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

          message match {
            case SubGoalMessage(_,_,_,_) =>
              message.goal match {

                case bank.register =>
                  bank.register.execute(message.params.asInstanceOf[bank.register.Parameters])

                case bank.interview_complete =>
                  bank.interview_complete.execute(message.params.asInstanceOf[bank.interview_complete.Parameters])

                case bank.register_client =>
                  bank.register_client.execute(message.params.asInstanceOf[bank.register_client.Parameters])

                case bank.update_client =>
                  bank.update_client.execute(message.params.asInstanceOf[bank.update_client.Parameters])

                case bank.assign_risk =>
                  bank.assign_risk.execute(message.params.asInstanceOf[bank.assign_risk.Parameters])

                case bank.need_to_update_data =>
                  bank.need_to_update_data.execute(message.params.asInstanceOf[bank.need_to_update_data.Parameters])


                case _ =>
                  context.log.error("This actor can not handle goal of type {}", message.goal)
              }
              Behaviors.same
            case InitEndMessage(s,r) => r ! message
              Behaviors.stopped
          }
      }
    }
  }

  object Agent extends IAgent {

    override def agent_type: String = "bank"

    def initGoals()(implicit executionContext: ExecutionContext) = List[(IGoal,IParams)](
    )

    def initBeliefs()(implicit executionContext: ExecutionContext) = List[Struct](
      Struct.of("employee",toTerm(kyc.data_gen.bank_data(myName).employee(0)))

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
              initGoals.foreach( tuple => initiator ! SubGoalMessage(tuple._1,tuple._2,name,context.self))
              initiator ! InitEndMessage(name,context.self)
              Behaviors.same
            case InitEndMessage(_,_) =>
//              context.log.debug(f"$name: I have started, switching behavior")
              normal_behavior()
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
            case SubGoalMessage(_, _, _,_) =>
              router ! message.asInstanceOf[SubGoalMessage]
            case GoalMessage(m,r,ref) =>
              m match {
                case t: Struct =>
                  if(t.getFunctor=="register" && t.getArity == 0 ) {
                    val args : register.Parameters = if(t.getArity== 0)  register.Parameters(List( )) else register.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(register, args,r,ref)
                  } else if(t.getFunctor=="interview_complete" && t.getArity == 3 ) {
                    val args : interview_complete.Parameters = if(t.getArity== 0)  interview_complete.Parameters(List( )) else interview_complete.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(interview_complete, args,r,ref)
                  } else if(t.getFunctor=="register_client" && t.getArity == 2 ) {
                    val args : register_client.Parameters = if(t.getArity== 0)  register_client.Parameters(List( )) else register_client.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(register_client, args,r,ref)
                  } else if(t.getFunctor=="update_client" && t.getArity == 2 ) {
                    val args : update_client.Parameters = if(t.getArity== 0)  update_client.Parameters(List( )) else update_client.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(update_client, args,r,ref)
                  } else if(t.getFunctor=="assign_risk" && t.getArity == 2 ) {
                    val args : assign_risk.Parameters = if(t.getArity== 0)  assign_risk.Parameters(List( )) else assign_risk.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(assign_risk, args,r,ref)
                  } else if(t.getFunctor=="need_to_update_data" && t.getArity == 0 ) {
                    val args : need_to_update_data.Parameters = if(t.getArity== 0)  need_to_update_data.Parameters(List( )) else need_to_update_data.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(need_to_update_data, args,r,ref)
                  } else  if(true) { println("no plan for this goal") }
              }
          }
            Behaviors.same
        }
      }
    }
  }

  object register extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("register",java.util.List.of[Term]()) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of("employee",VarManager.bindVar("E", vars)))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("E"),Struct.of("interview",toTerm(executionContext.sender.name)))))


    }


  }

  object interview_complete extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var Client = params.l_params(0)
      //var I = params.l_params(1)
      //var  = params.l_params(2)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "Client" -> params.l_params(0), "I" -> params.l_params(1), "2" -> params.l_params(2) )

      val m0 = executionContext.beliefBase.matchTerms(Struct.of("interview_complete",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars),Truth.of(true)),Struct.of("interview_complete",params.l_params.asJava));

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of("=",VarManager.bindVar("E", vars),toTerm(executionContext.sender.name)),Struct.of("employee",VarManager.bindVar("E", vars))),Struct.of("not",Struct.of("client",VarManager.bindVar("Client", vars)))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      } //plan 1

      vars.clear()
      vars +=( "Client" -> params.l_params(0), "I" -> params.l_params(1), "2" -> params.l_params(2) )

      val m1 = executionContext.beliefBase.matchTerms(Struct.of("interview_complete",VarManager.bindVar("Client", vars),VarManager.bindVar("I", vars),Truth.of(true)),Struct.of("interview_complete",params.l_params.asJava));

      if(m1.result)
      {
        m1.bindings foreach { case (k, v) => vars += (k -> v) }

        val r1 = executionContext.beliefBase.query(Struct.of(",",Struct.of("=",VarManager.bindVar("E", vars),toTerm(executionContext.sender.name)),Struct.of("employee",VarManager.bindVar("E", vars))))

        if (r1.result) {
          r1.bindings foreach { case (k, v) => vars += (k -> v) }
          plan1(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( "interview complete for " + vars("Client") )))
      register_client.execute(register_client.Parameters(List( VarManager.bindVar("Client", vars) , VarManager.bindVar("I", vars)  )))


    }
    def plan1(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( "interview complete for " + vars("Client") )))
      update_client.execute(update_client.Parameters(List( VarManager.bindVar("Client", vars) , VarManager.bindVar("I", vars)  )))


    }


  }

  object register_client extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var C = params.l_params(0)
      //var I = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "C" -> params.l_params(0), "I" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("register_client",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of("employee",VarManager.bindVar("E", vars)))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("information",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars))))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("client",VarManager.bindVar("Client", vars))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("E"),Struct.of("do_risk_analysis",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars)))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("C"),Struct.of("be_informed_of_acceptance",toTerm(executionContext.name)))))


    }


  }

  object update_client extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var C = params.l_params(0)
      //var I = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "C" -> params.l_params(0), "I" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("update_client",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of("employee",VarManager.bindVar("E", vars)))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("information",VarManager.bindVar("C", vars),VarManager.bindVar("_", vars))))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("information",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("E"),Struct.of("do_risk_analysis",VarManager.bindVar("C", vars),VarManager.bindVar("I", vars)))))


    }


  }

  object assign_risk extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var C = params.l_params(0)
      //var R = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "C" -> params.l_params(0), "R" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("assign_risk",VarManager.bindVar("C", vars),VarManager.bindVar("R", vars)) All vars no need to check */);

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


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("risk",VarManager.bindVar("C", vars),VarManager.bindVar("_", vars))))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("risks",VarManager.bindVar("C", vars),VarManager.bindVar("R", vars))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println(   "risk " + vars("R")  + " was assigned to "  + vars("C") )))


    }


  }

  object need_to_update_data extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("need_to_update_data",java.util.List.of[Term]()) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of("client",toTerm(executionContext.sender.name)),Struct.of("employee",VarManager.bindVar("E", vars))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("E"),Struct.of("interview",toTerm(executionContext.sender.name)))))


    }


  }



  def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

}