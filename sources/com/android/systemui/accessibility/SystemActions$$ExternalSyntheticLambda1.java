package com.android.systemui.accessibility;

import com.android.systemui.recents.Recents;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SystemActions$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ SystemActions$$ExternalSyntheticLambda1 INSTANCE = new SystemActions$$ExternalSyntheticLambda1();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((Recents) obj).toggleRecentApps();
    }
}
