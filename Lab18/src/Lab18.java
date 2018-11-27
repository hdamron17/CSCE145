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

public class Lab18 {
  private static final String FILENAME = "data/grades.txt";

  private static final String[] HDRS = {
    "LABS", "LAB REPORTS", "HOMEWORK", "EXAM 1", "EXAM 2",
    "LAB EXAM 1", "LAB EXAM 2", "FINAL", "EXTRA CREDIT"
  };
  private static final int NUM_HDRS = HDRS.length;

  private static final String[] LABELS = {
    "lab average", "lab report average", "homework average",
    "first exam", "second exam", "first lab exam",
    "second lab exam", "final", "extra work total"
  };

  private static final double[] WEIGHTS = {
    0.20, 0.10, 0.20, 0.10, 0.10, 0.10, 0.10, 0.10, 1
  };

  private static final String[] LETTERS = {
    "A", "B+", "B", "C+", "C", "D+", "D", "F"
  };

  private static final double[] LETTER_RANGES = {
    90, 85, 80, 74, 70, 65, 60  // Divides between letter grades
  };

  public static void main(String[] argv) {
    String fname = FILENAME;  // Default file name if not provided as argument

    if (argv.length >= 1) {
      fname = argv[0];  // Use file provided as command line argument
    }
    Scanner scan;  // File reader initialized inside try catch
    try {
      scan = new Scanner(new File(fname));
    } catch(FileNotFoundException e) {
      System.err.println("File not found");
      return;  // Cannot continue if the file is invalid
    }

    double[] averages = new double[NUM_HDRS];
    double run_sum = 0;
    double run_count = 0;
    int section = 0;
    scan.nextLine();  // Skip over first header
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      if (section + 1 < NUM_HDRS && line.equals(HDRS[section + 1])) {
        // This is the header of the next section so transition to calculate
        //   current average and start over
        if (run_count == 0) {
          // Assume the average is 0 if there are none of an assignment type
          averages[section] = 0;
        } else {
          averages[section] = run_sum / run_count;
        }
        run_sum = 0;
        run_count = 0;
        section++;
      } else {
        // If not a section header, add it to the running sum as a double
        run_sum += Double.parseDouble(line);
        run_count++;
      }
    }
    averages[section] = run_sum / 100;  // running_sum is extra credit now

    double finalGrade = 0;
    for (int i = 0; i < NUM_HDRS; i++) {
      finalGrade += averages[i] * WEIGHTS[i];
    }
    if (finalGrade > 100) {
      finalGrade = 100;  // Final grade has maximum 100
    }

    double roundedGrade = Math.ceil(finalGrade);

    String letterGrade = LETTERS[LETTERS.length-1];  // Default to last grade
    for (int i = 0; i < LETTERS.length; i++) {
      if (roundedGrade > LETTER_RANGES[i]) {
        letterGrade = LETTERS[i];
        break;
      }
    }

    for (int i = 0; i < NUM_HDRS; i++) {
      System.out.println("Your " + LABELS[i] + " is " + averages[i]);
    }
    System.out.println("Your raw total is " + finalGrade);
    System.out.println("Your final grade is " + letterGrade);
  }
}
