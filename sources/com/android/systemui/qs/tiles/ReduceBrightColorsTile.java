package com.android.systemui.qs.tiles;

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
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ReduceBrightColorsTile extends QSTileImpl<QSTile.BooleanState> implements ReduceBrightColorsController.Listener {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(2131232243);
    public final boolean mIsAvailable;
    public final ReduceBrightColorsController mReduceBrightColorsController;

    public ReduceBrightColorsTile(boolean z, ReduceBrightColorsController reduceBrightColorsController, QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mReduceBrightColorsController = reduceBrightColorsController;
        reduceBrightColorsController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        this.mIsAvailable = z;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.ReduceBrightColorsController.Listener
    public final void onActivated(boolean z) {
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.REDUCE_BRIGHT_COLORS_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(17041351);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        ReduceBrightColorsController reduceBrightColorsController = this.mReduceBrightColorsController;
        Objects.requireNonNull(reduceBrightColorsController);
        reduceBrightColorsController.mManager.setReduceBrightColorsActivated(!((QSTile.BooleanState) this.mState).value);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        QSTile.BooleanState booleanState2 = booleanState;
        ReduceBrightColorsController reduceBrightColorsController = this.mReduceBrightColorsController;
        Objects.requireNonNull(reduceBrightColorsController);
        boolean isReduceBrightColorsActivated = reduceBrightColorsController.mManager.isReduceBrightColorsActivated();
        booleanState2.value = isReduceBrightColorsActivated;
        if (isReduceBrightColorsActivated) {
            i = 2;
        } else {
            i = 1;
        }
        booleanState2.state = i;
        booleanState2.label = this.mContext.getString(17041351);
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        booleanState2.contentDescription = booleanState2.label;
        booleanState2.icon = this.mIcon;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mIsAvailable;
    }
}
