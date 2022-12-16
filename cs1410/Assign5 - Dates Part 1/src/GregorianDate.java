import java.sql.SQLOutput;

public class GregorianDate {
    int day = 5;
    int month = 4;
    int year = 1900;

    GregorianDate() {

    }

    GregorianDate(int setYear, int setMonth, int setDay) {
        day = setDay;
        month = setMonth;
        year = setYear;
    }

    // GETTERS
    String getMonthName() {
        
    }

    int getMonth() { return month; }
    int getYear() { return year; }
    int getDayOfMonth() { return day; }

    void addDays(int days) {
        for (int i = 0; i <= days; i++) {
            if (++day > getNumberOfDaysInMonth()) {
                day = 1;
                if (++month > 12) {
                    month = 1;
                    year++;
                }
            }
        }
    }

    void subtractDays(int days) {
        for (int i = days; i > 0; i--) {
            if (--day < 1) {
                day = getNumberOfDaysInMonth();
                if (--month < 1) {
                    month = 12;
                    year--;
                }
            }
        }
    }

    // UTILITY METHODS
    boolean isLeapYear() {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            }
            return true;
        }
        return false;
    }

    int getNumberOfDaysInMonth() {
        switch (month) {
            case 2:
                return isLeapYear() ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    // DISPLAY METHODS
    void printShortDate() {
        System.out.printf("%02d/%02d/%04d", month, day, year);
    }

    void printLongDate() {
//        System.out.printf("%s %02d, %04d");
    }

    public static void main(String[] args) {
        GregorianDate date = new GregorianDate();
//        System.out.println(date.isLeapYear());
        date.printShortDate();
    }
}


