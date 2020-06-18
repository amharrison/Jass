/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>With As Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.WithAsStatement#getAssumed <em>Assumed</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getWithAsStatement()
 * @model
 * @generated
 */
public interface WithAsStatement extends EObject
{
  /**
   * Returns the value of the '<em><b>Assumed</b></em>' containment reference list.
   * The list contents are of type {@link mil.navy.nrl.ncarai.jasm.program.AsStatement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Assumed</em>' containment reference list.
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getWithAsStatement_Assumed()
   * @model containment="true"
   * @generated
   */
  EList<AsStatement> getAssumed();

} // WithAsStatement
