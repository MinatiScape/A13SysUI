package androidx.slice;

import android.net.Uri;
/* loaded from: classes.dex */
public abstract class SliceViewManager {

    /* loaded from: classes.dex */
    public interface SliceCallback {
    }

    public abstract void pinSlice(Uri uri);

    public abstract void unpinSlice(Uri uri);
}
