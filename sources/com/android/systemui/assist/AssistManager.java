package com.android.systemui.assist;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public class AssistManager {
    public final AssistDisclosure mAssistDisclosure;
    public final AssistLogger mAssistLogger;
    public final AssistUtils mAssistUtils;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final PhoneStateMonitor mPhoneStateMonitor;
    public final Lazy<SysUiState> mSysUiState;
    public final DefaultUiController mUiController;

    /* loaded from: classes.dex */
    public interface UiController {
        void hide();

        void onGestureCompletion(float f);

        void onInvocationProgress(int i, float f);
    }

    public final void hideAssist() {
        this.mAssistUtils.hideCurrentSession();
    }

    public void logStartAssistLegacy(int i, int i2) {
        MetricsLogger.action(new LogMaker(1716).setType(1).setSubtype((i << 1) | 0 | (i2 << 4)));
    }

    public void onGestureCompletion(float f) {
        this.mUiController.onGestureCompletion(f);
    }

    public void onInvocationProgress(int i, float f) {
        this.mUiController.onInvocationProgress(1, f);
    }

    public void registerVoiceInteractionSessionListener() {
        this.mAssistUtils.registerVoiceInteractionSessionListener(new IVoiceInteractionSessionListener.Stub() { // from class: com.android.systemui.assist.AssistManager.2
            public final void onSetUiHints(Bundle bundle) {
                if ("set_assist_gesture_constrained".equals(bundle.getString("action"))) {
                    SysUiState sysUiState = AssistManager.this.mSysUiState.get();
                    sysUiState.setFlag(8192, bundle.getBoolean("should_constrain", false));
                    sysUiState.commitUpdate(0);
                }
            }

            public final void onVoiceSessionHidden() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
            }

            public final void onVoiceSessionShown() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_UPDATE);
            }
        });
    }

    public AssistManager(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, CommandQueue commandQueue, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, Lazy<SysUiState> lazy, DefaultUiController defaultUiController, AssistLogger assistLogger, Handler handler) {
        this.mContext = context;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mAssistUtils = assistUtils;
        this.mAssistDisclosure = new AssistDisclosure(context, handler);
        this.mPhoneStateMonitor = phoneStateMonitor;
        this.mAssistLogger = assistLogger;
        registerVoiceInteractionSessionListener();
        this.mUiController = defaultUiController;
        this.mSysUiState = lazy;
        overviewProxyService.addCallback(new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.assist.AssistManager.1
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onAssistantGestureCompletion(float f) {
                AssistManager.this.onGestureCompletion(f);
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onAssistantProgress(float f) {
                AssistManager.this.onInvocationProgress(1, f);
            }
        });
    }

    public final void startAssist(Bundle bundle) {
        final Intent assistIntent;
        ComponentName assistComponentForUser = this.mAssistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser());
        if (assistComponentForUser != null) {
            boolean equals = assistComponentForUser.equals(this.mAssistUtils.getActiveServiceComponentName());
            if (bundle == null) {
                bundle = new Bundle();
            }
            boolean z = false;
            int i = bundle.getInt("invocation_type", 0);
            int phoneState = this.mPhoneStateMonitor.getPhoneState();
            bundle.putInt("invocation_phone_state", phoneState);
            bundle.putLong("invocation_time_ms", SystemClock.elapsedRealtime());
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, true, assistComponentForUser, Integer.valueOf(phoneState));
            logStartAssistLegacy(i, phoneState);
            if (equals) {
                this.mAssistUtils.showSessionForActiveService(bundle, 4, (IVoiceInteractionSessionShowCallback) null, (IBinder) null);
            } else if (this.mDeviceProvisionedController.isDeviceProvisioned()) {
                this.mCommandQueue.animateCollapsePanels(3, false);
                if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_structure_enabled", 1, -2) != 0) {
                    z = true;
                }
                SearchManager searchManager = (SearchManager) this.mContext.getSystemService("search");
                if (searchManager != null && (assistIntent = searchManager.getAssistIntent(z)) != null) {
                    assistIntent.setComponent(assistComponentForUser);
                    assistIntent.putExtras(bundle);
                    if (z && AssistUtils.isDisclosureEnabled(this.mContext)) {
                        AssistDisclosure assistDisclosure = this.mAssistDisclosure;
                        Objects.requireNonNull(assistDisclosure);
                        assistDisclosure.mHandler.removeCallbacks(assistDisclosure.mShowRunnable);
                        assistDisclosure.mHandler.post(assistDisclosure.mShowRunnable);
                    }
                    try {
                        final ActivityOptions makeCustomAnimation = ActivityOptions.makeCustomAnimation(this.mContext, 2130772554, 2130772555);
                        assistIntent.addFlags(268435456);
                        AsyncTask.execute(new Runnable() { // from class: com.android.systemui.assist.AssistManager.3
                            @Override // java.lang.Runnable
                            public final void run() {
                                AssistManager.this.mContext.startActivityAsUser(assistIntent, makeCustomAnimation.toBundle(), new UserHandle(-2));
                            }
                        });
                    } catch (ActivityNotFoundException unused) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Activity not found for ");
                        m.append(assistIntent.getAction());
                        Log.w("AssistManager", m.toString());
                    }
                }
            }
        }
    }
}
