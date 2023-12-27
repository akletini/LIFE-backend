package akletini.life.core.shared.validation;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Errors {

    // Misc
    public static final String ENTITY_NOT_FOUND = "entityNotFound";
    public static final String COULD_NOT_STORE = "couldNotStore";
    public static final String ENTITY_IS_NULL = "entityIsNull";
    public static final String ENTITY_NOT_FOUND_BY_NAME = "entityNotFoundByName";
    public static final String ENITTY_ALREADY_EXISTS = "entityAlreadyExists";

    public interface USER {
        String INVALID_TOKEN_CONTAINER = "invalidTokenContainer";

        String MISSING_PASSWORD = "missingPassword";
    }

    public interface CHORE {
        String POSITIVE_INTERVAL = "positiveInterval";
        String START_IN_THE_PAST = "startDateInThePast";
    }

    public interface TASK {

        String CREATED_DATE_UNCHANGED = "createdDateUnchanged";

        String ASSIGNED_USER_MATCH = "assignedUserMatch";
    }

    public interface TAG {
        String TAG_NAME_UNIQUE = "tagNameUnique";
    }

    public interface ATTRIBUTE_TYPE {
        String ILLEGAL_TYPE_MIGRATION = "illegalTypeMigration";
    }


    public static String getError(String errorMessage, Object... arguments) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("errors");
        String rawMessage = resourceBundle.getString(errorMessage);
        return MessageFormat.format(rawMessage, arguments);
    }
}
