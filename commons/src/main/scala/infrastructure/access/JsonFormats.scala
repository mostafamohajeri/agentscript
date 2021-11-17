package infrastructure.access

//#json-formats
import infrastructure.access.AgentRegistry.ActionPerformed
import spray.json.DefaultJsonProtocol

object JsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val agentJsonFormat = jsonFormat3(PerformCommand)
//  implicit val agentsJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(AgentRegistry.ActionPerformed)

}
//#json-formats
