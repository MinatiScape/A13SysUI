package com.android.systemui.util.condition.dagger;

import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.condition.Monitor;
import java.util.Set;
/* loaded from: classes.dex */
public interface MonitorComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        MonitorComponent create(Set<Condition> set, Set<Monitor.Callback> set2);
    }

    Monitor getMonitor();
}
