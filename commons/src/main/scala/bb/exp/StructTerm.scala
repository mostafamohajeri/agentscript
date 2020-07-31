package bb.exp

import it.unibo.tuprolog.core.{Atom, Struct, Term, Var}
import scala.jdk.CollectionConverters._

case class StructTerm(functor: String,terms: Seq[GenericTerm]) extends GenericTerm {
  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String = functor + terms.map(t => t.getStringValue).mkString(",")

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = struct

  override def getVarValue: Var = throw new TypeException()

  override def toString: String = getStringValue

  lazy val struct = Struct.of(functor,terms.map(t => t.getTermValue).toList.asJava)

}
