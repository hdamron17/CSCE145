/**
 * Hunter Damron
 * Copyright 2019
 *
 * NOTE: Submitting any part of this assignment for credit is plagiarism.
 *   Referencing this material to any extent without citation is plagiarism.
 */

package hw7;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
  private static final String FILENAME = "data/hw7/dict.txt";
  private static final int N = 5;

  public static void main(String[] argv) {
    /* Open dictionary file */
    Scanner fscan;
    try {
      fscan = new Scanner(new File(FILENAME));
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file");
      return;
    }

    /* Loads dictionary into an array */
    LinkedList<String> dictlist = new LinkedList<String>();
    while (fscan.hasNextLine()) {
      dictlist.add(fscan.nextLine().toUpperCase());
    }
    fscan.close();

    String[] dict = dictlist.toArray(new String[0]);
    Arrays.sort(dict);

    /* Load game board from stdin */
    char[][] board = new char[N][N];
    Scanner scan = new Scanner(System.in);
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        board[i][j] = scan.next().toUpperCase().charAt(0);
      }
    }
    scan.close();

    /* Begin searching the board */
    searchBoard(board, dict);
  }

  /*
   * Starts searching board from each position, prints all words found from each
   */
  private static void searchBoard(char[][] board, String[] dict) {
    boolean[][] used = new boolean[N][N];

    /* Start from each board position */
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        System.out.println("Starting " + i + " " + j);

        char c = board[i][j];

        /* Find subdictionary beginning with start character c */
        int dictstart = restrictstart(dict, 0, dict.length, 0, c);
        int dictend = restrictend(dict, dictstart, dict.length, 0, c);

        /* Ignore dictionary words of length 1 */
        if (dictstart < dictend && dict[dictstart].length() == 1) {
          dictstart++;
        }

        /* Enter recursive searching starting from index i,j */
        used[i][j] = true;
        recSearchBoard("" + c, 1, board, used, i, j, dict, dictstart, dictend);
        used[i][j] = false;  // Pop off this used start
      }
    }
  }

  /*
   * Recursively searches for words in dict which are found in the board
   */
  private static void recSearchBoard(String prev, int prevn, char[][] board,
                                     boolean[][] searched, int i, int j,
                                     String[] dict, int dictstart, int dictend) {
    /* Add each possible next character from the board */
    for (int ni = i-1; ni <= i+1; ++ni) {
      for (int nj = j-1; nj <= j+1; ++nj) {
        /* Check that ni, nj are valid indices and are not already searched */
        if (ni >= 0 && ni < N && nj >= 0 && nj < N && !searched[ni][nj]) {

          /* Find new starts after adding a character */
          int ndictstart = restrictstart(dict, dictstart, dictend, prevn, board[ni][nj]);
          int ndictend = restrictend(dict, ndictstart, dictend, prevn, board[ni][nj]);

          /* If the subdictionary is empty, just quit */
          if (ndictstart >= ndictend) {
            continue;
          }

          /* If this word is in dictionary, print */
          if (dict[ndictstart].equals(prev + board[ni][nj])) {
            // Because dict is sorted, the first will match if any
            // Assumes no repeats in the dictionary and that prevn > 0
            if (dict[ndictstart].length() <= 6)  // Optional: restricts words to length 6
              System.out.println("Found Word: " + dict[ndictstart]);
            ndictstart++;
          }

          /* Search recursively for extending strings */
          searched[ni][nj] = true;
          recSearchBoard(prev + board[ni][nj], prevn + 1, board, searched, ni, nj,
                         dict, ndictstart, ndictend);
          searched[ni][nj] = false;  // Pop off this recursive step
        }
      }
    }
  }

  /*
   * Finds index of first item in dict which has character c at index i
   */
  private static int restrictstart(String[] dict, int curstart, int curend, int i, char c) {
    int newstart = curstart;
    while (newstart < curend && dict[newstart].charAt(i) < c) {
      newstart++;
    }
    return newstart;
  }

  /*
   * Finds index of last item (plus one) in dict which has character c at index i
   */
  private static int restrictend(String[] dict, int curstart, int curend, int i, char c) {
    int newend = curend;
    while (newend > curstart && dict[newend-1].charAt(i) > c) {
      newend--;
    }
    return newend;
  }
}
