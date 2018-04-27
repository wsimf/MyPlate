package compsci701.uoa.calcounter.security;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/* Copyright (C) Sudara - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Sudara <CalCounter>, on 27/04/2018 
 */
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
}
