package data;

public enum Status {

    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
