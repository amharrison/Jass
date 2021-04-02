package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import java.util.HashMap
import java.util.Map
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.BufferPattern
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.OutputStatement
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class OutputGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof OutputStatement) {

      val rtn = ModelFragmentFactory.eINSTANCE.createProduction
      rtn.name = computeBaseName(context) + "-Line" + ProcGenUtilities.lineNumber(inst)
      val goalBuffer = context._resolver.resolveBuffer("goal")

      var conditions = new HashMap<Buffer, BufferPattern> as Map<Buffer, BufferPattern>
      if (inst.bindings !== null) {
        conditions = ProcGenUtilities.mergePatterns(currentGoal, inst.bindings.bindings, context)

        // handle goal first
        rtn.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))
      } else {
        val bufferMatch = ProcGenUtilities.toMatch(currentGoal, context)
        bufferMatch.name = goalBuffer
        rtn.conditions.add(bufferMatch)
      }

      conditions.values.forEach [ bPattern |
        rtn.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
      ]

      val modify = ProcGenUtilities.toModify(nextGoal, context)
      modify.name = goalBuffer
      rtn.actions.add(modify)

      val output = ModelFragmentFactory.eINSTANCE.createOutput
      output.string = inst.string
      rtn.actions.add(output)

      // and any touches we might need
      conditions.keySet.forEach [ buffer |
        val touch = ModelFragmentFactory.eINSTANCE.createModify
        touch.name = buffer
        rtn.actions.add(touch)
      ]

      context.consumer.accept(rtn)

    }
  }

  override handles(Instruction instr) {
    instr instanceof OutputStatement
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-output"
  }

}
