package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.Card;
import data.StandardResponse;
import data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardService {

    private List<Card> cards = new ArrayList<>(Arrays.asList(
            new Card("hi", "bye"),
            new Card("0", "1")
    ));

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Logger log = LoggerFactory.getLogger(CardService.class);

    public String getAll() {
        return gson.toJson(new StandardResponse(Status.SUCCESS, gson.toJsonTree(cards)));
    }

    public String getCards(String searchQuery) {

        List<Card> foundCards = cards.stream()
                .filter(card -> (card.getFront().equals(searchQuery) || card.getBack().equals(searchQuery)))
                .toList();

        if (foundCards.isEmpty()) {
            return gson.toJson(new StandardResponse(
                    Status.ERROR, "Failed to find card: " + searchQuery, gson.toJsonTree(Collections.EMPTY_LIST)));
        }

        return gson.toJson(new StandardResponse(Status.SUCCESS, gson.toJsonTree(foundCards)));
    }

    public String postCard(String toPost) {

        Type type = new TypeToken<ArrayList<Card>>() {}.getType();
        List<Card> cardsToSave = gson.fromJson(toPost, type);

        cards.addAll(cardsToSave);

        return gson.toJson(new StandardResponse(Status.SUCCESS));
    }

    public String delete(String toDelete) {

        StandardResponse response = new StandardResponse(Status.SUCCESS);

        cards.stream()
                .filter(card -> (card.getFront().equals(toDelete) || card.getBack().equals(toDelete)))
                .findFirst()
                .ifPresentOrElse(card -> cards.remove(card), () -> {
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

    public Boolean updateFront(String id, String correction) {

        cards.stream()
                .filter(card -> card.getBack().equals(id))
                .findFirst()
                .ifPresentOrElse(card -> card.setFront(correction), () -> log.error("Card not found"));

        return true;
    }
}
