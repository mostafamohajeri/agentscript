package bb.exp
import it.unibo.tuprolog.core.{Real, Term, Var}

case class DoubleTerm(value: Double) extends GenericTerm {
  override def getIntValue: Int = value.intValue

  override def getDoubleValue: Double = value

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = Real.of(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
