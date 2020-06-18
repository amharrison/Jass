/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getBindings <em>Bindings</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getPriority <em>Priority</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getBlock <em>Block</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getCaseStatement()
 * @model
 * @generated
 */
public interface CaseStatement extends EObject
{
  /**
   * Returns the value of the '<em><b>Bindings</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bindings</em>' containment reference.
   * @see #setBindings(Bindings)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getCaseStatement_Bindings()
   * @model containment="true"
   * @generated
   */
  Bindings getBindings();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getBindings <em>Bindings</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Bindings</em>' containment reference.
   * @see #getBindings()
   * @generated
   */
  void setBindings(Bindings value);

  /**
   * Returns the value of the '<em><b>Priority</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Priority</em>' attribute.
   * @see #setPriority(Double)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getCaseStatement_Priority()
   * @model
   * @generated
   */
  Double getPriority();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getPriority <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Priority</em>' attribute.
   * @see #getPriority()
   * @generated
   */
  void setPriority(Double value);

  /**
   * Returns the value of the '<em><b>Block</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Block</em>' containment reference.
   * @see #setBlock(Block)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getCaseStatement_Block()
   * @model containment="true"
   * @generated
   */
  Block getBlock();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.CaseStatement#getBlock <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Block</em>' containment reference.
   * @see #getBlock()
   * @generated
   */
  void setBlock(Block value);

} // CaseStatement
