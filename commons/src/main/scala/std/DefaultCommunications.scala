package std

import _root_.akka.actor.typed.scaladsl.AskPattern._
import _root_.akka.util.Timeout
import akka.actor.typed.Scheduler
import bb.expstyla.exp.{BooleanTerm, GenericTerm, IntTerm, StructTerm}
import com.typesafe.config.ConfigException.Generic
import infrastructure._

import _root_.scala.concurrent.duration._
import _root_.scala.concurrent.{Await, ExecutionContextExecutor, Future}
import _root_.scala.language.implicitConversions
import _root_.scala.util.{Failure, Success}
import scala.collection.parallel.CollectionConverters._

class DefaultCommunications(logger: CommunicationLogger = SinkComminicationLogger()) extends AgentCommunicationLayer {


  def sendGoal(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(
      message,
      src
    )
    logger.logAchieveMessage(src.name(),message.toString,dest.name())
    true
  }

  def sendBelief(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(
      message,
      src
    )
    logger.logBeliefMessage(src.name(),message.toString,dest.name())
    true
  }

  def sendUnBelief(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! UnBeliefMessage(
      message,
      src
    )
    logger.logUnBeliefMessage(src.name(),message.toString,dest.name())
    true
  }


  def sendResponse(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(
      message,
      src
    )
    true
  }

  def sendAsk(dest: IMessageSource, message: Any, src: IMessageSource,t: IntTerm)(implicit executionContext: ExecutionContext): GenericTerm = {

    implicit val timeout: Timeout = t.getIntValue.milliseconds
    implicit val ec: ExecutionContextExecutor = executionContext.intention.executionContext
    implicit val scheduler: Scheduler = executionContext.intention.system.scheduler



    val result: Future[IMessage] = dest.asInstanceOf[AkkaMessageSource].address().ask[IMessage]( ref => {
      AskMessage(
        message,
        AkkaMessageSource(ref)
      )
    }
    )

    logger.logAskMessage(src.name(),message.toString,dest.name())


    try {
      val response = Await.result(result, timeout.duration).asInstanceOf[BeliefMessage].content.asInstanceOf[GenericTerm]
      logger.logRespondMessage(dest.name(),response.toString,src.name())
      return response
    }
    catch {
      case _ =>
        return BooleanTerm(false)
    }

  }


  override def achieve(ref: IMessageSource, message: Any)
                      (implicit executionContext: ExecutionContext)
  : Any = {
    ref match {
      case dest: AkkaMessageSource =>
        sendGoal(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        )
      case _ =>
        throw new RuntimeException("Message Source Unknown")
    }
  }

  override def achieve(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any = {
    val destination = executionContext.yellowPages.getAgent(destName.toString)
    if (destination.isEmpty)
      println(f"$destName Not Found")
    else {
      destination.get match {
        case dest: AkkaMessageSource =>
          sendGoal(dest,
            message,
            AkkaMessageSource(executionContext.agent.self)
          )
      }
    }
  }

  override def broadcast_achieve(message: Any)(implicit executionContext: ExecutionContext): Any =
    executionContext.yellowPages.getAll().par
      .filter(a => executionContext.name != a._1 && !a._1.equals("__MAS"))
      .foreach(a =>
        a._2 match {
          case dest: AkkaMessageSource =>
            sendGoal(dest,
              message,
              AkkaMessageSource(executionContext.agent.self)
            )
        }
      )

  override def broadcast_achieve(reg: String, message: Any)(implicit executionContext: ExecutionContext): Any =
    executionContext.yellowPages.getAll().par
      .filter(a => executionContext.name != a._1 && a._1.matches(reg) && !a._1.equals("__MAS"))
      .foreach(a =>
        a._2 match {
          case dest: AkkaMessageSource =>
            sendGoal(dest,
              message,
              AkkaMessageSource(executionContext.agent.self)
            )
        }
      )

  override def inform(ref: IMessageSource, message: Any)
                     (implicit executionContext: ExecutionContext)
  : Any = {
    ref match {
      case dest: AkkaMessageSource =>
        sendBelief(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        )
      case _ =>
        throw new RuntimeException("Message Source Unknown")
    }
  }

  override def inform(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any = {
    val destination = executionContext.yellowPages.getAgent(destName.toString)
    if (destination.isEmpty)
      println(f"$destName Not Found")
    else {
      destination.get match {
        case dest: AkkaMessageSource =>
          sendBelief(dest,
            message,
            AkkaMessageSource(executionContext.agent.self)
          )
      }
    }
  }



  override def broadcast_inform(message: Any)(implicit executionContext: ExecutionContext): Any =
    executionContext.yellowPages.getAll().par
      .filter(a => executionContext.name != a._1 && !a._1.equals("__MAS"))
      .foreach(a =>
        a._2 match {
          case dest: AkkaMessageSource =>
            sendBelief(dest,
              message,
              AkkaMessageSource(executionContext.agent.self)
            )
        }
      )

  override def broadcast_inform(reg: String, message: Any)(implicit executionContext: ExecutionContext): Any =
    executionContext.yellowPages.getAll().par
      .filter(a => executionContext.name != a._1 && a._1.matches(reg) && !a._1.equals("__MAS"))
      .foreach(a =>
        a._2 match {
          case dest: AkkaMessageSource =>
            sendBelief(dest,
              message,
              AkkaMessageSource(executionContext.agent.self)
            )
        }
      )

  override def ask(destName: String, message: Any, timeout: IntTerm)(implicit executionContext: ExecutionContext): GenericTerm = {
    val destination = executionContext.yellowPages.getAgent(destName.toString)
    if (destination.isEmpty) {
      println(f"$destName Not Found")
      BooleanTerm(false)
    }
    else {
      destination.get match {
        case dest: AkkaMessageSource =>
          sendAsk(dest,
            message,
            AkkaMessageSource(executionContext.agent.self),
            timeout
          ).asInstanceOf[GenericTerm]
      }
    }
  }


  override def un_inform(ref: IMessageSource, message: Any)
                     (implicit executionContext: ExecutionContext)
  : Any = {
    ref match {
      case dest: AkkaMessageSource =>
        sendUnBelief(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        )
      case _ =>
        throw new RuntimeException("Message Source Unknown")
    }
  }

  override def un_inform(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any = {
    val destination = executionContext.yellowPages.getAgent(destName.toString)
    if (destination.isEmpty)
      println(f"$destName Not Found")
    else {
      destination.get match {
        case dest: AkkaMessageSource =>
          sendUnBelief(dest,
            message,
            AkkaMessageSource(executionContext.agent.self)
          )
      }
    }
  }

  override def ask(ref: IMessageSource, message: Any,timeout: IntTerm)(implicit executionContext: ExecutionContext): GenericTerm =  {
    ref match {
      case dest: AkkaMessageSource =>
        sendAsk(dest,
          message,
          AkkaMessageSource(executionContext.agent.self),
          timeout
        ).asInstanceOf[GenericTerm]
      case _ =>
        throw new RuntimeException("Message Source Unknown")
    }
  }

  override def ask(destName: String, message: Any, response: GenericTerm, timeout: IntTerm)(implicit executionContext: ExecutionContext): BooleanTerm =
    {
      val answer = this.ask(destName,message,timeout)
      response.unify(answer)
    }

  override def ask(ref: IMessageSource, message: Any, response: GenericTerm, timeout: IntTerm )(implicit executionContext: ExecutionContext): BooleanTerm =
    {
      val answer = this.ask(ref, message,timeout)
      response.unify(answer)
    }

  override def ask(destName: String, message: Any, response: GenericTerm)(implicit executionContext: ExecutionContext): BooleanTerm = ask(destName,message,response,IntTerm(5000))

  override def ask(ref: IMessageSource, message: Any, response: GenericTerm)(implicit executionContext: ExecutionContext): BooleanTerm = ask(ref,message,response,IntTerm(5000))

  override def ask(destName: String, message: Any)(implicit executionContext: ExecutionContext): GenericTerm = ask(destName,message,IntTerm(5000))

  override def ask(ref: IMessageSource, message: Any)(implicit executionContext: ExecutionContext): GenericTerm = ask(ref,message,IntTerm(5000))


  override def respond(message: Any)(implicit executionContext: ExecutionContext):Any = {
    executionContext.src match {
      case dest: AkkaMessageSource =>
        sendResponse(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        )
    }
  }

}
