package bb.exp

import it.unibo.tuprolog.core.{Integer, Term, Var}

case class VarTerm(value: String) extends GenericTerm {

  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = "Var:"+value

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = variable

  override def getVarValue: Var = variable

  override def toString: String = value.toString

  lazy val variable = Var.of(value)
}