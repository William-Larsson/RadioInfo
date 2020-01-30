package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class that handles everything regarding date and time
 * for the model.
 */
public class DateTimeUtil {
    private DateTimeFormatter dateFormat;
    private DateTimeFormatter hourFormat;

    /**
     * init DateTimeFormatter-patterns
     */
    public DateTimeUtil(){
        dateFormat = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        hourFormat = DateTimeFormatter.ofPattern("HH");
    }


    /**
     * Get the current date and time as a ZonedDateTime'
     * object.
     * @return = ZonedDateTime
     */
    public ZonedDateTime getCurrentTimeAsZonedDateTime(){
        return ZonedDateTime.now();
    }


    /**
     * Get current whole hours as a string.
     * @return = the hours.
     */
    public int getCurrentHours(){
        return Integer.parseInt(ZonedDateTime.now().format(hourFormat));
    }


    /**
     * Get the current date in an easily readable format
     * @return = date as string.
     */
    public String getCurrentDate(){
        return ZonedDateTime.now().format(dateFormat);
    }


    /**
     * Get the other date, either one day prior to or
     * one day after today.
     * @return = the day.
     */
    public String getOtherDate(){
        if (Integer.parseInt(ZonedDateTime.now().format(hourFormat)) >= 12){
            return ZonedDateTime.now().plusDays(1).format(dateFormat);
        } else {
            return ZonedDateTime.now().minusDays(1).format(dateFormat);
        }
    }


    /**
     * Get amount of hours in between two given
     * timestamps.
     * @param zdtA = timestamp as ZonedDateTime
     * @param zdtB = timestamp as ZonedDateTime
     * @return = hours.
     */
    public long durationBetweenTimes(ZonedDateTime zdtA, ZonedDateTime zdtB){
        return Duration.between(zdtA.toLocalDateTime(), zdtB.toLocalDateTime()).toHours();
    }


    /**
     * Convert a date/time string to a ZonedDateTime
     * object.
     * @param time = the time that should be converted
     * @return = ZDT object.
     */
    public ZonedDateTime stringToZonedDateTime(String time){
        return ZonedDateTime.parse(time);
    }


    /**
     * True if the value in the table cell is a time2 stamp
     * that hasn't happened yet. Otherwise false
     * @param time2 = the utc time2.
     * @return = see above.
     */
    public String isBeforeNow (String time1, String time2){
        if (ZonedDateTime.now().toLocalDateTime()
                .isAfter(ZonedDateTime.parse(time2).toLocalDateTime())){
            return "Avslutad";
        } else if (ZonedDateTime.now().toLocalDateTime()
                .isBefore(ZonedDateTime.parse(time2).toLocalDateTime())) {
            if (ZonedDateTime.now().toLocalDateTime()
                    .isAfter(ZonedDateTime.parse(time1).toLocalDateTime())){
                return "Pågående";
            } else return "Kommande";
        } else return "Ett fel inträffade";
    }
}
