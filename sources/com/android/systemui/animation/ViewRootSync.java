package com.android.systemui.animation;

import android.app.ActivityManager;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$IntRef;
/* compiled from: ViewRootSync.kt */
/* loaded from: classes.dex */
public final class ViewRootSync {
    public static final boolean forceDisableSynchronization = ActivityManager.isLowRamDeviceStatic();

    public static void synchronizeNextDraw(View view, View view2, final Function0 function0) {
        if (forceDisableSynchronization || !view.isAttachedToWindow() || view.getViewRootImpl() == null || !view2.isAttachedToWindow() || view2.getViewRootImpl() == null || Intrinsics.areEqual(view.getViewRootImpl(), view2.getViewRootImpl())) {
            function0.invoke();
            return;
        }
        final Ref$IntRef ref$IntRef = new Ref$IntRef();
        final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl.consumeNextDraw(new Consumer() { // from class: com.android.systemui.animation.ViewRootSync$synchronizeNextDraw$consumeNextDraw$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SurfaceControl.Transaction transaction2 = (SurfaceControl.Transaction) obj;
                Ref$IntRef ref$IntRef2 = Ref$IntRef.this;
                SurfaceControl.Transaction transaction3 = transaction;
                Function0<Unit> function02 = function0;
                ref$IntRef2.element--;
                if (transaction2 != null) {
                    transaction3.merge(transaction2);
                }
                if (ref$IntRef2.element == 0) {
                    transaction3.apply();
                    function02.invoke();
                }
            }
        })) {
            ref$IntRef.element++;
            viewRootImpl.getView().invalidate();
        }
        ViewRootImpl viewRootImpl2 = view2.getViewRootImpl();
        if (viewRootImpl2.consumeNextDraw(new Consumer() { // from class: com.android.systemui.animation.ViewRootSync$synchronizeNextDraw$consumeNextDraw$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SurfaceControl.Transaction transaction2 = (SurfaceControl.Transaction) obj;
                Ref$IntRef ref$IntRef2 = Ref$IntRef.this;
                SurfaceControl.Transaction transaction3 = transaction;
                Function0<Unit> function02 = function0;
                ref$IntRef2.element--;
                if (transaction2 != null) {
                    transaction3.merge(transaction2);
                }
                if (ref$IntRef2.element == 0) {
                    transaction3.apply();
                    function02.invoke();
                }
            }
        })) {
            ref$IntRef.element++;
            viewRootImpl2.getView().invalidate();
        }
        if (ref$IntRef.element == 0) {
            function0.invoke();
        }
    }
}
