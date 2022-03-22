package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonUtf8Reader;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import com.android.systemui.qs.external.QSTileServiceWrapper;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class AnimatablePathValueParser {
    public static JsonReader.Options NAMES = JsonReader.Options.of("k", "x", "y");

    public static QSTileServiceWrapper parse(JsonUtf8Reader jsonUtf8Reader, LottieComposition lottieComposition) throws IOException {
        boolean z;
        ArrayList arrayList = new ArrayList();
        if (jsonUtf8Reader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            jsonUtf8Reader.beginArray();
            while (jsonUtf8Reader.hasNext()) {
                if (jsonUtf8Reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                    z = true;
                } else {
                    z = false;
                }
                arrayList.add(new PathKeyframe(lottieComposition, KeyframeParser.parse(jsonUtf8Reader, lottieComposition, Utils.dpScale(), PathParser.INSTANCE, z)));
            }
            jsonUtf8Reader.endArray();
            KeyframesParser.setEndFrames(arrayList);
        } else {
            arrayList.add(new Keyframe(JsonUtils.jsonToPoint(jsonUtf8Reader, Utils.dpScale())));
        }
        return new QSTileServiceWrapper(arrayList);
    }

    public static AnimatableValue parseSplitPath(JsonUtf8Reader jsonUtf8Reader, LottieComposition lottieComposition) throws IOException {
        JsonReader.Token token = JsonReader.Token.STRING;
        jsonUtf8Reader.beginObject();
        QSTileServiceWrapper qSTileServiceWrapper = null;
        AnimatableFloatValue animatableFloatValue = null;
        boolean z = false;
        AnimatableFloatValue animatableFloatValue2 = null;
        while (jsonUtf8Reader.peek() != JsonReader.Token.END_OBJECT) {
            int selectName = jsonUtf8Reader.selectName(NAMES);
            if (selectName == 0) {
                qSTileServiceWrapper = parse(jsonUtf8Reader, lottieComposition);
            } else if (selectName != 1) {
                if (selectName != 2) {
                    jsonUtf8Reader.skipName();
                    jsonUtf8Reader.skipValue();
                } else if (jsonUtf8Reader.peek() == token) {
                    jsonUtf8Reader.skipValue();
                    z = true;
                } else {
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonUtf8Reader, lottieComposition, true);
                }
            } else if (jsonUtf8Reader.peek() == token) {
                jsonUtf8Reader.skipValue();
                z = true;
            } else {
                animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonUtf8Reader, lottieComposition, true);
            }
        }
        jsonUtf8Reader.endObject();
        if (z) {
            lottieComposition.addWarning("Lottie doesn't support expressions.");
        }
        if (qSTileServiceWrapper != null) {
            return qSTileServiceWrapper;
        }
        return new AnimatableSplitDimensionPathValue(animatableFloatValue2, animatableFloatValue);
    }
}
