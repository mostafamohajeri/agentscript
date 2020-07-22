//import TheParent.StartMessage
//import akka.actor.typed.{ActorRef, Behavior}
//import akka.actor.typed.scaladsl.Behaviors
//
//
//import scala.collection.mutable
//import scala.collection.mutable.ListBuffer
//import scala.util.Random
//
//object SomeActor {
//
//
//
//  def apply(): Behavior[StartMessage] = {
//    Behaviors.setup { context => {
//
//      Behaviors.receive { (context, message) =>
//        var beliefs = new mutable.HashMap[String, ListBuffer[Seq[Any]]]()
//
//        beliefs += ("neighbor" -> new ListBuffer[Seq[Any]])
//        beliefs("neighbor").append(Seq("tom",10, "me"))
//        beliefs("neighbor").append(Seq("ali",15, "me"))
//        beliefs("neighbor").append(Seq("gio",20, "you"))
//
//        Seq("Me").foreach(N => {
//          beliefs("neighbor").filter(t => {
//            t(2) == N.toLowerCase
//          }).foreach ( Tuple123 => {
//            var Neighbor = Tuple123(0)
//            var Distance = Tuple123(1)
//            Seq( Distance.asInstanceOf[Int] +1 ).foreach( X => {
//              Seq(Distance.asInstanceOf[Int] ).foreach( Z => {
//                if( X > 12 ) {
//                  println(X)
//                  println(Z)
//                  println("finished " + System.nanoTime())
//                }
//              })
//            } )
//          })
//        })
//
//        Behaviors.stopped
//      }
//    }
//    }
//  }
//}
//
//
//
//object TheParent {
//
//  trait StartMessage
//
//  def apply(): Behavior[StartMessage] = {
//    Behaviors.setup { context => {
//      var refs = new mutable.HashMap[String,ActorRef[StartMessage]]()
//
//      for(i <- 1 to 500) {
//        refs.put(i.toString , context.spawn(SomeActor(), i.toString))
//      }
//
//      Behaviors.receive { (context, message) =>
//        println("started " + System.nanoTime())
//        refs.foreach(k => {
//          k._2 ! new StartMessage {}
//        })
//
//
//
//        Behaviors.stopped
//      }
//    }
//    }
//  }
//}
