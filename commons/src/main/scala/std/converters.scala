package std

import bb.expstyla.exp.{BooleanTerm, DoubleTerm, GenericTerm, IntTerm, ObjectTerm, StringTerm}

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

  implicit def Native2Term(x: Long): IntTerm =
    IntTerm(x.toInt)

  implicit def Native2Term(x: Double): DoubleTerm =
    DoubleTerm(x)

  implicit def Term2String(x: StringTerm): String =
    x.getStringValue

  implicit def Term2Boolean(x: BooleanTerm): Boolean =
    x.getBooleanValue

  implicit def Term2Int(x: IntTerm): Int =
    x.getIntValue

  implicit def Term2Double(x: DoubleTerm): Double =
    x.getDoubleValue

  def asDouble(genericTerm: GenericTerm): Double   = genericTerm.getDoubleValue;
  def asInteger(genericTerm: GenericTerm): Int     = genericTerm.getIntValue;
  def asBoolean(genericTerm: GenericTerm): Boolean = genericTerm.getBooleanValue;
  def asString(genericTerm: GenericTerm): String   = genericTerm.getStringValue;
  def w[M](obj: M): ObjectTerm[M]                  = ObjectTerm(obj)
  def uw[M](obj: GenericTerm): M                   = obj.asInstanceOf[ObjectTerm[M]].getTHEValue

//  implicit def Int2Numeric(x: Int): Numeric =
//    Numeric.of(x)
//
//  implicit def Double2Numeric(x: Double): Numeric =
//    Numeric.of(x)
//
//  implicit def Bool2Truth(x: Boolean): Truth =
//    Truth.of(x)

}
