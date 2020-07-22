package infrastructure.termutils



import java.util

import it.unibo.tuprolog.core.{List, Struct, Term}
import it.unibo.tuprolog.serialize.JvmTermObjectifier

import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object ExTermVisitorKT {

  val ob : JvmTermObjectifier = new JvmTermObjectifier

  def visitTermList(struct: Term): mutable.Buffer[Term] = {
    struct.as[List].toList.asScala
  }

  def visit(term: Term): AnyRef = ob.objectify(term)

}