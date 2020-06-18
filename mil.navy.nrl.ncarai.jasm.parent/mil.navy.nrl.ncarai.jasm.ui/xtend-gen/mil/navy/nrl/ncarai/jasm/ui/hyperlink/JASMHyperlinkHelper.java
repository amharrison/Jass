package mil.navy.nrl.ncarai.jasm.ui.hyperlink;

import com.google.common.collect.LinkedListMultimap;
import java.util.List;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.program.AsStatement;
import mil.navy.nrl.ncarai.jasm.program.Include;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;
import mil.navy.nrl.ncarai.jasm.program.RequestStatement;
import mil.navy.nrl.ncarai.jasm.program.VariableReference;
import mil.navy.nrl.ncarai.jasm.ui.internal.JasmActivator;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.xtext.ui.TypeAwareHyperlinkHelper;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.Import;
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class JASMHyperlinkHelper extends TypeAwareHyperlinkHelper {
  @Inject
  @Extension
  private ValidationUtilities _validationUtilities;
  
  @Override
  public void createHyperlinksByOffset(final XtextResource resource, final int offset, final IHyperlinkAcceptor acceptor) {
    final EObject astNode = this.getEObjectAtOffsetHelper().resolveElementAt(resource, offset);
    if ((astNode instanceof Value)) {
      String _name = ((Value)astNode).getName();
      boolean _tripleNotEquals = (_name != null);
      if (_tripleNotEquals) {
        final ICompositeNode parseNode = NodeModelUtils.findActualNodeFor(astNode);
        final EObject symbol = this.getSymbol(astNode, ((Value)astNode).getName());
        if ((symbol != null)) {
          this.createHyperlinksTo(resource, parseNode, symbol, acceptor);
        }
      }
    } else {
      if ((astNode instanceof Import)) {
        String importSource = ((Import)astNode).getImportedNamespace();
        int _length = importSource.length();
        int _minus = (_length - 2);
        importSource = importSource.substring(0, _minus);
        final ICompositeNode parseNode_1 = NodeModelUtils.findActualNodeFor(astNode);
        final EObject symbol_1 = this.getNamespaceOrPackage(astNode, importSource);
        if ((symbol_1 != null)) {
          this.createHyperlinksTo(resource, parseNode_1, symbol_1, acceptor);
        }
      } else {
        if ((astNode instanceof Include)) {
          String importSource_1 = ((Include)astNode).getImportedNamespace();
          int _length_1 = importSource_1.length();
          int _minus_1 = (_length_1 - 2);
          importSource_1 = importSource_1.substring(0, _minus_1);
          final ICompositeNode parseNode_2 = NodeModelUtils.findActualNodeFor(astNode);
          final EObject symbol_2 = this.getNamespaceOrPackage(astNode, importSource_1);
          if ((symbol_2 != null)) {
            this.createHyperlinksTo(resource, parseNode_2, symbol_2, acceptor);
          }
        } else {
          if ((astNode instanceof Pattern)) {
            final List<INode> featureNodes = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.PATTERN__TYPE);
            for (final INode featureNode : featureNodes) {
              boolean _contains = featureNode.getTotalTextRegion().contains(offset);
              if (_contains) {
                final String text = featureNode.getText();
                final EObject symbol_3 = this.getSymbol(astNode, text);
                if ((symbol_3 != null)) {
                  this.createHyperlinksTo(resource, featureNode, symbol_3, acceptor);
                }
              } else {
                super.createHyperlinksByOffset(resource, offset, acceptor);
              }
            }
          } else {
            if ((astNode instanceof AsStatement)) {
              final List<INode> featureNodes_1 = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.AS_STATEMENT__TYPE);
              for (final INode featureNode_1 : featureNodes_1) {
                boolean _contains_1 = featureNode_1.getTotalTextRegion().contains(offset);
                if (_contains_1) {
                  final String text_1 = featureNode_1.getText();
                  final EObject symbol_4 = this.getSymbol(astNode, text_1);
                  if ((symbol_4 != null)) {
                    this.createHyperlinksTo(resource, featureNode_1, symbol_4, acceptor);
                  }
                }
              }
            } else {
              if ((astNode instanceof RequestStatement)) {
                final List<INode> featureNodes_2 = NodeModelUtils.findNodesForFeature(astNode, ProgramPackage.Literals.REQUEST_STATEMENT__CAST);
                for (final INode featureNode_2 : featureNodes_2) {
                  boolean _contains_2 = featureNode_2.getTotalTextRegion().contains(offset);
                  if (_contains_2) {
                    final String text_2 = featureNode_2.getText();
                    final EObject symbol_5 = this.getSymbol(astNode, text_2);
                    if ((symbol_5 != null)) {
                      this.createHyperlinksTo(resource, featureNode_2, symbol_5, acceptor);
                    }
                  }
                }
              } else {
                if ((astNode instanceof VariableReference)) {
                  Buffer _buffer = ((VariableReference)astNode).getBuffer();
                  boolean _tripleEquals = (_buffer == null);
                  if (_tripleEquals) {
                    final ICompositeNode parseNode_3 = NodeModelUtils.findActualNodeFor(astNode);
                    final EObject symbol_6 = this.getSymbol(astNode, ((VariableReference)astNode).getName());
                    if ((symbol_6 != null)) {
                      this.createHyperlinksTo(resource, parseNode_3, symbol_6, acceptor);
                    }
                  }
                } else {
                  super.createHyperlinksByOffset(resource, offset, acceptor);
                }
              }
            }
          }
        }
      }
    }
  }
  
  public EObject getNamespaceOrPackage(final EObject astNode, final String name) {
    final LinkedListMultimap<String, IEObjectDescription> table = this._validationUtilities.packageTable(astNode);
    IEObjectDescription descriptor = IterableExtensions.<IEObjectDescription>last(table.get(name));
    if ((descriptor != null)) {
      return this._validationUtilities.toObject(descriptor, astNode.eResource().getResourceSet());
    }
    return null;
  }
  
  public EObject getSymbol(final EObject astNode, final String name) {
    try {
      IEObjectDescription descriptor = this._validationUtilities.findInTypeTable(astNode, ProgramPackage.Literals.FUNCTION, name);
      if ((descriptor == null)) {
        descriptor = this._validationUtilities.findInTypeTable(astNode, ModelFragmentPackage.Literals.BUFFER, name);
      }
      if ((descriptor == null)) {
        descriptor = this._validationUtilities.findInTypeTable(astNode, ProgramPackage.Literals.PATTERN, name);
      }
      if ((descriptor == null)) {
        descriptor = this._validationUtilities.findInTypeTable(astNode, ModelFragmentPackage.Literals.CHUNK_TYPE, name);
      }
      if ((descriptor == null)) {
        descriptor = this._validationUtilities.findInTypeTable(astNode, ModelFragmentPackage.Literals.CHUNK_DEF, name);
      }
      return this._validationUtilities.toObject(descriptor, astNode.eResource().getResourceSet());
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        ILog _log = JasmActivator.getInstance().getLog();
        Status _status = new Status(IStatus.ERROR, JasmActivator.PLUGIN_ID, ("Failed to find descriptor for " + name), e);
        _log.log(_status);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
}
