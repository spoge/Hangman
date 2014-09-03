package hioa.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Sondre on 23.08.2014.
 * Database containing multiple words
 */
public class WordDatabase {
    private static ArrayList<Word> words = new ArrayList<Word>();

    // running at onCreate
    static{
        words.add(new Word("MOTHERFUCKER"));
        words.add(new Word("DOVENDYR"));
        words.add(new Word("RUMPEBALLE"));
        words.add(new Word("FJELLSAU"));
        words.add(new Word("LLAMA"));
        words.add(new Word("RANDOM"));
        words.add(new Word("RUSS"));
        words.add(new Word("LARS"));
        words.add(new Word("ERIK"));
        words.add(new Word("KJEMPE"));
        words.add(new Word("SMART"));
        words.add(new Word("IKKE"));
        words.add(new Word("RETARD"));
        words.add(new Word("HILSEN"));
    }

    // returns a random word
    public static String getRandomWord() {
        Random r = new Random();
        int i = r.nextInt(words.size());
        return words.get(i).word;
    }

    // returns a random word, except the input word
    public static String getRandomWord(String except) {
        int p = getIndex(except);
        int l = words.size() - 1;
        Collections.swap(words, p, l);

        Random r = new Random();
        int i = r.nextInt(words.size() - 1);
        String word = words.get(i).word;

        Collections.swap(words, p, l);
        return word;
    }

    // index of input in the words-list, return -1 if it is not i list
    private static int getIndex(String in) {
        for(int i = 0; i < words.size(); i++)
            if(words.get(i).word.equals(in)) return i;
        return -1; // not found
    }
}
