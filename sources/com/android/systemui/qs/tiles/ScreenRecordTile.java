package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScreenRecordTile extends QSTileImpl<QSTile.BooleanState> implements RecordingController.RecordingStateChangeCallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final RecordingController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public final KeyguardStateController mKeyguardStateController;
    public long mMillisUntilFinished = 0;

    /* loaded from: classes.dex */
    public final class Callback implements RecordingController.RecordingStateChangeCallback {
        public Callback() {
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public final void onCountdown(long j) {
            ScreenRecordTile screenRecordTile = ScreenRecordTile.this;
            screenRecordTile.mMillisUntilFinished = j;
            screenRecordTile.refreshState(null);
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public final void onCountdownEnd() {
            ScreenRecordTile screenRecordTile = ScreenRecordTile.this;
            Objects.requireNonNull(screenRecordTile);
            screenRecordTile.refreshState(null);
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public final void onRecordingEnd() {
            ScreenRecordTile screenRecordTile = ScreenRecordTile.this;
            Objects.requireNonNull(screenRecordTile);
            screenRecordTile.refreshState(null);
        }

        @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
        public final void onRecordingStart() {
            ScreenRecordTile screenRecordTile = ScreenRecordTile.this;
            Objects.requireNonNull(screenRecordTile);
            screenRecordTile.refreshState(null);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953134);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        RecordingController recordingController = this.mController;
        Objects.requireNonNull(recordingController);
        if (recordingController.mIsStarting) {
            Log.d("ScreenRecordTile", "Cancelling countdown");
            this.mController.cancelCountdown();
        } else if (this.mController.isRecording()) {
            this.mController.stopRecording();
        } else {
            this.mUiHandler.post(new ScreenRecordTile$$ExternalSyntheticLambda1(this, view, 0));
        }
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        int i;
        boolean z2;
        CharSequence charSequence;
        QSTile.BooleanState booleanState2 = booleanState;
        RecordingController recordingController = this.mController;
        Objects.requireNonNull(recordingController);
        boolean z3 = recordingController.mIsStarting;
        boolean isRecording = this.mController.isRecording();
        if (isRecording || z3) {
            z = true;
        } else {
            z = false;
        }
        booleanState2.value = z;
        if (isRecording || z3) {
            i = 2;
        } else {
            i = 1;
        }
        booleanState2.state = i;
        booleanState2.label = this.mContext.getString(2131953134);
        booleanState2.icon = QSTileImpl.ResourceIcon.get(2131232251);
        if (booleanState2.state == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        booleanState2.forceExpandIcon = z2;
        if (isRecording) {
            booleanState2.secondaryLabel = this.mContext.getString(2131953136);
        } else if (z3) {
            booleanState2.secondaryLabel = String.format("%d...", Integer.valueOf((int) Math.floorDiv(this.mMillisUntilFinished + 500, 1000)));
        } else {
            booleanState2.secondaryLabel = this.mContext.getString(2131953135);
        }
        if (TextUtils.isEmpty(booleanState2.secondaryLabel)) {
            charSequence = booleanState2.label;
        } else {
            charSequence = TextUtils.concat(booleanState2.label, ", ", booleanState2.secondaryLabel);
        }
        booleanState2.contentDescription = charSequence;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = this.mContext.getString(2131953134);
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    public ScreenRecordTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RecordingController recordingController, KeyguardDismissUtil keyguardDismissUtil, KeyguardStateController keyguardStateController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mController = recordingController;
        Objects.requireNonNull(recordingController);
        recordingController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) callback);
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mKeyguardStateController = keyguardStateController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
    }
}
