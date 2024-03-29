/*
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm.generator

import com.google.inject.Inject
import com.google.inject.Provider
import mil.navy.nrl.ncarai.jasm.jactrio2.IModelBuilder
import mil.navy.nrl.ncarai.jasm.program.PackageDef
import mil.navy.nrl.ncarai.jasm.program.Program
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class ProgramGenerator extends AbstractGenerator {

  @Inject IModelBuilder _modelBuilder
  @Inject Provider<ResourceSet> _resourceSetProvider;
  

  override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {

    var name = resource.allContents.filter(PackageDef).toIterable.get(0).name
    val modelFragments = _modelBuilder.build(resource.allContents.filter(Program).toIterable.get(0))

    val firstFragment = modelFragments.get(0)
    modelFragments.remove(firstFragment)
    /*
     * save to temp .generated file
     */
    var loaderPath = firstFragment.package.name.replace(".", "/")
    var loaderTmpPath = loaderPath
    loaderTmpPath = loaderTmpPath + ".generated"
    val loaderURI = fsa.getURI(loaderTmpPath + ".jactr")
    var newResource = _resourceSetProvider.get.createResource(loaderURI)
    newResource.contents.add(firstFragment)
    newResource.save(null)

    fsa.generateFile(loaderPath + ".jactr", fsa.readTextFile(loaderTmpPath + ".jactr"))

    fsa.deleteFile(loaderTmpPath + ".jactr")

    for (modelFragment : modelFragments) {
      name = modelFragment.package.name
      name = name.replace(".", "/")
      var newURI = fsa.getURI(name + ".jactr")
      newResource = _resourceSetProvider.get.createResource(newURI)
      newResource.contents.add(modelFragment)

      fsa.generateFile(name + ".jactr", '')

      newResource.save(null)
    }

    /**
     * crappy hack to force the indexing of generated files. For some reason
     * xtext does not run its builder on freshly generated files. This delayed touch
     * allows us to force the builder to process the generated files.
     */
    val workspace = ResourcesPlugin.workspace
    val path = fsa.getURI(loaderPath + ".jactr").toPlatformString(true)
    val file = workspace.root.getFile(new Path(path))

    val job = Job.create("touch", [ monitor |
      file.touch(new NullProgressMonitor)
    ])
    job.schedule(500)
  }
}
