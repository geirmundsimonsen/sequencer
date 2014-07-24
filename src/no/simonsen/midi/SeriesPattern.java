package no.simonsen.midi;

public class SeriesPattern implements ValueSupplier {
	private String id;
	private int counter;
	private int length;
	private SeriesMode seriesMode;
	private double initialValue;
	private double currentValue;
	private double step;
	private double factor;
	
	public SeriesPattern(String id) {
		this.id = id;
		counter = 0;
		length = Integer.MAX_VALUE;
		seriesMode = SeriesMode.CONSTANT;
		initialValue = 0;
		currentValue = initialValue;
		step = 1;
	}
	
	public String getId() {
		return id;
	}
	
	public double nextValue() {
		double value = -1;
		
		if (seriesMode == SeriesMode.CONSTANT) {
			value = currentValue;
			currentValue += step;
			counter++;
		} else if (seriesMode == SeriesMode.EXPONENTIAL) {
			value = currentValue;
			currentValue *= factor;
			counter++;
		}
		
		return value;
	}

	public boolean hasNext() {
		return counter < length;
	}

	public void reset() {
		currentValue = initialValue;
	}
}

enum SeriesMode {
	CONSTANT, EXPONENTIAL
}