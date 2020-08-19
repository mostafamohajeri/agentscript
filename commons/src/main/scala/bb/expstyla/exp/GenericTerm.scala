package bb.expstyla.exp

import bb.IGenericTerm
import prolog.builtins.{fail_, true_}
import prolog.terms.{Const, Fun, Real, SmallInt, Term, Truth, Var}

abstract class GenericTerm extends IGenericTerm {


  def +(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => IntTerm(l + r)
      case (DoubleTerm(l),IntTerm(r)) => DoubleTerm(l + r)
      case (DoubleTerm(l),DoubleTerm(r)) => DoubleTerm(l + r)
      case (IntTerm(l),DoubleTerm(r)) => DoubleTerm(l + r)
      case (StringTerm(l),r : GenericTerm) => StringTerm(l + r.getStringValue)
      case (l : GenericTerm, StringTerm(_)) => StringTerm(l.getStringValue + other.getStringValue)
      case (l : Any,r:Any) => throw new TypeException(f"Can not apply operator {+} to types ${l.getClass} and ${r.getClass}" )
    }
  }


  def -(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => IntTerm(l - r)
      case (DoubleTerm(l),IntTerm(r)) => DoubleTerm(l- r)
      case (DoubleTerm(l),DoubleTerm(r)) => DoubleTerm(l - r)
      case (IntTerm(l),DoubleTerm(r)) => DoubleTerm(l - r)
      case (l : Any,r:Any) => throw new TypeException(f"Can not apply operator {-} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def *(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => IntTerm(l * r)
      case (DoubleTerm(l),IntTerm(r)) => DoubleTerm(l * r)
      case (DoubleTerm(l),DoubleTerm(r)) => DoubleTerm(l * r)
      case (IntTerm(l),DoubleTerm(r)) => DoubleTerm(l * r)
      case (l : Any,r:Any) => throw new TypeException(f"Can not apply operator {*} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def /(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => DoubleTerm(l.toDouble / r.toDouble)
      case (DoubleTerm(l),IntTerm(r)) => DoubleTerm(l / r)
      case (DoubleTerm(l),DoubleTerm(r)) => DoubleTerm(l / r)
      case (IntTerm(l),DoubleTerm(r)) => DoubleTerm(l / r)
      case (l : Any,r:Any) => throw new TypeException(f"Can not apply operator {/} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def %(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => IntTerm(l % r)
      case (DoubleTerm(l),IntTerm(r)) => DoubleTerm(l % r)
      case (DoubleTerm(l),DoubleTerm(r)) => DoubleTerm(l % r)
      case (IntTerm(l),DoubleTerm(r)) => DoubleTerm(l % r)
      case (l : Any,r:Any) => throw new TypeException(f"Can not apply operator {*} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def >(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => BooleanTerm(l > r)
      case (DoubleTerm(l),IntTerm(r)) => BooleanTerm(l > r)
      case (DoubleTerm(l),DoubleTerm(r)) => BooleanTerm(l > r)
      case (IntTerm(l),DoubleTerm(r)) => BooleanTerm(l > r)
      case (StringTerm(l),StringTerm(r)) => BooleanTerm(l > r)
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l > r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {>} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def <(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => BooleanTerm(l < r)
      case (DoubleTerm(l),IntTerm(r)) => BooleanTerm(l < r)
      case (DoubleTerm(l),DoubleTerm(r)) => BooleanTerm(l < r)
      case (IntTerm(l),DoubleTerm(r)) => BooleanTerm(l < r)
      case (StringTerm(l),StringTerm(r)) => BooleanTerm(l < r)
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l < r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {<} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def >=(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => BooleanTerm(l >= r)
      case (DoubleTerm(l),IntTerm(r)) => BooleanTerm(l >= r)
      case (DoubleTerm(l),DoubleTerm(r)) => BooleanTerm( l>= r)
      case (IntTerm(l),DoubleTerm(r)) => BooleanTerm(l >= r)
      case (StringTerm(l),StringTerm(r)) => BooleanTerm(l >= r)
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l >= r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {>=} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def <=(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => BooleanTerm(l <= r)
      case (DoubleTerm(l),IntTerm(r)) => BooleanTerm(l <= r)
      case (DoubleTerm(l),DoubleTerm(r)) => BooleanTerm( l<= r)
      case (IntTerm(l),DoubleTerm(r)) => BooleanTerm(l <= r)
      case (StringTerm(l),StringTerm(r)) => BooleanTerm(l <= r)
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l <= r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {<=} to types ${l.getClass} and ${r.getClass}" )
    }
  }

  def equals(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (IntTerm(l),IntTerm(r)) => BooleanTerm(l == r)
      case (DoubleTerm(l),IntTerm(r)) => BooleanTerm(l == r)
      case (DoubleTerm(l),DoubleTerm(r)) => BooleanTerm( l == r)
      case (IntTerm(l),DoubleTerm(r)) => BooleanTerm(l == r)
      case (l: Any, r: Any) =>  BooleanTerm(this.equals(other))
    }
  }

  def ==(other: GenericTerm): GenericTerm =
         this.equals(other)

  def !=(other: GenericTerm): GenericTerm =
    !this.equals(other)


  def &&(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l && r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {&&} to types ${l.getClass} and ${r.getClass}" )
    }
  }


  def ||(other: GenericTerm): GenericTerm = {
    (this,other) match {
      case (BooleanTerm(l),BooleanTerm(r)) => BooleanTerm(l || r)
      case (l: Any, r: Any) => throw new TypeException(f"Can not apply operator {||} to types ${l.getClass} and ${r.getClass}" )
    }
  }



  def unary_! : GenericTerm = {
    (this) match {
      case BooleanTerm(l) => BooleanTerm(!l)
      case l: Any => throw new TypeException(f"Can not apply operator {not} to types ${l.getClass}" )
    }
  }

  def unary_- : GenericTerm = {
    (this) match {
      case IntTerm(l) => IntTerm(-l)
      case DoubleTerm(l) => DoubleTerm(-l)
      case l: Any => throw new TypeException(f"Can not apply operator {not} to types ${l.getClass}" )
    }
  }

  def holds : Boolean = {
    this match {
      case BooleanTerm(r) => r
      case a : Any => throw new TypeException(f"The type ${a.getClass} can not be cast to {Boolean}" )
    }
  }

  def getIntValue : Int
  def getDoubleValue : Double
  def getStringValue : String
  def getBooleanValue: Boolean
  def getTermValue : Term
  def getVarValue  : Var

}

object GenericTerm {
  def create(value : Int): GenericTerm = IntTerm(value)
  def create(value : Double): GenericTerm = DoubleTerm(value)
  def create(value : String): GenericTerm = StringTerm(value)
  def create(value : Boolean): GenericTerm = BooleanTerm(value)
  def create(value : Fun): GenericTerm = StructTerm(value.sym,value.args.map(a => create(a)))
  def create(value : Var): GenericTerm = VarTerm(value.v_name)
  def create(value : Term): GenericTerm =  {
    value match {
      case a: Const => create(a.sym)
      case true_() => create(true)
      case fail_() => create(false)
      case a: Fun => create(a)
      case a: SmallInt => create(a.getValue.toInt)
      case a: Real => create(a.getValue.toDouble)
      case a: Var => create(a)
    }
  }
}


