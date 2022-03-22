package com.android.settingslib.deviceinfo;

import android.content.IntentFilter;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public abstract class AbstractConnectivityPreferenceController extends AbstractPreferenceController implements LifecycleObserver, OnStart, OnStop {
    public abstract String[] getConnectivityIntents();

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public final void onStart() {
        IntentFilter intentFilter = new IntentFilter();
        for (String str : getConnectivityIntents()) {
            intentFilter.addAction(str);
        }
        throw null;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public final void onStop() {
        throw null;
    }
}
