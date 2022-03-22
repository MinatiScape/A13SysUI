package com.android.systemui.screenshot;

import android.frameworks.stats.VendorAtom;
import android.frameworks.stats.VendorAtomValue;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda6;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedEventCallback;
import com.android.wm.shell.startingsurface.TaskSnapshotWindow;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.elmyra.actions.UnpinNotifications$$ExternalSyntheticLambda0;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.reversecharging.ReverseChargingMetrics;
import java.util.Objects;
import java.util.concurrent.Executor;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda6(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        long j;
        String str;
        String str2;
        int i;
        Object[] objArr;
        Object[] objArr2;
        int i2;
        int i3 = 0;
        switch (this.$r8$classId) {
            case 0:
                ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                final ScrollCaptureResponse scrollCaptureResponse = (ScrollCaptureResponse) this.f$1;
                Objects.requireNonNull(screenshotController);
                screenshotController.mLastScrollCaptureResponse = null;
                CallbackToFutureAdapter.SafeFuture safeFuture = screenshotController.mLongScreenshotFuture;
                if (safeFuture != null) {
                    safeFuture.cancel(true);
                }
                final ScrollCaptureController scrollCaptureController = screenshotController.mScrollCaptureController;
                Objects.requireNonNull(scrollCaptureController);
                scrollCaptureController.mCancelled = false;
                CallbackToFutureAdapter.SafeFuture future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda0
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                        ScrollCaptureController scrollCaptureController2 = ScrollCaptureController.this;
                        ScrollCaptureResponse scrollCaptureResponse2 = scrollCaptureResponse;
                        Objects.requireNonNull(scrollCaptureController2);
                        scrollCaptureController2.mCaptureCompleter = completer;
                        scrollCaptureController2.mWindowOwner = scrollCaptureResponse2.getPackageName();
                        CallbackToFutureAdapter.Completer<ScrollCaptureController.LongScreenshot> completer2 = scrollCaptureController2.mCaptureCompleter;
                        QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0 = new QSTileImpl$$ExternalSyntheticLambda0(scrollCaptureController2, 4);
                        Executor executor = scrollCaptureController2.mBgExecutor;
                        Objects.requireNonNull(completer2);
                        ResolvableFuture<Void> resolvableFuture = completer2.cancellationFuture;
                        if (resolvableFuture != null) {
                            resolvableFuture.addListener(qSTileImpl$$ExternalSyntheticLambda0, executor);
                        }
                        scrollCaptureController2.mBgExecutor.execute(new NavBarTuner$$ExternalSyntheticLambda6(scrollCaptureController2, scrollCaptureResponse2, 3));
                        return "<batch scroll capture>";
                    }
                });
                screenshotController.mLongScreenshotFuture = future;
                future.delegate.addListener(new WMShell$7$$ExternalSyntheticLambda2(screenshotController, 4), screenshotController.mMainExecutor);
                return;
            case 1:
                PluginActionManager pluginActionManager = (PluginActionManager) this.f$0;
                Objects.requireNonNull(pluginActionManager);
                pluginActionManager.onPluginDisconnected((PluginInstance) this.f$1);
                return;
            case 2:
                HotspotControllerImpl hotspotControllerImpl = (HotspotControllerImpl) this.f$0;
                boolean z = HotspotControllerImpl.DEBUG;
                Objects.requireNonNull(hotspotControllerImpl);
                ((HotspotController.Callback) this.f$1).onHotspotChanged(hotspotControllerImpl.isHotspotEnabled(), hotspotControllerImpl.mNumConnectedDevices);
                return;
            case 3:
                OneHandedController.OneHandedImpl oneHandedImpl = (OneHandedController.OneHandedImpl) this.f$0;
                int i4 = OneHandedController.OneHandedImpl.$r8$clinit;
                Objects.requireNonNull(oneHandedImpl);
                OneHandedController oneHandedController = OneHandedController.this;
                Objects.requireNonNull(oneHandedController);
                oneHandedController.mEventCallback = (OneHandedEventCallback) this.f$1;
                return;
            case 4:
                TaskSnapshotWindow taskSnapshotWindow = (TaskSnapshotWindow) this.f$0;
                Objects.requireNonNull(taskSnapshotWindow);
                taskSnapshotWindow.removeImmediately();
                ((Runnable) this.f$1).run();
                return;
            case 5:
                EdgeLightsView edgeLightsView = (EdgeLightsView) this.f$0;
                int i5 = EdgeLightsView.$r8$clinit;
                Objects.requireNonNull(edgeLightsView);
                edgeLightsView.mAssistLights = EdgeLight.copy((EdgeLight[]) this.f$1);
                edgeLightsView.mListeners.forEach(new UnpinNotifications$$ExternalSyntheticLambda0(edgeLightsView, 2));
                edgeLightsView.invalidate();
                return;
            default:
                ReverseChargingController reverseChargingController = (ReverseChargingController) this.f$0;
                Bundle bundle = (Bundle) this.f$1;
                boolean z2 = ReverseChargingController.DEBUG;
                Objects.requireNonNull(reverseChargingController);
                boolean z3 = ReverseChargingController.DEBUG;
                if (z3) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onReverseStateChangedOnMainThread(): rtx=");
                    if (bundle.getInt("key_rtx_mode") == 1) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    m.append(i2);
                    m.append(" bundle=");
                    m.append(bundle.toString());
                    m.append(" this=");
                    m.append(reverseChargingController);
                    Log.d("ReverseChargingControl", m.toString());
                }
                int i6 = bundle.getInt("key_rtx_mode");
                int i7 = bundle.getInt("key_reason_type");
                boolean z4 = bundle.getBoolean("key_rtx_connection");
                int i8 = bundle.getInt("key_accessory_type");
                int i9 = bundle.getInt("key_rtx_level");
                if (!reverseChargingController.mReverse && reverseChargingController.mWirelessCharging && i6 == 0 && i9 > 0) {
                    reverseChargingController.mRtxLevel = i9;
                    if (TextUtils.isEmpty(reverseChargingController.mName)) {
                        reverseChargingController.mName = reverseChargingController.mContext.getString(2131953162);
                    }
                    reverseChargingController.fireReverseChanged();
                    return;
                } else if (!reverseChargingController.isReverseSupported()) {
                    reverseChargingController.mReverse = false;
                    reverseChargingController.mRtxLevel = -1;
                    reverseChargingController.mName = null;
                    reverseChargingController.fireReverseChanged();
                    return;
                } else {
                    int i10 = reverseChargingController.mCurrentRtxMode;
                    if (i10 == 1 && i6 != 1 && reverseChargingController.mReverse) {
                        if (i7 != 0) {
                            if (i7 == 1) {
                                reverseChargingController.logReverseStopEvent(4);
                            } else if (i7 == 2) {
                                reverseChargingController.logReverseStopEvent(3);
                            } else if (i7 == 3) {
                                reverseChargingController.logReverseStopEvent(102);
                            } else if (i7 == 4) {
                                reverseChargingController.logReverseStopEvent(110);
                            } else if (i7 == 15) {
                                reverseChargingController.logReverseStopEvent(8);
                            }
                        } else if (i6 != 2 || reverseChargingController.mCurrentRtxReceiverType == 0) {
                            reverseChargingController.logReverseStopEvent(1);
                        } else {
                            reverseChargingController.logReverseStopEvent(8);
                        }
                        GridLayoutManager$$ExternalSyntheticOutline1.m("Reverse charging error happened : ", i7, "ReverseChargingControl");
                    } else if (i10 != 1 && i6 == 1 && !reverseChargingController.mReverse) {
                        reverseChargingController.logReverseStartEvent(1);
                    }
                    if (reverseChargingController.mCurrentRtxMode != 1 && i6 == 1 && !reverseChargingController.mReverse && reverseChargingController.mDoesNfcConflictWithWlc && !reverseChargingController.mRestoreWlcNfcPollingMode) {
                        reverseChargingController.enableNfcPollingMode(false);
                        reverseChargingController.mRestoreWlcNfcPollingMode = true;
                    }
                    reverseChargingController.mCurrentRtxMode = i6;
                    reverseChargingController.mReverse = false;
                    reverseChargingController.mRtxLevel = -1;
                    reverseChargingController.mName = null;
                    if (i6 == 1) {
                        boolean z5 = reverseChargingController.mProvidingBattery;
                        if (z5 || !z4) {
                            if (z5 && !z4) {
                                if (z3) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("playSoundIfNecessary() play end charging sound: ");
                                    sb.append(z4);
                                    sb.append(", accType : ");
                                    sb.append(i8);
                                    sb.append(", mStartReConnected : ");
                                    KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb, reverseChargingController.mStartReconnected, "ReverseChargingControl");
                                }
                                if (!reverseChargingController.mStartReconnected) {
                                    if (i8 == 16 || i8 == 90 || i8 == 114) {
                                        objArr = 1;
                                    } else {
                                        objArr = null;
                                    }
                                    if (objArr != null) {
                                        reverseChargingController.mStartReconnected = true;
                                        if (z3) {
                                            Log.w("ReverseChargingControl", "playSoundIfNecessary() start reconnected");
                                        }
                                    }
                                }
                            }
                            str2 = null;
                        } else {
                            if (z3) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("playSoundIfNecessary() play start charging sound: ");
                                sb2.append(z4);
                                sb2.append(", accType : ");
                                sb2.append(i8);
                                sb2.append(", mStartReconnected : ");
                                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb2, reverseChargingController.mStartReconnected, "ReverseChargingControl");
                            }
                            if (reverseChargingController.mStartReconnected) {
                                if (i8 == 16 || i8 == 90 || i8 == 114) {
                                    objArr2 = 1;
                                } else {
                                    objArr2 = null;
                                }
                                if (objArr2 != null) {
                                    str2 = null;
                                    reverseChargingController.mStartReconnected = false;
                                }
                            }
                            str2 = reverseChargingController.mContext.getString(2131953165);
                            reverseChargingController.mStartReconnected = false;
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            reverseChargingController.playSound(RingtoneManager.getRingtone(reverseChargingController.mContext, new Uri.Builder().scheme("file").appendPath(str2).build()));
                        }
                        reverseChargingController.mProvidingBattery = z4;
                        reverseChargingController.mReverse = true;
                        if (!z4) {
                            if (z3) {
                                Log.d("ReverseChargingControl", "receiver is not available");
                            }
                            reverseChargingController.mRtxLevel = -1;
                            reverseChargingController.mCurrentRtxReceiverType = 0;
                        } else {
                            reverseChargingController.mStopReverseAtAcUnplug = false;
                            reverseChargingController.mRtxLevel = i9;
                            reverseChargingController.mUseRxRemovalTimeOut = true;
                            if (reverseChargingController.mCurrentRtxReceiverType != i8) {
                                if (z3) {
                                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("receiver type updated: ");
                                    m2.append(reverseChargingController.mCurrentRtxReceiverType);
                                    m2.append(" ");
                                    m2.append(i8);
                                    Log.d("ReverseChargingControl", m2.toString());
                                }
                                if (z3) {
                                    ExifInterface$$ExternalSyntheticOutline1.m("logReverseAccessoryType: ", i8, "ReverseChargingControl");
                                }
                                if (i8 != 0) {
                                    VendorAtom createVendorAtom = ReverseChargingMetrics.createVendorAtom(1);
                                    createVendorAtom.atomId = 100040;
                                    VendorAtomValue[] vendorAtomValueArr = createVendorAtom.values;
                                    if (i8 == 16 || i8 == 114) {
                                        i = 1;
                                    } else {
                                        i = 0;
                                    }
                                    vendorAtomValueArr[0] = VendorAtomValue.intValue(i);
                                    ReverseChargingMetrics.reportVendorAtom(createVendorAtom);
                                }
                                reverseChargingController.mCurrentRtxReceiverType = i8;
                            }
                        }
                    } else {
                        reverseChargingController.mStopReverseAtAcUnplug = false;
                        reverseChargingController.mProvidingBattery = false;
                        reverseChargingController.mUseRxRemovalTimeOut = false;
                        reverseChargingController.mStartReconnected = false;
                        if (reverseChargingController.mDoesNfcConflictWithWlc && reverseChargingController.mRestoreWlcNfcPollingMode) {
                            reverseChargingController.mRestoreWlcNfcPollingMode = false;
                            reverseChargingController.enableNfcPollingMode(!reverseChargingController.mRestoreUsbNfcPollingMode);
                        }
                    }
                    reverseChargingController.fireReverseChanged();
                    reverseChargingController.cancelRtxTimer(0);
                    reverseChargingController.cancelRtxTimer(1);
                    reverseChargingController.cancelRtxTimer(4);
                    if (!reverseChargingController.mStartReconnected) {
                        reverseChargingController.cancelRtxTimer(3);
                    }
                    boolean z6 = reverseChargingController.mReverse;
                    if (z6 && reverseChargingController.mRtxLevel == -1) {
                        if (reverseChargingController.mStartReconnected) {
                            if (i8 == 16) {
                                j = ReverseChargingController.DURATION_TO_ADVANCED_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
                            } else if (i8 == 114) {
                                j = ReverseChargingController.DURATION_TO_ADVANCED_PHONE_RECONNECTED_TIME_OUT;
                            } else {
                                j = ReverseChargingController.DURATION_TO_ADVANCED_PLUS_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
                            }
                        } else if (reverseChargingController.mStopReverseAtAcUnplug) {
                            j = ReverseChargingController.DURATION_TO_REVERSE_AC_TIME_OUT;
                        } else if (reverseChargingController.mUseRxRemovalTimeOut) {
                            j = ReverseChargingController.DURATION_TO_REVERSE_RX_REMOVAL_TIME_OUT;
                        } else {
                            j = ReverseChargingController.DURATION_TO_REVERSE_TIME_OUT;
                        }
                        if (reverseChargingController.mStopReverseAtAcUnplug) {
                            str = "rtx.ac.timeout";
                        } else {
                            str = "rtx.timeout";
                        }
                        String str3 = SystemProperties.get(str);
                        if (!TextUtils.isEmpty(str3)) {
                            try {
                                j = Long.parseLong(str3);
                            } catch (NumberFormatException e) {
                                Log.w("ReverseChargingControl", "getRtxTimeOut(): invalid timeout, " + e);
                            }
                        }
                        if (ReverseChargingController.DEBUG) {
                            Log.d("ReverseChargingControl", "onReverseStateChangedOnMainThread(): time out, setRtxTimer, duration=" + j);
                        }
                        if (reverseChargingController.mStartReconnected) {
                            i3 = 3;
                        } else if (reverseChargingController.mUseRxRemovalTimeOut && !reverseChargingController.mStopReverseAtAcUnplug) {
                            i3 = 4;
                        }
                        reverseChargingController.setRtxTimer(i3, j);
                        return;
                    } else if (z6 && reverseChargingController.mRtxLevel >= 100) {
                        if (z3) {
                            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("onReverseStateChangedOnMainThread(): rtx=");
                            m3.append(reverseChargingController.mReverse ? 1 : 0);
                            m3.append(", Rx fully charged, setRtxTimer, REVERSE_FINISH_RX_FULL");
                            Log.d("ReverseChargingControl", m3.toString());
                        }
                        reverseChargingController.setRtxTimer(1, 0L);
                        return;
                    } else {
                        return;
                    }
                }
        }
    }
}
