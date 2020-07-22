//package infrastructure
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
//      case s: termutils.Cons[Any] =>
//        tuprolog.Struct.list(
//          s.map(toTerm).dropRight(1).toArray[Term].iterator.asJava
//          ,
//          toTerm(s.tail)
//        )
//      case t: Tuple[Any] => tuprolog.Struct.tuple(t.map(toTerm).toArray[Term].iterator.asJava)
//      case b: Boolean => tuprolog.Struct.truth(b)
//    }
//
//  }
//}
//
//object ExTermVisitor {
//
//  def   visitTermList(struct: Term) : Seq[Term] = {
//    struct.asInstanceOf[Struct]
//      .listStream
//      .iterator()
//      .asScala
//      .toSeq
//  }
//
//  def visit(term: Term): Any =
//    term match {
//      case num: tuprolog.Number => visit(num)
//      case v: tuprolog.Var => visit(v)
//      case st: tuprolog.Struct => visit(st)
//    }
//
//  def visit(struct: Struct): Any =
//    if (struct.isList) visitList(struct, struct.listStream.iterator().asScala.toSeq)
//    else if (struct.isCons) visitCons(struct, struct.unfoldedListStream.iterator().asScala.toSeq)
//    else if (struct.isTuple) visitTuple(struct, struct.unfoldedTupleStream.iterator().asScala.toSeq)
//    else if (struct.isSet) visitSet(struct, struct.unfoldedSetStream.iterator().asScala.toSeq)
//    else if (struct.isAtom) visitAtom(struct, struct.getName)
//    else visitCompound(struct, struct.getName, struct.getArity, struct.getArg)
//
//  private def visitCompound(struct: Struct, functor: String, arity: Int, args: (Int => Term)): Map[String, Any] =
//    Map(
//      ("fun" -> functor),
//      ("args" -> (Seq.range(0, arity).map(args).map((it: Term) => {
//        visit(it)
//      }) collect { case obj: Any => obj })))
//
//  private def visitSet(struct: Struct, items: Seq[Term]): Set[Any] =
//    items.map((it: Term) => visit(it)) collect { case term: Any => term } toSet
//
//  private def visitTuple(struct: Struct, items: Seq[Term]): Tuple[Any] =
//    (items.map((it: Term) => visit(it)) collect { case term: Any => term }).asInstanceOf[Tuple[Any]]
//
//  private def visitCons(struct: Struct, items: Seq[Term]): termutils.Cons[Any] =
//    (items.map((it: Term) => visit(it)) collect { case term: Any => term }).asInstanceOf[termutils.Cons[Any]]
//
//  private def visitList(struct: Struct, items: Seq[Term]): Seq[Any] =
//    items.map((it: Term) => visit(it)) collect { case term: Any => term }
//
//  private def visitAtom(struct: Struct, value: String): Any =
//    if (value == "true") true
//    else if (value == "false") false
//    else value
//
//  private def visitFreeVariable(variable: Var): Var =
//  //     Map("var" -> variable.getName)
//    Var.of(variable.getName)
//
//  private def visitBoundVariable(variable: Var, link: Term): Any = visit(link)
//
//  def visit(variable: Var): Any =
//    if (variable.isBound) visitBoundVariable(variable, variable.getTerm)
//    else visitFreeVariable(variable)
//
//    private def visit(number: tuprolog.Number): Any =
//    number match {
//      case num: tuprolog.Int => visit(num)
//      case num: tuprolog.Double => visit(num)
//      case num: tuprolog.Float => visit(num)
//      case num: tuprolog.Long => visit(num)
//    }
//
//  def visit(number: tuprolog.Double): Double = number.doubleValue()
//
//  def visit(number: tuprolog.Int): Any = number.intValue()
//
//  def visit(number: tuprolog.Long): Any = number.longValue()
//
//  def visit(number: tuprolog.Float): Any = number.floatValue()
//}
//
//// Temporary traits
//trait Tuple[T] extends Seq[T] {
//}
//
//trait Cons[T] extends Seq[T] {
//}