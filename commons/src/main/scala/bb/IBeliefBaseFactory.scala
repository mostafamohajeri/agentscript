package bb
import bb.expstyla.exp.GenericTerm

trait IBeliefBaseFactory {
  def apply(): IBeliefBase[GenericTerm]
}
