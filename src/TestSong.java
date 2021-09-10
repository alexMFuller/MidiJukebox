import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Note;
import edu.uwm.cs351.Song;


public class TestSong extends LockedTestCase {
	Note e1 = new Note("e1",1.0); 
	Note e2 = new Note("e2",0.5);
	Note e3 = new Note("e3",0.3333);
	Note e4 = new Note("e4",0.25);
	Note e5 = new Note("e5",0.2);

	Song s;
	
	protected Song newSong() {
		return new Song();
	}

	@Override
	protected void setUp() {
		s = newSong();
		try {
			assert 1/((int)e1.getDuration()-1) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	protected <T> void assertException(Class<?> excClass, Supplier<T> producer) {
		try {
			T result = producer.get();
			assertFalse("Should have thrown an exception, not returned " + result,true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	protected <T> void assertException(Runnable f, Class<?> excClass) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Return the note as an integer
	 * <dl>
	 * <dt>-1<dd><i>(an exception was thrown)
	 * <dt>0<dd>null
	 * <dt>1<dd>e1
	 * <dt>2<dd>e2
	 * <dt>3<dd>e3
	 * <dt>4<dd>e4
	 * <dt>5<dd>e5
	 * </dl>
	 * @return integer encoding of note supplied
	 */
	protected int asInt(Supplier<Note> g) {
		try {
			Note n = g.get();
			if (n == null) return 0;
			return (n.getPitch().charAt(1))-'0';
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	public void test00() {
		// Nothing inserted yet:
		assertEquals(Ti(1112658640),s.size());
		assertFalse(s.hasCurrent());
		s.start();
		assertFalse(s.hasCurrent());
	}
	
	public void test01() {
		// Initially empty.
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1848063),asInt(() -> s.getCurrent()));
		s.insert(e1);
		assertEquals(Ti(337008384),asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(Ti(901033071),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(257085790),asInt(() -> s.getCurrent()));
	}
	
	public void test02() {
		// Initially empty.
		s.insert(e4);
		s.insert(e5);
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1876093076),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(56523864),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1671626331),asInt(() -> s.getCurrent()));
	}
	
	public void test03() {
		// Initially empty
		s.insert(e3);
		s.advance();
		s.insert(e2);
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1068398624),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(2129376917),asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(Ti(2073343044),asInt(() -> s.getCurrent()));
	}
	
	public void test05() {
		// Initially empty
		s.insert(null);
		assertEquals(Ti(1049586199),s.size());
		assertEquals(Tb(1284130738),s.hasCurrent());
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1728369560),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(718450125),asInt(() -> s.getCurrent()));
	}
	
	public void test06() {
		s.insert(e1);
		s.insert(e2);
		s.start();
		s.advance();
		assertSame(e1,s.getCurrent());
		Song s2 = newSong();
		s2.insert(e4);
		s.insertAll(s2);
		assertEquals(Ti(1153117195),s.size());
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(429923611),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(13699069),asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1034153840),asInt(() -> s.getCurrent()));
	}
	
	public void test07() {
		s.start();
		try {
			s.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	
	public void test08() {
		s.start();
		try {
			s.removeCurrent();
			assertFalse("empty.removeCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	
	public void test09() {
		try {
			s.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	
	public void test10() {
		s.insert(e1);
		assertEquals(1,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(1,s.size());
	}

	public void test11() {
		s.insert(e1);
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertEquals(0,s.size());	
		s.insert(e2);
		s.start();
		assertSame(e2,s.getCurrent());
		assertEquals(1,s.size());
	}
	
	public void test12() {
		s.insert(e2);
		s.start();
		s.advance();
		try {
			s.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(s.hasCurrent());
		assertEquals(1,s.size());
	}


	public void test13() {
		s.insert(e2);
		s.advance();
		try {
			s.removeCurrent();
			assertFalse("s.removeCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(s.hasCurrent());
		assertEquals(1,s.size());
	}

	public void test14() {
		s.insert(e2);
		s.advance();
		try {
			s.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(s.hasCurrent());
		assertEquals(1,s.size());
	}

	public void test20() {
		s.insert(e1);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.insert(e2);
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		s.start();
		assertSame(e2,s.getCurrent());
	}
	
	public void test21() {
		s.insert(e1);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		s.insert(e2);
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(e1,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		assertTrue(s.hasCurrent());
	}
	
	public void test22() {
		s.insert(e2);
		s.insert(e1);
		s.start();
		s.removeCurrent();
		assertTrue(s.hasCurrent());
		assertEquals(1,s.size());
		assertEquals(e2,s.getCurrent());
		s.advance();
		s.insert(e3);
		assertTrue(s.hasCurrent());
		assertEquals(2,s.size());
		assertSame(e3,s.getCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
	}

	public void test23() {
		s.insert(e2);
		s.insert(e1);
		s.start();
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertEquals(1,s.size());
		try {
			s.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		s.insert(e3);
		assertTrue(s.hasCurrent());
		assertEquals(2,s.size());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		s.start();
		assertTrue(s.hasCurrent());
		assertEquals(e1,s.getCurrent());
	}
	
	public void test30() {
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e3,s.getCurrent());
		assertTrue(s.hasCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(e3,s.getCurrent());
		s.advance();
		s.start();
		assertSame(e3,s.getCurrent());
	}
	
	public void test31() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insert(e3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(e1,s.getCurrent());
	}

	public void test32() {
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.start();
		s.insert(e1);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		assertEquals(3,s.size());
	}
	
	public void test33() {
		s.insert(e3);
		s.insert(e2);
		s.insert(e1);
		// s.start();  // redundant
		assertEquals(e1,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
	}

	public void test34() {
		s.insert(e3);
		s.insert(e2);
		s.insert(e1);
		s.start(); // REDUNDANT
		s.advance();
		assertSame(e2,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e3,s.getCurrent());
	}
	
	public void test35() {
		s.insert(e3);
		s.insert(e2);
		s.insert(e1);
		// s.start(); // REDUNDANT
		s.advance();
		s.advance();
		assertSame(e3,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertFalse(s.hasCurrent());
		try {
			s.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex, ex instanceof IllegalStateException);
		}
		try {
			s.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex, ex instanceof IllegalStateException);
		}
		s.start();
		assertSame(e1,s.getCurrent());
	}
 
	public void test39() {
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		s.insert(e4);
		s.insert(e5);
		assertSame(e5,s.getCurrent());
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		s.insert(e4);
		s.insert(e5);
		s.insert(e1);
		s.insert(e2);
		assertEquals(12,s.size());
		s.removeCurrent();
		assertTrue(s.hasCurrent());
		s.start();
		s.removeCurrent();
		assertSame(e5,s.getCurrent());
		assertEquals(10,s.size());
		s.start();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test40() {
		s.insert(null);
		s.advance();
		s.insert(e2);
		s.advance();
		s.insert(null);
		
		assertEquals(3,s.size());
		assertTrue(s.hasCurrent());
		assertSame(null,s.getCurrent());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(null,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(null,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}

	public void test50() {
		s.insert(e1); // duration 1.0
		s.insert(e2); // duration 0.5
		s.insert(e4); // duration 0.25
		
		assertEquals(Tf(1650759331),(float)s.getDuration());
		
		s.insert(e4); // duration 0.25
		
		assertEquals(Tf(446229898),(float)s.getDuration());
	}
	
	public void test51() {
		s.insert(e1); // duration = 1.0
		s.insert(e2); // will be first, duration = 0.5
		
		s.stretch(0.5);
		s.start();
		assertEquals(Tf(956306836),(float)s.getCurrent().getDuration());
		s.advance();
		assertEquals(Tf(1190904697), (float)s.getCurrent().getDuration());
		
		assertException(() -> s.stretch(0), IllegalArgumentException.class);
		assertException(() -> s.stretch(100), IllegalArgumentException.class);
	}
	
	public void test52() {
		s.insert(e1);
		s.insert(e2); // will be first
		
		s.transpose(3); // will transform an "e" into a "g"
		s.start();
		assertEquals("g2",s.getCurrent().getPitch());
		s.advance();
		assertEquals("g1",s.getCurrent().getPitch());
		
		s.transpose(-15); // back to e0 etc.
		assertEquals("e0",s.getCurrent().getPitch());
		
		assertException(() -> s.transpose(-5), IllegalArgumentException.class);
	}
	
	public void test53() {
		s.insert(null);
		s.advance();
		s.insert(e1);
		s.advance();
		s.insert(null);
		s.advance();
		s.insert(e4);
		s.transpose(-4); // transform e to c
		s.stretch(2);
		s.start();
		assertNull(s.getCurrent());
		s.advance();
		assertEquals("c1",s.getCurrent().getPitch());
		s.advance();
		assertNull(s.getCurrent());
		assertEquals(2.5,s.getDuration());
	}
	
	public void test60() {
		Song se = newSong();
		s.insertAll(se);
		assertFalse(s.hasCurrent());
		assertEquals(0,s.size());
		s.insert(e1);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		assertSame(e1,s.getCurrent());
		s.advance();
		s.insertAll(se);
		assertFalse(s.hasCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
		s.start();
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
	}
	
	public void test61() {
		Song se = newSong();
		se.insert(e1);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertTrue(se.hasCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(e1,s.getCurrent());
		assertSame(e1,se.getCurrent());
	}
	
	public void test62() {
		Song se = newSong();
		se.insert(e1);
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
	}
	
	public void test63() {
		Song se = newSong();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.hasCurrent());
		assertSame(e1,se.getCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e1,s.getCurrent());
	}
	
	public void test64() {
		Song se = newSong();
		se.insert(e1);
		se.advance();
		s.insert(e3);
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.hasCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());	
	}
	
	public void test65() {
		Song se = newSong();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
	}
	
	public void test66() {
		Song se = newSong();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.advance();
		assertFalse(s.hasCurrent());
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(e1,se.getCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.advance();
		assertSame(e1,s.getCurrent());
	}

	public void test67() {
		Song se = newSong();
		se.insert(e2);
		se.insert(e1);	
		s.insert(e4);
		s.insert(e3);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
	}

	public void test68() {
		Song se = newSong();
		se.insert(e2);
		se.insert(e1);
		se.advance();
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(e2,se.getCurrent()); se.advance();
		assertFalse(se.hasCurrent());
		// check s
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
		s.start();
		assertSame(e3,s.getCurrent());
	}

	public void test69() {
		Song se = newSong();
		se.insert(e2);
		se.insert(e1);
		se.advance();
		se.advance();
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.advance();
		assertFalse(s.hasCurrent());
		assertFalse(se.hasCurrent());
		s.insertAll(se);
		assertTrue(s.hasCurrent());
		assertFalse(se.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		s.start();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
	}

	public void test70() {
		Song se = newSong();
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		// se has 24 elements
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(se);
		assertEquals(26,s.size());
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		s.insertAll(se);
		assertEquals(50,s.size());
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();		
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		// interruption
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		// done with all 24 copies most recently inserted
		// now back to the original
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); // etc.
	}
	
	public void test71() {
		s.insertAll(s);
		assertFalse(s.hasCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test72() {
		s.insert(e1);
		s.insertAll(s);
		assertEquals(2,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test73() {
		s.insert(e1);
		s.advance();
		s.insertAll(s);
		assertEquals(2,s.size());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test74() {
		s.insert(e1);
		s.removeCurrent();
		assertEquals(0,s.size());
		assertFalse(s.hasCurrent());
	}
	
	public void test75() {
		s.insert(e2);
		s.insert(e1);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}
	
	public void test76() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
		s.start();
		assertSame(e1,s.getCurrent());
	}

	public void test77() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.advance();
		assertFalse(s.hasCurrent());
		s.insertAll(s);
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}

	public void test78() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(s);
		s.removeCurrent();
		s.insert(e3);
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		s.advance();
		s.insertAll(s);
		assertEquals(8,s.size());
		assertTrue(s.hasCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}
	
	public void test79() {
		Song se = newSong();
		se.insert(e1);
		se.advance();
		se.insert(e2);	
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.insertAll(se); // s = e3 * e1 e2 e4
		s.advance();
		s.advance();
		s.insert(e5); // s = e3 e1 e2 * e5 e4
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(e4,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(e2,se.getCurrent());
		se.advance();
		assertFalse(se.hasCurrent());
		se.start();
		assertSame(e1,se.getCurrent());
	}
	
	public void test80() {
		Song c = s.clone();
		assertFalse(c.hasCurrent());
		assertEquals(0, c.size());
	}
	
	public void test81() {
		s.insert(e1);
		Song c = s.clone();
		
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,c.getCurrent()); c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
	}
	
	public void test82() {
		s.insert(e1);
		s.advance();
		Song c = s.clone();
		
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
	}

	public void test83() {
		Song c = s.clone();
		assertFalse(c.hasCurrent());
		
		s.insert(e1);
		c = s.clone();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		
		s.advance();
		s.insert(e2);
		c = s.clone();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		
		s.insert(e3);
		assertTrue(s.hasCurrent());
		assertFalse(c.hasCurrent());
		
		c = s.clone();
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		s.start();
		c.start();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		
		s.start();
		c = s.clone();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());		
	}
	
	public void test84() {
		s.insert(e1);
		s.advance();
		s.insert(e3);
		s.insert(e2);
		s.removeCurrent();
		
		Song c = s.clone();
		
		assertEquals(2,c.size());
		
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());
	}

	public void test85() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		
		Song c = s.clone();
		s.insert(e3);
		c.insert(e4);
		
		assertSame(e3,s.getCurrent());
		assertSame(e4,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		
		s.start();
		c.start();
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(e3,s.getCurrent());
		assertSame(e4,c.getCurrent());
	}
	
	public void test90() {
		Song s1 = Song.catenation(s, s);
		assertEquals(0,s1.size());
		
		s.insert(e1);
		s1 = Song.catenation(s1,s);
		
		assertSame(e1,s.getCurrent());
		assertEquals(1,s1.size());
		assertFalse(s1.hasCurrent());
		s1.start();
		assertSame(e1,s1.getCurrent());
	}
	
	public void test91() {
		s.insert(e2);
		Song s1 = newSong();
		s1.insert(e4);
		
		Song s2 = Song.catenation(s,s1);
		assertFalse(s2.hasCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e4,s1.getCurrent());
		
		assertEquals(2,s2.size());
		s2.start();
		assertSame(e2,s2.getCurrent());
		s2.advance();
		assertSame(e4,s2.getCurrent());
	}
	
	public void test92() {
		Song s1 = new Song("Test 1",75);
		Song s2 = new Song("Test 2",125);
		
		s1.insert(e1); // 1.00
		s2.insert(e4); // 0.25
		s2.insert(e2); // 0.50
		
		s = Song.catenation(s1, s2);
		assertEquals(Ts(1535812583),s.getName());
		assertEquals(Ti(445953280),s.getBPM());
		assertEquals(Ti(97893845),s.size());
		assertEquals(Tf(1229875862),(float)s.getDuration());

		s.start();
		assertSame(e1,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e4,s.getCurrent());
	}	
	
	public void test93() {
		s.insert(e3);
		s.insert(null);
		s.insert(e1);
		Song s1 = newSong();
		s1.insert(e2);
		
		Song s2 = Song.catenation(s,s1);
		assertEquals(4,s2.size());
		assertFalse(s2.hasCurrent());
		s2.start();
		assertSame(e1,s2.getCurrent());
		s2.advance();
		assertSame(null,s2.getCurrent());
		s2.advance();
		assertSame(e3,s2.getCurrent());
		s2.advance();
		assertSame(e2,s2.getCurrent());
		s2.advance();
		assertFalse(s2.hasCurrent());
	}
	
	public void test95() {
		s = new Song("",20);
		s = new Song("",1000);
		s = new Song("hi",20,0);
		s = new Song("bye",1000,10);
		
		assertException(IllegalArgumentException.class,() -> new Song(null,60));
		assertException(IllegalArgumentException.class,() -> new Song("test",19));
		assertException(IllegalArgumentException.class,() -> new Song("test",1001));
		assertException(IllegalArgumentException.class,() -> new Song("test",60,-1));
	}
	
	public void test96() {
		s.setBPM(20);
		assertEquals(20,s.getBPM());
		s.setName("");
		assertEquals("",s.getName());
		
		assertException(() -> s.setBPM(15),IllegalArgumentException.class);
		assertException(() -> s.setBPM(1005),IllegalArgumentException.class);
		assertException(() -> s.setName(null),IllegalArgumentException.class);
	}
}
