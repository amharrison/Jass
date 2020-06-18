package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl

import java.util.Collection
import java.util.List
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IGoalStateProvider
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory
import org.eclipse.emf.ecore.EObject
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class DefaultGoalStateProvider implements IGoalStateProvider {

  Function _currentFunction;
  ChunkType _isa;
  Pattern _prior;
  Pattern _current;
  Pattern _next;
  int _position = -1;

  List<String> _stageChunkNames = newLinkedList
  List<List<String>> _stack = newArrayList
  List<Integer> _indexStack = newArrayList

  new(Function root, ProceduralGenerationContext context) {
    // System.err.println("new Function " + root.name)
    _stageChunkNames.add("starting")
    _stageChunkNames.add("completed")
    // System.err.println(_stageChunkNames)
    _currentFunction = root;
    _isa = context._resolver.resolveChunkType(root.name)

    _next = buildNext()
    nextState
  }

  override priorGoalState() {
    _prior
  }

  override currentGoalState() {
    _current
  }

  override nextGoalState() {
    _next
  }

  override nextState() {
    _prior = _current
    _current = _next
    _next = buildNext

//    if (_next !== null)
//      System.err.println("Current:" + _current.slots.last.value.name + " next:" + _next.slots.last.value.name)
  }

  def buildNext() {
    _position++
    if(_position >= _stageChunkNames.size) return null

    val stageChunkName = _stageChunkNames.get(_position)
    val goalType = _isa
    val pattern = ProgramFactory.eINSTANCE.createPattern

    pattern.type = goalType.name
    var stageSlot = ProcGenUtilities.conditionalSlot("stage", Condition.EQUALS)
    stageSlot.value.name = stageChunkName
    pattern.slots.add(stageSlot)

    pattern
  }

  override startBranch(Collection<? extends EObject> implicitBlock, ProceduralGenerationContext context) {
    // push the current stage names
//    System.err.println("startBranch")
    _stack.add(_stack.size(), _stageChunkNames)
    _indexStack.add(_indexStack.size(), _position)
    _stageChunkNames = newLinkedList
    _position = -1

    if (_current !== null)
      _stageChunkNames.add(_current.slots.last.value.name)

    buildStateChunks(implicitBlock, context)

    _stageChunkNames.add(_next.slots.last.value.name)

//    System.err.println(_stageChunkNames)
    _next = buildNext
    nextState
  }

  override startBranch(String rootStageName, ProceduralGenerationContext context, String ... stages) {
//    System.err.println("startBranch")
    _stack.add(_stack.size(), _stageChunkNames)
    _indexStack.add(_indexStack.size(), _position)
    _stageChunkNames = newLinkedList
    _position = -1

    if (_current !== null)
      _stageChunkNames.add(_current.slots.last.value.name)

    buildStateChunks(rootStageName, context, stages)

    _stageChunkNames.add(_next.slots.last.value.name)

//    System.err.println(_stageChunkNames)
    _next = buildNext
    nextState
  }

  override startBranch(Collection<? extends EObject> implicitBlock, String rootChunkName,
    ProceduralGenerationContext context, String ... stages) {
    // push the current stage names
//    System.err.println("startBranch")
    _stack.add(_stack.size(), _stageChunkNames)
    _indexStack.add(_indexStack.size(), _position)
    _stageChunkNames = newLinkedList
    _position = -1

    if (_current !== null)
      _stageChunkNames.add(_current.slots.last.value.name)

    buildStateChunks(implicitBlock, context)
    buildStateChunks(rootChunkName, context, stages)

    _stageChunkNames.add(_next.slots.last.value.name)

//    System.err.println(_stageChunkNames)
    _next = buildNext
    nextState
  }

  override endBranch() {
//    System.err.println("endBranch [" + _position + "/" + _stageChunkNames.size() + "]")
    _stageChunkNames = _stack.remove(_stack.size - 1)
    _position = _indexStack.remove(_indexStack.size - 1)

  // _next = buildNext
  }

  protected def buildStateChunks(Collection<? extends EObject> implicitBlock, ProceduralGenerationContext context) {
    val chunks = ModelFragmentFactory.eINSTANCE.createChunks
    chunks.type = context._resolver.resolveChunkType("chunk")
    implicitBlock.forEach [ inst |
      val name = _currentFunction.name + "_Line_" + ProcGenUtilities.lineNumber(inst)

      val chunkDef = ModelFragmentFactory.eINSTANCE.createChunkDef
      chunkDef.name = name
      chunks.chunks.add(chunkDef)
      _stageChunkNames.add(name)
    ]
    if (chunks.chunks.size() > 0)
      context._consumer.accept(chunks)
  }

  protected def buildStateChunks(String rootStageName, ProceduralGenerationContext context, String ... stages) {
    val chunks = ModelFragmentFactory.eINSTANCE.createChunks
    chunks.type = context._resolver.resolveChunkType("chunk")
    stages.forEach [ inst |
      var name = inst
      if (rootStageName.length != 0)
        name = rootStageName + "_" + inst

      val chunkDef = ModelFragmentFactory.eINSTANCE.createChunkDef
      chunkDef.name = name
      chunks.chunks.add(chunkDef)
      _stageChunkNames.add(name)
    ]
    if (chunks.chunks.size() > 0)
      context._consumer.accept(chunks)
  }

  override currentFunction() {
    _currentFunction
  }

}
