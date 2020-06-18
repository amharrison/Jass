package mil.navy.nrl.ncarai.jasm.jactrio2.procedural

import com.google.inject.Injector
import java.util.Collection
import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.jactrio2.IModelBuilder
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.GeneratorDef
import mil.navy.nrl.ncarai.jasm.program.Program
import mil.navy.nrl.ncarai.jasm.program.VariableStatement
import mil.navy.nrl.ncarai.jasm.scoping.GlobalImplicitImportManager
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.EcoreUtil2
import org.jactr.io2.jactr.comment.CommentUtilities
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Chunks
import org.jactr.io2.jactr.modelFragment.ModelFragment
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory
import org.jactr.io2.jactr.modelFragment.Production
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import mil.navy.nrl.ncarai.jasm.program.PackageDef

class DefaultProceduralModelBuilder implements IModelBuilder {

  @Inject Injector _injector
  @Inject GlobalImplicitImportManager _importManager
  @Inject ValidationUtilities _validationUtilities
  ModelFragment _parameters

  ModelFragment _procedural

  Collection<Production> _fallbackProductions
  Collection<Production> _nonFallbackProductions

  override build(Program program) {
    _fallbackProductions = newArrayList
    _nonFallbackProductions = newArrayList

    program.buildParameters

    program.buildProcedural

    program.process

    newArrayList(_procedural, _parameters)
  }

  protected def buildParameters(Program program) {
    val model = ModelFragmentFactory.eINSTANCE.createModelFragment
    model.package = ModelFragmentFactory.eINSTANCE.createPackageDeclaration
    model.package.name = program.package.target + "Parameters"

    var imp = ModelFragmentFactory.eINSTANCE.createImport
    imp.importedNamespace = program.package.target + ".*"
    model.package.imports.add(imp)

    _parameters = model
  }

  protected def buildProcedural(Program program) {
    val model = ModelFragmentFactory.eINSTANCE.createModelFragment
    model.package = ModelFragmentFactory.eINSTANCE.createPackageDeclaration
    model.package.name = program.package.target

//    val defaults = #['jactr.declarative.*', 'jactr.procedural.*', 'jactr.retrieval.*', 'jactr.imaginal.*',
//      'jactr.visual.*', 'jactr.aural.*', 'jactr.vocal.*', 'jactr.motor.*', 'jactr.goal.*']
//    
//    defaults.forEach[str  | 
//      var imp = ModelFragmentFactory.eINSTANCE.createImport
//      imp.importedNamespace = str
//      model.package.imports.add(imp)
//    ]  
    program.package.imports.forEach [ imp |
      model.package.imports.add(EcoreUtil.copy(imp))
    ]

    val packageTable = _validationUtilities.packageTable(program)

    program.package.includes.forEach [ inc |

      val jasmNamespace = inc.importedNamespace.substring(0, inc.importedNamespace.lastIndexOf('.'))
      val packageSpace = _validationUtilities.toObject(packageTable.get(jasmNamespace).last,
        program.eResource.resourceSet) as PackageDef

      var imp = ModelFragmentFactory.eINSTANCE.createImport
      imp.importedNamespace = packageSpace.target + '.*'
      model.package.imports.add(imp)
    ]

//    var imp = ModelFragmentFactory.eINSTANCE.createImport
//    imp.importedNamespace = program.package.name + "Declarative.*"
//    model.package.imports.add(imp)
    _procedural = model
  }

  protected def process(Program program) {
    val resolver = _injector.getInstance(IReferenceResolver)
    resolver.setProgram(program)
    val generator = EcoreUtil2.getAllContentsOfType(program, GeneratorDef).last

    val context = new ProceduralGenerationContext(_injector, resolver, generator, [ element |
      if (element instanceof Production) {
        _procedural.package.elements.add(element)

        if (element.parameters !== null)
          _fallbackProductions.add(element)
        else
          _nonFallbackProductions.add(element)
      }
      if (element instanceof Chunks) {
        /*
         * if we split file content we have to deal with scope providers.
         */
//        _declarative.package.elements.add(element)
        _procedural.package.elements.add(element)
        element.chunks.forEach[chunk|resolver.add(chunk)]
      }
      if (element instanceof ChunkType) {
//        _declarative.package.elements.add(element)
        _procedural.package.elements.add(element)
        resolver.add(element)
      }
    ])

    /*
     * we tell the global implicit import manager what the goal manipulator says
     * is an import we need. 
     */
    var namespace = context._goalManipulator.imports.get(0)
    namespace = namespace.substring(1, namespace.lastIndexOf(".")).replace('/', '.') + ".*"
    _importManager.accept(namespace)

    /*
     * copy existing chunktypes & chunks
     */
    EcoreUtil2.getAllContentsOfType(program, ChunkType).forEach [ chunkType |
      context.consumer.accept(EcoreUtil.copy(chunkType))
    ]

    EcoreUtil2.getAllContentsOfType(program, Chunks).forEach [ chunks |
      val chunksCopy = EcoreUtil.copy(chunks)
      chunksCopy.type = resolver.resolveChunkType(chunks.type.name)
      context.consumer.accept(chunksCopy)
    ]

    /*
     * create all the function goal types first so that we can dispatch to them in production
     */
    program.package.functions.forEach [ func |
      val goalType = createGoalChunkType(func, context)
      context.consumer.accept(goalType)
    ]

//should deal with imports too
    context._functionTable.populate(program.package.functions)

    program.package.functions.forEach [ func |
      
      context.newFunction(func)
      context._goalStateProvider.startBranch(func.instructions, context)

      /*
       * snag the start of the block so that stage default value is correctly
       * specified.
       */
      val goalType = context._resolver.resolveChunkType(func.name)
      val pattern = context._goalStateProvider.nextGoalState
      val lastSlot = pattern.slots.last
      val slot = ProcGenUtilities.simpleSlot("stage")
      slot.value.name = lastSlot.value.name
      goalType.slots.add(slot)

      context._productionSequencer.sequence(func.instructions, context)
      context._goalStateProvider.endBranch
    ]

    /*
     * take care of _procedural imports now that we have context
     */
    context._goalManipulator.imports.forEach [ str |
      var imp = ModelFragmentFactory.eINSTANCE.createImport
      imp.importedNamespace = str.substring(1, str.lastIndexOf(".")).replace('/', '.') + ".*"
      _procedural.package.imports.add(imp)
    ]

    /*
     * now for the loader. we populate it with two sets of parameter calls. One for
     * the fallback productions, and one for all other others
     */
    var pp = productionParameters(_fallbackProductions, context._prioritizer.fallbackPriority, context);
    if (pp.elements.size > 0) {
      CommentUtilities.comment(pp, 'Provided to easily change fall through priorities')
      _parameters.package.elements.add(pp)
    }

    pp = productionParameters(_nonFallbackProductions, context._prioritizer.defaultPriority, context)
    if (pp.elements.size > 0) {
      CommentUtilities.comment(pp, 'Provided to easily change default priorities')
      _parameters.package.elements.add(pp)
    }
  }

  protected def productionParameters(Collection<Production> productions, String defaultValue,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createParameters
    productions.forEach [ p |
      rtn.elements.add(p.name)
    ]
    rtn.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
    var parameter = ModelFragmentFactory.eINSTANCE.createParameter
    parameter.name = context._prioritizer.priorityParameterName
    parameter.value = ModelFragmentFactory.eINSTANCE.createValue
    parameter.value.string = defaultValue
    rtn.parameters.parameter.add(parameter)
    rtn
  }

  protected def createGoalChunkType(Function function, ProceduralGenerationContext context) {

    val chunkType = context._goalManipulator.newGoalType(function, context);
    // add the slots. These should all be simple assignments.
    addSlots(chunkType, function.slots, context)
    addSlots(chunkType, function.variables, context)
    CommentUtilities.comment(chunkType, ProcGenUtilities.text(function))
    context._consumer.accept(chunkType)
    chunkType
  }

  protected def addSlots(ChunkType type, Collection<VariableStatement> list, ProceduralGenerationContext context) {
    for (variable : list) {
      val slot = ProcGenUtilities.simpleSlot(variable.assignment.reference.name)
      if (variable.assignment.assignment.value !== null)
        slot.value = EcoreUtil.copy(variable.assignment.assignment.value)
      else
        slot.value.name = "null"

      type.slots.add(slot)
    }
  }

}
