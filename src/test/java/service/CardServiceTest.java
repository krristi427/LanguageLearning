package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.Card;
import data.StandardResponse;
import data.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardDatabaseService databaseService;

    @InjectMocks
    private CardService cardService;

    private static final List<Card> expectedCards = new ArrayList<>(Arrays.asList(
            new Card("hi", "bye"),
            new Card("0", "1")
    ));

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BeforeEach
    void setup() {

        // define what happens when the DB is called
        lenient().when(databaseService.retrieveCards()).thenReturn(expectedCards);
        lenient().when(databaseService.addCards(anyList())).then(invocation -> {
            List<Card> toAdd = invocation.getArgument(0);
            return expectedCards.addAll(toAdd);
        });
        lenient().when(databaseService.removeCard(any(Card.class))).then(invocation -> {
            Card toRemove = invocation.getArgument(0);
            return expectedCards.remove(toRemove);
        });
    }

    @Test
    public void testGetAll() {

        // this is what we should get
        String expectedResponse = gson.toJson(new StandardResponse(Status.SUCCESS, gson.toJsonTree(expectedCards)));

        // this is what we actually get
        // bear in mind: we only mock the DB-interaction, not the class itself, as that's what we're testing
        String receivedCards = cardService.getAll();

        // so if what we expect matches what we get, we know that our method in itself works fine
        Assertions.assertEquals(receivedCards, expectedResponse);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "\t", "\n"})
    public void testGetCard_NotFound(String searchQuery) {

        String expected = gson.toJson(new StandardResponse(
                Status.ERROR, "Failed to find card: " + searchQuery, gson.toJsonTree(Collections.EMPTY_LIST)));

        String found = cardService.getCards(searchQuery);
        Assertions.assertEquals(expected, found);
    }

    @Test
    public void testGetCard_Found() {

        List<Card> foundCards = new ArrayList<>(List.of(new Card("hi", "bye")));

        String expected = gson.toJson(new StandardResponse(
                Status.SUCCESS, gson.toJsonTree(foundCards, new TypeToken<ArrayList<Card>>() {}.getType())));

        String found = cardService.getCards("hi");
        Assertions.assertEquals(expected, found);
    }

    @Test
    public void testPostSuccess() {

        List<Card> newCards = new ArrayList<>(List.of(
                new Card("cat", "katze"),
                new Card("dog", "hund")
        ));
        String expected = gson.toJson(new StandardResponse(
                Status.SUCCESS, gson.toJsonTree(newCards, new TypeToken<ArrayList<Card>>() {}.getType())));

        String toPost = "[\n" +
                "    {\n" +
                "        \"front\": \"cat\",\n" +
                "        \"back\": \"katze\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"front\": \"dog\",\n" +
                "        \"back\": \"hund\"\n" +
                "    }\n" +
                "]";

        String received = cardService.postCard(toPost);
        Assertions.assertEquals(expected, received);
    }

    @Test
    public void testPostFailure() {

        String expected = gson.toJson(new StandardResponse(Status.ERROR, gson.toJsonTree(Collections.EMPTY_LIST)));

        String toPost = "[\n";

        String received = cardService.postCard(toPost);
        Assertions.assertEquals(expected, received);
    }

    @Test
    public void testDeleteSuccess() {

        String expected = gson.toJson(new StandardResponse(Status.SUCCESS));
        String received = cardService.delete("hi");
        Assertions.assertEquals(expected, received);
    }

    @Test
    public void testDeleteFailure() {

        String toDelete = "fuck off";

        StandardResponse response = new StandardResponse(Status.ERROR);
        response.setMessage("Failed to find card: " + toDelete);

        String expected = gson.toJson(response);
        String received = cardService.delete("fuck off");
        Assertions.assertEquals(expected, received);
    }
}
