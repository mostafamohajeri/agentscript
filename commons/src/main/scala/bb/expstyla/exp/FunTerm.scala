package bb.expstyla.exp

import prolog.terms.{FunBuiltin, Term, Var}

case class FunTerm(value:  ( () => GenericTerm) ) extends GenericTerm {

  override def getIntValue: Int = value().getIntValue

  override def getDoubleValue: Double = value().getDoubleValue

  override def getStringValue: String = value().getStringValue

  override def getBooleanValue: Boolean = value().getBooleanValue

  override def getTermValue: Term = throw new NotImplementedError()//(new FunBuiltin("call",1) {}).exec()

  override def getVarValue: Var = value().getVarValue

  override def getObjectValue: Object = getObjectValue

  override def toString: String = value.toString
}
