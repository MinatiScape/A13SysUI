package com.android.wm.shell.splitscreen;

import android.view.RemoteAnimationTarget;
import java.util.Comparator;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SplitScreenController$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ SplitScreenController$$ExternalSyntheticLambda0 INSTANCE = new SplitScreenController$$ExternalSyntheticLambda0();

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((RemoteAnimationTarget) obj).prefixOrderIndex - ((RemoteAnimationTarget) obj2).prefixOrderIndex;
    }
}
