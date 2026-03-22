package vn.edu.hcmuaf.fit.ttltw_nhom6.utils;

public class ValidationUtils {
    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final String PHONE_REGEX = "^(0[35789])[0-9]{8}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$";

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && email.matches(EMAIL_REGEX);
    }

    public static boolean isValidPhone(String phone) {
        return !isBlank(phone) && phone.matches(PHONE_REGEX);
    }

    public static boolean isBlank(String... values) {
        for (String v : values) {
            if (v == null || v.trim().isEmpty()) return true;
        }
        return false;
    }

    public static boolean isAllBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return false;
        }
        return true;
    }

    public static boolean isAtLeastOne(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return true;
        }
        return false;
    }
}
