package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
/* loaded from: classes.dex */
public final class DaggerViewModelProviderFactory implements ViewModelProvider.Factory {
    public final ViewModelCreator mCreator;

    /* loaded from: classes.dex */
    public interface ViewModelCreator {
        ViewModel create();
    }

    @Override // androidx.lifecycle.ViewModelProvider.Factory
    public final ViewModel create() {
        return this.mCreator.create();
    }

    public DaggerViewModelProviderFactory(ViewModelCreator viewModelCreator) {
        this.mCreator = viewModelCreator;
    }
}
