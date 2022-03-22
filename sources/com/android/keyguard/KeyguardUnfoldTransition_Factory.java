package com.android.keyguard;

import android.content.Context;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.ZigZagClassifier;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardUnfoldTransition_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider unfoldProgressProvider;

    public /* synthetic */ KeyguardUnfoldTransition_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.unfoldProgressProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new KeyguardUnfoldTransition((Context) this.contextProvider.mo144get(), (NaturalRotationUnfoldProgressProvider) this.unfoldProgressProvider.mo144get());
            default:
                return new ZigZagClassifier((FalsingDataProvider) this.contextProvider.mo144get(), (DeviceConfigProxy) this.unfoldProgressProvider.mo144get());
        }
    }
}
