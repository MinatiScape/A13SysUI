package com.google.android.systemui.assist.uihints.input;

import com.google.android.systemui.assist.uihints.IconController;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class InputModule_ProvideTouchActionRegionsFactory implements Factory<Set<TouchActionRegion>> {
    public final Provider<IconController> iconControllerProvider;
    public final Provider<TranscriptionController> transcriptionControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.iconControllerProvider.mo144get(), this.transcriptionControllerProvider.mo144get()));
    }

    public InputModule_ProvideTouchActionRegionsFactory(Provider<IconController> provider, Provider<TranscriptionController> provider2) {
        this.iconControllerProvider = provider;
        this.transcriptionControllerProvider = provider2;
    }
}
