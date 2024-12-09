package de.jare.gildeddice.entities.enums;

public enum Category {

    WIN("Money gewinn"),
    LOSE("Money verlust"),
    EXTRA("Sonstiges"),
    MAINX("Non-Linear"),
    MAINL("Linear"),
    INVESTMENT("Investiton"),
    FATE("Schicksal");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
