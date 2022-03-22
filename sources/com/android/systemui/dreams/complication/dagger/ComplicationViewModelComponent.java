package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;
/* loaded from: classes.dex */
public interface ComplicationViewModelComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId);
    }

    ComplicationViewModelProvider getViewModelProvider();
}
