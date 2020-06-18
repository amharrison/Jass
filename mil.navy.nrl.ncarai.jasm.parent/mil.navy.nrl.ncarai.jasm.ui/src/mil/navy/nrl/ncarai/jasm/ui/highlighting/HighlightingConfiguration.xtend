package mil.navy.nrl.ncarai.jasm.ui.highlighting

import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.RGB
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor
import org.eclipse.xtext.ui.editor.utils.TextStyle

class HighlightingConfiguration extends DefaultHighlightingConfiguration {
  static public val ID_ID = "id"
  static public val TYPE_ID = "type"
  static public val VARIABLE_ID = "variable"
  static public val SLOT_ID = "slot"
  static public val BUFFER_ID = "buffer"
  static public val FUNCTION_ID = "function"

  override configure(IHighlightingConfigurationAcceptor acceptor) {
    super.configure(acceptor)
    acceptor.acceptDefaultHighlighting(ID_ID, "Instance", chunkStyle)
    acceptor.acceptDefaultHighlighting(TYPE_ID, "ChunkType", chunkStyle.copy)
    acceptor.acceptDefaultHighlighting(VARIABLE_ID, "Variables", variableStyle)
    acceptor.acceptDefaultHighlighting(SLOT_ID, "Slot", slotStyle)
    acceptor.acceptDefaultHighlighting(BUFFER_ID, "Buffer", bufferStyle)
    acceptor.acceptDefaultHighlighting(FUNCTION_ID, "Function", chunkStyle.copy)
  }

  def TextStyle variableStyle() {
    val t = defaultTextStyle().copy();
    t.setColor(new RGB(0x99, 0x33, 0));
    t.setStyle(SWT.ITALIC);
    return t;
  }

  def TextStyle slotStyle() {
    val t = defaultTextStyle().copy();
    t.setColor(new RGB(0, 0x66, 0));
    return t;
  }

  def TextStyle chunkStyle() {
    val t = defaultTextStyle().copy();
    t.setColor(new RGB(0x66, 0x66, 0xcc));
    return t;
  }

  def TextStyle chunkTypeStyle() {
    val t = defaultTextStyle().copy();
    t.setColor(new RGB(0, 0, 0x99));
    return t;
  }

  def TextStyle bufferStyle() {
    val t = defaultTextStyle().copy();
    t.setColor(new RGB(0x99, 0, 0x99));
//    t.setStyle(SWT.BOLD);
    return t;
  }
}
