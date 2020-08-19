//
//
//import akka.actor.typed
//import akka.actor.typed.{ActorRefResolver, ActorSystem}
//import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}
//import it.unibo.tuprolog.core.{Atom, Integer, Numeric, Real, Struct, Term, Truth}
//import kyc.{bank, client, employee}
//import std.converters._
//
//import scala.language.implicitConversions
//
//
//object Main {
//
//  //  val gtSignature = new Signature("gt", 2,false)
////  implicit def Integer2Integer(x: Integer): Int =
////    x.getIntValue.toInt
////
////  implicit def Real2Real(x: Real): Double =
////    x.getDecimalValue.toDouble
//
////  implicit def Term2Real(x: Term): Double =
////    x.as[Real].getValue.toDouble
////
//////  implicit def Term2Integer(x: Term): Int =
//////    x.as[Integer].getValue.toInt
////////
////  implicit def Term2String(x: Term): String =
////    x.as[Atom].getValue.toString
////
////  implicit def Term2Boolean(x: Term): Boolean =
////    x.as[Truth].isTrue
//
//
////
////  implicit def Real2Integer(x: Real): Integer =
////    Integer.of(x.getIntValue.toIntExact)
////
////  implicit def Integer2Real(x: Integer): Real =
////    Real.of(x.getIntValue.toDouble)
//
//
//  def main(args: Array[String]): Unit = {
//    import org.apache.log4j.BasicConfigurator
//    BasicConfigurator.configure()
//////
////    val vars : Map[String,Term] = Map(
////      "M" -> Real.of(3),
////      "A" -> Atom.of("a"),
////      "B" -> Atom.of("b"),
////      "C" -> Real.of(3),
////      "H1" -> Struct.of("term",Struct.of("t",java.util.List.of[Term]())),
////    "H2" -> Struct.of("term",Struct.of("t",java.util.List.of[Term]())),
////      "T" -> Truth.of(false)
////
////    )
//////
//
//
//    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")
//    val resolver : ActorRefResolver = ActorRefResolver(system)
//
//    system ! AgentRequestMessage(
//      Seq(
//        AgentRequest(client.Agent, "client", 4),
//        AgentRequest(bank.Agent, "ing", 1),
//        AgentRequest(bank.Agent, "abn", 1),
//        AgentRequest(employee.Agent, "employee", 2)
//      )
//    )
//
//  }
//
//}
