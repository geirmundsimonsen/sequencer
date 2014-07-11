package no.simonsen.data;

public enum SequenceMode {
	MANUAL, // lazy, immediate, immediate and reset
	NORMAL, 
	REVERSE,
	BACK_AND_FORTH, // opt.: counting edges
	JUMP, // jump over 1..N, consider reverse
	RANDOM, 
	RANDOM_NO_REPEAT,
	SCRAMBLE,
	RANDOM_WALK // opt.: including no walk
}
