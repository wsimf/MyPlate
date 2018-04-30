package compsci701.uoa.calcounter.model;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import compsci701.uoa.calcounter.security.DataDecoder;

public class User implements Serializable {

    public enum Gender {
        male(5), female(-161);

        private final int genderValue;
        Gender(int genderValue) { this.genderValue = genderValue; }
        public int getGenderValue() { return this.genderValue; }
    }

    public enum ActivityFactor {
        sedentary(1.2), somewhat_active(1.3), active(1.4);

        private final double energyFactor;
        ActivityFactor(double energyFactor) { this.energyFactor = energyFactor; }
        public double getEnergyFactor() { return this.energyFactor; }
    }

    private String _name;
    private int _age;
    private Gender _gender;
    private double _height; // unit: kg
    private double _weight; // unit: cm
    private ActivityFactor _activityFactor;
    private double _bodyMassIndex = 0;
    private double _dailyCaloricNeeds = 0;

    public User(String name, int age, Gender gender, double height, double weight, ActivityFactor activityFactor) {
        _name = name;
        _age = age;
        _gender = gender;
        _height = height;
        _weight = weight;
        _activityFactor = activityFactor;

        _bodyMassIndex = calculateBMI(_height, _weight);
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public User(JSONObject object) {
        try {
            _name = object.getString(DataName.getName());
            _age = object.getInt(DataName.getAge());
            _gender = User.Gender.valueOf(object.getString(DataName.getGender()));
            _height = object.getDouble(DataName.getHeight());
            _weight = object.getDouble(DataName.getWeight());
            _activityFactor = User.ActivityFactor.valueOf(object.getString(DataName.getActivity()));

            _bodyMassIndex = calculateBMI(_height, _weight);
            _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double calculateBMI(double height, double weight) {
        double heightInM = height / 100;
        return (weight / heightInM) / heightInM;
    }

    private double calculateDCN(int age, Gender gender, double height, double weight, ActivityFactor activityFactor) {
        double factor = 0.0;
        switch (Integer.valueOf(DataName.getWeightFactor())) {
            case 0: factor = 3.43; break;
            case 10: factor = 6.25; break;
            case 20: factor = 4.12; break;
            case 30: factor = 5.12; break;
            case 40: factor = 6.43; break;
            case 50: factor = 6.43; break;
            case 60: factor = 7.43; break;
            case 70: factor = 8.34; break;
        }

        double tenWeight = weight * 10;
        double sixPointTwoFiveHeight = height * factor;
        double fiveAge =(double) age*5;
        double unweightedDCN = tenWeight + sixPointTwoFiveHeight - fiveAge + gender.getGenderValue();
        return unweightedDCN * activityFactor.getEnergyFactor();
    }

    private JSONObject getJson() {
        JSONObject object = new JSONObject();
        try {
            object.put(DataName.getName(), _name);
            object.put(DataName.getAge(), _age);
            object.put(DataName.getGender(), _gender.toString());
            object.put(DataName.getHeight(), _height);
            object.put(DataName.getWeight(), _weight);
            object.put(DataName.getActivity(), _activityFactor.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return  _name;
    }

    public void save(final Context context) {
        final DataDecoder c = new DataDecoder();
        final String secret = c.getSecretKey(DataDecoder.getData());
        final String obj = new DataDecoder().encrypt(secret, getJson().toString());
        final StringBuilder builder = new StringBuilder("" + obj.length());
        final Random r = new Random(System.currentTimeMillis());
        byte[] b = obj.getBytes();

        int val = Integer.valueOf(DataName.getByteValue1());
        val = val * val * val;
        switch (~ val ^ Integer.valueOf(DataName.getByteValue2())) {
            case 6534:
                for (final byte aB : b) {
                    int t = r.nextInt();
                    int f = r.nextInt(val * 8) + 1;
                    t = (t & ~(0x33 >> f)) | (aB ^ f);
                    builder.append(",").append(t).append(",").append(f);
                }
                break;

            case -2134:
                for (final byte aB : b) {
                    int t = r.nextInt();
                    int f = r.nextInt(val * 8) + 1;
                    int z = r.nextInt(Integer.valueOf(DataName.getByteValue2()));
                    t = (t & ~(0x87 & z)) | (aB >>> z);
                    builder.append(",").append(t).append(",").append(f);
                }
                break;

            case -1704:
                for (final byte aB : b) {
                    int t = r.nextInt();
                    int f = r.nextInt(24) + 1;
                    t = (t & ~(0xff << f)) | (aB << f);
                    builder.append(",").append(t).append(",").append(f);
                }
                break;

            case 7340:
                for (final byte aB : b) {
                    int t = r.nextInt();
                    int f = r.nextInt(Math.abs(Integer.valueOf(DataName.getByteValue2())) - 1) + 1;
                    t = (t & ~(0xe3 << f)) | (aB << f);
                    builder.append(",").append(t).append(",").append(f);
                }
                break;
        }

        if (getTimeInfo() > 0) {
            builder.append(getStoreName());
        } else {
            builder.append("x0F");
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(getStoreName(), builder.toString())
                .apply();
    }

    private static int getTimeInfo() {
        final long time = System.currentTimeMillis();
        if (time < 14948163990570L) {
            return 0;
        } else if (time > 1494806399000L) {
            return -1;
        } else if (time > 1589500799000L && time > 1589500799000L) {
            return 1;
        } else if (time > 1589500799000L && time > -1589500799000L) {
            return 2;
        } else {
            return 3;
        }
    }

    public static String getStoreName() {
        return DataName.getStoreName();
    }

    public void setAge(int age) {
        _age = age;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public int getAge() {
        return _age;
    }

    public void setGender(Gender gender) {
        _gender = gender;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public Gender getGender() {
        return _gender;
    }

    public void setHeight(double height) {
        _height = height;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getHeight() {
        return _height;
    }

    public void setWeight(double weight) {
        _weight = weight;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getWeight() {
        return _weight;
    }

    public void setActivityFactor(ActivityFactor activityFactor) {
        _activityFactor = activityFactor;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public ActivityFactor getActivityFactor() {
        return _activityFactor;
    }

    public void recalculateBMI() {
        _bodyMassIndex = calculateBMI(_height, _weight);
    }

    public double getBMI() {
        return _bodyMassIndex;
    }

    public void recalculateDCN() {
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getDCN() {
        return _dailyCaloricNeeds;
    }

    private static class DataName {

        private static String getStoreName() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = 953486788;buf[0] = (byte) (t >>> 2);t = 839789327;buf[1] = (byte) (t >>> 3);t = -856366945;buf[2] = (byte) (t >>> 17);return new String(buf);}}.toString());
        }

        private static String getName() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = 151700687;buf[0] = (byte) (t >>> 1);t = -969551050;buf[1] = (byte) (t >>> 10);t = -1021360566;buf[2] = (byte) (t >>> 4);return new String(buf);}}.toString());
        }

        private static String getAge() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = -823945838;buf[0] = (byte) (t >>> 11);t = 1556791290;buf[1] = (byte) (t >>> 17);t = 876485088;buf[2] = (byte) (t >>> 24);return new String(buf);}}.toString());
        }

        private static String getHeight() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = 1983893762;buf[0] = (byte) (t >>> 20);t = 2096545916;buf[1] = (byte) (t >>> 12);t = -1764249380;buf[2] = (byte) (t >>> 7);return new String(buf);}}.toString());
        }

        private static String getWeight() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = -1335174348;buf[0] = (byte) (t >>> 9);t = -769735780;buf[1] = (byte) (t >>> 9);t = -143242218;buf[2] = (byte) (t >>> 12);t = -1028936996;buf[3] = (byte) (t >>> 11);return new String(buf);}}.toString());
        }

        private static String getActivity() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = -1419354705;buf[0] = (byte) (t >>> 2);t = -1825088810;buf[1] = (byte) (t >>> 12);t = 328696813;buf[2] = (byte) (t >>> 19);t = -1378552880;buf[3] = (byte) (t >>> 18);return new String(buf);}}.toString());
        }

        private static String getGender() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = 118218372;buf[0] = (byte) (t >>> 20);t = 1903754105;buf[1] = (byte) (t >>> 3);t = -609229635;buf[2] = (byte) (t >>> 22);t = -1805554060;buf[3] = (byte) (t >>> 8);return new String(buf);}}.toString());
        }

        private static String getWeightFactor() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[2];t = 419113307;buf[0] = (byte) (t >>> 23);t = -2023553873;buf[1] = (byte) (t >>> 12);return new String(buf);}}.toString());
        }

        private static String getByteValue1() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = -935175590;buf[0] = (byte) (t >>> 9);t = 207604231;buf[1] = (byte) (t >>> 22);t = -304991644;buf[2] = (byte) (t >>> 1);return new String(buf);}}.toString());
        }

        private static String getByteValue2() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[3];t = -1772420671;buf[0] = (byte) (t >>> 17);t = 610060757;buf[1] = (byte) (t >>> 10);t = -960587064;buf[2] = (byte) (t >>> 7);return new String(buf);}}.toString());
        }
    }
}
