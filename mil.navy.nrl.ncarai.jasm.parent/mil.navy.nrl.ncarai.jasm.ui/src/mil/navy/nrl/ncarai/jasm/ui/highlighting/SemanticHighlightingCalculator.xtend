package mil.navy.nrl.ncarai.jasm.ui.highlighting

import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.program.AsStatement
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement
import mil.navy.nrl.ncarai.jasm.program.BufferPattern
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage
import mil.navy.nrl.ncarai.jasm.program.RemoveStatement
import mil.navy.nrl.ncarai.jasm.program.RequestStatement
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator
import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor
import org.eclipse.xtext.util.CancelIndicator
import org.jactr.io2.jactr.modelFragment.ChunkDef
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Chunks
import org.jactr.io2.jactr.modelFragment.ConditionalSlot
import org.jactr.io2.jactr.modelFragment.ModelFragmentPackage
import org.jactr.io2.jactr.modelFragment.SimpleSlot
import org.jactr.io2.jactr.modelFragment.Value
import mil.navy.nrl.ncarai.jasm.program.VariableReference

class SemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {

  @Inject extension ValidationUtilities

  override highlightElement(EObject object, IHighlightedPositionAcceptor acceptor, CancelIndicator cancelIndicator) {

    // return true to skip the children
    if (object instanceof SimpleSlot)
      highlightFeature(acceptor, object, ModelFragmentPackage.Literals.SIMPLE_SLOT__NAME,
        HighlightingConfiguration.SLOT_ID)
    else if (object instanceof ConditionalSlot)
      highlightFeature(acceptor, object, ModelFragmentPackage.Literals.CONDITIONAL_SLOT__NAME,
        HighlightingConfiguration.SLOT_ID)
    else if (object instanceof AssignmentStatement) {

      if (object.assignment !== null)
        if (object.assignment.value !== null)
          if (object.assignment.buffer !== null) {
            highlightFeature(acceptor, object.assignment, ProgramPackage.Literals.VARIABLE_VALUE__BUFFER,
              HighlightingConfiguration.BUFFER_ID)
            highlightFeature(acceptor, object.assignment.value, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
              HighlightingConfiguration.SLOT_ID)
          } else if (isBuffer(object.assignment.value.name, object))
            highlightFeature(acceptor, object.assignment.value, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
              HighlightingConfiguration.BUFFER_ID)
          else if (isChunk(object.assignment.value.name, object))
            highlightFeature(acceptor, object.assignment.value, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
              HighlightingConfiguration.ID_ID)
          else
            highlightFeature(acceptor, object.assignment.value, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
              HighlightingConfiguration.SLOT_ID)

      if (object.reference !== null)
        if (object.reference.buffer !== null)
          highlightFeature(acceptor, object.reference, ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER,
            HighlightingConfiguration.BUFFER_ID)
        else
          highlightFeature(acceptor, object.reference, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
            HighlightingConfiguration.SLOT_ID)

    } else if (object instanceof Value) {
      if (object.name !== null) {
        if (object.name.startsWith("="))
          highlightFeature(acceptor, object, ModelFragmentPackage.Literals.VALUE__NAME,
            HighlightingConfiguration.VARIABLE_ID)
      }
    } else if (object instanceof ChunkType) {
      highlightFeature(acceptor, object, ModelFragmentPackage.Literals.OVERRIDABLE__NAME,
        HighlightingConfiguration.TYPE_ID)
    } else if (object instanceof Chunks) {
      highlightFeature(acceptor, object, ModelFragmentPackage.Literals.CHUNKS__TYPE, HighlightingConfiguration.TYPE_ID)
    } else if (object instanceof ChunkDef) {
      highlightFeature(acceptor, object, ModelFragmentPackage.Literals.OVERRIDABLE__NAME,
        HighlightingConfiguration.ID_ID)
    } else if (object instanceof Function) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.FUNCTION__NAME, HighlightingConfiguration.TYPE_ID)
    } else if (object instanceof AsStatement) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.AS_STATEMENT__BUFFER,
        HighlightingConfiguration.BUFFER_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.AS_STATEMENT__TYPE, HighlightingConfiguration.ID_ID)
    } else if (object instanceof BufferPattern) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.BUFFER_PATTERN__BUFFER,
        HighlightingConfiguration.BUFFER_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.BUFFER_PATTERN__PATTERN,
        HighlightingConfiguration.ID_ID)
    } else if (object instanceof Pattern) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.PATTERN__NAME, HighlightingConfiguration.ID_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.PATTERN__TYPE, HighlightingConfiguration.TYPE_ID)
    } else if (object instanceof RequestStatement) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__BUFFER,
        HighlightingConfiguration.BUFFER_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__PATTERN_REFERENCE,
        HighlightingConfiguration.ID_ID)
      if (object.cast !== null)
        highlightFeature(acceptor, object, ProgramPackage.Literals.REQUEST_STATEMENT__CAST,
          HighlightingConfiguration.TYPE_ID)
    } else if (object instanceof ForLoopStatement) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__FOR_BUFFER,
        HighlightingConfiguration.BUFFER_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__INITIAL,
        HighlightingConfiguration.ID_ID)
      highlightFeature(acceptor, object, ProgramPackage.Literals.FOR_LOOP_STATEMENT__SUBSEQUENT,
        HighlightingConfiguration.ID_ID)
    } else if (object instanceof ReturnStatement) {
      if (object.reference !== null) {
        if (object.reference.buffer !== null) {
          highlightFeature(acceptor, object.reference, ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER,
            HighlightingConfiguration.BUFFER_ID)
          highlightFeature(acceptor, object.reference, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
            HighlightingConfiguration.SLOT_ID)
        } else
          highlightFeature(acceptor, object.reference, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME,
            HighlightingConfiguration.BUFFER_ID)
      }
    } else if (object instanceof RemoveStatement) {
      highlightFeature(acceptor, object, ProgramPackage.Literals.REMOVE_STATEMENT__BUFFERS,
        HighlightingConfiguration.BUFFER_ID)
    } else if (object instanceof VariableReference) {
      var color = HighlightingConfiguration.SLOT_ID
      if (object.buffer === null) {
        if (isBuffer(object.name, object))
          color = HighlightingConfiguration.BUFFER_ID
        else if (isPattern(object.name, object) || isChunk(object.name, object))
          color = HighlightingConfiguration.ID_ID
      } else {
        highlightFeature(acceptor, object, ProgramPackage.Literals.VARIABLE_REFERENCE__BUFFER,
          HighlightingConfiguration.BUFFER_ID)
        // buffer will be taken care of, we want the slot color
        color = HighlightingConfiguration.SLOT_ID
      }

      highlightFeature(acceptor, object, ProgramPackage.Literals.VARIABLE_REFERENCE__NAME, color)
    }
    return false
  }
}
