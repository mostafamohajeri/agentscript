package infrastructure

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object ExecutorActor {

  def apply(): Behavior[ExecutionRequest] = {
    Behaviors.setup { context => {
      Behaviors.receive { (context, message) =>
        message.step()
        message.c_sender.tell(
          ExecutionResponse(
              stepNo = message.stepNo
          )
        )
        Behaviors.same
      }
    }
    }
  }
}
