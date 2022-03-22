package com.android.wm.shell.compatui;

import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CompatUIController$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ CompatUIController$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = this.f$0;
        CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = (CompatUIWindowManagerAbstract) obj;
        Objects.requireNonNull(compatUIWindowManagerAbstract);
        if (compatUIWindowManagerAbstract.mDisplayId == i) {
            return true;
        }
        return false;
    }
}
