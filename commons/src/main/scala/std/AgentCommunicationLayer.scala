package std

import bb.expstyla.exp.{BooleanTerm, GenericTerm, IntTerm}
import infrastructure.{ExecutionContext, IMessageSource}

trait AgentCommunicationLayer {

  def achieve(ref: IMessageSource, message: Any)(implicit executionContext: ExecutionContext): Any

  def achieve(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any

  def broadcast_achieve(message: Any)(implicit executionContext: ExecutionContext): Any

  def broadcast_achieve(reg: String, message: Any)(implicit executionContext: ExecutionContext): Any

  def broadcast_inform(reg: String, message: Any)(implicit executionContext: ExecutionContext): Any

  def broadcast_inform(message: Any)(implicit executionContext: ExecutionContext): Any

  def inform(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any

  def inform(ref: IMessageSource, message: Any)
            (implicit executionContext: ExecutionContext)
  : Any

  def un_inform(destName: String, message: Any)(implicit executionContext: ExecutionContext): Any

  def un_inform(ref: IMessageSource, message: Any)
            (implicit executionContext: ExecutionContext)
  : Any


  def ask(destName: String, message: Any, response:GenericTerm, timeout: IntTerm)(implicit executionContext: ExecutionContext): BooleanTerm

  def ask(ref: IMessageSource, message: Any, response:GenericTerm, timeout: IntTerm)
         (implicit executionContext: ExecutionContext)
  : BooleanTerm

  def ask(destName: String, message: Any, timeout: IntTerm)(implicit executionContext: ExecutionContext): GenericTerm

  def ask(ref: IMessageSource, message: Any, timeout: IntTerm)
         (implicit executionContext: ExecutionContext)
  : GenericTerm

  def ask(destName: String, message: Any, response:GenericTerm)(implicit executionContext: ExecutionContext): BooleanTerm

  def ask(ref: IMessageSource, message: Any, response:GenericTerm)
         (implicit executionContext: ExecutionContext)
  : BooleanTerm

  def ask(destName: String, message: Any)(implicit executionContext: ExecutionContext): GenericTerm

  def ask(ref: IMessageSource, message: Any)
         (implicit executionContext: ExecutionContext)
  : GenericTerm

  def respond(message: Any)(implicit executionContext: ExecutionContext): Any
}
