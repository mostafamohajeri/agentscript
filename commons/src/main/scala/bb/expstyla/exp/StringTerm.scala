package bb.expstyla.exp

import prolog.terms.{Const, Real, Term, Var}

case class StringTerm(value: String) extends GenericTerm {
  override def getIntValue: Int = value.toInt

  override def ref: GenericTerm = super.ref

  override def getDoubleValue: Double = value.toDouble

  override def getStringValue: String = value

  override def getBooleanValue: Boolean = value.toBoolean

  override def getTermValue: Term = new Const(value)

  override def getVarValue: Var = throw TypeException()

  override def getObjectValue: Object = value

  override def toString: String = value.toString
}
