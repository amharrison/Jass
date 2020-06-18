package mil.navy.nrl.ncarai.jasm.ui.hyperlink

import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.program.AsStatement
import mil.navy.nrl.ncarai.jasm.program.Include
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage
import mil.navy.nrl.ncarai.jasm.program.RequestStatement
import mil.navy.nrl.ncarai.jasm.program.VariableReference
import mil.navy.nrl.ncarai.jasm.ui.internal.JasmActivator
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.xtext.ui.TypeAwareHyperlinkHelper
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor
import org.jactr.io2.jactr.modelFragment.Import
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage
import org.jactr.io2.jactr.modelFragment.Value

class JASMHyperlinkHelper extends TypeAwareHyperlinkHelper {

  @Inject extension ValidationUtilities

  override createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
    val astNode = EObjectAtOffsetHelper.resolveElementAt(resource, offset)
    if (astNode instanceof Value) {

      if (astNode.name !== null) {
        val parseNode = NodeModelUtils.findActualNodeFor(astNode)
        val symbol = getSymbol(astNode, astNode.name)
        if (symbol !== null)
          createHyperlinksTo(resource, parseNode, symbol, acceptor)
      }
    } else if (astNode instanceof Import) {
      var importSource = astNode.importedNamespace
      importSource = importSource.substring(0, importSource.length - 2)
      val parseNode = NodeModelUtils.findActualNodeFor(astNode)
      val symbol = getNamespaceOrPackage(astNode, importSource)
      if (symbol !== null)
        createHyperlinksTo(resource, parseNode, symbol, acceptor)
    } else if (astNode instanceof Include) {
      var importSource = astNode.importedNamespace
      importSource = importSource.substring(0, importSource.length - 2)
      val parseNode = NodeModelUtils.findActualNodeFor(astNode)
      val symbol = getNamespaceOrPackage(astNode, importSource)
      if (symbol !== null)
        createHyperlinksTo(resource, parseNode, symbol, acceptor)
    } else if (astNode instanceof Pattern) {

      val featureNodes = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.PATTERN__TYPE)
      for (featureNode : featureNodes)
        if (featureNode.totalTextRegion.contains(offset)) {
          val text = featureNode.text
          val symbol = getSymbol(astNode, text)
          if (symbol !== null)
            createHyperlinksTo(resource, featureNode, symbol, acceptor)
        } else
          super.createHyperlinksByOffset(resource, offset, acceptor)

    } else if (astNode instanceof AsStatement) {
      val featureNodes = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.AS_STATEMENT__TYPE)
      for (featureNode : featureNodes)
        if (featureNode.totalTextRegion.contains(offset)) {
          val text = featureNode.text
          val symbol = getSymbol(astNode, text)
          if (symbol !== null)
            createHyperlinksTo(resource, featureNode, symbol, acceptor)
        }
    } else if (astNode instanceof RequestStatement) {
      val featureNodes = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.REQUEST_STATEMENT__CAST)
      for (featureNode : featureNodes)
      if (featureNode.totalTextRegion.contains(offset)) {
          val text = featureNode.text
          val symbol = getSymbol(astNode, text)
          if (symbol !== null)
            createHyperlinksTo(resource, featureNode, symbol, acceptor)
        }
    } else if (astNode instanceof VariableReference) {
      if (astNode.buffer === null) {
        val parseNode = NodeModelUtils.findActualNodeFor(astNode)
        val symbol = getSymbol(astNode, astNode.name)
        if (symbol !== null)
          createHyperlinksTo(resource, parseNode, symbol, acceptor)
      }
    } else
      super.createHyperlinksByOffset(resource, offset, acceptor)
  }

  def getNamespaceOrPackage(EObject astNode, String name) {
    val table = packageTable(astNode)
    var descriptor = table.get(name).last

    if (descriptor !== null)
      return descriptor.toObject(astNode.eResource.resourceSet)
    return null
  }

  def getSymbol(EObject astNode, String name) {
    try {

      var descriptor = findInTypeTable(astNode, ProgramPackage.Literals.FUNCTION, name)
      if (descriptor === null)
        descriptor = findInTypeTable(astNode, ModelFragmentPackage.Literals.BUFFER, name)
      if(descriptor === null)
       descriptor = findInTypeTable(astNode, ProgramPackage.Literals.PATTERN, name)  
      if (descriptor === null)
        descriptor = findInTypeTable(astNode, ModelFragmentPackage.Literals.CHUNK_TYPE, name)
      if (descriptor === null)
        descriptor = findInTypeTable(astNode, ModelFragmentPackage.Literals.CHUNK_DEF, name)

      return descriptor.toObject(astNode.eResource.resourceSet)
    } catch (Exception e) {
      // easily possible during an edit
      JasmActivator.instance.log.log(
        new Status(IStatus.ERROR, JasmActivator.PLUGIN_ID, "Failed to find descriptor for " + name, e));
    }
    return null
  }
}
