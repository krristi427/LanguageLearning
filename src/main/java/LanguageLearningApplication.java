import service.CardDatabaseService;
import service.CardService;

import static spark.Spark.*;

public class LanguageLearningApplication {

    public static void main(String[] args) {

        CardService service = new CardService(new CardDatabaseService());

        get("/", (request, response) -> {
            response.type("application/json");
            return service.getAll();
        });

        get("/cards/:id", (request, response) -> {
            response.type("application/json");
            return service.getCards(request.params(":id"));
        });

        post("/cards", (request, response) -> {
            response.type("application/json");
            return service.postCard(request.body());
        });

        delete("/cards/:id", (request, response) -> {
            response.type("application/json");
            return service.delete(request.params(":id"));
        });

        put("/cards/:id/:toUpdate", (request, response) -> {
            response.type("application/json");
            return service.updateBack(request.params(":id"), request.params(":toUpdate"));
        });
    }
}
