package com.nuanalytics.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Emulate a game of Wordle 
 * 
 *    String g = "\ud83d\ude00";
 *    String g = "\ud83d\udfe9";
 *
 *	  b = black
 *    g = green
 *    y = yellow
 *
 */
public class Game {

	private static final String FILE_NAME = "frequency2.txt";
	private static final Predicate<String> IS_FIVE_LETTERS = str -> str.length() == 5;
	private static final Predicate<String> IS_WORD = str -> str.matches("[a-zA-Z]+$");
	private static final Function<String, String> FIRST_COLUMN = str -> str.split(" ")[0];
	//dictionary size determines how difficult the game will be
	private static final int DICTIONARY_SIZE = 5000;

	private List<String> wordleDictionary;
	private String answer;
	private List<String> board = new ArrayList<>();

	public Game() {
		wordleDictionary = createDictionary(FILE_NAME);
	}

	public void setAnswer(String answer) {
		// for testing
		this.answer = answer;
		log("Picked Solution word = " + answer);
	}

	private List<String> createDictionary(String fileName) {
		List<String> sortedList = new ArrayList<>();

		try {
			sortedList = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI()))
					.stream().map(FIRST_COLUMN).filter(IS_WORD).filter(IS_FIVE_LETTERS).limit(DICTIONARY_SIZE)
					.collect(Collectors.toList());
		} catch (IOException | URISyntaxException e) {
			log(e);
		}
		log("Game dictionary size " + sortedList.size());
		return sortedList;
	}

	public void pickRandomWord() {
		List<String> dictCopy = wordleDictionary.stream()
				  .collect(Collectors.toList());
		Collections.shuffle(wordleDictionary);
		answer = wordleDictionary.subList(0, 1).get(0);
		int index = dictCopy.indexOf(answer);
		log("Picked Solution word = " + answer+" "+index);
	}

	public List<String> getResultsOfGuess(String guess) {
		char[] row = "bbbbb".toCharArray();

		int i = 0;
		for (char letter : guess.toCharArray()) {
			if (letter == answer.charAt(i)) {
				row[i] = 'g';
			} else if (answer.contains(String.valueOf(letter))) {
				row[i] = 'y';
			}
			i++;
		}

		String rowStr = new String(row);
		if (rowStr.equals("ggggg")) {
			log("WIN!");

		}
		board.add(rowStr);
		log(board.toString());

		return board;
	}

	public String getGuessInput() {
		List<String> tokens = new ArrayList<>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			StringTokenizer st = new StringTokenizer(br.readLine());

			while (st != null && st.hasMoreElements()) {
				tokens.add(st.nextToken());
			}

			log(tokens.toString());
		} catch (IOException e) {
			log(e.toString());
		}
		String guess = tokens.get(0);
		if (guess.length() != 5) {
			log("Not Five Letters, try again");
			return getGuessInput();
		}
		if (!wordleDictionary.contains(guess)) {
			log("Not in Dictionary, try again");
			return getGuessInput();
		}
		return guess;
	}

	private static void log(String msg) {
		System.out.println(msg);
	}

	private static void log(Exception e) {
		e.printStackTrace();
	}

}
