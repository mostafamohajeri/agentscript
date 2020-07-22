package infrastructure.termutils


import it.unibo.tuprolog.core.{List, Set, Struct, Term, Truth, Tuple, Var}
import it.unibo.tuprolog.serialize.{JvmTermDeobjectifier, JvmTermSerializer}

import scala.jdk.CollectionConverters._
import scala.language.postfixOps

object ExTermFactoryKT {

  val deobjectifier : JvmTermDeobjectifier  = new JvmTermDeobjectifier()

  def toTerm(obj: Any): Term = {
    obj match {
    case t: Term => t
    case d: Double => deobjectifier.deobjectify(d)
    case i: Int => deobjectifier.deobjectify(i)
    case f: Float => deobjectifier.deobjectify(f)
    case l: Long => deobjectifier.deobjectify(l)
    case string: String => deobjectifier.deobjectify(string)
    case v: Var => v
    case s: scala.collection.immutable.Set[_] => Set.of(s.map(toTerm).toList.asJava)
    case s: scala.Seq[_] => List.of(s.map(toTerm).toList.asJava)
    case s: scala.List[_] =>
      List.from(s.map(toTerm).asJava)
    case s: Cons[_] =>
      List.of(
        toTerm(s.map(toTerm).dropRight(1).toList),
        toTerm(s.tail)
      )
    case t: FakeTuple[_] => Tuple.of(t.map(toTerm _).toList.asJava)
    case b: Boolean => Truth.of(b)
    case _ => throw new RuntimeException("What type is this?")
    }

  }
}