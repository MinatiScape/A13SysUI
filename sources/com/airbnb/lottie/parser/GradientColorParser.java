package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.PointF;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.MiscUtils;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class GradientColorParser implements ValueParser<GradientColor> {
    public int colorPoints;

    @Override // com.airbnb.lottie.parser.ValueParser
    public final GradientColor parse(JsonReader jsonReader, float f) throws IOException {
        boolean z;
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        if (jsonReader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            jsonReader.beginArray();
        }
        while (jsonReader.hasNext()) {
            arrayList.add(Float.valueOf((float) jsonReader.nextDouble()));
        }
        if (z) {
            jsonReader.endArray();
        }
        if (this.colorPoints == -1) {
            this.colorPoints = arrayList.size() / 4;
        }
        int i4 = this.colorPoints;
        float[] fArr = new float[i4];
        int[] iArr = new int[i4];
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            i = this.colorPoints * 4;
            if (i5 >= i) {
                break;
            }
            int i8 = i5 / 4;
            double floatValue = ((Float) arrayList.get(i5)).floatValue();
            int i9 = i5 % 4;
            if (i9 == 0) {
                fArr[i8] = (float) floatValue;
            } else if (i9 == 1) {
                i6 = (int) (floatValue * 255.0d);
            } else if (i9 == 2) {
                i7 = (int) (floatValue * 255.0d);
            } else if (i9 == 3) {
                iArr[i8] = Color.argb(255, i6, i7, (int) (floatValue * 255.0d));
            }
            i5++;
        }
        GradientColor gradientColor = new GradientColor(fArr, iArr);
        if (arrayList.size() > i) {
            int size = (arrayList.size() - i) / 2;
            double[] dArr = new double[size];
            double[] dArr2 = new double[size];
            int i10 = 0;
            while (i < arrayList.size()) {
                if (i % 2 == 0) {
                    dArr[i10] = ((Float) arrayList.get(i)).floatValue();
                } else {
                    dArr2[i10] = ((Float) arrayList.get(i)).floatValue();
                    i10++;
                }
                i++;
            }
            while (true) {
                int[] iArr2 = gradientColor.colors;
                if (i3 >= iArr2.length) {
                    break;
                }
                int i11 = iArr2[i3];
                double d = gradientColor.positions[i3];
                int i12 = 1;
                while (true) {
                    if (i12 >= size) {
                        i2 = (int) (dArr2[size - 1] * 255.0d);
                        break;
                    }
                    int i13 = i12 - 1;
                    double d2 = dArr[i13];
                    double d3 = dArr[i12];
                    if (dArr[i12] >= d) {
                        double d4 = dArr2[i13];
                        double d5 = dArr2[i12];
                        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
                        i2 = (int) ((((d5 - d4) * ((d - d2) / (d3 - d2))) + d4) * 255.0d);
                        break;
                    }
                    i12++;
                }
                gradientColor.colors[i3] = Color.argb(i2, Color.red(i11), Color.green(i11), Color.blue(i11));
                i3++;
            }
        }
        return gradientColor;
    }

    public GradientColorParser(int i) {
        this.colorPoints = i;
    }
}
