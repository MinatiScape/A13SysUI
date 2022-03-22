package androidx.recyclerview.widget;
/* loaded from: classes.dex */
public final class OpReorderer {
    public final Callback mCallback;

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public OpReorderer(Callback callback) {
        this.mCallback = callback;
    }
}
