package mil.navy.nrl.ncarai.jasm.scoping

import com.google.inject.Inject
import java.util.List
import org.eclipse.xtext.scoping.impl.ImportNormalizer
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider

class LocalScopingProvider extends ImportedNamespaceAwareLocalScopeProvider {

  @Inject GlobalImplicitImportManager _importManager

  override List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
    val list = newArrayList
    if (_importManager.get !== null)
      list.add(createImportedNamespaceResolver(_importManager.get, false))
    list.add(createImportedNamespaceResolver("jasm.procedural.Buffers.*", false))
    return list
  }
}
