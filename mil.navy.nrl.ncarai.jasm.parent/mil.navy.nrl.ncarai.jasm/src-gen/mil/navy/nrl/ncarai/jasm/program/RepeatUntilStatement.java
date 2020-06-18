/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repeat Until Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement#getRepeat <em>Repeat</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement#getUntil <em>Until</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRepeatUntilStatement()
 * @model
 * @generated
 */
public interface RepeatUntilStatement extends Instruction
{
  /**
   * Returns the value of the '<em><b>Repeat</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repeat</em>' containment reference.
   * @see #setRepeat(Repeat)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRepeatUntilStatement_Repeat()
   * @model containment="true"
   * @generated
   */
  Repeat getRepeat();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement#getRepeat <em>Repeat</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Repeat</em>' containment reference.
   * @see #getRepeat()
   * @generated
   */
  void setRepeat(Repeat value);

  /**
   * Returns the value of the '<em><b>Until</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Until</em>' containment reference.
   * @see #setUntil(Until)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRepeatUntilStatement_Until()
   * @model containment="true"
   * @generated
   */
  Until getUntil();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement#getUntil <em>Until</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Until</em>' containment reference.
   * @see #getUntil()
   * @generated
   */
  void setUntil(Until value);

} // RepeatUntilStatement
