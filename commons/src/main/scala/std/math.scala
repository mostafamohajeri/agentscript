package std

import Numeric._
import bb.expstyla.exp.{DoubleTerm, GenericTerm, IntTerm}

import scala.jdk.CollectionConverters._

object math {

  def plus(double1: Double,double2: Double) : Double =
    double1 + double2

  def range(from: Any, to: Any) : List[Any] =
    List.range(from.asInstanceOf[Int],to.asInstanceOf[Int])

  def round(double: GenericTerm) : IntTerm =
    double match {
      case DoubleTerm(d) => IntTerm(Math.round(double.getDoubleValue).toInt)
      case IntTerm(d) => IntTerm(Math.round(double.getDoubleValue).toInt)
      case _ => throw new ClassCastException

    }

  def ceil(double: GenericTerm) : IntTerm =
    double match {
      case DoubleTerm(d) => IntTerm(Math.ceil(double.getDoubleValue).toInt)
      case IntTerm(d) => IntTerm(Math.ceil(double.getDoubleValue).toInt)
      case _ => throw new ClassCastException
    }




}
