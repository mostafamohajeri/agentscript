package listener_talker


import akka.actor.typed
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import akka.actor.typed.ActorSystem
import bb.exp.{BooleanTerm, DoubleTerm, GenericTerm, IntTerm, StringTerm}
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}
import it.unibo.tuprolog.core.{Atom, Real, Term, Truth}

import scala.language.implicitConversions

object Main {


  def main(args: Array[String]): Unit = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()
//
    val vars : Map[String,GenericTerm] = Map("M" -> IntTerm(10),"N"-> DoubleTerm(10))
//
//    println(true  > true )
//

    println( (vars("M") == vars("N") ).holds )

    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")


    system ! AgentRequestMessage(
      Seq(
        AgentRequest(listener.Agent, "listener", 1000),
        AgentRequest(talker.Agent, "talker", 1)
      )
    )

  }

}
