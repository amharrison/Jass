/*
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm.validation

import com.google.common.collect.Sets
import com.google.inject.Inject
import java.util.Collection
import java.util.Collections
import java.util.Set
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement
import mil.navy.nrl.ncarai.jasm.program.BufferPattern
import mil.navy.nrl.ncarai.jasm.program.DoWhileStatement
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.FunctionCallStatement
import mil.navy.nrl.ncarai.jasm.program.IfThenElseStatement
import mil.navy.nrl.ncarai.jasm.program.Include
import mil.navy.nrl.ncarai.jasm.program.MatchStatement
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage
import mil.navy.nrl.ncarai.jasm.program.RemoveStatement
import mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement
import mil.navy.nrl.ncarai.jasm.program.RequestStatement
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement
import mil.navy.nrl.ncarai.jasm.program.UntilRepeatStatement
import mil.navy.nrl.ncarai.jasm.program.VariableStatement
import mil.navy.nrl.ncarai.jasm.program.WhileDoStatement
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.validation.Check
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Chunks
import org.jactr.io2.jactr.modelFragment.ConditionalSlot
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage
import org.jactr.io2.jactr.modelFragment.SimpleSlot
import org.jactr.io2.jactr.validation.IClassNameValidator

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class ProgramValidator extends AbstractProgramValidator {

  @Inject extension ValidationUtilities
  @Inject IClassNameValidator _classNameValidator

//	public static val INVALID_NAME = 'invalidName'
//
//	@Check
//	def checkGreetingStartsWithCapital(Greeting greeting) {
//		if (!Character.isUpperCase(greeting.name.charAt(0))) {
//			warning('Name should start with a capital', 
//					ProgramPackage.Literals.GREETING__NAME,
//					INVALID_NAME)
//		}
//	}
  /**
   * does one of the buffer patterns bind the variable name
   */
  protected def boolean defines(String variableName, Collection<? extends BufferPattern> bindings) {

    for (bPattern : bindings)
      if (variableName.equals("=" + bPattern.buffer.name))
        return true
      else if (bPattern.pattern.slots.filter[cSlot|variableName.equals(cSlot.value.name)].last !== null)
        return true

    return false
  }

//  @Check(FAST)
//  override checkImportsOnClasspath(Import importDec) {
//    var resource = importDec.importedNamespace.substring(0, importDec.importedNamespace.length - 2).replace('.', '/') +
//      ".jactr";
//    var altResource = importDec.importedNamespace.substring(0, importDec.importedNamespace.length - 2).replace('.',
//      '/') + ".jasm";
//    if (!_classNameValidator.canResolve(importDec.eResource, resource) &&
//      !_classNameValidator.canResolve(importDec.eResource, altResource)) {
//      error("Could not find " + resource + " in classpath", importDec,
//        ModelFragmentPackage.Literals.IMPORT__IMPORTED_NAMESPACE);
//    }
//  }
  @Check(NORMAL)
  def checkIncludesOnClasspath(Include includeDef) {
    var altResource = includeDef.importedNamespace.substring(0, includeDef.importedNamespace.length - 2).replace('.',
      '/') + ".jasm";
    if (!_classNameValidator.canResolve(includeDef.eResource, altResource)) {
      error("Could not find " + altResource + " in classpath", includeDef,
        ProgramPackage.Literals.INCLUDE__IMPORTED_NAMESPACE);
    }
  }

  /**
   * check that each variable in pattern is defined in at least one of the buffer patterns. OR, the variable
   * is used more than once in the pattern
   */
  protected def checkVariablesInPattern(Pattern pattern, Collection<? extends BufferPattern> bindings,
    EReference errorLiteral) {
//    val bufferContents = inferBufferContents(pattern)
//    val bufferVariables = bufferContents.keySet.map[buffer|"=" + buffer.name].toSet

    
    pattern?.slots.filter [ cSlot |
      cSlot.value.name !== null && cSlot.value.name.startsWith("=")
    ].forEach [ cSlot |
      val anotherDef = pattern.slots.findFirst [ slot |
        slot !== cSlot && cSlot.value.name == slot.value.name
      ]
      if (!defines(cSlot.value.name, bindings) && anotherDef === null)
        error('Pattern ' + pattern.name + ' references variable ' + cSlot.value.name +
          ' but it was not bound by binding patterns', errorLiteral)
    ]
  }

  /**
   * make sure the function return type and the return statement are consistent. Validate
   * the return value/slots
   */
  @Check(NORMAL)
  def checkReturn(ReturnStatement returnStatement) {
    val returnSupplied = returnStatement.reference !== null;
    val function = EcoreUtil2.getContainerOfType(returnStatement, Function)
    if ((function.isVoid && returnSupplied) || (function.isChunk && !returnSupplied))
      error('Function and return are inconsistent', ProgramPackage.Literals.RETURN_STATEMENT__REFERENCE)

    if(function.isVoid) return
    var slotsOf = null as Set
    if (returnStatement.reference !== null && returnStatement.reference.value !== null &&
      returnStatement.reference.value.name !== null) {

      val name = returnStatement.reference.value.name
      if(name.equals("null")) return;

      /*
       * also a slot test if this is an under specified goal slot
       */
      if (returnStatement.reference.buffer === null || returnStatement.reference.buffer.name == "goal") {
        val func = EcoreUtil2.getAllContainers(returnStatement).filter(Function).last
        slotsOf = func.slotsOf
        if (!slotsOf.contains(name))
          if (!isBuffer(name, returnStatement))
            if (!isChunkType(name, returnStatement))
              if (!isChunk(name, returnStatement))
                error("Unknown chunk " + name, ProgramPackage.Literals.RETURN_STATEMENT__REFERENCE)
      } else {
        val bufferContents = inferBufferContents(returnStatement)
        val chunkType = bufferContents.get(returnStatement.reference.buffer)
        if (chunkType === null) {
          error("Could not infer chunktype to validate slot " + name,
            ProgramPackage.Literals.RETURN_STATEMENT__REFERENCE)
          return
        }

        slotsOf = chunkType.slotsOf
        if (!slotsOf.contains(name))
          error(name + " is an unknown slot for " + chunkType.name + " known: " + slotsOf,
            ProgramPackage.Literals.RETURN_STATEMENT__REFERENCE)
      }
    }
  }

  /**
   * verify that function has an unambiguous exit point
   */
  @Check(NORMAL)
  def checkFunctionReturn(Function function) {
//    for (rtnStmnt : EcoreUtil2.getAllContentsOfType(function, ReturnStatement)) {
//      val container = rtnStmnt.eContainer
//      if (container === function)
//        return
//    }
//    error(function.name + " does not have unambiguous exit (return).", function, ProgramPackage.Literals.FUNCTION__NAME)
  }

  @Check(NORMAL)
  def checkVariableDefinition(VariableStatement statement) {
    if (statement.assignment.assignment.functionCall !== null)
      error('Only simple assignments, not function calls, are permitted in variable definitions',
        ProgramPackage.Literals.VARIABLE_STATEMENT__ASSIGNMENT)
  }

  @Check(NORMAL)
  def checkBufferRetention(Function function) {
    val retained = newHashSet
    if (function.retainedContents !== null)
      function.retainedContents.retained.forEach [ retain |
        retained.add(retain.buffer)
      ]

    for (RemoveStatement remove : EcoreUtil2.getAllContentsOfType(function, RemoveStatement).filter [ rmv |
      val intersection = Sets.intersection(rmv.buffers.toSet, retained)
      intersection.size > 0
    ]) {
      for (buffer : remove.buffers) {
        val open = remove.nodesOfTypeBefore(RequestStatement).filter(RequestStatement).findLast [ statement |
          buffer === statement.buffer
        ]
        if (open === null)
          warning('Retained buffer ' + buffer.name + ' was removed.', remove,
            ProgramPackage.Literals.REMOVE_STATEMENT__BUFFERS)
      }
    }
  }

  /**
   * check open requests for leakage
   * 
   * 
   */
  @Check(NORMAL)
  def checkOpenRequestHasRemove(RequestStatement request) {
    if(request.mapping !== null || request.patternReference === null) return

    val removes = request.nodesOfTypeAfter(RemoveStatement).filter(RemoveStatement).filter [ rmv |
      rmv.buffers.contains(request.buffer)
    ]
    /*
     * what are we allowed to leak? assumed of course
     */
    val function = EcoreUtil2.getContainerOfType(request, Function)
    val retained = newHashSet
    if (function.retainedContents !== null)
      function.retainedContents.retained.forEach [ retain |
        retained.add(retain.buffer)
      ]

    val bufferDesc = bufferDefinition(request.buffer.name, request)
    var isEmpty = false
    if (bufferDesc !== null) {
      if (request.patternReference.name.isPattern(request)) {
        val pattern = patternDefinition(request.patternReference.name, request) as Pattern
        val requestInfo = getMatchingRequestInfo(bufferDesc, getChunkType(request, pattern.type))
        if (requestInfo !== null)
          if (!requestInfo.wildcardResult)
            isEmpty = requestInfo.resultType === null
      } else {
      }
    }

    if (removes.size == 0) {
      if (!retained.contains(request.buffer) && !isEmpty)
        error('Request of ' + request.buffer.name + ' leaks. Advise adding remove statement', request,
          ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    } else {
      val siblingOrRoot = removes.findFirst [ rmv |
        rmv.eContainer === request.eContainer || rmv.eContainer === function
      ]
      if (siblingOrRoot === null && !retained.contains(request.buffer) && !isEmpty)
        warning('Request of ' + request.buffer.name + ' potentially leaks. Advise using terminal remove statement.',
          request, ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    }
  }

  /**
   *  check for reserved names
   */
  @Check(NORMAL)
  def checkVariableDeclarations(VariableStatement statement) {
    val value = statement.assignment.reference
    val isGoal = (value.buffer === null || value.buffer.name == "goal")
    if (value.name !== null && isGoal) {
      /*
       * check for reserved names
       */
      if (value.name == "return" || value.name == "parent" || value.name == "stage")
        error(value.name + ' is a reserved variable name', ProgramPackage.Literals.VARIABLE_STATEMENT__ASSIGNMENT)
    }
  }

  /**
   * Check assignment side of the assignemnt statement
   */
  @Check(NORMAL)
  def checkAssignmentStatementAssignment(AssignmentStatement statement) {
    /*
     * is it a buffer? or a type or a chunk
     */
    if(statement.assignment.functionCall!==null) return //ignore for now
    
    val name = statement.assignment.value.name
    // number, boolean or some other
    if(name === null) return;
    if("null" == name || "true" == name || "false" == name) return;

    var slotsOf = null as Set
    /*
     * also a slot test if this is an under specified goal slot
     */
    if (statement.assignment.buffer === null || statement.assignment.buffer.name == "goal") {
      val func = EcoreUtil2.getAllContainers(statement).filter(Function).last
      slotsOf = func.slotsOf
      if (!slotsOf.contains(name))
        if (!isBuffer(name, statement))
          if (!isChunkType(name, statement))
            if (!isChunk(name, statement))
              error("Unknown chunk " + name, ProgramPackage.Literals.ASSIGNMENT_STATEMENT__ASSIGNMENT)
    } else {
      val bufferContents = inferBufferContents(statement)
      val chunkType = bufferContents.get(statement.assignment.buffer)
      if (chunkType === null) {
        inferBufferContents(statement)
        error("Could not infer chunktype to validate slot " + name,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__ASSIGNMENT)
        return
      }

      slotsOf = chunkType.slotsOf
      if (!slotsOf.contains(name))
        error(name + " is an unknown slot for " + chunkType.name + " known: " + slotsOf,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__ASSIGNMENT)
    }

  }

  @Check(NORMAL)
  def checkSlotOfSimpleSlot(SimpleSlot statement) {
    if(statement.name.startsWith(":") || statement.name.startsWith("=")) return
    /*
     * check slot name. simple slots are only used in chunk_def or chunktype
     */
    val containers = EcoreUtil2.getAllContainers(statement)
    val chunks = containers.filter(Chunks).last
    var chunkType = containers.filter(ChunkType).last
    if (chunks !== null)
      chunkType = chunks.type
    val slotsOf = chunkType.slotsOf
    if (!slotsOf.contains(statement.name))
      error(statement.name + " is an unknown slot for " + chunkType.name + " known: " + slotsOf,
        ModelFragmentPackage.Literals.SIMPLE_SLOT__NAME)
  }

  @Check(NORMAL)
  def checkIDofSimpleSlot(SimpleSlot statement) {

    /*
     * is it a buffer? or a type or a chunk
     */
    val name = statement.value.name
    // number, boolean or some other
    if(name === null) return;
    if("null" == name || "true" == name || "false" == name) return;
    // variable
    if(name.startsWith("=")) return;

    if (!isBuffer(name, statement))
      if (!isChunkType(name, statement))
        if (!isChunk(name, statement))
          error("Unknown chunk " + name, ModelFragmentPackage.Literals.SIMPLE_SLOT__VALUE)
  }

  @Check(NORMAL)
  def checkSlotOfConditionalSlot(ConditionalSlot statement) {
    if(statement.name.startsWith(":") || statement.name.startsWith("=")) return
    /*
     * check slot name. condiitonal slots are only used in patterns
     */
    val pattern = EcoreUtil2.getAllContainers(statement).filter(Pattern).last
    var slotsOf = Collections.emptySet
    if (isFunction(pattern.type, pattern)) {
      val function = functionTable(pattern).get(pattern.type).last.toObject(pattern.eResource.resourceSet) as Function
      slotsOf = function.slotsOf
    } else {
      val chunkType = findInTypeTable(statement, ModelFragmentPackage.Literals.CHUNK_TYPE, pattern.type).toObject(
        pattern.eResource.resourceSet) as ChunkType
      slotsOf = chunkType.slotsOf
    }
    if (!slotsOf.contains(statement.name))
      error(statement.name + " is an unknown slot for " + pattern.type + " known: " + slotsOf,
        ModelFragmentPackage.Literals.CONDITIONAL_SLOT__NAME)
  }

  @Check(NORMAL)
  def checkIDofConditionalSlot(ConditionalSlot statement) {

    /*
     * is it a buffer? or a type or a chunk
     */
    val name = statement.value.name
    // number, boolean or some other
    if(name === null) return;
    if("null" == name || "true" == name || "false" == name) return;
    // variable
    if(name.startsWith("=")) return;

    if (!isBuffer(name, statement))
      if (!isChunkType(name, statement))
        if (!isChunk(name, statement))
          error("Unknown chunk " + name, ModelFragmentPackage.Literals.CONDITIONAL_SLOT__VALUE)
  }

  @Check(NORMAL)
  def checkAssignmentStatementBufferPermissions(AssignmentStatement statement) {

    if (statement.reference.buffer !== null) {
      val def = bufferDefinition(statement.reference.buffer.name, statement)
      if (def === null)
        warning('No buffer meta description could be found for ' + statement.reference.buffer.name,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__REFERENCE)
      else {
        if (!def.isWritable)
          error(def.name + ' is not writable', ProgramPackage.Literals.ASSIGNMENT_STATEMENT__REFERENCE)
      }
    }

    if (statement.assignment.buffer !== null) {
      val def = bufferDefinition(statement.assignment.buffer.name, statement)
      if (def === null)
        warning('No buffer meta description could be found for ' + statement.assignment.buffer.name,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__ASSIGNMENT)
      else {
        if (!def.isWritable && !def.isReadable)
          error(def.name + ' is not readable', ProgramPackage.Literals.ASSIGNMENT_STATEMENT__ASSIGNMENT)
      }
    }
  }

  @Check(NORMAL)
  def checkRequestParameters(RequestStatement statement) {
    var requiresCast = false
    if(statement.patternReference === null) return; // empty request, error handler, ignore
    if (statement.patternReference.buffer === null) {
      // .slot, buffer, literal, pattern
      val name = statement.patternReference.name
      if (isBuffer(name, statement))
        requiresCast = true
      else if (isGoalSlot(name, statement))
        requiresCast = true
      else if (isPattern(name, statement))
        requiresCast = false
      else if (isChunk(name, statement))
        requiresCast = true
      else
        error('No pattern, buffer or slot named ' + name + ' found.',
          ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE, Diagnostic.LINKING_DIAGNOSTIC, name)
    } else {
      // buffer.slot, lets validate the slot against the contents of the buffer
      requiresCast = true
      val contents = inferBufferContents(statement)
      val chunkType = contents.get(statement.patternReference.buffer)
      if (chunkType === null)
        error('Could not infer chunktype for buffer ' + statement.patternReference.buffer.name,
          ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE)
      else if (!isValidSlotName(statement.patternReference.name, chunkType))
        error(statement.patternReference.name + ' is not a valid slot of chunktype ' + chunkType.name,
          ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE)
    }

    if (requiresCast && statement.cast === null)
      error('Request requires a cast as statement.', ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE)
  }

  @Check(NORMAL)
  def checkRequestTypes(RequestStatement statement) {
    if(statement.patternReference === null) return; // empty request, this is an error handler
    if(statement.patternReference.buffer !== null) return; // buffer or buffer.slot reference, can't validate
    val pattern = patternDefinition(statement.patternReference.name, statement)
    if(pattern === null) return;

    val def = bufferDefinition(statement.buffer.name, statement)
    if (def === null)
      warning('No buffer meta description could be found for ' + statement.buffer.name,
        ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    else {
      // checking the pattern type and cast against requests
      // if we've got a wildcard, ignore
      val wildCardRequest = def.requests.findFirst [ request |
        request.wildcardRequest
      ]

      if(wildCardRequest !== null) return;
      /*
       * otherwise, check them all for a chunktype match. If none, flag error
       */
      var anyValid = false
      for (request : def.requests) {
        if (!request.wildcardRequest) {
          val refType = getChunkType(statement, pattern.type)
          if (refType.isA(request.requestType)) {
            /*
             * check the cast type
             */
            if (statement.cast !== null) {
              val ct = getChunkType(statement, statement.cast)
              if (!ct.isA(request.resultType))
                error(def.name + " results in " + request.resultType.name + " not " + statement.cast,
                  ProgramPackage.Literals.REQUEST_STATEMENT__CAST)
            }
            anyValid = true
            return
          }
        }
      }

      if (!anyValid) {
        val expectedTypes = def.requests.map[req|req.requestType.name]
        error(def.name + " expects " + expectedTypes + " not " + pattern.type,
          ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
      }
    }
  }

  @Check(NORMAL)
  def checkForLoopTypes(ForLoopStatement statement) {

    val def = bufferDefinition(statement.forBuffer.name, statement)
    if (def === null)
      warning('No buffer meta description could be found for ' + statement.forBuffer.name,
        ProgramPackage.Literals.FOR_LOOP_STATEMENT__FOR_BUFFER)
    else {
      // checking the pattern type and cast against requests
      // if we've got a wildcard, ignore
      val wildCardRequest = def.requests.findFirst [ request |
        request.wildcardRequest
      ]

      if(wildCardRequest !== null) return;
      /*
       * otherwise, check them all for a chunktype match. If none, flag error
       */
      for (request : def.requests) {
        if (!request.wildcardRequest) {
          val refType = getChunkType(statement, statement.initial.type)
          if (refType.isA(request.requestType)) {
            return
          }
        }
      }

      val expectedTypes = def.requests.map[req|req.requestType.name]
      error(def.name + " expects " + expectedTypes + " not " + statement.initial.type,
        ProgramPackage.Literals.FOR_LOOP_STATEMENT__INITIAL)

      if (statement.subsequent !== null) {
        for (request : def.requests) {
          if (!request.wildcardRequest) {
            val refType = getChunkType(statement, statement.subsequent.type)
            if (refType.isA(request.requestType)) {
              return
            }
          }
        }

        error(def.name + " expects " + expectedTypes + " not " + statement.subsequent.type,
          ProgramPackage.Literals.FOR_LOOP_STATEMENT__SUBSEQUENT)

      }
    }
  }

  @Check(NORMAL)
  def checkAssignmentStatementReference(AssignmentStatement statement) {
    val value = statement.reference
    var isGoal = statement.reference.buffer === null || statement.reference.buffer.name == "goal"

    /*
     * check the slots of the function for a match
     */
    if (isGoal) {
      val allContainers = EcoreUtil2.getAllContainers(statement)
      val slotsOf = allContainers.filter(Function).last.slotsOf
      val inFunctionCall = allContainers.filter(FunctionCallStatement).last !== null
      val first = slotsOf.findFirst[slotName|slotName.equals(value.name)]
      if (first === null && !inFunctionCall)
        error(value.name + ' is an unknown variable name.', ProgramPackage.Literals.ASSIGNMENT_STATEMENT__REFERENCE)
    } else {

      val bufferContents = inferBufferContents(statement)
      val type = bufferContents.get(statement.reference.buffer)
      if (type === null)
        error('No mapping-free request on ' + statement.reference.buffer.name,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__REFERENCE)
      else if (!isValidSlotName(statement.reference.name, type))
        error(statement.reference.name + ' is not a valid slot of ' + type.name,
          ProgramPackage.Literals.ASSIGNMENT_STATEMENT__REFERENCE)
    }

    if (statement.assignment!==null && statement.assignment.functionCall !== null)
      return
  }

  @Check(NORMAL)
  def checkFunctionCall(FunctionCallStatement functionCall) {
  }

  protected def isGoalSlot(String slotName, EObject inTree) {
    val function = EcoreUtil2.getContainerOfType(inTree, Function)
    val chunkType = getChunkType(inTree, function.name)
    return isValidSlotName(slotName, chunkType)
  }

  protected def isValidSlotName(String slotName, ChunkType chunkType) {
    var ct = chunkType
    while (ct !== null) {
      val exists = ct.slots.filter[slot|slot.name == slotName].last !== null
      if(exists) return true
      ct = ct.superType
    }
    return false
  }

  protected def checkBindingOfGoal(Collection<? extends BufferPattern> bufferPatterns, EReference errorLiteral) {
    if(bufferPatterns.size==0) return
    
    val contents = bufferPatterns.last.inferBufferContents
    bufferPatterns.forEach [ bP |
      // resolve the types
      // compare, are they related?
      if (bP.pattern.name !== null)
        if (bP.buffer.name == "goal") {
          val function = EcoreUtil2.getAllContainers(bP).filter(Function).last
          if (bP.pattern.type != function.name)
            error('Goal pattern type should match against ' + function.name, errorLiteral)
        } else {
          val type = contents.get(bP.buffer)
          if (type === null)
            error('Could not infer chunktype from open request', errorLiteral)
        }
    ]
  }

  @Check(NORMAL)
  def checkIfBindings(IfThenElseStatement statement) {
    checkBindingOfGoal(statement.bindings.bindings, ProgramPackage.Literals.IF_THEN_ELSE_STATEMENT__BINDINGS)
  }

  @Check(NORMAL)
  def checkWhileDoBindings(WhileDoStatement statement) {
    checkBindingOfGoal(statement.^while.bindings.bindings, ProgramPackage.Literals.WHILE_DO_STATEMENT__WHILE)
  }

  @Check(NORMAL)
  def checkDoWhileBindings(DoWhileStatement statement) {
    checkBindingOfGoal(statement.^while.bindings.bindings, ProgramPackage.Literals.DO_WHILE_STATEMENT__WHILE)
  }

  @Check(NORMAL)
  def checkRepeatUntilBindings(RepeatUntilStatement statement) {
    checkBindingOfGoal(statement.until.bindings.bindings, ProgramPackage.Literals.REPEAT_UNTIL_STATEMENT__UNTIL)
  }

  @Check(NORMAL)
  def checkUntilRepeatBindings(UntilRepeatStatement statement) {
    checkBindingOfGoal(statement.until.bindings.bindings, ProgramPackage.Literals.UNTIL_REPEAT_STATEMENT__UNTIL)
  }

  @Check(NORMAL)
  def checkMatchStatementBindings(MatchStatement statement) {
    statement.cases.forEach [ caseNode |
      checkBindingOfGoal(caseNode.bindings.bindings, ProgramPackage.Literals.MATCH_STATEMENT__CASES)
    ]
  }

  @Check(NORMAL)
  def checkRequest(RequestStatement request) {
    var hasError = true
    val bufferDesc = bufferDefinition(request.buffer.name, request)
    if (bufferDesc !== null)
      hasError = bufferDesc.isError
    if (request.handler === null) {

      // is there a handler later?
      var anyHandler = false
      for (RequestStatement statement : EcoreUtil2.getAllContentsOfType(request.eContainer, RequestStatement).filter [ statement |
        request !== statement && request.buffer === statement.buffer
      ]) {
        if (statement.handler !== null)
          anyHandler = true
      }
      if (!anyHandler && hasError)
        warning('An error handler should be specified either in this or a subsequent request call',
          ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    }

    if (hasError && request.patternReference !== null) 
     if(request.mapping === null){
       /*
        * are we expecting an empty request result?
        */
      var emptyIsValid = false  
      var chunkType = null as ChunkType
      if(request.cast!==null) 
       chunkType = getChunkType(request, request.cast)
      else
       {
         val pattern = patternDefinition(request.patternReference.name, request)
         chunkType = getChunkType(request, pattern.type)
       } 
      if(chunkType!==null && bufferDesc!==null)
      {
        val requestInfo = getMatchingRequestInfo(bufferDesc, chunkType) 
        if(requestInfo!==null)
         emptyIsValid = requestInfo.resultType===null
      } 
      
      if(!emptyIsValid)
       info('Good form recommends using the mapping block to grab a reference and release the resource',
         ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    }

  }

  @Check(NORMAL)
  def checkRequestPattern(RequestStatement request) {
    if(request.patternReference === null) return;
    if(request.patternReference.buffer !== null) return;

    var bindings = Collections.emptyList
    if (request.bindings !== null)
      bindings = request.bindings.bindings

    val pattern = patternDefinition(request.patternReference.name, request)
    if(pattern!==null)
     checkVariablesInPattern(pattern, bindings, ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE)

    // check bindings
    checkBindingOfGoal(bindings, ProgramPackage.Literals.REQUEST_STATEMENT__BINDINGS)
  }

  @Check(NORMAL)
  def checkForLoopPatterns(ForLoopStatement loopStatement) {
    var bindings = Collections.emptyList
    if (loopStatement.initialBindings !== null)
      bindings = loopStatement.initialBindings.bindings

    checkVariablesInPattern(loopStatement.initial, bindings, ProgramPackage.Literals.FOR_LOOP_STATEMENT__INITIAL)
    checkBindingOfGoal(bindings, ProgramPackage.Literals.FOR_LOOP_STATEMENT__INITIAL_BINDINGS)

    bindings = Collections.emptyList
    if (loopStatement.subsequentBindings !== null)
      bindings = loopStatement.subsequentBindings.bindings

    checkVariablesInPattern(loopStatement.subsequent, bindings, ProgramPackage.Literals.FOR_LOOP_STATEMENT__SUBSEQUENT)
    checkBindingOfGoal(bindings, ProgramPackage.Literals.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS)
  }

  @Check(NORMAL)
  def checkPatternType(Pattern pattern) {
    var valid = isFunction(pattern.type, pattern)

    if (!valid)
      valid = isChunkType(pattern.type, pattern)

    if (!valid)
      error('Pattern isa must be a valid chunktype or function', pattern, ProgramPackage.Literals.PATTERN__TYPE)
  }

  /**
   * 
   */
  @Check(NORMAL)
  def checkPatternName(Pattern pattern) {
    val table = patternTable(pattern) //expensive
    if (table.get(pattern.name).size > 1)
      error('Patterns must be uniquely named', ProgramPackage.Literals.PATTERN__NAME)
  }

  @Check(NORMAL)
  def checkFunctionName(Function function) {
    val table = functionTable(function)
    if (table.get(function.name).size > 1)
      error('Functions must be uniquely named', ProgramPackage.Literals.FUNCTION__NAME)
  }

  @Check(NORMAL)
  def checkRequestMappingForRemove(RequestStatement statement){
    if(statement.mapping===null) return;
    
    val removes = EcoreUtil2.getAllContentsOfType(statement.mapping, RemoveStatement)
    if(removes.size==0)
      error('Mapping handler must include a remove to clear the buffer', ProgramPackage.Literals.REQUEST_STATEMENT__MAPPING);
  }

  @Check(NORMAL)
  def checkRequestForForce(RequestStatement statement) {
    if(statement.patternReference === null || statement.forced) return

    val contents = statement.inferBufferContents
    if (contents.containsKey(statement.buffer)) {
      val priorRemove = statement.nodesOfTypeBefore(RemoveStatement).filter(RemoveStatement).findLast [ remove |
        remove.buffers.contains(statement.buffer)
      ]
      if (priorRemove === null)
        warning(
          statement.buffer.name + ' may not be empty. Request is likely to block on contents. Recommend using force.',
          ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER)
    }
  }

  @Check(NORMAL)
  def checkForLoopForForce(ForLoopStatement statement) {
    if(statement.forced) return

    val contents = statement.inferBufferContents
    if (contents.containsKey(statement.forBuffer)) {
      val priorRemove = statement.nodesOfTypeBefore(RemoveStatement).filter(RemoveStatement).findLast [ remove |
        remove.buffers.contains(statement.forBuffer)
      ]
      if (priorRemove === null)
        warning(statement.forBuffer.name +
          ' may not be empty. Request is likely to block on contents. Recommend using force.',
          ProgramPackage.Literals.FOR_LOOP_STATEMENT__FOR_BUFFER)
    }
  }

  @Check(NORMAL)
  def checkIrrelevantRemove(RemoveStatement remove) {
    val contents = remove.inferBufferContents
    for (buffer : remove.buffers)
      if (!contents.containsKey(buffer))
        warning('Contents of buffer are likely empty.', ProgramPackage.Literals.REMOVE_STATEMENT__BUFFERS)
  }

}
