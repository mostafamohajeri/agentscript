
import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory, Var}

import scala.collection.JavaConverters._
import scala.collection.mutable

object BeliefBaseFactory {
    def apply(): BeliefBase = new BeliefBase
}

case class QueryResponse(result: Boolean,bindings: Map[String,Term])


class BeliefBase() {
  val engine: Prolog = new Prolog()

  def assert(term :Term): Unit = {
    engine.solve(Struct.of("assertz",term))
  }

  def assert(terms :List[Term]): Unit = {

    val clauses : Struct = Struct.list(terms.asJava)
    engine.addTheory(Theory.fromPrologList(clauses))
    println(engine.getTheory)
  }

  def retract(term :Term): Unit = {
    engine.solve(Struct.of("retractall",term))
  }

  def query(term : Term) : QueryResponse = {
    val info = engine.solve(term)
    if(info.isSuccess) {
      val vars : Map[String, Term] = info.getBindingVars.asScala map { v => v.getName -> v.getLink } toMap

      QueryResponse(result = true,vars)
    }
    else QueryResponse(result = false,Map[String,Term]())
  }



}


