package service;

import data.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardDatabaseService {

    private List<Card> cards = new ArrayList<>(Arrays.asList(
            new Card("hi", "bye"),
            new Card("0", "1")
    ));

    public List<Card> retrieveCards() {
        return cards;
    }
}
