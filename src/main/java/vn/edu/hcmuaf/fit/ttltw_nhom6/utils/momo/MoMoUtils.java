package vn.edu.hcmuaf.fit.ttltw_nhom6.utils.momo;
import java.util.Map;

public class MoMoUtils {
    public static final String PARTNER_CODE = "MOMO";
    public static final String ACCESS_KEY   = "F8BBA842ECF85";
    public static final String SECRET_KEY   = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    public static final String ENDPOINT     = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static final String REDIRECT_URL  = "http://10.51.65.169:8080/TTLTW_Nhom6/momo-return";
    public static final String IPN_URL       = "http://10.51.65.169:8080/TTLTW_Nhom6/momo-return";
    public static final String REQUEST_TYPE  = "captureWallet";


    public static String createPaymentUrl(int orderId, long amount, String orderInfo) throws Exception {
        String BANK_ID = "970418";      // Mã ngân hàng
        String ACCOUNT_NO = "8870321924"; // Số tài khoản
        String ACCOUNT_NAME = "TRAN THI QUYNH TRAM"; // Tên tài khoản

        String orderIdStr = "ORDER_" + orderId + "_" + System.currentTimeMillis();
        String addInfo    = java.net.URLEncoder.encode("TT " + orderIdStr, "UTF-8");
        String accName    = java.net.URLEncoder.encode(ACCOUNT_NAME, "UTF-8");

        String vietQrUrl = "https://img.vietqr.io/image/"
                + BANK_ID + "-" + ACCOUNT_NO + "-qr_only.png"
                + "?amount=" + amount
                + "&addInfo=" + addInfo
                + "&accountName=" + accName;

        return "/momo-qr"
                + "?amount=" + amount
                + "&orderId=" + java.net.URLEncoder.encode(orderIdStr, "UTF-8")
                + "&qr="     + java.net.URLEncoder.encode(vietQrUrl, "UTF-8")
                + "&info="   + java.net.URLEncoder.encode(orderInfo, "UTF-8");
    }

    public static String getParam(Map<String, String[]> params, String key) {
        String[] v = params.get(key);
        return (v != null && v.length > 0) ? v[0] : null;
    }
}


