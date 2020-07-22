package std
import it.unibo.tuprolog.core.{Term, Struct,Numeric}

import Numeric._


import scala.jdk.CollectionConverters._

object math {

  def plus(double1: Double,double2: Double) : Double =
    double1 + double2

  def range(from: Any, to: Any) : List[Any] =
    List.range(from.asInstanceOf[Int],to.asInstanceOf[Int])



//  def plus[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.plus(x,y)
//
//  def minus[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.minus(x,y)
//
//  def mul[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.times(x,y)
//
//  def div[A](x: A, y: A)(implicit fractional: Fractional[A]): A = fractional.div(x,y)

}
