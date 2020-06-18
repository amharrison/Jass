/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program.impl;

import java.util.Collection;

import mil.navy.nrl.ncarai.jasm.program.Block;
import mil.navy.nrl.ncarai.jasm.program.CaseStatement;
import mil.navy.nrl.ncarai.jasm.program.MatchStatement;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Match Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.MatchStatementImpl#getCases <em>Cases</em>}</li>
 *   <li>{@link mil.navy.nrl.ncarai.jasm.program.impl.MatchStatementImpl#getDefaultBlock <em>Default Block</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MatchStatementImpl extends InstructionImpl implements MatchStatement
{
  /**
   * The cached value of the '{@link #getCases() <em>Cases</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCases()
   * @generated
   * @ordered
   */
  protected EList<CaseStatement> cases;

  /**
   * The cached value of the '{@link #getDefaultBlock() <em>Default Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultBlock()
   * @generated
   * @ordered
   */
  protected Block defaultBlock;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MatchStatementImpl()
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
    return ProgramPackage.Literals.MATCH_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<CaseStatement> getCases()
  {
    if (cases == null)
    {
      cases = new EObjectContainmentEList<CaseStatement>(CaseStatement.class, this, ProgramPackage.MATCH_STATEMENT__CASES);
    }
    return cases;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Block getDefaultBlock()
  {
    return defaultBlock;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDefaultBlock(Block newDefaultBlock, NotificationChain msgs)
  {
    Block oldDefaultBlock = defaultBlock;
    defaultBlock = newDefaultBlock;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK, oldDefaultBlock, newDefaultBlock);
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
  public void setDefaultBlock(Block newDefaultBlock)
  {
    if (newDefaultBlock != defaultBlock)
    {
      NotificationChain msgs = null;
      if (defaultBlock != null)
        msgs = ((InternalEObject)defaultBlock).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK, null, msgs);
      if (newDefaultBlock != null)
        msgs = ((InternalEObject)newDefaultBlock).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK, null, msgs);
      msgs = basicSetDefaultBlock(newDefaultBlock, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK, newDefaultBlock, newDefaultBlock));
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
      case ProgramPackage.MATCH_STATEMENT__CASES:
        return ((InternalEList<?>)getCases()).basicRemove(otherEnd, msgs);
      case ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK:
        return basicSetDefaultBlock(null, msgs);
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
      case ProgramPackage.MATCH_STATEMENT__CASES:
        return getCases();
      case ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK:
        return getDefaultBlock();
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
      case ProgramPackage.MATCH_STATEMENT__CASES:
        getCases().clear();
        getCases().addAll((Collection<? extends CaseStatement>)newValue);
        return;
      case ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK:
        setDefaultBlock((Block)newValue);
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
      case ProgramPackage.MATCH_STATEMENT__CASES:
        getCases().clear();
        return;
      case ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK:
        setDefaultBlock((Block)null);
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
      case ProgramPackage.MATCH_STATEMENT__CASES:
        return cases != null && !cases.isEmpty();
      case ProgramPackage.MATCH_STATEMENT__DEFAULT_BLOCK:
        return defaultBlock != null;
    }
    return super.eIsSet(featureID);
  }

} //MatchStatementImpl