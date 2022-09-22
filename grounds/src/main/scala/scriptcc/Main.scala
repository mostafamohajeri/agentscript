package scriptcc

import HandyTests.MainTest.system
import agentfactory.FactoryManager
import akka.actor.typed
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.collection.mutable
import scala.io.Source
import scala.language.implicitConversions

object Main {

  def main(args: Array[String]): Unit = {

    val usage = """
    Usage: as.jar [-verbose v] filename
    """

    if (args.length == 0) { println(usage); return }
    val arglist = args.toList
    type OptionMap = Map[Symbol, Any]

    def nextOption(map: OptionMap, list: List[String]): OptionMap = {
      def isSwitch(s: String) = (s(0) == '-')

      list match {
        case Nil => map
        case "-verbose" :: value :: tail =>
          nextOption(map ++ Map(Symbol("verbose") -> value.toInt), tail)
        case string :: opt2 :: tail if isSwitch(opt2) =>
          nextOption(map ++ Map(Symbol("infile") -> string), list.tail)
        case string :: Nil  => nextOption(map ++ Map(Symbol("infile") -> string), list.tail)
        case option :: tail => println("Unknown option " + option); null

      }
    }

    val options = nextOption(Map(), arglist)

    if (!options.contains(Symbol("infile"))) {
      println(usage)
      return
    }

    if (options.contains(Symbol("verbose")) && options(Symbol("verbose")).equals(1)) {
      import org.apache.log4j.BasicConfigurator
      BasicConfigurator.configure()
    }

    val file   = options(Symbol("infile")).toString
    val source = Source.fromFile(file)

    val loc = file.replaceAll(file.split("/").last, "")

    val input = source.getLines().mkString

    val data = ujson.read(input)
    val requests: Seq[AgentRequest] = data("agents").arr
      .map(o =>
        AgentRequest(
          FactoryManager.create(loc + o("script_file").str, o("name").str),
          o("name").str,
          o("number").num.toInt
        )
      )
      .toSeq

//    println(requests)

//
//    vars +=+ ("B" -> IntTerm(10))
//
//    val c = StructTerm("a",Seq(vars("A"))).get().iterator
//
//    val d = StructTerm("b",Seq(vars("A"),vars("B"))).get().iterator
//    c.foreach(s =>println(s))
//

//    StructTerm("a",Seq(vars("A"))).get().foreach(
//      s => {
//        println(s.map)
//        val subs1 = VarMap() ++ vars
//        s.map.foreach(t => subs1+=(t._1->t._2))
//        StructTerm("b",Seq(subs1("B"))).get().foreach(
//          s => {
//            val subs2 = subs1 ++ VarMap()
//            s.map.foreach(t => subs2+=(t._1->t._2))
//            println("subs2:"  + subs2)
//          }
//        )
//      }
//    )

//    println(StructTerm("a",Seq(IntTerm(2))) == StructTerm("a",Seq(IntTerm(1))))

//    vars += ("M" -> StructTerm("a",Seq(IntTerm(1))))
//    vars += ("N" -> StructTerm("a",Seq(IntTerm(2))))
//
//    println(vars.vars )
//

//    (vars("M").holds)
//    vars2("M") = IntTerm(1)
//    println( (vars+=+ ("M" -> IntTerm(2))).getClass )
//    println( vars+=+ ("M" -> IntTerm(2)) )
//    println(  (vars -=- IntTerm(100))  && StructTerm("neighbor",Seq[GenericTerm](vars("N"))) )

    val conf1 = ConfigFactory.load(ConfigFactory.parseString("""
 akka {
 actor {
  default-dispatcher {
    # Dispatcher is the name of the event-based dispatcher
    type = Dispatcher
    # What kind of ExecutionService to use
    executor = "fork-join-executor"
    # Configuration for the fork join pool
    fork-join-executor {
      # Min number of threads to cap factor-based parallelism number to
      parallelism-min = 32
      # Parallelism (threads) ... ceil(available processors * factor)
      parallelism-factor = 1.0
      # Max number of threads to cap factor-based parallelism number to
      parallelism-max = 32
    }
    # Throughput defines the maximum number of messages to be
    # processed per actor before the thread jumps to the next actor.
    # Set to 1 for as fair as possible.
    throughput = 1
  }}}

 """))

    val mas : MAS = MAS()
    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(), "MAS", conf1)

//    system ! AgentRequestMessage(
//      Seq(
////        AgentRequest(listener.Agent, "listener", 1000),
//        AgentRequest(broker.Agent, "broker", 1),
//        AgentRequest(cham.Agent, "cham", 500)
//      )
//    )
//
    system ! AgentRequestMessage(
      requests, system.ref
    )

  }

}
