package std

import bb.exp.{BooleanTerm, DoubleTerm, IntTerm, StringTerm}
import it.unibo.tuprolog.core.impl.IntegerImpl
import it.unibo.tuprolog.core.{Atom, Integer, Numeric, Real, Term, Truth}

object converters {

  //
  //  implicit def Real2Real(x: Real): Double =
  //    x.getDecimalValue.toDouble
//
//  implicit def Term2Real(x: Term): Double =
//    x.as[Real].getValue.toDouble

//    implicit def Term2Integer(x: Term): Int =
//      x.as[Integer].getIntValue.toInt
  //
//  implicit def Term2String(x: Term): String =
//    x.as[Atom].getValue.toString

//  implicit def Term2Boolean(x: Term): Boolean =
//    x.as[Truth].isTrue


  //
  //  implicit def Real2Integer(x: Real): Integer =
  //    Integer.of(x.getIntValue.toIntExact)
  //
//    implicit def Integer2Real(x: IntegerImpl): Real =
//      Real.of(x.getIntValue.toDouble)


  implicit def Native2Term(x: String): StringTerm =
    StringTerm(x)

  implicit def Native2Term(x: Boolean): BooleanTerm =
    BooleanTerm(x)

  implicit def Native2Term(x: Int): IntTerm =
    IntTerm(x)

  implicit def Native2Term(x: Double): DoubleTerm =
    DoubleTerm(x)


  implicit def Term2Native(x: StringTerm): StringTerm =
    x.getStringValue

  implicit def Term2Native(x: BooleanTerm): BooleanTerm =
    x.getBooleanValue

  implicit def Term2Native(x: IntTerm): IntTerm =
    x.getIntValue

  implicit def Term2Native(x: DoubleTerm): DoubleTerm =
    x.getDoubleValue


//  implicit def Int2Numeric(x: Int): Numeric =
//    Numeric.of(x)
//
//  implicit def Double2Numeric(x: Double): Numeric =
//    Numeric.of(x)
//
//  implicit def Bool2Truth(x: Boolean): Truth =
//    Truth.of(x)

}
