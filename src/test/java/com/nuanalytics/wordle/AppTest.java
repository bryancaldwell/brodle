package com.nuanalytics.wordle;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;



/**
 * Unit test for simple App.
 */
public class AppTest  {

    
	App target;
	
	@Before
	public void setUp() {
		target = new App();
	}
	
	@Test
	public void newApp() {
		assertNotNull(target);
	}
	
	@Test
	public void testProcessGuess() {
		assertTrue(false);
	}

}
