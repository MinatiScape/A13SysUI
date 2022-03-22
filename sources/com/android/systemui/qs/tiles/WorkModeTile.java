package com.android.systemui.qs.tiles;

import android.app.admin.DevicePolicyManager;
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
import com.android.systemui.statusbar.phone.ManagedProfileController;
import java.util.Objects;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public final class WorkModeTile extends QSTileImpl<QSTile.BooleanState> implements ManagedProfileController.Callback {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(2131232684);
    public final ManagedProfileController mProfileController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 257;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.MANAGED_PROFILE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getString("SystemUi.QS_WORK_PROFILE_LABEL", new Callable() { // from class: com.android.systemui.qs.tiles.WorkModeTile$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                WorkModeTile workModeTile = WorkModeTile.this;
                Objects.requireNonNull(workModeTile);
                return workModeTile.mContext.getString(2131953147);
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        this.mProfileController.setWorkModeEnabled(!((QSTile.BooleanState) this.mState).value);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        QSTile.BooleanState booleanState2 = booleanState;
        if (!isAvailable()) {
            onManagedProfileRemoved();
        }
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        if (obj instanceof Boolean) {
            booleanState2.value = ((Boolean) obj).booleanValue();
        } else {
            booleanState2.value = this.mProfileController.isWorkModeEnabled();
        }
        booleanState2.icon = this.mIcon;
        int i = 1;
        if (booleanState2.value) {
            booleanState2.slash.isSlashed = false;
        } else {
            booleanState2.slash.isSlashed = true;
        }
        CharSequence tileLabel = getTileLabel();
        booleanState2.label = tileLabel;
        booleanState2.contentDescription = tileLabel;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        if (booleanState2.value) {
            i = 2;
        }
        booleanState2.state = i;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mProfileController.hasActiveProfile();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
    public final void onManagedProfileChanged() {
        refreshState(Boolean.valueOf(this.mProfileController.isWorkModeEnabled()));
    }

    @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
    public final void onManagedProfileRemoved() {
        this.mHost.removeTile(this.mTileSpec);
        this.mHost.unmarkTileAsAutoAdded(this.mTileSpec);
    }

    public WorkModeTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ManagedProfileController managedProfileController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mProfileController = managedProfileController;
        managedProfileController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
    }
}
