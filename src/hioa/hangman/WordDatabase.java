package hioa.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by Sondre on 23.08.2014.
 * Class dedicated to grabbing words from related language xml, then managing them.
 */
public class WordDatabase {
    private ArrayList<String> words = new ArrayList<String>();

    //generates a new database with the string-array retrieved from values.arrays.words depending on language-settings
    public WordDatabase(String[] words){
    	for(String word : words)
    		this.words.add(word);
    }

    // returns a random word
    public String getRandomWord() throws NoSuchElementException{
    	if(words.size() == 0){
    		throw new NoSuchElementException("The wordlist is empty");
    	}
        Random r = new Random();
        int i = r.nextInt(words.size());
        String word = words.get(i);
        words.remove(i);
        return word;
    }

    // returns a random word, except the input word
    public String getRandomWord(String except) {
    	if(except.equals("")) return getRandomWord();
    	
        int p = getIndex(except);
        int l = words.size() - 1;
        Collections.swap(words, p, l);

        Random r = new Random();
        int i = r.nextInt(words.size() - 1);
        String word = words.get(i);

        Collections.swap(words, p, l);
        
        return word;
    }

    // index of input in the words-list, return -1 if it is not i list
    private int getIndex(String in) {
        for(int i = 0; i < words.size(); i++)
            if(words.get(i).equals(in)) return i;
        return -1; // not found
    }
}
