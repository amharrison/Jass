/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.eclipse.emf.common.util.EList;

import org.jactr.io2.jactr.modelFragment.Buffer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remove Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RemoveStatement#getBuffers <em>Buffers</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRemoveStatement()
 * @model
 * @generated
 */
public interface RemoveStatement extends Instruction
{
  /**
   * Returns the value of the '<em><b>Buffers</b></em>' reference list.
   * The list contents are of type {@link org.jactr.io2.jactr.modelFragment.Buffer}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Buffers</em>' reference list.
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRemoveStatement_Buffers()
   * @model
   * @generated
   */
  EList<Buffer> getBuffers();

} // RemoveStatement
