package infrastructure.termutils



import java.util

import it.unibo.tuprolog.core.{List, Struct, Term,Numeric,Atom,Integer,Real}
import it.unibo.tuprolog.serialize.JvmTermObjectifier

import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object ExTermVisitorKT {

  val ob : JvmTermObjectifier = new JvmTermObjectifier



  def valueOf(term: Term) : Any =
    term match {
      case a: Atom =>  a.getValue
      case numeric: Integer =>  numeric.getValue.toIntExact
      case numeric: Real => numeric.getValue.toDouble
      case struct: Struct => struct
      case _ => throw new RuntimeException("wtf")
    }

  def visitTermList(struct: Term): mutable.Buffer[Term] = {
    struct.as[List].toList.asScala
  }

  def visit(term: Term): AnyRef = ob.objectify(term)

}