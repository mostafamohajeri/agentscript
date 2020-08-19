package benchmark

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
 import bb.expstyla.exp._

 object thread {

 object Intention {

       def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


         Behaviors.receive {
         (context, message) =>

         implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

         message match {
            case SubGoalMessage(_,_,_,r) =>
               message.goal match {

               case thread.init =>
                    thread.init.execute(message.params.asInstanceOf[thread.init.Parameters])

               case thread.token =>
                    thread.token.execute(message.params.asInstanceOf[thread.token.Parameters])


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

         override def agent_type: String = "thread"

         var vars = VarMap()

         def initGoals()(implicit executionContext: ExecutionContext) = List[StructTerm](
                     StructTerm("init",Seq[GenericTerm](  ))


         )

         def initBeliefs()(implicit executionContext: ExecutionContext) = List[StructTerm](
         )

         def create_goal_message(t: StructTerm,r:String,ref:ActorRef[IMessage]) : Option[SubGoalMessage] = {
             if(t.functor=="init" && t.terms.size == 0 ) {
                                                        val args : init.Parameters = if(t.terms.size == 0)  init.Parameters(List( )) else init.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(init, args,r,ref))
                                                    } else if(t.functor=="token" && t.terms.size == 1 ) {
                                                        val args : token.Parameters = if(t.terms.size == 0)  token.Parameters(List( )) else token.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(token, args,r,ref))
                                                    } else  {
                                             println("no plan for this goal: " + t.toString)
                                             Option.empty[SubGoalMessage]
                                             }
         }

         def apply(name: String,yellowPages: ActorRef[IMessage],MAS: ActorRef[IMessage]): Behavior[IMessage] = {
               Behaviors.setup { context =>
                 val yp : ActorRef[IMessage] = yellowPages
                 val bb : BeliefBaseStyla = BeliefBaseFactory()
                 val logger = AgentLogger()
                 implicit val executionContext : ExecutionContext = ExecutionContext(name,agent_type,context,yp,bb,logger)
                 bb.assert(initBeliefs)

                 val initiator = context.spawn(Intention(executionContext),"initiator")

                 MAS ! ReadyMessage(context.self)
                 Behaviors.receive {
                   (context, message) => message match {
                     case StartMessage() =>
                       logger.start()

               implicit val timeout: Timeout = 99999.seconds
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
               //context.log.debug(f"$name: I have started, switching behavior")
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

      object init extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("init",Seq[GenericTerm]()) All vars no need to check */);

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query()

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                           vars += ("N" ->  (StringTerm("thread") +  ( (executionContext.name.replaceAll(StringTerm("thread"),StringTerm("")).toInt % benchmark.ring_data.nb_agents)  + IntTerm(1)) ) )
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("neighbor",Seq[GenericTerm](vars("N")))))


                     }


      }

      object token extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         vars +=( "0" -> params.l_params(0))

                         val m0 = executionContext.beliefBase.matchTerms(StructTerm("token",Seq[GenericTerm](IntTerm(0))),StructTerm("token",params.l_params));

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query()

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0
                 //plan 1 start

                         vars.clear()
                         vars +=(   "T" -> params.l_params(0))

                         val m1 = executionContext.beliefBase.matchTerms(/* StructTerm("token",Seq[GenericTerm](vars("T"))) All vars no need to check */);

                        if(m1.result)
                         {
                         m1.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r1 = executionContext.beliefBase.query(StructTerm("neighbor",Seq[GenericTerm](vars("N"))))

                         if (r1.result) {
                             r1.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan1(vars)
                             return
                         }

                          }
                          // plan 1
                 //plan 2 start

                         vars.clear()
                         vars +=(   "T" -> params.l_params(0))

                         val m2 = executionContext.beliefBase.matchTerms(/* StructTerm("token",Seq[GenericTerm](vars("T"))) All vars no need to check */);

                        if(m2.result)
                         {
                         m2.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r2 = executionContext.beliefBase.query(StructTerm("not",Seq[GenericTerm](StructTerm("neighbor",Seq[GenericTerm](vars("N"))))))

                         if (r2.result) {
                             r2.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan2(vars)
                             return
                         }

                          }
                          // plan 2


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(StringTerm("master1"),StructTerm("done",Seq[GenericTerm]()))))


                     }
                      def plan1(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(vars("N"),StructTerm("token",Seq[GenericTerm]( (vars("T") - IntTerm(1)) )))))


                     }
                      def plan2(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (StringTerm("problem") + executionContext.agentLogger.timeTaken) )))


                     }


      }



    def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

 }