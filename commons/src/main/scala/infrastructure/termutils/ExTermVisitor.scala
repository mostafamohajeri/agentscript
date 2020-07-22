//package infrastructure.termutils
//
//import alice.tuprolog
//import alice.tuprolog.{Struct, Term, Var}
//
//import scala.jdk.CollectionConverters._
//import scala.language.postfixOps
//
//object ExTermVisitor {
//
//  def visitTermList(struct: Term): Seq[Term] = {
//    struct.asInstanceOf[Struct]
//      .listStream
//      .iterator()
//      .asScala
//      .toSeq
//  }
//
//  def visit(term: Term): Any =
//    term match {
//      case num: tuprolog.Number => visitNum(num)
//      case v: tuprolog.Var => visitVar(v)
//      case st: tuprolog.Struct => visitStruct(st)
//    }
//
//  def visitStruct(struct: Struct): Any =
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
//  private def visitCons(struct: Struct, items: Seq[Term]): Cons[Any] =
//    (items.map((it: Term) => visit(it)) collect { case term: Any => term }).asInstanceOf[Cons[Any]]
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
//  def visitVar(variable: Var): Any =
//    if (variable.isBound) visitBoundVariable(variable, variable.getTerm)
//    else visitFreeVariable(variable)
//
//  private def visitNum(number: tuprolog.Number): AnyVal =
//    number match {
//      case num: tuprolog.Int => visitInt(num)
//      case num: tuprolog.Double => visitDouble(num)
//      case num: tuprolog.Float => visitFloat(num)
//      case num: tuprolog.Long => visitLong(num)
//    }
//
//  def visitDouble(number: tuprolog.Double): Double = number.doubleValue()
//
//  def visitInt(number: tuprolog.Int): Int = number.intValue()
//
//  def visitLong(number: tuprolog.Long): Long = number.longValue()
//
//  def visitFloat(number: tuprolog.Float): Float = number.floatValue()
//}