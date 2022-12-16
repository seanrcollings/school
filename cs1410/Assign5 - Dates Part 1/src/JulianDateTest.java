//import org.junit.Assert;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;
//
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class JulianDateTest {
//
//    /**
//     * @brief This test verifies that the default constructor successfully sets the current day, month, and year
//     */
//    @org.junit.Test
//    public void aTestJulianDateTestDefaultConstructor() {
//        JulianDate date = new JulianDate();
//
//        GregorianCalendar today = new GregorianCalendar();
//        today.setGregorianChange(new java.util.Date(Long.MAX_VALUE));
//        Assert.assertEquals("Year in default constructor was not right", today.get(Calendar.YEAR), date.getYear());
//        Assert.assertEquals("Month number in default constructor was not right", today.get(Calendar.MONTH) + 1, date.getMonth());
//        Assert.assertEquals("String name in default constructor was not right", today.getDisplayName(Calendar.MONTH, Calendar.LONG, new java.util.Locale("en", "US")), date.getMonthName());
//        Assert.assertEquals("Days in default constructor was not right", today.get(Calendar.DAY_OF_MONTH), date.getDayOfMonth());
//    }
//
//    /**
//     * @brief This test checks that the constructor correctly sets the year, month, and day
//     */
//    @org.junit.Test
//    public void bTestJulianDateTestConstructorWithParameters() {
//        int year = 2016;
//        int month = 4;
//        int day = 25;
//        JulianDate date = new JulianDate(year, month, day);
//        Assert.assertEquals("Year in constructor with parameters was not right", year, date.getYear());
//        Assert.assertEquals("Month number in constructor with parameters was not right", month, date.getMonth());
//        Assert.assertEquals("String name in constructor with parameters was not right", "April", date.getMonthName());
//        Assert.assertEquals("Days in constructor with parameters was not right", day, date.getDayOfMonth());
//    }
//
//    /**
//     * Test month
//     */
//    @org.junit.Test
//    public void cTestGetMonth() {
//        JulianDate date = new JulianDate(2020, 5, 27);
//        Assert.assertEquals("Get Month test 1 not right", date.getMonth(), 5);
//
//        date = new JulianDate(2020, 11, 26);
//        Assert.assertEquals("Get Month test 2 not right", date.getMonth(), 11);
//
//        date = new JulianDate(2020, 1, 22);
//        Assert.assertEquals("Get Month test 3 not right", date.getMonth(), 1);
//    }
//
//    /**
//     * Test month name
//     */
//    @org.junit.Test
//    public void dTestGetMonthName() {
//        JulianDate date = new JulianDate(2016, 4, 25);
//        Assert.assertEquals("Month name wrong, test 1", date.getMonthName().compareTo("April"), 0);
//
//        date = new JulianDate(2020, 11, 26);
//        Assert.assertEquals("Month name wrong, test 2", date.getMonthName().compareTo("November"), 0);
//
//        date = new JulianDate(2020, 1, 22);
//        Assert.assertEquals("Month name wrong, test 3", date.getMonthName().compareTo("January"), 0);
//    }
//
//    /**
//     * Test that we can get the year appropriately
//     */
//    @org.junit.Test
//    public void eTestGetYear() {
//        JulianDate date = new JulianDate(2016, 4, 25);
//        Assert.assertEquals("Year wrong, test 1", date.getYear(), 2016);
//
//        date = new JulianDate(2111, 11, 26);
//        Assert.assertEquals("Year wrong, test 2", date.getYear(), 2111);
//
//        date = new JulianDate(1971, 1, 22);
//        Assert.assertEquals("Year wrong, test 3", date.getYear(), 1971);
//    }
//
//    /**
//     * Test day of month
//     */
//    @org.junit.Test
//    public void fTestGetDayOfMonth() {
//        JulianDate date = new JulianDate(2016, 4, 25);
//        Assert.assertEquals("Day of month wrong, test 1", date.getDayOfMonth(), 25);
//
//        date = new JulianDate(2111, 11, 26);
//        Assert.assertEquals("Day of month wrong, test 2", date.getDayOfMonth(), 26);
//
//        date = new JulianDate(1971, 1, 1);
//        Assert.assertEquals("Day of month wrong, test 3", date.getDayOfMonth(), 1);
//    }
//
//    /**
//     * @brief This test runs through a set of years and checks if they are leap years using an array of known leap years
//     */
//    @org.junit.Test
//    public void gTestJulianDateTestIsLeapYearMethod() {
//        int[] leapYears = {2004, 2008, 2012, 2016, 2020, 2024, 2028, 2032, 2036, 2040, 2044, 2048, 2052, 2056, 2060, 2064, 2068, 2072, 2076, 2080, 2084, 2088, 2092, 2096};
//        int leapYearIndex = 0;
//        int month = 12;
//        int day = 15;
//
//        for (int year = leapYears[0]; year < leapYears[leapYears.length - 1]; year++) {
//            JulianDate date = new JulianDate(year, month, day);
//
//            if (year == leapYears[leapYearIndex]) {
//                Assert.assertTrue("Leap year test 1 wrong", date.isLeapYear());
//                leapYearIndex++;
//            } else {
//                Assert.assertFalse("Leap year test 2 wrong", date.isLeapYear());
//            }
//        }
//
//        for (int year = 1; year < 4000; year++) {
//            JulianDate date = new JulianDate(year, month, day);
//
//            if (year % 4 == 0) {
//                Assert.assertTrue("Leap year test 3 wrong", date.isLeapYear());
//            } else {
//                Assert.assertFalse("Leap year test 4 wrong", date.isLeapYear());
//            }
//        }
//    }
//
//    /**
//     * @brief This test adds days from a known date to jump days, months, and years
//     */
//    @org.junit.Test
//    public void hTestJulianDateTestAddDaysMethod() {
//        int year = 2016;
//        int month = 4;
//        int day = 15;
//
//        // Test an increase in days
//        for (int i = 0; i < 10; i++) {
//            JulianDate date = new JulianDate(year, month, day);
//            date.addDays(i);
//
//            Assert.assertEquals("Add Days year wrong, test 1", year, date.getYear());
//            Assert.assertEquals("Add Days month wrong, test 1", month, date.getMonth());
//            Assert.assertEquals("Add Days day wrong, test 1", day + i, date.getDayOfMonth());
//        }
//
//        // Test an increase in months
//        for (int i = 0; i < 5; i++) {
//            int dayIncrease = i * 28;
//            JulianDate date = new JulianDate(year, month, day);
//            date.addDays(dayIncrease);
//
//            Assert.assertEquals("Year in adding months went wrong, test 2", year, date.getYear());
//            Assert.assertEquals("Month in adding months went wrong, test 2", month + i, date.getMonth());
//        }
//
//        // Test an increase in years
//        for (int i = 0; i < 10; i++) {
//            int dayIncrease = i * 365;
//            JulianDate date = new JulianDate(year, month, day);
//            date.addDays(dayIncrease);
//
//            Assert.assertEquals("Year in adding years went wrong, test 3", year + i, date.getYear());
//            Assert.assertEquals("Month adding years went wrong, test 3", month, date.getMonth());
//        }
//    }
//
//    /**
//     * @brief This test subtracts days from a known date to jump days, months, and years
//     */
//    @org.junit.Test
//    public void iTestJulianDateTestSubtractDaysMethod() {
//        int year = 2016;
//        int month = 12;
//        int day = 15;
//
//        // Test an increase in days
//        for (int i = 0; i < 10; i++) {
//            JulianDate date = new JulianDate(year, month, day);
//            date.subtractDays(i);
//
//            Assert.assertEquals("Subtract days day wrong, test 1", year, date.getYear());
//            Assert.assertEquals("Subtract days year wrong, test 1", month, date.getMonth());
//            Assert.assertEquals("Subtract days month wrong, test 1", day - i, date.getDayOfMonth());
//        }
//
//        // Test an increase in months
//        for (int i = 0; i < 5; i++) {
//            int dayIncrease = i * 28;
//            JulianDate date = new JulianDate(year, month, day);
//            date.subtractDays(dayIncrease);
//
//            Assert.assertEquals("Subtract months year wrong, test 2", year, date.getYear());
//            Assert.assertEquals("Subtract months month wrong, test 2", month - i, date.getMonth());
//        }
//
//        // Test an increase in years
//        for (int i = 0; i < 10; i++) {
//            int dayIncrease = i * 365;
//            JulianDate date = new JulianDate(year, month, day);
//            date.subtractDays(dayIncrease);
//
//            Assert.assertEquals("Subtract years year wrong, test 3", year - i, date.getYear());
//            Assert.assertEquals("Subtract years month wrong, test 3", month, date.getMonth());
//        }
//    }
//
//    /**
//     * @brief This test adds lots of days to well-known days
//     */
//    @org.junit.Test
//    public void jTestJulianDateDateTestAddLotsOfDays() {
//        JulianDate date = new JulianDate(1989, 11, 9);
//        date.addDays(10000);
//        Assert.assertEquals("Year wrong adding lots of days, test 1", 2017, date.getYear());
//        Assert.assertEquals("Month wrong adding lots of days, test 1", 3, date.getMonth());
//        Assert.assertEquals("Day wrong adding lots of days, test 1", 27, date.getDayOfMonth());
//
//        date = new JulianDate(1980, 1, 6);
//        date.addDays(30000);
//        Assert.assertEquals("Year adding lots of days wrong, test 2", 2062, date.getYear());
//        Assert.assertEquals("Month adding lots of days wrong, test 2", 2, date.getMonth());
//        Assert.assertEquals("Day adding lots of days wrong, test 2", 24, date.getDayOfMonth());
//
//        date = new JulianDate(1970, 1, 1);
//        date.addDays(24855);
//        Assert.assertEquals("Year adding lots of days wrong, test 3", 2038, date.getYear());
//        Assert.assertEquals("Month adding lots of days wrong, test 3", 1, date.getMonth());
//        Assert.assertEquals("Day adding lots of days wrong, test 3", 19, date.getDayOfMonth());
//    }
//
//    /**
//     * @brief This test subtracts a lot of days from a known day
//     */
//    @org.junit.Test
//    public void kTestJulianDateDateTestSubtractLotsOfDays() {
//        JulianDate date = new JulianDate(2017, 3, 27);
//        date.subtractDays(10000);
//        Assert.assertEquals("Year subtracting lots of days wrong, test 1", 1989, date.getYear());
//        Assert.assertEquals("Month subtracting lots of days wrong, test 1", 11, date.getMonth());
//        Assert.assertEquals("Day subtracting lots of days wrong, test 1", 9, date.getDayOfMonth());
//
//        date = new JulianDate(2062, 2, 24);
//        date.subtractDays(30000);
//        Assert.assertEquals("Year subtracting lots of days wrong, test 2", 1980, date.getYear());
//        Assert.assertEquals("Month subtracting lots of days wrong, test 2", 1, date.getMonth());
//        Assert.assertEquals("Day subtracting lots of days wrong, test 2", 6, date.getDayOfMonth());
//
//        date = new JulianDate(2038, 1, 19);
//        date.subtractDays(24855);
//        Assert.assertEquals("Year subtracting lots of days wrong, test 3", 1970, date.getYear());
//        Assert.assertEquals("Month subtracting lots of days wrong, test 3", 1, date.getMonth());
//        Assert.assertEquals("Day subtracting lots of days wrong, test 3", 1, date.getDayOfMonth());
//    }
//}