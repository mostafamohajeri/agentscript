package bb.expstyla.exp

import prolog.builtins.{fail_, true_}
import prolog.terms.{Term, Var}


case class BooleanTerm(value: Boolean) extends GenericTerm {
  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = if(value) "true" else "false"

  override def getBooleanValue: Boolean = value

  override def getTermValue: Term = truth

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = value.toString

  override def getObjectValue: Object = value.toString

  lazy val truth = if(value) true_() else fail_()
}
