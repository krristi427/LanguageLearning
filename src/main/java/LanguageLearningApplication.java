import static spark.Spark.*;

public class LanguageLearningApplication {

    public static void main(String[] args) {

        get("/hello", (request, response) -> "Hello World");
    }
}