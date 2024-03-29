package bb.expstyla.exp

import prolog.terms.{Const, Term, Var}

case class ObjectTerm[M](value: M) extends GenericTerm {

  override def getIntValue: Int = throw TypeException()

  override def getDoubleValue: Double = throw TypeException()

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw TypeException()

  override def getTermValue: Term = throw TypeException()

  override def getVarValue: Var = throw TypeException()

  override def getObjectValue: Object = value.asInstanceOf[Object]

  def getTHEValue: M = value

  override def toString: String = value.toString
}
