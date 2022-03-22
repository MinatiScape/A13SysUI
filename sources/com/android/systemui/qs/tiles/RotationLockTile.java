package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorPrivacyManager;
import android.view.View;
import android.widget.Switch;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.RotationLockController;
import java.util.Objects;
/* loaded from: classes.dex */
public class RotationLockTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final BatteryController mBatteryController;
    public final AnonymousClass2 mCallback;
    public final RotationLockController mController;
    public final SensorPrivacyManager mPrivacyManager;
    public final AnonymousClass1 mSetting;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302821);
    public final RotationLockTile$$ExternalSyntheticLambda0 mSensorPrivacyChangedListener = new SensorPrivacyManager.OnSensorPrivacyChangedListener() { // from class: com.android.systemui.qs.tiles.RotationLockTile$$ExternalSyntheticLambda0
        public final void onSensorPrivacyChanged(int i, boolean z) {
            RotationLockTile rotationLockTile = RotationLockTile.this;
            Objects.requireNonNull(rotationLockTile);
            rotationLockTile.refreshState(null);
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 123;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onPowerSaveChanged(boolean z) {
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.AUTO_ROTATE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return ((QSTile.BooleanState) this.mState).label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        boolean z = !((QSTile.BooleanState) this.mState).value;
        this.mController.setRotationLocked(!z);
        refreshState(Boolean.valueOf(z));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleInitialize() {
        this.mPrivacyManager.addSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isRotationLocked = this.mController.isRotationLocked();
        boolean isPowerSave = this.mBatteryController.isPowerSave();
        int i = 2;
        boolean isSensorPrivacyEnabled = this.mPrivacyManager.isSensorPrivacyEnabled(2);
        boolean z = false;
        if (!isPowerSave && !isSensorPrivacyEnabled) {
            PackageManager packageManager = this.mContext.getPackageManager();
            String rotationResolverPackageName = packageManager.getRotationResolverPackageName();
            if ((rotationResolverPackageName != null && packageManager.checkPermission("android.permission.CAMERA", rotationResolverPackageName) == 0) && this.mController.isCameraRotationEnabled()) {
                z = true;
            }
        }
        booleanState.value = !isRotationLocked;
        booleanState.label = this.mContext.getString(2131953133);
        booleanState.icon = this.mIcon;
        booleanState.contentDescription = this.mContext.getString(2131951798);
        if (isRotationLocked || !z) {
            booleanState.secondaryLabel = "";
        } else {
            booleanState.secondaryLabel = this.mContext.getResources().getString(2131953170);
        }
        booleanState.stateDescription = booleanState.secondaryLabel;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (!booleanState.value) {
            i = 1;
        }
        booleanState.state = i;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
        setUserId(i);
        handleRefreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.Object, com.android.systemui.qs.tiles.RotationLockTile$2] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.qs.tiles.RotationLockTile$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public RotationLockTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.statusbar.policy.RotationLockController r9, android.hardware.SensorPrivacyManager r10, com.android.systemui.statusbar.policy.BatteryController r11, com.android.systemui.util.settings.SecureSettings r12) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r2 = 17302821(0x1080525, float:2.4982946E-38)
            com.android.systemui.plugins.qs.QSTile$Icon r2 = com.android.systemui.qs.tileimpl.QSTileImpl.ResourceIcon.get(r2)
            r0.mIcon = r2
            com.android.systemui.qs.tiles.RotationLockTile$2 r2 = new com.android.systemui.qs.tiles.RotationLockTile$2
            r2.<init>()
            r0.mCallback = r2
            com.android.systemui.qs.tiles.RotationLockTile$$ExternalSyntheticLambda0 r3 = new com.android.systemui.qs.tiles.RotationLockTile$$ExternalSyntheticLambda0
            r3.<init>()
            r0.mSensorPrivacyChangedListener = r3
            r0.mController = r9
            r9.observe(r0, r2)
            r0.mPrivacyManager = r10
            r0.mBatteryController = r11
            android.content.Context r1 = r1.getUserContext()
            int r1 = r1.getUserId()
            com.android.systemui.qs.tiles.RotationLockTile$1 r2 = new com.android.systemui.qs.tiles.RotationLockTile$1
            com.android.systemui.qs.tileimpl.QSTileImpl<TState>$H r3 = r0.mHandler
            r2.<init>(r12, r3, r1)
            r0.mSetting = r2
            androidx.lifecycle.LifecycleRegistry r1 = r0.mLifecycle
            r11.observe(r1, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.RotationLockTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.statusbar.policy.RotationLockController, android.hardware.SensorPrivacyManager, com.android.systemui.statusbar.policy.BatteryController, com.android.systemui.util.settings.SecureSettings):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
        setListening(false);
        this.mPrivacyManager.removeSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        setListening(z);
    }
}
