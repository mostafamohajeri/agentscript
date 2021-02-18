//package infrastructure.termutils
//
//
//import alice.tuprolog
//import alice.tuprolog.{Struct, Term, Var}
//
//import scala.jdk.CollectionConverters._
//import scala.language.postfixOps
//
//object ExTermFactory {
//
//  def toTerm(obj: Any): Term = {
//    obj match {
//      case t: Term => t
//      case d: Double => tuprolog.Double.of(d)
//      case i: Int => tuprolog.Int.of(i)
//      case f: Float => tuprolog.Float.of(f)
//      case l: Long => tuprolog.Long.of(l)
//      case string: String => tuprolog.Struct.atom(string)
//      case v: Var => v
//      case m: Map[String, Any] =>
//        tuprolog.Struct.of(m("fun").asInstanceOf[String],
//          m("args").asInstanceOf[Seq[Any]].map(toTerm).toArray[Term]
//        )
//      case s: Set[Any] => tuprolog.Struct.set(s.map(toTerm).toArray[Term].iterator.asJava)
//      case s: Seq[Any] => tuprolog.Struct.list(s.map(toTerm).toArray[Term].iterator.asJava)
//      case s: Array[Any] => tuprolog.Struct.list(s.map(toTerm).iterator.asJava)
//      case s: Cons[Any] =>
//        tuprolog.Struct.list(
//          s.map(toTerm).dropRight(1).toArray[Term].iterator.asJava
//          ,
//          toTerm(s.tail)
//        )
//      case t: Tuple[Any] => tuprolog.Struct.tuple(t.map(toTerm).toArray[Term].iterator.asJava)
//      case b: Boolean => tuprolog.Struct.truth(b)
//      case _ => throw new RuntimeException("What type is this?")
//    }
//
//  }
//}
