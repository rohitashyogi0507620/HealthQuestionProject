package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 7/4/17.
 */

public class FormatAndValidateCredentials {
    private static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String dateDifference(String departureDate, String returnDate) {
        try {
            Date firstDate = new SimpleDateFormat("dd MMM yyyy").parse(departureDate);
            Date secondDate = new SimpleDateFormat("dd MMM yyyy").parse(returnDate);
            long diff = secondDate.getTime() - firstDate.getTime();

            long difference = (diff / (1000 * 60 * 60 * 24)) + 1;

            return Integer.toString((int) difference);
        } catch (Exception exception) {
            Log.e("Date Time Exception", ":" + exception);
        }
        return null;
    }

    public static int dateFFDifference(String departureDate, String returnDate) {
        try {
            Date firstDate = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy").parse(departureDate);
            Date secondDate = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy").parse(returnDate);
            long diff = secondDate.getTime() - firstDate.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception exception) {
            Log.e("Date Time Exception", ":" + exception);
        }
        return 0;
    }

    public static String getCurrentDateTime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    public static String[] getDateOfBirth(String age[]) {
        String dateOfBirths[] = new String[age.length];
//        int ages[] = new int[age.length];
//        for (int i = 0; i < age.length; i++) {
//            try {
//                ages[i] = Integer.parseInt(age[i]);
//            } catch (NumberFormatException e) {
//                ages[i] = 0;
//                e.printStackTrace();
//            }
//        }
        for (int i = 0; i < age.length; i++) {
            Calendar cal = Calendar.getInstance();
            try {
                int yearInt = Integer.parseInt(age[i]);
                cal.setTime(new Date());
                cal.add(Calendar.YEAR, -yearInt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                int monthInt = Integer.parseInt(age[i].substring(age[i].lastIndexOf(".") + 1));
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -monthInt);
            }
            String temporary[] = formatDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            if (temporary != null) {
                dateOfBirths[i] = temporary[1];
            }
        }
        return dateOfBirths;
    }

    public static String[] formatDate(int year, int monthOfYear, int dayOfMonth) {
        String[] dates = new String[2];
        switch (monthOfYear) {
            case 0: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 1: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 2: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 3: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 4: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 5: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 6: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 7: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 8: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 9: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 10: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            case 11: {
                dates[0] = dayOfMonth + "+" + months[monthOfYear] + "+" + year;
                dates[1] = dayOfMonth + " " + months[monthOfYear] + " " + year;
                return dates;
            }
            default: {
                return null;
            }
        }
    }

    public static String addDays(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.add(Calendar.YEAR, 1);
        return simpleDateFormat.format(c.getTime());
    }

//    public static boolean validateName(String txt, boolean isMiddleName) {
//        String regularExpression = "^[\\p{Lu}]+[a-zA-Z\\s]+$";
//        Pattern pattern = Pattern.compile(regularExpression, Pattern.UNICODE_CASE);
//        Matcher matcher = pattern.matcher(txt);
//        if (isMiddleName) {
//            String regularExpression2 = "^[\\p{Lu}]+$";
//            Pattern pattern2 = Pattern.compile(regularExpression2, Pattern.UNICODE_CASE);
//            Matcher matcher2 = pattern2.matcher(txt);
//            return matcher.find() || txt.isEmpty() || matcher2.find();
//        } else {
//            return matcher.find();
//        }
//    }

    public static boolean validatePhoneNumber(String txt) {

        return (txt.length() == 10);
    }


    public static boolean validateOfficeNumber(String txt) {

        return (txt.length() > 9);
    }

    public static boolean validatePassportNumber(String txt) {
        String regularExpression = "^[\\p{Lu}]+\\d{2}+\\s?+\\d{5}+$";
        Pattern pattern = Pattern.compile(regularExpression, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }

    public static boolean validateEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validateGstin(String gstin) {
        String ePattern = "^[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[a-zA-Z0-9]{1}$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(gstin);
        return m.matches();
    }

    public static boolean validateRegistrationNumber(String number) {
        String ePattern = "(([A-Za-z]){2,3}(|-)(?:[0-9]){1,2}(|-)(?:[A-Za-z]){2}(|-)([0-9]){1,4})|(([A-Za-z]){2,3}(|-)([0-9]){1,4})";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean validateVehicleRegistrationNumberOld(String number) {
        String ePattern = "(([A-Za-z]){2,3}(|-)(?:[0-9]){1,2}(|-)(?:[A-Za-z]){1,3}(|-)([0-9]){1,4})|(([A-Za-z]){2,3}(|-)([0-9]){1,4})";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean validateVehicleRegistrationNumber(String number) {


        String ePattern = "[A-Za-z]{2}[-][0-9]{2}[-][A-Za-z]{1,3}[-][0-9]{1,4}";

        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(number);
        // return m.matches();


        if (!m.matches()) {
            ePattern = "[A-Za-z]{2}[-][0-9]{2}[-][0-9]{1,4}";
            p = Pattern.compile(ePattern);
            m = p.matcher(number);
            if (!m.matches()) {
                ePattern = "[A-Za-z]{2}[-][0-9]{2}[-][0-9A-Za-z]{1,2}[-][0-9]{1,4}";
                p = Pattern.compile(ePattern);
                m = p.matcher(number);
                return m.matches();
            }
            return m.matches();
        } else {
            return m.matches();
        }
    }

    public static boolean validatePanCard(String panCard) {
        String ePattern = "^[A-Z]{5}\\d{4}[A-Z]{1}$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(panCard);
        return m.matches();
    }

    public static boolean validateQuotationNumber(String quotationNumber) {
        String ePattern = "^[A-Z]{9}\\d{16}$";
        Pattern pattern = Pattern.compile(ePattern, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(quotationNumber);
        return matcher.find();
    }

    public static boolean validateOrderNumber(String quotationNumber) {
        String ePattern = "^[A-Z]{5}\\d{7}$";
        Pattern pattern = Pattern.compile(ePattern, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(quotationNumber);
        return matcher.find();
    }

    public static String calculateAge(int day, int month, int year) {
        month++;
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return Integer.toString(age);
    }
}
