/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program.impl;

import mil.navy.nrl.ncarai.jasm.program.Bindings;
import mil.navy.nrl.ncarai.jasm.program.Block;
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.jactr.io2.jactr.modelFragment.Buffer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>For Loop Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#isForced <em>Forced</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getForBuffer <em>For Buffer</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getInitial <em>Initial</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getInitialBindings <em>Initial Bindings</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getSubsequent <em>Subsequent</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getSubsequentBindings <em>Subsequent Bindings</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.ForLoopStatementImpl#getBlock <em>Block</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ForLoopStatementImpl extends InstructionImpl implements ForLoopStatement
{
  /**
   * The default value of the '{@link #isForced() <em>Forced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForced()
   * @generated
   * @ordered
   */
  protected static final boolean FORCED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isForced() <em>Forced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForced()
   * @generated
   * @ordered
   */
  protected boolean forced = FORCED_EDEFAULT;

  /**
   * The cached value of the '{@link #getForBuffer() <em>For Buffer</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getForBuffer()
   * @generated
   * @ordered
   */
  protected Buffer forBuffer;

  /**
   * The cached value of the '{@link #getInitial() <em>Initial</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitial()
   * @generated
   * @ordered
   */
  protected Pattern initial;

  /**
   * The cached value of the '{@link #getInitialBindings() <em>Initial Bindings</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitialBindings()
   * @generated
   * @ordered
   */
  protected Bindings initialBindings;

  /**
   * The cached value of the '{@link #getSubsequent() <em>Subsequent</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubsequent()
   * @generated
   * @ordered
   */
  protected Pattern subsequent;

  /**
   * The cached value of the '{@link #getSubsequentBindings() <em>Subsequent Bindings</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubsequentBindings()
   * @generated
   * @ordered
   */
  protected Bindings subsequentBindings;

  /**
   * The cached value of the '{@link #getBlock() <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlock()
   * @generated
   * @ordered
   */
  protected Block block;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ForLoopStatementImpl()
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
    return ProgramPackage.Literals.FOR_LOOP_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isForced()
  {
    return forced;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setForced(boolean newForced)
  {
    boolean oldForced = forced;
    forced = newForced;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__FORCED, oldForced, forced));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Buffer getForBuffer()
  {
    if (forBuffer != null && forBuffer.eIsProxy())
    {
      InternalEObject oldForBuffer = (InternalEObject)forBuffer;
      forBuffer = (Buffer)eResolveProxy(oldForBuffer);
      if (forBuffer != oldForBuffer)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER, oldForBuffer, forBuffer));
      }
    }
    return forBuffer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Buffer basicGetForBuffer()
  {
    return forBuffer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setForBuffer(Buffer newForBuffer)
  {
    Buffer oldForBuffer = forBuffer;
    forBuffer = newForBuffer;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER, oldForBuffer, forBuffer));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Pattern getInitial()
  {
    if (initial != null && initial.eIsProxy())
    {
      InternalEObject oldInitial = (InternalEObject)initial;
      initial = (Pattern)eResolveProxy(oldInitial);
      if (initial != oldInitial)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProgramPackage.FOR_LOOP_STATEMENT__INITIAL, oldInitial, initial));
      }
    }
    return initial;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pattern basicGetInitial()
  {
    return initial;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInitial(Pattern newInitial)
  {
    Pattern oldInitial = initial;
    initial = newInitial;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__INITIAL, oldInitial, initial));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Bindings getInitialBindings()
  {
    return initialBindings;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInitialBindings(Bindings newInitialBindings, NotificationChain msgs)
  {
    Bindings oldInitialBindings = initialBindings;
    initialBindings = newInitialBindings;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS, oldInitialBindings, newInitialBindings);
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
  public void setInitialBindings(Bindings newInitialBindings)
  {
    if (newInitialBindings != initialBindings)
    {
      NotificationChain msgs = null;
      if (initialBindings != null)
        msgs = ((InternalEObject)initialBindings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS, null, msgs);
      if (newInitialBindings != null)
        msgs = ((InternalEObject)newInitialBindings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS, null, msgs);
      msgs = basicSetInitialBindings(newInitialBindings, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS, newInitialBindings, newInitialBindings));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Pattern getSubsequent()
  {
    if (subsequent != null && subsequent.eIsProxy())
    {
      InternalEObject oldSubsequent = (InternalEObject)subsequent;
      subsequent = (Pattern)eResolveProxy(oldSubsequent);
      if (subsequent != oldSubsequent)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT, oldSubsequent, subsequent));
      }
    }
    return subsequent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pattern basicGetSubsequent()
  {
    return subsequent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSubsequent(Pattern newSubsequent)
  {
    Pattern oldSubsequent = subsequent;
    subsequent = newSubsequent;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT, oldSubsequent, subsequent));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Bindings getSubsequentBindings()
  {
    return subsequentBindings;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubsequentBindings(Bindings newSubsequentBindings, NotificationChain msgs)
  {
    Bindings oldSubsequentBindings = subsequentBindings;
    subsequentBindings = newSubsequentBindings;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS, oldSubsequentBindings, newSubsequentBindings);
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
  public void setSubsequentBindings(Bindings newSubsequentBindings)
  {
    if (newSubsequentBindings != subsequentBindings)
    {
      NotificationChain msgs = null;
      if (subsequentBindings != null)
        msgs = ((InternalEObject)subsequentBindings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS, null, msgs);
      if (newSubsequentBindings != null)
        msgs = ((InternalEObject)newSubsequentBindings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS, null, msgs);
      msgs = basicSetSubsequentBindings(newSubsequentBindings, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS, newSubsequentBindings, newSubsequentBindings));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Block getBlock()
  {
    return block;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBlock(Block newBlock, NotificationChain msgs)
  {
    Block oldBlock = block;
    block = newBlock;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__BLOCK, oldBlock, newBlock);
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
  public void setBlock(Block newBlock)
  {
    if (newBlock != block)
    {
      NotificationChain msgs = null;
      if (block != null)
        msgs = ((InternalEObject)block).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__BLOCK, null, msgs);
      if (newBlock != null)
        msgs = ((InternalEObject)newBlock).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.FOR_LOOP_STATEMENT__BLOCK, null, msgs);
      msgs = basicSetBlock(newBlock, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.FOR_LOOP_STATEMENT__BLOCK, newBlock, newBlock));
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
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS:
        return basicSetInitialBindings(null, msgs);
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS:
        return basicSetSubsequentBindings(null, msgs);
      case ProgramPackage.FOR_LOOP_STATEMENT__BLOCK:
        return basicSetBlock(null, msgs);
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
      case ProgramPackage.FOR_LOOP_STATEMENT__FORCED:
        return isForced();
      case ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER:
        if (resolve) return getForBuffer();
        return basicGetForBuffer();
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL:
        if (resolve) return getInitial();
        return basicGetInitial();
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS:
        return getInitialBindings();
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT:
        if (resolve) return getSubsequent();
        return basicGetSubsequent();
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS:
        return getSubsequentBindings();
      case ProgramPackage.FOR_LOOP_STATEMENT__BLOCK:
        return getBlock();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ProgramPackage.FOR_LOOP_STATEMENT__FORCED:
        setForced((Boolean)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER:
        setForBuffer((Buffer)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL:
        setInitial((Pattern)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS:
        setInitialBindings((Bindings)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT:
        setSubsequent((Pattern)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS:
        setSubsequentBindings((Bindings)newValue);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__BLOCK:
        setBlock((Block)newValue);
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
      case ProgramPackage.FOR_LOOP_STATEMENT__FORCED:
        setForced(FORCED_EDEFAULT);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER:
        setForBuffer((Buffer)null);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL:
        setInitial((Pattern)null);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS:
        setInitialBindings((Bindings)null);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT:
        setSubsequent((Pattern)null);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS:
        setSubsequentBindings((Bindings)null);
        return;
      case ProgramPackage.FOR_LOOP_STATEMENT__BLOCK:
        setBlock((Block)null);
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
      case ProgramPackage.FOR_LOOP_STATEMENT__FORCED:
        return forced != FORCED_EDEFAULT;
      case ProgramPackage.FOR_LOOP_STATEMENT__FOR_BUFFER:
        return forBuffer != null;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL:
        return initial != null;
      case ProgramPackage.FOR_LOOP_STATEMENT__INITIAL_BINDINGS:
        return initialBindings != null;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT:
        return subsequent != null;
      case ProgramPackage.FOR_LOOP_STATEMENT__SUBSEQUENT_BINDINGS:
        return subsequentBindings != null;
      case ProgramPackage.FOR_LOOP_STATEMENT__BLOCK:
        return block != null;
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
    result.append(" (forced: ");
    result.append(forced);
    result.append(')');
    return result.toString();
  }

} //ForLoopStatementImpl