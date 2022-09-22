package bb.expstyla.exp

import prolog.terms.{Cons, Const, Term, Var}

case class ListTerm(items: Seq[GenericTerm]) extends GenericTerm {
  override def getIntValue: Int = throw TypeException()

  override def getDoubleValue: Double = throw TypeException()

  override def getStringValue: String = items.toString()

  override def getBooleanValue: Boolean = throw TypeException()

  override def getTermValue: Term = Cons.fromList(items.map(i => i.getTermValue).toList)

  override def getVarValue: Var = throw TypeException()

  override def getObjectValue: Object = items

  override def toString: String = throw TypeException()
}
