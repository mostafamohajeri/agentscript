package benchmark

import bb.expstyla.exp.{DoubleTerm, GenericTerm}

object PingPong {

  def waitForSomeTime(double: Double)  : Boolean = {
    val wait = (double * 1000d).toInt
    println(f"waiting  for $wait ms")
    Thread.sleep(wait)
    true
  }
}
