package com.android.systemui.dreams.complication;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
/* loaded from: classes.dex */
public final class ComplicationCollectionViewModel extends ViewModel {
    public final MediatorLiveData mComplications;
    public final ComplicationViewModelTransformer mTransformer;

    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.lifecycle.Transformations$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ComplicationCollectionViewModel(com.android.systemui.dreams.complication.ComplicationCollectionLiveData r6, com.android.systemui.dreams.complication.ComplicationViewModelTransformer r7) {
        /*
            r5 = this;
            r5.<init>()
            com.android.systemui.dreams.complication.ComplicationCollectionViewModel$$ExternalSyntheticLambda0 r0 = new com.android.systemui.dreams.complication.ComplicationCollectionViewModel$$ExternalSyntheticLambda0
            r0.<init>(r5)
            androidx.lifecycle.MediatorLiveData r1 = new androidx.lifecycle.MediatorLiveData
            r1.<init>()
            androidx.lifecycle.Transformations$1 r2 = new androidx.lifecycle.Transformations$1
            r2.<init>()
            androidx.lifecycle.MediatorLiveData$Source r0 = new androidx.lifecycle.MediatorLiveData$Source
            r0.<init>(r6, r2)
            androidx.arch.core.internal.SafeIterableMap<androidx.lifecycle.LiveData<?>, androidx.lifecycle.MediatorLiveData$Source<?>> r3 = r1.mSources
            java.lang.Object r3 = r3.putIfAbsent(r6, r0)
            androidx.lifecycle.MediatorLiveData$Source r3 = (androidx.lifecycle.MediatorLiveData.Source) r3
            if (r3 == 0) goto L_0x002e
            androidx.lifecycle.Observer<? super V> r4 = r3.mObserver
            if (r4 != r2) goto L_0x0026
            goto L_0x002e
        L_0x0026:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.String r6 = "This source was already added with the different observer"
            r5.<init>(r6)
            throw r5
        L_0x002e:
            if (r3 == 0) goto L_0x0031
            goto L_0x003d
        L_0x0031:
            int r2 = r1.mActiveCount
            if (r2 <= 0) goto L_0x0037
            r2 = 1
            goto L_0x0038
        L_0x0037:
            r2 = 0
        L_0x0038:
            if (r2 == 0) goto L_0x003d
            r6.observeForever(r0)
        L_0x003d:
            r5.mComplications = r1
            r5.mTransformer = r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dreams.complication.ComplicationCollectionViewModel.<init>(com.android.systemui.dreams.complication.ComplicationCollectionLiveData, com.android.systemui.dreams.complication.ComplicationViewModelTransformer):void");
    }
}
