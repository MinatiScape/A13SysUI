package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;
/* loaded from: classes.dex */
public abstract class Violation extends RuntimeException {
    public final Fragment mFragment;

    public Violation(Fragment fragment) {
        this.mFragment = fragment;
    }
}
