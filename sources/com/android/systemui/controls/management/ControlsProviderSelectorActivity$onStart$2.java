package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.systemui.controls.controller.ControlsController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlsProviderSelectorActivity$onStart$2 extends FunctionReferenceImpl implements Function1<ComponentName, Integer> {
    public ControlsProviderSelectorActivity$onStart$2(ControlsController controlsController) {
        super(1, controlsController, ControlsController.class, "countFavoritesForComponent", "countFavoritesForComponent(Landroid/content/ComponentName;)I", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Integer invoke(ComponentName componentName) {
        return Integer.valueOf(((ControlsController) this.receiver).countFavoritesForComponent(componentName));
    }
}
