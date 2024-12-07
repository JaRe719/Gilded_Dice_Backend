package de.jare.gildeddice.entities.enums;

public enum Skill {

    INTELLIGENCE("Intelligenz"),
    NEGOTIATE("Verhandeln"),
    ABILITY("Geschicklichkeit"),
    PLANNING("Plannen"),
    STAMINA("Ausdauer");

    private final String skillname;

    Skill(String skillname) {
        this.skillname = skillname;
    }

    public String getSkillname() {
        return skillname;
    }

}

