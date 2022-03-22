package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: classes.dex */
public abstract class Key {
    public HashMap<String, ConstraintAttribute> mCustomConstraints;
    public int mFramePosition = -1;
    public int mTargetId = -1;
    public String mTargetString = null;

    public abstract void addValues(HashMap<String, SplineSet> hashMap);

    public abstract void getAttributeNames(HashSet<String> hashSet);

    public abstract void load(Context context, AttributeSet attributeSet);

    public void setInterpolation(HashMap<String, Integer> hashMap) {
    }
}
