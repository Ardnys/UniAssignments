package com.wow.hangmanassist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HangmanGame {
    private static final String RANDOM_WORDS = "randomWords.txt";
    private static final int MAX_GUESSES = 6;

    private String secretWord;
    private boolean[] guessedLetters;
    private int numGuesses;

    public HangmanGame() {
        List<String> words = readWordsFromFile(RANDOM_WORDS);
        Random rand = new Random();
        secretWord = words.get(rand.nextInt(words.size()));
        guessedLetters = new boolean[secretWord.length()];
        numGuesses = 0;
    }

    public void play() {
        System.out.println("Welcome to Hangman!");
        while (numGuesses < MAX_GUESSES && !isWordGuessed()) {
            displayGameStatus();
            String guess = getGuessFromUser();
            if (guess.length() == 1) {
                boolean foundLetter = false;
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess.charAt(0)) {
                        foundLetter = true;
                        guessedLetters[i] = true;
                    }

                }
                if (!foundLetter) {
                    numGuesses++;
                }
            } else {
                if (guess.equals(secretWord)) {
                    guessedLetters = new boolean[secretWord.length()];
                    for (int i = 0; i < secretWord.length(); i++) {
                        guessedLetters[i] = true;
                    }
                } else {
                    numGuesses++;
                }
            }
        }
        displayGameOutcome();
    }

    public static void main(String []s) {
        new HangmanGame().play();
    }
    private void displayGameStatus() {
        System.out.println("Secret word: " + getSecretWordWithGuesses());
        System.out.println("Guesses left: " + (MAX_GUESSES - numGuesses));
    }

    private String getGuessFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your guess: ");
        String guess = scanner.nextLine();
        return guess.toLowerCase(Locale.ENGLISH);
    }

    private boolean isWordGuessed() {
        for (boolean guessedLetter : guessedLetters) {
            if (!guessedLetter) {
                return false;
            }
        }
        return true;
    }

    private String getSecretWordWithGuesses() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            if (guessedLetters[i]) {
                sb.append(secretWord.charAt(i));
            } else {
                sb.append("_");
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private void displayGameOutcome() {
        if (isWordGuessed()) {
            System.out.println("Congratulations! You guessed the word.");
        } else {
            System.out.println("Sorry, you ran out of guesses. The word was " + secretWord);
        }
    }

    private List<String> readWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        URI file = null;
        try {
            file = Objects.requireNonNull(HangmanGame.class.getResource(filename)).toURI();
        } catch (URISyntaxException e) {
            System.err.println("Error while URI " + e.getMessage());
        }
        assert file != null;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return words;
    }
}

