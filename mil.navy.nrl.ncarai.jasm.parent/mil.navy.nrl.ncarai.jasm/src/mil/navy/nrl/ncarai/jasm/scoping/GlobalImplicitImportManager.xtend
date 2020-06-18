package mil.navy.nrl.ncarai.jasm.scoping

import com.google.inject.Provider
import java.util.function.Consumer
import javax.inject.Singleton

@Singleton
class GlobalImplicitImportManager implements Provider<String>, Consumer<String> {
  
  String _globalImport
  
  override get() {
    _globalImport
  }
  
  override accept(String t) {
    _globalImport = t;
  }
  
   
}