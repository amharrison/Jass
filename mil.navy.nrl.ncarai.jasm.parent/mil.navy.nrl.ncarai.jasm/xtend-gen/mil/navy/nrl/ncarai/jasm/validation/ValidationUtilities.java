package mil.navy.nrl.ncarai.jasm.validation;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.program.AsStatement;
import mil.navy.nrl.ncarai.jasm.program.BufferDef;
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Mapping;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.Program;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;
import mil.navy.nrl.ncarai.jasm.program.RequestDef;
import mil.navy.nrl.ncarai.jasm.program.RequestStatement;
import mil.navy.nrl.ncarai.jasm.program.VariableStatement;
import mil.navy.nrl.ncarai.jasm.program.WithAsStatement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Import;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class ValidationUtilities {
  @Inject
  private ResourceDescriptionsProvider _descriptors;
  
  @Inject
  private IResourceServiceProvider.Registry _resourceServiceProvider;
  
  @Inject
  private IQualifiedNameConverter _qualifiedNameConverter;
  
  @Inject
  private IContainer.Manager _containerManager;
  
  public ValidationUtilities() {
  }
  
  private boolean startsWith(final String string, final Set<String> startsWith) {
    for (final String start : startsWith) {
      boolean _startsWith = string.startsWith(start);
      if (_startsWith) {
        return true;
      }
    }
    return false;
  }
  
  private Function1<? super IEObjectDescription, Boolean> localAndImportFilter(final EObject root, final boolean excludeLocal) {
    Function1<? super IEObjectDescription, Boolean> _xblockexpression = null;
    {
      final TreeSet<String> startsWith = new TreeSet<String>();
      final Consumer<Import> _function = (Import imp) -> {
        String _importedNamespace = imp.getImportedNamespace();
        int _length = imp.getImportedNamespace().length();
        int _minus = (_length - 1);
        startsWith.add(_importedNamespace.substring(0, _minus));
      };
      EcoreUtil2.<Import>getAllContentsOfType(root, Import.class).forEach(_function);
      if ((!excludeLocal)) {
        if ((root instanceof Program)) {
          String _name = ((Program)root).getPackage().getName();
          String _plus = (_name + ".");
          startsWith.add(_plus);
        }
      }
      final Function1<IEObjectDescription, Boolean> _function_1 = (IEObjectDescription desc) -> {
        return Boolean.valueOf(this.startsWith(this._qualifiedNameConverter.toString(desc.getQualifiedName()), startsWith));
      };
      final Function1<? super IEObjectDescription, Boolean> handler = _function_1;
      _xblockexpression = handler;
    }
    return _xblockexpression;
  }
  
  public Set<IEObjectDescription> getVisibileExportedObjectDescriptions(final EObject o, final EClass type) {
    Set<IEObjectDescription> _xblockexpression = null;
    {
      final Function1<IContainer, Iterable<IEObjectDescription>> _function = (IContainer container) -> {
        return container.getExportedObjectsByType(type);
      };
      final Set<IEObjectDescription> all = IterableExtensions.<IEObjectDescription>toSet(Iterables.<IEObjectDescription>concat(ListExtensions.<IContainer, Iterable<IEObjectDescription>>map(this.getVisibileContainers(o), _function)));
      all.removeAll(this.getExportedObjectDescriptions(o, type));
      _xblockexpression = all;
    }
    return _xblockexpression;
  }
  
  protected Set<IEObjectDescription> getExportedObjectDescriptions(final EObject o, final EClass type) {
    Set<IEObjectDescription> _xblockexpression = null;
    {
      final IResourceServiceProvider resServiceProvider = this._resourceServiceProvider.getResourceServiceProvider(o.eResource().getURI());
      final IResourceDescription.Manager manager = resServiceProvider.getResourceDescriptionManager();
      final IResourceDescription description = manager.getResourceDescription(o.eResource());
      final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription desc) -> {
        EClass _eClass = desc.getEClass();
        return Boolean.valueOf((_eClass == type));
      };
      _xblockexpression = IterableExtensions.<IEObjectDescription>toSet(IterableExtensions.<IEObjectDescription>filter(description.getExportedObjects(), _function));
    }
    return _xblockexpression;
  }
  
  /**
   * visibility
   */
  private List<IContainer> getVisibileContainers(final EObject o) {
    List<IContainer> _xblockexpression = null;
    {
      final IResourceServiceProvider resServiceProvider = this._resourceServiceProvider.getResourceServiceProvider(o.eResource().getURI());
      final IResourceDescription.Manager manager = resServiceProvider.getResourceDescriptionManager();
      final IResourceDescription description = manager.getResourceDescription(o.eResource());
      final IResourceDescriptions index = this._descriptors.getResourceDescriptions(o.eResource());
      _xblockexpression = this._containerManager.getVisibleContainers(description, index);
    }
    return _xblockexpression;
  }
  
  protected LinkedListMultimap<String, IEObjectDescription> typeTable(final EObject inTree, final EClass literal) {
    LinkedListMultimap<String, IEObjectDescription> _xblockexpression = null;
    {
      final LinkedListMultimap<String, IEObjectDescription> map = LinkedListMultimap.<String, IEObjectDescription>create();
      final Set<IEObjectDescription> literals = this.getVisibileExportedObjectDescriptions(inTree, literal);
      final Consumer<IEObjectDescription> _function = (IEObjectDescription buffer) -> {
        map.put(buffer.getQualifiedName().getLastSegment(), buffer);
      };
      IterableExtensions.<IEObjectDescription>filter(literals, this.localAndImportFilter(EcoreUtil.getRootContainer(inTree), false)).forEach(_function);
      _xblockexpression = map;
    }
    return _xblockexpression;
  }
  
  public LinkedListMultimap<String, IEObjectDescription> packageTable(final EObject o) {
    LinkedListMultimap<String, IEObjectDescription> _xblockexpression = null;
    {
      final LinkedListMultimap<String, IEObjectDescription> map = LinkedListMultimap.<String, IEObjectDescription>create();
      Set<IEObjectDescription> types = this.getVisibileExportedObjectDescriptions(o, ModelFragmentPackage.Literals.PACKAGE_DECLARATION);
      final Consumer<IEObjectDescription> _function = (IEObjectDescription type) -> {
        map.put(type.getQualifiedName().toString(), type);
      };
      types.forEach(_function);
      types = this.getVisibileExportedObjectDescriptions(o, ProgramPackage.Literals.PACKAGE_DEF);
      final Consumer<IEObjectDescription> _function_1 = (IEObjectDescription type) -> {
        map.put(type.getQualifiedName().toString(), type);
      };
      types.forEach(_function_1);
      _xblockexpression = map;
    }
    return _xblockexpression;
  }
  
  public IEObjectDescription findInTypeTable(final EObject inTree, final EClass literal, final String name) {
    final Set<IEObjectDescription> literals = this.getVisibileExportedObjectDescriptions(inTree, literal);
    final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription desc) -> {
      return Boolean.valueOf(desc.getQualifiedName().getLastSegment().equals(name));
    };
    return IterableExtensions.<IEObjectDescription>findFirst(IterableExtensions.<IEObjectDescription>filter(literals, this.localAndImportFilter(EcoreUtil.getRootContainer(inTree), false)), _function);
  }
  
  public IEObjectDescription findExactInTypeTable(final EObject inTree, final EClass literal, final QualifiedName fqn) {
    final Set<IEObjectDescription> literals = this.getVisibileExportedObjectDescriptions(inTree, literal);
    final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription desc) -> {
      return Boolean.valueOf(desc.getQualifiedName().equals(fqn));
    };
    return IterableExtensions.<IEObjectDescription>findFirst(literals, _function);
  }
  
  public BufferDef bufferDefinition(final String bufferName, final EObject inTree) {
    final IEObjectDescription desc = this.findExactInTypeTable(inTree, ProgramPackage.Literals.BUFFER_DEF, 
      QualifiedName.create("jasm", "procedural", "Buffers", bufferName));
    if ((desc != null)) {
      EObject _object = this.toObject(desc, inTree.eResource().getResourceSet());
      return ((BufferDef) _object);
    }
    return null;
  }
  
  public LinkedListMultimap<String, IEObjectDescription> functionTable(final EObject inTree) {
    return this.typeTable(inTree, ProgramPackage.Literals.FUNCTION);
  }
  
  public boolean isFunction(final String functionName, final EObject inTree) {
    IEObjectDescription _findInTypeTable = this.findInTypeTable(inTree, ProgramPackage.Literals.FUNCTION, functionName);
    return (_findInTypeTable != null);
  }
  
  public LinkedListMultimap<String, IEObjectDescription> chunkTypeTable(final EObject inTree) {
    return this.typeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE);
  }
  
  public boolean isChunkType(final String chunkTypeName, final EObject inTree) {
    IEObjectDescription _findInTypeTable = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE, chunkTypeName);
    return (_findInTypeTable != null);
  }
  
  public LinkedListMultimap<String, IEObjectDescription> bufferTable(final EObject inTree) {
    return this.typeTable(inTree, ModelFragmentPackage.Literals.BUFFER);
  }
  
  public boolean isBuffer(final String bufferName, final EObject inTree) {
    IEObjectDescription _findInTypeTable = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.BUFFER, bufferName);
    return (_findInTypeTable != null);
  }
  
  public LinkedListMultimap<String, IEObjectDescription> chunkTable(final EObject inTree) {
    return this.typeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF);
  }
  
  public ChunkDef chunkDefinition(final String chunkName, final EObject inTree) {
    final IEObjectDescription rtn = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF, chunkName);
    if ((rtn != null)) {
      EObject _object = this.toObject(rtn, inTree.eResource().getResourceSet());
      return ((ChunkDef) _object);
    }
    return ((ChunkDef) null);
  }
  
  public boolean isChunk(final String chunkName, final EObject inTree) {
    IEObjectDescription _findInTypeTable = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF, chunkName);
    return (_findInTypeTable != null);
  }
  
  public LinkedListMultimap<String, IEObjectDescription> patternTable(final EObject inTree) {
    return this.typeTable(inTree, ProgramPackage.Literals.PATTERN);
  }
  
  public boolean isPattern(final String patternName, final EObject inTree) {
    IEObjectDescription _findInTypeTable = this.findInTypeTable(inTree, ProgramPackage.Literals.PATTERN, patternName);
    return (_findInTypeTable != null);
  }
  
  public Pattern patternDefinition(final String patternName, final EObject inTree) {
    final IEObjectDescription rtn = this.findInTypeTable(inTree, ProgramPackage.Literals.PATTERN, patternName);
    if ((rtn != null)) {
      EObject _object = this.toObject(rtn, inTree.eResource().getResourceSet());
      return ((Pattern) _object);
    }
    return ((Pattern) null);
  }
  
  public EObject toObject(final IEObjectDescription descriptor, final ResourceSet resourceSet) {
    return EcoreUtil.resolve(descriptor.getEObjectOrProxy(), resourceSet);
  }
  
  protected TreeSet<String> _slotsOf(final Function function) {
    TreeSet<String> _xblockexpression = null;
    {
      final TreeSet<String> rtn = CollectionLiterals.<String>newTreeSet(null);
      final Consumer<VariableStatement> _function = (VariableStatement slot) -> {
        rtn.add(slot.getAssignment().getReference().getName());
      };
      function.getSlots().forEach(_function);
      final Consumer<VariableStatement> _function_1 = (VariableStatement slot) -> {
        rtn.add(slot.getAssignment().getReference().getName());
      };
      function.getVariables().forEach(_function_1);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  protected TreeSet<String> _slotsOf(final ChunkType chunkType) {
    TreeSet<String> _xblockexpression = null;
    {
      final TreeSet<String> rtn = CollectionLiterals.<String>newTreeSet(null);
      ChunkType ct = chunkType;
      while ((ct != null)) {
        {
          final Consumer<SimpleSlot> _function = (SimpleSlot slot) -> {
            rtn.add(slot.getName());
          };
          ct.getSlots().forEach(_function);
          ct = ct.getSuperType();
        }
      }
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  protected TreeSet<String> _slotsOf(final Pattern pattern) {
    TreeSet<String> _xblockexpression = null;
    {
      final TreeSet<String> rtn = CollectionLiterals.<String>newTreeSet(null);
      final Consumer<ConditionalSlot> _function = (ConditionalSlot slot) -> {
        rtn.add(slot.getName());
      };
      pattern.getSlots().forEach(_function);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public Iterable<? extends EObject> nodesOfTypeBefore(final EObject reference, final Class<? extends EObject> clazz) {
    final Function function = IterableExtensions.<Function>last(Iterables.<Function>filter(EcoreUtil2.getAllContainers(reference), Function.class));
    final int referenceOffset = NodeModelUtils.findActualNodeFor(reference).getOffset();
    final Function1<EObject, Boolean> _function = (EObject object) -> {
      int _offset = NodeModelUtils.findActualNodeFor(object).getOffset();
      return Boolean.valueOf((_offset < referenceOffset));
    };
    return Iterables.filter(IterableExtensions.filter(EcoreUtil2.getAllContentsOfType(function, clazz), _function), clazz);
  }
  
  public Iterable<? extends EObject> nodesOfTypeAfter(final EObject reference, final Class<? extends EObject> clazz) {
    final Function function = IterableExtensions.<Function>last(Iterables.<Function>filter(EcoreUtil2.getAllContainers(reference), Function.class));
    final int referenceOffset = NodeModelUtils.findActualNodeFor(reference).getOffset();
    final Function1<EObject, Boolean> _function = (EObject object) -> {
      int _offset = NodeModelUtils.findActualNodeFor(object).getOffset();
      return Boolean.valueOf((_offset > referenceOffset));
    };
    return Iterables.filter(IterableExtensions.filter(EcoreUtil2.getAllContentsOfType(function, clazz), _function), clazz);
  }
  
  public Iterable<? extends EObject> nodesOfTypeBetween(final EObject earlier, final EObject later, final Class<? extends EObject> clazz) {
    final Function function = IterableExtensions.<Function>last(Iterables.<Function>filter(EcoreUtil2.getAllContainers(earlier), Function.class));
    final int early = NodeModelUtils.findActualNodeFor(earlier).getOffset();
    final int late = NodeModelUtils.findActualNodeFor(later).getOffset();
    final Function1<EObject, Boolean> _function = (EObject object) -> {
      boolean _xblockexpression = false;
      {
        final int offset = NodeModelUtils.findActualNodeFor(object).getOffset();
        _xblockexpression = ((offset > early) && (offset < late));
      }
      return Boolean.valueOf(_xblockexpression);
    };
    return Iterables.filter(IterableExtensions.filter(EcoreUtil2.getAllContentsOfType(function, clazz), _function), clazz);
  }
  
  /**
   * proxy chunktype describing a function
   */
  public ChunkType asChunkType(final Function function) {
    ChunkType _xblockexpression = null;
    {
      final ChunkType chunkType = ModelFragmentFactory.eINSTANCE.createChunkType();
      chunkType.setName(function.getName());
      this.addSlots(chunkType, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("stage", "parent", "return")));
      this.addSlots(chunkType, function.getSlots());
      this.addSlots(chunkType, function.getVariables());
      _xblockexpression = chunkType;
    }
    return _xblockexpression;
  }
  
  public void addSlots(final ChunkType type, final Iterable<String> itr) {
    for (final String s : itr) {
      {
        final SimpleSlot slot = ProcGenUtilities.simpleSlot(s);
        Value _value = slot.getValue();
        _value.setName("null");
        type.getSlots().add(slot);
      }
    }
  }
  
  public void addSlots(final ChunkType type, final Collection<VariableStatement> list) {
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
  
  public boolean isA(final ChunkType query, final ChunkType potentialAncestor) {
    ChunkType q = query;
    while ((q != null)) {
      {
        String _name = potentialAncestor.getName();
        String _name_1 = q.getName();
        boolean _equals = Objects.equal(_name, _name_1);
        if (_equals) {
          return true;
        }
        q = q.getSuperType();
      }
    }
    return false;
  }
  
  public ChunkType getChunkType(final EObject inTree, final String chunkTypeName) {
    ChunkType _xblockexpression = null;
    {
      ChunkType type = ((ChunkType) null);
      IEObjectDescription descriptor = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE, chunkTypeName);
      if ((descriptor != null)) {
        EObject _object = this.toObject(descriptor, inTree.eResource().getResourceSet());
        type = ((ChunkType) _object);
      } else {
        descriptor = this.findInTypeTable(inTree, ProgramPackage.Literals.FUNCTION, chunkTypeName);
        if ((descriptor != null)) {
          EObject _object_1 = this.toObject(descriptor, inTree.eResource().getResourceSet());
          final Function function = ((Function) _object_1);
          type = this.asChunkType(function);
        }
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }
  
  protected ChunkType _inferBufferContentsFrom(final Function function) {
    return this.getChunkType(function, function.getName());
  }
  
  protected ChunkType _inferBufferContentsFrom(final RequestStatement statement) {
    ChunkType _xblockexpression = null;
    {
      ChunkType type = this.getChunkType(statement, statement.getCast());
      if (((type == null) && (statement.getPatternReference() != null))) {
        Pattern pattern = ((Pattern) null);
        if (((statement.getPatternReference().getName() != null) && this.isPattern(statement.getPatternReference().getName(), statement))) {
          pattern = this.patternDefinition(statement.getPatternReference().getName(), statement);
          type = this.getChunkType(statement, pattern.getType());
        } else {
          if (((statement.getPatternReference().getName() != null) && this.isBuffer(statement.getPatternReference().getName(), statement))) {
          }
        }
        final BufferDef def = this.bufferDefinition(statement.getBuffer().getName(), statement);
        if ((def != null)) {
          final Function1<RequestDef, Boolean> _function = (RequestDef request) -> {
            return Boolean.valueOf(request.isWildcardRequest());
          };
          final RequestDef wildCardRequest = IterableExtensions.<RequestDef>findFirst(def.getRequests(), _function);
          if ((wildCardRequest != null)) {
            return type;
          }
          if ((pattern != null)) {
            EList<RequestDef> _requests = def.getRequests();
            for (final RequestDef request : _requests) {
              boolean _isWildcardRequest = request.isWildcardRequest();
              boolean _not = (!_isWildcardRequest);
              if (_not) {
                final ChunkType refType = this.getChunkType(statement, pattern.getType());
                boolean _isA = this.isA(refType, request.getRequestType());
                if (_isA) {
                  type = request.getResultType();
                }
              }
            }
          }
        }
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }
  
  protected ChunkType _inferBufferContentsFrom(final ForLoopStatement statement) {
    ChunkType _xblockexpression = null;
    {
      ChunkType type = this.getChunkType(statement, statement.getInitial().getType());
      final BufferDef def = this.bufferDefinition(statement.getForBuffer().getName(), statement);
      if ((def != null)) {
        final Function1<RequestDef, Boolean> _function = (RequestDef request) -> {
          return Boolean.valueOf(request.isWildcardRequest());
        };
        final RequestDef wildCardRequest = IterableExtensions.<RequestDef>findFirst(def.getRequests(), _function);
        if ((wildCardRequest != null)) {
          return type;
        }
        EList<RequestDef> _requests = def.getRequests();
        for (final RequestDef request : _requests) {
          boolean _isWildcardRequest = request.isWildcardRequest();
          boolean _not = (!_isWildcardRequest);
          if (_not) {
            final ChunkType refType = this.getChunkType(statement, statement.getInitial().getType());
            boolean _isA = this.isA(refType, request.getRequestType());
            if (_isA) {
              type = request.getResultType();
            }
          }
        }
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }
  
  protected ChunkType _inferBufferContentsFrom(final Instruction statement) {
    return ((ChunkType) null);
  }
  
  /**
   * from this point, use the containing forEach & (at most one) open (no-mapping)
   * request to build the initial estimated contents. We then append the set of
   * open requests before this instruction.
   * Note: we do not respect remove
   */
  public HashMap<Buffer, ChunkType> inferBufferContents(final EObject inTree) {
    HashMap<Buffer, ChunkType> _xblockexpression = null;
    {
      final HashMap<Buffer, ChunkType> rtnMap = CollectionLiterals.<Buffer, ChunkType>newHashMap();
      try {
        final Consumer<EObject> _function = (EObject container) -> {
          if ((container instanceof RequestStatement)) {
            boolean _containsKey = rtnMap.containsKey(((RequestStatement)container).getBuffer());
            boolean _not = (!_containsKey);
            if (_not) {
              rtnMap.putIfAbsent(((RequestStatement)container).getBuffer(), this.inferBufferContentsFrom(container));
            }
          } else {
            if ((container instanceof ForLoopStatement)) {
              rtnMap.putIfAbsent(((ForLoopStatement)container).getForBuffer(), this.inferBufferContentsFrom(container));
            } else {
              if ((container instanceof Function)) {
                WithAsStatement _assumedContents = ((Function)container).getAssumedContents();
                boolean _tripleNotEquals = (_assumedContents != null);
                if (_tripleNotEquals) {
                  final Consumer<AsStatement> _function_1 = (AsStatement assumed) -> {
                    final ChunkType chunkType = this.getChunkType(container, assumed.getType());
                    if ((chunkType != null)) {
                      rtnMap.putIfAbsent(assumed.getBuffer(), chunkType);
                    }
                  };
                  ((Function)container).getAssumedContents().getAssumed().forEach(_function_1);
                }
                final IEObjectDescription goalBufferDesc = this.findInTypeTable(inTree, ModelFragmentPackage.Literals.BUFFER, "goal");
                if ((goalBufferDesc != null)) {
                  EObject _object = this.toObject(goalBufferDesc, inTree.eResource().getResourceSet());
                  rtnMap.put(((Buffer) _object), 
                    this.inferBufferContentsFrom(container));
                }
              }
            }
          }
        };
        EcoreUtil2.getAllContainers(inTree).forEach(_function);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          e.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final Function1<RequestStatement, Boolean> _function = (RequestStatement request) -> {
        Mapping _mapping = request.getMapping();
        return Boolean.valueOf((_mapping == null));
      };
      final Consumer<RequestStatement> _function_1 = (RequestStatement noMapRequest) -> {
        rtnMap.putIfAbsent(noMapRequest.getBuffer(), this.inferBufferContentsFrom(noMapRequest));
      };
      IterableExtensions.<RequestStatement>filter(Iterables.<RequestStatement>filter(this.nodesOfTypeBefore(inTree, RequestStatement.class), RequestStatement.class), _function).forEach(_function_1);
      _xblockexpression = rtnMap;
    }
    return _xblockexpression;
  }
  
  /**
   * return the matching request info for the requestType.
   */
  protected RequestDef getMatchingRequestInfo(final BufferDef bufferDesc, final ChunkType requestType) {
    EList<RequestDef> _requests = bufferDesc.getRequests();
    for (final RequestDef req : _requests) {
      boolean _isWildcardRequest = req.isWildcardRequest();
      boolean _not = (!_isWildcardRequest);
      if (_not) {
        boolean _isA = this.isA(requestType, req.getRequestType());
        if (_isA) {
          return req;
        } else {
          return req;
        }
      }
    }
    return null;
  }
  
  public TreeSet<String> slotsOf(final EObject chunkType) {
    if (chunkType instanceof ChunkType) {
      return _slotsOf((ChunkType)chunkType);
    } else if (chunkType instanceof Function) {
      return _slotsOf((Function)chunkType);
    } else if (chunkType instanceof Pattern) {
      return _slotsOf((Pattern)chunkType);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(chunkType).toString());
    }
  }
  
  protected ChunkType inferBufferContentsFrom(final EObject statement) {
    if (statement instanceof ForLoopStatement) {
      return _inferBufferContentsFrom((ForLoopStatement)statement);
    } else if (statement instanceof RequestStatement) {
      return _inferBufferContentsFrom((RequestStatement)statement);
    } else if (statement instanceof Function) {
      return _inferBufferContentsFrom((Function)statement);
    } else if (statement instanceof Instruction) {
      return _inferBufferContentsFrom((Instruction)statement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement).toString());
    }
  }
}
