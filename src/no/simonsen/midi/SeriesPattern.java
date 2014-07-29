package no.simonsen.midi;

/**
 * SeriesPattern outputs a value in a constant or exponential series.
 */
public class SeriesPattern implements ValueSupplier {
	private String id;
	private int counter;
	private int length;
	private SeriesPatternMode seriesMode;
	private double initialValue;
	private double currentValue;
	private double step;
	private double factor;
	
	public SeriesPattern(String id) {
		this.id = id;
		counter = 0;
		length = Integer.MAX_VALUE;
		seriesMode = SeriesPatternMode.CONSTANT;
		initialValue = 0;
		currentValue = initialValue;
		step = 1;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public double nextValue() {
		double value = -1;
		
		if (seriesMode == SeriesPatternMode.CONSTANT) {
			value = currentValue;
			currentValue += step;
			counter++;
		} else if (seriesMode == SeriesPatternMode.EXPONENTIAL) {
			value = currentValue;
			currentValue *= factor;
			counter++;
		}
		
		return value;
	}

	@Override
	public boolean hasNext() {
		return counter < length;
	}

	@Override
	public void reset() {
		currentValue = initialValue;
		counter = 0;
	}
	
	public double getInitialValue() {
		return initialValue;
	}
	
	public void setInitialValue(double value) {
		initialValue = value;
	}
	
	public double getLength() {
		return length;
	}
	
	public void setLength(int value) {
		length = value;
	}
	
	public SeriesPatternMode getMode() {
		return seriesMode;
	}
	
	public void setMode(SeriesPatternMode mode) {
		seriesMode = mode;
	}
	
	public double getStep() {
		return step;
	}
	
	public void setStep(double value) {
		step = value;
	}
	
	public double getFactor() {
		return factor;
	}
	
	public void setFactor(double value) {
		factor = value;
	}
}