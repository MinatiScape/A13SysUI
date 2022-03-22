package com.android.keyguard;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0 implements Runnable {
    public static final /* synthetic */ KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0 INSTANCE = new KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0();

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException unused) {
        }
        System.gc();
        System.runFinalization();
        System.gc();
    }
}
