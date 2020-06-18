package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import com.google.common.collect.LinkedListMultimap;
import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.jactrio2.IModelBuilder;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IReferenceResolver;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.GeneratorDef;
import mil.navy.nrl.ncarai.jasm.program.Include;
import mil.navy.nrl.ncarai.jasm.program.PackageDef;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.Program;
import mil.navy.nrl.ncarai.jasm.program.VariableStatement;
import mil.navy.nrl.ncarai.jasm.scoping.GlobalImplicitImportManager;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.comment.CommentUtilities;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Chunks;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Element;
import org.jactr.io2.jactr.modelFragment.Import;
import org.jactr.io2.jactr.modelFragment.ModelFragment;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.PackageDeclaration;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.Parameters;
import org.jactr.io2.jactr.modelFragment.ParametersBlock;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class DefaultProceduralModelBuilder implements IModelBuilder {
  @Inject
  private Injector _injector;
  
  @Inject
  private GlobalImplicitImportManager _importManager;
  
  @Inject
  private ValidationUtilities _validationUtilities;
  
  private ModelFragment _parameters;
  
  private ModelFragment _procedural;
  
  private Collection<Production> _fallbackProductions;
  
  private Collection<Production> _nonFallbackProductions;
  
  @Override
  public Collection<ModelFragment> build(final Program program) {
    ArrayList<ModelFragment> _xblockexpression = null;
    {
      this._fallbackProductions = CollectionLiterals.<Production>newArrayList();
      this._nonFallbackProductions = CollectionLiterals.<Production>newArrayList();
      this.buildParameters(program);
      this.buildProcedural(program);
      this.process(program);
      _xblockexpression = CollectionLiterals.<ModelFragment>newArrayList(this._procedural, this._parameters);
    }
    return _xblockexpression;
  }
  
  protected ModelFragment buildParameters(final Program program) {
    ModelFragment _xblockexpression = null;
    {
      final ModelFragment model = ModelFragmentFactory.eINSTANCE.createModelFragment();
      model.setPackage(ModelFragmentFactory.eINSTANCE.createPackageDeclaration());
      PackageDeclaration _package = model.getPackage();
      String _target = program.getPackage().getTarget();
      String _plus = (_target + "Parameters");
      _package.setName(_plus);
      Import imp = ModelFragmentFactory.eINSTANCE.createImport();
      String _target_1 = program.getPackage().getTarget();
      String _plus_1 = (_target_1 + ".*");
      imp.setImportedNamespace(_plus_1);
      model.getPackage().getImports().add(imp);
      _xblockexpression = this._parameters = model;
    }
    return _xblockexpression;
  }
  
  protected ModelFragment buildProcedural(final Program program) {
    ModelFragment _xblockexpression = null;
    {
      final ModelFragment model = ModelFragmentFactory.eINSTANCE.createModelFragment();
      model.setPackage(ModelFragmentFactory.eINSTANCE.createPackageDeclaration());
      PackageDeclaration _package = model.getPackage();
      _package.setName(program.getPackage().getTarget());
      final Consumer<Import> _function = (Import imp) -> {
        model.getPackage().getImports().add(EcoreUtil.<Import>copy(imp));
      };
      program.getPackage().getImports().forEach(_function);
      final LinkedListMultimap<String, IEObjectDescription> packageTable = this._validationUtilities.packageTable(program);
      final Consumer<Include> _function_1 = (Include inc) -> {
        final String jasmNamespace = inc.getImportedNamespace().substring(0, inc.getImportedNamespace().lastIndexOf("."));
        EObject _object = this._validationUtilities.toObject(IterableExtensions.<IEObjectDescription>last(packageTable.get(jasmNamespace)), 
          program.eResource().getResourceSet());
        final PackageDef packageSpace = ((PackageDef) _object);
        Import imp = ModelFragmentFactory.eINSTANCE.createImport();
        String _target = packageSpace.getTarget();
        String _plus = (_target + ".*");
        imp.setImportedNamespace(_plus);
        model.getPackage().getImports().add(imp);
      };
      program.getPackage().getIncludes().forEach(_function_1);
      _xblockexpression = this._procedural = model;
    }
    return _xblockexpression;
  }
  
  protected boolean process(final Program program) {
    boolean _xblockexpression = false;
    {
      final IReferenceResolver resolver = this._injector.<IReferenceResolver>getInstance(IReferenceResolver.class);
      resolver.setProgram(program);
      final GeneratorDef generator = IterableExtensions.<GeneratorDef>last(EcoreUtil2.<GeneratorDef>getAllContentsOfType(program, GeneratorDef.class));
      final Consumer<Element> _function = (Element element) -> {
        if ((element instanceof Production)) {
          this._procedural.getPackage().getElements().add(element);
          ParametersBlock _parameters = ((Production)element).getParameters();
          boolean _tripleNotEquals = (_parameters != null);
          if (_tripleNotEquals) {
            this._fallbackProductions.add(((Production)element));
          } else {
            this._nonFallbackProductions.add(((Production)element));
          }
        }
        if ((element instanceof Chunks)) {
          this._procedural.getPackage().getElements().add(element);
          final Consumer<ChunkDef> _function_1 = (ChunkDef chunk) -> {
            resolver.add(chunk);
          };
          ((Chunks)element).getChunks().forEach(_function_1);
        }
        if ((element instanceof ChunkType)) {
          this._procedural.getPackage().getElements().add(element);
          resolver.add(((ChunkType)element));
        }
      };
      final ProceduralGenerationContext context = new ProceduralGenerationContext(this._injector, resolver, generator, _function);
      String namespace = context._goalManipulator.getImports().get(0);
      String _replace = namespace.substring(1, namespace.lastIndexOf(".")).replace("/", ".");
      String _plus = (_replace + ".*");
      namespace = _plus;
      this._importManager.accept(namespace);
      final Consumer<ChunkType> _function_1 = (ChunkType chunkType) -> {
        context.getConsumer().accept(EcoreUtil.<ChunkType>copy(chunkType));
      };
      EcoreUtil2.<ChunkType>getAllContentsOfType(program, ChunkType.class).forEach(_function_1);
      final Consumer<Chunks> _function_2 = (Chunks chunks) -> {
        final Chunks chunksCopy = EcoreUtil.<Chunks>copy(chunks);
        chunksCopy.setType(resolver.resolveChunkType(chunks.getType().getName()));
        context.getConsumer().accept(chunksCopy);
      };
      EcoreUtil2.<Chunks>getAllContentsOfType(program, Chunks.class).forEach(_function_2);
      final Consumer<Function> _function_3 = (Function func) -> {
        final ChunkType goalType = this.createGoalChunkType(func, context);
        context.getConsumer().accept(goalType);
      };
      program.getPackage().getFunctions().forEach(_function_3);
      context._functionTable.populate(program.getPackage().getFunctions());
      final Consumer<Function> _function_4 = (Function func) -> {
        context.newFunction(func);
        context._goalStateProvider.startBranch(func.getInstructions(), context);
        final ChunkType goalType = context._resolver.resolveChunkType(func.getName());
        final Pattern pattern = context._goalStateProvider.nextGoalState();
        final ConditionalSlot lastSlot = IterableExtensions.<ConditionalSlot>last(pattern.getSlots());
        final SimpleSlot slot = ProcGenUtilities.simpleSlot("stage");
        Value _value = slot.getValue();
        _value.setName(lastSlot.getValue().getName());
        goalType.getSlots().add(slot);
        context._productionSequencer.sequence(func.getInstructions(), context);
        context._goalStateProvider.endBranch();
      };
      program.getPackage().getFunctions().forEach(_function_4);
      final Consumer<String> _function_5 = (String str) -> {
        Import imp = ModelFragmentFactory.eINSTANCE.createImport();
        String _replace_1 = str.substring(1, str.lastIndexOf(".")).replace("/", ".");
        String _plus_1 = (_replace_1 + ".*");
        imp.setImportedNamespace(_plus_1);
        this._procedural.getPackage().getImports().add(imp);
      };
      context._goalManipulator.getImports().forEach(_function_5);
      Parameters pp = this.productionParameters(this._fallbackProductions, context._prioritizer.getFallbackPriority(), context);
      int _size = pp.getElements().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        CommentUtilities.comment(pp, "Provided to easily change fall through priorities");
        this._parameters.getPackage().getElements().add(pp);
      }
      pp = this.productionParameters(this._nonFallbackProductions, context._prioritizer.getDefaultPriority(), context);
      boolean _xifexpression = false;
      int _size_1 = pp.getElements().size();
      boolean _greaterThan_1 = (_size_1 > 0);
      if (_greaterThan_1) {
        boolean _xblockexpression_1 = false;
        {
          CommentUtilities.comment(pp, "Provided to easily change default priorities");
          _xblockexpression_1 = this._parameters.getPackage().getElements().add(pp);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected Parameters productionParameters(final Collection<Production> productions, final String defaultValue, final ProceduralGenerationContext context) {
    Parameters _xblockexpression = null;
    {
      final Parameters rtn = ModelFragmentFactory.eINSTANCE.createParameters();
      final Consumer<Production> _function = (Production p) -> {
        rtn.getElements().add(p.getName());
      };
      productions.forEach(_function);
      rtn.setParameters(ModelFragmentFactory.eINSTANCE.createParametersBlock());
      Parameter parameter = ModelFragmentFactory.eINSTANCE.createParameter();
      parameter.setName(context._prioritizer.getPriorityParameterName());
      parameter.setValue(ModelFragmentFactory.eINSTANCE.createValue());
      Value _value = parameter.getValue();
      _value.setString(defaultValue);
      rtn.getParameters().getParameter().add(parameter);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  protected ChunkType createGoalChunkType(final Function function, final ProceduralGenerationContext context) {
    ChunkType _xblockexpression = null;
    {
      final ChunkType chunkType = context._goalManipulator.newGoalType(function, context);
      this.addSlots(chunkType, function.getSlots(), context);
      this.addSlots(chunkType, function.getVariables(), context);
      CommentUtilities.comment(chunkType, ProcGenUtilities.text(function));
      context._consumer.accept(chunkType);
      _xblockexpression = chunkType;
    }
    return _xblockexpression;
  }
  
  protected void addSlots(final ChunkType type, final Collection<VariableStatement> list, final ProceduralGenerationContext context) {
    for (final VariableStatement variable : list) {
      {
        final SimpleSlot slot = ProcGenUtilities.simpleSlot(variable.getAssignment().getReference().getName());
        Value _value = variable.getAssignment().getAssignment().getValue();
        boolean _tripleNotEquals = (_value != null);
        if (_tripleNotEquals) {
          slot.setValue(EcoreUtil.<Value>copy(variable.getAssignment().getAssignment().getValue()));
        } else {
          Value _value_1 = slot.getValue();
          _value_1.setName("null");
        }
        type.getSlots().add(slot);
      }
    }
  }
}
