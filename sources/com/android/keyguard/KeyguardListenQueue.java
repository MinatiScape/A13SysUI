package com.android.keyguard;

import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt___CollectionsKt;
/* compiled from: KeyguardListenQueue.kt */
/* loaded from: classes.dex */
public final class KeyguardListenQueue {
    public final int sizePerModality = 20;
    public final ArrayDeque<KeyguardFaceListenModel> faceQueue = new ArrayDeque<>();
    public final ArrayDeque<KeyguardFingerprintListenModel> fingerprintQueue = new ArrayDeque<>();
    public final ArrayDeque<KeyguardActiveUnlockModel> activeUnlockQueue = new ArrayDeque<>();

    public final void add(KeyguardListenModel keyguardListenModel) {
        ArrayDeque arrayDeque;
        if (keyguardListenModel instanceof KeyguardFaceListenModel) {
            arrayDeque = this.faceQueue;
            Objects.requireNonNull(arrayDeque);
            arrayDeque.addLast(keyguardListenModel);
        } else if (keyguardListenModel instanceof KeyguardFingerprintListenModel) {
            arrayDeque = this.fingerprintQueue;
            Objects.requireNonNull(arrayDeque);
            arrayDeque.addLast(keyguardListenModel);
        } else if (keyguardListenModel instanceof KeyguardActiveUnlockModel) {
            arrayDeque = this.activeUnlockQueue;
            Objects.requireNonNull(arrayDeque);
            arrayDeque.addLast(keyguardListenModel);
        } else {
            throw new NoWhenBranchMatchedException();
        }
        if (arrayDeque.size > this.sizePerModality && !arrayDeque.isEmpty()) {
            arrayDeque.removeFirst();
        }
    }

    public final List<KeyguardListenModel> getModels() {
        return CollectionsKt___CollectionsKt.plus((List) CollectionsKt___CollectionsKt.plus((List) this.faceQueue, (List) this.fingerprintQueue), (List) this.activeUnlockQueue);
    }
}
