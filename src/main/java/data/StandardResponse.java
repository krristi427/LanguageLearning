package data;

import com.google.gson.JsonElement;
import data.Status;

public class StandardResponse {

    private Status status;
    private String message;
    private JsonElement jsonElement;

    // for stuff that just needs to see everything's fine
    public StandardResponse(Status status) {
        this.status = status;
    }

    // for stuff that went wrong
    public StandardResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    // for stuff that needs response
    public StandardResponse(Status status, JsonElement jsonElement) {
        this.status = status;
        this.jsonElement = jsonElement;
    }

    // everything else
    public StandardResponse(Status status, String message, JsonElement jsonElement) {
        this.status = status;
        this.message = message;
        this.jsonElement = jsonElement;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }
}
