package kotlinx.coroutines.internal;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
/* compiled from: Symbol.kt */
/* loaded from: classes.dex */
public final class Symbol {
    public final String symbol;

    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('<');
        m.append(this.symbol);
        m.append('>');
        return m.toString();
    }

    public Symbol(String str) {
        this.symbol = str;
    }
}
