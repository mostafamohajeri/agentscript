package normconnector

import akka.actor.typed.ActorRef

case class NormActorCreationMessage ( name: String, actorRef:  ActorRef[ norms . NormActor.Message ] )
  extends NormConsciences . NormsConnectorMessage
