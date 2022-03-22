package androidx.leanback.widget;

import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class ParallaxEffect {
    public final ArrayList mMarkerValues = new ArrayList(2);
    public final ArrayList mTargets = new ArrayList(4);

    public abstract float calculateFraction();

    public ParallaxEffect() {
        new ArrayList(2);
        new ArrayList(2);
    }
}
