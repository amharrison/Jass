package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;

import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory;
import mil.navy.nrl.ncarai.jasm.program.ReturnVariableReference;
import mil.navy.nrl.ncarai.jasm.program.VariableReference;

public class ProcGenUtilities {

	static public Modify toModify(Pattern pattern, ProceduralGenerationContext context) {
		Modify rtn = ModelFragmentFactory.eINSTANCE.createModify();

		for (ConditionalSlot cSlot : pattern.getSlots())
			if (cSlot.getCondition() == Condition.EQUALS) {
				SimpleSlot slot = simpleSlot(cSlot.getName());
				slot.setValue(EcoreUtil.copy(cSlot.getValue()));
				rtn.getSlots().add(slot);
			}

		return rtn;
	}

	static public Add toAdd(Pattern pattern, ProceduralGenerationContext context) {
		Add rtn = ModelFragmentFactory.eINSTANCE.createAdd();
		rtn.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
		rtn.getIsa().setType(context._resolver.resolveChunkType(pattern.getType()));

		// copy slots
		for (ConditionalSlot cSlot : pattern.getSlots())
			rtn.getSlots().add(EcoreUtil.copy(cSlot));

		return rtn;
	}

	static public Add toAdd(BufferPattern pattern, ProceduralGenerationContext context) {
		Add rtn = toAdd(pattern.getPattern(), context);
		rtn.setName(pattern.getBuffer());
		return rtn;
	}

	/**
	 * creates a partially defined Match instance. Match.name must be set
	 * 
	 * @param pattern
	 * @return
	 */
	static public Match toMatch(Pattern pattern, ProceduralGenerationContext context) {
		Match rtn = ModelFragmentFactory.eINSTANCE.createMatch();

		rtn.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
		ChunkType chunkType = context._resolver.resolveChunkType(pattern.getType());
		if (chunkType == null)
			System.err.println("null chunktype for " + pattern.getType());
		rtn.getIsa().setType(chunkType);

		// copy slots
		for (ConditionalSlot cSlot : pattern.getSlots())
			rtn.getSlots().add(EcoreUtil.copy(cSlot));

		return rtn;
	}

	static public Match toMatch(BufferPattern pattern, ProceduralGenerationContext context) {
		Match rtn = toMatch(pattern.getPattern(), context);
		rtn.setName(pattern.getBuffer());
		return rtn;
	}

	static public ConditionalSlot conditionalSlot(String slotName, Condition condition) {
		ConditionalSlot frag = ModelFragmentFactory.eINSTANCE.createConditionalSlot();
		frag.setName(slotName);
		if (slotName == null) {
			System.err.println("null name");
		}

		frag.setCondition(condition);
		frag.setValue(ModelFragmentFactory.eINSTANCE.createValue());
		return frag;
	}

	static public SimpleSlot simpleSlot(String slotName) {
		SimpleSlot frag = ModelFragmentFactory.eINSTANCE.createSimpleSlot();
		frag.setName(slotName);

		frag.setValue(ModelFragmentFactory.eINSTANCE.createValue());
		return frag;
	}

	static public int lineNumber(EObject object) {
		INode node = NodeModelUtils.getNode(object);
		int lineNumber = 0;
		if (node != null)
			lineNumber = node.getStartLine();
		return lineNumber;
	}

	static public String text(EObject object) {
		INode node = NodeModelUtils.getNode(object);
		if (node != null)
			return node.getText();
		return "";
	}
	
	static public BufferPattern asPattern(VariableReference reference, String variableValue,
			ProceduralGenerationContext context) {
		Buffer buffer = reference.getBuffer();
		if (buffer == null)
			buffer = context._resolver.resolveBuffer("goal");
		BufferPattern rtn = ProgramFactory.eINSTANCE.createBufferPattern();
		rtn.setPattern(ProgramFactory.eINSTANCE.createPattern());
		rtn.setBuffer(buffer);
		ConditionalSlot cSlot = conditionalSlot(reference.getName(), Condition.EQUALS);
		cSlot.getValue().setName(variableValue);
		rtn.getPattern().getSlots().add(cSlot);

		return rtn;
	}

	/**
	 * the pattern will have empty type information if its not available
	 * @param reference
	 * @param variableValue
	 * @param context
	 * @return
	 */
	static public BufferPattern asPattern(ReturnVariableReference reference, String variableValue,
			ProceduralGenerationContext context) {
		Buffer buffer = reference.getBuffer();		
		if (buffer == null)
			buffer = context._resolver.resolveBuffer("goal");
		Optional<ChunkType> ct = context._bufferStateManager.getContentType(buffer.getName());
		BufferPattern rtn = ProgramFactory.eINSTANCE.createBufferPattern();
		rtn.setPattern(ProgramFactory.eINSTANCE.createPattern());
		if(ct.isPresent())
			rtn.getPattern().setType(ct.get().getName());
		
		rtn.setBuffer(buffer);
		ConditionalSlot cSlot = conditionalSlot(reference.getValue().getName(), Condition.EQUALS);
		cSlot.getValue().setName(variableValue);
		rtn.getPattern().getSlots().add(cSlot);

		return rtn;
	}

	static public boolean binds(Pattern pattern, String variableName) {
		return pattern.getSlots().stream().anyMatch(predicate -> {
			return variableName.equals(predicate.getValue().getName())
					&& predicate.getCondition().equals(Condition.EQUALS);
		});
	}

	static public boolean isAncestorOrSame(ChunkType one, ChunkType potentialAncestor) {
		ChunkType reference = one;
		while (reference != null) {
			if (reference.equals(potentialAncestor))
				return true;
			reference = reference.getSuperType();
		}
		return false;
	}

	static public Map<Buffer, BufferPattern> mergePatterns(Pattern goalPattern,
			Collection<? extends BufferPattern> patterns, ProceduralGenerationContext context) {
		Map<Buffer, BufferPattern> rtn = new HashMap<>();
		BufferPattern bp = ProgramFactory.eINSTANCE.createBufferPattern();
		bp.setBuffer(context._resolver.resolveBuffer("goal"));
		bp.setPattern(goalPattern);
		rtn.put(bp.getBuffer(), bp);

		patterns.forEach(bPattern -> {
			BufferPattern existing = rtn.get(bPattern.getBuffer());
			if (existing == null)
				existing = bPattern;
			else
				existing.setPattern(merge(existing.getPattern(), bPattern.getPattern(), context));
			rtn.put(existing.getBuffer(), existing);
		});

		return rtn;
	}

	static public Map<Buffer, Pattern> mergedPatternConditions(Pattern goalPattern,
			Collection<? extends AssignmentStatement> variableAssignments, ProceduralGenerationContext context) {

		Map<Buffer, Pattern> rtn = new HashMap<>();
		rtn.put(context._resolver.resolveBuffer("goal"), goalPattern);

		variableAssignments.forEach(va -> {
			Buffer buffer = va.getAssignment().getBuffer();
			if (buffer == null)
				buffer = context._resolver.resolveBuffer("goal");
			Optional<ChunkType> chunkTypeOpt = context._bufferStateManager.getContentType(buffer.getName());
			Pattern pattern = ProgramFactory.eINSTANCE.createPattern();
			if (chunkTypeOpt.isPresent())
				pattern.setType(chunkTypeOpt.get().getName());

			if (va.getAssignment().getValue().getName() != null
					&& context._resolver.resolveChunk(va.getAssignment().getValue().getName()) == null) {
				ConditionalSlot cSlot = conditionalSlot(va.getAssignment().getValue().getName(), Condition.EQUALS);
				cSlot.getValue().setName("=" + buffer.getName() + "-" + cSlot.getName());
				pattern.getSlots().add(cSlot);
			}

			Pattern oldPattern = rtn.get(buffer);
			if (oldPattern != null)
				pattern = merge(pattern, oldPattern, context);
			rtn.put(buffer, pattern);
		});

		return rtn;
	}

	static public Pattern patternActions(Collection<? extends AssignmentStatement> variableAssignments,
			ProceduralGenerationContext context) {

		Pattern pattern = ProgramFactory.eINSTANCE.createPattern();

		variableAssignments.forEach(va -> {
			Buffer buffer = va.getAssignment().getBuffer();
			if (buffer == null)
				buffer = context._resolver.resolveBuffer("goal");

			ConditionalSlot cSlot = conditionalSlot(va.getReference().getName(), Condition.EQUALS);
			if (va.getAssignment().getValue().getName() != null
					&& context._resolver.resolveChunk(va.getAssignment().getValue().getName()) == null)
				cSlot.getValue().setName("=" + buffer.getName() + "-" + va.getAssignment().getValue().getName());
			else
				cSlot.setValue(EcoreUtil.copy(va.getAssignment().getValue()));

			pattern.getSlots().add(cSlot);

		});

		return pattern;
	}

	static public Pattern merge(Pattern one, Pattern two, ProceduralGenerationContext context) {
		ChunkType typeOne = context._resolver.resolveChunkType(one.getType());
		ChunkType typeTwo = context._resolver.resolveChunkType(two.getType());
		boolean oneIsMoreStrict = isAncestorOrSame(typeOne, typeTwo) || typeTwo == null;
		boolean twoIsMoreStrict = isAncestorOrSame(typeTwo, typeOne) || typeOne == null;
		Pattern rtn = ProgramFactory.eINSTANCE.createPattern();
		if (oneIsMoreStrict)
			rtn.setType(one.getType());
		else if (twoIsMoreStrict)
			rtn.setType(two.getType());
		else
			throw new IllegalStateException(one.getType() + " and " + two.getType() + " are not related");

		for (ConditionalSlot cSlot : one.getSlots())
			rtn.getSlots().add(EcoreUtil.copy(cSlot));

		for (ConditionalSlot cSlot : two.getSlots())
			rtn.getSlots().add(EcoreUtil.copy(cSlot));

		return rtn;
	}

}
