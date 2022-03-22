package com.android.systemui.keyguard;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.keyguard.KeyguardUpdateMonitor;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ KeyguardViewMediator f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda5(KeyguardViewMediator keyguardViewMediator, boolean z) {
        this.f$0 = keyguardViewMediator;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        KeyguardViewMediator keyguardViewMediator = this.f$0;
        boolean z = this.f$1;
        boolean z2 = KeyguardViewMediator.DEBUG;
        Objects.requireNonNull(keyguardViewMediator);
        int size = keyguardViewMediator.mKeyguardStateCallbacks.size();
        while (true) {
            size--;
            if (size >= 0) {
                IKeyguardStateCallback iKeyguardStateCallback = keyguardViewMediator.mKeyguardStateCallbacks.get(size);
                try {
                    iKeyguardStateCallback.onShowingStateChanged(z, KeyguardUpdateMonitor.getCurrentUser());
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onShowingStateChanged", e);
                    if (e instanceof DeadObjectException) {
                        keyguardViewMediator.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                    }
                }
            } else {
                return;
            }
        }
    }
}
