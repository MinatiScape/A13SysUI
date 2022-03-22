package com.android.systemui.media;

import android.app.smartspace.SmartspaceTarget;
import android.util.Log;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceMediaDataProvider.kt */
/* loaded from: classes.dex */
public final class SmartspaceMediaDataProvider implements BcSmartspaceDataPlugin {
    public final ArrayList smartspaceMediaTargetListeners = new ArrayList();
    public List<SmartspaceTarget> smartspaceMediaTargets = EmptyList.INSTANCE;

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void onTargetsAvailable(List<SmartspaceTarget> list) {
        ArrayList arrayList = new ArrayList();
        for (SmartspaceTarget smartspaceTarget : list) {
            if (smartspaceTarget.getFeatureType() == 15) {
                arrayList.add(smartspaceTarget);
            }
        }
        if (!arrayList.isEmpty()) {
            Log.d("SsMediaDataProvider", Intrinsics.stringPlus("Forwarding Smartspace media updates ", arrayList));
        }
        this.smartspaceMediaTargets = arrayList;
        Iterator it = this.smartspaceMediaTargetListeners.iterator();
        while (it.hasNext()) {
            ((BcSmartspaceDataPlugin.SmartspaceTargetListener) it.next()).onSmartspaceTargetsUpdated(this.smartspaceMediaTargets);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void registerListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.smartspaceMediaTargetListeners.add(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void unregisterListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.smartspaceMediaTargetListeners.remove(smartspaceTargetListener);
    }
}
