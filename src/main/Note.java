package main;

/**
 * The Immutable Class Note.
 */
public class Note {

	/** Static Constants */
	public static final int DEFAULT_INTENSITY = 50;
	public static final int REST_PITCH = 128; // First illegal pitch, used for rests.
	private static final int PITCHES_PER_OCTAVE = 12;
	private static final String[] NOTE_LETTERS = {"c","c#","d","d#","e","f","f#","g","g#","a","a#","b"};
	private static final double MIN_DURATION = 1.0/64, // One sixty-fourth
								MAX_DURATION = 8.0; // Eight whole notes

	/** Fields (Immutable) */
	private final String pitch;
	private final int midiValue;
	private final double duration;

	/**
	 * Instantiates a new note based on a string denoting note letter and octave.
	 * ### \subsection{Constructors}
	 * ### \verb|Node(String pitch, double duration)|
	 *
	 * @param pitch the pitch (e.g. "f6")
	 * @param duration the duration
	 * @throws NullPointerException if pitch is null
	 * @throws IllegalArgumentException if:
	 * 				1. The pitch parameter is malformed or out of range.
	 * 				2. The duration parameter is out of range.
	 */
	public Note(String pitch, double duration) {
		// #(
		this(toMidi(pitch), duration);
		// #)
		// TODO
		// Recommended: First implement toMidi(String).
	}

	/**
	 * Instantiates a new note based on MIDI value.
	 * ### \verb|Node(int midiValue, double duration)|
	 * @param midiValue the MIDI value (e.g. 68)
	 * @param duration the duration
	 * @throws IllegalArgumentException if:
	 * 				1. The MIDI pitch parameter is out of range.
	 * 				2. The duration parameter is out of range.
	 */
	public Note(int midiValue, double duration) {
		// #(
		String pitch = toPitch(midiValue);

		if (pitch == null)
			throw new IllegalArgumentException("Invalid MIDI value: " + midiValue + " -- Must be [0, 128]");
		if (duration < MIN_DURATION || duration > MAX_DURATION)
			throw new IllegalArgumentException("Invalid note duration: " + duration+" -- Must be [1/64, 8]");

		this.pitch = pitch;
		this.midiValue = midiValue;
		this.duration = duration;
		// #)
		// TODO
		// Recommended: First implement toPitch(int).
	}

	/**
	 * Instantiates a new note from a String matching the format of Note's toString() method.
	 *
	 * @param note the string representation
	 * 
	 * @throws IndexOutOfBoundsException if parameter isn't in correct format
	 * @throws NumberFormatException if duration representation cannot be parsed as double
	 * @throws IllegalArgumentException if the elements in the format are not permitted.
	 */
	public Note(String note) {
		this(note.split(" x ")[0], Double.parseDouble(note.split(" x ")[1]));
	}

	/**
	 * Converts a pitch string to a MIDI value.
	 * The pitch "rest" should return {@link #REST_PITCH}.
	 * ### \subsection{toMidi}
	 *
	 * @param pitch the pitch to convert
	 * @throws NullPointerException if pitch is null
	 * @throws IllegalArgumentException is the String is not a legal pitch
	 * @return the MIDI value
	 */
	public static int toMidi(String pitch) {
		// #(
		int result;
		if (pitch.equals("rest")) // force NPE
			result = REST_PITCH;
		else {
			try {
				String[] tokens = pitch.split("(?=\\d)", 2);
				findNote: if (tokens.length == 2) {
					int octave = Integer.parseInt(tokens[1]);
					for (int i = 0; i < NOTE_LETTERS.length; i++) {
						if (NOTE_LETTERS[i].equals(tokens[0])) {
							// i is the offset from the octave's "c" note
							result = i + PITCHES_PER_OCTAVE * octave;
							if (result < 0 || result >= REST_PITCH)
								throw new IllegalArgumentException("pitch out of legal range.");
							break findNote;
						}
					}
					throw new IllegalArgumentException("unknown note: '" + tokens[0] + "'");
				} else {
					throw new IllegalArgumentException("can't parse '" + pitch + "'");
				}
			} catch (IllegalArgumentException ex) {
				throw ex;
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		return result;
		/* #)
		// TODO
		return -1;
		## */
	}

	/**
	 * Converts a MIDI value to a pitch string.
	 * The MIDI value 128 should return "rest".
	 * ### \subsection{toPitch}
	 * 
	 * @param midiValue the MIDI value to convert
	 * @throws IllegalArgumentException if the MIDI value is outside of legal range
	 * @return the pitch string
	 */
	public static String toPitch(int midiValue) {
		// #(
		String result;
		if (midiValue >= 0 && midiValue < REST_PITCH) {
			int noteIndex = midiValue % PITCHES_PER_OCTAVE;
			int octave = midiValue / PITCHES_PER_OCTAVE;
			result = NOTE_LETTERS[noteIndex] + (int) octave;
		}
		else if (midiValue == REST_PITCH)
			result = "rest";
		else throw new IllegalArgumentException("pitch out of range");
		return result;
		/* #)
		// TODO
		return null;
		## */
	}

	/**
	 * Gets the pitch string of this note.
	 *
	 * @return the pitch
	 */
	public String getPitch() { return pitch; }

	/**
	 * Gets the MIDI value of this note.
	 *
	 * @return the MIDI value
	 */
	public int getMidiPitch() { return midiValue; }

	/**
	 * Gets the duration of this note.
	 *
	 * @return the duration
	 */
	public double getDuration() { return duration; }
	

	/**
	 * Returns a new note with the same pitch, but with its duration multiplied by the parameter.
	 * ### \subsection{stretch}
	 * 
	 * @param factor the amount to scale by
	 * @throws IllegalArgumentException if resultant duration is outside of valid range
	 * @return the stretched note
	 */
	public Note stretch(double factor) {
		// #(
		return new Note(pitch, duration * factor);
		/* #)
		// TODO
		return null;
		## */
	}

	// ### \subsection{transpose}
	// ### Documentation comment:
	/** #(
	 * Returns a (new) note with the same duration, but transposed by the given interval.
	 * Rests are unaffected.
	 * @param interval the interval to transpose by
	 * @throws IllegalArgumentException if note is transposed beyond valid bounds [c0, g10] 
	 * @return the transposed note
	 */ // #)
	// TODO: add documentation comment. "Source > Generate Element Comment" can help
	public Note transpose(int interval) {
		// ### Code:
		// #(
		Note result = null;
		if (midiValue == REST_PITCH)
			result = this;
		else {
			int newMidiValue = midiValue + interval;
			if (newMidiValue == REST_PITCH)
				newMidiValue++;
			result = new Note(newMidiValue, duration);
		}
		return result;
		/* #)
		// TODO
		return null;
		## */
	}

	/**
	 * Returns a string representation of this Note.
	 * It should follow the format found in songs/InMyLife.song, namely:
	 * 	For a Note with pitch "g#4" and duration 1.0625 -> "g#4 x 1.0625"
	 * NB1: Identical spacing and format are important!
	 * NB2: For a "rest" note, the same format must be used (including duration).
	 * ### \subsection{toString}
	 * 
	 * @return the string representation
	 */
	@Override // completely new implementation
	public String toString() {
		// #(
		return pitch + " x " + duration;
		/* #)
		// TODO
		return null;
		## */
	}

	// ### \subsection{Other methods}
	// #(
	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Note) {
			Note other = (Note) o;
			return this.pitch.equals(other.pitch) &&
					this.midiValue == other.midiValue &&
					this.duration == other.duration;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// #(
		long durHash = Double.doubleToLongBits(duration);
		durHash = durHash ^ (durHash >> 32);
		
		return (int) (durHash + midiValue);
		// #)
	}
	// #)
	// TODO: more to do.  (Read homework assignment.)
}
