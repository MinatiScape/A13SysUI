package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class ComplicationViewModelTransformer {
    public final ComplicationId.Factory mComplicationIdFactory = new ComplicationId.Factory();
    public final HashMap<Complication, ComplicationId> mComplicationIdMapping = new HashMap<>();
    public final ComplicationViewModelComponent.Factory mViewModelComponentFactory;

    public ComplicationViewModelTransformer(ComplicationViewModelComponent.Factory factory) {
        this.mViewModelComponentFactory = factory;
    }
}
