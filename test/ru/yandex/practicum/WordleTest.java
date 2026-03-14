package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class WordleTest {

    PrintWriter logFile;

    {
        try {
            logFile = new PrintWriter(Paths.get("log.txt").toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWordSelection() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add("кости");
        dictionary.add("солнце");
        dictionary.add("коробка");
        dictionary.add("короб");
        dictionary.add("канал");

        WordleDictionary wr = new WordleDictionary(dictionary, logFile);

        WordleGame wordleGame = new WordleGame(wr);

        String chosenWord = wordleGame.getRandomWordForTesting();
        assert chosenWord.length() == 5 : "Длина выбранного слова должна быть равна 5";

        boolean wordExists = dictionary.contains(chosenWord);
        assertTrue(wordExists, "Выбранное слово должно присутствовать в словаре");
    }

    @Test
    public void testGameWin() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add("кости");
        WordleDictionary wr = new WordleDictionary(dictionary, logFile);
        WordleGame wordleGame = new WordleGame(wr);

        boolean test = wordleGame.makeMove("кости");

        assertTrue(test, "Игра должна завершиться с победой");
    }

    @Test
    public void testGameLoseByExhaustingAttempts() {
        int maxAttempts = 6;
        List<String> dictionary = new ArrayList<>();
        dictionary.add("кости");
        WordleDictionary wr = new WordleDictionary(dictionary, logFile);
        WordleGame wordleGame = new WordleGame(wr);

        boolean test = true;
        for (int i = 0; i < maxAttempts; i++) {
            test = wordleGame.makeMove("слово");
        }
        assertFalse(test, "Игра должна завершиться по исчерпанию попыток");
    }

    @Test
    public void filteringAlgorithmInFileTest() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add("кости");
        dictionary.add("солнце");
        dictionary.add("коробка");
        dictionary.add("короб");
        dictionary.add("канал");
        dictionary.add("apple");

        WordleDictionary wr = new WordleDictionary(dictionary, logFile);

        int actual = wr.chooseCorrectWords().size();
        int extend = 3;
        assertEquals(extend, actual, "Не верный результат для фильтрации слов в файле");
    }

    @Test
    public void hintOperationTest() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add("кости");
        dictionary.add("короб");
        dictionary.add("ногти");

        WordleDictionary wr = new WordleDictionary(dictionary, logFile);

        WordleGame wordleGame = new WordleGame(wr);

        wordleGame.setAnswer("кости");

        wordleGame.makeMove("короб");

        String hint = wordleGame.getHint();

        assertEquals("кости", hint, "Подсказка должна соответствовать выбранному слову");
    }
}
