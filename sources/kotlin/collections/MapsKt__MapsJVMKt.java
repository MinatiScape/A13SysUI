package kotlin.collections;

import java.util.Map;
import kotlin.jvm.functions.Function1;
/* compiled from: MapsJVM.kt */
/* loaded from: classes.dex */
public class MapsKt__MapsJVMKt {
    public static final int mapCapacity(int i) {
        if (i < 0) {
            return i;
        }
        if (i < 3) {
            return i + 1;
        }
        if (i < 1073741824) {
            return (int) ((i / 0.75f) + 1.0f);
        }
        return Integer.MAX_VALUE;
    }

    public static final Map withDefault(Map map, Function1 function1) {
        if (map instanceof MapWithDefault) {
            return withDefault(((MapWithDefault) map).getMap(), function1);
        }
        return new MapWithDefaultImpl(map, function1);
    }
}
