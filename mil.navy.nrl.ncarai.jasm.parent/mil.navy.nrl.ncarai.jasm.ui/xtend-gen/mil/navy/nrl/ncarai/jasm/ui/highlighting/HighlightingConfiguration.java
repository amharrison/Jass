package mil.navy.nrl.ncarai.jasm.ui.highlighting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

@SuppressWarnings("all")
public class HighlightingConfiguration extends DefaultHighlightingConfiguration {
  public static final String ID_ID = "id";
  
  public static final String TYPE_ID = "type";
  
  public static final String VARIABLE_ID = "variable";
  
  public static final String SLOT_ID = "slot";
  
  public static final String BUFFER_ID = "buffer";
  
  public static final String FUNCTION_ID = "function";
  
  @Override
  public void configure(final IHighlightingConfigurationAcceptor acceptor) {
    super.configure(acceptor);
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.ID_ID, "Instance", this.chunkStyle());
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.TYPE_ID, "ChunkType", this.chunkStyle().copy());
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.VARIABLE_ID, "Variables", this.variableStyle());
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.SLOT_ID, "Slot", this.slotStyle());
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.BUFFER_ID, "Buffer", this.bufferStyle());
    acceptor.acceptDefaultHighlighting(HighlightingConfiguration.FUNCTION_ID, "Function", this.chunkStyle().copy());
  }
  
  public TextStyle variableStyle() {
    final TextStyle t = this.defaultTextStyle().copy();
    RGB _rGB = new RGB(0x99, 0x33, 0);
    t.setColor(_rGB);
    t.setStyle(SWT.ITALIC);
    return t;
  }
  
  public TextStyle slotStyle() {
    final TextStyle t = this.defaultTextStyle().copy();
    RGB _rGB = new RGB(0, 0x66, 0);
    t.setColor(_rGB);
    return t;
  }
  
  public TextStyle chunkStyle() {
    final TextStyle t = this.defaultTextStyle().copy();
    RGB _rGB = new RGB(0x66, 0x66, 0xcc);
    t.setColor(_rGB);
    return t;
  }
  
  public TextStyle chunkTypeStyle() {
    final TextStyle t = this.defaultTextStyle().copy();
    RGB _rGB = new RGB(0, 0, 0x99);
    t.setColor(_rGB);
    return t;
  }
  
  public TextStyle bufferStyle() {
    final TextStyle t = this.defaultTextStyle().copy();
    RGB _rGB = new RGB(0x99, 0, 0x99);
    t.setColor(_rGB);
    return t;
  }
}
