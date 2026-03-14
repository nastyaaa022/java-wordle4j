package ru.yandex.practicum;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/* в главном классе нам нужно: - создать лог-файл (он должен передаваться во все классы)
   создать загрузчик словарей WordleDictionaryLoader
   загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
   затем создать игру WordleGame и передать ей словарь
   вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
   вывести состояние игры и конечный результат */

public class Wordle {
    public static void main(String[] args) throws IOException {
        try (PrintWriter logFile = new PrintWriter(Paths.get("log.txt").toFile())) {

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logFile);
            List<String> dictionary = loader.loadDictionary("words_ru.txt");

            WordleDictionary wordleDictionary = new WordleDictionary(dictionary, logFile);
            WordleGame wordleGame = new WordleGame(wordleDictionary, logFile);

            Scanner scanner = new Scanner(System.in);
            boolean gameOver = false;

            while (wordleGame.steps < wordleGame.maxSteps) {
                System.out.println("------------------------------------------------------");
                System.out.println("Введите слово или 'подсказка' для получения подсказки:");
                String guessWord = scanner.nextLine();

                if (guessWord.equals("подсказка")) {
                    System.out.println("Подсказка: " + wordleGame.getHint());
                } else if (wordleGame.checkWordInFilteredDictionary(guessWord)) {
                    gameOver = wordleGame.makeMove(guessWord);
                    System.out.println(wordleGame.getGameState());
                } else {
                    System.out.println("Слово не найдено в словаре.");
                }
                if (gameOver) {
                    break;
                }
            }

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Загаданное слово было: " + wordleGame.getAnswer());
            if (gameOver) {
                System.out.println("Вы победили! :)");
            } else {
                System.out.println("К сожалению попытки закончились, попробуйте снова!");

            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке словаря: " + e.getMessage());
        }
    }
}