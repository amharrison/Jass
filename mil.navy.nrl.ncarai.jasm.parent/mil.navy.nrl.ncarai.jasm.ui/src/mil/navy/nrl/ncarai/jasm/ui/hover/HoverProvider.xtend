package mil.navy.nrl.ncarai.jasm.ui.hover

import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.program.VariableReference
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.nodemodel.ICompositeNode
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider
import org.eclipse.xtext.ui.editor.hover.html.IEObjectHoverDocumentationProvider

class HoverProvider extends DefaultEObjectHoverProvider implements IEObjectHoverDocumentationProvider, IEObjectHoverProvider {

  @Inject extension ValidationUtilities

  override getDocumentation(EObject object) {
    var node = null as ICompositeNode
    
    
    if (object instanceof VariableReference) {
      /*
       * buffer, buffer.slot, .slot, pattern
       */
      if (object.buffer === null) {
        val name = object.name
        if (isPattern(name, object))
          node = NodeModelUtils.findActualNodeFor(patternDefinition(name, object))
        else if (isBuffer(name, object))
          node = NodeModelUtils.findActualNodeFor(bufferDefinition(name, object))
      } 
    } else {
      node = NodeModelUtils.findActualNodeFor(object)
    }

    if (node === null)
      return ""

    return '<pre>' + node.text + '</pre>'
  }

  override getFirstLine(EObject object) {

    return super.getFirstLine(object)
  }

}
