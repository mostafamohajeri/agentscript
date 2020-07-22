package infrastructure.access

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import infrastructure.IMessage

import scala.util.Failure
import scala.util.Success

//#main-class
object ASHttpServer {
  //#start-http-server
  private def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    val futureBinding = Http().bindAndHandle(routes, "localhost", 8080)
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
  def create(context: ActorContext[IMessage]): Unit = {
      val agentRegistryActor = context.spawn(AgentRegistry(context), "AgentRegistryActor")
      context.watch(agentRegistryActor)

      val routes = new AgentRoutes(agentRegistryActor)(context.system)
      startHttpServer(routes.routes, context.system)
  }
}
//#main-class
