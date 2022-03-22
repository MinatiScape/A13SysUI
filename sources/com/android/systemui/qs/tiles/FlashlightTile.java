package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.FlashlightController;
/* loaded from: classes.dex */
public final class FlashlightTile extends QSTileImpl<QSTile.BooleanState> implements FlashlightController.FlashlightListener {
    public final FlashlightController mFlashlightController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302825);

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 119;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
    }

    @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
    public final void onFlashlightAvailabilityChanged(boolean z) {
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.media.action.STILL_IMAGE_CAMERA");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953111);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        QSTile.BooleanState booleanState2 = booleanState;
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        booleanState2.label = this.mHost.getContext().getString(2131953111);
        booleanState2.secondaryLabel = "";
        booleanState2.stateDescription = "";
        int i = 1;
        if (!this.mFlashlightController.isAvailable()) {
            booleanState2.icon = this.mIcon;
            booleanState2.slash.isSlashed = true;
            String string = this.mContext.getString(2131953110);
            booleanState2.secondaryLabel = string;
            booleanState2.stateDescription = string;
            booleanState2.state = 0;
            return;
        }
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            if (booleanValue != booleanState2.value) {
                booleanState2.value = booleanValue;
            } else {
                return;
            }
        } else {
            booleanState2.value = this.mFlashlightController.isEnabled();
        }
        booleanState2.icon = this.mIcon;
        booleanState2.slash.isSlashed = !booleanState2.value;
        booleanState2.contentDescription = this.mContext.getString(2131953111);
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        if (booleanState2.value) {
            i = 2;
        }
        booleanState2.state = i;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mFlashlightController.hasFlashlight();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
    public final void onFlashlightError() {
        refreshState(Boolean.FALSE);
    }

    public FlashlightTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, FlashlightController flashlightController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mFlashlightController = flashlightController;
        flashlightController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (!ActivityManager.isUserAMonkey()) {
            boolean z = !((QSTile.BooleanState) this.mState).value;
            refreshState(Boolean.valueOf(z));
            this.mFlashlightController.setFlashlight(z);
        }
    }

    @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
    public final void onFlashlightChanged(boolean z) {
        refreshState(Boolean.valueOf(z));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleLongClick(View view) {
        handleClick(view);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
    }
}
