package com.android.systemui.qs;

import android.content.Context;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface QSHost {

    /* loaded from: classes.dex */
    public interface Callback {
        void onTilesChanged();
    }

    void collapsePanels();

    Context getContext();

    InstanceId getNewInstanceId();

    UiEventLogger getUiEventLogger();

    Context getUserContext();

    int getUserId();

    int indexOf(String str);

    void openPanels();

    void removeTile(String str);

    void removeTiles(ArrayList arrayList);

    void unmarkTileAsAutoAdded(String str);

    void warn();
}
