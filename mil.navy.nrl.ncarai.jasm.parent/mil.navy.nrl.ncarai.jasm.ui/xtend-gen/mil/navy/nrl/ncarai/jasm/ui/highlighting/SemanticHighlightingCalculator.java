package mil.navy.nrl.ncarai.jasm.ui.highlighting;

import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.program.AsStatement;
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;
import mil.navy.nrl.ncarai.jasm.program.RemoveStatement;
import mil.navy.nrl.ncarai.jasm.program.RequestStatement;
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement;
import mil.navy.nrl.ncarai.jasm.program.ReturnVariableReference;
import mil.navy.nrl.ncarai.jasm.program.VariableReference;
import mil.navy.nrl.ncarai.jasm.program.VariableValue;
import mil.navy.nrl.ncarai.jasm.ui.highlighting.HighlightingConfiguration;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Chunks;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class SemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
  @Inject
  @Extension
  private ValidationUtilities _validationUtilities;
  
  @Override
  public boolean highlightElement(final EObject object, final IHighlightedPositionAcceptor acceptor, final CancelIndicator cancelIndicator) {
    if ((object instanceof SimpleSlot)) {
      this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.SIMPLE_SLOT__NAME, 
        HighlightingConfiguration.SLOT_ID);
    } else {
      if ((object instanceof ConditionalSlot)) {
        this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.CONDITIONAL_SLOT__NAME, 
          HighlightingConfiguration.SLOT_ID);
      } else {
        if ((object instanceof AssignmentStatement)) {
          VariableValue _assignment = ((AssignmentStatement)object).getAssignment();
          boolean _tripleNotEquals = (_assignment != null);
          if (_tripleNotEquals) {
            Value _value = ((AssignmentStatement)object).getAssignment().getValue();
            boolean _tripleNotEquals_1 = (_value != null);
            if (_tripleNotEquals_1) {
              Buffer _buffer = ((AssignmentStatement)object).getAssignment().getBuffer();
              boolean _tripleNotEquals_2 = (_buffer != null);
              if (_tripleNotEquals_2) {
                this.highlightFeature(acceptor, ((AssignmentStatement)object).getAssignment(), ProgramPackage.Literals.VARIABLE_VALUE__BUFFER, 
                  HighlightingConfiguration.BUFFER_ID);
                this.highlightFeature(acceptor, ((AssignmentStatement)object).getAssignment().getValue(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                  HighlightingConfiguration.SLOT_ID);
              } else {
                boolean _isBuffer = this._validationUtilities.isBuffer(((AssignmentStatement)object).getAssignment().getValue().getName(), object);
                if (_isBuffer) {
                  this.highlightFeature(acceptor, ((AssignmentStatement)object).getAssignment().getValue(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                    HighlightingConfiguration.BUFFER_ID);
                } else {
                  boolean _isChunk = this._validationUtilities.isChunk(((AssignmentStatement)object).getAssignment().getValue().getName(), object);
                  if (_isChunk) {
                    this.highlightFeature(acceptor, ((AssignmentStatement)object).getAssignment().getValue(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                      HighlightingConfiguration.ID_ID);
                  } else {
                    this.highlightFeature(acceptor, ((AssignmentStatement)object).getAssignment().getValue(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                      HighlightingConfiguration.SLOT_ID);
                  }
                }
              }
            }
          }
          VariableReference _reference = ((AssignmentStatement)object).getReference();
          boolean _tripleNotEquals_3 = (_reference != null);
          if (_tripleNotEquals_3) {
            Buffer _buffer_1 = ((AssignmentStatement)object).getReference().getBuffer();
            boolean _tripleNotEquals_4 = (_buffer_1 != null);
            if (_tripleNotEquals_4) {
              this.highlightFeature(acceptor, ((AssignmentStatement)object).getReference(), ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER, 
                HighlightingConfiguration.BUFFER_ID);
            } else {
              this.highlightFeature(acceptor, ((AssignmentStatement)object).getReference(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                HighlightingConfiguration.SLOT_ID);
            }
          }
        } else {
          if ((object instanceof Value)) {
            String _name = ((Value)object).getName();
            boolean _tripleNotEquals_5 = (_name != null);
            if (_tripleNotEquals_5) {
              boolean _startsWith = ((Value)object).getName().startsWith("=");
              if (_startsWith) {
                this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.VALUE__NAME, 
                  HighlightingConfiguration.VARIABLE_ID);
              }
            }
          } else {
            if ((object instanceof ChunkType)) {
              this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.OVERRIDABLE__NAME, 
                HighlightingConfiguration.TYPE_ID);
            } else {
              if ((object instanceof Chunks)) {
                this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.CHUNKS__TYPE, HighlightingConfiguration.TYPE_ID);
              } else {
                if ((object instanceof ChunkDef)) {
                  this.highlightFeature(acceptor, object, ModelFragmentPackage.Literals.OVERRIDABLE__NAME, 
                    HighlightingConfiguration.ID_ID);
                } else {
                  if ((object instanceof Function)) {
                    this.highlightFeature(acceptor, object, ProgramPackage.Literals.FUNCTION__NAME, HighlightingConfiguration.TYPE_ID);
                  } else {
                    if ((object instanceof AsStatement)) {
                      this.highlightFeature(acceptor, object, ProgramPackage.Literals.AS_STATEMENT__BUFFER, 
                        HighlightingConfiguration.BUFFER_ID);
                      this.highlightFeature(acceptor, object, ProgramPackage.Literals.AS_STATEMENT__TYPE, HighlightingConfiguration.ID_ID);
                    } else {
                      if ((object instanceof BufferPattern)) {
                        this.highlightFeature(acceptor, object, ProgramPackage.Literals.BUFFER_PATTERN__BUFFER, 
                          HighlightingConfiguration.BUFFER_ID);
                        this.highlightFeature(acceptor, object, ProgramPackage.Literals.BUFFER_PATTERN__PATTERN, 
                          HighlightingConfiguration.ID_ID);
                      } else {
                        if ((object instanceof Pattern)) {
                          this.highlightFeature(acceptor, object, ProgramPackage.Literals.PATTERN__NAME, HighlightingConfiguration.ID_ID);
                          this.highlightFeature(acceptor, object, ProgramPackage.Literals.PATTERN__TYPE, HighlightingConfiguration.TYPE_ID);
                        } else {
                          if ((object instanceof RequestStatement)) {
                            this.highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER, 
                              HighlightingConfiguration.BUFFER_ID);
                            this.highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE, 
                              HighlightingConfiguration.ID_ID);
                            String _cast = ((RequestStatement)object).getCast();
                            boolean _tripleNotEquals_6 = (_cast != null);
                            if (_tripleNotEquals_6) {
                              this.highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__CAST, 
                                HighlightingConfiguration.TYPE_ID);
                            }
                          } else {
                            if ((object instanceof ForLoopStatement)) {
                              this.highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__FOR_BUFFER, 
                                HighlightingConfiguration.BUFFER_ID);
                              this.highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__INITIAL, 
                                HighlightingConfiguration.ID_ID);
                              this.highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__SUBSEQUENT, 
                                HighlightingConfiguration.ID_ID);
                            } else {
                              if ((object instanceof ReturnStatement)) {
                                ReturnVariableReference _reference_1 = ((ReturnStatement)object).getReference();
                                boolean _tripleNotEquals_7 = (_reference_1 != null);
                                if (_tripleNotEquals_7) {
                                  Buffer _buffer_2 = ((ReturnStatement)object).getReference().getBuffer();
                                  boolean _tripleNotEquals_8 = (_buffer_2 != null);
                                  if (_tripleNotEquals_8) {
                                    this.highlightFeature(acceptor, ((ReturnStatement)object).getReference(), ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER, 
                                      HighlightingConfiguration.BUFFER_ID);
                                    this.highlightFeature(acceptor, ((ReturnStatement)object).getReference(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                                      HighlightingConfiguration.SLOT_ID);
                                  } else {
                                    this.highlightFeature(acceptor, ((ReturnStatement)object).getReference(), ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, 
                                      HighlightingConfiguration.BUFFER_ID);
                                  }
                                }
                              } else {
                                if ((object instanceof RemoveStatement)) {
                                  this.highlightFeature(acceptor, object, ProgramPackage.Literals.REMOVE_STATEMENT__BUFFERS, 
                                    HighlightingConfiguration.BUFFER_ID);
                                } else {
                                  if ((object instanceof VariableReference)) {
                                    String color = HighlightingConfiguration.SLOT_ID;
                                    Buffer _buffer_3 = ((VariableReference)object).getBuffer();
                                    boolean _tripleEquals = (_buffer_3 == null);
                                    if (_tripleEquals) {
                                      boolean _isBuffer_1 = this._validationUtilities.isBuffer(((VariableReference)object).getName(), object);
                                      if (_isBuffer_1) {
                                        color = HighlightingConfiguration.BUFFER_ID;
                                      } else {
                                        if ((this._validationUtilities.isPattern(((VariableReference)object).getName(), object) || this._validationUtilities.isChunk(((VariableReference)object).getName(), object))) {
                                          color = HighlightingConfiguration.ID_ID;
                                        }
                                      }
                                    } else {
                                      this.highlightFeature(acceptor, object, ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER, 
                                        HighlightingConfiguration.BUFFER_ID);
                                      color = HighlightingConfiguration.SLOT_ID;
                                    }
                                    this.highlightFeature(acceptor, object, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, color);
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return false;
  }
}
