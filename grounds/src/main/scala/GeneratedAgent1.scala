
import scala.collection.mutable
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import alice.tuprolog.{Term,Struct,Number}

import scala.util.Random
object GeneratedAgent1 {


  object Intention {

    def apply(executionContext: ExecutionContext): Behavior[IGoalMessage] = Behaviors.setup { context =>

      implicit val l_executionContext = executionContext.copy(intention = context)
      Behaviors.receive {
        (context, message) => message.goal() match {

          case GeneratedAgent1.hi =>
            GeneratedAgent1.hi.execute(message.params().asInstanceOf[GeneratedAgent1.hi.Parameters])

          case GeneratedAgent1.say_distance =>
            GeneratedAgent1.say_distance.execute(message.params().asInstanceOf[GeneratedAgent1.say_distance.Parameters])


          case _ =>
            context.log.error("This actor can not handle goal of type {}", message.goal())
        }
          Behaviors.same
      }
    }
  }

  object Agent extends IAgent {

    def initGoals() = List[IGoalMessage](
      GoalMessage(hi,hi.Parameters(List( Term.createTerm("'alice'") )),null),
      GoalMessage(hi,hi.Parameters(List( Term.createTerm("'bob'") )),null),
      GoalMessage(hi,hi.Parameters(List( Term.createTerm("'charlie'") )),null)
    )

    def initBeliefs() = List[Term](
      Struct.of("friend",Term.createTerm("'charlie'"))
      ,
      Struct.of("friend",Term.createTerm("'alice'"))
      ,
      Struct.of("greet",Term.createTerm("'friendly'"),Term.createTerm("'hi'"))
      ,
      Struct.of("greet",Term.createTerm("'formal'"),Term.createTerm("'hello'"))
      ,
      Struct.of("greet",Term.createTerm("'far_friendly'"),Term.createTerm("'HIIIIII'"))
      ,
      Struct.of("distance",Struct.of("alice",Array[Term]()),Number.of(3.0))
      ,
      Struct.of("distance",Term.createTerm("'bob'"),Number.of(10.0))
      ,
      Struct.of("distance",Term.createTerm("'charlie'"),Number.of(8.0))

    )

    def apply(name: String,yellowPages: ActorRef[IMessage],MAS: ActorRef[IMessage]): Behavior[IMessage] = {
      Behaviors.setup { context =>
        val yp : ActorRef[IMessage] = yellowPages
        val bb : BeliefBase = BeliefBaseFactory()
        bb.assert(initBeliefs())
        MAS ! ReadyMessage(context.self)
        Behaviors.receive {
          (context, message) => message match {

            case GoalMessage(_, _, _) =>
              context.spawn(Intention(ExecutionContext(context,null,yp,bb)), "GOAL_" + Random.nextInt(Int.MaxValue)) ! message.asInstanceOf[GoalMessage]
            case StartMessage() =>
              initGoals().foreach(context.self.tell)
          }
            Behaviors.same
        }
      }
    }
  }

  object hi extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      var Y = params.l_params(0)
      var vars = new mutable.HashMap[String,Term]
      //plan 0
      vars.clear()
      vars +=( "Y" -> params.l_params(0) )
      val r0 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of(",",Struct.of("greet",VarManager.bindVar("Z", vars),VarManager.bindVar("X", vars)),Struct.of("distance",VarManager.bindVar("Y", vars),VarManager.bindVar("D", vars))),Struct.of(";",Struct.of(",",Struct.of("=",VarManager.bindVar("Z", vars),Term.createTerm("'friendly'")),Struct.of("<",VarManager.bindVar("D", vars),Number.of(5.0))),Struct.of(",",Struct.of("=",VarManager.bindVar("Z", vars),Term.createTerm("'far_friendly'")),Struct.of(">",VarManager.bindVar("D", vars),Number.of(5.0))))),Struct.of("friend",VarManager.bindVar("Y", vars))))

      if (r0.result) {
        r0.bindings foreach { case (k, v) => vars += (k -> v) }
        plan0(vars)
        return
      }
      //plan 1
      vars.clear()
      vars +=( "Y" -> params.l_params(0) )
      val r1 = executionContext.beliefBase.query(Struct.of(",",Struct.of(",",Struct.of(",",Struct.of("greet",VarManager.bindVar("Z", vars),VarManager.bindVar("X", vars)),Struct.of("\\=",VarManager.bindVar("Z", vars),Term.createTerm("'friendly'"))),Struct.of("not",Struct.of("friend",VarManager.bindVar("Y", vars)))),Struct.of("distance",VarManager.bindVar("Y", vars),VarManager.bindVar("D", vars))))

      if (r1.result) {
        r1.bindings foreach { case (k, v) => vars += (k -> v) }
        plan1(vars)
        return
      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      say_distance.execute(say_distance.Parameters(List( VarManager.bindVar("Y", vars) , VarManager.bindVar("D", vars)  )))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( VarManager.bindVar("X", vars) , VarManager.bindVar("Y", vars)  )))

    }
    def plan1(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      say_distance.execute(say_distance.Parameters(List( VarManager.bindVar("Y", vars) , VarManager.bindVar("D", vars)  )))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => inform( Term.createTerm("'agent2'") , Term.createTerm("'hello'")  )))
      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( VarManager.bindVar("X", vars) , VarManager.bindVar("Y", vars)  )))

    }


  }

  object say_distance extends IGoal {
    case class Parameters(l_params: List[Term]) extends IParams {}

    def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
      var Y = params.l_params(0)
      var D = params.l_params(1)
      var vars = new mutable.HashMap[String,Term]
      //plan 0
      vars.clear()
      vars +=( "Y" -> params.l_params(0), "D" -> params.l_params(1) )
      val r0 = executionContext.beliefBase.query(Struct.of("==",VarManager.bindVar("D", vars),Number.of(10.0)))

      if (r0.result) {
        r0.bindings foreach { case (k, v) => vars += (k -> v) }
        plan0(vars)
        return
      }
      //plan 1
      vars.clear()
      vars +=( "Y" -> params.l_params(0), "D" -> params.l_params(1) )
      val r1 = executionContext.beliefBase.query(Struct.of(">=",VarManager.bindVar("D", vars),Number.of(5.0)))

      if (r1.result) {
        r1.bindings foreach { case (k, v) => vars += (k -> v) }
        plan1(vars)
        return
      }
      //plan 2
      vars.clear()
      vars +=( "Y" -> params.l_params(0), "D" -> params.l_params(1) )
      val r2 = executionContext.beliefBase.query(Struct.of("<",VarManager.bindVar("D", vars),Number.of(5.0)))

      if (r2.result) {
        r2.bindings foreach { case (k, v) => vars += (k -> v) }
        plan2(vars)
        return
      }

    }


    def plan0(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( VarManager.bindVar("Y", vars) , Term.createTerm("'is So far away! at'") , VarManager.bindVar("D", vars)  )))

    }
    def plan1(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( VarManager.bindVar("Y", vars) , Term.createTerm("'is a bit far, at'") , VarManager.bindVar("D", vars)  )))

    }
    def plan2(vars: mutable.HashMap[String,Term])(implicit executionContext: ExecutionContext): Unit = {

      PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( VarManager.bindVar("Y", vars) , Term.createTerm("'is close by'") , VarManager.bindVar("D", vars)  )))

    }


  }




  def inform( dest: Term,  message:Term) (implicit executionContext: ExecutionContext): Unit = {
    executionContext.yellowPages ! ActorMessage(
      dest.toString.replace("'",""),
      new BeliefMessage(message,executionContext.agent.self),
      executionContext.agent.self
    )
  }

  object Comparer {
    def compare(lhs: AnyVal,rhs: AnyVal, operator: String) : Boolean = true
  }

  def add_and_print(num1 : Any,num2:Any): Unit = {
    println(" sum of " + num1.asInstanceOf[Double] + " and " + num2.asInstanceOf[Double] + " = " )
    println(num1.asInstanceOf[Double] + num2.asInstanceOf[Double])
  }

}
