package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final PrintWriter logFile;

    public List<String> loadDictionary(String dictionaryFilePath) throws IOException {
        List<String> dictionary = new ArrayList<>();
        if (!Files.exists(Paths.get(dictionaryFilePath))) {
            throw new IOException("Файл словаря не найден: " + dictionaryFilePath);
        }
        try (BufferedReader reader = new BufferedReader(
                new FileReader(dictionaryFilePath, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
        }
        return dictionary;
    }

    public WordleDictionaryLoader(PrintWriter logFile) {
        if (logFile == null) {
            throw new IllegalArgumentException("logFile не должен быть null.");
        }
        this.logFile = logFile;
    }

    public void logMessage(String message) {
        logFile.println(message);
        logFile.flush();
    }
}
