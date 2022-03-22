package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class SmartReplyConstants {
    public final boolean mDefaultEditChoicesBeforeSending;
    public final boolean mDefaultEnabled;
    public final int mDefaultMaxNumActions;
    public final int mDefaultMaxSqueezeRemeasureAttempts;
    public final int mDefaultMinNumSystemGeneratedReplies;
    public final int mDefaultOnClickInitDelay;
    public final boolean mDefaultRequiresP;
    public final boolean mDefaultShowInHeadsUp;
    public final DeviceConfigProxy mDeviceConfig;
    public volatile boolean mEditChoicesBeforeSending;
    public volatile boolean mEnabled;
    public final Handler mHandler;
    public volatile int mMaxNumActions;
    public volatile int mMaxSqueezeRemeasureAttempts;
    public volatile int mMinNumSystemGeneratedReplies;
    public volatile long mOnClickInitDelay;
    public final AnonymousClass1 mOnPropertiesChangedListener;
    public volatile boolean mRequiresTargetingP;
    public volatile boolean mShowInHeadsUp;

    public final void updateConstants() {
        synchronized (this) {
            this.mEnabled = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_enabled", this.mDefaultEnabled);
            this.mRequiresTargetingP = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_requires_targeting_p", this.mDefaultRequiresP);
            DeviceConfigProxy deviceConfigProxy = this.mDeviceConfig;
            int i = this.mDefaultMaxSqueezeRemeasureAttempts;
            Objects.requireNonNull(deviceConfigProxy);
            this.mMaxSqueezeRemeasureAttempts = DeviceConfig.getInt("systemui", "ssin_max_squeeze_remeasure_attempts", i);
            this.mEditChoicesBeforeSending = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_edit_choices_before_sending", this.mDefaultEditChoicesBeforeSending);
            this.mShowInHeadsUp = readDeviceConfigBooleanOrDefaultIfEmpty("ssin_show_in_heads_up", this.mDefaultShowInHeadsUp);
            DeviceConfigProxy deviceConfigProxy2 = this.mDeviceConfig;
            int i2 = this.mDefaultMinNumSystemGeneratedReplies;
            Objects.requireNonNull(deviceConfigProxy2);
            this.mMinNumSystemGeneratedReplies = DeviceConfig.getInt("systemui", "ssin_min_num_system_generated_replies", i2);
            DeviceConfigProxy deviceConfigProxy3 = this.mDeviceConfig;
            int i3 = this.mDefaultMaxNumActions;
            Objects.requireNonNull(deviceConfigProxy3);
            this.mMaxNumActions = DeviceConfig.getInt("systemui", "ssin_max_num_actions", i3);
            DeviceConfigProxy deviceConfigProxy4 = this.mDeviceConfig;
            int i4 = this.mDefaultOnClickInitDelay;
            Objects.requireNonNull(deviceConfigProxy4);
            this.mOnClickInitDelay = DeviceConfig.getInt("systemui", "ssin_onclick_init_delay", i4);
        }
    }

    public final boolean readDeviceConfigBooleanOrDefaultIfEmpty(String str, boolean z) {
        Objects.requireNonNull(this.mDeviceConfig);
        String property = DeviceConfig.getProperty("systemui", str);
        if (TextUtils.isEmpty(property)) {
            return z;
        }
        if ("true".equals(property)) {
            return true;
        }
        if ("false".equals(property)) {
            return false;
        }
        return z;
    }

    public SmartReplyConstants(Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        new KeyValueListParser(',');
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.statusbar.policy.SmartReplyConstants.1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (!"systemui".equals(properties.getNamespace())) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Received update from DeviceConfig for unrelated namespace: ");
                    m.append(properties.getNamespace());
                    Log.e("SmartReplyConstants", m.toString());
                    return;
                }
                SmartReplyConstants.this.updateConstants();
            }
        };
        this.mOnPropertiesChangedListener = onPropertiesChangedListener;
        this.mHandler = handler;
        Resources resources = context.getResources();
        this.mDefaultEnabled = resources.getBoolean(2131034174);
        this.mDefaultRequiresP = resources.getBoolean(2131034175);
        this.mDefaultMaxSqueezeRemeasureAttempts = resources.getInteger(2131492906);
        this.mDefaultEditChoicesBeforeSending = resources.getBoolean(2131034173);
        this.mDefaultShowInHeadsUp = resources.getBoolean(2131034176);
        this.mDefaultMinNumSystemGeneratedReplies = resources.getInteger(2131492907);
        this.mDefaultMaxNumActions = resources.getInteger(2131492905);
        this.mDefaultOnClickInitDelay = resources.getInteger(2131492908);
        this.mDeviceConfig = deviceConfigProxy;
        Executor smartReplyConstants$$ExternalSyntheticLambda0 = new Executor() { // from class: com.android.systemui.statusbar.policy.SmartReplyConstants$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                SmartReplyConstants smartReplyConstants = SmartReplyConstants.this;
                Objects.requireNonNull(smartReplyConstants);
                smartReplyConstants.mHandler.post(runnable);
            }
        };
        Objects.requireNonNull(deviceConfigProxy);
        DeviceConfig.addOnPropertiesChangedListener("systemui", smartReplyConstants$$ExternalSyntheticLambda0, onPropertiesChangedListener);
        updateConstants();
    }
}
