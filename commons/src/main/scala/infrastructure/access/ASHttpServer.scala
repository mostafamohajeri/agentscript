package infrastructure.access

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import infrastructure.{IMessage, YellowPages}

import scala.util.Failure
import scala.util.Success

//#main-class
object ASHttpServer {
  //#start-http-server

  private def startHttpServer(routes: Route, system: ActorSystem[_], port : Int): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    val futureBinding = Http().bindAndHandle(routes, "localhost", port)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  //#start-http-server
  def create(context: ActorContext[IMessage], yellowPages: YellowPages, port : Int): Unit = {
    val agentRegistryActor = context.spawn(AgentRegistry(context,yellowPages), "AgentRegistryActor")
    context.watch(agentRegistryActor)

    val routes = new AgentRoutes(agentRegistryActor)(context.system)
    startHttpServer(routes.routes, context.system,port)
  }
}
//#main-class
