package infrastructure.access

//#user-registry-actor
import java.util

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import bb.expstyla.exp.StructTerm
import bb.serialize.terms.JsonTermDeserializer
import com.google.gson.Gson
import infrastructure.{AkkaMessageSource, ExecutionContext, GoalMessage, IMessage, YellowPages}

import scala.collection.immutable
import org.antlr.v4.runtime.{BufferedTokenStream, CharStreams, IntStream, Token, TokenStream}
//#user-case-classes
final case class PerformCommand(agent: String, command: spray.json.JsObject,src:String)

//#user-case-classes

object AgentRegistry {
  // actor protocol

  sealed trait Command
  final case class Achieve(command: PerformCommand, replyTo: ActorRef[ActionPerformed])
      extends Command

  final case class ActionPerformed(description: String)

  val deserializer = JsonTermDeserializer(new Gson())

  def apply(parent: ActorContext[IMessage], yellowPages: YellowPages): Behavior[Command] =
    registry(parent: ActorContext[IMessage],yellowPages)

  private def registry(parent: ActorContext[IMessage], yellowPages: YellowPages): Behavior[Command] =
    Behaviors.setup { context =>
      {
//        implicit val executionContext: ExecutionContext = ExecutionContext("god", "god", parent)
        Behaviors.receive { (context, message) =>
          message match {
            case Achieve(command, replyTo) =>
              try {
                val t = deserializer.deserialize(command.command.toString()).asInstanceOf[StructTerm]
                println(yellowPages.getAgent(command.src).get.asInstanceOf[AkkaMessageSource])
                replyTo ! ActionPerformed(
                  s"Command ${command.toString} created. and it was like ${t}"
                )
                yellowPages.getAgent(command.agent).get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(t.asInstanceOf[StructTerm],yellowPages.getAgent(command.src).get.asInstanceOf[AkkaMessageSource])
                // TODO: this was removed temporarily, needs DI
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
