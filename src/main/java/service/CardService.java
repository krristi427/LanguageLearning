package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import data.Card;
import data.StandardResponse;
import data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardService {

    CardDatabaseService service;

    public CardService(CardDatabaseService databaseService) {
        this.service = databaseService;
    }

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Logger log = LoggerFactory.getLogger(CardService.class);

    public String getAll() {

        // type needs to be provided when working with generic types such as List
        return gson.toJson(new StandardResponse(
                Status.SUCCESS, gson.toJsonTree(service.retrieveCards(), new TypeToken<ArrayList<Card>>() {}.getType())));
    }

    public String getCards(String searchQuery) {

        List<Card> foundCards = service.retrieveCards()
                .stream()
                .filter(card -> (card.getFront().equals(searchQuery) || card.getBack().equals(searchQuery)))
                .toList();

        if (foundCards.isEmpty()) {
            return gson.toJson(new StandardResponse(
                    Status.ERROR, "Failed to find card: " + searchQuery, gson.toJsonTree(Collections.EMPTY_LIST)));
        }

        return gson.toJson(new StandardResponse(
                Status.SUCCESS, gson.toJsonTree(foundCards, new TypeToken<ArrayList<Card>>() {}.getType())));
    }

    public String postCard(String toPost) {

        Type type = new TypeToken<ArrayList<Card>>() {}.getType();

        StandardResponse response = new StandardResponse(Status.SUCCESS);

        List<Card> cardsToSave = new ArrayList<>();

        try {
            cardsToSave = gson.fromJson(toPost, type);
        } catch (JsonParseException exception) {
            log.error("Somewhat malformed JSON my friend don't you think");
            response.setStatus(Status.ERROR);
        }

        service.addCards(cardsToSave);

        response.setElement(gson.toJsonTree(cardsToSave, new TypeToken<ArrayList<Card>>() {}.getType()));
        return gson.toJson(response);
    }

    public String delete(String toDelete) {

        StandardResponse response = new StandardResponse(Status.SUCCESS);

        List<Card> cards = service.retrieveCards();

        cards.stream()
                .filter(card -> (card.getFront().equals(toDelete) || card.getBack().equals(toDelete)))
                .findFirst()
                .ifPresentOrElse(card -> service.removeCard(card), () -> {
                    log.error("Card not found");
                    response.setStatus(Status.ERROR);
                });

        if (response.getStatus() == Status.ERROR) {
            response.setMessage("Failed to find card: " + toDelete);
        }

        return gson.toJson(response);
    }

    public String updateBack(String id, String correction) {

        StandardResponse response = new StandardResponse(Status.SUCCESS);

        List<Card> cards = service.retrieveCards();

        cards.stream()
                .filter(card -> card.getFront().equals(id))
                .findFirst()
                .ifPresentOrElse(card -> card.setBack(correction), () -> {
                    log.error("Card not found");
                    response.setStatus(Status.ERROR);
                });

        if (response.getStatus() == Status.ERROR) {
            response.setMessage("Failed to find card: " + id);
        }

        return gson.toJson(response);
    }
}
