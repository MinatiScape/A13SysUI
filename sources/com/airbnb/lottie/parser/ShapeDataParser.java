package com.airbnb.lottie.parser;

import android.graphics.PointF;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.MiscUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/* loaded from: classes.dex */
public final class ShapeDataParser implements ValueParser<ShapeData> {
    public static final ShapeDataParser INSTANCE = new ShapeDataParser();
    public static final JsonReader.Options NAMES = JsonReader.Options.of("c", "v", "i", "o");

    @Override // com.airbnb.lottie.parser.ValueParser
    public final ShapeData parse(JsonReader jsonReader, float f) throws IOException {
        if (jsonReader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            jsonReader.beginArray();
        }
        jsonReader.beginObject();
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        ArrayList arrayList3 = null;
        boolean z = false;
        while (jsonReader.hasNext()) {
            int selectName = jsonReader.selectName(NAMES);
            if (selectName == 0) {
                z = jsonReader.nextBoolean();
            } else if (selectName == 1) {
                arrayList = JsonUtils.jsonToPoints(jsonReader, f);
            } else if (selectName == 2) {
                arrayList2 = JsonUtils.jsonToPoints(jsonReader, f);
            } else if (selectName != 3) {
                jsonReader.skipName();
                jsonReader.skipValue();
            } else {
                arrayList3 = JsonUtils.jsonToPoints(jsonReader, f);
            }
        }
        jsonReader.endObject();
        if (jsonReader.peek() == JsonReader.Token.END_ARRAY) {
            jsonReader.endArray();
        }
        if (arrayList == null || arrayList2 == null || arrayList3 == null) {
            throw new IllegalArgumentException("Shape data was missing information.");
        } else if (arrayList.isEmpty()) {
            return new ShapeData(new PointF(), false, Collections.emptyList());
        } else {
            int size = arrayList.size();
            PointF pointF = (PointF) arrayList.get(0);
            ArrayList arrayList4 = new ArrayList(size);
            for (int i = 1; i < size; i++) {
                PointF pointF2 = (PointF) arrayList.get(i);
                int i2 = i - 1;
                arrayList4.add(new CubicCurveData(MiscUtils.addPoints((PointF) arrayList.get(i2), (PointF) arrayList3.get(i2)), MiscUtils.addPoints(pointF2, (PointF) arrayList2.get(i)), pointF2));
            }
            if (z) {
                PointF pointF3 = (PointF) arrayList.get(0);
                int i3 = size - 1;
                arrayList4.add(new CubicCurveData(MiscUtils.addPoints((PointF) arrayList.get(i3), (PointF) arrayList3.get(i3)), MiscUtils.addPoints(pointF3, (PointF) arrayList2.get(0)), pointF3));
            }
            return new ShapeData(pointF, z, arrayList4);
        }
    }
}
