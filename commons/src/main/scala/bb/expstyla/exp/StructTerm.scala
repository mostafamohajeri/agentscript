package bb.expstyla.exp

import prolog.builtins.is
import prolog.io.TermParser
import prolog.terms.{Clause, Conj, Const, Disj, Fun, Real, Term, Var}

import scala.jdk.CollectionConverters._

case class StructTerm(functor: String, terms: Seq[GenericTerm] = Seq()) extends GenericTerm {

  override def bind_to(term: GenericTerm): Boolean = {
    term match {
      case StructTerm(f,params) =>
        if(f==functor && params.length == terms.length) {
          for (i <- params.indices) {
            if (!terms(i).bind_to(params(i)))
              false
          }
          return true
        }
        false
      case _ => false
    }

  }

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

  override def isClause: Boolean = terms.size == 2 && functor.equals(":-")

  def asTermList : List[Term] = List(terms.head.getTermValue, terms(1).getTermValue)

  def struct: Term = {
    if (terms.isEmpty) {
      functor match {
        case "cut" => prolog.builtins.cut(new Var())
        case _ => new Const(functor)
      }

    } else {
      functor match {
        case ","   => Conj.build(terms(0).getTermValue, terms(1).getTermValue)
        case ";"   => Disj.build(terms(0).getTermValue, terms(1).getTermValue)
        case "not" => new Fun("\\+", terms.map(t => t.getTermValue).toArray)
        case "\\=" => new Fun("=\\=", terms.map(t => t.getTermValue).toArray)
        case _     => TermParser.toFunBuiltin(new Fun(functor, terms.map(t => t.getTermValue).toArray))
      }
    }
  }

  def matchOnlyFunctorAndArity(functor:String,arity:Int) : Boolean = {
    (this.functor == functor && this.terms.size == arity)
  }

}

object StructTerm {

}
