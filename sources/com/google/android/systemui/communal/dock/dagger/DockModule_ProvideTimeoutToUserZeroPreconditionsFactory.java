package com.google.android.systemui.communal.dock.dagger;

import com.android.systemui.util.condition.Condition;
import com.google.android.systemui.communal.dock.conditions.TimeoutToUserZeroFeatureCondition;
import com.google.android.systemui.communal.dock.conditions.TimeoutToUserZeroSettingCondition;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DockModule_ProvideTimeoutToUserZeroPreconditionsFactory implements Factory<Set<Condition>> {
    public final Provider<TimeoutToUserZeroFeatureCondition> featureEnabledConditionProvider;
    public final Provider<TimeoutToUserZeroSettingCondition> settingsEnabledConditionProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.featureEnabledConditionProvider.mo144get(), this.settingsEnabledConditionProvider.mo144get()));
    }

    public DockModule_ProvideTimeoutToUserZeroPreconditionsFactory(Provider<TimeoutToUserZeroFeatureCondition> provider, Provider<TimeoutToUserZeroSettingCondition> provider2) {
        this.featureEnabledConditionProvider = provider;
        this.settingsEnabledConditionProvider = provider2;
    }
}
