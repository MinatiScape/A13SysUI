package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.Map;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsControllerImpl$replaceFavoritesForStructure$1 implements Runnable {
    public final /* synthetic */ StructureInfo $structureInfo;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$replaceFavoritesForStructure$1(StructureInfo structureInfo, ControlsControllerImpl controlsControllerImpl) {
        this.$structureInfo = structureInfo;
        this.this$0 = controlsControllerImpl;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Map<ComponentName, ? extends List<StructureInfo>> map = Favorites.favMap;
        Favorites.replaceControls(this.$structureInfo);
        this.this$0.persistenceWrapper.storeFavorites(Favorites.getAllStructures());
    }
}
