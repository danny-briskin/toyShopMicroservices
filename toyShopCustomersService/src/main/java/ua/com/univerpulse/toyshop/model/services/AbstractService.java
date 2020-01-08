package ua.com.univerpulse.toyshop.model.services;

import org.jetbrains.annotations.NotNull;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshop project.
 */
public abstract class AbstractService {
    protected LocalDateTime tryToParseStringDate(@NotNull String stringDate) throws DateParseException {
        if (stringDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        }
        if (stringDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")
                || stringDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z")
        ) {
            return LocalDateTime
                    .parse(stringDate, DateTimeFormatter.ISO_DATE_TIME);
        }
        throw new DateParseException(stringDate);
    }
}
