package com.android.systemui.communal.dagger;

import com.android.systemui.communal.CommunalHostView;
import com.android.systemui.communal.CommunalHostViewController;
/* loaded from: classes.dex */
public interface CommunalViewComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        CommunalViewComponent build(CommunalHostView communalHostView);
    }

    CommunalHostViewController getCommunalHostViewController();
}
