package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
/* compiled from: ControlsController.kt */
/* loaded from: classes.dex */
public final class ControlsControllerKt$createLoadDataObject$1 implements ControlsController.LoadData {
    public final /* synthetic */ List<ControlStatus> $allControls;
    public final /* synthetic */ List<String> $favorites;
    public final List<ControlStatus> allControls;
    public final boolean errorOnLoad;
    public final List<String> favoritesIds;

    public ControlsControllerKt$createLoadDataObject$1(List<ControlStatus> list, List<String> list2, boolean z) {
        this.$allControls = list;
        this.$favorites = list2;
        this.allControls = list;
        this.favoritesIds = list2;
        this.errorOnLoad = z;
    }

    @Override // com.android.systemui.controls.controller.ControlsController.LoadData
    public final List<ControlStatus> getAllControls() {
        return this.allControls;
    }

    @Override // com.android.systemui.controls.controller.ControlsController.LoadData
    public final boolean getErrorOnLoad() {
        return this.errorOnLoad;
    }

    @Override // com.android.systemui.controls.controller.ControlsController.LoadData
    public final List<String> getFavoritesIds() {
        return this.favoritesIds;
    }
}
