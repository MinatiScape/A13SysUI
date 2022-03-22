package com.android.systemui;

import java.util.ArrayList;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DejankUtils$$ExternalSyntheticLambda0 implements Runnable {
    public static final /* synthetic */ DejankUtils$$ExternalSyntheticLambda0 INSTANCE = new DejankUtils$$ExternalSyntheticLambda0();

    @Override // java.lang.Runnable
    public final void run() {
        int i = 0;
        while (true) {
            ArrayList<Runnable> arrayList = DejankUtils.sPendingRunnables;
            if (i < arrayList.size()) {
                DejankUtils.sHandler.post(arrayList.get(i));
                i++;
            } else {
                arrayList.clear();
                return;
            }
        }
    }
}
