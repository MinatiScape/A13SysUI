package com.android.systemui.dreams.complication;

import android.view.View;
/* loaded from: classes.dex */
public interface Complication {

    /* loaded from: classes.dex */
    public interface Host {
    }

    /* loaded from: classes.dex */
    public interface ViewHolder {
        ComplicationLayoutParams getLayoutParams();

        View getView();
    }

    ViewHolder createView(ComplicationViewModel complicationViewModel);

    default int getRequiredTypeAvailability() {
        return 0;
    }
}
