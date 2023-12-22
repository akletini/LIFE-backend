package akletini.life.core.shared.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SortingConstants {
    createdAt("Created at"),

    dueAt("Due at");

    private String value;

    SortingConstants(String name) {
        this.value = name;
    }

    public static List<String> getNames() {
        return Arrays.stream(SortingConstants.values()).map(constant -> constant.value).collect(Collectors.toList());
    }

    public static SortingConstants getByValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Created at" -> createdAt;
            case "Due at" -> dueAt;
            default -> null;
        };
    }
}
