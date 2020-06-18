package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.AssignmentGenerator;
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement;
import mil.navy.nrl.ncarai.jasm.program.Bindings;
import mil.navy.nrl.ncarai.jasm.program.BufferDef;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Handler;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Mapping;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory;
import mil.navy.nrl.ncarai.jasm.program.RequestDef;
import mil.navy.nrl.ncarai.jasm.program.RequestStatement;
import mil.navy.nrl.ncarai.jasm.program.VariableReference;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.comment.CommentUtilities;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.IsaBlock;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Query;
import org.jactr.io2.jactr.modelFragment.Remove;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class BufferRequestGenerator implements IProductionGenerator {
  @Inject
  @Extension
  private ValidationUtilities _validationUtilities;
  
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof RequestStatement)) {
      final boolean needsBranch = (((((RequestStatement)inst).getMapping() == null) && (((RequestStatement)inst).getHandler() == null)) || ((((RequestStatement)inst).getMapping() != null) || (((RequestStatement)inst).getHandler() != null)));
      final boolean needsRequest = (needsBranch && (((RequestStatement)inst).getPatternReference() != null));
      final Pattern priorState = context._goalStateProvider.priorGoalState();
      if (needsBranch) {
        Mapping _mapping = ((RequestStatement)inst).getMapping();
        boolean _tripleNotEquals = (_mapping != null);
        if (_tripleNotEquals) {
          context._goalStateProvider.startBranch(((RequestStatement)inst).getMapping().getAssignments(), context);
        } else {
          context._goalStateProvider.startBranch(IterableExtensions.<ConditionalSlot>last(currentGoal.getSlots()).getValue().getName(), context, "no_map");
        }
      }
      final Pattern interimGoal = context._goalStateProvider.currentGoalState();
      final Pattern finalGoal = context._goalStateProvider.nextGoalState();
      if (needsRequest) {
        context.getConsumer().accept(this.buildRequestProduction(currentGoal, finalGoal, ((RequestStatement)inst), context));
      }
      Pattern entryPattern = ((Pattern) null);
      Mapping _mapping_1 = ((RequestStatement)inst).getMapping();
      boolean _tripleNotEquals_1 = (_mapping_1 != null);
      if (_tripleNotEquals_1) {
        entryPattern = this.buildMappingProductions(interimGoal, finalGoal, ((RequestStatement)inst), context);
      } else {
        VariableReference _patternReference = ((RequestStatement)inst).getPatternReference();
        boolean _tripleNotEquals_2 = (_patternReference != null);
        if (_tripleNotEquals_2) {
          entryPattern = this.buildNoMappingProduction(finalGoal, nextGoal, ((RequestStatement)inst), context);
          context._goalStateProvider.nextState();
        }
      }
      Handler _handler = ((RequestStatement)inst).getHandler();
      boolean _tripleNotEquals_3 = (_handler != null);
      if (_tripleNotEquals_3) {
        VariableReference _patternReference_1 = ((RequestStatement)inst).getPatternReference();
        boolean _tripleEquals = (_patternReference_1 == null);
        if (_tripleEquals) {
          entryPattern = priorState;
        }
        context.getConsumer().accept(this.buildRequestFailedProduction(entryPattern, nextGoal, ((RequestStatement)inst), context));
      }
      if (needsBranch) {
        context._goalStateProvider.endBranch();
      }
    }
  }
  
  public Production buildRequestFailedProduction(final Pattern currentGoal, final Pattern nextGoal, final RequestStatement request, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _name = context._goalStateProvider.currentFunction().getName();
      String _plus = (_name + "-request-failed-");
      String _name_1 = request.getBuffer().getName();
      String _plus_1 = (_plus + _name_1);
      String _plus_2 = (_plus_1 + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(request);
      final String pName = (_plus_2 + Integer.valueOf(_lineNumber));
      production.setName(pName);
      final Match goal = ProcGenUtilities.toMatch(currentGoal, context);
      goal.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goal);
      final Consumer<Match> _function = (Match match) -> {
        production.getConditions().add(match);
      };
      context._goalManipulator.newGoal().ifPresent(_function);
      final Query error = ModelFragmentFactory.eINSTANCE.createQuery();
      error.setName(request.getBuffer());
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS);
      Value _value = slot.getValue();
      _value.setName("error");
      error.getSlots().add(slot);
      production.getConditions().add(error);
      context._goalStateProvider.startBranch(request.getHandler().getBlock().getInstructions(), context);
      final Pattern ngp = context._goalStateProvider.nextGoalState();
      Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      production.getActions().add(modify);
      context._productionSequencer.sequence(request.getHandler().getBlock().getInstructions(), context);
      context._goalStateProvider.endBranch();
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Pattern buildMappingProductions(final Pattern currentGoal, final Pattern nextGoal, final RequestStatement request, final ProceduralGenerationContext context) {
    Pattern _xblockexpression = null;
    {
      final Function1<AssignmentStatement, Boolean> _function = (AssignmentStatement ass) -> {
        return Boolean.valueOf(((ass.getAssignment().getBuffer() == request.getBuffer()) || (((ass.getAssignment().getBuffer() == null) && (ass.getAssignment().getValue().getName() != null)) && 
          (request.getBuffer() == context._resolver.resolveBuffer(ass.getAssignment().getValue().getName())))));
      };
      final AssignmentStatement lastAssignment = IterableExtensions.<AssignmentStatement>findLast(request.getMapping().getAssignments(), _function);
      Pattern firstPattern = ((Pattern) null);
      EList<AssignmentStatement> _assignments = request.getMapping().getAssignments();
      for (final AssignmentStatement ass : _assignments) {
        {
          context._goalStateProvider.nextState();
          Pattern ngp = context._goalStateProvider.nextGoalState();
          if ((firstPattern == null)) {
            firstPattern = context._goalStateProvider.currentGoalState();
          }
          List<Production> _buildAssignmentProductions = this.buildAssignmentProductions(context._goalStateProvider.currentGoalState(), ngp, ass, request.getBuffer(), context);
          for (final Production production : _buildAssignmentProductions) {
            if ((production != null)) {
              if ((ass == lastAssignment)) {
                this.insertRemove(production, request.getBuffer());
                context._bufferStateManager.setContentType(request.getBuffer().getName(), null);
              }
              context.getConsumer().accept(production);
            }
          }
        }
      }
      _xblockexpression = firstPattern;
    }
    return _xblockexpression;
  }
  
  public Pattern buildNoMappingProduction(final Pattern currentGoal, final Pattern nextGoal, final RequestStatement request, final ProceduralGenerationContext context) {
    Pattern _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _name = context._goalStateProvider.currentFunction().getName();
      String _plus = (_name + "-noMap-");
      String _name_1 = request.getBuffer().getName();
      String _plus_1 = (_plus + _name_1);
      String _plus_2 = (_plus_1 + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(request);
      final String pName = (_plus_2 + Integer.valueOf(_lineNumber));
      rtn.setName(pName);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Match match = ProcGenUtilities.toMatch(currentGoal, context);
      match.setName(goalBuffer);
      rtn.getConditions().add(match);
      final BufferDef bufferDesc = this._validationUtilities.bufferDefinition(request.getBuffer().getName(), request);
      boolean hasResult = true;
      if ((bufferDesc != null)) {
        final RequestDef req = this.getMatchingRequestInfo(bufferDesc, request, context);
        if ((((req != null) && (!req.isWildcardResult())) && (req.getResultType() == null))) {
          hasResult = false;
        }
      }
      if (((bufferDesc == null) || hasResult)) {
        final Match requestVerification = ModelFragmentFactory.eINSTANCE.createMatch();
        requestVerification.setName(request.getBuffer());
        requestVerification.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        IsaBlock _isa = requestVerification.getIsa();
        String _name_2 = request.getBuffer().getName();
        String _plus_3 = ("=" + _name_2);
        _isa.setName(_plus_3);
        rtn.getConditions().add(requestVerification);
      }
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goalBuffer);
      rtn.getActions().add(modify);
      if (hasResult) {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(request.getBuffer());
        rtn.getActions().add(touch);
      }
      this.updateInferredContents(request, context);
      context.getConsumer().accept(rtn);
      _xblockexpression = currentGoal;
    }
    return _xblockexpression;
  }
  
  public boolean insertRemove(final Production production, final Buffer toRemove) {
    boolean _xblockexpression = false;
    {
      final Function1<Modify, Boolean> _function = (Modify modify) -> {
        Buffer _name = modify.getName();
        return Boolean.valueOf((_name == toRemove));
      };
      final Modify modify = IterableExtensions.<Modify>last(IterableExtensions.<Modify>filter(Iterables.<Modify>filter(production.getActions(), Modify.class), _function));
      boolean _xifexpression = false;
      if ((modify != null)) {
        boolean _xblockexpression_1 = false;
        {
          production.getActions().remove(modify);
          final Remove remove = ModelFragmentFactory.eINSTANCE.createRemove();
          remove.setName(toRemove);
          remove.getSlots().addAll(modify.getSlots());
          _xblockexpression_1 = production.getActions().add(remove);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public List<Production> buildAssignmentProductions(final Pattern currentGoal, final Pattern nextGoal, final AssignmentStatement assignment, final Buffer bufferToQuery, final ProceduralGenerationContext context) {
    Production _createSimpleAssignmentProduction = AssignmentGenerator.createSimpleAssignmentProduction(currentGoal, nextGoal, assignment, bufferToQuery, context);
    Production _createSimpleAssignmentNullProduction = AssignmentGenerator.createSimpleAssignmentNullProduction(currentGoal, nextGoal, assignment, bufferToQuery, context);
    return Collections.<Production>unmodifiableList(CollectionLiterals.<Production>newArrayList(_createSimpleAssignmentProduction, _createSimpleAssignmentNullProduction));
  }
  
  public Production buildRequestProduction(final Pattern currentGoal, final Pattern nextGoal, final RequestStatement request, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _name = context._goalStateProvider.currentFunction().getName();
      String _plus = (_name + "-request-");
      String _name_1 = request.getBuffer().getName();
      String _plus_1 = (_plus + _name_1);
      String _plus_2 = (_plus_1 + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(request);
      final String pName = (_plus_2 + Integer.valueOf(_lineNumber));
      production.setName(pName);
      Map<Buffer, BufferPattern> conditions = ((Map<Buffer, BufferPattern>) null);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      Pattern requestPattern = ((Pattern) null);
      ChunkDef requestLiteral = ((ChunkDef) null);
      String requestVariable = ((String) null);
      final ArrayList<BufferPattern> bindings = CollectionLiterals.<BufferPattern>newArrayList();
      Bindings _bindings = request.getBindings();
      boolean _tripleNotEquals = (_bindings != null);
      if (_tripleNotEquals) {
        bindings.addAll(request.getBindings().getBindings());
      }
      Buffer referenceBuffer = request.getPatternReference().getBuffer();
      if ((referenceBuffer == null)) {
        boolean _isPattern = this._validationUtilities.isPattern(request.getPatternReference().getName(), request);
        if (_isPattern) {
          requestPattern = this._validationUtilities.patternDefinition(request.getPatternReference().getName(), request);
        } else {
          boolean _isGoalSlot = this.isGoalSlot(request.getPatternReference().getName(), request, context);
          if (_isGoalSlot) {
            referenceBuffer = goalBuffer;
          } else {
            boolean _isBuffer = this._validationUtilities.isBuffer(request.getPatternReference().getName(), request);
            if (_isBuffer) {
              final BufferPattern bPattern = ProgramFactory.eINSTANCE.createBufferPattern();
              bPattern.setBuffer(context._resolver.resolveBuffer(request.getPatternReference().getName()));
              bPattern.setPattern(ProgramFactory.eINSTANCE.createPattern());
              Pattern _pattern = bPattern.getPattern();
              _pattern.setName("tmp");
              Pattern _pattern_1 = bPattern.getPattern();
              _pattern_1.setType(context._bufferStateManager.getContentType(bPattern.getBuffer().getName()).get().getName());
              String _name_2 = bPattern.getBuffer().getName();
              String _plus_3 = ("=" + _name_2);
              requestVariable = _plus_3;
              bindings.add(bPattern);
            } else {
              requestLiteral = context._resolver.resolveChunk(request.getPatternReference().getName());
            }
          }
        }
      }
      if (((referenceBuffer != null) && (request.getPatternReference().getName() != null))) {
        final BufferPattern bPattern_1 = ProgramFactory.eINSTANCE.createBufferPattern();
        bPattern_1.setBuffer(referenceBuffer);
        bPattern_1.setPattern(ProgramFactory.eINSTANCE.createPattern());
        Pattern _pattern_2 = bPattern_1.getPattern();
        _pattern_2.setName("tmp");
        Pattern _pattern_3 = bPattern_1.getPattern();
        _pattern_3.setType(context._bufferStateManager.getContentType(bPattern_1.getBuffer().getName()).get().getName());
        final ConditionalSlot slot = ProcGenUtilities.conditionalSlot(request.getPatternReference().getName(), Condition.EQUALS);
        String _name_3 = bPattern_1.getBuffer().getName();
        String _plus_4 = ("=" + _name_3);
        String _plus_5 = (_plus_4 + "-");
        String _name_4 = request.getPatternReference().getName();
        String _plus_6 = (_plus_5 + _name_4);
        requestVariable = _plus_6;
        Value _value = slot.getValue();
        _value.setName(requestVariable);
        bPattern_1.getPattern().getSlots().add(slot);
        bindings.add(bPattern_1);
      }
      conditions = ProcGenUtilities.mergePatterns(currentGoal, bindings, context);
      production.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern_2) -> {
        production.getConditions().add(ProcGenUtilities.toMatch(bPattern_2, context));
      };
      conditions.values().forEach(_function);
      final Query emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery();
      emptyNotBusy.setName(request.getBuffer());
      ConditionalSlot slot_1 = ProcGenUtilities.conditionalSlot("state", Condition.NOT);
      Value _value_1 = slot_1.getValue();
      _value_1.setName("busy");
      emptyNotBusy.getSlots().add(slot_1);
      boolean _isForced = request.isForced();
      boolean _not = (!_isForced);
      if (_not) {
        slot_1 = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS);
        Value _value_2 = slot_1.getValue();
        _value_2.setName("empty");
        emptyNotBusy.getSlots().add(slot_1);
      }
      production.getConditions().add(emptyNotBusy);
      if ((requestPattern != null)) {
        final Add addRequest = ProcGenUtilities.toAdd(requestPattern, context);
        addRequest.setName(request.getBuffer());
        production.getActions().add(addRequest);
      }
      if ((requestLiteral != null)) {
        final Add addRequest_1 = ModelFragmentFactory.eINSTANCE.createAdd();
        addRequest_1.setName(request.getBuffer());
        addRequest_1.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        IsaBlock _isa = addRequest_1.getIsa();
        _isa.setChunk(requestLiteral);
        production.getActions().add(addRequest_1);
      }
      if ((requestVariable != null)) {
        final Add addRequest_2 = ModelFragmentFactory.eINSTANCE.createAdd();
        addRequest_2.setName(request.getBuffer());
        addRequest_2.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        IsaBlock _isa_1 = addRequest_2.getIsa();
        _isa_1.setName(requestVariable);
        production.getActions().add(addRequest_2);
      }
      this.updateInferredContents(request, context);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      production.getActions().add(modify);
      if ((conditions != null)) {
        final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
          final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
          touch.setName(buffer);
          production.getActions().add(touch);
        };
        conditions.keySet().forEach(_function_1);
      }
      CommentUtilities.comment(production, ProcGenUtilities.text(request));
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof RequestStatement);
  }
  
  protected boolean isGoalSlot(final String slotName, final EObject inFunction, final ProceduralGenerationContext context) {
    final Function func = EcoreUtil2.<Function>getContainerOfType(inFunction, Function.class);
    final ChunkType chunkType = context._resolver.resolveChunkType(func.getName());
    final Function1<SimpleSlot, Boolean> _function = (SimpleSlot slot) -> {
      return Boolean.valueOf(slot.getName().equals(slotName));
    };
    SimpleSlot _findFirst = IterableExtensions.<SimpleSlot>findFirst(chunkType.getSlots(), _function);
    return (_findFirst != null);
  }
  
  protected RequestDef getMatchingRequestInfo(final BufferDef bufferDesc, final RequestStatement request, final ProceduralGenerationContext context) {
    final Function1<RequestDef, Boolean> _function = (RequestDef req) -> {
      return Boolean.valueOf(req.isWildcardRequest());
    };
    final RequestDef wildCardRequest = IterableExtensions.<RequestDef>findFirst(bufferDesc.getRequests(), _function);
    if ((wildCardRequest != null)) {
      return wildCardRequest;
    }
    final Pattern patternDef = this._validationUtilities.patternDefinition(request.getPatternReference().getName(), request);
    final ChunkType requestType = context._resolver.resolveChunkType(patternDef.getType());
    EList<RequestDef> _requests = bufferDesc.getRequests();
    for (final RequestDef req : _requests) {
      boolean _isWildcardRequest = req.isWildcardRequest();
      boolean _not = (!_isWildcardRequest);
      if (_not) {
        boolean _isA = this._validationUtilities.isA(requestType, req.getRequestType());
        if (_isA) {
          return req;
        }
      }
    }
    return null;
  }
  
  protected void updateInferredContents(final RequestStatement request, final ProceduralGenerationContext context) {
    String _cast = request.getCast();
    boolean _tripleNotEquals = (_cast != null);
    if (_tripleNotEquals) {
      context._bufferStateManager.setContentType(request.getBuffer().getName(), this._validationUtilities.getChunkType(request, request.getCast()));
    } else {
      ChunkType requestType = ((ChunkType) null);
      final Pattern patternDef = this._validationUtilities.patternDefinition(request.getPatternReference().getName(), request);
      if ((patternDef != null)) {
        requestType = context._resolver.resolveChunkType(patternDef.getType());
      } else {
        requestType = context._bufferStateManager.getContentType(request.getPatternReference().getName()).get();
      }
      final BufferDef bufferDesc = this._validationUtilities.bufferDefinition(request.getBuffer().getName(), request);
      if ((bufferDesc == null)) {
        context._bufferStateManager.setContentType(request.getBuffer().getName(), requestType);
      } else {
        final RequestDef req = this.getMatchingRequestInfo(bufferDesc, request, context);
        if ((req == null)) {
          String _name = request.getBuffer().getName();
          String _plus = ("Request of " + _name);
          String _plus_1 = (_plus + " with ");
          String _name_1 = requestType.getName();
          String _plus_2 = (_plus_1 + _name_1);
          String _plus_3 = (_plus_2 + 
            " has no matching result type. Check buffer meta defintions or your code.");
          throw new IllegalStateException(_plus_3);
        }
        boolean _isWildcardResult = req.isWildcardResult();
        if (_isWildcardResult) {
          context._bufferStateManager.setContentType(request.getBuffer().getName(), requestType);
        } else {
          ChunkType _requestType = req.getRequestType();
          boolean _tripleEquals = (_requestType == null);
          if (_tripleEquals) {
            context._bufferStateManager.setContentType(request.getBuffer().getName(), null);
          } else {
            context._bufferStateManager.setContentType(request.getBuffer().getName(), req.getResultType());
          }
        }
      }
    }
  }
}
