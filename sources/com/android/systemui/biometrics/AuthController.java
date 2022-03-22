package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintStateListener;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.WindowManager;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$id;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class AuthController extends CoreStartable implements CommandQueue.Callbacks, AuthDialogCallback, DozeReceiver {
    public final ActivityTaskManager mActivityTaskManager;
    public boolean mAllAuthenticatorsRegistered;
    public IBiometricContextListener mBiometricContextListener;
    @VisibleForTesting
    public final BroadcastReceiver mBroadcastReceiver;
    public final CommandQueue mCommandQueue;
    @VisibleForTesting
    public AuthDialog mCurrentDialog;
    public SomeArgs mCurrentDialogArgs;
    public final Execution mExecution;
    public final PointF mFaceAuthSensorLocation;
    public final List<FaceSensorPropertiesInternal> mFaceProps;
    public PointF mFingerprintLocation;
    public final FingerprintManager mFingerprintManager;
    public List<FingerprintSensorPropertiesInternal> mFpProps;
    public final Handler mHandler;
    @VisibleForTesting
    public final BiometricDisplayListener mOrientationListener;
    @VisibleForTesting
    public IBiometricSysuiReceiver mReceiver;
    public final SensorPrivacyManager mSensorPrivacyManager;
    public final Provider<SidefpsController> mSidefpsControllerFactory;
    public ArrayList mSidefpsProps;
    public final StatusBarStateController mStatusBarStateController;
    @VisibleForTesting
    public TaskStackListener mTaskStackListener;
    public UdfpsController mUdfpsController;
    public final Provider<UdfpsController> mUdfpsControllerFactory;
    public IUdfpsHbmListener mUdfpsHbmListener;
    public ArrayList mUdfpsProps;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WindowManager mWindowManager;
    public final HashSet mCallbacks = new HashSet();
    public final AnonymousClass1 mFingerprintAuthenticatorsRegisteredCallback = new AnonymousClass1();
    public final AnonymousClass2 mFingerprintStateListener = new AnonymousClass2();
    public final SparseBooleanArray mUdfpsEnrolledForUser = new SparseBooleanArray();

    /* renamed from: com.android.systemui.biometrics.AuthController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IFingerprintAuthenticatorsRegisteredCallback.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
        }

        public final void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
            AuthController.this.mHandler.post(new BubblesManager$5$$ExternalSyntheticLambda1(this, list, 1));
        }
    }

    /* renamed from: com.android.systemui.biometrics.AuthController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends FingerprintStateListener {
        public AnonymousClass2() {
        }

        public final void onEnrollmentsChanged(final int i, final int i2, final boolean z) {
            AuthController.this.mHandler.post(new Runnable() { // from class: com.android.systemui.biometrics.AuthController$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AuthController.AnonymousClass2 r0 = AuthController.AnonymousClass2.this;
                    int i3 = i;
                    int i4 = i2;
                    boolean z2 = z;
                    Objects.requireNonNull(r0);
                    AuthController authController = AuthController.this;
                    Objects.requireNonNull(authController);
                    authController.mExecution.assertIsMainThread();
                    Log.d("AuthController", "handleEnrollmentsChanged, userId: " + i3 + ", sensorId: " + i4 + ", hasEnrollments: " + z2);
                    ArrayList<FingerprintSensorPropertiesInternal> arrayList = authController.mUdfpsProps;
                    if (arrayList == null) {
                        Log.d("AuthController", "handleEnrollmentsChanged, mUdfpsProps is null");
                    } else {
                        for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : arrayList) {
                            if (fingerprintSensorPropertiesInternal.sensorId == i4) {
                                authController.mUdfpsEnrolledForUser.put(i3, z2);
                            }
                        }
                    }
                    Iterator it = authController.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((AuthController.Callback) it.next()).onEnrollmentsChanged();
                    }
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public class BiometricTaskStackListener extends TaskStackListener {
        public static final /* synthetic */ int $r8$clinit = 0;

        public BiometricTaskStackListener() {
        }

        public final void onTaskStackChanged() {
            AuthController authController = AuthController.this;
            authController.mHandler.post(new WifiEntry$$ExternalSyntheticLambda2(authController, 2));
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        default void onAllAuthenticatorsRegistered() {
        }

        default void onBiometricPromptDismissed() {
        }

        default void onBiometricPromptShown() {
        }

        default void onEnrollmentsChanged() {
        }
    }

    public AuthController(Context context, Execution execution, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateController statusBarStateController, Handler handler) {
        super(context);
        List<FaceSensorPropertiesInternal> list;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.biometrics.AuthController.3
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                if (AuthController.this.mCurrentDialog != null && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.w("AuthController", "ACTION_CLOSE_SYSTEM_DIALOGS received");
                    AuthContainerView authContainerView = (AuthContainerView) AuthController.this.mCurrentDialog;
                    Objects.requireNonNull(authContainerView);
                    authContainerView.animateAway(false, 0);
                    AuthController authController = AuthController.this;
                    authController.mCurrentDialog = null;
                    authController.mOrientationListener.disable();
                    Iterator it = AuthController.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((Callback) it.next()).onBiometricPromptDismissed();
                    }
                    try {
                        IBiometricSysuiReceiver iBiometricSysuiReceiver = AuthController.this.mReceiver;
                        if (iBiometricSysuiReceiver != null) {
                            iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                            AuthController.this.mReceiver = null;
                        }
                    } catch (RemoteException e) {
                        Log.e("AuthController", "Remote exception", e);
                    }
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mExecution = execution;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mHandler = handler;
        this.mCommandQueue = commandQueue;
        this.mActivityTaskManager = activityTaskManager;
        this.mFingerprintManager = fingerprintManager;
        this.mUdfpsControllerFactory = provider;
        this.mSidefpsControllerFactory = provider2;
        this.mWindowManager = windowManager;
        this.mOrientationListener = new BiometricDisplayListener(context, displayManager, handler, BiometricDisplayListener.SensorType.Generic.INSTANCE, new Function0() { // from class: com.android.systemui.biometrics.AuthController$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                AuthController authController = AuthController.this;
                Objects.requireNonNull(authController);
                authController.updateFingerprintLocation();
                AuthDialog authDialog = authController.mCurrentDialog;
                if (authDialog != null) {
                    ((AuthContainerView) authDialog).maybeUpdatePositionForUdfps(true);
                }
                return Unit.INSTANCE;
            }
        });
        this.mStatusBarStateController = statusBarStateController;
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.biometrics.AuthController.4
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onDozingChanged(boolean z) {
                AuthController authController = AuthController.this;
                Objects.requireNonNull(authController);
                IBiometricContextListener iBiometricContextListener = authController.mBiometricContextListener;
                if (iBiometricContextListener != null) {
                    try {
                        iBiometricContextListener.onDozeChanged(z);
                    } catch (RemoteException unused) {
                        Log.w("AuthController", "failed to notify initial doze state");
                    }
                }
            }
        });
        if (faceManager != null) {
            list = faceManager.getSensorPropertiesInternal();
        } else {
            list = null;
        }
        this.mFaceProps = list;
        int[] intArray = context.getResources().getIntArray(2130903093);
        if (intArray == null || intArray.length < 2) {
            this.mFaceAuthSensorLocation = null;
        } else {
            this.mFaceAuthSensorLocation = new PointF(intArray[0], intArray[1]);
        }
        updateFingerprintLocation();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(broadcastReceiver, intentFilter, 2);
        this.mSensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onBiometricError(int i, int i2, int i3) {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        boolean z4 = true;
        Log.d("AuthController", String.format("onBiometricError(%d, %d, %d)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
        if (i2 == 7 || i2 == 9) {
            z = true;
        } else {
            z = false;
        }
        if (i2 != 1 || !this.mSensorPrivacyManager.isSensorPrivacyEnabled(2, this.mCurrentDialogArgs.argi1)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (i2 == 100 || i2 == 3 || z2) {
            z3 = true;
        } else {
            z3 = false;
        }
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            if ((((AuthContainerView) authDialog).mConfig.mPromptInfo.getAuthenticators() & 32768) == 0) {
                z4 = false;
            }
            if (!z4 || !z) {
                String str2 = "";
                if (z3) {
                    if (i2 == 100) {
                        str = this.mContext.getString(17039796);
                    } else {
                        if (i == 2) {
                            str2 = FingerprintManager.getErrorString(this.mContext, i2, i3);
                        } else if (i == 8) {
                            str2 = FaceManager.getErrorString(this.mContext, i2, i3);
                        }
                        str = str2;
                    }
                    DialogFragment$$ExternalSyntheticOutline0.m("onBiometricError, soft error: ", str, "AuthController");
                    if (z2) {
                        this.mHandler.postDelayed(new AuthController$$ExternalSyntheticLambda0(this, i, 0), 500L);
                    } else {
                        AuthContainerView authContainerView = (AuthContainerView) this.mCurrentDialog;
                        Objects.requireNonNull(authContainerView);
                        authContainerView.mBiometricView.onAuthenticationFailed(i, str);
                    }
                } else {
                    if (i == 2) {
                        str2 = FingerprintManager.getErrorString(this.mContext, i2, i3);
                    } else if (i == 8) {
                        str2 = FaceManager.getErrorString(this.mContext, i2, i3);
                    }
                    DialogFragment$$ExternalSyntheticOutline0.m("onBiometricError, hard error: ", str2, "AuthController");
                    AuthContainerView authContainerView2 = (AuthContainerView) this.mCurrentDialog;
                    Objects.requireNonNull(authContainerView2);
                    authContainerView2.mBiometricView.onError(i, str2);
                }
            } else {
                Log.d("AuthController", "onBiometricError, lockout");
                AuthContainerView authContainerView3 = (AuthContainerView) this.mCurrentDialog;
                Objects.requireNonNull(authContainerView3);
                authContainerView3.mBiometricView.startTransitionToCredentialUI();
            }
        } else {
            Log.w("AuthController", "onBiometricError callback but dialog is gone");
        }
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.onCancelUdfps();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        int authenticators = promptInfo.getAuthenticators();
        StringBuilder sb = new StringBuilder();
        boolean z3 = false;
        for (int i3 : iArr) {
            sb.append(i3);
            sb.append(" ");
        }
        StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("showAuthenticationDialog, authenticators: ", authenticators, ", sensorIds: ");
        m.append(sb.toString());
        m.append(", credentialAllowed: ");
        m.append(z);
        m.append(", requireConfirmation: ");
        m.append(z2);
        m.append(", operationId: ");
        m.append(j);
        m.append(", requestId: ");
        m.append(j2);
        m.append(", multiSensorConfig: ");
        m.append(i2);
        Log.d("AuthController", m.toString());
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = promptInfo;
        obtain.arg2 = iBiometricSysuiReceiver;
        obtain.arg3 = iArr;
        obtain.arg4 = Boolean.valueOf(z);
        obtain.arg5 = Boolean.valueOf(z2);
        obtain.argi1 = i;
        obtain.arg6 = str;
        obtain.argl1 = j;
        obtain.argl2 = j2;
        obtain.argi2 = i2;
        if (this.mCurrentDialog != null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("mCurrentDialog: ");
            m2.append(this.mCurrentDialog);
            Log.w("AuthController", m2.toString());
            z3 = true;
        }
        showDialog(obtain, z3, null);
    }

    public final void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public final void dozeTimeTick() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dozeTimeTick();
        }
    }

    public final PointF getUdfpsSensorLocation() {
        if (this.mUdfpsController == null) {
            return null;
        }
        return new PointF(this.mUdfpsController.getSensorLocation().centerX(), this.mUdfpsController.getSensorLocation().centerY());
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void hideAuthenticationDialog() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("hideAuthenticationDialog: ");
        m.append(this.mCurrentDialog);
        Log.d("AuthController", m.toString());
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.d("AuthController", "dialog already gone");
            return;
        }
        AuthContainerView authContainerView = (AuthContainerView) authDialog;
        Objects.requireNonNull(authContainerView);
        authContainerView.animateAway(false, 0);
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    public final boolean isUdfpsEnrolled(int i) {
        if (this.mUdfpsController == null) {
            return false;
        }
        return this.mUdfpsEnrolledForUser.get(i);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onBiometricAuthenticated() {
        Log.d("AuthController", "onBiometricAuthenticated: ");
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            AuthContainerView authContainerView = (AuthContainerView) authDialog;
            Objects.requireNonNull(authContainerView);
            AuthBiometricView authBiometricView = authContainerView.mBiometricView;
            Objects.requireNonNull(authBiometricView);
            authBiometricView.removePendingAnimations();
            if (authBiometricView.mRequireConfirmation) {
                authBiometricView.updateState(5);
            } else {
                authBiometricView.updateState(6);
            }
        } else {
            Log.w("AuthController", "onBiometricAuthenticated callback but dialog gone");
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onBiometricHelp(int i, String str) {
        DialogFragment$$ExternalSyntheticOutline0.m("onBiometricHelp: ", str, "AuthController");
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            ((AuthContainerView) authDialog).mBiometricView.onHelp(str);
        } else {
            Log.w("AuthController", "onBiometricHelp callback but dialog gone");
        }
    }

    public final void sendResultAndCleanUp(int i, byte[] bArr) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "sendResultAndCleanUp: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogDismissed(i, bArr);
        } catch (RemoteException e) {
            Log.w("AuthController", "Remote exception", e);
        }
        ExifInterface$$ExternalSyntheticOutline1.m("onDialogDismissed: ", i, "AuthController");
        if (this.mCurrentDialog == null) {
            Log.w("AuthController", "Dialog already dismissed");
        }
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onBiometricPromptDismissed();
        }
        this.mReceiver = null;
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        this.mBiometricContextListener = iBiometricContextListener;
        boolean isDozing = this.mStatusBarStateController.isDozing();
        IBiometricContextListener iBiometricContextListener2 = this.mBiometricContextListener;
        if (iBiometricContextListener2 != null) {
            try {
                iBiometricContextListener2.onDozeChanged(isDozing);
            } catch (RemoteException unused) {
                Log.w("AuthController", "failed to notify initial doze state");
            }
        }
    }

    public final void showDialog(SomeArgs someArgs, boolean z, Bundle bundle) {
        this.mCurrentDialogArgs = someArgs;
        ((Boolean) someArgs.arg4).booleanValue();
        boolean booleanValue = ((Boolean) someArgs.arg5).booleanValue();
        int i = someArgs.argi1;
        long j = someArgs.argl1;
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Context context = this.mContext;
        AuthContainerView.Config config = new AuthContainerView.Config();
        config.mContext = context;
        config.mCallback = this;
        config.mPromptInfo = (PromptInfo) someArgs.arg1;
        config.mRequireConfirmation = booleanValue;
        config.mUserId = i;
        config.mOpPackageName = (String) someArgs.arg6;
        config.mSkipIntro = z;
        config.mOperationId = j;
        List<FingerprintSensorPropertiesInternal> list = this.mFpProps;
        List<FaceSensorPropertiesInternal> list2 = this.mFaceProps;
        config.mSensorIds = (int[]) someArgs.arg3;
        AuthContainerView authContainerView = new AuthContainerView(config, new AuthContainerView.Injector(), list, list2, wakefulnessLifecycle);
        Log.d("AuthController", "userId: " + i + " savedState: " + bundle + " mCurrentDialog: " + this.mCurrentDialog + " newDialog: " + authContainerView);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            ((AuthContainerView) authDialog).removeWindowIfAttached();
        }
        this.mReceiver = (IBiometricSysuiReceiver) someArgs.arg2;
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onBiometricPromptShown();
        }
        this.mCurrentDialog = authContainerView;
        WindowManager windowManager = this.mWindowManager;
        AuthBiometricView authBiometricView = authContainerView.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.restoreState(bundle);
        }
        windowManager.addView(authContainerView, AuthContainerView.getLayoutParams(authContainerView.mWindowToken, authContainerView.mConfig.mPromptInfo.getTitle()));
        this.mOrientationListener.enable();
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            fingerprintManager.addAuthenticatorsRegisteredCallback(this.mFingerprintAuthenticatorsRegisteredCallback);
        }
        BiometricTaskStackListener biometricTaskStackListener = new BiometricTaskStackListener();
        this.mTaskStackListener = biometricTaskStackListener;
        this.mActivityTaskManager.registerTaskStackListener(biometricTaskStackListener);
    }

    public final void updateFingerprintLocation() {
        int width = R$id.getWidth(this.mContext) / 2;
        try {
            width = this.mContext.getResources().getDimensionPixelSize(2131166767);
        } catch (Resources.NotFoundException unused) {
        }
        this.mFingerprintLocation = new PointF(width, this.mContext.getResources().getDimensionPixelSize(2131166768));
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        boolean z;
        updateFingerprintLocation();
        if (this.mCurrentDialog != null) {
            Bundle bundle = new Bundle();
            AuthContainerView authContainerView = (AuthContainerView) this.mCurrentDialog;
            Objects.requireNonNull(authContainerView);
            bundle.putInt("container_state", authContainerView.mContainerState);
            boolean z2 = false;
            if (authContainerView.mBiometricView == null || authContainerView.mCredentialView != null) {
                z = false;
            } else {
                z = true;
            }
            bundle.putBoolean("biometric_showing", z);
            if (authContainerView.mCredentialView != null) {
                z2 = true;
            }
            bundle.putBoolean("credential_showing", z2);
            AuthBiometricView authBiometricView = authContainerView.mBiometricView;
            if (authBiometricView != null) {
                authBiometricView.onSaveState(bundle);
            }
            AuthContainerView authContainerView2 = (AuthContainerView) this.mCurrentDialog;
            Objects.requireNonNull(authContainerView2);
            authContainerView2.removeWindowIfAttached();
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
            if (bundle.getInt("container_state") != 4) {
                if (bundle.getBoolean("credential_showing")) {
                    ((PromptInfo) this.mCurrentDialogArgs.arg1).setAuthenticators(32768);
                }
                showDialog(this.mCurrentDialogArgs, true, bundle);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        this.mUdfpsHbmListener = iUdfpsHbmListener;
    }
}
