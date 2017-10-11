package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

public class Event {

    public static final String MESSAGE_EVENT_CONSTRAINTS =
            "Person emails should be 2 alphanumeric/period strings separated by '@'";
    public static final String EVENT_VALIDATION_REGEX = "[\\w\\.]+@[\\w\\.]+";

    public final String eventAll;
    public final String eventName;
    public final String eventDate;

    /**
     * Validates given email.
     *
     * @throws IllegalValueException if given email address string is invalid.
     */
    public Event(String event) throws IllegalValueException {
        requireNonNull(event);
        String trimmedEvent = event.trim();
        if (!isValidEvent(trimmedEvent)) {
            throw new IllegalValueException(MESSAGE_EVENT_CONSTRAINTS);
        }
        this.eventAll = trimmedEvent;
        String[] splitEvent = trimmedEvent.split("@");
        this.eventName = splitEvent[0];
        this.eventDate = splitEvent[1];
    }

    /**
     * Returns if a given string is a valid event name.
     */
    public static boolean isValidEvent(String test) {
        return test.matches(EVENT_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Event // instanceof handles nulls
                && this.eventAll.equals(((Event) other).eventAll)); // state check
    }

    @Override
    public int hashCode() { return eventAll.hashCode(); }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + eventAll + ']';
    }

}
