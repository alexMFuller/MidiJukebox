// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.
/*
 * 
 */


/* Alex Fuller
 * CS 351
 */

package edu.uwm.cs351;

import junit.framework.TestCase;

/******************************************************************************
 * This class is a homework assignment;
 * A Song is a sequence of Note objects.
 * The song can have a special "current element," which is specified and 
 * accessed through four methods that are available in the song class 
 * (start, getCurrent, advance and hasCurrent).
 *
 * @note
 *   (1) The capacity of a song can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, insert, insertAll, clone, 
 *   and catenation methods will result in an
 *   OutOfMemoryError when free memory is exhausted.
 *   <p>
 *   (2) A song's capacity cannot exceed the maximum integer 2,147,483,647
 *   (Integer.MAX_VALUE). Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 *   
 *   NB: Neither of these conditions require any work for the implementors (students).
 *
 *
 ******************************************************************************/
public class Song implements Cloneable {

	/** Static Constants */
	private static final String DEFAULT_NAME = "Untitled";
	private static final int DEFAULT_BPM = 60;
	private static final int INITIAL_CAPACITY = 1;
	public static final int MIN_BPM = 20, MAX_BPM = 1000;

	/** Fields */
	private String name;
	private int bpm;
	private Note[ ] data;
	private int manyItems;
	private int currentIndex;

	// Invariant of the Song class:
	//   1. The name is not null.
	//   2. The bpm is in the range [MIN_BPM,MAX_BPM]
	//   3. The number of elements in the songs is in the instance variable 
	//      manyItems.
	//   4. For an empty song (with no elements), we do not care what is 
	//      stored in any of data; for a non-empty song, the elements of the
	//      song are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   5. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex equals manyItems.

	private static boolean doReport = true; // change onluy in invariant tests
	private boolean report(String error) {
		if (doReport) {
			System.out.println("Invariant error: " + error);
		}
		return false;
	}

	private boolean wellFormed() {
		// Check the invariant.
		
		// 1. name is never null
		if (name == null) return report("name is null");

		// 2. _bpm is in valid range: [MIN_BPM, MAX_BPM]
		// TODO
		if (bpm>MAX_BPM||bpm<MIN_BPM) return report("BPM is out of range");
		// 3. data array is never null
		// TODO
		if (data==null) return report ("data array is null");
		// 4. The data array has at least as many items in it as manyItems
		//    claims the song has
		if (data.length<manyItems) return report("manyItems is too large for the given data array");
		// TODO
		
		// 5. currentIndex is never negative and never more than the number of
		//    items in the song.
		// TODO	
		if (currentIndex<0||currentIndex>manyItems) return report("invalid current index");
		// If no problems discovered, return true
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private Song(boolean testInvariant) { }

	/**
	 * Initialize an empty song with name DEFAULT_NAME, bpm DEFAULT_BPM, and 
	 * an initial capacity of INITIAL_CAPACITY. The {@link #insert(Note)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This song has the default name and BPM, is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public Song( )
	{
		// TODO: Implemented by student.
		name = DEFAULT_NAME;
		bpm = DEFAULT_BPM;
		data= new Note[INITIAL_CAPACITY];
		
		assert wellFormed() : "invariant failed at end of constructor";
	}

	/**
	 * Initialize an empty song with a specified name and bpm, an initial
	 * capacity of INITIAL_CAPACITY. The {@link #insert(Note)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param name
	 *   the name of this song, must not be null
	 * @param bpm
	 *   the beats per minute of this song, must be in the range [MIN_BPM,MAX_BPM]
	 * @postcondition
	 *   This song is empty, has specified name and bpm, and has an initial
	 *   capacity of INITIAL_CAPACITY.
	 * @throws IllegalArgumentException
	 *    If the name is null, or the bpm is out of the legal range.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Note[initialCapacity].
	 **/   
	public Song(String name, int bpm)
	{
		// TODO: Implemented by student.
		if (name == null) throw new IllegalArgumentException("Name is null");
		if (bpm>MAX_BPM||bpm<MIN_BPM) throw new IllegalArgumentException("BPM is an invalid value");
		this.name = name;
		this.bpm = bpm;
		data= new Note[INITIAL_CAPACITY];
		
		assert wellFormed() : "invariant failed at end of constructor";
	}

	/**
	 * Initialize an empty song with a specified initial capacity.
	 * The {@link #insert(Note)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param n
	 *   the name of this song, must not be null
	 * @param beats
	 *   the beats per minute of this song, must be in the range [MIN_BPM,MAX_BPM]
	 * @param initialCapacity
	 *   the initial capacity of this song, must not be negative
	 * @exception IllegalArgumentException
	 *   Indicates that name, bpm or initialCapacity are invalid
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Note[initialCapacity].
	 **/   
	public Song(String n, int beats, int initialCapacity)
	{
		if (n == null) throw new IllegalArgumentException("Name is null");
		if (beats>MAX_BPM||beats<MIN_BPM) throw new IllegalArgumentException("BPM is an invalid value");
		if (initialCapacity<0) throw new IllegalArgumentException("Initial Capacity is invalid");
		this.name = n;
		this.bpm = beats;
		data= new Note[initialCapacity];
		
		// TODO: Implemented by student.
		assert wellFormed() : "invariant failed at end of constructor";
	}

	/**
	 * Gets the name of the song
	 * @return the name
	 */
	public String getName() {
		assert wellFormed() : "invariant failed at start of getName";
		return name;
	}

	/**
	 * Gets the beats per minute of the song.
	 * @return the bpm
	 */
	public int getBPM() {
		assert wellFormed() : "invariant failed at start of getBPM";
		return bpm;
	}

	/**
	 * Gets the total duration of the song by adding duration of all its notes.
	 * Null notes should be ignored.
	 * @return the total duration
	 */
	public double getDuration() {
		assert wellFormed() : "invariant failed at start of getDuration";
		double result = 0;
		// TODO
		for (int i = 0; i<manyItems;i++) {
			if(data[i]!=null) {
				result = result+data[i].getDuration();
			}
		}
		return result;
	}

	/**
	 * Sets the name of the song.
	 * @param newName the new name, must not be null
	 * @throws IllegalArgumentException in the new name is null
	 */
	public void setName(String newName) {
		assert wellFormed() : "invariant failed at start of setName";
		// TODO
		if (newName == null) throw new IllegalArgumentException("name is null");
		this.name = newName;
		
		
		assert wellFormed() : "invariant failed at end of setName";
	}

	/**
	 * Sets the beats per minute (BPM) of the song.
	 * @param newBPM the new bpm
	 * @throws IllegalArgumentException in the new BPM is not in the range [MIN_BPM,MAX_BPM]
	 */
	public void setBPM(int newBPM) {
		assert wellFormed() : "invariant failed at start of setBPM";
		// TODO
		if (newBPM>MAX_BPM||newBPM<MIN_BPM) throw new IllegalArgumentException("BPM is an invalid value");
		if (newBPM != this.bpm) {
			this.bpm = newBPM;
		}
		assert wellFormed() : "invariant failed at end of setBPM";
	}

	/**
	 * Stretches the song by the given factor, lengthening or shortening its duration.
	 * Null note sin the Song are left unchanged.
	 * If there's a problem with one of the notes, this Song may be left
	 * partially stretched.
	 * @param factor the factor to multiply each note's duration by
	 * @throws IllegalArgumentException if song is transposed where a note's duration
	 * 				is beyond the valid bounds
	 */
	public void stretch(double factor) {
		assert wellFormed() : "invariant failed at start of stretch";
		// TODO stretch each note in the song
		for (int i = 0; i<manyItems;i++) {
			if(data[i]!=null) {
				if (data[i].getDuration()*factor<0) throw new IllegalArgumentException("Invalid duration found, song partially streched");
				data[i] = data[i].stretch(factor);
			}
		}
		
		assert wellFormed() : "invariant failed at end of stretch";
	}


	/**
	 * Transposes the song by the given interval, raising or lowering its pitch.
	 * Null notes are ignored (left in place unchanged).
	 * If there's a problem with transposing one of the notes, this Song may be left
	 * partially transposed.
	 * @param interval the interval to transpose each note in the song
	 * @throws IllegalArgumentException if song is transposed where a note is beyond the bounds
	 * 									of valid MIDI pitch values [0,127]
	 */
	public void transpose(int interval) {
		assert wellFormed() : "invariant failed at start of transpose";
		// TODO transpose each note in the song
		for (int i = 0; i<manyItems;i++) {
			if(data[i]!=null) {
				if (data[i].getMidiPitch()+interval>127||data[i].getMidiPitch()+interval<0) throw new IllegalArgumentException("Invalid transposal found, song partially streched");
				data[i] = data[i].transpose(interval);
			}
		}
		assert wellFormed() : "invariant failed at end of transpose";
	}


	/**
	 * Determine the number of elements in this song.
	 * @param - none
	 * @return
	 *   the number of elements in this song
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant failed at start of size";
		// TODO: Implemented by student.
		return manyItems;
	}

	/**
	 * Set the current element at the front of this song.
	 * @param - none
	 * @postcondition
	 *   The front element of this song is now the current element (but 
	 *   if this song has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant failed at start of start";
		// TODO: Implemented by student.
		if (data==null) currentIndex= manyItems;
		else {
			currentIndex=0;
			
		}
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this song has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean hasCurrent( )
	{
		assert wellFormed() : "invariant failed at start of hasCurrent";
		
		return !(currentIndex>=manyItems);
		// TODO: Implemented by student.
	}

	/**
	 * Accessor method to get the current element of this song. 
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @return
	 *   the current element of this song
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Note getCurrent( )
	{
		assert wellFormed() : "invariant failed at start of getCurrent";
		// TODO: Implemented by student.
		// Don't change "this"!
		if (!hasCurrent()) throw new IllegalStateException("No current value");
		return data[currentIndex];
	}

	/**
	 * Move forward, so that the current element will be the next element in
	 * this song.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this song 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant failed at start of advance";
		// TODO: Implemented by student.
		if (!hasCurrent()) throw new IllegalStateException("No current value");
		
		currentIndex++;
		
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Change the current capacity of this song as needed so that
	 * the capacity is at least as big as the parameter.
	 * This code must work correctly and efficiently if the minimum
	 * capacity is (1) smaller or equal to the current capacity (do nothing)
	 * (2) at most double the current capacity (double the capacity)
	 * or (3) more than double the current capacity (new capacity is the
	 * minimum passed).
	 * @param minimumCapacity
	 *   the new capacity for this song
	 * @postcondition
	 *   This song's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		// TODO: Implemented by student.
		// NB: do not check invariant
		int newCap = 0;
		Note[] newData;
		if (data.length < minimumCapacity) {
			if (data.length*2<minimumCapacity) {
				newData= new Note[ minimumCapacity];
				
			}
			else {
				newData = new Note[data.length*2];
			}
			for (int i = 0; i<manyItems;i++) {
				newData[i] = data[i];
			}
			data = newData;
			
		}
	}

	/**
	 * Add a new element to this song, before the current element (if any). 
	 * If the new element would take this song beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this song. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the song. In all cases, the new element becomes the
	 *   new current element of this song. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the song's capacity.
	 **/
	public void insert(Note element)
	{
		assert wellFormed() : "invariant failed at start of insert";
		// TODO: Implemented by student.
		ensureCapacity(manyItems+1);
		if(hasCurrent()) {
			int i;
			for (i=manyItems; i>currentIndex; --i) {
				data[i] = data[i-1];
			}
			data[i] = element;
			manyItems++;
			
		}
		else {
			
			data[manyItems++] = element;
			

		}
		assert wellFormed() : "invariant failed at end of insert";
	}


	/**
	 * Remove the current element from this song.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this song, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant failed at start of removeCurrent";
		// TODO: Implemented by student.
		if (!hasCurrent()) throw new IllegalStateException("No current value");
		manyItems--;
		for (int i = currentIndex;i<manyItems;i++) {
			data[i]=data[i+1];
		}
		
		assert wellFormed() : "invariant failed at end of removeCurrent";
	}

	
	/**
	 * Place the contents of another song (which may be the
	 * same song as this!) into this song before the current element.
	 * @param addend
	 *   a song whose contents will be placed into this song
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this song. The current element of this song is now
	 *   the first element inserted (if any).  If the added song
	 *   is empty, this song and the current element (if any) are
	 *   unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this song.
	 **/
	public void insertAll(Song addend)
	{
		assert wellFormed() : "invariant failed at start of insertAll";
		// TODO: Implemented by student.
		// Watch out for the this==addend case!
		
		// (It is possible to write code that works for this case AND 
		// the normal case, but you have to be very careful.)
		if (addend==null) throw new NullPointerException("addend is null");
		ensureCapacity(manyItems+addend.manyItems);
		int i;
		Song k = addend.clone();
		for (i=k.manyItems;i>0;--i) {
			int j;
			if(hasCurrent()) {
			for (j=manyItems; j>currentIndex; --j) {
				data[j] = data[j-1];
			}
			data[j] = k.data[i-1];
			manyItems++;
			}
			
			
			else {
				
				if (currentIndex>0) {
				
				data[currentIndex] = addend.data[i-1];
				}
				else {
					data[currentIndex] = addend.data[i-1];
				}
				manyItems++;
			}
		}
		
		
		assert wellFormed() : "invariant failed at end of insertAll";
		assert addend.wellFormed() : "invariant of addend broken in insertAll";
	}

	/**
	 * Generate a copy of this song.
	 * @param - none
	 * @return
	 *   The return value is a copy of this song. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public Song clone( ) { 
		assert wellFormed() : "invariant failed at start of clone";
		Song answer;
	
		try
		{
			answer = (Song) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}
	
		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		answer.data = data.clone( );
	
		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant on answer failed at end of clone";
		return answer;
	}

	/**
	 * Create a new song that contains all the elements from one song
	 * followed by another.  The new BPM is the average of the two songs,
	 * and the name is the concatenation of the two names separated by " and "
	 * @param s1
	 *   the first of two songs
	 * @param s2
	 *   the second of two songs
	 * @precondition
	 *   Neither s1 nor s2 is null.
	 * @return
	 *   a new song that has the elements of s1 followed by the
	 *   elements of s2 (with no current element).
	 * @exception NullPointerException.
	 *   Indicates that one of the arguments is null.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for the new song.
	 **/   
	public static Song catenation(Song s1, Song s2)
	{
		assert s1.wellFormed() : "invariant of s1 failed at start of catenation";
		assert s2.wellFormed() : "invariant of s2 failed at start of catenation";
		
		if (s1==null) throw new NullPointerException("s1 is null");
		if (s2==null) throw new NullPointerException("s2 is null");
		
		Song res = new Song(s1.getName()+" and "+s2.getName(), (s1.getBPM()+s2.getBPM())/2,s1.manyItems+s2.manyItems);
		
		res.insertAll(s2);
		res.insertAll(s1);
			
		res.currentIndex = res.manyItems;
		
		// TODO: Implemented by student.
		assert s1.wellFormed() : "invariant of s1 failed at end of catenation";
		assert s2.wellFormed() : "invariant of s2 failed at end of catenation";
		assert res.wellFormed() : "invariant of res failed at end of catenation";
		return res;
	}


	// don't change this nested class:
	public static class TestInvariantChecker extends TestCase {
		Song hs = new Song(false);

		protected void setUp() {
			hs = new Song(false);
			hs.name = "";
			hs.bpm = MIN_BPM;
			Song.doReport = false;
		}

		public void test0() {
			assertFalse(hs.wellFormed());
		}
		
		public void test1() {
			hs.data = new Note[0];
			assertTrue(hs.wellFormed());
			hs.name = null;
			assertFalse(hs.wellFormed());
		}
		
		public void test2() {
			hs.data = new Note[1];
			assertTrue(hs.wellFormed());
			hs.bpm = MIN_BPM-1;
			assertFalse(hs.wellFormed());
			hs.bpm = MAX_BPM+1;
			assertFalse(hs.wellFormed());
			hs.bpm = MAX_BPM;
			assertTrue(hs.wellFormed());
		}

		public void test6() {
			hs.data = new Note[3];
			hs.manyItems = -2;
			assertFalse(hs.wellFormed());
			hs.manyItems = -1;
			assertFalse(hs.wellFormed());
			hs.manyItems = 0;
			assertTrue(hs.wellFormed());
		}

		public void test7() {
			hs.data = new Note[3];
			hs.manyItems = 4;
			assertFalse(hs.wellFormed());
			hs.manyItems = 3;
			assertTrue(hs.wellFormed());
		}

		public void test8() {
			hs.data = new Note[3];
			hs.manyItems = 2;
			hs.currentIndex = -1;
			assertFalse(hs.wellFormed());
			hs.currentIndex = 3;
			assertFalse(hs.wellFormed());
			hs.currentIndex = 2;
			assertTrue(hs.wellFormed());
		}
	}
}

