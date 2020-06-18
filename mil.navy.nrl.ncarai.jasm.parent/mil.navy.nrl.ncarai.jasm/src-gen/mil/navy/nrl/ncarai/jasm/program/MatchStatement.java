/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Match Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.MatchStatement#getCases <em>Cases</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.MatchStatement#getDefaultBlock <em>Default Block</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getMatchStatement()
 * @model
 * @generated
 */
public interface MatchStatement extends Instruction
{
  /**
   * Returns the value of the '<em><b>Cases</b></em>' containment reference list.
   * The list contents are of type {@link mil.navy.nrl.ncarai.jasm.program.CaseStatement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Cases</em>' containment reference list.
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getMatchStatement_Cases()
   * @model containment="true"
   * @generated
   */
  EList<CaseStatement> getCases();

  /**
   * Returns the value of the '<em><b>Default Block</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Block</em>' containment reference.
   * @see #setDefaultBlock(Block)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getMatchStatement_DefaultBlock()
   * @model containment="true"
   * @generated
   */
  Block getDefaultBlock();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.MatchStatement#getDefaultBlock <em>Default Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Block</em>' containment reference.
   * @see #getDefaultBlock()
   * @generated
   */
  void setDefaultBlock(Block value);

} // MatchStatement
