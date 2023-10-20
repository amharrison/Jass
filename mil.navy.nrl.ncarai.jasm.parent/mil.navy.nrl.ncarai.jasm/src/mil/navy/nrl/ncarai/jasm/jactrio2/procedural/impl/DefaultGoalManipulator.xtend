package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl

import com.google.inject.Inject
import java.util.Collections
import java.util.Optional
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IGoalManipulator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.QualifiedName
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage

class DefaultGoalManipulator implements IGoalManipulator {
  @Inject extension ValidationUtilities

  override newGoal(ChunkType goalType, ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createAdd
    rtn.name = context._resolver.resolveBuffer("goal")
    rtn.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
    rtn.isa.type = goalType

    var slot = ProcGenUtilities.conditionalSlot("parent", Condition.EQUALS)
    slot.value.name = "=goal"
    rtn.slots.add(slot)

    rtn
  }

  override newGoal() {
    Optional.empty
  }

  override newGoalType(Function function, ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createChunkType
    rtn.name = function.name
    
    rtn.superType = getGoalType(function)
    rtn
  }

  override getImports() {
    Collections.singletonList("/jasm/procedural/Goal.jactr")
  }
  
  override getGoalType(EObject inTree) {
    val fqnGoal = QualifiedName.create("jasm", "procedural", "Goal", "Goal")
    val literals = getVisibileExportedObjectDescriptions(inTree,
      ModelFragmentPackage.Literals.CHUNK_TYPE)
    val chunkTypeDesc = literals.findFirst [ chunkType |
      chunkType.name.equals(fqnGoal)
    ]
    chunkTypeDesc.toObject(
      inTree.eResource.resourceSet) as ChunkType
  }

}
