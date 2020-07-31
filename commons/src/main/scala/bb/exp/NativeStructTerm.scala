package bb.exp

import it.unibo.tuprolog.core.{Term, Var}

case class NativeStructTerm(value: Term) extends GenericTerm {
  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = value

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
