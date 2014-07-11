package no.simonsen.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class DataStructures {
	public static void main(String[] args) {
		// Let's start with a list.
		// The list is a container of Double values. Double is chosen to maintain precision.
		System.out.println("The list:");
		List<Double> list = new ArrayList<>();
		list.add(1.); list.add(2.); list.add(3.); list.add(4.);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i));
		}
		System.out.println();
		
		// The list acts as a pattern.
		// We can loop the pattern. Let's output 16 values. We can output as many values as we want,
		// depending on n.
		System.out.println("Looping the list, 16 values:");
		int n = 16;
		for (int i = 0; i < n; i++) {
			System.out.print(list.get(i % list.size()) + " ");
		}
		System.out.println();
		
		// We can reverse the pattern as well.
		System.out.println("The list reversed.");
		for (int i = list.size()-1; i >= 0; i--) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
		
		// We can reverse loop as well. We make a small modification to the earlier loop.
		System.out.println("The reversed list is looped:");
		for (int i = 0; i < n; i++) {
			System.out.print(list.get(list.size() - 1 - i % list.size()) + " ");
		}
		System.out.println();
		
		// in the two previous loops, i is starting at zero. This is our offset, should we
		// wish to start elsewhere in the pattern. Note: The reversed list will start at an offset
		// relative to its reversed list.
		System.out.println("Looping the lists, offset at 2:");
		for (int i = 2; i < n; i++) {
			System.out.print(list.get(i % list.size()) + " ");
		}
		System.out.println();
		for (int i = 2; i < n; i++) {
			System.out.print(list.get(list.size() - 1 - i % list.size()) + " ");
		}
		System.out.println();
		
		// We might randomize the values. There are many different randomizing functions, this one
		// is the simplest.
		System.out.println("Randomizing the list.");
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			System.out.print(list.get(random.nextInt(list.size()) % list.size()) + " ");
		}
		System.out.println();
		
		// We can have a rule that prevents repeating indices. This only works when the list has
		// three or more values.
		System.out.println("Random, but with no-repeat rule.");
		int prevInt = -1;
		int newInt;
		for (int i = 0; i < n; i++) {
			while ((newInt = random.nextInt(list.size())) == prevInt) { }
			System.out.print(list.get(newInt % list.size()) + " ");
			prevInt = newInt;
		}
		System.out.println();
		
		// A more general approach to this is to specify that the last k values should not be
		// repeated, where k >= list.size() - 2.
		System.out.println("Random, but with generalized no-repeat rule.");
		int nrOfNoRepeats = 2;
		if (nrOfNoRepeats >= list.size() - 2) {
			Queue<Integer> linkedList = new LinkedList<>();
			for (int i = 0; i < nrOfNoRepeats; i++) linkedList.add(-1);
			
			for (int i = 0; i < n; i++) {
				while (true) {
					boolean repeatedInteger = false;
					newInt = random.nextInt(list.size());
					for (Integer integer : linkedList) {
						if (newInt == integer) {
							repeatedInteger = true;
							break;
						}
					}
					if (!repeatedInteger) break;
				}
				System.out.print(list.get(newInt % list.size()) + " ");
				linkedList.remove();
				linkedList.add(newInt);
			}
			System.out.println();
		}
		
		// For large lists and a large k size, this is not particularly efficient - but that can
		// be remedied. If we made a new temporary list and took out the values as they were
		// drawn, we could re-insert them after a given amount of iterations.
		
		// When k is maximum (as in this example, where k is 2 and the list size is 4), we really
		// have a scrambled looped list. The list is scrambled once, and is then looped.
		List<Integer> indexPool = new LinkedList<>();
		for (int i = 0; i < list.size(); i++) indexPool.add(i);
		List<Double> scrambledList = new ArrayList<>();
		for (int i = 0, j = list.size(); i < list.size(); i++, j--) {
			scrambledList.add(list.get(indexPool.remove(random.nextInt(j))));
		}
		System.out.println("Scrambled, looped list:");
		for (int i = 0; i < n; i++) {
			System.out.print(scrambledList.get(i % list.size()) + " ");
		}
		System.out.println();
		
		// We might shorten a list, until there is one element left.
		// The shortening of the list is a fairly basic operation.
		// In fact, any excerpt from the main list should be done before the randomizing functions.
		int shortenBy = 1;
		int newListSize = list.size() - shortenBy;
		if (newListSize < 1) newListSize = 1;
		System.out.println("Looping a limited list.");
		for (int i = 0; i < n; i++) {
			System.out.print(list.get(i % newListSize) + " ");
		}
		System.out.println();
		
	}
}
