/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.jactr.io2.jactr.modelFragment.Buffer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#isForced <em>Forced</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getBuffer <em>Buffer</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getPatternReference <em>Pattern Reference</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getBindings <em>Bindings</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getCast <em>Cast</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getMapping <em>Mapping</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getHandler <em>Handler</em>}</li>
 * </ul>
 *
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement()
 * @model
 * @generated
 */
public interface RequestStatement extends Instruction
{
  /**
   * Returns the value of the '<em><b>Forced</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Forced</em>' attribute.
   * @see #setForced(boolean)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Forced()
   * @model
   * @generated
   */
  boolean isForced();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#isForced <em>Forced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Forced</em>' attribute.
   * @see #isForced()
   * @generated
   */
  void setForced(boolean value);

  /**
   * Returns the value of the '<em><b>Buffer</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Buffer</em>' reference.
   * @see #setBuffer(Buffer)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Buffer()
   * @model
   * @generated
   */
  Buffer getBuffer();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getBuffer <em>Buffer</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Buffer</em>' reference.
   * @see #getBuffer()
   * @generated
   */
  void setBuffer(Buffer value);

  /**
   * Returns the value of the '<em><b>Pattern Reference</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pattern Reference</em>' containment reference.
   * @see #setPatternReference(VariableReference)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_PatternReference()
   * @model containment="true"
   * @generated
   */
  VariableReference getPatternReference();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getPatternReference <em>Pattern Reference</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pattern Reference</em>' containment reference.
   * @see #getPatternReference()
   * @generated
   */
  void setPatternReference(VariableReference value);

  /**
   * Returns the value of the '<em><b>Bindings</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bindings</em>' containment reference.
   * @see #setBindings(Bindings)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Bindings()
   * @model containment="true"
   * @generated
   */
  Bindings getBindings();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getBindings <em>Bindings</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Bindings</em>' containment reference.
   * @see #getBindings()
   * @generated
   */
  void setBindings(Bindings value);

  /**
   * Returns the value of the '<em><b>Cast</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Cast</em>' attribute.
   * @see #setCast(String)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Cast()
   * @model
   * @generated
   */
  String getCast();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getCast <em>Cast</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Cast</em>' attribute.
   * @see #getCast()
   * @generated
   */
  void setCast(String value);

  /**
   * Returns the value of the '<em><b>Mapping</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mapping</em>' containment reference.
   * @see #setMapping(Mapping)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Mapping()
   * @model containment="true"
   * @generated
   */
  Mapping getMapping();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getMapping <em>Mapping</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mapping</em>' containment reference.
   * @see #getMapping()
   * @generated
   */
  void setMapping(Mapping value);

  /**
   * Returns the value of the '<em><b>Handler</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Handler</em>' containment reference.
   * @see #setHandler(Handler)
   * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage#getRequestStatement_Handler()
   * @model containment="true"
   * @generated
   */
  Handler getHandler();

  /**
   * Sets the value of the '{@link mil.navy.nrl.ncarai.jasm.program.RequestStatement#getHandler <em>Handler</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Handler</em>' containment reference.
   * @see #getHandler()
   * @generated
   */
  void setHandler(Handler value);

} // RequestStatement