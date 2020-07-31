package bb.exp
import it.unibo.tuprolog.core.{Atom, Term, Var}

case class StringTerm(value: String) extends GenericTerm {
  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = value

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = Atom.of(value)

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString
}
