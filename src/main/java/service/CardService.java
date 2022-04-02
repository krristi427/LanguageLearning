package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardService {

    private List<Card> cards = new ArrayList<>(Arrays.asList(
            new Card("hi", "bye"),
            new Card("love", "aura"),
            new Card("0", "1")
    ));
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Logger log = LoggerFactory.getLogger(CardService.class);

    public String getAll() {
        return gson.toJson(cards);
    }

    public String getCards(String searchQuery) {

        List<Card> foundCards = cards.stream()
                .filter(card -> (card.getFront().equals(searchQuery) || card.getBack().equals(searchQuery)))
                .toList();

        return gson.toJson(foundCards);
    }

    public Boolean postCard(String toPost) {

        Type type = new TypeToken<ArrayList<Card>>() {}.getType();
        List<Card> cardsToSave = gson.fromJson(toPost, type);

        cards.addAll(cardsToSave);

        return true;
    }

    public Boolean delete(String toDelete) {

        cards.stream()
                .filter(card -> (card.getFront().equals(toDelete) || card.getBack().equals(toDelete)))
                .findFirst()
                .ifPresentOrElse(card -> cards.remove(card), () -> log.error("Card not found"));

        return true;
    }

    public Boolean updateBack(String id, String correction) {

        cards.stream()
                .filter(card -> card.getFront().equals(id))
                .findFirst()
                .ifPresentOrElse(card -> card.setBack(correction), () -> log.error("Card not found"));

        return true;
    }

    public Boolean updateFront(String id, String correction) {

        cards.stream()
                .filter(card -> card.getBack().equals(id))
                .findFirst()
                .ifPresentOrElse(card -> card.setFront(correction), () -> log.error("Card not found"));

        return true;
    }
}
