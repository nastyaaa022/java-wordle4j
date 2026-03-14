package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private String answer;
    int steps;
    private final WordleDictionary dictionary;
    final int MAX_STEPS = 6;

    private final PrintWriter logFile;

    private final List<String> gameWords = new ArrayList<>();
    private final List<Character> incorrectLetters = new ArrayList<>();
    private final Set<Character> lettersNotInCorrectPositions = new HashSet<>();
    private final Map<Integer, Character> correctLetters = new HashMap<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter logFile) {
        this.dictionary = dictionary;
        this.logFile = logFile;
        this.answer = chooseRandomWord();
        this.steps = 0;
    }

    public WordleGame(WordleDictionary dictionary) {
        this(dictionary, new PrintWriter(System.err));
    }

    public String getHint() {
        List<String> filteredDictionary = dictionary.getWords();

        filteredDictionary = filterDictionary(filteredDictionary);

        Random random = new Random();
        return filteredDictionary.get(random.nextInt(filteredDictionary.size()));
    }

    private List<String> filterDictionary(List<String> dictionary) {
        List<String> filtered = new ArrayList<>();
        Set<String> alreadyUsedWords = new HashSet<>();

        for (String word : dictionary) {
            if (word.length() == answer.length()) {
                boolean isValid = true;
                for (int i = 0; i < word.length(); i++) {
                    char letter = word.charAt(i);
                    if (incorrectLetters.contains(letter) ||
                            (correctLetters.containsKey(i) && letter != correctLetters.get(i))) {
                        isValid = false;
                        break;
                    }
                }
                for (Character ch : lettersNotInCorrectPositions) {
                    if (!word.contains(String.valueOf(ch))) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid && !alreadyUsedWords.contains(word)) {
                    filtered.add(word);
                    alreadyUsedWords.add(word);
                }
            }
        }
        return filtered;
    }

    public String getGameState() {
        if (steps >= MAX_STEPS || checkWinCondition()) {
            return "Игра окончена. ";
        }

        StringBuilder state = new StringBuilder();

        state.append("---------------------------------------------");
        state.append("\nУгаданные буквы на правильной позиции: ");
        for (Map.Entry<Integer, Character> entry : correctLetters.entrySet()) {
            state.append((entry.getKey() + 1)).append(" - ").append(entry.getValue()).append(", ");
        }
        if (!correctLetters.isEmpty()) {
            state.delete(state.length() - 2, state.length());
        }

        state.append("\nПрисутствующие буквы: ");
        for (Character ch : lettersNotInCorrectPositions) {
            state.append(ch).append(", ");
        }
        if (!lettersNotInCorrectPositions.isEmpty()) {
            state.deleteCharAt(state.length() - 2);
            state.deleteCharAt(state.length() - 1);
        }

        state.append("\nОсталось попыток: ").append(MAX_STEPS - steps).append("\n");

        return state.toString();
    }

    private boolean checkWinCondition() {
        return correctLetters.size() == answer.length();
    }

    private String chooseRandomWord() {
        List<String> words = dictionary.getWords();
        Random random = new Random();
        String word;

        do {
            word = words.get(random.nextInt(words.size()));
        } while (word.length() != 5);

        return word;
    }

    public String getRandomWordForTesting() {
        return chooseRandomWord();
    }

    public boolean makeMove(String guessWord) {
        steps++;
        try {
            String result = compareWords(guessWord, answer);

            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(i) == '+') {
                    char letter = guessWord.charAt(i);
                    if (!correctLetters.containsKey(i)) {
                        correctLetters.put(i, letter);
                    }

                } else if (result.charAt(i) == '-') {
                    if (!incorrectLetters.contains(guessWord.charAt(i))) {
                        incorrectLetters.add(guessWord.charAt(i));
                    }
                } else if (result.charAt(i) == '^') {
                    lettersNotInCorrectPositions.add(guessWord.charAt(i));
                }
            }
            System.out.println(result);

            if (result.equals("+++++")) {
                logMessage("Вы выиграли!");
                return true;
            } else if (steps >= MAX_STEPS) {
                logMessage("Вы проиграли, попытки закончились.");
                return false;
            }
            return false;
        } catch (IllegalArgumentException e) {
            logMessage("Ошибка: " + e.getMessage());
            return false;
        }
    }

    public String compareWords(String guessWord, String targetWord) {
        if (guessWord.length() != targetWord.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < guessWord.length(); i++) {
            char guessChar = guessWord.charAt(i);
            char targetChar = targetWord.charAt(i);

            if (guessChar == targetChar) {
                result.append('+');
            } else if (targetWord.indexOf(guessChar) != -1) {
                result.append('^');
            } else {
                result.append('-');
            }
        }
        return result.toString();
    }

    public boolean checkWordInFilteredDictionary(String word) {
        if (word == null) {
            logMessage("Слово для проверки не указано.");
            return false;
        }
        List<String> filteredDictionary = dictionary.chooseCorrectWords();
        return filteredDictionary.contains(word);
    }

    public void logMessage(String message) {
        if (logFile != null) {
            logFile.println(message);
            logFile.flush();
        } else {
            System.err.println("Лог-файл не инициализирован.");
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getGameWords() {
        return gameWords;
    }
}
