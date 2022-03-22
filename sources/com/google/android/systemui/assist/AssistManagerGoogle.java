package com.google.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Paint;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.IWindowManager;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistantSessionEvent;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.assist.uihints.AssistantInvocationLightsView;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.GoogleDefaultUiController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.NgaUiController;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AssistManagerGoogle extends AssistManager {
    public final AssistantPresenceHandler mAssistantPresenceHandler;
    public final GoogleDefaultUiController mDefaultUiController;
    public boolean mGoogleIsAssistant;
    public int mNavigationMode;
    public boolean mNgaIsAssistant;
    public final NgaMessageHandler mNgaMessageHandler;
    public final NgaUiController mNgaUiController;
    public final OpaEnabledReceiver mOpaEnabledReceiver;
    public boolean mSqueezeSetUp;
    public AssistManager.UiController mUiController;
    public final Handler mUiHandler;
    public final IWindowManager mWindowManagerService;
    public boolean mCheckAssistantStatus = true;
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda8 mOnProcessBundle = new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this, 5);

    public AssistManagerGoogle(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, NgaUiController ngaUiController, CommandQueue commandQueue, OpaEnabledReceiver opaEnabledReceiver, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, OpaEnabledDispatcher opaEnabledDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, AssistantPresenceHandler assistantPresenceHandler, NgaMessageHandler ngaMessageHandler, Lazy<SysUiState> lazy, Handler handler, DefaultUiController defaultUiController, GoogleDefaultUiController googleDefaultUiController, IWindowManager iWindowManager, AssistLogger assistLogger) {
        super(deviceProvisionedController, context, assistUtils, commandQueue, phoneStateMonitor, overviewProxyService, lazy, defaultUiController, assistLogger, handler);
        this.mUiHandler = handler;
        this.mOpaEnabledReceiver = opaEnabledReceiver;
        addOpaEnabledListener(opaEnabledDispatcher);
        keyguardUpdateMonitor.registerCallback(new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.assist.AssistManagerGoogle.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public final void onUserSwitching(int i) {
                OpaEnabledReceiver opaEnabledReceiver2 = AssistManagerGoogle.this.mOpaEnabledReceiver;
                Objects.requireNonNull(opaEnabledReceiver2);
                opaEnabledReceiver2.updateOpaEnabledState(true, null);
                opaEnabledReceiver2.mContentResolver.unregisterContentObserver(opaEnabledReceiver2.mContentObserver);
                opaEnabledReceiver2.registerContentObserver();
                opaEnabledReceiver2.mBroadcastDispatcher.unregisterReceiver(opaEnabledReceiver2.mBroadcastReceiver);
                opaEnabledReceiver2.registerEnabledReceiver(i);
            }
        });
        this.mNgaUiController = ngaUiController;
        this.mDefaultUiController = googleDefaultUiController;
        this.mUiController = googleDefaultUiController;
        this.mNavigationMode = navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.assist.AssistManagerGoogle$$ExternalSyntheticLambda0
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                AssistManagerGoogle assistManagerGoogle = AssistManagerGoogle.this;
                Objects.requireNonNull(assistManagerGoogle);
                assistManagerGoogle.mNavigationMode = i;
            }
        });
        this.mAssistantPresenceHandler = assistantPresenceHandler;
        AssistantPresenceHandler.AssistantPresenceChangeListener assistManagerGoogle$$ExternalSyntheticLambda1 = new AssistantPresenceHandler.AssistantPresenceChangeListener() { // from class: com.google.android.systemui.assist.AssistManagerGoogle$$ExternalSyntheticLambda1
            @Override // com.google.android.systemui.assist.uihints.AssistantPresenceHandler.AssistantPresenceChangeListener
            public final void onAssistantPresenceChanged(boolean z, boolean z2) {
                AssistManagerGoogle assistManagerGoogle = AssistManagerGoogle.this;
                Objects.requireNonNull(assistManagerGoogle);
                if (!(assistManagerGoogle.mGoogleIsAssistant == z && assistManagerGoogle.mNgaIsAssistant == z2)) {
                    if (!z2) {
                        if (!assistManagerGoogle.mUiController.equals(assistManagerGoogle.mDefaultUiController)) {
                            AssistManager.UiController uiController = assistManagerGoogle.mUiController;
                            assistManagerGoogle.mUiController = assistManagerGoogle.mDefaultUiController;
                            Handler handler2 = assistManagerGoogle.mUiHandler;
                            Objects.requireNonNull(uiController);
                            handler2.post(new CarrierTextManager$$ExternalSyntheticLambda0(uiController, 10));
                        }
                        GoogleDefaultUiController googleDefaultUiController2 = assistManagerGoogle.mDefaultUiController;
                        Objects.requireNonNull(googleDefaultUiController2);
                        AssistantInvocationLightsView assistantInvocationLightsView = (AssistantInvocationLightsView) googleDefaultUiController2.mInvocationLightsView;
                        Objects.requireNonNull(assistantInvocationLightsView);
                        if (z) {
                            int i = assistantInvocationLightsView.mColorBlue;
                            int i2 = assistantInvocationLightsView.mColorRed;
                            int i3 = assistantInvocationLightsView.mColorYellow;
                            int i4 = assistantInvocationLightsView.mColorGreen;
                            assistantInvocationLightsView.mUseNavBarColor = false;
                            assistantInvocationLightsView.attemptUnregisterNavBarListener();
                            EdgeLight edgeLight = assistantInvocationLightsView.mAssistInvocationLights.get(0);
                            Objects.requireNonNull(edgeLight);
                            edgeLight.mColor = i;
                            EdgeLight edgeLight2 = assistantInvocationLightsView.mAssistInvocationLights.get(1);
                            Objects.requireNonNull(edgeLight2);
                            edgeLight2.mColor = i2;
                            EdgeLight edgeLight3 = assistantInvocationLightsView.mAssistInvocationLights.get(2);
                            Objects.requireNonNull(edgeLight3);
                            edgeLight3.mColor = i3;
                            EdgeLight edgeLight4 = assistantInvocationLightsView.mAssistInvocationLights.get(3);
                            Objects.requireNonNull(edgeLight4);
                            edgeLight4.mColor = i4;
                        } else {
                            assistantInvocationLightsView.mUseNavBarColor = true;
                            assistantInvocationLightsView.mPaint.setStrokeCap(Paint.Cap.BUTT);
                            assistantInvocationLightsView.attemptRegisterNavBarListener();
                        }
                    } else if (!assistManagerGoogle.mUiController.equals(assistManagerGoogle.mNgaUiController)) {
                        AssistManager.UiController uiController2 = assistManagerGoogle.mUiController;
                        assistManagerGoogle.mUiController = assistManagerGoogle.mNgaUiController;
                        Handler handler3 = assistManagerGoogle.mUiHandler;
                        Objects.requireNonNull(uiController2);
                        handler3.post(new StatusBar$$ExternalSyntheticLambda19(uiController2, 9));
                    }
                    assistManagerGoogle.mGoogleIsAssistant = z;
                    assistManagerGoogle.mNgaIsAssistant = z2;
                }
                assistManagerGoogle.mCheckAssistantStatus = false;
            }
        };
        Objects.requireNonNull(assistantPresenceHandler);
        assistantPresenceHandler.mAssistantPresenceChangeListeners.add(assistManagerGoogle$$ExternalSyntheticLambda1);
        this.mNgaMessageHandler = ngaMessageHandler;
        this.mWindowManagerService = iWindowManager;
    }

    @Override // com.android.systemui.assist.AssistManager
    public final void logStartAssistLegacy(int i, int i2) {
        AssistantPresenceHandler assistantPresenceHandler = this.mAssistantPresenceHandler;
        Objects.requireNonNull(assistantPresenceHandler);
        MetricsLogger.action(new LogMaker(1716).setType(1).setSubtype(((assistantPresenceHandler.mNgaIsAssistant ? 1 : 0) << 8) | (i << 1) | 0 | (i2 << 4)));
    }

    @Override // com.android.systemui.assist.AssistManager
    public final void onGestureCompletion(float f) {
        this.mCheckAssistantStatus = true;
        this.mUiController.onGestureCompletion(f / this.mContext.getResources().getDisplayMetrics().density);
    }

    @Override // com.android.systemui.assist.AssistManager
    public final void onInvocationProgress(int i, float f) {
        boolean z;
        boolean z2 = true;
        if (f == 0.0f || f == 1.0f) {
            this.mCheckAssistantStatus = true;
            if (i == 2) {
                if (Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_gesture_setup_complete", 0) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                this.mSqueezeSetUp = z;
            }
        }
        if (this.mCheckAssistantStatus) {
            AssistantPresenceHandler assistantPresenceHandler = this.mAssistantPresenceHandler;
            Objects.requireNonNull(assistantPresenceHandler);
            ComponentName assistComponentForUser = assistantPresenceHandler.mAssistUtils.getAssistComponentForUser(-2);
            if (assistComponentForUser == null || !"com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString())) {
                z2 = false;
            }
            assistantPresenceHandler.updateAssistantPresence(z2, assistantPresenceHandler.mNgaIsAssistant, assistantPresenceHandler.mSysUiIsNgaUi);
            this.mCheckAssistantStatus = false;
        }
        if (i != 2 || this.mSqueezeSetUp) {
            this.mUiController.onInvocationProgress(i, f);
        }
    }

    public final void addOpaEnabledListener(OpaEnabledListener opaEnabledListener) {
        OpaEnabledReceiver opaEnabledReceiver = this.mOpaEnabledReceiver;
        Objects.requireNonNull(opaEnabledReceiver);
        opaEnabledReceiver.mListeners.add(opaEnabledListener);
        opaEnabledListener.onOpaEnabledReceived(opaEnabledReceiver.mContext, opaEnabledReceiver.mIsOpaEligible, opaEnabledReceiver.mIsAGSAAssistant, opaEnabledReceiver.mIsOpaEnabled, opaEnabledReceiver.mIsLongPressHomeEnabled);
    }

    @Override // com.android.systemui.assist.AssistManager
    public final void registerVoiceInteractionSessionListener() {
        this.mAssistUtils.registerVoiceInteractionSessionListener(new IVoiceInteractionSessionListener.Stub() { // from class: com.google.android.systemui.assist.AssistManagerGoogle.2
            public final void onSetUiHints(Bundle bundle) {
                String string = bundle.getString("action");
                if ("set_assist_gesture_constrained".equals(string)) {
                    SysUiState sysUiState = AssistManagerGoogle.this.mSysUiState.get();
                    sysUiState.setFlag(8192, bundle.getBoolean("should_constrain", false));
                    sysUiState.commitUpdate(0);
                } else if ("show_global_actions".equals(string)) {
                    try {
                        AssistManagerGoogle.this.mWindowManagerService.showGlobalActions();
                    } catch (RemoteException e) {
                        Log.e("AssistManagerGoogle", "showGlobalActions failed", e);
                    }
                } else {
                    AssistManagerGoogle assistManagerGoogle = AssistManagerGoogle.this;
                    assistManagerGoogle.mNgaMessageHandler.processBundle(bundle, assistManagerGoogle.mOnProcessBundle);
                }
            }

            public final void onVoiceSessionHidden() throws RemoteException {
                AssistManagerGoogle.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
            }

            public final void onVoiceSessionShown() throws RemoteException {
                AssistManagerGoogle.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_UPDATE);
            }
        });
    }
}
