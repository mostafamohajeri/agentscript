//package bb
//
//import alice.tuprolog.lib.{BasicLibrary, ISOLibrary}
//import alice.tuprolog.{Prolog, Struct, Term, Theory, TheoryManager}
//import infrastructure.{ExTermVisitor, QueryResponse}
//
//import scala.jdk.CollectionConverters._
//import scala.language.postfixOps
//
//class BeliefBase(eng: Prolog) {
//  val engine = new Prolog(Array("alice.tuprolog.lib.BasicLibrary","alice.tuprolog.lib.ISOLibrary","alice.tuprolog.lib.OOLibrary"))
////  val engine: Prolog = eng
////  val engine = new Prolog()
//
//  var  totalQTime = 0L
//
//  var  totalMatchTime = 0
//
////  val engine = new Prolog()
////  val theoryManager : TheoryManager = new TheoryManager
////  theoryManager.initialize(engine)
////  theoryManager.initializeFromOther(engine.getTheoryManager)
//  var startExec = System.nanoTime()
//  var totalMessageMatchTime = 0
//  def assert(term :Term): Boolean = {
////    engine.solve(Struct.of(",",Struct.of("not",term),Struct.of("assertz",term))).isSuccess
//    query(Struct.of(",",Struct.of("not",term),Struct.of("assertz",term))).result
////    theoryManager.assertA(term.asInstanceOf[Struct],true,null,false)
////    println("after assert: " + theoryManager.getTheory(true))
////    println("asserted " + term)
//  }
//
//  def assert(serialize.terms :List[Term]): Unit = {
//    val clauses : Struct = Struct.list(serialize.terms.asJava)
//    serialize.terms.foreach(t => assert(t))
////    serialize.terms.foreach(term => theoryManager.assertA(term.asInstanceOf[Struct],true,null,false))
////    engine.addTheory(Theory.fromPrologList(clauses))
//  }
//
//  def retract(term :Term): Boolean = {
//    query(Struct.of(",",term,Struct.of("retractAll",term))).result
////    theoryManager.retract(term.asInstanceOf[Struct])
//    true
//  }
//
//  def query(term : Term) : QueryResponse = {
////    println("before q: " + theoryManager.getTheory(true))
//
//    val start = System.nanoTime()
//    val info = engine.solve(term)
////    val info = engine.solve(term)
////    println(engine.getTheory)
//
//
//
////    println(term)
//    if(info.isSuccess) {
//      val vars : Map[String, Term] = info.getBindingVars.asScala map { v => v.getName -> v.getTerm } toMap
//
//      totalQTime += ((System.nanoTime() - start))
//
//      QueryResponse(result = true,vars)
//    }
//    else
//      {
//        totalQTime += ((System.nanoTime() - start))
//        QueryResponse(result = false,Map[String,Term]())
//      }
//  }
//
//  def query() : QueryResponse = QueryResponse(result = true,Map[String,Term]())
//
//  def matchTerms (term1: Term,term2: Term): Boolean = {
//    val start = System.nanoTime()
//    val ret = term1.`match`(term2)
//    totalMatchTime += ((System.nanoTime() - start)).asInstanceOf[Int]
//    ret
//  }
//}
//
//
///*
//println("total query time = " + (executionContext.beliefBase.totalQTime.asInstanceOf[Double] / 1e9d))
//println("total exec time = " + ((System.nanoTime() - executionContext.beliefBase.start).asInstanceOf[Double] / 1e9d))
//println("total agent search time = " + ((coms.total_send_time).asInstanceOf[Double] / 1e9d))
//*/
