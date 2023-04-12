package com.wow.hangmanassist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HangmanGame {
    private static final String RANDOM_WORDS = "wordList.txt";
    private static final int MAX_GUESSES = 6;
    private List<String> words;
    private Random rand = new Random();
    private String secretWord;
    private boolean[] guessedLetters;
    private int numGuesses;
    public Trie trie;
    public Map<Character, Integer> topSuggestions;

    public HangmanGame() {
        words = readWordsFromFile(RANDOM_WORDS);
        trie = new Trie();
        for (String s : words) {
            trie.insert(s);
        }
        pickRandomWord();
    }
    
    public void pickRandomWord() {
        secretWord = words.get(rand.nextInt(words.size()));
        guessedLetters = new boolean[secretWord.length()];
        numGuesses = 0;
    }

    public boolean playFromGUI(String guess) {
        if (numGuesses < MAX_GUESSES && !isWordGuessed()) {
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
            return !isWordGuessed();
        }
        else {
            return false;
        }
    }

    public String getGameStatus() {
        String guessedWord = getSecretWordWithGuesses();
        String suggestionPattern = guessedWord.replace(" ", "");
        topSuggestions = trie.suggest(suggestionPattern);

        return "Secret word: " +
                guessedWord +
                "\nGuesses left: " +
                (MAX_GUESSES - numGuesses) +
                "\n";
    }

    public String getGameOutcome() {
        if (isWordGuessed()) {
            return "Congratulations! You guessed the word " + secretWord + "\n";
        } else {
            return "Sorry, you ran out of guesses. The word was " + secretWord + "\n";
        }
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
    public String displaySuggestions() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : topSuggestions.entrySet()) {
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(' ');
            sb.append(entry.getValue());
            sb.append('\n');
        }
        return sb.toString();
    }
    private void displayGameStatus() {
        String guessedWord = getSecretWordWithGuesses();
        String suggestionPattern = guessedWord.replace(" ", "");
        topSuggestions = trie.suggest(suggestionPattern);
        System.out.println("Secret word: " + guessedWord);
        System.out.println("Guesses left: " + (MAX_GUESSES - numGuesses));
        System.out.println("Top letters: " + displaySuggestions() + "\n");
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
        return sb.toString();
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
    public static void main(String []s) {
        new HangmanGame();
    }
}

