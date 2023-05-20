package bullscows;
import java.util.*;


public class BullsCows {
    private final String secretCode;
    private int codeLength;
    private final Scanner scanner = new Scanner(System.in);

    BullsCows() {
        secretCode = generateSecretNumber();
    }

    public void play() {
        System.out.println("Okay, let's start a game!");

        String answer = "";

        int turn = 1;
        while (!Objects.equals(answer, secretCode)) {
            System.out.printf("Turn %d. Answer:%n", turn);
            answer = scanner.next();

            int[] bullsAndCows = countBullsAndCows(answer);

            System.out.println(getGradeString(bullsAndCows[0], bullsAndCows[1]));

            if (bullsAndCows[0] == codeLength) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }

            turn++;
        }
    }

    public void playOne() {
        String answer = scanner.nextLine();

        int[] bullsAndCows = countBullsAndCows(answer);

        String grade = getGradeString(bullsAndCows[0], bullsAndCows[1]);

        System.out.printf("%s The secret code is %s.%n", grade, secretCode);
    }

    private int[] countBullsAndCows(String answer) {
        List<Integer> cows = new ArrayList<>();
        List<Integer> bulls = new ArrayList<>();

        for (int i = 0; i < codeLength; i++) {
            char answerDigit = answer.charAt(i);
            for (int j = 0; j < codeLength; j++) {
                if (cows.contains(j) | bulls.contains(j)) {
                    continue;
                }

                char codeDigit = secretCode.charAt(j);

                if (i == j ) {
                    if (answerDigit == codeDigit) {
                        bulls.add(i);
                    }
                    break;
                }

                if (answerDigit == codeDigit) {
                    cows.add(j);
                    break;
                }
            }
        }

        return new int[] {bulls.size(), cows.size()};
    }

    private String getPluralOrSingular(String word, int count) {
        if (count > 1) {
            return word + "s";
        }

        return word;
    }

    private String getGradeString(int bulls, int cows) {
        String grade = "Grade: ";
        if (bulls == 0 & cows == 0) {
            grade += "None";
        } else if (bulls >= 1 & cows == 0) {
            grade += "%d %s".formatted(bulls, getPluralOrSingular("bull", bulls));
        } else if (bulls == 0 & cows >= 1) {
            grade += "%d %s".formatted(cows, getPluralOrSingular("cow", cows));
        } else {
            grade += "%d %s and %d %s".formatted(bulls, getPluralOrSingular("bull", bulls),
                    cows, getPluralOrSingular("cow", cows));
        }

        return grade;
    }

    public String generateSecretNumber() {
        List<Character> symbols = new ArrayList<>();

        StringBuilder secretCode = new StringBuilder();

        System.out.println("Input the length of the secret code:");
        codeLength = 0;
        String line = "";
        try {
            line = scanner.nextLine();
            codeLength = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.%n", line);
            return secretCode.toString();
        }

        if (codeLength == 0) {
            System.out.println("Error: can't generate a secret number with a length of 0");
            return secretCode.toString();
        }

        if (codeLength > 36) {
            System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.",
                    codeLength);
            return secretCode.toString();
        }

        System.out.println("Input the number of possible symbols in the code:");
        int symbolCount = 0;

        try {
            line = scanner.nextLine();
            symbolCount = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.%n", line);
            return secretCode.toString();
        }
        if (symbolCount > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return secretCode.toString();
        }

        if (symbolCount < codeLength) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.%n",
                    codeLength, symbolCount);
            return secretCode.toString();
        }

        for (int i = 0; i < 10 & i < symbolCount; i++) {
            symbols.add((char) (48 + i));
        }

        if (symbolCount > 10) {
            for (int i = 0; i < 26 & i < symbolCount - 10; i++) {
                symbols.add((char) (97 + i));
            }
        }


        List<Character> digits = new ArrayList<>();

        Random random = new Random();

        int firstDigit = random.nextInt(1, symbolCount);

        secretCode.append(symbols.get(firstDigit));


        int index = 0;
        while (index < codeLength - 1) {
            char digit = symbols.get(random.nextInt(symbolCount));

            if (!digits.contains(digit)) {
                digits.add(digit);
                secretCode.append(digit);
                index++;
            }
        }

        System.out.printf("The secret is prepared: %s (%s).%n", "*".repeat(codeLength), getSymbolRange(symbols));

        return secretCode.toString();
    }

    private String getSymbolRange(List<Character> symbols) {
        int length = symbols.size();
        StringBuilder range = new StringBuilder();
        range.append("0-");

        if (length <= 10) {
            range.append(length - 1);
        } else {
            range.append(length - 1).append(", a");
            if (length > 11) {
                range.append("-").append(symbols.get(length - 1));
            }
        }

        return range.toString();
    }

}
