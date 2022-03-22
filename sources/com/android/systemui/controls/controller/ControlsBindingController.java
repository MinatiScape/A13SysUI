package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import com.android.systemui.util.UserAwareController;
import java.util.List;
import java.util.function.Consumer;
/* compiled from: ControlsBindingController.kt */
/* loaded from: classes.dex */
public interface ControlsBindingController extends UserAwareController {

    /* compiled from: ControlsBindingController.kt */
    /* loaded from: classes.dex */
    public interface LoadCallback extends Consumer<List<? extends Control>> {
        void error(String str);
    }

    void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction);

    ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1 bindAndLoad(ComponentName componentName, ControlsControllerImpl$loadForComponent$2 controlsControllerImpl$loadForComponent$2);

    void bindAndLoadSuggested(ComponentName componentName, ControlsControllerImpl$startSeeding$1 controlsControllerImpl$startSeeding$1);

    void onComponentRemoved(ComponentName componentName);

    void subscribe(StructureInfo structureInfo);

    void unsubscribe();
}
