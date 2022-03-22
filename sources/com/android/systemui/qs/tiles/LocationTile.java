package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LocationTile extends QSTileImpl<QSTile.BooleanState> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final LocationController mController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(2131232026);
    public final KeyguardStateController mKeyguard;

    /* loaded from: classes.dex */
    public final class Callback implements LocationController.LocationChangeCallback, KeyguardStateController.Callback {
        public Callback() {
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            LocationTile locationTile = LocationTile.this;
            Objects.requireNonNull(locationTile);
            locationTile.refreshState(null);
        }

        @Override // com.android.systemui.statusbar.policy.LocationController.LocationChangeCallback
        public final void onLocationSettingsChanged() {
            LocationTile locationTile = LocationTile.this;
            Objects.requireNonNull(locationTile);
            locationTile.refreshState(null);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 122;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953117);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (!this.mKeyguard.isMethodSecure() || !this.mKeyguard.isShowing()) {
            this.mController.setLocationEnabled(!((QSTile.BooleanState) this.mState).value);
            return;
        }
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new WMShell$7$$ExternalSyntheticLambda2(this, 2));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        QSTile.BooleanState booleanState2 = booleanState;
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        booleanState2.value = this.mController.isLocationEnabled();
        checkIfRestrictionEnforcedByAdminOnly(booleanState2, "no_share_location");
        if (!booleanState2.disabledByPolicy) {
            checkIfRestrictionEnforcedByAdminOnly(booleanState2, "no_config_location");
        }
        booleanState2.icon = this.mIcon;
        int i = 1;
        booleanState2.slash.isSlashed = !booleanState2.value;
        String string = this.mContext.getString(2131953117);
        booleanState2.label = string;
        booleanState2.contentDescription = string;
        if (booleanState2.value) {
            i = 2;
        }
        booleanState2.state = i;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public LocationTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, LocationController locationController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mController = locationController;
        this.mKeyguard = keyguardStateController;
        locationController.observe((LifecycleOwner) this, (LocationTile) callback);
        keyguardStateController.observe((LifecycleOwner) this, (LocationTile) callback);
    }
}
