package accountingApp.util

import java.time.{LocalDate,LocalDateTime}
import java.time.format.{DateTimeFormatter,DateTimeParseException};

object DateUtil {

  val DATE_FORMATTER : DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  val DATE_TIME_FORMATTER : DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

  implicit class DateFormater (val date : LocalDate){
     /**
     * Returns the given date as a well formatted String. The above defined 
     * {@link DateUtil#DATE_PATTERN} is used.
     * 
     * @param date the date to be returned as a string
     * @return formatted string
     */
     def asString : String = {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
  }
    implicit class DateTimeFormater (val date : LocalDateTime){
     /**
     * Returns the given date as a well formatted String. The above defined 
     * {@link DateUtil#DATE_PATTERN} is used.
     * 
     * @param date the date to be returned as a string
     * @return formatted string
     */
     def asString : String = {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMATTER.format(date);
    }
  }
  implicit class StringFormater (val data : String) {
        /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN} 
     * to a {@link LocalDate} object.
     * 
     * Returns null if the String could not be converted.
     * 
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    def parseLocalDate : LocalDate = {
        try {
          LocalDate.parse(data, DATE_FORMATTER)
        } catch {
          case  e: DateTimeParseException => null
        }
    }
    def isValid : Boolean = {
      data.parseLocalDate != null
    }
  }
}