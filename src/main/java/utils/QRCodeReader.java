package utils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class QRCodeReader {

    // Path A: decode a QR code directly from a URL that serves the image (no browser needed)
    public static String readQRFromUrl(String imageUrl) {
        try {
            trustAllCertificates(); // Only for known test/staging domains — see note below
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            return decode(image);
        } catch (Exception e) {
            throw new RuntimeException("Could not fetch QR image from URL: " + imageUrl, e);
        }
    }

    // Path B: decode a QR code from an image already saved on disk
    public static String readQRFromFile(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return decode(image);
        } catch (IOException e) {
            throw new RuntimeException("Could not read QR image file: " + imagePath, e);
        }
    }

    private static String decode(BufferedImage image) {
        try {
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            throw new RuntimeException("Could not decode QR code from image", e);
        }
    }

    // Disables SSL certificate verification — acceptable ONLY for internal test/staging domains
    private static void trustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}