package androidx.lifecycle.runtime;

import java.util.LinkedHashMap;
import kotlinx.coroutines.internal.Symbol;
/* loaded from: classes.dex */
public final class R$id {
    public static final Symbol RESUME_TOKEN = new Symbol("RESUME_TOKEN");

    public static LinkedHashMap newLinkedHashMapWithExpectedSize(int i) {
        int i2;
        if (i < 3) {
            i2 = i + 1;
        } else if (i < 1073741824) {
            i2 = (int) ((i / 0.75f) + 1.0f);
        } else {
            i2 = Integer.MAX_VALUE;
        }
        return new LinkedHashMap(i2);
    }
}
