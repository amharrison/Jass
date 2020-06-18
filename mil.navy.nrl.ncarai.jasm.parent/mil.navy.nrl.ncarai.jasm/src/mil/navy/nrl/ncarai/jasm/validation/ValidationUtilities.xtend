package mil.navy.nrl.ncarai.jasm.validation

import com.google.common.collect.LinkedListMultimap
import java.util.Collection
import java.util.Set
import java.util.TreeSet
import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.program.BufferDef
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.Program
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage
import mil.navy.nrl.ncarai.jasm.program.RequestStatement
import mil.navy.nrl.ncarai.jasm.program.VariableStatement
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.eclipse.xtext.xbase.lib.Functions.Function1
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.ChunkDef
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Import
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage

class ValidationUtilities {

  @Inject ResourceDescriptionsProvider _descriptors
  @Inject IResourceServiceProvider.Registry _resourceServiceProvider
  @Inject IQualifiedNameConverter _qualifiedNameConverter
  @Inject IContainer.Manager _containerManager


  new(){
    
  }
  
  private def startsWith(String string, Set<String> startsWith) {

    for (start : startsWith)
      if (string.startsWith(start))
        return true
    return false
  }

  private def localAndImportFilter(EObject root, boolean excludeLocal) {
    val startsWith = new TreeSet

    EcoreUtil2.getAllContentsOfType(root, Import).forEach [ imp |
      startsWith.add(imp.importedNamespace.substring(0, imp.importedNamespace.length - 1)) // include the dot
    ]

    if (!excludeLocal)
      if (root instanceof Program)
        startsWith.add(root.package.name + ".")

    val Function1<? super IEObjectDescription, Boolean> handler = [ desc |
      startsWith(_qualifiedNameConverter.toString(desc.qualifiedName), startsWith)
    ]

    handler
  }

  def getVisibileExportedObjectDescriptions(EObject o, EClass type) {
    val all = o.visibileContainers.map [ container |
      container.getExportedObjectsByType(type)
    ].flatten.toSet
    all.removeAll(getExportedObjectDescriptions(o, type))
    all
  }

  protected def getExportedObjectDescriptions(EObject o, EClass type) {
    val resServiceProvider = _resourceServiceProvider.getResourceServiceProvider(o.eResource.URI)
    val manager = resServiceProvider.getResourceDescriptionManager()
    val description = manager.getResourceDescription(o.eResource)

    description.exportedObjects.filter [ desc |
      desc.EClass === type
    ].toSet
  }

  /**
   * 
   * 
   * visibility
   */
  private def getVisibileContainers(EObject o) {
    val resServiceProvider = _resourceServiceProvider.getResourceServiceProvider(o.eResource.URI)
    val manager = resServiceProvider.getResourceDescriptionManager()
    val description = manager.getResourceDescription(o.eResource)

    val index = _descriptors.getResourceDescriptions(o.eResource)
//    val rd = index.getResourceDescription(o.eResource.URI)
    _containerManager.getVisibleContainers(description, index)
  }

  protected def typeTable(EObject inTree, EClass literal) {
    val map = LinkedListMultimap.create
    val literals = getVisibileExportedObjectDescriptions(inTree, literal)
    literals.filter(localAndImportFilter(EcoreUtil.getRootContainer(inTree), false)).forEach([ buffer |
      map.put(buffer.qualifiedName.lastSegment, buffer)
    ])

    map
  }

  def packageTable(EObject o) {
    val map = LinkedListMultimap.create
    var types = getVisibileExportedObjectDescriptions(o, ModelFragmentPackage.Literals.PACKAGE_DECLARATION)
    types.forEach([ type |
      map.put(type.qualifiedName.toString, type)
    ])
    types = getVisibileExportedObjectDescriptions(o, ProgramPackage.Literals.PACKAGE_DEF)
    types.forEach([ type |
      map.put(type.qualifiedName.toString, type)
    ])
    map
  }

  def findInTypeTable(EObject inTree, EClass literal, String name) {
    val literals = getVisibileExportedObjectDescriptions(inTree, literal)
    return literals.filter(localAndImportFilter(EcoreUtil.getRootContainer(inTree), false)).findFirst [ desc |
      desc.qualifiedName.lastSegment.equals(name)
    ]
  }

  def findExactInTypeTable(EObject inTree, EClass literal, QualifiedName fqn) {
    val literals = getVisibileExportedObjectDescriptions(inTree, literal)
    return literals.findFirst [ desc |
      desc.qualifiedName.equals(fqn)
    ]
  }

  def bufferDefinition(String bufferName, EObject inTree) {
    /*
     * buffer definitions come from an implicit import, so it won't be visible in the standard
     * findInTypeTable which filters by import contents, hence the use of findExactInTypeTable
     */
    val desc = findExactInTypeTable(inTree, ProgramPackage.Literals.BUFFER_DEF,
      QualifiedName.create("jasm", "procedural", "Buffers", bufferName))
    if (desc !== null)
      return desc.toObject(inTree.eResource.resourceSet) as BufferDef
    return null
  }

  def functionTable(EObject inTree) {
    typeTable(inTree, ProgramPackage.Literals.FUNCTION)
  }

  def isFunction(String functionName, EObject inTree) {
    findInTypeTable(inTree, ProgramPackage.Literals.FUNCTION, functionName) !== null
  }

  def chunkTypeTable(EObject inTree) {
    typeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE)
  }

  def isChunkType(String chunkTypeName, EObject inTree) {
    findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE, chunkTypeName) !== null
  }

  def bufferTable(EObject inTree) {
    typeTable(inTree, ModelFragmentPackage.Literals.BUFFER)
  }

  def isBuffer(String bufferName, EObject inTree) {
    findInTypeTable(inTree, ModelFragmentPackage.Literals.BUFFER, bufferName) !== null
  }

  def chunkTable(EObject inTree) {
    typeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF)
  }

  def chunkDefinition(String chunkName, EObject inTree) {
    val rtn = findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF, chunkName)
    if (rtn !== null)
      return rtn.toObject(inTree.eResource.resourceSet) as ChunkDef
    return null as ChunkDef
  }

  def isChunk(String chunkName, EObject inTree) {
    findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_DEF, chunkName) !== null
  }

  def patternTable(EObject inTree) {
    typeTable(inTree, ProgramPackage.Literals.PATTERN)
  }

  def isPattern(String patternName, EObject inTree) {
    findInTypeTable(inTree, ProgramPackage.Literals.PATTERN, patternName) !== null
  }

  def patternDefinition(String patternName, EObject inTree) {
    val rtn = findInTypeTable(inTree, ProgramPackage.Literals.PATTERN, patternName)
    if (rtn !== null)
      return rtn.toObject(inTree.eResource.resourceSet) as Pattern
    return null as Pattern
  }

  def toObject(IEObjectDescription descriptor, ResourceSet resourceSet) {
    EcoreUtil.resolve(descriptor.EObjectOrProxy, resourceSet)
  }

  dispatch def slotsOf(Function function) {
    val rtn = newTreeSet(null)
    function.slots.forEach [ slot |
      rtn.add(slot.assignment.reference.name)
    ]
    function.variables.forEach [ slot |
      rtn.add(slot.assignment.reference.name)
    ]
    rtn
  }

  dispatch def slotsOf(ChunkType chunkType) {
    val rtn = newTreeSet(null)
    var ct = chunkType
    while (ct !== null) {
      ct.slots.forEach [ slot |
        rtn.add(slot.name)
      ]
      ct = ct.superType
    }
    rtn
  }

  dispatch def slotsOf(Pattern pattern) {
    val rtn = newTreeSet(null)
    pattern.slots.forEach [ slot |
      rtn.add(slot.name)
    ]
    rtn
  }

  /**
   * 
   */
  def nodesOfTypeBefore(EObject reference, Class<? extends EObject> clazz) {
    val function = EcoreUtil2.getAllContainers(reference).filter(Function).last
    val referenceOffset = NodeModelUtils.findActualNodeFor(reference).offset
    return EcoreUtil2.getAllContentsOfType(function, clazz).filter [ object |
      NodeModelUtils.findActualNodeFor(object).offset < referenceOffset
    ].filter(clazz)
  }

  def nodesOfTypeAfter(EObject reference, Class<? extends EObject> clazz) {
    val function = EcoreUtil2.getAllContainers(reference).filter(Function).last
    val referenceOffset = NodeModelUtils.findActualNodeFor(reference).offset
    return EcoreUtil2.getAllContentsOfType(function, clazz).filter [ object |
      NodeModelUtils.findActualNodeFor(object).offset > referenceOffset
    ].filter(clazz)
  }

  def nodesOfTypeBetween(EObject earlier, EObject later, Class<? extends EObject> clazz) {
    val function = EcoreUtil2.getAllContainers(earlier).filter(Function).last
    val early = NodeModelUtils.findActualNodeFor(earlier).offset
    val late = NodeModelUtils.findActualNodeFor(later).offset
    return EcoreUtil2.getAllContentsOfType(function, clazz).filter [ object |
      val offset = NodeModelUtils.findActualNodeFor(object).offset
      offset > early && offset < late
    ].filter(clazz)
  }

  /**
   * proxy chunktype describing a function
   */
  def asChunkType(Function function) {
    val chunkType = ModelFragmentFactory.eINSTANCE.createChunkType
    chunkType.name = function.name
    // what about the supertype?
    // context has that info, not validation. For now, hard code
    addSlots(chunkType, #["stage", "parent", "return"])
    addSlots(chunkType, function.slots)
    addSlots(chunkType, function.variables)

    chunkType
  }

  def addSlots(ChunkType type, Iterable<String> itr) {
    for (s : itr) {
      val slot = ProcGenUtilities.simpleSlot(s)
      slot.value.name = "null"

      type.slots.add(slot)
    }
  }

  def addSlots(ChunkType type, Collection<VariableStatement> list) {
    for (variable : list) {
      val slot = ProcGenUtilities.simpleSlot(variable.assignment.reference.name)
      if (variable.assignment.assignment.value !== null)
        slot.value = EcoreUtil.copy(variable.assignment.assignment.value)
      else
        slot.value.name = "null"

      type.slots.add(slot)
    }
  }

  def isA(ChunkType query, ChunkType potentialAncestor) {
    var q = query
    while (q !== null) {
      if(potentialAncestor.name == q.name) return true
      q = q.superType
    }
    return false
  }

  def getChunkType(EObject inTree, String chunkTypeName) {
    var type = null as ChunkType
    var descriptor = findInTypeTable(inTree, ModelFragmentPackage.Literals.CHUNK_TYPE, chunkTypeName)
    if (descriptor !== null)
      type = descriptor.toObject(inTree.eResource.resourceSet) as ChunkType
    else {
      descriptor = findInTypeTable(inTree, ProgramPackage.Literals.FUNCTION, chunkTypeName)
      if (descriptor !== null) {
        val function = descriptor.toObject(inTree.eResource.resourceSet) as Function
        type = function.asChunkType
      }
    }

    type
  }

  protected dispatch def inferBufferContentsFrom(Function function) {
    getChunkType(function, function.name)
  }

  protected dispatch def inferBufferContentsFrom(RequestStatement statement) {
    var type = getChunkType(statement, statement.cast)
    if (type === null && statement.patternReference !== null) {
      var pattern = null as Pattern
      if (statement.patternReference.name !== null && isPattern(statement.patternReference.name, statement)) {
        pattern = patternDefinition(statement.patternReference.name, statement)
        type = getChunkType(statement, pattern.type)
      }
      else
      if(statement.patternReference.name!==null && isBuffer(statement.patternReference.name, statement))
      {
        //type = //would need ProcGenContext to get the buffer contents.. 
      }

      val def = bufferDefinition(statement.buffer.name, statement)
      if (def !== null) {
        val wildCardRequest = def.requests.findFirst [ request |
          request.wildcardRequest
        ]

        if(wildCardRequest !== null) return type

        if (pattern !== null)
          for (request : def.requests) {
            if (!request.wildcardRequest) {
              val refType = getChunkType(statement, pattern.type)
              if (refType.isA(request.requestType)) {
                type = request.resultType
              }
            }
          }
      }
    }

    type
  }

  protected dispatch def inferBufferContentsFrom(ForLoopStatement statement) {
    var type = getChunkType(statement, statement.initial.type)

    val def = bufferDefinition(statement.forBuffer.name, statement)
    if (def !== null) {
      val wildCardRequest = def.requests.findFirst [ request |
        request.wildcardRequest
      ]

      if(wildCardRequest !== null) return type

      for (request : def.requests) {
        if (!request.wildcardRequest) {
          val refType = getChunkType(statement, statement.initial.type)
          if (refType.isA(request.requestType)) {
            type = request.resultType
          }
        }
      }
    }

    type
  }

  protected dispatch def inferBufferContentsFrom(Instruction statement) {
    null as ChunkType
  }

  /**
   * from this point, use the containing forEach & (at most one) open (no-mapping) 
   * request to build the initial estimated contents. We then append the set of
   * open requests before this instruction. 
   * Note: we do not respect remove 
   */
  def inferBufferContents(EObject inTree) {
    val rtnMap = newHashMap

    /*
     * get the chunktype inferences for the for loop and request statement
     * 
     */
    try {
//from leaf to root
      EcoreUtil2.getAllContainers(inTree).forEach [ container |
        if (container instanceof RequestStatement) {
          if (!rtnMap.containsKey(container.buffer))
            rtnMap.putIfAbsent(container.buffer, container.inferBufferContentsFrom)
        } else if (container instanceof ForLoopStatement)
          rtnMap.putIfAbsent(container.forBuffer, container.inferBufferContentsFrom)
        else if (container instanceof Function) {
          /*
           * use any with statements as content
           */
          if (container.assumedContents !== null)
            container.assumedContents.assumed.forEach [ assumed |
              val chunkType = getChunkType(container, assumed.type)
              if (chunkType !== null)
                rtnMap.putIfAbsent(assumed.buffer, chunkType)
            ]

          val goalBufferDesc = findInTypeTable(inTree, ModelFragmentPackage.Literals.BUFFER, "goal")
          if (goalBufferDesc !== null)
            rtnMap.put(goalBufferDesc.toObject(inTree.eResource.resourceSet) as Buffer,
              container.inferBufferContentsFrom)
        }
      ]
    } catch (Exception e) {
      e.printStackTrace
    }

    /* now for all of the open (no mapping) requests
     * 
     */
    inTree.nodesOfTypeBefore(RequestStatement).filter(RequestStatement).filter [ request |
      request.mapping === null
    ].forEach [ noMapRequest |
      rtnMap.putIfAbsent(noMapRequest.buffer, noMapRequest.inferBufferContentsFrom)
    ]

    rtnMap
  }

/*
 * return the matching request info for the requestType.
 */
  protected def getMatchingRequestInfo(BufferDef bufferDesc, ChunkType requestType) {

    /*
     * in depth check
     */
    for (req : bufferDesc.requests) {
      if (!req.wildcardRequest)
        if (requestType.isA(req.requestType)) {
          return req
        }
       else
        return req 
    }
    return null
  }
}
