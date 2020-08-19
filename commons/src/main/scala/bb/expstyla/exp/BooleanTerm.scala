package bb.exp
import it.unibo.tuprolog.core.{Term, Truth, Var}

case class BooleanTerm(value: Boolean) extends GenericTerm {
  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = if(value) "true" else "false"

  override def getBooleanValue: Boolean = value

  override def getTermValue: Term = Truth.of(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
