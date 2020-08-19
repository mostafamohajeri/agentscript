package benchmark

import akka.actor.typed
import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.language.implicitConversions

object Chameneos {


  def main(args: Array[String]): Unit = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()

    if(args.length < 2)
      {
        println("please provide the needed args")
        return
      }

    cham_data.nb_chameneos = args(1).toInt
    cham_data.nb_meetings = args(0).toInt


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
      parallelism-max = 64
    }
    # Throughput defines the maximum number of messages to be
    # processed per actor before the thread jumps to the next actor.
    # Set to 1 for as fair as possible.
    throughput = 1
  }}}

 """))


    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")



    system ! AgentRequestMessage(
      Seq(
        AgentRequest(broker.Agent, "broker", 1),
        AgentRequest(cham.Agent, "cham", cham_data.nb_chameneos),
      )
    )

  }

}
