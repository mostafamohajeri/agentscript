package scriptcc

import agentfactory.FactoryManager
import akka.actor.typed
import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.io.Source

class ScriptRunner {

  def createMas(jsonFile: String, verbose : Boolean = false) : MAS = {


    if (verbose) {
      import org.apache.log4j.BasicConfigurator
      BasicConfigurator.configure()
    }

    val requests = Configure.create(jsonFile = jsonFile)

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

    val conf1 = ConfigFactory.load(ConfigFactory.parseString(
      """
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

    val mas: MAS = MAS()
    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(), "MAS", conf1)

    system ! AgentRequestMessage(
      requests, system.ref
    )

    mas
  }
}
