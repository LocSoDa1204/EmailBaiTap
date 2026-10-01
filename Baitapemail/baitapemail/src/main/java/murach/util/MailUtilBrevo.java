package murach.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;

public class MailUtilBrevo {

    private static final String API_URL = "https://api.brevo.com/v3/smtp/email";

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        String apiKey = System.getenv("BREVO_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new MessagingException("Thieu bien moi truong BREVO_API_KEY");
        }
        String sender = System.getenv("MAIL_FROM");
        if (sender == null || sender.isEmpty()) {
            sender = from;
        }
        String bcc = System.getenv("MAIL_BCC");

        // Brevo nhận nội dung HTML; văn bản thường được đổi sang HTML đơn giản
        String html = bodyIsHTML ? body
                : "<html><body>" + htmlEscape(body).replace("\n", "<br>")
                + "</body></html>";

        StringBuilder json = new StringBuilder();
        json.append("{\"sender\":{\"name\":\"MailList\",\"email\":\"")
            .append(esc(sender)).append("\"},")
            .append("\"to\":[{\"email\":\"").append(esc(to)).append("\"}],");
        if (bcc != null && !bcc.isEmpty()) {
            json.append("\"bcc\":[{\"email\":\"").append(esc(bcc)).append("\"}],");
        }
        json.append("\"subject\":\"").append(esc(subject)).append("\",")
            .append("\"htmlContent\":\"").append(esc(html)).append("\"}");

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("api-key", apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data);
            }

            int code = conn.getResponseCode();
            if (code < 200 || code >= 300) {
                throw new MessagingException(
                        "Brevo API loi " + code + ": " + read(conn.getErrorStream()));
            }
        } catch (IOException e) {
            throw new MessagingException(
                    "Khong goi duoc Brevo API: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String read(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    private static String htmlEscape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String esc(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}