/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insertion Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#getEntryState <em>Entry State</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#getExitState <em>Exit State</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#isShouldGenerate <em>Should Generate</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getInsertionStatement()
 * @model
 * @generated
 */
public interface InsertionStatement extends Instruction
{
  /**
   * Returns the value of the '<em><b>Entry State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Entry State</em>' attribute.
   * @see #setEntryState(String)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getInsertionStatement_EntryState()
   * @model
   * @generated
   */
  String getEntryState();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#getEntryState <em>Entry State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Entry State</em>' attribute.
   * @see #getEntryState()
   * @generated
   */
  void setEntryState(String value);

  /**
   * Returns the value of the '<em><b>Exit State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exit State</em>' attribute.
   * @see #setExitState(String)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getInsertionStatement_ExitState()
   * @model
   * @generated
   */
  String getExitState();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#getExitState <em>Exit State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exit State</em>' attribute.
   * @see #getExitState()
   * @generated
   */
  void setExitState(String value);

  /**
   * Returns the value of the '<em><b>Should Generate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Should Generate</em>' attribute.
   * @see #setShouldGenerate(boolean)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getInsertionStatement_ShouldGenerate()
   * @model
   * @generated
   */
  boolean isShouldGenerate();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.InsertionStatement#isShouldGenerate <em>Should Generate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Should Generate</em>' attribute.
   * @see #isShouldGenerate()
   * @generated
   */
  void setShouldGenerate(boolean value);

} // InsertionStatement
