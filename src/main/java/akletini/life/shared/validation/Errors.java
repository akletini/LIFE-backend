package akletini.life.shared.validation;

import java.util.ResourceBundle;

public class Errors {

    // Misc
    public static final String WRONG_DATE_FORMAT = "wrongDateFormat";

    // Todo
    public static final String CREATED_DATE_UNCHANGED = "createdDateUnchanged";

    public static String getError(String errorMessage, String... arguments) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("errors");
        String rawMessage = resourceBundle.getString(errorMessage);
        return String.format(rawMessage, arguments);
    }
}
