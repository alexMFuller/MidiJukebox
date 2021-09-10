import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import edu.uwm.cs351.Note;
import edu.uwm.cs351.Song;

public class TestEfficiency extends TestCase {

	Note p1 = new Note(10,1.0);
	Note p2 = new Note(20,2.0);
	Note p3 = new Note(30,3.0);
	Note p4 = new Note(40,4.0);
	Note p5 = new Note(50,5.0);
	Note p6 = new Note(60,6.0);
	Note p7 = new Note(70,7.0);
	Note p8 = new Note(80,8.0);

	Note p[] = {null, p1, p2, p3, p4, p5, p6, p7, p8};
	
	Song s;
	Random r;
	
	@Override
	public void setUp() {
		s = new Song();
		r = new Random();
		try {
			assert 1/(int)(p5.getMidiPitch()-50) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}

	private static final int MAX_LENGTH = 1000000;
	private static final int SAMPLE = 100;
	
	public void testLong() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.insert(p[i%6]);
			s.advance();
		}
		
		int sum = 0;
		s.start();
		for (int j=0; j < SAMPLE; ++j) {
			int n = r.nextInt(MAX_LENGTH/SAMPLE);
			for (int i=0; i < n; ++i) {
				s.advance();
			}
			sum += n;
			assertSame(p[sum%6],s.getCurrent());
		}
	}
	
	private static final int MAX_WIDTH = 100000;
	
	public void testWide() {
		Song[] a = new Song[MAX_WIDTH];
		for (int i=0; i < MAX_WIDTH; ++i) {
			a[i] = s = new Song();
			int n = r.nextInt(SAMPLE);
			for (int j=0; j < n; ++j) {
				s.insert(p[j%6]);
				s.advance();
			}
		}
		
		for (int j = 0; j < SAMPLE; ++j) {
			int i = r.nextInt(a.length);
			s = a[i];
			if (s.size() == 0) continue;
			int n = r.nextInt(s.size());
			s.start();
			for (int k=0; k < n; ++k) {
				s.advance();
			}
			assertSame(p[n%6],s.getCurrent());
		}
	}
	
	public void testStochastic() {
		List<Song> ss = new ArrayList<Song>();
		ss.add(s);
		int max = 1;
		for (int i=0; i < MAX_LENGTH; ++i) {
			if (r.nextBoolean()) {
				s = new Song();
				s.insert(p3);
				ss.add(s);
			} else {
				s.insertAll(s); // double size of s
				if (s.size() > max) {
					max = s.size();
					// System.out.println("Reached " + max);
				}
			}
		}
	}
}
