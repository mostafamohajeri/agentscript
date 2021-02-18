package bb.expstyla.exp

import prolog.terms.{SmallInt, Term, Var}

case class IntTerm(value: Int) extends GenericTerm {
  override def getIntValue: Int = value

  override def getDoubleValue: Double = value.doubleValue()

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw TypeException()

  override def getTermValue: Term = SmallInt(value)

  override def getVarValue: Var = throw TypeException()

  override def getObjectValue: Object = value.toString

  override def toString: String = value.toString
}
