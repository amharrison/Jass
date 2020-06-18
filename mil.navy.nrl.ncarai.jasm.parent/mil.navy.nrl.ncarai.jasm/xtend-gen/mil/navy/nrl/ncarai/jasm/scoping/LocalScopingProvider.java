package mil.navy.nrl.ncarai.jasm.scoping;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.scoping.GlobalImplicitImportManager;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class LocalScopingProvider extends ImportedNamespaceAwareLocalScopeProvider {
  @Inject
  private GlobalImplicitImportManager _importManager;
  
  @Override
  public List<ImportNormalizer> getImplicitImports(final boolean ignoreCase) {
    final ArrayList<ImportNormalizer> list = CollectionLiterals.<ImportNormalizer>newArrayList();
    String _get = this._importManager.get();
    boolean _tripleNotEquals = (_get != null);
    if (_tripleNotEquals) {
      list.add(this.createImportedNamespaceResolver(this._importManager.get(), false));
    }
    list.add(this.createImportedNamespaceResolver("jasm.procedural.Buffers.*", false));
    return list;
  }
}
