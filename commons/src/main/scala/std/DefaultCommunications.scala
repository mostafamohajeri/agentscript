package std

import _root_.akka.actor.typed.scaladsl.AskPattern._
import _root_.akka.util.Timeout
import akka.actor.typed.Scheduler
import bb.expstyla.exp.{BooleanTerm, GenericTerm, StructTerm}
import com.typesafe.config.ConfigException.Generic
import infrastructure._

import _root_.scala.concurrent.duration._
import _root_.scala.concurrent.{Await, ExecutionContextExecutor, Future}
import _root_.scala.language.implicitConversions
import _root_.scala.util.{Failure, Success}
import scala.collection.parallel.CollectionConverters._

class DefaultCommunications extends AgentCommunicationLayer {


  def sendGoal(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(
      message,
      src
    )
    true
  }

  def sendBelief(dest: IMessageSource, message: Any, src: IMessageSource): Any = {
    dest.asInstanceOf[AkkaMessageSource].address() ! BeliefMessage(
      message,
      src
    )
    true
  }

  def sendAsk(dest: IMessageSource, message: Any, src: IMessageSource)(implicit executionContext: ExecutionContext): GenericTerm = {

    implicit val timeout: Timeout = 3.seconds
    implicit val ec: ExecutionContextExecutor = executionContext.intention.executionContext
    implicit val scheduler: Scheduler = executionContext.intention.system.scheduler

    val result: Future[IMessage] = dest.asInstanceOf[AkkaMessageSource].address().ask[IMessage]( ref => {
      AskMessage(
        message,
        AkkaMessageSource(ref)
      )
    }
    )


    //    result.onComplete {
    //      case Success(BeliefMessage(c,s)) => c.asInstanceOf[GenericTerm]
    //      case Failure(_) => BooleanTerm(false)
    //    }

    try {
      return Await.result(result, timeout.duration).asInstanceOf[BeliefMessage].content.asInstanceOf[StructTerm]
    }
    catch {
      case _ => return BooleanTerm(false)
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

  override def ask(destName: String, message: Any)(implicit executionContext: ExecutionContext): GenericTerm = {
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
            AkkaMessageSource(executionContext.agent.self)
          ).asInstanceOf[GenericTerm]
      }
    }
  }

  override def ask(ref: IMessageSource, message: Any)(implicit executionContext: ExecutionContext): GenericTerm =  {
    ref match {
      case dest: AkkaMessageSource =>
        sendAsk(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        ).asInstanceOf[GenericTerm]
      case _ =>
        throw new RuntimeException("Message Source Unknown")
    }
  }

  override def respond(message: Any)(implicit executionContext: ExecutionContext):Any = {
    executionContext.src match {
      case dest: AkkaMessageSource =>
        sendBelief(dest,
          message,
          AkkaMessageSource(executionContext.agent.self)
        )
    }
  }

  override def ask(destName: String, message: Any, response: GenericTerm)(implicit executionContext: ExecutionContext): BooleanTerm =
    {
      val answer = this.ask(destName,message)
      response.unify(answer)
    }

  override def ask(ref: IMessageSource, message: Any, response: GenericTerm)(implicit executionContext: ExecutionContext): BooleanTerm =
    {
      val answer = this.ask(ref, message)
      response.unify(answer)
    }
}
