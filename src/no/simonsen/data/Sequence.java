package no.simonsen.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.ValueSupplier;

public class Sequence implements ValueSupplier {
	private List<ValueSupplier> sequence;
	private ValueSupplier valueSupplier;
	private int counter;
	private int length;
	private int startOffset;
	private int endOffset;
	private int localOffset;
	private SequenceMode sequenceMode;
	private Random random;
	private Queue<Integer> noRepeatQueue;
	private int noRepeatMemory;
	private int randomWalkMemory;
	
	public Sequence() {
		sequence = new ArrayList<>();
		sequence.add(new ConstantPattern(0));
		
		setLength(Integer.MAX_VALUE);
		setStartOffset(0);
		setEndOffset(0);
		setLocalOffset(0);
		setSequenceMode(SequenceMode.NORMAL);
		
		random = new Random();
		noRepeatQueue = new LinkedList<>();
		setNoRepeatMemory(1);
	}
	
	public void setSequence(List<ValueSupplier> seq) {
		if (seq == null || seq.size() == 0) 
			return;
		sequence = seq;
		setStartOffset(startOffset);
	}
	
	public int getLength() { return length; }
	
	public void setLength(int len) {
		if (len < 1) {
			len = 1;
		}
		length = len;
	}
	
	public int getStartOffset() { return startOffset; }
	
	public void setStartOffset(int value) {
		if (value < 0)
			value = 0;
		if (!(value < sequence.size()))
			value = sequence.size() - 1;
		startOffset = value;
	}
	
	public int getEndOffset() { return endOffset; }
	
	public void setEndOffset(int value) {
		if (value < 0)
			value = 0;
		endOffset = value;
	}
	
	public int getLocalOffset() { return localOffset; }

	public void setLocalOffset(int value) {
		if (value < 0)
			value = 0;
		localOffset = value;
	}
	
	public SequenceMode getSequenceMode() { return sequenceMode; }

	public void setSequenceMode(SequenceMode mode) {
		sequenceMode = mode;
		for (ValueSupplier valueSupplier : sequence) valueSupplier.reset();
		
		if (mode == SequenceMode.NORMAL) {
			counter = localOffset;
		} else if (mode == SequenceMode.REVERSE) {
			counter = localOffset;
		} else if (mode == SequenceMode.RANDOM) {
			counter = 0;
			valueSupplier = null;
		} else if (mode == SequenceMode.RANDOM_NO_REPEAT) {
			counter = 0;
			valueSupplier = null;
			setNoRepeatMemory(noRepeatMemory);
		} else if (mode == SequenceMode.RANDOM_WALK) {
			counter = 0;
			valueSupplier = null;
			int k = 1000000000 / currentListSize(); 
			randomWalkMemory = currentListSize() * k + localOffset;
		}
	}
	
	public int getNoRepeatMemory() { return noRepeatMemory; }
	
	public void setNoRepeatMemory(int value) {
		if (value < 0)
			value = 0;
		noRepeatMemory = value;
		noRepeatQueue.clear();
		for (int i = 0; i < noRepeatMemory; i++) noRepeatQueue.add(-1);
	}
	
	private int currentListSize() {
		int size = sequence.size() - startOffset - endOffset;
		if (size < 1)
			size = 1;
		return size;
	}
	
	public double nextValue() {
		double value = -1;
		
		int listSize = currentListSize();
		
		if (listSize < 2)
			setNoRepeatMemory(0);

		if (noRepeatMemory > listSize - 1)
			setNoRepeatMemory(listSize - 1);
						
		if (sequenceMode == SequenceMode.NORMAL) {
			valueSupplier = sequence.get((counter % listSize) + startOffset);
			value = valueSupplier.nextValue();
			if (!valueSupplier.hasNext()) {
				valueSupplier.reset();
				counter++;			
			}
		} else if (sequenceMode == SequenceMode.REVERSE) {
			valueSupplier = sequence.get((listSize - 1 - counter % listSize) + startOffset);
			value = valueSupplier.nextValue();
			if (!valueSupplier.hasNext()) {
				valueSupplier.reset();
				counter++;
			}
			// important logic:
			// if a valuesupplier is null or doesn't have a next value, it should be reset and a 
			// new valuesupplier should be chosen.
			// If get past these guards, the previous valuesupplier is assumed.
			
			// A value is extracted from the valuesupplier.
			
			// If there aren't more values in the valueSupplier, we increment our counter
			// since the valuesupplier has run its course. Remember it's a difference between calling
			// .hasNext() before or after .nextValue();
			
			// This means we have to null the valueSupplier field when we change mode, as this is 
			// memory.
			
			// We should consider making an abstraction. But let's see how general it is.
		} else if (sequenceMode == SequenceMode.RANDOM) {
			if (valueSupplier == null || !valueSupplier.hasNext()) {
				if (valueSupplier != null) valueSupplier.reset();
				valueSupplier = sequence.get(random.nextInt(listSize) + startOffset);
			}
			
			value = valueSupplier.nextValue();	
			
			if (!valueSupplier.hasNext()) {
				counter++;
			}
			
			// using the exact same logic: guard clauses (where we potentially choose a new 
			// valuesupplier), retrieve value, post clauses.
		} else if (sequenceMode == SequenceMode.RANDOM_NO_REPEAT) {
			if (valueSupplier == null || !valueSupplier.hasNext()) {
				if (valueSupplier != null) valueSupplier.reset();
				// new valuesupplier here
				int randomIndex;
				while (true) {
					boolean repeatedInteger = false;
					randomIndex = random.nextInt(listSize) + startOffset;
					for (Integer previousIndex : noRepeatQueue) {
						if (randomIndex == previousIndex) {
							repeatedInteger = true;
							break;
						}
					}
					if (!repeatedInteger) break;
				}
				noRepeatQueue.add(randomIndex);
				noRepeatQueue.remove();
				valueSupplier = sequence.get(randomIndex);
			}
			
			value = valueSupplier.nextValue();
			
			if (!valueSupplier.hasNext()) {
				counter++;
			}
		} else if (sequenceMode == SequenceMode.RANDOM_WALK) {
			if (valueSupplier == null || !valueSupplier.hasNext()) {
				if (valueSupplier != null) valueSupplier.reset();
				valueSupplier = sequence.get(randomWalkMemory % listSize + startOffset);
			}
			
			value = valueSupplier.nextValue();
			
			if (!valueSupplier.hasNext()) {
				counter++;
				if (random.nextBoolean())
					randomWalkMemory += 1;
				else
					randomWalkMemory -= 1;
			}
		}
		return value;
	}
	
	public boolean hasNext() {
		return counter < length;
	}
	
	public void reset() {
		counter = 0;
	}
	
	public void add(ValueSupplier valueSupplier) {
		sequence.add(valueSupplier);
	}
	
	public void add(int index, ValueSupplier valueSupplier) {
		sequence.add(index, valueSupplier);
	}
	
	public ValueSupplier remove(int index) {
		if (sequence.size() <= 1) {
			throw new RuntimeException("A sequence has to have at least one element.");
		}
		return sequence.remove(index);
	}
	
	public int size() {
		return sequence.size();
	}
	
	public String getValueSupplierId(int index) {
		return sequence.get(index).toString();
	}
	
	public void set(int index, ValueSupplier valueSupplier) {
		sequence.set(index, valueSupplier);
	}
}
