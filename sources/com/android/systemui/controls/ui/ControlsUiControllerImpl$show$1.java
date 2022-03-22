package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public /* synthetic */ class ControlsUiControllerImpl$show$1 extends FunctionReferenceImpl implements Function1<List<? extends SelectionItem>, Unit> {
    public ControlsUiControllerImpl$show$1(ControlsUiController controlsUiController) {
        super(1, controlsUiController, ControlsUiControllerImpl.class, "showSeedingView", "showSeedingView(Ljava/util/List;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(List<? extends SelectionItem> list) {
        ControlsUiControllerImpl controlsUiControllerImpl = (ControlsUiControllerImpl) this.receiver;
        ComponentName componentName = ControlsUiControllerImpl.EMPTY_COMPONENT;
        Objects.requireNonNull(controlsUiControllerImpl);
        LayoutInflater from = LayoutInflater.from(controlsUiControllerImpl.context);
        ViewGroup viewGroup = controlsUiControllerImpl.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        from.inflate(2131624055, viewGroup, true);
        ViewGroup viewGroup3 = controlsUiControllerImpl.parent;
        if (viewGroup3 != null) {
            viewGroup2 = viewGroup3;
        }
        ((TextView) viewGroup2.requireViewById(2131427772)).setText(controlsUiControllerImpl.context.getResources().getString(2131952204));
        return Unit.INSTANCE;
    }
}
