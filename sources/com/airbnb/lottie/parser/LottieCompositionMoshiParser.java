package com.airbnb.lottie.parser;

import android.graphics.Rect;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonUtf8Reader;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import com.android.systemui.plugins.FalsingManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class LottieCompositionMoshiParser {
    public static final JsonReader.Options NAMES = JsonReader.Options.of("w", "h", "ip", "op", "fr", "v", "layers", "assets", "fonts", "chars", "markers");
    public static JsonReader.Options ASSETS_NAMES = JsonReader.Options.of("id", "layers", "w", "h", "p", "u");
    public static final JsonReader.Options FONT_NAMES = JsonReader.Options.of("list");
    public static final JsonReader.Options MARKER_NAMES = JsonReader.Options.of("cm", "tm", "dr");

    public static LottieComposition parse(JsonUtf8Reader jsonUtf8Reader) throws IOException {
        float f;
        HashMap hashMap;
        ArrayList arrayList;
        SparseArrayCompat<FontCharacter> sparseArrayCompat;
        float f2;
        float f3;
        LongSparseArray<Layer> longSparseArray;
        float f4;
        HashMap hashMap2;
        ArrayList arrayList2;
        SparseArrayCompat<FontCharacter> sparseArrayCompat2;
        int i;
        LongSparseArray<Layer> longSparseArray2;
        float f5;
        float f6;
        boolean z;
        int i2;
        float f7;
        LongSparseArray<Layer> longSparseArray3;
        float f8;
        float f9;
        float dpScale = Utils.dpScale();
        LongSparseArray<Layer> longSparseArray4 = new LongSparseArray<>();
        ArrayList arrayList3 = new ArrayList();
        HashMap hashMap3 = new HashMap();
        HashMap hashMap4 = new HashMap();
        HashMap hashMap5 = new HashMap();
        ArrayList arrayList4 = new ArrayList();
        SparseArrayCompat<FontCharacter> sparseArrayCompat3 = new SparseArrayCompat<>();
        LottieComposition lottieComposition = new LottieComposition();
        jsonUtf8Reader.beginObject();
        int i3 = 0;
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        int i4 = 0;
        while (jsonUtf8Reader.hasNext()) {
            switch (jsonUtf8Reader.selectName(NAMES)) {
                case 0:
                    f = dpScale;
                    hashMap = hashMap5;
                    arrayList = arrayList4;
                    sparseArrayCompat = sparseArrayCompat3;
                    f2 = f11;
                    f3 = f12;
                    longSparseArray = longSparseArray4;
                    i4 = jsonUtf8Reader.nextInt();
                    longSparseArray4 = longSparseArray;
                    f10 = f10;
                    dpScale = f;
                    f11 = f2;
                    arrayList4 = arrayList;
                    f12 = f3;
                    hashMap5 = hashMap;
                    sparseArrayCompat3 = sparseArrayCompat;
                case 1:
                    f = dpScale;
                    hashMap = hashMap5;
                    arrayList = arrayList4;
                    sparseArrayCompat = sparseArrayCompat3;
                    f2 = f11;
                    f3 = f12;
                    longSparseArray = longSparseArray4;
                    i3 = jsonUtf8Reader.nextInt();
                    longSparseArray4 = longSparseArray;
                    f10 = f10;
                    dpScale = f;
                    f11 = f2;
                    arrayList4 = arrayList;
                    f12 = f3;
                    hashMap5 = hashMap;
                    sparseArrayCompat3 = sparseArrayCompat;
                case 2:
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f11 = (float) jsonUtf8Reader.nextDouble();
                    f10 = f10;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 3:
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f12 = ((float) jsonUtf8Reader.nextDouble()) - 0.01f;
                    f10 = f10;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 4:
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f10 = (float) jsonUtf8Reader.nextDouble();
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 5:
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    f5 = f11;
                    f6 = f12;
                    longSparseArray2 = longSparseArray4;
                    String[] split = jsonUtf8Reader.nextString().split("\\.");
                    int parseInt = Integer.parseInt(split[0]);
                    int parseInt2 = Integer.parseInt(split[1]);
                    int parseInt3 = Integer.parseInt(split[2]);
                    if (parseInt >= 4 && (parseInt > 4 || (parseInt2 >= 4 && (parseInt2 > 4 || parseInt3 >= 0)))) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        lottieComposition.addWarning("Lottie only supports bodymovin >= 4.4.0");
                    }
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case FalsingManager.VERSION /* 6 */:
                    f4 = dpScale;
                    LongSparseArray<Layer> longSparseArray5 = longSparseArray4;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    f5 = f11;
                    f6 = f12;
                    jsonUtf8Reader.beginArray();
                    int i5 = 0;
                    while (jsonUtf8Reader.hasNext()) {
                        Layer parse = LayerParser.parse(jsonUtf8Reader, lottieComposition);
                        if (parse.layerType == Layer.LayerType.IMAGE) {
                            i5++;
                        }
                        arrayList3.add(parse);
                        longSparseArray5.put(parse.layerId, parse);
                        if (i5 > 4) {
                            Logger.warning("You have " + i5 + " images. Lottie should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
                        }
                        longSparseArray5 = longSparseArray5;
                    }
                    longSparseArray2 = longSparseArray5;
                    jsonUtf8Reader.endArray();
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 7:
                    arrayList2 = arrayList4;
                    f5 = f11;
                    f6 = f12;
                    jsonUtf8Reader.beginArray();
                    while (jsonUtf8Reader.hasNext()) {
                        ArrayList arrayList5 = new ArrayList();
                        LongSparseArray longSparseArray6 = new LongSparseArray();
                        jsonUtf8Reader.beginObject();
                        String str = null;
                        String str2 = null;
                        int i6 = 0;
                        int i7 = 0;
                        while (jsonUtf8Reader.hasNext()) {
                            int selectName = jsonUtf8Reader.selectName(ASSETS_NAMES);
                            if (selectName != 0) {
                                i2 = i3;
                                if (selectName == 1) {
                                    jsonUtf8Reader.beginArray();
                                    while (jsonUtf8Reader.hasNext()) {
                                        Layer parse2 = LayerParser.parse(jsonUtf8Reader, lottieComposition);
                                        longSparseArray6.put(parse2.layerId, parse2);
                                        arrayList5.add(parse2);
                                        dpScale = dpScale;
                                        longSparseArray4 = longSparseArray4;
                                    }
                                    f7 = dpScale;
                                    longSparseArray3 = longSparseArray4;
                                    jsonUtf8Reader.endArray();
                                } else if (selectName == 2) {
                                    i6 = jsonUtf8Reader.nextInt();
                                } else if (selectName == 3) {
                                    i7 = jsonUtf8Reader.nextInt();
                                } else if (selectName == 4) {
                                    str2 = jsonUtf8Reader.nextString();
                                } else if (selectName != 5) {
                                    jsonUtf8Reader.skipName();
                                    jsonUtf8Reader.skipValue();
                                    f7 = dpScale;
                                    longSparseArray3 = longSparseArray4;
                                } else {
                                    jsonUtf8Reader.nextString();
                                }
                                dpScale = f7;
                                sparseArrayCompat3 = sparseArrayCompat3;
                                i3 = i2;
                                longSparseArray4 = longSparseArray3;
                            } else {
                                i2 = i3;
                                str = jsonUtf8Reader.nextString();
                            }
                            sparseArrayCompat3 = sparseArrayCompat3;
                            i3 = i2;
                        }
                        jsonUtf8Reader.endObject();
                        if (str2 != null) {
                            hashMap4.put(str, new LottieImageAsset(i6, i7, str, str2));
                        } else {
                            hashMap3.put(str, arrayList5);
                        }
                        dpScale = dpScale;
                        hashMap5 = hashMap5;
                        sparseArrayCompat3 = sparseArrayCompat3;
                        i3 = i3;
                        longSparseArray4 = longSparseArray4;
                    }
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    jsonUtf8Reader.endArray();
                    longSparseArray2 = longSparseArray4;
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 8:
                    f5 = f11;
                    f6 = f12;
                    jsonUtf8Reader.beginObject();
                    while (jsonUtf8Reader.hasNext()) {
                        if (jsonUtf8Reader.selectName(FONT_NAMES) != 0) {
                            jsonUtf8Reader.skipName();
                            jsonUtf8Reader.skipValue();
                        } else {
                            jsonUtf8Reader.beginArray();
                            while (jsonUtf8Reader.hasNext()) {
                                JsonReader.Options options = FontParser.NAMES;
                                jsonUtf8Reader.beginObject();
                                String str3 = null;
                                String str4 = null;
                                String str5 = null;
                                while (jsonUtf8Reader.hasNext()) {
                                    int selectName2 = jsonUtf8Reader.selectName(FontParser.NAMES);
                                    if (selectName2 != 0) {
                                        if (selectName2 == 1) {
                                            str4 = jsonUtf8Reader.nextString();
                                        } else if (selectName2 == 2) {
                                            str5 = jsonUtf8Reader.nextString();
                                        } else if (selectName2 != 3) {
                                            jsonUtf8Reader.skipName();
                                            jsonUtf8Reader.skipValue();
                                        } else {
                                            jsonUtf8Reader.nextDouble();
                                        }
                                        arrayList4 = arrayList4;
                                    } else {
                                        str3 = jsonUtf8Reader.nextString();
                                    }
                                }
                                jsonUtf8Reader.endObject();
                                hashMap5.put(str4, new Font(str3, str4, str5));
                                arrayList4 = arrayList4;
                            }
                            jsonUtf8Reader.endArray();
                        }
                    }
                    arrayList2 = arrayList4;
                    jsonUtf8Reader.endObject();
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 9:
                    f5 = f11;
                    f6 = f12;
                    jsonUtf8Reader.beginArray();
                    while (jsonUtf8Reader.hasNext()) {
                        JsonReader.Options options2 = FontCharacterParser.NAMES;
                        ArrayList arrayList6 = new ArrayList();
                        jsonUtf8Reader.beginObject();
                        double d = 0.0d;
                        String str6 = null;
                        String str7 = null;
                        char c = 0;
                        while (jsonUtf8Reader.hasNext()) {
                            int selectName3 = jsonUtf8Reader.selectName(FontCharacterParser.NAMES);
                            if (selectName3 == 0) {
                                c = jsonUtf8Reader.nextString().charAt(0);
                            } else if (selectName3 == 1) {
                                jsonUtf8Reader.nextDouble();
                            } else if (selectName3 == 2) {
                                d = jsonUtf8Reader.nextDouble();
                            } else if (selectName3 == 3) {
                                str6 = jsonUtf8Reader.nextString();
                            } else if (selectName3 == 4) {
                                str7 = jsonUtf8Reader.nextString();
                            } else if (selectName3 != 5) {
                                jsonUtf8Reader.skipName();
                                jsonUtf8Reader.skipValue();
                            } else {
                                jsonUtf8Reader.beginObject();
                                while (jsonUtf8Reader.hasNext()) {
                                    if (jsonUtf8Reader.selectName(FontCharacterParser.DATA_NAMES) != 0) {
                                        jsonUtf8Reader.skipName();
                                        jsonUtf8Reader.skipValue();
                                    } else {
                                        jsonUtf8Reader.beginArray();
                                        while (jsonUtf8Reader.hasNext()) {
                                            arrayList6.add((ShapeGroup) ContentModelParser.parse(jsonUtf8Reader, lottieComposition));
                                        }
                                        jsonUtf8Reader.endArray();
                                    }
                                }
                                jsonUtf8Reader.endObject();
                            }
                        }
                        jsonUtf8Reader.endObject();
                        FontCharacter fontCharacter = new FontCharacter(arrayList6, c, d, str6, str7);
                        sparseArrayCompat3.put(fontCharacter.hashCode(), fontCharacter);
                    }
                    jsonUtf8Reader.endArray();
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                case 10:
                    jsonUtf8Reader.beginArray();
                    while (jsonUtf8Reader.hasNext()) {
                        jsonUtf8Reader.beginObject();
                        String str8 = null;
                        float f13 = 0.0f;
                        float f14 = 0.0f;
                        while (jsonUtf8Reader.hasNext()) {
                            int selectName4 = jsonUtf8Reader.selectName(MARKER_NAMES);
                            if (selectName4 != 0) {
                                f8 = f12;
                                if (selectName4 == 1) {
                                    f9 = f11;
                                    f13 = (float) jsonUtf8Reader.nextDouble();
                                } else if (selectName4 != 2) {
                                    jsonUtf8Reader.skipName();
                                    jsonUtf8Reader.skipValue();
                                } else {
                                    f9 = f11;
                                    f14 = (float) jsonUtf8Reader.nextDouble();
                                }
                                f11 = f9;
                            } else {
                                f8 = f12;
                                str8 = jsonUtf8Reader.nextString();
                            }
                            f12 = f8;
                        }
                        jsonUtf8Reader.endObject();
                        arrayList4.add(new Marker(str8, f13, f14));
                        f11 = f11;
                        f12 = f12;
                    }
                    f5 = f11;
                    f6 = f12;
                    jsonUtf8Reader.endArray();
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    longSparseArray2 = longSparseArray4;
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
                default:
                    f4 = dpScale;
                    hashMap2 = hashMap5;
                    arrayList2 = arrayList4;
                    sparseArrayCompat2 = sparseArrayCompat3;
                    i = i3;
                    f5 = f11;
                    f6 = f12;
                    longSparseArray2 = longSparseArray4;
                    jsonUtf8Reader.skipName();
                    jsonUtf8Reader.skipValue();
                    f10 = f10;
                    f11 = f5;
                    f12 = f6;
                    longSparseArray4 = longSparseArray2;
                    dpScale = f4;
                    arrayList4 = arrayList2;
                    hashMap5 = hashMap2;
                    sparseArrayCompat3 = sparseArrayCompat2;
                    i3 = i;
            }
        }
        lottieComposition.bounds = new Rect(0, 0, (int) (i4 * dpScale), (int) (i3 * dpScale));
        lottieComposition.startFrame = f11;
        lottieComposition.endFrame = f12;
        lottieComposition.frameRate = f10;
        lottieComposition.layers = arrayList3;
        lottieComposition.layerMap = longSparseArray4;
        lottieComposition.precomps = hashMap3;
        lottieComposition.images = hashMap4;
        lottieComposition.characters = sparseArrayCompat3;
        lottieComposition.fonts = hashMap5;
        lottieComposition.markers = arrayList4;
        return lottieComposition;
    }
}
