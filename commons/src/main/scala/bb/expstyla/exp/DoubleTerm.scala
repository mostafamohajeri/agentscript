package bb.expstyla.exp

import prolog.terms.{Real, Term, Var}

case class DoubleTerm(value: Double) extends GenericTerm {
  override def getIntValue: Int = value.intValue

  override def getDoubleValue: Double = value

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = new Real(value)

  override def getVarValue: Var = throw new TypeException()

  override def getObjectValue: Object = value.toString

  override def toString: String = value.toString
}
