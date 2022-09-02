package com.nuanalytics.wordle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Give hints to play wordle
 * @author bryan
 *
 */
public class Guess {

	private static final String FILE_NAME = "frequency2.txt";
	private static final Predicate<String> IS_FIVE_LETTERS = str -> str.length() == 5;
	private static final Predicate<String> IS_WORD = str -> str.matches("[a-zA-Z]+$");
	private static final Function<String, String> FIRST_COLUMN = str -> str.split(" ")[0];

	private List<String> possibilites;
	private List<Character> hits = new ArrayList<>();

	public Guess() {
		possibilites = loadDictionary(FILE_NAME);
		log("Possibilities Dictionary, size = " + possibilites.size());
	}

	public List<String> loadDictionary(String fileName) {
		List<String> sortedList = new ArrayList<>();

		try {
			sortedList = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI()))
					.stream().map(FIRST_COLUMN).filter(IS_WORD).filter(IS_FIVE_LETTERS).collect(Collectors.toList());
		} catch (IOException | URISyntaxException e) {
			log(e);
		}
		return sortedList;
	}

	// Eliminate words that are not possible
	private void filterPossibilites(String guess, char[] results) {

		possibilites.remove(guess);
		
		Stream.iterate(0, n -> n + 1).limit(5).forEach(i -> {

			Predicate<String> exactMatchCharacter = str -> str.charAt(i) == guess.charAt(i);
			Predicate<String> characterIsInWrongPlace = str -> str.contains(String.valueOf(guess.charAt(i)))
					&& str.charAt(i) != guess.charAt(i);
			Predicate<String> characterIsNotInWord = str -> !str.contains(String.valueOf(guess.charAt(i)));

			if (results[i] == 'g') {
				hits.add(guess.charAt(i));
				possibilites = possibilites.stream().filter(exactMatchCharacter).collect(Collectors.toList());
			} else if (results[i] == 'y') {
				hits.add(guess.charAt(i));
				possibilites = possibilites.stream().filter(characterIsInWrongPlace).collect(Collectors.toList());
			} else if (results[i] == 'b' && !hits.contains(guess.charAt(i))) {
				possibilites = possibilites.stream().filter(characterIsNotInWord).collect(Collectors.toList());
			}

		});
		

		log("There are now " + possibilites.size() + " possible words ");
		log(" ------------------------------------------------------");

	}

	public List<String> possible(String guess, char[] row) {
		filterPossibilites(guess, row);
		return possibilites;
	}

	private static void log(String msg) {
		System.out.println(msg);
	}

	private static void log(Exception e) {
		e.printStackTrace();
	}

	/*
	 * manually play the game
	 */
	public static void main(String args[]) {

		Guess app = new Guess();
		Scanner userInput = new Scanner(System.in);
		String guess = "";
		while (true) {
			log("Enter guess:  ");
			guess = userInput.nextLine();

			log("Enter result in format like \"bygby\":  ");
			String resultStr = userInput.nextLine().toLowerCase();
			char[] result12 = resultStr.toLowerCase().toCharArray();
			List<String> possible = app.possible(guess, result12);
			log("Possible words " + possible);
			if (resultStr.equals("ggggg")) {
				break;
			}
		}
		userInput.close();
		log(guess + " is correct, Winner!");

	}

}
