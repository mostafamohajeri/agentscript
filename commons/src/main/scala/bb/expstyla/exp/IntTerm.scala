package bb.expstyla.exp

import prolog.terms.{Real, SmallInt, Term, Var}

case class IntTerm(value: Int) extends GenericTerm {
  override def getIntValue: Int = value

  override def getDoubleValue: Double = value.doubleValue()

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = new SmallInt(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
