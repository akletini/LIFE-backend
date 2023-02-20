package akletini.life.shared.validation;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Errors {

    // Misc
    public static final String WRONG_DATE_FORMAT = "wrongDateFormat";
    public static final String ENTITY_NOT_FOUND = "entityNotFound";
    public static final String COULD_NOT_STORE = "couldNotStore";

    public static class TODO {
        public static final String CREATED_DATE_UNCHANGED = "createdDateUnchanged";
    }

    public static class CHORE {
        public static final String POSITIVE_INTERVAL = "positiveInterval";
        public static final String START_IN_THE_PAST = "startDateInThePast";
    }


    public static String getError(String errorMessage, Object... arguments) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("errors");
        String rawMessage = resourceBundle.getString(errorMessage);
        return MessageFormat.format(rawMessage, arguments);
    }
}
