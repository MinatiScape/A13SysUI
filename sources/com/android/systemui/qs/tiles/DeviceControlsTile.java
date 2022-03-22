package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsTile.kt */
/* loaded from: classes.dex */
public final class DeviceControlsTile extends QSTileImpl<QSTile.State> {
    public final ControlsComponent controlsComponent;
    public final KeyguardStateController keyguardStateController;
    public AtomicBoolean hasControlsApps = new AtomicBoolean(false);
    public final DeviceControlsTile$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public final void onServicesUpdated(ArrayList arrayList) {
            if (DeviceControlsTile.this.hasControlsApps.compareAndSet(arrayList.isEmpty(), !arrayList.isEmpty())) {
                DeviceControlsTile deviceControlsTile = DeviceControlsTile.this;
                Objects.requireNonNull(deviceControlsTile);
                deviceControlsTile.refreshState(null);
            }
        }
    };

    public static /* synthetic */ void getIcon$annotations() {
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleLongClick(View view) {
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        Context context = this.mContext;
        ControlsComponent controlsComponent = this.controlsComponent;
        Objects.requireNonNull(controlsComponent);
        return context.getText(controlsComponent.controlsTileResourceConfiguration.getTileTitleId());
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (this.mState.state != 0) {
            final Intent intent = new Intent();
            intent.setComponent(new ComponentName(this.mContext, ControlsActivity.class));
            intent.addFlags(335544320);
            intent.putExtra("extra_animate", true);
            final GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = null;
            if (view != null) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view + " is not attached to a ViewGroup", new Exception());
                } else {
                    ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view, (Integer) 32, 4);
                }
            }
            this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile$handleClick$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    DeviceControlsTile deviceControlsTile = DeviceControlsTile.this;
                    Objects.requireNonNull(deviceControlsTile);
                    if (deviceControlsTile.mState.state == 2) {
                        z = true;
                    } else {
                        z = false;
                    }
                    DeviceControlsTile.this.mActivityStarter.startActivity(intent, true, ghostedViewLaunchAnimatorController, z);
                }
            });
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.controlsComponent.getControlsController().isPresent();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.state = 0;
        state.handlesLongClick = false;
        return state;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.qs.tiles.DeviceControlsTile$listingCallback$1] */
    public DeviceControlsTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ControlsComponent controlsComponent, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.controlsComponent = controlsComponent;
        this.keyguardStateController = keyguardStateController;
        controlsComponent.getControlsListingController().ifPresent(new Consumer() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile.1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DeviceControlsTile deviceControlsTile = DeviceControlsTile.this;
                ((ControlsListingController) obj).observe((LifecycleOwner) deviceControlsTile, (DeviceControlsTile) deviceControlsTile.listingCallback);
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.State state, Object obj) {
        CharSequence tileLabel = getTileLabel();
        state.label = tileLabel;
        state.contentDescription = tileLabel;
        ControlsComponent controlsComponent = this.controlsComponent;
        Objects.requireNonNull(controlsComponent);
        state.icon = QSTileImpl.ResourceIcon.get(controlsComponent.controlsTileResourceConfiguration.getTileImageId());
        ControlsComponent controlsComponent2 = this.controlsComponent;
        Objects.requireNonNull(controlsComponent2);
        if (!controlsComponent2.featureEnabled || !this.hasControlsApps.get()) {
            state.state = 0;
            return;
        }
        if (this.controlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
            StructureInfo preferredStructure = this.controlsComponent.getControlsController().get().getPreferredStructure();
            Objects.requireNonNull(preferredStructure);
            CharSequence charSequence = preferredStructure.structure;
            state.state = 2;
            if (Intrinsics.areEqual(charSequence, getTileLabel())) {
                charSequence = null;
            }
            state.secondaryLabel = charSequence;
        } else {
            state.state = 1;
            state.secondaryLabel = this.mContext.getText(2131952206);
        }
        state.stateDescription = state.secondaryLabel;
    }
}
