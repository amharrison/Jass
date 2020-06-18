package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IGoalStateProvider;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Chunks;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class DefaultGoalStateProvider implements IGoalStateProvider {
  private Function _currentFunction;
  
  private ChunkType _isa;
  
  private Pattern _prior;
  
  private Pattern _current;
  
  private Pattern _next;
  
  private int _position = (-1);
  
  private List<String> _stageChunkNames = CollectionLiterals.<String>newLinkedList();
  
  private List<List<String>> _stack = CollectionLiterals.<List<String>>newArrayList();
  
  private List<Integer> _indexStack = CollectionLiterals.<Integer>newArrayList();
  
  public DefaultGoalStateProvider(final Function root, final ProceduralGenerationContext context) {
    this._stageChunkNames.add("starting");
    this._stageChunkNames.add("completed");
    this._currentFunction = root;
    this._isa = context._resolver.resolveChunkType(root.getName());
    this._next = this.buildNext();
    this.nextState();
  }
  
  @Override
  public Pattern priorGoalState() {
    return this._prior;
  }
  
  @Override
  public Pattern currentGoalState() {
    return this._current;
  }
  
  @Override
  public Pattern nextGoalState() {
    return this._next;
  }
  
  @Override
  public void nextState() {
    this._prior = this._current;
    this._current = this._next;
    this._next = this.buildNext();
  }
  
  public Pattern buildNext() {
    Pattern _xblockexpression = null;
    {
      this._position++;
      int _size = this._stageChunkNames.size();
      boolean _greaterEqualsThan = (this._position >= _size);
      if (_greaterEqualsThan) {
        return null;
      }
      final String stageChunkName = this._stageChunkNames.get(this._position);
      final ChunkType goalType = this._isa;
      final Pattern pattern = ProgramFactory.eINSTANCE.createPattern();
      pattern.setType(goalType.getName());
      ConditionalSlot stageSlot = ProcGenUtilities.conditionalSlot("stage", Condition.EQUALS);
      Value _value = stageSlot.getValue();
      _value.setName(stageChunkName);
      pattern.getSlots().add(stageSlot);
      _xblockexpression = pattern;
    }
    return _xblockexpression;
  }
  
  @Override
  public void startBranch(final Collection<? extends EObject> implicitBlock, final ProceduralGenerationContext context) {
    this._stack.add(this._stack.size(), this._stageChunkNames);
    this._indexStack.add(this._indexStack.size(), Integer.valueOf(this._position));
    this._stageChunkNames = CollectionLiterals.<String>newLinkedList();
    this._position = (-1);
    if ((this._current != null)) {
      this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._current.getSlots()).getValue().getName());
    }
    this.buildStateChunks(implicitBlock, context);
    this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._next.getSlots()).getValue().getName());
    this._next = this.buildNext();
    this.nextState();
  }
  
  @Override
  public void startBranch(final String rootStageName, final ProceduralGenerationContext context, final String... stages) {
    this._stack.add(this._stack.size(), this._stageChunkNames);
    this._indexStack.add(this._indexStack.size(), Integer.valueOf(this._position));
    this._stageChunkNames = CollectionLiterals.<String>newLinkedList();
    this._position = (-1);
    if ((this._current != null)) {
      this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._current.getSlots()).getValue().getName());
    }
    this.buildStateChunks(rootStageName, context, stages);
    this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._next.getSlots()).getValue().getName());
    this._next = this.buildNext();
    this.nextState();
  }
  
  @Override
  public void startBranch(final Collection<? extends EObject> implicitBlock, final String rootChunkName, final ProceduralGenerationContext context, final String... stages) {
    this._stack.add(this._stack.size(), this._stageChunkNames);
    this._indexStack.add(this._indexStack.size(), Integer.valueOf(this._position));
    this._stageChunkNames = CollectionLiterals.<String>newLinkedList();
    this._position = (-1);
    if ((this._current != null)) {
      this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._current.getSlots()).getValue().getName());
    }
    this.buildStateChunks(implicitBlock, context);
    this.buildStateChunks(rootChunkName, context, stages);
    this._stageChunkNames.add(IterableExtensions.<ConditionalSlot>last(this._next.getSlots()).getValue().getName());
    this._next = this.buildNext();
    this.nextState();
  }
  
  @Override
  public void endBranch() {
    int _size = this._stack.size();
    int _minus = (_size - 1);
    this._stageChunkNames = this._stack.remove(_minus);
    int _size_1 = this._indexStack.size();
    int _minus_1 = (_size_1 - 1);
    this._position = (this._indexStack.remove(_minus_1)).intValue();
  }
  
  protected void buildStateChunks(final Collection<? extends EObject> implicitBlock, final ProceduralGenerationContext context) {
    final Chunks chunks = ModelFragmentFactory.eINSTANCE.createChunks();
    chunks.setType(context._resolver.resolveChunkType("chunk"));
    final Consumer<EObject> _function = (EObject inst) -> {
      String _name = this._currentFunction.getName();
      String _plus = (_name + "_Line_");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      final String name = (_plus + Integer.valueOf(_lineNumber));
      final ChunkDef chunkDef = ModelFragmentFactory.eINSTANCE.createChunkDef();
      chunkDef.setName(name);
      chunks.getChunks().add(chunkDef);
      this._stageChunkNames.add(name);
    };
    implicitBlock.forEach(_function);
    int _size = chunks.getChunks().size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      context._consumer.accept(chunks);
    }
  }
  
  protected void buildStateChunks(final String rootStageName, final ProceduralGenerationContext context, final String... stages) {
    final Chunks chunks = ModelFragmentFactory.eINSTANCE.createChunks();
    chunks.setType(context._resolver.resolveChunkType("chunk"));
    final Consumer<String> _function = (String inst) -> {
      String name = inst;
      int _length = rootStageName.length();
      boolean _notEquals = (_length != 0);
      if (_notEquals) {
        name = ((rootStageName + "_") + inst);
      }
      final ChunkDef chunkDef = ModelFragmentFactory.eINSTANCE.createChunkDef();
      chunkDef.setName(name);
      chunks.getChunks().add(chunkDef);
      this._stageChunkNames.add(name);
    };
    ((List<String>)Conversions.doWrapArray(stages)).forEach(_function);
    int _size = chunks.getChunks().size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      context._consumer.accept(chunks);
    }
  }
  
  @Override
  public Function currentFunction() {
    return this._currentFunction;
  }
}
