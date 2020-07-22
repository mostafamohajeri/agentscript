package std

import java.math.BigInteger
import java.util

import it.unibo.tuprolog.core.{Atom, List, Numeric, Substitution, Term, Var}
import it.unibo.tuprolog.solve.{ExecutionContext, SideEffectsBuilder, Signature}
import it.unibo.tuprolog.solve.exception.error.TypeError
import it.unibo.tuprolog.solve.primitive.Solve
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function3
import kotlin.sequences.Sequence
import org.gciatto.kt.math.BigInteger
import scala.jdk.CollectionConverters._
//import scala.reflect.runtime.universe
object prolog {

  object fun_creator {

    val num2strSignature = new Signature("num2str", 2,false)
    val str2numSignature = new Signature("str2num", 2,false)
    val replaceAllSignature = new Signature("replaceAll", 4,false)
    val concatSignature = new Signature("concat", 3,false)
    val betweenSignature = new Signature("between", 3,false)

    val null_pointer_to_a_side_effect_manager : kotlin.jvm.functions.Function1[_ >: it.unibo.tuprolog.solve.SideEffectsBuilder, kotlin.Unit] = (b: SideEffectsBuilder)  => kotlin.Unit.INSTANCE

    val gt: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)


      if (!arg1.isInstanceOf[Numeric]) {
        throw (new TypeError.Companion()).forGoal(
          request.getContext,
          request.getSignature,
          (new TypeError.Expected.Companion()).getNUMBER,
          arg1
        )
      }

      if (!arg2.isInstanceOf[Numeric]) {
        throw (new TypeError.Companion()).forGoal(
          request.getContext,
          request.getSignature,
          (new TypeError.Expected.Companion()).getNUMBER,
          arg2
        )
      }


      if (arg1.asInstanceOf[Numeric].getDecimalValue.compareTo(arg2.asInstanceOf[Numeric].getDecimalValue) > 0)
        (() => Seq(request.replySuccess(request.getContext.getSubstitution, null)).asJava.iterator())
      else
        () => Seq(request.replyFail(null)).asJava.iterator()

    })

    val replaceAll: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)
      val arg3: Term = request.getArguments.get(2)
      val arg4: Term = request.getArguments.get(3)

      val subs = Substitution.of(arg4.as[Var], Atom.of(arg1.as[Atom].getValue.replaceAll(arg2.as[Atom].getValue,arg3.as[Atom].getValue)))

      (() => Seq(request.replySuccess(subs, null)).asJava.iterator())

    })

    val concat: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)
      val arg3: Term = request.getArguments.get(2)

      val subs = Substitution.of(arg3.as[Var], Atom.of(arg1.as[Atom].getValue.concat(arg2.as[Atom].getValue)))

      (() => Seq(request.replySuccess(subs, null)).asJava.iterator())

    })

    val num2str: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)

//      println(arg1.as[Numeric].toString)

      val subs = Substitution.of(arg2.as[Var], Atom.of(arg1.as[Numeric].toString) )

      (() => Seq(request.replySuccess(subs, null)).asJava.iterator())

    })

    val str2num: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)


      val subs = Substitution.of(arg2.as[Var], Numeric.of(arg1.as[Atom].getValue) )

      (() => Seq(request.replySuccess(subs, null)).asJava.iterator())

    })

    val between: Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = ((request: Solve.Request[ExecutionContext]) => {
      val arg1: Term = request.getArguments.get(0)
      val arg2: Term = request.getArguments.get(1)
      val arg3: Term = request.getArguments.get(2)

      val it = new util.Iterator[Solve.Response] {
        val start = arg1.as[Numeric].getIntValue
        val end = arg2.as[Numeric].getIntValue

        val step = Numeric.of(1).getIntValue

        var current = start.minus(step)

        override def hasNext: Boolean = current.compareTo(end) < 0

        override def next(): Solve.Response = {
          current = current.plus(step)
          request.replySuccess(Substitution.of(
            arg3.as[Var], Numeric.of(current)),null)
        }
      }

      (() => it)

    })

  }

  def createFunc2(obj: AnyRef, fun: String ,arg1: AnyRef,arg2 : AnyRef) : Function1[Solve.Request[ExecutionContext], Sequence[Solve.Response]] = {

    val method = obj.getClass.getMethod(fun,arg1.getClass,arg2.getClass);

    val o =  println( method.invoke(obj,arg1,arg2) )



    ((request: Solve.Request[ExecutionContext]) => {


      if (arg1.asInstanceOf[Numeric].getDecimalValue.compareTo(arg2.asInstanceOf[Numeric].getDecimalValue) > 0)
        (() => Seq(request.replySuccess(request.getContext.getSubstitution, null)).asJava.iterator())
      else
        () => Seq(request.replyFail(null)).asJava.iterator()
    })

  }









}
