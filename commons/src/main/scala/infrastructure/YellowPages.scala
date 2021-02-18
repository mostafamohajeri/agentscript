package infrastructure

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.mutable

object YellowPages {

  val agents: mutable.HashMap[String, ActorRef[IMessage]] = mutable.HashMap()

  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context =>
      {
        var agentsPersistent: mutable.HashMap[String, ActorRef[IMessage]] = mutable.HashMap()
        Behaviors.receive { (context, message) =>
          message match {
            case ActorSubscribeMessage(name, ref) =>
              agentsPersistent.put(name, ref)
              agents.put(name, ref)
            case ActorMessage(d, m, s_r) =>
              m match {
                case BeliefMessage(b, _) =>
                  //              println (f" $s_n wants to send belief $b to $d")
                  if (agentsPersistent.contains(d))
                    agentsPersistent(d) ! m
                  else println("no such agent")
              }
          }
          Behaviors.same
        }
      }
    }
  }
}
