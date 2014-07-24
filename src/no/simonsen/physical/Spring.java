package no.simonsen.physical;

public class Spring {
	private double strength;
	private double frequency;
	private double initialPosition; // in radians.
	private int counter;
	
	public Spring() {
		strength = 1;
		frequency = 0.1;
		initialPosition = 0;
		counter = 0;
	}
	
	public double getNextSample() {
		if (strength < 0) { return 0; }
		double value = Math.sin(initialPosition + Math.PI * frequency * counter) * strength;
		counter++;
		strength -= 0.01;
		return value;
	}
	
	public static void main(String[] args) {
		Spring spring = new Spring();
		
		for (int i = 0; i < 100; i++) {
			double value = spring.getNextSample();
			System.out.println(valueAsAsciiArt(value));
		}
	}
	
	public static String valueAsAsciiArt(double value) {
		int slot = (int) Math.rint(20 + 20 * value);
		if (slot < 0) slot = 0;
		if (slot > 40) slot = 40;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < slot; i++) {
			sb.append('-');
		}
		sb.append('#');
		return sb.toString();
	}
}
