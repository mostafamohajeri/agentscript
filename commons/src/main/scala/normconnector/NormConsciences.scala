package normconnector

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import infrastructure.IMessage
import norms.NormActor.Message

import scala.collection.mutable

object NormConsciences {

  trait NormsConnectorMessage extends norms.Message

  def apply(agent: ActorRef[IMessage]): Behavior[norms.Message] = {
    Behaviors.setup { context => {
      var norms_connected: mutable.HashMap[ String, ActorRef[ norms.NormActor.Message ] ] = mutable.HashMap( )
      Behaviors.receive { (context, message) =>
        message match {
          case NormActorCreationMessage( name, ref ) =>
            norms_connected.put( name, ref )

        }
        Behaviors.same
      }
    }
    }
  }
}
