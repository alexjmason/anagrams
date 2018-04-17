/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, List<String>> lettersToWord = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sorted = sortLetters(word);
            wordList.add(word);
            wordSet.add(word);

            if(lettersToWord.get(sorted) == null) {
                List<String> newAnagram = new ArrayList<>();
                newAnagram.add(word);
                lettersToWord.put(sorted, newAnagram);
            } else {
                List<String> anagramList = lettersToWord.get(sorted);
                anagramList.add(word);
                lettersToWord.put(sorted, anagramList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        boolean result = false;
        if(wordSet.contains(word) && !word.contains(base))
            result = true;
        return result;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        int length = targetWord.length();
        String sorted = sortLetters(targetWord);

        for(String word : wordList){
            if(word.length() == length && sorted.equals(sortLetters(word)))
                result.add(word);
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<>();
        List<String> extendedAnagrams;
        String extendedWord;
        char extend = 'a';

        for(int i = 0; i < 26; ++i) {
            extendedWord = word + extend;
            extendedAnagrams = lettersToWord.get(sortLetters(extendedWord));
            if(extendedAnagrams != null) {
                if (result == null)
                    result = extendedAnagrams;
                else {
                    for (String toAdd : extendedAnagrams)
                        result.add(toAdd);
                }
            }
            extend += 1;
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int i = random.nextInt(wordList.size());
        String chosen = wordList.get(i);
        while(lettersToWord.get(sortLetters(chosen)).size() < MIN_NUM_ANAGRAMS) {
            if(i == wordList.size())
                i = 0;
            chosen = wordList.get(i++);
        }
        return chosen;
    }

    private String sortLetters(String targetWord) {
        char tempArray[] = targetWord.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }
}
