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

object client {

  object Intention {

    def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


      Behaviors.receive {
        (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

          message match {
            case SubGoalMessage(_,_,_,_) =>
              message.goal match {

                case client.apply_to_bank =>
                  client.apply_to_bank.execute(message.params.asInstanceOf[client.apply_to_bank.Parameters])

                case client.give_info =>
                  client.give_info.execute(message.params.asInstanceOf[client.give_info.Parameters])

                case client.give_consent =>
                  client.give_consent.execute(message.params.asInstanceOf[client.give_consent.Parameters])

                case client.be_informed_of_acceptance =>
                  client.be_informed_of_acceptance.execute(message.params.asInstanceOf[client.be_informed_of_acceptance.Parameters])

                case client.change_data =>
                  client.change_data.execute(message.params.asInstanceOf[client.change_data.Parameters])

                case client.need_to_update_data =>
                  client.need_to_update_data.execute(message.params.asInstanceOf[client.need_to_update_data.Parameters])


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

    override def agent_type: String = "client"

    def initGoals()(implicit executionContext: ExecutionContext) = List[(IGoal,IParams)](
      (apply_to_bank,apply_to_bank.Parameters(List( toTerm(kyc.data_gen.customer_data(myName).preferred_bank) )))
    )

    def initBeliefs()(implicit executionContext: ExecutionContext) = List[Struct](
      Struct.of("my_sib",toTerm(kyc.data_gen.customer_data(myName).sib))
      ,
      Struct.of("my_name",toTerm(kyc.data_gen.customer_data(myName).name))
      ,
      Struct.of("my_country",toTerm(kyc.data_gen.customer_data(myName).country))
      ,
      Struct.of("acceptable_purpose",Atom.of("KYC"))
      ,
      Struct.of("acceptable_purpose",Atom.of("research"))

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
                  if(t.getFunctor=="apply_to_bank" && t.getArity == 1 ) {
                    val args : apply_to_bank.Parameters = if(t.getArity== 0)  apply_to_bank.Parameters(List( )) else apply_to_bank.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(apply_to_bank, args,r,ref)
                  } else if(t.getFunctor=="give_info" && t.getArity == 1 ) {
                    val args : give_info.Parameters = if(t.getArity== 0)  give_info.Parameters(List( )) else give_info.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(give_info, args,r,ref)
                  } else if(t.getFunctor=="give_consent" && t.getArity == 2 ) {
                    val args : give_consent.Parameters = if(t.getArity== 0)  give_consent.Parameters(List( )) else give_consent.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(give_consent, args,r,ref)
                  } else if(t.getFunctor=="be_informed_of_acceptance" && t.getArity == 1 ) {
                    val args : be_informed_of_acceptance.Parameters = if(t.getArity== 0)  be_informed_of_acceptance.Parameters(List( )) else be_informed_of_acceptance.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(be_informed_of_acceptance, args,r,ref)
                  } else if(t.getFunctor=="change_data" && t.getArity == 2 ) {
                    val args : change_data.Parameters = if(t.getArity== 0)  change_data.Parameters(List( )) else change_data.Parameters(t.getArgs.toList)
                    context.self ! SubGoalMessage(change_data, args,r,ref)
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

  object apply_to_bank extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var Bank = params.l_params(0)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "Bank" -> params.l_params(0) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("apply_to_bank",VarManager.bindVar("Bank", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of("==",toTerm(myName),toTerm(executionContext.sender.name)))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println(  myName + " applying to "  + vars("Bank") )))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("applying_to",VarManager.bindVar("Bank", vars))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("Bank"),Struct.of("register",java.util.List.of[Term]()))))


    }


  }

  object give_info extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var B = params.l_params(0)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "B" -> params.l_params(0) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("give_info",VarManager.bindVar("B", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of(",",Struct.of("my_name",VarManager.bindVar("M", vars)),Struct.of("my_sib",VarManager.bindVar("S", vars))),Struct.of("my_country",VarManager.bindVar("C", vars))),Struct.of(";",Struct.of("applying_to",VarManager.bindVar("B", vars)),Struct.of("client_of",VarManager.bindVar("B", vars)))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(executionContext.sender.ref,Struct.of("info",VarManager.bindVar("M", vars),VarManager.bindVar("S", vars),VarManager.bindVar("C", vars)))))


    }


  }

  object give_consent extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var B = params.l_params(0)
      //var Purpose = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "B" -> params.l_params(0), "Purpose" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("give_consent",VarManager.bindVar("B", vars),VarManager.bindVar("Purpose", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of(";",Struct.of("applying_to",VarManager.bindVar("B", vars)),Struct.of("client_of",VarManager.bindVar("B", vars))),Struct.of("acceptable_purpose",VarManager.bindVar("Purpose", vars))))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(executionContext.sender.ref,Struct.of("consent",VarManager.bindVar("B", vars),VarManager.bindVar("Purpose", vars),Truth.of(true)))))


    }


  }

  object be_informed_of_acceptance extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var Bank = params.l_params(0)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "Bank" -> params.l_params(0) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("be_informed_of_acceptance",VarManager.bindVar("Bank", vars)) All vars no need to check */);

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

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("client_of",VarManager.bindVar("Bank", vars))))
      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("applying_to",VarManager.bindVar("Bank", vars))))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( myName() + ": Yaay! I am now a client of " + vars("Bank") )))


    }


  }

  object change_data extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      //var SIB = params.l_params(0)
      //var Country = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0

      vars.clear()
      vars +=( "SIB" -> params.l_params(0), "Country" -> params.l_params(1) )

      val m0 = executionContext.beliefBase.matchTerms(/* Struct.of("change_data",VarManager.bindVar("SIB", vars),VarManager.bindVar("Country", vars)) All vars no need to check */);

      if(m0.result)
      {
        m0.bindings foreach { case (k, v) => vars += (k -> v) }

        val r0 = executionContext.beliefBase.query(Struct.of("my_name",VarManager.bindVar("M", vars)))

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("my_sib",Atom.of("trader"))))

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", Struct.of("my_country",VarManager.bindVar("_", vars))))

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("my_sib",VarManager.bindVar("SIB", vars))))

      BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", Struct.of("my_country",VarManager.bindVar("Country", vars))))

      need_to_update_data.execute(need_to_update_data.Parameters(List(  )))


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

        val r0 = executionContext.beliefBase.query()

        if (r0.result) {
          r0.bindings foreach { case (k, v) => vars += (k -> v) }
          plan0(vars)
          return
        }

      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      val ex_L28916 = executionContext.beliefBase.rawQuery( Struct.of("client_of",VarManager.bindVar("L28916", vars)) )
      while (ex_L28916.hasNext) {
        val sol_L28916 = ex_L28916.next
        if(sol_L28916.isYes) {
          vars += ("B" -> sol_L28916.getSubstitution.get( VarManager.bindVar("L28916", vars)).asInstanceOf[Term])
          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("B"),Struct.of("need_to_update_data",java.util.List.of[Term]()))))

        }
      }
      vars -= ("B")


    }


  }



  def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

}