package bb.expstyla.exp

import prolog.terms.{Term, Var}

case class VarTerm(name: String) extends GenericTerm {


//  def apply = if(!this.value.eq(this)) this.value else this
  private var value : GenericTerm = this
  private var bound = false
  override def is_bound = bound
  override def bind_to(genericTerm: GenericTerm) = {this.value = genericTerm ; this.bound = true; true}

  override def ref: GenericTerm = if(!this.value.eq(this)) this.value.ref else this

  override def getIntValue: Int = if(!this.value.eq(this)) this.value.getIntValue else throw new TypeException()

  override def getDoubleValue: Double = if(!this.value.eq(this)) this.value.getDoubleValue else throw new TypeException()

  override def getStringValue: String = if(!this.value.eq(this)) this.value.getStringValue else "var:"+name

  override def getBooleanValue: Boolean = if(!this.value.eq(this)) this.value.getBooleanValue else throw new TypeException()

  override def getTermValue: Term =
    if(!this.value.eq(this))
      this.value.getTermValue
    else
      variable

  override def getVarValue: Var = variable

  override def getObjectValue: Object = if(!this.value.eq(this)) this.value.getObjectValue else name

  override def toString: String = if(!this.value.eq(this)) this.value.getStringValue else "var:"+name

  lazy val variable = new Var(name)
}
