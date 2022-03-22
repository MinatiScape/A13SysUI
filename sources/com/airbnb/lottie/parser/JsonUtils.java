package com.airbnb.lottie.parser;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.graphics.PointF;
import com.airbnb.lottie.parser.moshi.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class JsonUtils {
    public static final JsonReader.Options POINT_NAMES = JsonReader.Options.of("x", "y");

    public static ArrayList jsonToPoints(JsonReader jsonReader, float f) throws IOException {
        ArrayList arrayList = new ArrayList();
        jsonReader.beginArray();
        while (jsonReader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            jsonReader.beginArray();
            arrayList.add(jsonToPoint(jsonReader, f));
            jsonReader.endArray();
        }
        jsonReader.endArray();
        return arrayList;
    }

    public static int jsonToColor(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        int nextDouble = (int) (jsonReader.nextDouble() * 255.0d);
        int nextDouble2 = (int) (jsonReader.nextDouble() * 255.0d);
        int nextDouble3 = (int) (jsonReader.nextDouble() * 255.0d);
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        jsonReader.endArray();
        return Color.argb(255, nextDouble, nextDouble2, nextDouble3);
    }

    public static PointF jsonToPoint(JsonReader jsonReader, float f) throws IOException {
        int ordinal = jsonReader.peek().ordinal();
        if (ordinal == 0) {
            jsonReader.beginArray();
            float nextDouble = (float) jsonReader.nextDouble();
            float nextDouble2 = (float) jsonReader.nextDouble();
            while (jsonReader.peek() != JsonReader.Token.END_ARRAY) {
                jsonReader.skipValue();
            }
            jsonReader.endArray();
            return new PointF(nextDouble * f, nextDouble2 * f);
        } else if (ordinal == 2) {
            jsonReader.beginObject();
            float f2 = 0.0f;
            float f3 = 0.0f;
            while (jsonReader.hasNext()) {
                int selectName = jsonReader.selectName(POINT_NAMES);
                if (selectName == 0) {
                    f2 = valueFromObject(jsonReader);
                } else if (selectName != 1) {
                    jsonReader.skipName();
                    jsonReader.skipValue();
                } else {
                    f3 = valueFromObject(jsonReader);
                }
            }
            jsonReader.endObject();
            return new PointF(f2 * f, f3 * f);
        } else if (ordinal == 6) {
            float nextDouble3 = (float) jsonReader.nextDouble();
            float nextDouble4 = (float) jsonReader.nextDouble();
            while (jsonReader.hasNext()) {
                jsonReader.skipValue();
            }
            return new PointF(nextDouble3 * f, nextDouble4 * f);
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown point starts with ");
            m.append(jsonReader.peek());
            throw new IllegalArgumentException(m.toString());
        }
    }

    public static float valueFromObject(JsonReader jsonReader) throws IOException {
        JsonReader.Token peek = jsonReader.peek();
        int ordinal = peek.ordinal();
        if (ordinal == 0) {
            jsonReader.beginArray();
            float nextDouble = (float) jsonReader.nextDouble();
            while (jsonReader.hasNext()) {
                jsonReader.skipValue();
            }
            jsonReader.endArray();
            return nextDouble;
        } else if (ordinal == 6) {
            return (float) jsonReader.nextDouble();
        } else {
            throw new IllegalArgumentException("Unknown value for token of type " + peek);
        }
    }
}
