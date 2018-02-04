package it.unisa.sisd.question;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gab on 21/01/18.
 */
public class QuestionsLoader {

    private static final String QUESTIONS_FILE_NAME = "questions.txt";

    public static List<String> getQuestions() { return getQuestions(QUESTIONS_FILE_NAME); }

    public static List<String> getQuestions(String fileName){
        try {
            URI uri = Thread.currentThread().getContextClassLoader().getResource(fileName).toURI();
            return Files.readAllLines(Paths.get(uri), Charset.defaultCharset());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
