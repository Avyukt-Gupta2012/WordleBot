import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class WordleBot {
    public static void main(String[] args) {
        ArrayList<String> possibleWords = new ArrayList<>();
        loadWords(possibleWords);

        Scanner scanner = new Scanner(System.in);

        for (int attempt = 1; attempt <= 6 && !possibleWords.isEmpty(); attempt++) {
            String guess = possibleWords.get((int) (Math.random() * possibleWords.size()));
            System.out.println("Attempt " + attempt + ": Try this word -> " + guess);

            System.out.print("Enter feedback (g for green, y for yellow, x for gray, e.g. gyxgx): ");
            String feedback = scanner.nextLine().trim().toLowerCase();

            if (feedback.equals("ggggg")) {
                System.out.println("Solved in " + attempt + " attempts!");
                break;
            }

            // Filter possible words based on feedback
            possibleWords.removeIf(word -> !matchesFeedback(word, guess, feedback));
        }

        if (possibleWords.isEmpty()) {
            System.out.println("No possible words left. Check your feedback for mistakes.");
        }
    }

    // Returns true if 'word' matches the feedback for 'guess'
    private static boolean matchesFeedback(String word, String guess, String feedback) {
        boolean[] used = new boolean[5];

        // First pass: handle greens
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == 'g') {
                if (word.charAt(i) != guess.charAt(i)) return false;
                used[i] = true;
            }
        }

        // Second pass: handle yellows and grays
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            if (feedback.charAt(i) == 'y') {
                if (word.charAt(i) == c) return false;
                if (!word.contains("" + c)) return false;
            }
            if (feedback.charAt(i) == 'x') {
                // If the letter occurs elsewhere as green/yellow, allow it
                boolean occursElsewhere = false;
                for (int j = 0; j < 5; j++) {
                    if (j != i && guess.charAt(j) == c && (feedback.charAt(j) == 'g' || feedback.charAt(j) == 'y')) {
                        occursElsewhere = true;
                        break;
                    }
                }
                if (!occursElsewhere && word.contains("" + c)) return false;
            }
        }
        return true;
    }

    // Loads words from words.txt
    static void loadWords(ArrayList<String> array) {
        try {
            File file = new File("words.txt");
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String word = sc.nextLine().trim();
                    if (word.length() == 5) array.add(word);
                }
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }
}