package bb

import bb.expstyla.exp.{GenericTerm, StructTerm}

class StylaBeliefBaseFactory extends IBeliefBaseFactory {
  override def apply(): IBeliefBase[GenericTerm] = new BeliefBaseStylaConcurrent()
}
