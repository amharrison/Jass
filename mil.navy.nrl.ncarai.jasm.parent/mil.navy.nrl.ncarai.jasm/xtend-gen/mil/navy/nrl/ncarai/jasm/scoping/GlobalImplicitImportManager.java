package mil.navy.nrl.ncarai.jasm.scoping;

import com.google.inject.Provider;
import java.util.function.Consumer;
import javax.inject.Singleton;

@Singleton
@SuppressWarnings("all")
public class GlobalImplicitImportManager implements Provider<String>, Consumer<String> {
  private String _globalImport;
  
  @Override
  public String get() {
    return this._globalImport;
  }
  
  @Override
  public void accept(final String t) {
    this._globalImport = t;
  }
}
