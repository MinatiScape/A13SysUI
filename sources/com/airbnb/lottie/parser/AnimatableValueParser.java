package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonUtf8Reader;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
/* loaded from: classes.dex */
public final class AnimatableValueParser {
    public static AnimatableFloatValue parseFloat(JsonReader jsonReader, LottieComposition lottieComposition, boolean z) throws IOException {
        float f;
        if (z) {
            f = Utils.dpScale();
        } else {
            f = 1.0f;
        }
        return new AnimatableFloatValue(KeyframesParser.parse(jsonReader, lottieComposition, f, FloatParser.INSTANCE));
    }

    public static AnimatableIntegerValue parseInteger(JsonUtf8Reader jsonUtf8Reader, LottieComposition lottieComposition) throws IOException {
        return new AnimatableIntegerValue(KeyframesParser.parse(jsonUtf8Reader, lottieComposition, 1.0f, IntegerParser.INSTANCE));
    }

    public static AnimatablePointValue parsePoint(JsonUtf8Reader jsonUtf8Reader, LottieComposition lottieComposition) throws IOException {
        return new AnimatablePointValue(KeyframesParser.parse(jsonUtf8Reader, lottieComposition, Utils.dpScale(), PointFParser.INSTANCE));
    }
}
