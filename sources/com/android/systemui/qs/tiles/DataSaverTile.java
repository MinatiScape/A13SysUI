package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Prefs;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public final class DataSaverTile extends QSTileImpl<QSTile.BooleanState> implements DataSaverController.Listener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final DataSaverController mDataSaverController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 284;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.DATA_SAVER_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131952237);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).value || Prefs.getBoolean(this.mContext, "QsDataSaverDialogShown")) {
            toggleDataSaver();
        } else {
            this.mUiHandler.post(new ShellExecutor$$ExternalSyntheticLambda0(this, view, 2));
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        int i;
        int i2;
        QSTile.BooleanState booleanState2 = booleanState;
        if (obj instanceof Boolean) {
            z = ((Boolean) obj).booleanValue();
        } else {
            z = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState2.value = z;
        if (z) {
            i = 2;
        } else {
            i = 1;
        }
        booleanState2.state = i;
        String string = this.mContext.getString(2131952237);
        booleanState2.label = string;
        booleanState2.contentDescription = string;
        if (booleanState2.value) {
            i2 = 2131231797;
        } else {
            i2 = 2131231798;
        }
        booleanState2.icon = QSTileImpl.ResourceIcon.get(i2);
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public final void toggleDataSaver() {
        ((QSTile.BooleanState) this.mState).value = !this.mDataSaverController.isDataSaverEnabled();
        this.mDataSaverController.setDataSaverEnabled(((QSTile.BooleanState) this.mState).value);
        refreshState(Boolean.valueOf(((QSTile.BooleanState) this.mState).value));
    }

    public DataSaverTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, DataSaverController dataSaverController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mDataSaverController = dataSaverController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        dataSaverController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
    }

    @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
    public final void onDataSaverChanged(boolean z) {
        refreshState(Boolean.valueOf(z));
    }
}
