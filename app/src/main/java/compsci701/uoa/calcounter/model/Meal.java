package compsci701.uoa.calcounter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

public class Meal implements Serializable {

    public enum MealType {
        Breakfast, Lunch, Dinner
    }

    private String _name;
    private MealType _mealType;
    private Double _calories;
    private String[] _ingredients;

    public Meal(MealType mealType, JSONObject json) {
        _mealType = mealType;
        try {
            _name = json.has(DataName.getName1()) ? json.getString(DataName.getName1()) : json.getString(DataName.getName2());
            _calories = json.has(DataName.getCal1()) ? json.getDouble(DataName.getCal1()) : json.getDouble(DataName.getCal2());
            JSONArray jsonArray = json.has(DataName.getIn1()) ? json.getJSONArray(DataName.getIn1()) : json.getJSONArray(DataName.getIn2());
            _ingredients = new String[jsonArray.length()];
            for (int j = 0; j < jsonArray.length(); j++) {
                _ingredients[j] = (String) jsonArray.get(j);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return _name;
    }

    public Double getCalories() {
        return _calories;
    }

    public String[] getIngredients() {
        return _ingredients;
    }

    public MealType getMealType() {
        return _mealType;
    }

    private static class DataName {

        private static String getName1() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = -185964840;buf[0] = (byte) (t >>> 20);t = -2066697852;buf[1] = (byte) (t >>> 2);t = -1230600982;buf[2] = (byte) (t >>> 23);t = 1952797947;buf[3] = (byte) (t >>> 16);return new String(buf);}}.toString());
        }

        private static String getName2() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[4];t = -1471488669;buf[0] = (byte) (t >>> 9);t = 1813285645;buf[1] = (byte) (t >>> 3);t = -52735561;buf[2] = (byte) (t >>> 14);t = 1498032592;buf[3] = (byte) (t >>> 22);return new String(buf);}}.toString());
        }

        private static String getCal1() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[8];t = 860937203;buf[0] = (byte) (t >>> 14);t = -431715280;buf[1] = (byte) (t >>> 5);t = 1995271367;buf[2] = (byte) (t >>> 8);t = -1893613769;buf[3] = (byte) (t >>> 10);t = -696148811;buf[4] = (byte) (t >>> 6);t = 1934558562;buf[5] = (byte) (t >>> 19);t = 1801790607;buf[6] = (byte) (t >>> 16);t = -927228711;buf[7] = (byte) (t >>> 11);return new String(buf);}}.toString());
        }

        private static String getCal2() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[8];t = -799655971;buf[0] = (byte) (t >>> 12);t = -869988092;buf[1] = (byte) (t >>> 21);t = 919303782;buf[2] = (byte) (t >>> 20);t = 1371007164;buf[3] = (byte) (t >>> 15);t = 380520750;buf[4] = (byte) (t >>> 13);t = -207109303;buf[5] = (byte) (t >>> 3);t = -1397036088;buf[6] = (byte) (t >>> 21);t = -1422104857;buf[7] = (byte) (t >>> 1);return new String(buf);}}.toString());
        }

        private static String getIn1() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[11];t = 293610790;buf[0] = (byte) (t >>> 2);t = -1568237844;buf[1] = (byte) (t >>> 4);t = -682248590;buf[2] = (byte) (t >>> 4);t = -305560824;buf[3] = (byte) (t >>> 18);t = 852544477;buf[4] = (byte) (t >>> 23);t = 1692571757;buf[5] = (byte) (t >>> 24);t = -1113265085;buf[6] = (byte) (t >>> 18);t = -162444065;buf[7] = (byte) (t >>> 20);t = 1851572044;buf[8] = (byte) (t >>> 24);t = -1528272518;buf[9] = (byte) (t >>> 17);t = 1303733478;buf[10] = (byte) (t >>> 1);return new String(buf);}}.toString());
        }

        private static String getIn2() {
            return (new Object() {int t;public String toString() {byte[] buf = new byte[11];t = 758446982;buf[0] = (byte) (t >>> 15);t = -149195916;buf[1] = (byte) (t >>> 3);t = 1202090204;buf[2] = (byte) (t >>> 12);t = 221475538;buf[3] = (byte) (t >>> 8);t = -861333072;buf[4] = (byte) (t >>> 21);t = -1627525742;buf[5] = (byte) (t >>> 2);t = 632587556;buf[6] = (byte) (t >>> 15);t = 1667140793;buf[7] = (byte) (t >>> 5);t = -152642282;buf[8] = (byte) (t >>> 20);t = -1487972740;buf[9] = (byte) (t >>> 20);t = 288935539;buf[10] = (byte) (t >>> 5);return new String(buf);}}.toString());
        }
    }
}
