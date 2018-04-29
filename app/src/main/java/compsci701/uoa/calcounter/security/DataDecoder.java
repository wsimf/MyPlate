package compsci701.uoa.calcounter.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import compsci701.uoa.calcounter.model.User;

public class DataDecoder {

    public JSONObject getMeals(final Context context) {
        final StringBuilder builder = new StringBuilder("5734,");
        try {
            final String[] data = getObfuscatedValues(context);
            for (int i = 0; i < data.length; i++) {
                builder.append(data[i]);
                if (i != data.length - 1) {
                    builder.append(",");
                }
            }
            return new JSONObject(decodeString(builder.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(final Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String s = prefs.getString(User.getStoreName(), getAssetName("google"));
        if (s.equals(getAssetName("google"))) { return null; }
        try {
            return new User(new JSONObject(decrypt(getSecretKey(getData()), decodeString(s))));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] getObfuscatedValues(final Context context) throws IOException {
        final String[] result = new String[3];
        final InputStream[] assets = new InputStream[3];
        assets[0] = context.getAssets().open(decodeString(getAssetName("google")));
        assets[1] = context.getAssets().open(decodeString(getAssetName("samsung")));
        assets[2] = context.getAssets().open(decodeString(getAssetName("lg")));
        for (int i = 0; i < 3; i++) {
            int size = assets[i].available();
            byte[] buffer = new byte[size];
            assets[i].read(buffer);
            assets[i].close();
            result[i] = new String(buffer, "UTF-8");
        }
        return result;
    }

    private String getAssetName(String place) {
        switch (place) {
            case "google":
                return "13,1431229975,4,-314968757,15,1371370296,8,1892367637,17,960236776,1,1473423511,22,-1819102519,7,-924081319,3,-264892048,12,-1373610505,9,-976976987,3,2026227436,24,1149714950,12";

            case "samsung":
                return "14,-1228778703,17,926015271,20,-588637715,22,-1815305593,13,1958710495,24,-444655586,18,-1548915558,5,450449920,14,-533356250,15,-2068787415,15,-716951941,8,-414369580,20,394331051,20,1952637197,24";

            case "lg":
                return "15,-1942817647,21,-1906301300,21,-78550580,2,-1184557324,16,-583822095,22,892067908,13,-1172929502,23,2094067620,17,-1164522453,23,115102762,17,562442770,18,2032801644,7,1820622568,1,1222508886,14,2001436449,20";
        }

        return null;
    }

    private String decodeString(String encodedString) {
        final String[] split = encodedString.split(",");
        if (split.length == 0) {
            return "";
        }

        final int[][] data = new int[2][];
        data[0] = new int[Integer.valueOf(split[0])];
        data[1] = new int[Integer.valueOf(split[0])];
        byte[] buffer = new byte[Integer.valueOf(split[0])];

        int currentData0 = 0;
        int currentData1 = 0;

        for (int i = 1; i < split.length; i++) {
            if (i % 2 == 0) {
                data[1][currentData1] = Integer.valueOf(split[i]);
                currentData1++;
            } else {
                data[0][currentData0] = Integer.valueOf(split[i]);
                currentData0++;
            }
        }

        for (int i = 0; i < data[0].length; i++) {
            buffer[i] = (byte) (data[0][i] >>> data[1][i]);
        }

        return new String(buffer);
    }

    public static String getData() {
        return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = -428145891;buf[0] = (byte) (t >>> 21);t = 1460615416;buf[1] = (byte) (t >>> 8);t = 124008100;buf[2] = (byte) (t >>> 8);t = 465085236;buf[3] = (byte) (t >>> 23);return new String(buf);}}.toString());
    }

    private BigInteger getPublicKey(String pinNumber){
        final BigInteger big2 = new BigInteger("60");
        return big2.pow(Integer.parseInt(pinNumber)).mod(new BigInteger("12345678901234567"));
    }

    public String getSecretKey(String pinNumber) {
        String secretKey = null;
        final BigInteger bi = getPublicKey(getData());
        final int Random = Integer.parseInt(pinNumber);
        BigInteger reallyBig = new BigInteger("12345678901234567");
        BigInteger bigk = bi.pow(Random).mod(reallyBig);
        String array = bigk.toString();
        if (array != null && array.length() == 16) {
            secretKey = String.valueOf(array);
        } else if (array != null && array.length() == 17) {
            String rephrase = array.substring(0, array.length() - 1);
            secretKey = String.valueOf(rephrase);
        }

        return secretKey;
    }

    private String generateKey(String secretKeyString) {
        char[] Key = secretKeyString.toCharArray();
        String reverse_STR = new StringBuilder(secretKeyString).reverse().toString();
        String rephrase = reverse_STR.substring(0, reverse_STR.length() - 4);
        char[] Message = rephrase.toCharArray();
        char[] Encrypted = new char[Message.length];
        for (int i = 0; i < Message.length; i++) {
            Encrypted[i] = (char) (Message[i] ^ Key[i % Key.length]);
        }
        return Base64.encodeToString(new String(Encrypted).getBytes(), Base64.NO_WRAP);
    }

    public String encrypt(String secretKey, String content) {
        try {
            String key2 = generateKey(secretKey);
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"),"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String decrypt(String secretKeyString, String msgContent2) {
        try {
            String key2 = generateKey(secretKeyString);
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKeyString.getBytes("UTF-8"),"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(Base64.decode(msgContent2.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
