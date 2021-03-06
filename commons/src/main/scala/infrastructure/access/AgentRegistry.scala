package infrastructure.access

//#user-registry-actor
import java.util

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import infrastructure.{ExecutionContext, IMessage, YellowPages}


import scala.collection.immutable
import org.antlr.v4.runtime.{BufferedTokenStream, CharStreams, IntStream, Token, TokenStream}
//#user-case-classes
final case class PerformCommand(agent: String, command: String)
//#user-case-classes

object AgentRegistry {
  // actor protocol
  sealed trait Command
  final case class Achieve(command: PerformCommand, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class ActionPerformed(description: String)





  def apply(parent: ActorContext[IMessage]): Behavior[Command] = registry(parent: ActorContext[IMessage])

  private def registry(parent: ActorContext[IMessage]): Behavior[Command] =
    Behaviors.setup { context => {
      implicit val executionContext : ExecutionContext = ExecutionContext("god","god",parent)
      Behaviors.receive  { (context,message) =>
        message match {
          case Achieve(command, replyTo) =>
            try {
              val t = null
              replyTo ! ActionPerformed(s"Command ${command.toString} created. and it was like ${t}")
              std.coms.achieve(command.agent, t)
            } catch {
              case t: Throwable => replyTo ! ActionPerformed(s"${t.getLocalizedMessage}")
            }
            Behaviors.same
          case _ =>
            context.log.error(s"Unknown message type ${message.getClass}")
            Behaviors.same
        }
      }
    }
    }



}
//#user-registry-actor
