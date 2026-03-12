package vn.edu.hcmuaf.fit.ttltw_nhom6.utils.vnpay;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class VNPayUtils {
    public static final String VNP_TMN_CODE = "JQP2H1FD";
    public static final String VNP_HASH_SECRET = "PSRIWGYCL6C0XAMWPIRDQXZAN8AYCRA2";
    public static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";
    public static final String VNP_CURRENCY = "VND";
    public static final String VNP_LOCALE = "vn";

    public static String createPaymentUrl(int orderId, long amount, String orderInfo, String returnUrl, String ipAddress) {
        // tao ma giao dich duy nhat
        String txnRef = orderId + "_" + System.currentTimeMillis();
        String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", VNP_VERSION);
        params.put("vnp_Command", VNP_COMMAND);
        params.put("vnp_TmnCode", VNP_TMN_CODE);
//vnpay yeu cau nhan 100
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_CurrCode", VNP_CURRENCY);
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", VNP_LOCALE);
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_CreateDate", createDate);
        // Tạo chuỗi hash data và query string
        StringBuilder hashData = new StringBuilder();
        StringBuilder queryString = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                if (!first) {
                    hashData.append('&');
                    queryString.append('&');
                }
                hashData.append(key).append('=').append(value);
                queryString.append(URLEncoder.encode(key, StandardCharsets.US_ASCII))
                        .append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
                first = false;
            }
        }
        String secureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        queryString.append("&vnp_SecureHash=").append(secureHash);

        return VNP_URL + "?" + queryString;
    }

    //    xac minh chu ky call back tuu vnpay
    public static boolean verifySignature(Map<String, String[]> params) {
        String vnpSecureHash = getParam(params, "vnp_SecureHash");
        if (vnpSecureHash == null) return false;

        // Lọc bỏ các param không dùng để hash
        Map<String, String> signParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
                signParams.put(key, entry.getValue()[0]);
            }
        }

        StringBuilder hashData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : signParams.entrySet()) {
            if (!first) hashData.append('&');
            hashData.append(entry.getKey()).append('=').append(entry.getValue());
            first = false;
        }

        String expectedHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        return expectedHash.equalsIgnoreCase(vnpSecureHash);
    }

    //    Lấy orderId gốc từ vnp_TxnRef (format: "orderId_timestamp")
    public static int extractOrderId(String txnRef) {
        if (txnRef == null || !txnRef.contains("_")) return 0;
        try {
            return Integer.parseInt(txnRef.split("_")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public static String getParam(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values != null && values.length > 0) ? values[0] : null;
    }
    private static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi HMAC-SHA512: " + e.getMessage(), e);
        }
    }

}
