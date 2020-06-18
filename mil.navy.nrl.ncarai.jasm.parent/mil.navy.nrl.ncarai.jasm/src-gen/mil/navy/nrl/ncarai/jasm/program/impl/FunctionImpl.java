/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program.impl;

import java.util.Collection;

import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;
import mil.navy.nrl.ncarai.jasm.program.RetainAsStatement;
import mil.navy.nrl.ncarai.jasm.program.VariableStatement;
import mil.navy.nrl.ncarai.jasm.program.WithAsStatement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#isVoid <em>Void</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#isChunk <em>Chunk</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getName <em>Name</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getSlots <em>Slots</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getAssumedContents <em>Assumed Contents</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getRetainedContents <em>Retained Contents</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.FunctionImpl#getInstructions <em>Instructions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionImpl extends MinimalEObjectImpl.Container implements Function
{
  /**
   * The default value of the '{@link #isVoid() <em>Void</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVoid()
   * @generated
   * @ordered
   */
  protected static final boolean VOID_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isVoid() <em>Void</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVoid()
   * @generated
   * @ordered
   */
  protected boolean void_ = VOID_EDEFAULT;

  /**
   * The default value of the '{@link #isChunk() <em>Chunk</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isChunk()
   * @generated
   * @ordered
   */
  protected static final boolean CHUNK_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isChunk() <em>Chunk</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isChunk()
   * @generated
   * @ordered
   */
  protected boolean chunk = CHUNK_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getSlots() <em>Slots</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSlots()
   * @generated
   * @ordered
   */
  protected EList<VariableStatement> slots;

  /**
   * The cached value of the '{@link #getAssumedContents() <em>Assumed Contents</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssumedContents()
   * @generated
   * @ordered
   */
  protected WithAsStatement assumedContents;

  /**
   * The cached value of the '{@link #getRetainedContents() <em>Retained Contents</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRetainedContents()
   * @generated
   * @ordered
   */
  protected RetainAsStatement retainedContents;

  /**
   * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariables()
   * @generated
   * @ordered
   */
  protected EList<VariableStatement> variables;

  /**
   * The cached value of the '{@link #getInstructions() <em>Instructions</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstructions()
   * @generated
   * @ordered
   */
  protected EList<Instruction> instructions;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FunctionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ProgramPackage.Literals.FUNCTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isVoid()
  {
    return void_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVoid(boolean newVoid)
  {
    boolean oldVoid = void_;
    void_ = newVoid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__VOID, oldVoid, void_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isChunk()
  {
    return chunk;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setChunk(boolean newChunk)
  {
    boolean oldChunk = chunk;
    chunk = newChunk;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__CHUNK, oldChunk, chunk));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<VariableStatement> getSlots()
  {
    if (slots == null)
    {
      slots = new EObjectContainmentEList<VariableStatement>(VariableStatement.class, this, ProgramPackage.FUNCTION__SLOTS);
    }
    return slots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public WithAsStatement getAssumedContents()
  {
    return assumedContents;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAssumedContents(WithAsStatement newAssumedContents, NotificationChain msgs)
  {
    WithAsStatement oldAssumedContents = assumedContents;
    assumedContents = newAssumedContents;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__ASSUMED_CONTENTS, oldAssumedContents, newAssumedContents);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAssumedContents(WithAsStatement newAssumedContents)
  {
    if (newAssumedContents != assumedContents)
    {
      NotificationChain msgs = null;
      if (assumedContents != null)
        msgs = ((InternalEObject)assumedContents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FUNCTION__ASSUMED_CONTENTS, null, msgs);
      if (newAssumedContents != null)
        msgs = ((InternalEObject)newAssumedContents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FUNCTION__ASSUMED_CONTENTS, null, msgs);
      msgs = basicSetAssumedContents(newAssumedContents, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__ASSUMED_CONTENTS, newAssumedContents, newAssumedContents));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RetainAsStatement getRetainedContents()
  {
    return retainedContents;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRetainedContents(RetainAsStatement newRetainedContents, NotificationChain msgs)
  {
    RetainAsStatement oldRetainedContents = retainedContents;
    retainedContents = newRetainedContents;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__RETAINED_CONTENTS, oldRetainedContents, newRetainedContents);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRetainedContents(RetainAsStatement newRetainedContents)
  {
    if (newRetainedContents != retainedContents)
    {
      NotificationChain msgs = null;
      if (retainedContents != null)
        msgs = ((InternalEObject)retainedContents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FUNCTION__RETAINED_CONTENTS, null, msgs);
      if (newRetainedContents != null)
        msgs = ((InternalEObject)newRetainedContents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FUNCTION__RETAINED_CONTENTS, null, msgs);
      msgs = basicSetRetainedContents(newRetainedContents, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FUNCTION__RETAINED_CONTENTS, newRetainedContents, newRetainedContents));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<VariableStatement> getVariables()
  {
    if (variables == null)
    {
      variables = new EObjectContainmentEList<VariableStatement>(VariableStatement.class, this, ProgramPackage.FUNCTION__VARIABLES);
    }
    return variables;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Instruction> getInstructions()
  {
    if (instructions == null)
    {
      instructions = new EObjectContainmentEList<Instruction>(Instruction.class, this, ProgramPackage.FUNCTION__INSTRUCTIONS);
    }
    return instructions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ProgramPackage.FUNCTION__SLOTS:
        return ((InternalEList<?>)getSlots()).basicRemove(otherEnd, msgs);
      case ProgramPackage.FUNCTION__ASSUMED_CONTENTS:
        return basicSetAssumedContents(null, msgs);
      case ProgramPackage.FUNCTION__RETAINED_CONTENTS:
        return basicSetRetainedContents(null, msgs);
      case ProgramPackage.FUNCTION__VARIABLES:
        return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
      case ProgramPackage.FUNCTION__INSTRUCTIONS:
        return ((InternalEList<?>)getInstructions()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ProgramPackage.FUNCTION__VOID:
        return isVoid();
      case ProgramPackage.FUNCTION__CHUNK:
        return isChunk();
      case ProgramPackage.FUNCTION__NAME:
        return getName();
      case ProgramPackage.FUNCTION__SLOTS:
        return getSlots();
      case ProgramPackage.FUNCTION__ASSUMED_CONTENTS:
        return getAssumedContents();
      case ProgramPackage.FUNCTION__RETAINED_CONTENTS:
        return getRetainedContents();
      case ProgramPackage.FUNCTION__VARIABLES:
        return getVariables();
      case ProgramPackage.FUNCTION__INSTRUCTIONS:
        return getInstructions();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ProgramPackage.FUNCTION__VOID:
        setVoid((Boolean)newValue);
        return;
      case ProgramPackage.FUNCTION__CHUNK:
        setChunk((Boolean)newValue);
        return;
      case ProgramPackage.FUNCTION__NAME:
        setName((String)newValue);
        return;
      case ProgramPackage.FUNCTION__SLOTS:
        getSlots().clear();
        getSlots().addAll((Collection<? extends VariableStatement>)newValue);
        return;
      case ProgramPackage.FUNCTION__ASSUMED_CONTENTS:
        setAssumedContents((WithAsStatement)newValue);
        return;
      case ProgramPackage.FUNCTION__RETAINED_CONTENTS:
        setRetainedContents((RetainAsStatement)newValue);
        return;
      case ProgramPackage.FUNCTION__VARIABLES:
        getVariables().clear();
        getVariables().addAll((Collection<? extends VariableStatement>)newValue);
        return;
      case ProgramPackage.FUNCTION__INSTRUCTIONS:
        getInstructions().clear();
        getInstructions().addAll((Collection<? extends Instruction>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ProgramPackage.FUNCTION__VOID:
        setVoid(VOID_EDEFAULT);
        return;
      case ProgramPackage.FUNCTION__CHUNK:
        setChunk(CHUNK_EDEFAULT);
        return;
      case ProgramPackage.FUNCTION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ProgramPackage.FUNCTION__SLOTS:
        getSlots().clear();
        return;
      case ProgramPackage.FUNCTION__ASSUMED_CONTENTS:
        setAssumedContents((WithAsStatement)null);
        return;
      case ProgramPackage.FUNCTION__RETAINED_CONTENTS:
        setRetainedContents((RetainAsStatement)null);
        return;
      case ProgramPackage.FUNCTION__VARIABLES:
        getVariables().clear();
        return;
      case ProgramPackage.FUNCTION__INSTRUCTIONS:
        getInstructions().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ProgramPackage.FUNCTION__VOID:
        return void_ != VOID_EDEFAULT;
      case ProgramPackage.FUNCTION__CHUNK:
        return chunk != CHUNK_EDEFAULT;
      case ProgramPackage.FUNCTION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ProgramPackage.FUNCTION__SLOTS:
        return slots != null && !slots.isEmpty();
      case ProgramPackage.FUNCTION__ASSUMED_CONTENTS:
        return assumedContents != null;
      case ProgramPackage.FUNCTION__RETAINED_CONTENTS:
        return retainedContents != null;
      case ProgramPackage.FUNCTION__VARIABLES:
        return variables != null && !variables.isEmpty();
      case ProgramPackage.FUNCTION__INSTRUCTIONS:
        return instructions != null && !instructions.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (void: ");
    result.append(void_);
    result.append(", chunk: ");
    result.append(chunk);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //FunctionImpl
