package com.android.systemui;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.wm.shell.transition.ShellTransitions;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LatencyTester_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider biometricUnlockControllerProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider contextProvider;

    public /* synthetic */ LatencyTester_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.biometricUnlockControllerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new LatencyTester((Context) this.contextProvider.mo144get(), (BiometricUnlockController) this.biometricUnlockControllerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get());
            case 1:
                AsyncSensorManager asyncSensorManager = (AsyncSensorManager) this.contextProvider.mo144get();
                Context context = (Context) this.biometricUnlockControllerProvider.mo144get();
                DozeParameters dozeParameters = (DozeParameters) this.broadcastDispatcherProvider.mo144get();
                Objects.requireNonNull(dozeParameters);
                String[] stringArray = dozeParameters.mResources.getStringArray(2130903107);
                if (stringArray.length == 0) {
                    return new Optional[]{Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(2131952299), null))};
                }
                Optional[] optionalArr = new Optional[5];
                Arrays.fill(optionalArr, Optional.empty());
                HashMap hashMap = new HashMap();
                for (int i = 0; i < stringArray.length; i++) {
                    String str = stringArray[i];
                    if (!hashMap.containsKey(str)) {
                        hashMap.put(str, Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(2131952299), stringArray[i])));
                    }
                    optionalArr[i] = (Optional) hashMap.get(str);
                }
                return optionalArr;
            case 2:
                return new KeyguardService((KeyguardViewMediator) this.contextProvider.mo144get(), (KeyguardLifecyclesDispatcher) this.biometricUnlockControllerProvider.mo144get(), (ShellTransitions) this.broadcastDispatcherProvider.mo144get());
            default:
                return new TvOngoingPrivacyChip((Context) this.contextProvider.mo144get(), (PrivacyItemController) this.biometricUnlockControllerProvider.mo144get(), (IWindowManager) this.broadcastDispatcherProvider.mo144get());
        }
    }
}
