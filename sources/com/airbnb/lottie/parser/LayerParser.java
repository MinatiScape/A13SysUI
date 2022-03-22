package com.airbnb.lottie.parser;

import android.graphics.Color;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonUtf8Reader;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LayerParser {
    public static final JsonReader.Options NAMES = JsonReader.Options.of("nm", "ind", "refId", "ty", "parent", "sw", "sh", "sc", "ks", "tt", "masksProperties", "shapes", "t", "ef", "sr", "st", "w", "h", "ip", "op", "tm", "cl", "hd");
    public static final JsonReader.Options TEXT_NAMES = JsonReader.Options.of("d", "a");
    public static final JsonReader.Options EFFECTS_NAMES = JsonReader.Options.of("nm");

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v13 */
    public static Layer parse(JsonUtf8Reader jsonUtf8Reader, LottieComposition lottieComposition) throws IOException {
        ArrayList arrayList;
        ArrayList arrayList2;
        String str;
        long j;
        String str2;
        String str3;
        char c;
        char c2;
        String str4;
        long j2;
        long j3;
        String str5;
        Layer.MatteType matteType = Layer.MatteType.NONE;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        jsonUtf8Reader.beginObject();
        Float valueOf = Float.valueOf(1.0f);
        Float valueOf2 = Float.valueOf(0.0f);
        boolean z = false;
        Layer.MatteType matteType2 = matteType;
        float f = 1.0f;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        boolean z2 = false;
        float f2 = 0.0f;
        float f3 = 0.0f;
        long j4 = -1;
        String str6 = null;
        Layer.LayerType layerType = null;
        String str7 = null;
        AnimatableTransform animatableTransform = null;
        AnimatableTextFrame animatableTextFrame = null;
        AnimatableTextProperties animatableTextProperties = null;
        AnimatableFloatValue animatableFloatValue = null;
        long j5 = 0;
        String str8 = "UNSET";
        float f4 = 0.0f;
        JsonUtf8Reader jsonUtf8Reader2 = jsonUtf8Reader;
        while (jsonUtf8Reader.hasNext()) {
            int i6 = 1;
            switch (jsonUtf8Reader2.selectName(NAMES)) {
                case 0:
                    str3 = str6;
                    str8 = jsonUtf8Reader.nextString();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 1:
                    str3 = str6;
                    j5 = jsonUtf8Reader.nextInt();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 2:
                    str3 = str6;
                    str7 = jsonUtf8Reader.nextString();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 3:
                    str2 = str6;
                    j = j5;
                    int nextInt = jsonUtf8Reader.nextInt();
                    Layer.LayerType layerType2 = Layer.LayerType.UNKNOWN;
                    if (nextInt < 6) {
                        layerType = Layer.LayerType.values()[nextInt];
                    } else {
                        layerType = layerType2;
                    }
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case 4:
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    j4 = jsonUtf8Reader.nextInt();
                    break;
                case 5:
                    str2 = str6;
                    j = j5;
                    i = (int) (Utils.dpScale() * jsonUtf8Reader.nextInt());
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case FalsingManager.VERSION /* 6 */:
                    str2 = str6;
                    j = j5;
                    i2 = (int) (Utils.dpScale() * jsonUtf8Reader.nextInt());
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case 7:
                    str3 = str6;
                    i3 = Color.parseColor(jsonUtf8Reader.nextString());
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 8:
                    str3 = str6;
                    animatableTransform = AnimatableTransformParser.parse(jsonUtf8Reader, lottieComposition);
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 9:
                    str3 = str6;
                    matteType2 = Layer.MatteType.values()[jsonUtf8Reader.nextInt()];
                    lottieComposition.maskAndMatteCount++;
                    j5 = j5;
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str3;
                    break;
                case 10:
                    str2 = str6;
                    j = j5;
                    Mask.MaskMode maskMode = null;
                    jsonUtf8Reader.beginArray();
                    while (jsonUtf8Reader.hasNext()) {
                        Mask.MaskMode maskMode2 = Mask.MaskMode.MASK_MODE_ADD;
                        jsonUtf8Reader.beginObject();
                        Mask.MaskMode maskMode3 = maskMode;
                        Mask.MaskMode maskMode4 = maskMode3;
                        AnimatableIntegerValue animatableIntegerValue = maskMode4;
                        boolean z3 = false;
                        AnimatableShapeValue animatableShapeValue = maskMode4;
                        while (jsonUtf8Reader.hasNext()) {
                            String nextName = jsonUtf8Reader.nextName();
                            Objects.requireNonNull(nextName);
                            switch (nextName.hashCode()) {
                                case 111:
                                    if (nextName.equals("o")) {
                                        c = 0;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 3588:
                                    if (nextName.equals("pt")) {
                                        c = 1;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 104433:
                                    if (nextName.equals("inv")) {
                                        c = 2;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 3357091:
                                    if (nextName.equals("mode")) {
                                        c = 3;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                default:
                                    c = 65535;
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    animatableIntegerValue = AnimatableValueParser.parseInteger(jsonUtf8Reader, lottieComposition);
                                    break;
                                case 1:
                                    animatableShapeValue = new AnimatableShapeValue(KeyframesParser.parse(jsonUtf8Reader, lottieComposition, Utils.dpScale(), ShapeDataParser.INSTANCE));
                                    break;
                                case 2:
                                    z3 = jsonUtf8Reader.nextBoolean();
                                    break;
                                case 3:
                                    String nextString = jsonUtf8Reader.nextString();
                                    Objects.requireNonNull(nextString);
                                    switch (nextString.hashCode()) {
                                        case 97:
                                            if (nextString.equals("a")) {
                                                c2 = 0;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 105:
                                            if (nextString.equals("i")) {
                                                c2 = 1;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 110:
                                            if (nextString.equals("n")) {
                                                c2 = 2;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 115:
                                            if (nextString.equals("s")) {
                                                c2 = 3;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        default:
                                            c2 = 65535;
                                            break;
                                    }
                                    switch (c2) {
                                        case 0:
                                            maskMode3 = maskMode2;
                                            break;
                                        case 1:
                                            lottieComposition.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                                            maskMode3 = Mask.MaskMode.MASK_MODE_INTERSECT;
                                            break;
                                        case 2:
                                            maskMode3 = Mask.MaskMode.MASK_MODE_NONE;
                                            break;
                                        case 3:
                                            maskMode3 = Mask.MaskMode.MASK_MODE_SUBTRACT;
                                            break;
                                        default:
                                            Logger.warning("Unknown mask mode " + nextName + ". Defaulting to Add.");
                                            maskMode3 = maskMode2;
                                            break;
                                    }
                                default:
                                    jsonUtf8Reader.skipValue();
                                    break;
                            }
                            animatableShapeValue = animatableShapeValue;
                        }
                        jsonUtf8Reader.endObject();
                        arrayList3.add(new Mask(maskMode3, animatableShapeValue, animatableIntegerValue, z3));
                        maskMode = null;
                    }
                    lottieComposition.maskAndMatteCount += arrayList3.size();
                    jsonUtf8Reader.endArray();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    str2 = str6;
                    j = j5;
                    jsonUtf8Reader.beginArray();
                    while (jsonUtf8Reader.hasNext()) {
                        ContentModel parse = ContentModelParser.parse(jsonUtf8Reader, lottieComposition);
                        if (parse != null) {
                            arrayList4.add(parse);
                        }
                    }
                    jsonUtf8Reader.endArray();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    jsonUtf8Reader.beginObject();
                    while (jsonUtf8Reader.hasNext()) {
                        int selectName = jsonUtf8Reader2.selectName(TEXT_NAMES);
                        if (selectName == 0) {
                            str4 = str6;
                            j2 = j5;
                            animatableTextFrame = new AnimatableTextFrame(KeyframesParser.parse(jsonUtf8Reader2, lottieComposition, 1.0f, DocumentDataParser.INSTANCE));
                        } else if (selectName != i6) {
                            jsonUtf8Reader.skipName();
                            jsonUtf8Reader.skipValue();
                        } else {
                            jsonUtf8Reader.beginArray();
                            if (jsonUtf8Reader.hasNext()) {
                                JsonReader.Options options = AnimatableTextPropertiesParser.PROPERTIES_NAMES;
                                jsonUtf8Reader.beginObject();
                                AnimatableTextProperties animatableTextProperties2 = null;
                                while (jsonUtf8Reader.hasNext()) {
                                    if (jsonUtf8Reader2.selectName(AnimatableTextPropertiesParser.PROPERTIES_NAMES) != 0) {
                                        jsonUtf8Reader.skipName();
                                        jsonUtf8Reader.skipValue();
                                    } else {
                                        jsonUtf8Reader.beginObject();
                                        AnimatableFloatValue animatableFloatValue2 = null;
                                        AnimatableFloatValue animatableFloatValue3 = null;
                                        AnimatableColorValue animatableColorValue = null;
                                        AnimatableColorValue animatableColorValue2 = null;
                                        while (jsonUtf8Reader.hasNext()) {
                                            int selectName2 = jsonUtf8Reader2.selectName(AnimatableTextPropertiesParser.ANIMATABLE_PROPERTIES_NAMES);
                                            if (selectName2 != 0) {
                                                str5 = str6;
                                                if (selectName2 != 1) {
                                                    if (selectName2 == 2) {
                                                        animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonUtf8Reader2, lottieComposition, true);
                                                    } else if (selectName2 != 3) {
                                                        jsonUtf8Reader.skipName();
                                                        jsonUtf8Reader.skipValue();
                                                        j3 = j5;
                                                    } else {
                                                        animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonUtf8Reader2, lottieComposition, true);
                                                    }
                                                    str6 = str5;
                                                } else {
                                                    j3 = j5;
                                                    animatableColorValue2 = new AnimatableColorValue(KeyframesParser.parse(jsonUtf8Reader, lottieComposition, 1.0f, ColorParser.INSTANCE));
                                                }
                                            } else {
                                                str5 = str6;
                                                j3 = j5;
                                                animatableColorValue = new AnimatableColorValue(KeyframesParser.parse(jsonUtf8Reader, lottieComposition, 1.0f, ColorParser.INSTANCE));
                                            }
                                            str6 = str5;
                                            j5 = j3;
                                        }
                                        jsonUtf8Reader.endObject();
                                        animatableTextProperties2 = new AnimatableTextProperties(animatableColorValue, animatableColorValue2, animatableFloatValue2, animatableFloatValue3);
                                        str6 = str6;
                                        j5 = j5;
                                    }
                                }
                                str4 = str6;
                                j2 = j5;
                                jsonUtf8Reader.endObject();
                                if (animatableTextProperties2 == null) {
                                    animatableTextProperties = new AnimatableTextProperties(null, null, null, null);
                                } else {
                                    animatableTextProperties = animatableTextProperties2;
                                }
                            } else {
                                str4 = str6;
                                j2 = j5;
                            }
                            while (jsonUtf8Reader.hasNext()) {
                                jsonUtf8Reader.skipValue();
                            }
                            jsonUtf8Reader.endArray();
                        }
                        str6 = str4;
                        j5 = j2;
                        i6 = 1;
                    }
                    str2 = str6;
                    j = j5;
                    jsonUtf8Reader.endObject();
                    str6 = str2;
                    j5 = j;
                    break;
                case QS.VERSION /* 13 */:
                    jsonUtf8Reader.beginArray();
                    ArrayList arrayList5 = new ArrayList();
                    while (jsonUtf8Reader.hasNext()) {
                        jsonUtf8Reader.beginObject();
                        while (jsonUtf8Reader.hasNext()) {
                            if (jsonUtf8Reader2.selectName(EFFECTS_NAMES) != 0) {
                                jsonUtf8Reader.skipName();
                                jsonUtf8Reader.skipValue();
                            } else {
                                arrayList5.add(jsonUtf8Reader.nextString());
                            }
                        }
                        jsonUtf8Reader.endObject();
                    }
                    jsonUtf8Reader.endArray();
                    lottieComposition.addWarning("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: " + arrayList5);
                    str2 = str6;
                    j = j5;
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
                case 14:
                    f = (float) jsonUtf8Reader.nextDouble();
                    str2 = str6;
                    j = j5;
                    str6 = str2;
                    j5 = j;
                    break;
                case 15:
                    f3 = (float) jsonUtf8Reader.nextDouble();
                    str2 = str6;
                    j = j5;
                    str6 = str2;
                    j5 = j;
                    break;
                case 16:
                    i4 = (int) (Utils.dpScale() * jsonUtf8Reader.nextInt());
                    str2 = str6;
                    j = j5;
                    str6 = str2;
                    j5 = j;
                    break;
                case 17:
                    i5 = (int) (Utils.dpScale() * jsonUtf8Reader.nextInt());
                    str2 = str6;
                    j = j5;
                    str6 = str2;
                    j5 = j;
                    break;
                case 18:
                    f2 = (float) jsonUtf8Reader.nextDouble();
                    str2 = str6;
                    j = j5;
                    str6 = str2;
                    j5 = j;
                    break;
                case 19:
                    f4 = (float) jsonUtf8Reader.nextDouble();
                    break;
                case 20:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonUtf8Reader2, lottieComposition, z);
                    break;
                case 21:
                    str6 = jsonUtf8Reader.nextString();
                    break;
                case 22:
                    z2 = jsonUtf8Reader.nextBoolean();
                    break;
                default:
                    str2 = str6;
                    j = j5;
                    jsonUtf8Reader.skipName();
                    jsonUtf8Reader.skipValue();
                    jsonUtf8Reader2 = jsonUtf8Reader;
                    str6 = str2;
                    j5 = j;
                    break;
            }
            z = false;
        }
        jsonUtf8Reader.endObject();
        float f5 = f2 / f;
        float f6 = f4 / f;
        ArrayList arrayList6 = new ArrayList();
        if (f5 > 0.0f) {
            arrayList = arrayList4;
            arrayList2 = arrayList3;
            str = str6;
            arrayList6.add(new Keyframe(lottieComposition, valueOf2, valueOf2, null, 0.0f, Float.valueOf(f5)));
        } else {
            arrayList = arrayList4;
            arrayList2 = arrayList3;
            str = str6;
        }
        if (f6 <= 0.0f) {
            f6 = lottieComposition.endFrame;
        }
        arrayList6.add(new Keyframe(lottieComposition, valueOf, valueOf, null, f5, Float.valueOf(f6)));
        arrayList6.add(new Keyframe(lottieComposition, valueOf2, valueOf2, null, f6, Float.valueOf(Float.MAX_VALUE)));
        if (str8.endsWith(".ai") || "ai".equals(str)) {
            lottieComposition.addWarning("Convert your Illustrator layers to shape layers.");
        }
        return new Layer(arrayList, lottieComposition, str8, j5, layerType, j4, str7, arrayList2, animatableTransform, i, i2, i3, f, f3, i4, i5, animatableTextFrame, animatableTextProperties, arrayList6, matteType2, animatableFloatValue, z2);
    }
}
