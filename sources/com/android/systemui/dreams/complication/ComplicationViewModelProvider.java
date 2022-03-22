package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;
import com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public final class ComplicationViewModelProvider extends ViewModelProvider {
    public ComplicationViewModelProvider(ViewModelStore viewModelStore, ComplicationViewModel complicationViewModel) {
        super(viewModelStore, new DaggerViewModelProviderFactory(new NgaUiController$$ExternalSyntheticLambda1(complicationViewModel)));
    }
}
