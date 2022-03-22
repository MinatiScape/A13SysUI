package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationHostViewController;
/* loaded from: classes.dex */
public interface ComplicationHostViewComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        ComplicationHostViewComponent create();
    }

    ComplicationHostViewController getController();
}
