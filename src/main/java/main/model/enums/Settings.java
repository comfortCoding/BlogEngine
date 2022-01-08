package main.model.enums;

public enum Settings {
    MULTIUSER_MODE("MULTIUSER_MODE"),
    POST_PREMODERATION("POST_PREMODERATION"),
    STATISTICS_IS_PUBLIC("STATISTICS_IS_PUBLIC");

    private final String name;

    Settings(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
