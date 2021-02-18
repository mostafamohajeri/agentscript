package bb.expstyla.exp

import prolog.builtins.is
import prolog.terms.{Conj, Fun, Real, Term, Var, Const,Disj}

import scala.jdk.CollectionConverters._

case class StructTerm(functor: String, terms: Seq[GenericTerm] = Seq()) extends GenericTerm {

  override def getIntValue: Int = throw new TypeException()

  override def getDoubleValue: Double = throw new TypeException()

  override def getStringValue: String =
    functor + (if (terms.nonEmpty) ("(" + terms.map(t => t.getStringValue).mkString(",") + ")")
               else "")

  override def getBooleanValue: Boolean = throw new TypeException()

  override def getTermValue: Term = struct

  override def getVarValue: Var = throw new TypeException()

  override def getObjectValue: Object = struct

  override def toString: String = getStringValue

  def struct: Term = {
    if (terms.size == 0)
      new Const(functor)
    else {
      functor match {
        case ","   => Conj.build(terms(0).getTermValue, terms(1).getTermValue)
        case ";"   => Disj.build(terms(0).getTermValue, terms(1).getTermValue)
        case "not" => new Fun("\\+", terms.map(t => t.getTermValue).toArray)
        case "\\=" => new Fun("=\\=", terms.map(t => t.getTermValue).toArray)
        case _     => new Fun(functor, terms.map(t => t.getTermValue).toArray)
      }
    }
  }

}
