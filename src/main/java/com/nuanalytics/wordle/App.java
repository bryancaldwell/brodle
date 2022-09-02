package com.nuanalytics.wordle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * App plays the game by itself
 *
 */
public class App {

	private static final Predicate<String> IS_FIVE_LETTERS = str -> str.length() == 5;
	private static final Predicate<String> IS_WORD = str -> str.matches("[a-zA-Z]+$");
	private static final Function<String, String> FIRST_COLUMN = str -> str.split(" ")[0];
	private static final String FILE_NAME = "frequency2.txt";
	
	private static Game game = new Game();
	
	private List<String> possibilites;
	private List<String> board;
	private List<Character> hits = new ArrayList<>();

	

	public App() {
		possibilites = loadFrequencies(FILE_NAME);
		log("Possibilities Dictionary, size = " + possibilites.size());
	}



	private List<String> loadFrequencies(String fileName) {
		List<String> sortedList = new ArrayList<>();

		try {
			sortedList = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI()))
					.stream().map(FIRST_COLUMN).filter(IS_WORD)
					.filter(IS_FIVE_LETTERS)
					.collect(Collectors.toList());
		} catch (IOException | URISyntaxException e) {
			log(e);
		}
		return sortedList;
	}


	private String nextGuess() {
		return possibilites.get(0);
	}

	// Eliminate words that are not possible
	private void filterDictionary(String guess, char[] results) {

		possibilites.remove(guess);
		Stream.iterate(0, n -> n + 1).limit(5).forEach(i -> {
			if (results[i] == 'g') {
				hits.add(guess.charAt(i));
				possibilites = possibilites.stream().filter(str -> str.charAt(i) == guess.charAt(i))
						.collect(Collectors.toList());
			} else if (results[i] == 'y') {
				hits.add(guess.charAt(i));
				possibilites = possibilites.stream().filter(
						str -> str.contains(String.valueOf(guess.charAt(i))) && str.charAt(i) != guess.charAt(i))
						.collect(Collectors.toList());

			} else if (results[i] == 'b') {
				
				if (!hits.contains(guess.charAt(i))) {
					//log("Remove words with " + guess.charAt(i));
					possibilites = possibilites.stream()
							.filter(str -> !str.contains(String.valueOf(guess.charAt(i)))).collect(Collectors.toList());
				}

			}

		});

		log("There are now " + possibilites.size()+" possible words ");
		log(" ------------------------------------------------------");

	}

	

	private boolean processGuess(String guess) {
		board = game.getResultsOfGuess(guess);
		filterDictionary(guess, board.get(board.size() - 1).toCharArray());
		return board.get(board.size() - 1).contentEquals("ggggg");
	}

	
	private static void log(String msg) {
		System.out.println(msg);
	}
	
	private static void log(Exception e) {
		e.printStackTrace();
	}

	public static void main(String[] args) {

		App app = new App();
		game.pickRandomWord();
		// read input eventually
		String guess = "alien";
		log("My guess number 1 is " + guess);
		int i = 2;
		while (!app.processGuess(guess)) {
			guess = app.nextGuess();
			log("My guess number " + i + " is " + guess);
			i++;
		}

	}

}
