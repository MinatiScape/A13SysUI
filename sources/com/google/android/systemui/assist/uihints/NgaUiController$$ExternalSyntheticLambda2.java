package com.google.android.systemui.assist.uihints;

import androidx.lifecycle.ViewModel;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NgaUiController$$ExternalSyntheticLambda2 implements DaggerViewModelProviderFactory.ViewModelCreator, EdgeLightsController.ModeChangeThrottler {
    public final /* synthetic */ Object f$0;

    @Override // com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory.ViewModelCreator
    public final ViewModel create() {
        return (ComplicationCollectionViewModel) this.f$0;
    }

    public /* synthetic */ NgaUiController$$ExternalSyntheticLambda2(Object obj) {
        this.f$0 = obj;
    }
}
