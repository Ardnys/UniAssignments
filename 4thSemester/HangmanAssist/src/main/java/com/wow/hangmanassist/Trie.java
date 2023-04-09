package com.wow.hangmanassist;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class Trie {

    TrieNode root;
    Map<Character, Integer> map;

    public class TrieNode {
        public TrieNode[] bois = new TrieNode[26]; // for all letters in the alphabet
        public boolean wordEnd = false;

        public TrieNode() {
            for (TrieNode n : bois) {
                n = null;
            }
        }
    }

    public Trie() {
        root = new TrieNode();
        map = new TreeMap<>();
    }

    public void insert(String s) {
        char[] charArray = s.toCharArray();
        TrieNode current = root;
        for (char c : charArray) {
            int idx = c - 'a';
            if (current.bois[idx] == null) {
                current.bois[idx] = new TrieNode();
            }
            current = current.bois[idx];
        }
        current.wordEnd = true;
    }

    // search is probably not needed
    public boolean search(String s) {
        char[] charArray = s.toCharArray();
        TrieNode current = root;
        for (char c : charArray) {
            int idx = c - 'a';
            if (current.bois[idx] == null) {
                return false;
            }
            current = current.bois[idx];
        }
        return current.wordEnd;
    }

    public Map<Character, Integer> suggest(String pattern) {
        // an s example "  l "
        // empty ones are anything
        TrieNode crawl = root;
        char c = pattern.charAt(0);
        if (c == '_') {
            // if it starts empty, everything is checked
            for (int i = 0; i < crawl.bois.length; i++) {
                if (crawl.bois[i] != null) {
                    // map.put((char) (i + 'a'), 1);
                    traverse(crawl.bois[i], pattern, 1, crawl.bois[i].wordEnd, (char) (i + 'a'));
                }
            }
        } else {
            // else just check the one starts with it
            int idx = c - 'a';
            traverse(crawl.bois[idx], pattern, 1, crawl.bois[idx].wordEnd, (char) (idx + 'a'));
        }
//        System.out.println(map);
        return magicFromStackOverflow();
    }

    private Map<Character, Integer> magicFromStackOverflow() {
        Map<Character, Integer> top = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue (Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        map.clear();
        return top;
    }

    public int traverse(TrieNode node, String pattern, int level, boolean isEnd, char letter) {
        if (pattern.length() <= level) {
            if (pattern.length() == level && isEnd) {
                if (letter != pattern.charAt(level-1)) {
                    addToMap(letter, 1);
//                    System.out.println("added the last " + letter);
                }
                return 1;
            }
            return 0;
        }
        int uh = 0;
        if (pattern.charAt(level) == '_') {
            // check all
            for (int i = 0; i < node.bois.length; i++) {
                if (node.bois[i] != null) {
//                    System.out.println("1 char: " + (char)(i+'a') + " in level: " + (level + 1));
                    uh += traverse(node.bois[i], pattern, level+1, node.bois[i].wordEnd,(char) (i + 'a'));
                }
            }
            if (uh != 0 && letter != pattern.charAt(level-1)) {
                // then there is a success so add it to map
                addToMap(letter, uh);
//                System.out.println("success " + letter + " and " + uh);
            }
        } else {
            int idx = pattern.charAt(level) - 'a';
            if (node.bois[idx] == null) {
                return 0;
            }
//            System.out.println("2 char: " + (char)(idx+'a') + " in level: " + (level + 1));
//            addToMap((char) (idx + 'a'));
            uh += traverse(node.bois[idx], pattern, level+1, node.bois[idx].wordEnd, (char) (idx + 'a'));
            if (uh != 0 && letter != pattern.charAt(level-1)) {
                // then there is a success so add it to map
                addToMap(letter, uh);
//                System.out.println("success " + letter + " and " + uh);
            }
        }
        return uh;
    }

    private void addToMap(char letter, int occurrence) {
        if (!map.containsKey(letter)) {
            map.put(letter, occurrence);
        } else {
            map.replace(letter, map.get(letter)+occurrence);
        }
    }

//    public static void main(String[] args) {
//        Trie t = new Trie();
//        String word = "and,art,ant,anton,boy,bot,boot,bolt,bald,cut,cute,clean,clear,coat,cold";
//        for (String s : word.split(",")) {
//            t.insert(s);
//        }
//        String pattern = "c    ";
//        t.suggest(pattern);
//        System.out.println(t.map);
//    }
}
