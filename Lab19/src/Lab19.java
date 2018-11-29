/**
 * Hunter Damron
 * Copyright 2018
 *
 * NOTE: Submitting any part of this assignment for credit is plagiarism.
 *   Referencing this material to any extent without citation is plagiarism.
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Lab19 {
  private static final String FILENAME = "data/blah.txt";

  public static void main(String[] argv) {
    String vowels = "AEIOU";  // List of lowercase vowels
    int count = 0;  // Count number of times the vowels have been looped through
    int i = 0;  // Which vowel it's looking at
    Scanner scan;
    try {
      scan = new Scanner(new File(FILENAME));
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file");
      return;
    }
    scan.useDelimiter("");  // Take each character individually
    while (scan.hasNext()) {
      char current = scan.next().toUpperCase().charAt(0);  // Get next character 
      if (current == vowels.charAt(i)) {
        i++;  // Look for next charcter
        if (i >= vowels.length()) {
          i = 0;
          count++;
        }
      } else if (vowels.indexOf(current) != -1) {
        // If current is a vowel but not in order, reset to looking for 'a'
        i = 0;
      }
    }
    System.out.println("The file " + FILENAME + " has \"" + vowels
        + "\" in order " + count + " times");
  }
}
