package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.LocationController;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes.dex */
public final class NightDisplayTile extends QSTileImpl<QSTile.BooleanState> implements NightDisplayListener.Callback {
    public boolean mIsListening;
    public NightDisplayListener mListener;
    public final LocationController mLocationController;
    public ColorDisplayManager mManager;
    public final NightDisplayListenerModule.Builder mNightDisplayListenerBuilder;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 491;
    }

    public final void onActivated(boolean z) {
        refreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.NIGHT_DISPLAY_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953126);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if ("1".equals(Settings.Global.getString(this.mContext.getContentResolver(), "night_display_forced_auto_mode_available")) && this.mManager.getNightDisplayAutoModeRaw() == -1) {
            this.mManager.setNightDisplayAutoMode(1);
            Log.i("NightDisplayTile", "Enrolled in forced night display auto mode");
        }
        this.mManager.setNightDisplayActivated(!((QSTile.BooleanState) this.mState).value);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        String str;
        CharSequence charSequence;
        LocalTime localTime;
        int i2;
        String str2;
        QSTile.BooleanState booleanState2 = booleanState;
        booleanState2.value = this.mManager.isNightDisplayActivated();
        booleanState2.label = this.mContext.getString(2131953126);
        booleanState2.icon = QSTileImpl.ResourceIcon.get(17302826);
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        boolean z = booleanState2.value;
        if (z) {
            i = 2;
        } else {
            i = 1;
        }
        booleanState2.state = i;
        int nightDisplayAutoMode = this.mManager.getNightDisplayAutoMode();
        if (nightDisplayAutoMode != 1) {
            str = null;
            if (nightDisplayAutoMode == 2 && this.mLocationController.isLocationEnabled()) {
                if (z) {
                    str2 = this.mContext.getString(2131953129);
                } else {
                    str2 = this.mContext.getString(2131953128);
                }
                str = str2;
            }
        } else {
            if (z) {
                localTime = this.mManager.getNightDisplayCustomEndTime();
                i2 = 2131953137;
            } else {
                localTime = this.mManager.getNightDisplayCustomStartTime();
                i2 = 2131953127;
            }
            Calendar instance = Calendar.getInstance();
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(this.mContext);
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            instance.setTimeZone(timeFormat.getTimeZone());
            instance.set(11, localTime.getHour());
            instance.set(12, localTime.getMinute());
            instance.set(13, 0);
            instance.set(14, 0);
            str = this.mContext.getString(i2, timeFormat.format(instance.getTime()));
        }
        booleanState2.secondaryLabel = str;
        if (TextUtils.isEmpty(str)) {
            charSequence = booleanState2.label;
        } else {
            charSequence = TextUtils.concat(booleanState2.label, ", ", booleanState2.secondaryLabel);
        }
        booleanState2.contentDescription = charSequence;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
        if (this.mIsListening) {
            this.mListener.setCallback((NightDisplayListener.Callback) null);
        }
        this.mManager = (ColorDisplayManager) this.mHost.getUserContext().getSystemService(ColorDisplayManager.class);
        NightDisplayListenerModule.Builder builder = this.mNightDisplayListenerBuilder;
        Objects.requireNonNull(builder);
        builder.mUserId = i;
        NightDisplayListener nightDisplayListener = new NightDisplayListener(builder.mContext, builder.mUserId, builder.mBgHandler);
        this.mListener = nightDisplayListener;
        if (this.mIsListening) {
            nightDisplayListener.setCallback(this);
        }
        handleRefreshState(null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return ColorDisplayManager.isNightDisplayAvailable(this.mContext);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public NightDisplayTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, LocationController locationController, ColorDisplayManager colorDisplayManager, NightDisplayListenerModule.Builder builder) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mLocationController = locationController;
        this.mManager = colorDisplayManager;
        this.mNightDisplayListenerBuilder = builder;
        int userId = qSHost.getUserContext().getUserId();
        Objects.requireNonNull(builder);
        builder.mUserId = userId;
        this.mListener = new NightDisplayListener(builder.mContext, builder.mUserId, builder.mBgHandler);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mIsListening = z;
        if (z) {
            this.mListener.setCallback(this);
            refreshState(null);
            return;
        }
        this.mListener.setCallback((NightDisplayListener.Callback) null);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).addTaggedData(1311, Integer.valueOf(this.mManager.getNightDisplayAutoModeRaw()));
    }
}
