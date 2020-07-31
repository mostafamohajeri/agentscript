package bb.exp
import it.unibo.tuprolog.core.{Integer, Term, Var}

case class IntTerm(value: Int) extends GenericTerm {
  override def getIntValue: Int = value

  override def getDoubleValue: Double = value.doubleValue()

  override def getStringValue: String = value.toString

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = Integer.of(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
