public enum DifficultyLevel {
    EASY,
    HARD;

    public static DifficultyLevel fromString(String value) {
        if (value == null) {
            return EASY;
        }

        if (value.equalsIgnoreCase("hard")) {
            return HARD;
        }

        return EASY;
    }
}
