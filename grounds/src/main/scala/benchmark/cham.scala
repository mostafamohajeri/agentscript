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

 object cham {

 object Intention {

       def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>


         Behaviors.receive {
         (context, message) =>

         implicit val executionContext = p_executionContext.copy(intention = context,sender = Sender(message.c_sender_name,message.c_sender))

         message match {
            case SubGoalMessage(_,_,_,r) =>
               message.goal match {

               case cham.set_and_print_color =>
                    cham.set_and_print_color.execute(message.params.asInstanceOf[cham.set_and_print_color.Parameters])

               case cham.go_mall =>
                    cham.go_mall.execute(message.params.asInstanceOf[cham.go_mall.Parameters])

               case cham.go_mall_again =>
                    cham.go_mall_again.execute(message.params.asInstanceOf[cham.go_mall_again.Parameters])

               case cham.mutate =>
                    cham.mutate.execute(message.params.asInstanceOf[cham.mutate.Parameters])

               case cham.same_agent =>
                    cham.same_agent.execute(message.params.asInstanceOf[cham.same_agent.Parameters])

               case cham.print_result =>
                    cham.print_result.execute(message.params.asInstanceOf[cham.print_result.Parameters])


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

         override def agent_type: String = "cham"

         var vars = VarMap()

         def initGoals()(implicit executionContext: ExecutionContext) = List[StructTerm](
                     StructTerm("set_and_print_color",Seq[GenericTerm](  ))


         )

         def initBeliefs()(implicit executionContext: ExecutionContext) = List[StructTerm](
                     StructTerm("nb_meetings",Seq[GenericTerm](IntTerm(0)))
           ,
            StructTerm("nb_meetings_same_color",Seq[GenericTerm](IntTerm(0)))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("blue",Seq[GenericTerm]()),StructTerm("red",Seq[GenericTerm]()),StructTerm("yellow",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("blue",Seq[GenericTerm]()),StructTerm("yellow",Seq[GenericTerm]()),StructTerm("red",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("red",Seq[GenericTerm]()),StructTerm("blue",Seq[GenericTerm]()),StructTerm("yellow",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("red",Seq[GenericTerm]()),StructTerm("yellow",Seq[GenericTerm]()),StructTerm("blue",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("yellow",Seq[GenericTerm]()),StructTerm("blue",Seq[GenericTerm]()),StructTerm("red",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](StructTerm("yellow",Seq[GenericTerm]()),StructTerm("red",Seq[GenericTerm]()),StructTerm("blue",Seq[GenericTerm]())))
           ,
            StructTerm("complement",Seq[GenericTerm](vars("C"),vars("C"),vars("C")))

         )

         def create_goal_message(t: StructTerm,r:String,ref:ActorRef[IMessage]) : Option[SubGoalMessage] = {
             if(t.functor=="set_and_print_color" && t.terms.size == 0 ) {
                                                        val args : set_and_print_color.Parameters = if(t.terms.size == 0)  set_and_print_color.Parameters(List( )) else set_and_print_color.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(set_and_print_color, args,r,ref))
                                                    } else if(t.functor=="go_mall" && t.terms.size == 0 ) {
                                                        val args : go_mall.Parameters = if(t.terms.size == 0)  go_mall.Parameters(List( )) else go_mall.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(go_mall, args,r,ref))
                                                    } else if(t.functor=="go_mall_again" && t.terms.size == 1 ) {
                                                        val args : go_mall_again.Parameters = if(t.terms.size == 0)  go_mall_again.Parameters(List( )) else go_mall_again.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(go_mall_again, args,r,ref))
                                                    } else if(t.functor=="mutate" && t.terms.size == 2 ) {
                                                        val args : mutate.Parameters = if(t.terms.size == 0)  mutate.Parameters(List( )) else mutate.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(mutate, args,r,ref))
                                                    } else if(t.functor=="same_agent" && t.terms.size == 0 ) {
                                                        val args : same_agent.Parameters = if(t.terms.size == 0)  same_agent.Parameters(List( )) else same_agent.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(same_agent, args,r,ref))
                                                    } else if(t.functor=="print_result" && t.terms.size == 0 ) {
                                                        val args : print_result.Parameters = if(t.terms.size == 0)  print_result.Parameters(List( )) else print_result.Parameters(t.terms.toList)
                                                         Option(SubGoalMessage(print_result, args,r,ref))
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

      object set_and_print_color extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("set_and_print_color",Seq[GenericTerm]()) All vars no need to check */);

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

                                           vars += ("C" -> benchmark.cham_data.color(myName.replaceAll(StringTerm("cham"),StringTerm("")).toInt))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println(vars("C"))))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(StringTerm("broker1"),StructTerm("ready",Seq[GenericTerm]()))))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("color",Seq[GenericTerm](vars("C")))))


                     }


      }

      object go_mall extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("go_mall",Seq[GenericTerm]()) All vars no need to check */);

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query(StructTerm("color",Seq[GenericTerm](vars("C"))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(StringTerm("broker1"),StructTerm("meet",Seq[GenericTerm](vars("C"))))))


                     }


      }

      object go_mall_again extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         vars +=(   "C" -> params.l_params(0))

                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("go_mall_again",Seq[GenericTerm](vars("C"))) All vars no need to check */);

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

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(StringTerm("broker1"),StructTerm("meet",Seq[GenericTerm](vars("C"))))))


                     }


      }

      object mutate extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         vars +=(   "A" -> params.l_params(0))
                          vars +=(   "C2" -> params.l_params(1))

                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("mutate",Seq[GenericTerm](vars("A"),vars("C2"))) All vars no need to check */);

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("color",Seq[GenericTerm](vars("C1"))),StructTerm("nb_meetings",Seq[GenericTerm](vars("N"))))),StructTerm("complement",Seq[GenericTerm](vars("C1"),vars("C2"),vars("C"))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("color",Seq[GenericTerm](vars("C1")))))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("color",Seq[GenericTerm](vars("C")))))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("nb_meetings",Seq[GenericTerm](vars("N")))))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("nb_meetings",Seq[GenericTerm]( (vars("N") + IntTerm(1)) ))))
                                          if(( (myName == vars("A")) ).holds) {
                                                                  same_agent.execute(same_agent.Parameters(List(  )))

                                          }
                                          go_mall_again.execute(go_mall_again.Parameters(List( vars("C")  )))


                     }


      }

      object same_agent extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("same_agent",Seq[GenericTerm]()) All vars no need to check */);

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query(StructTerm("nb_meetings_same_color",Seq[GenericTerm](vars("N"))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("nb_meetings_same_color",Seq[GenericTerm](vars("_")))))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("nb_meetings_same_color",Seq[GenericTerm]( (vars("N") + IntTerm(1)) ))))


                     }


      }

      object print_result extends IGoal {
         case class Parameters(l_params: List[GenericTerm]) extends IParams {}

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()
                 //plan 0 start

                         vars.clear()
                         val m0 = executionContext.beliefBase.matchTerms(/* StructTerm("print_result",Seq[GenericTerm]()) All vars no need to check */);

                        if(m0.result)
                         {
                         m0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }

                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm("nb_meetings_same_color",Seq[GenericTerm](vars("N"))),StructTerm("nb_meetings",Seq[GenericTerm](vars("N2"))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) => vars += (k -> v.asInstanceOf[GenericTerm]) }
                             plan0(vars)
                             return
                         }

                          }
                          // plan 0


        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( ( ( (StringTerm("meetings: ") + vars("N2"))  + StringTerm(" | same name: "))  + vars("N")) )))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => achieve(StringTerm("broker1"),StructTerm("done",Seq[GenericTerm]()))))


                     }


      }



    def myName() (implicit executionContext: ExecutionContext) : String = executionContext.name

 }