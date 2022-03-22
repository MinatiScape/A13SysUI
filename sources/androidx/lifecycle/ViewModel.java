package androidx.lifecycle;

import java.util.HashMap;
/* loaded from: classes.dex */
public abstract class ViewModel {
    public final HashMap mBagOfTags = new HashMap();

    public void onCleared() {
    }
}
