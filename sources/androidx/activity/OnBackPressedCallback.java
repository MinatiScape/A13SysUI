package androidx.activity;

import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public abstract class OnBackPressedCallback {
    public CopyOnWriteArrayList<Cancellable> mCancellables = new CopyOnWriteArrayList<>();
    public boolean mEnabled = false;

    public abstract void handleOnBackPressed();
}
