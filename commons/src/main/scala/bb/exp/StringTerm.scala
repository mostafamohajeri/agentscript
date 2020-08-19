package bb.exp
import it.unibo.tuprolog.core.{Atom, Term, Var}

case class StringTerm(value: String) extends GenericTerm {
  override def getIntValue: Int = value.toInt

  override def getDoubleValue: Double = value.toDouble

  override def getStringValue: String = value

  override def getBooleanValue: Boolean = value.toBoolean

  override def getTermValue: Term = Atom.of(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
