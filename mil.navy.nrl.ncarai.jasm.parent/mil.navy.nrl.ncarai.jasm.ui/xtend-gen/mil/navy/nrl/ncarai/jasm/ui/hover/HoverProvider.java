package mil.navy.nrl.ncarai.jasm.ui.hover;

import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.program.VariableReference;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.hover.html.IEObjectHoverDocumentationProvider;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jactr.io2.jactr.modelFragment.Buffer;

@SuppressWarnings("all")
public class HoverProvider extends DefaultEObjectHoverProvider implements IEObjectHoverDocumentationProvider, IEObjectHoverProvider {
  @Inject
  @Extension
  private ValidationUtilities _validationUtilities;
  
  @Override
  public String getDocumentation(final EObject object) {
    ICompositeNode node = ((ICompositeNode) null);
    if ((object instanceof VariableReference)) {
      Buffer _buffer = ((VariableReference)object).getBuffer();
      boolean _tripleEquals = (_buffer == null);
      if (_tripleEquals) {
        final String name = ((VariableReference)object).getName();
        boolean _isPattern = this._validationUtilities.isPattern(name, object);
        if (_isPattern) {
          node = NodeModelUtils.findActualNodeFor(this._validationUtilities.patternDefinition(name, object));
        } else {
          boolean _isBuffer = this._validationUtilities.isBuffer(name, object);
          if (_isBuffer) {
            node = NodeModelUtils.findActualNodeFor(this._validationUtilities.bufferDefinition(name, object));
          }
        }
      }
    } else {
      node = NodeModelUtils.findActualNodeFor(object);
    }
    if ((node == null)) {
      return "";
    }
    String _text = node.getText();
    String _plus = ("<pre>" + _text);
    return (_plus + "</pre>");
  }
  
  @Override
  public String getFirstLine(final EObject object) {
    return super.getFirstLine(object);
  }
}
