package HandyTests

import akka.actor.typed
import akka.actor.typed.ActorSystem
import bb.{BeliefBaseStylaConcurrent, StylaBeliefBaseFactory}
import bb.expstyla.exp.{FunTerm, GenericTerm, IntTerm, StringTerm, StructTerm, VarMap, VarTerm}
import infrastructure._

import scala.language.implicitConversions

object BBTest {


  def main(args: Array[String]): Unit = {

    var a = StructTerm("h",Seq(VarTerm("A")))
    var b = StructTerm("h",Seq(StringTerm("hello")))


    println(a.unify(b))
    println(a)


//    var bb = new StylaBeliefBaseFactory()()
//    bb.assertOne(StructTerm("a",Seq[GenericTerm](IntTerm(1))))
//    var vars = VarMap()
//    println(bb.query(
//
//      StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("a",Seq[GenericTerm](vars("N"))),StructTerm("is",Seq[GenericTerm](vars("S"),vars("N"))))),StructTerm("is",Seq[GenericTerm](vars("M"),FunTerm(() => addOne(vars("S")))))))
//
//    ))
  }

  def addOne(intTerm: GenericTerm) : IntTerm = {
    IntTerm(intTerm.getIntValue + 1)
  }

}
