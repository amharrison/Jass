package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IGoalManipulator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.IsaBlock;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class DefaultGoalManipulator implements IGoalManipulator {
  @Inject
  @Extension
  private ValidationUtilities _validationUtilities;
  
  @Override
  public Add newGoal(final ChunkType goalType, final ProceduralGenerationContext context) {
    Add _xblockexpression = null;
    {
      final Add rtn = ModelFragmentFactory.eINSTANCE.createAdd();
      rtn.setName(context._resolver.resolveBuffer("goal"));
      rtn.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
      IsaBlock _isa = rtn.getIsa();
      _isa.setType(goalType);
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("parent", Condition.EQUALS);
      Value _value = slot.getValue();
      _value.setName("=goal");
      rtn.getSlots().add(slot);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  @Override
  public Optional<Match> newGoal() {
    return Optional.<Match>empty();
  }
  
  @Override
  public ChunkType newGoalType(final Function function, final ProceduralGenerationContext context) {
    ChunkType _xblockexpression = null;
    {
      final ChunkType rtn = ModelFragmentFactory.eINSTANCE.createChunkType();
      rtn.setName(function.getName());
      rtn.setSuperType(this.getGoalType(function));
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  @Override
  public List<String> getImports() {
    return Collections.<String>singletonList("/jasm/procedural/Goal.jactr");
  }
  
  @Override
  public ChunkType getGoalType(final EObject inTree) {
    ChunkType _xblockexpression = null;
    {
      final QualifiedName fqnGoal = QualifiedName.create("jasm", "procedural", "Goal", "Goal");
      final Set<IEObjectDescription> literals = this._validationUtilities.getVisibileExportedObjectDescriptions(inTree, 
        ModelFragmentPackage.Literals.CHUNK_TYPE);
      final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription chunkType) -> {
        return Boolean.valueOf(chunkType.getName().equals(fqnGoal));
      };
      final IEObjectDescription chunkTypeDesc = IterableExtensions.<IEObjectDescription>findFirst(literals, _function);
      EObject _object = this._validationUtilities.toObject(chunkTypeDesc, 
        inTree.eResource().getResourceSet());
      _xblockexpression = ((ChunkType) _object);
    }
    return _xblockexpression;
  }
}
