import service.CardService;

import static spark.Spark.*;

public class LanguageLearningApplication {

    public static void main(String[] args) {

        CardService service = new CardService();

        get("/", (request, response) -> service.getAll());
        get("/cards/:id", (request, response) -> service.getCards(request.params(":id")));

        post("/cards", (request, response) -> service.postCard(request.body()));

        delete("/card/:id", (request, response) -> service.delete(request.params(":id")));

        put("/card/:id/:toUpdate", (request, response) ->
                service.updateBack(request.params(":id"), request.params(":toUpdate")));
    }
}
