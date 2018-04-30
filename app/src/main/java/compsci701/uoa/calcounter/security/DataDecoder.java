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
            return new User(new JSONObject(decrypt(getSecretKey(getData()), decodeString(s.subSequence(0, s.length() - 3).toString()))));
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
        return big2.pow(Integer.parseInt(pinNumber)).mod(new BigInteger(getInitValue()));
    }

    public String getSecretKey(String pinNumber) {
        String secretKey = null;
        final BigInteger bi = getPublicKey(getData());
        final int Random = Integer.parseInt(pinNumber);
        BigInteger reallyBig = new BigInteger(getInitValue());
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
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"),getSpecName());
            Cipher cipher = Cipher.getInstance(getName());
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
            SecretKeySpec skeySpec = new SecretKeySpec(secretKeyString.getBytes("UTF-8"),getSpecName());
            Cipher cipher = Cipher.getInstance(getName());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(Base64.decode(msgContent2.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getName() {
        return (new Object() {int t;public String toString() {byte[] buf = new byte[20];t = -2059290716;buf[0] = (byte) (t >>> 16);t = 1589351051;buf[1] = (byte) (t >>> 1);t = -1866117961;buf[2] = (byte) (t >>> 10);t = -1749588843;buf[3] = (byte) (t >>> 23);t = -2110121656;buf[4] = (byte) (t >>> 11);t = -2144845149;buf[5] = (byte) (t >>> 13);t = -510593241;buf[6] = (byte) (t >>> 14);t = 17402872;buf[7] = (byte) (t >>> 6);t = 1745486517;buf[8] = (byte) (t >>> 13);t = 695743093;buf[9] = (byte) (t >>> 21);t = 1585941105;buf[10] = (byte) (t >>> 17);t = 1579787552;buf[11] = (byte) (t >>> 15);t = 1162261459;buf[12] = (byte) (t >>> 13);t = 1691517973;buf[13] = (byte) (t >>> 11);t = -344322728;buf[14] = (byte) (t >>> 11);t = -788289478;buf[15] = (byte) (t >>> 22);t = -2054093346;buf[16] = (byte) (t >>> 14);t = 344982838;buf[17] = (byte) (t >>> 20);t = 655203633;buf[18] = (byte) (t >>> 23);t = 394567310;buf[19] = (byte) (t >>> 1);return new String(buf);}}.toString());
    }

    private String getSpecName() {
        return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = 332275944;buf[0] = (byte) (t >>> 7);t = 1933609606;buf[1] = (byte) (t >>> 9);t = 1462356073;buf[2] = (byte) (t >>> 15);return new String(buf);}}.toString());
    }

    private String getInitValue() {
        return (new Object() {int t;public String toString() {byte[] buf = new byte[17];t = -682980279;buf[0] = (byte) (t >>> 6);t = 1693764121;buf[1] = (byte) (t >>> 10);t = 53692200;buf[2] = (byte) (t >>> 20);t = -1953691119;buf[3] = (byte) (t >>> 14);t = 380217365;buf[4] = (byte) (t >>> 11);t = 886716784;buf[5] = (byte) (t >>> 18);t = -1663095073;buf[6] = (byte) (t >>> 18);t = 1725700546;buf[7] = (byte) (t >>> 3);t = -680496689;buf[8] = (byte) (t >>> 3);t = 214380555;buf[9] = (byte) (t >>> 8);t = -364007837;buf[10] = (byte) (t >>> 1);t = -1315875188;buf[11] = (byte) (t >>> 19);t = 1890793908;buf[12] = (byte) (t >>> 12);t = -964257852;buf[13] = (byte) (t >>> 13);t = -1765167575;buf[14] = (byte) (t >>> 11);t = -2138648967;buf[15] = (byte) (t >>> 13);t = 472086657;buf[16] = (byte) (t >>> 12);return new String(buf);}}.toString());
    }
}
