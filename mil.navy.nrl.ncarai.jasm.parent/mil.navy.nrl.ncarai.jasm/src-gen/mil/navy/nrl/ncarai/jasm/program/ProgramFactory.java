/**
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.program;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see mil.navy.nrl.ncarai.jasm.program.ProgramPackage
 * @generated
 */
public interface ProgramFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ProgramFactory eINSTANCE = mil.navy.nrl.ncarai.jasm.program.impl.ProgramFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Program</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program</em>'.
   * @generated
   */
  Program createProgram();

  /**
   * Returns a new object of class '<em>Package Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Package Def</em>'.
   * @generated
   */
  PackageDef createPackageDef();

  /**
   * Returns a new object of class '<em>Include</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Include</em>'.
   * @generated
   */
  Include createInclude();

  /**
   * Returns a new object of class '<em>Buffer Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Buffer Def</em>'.
   * @generated
   */
  BufferDef createBufferDef();

  /**
   * Returns a new object of class '<em>Request Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Request Def</em>'.
   * @generated
   */
  RequestDef createRequestDef();

  /**
   * Returns a new object of class '<em>Generator Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Generator Def</em>'.
   * @generated
   */
  GeneratorDef createGeneratorDef();

  /**
   * Returns a new object of class '<em>Pattern</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Pattern</em>'.
   * @generated
   */
  Pattern createPattern();

  /**
   * Returns a new object of class '<em>Function</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function</em>'.
   * @generated
   */
  Function createFunction();

  /**
   * Returns a new object of class '<em>With As Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>With As Statement</em>'.
   * @generated
   */
  WithAsStatement createWithAsStatement();

  /**
   * Returns a new object of class '<em>Retain As Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Retain As Statement</em>'.
   * @generated
   */
  RetainAsStatement createRetainAsStatement();

  /**
   * Returns a new object of class '<em>As Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>As Statement</em>'.
   * @generated
   */
  AsStatement createAsStatement();

  /**
   * Returns a new object of class '<em>Variable Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Statement</em>'.
   * @generated
   */
  VariableStatement createVariableStatement();

  /**
   * Returns a new object of class '<em>Block</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Block</em>'.
   * @generated
   */
  Block createBlock();

  /**
   * Returns a new object of class '<em>Insertion Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Insertion Statement</em>'.
   * @generated
   */
  InsertionStatement createInsertionStatement();

  /**
   * Returns a new object of class '<em>Instruction</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Instruction</em>'.
   * @generated
   */
  Instruction createInstruction();

  /**
   * Returns a new object of class '<em>Function Call Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Function Call Statement</em>'.
   * @generated
   */
  FunctionCallStatement createFunctionCallStatement();

  /**
   * Returns a new object of class '<em>Assignment Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Assignment Statement</em>'.
   * @generated
   */
  AssignmentStatement createAssignmentStatement();

  /**
   * Returns a new object of class '<em>Variable Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Reference</em>'.
   * @generated
   */
  VariableReference createVariableReference();

  /**
   * Returns a new object of class '<em>Variable Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable Value</em>'.
   * @generated
   */
  VariableValue createVariableValue();

  /**
   * Returns a new object of class '<em>Return Variable Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Return Variable Reference</em>'.
   * @generated
   */
  ReturnVariableReference createReturnVariableReference();

  /**
   * Returns a new object of class '<em>Return Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Return Statement</em>'.
   * @generated
   */
  ReturnStatement createReturnStatement();

  /**
   * Returns a new object of class '<em>Remove Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remove Statement</em>'.
   * @generated
   */
  RemoveStatement createRemoveStatement();

  /**
   * Returns a new object of class '<em>Request Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Request Statement</em>'.
   * @generated
   */
  RequestStatement createRequestStatement();

  /**
   * Returns a new object of class '<em>Repeat Until Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Repeat Until Statement</em>'.
   * @generated
   */
  RepeatUntilStatement createRepeatUntilStatement();

  /**
   * Returns a new object of class '<em>Until Repeat Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Until Repeat Statement</em>'.
   * @generated
   */
  UntilRepeatStatement createUntilRepeatStatement();

  /**
   * Returns a new object of class '<em>While Do Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>While Do Statement</em>'.
   * @generated
   */
  WhileDoStatement createWhileDoStatement();

  /**
   * Returns a new object of class '<em>Do While Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Do While Statement</em>'.
   * @generated
   */
  DoWhileStatement createDoWhileStatement();

  /**
   * Returns a new object of class '<em>Repeat</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Repeat</em>'.
   * @generated
   */
  Repeat createRepeat();

  /**
   * Returns a new object of class '<em>Reward Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Reward Statement</em>'.
   * @generated
   */
  RewardStatement createRewardStatement();

  /**
   * Returns a new object of class '<em>Until</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Until</em>'.
   * @generated
   */
  Until createUntil();

  /**
   * Returns a new object of class '<em>While</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>While</em>'.
   * @generated
   */
  While createWhile();

  /**
   * Returns a new object of class '<em>Match Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Match Statement</em>'.
   * @generated
   */
  MatchStatement createMatchStatement();

  /**
   * Returns a new object of class '<em>Case Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case Statement</em>'.
   * @generated
   */
  CaseStatement createCaseStatement();

  /**
   * Returns a new object of class '<em>Output Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Output Statement</em>'.
   * @generated
   */
  OutputStatement createOutputStatement();

  /**
   * Returns a new object of class '<em>For Loop Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>For Loop Statement</em>'.
   * @generated
   */
  ForLoopStatement createForLoopStatement();

  /**
   * Returns a new object of class '<em>If Then Else Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>If Then Else Statement</em>'.
   * @generated
   */
  IfThenElseStatement createIfThenElseStatement();

  /**
   * Returns a new object of class '<em>Bindings</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Bindings</em>'.
   * @generated
   */
  Bindings createBindings();

  /**
   * Returns a new object of class '<em>Buffer Pattern</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Buffer Pattern</em>'.
   * @generated
   */
  BufferPattern createBufferPattern();

  /**
   * Returns a new object of class '<em>Mapping</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Mapping</em>'.
   * @generated
   */
  Mapping createMapping();

  /**
   * Returns a new object of class '<em>Handler</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Handler</em>'.
   * @generated
   */
  Handler createHandler();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ProgramPackage getProgramPackage();

} //ProgramFactory
