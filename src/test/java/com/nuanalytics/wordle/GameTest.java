package com.nuanalytics.wordle;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class GameTest extends TestCase {
	Game target;
	
	@Before
	public void setUp() {
		target = new Game();
	}
	
	
	
	@Test
	public void newGame() {
		assertNotNull(target);
	}

	@Test
	public void testProcessGuess() {
		target.setAnswer("alien");
		List<String> board = target.getResultsOfGuess("alien");
		assertEquals("ggggg",board.get(0));
	}
	
	@Test
	public void testProcessGuessAllMiss() {
		target.setAnswer("whump");
		List<String> board = target.getResultsOfGuess("alien");
		assertEquals("bbbbb",board.get(0));
	}
	
	@Test
	public void testProcessGuessOneYellow() {
		target.setAnswer("could");
		List<String> board = target.getResultsOfGuess("alien");
		assertEquals("bybbb",board.get(0));
	}
	
	@Test
	public void testProcessGuessDoubleYellow() {
		target.setAnswer("apple");
		List<String> board = target.getResultsOfGuess("alien");
		assertEquals("gybyb",board.get(0));
	}
	
	@Test
	public void testProcessGuessComplexYellow() {
		target.setAnswer("apple");
		List<String> board = target.getResultsOfGuess("papal");
		assertEquals("yygyy",board.get(0));
	}
	
	@Test
	public void testProcessGuessComplexYellow2() {
		target.setAnswer("areng");
		List<String> board = target.getResultsOfGuess("papal");
		assertEquals("bybyb",board.get(0));
	}
	
	@Test
	public void testProcessGuessComplexYellow3() {
		target.setAnswer("marya");
		List<String> board = target.getResultsOfGuess("papal");
		assertEquals("bgbyb",board.get(0));
		board = target.getResultsOfGuess("mirna");
		assertEquals("gbgbg",board.get(1));
		board = target.getResultsOfGuess("marya");
		assertEquals("ggggg",board.get(2));

	}
	
	@Test
	public void testProcessGuessComplexYellow4() {
		target.setAnswer("uplay");
		List<String> board = target.getResultsOfGuess("papal");
		assertEquals("yyygy",board.get(0));
	}
	
	
	

}
