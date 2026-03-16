package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции посравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final PrintWriter logFile;

    public WordleDictionary(List<String> words, PrintWriter logFile) throws WordleDictionaryException {
        this.logFile = logFile;
        if (words == null) {
            logFile.println("Ошибка: список слов передан как null.");
            throw new WordleDictionaryException("Список слов не должен быть пустым.");

        }
        this.words = words;
    }

    public List<String> chooseCorrectWords() {
        List<String> correctWords;

        List<String> filteredWords = excludeEnglishWords();
        logMessage("После исключения английских слов: " + filteredWords.size());

        filteredWords = wordToLowerCase(filteredWords);
        logMessage("После замены регистра: " + filteredWords.size());

        filteredWords = letterReplacement(filteredWords);
        logMessage("После замены букв: " + filteredWords.size());

        correctWords = getWordsByLength(filteredWords, 5);
        logMessage("Окончательный список слов (после удаления слов != 5 символам): " + correctWords.size());

        return correctWords;
    }

    public List<String> excludeEnglishWords() {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (!word.matches(".*[a-zA-Z].*")) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    public List<String> wordToLowerCase(List<String> words) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            String modifiedWord = word.toLowerCase();
            filteredWords.add(modifiedWord);
        }
        return filteredWords;
    }

    public List<String> letterReplacement(List<String> words) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            String modifiedWord = word.toLowerCase().replace('ё', 'е');
            filteredWords.add(modifiedWord);
        }
        return filteredWords;
    }

    public List<String> getWordsByLength(List<String> words, int length) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() == length) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    public void logMessage(String message) {
        if (logFile != null) {
            logFile.println(message);
            logFile.flush();
        } else {
            System.err.println("Лог-файл не инициализирован.");
        }
    }

    public List<String> getWords() {
        return words;
    }
}