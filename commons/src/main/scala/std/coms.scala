package std

import akka.actor.typed.ActorRef
import infrastructure._

import scala.collection.parallel.CollectionConverters._

object coms {

  var total_send_time = 0

  def inform(destName: Any, message: Any)(implicit executionContext: ExecutionContext): Any = {
    executionContext.yellowPages !
      ActorMessage(
        destName.toString,
        BeliefMessage(
          message,
          executionContext.name,
          executionContext.agent.self
        ),
        executionContext.name,
        executionContext.agent.self
      )
  }

  def achieve(ref: ActorRef[IMessage], message: Any)(implicit executionContext: ExecutionContext): Any = {

    println(executionContext.name + " --[" + message + "]--> " + ref)


  ref !
    GoalMessage(
      message,
      executionContext.name,
      executionContext.agent.self
    )
}


  def achieve(destName: Any, message: Any)(implicit executionContext: ExecutionContext): Any = {

    val destination = YellowPages.agentsPersistentCentral.get(destName.toString)
//    if (destination.isEmpty)
//      executionContext.agent.log.error(f"agent $destName does not exist")
//    else
    println(executionContext.name + " --[" + message + "]--> " + destName)
      destination.get !
        GoalMessage(
          message,
          executionContext.name,
          executionContext.agent.self
        )
  }


  def broadcast_achieve(message: Any)(implicit executionContext: ExecutionContext): Any =
    YellowPages.agentsPersistentCentral.par.filter(a => executionContext.name != a._1).foreach(
      a => a._2 ! GoalMessage(
        message,
        executionContext.name,
        executionContext.agent.self
      )
    )

  def broadcast_achieve(reg: String, message: Any)(implicit executionContext: ExecutionContext): Any =
    YellowPages.agentsPersistentCentral.par.filter(a => executionContext.name != a._1 && a._1.matches(reg)).foreach(
      a => a._2 ! GoalMessage(
        message,
        executionContext.name,
        executionContext.agent.self
      )
    )

}
