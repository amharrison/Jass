/**
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm.formatting2;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import mil.navy.nrl.ncarai.jasm.program.Block;
import mil.navy.nrl.ncarai.jasm.program.CaseStatement;
import mil.navy.nrl.ncarai.jasm.program.DoWhileStatement;
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Handler;
import mil.navy.nrl.ncarai.jasm.program.IfThenElseStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Mapping;
import mil.navy.nrl.ncarai.jasm.program.MatchStatement;
import mil.navy.nrl.ncarai.jasm.program.PackageDef;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.Program;
import mil.navy.nrl.ncarai.jasm.program.ProgramPackage;
import mil.navy.nrl.ncarai.jasm.program.RepeatUntilStatement;
import mil.navy.nrl.ncarai.jasm.program.RequestStatement;
import mil.navy.nrl.ncarai.jasm.program.RetainAsStatement;
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement;
import mil.navy.nrl.ncarai.jasm.program.UntilRepeatStatement;
import mil.navy.nrl.ncarai.jasm.program.WhileDoStatement;
import mil.navy.nrl.ncarai.jasm.program.WithAsStatement;
import mil.navy.nrl.ncarai.jasm.services.ProgramGrammarAccess;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionsFinder;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jactr.io2.jactr.formatting2.ModelFragmentFormatter;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.AndSlot;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.Buffers;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Chunks;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Import;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelExtension;
import org.jactr.io2.jactr.modelFragment.ModelFragment;
import org.jactr.io2.jactr.modelFragment.ModelModule;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.NotSlot;
import org.jactr.io2.jactr.modelFragment.OrSlot;
import org.jactr.io2.jactr.modelFragment.Output;
import org.jactr.io2.jactr.modelFragment.PackageDeclaration;
import org.jactr.io2.jactr.modelFragment.Parameters;
import org.jactr.io2.jactr.modelFragment.ParametersBlock;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Proxy;
import org.jactr.io2.jactr.modelFragment.Query;
import org.jactr.io2.jactr.modelFragment.Remove;
import org.jactr.io2.jactr.modelFragment.Script;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;

@SuppressWarnings("all")
public class ProgramFormatter extends ModelFragmentFormatter {
  @Inject
  private ProgramGrammarAccess _grammarAccess;
  
  protected void _format(final Program program, @Extension final IFormattableDocument document) {
    document.<PackageDef>format(program.getPackage());
  }
  
  protected void _format(final PackageDef packageDef, @Extension final IFormattableDocument document) {
    EList<Import> _imports = packageDef.getImports();
    for (final Import _import : _imports) {
      document.<Import>format(_import);
    }
    EList<ChunkType> _chunkTypes = packageDef.getChunkTypes();
    for (final ChunkType chunkType : _chunkTypes) {
      document.<ChunkType>format(chunkType);
    }
    EList<Pattern> _patterns = packageDef.getPatterns();
    for (final Pattern pattern : _patterns) {
      document.<Pattern>format(pattern);
    }
    EList<Function> _functions = packageDef.getFunctions();
    for (final Function function : _functions) {
      document.<Function>format(function);
    }
  }
  
  protected void _format(final Pattern pattern, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 2, 3);
    };
    document.<Pattern>prepend(pattern, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(document.append(this.textRegionExtensions.regionFor(pattern).keyword("{"), _function_1), document.prepend(this.textRegionExtensions.regionFor(pattern).keyword("}"), _function_2), _function_3);
    EList<ConditionalSlot> _slots = pattern.getSlots();
    for (final ConditionalSlot slot : _slots) {
      document.<ConditionalSlot>format(slot);
    }
  }
  
  protected void _format(final Function function, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(this.textRegionExtensions.regionFor(function).feature(ProgramPackage.Literals.FUNCTION__NAME), _function);
    WithAsStatement _assumedContents = function.getAssumedContents();
    boolean _tripleNotEquals = (_assumedContents != null);
    if (_tripleNotEquals) {
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.surround(document.prepend(this.textRegionExtensions.regionFor(function.getAssumedContents()).keyword("with"), _function_1), _function_2);
    }
    RetainAsStatement _retainedContents = function.getRetainedContents();
    boolean _tripleNotEquals_1 = (_retainedContents != null);
    if (_tripleNotEquals_1) {
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.surround(document.prepend(this.textRegionExtensions.regionFor(function.getRetainedContents()).keyword("retain"), _function_3), _function_4);
    }
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(document.append(this.textRegionExtensions.regionFor(function).keyword("{"), _function_5), document.prepend(this.textRegionExtensions.regionFor(function).keyword("}"), _function_6), _function_7);
    EList<Instruction> _instructions = function.getInstructions();
    for (final Instruction inst : _instructions) {
      final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<Instruction>prepend(document.<Instruction>format(inst), _function_8);
    }
  }
  
  protected void _format(final Block block, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(document.append(document.prepend(this.textRegionExtensions.regionFor(block).keyword("{"), _function), _function_1), document.prepend(document.prepend(this.textRegionExtensions.regionFor(block).keyword("}"), _function_2), _function_3), _function_4);
    EList<Instruction> _instructions = block.getInstructions();
    for (final Instruction inst : _instructions) {
      final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<Instruction>prepend(document.<Instruction>format(inst), _function_5);
    }
  }
  
  protected void _format(final ReturnStatement statement, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.prepend(this.textRegionExtensions.regionFor(statement).keyword("return"), _function);
  }
  
  protected void _format(final RequestStatement request, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.prepend(this.textRegionExtensions.regionFor(request).keyword("request"), _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(request).keyword("("), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.prepend(this.textRegionExtensions.regionFor(request).keyword(")"), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(request).keyword("as"), _function_3);
    Mapping _mapping = request.getMapping();
    boolean _tripleNotEquals = (_mapping != null);
    if (_tripleNotEquals) {
      Mapping _mapping_1 = request.getMapping();
      ISemanticRegionsFinder _regionFor = null;
      if (_mapping_1!=null) {
        _regionFor=this.textRegionExtensions.regionFor(_mapping_1);
      }
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.surround(document.append(document.prepend(document.surround(_regionFor.keyword("->"), _function_4), _function_5), _function_6).getNextSemanticRegion(), _function_7);
    }
    Handler _handler = request.getHandler();
    boolean _tripleNotEquals_1 = (_handler != null);
    if (_tripleNotEquals_1) {
      Handler _handler_1 = request.getHandler();
      ISemanticRegionsFinder _regionFor_1 = null;
      if (_handler_1!=null) {
        _regionFor_1=this.textRegionExtensions.regionFor(_handler_1);
      }
      final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      final Procedure1<IHiddenRegionFormatter> _function_9 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      final Procedure1<IHiddenRegionFormatter> _function_10 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.surround(document.prepend(document.surround(_regionFor_1.keyword("=>"), _function_8), _function_9).getNextSemanticRegion(), _function_10);
    }
    Handler _handler_2 = request.getHandler();
    Block _block = null;
    if (_handler_2!=null) {
      _block=_handler_2.getBlock();
    }
    document.<Block>format(_block);
  }
  
  protected void _format(final IfThenElseStatement ite, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(ite).keyword("("), _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(ite).keyword(")"), _function_1);
    document.<Block>format(ite.getThenBlock());
    Block _elseBlock = ite.getElseBlock();
    boolean _tripleNotEquals = (_elseBlock != null);
    if (_tripleNotEquals) {
      final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      document.append(this.textRegionExtensions.regionFor(ite.getElseBlock()).keyword("else"), _function_2);
      document.<Block>format(ite.getElseBlock());
    }
  }
  
  protected void _format(final ForLoopStatement forLoop, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(document.surround(this.textRegionExtensions.regionFor(forLoop).keyword("("), _function), document.surround(this.textRegionExtensions.regionFor(forLoop).keyword(")"), _function_1), _function_2);
    int i = 0;
    List<ISemanticRegion> _keywords = this.textRegionExtensions.regionFor(forLoop).keywords(",");
    for (final ISemanticRegion region : _keywords) {
      {
        i++;
        if ((i == 0)) {
          final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
            it.newLine();
          };
          document.append(document.prepend(region, _function_3), _function_4);
        } else {
          if (((i % 2) == 1)) {
            final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
              it.newLine();
            };
            document.append(document.prepend(region, _function_5), _function_6);
          } else {
            final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            document.append(document.prepend(region, _function_7), _function_8);
          }
        }
      }
    }
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(forLoop.getBlock()), _function_3);
  }
  
  protected void _format(final DoWhileStatement doWhile, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(doWhile.getBlock()).keyword("do"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(doWhile.getBlock()), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(doWhile.getWhile()).keyword("("), _function_3);
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(doWhile.getWhile()).keyword(")"), _function_4), _function_5);
  }
  
  protected void _format(final WhileDoStatement doWhile, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(doWhile).keyword("while"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(doWhile.getWhile()).keyword("("), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(doWhile.getWhile()).keyword(")"), _function_3), _function_4);
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(doWhile.getBlock()), _function_5);
  }
  
  protected void _format(final RepeatUntilStatement repeatUntil, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(repeatUntil).keyword("repeat"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(repeatUntil.getRepeat().getBlock()), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(repeatUntil.getUntil()).keyword("("), _function_3);
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(repeatUntil.getUntil()).keyword(")"), _function_4), _function_5);
    final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(repeatUntil).keyword("until"), _function_6), _function_7);
  }
  
  protected void _format(final UntilRepeatStatement untilRepeat, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(untilRepeat).keyword("until"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(untilRepeat.getUntil()).keyword("("), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(untilRepeat.getUntil()).keyword(")"), _function_3), _function_4);
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(untilRepeat.getBlock()), _function_5);
  }
  
  protected void _format(final MatchStatement matchStatement, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(document.append(this.textRegionExtensions.regionFor(matchStatement).keyword("{"), _function), document.prepend(document.prepend(this.textRegionExtensions.regionFor(matchStatement).keyword("}"), _function_1), _function_2), _function_3);
    EList<CaseStatement> _cases = matchStatement.getCases();
    for (final CaseStatement caseStmt : _cases) {
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<CaseStatement>prepend(document.<CaseStatement>format(caseStmt), _function_4);
    }
  }
  
  protected void _format(final CaseStatement caseStatement, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.newLine();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.append(document.prepend(this.textRegionExtensions.regionFor(caseStatement).keyword("case"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.surround(this.textRegionExtensions.regionFor(caseStatement).keyword(":"), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<Block>prepend(document.<Block>format(caseStatement.getBlock()), _function_3);
  }
  
  public void format(final Object doWhile, final IFormattableDocument document) {
    if (doWhile instanceof XtextResource) {
      _format((XtextResource)doWhile, document);
      return;
    } else if (doWhile instanceof AndSlot) {
      _format((AndSlot)doWhile, document);
      return;
    } else if (doWhile instanceof NotSlot) {
      _format((NotSlot)doWhile, document);
      return;
    } else if (doWhile instanceof OrSlot) {
      _format((OrSlot)doWhile, document);
      return;
    } else if (doWhile instanceof DoWhileStatement) {
      _format((DoWhileStatement)doWhile, document);
      return;
    } else if (doWhile instanceof ForLoopStatement) {
      _format((ForLoopStatement)doWhile, document);
      return;
    } else if (doWhile instanceof IfThenElseStatement) {
      _format((IfThenElseStatement)doWhile, document);
      return;
    } else if (doWhile instanceof MatchStatement) {
      _format((MatchStatement)doWhile, document);
      return;
    } else if (doWhile instanceof RepeatUntilStatement) {
      _format((RepeatUntilStatement)doWhile, document);
      return;
    } else if (doWhile instanceof RequestStatement) {
      _format((RequestStatement)doWhile, document);
      return;
    } else if (doWhile instanceof ReturnStatement) {
      _format((ReturnStatement)doWhile, document);
      return;
    } else if (doWhile instanceof UntilRepeatStatement) {
      _format((UntilRepeatStatement)doWhile, document);
      return;
    } else if (doWhile instanceof WhileDoStatement) {
      _format((WhileDoStatement)doWhile, document);
      return;
    } else if (doWhile instanceof Add) {
      _format((Add)doWhile, document);
      return;
    } else if (doWhile instanceof Buffer) {
      _format((Buffer)doWhile, document);
      return;
    } else if (doWhile instanceof ChunkDef) {
      _format((ChunkDef)doWhile, document);
      return;
    } else if (doWhile instanceof ChunkType) {
      _format((ChunkType)doWhile, document);
      return;
    } else if (doWhile instanceof Chunks) {
      _format((Chunks)doWhile, document);
      return;
    } else if (doWhile instanceof ConditionalSlot) {
      _format((ConditionalSlot)doWhile, document);
      return;
    } else if (doWhile instanceof Match) {
      _format((Match)doWhile, document);
      return;
    } else if (doWhile instanceof Modify) {
      _format((Modify)doWhile, document);
      return;
    } else if (doWhile instanceof Output) {
      _format((Output)doWhile, document);
      return;
    } else if (doWhile instanceof Parameters) {
      _format((Parameters)doWhile, document);
      return;
    } else if (doWhile instanceof Production) {
      _format((Production)doWhile, document);
      return;
    } else if (doWhile instanceof Proxy) {
      _format((Proxy)doWhile, document);
      return;
    } else if (doWhile instanceof Query) {
      _format((Query)doWhile, document);
      return;
    } else if (doWhile instanceof Remove) {
      _format((Remove)doWhile, document);
      return;
    } else if (doWhile instanceof Script) {
      _format((Script)doWhile, document);
      return;
    } else if (doWhile instanceof Block) {
      _format((Block)doWhile, document);
      return;
    } else if (doWhile instanceof CaseStatement) {
      _format((CaseStatement)doWhile, document);
      return;
    } else if (doWhile instanceof Function) {
      _format((Function)doWhile, document);
      return;
    } else if (doWhile instanceof PackageDef) {
      _format((PackageDef)doWhile, document);
      return;
    } else if (doWhile instanceof Pattern) {
      _format((Pattern)doWhile, document);
      return;
    } else if (doWhile instanceof Program) {
      _format((Program)doWhile, document);
      return;
    } else if (doWhile instanceof Buffers) {
      _format((Buffers)doWhile, document);
      return;
    } else if (doWhile instanceof Import) {
      _format((Import)doWhile, document);
      return;
    } else if (doWhile instanceof ModelExtension) {
      _format((ModelExtension)doWhile, document);
      return;
    } else if (doWhile instanceof ModelFragment) {
      _format((ModelFragment)doWhile, document);
      return;
    } else if (doWhile instanceof ModelModule) {
      _format((ModelModule)doWhile, document);
      return;
    } else if (doWhile instanceof PackageDeclaration) {
      _format((PackageDeclaration)doWhile, document);
      return;
    } else if (doWhile instanceof ParametersBlock) {
      _format((ParametersBlock)doWhile, document);
      return;
    } else if (doWhile instanceof SimpleSlot) {
      _format((SimpleSlot)doWhile, document);
      return;
    } else if (doWhile instanceof EObject) {
      _format((EObject)doWhile, document);
      return;
    } else if (doWhile == null) {
      _format((Void)null, document);
      return;
    } else if (doWhile != null) {
      _format(doWhile, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(doWhile, document).toString());
    }
  }
}
