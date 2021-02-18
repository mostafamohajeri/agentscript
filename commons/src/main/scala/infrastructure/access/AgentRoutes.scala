package infrastructure.access

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import infrastructure.access.AgentRegistry.Achieve
import infrastructure.access.AgentRegistry._

import scala.concurrent.Future

//#import-json-formats
//#user-routes-class
class AgentRoutes(registry: ActorRef[AgentRegistry.Command])(implicit val system: ActorSystem[_]) {

  //#user-routes-class
  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout =
    Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def achieveCommand(performCommand: PerformCommand): Future[AgentRegistry.ActionPerformed] =
    registry.ask(Achieve(performCommand, _))

  //#all-routes
  //#users-get-post
  //#users-get-delete
  val routes: Route =
    pathPrefix("achieve") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(post {
            entity(as[PerformCommand]) { user =>
              onSuccess(achieveCommand(user)) { performed =>
                complete((StatusCodes.Created, performed))
              }
            }
          })
        }
      )
      //#users-get-delete
    }
  //#all-routes
}
