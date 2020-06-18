/*
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm

import com.google.inject.Binder
import com.google.inject.Provider
import com.google.inject.name.Names
import mil.navy.nrl.ncarai.jasm.jactrio2.IModelBuilder
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.DefaultProceduralModelBuilder
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IGoalManipulator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IReferenceResolver
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultGoalManipulator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultReferenceResolver
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.InstructionDispatcher
import mil.navy.nrl.ncarai.jasm.scoping.GlobalImplicitImportManager
import mil.navy.nrl.ncarai.jasm.scoping.LocalScopingProvider
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.xtext.conversion.IValueConverterService
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.jactr.io2.jactr.naming.QualifiedNameProvider
import org.jactr.io2.jactr.util.LoadDelegate
import org.jactr.io2.jactr.validation.DefaultClassNameValidator
import org.jactr.io2.jactr.validation.IClassNameValidator
import org.jactr.io2.jactr.validation.ModelFragmentUtil
import org.jactr.io2.jactr.value.ModelFragmentConverters

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class ProgramRuntimeModule extends AbstractProgramRuntimeModule {
  def bindModelFragmentUtil() {
    return ModelFragmentUtil
  }
  

  def Class<? extends IClassNameValidator> bindIClassNameValidator() {
    return DefaultClassNameValidator
  }

  override bindIQualifiedNameProvider() {
    QualifiedNameProvider
  }
  
  def Class<? extends IGoalManipulator> bindIGoalManipulator(){
    DefaultGoalManipulator
  }
  
  def Provider<? extends String> provideGlobalImplicitImportManager(){
    new GlobalImplicitImportManager
  }
  
  def Class<? extends InstructionDispatcher> bindInstructionDispatcher(){
    InstructionDispatcher
  }

  override Class<? extends IValueConverterService> bindIValueConverterService() {
    ModelFragmentConverters
  }
  
  def Class<? extends IModelBuilder> bindIModelBuilder(){
    DefaultProceduralModelBuilder
  }
  
  def Class<? extends IReferenceResolver> bindIReferenceResolver(){
    DefaultReferenceResolver
  }
  
  override configureIScopeProviderDelegate(Binder binder) {
    binder.bind(IScopeProvider).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(
      LocalScopingProvider);
  }
  
  def Class<? extends ValidationUtilities> bindValidationUtilities(){
    ValidationUtilities
  }
  
  def Class<? extends LoadDelegate> bindLoadDelegate(){
    LoadDelegate
  }
  
  
}
