package com.wow.hangmanassist;

import java.util.Map;
import java.util.TreeMap;

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

    public void suggest(String pattern) {
        // an s example "  l "
        // empty ones are anything
        TrieNode crawl = root;
        char c = pattern.charAt(0);
        if (c == ' ') {
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
    }

    public int traverse(TrieNode node, String pattern, int level, boolean isEnd, char letter) {
        if (pattern.length() <= level) {
            if (pattern.length() == level && isEnd) {
                addToMap(letter);
                return 1;
            }
            return 0;
        }
        int uh = 0;
        if (pattern.charAt(level) == ' ') {
            // check all
            for (int i = 0; i < node.bois.length; i++) {
                if (node.bois[i] != null) {
                    System.out.println("char: " + (char)(i+'a') + " in level: " + (level + 1));
                    uh += traverse(node.bois[i], pattern, level+1, node.bois[i].wordEnd,(char) (i + 'a'));
                }
            }
            if (uh != 0) {
                // then there is a success so add it to map
                addToMap(letter);
            }
        } else {
            int idx = pattern.charAt(level) - 'a';
            if (node.bois[idx] == null) {
                return 0;
            }
            System.out.println("char: " + (char)(idx+'a') + " in level: " + (level + 1));
            traverse(node.bois[idx], pattern, level+1, node.bois[idx].wordEnd, (char) (idx + 'a'));
        }
        return uh;
    }

    private void addToMap(char letter) {
        if (!map.containsKey(letter)) {
            map.put(letter, 1);
        } else {
            map.replace(letter, map.get(letter)+1);
        }
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        String word = "and,art,ant,anton,boy,bot,boot,bolt,bald,cut,cute,clean,clear,coat,cold";
        for (String s : word.split(",")) {
            t.insert(s);
        }
        String pattern = "  l ";
        t.suggest(pattern);
        System.out.println(t.map);
    }
}
