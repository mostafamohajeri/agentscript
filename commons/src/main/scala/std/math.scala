package std
import it.unibo.tuprolog.core.{Term, Struct,Numeric}

import Numeric._


import scala.jdk.CollectionConverters._

object math {

  def plus(double1: Double,double2: Double) : Double =
    double1 + double2

  def range(from: Any, to: Any) : List[Any] =
    List.range(from.asInstanceOf[Int],to.asInstanceOf[Int])


}
