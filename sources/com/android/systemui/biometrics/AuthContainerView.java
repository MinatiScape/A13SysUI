package com.android.systemui.biometrics;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.AuthCredentialView;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.CastTile$$ExternalSyntheticLambda1;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AuthContainerView extends LinearLayout implements AuthDialog, WakefulnessLifecycle.Observer {
    public static final /* synthetic */ int $r8$clinit = 0;
    @VisibleForTesting
    public final ImageView mBackgroundView;
    @VisibleForTesting
    public final ScrollView mBiometricScrollView;
    @VisibleForTesting
    public AuthBiometricView mBiometricView;
    public final Config mConfig;
    public byte[] mCredentialAttestation;
    @VisibleForTesting
    public AuthCredentialView mCredentialView;
    public final int mEffectiveUserId;
    public final List<FaceSensorPropertiesInternal> mFaceProps;
    public final List<FingerprintSensorPropertiesInternal> mFpProps;
    @VisibleForTesting
    public final FrameLayout mFrameLayout;
    public final Injector mInjector;
    public final AuthPanelController mPanelController;
    public final View mPanelView;
    public Integer mPendingCallbackReason;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final Binder mWindowToken = new Binder();
    @VisibleForTesting
    public int mContainerState = 0;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final WindowManager mWindowManager = (WindowManager) ((LinearLayout) this).mContext.getSystemService(WindowManager.class);
    public final float mTranslationY = getResources().getDimension(2131165367);
    public final PathInterpolator mLinearOutSlowIn = Interpolators.LINEAR_OUT_SLOW_IN;
    @VisibleForTesting
    public final BiometricCallback mBiometricCallback = new BiometricCallback();
    public final CredentialCallback mCredentialCallback = new CredentialCallback();

    /* renamed from: com.android.systemui.biometrics.AuthContainerView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ModalityListener {
        public AnonymousClass1() {
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class BiometricCallback implements AuthBiometricView.Callback {
        public BiometricCallback() {
        }

        public final void onAction(int i) {
            switch (i) {
                case 1:
                    AuthContainerView.this.animateAway(4);
                    return;
                case 2:
                    AuthContainerView.this.sendEarlyUserCanceled();
                    AuthContainerView.this.animateAway(1);
                    return;
                case 3:
                    AuthContainerView.this.animateAway(2);
                    return;
                case 4:
                    AuthController authController = (AuthController) AuthContainerView.this.mConfig.mCallback;
                    Objects.requireNonNull(authController);
                    IBiometricSysuiReceiver iBiometricSysuiReceiver = authController.mReceiver;
                    if (iBiometricSysuiReceiver == null) {
                        Log.e("AuthController", "onTryAgainPressed: Receiver is null");
                        return;
                    }
                    try {
                        iBiometricSysuiReceiver.onTryAgainPressed();
                        return;
                    } catch (RemoteException e) {
                        Log.e("AuthController", "RemoteException when handling try again", e);
                        return;
                    }
                case 5:
                    AuthContainerView.this.animateAway(5);
                    return;
                case FalsingManager.VERSION /* 6 */:
                    AuthController authController2 = (AuthController) AuthContainerView.this.mConfig.mCallback;
                    Objects.requireNonNull(authController2);
                    IBiometricSysuiReceiver iBiometricSysuiReceiver2 = authController2.mReceiver;
                    if (iBiometricSysuiReceiver2 == null) {
                        Log.e("AuthController", "onDeviceCredentialPressed: Receiver is null");
                    } else {
                        try {
                            iBiometricSysuiReceiver2.onDeviceCredentialPressed();
                        } catch (RemoteException e2) {
                            Log.e("AuthController", "RemoteException when handling credential button", e2);
                        }
                    }
                    AuthContainerView authContainerView = AuthContainerView.this;
                    Handler handler = authContainerView.mHandler;
                    Runnable authContainerView$BiometricCallback$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.systemui.biometrics.AuthContainerView$BiometricCallback$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            AuthContainerView.BiometricCallback biometricCallback = AuthContainerView.BiometricCallback.this;
                            Objects.requireNonNull(biometricCallback);
                            AuthContainerView authContainerView2 = AuthContainerView.this;
                            int i2 = AuthContainerView.$r8$clinit;
                            authContainerView2.addCredentialView(false, true);
                        }
                    };
                    Objects.requireNonNull(authContainerView.mInjector);
                    handler.postDelayed(authContainerView$BiometricCallback$$ExternalSyntheticLambda0, 300);
                    return;
                case 7:
                    AuthController authController3 = (AuthController) AuthContainerView.this.mConfig.mCallback;
                    Objects.requireNonNull(authController3);
                    IBiometricSysuiReceiver iBiometricSysuiReceiver3 = authController3.mReceiver;
                    if (iBiometricSysuiReceiver3 == null) {
                        Log.e("AuthController", "onStartUdfpsNow: Receiver is null");
                        return;
                    }
                    try {
                        iBiometricSysuiReceiver3.onStartFingerprintNow();
                        return;
                    } catch (RemoteException e3) {
                        Log.e("AuthController", "RemoteException when sending onDialogAnimatedIn", e3);
                        return;
                    }
                default:
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unhandled action: ", i, "BiometricPrompt/AuthContainerView");
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Config {
        public AuthDialogCallback mCallback;
        public Context mContext;
        public String mOpPackageName;
        public long mOperationId;
        public PromptInfo mPromptInfo;
        public boolean mRequireConfirmation;
        public int[] mSensorIds;
        public boolean mSkipIntro;
        public int mUserId;
    }

    /* loaded from: classes.dex */
    public final class CredentialCallback implements AuthCredentialView.Callback {
        public CredentialCallback() {
        }
    }

    /* loaded from: classes.dex */
    public static class Injector {
    }

    @VisibleForTesting
    public void animateAway(int i) {
        animateAway(true, i);
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onStartedGoingToSleep() {
        animateAway(1);
    }

    @VisibleForTesting
    public AuthContainerView(Config config, Injector injector, List<FingerprintSensorPropertiesInternal> list, List<FaceSensorPropertiesInternal> list2, WakefulnessLifecycle wakefulnessLifecycle) {
        super(config.mContext);
        boolean z;
        int[] iArr;
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal;
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal2;
        this.mConfig = config;
        this.mInjector = injector;
        this.mFpProps = list;
        this.mFaceProps = list2;
        Context context = ((LinearLayout) this).mContext;
        Objects.requireNonNull(injector);
        this.mEffectiveUserId = UserManager.get(context).getCredentialOwnerProfile(config.mUserId);
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        LayoutInflater from = LayoutInflater.from(((LinearLayout) this).mContext);
        FrameLayout frameLayout = (FrameLayout) from.inflate(2131624007, (ViewGroup) this, false);
        this.mFrameLayout = frameLayout;
        View findViewById = frameLayout.findViewById(2131428556);
        this.mPanelView = findViewById;
        this.mPanelController = new AuthPanelController(((LinearLayout) this).mContext, findViewById);
        int length = config.mSensorIds.length;
        if ((config.mPromptInfo.getAuthenticators() & 255) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (length == 1) {
                int i = config.mSensorIds[0];
                if (Utils.containsSensorId(list, i)) {
                    Iterator<FingerprintSensorPropertiesInternal> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            fingerprintSensorPropertiesInternal2 = null;
                            break;
                        }
                        fingerprintSensorPropertiesInternal2 = it.next();
                        if (fingerprintSensorPropertiesInternal2.sensorId == i) {
                            break;
                        }
                    }
                    if (fingerprintSensorPropertiesInternal2.isAnyUdfpsType()) {
                        AuthBiometricUdfpsView authBiometricUdfpsView = (AuthBiometricUdfpsView) from.inflate(2131624006, (ViewGroup) null, false);
                        Objects.requireNonNull(authBiometricUdfpsView);
                        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = authBiometricUdfpsView.mMeasureAdapter;
                        if (udfpsDialogMeasureAdapter == null || udfpsDialogMeasureAdapter.mSensorProps != fingerprintSensorPropertiesInternal2) {
                            authBiometricUdfpsView.mMeasureAdapter = new UdfpsDialogMeasureAdapter(authBiometricUdfpsView, fingerprintSensorPropertiesInternal2);
                        }
                        this.mBiometricView = authBiometricUdfpsView;
                    } else {
                        this.mBiometricView = (AuthBiometricFingerprintView) from.inflate(2131624005, (ViewGroup) null, false);
                    }
                } else if (Utils.containsSensorId(list2, i)) {
                    this.mBiometricView = (AuthBiometricFaceView) from.inflate(2131624004, (ViewGroup) null, false);
                } else {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unknown sensorId: ", i, "BiometricPrompt/AuthContainerView");
                    this.mBiometricView = null;
                    this.mBackgroundView = null;
                    this.mBiometricScrollView = null;
                    return;
                }
            } else if (length == 2) {
                int i2 = -1;
                int i3 = -1;
                for (int i4 : config.mSensorIds) {
                    if (Utils.containsSensorId(this.mFpProps, i4)) {
                        i2 = i4;
                    } else if (Utils.containsSensorId(this.mFaceProps, i4)) {
                        i3 = i4;
                    }
                    if (!(i2 == -1 || i3 == -1)) {
                        break;
                    }
                }
                int[] iArr2 = {i3, i2};
                int i5 = iArr2[0];
                int i6 = iArr2[1];
                if (i6 == -1 || i5 == -1) {
                    Log.e("BiometricPrompt/AuthContainerView", "Missing fingerprint or face for dual-sensor config");
                    this.mBiometricView = null;
                    this.mBackgroundView = null;
                    this.mBiometricScrollView = null;
                    return;
                }
                Iterator<FingerprintSensorPropertiesInternal> it2 = this.mFpProps.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        fingerprintSensorPropertiesInternal = null;
                        break;
                    }
                    fingerprintSensorPropertiesInternal = it2.next();
                    if (fingerprintSensorPropertiesInternal.sensorId == i6) {
                        break;
                    }
                }
                if (fingerprintSensorPropertiesInternal != null) {
                    AuthBiometricFaceToFingerprintView authBiometricFaceToFingerprintView = (AuthBiometricFaceToFingerprintView) from.inflate(2131624003, (ViewGroup) null, false);
                    Objects.requireNonNull(authBiometricFaceToFingerprintView);
                    authBiometricFaceToFingerprintView.mFingerprintSensorProps = fingerprintSensorPropertiesInternal;
                    authBiometricFaceToFingerprintView.mModalityListener = new AnonymousClass1();
                    this.mBiometricView = authBiometricFaceToFingerprintView;
                } else {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Fingerprint props not found for sensor ID: ", i6, "BiometricPrompt/AuthContainerView");
                    this.mBiometricView = null;
                    this.mBackgroundView = null;
                    this.mBiometricScrollView = null;
                    return;
                }
            } else {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unsupported sensor array, length: ", length, "BiometricPrompt/AuthContainerView");
                this.mBiometricView = null;
                this.mBackgroundView = null;
                this.mBiometricScrollView = null;
                return;
            }
        }
        Injector injector2 = this.mInjector;
        FrameLayout frameLayout2 = this.mFrameLayout;
        Objects.requireNonNull(injector2);
        this.mBiometricScrollView = (ScrollView) frameLayout2.findViewById(2131427580);
        Injector injector3 = this.mInjector;
        FrameLayout frameLayout3 = this.mFrameLayout;
        Objects.requireNonNull(injector3);
        ImageView imageView = (ImageView) frameLayout3.findViewById(2131427539);
        this.mBackgroundView = imageView;
        addView(this.mFrameLayout);
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.mRequireConfirmation = this.mConfig.mRequireConfirmation;
            AuthPanelController authPanelController = this.mPanelController;
            Objects.requireNonNull(authBiometricView);
            authBiometricView.mPanelController = authPanelController;
            AuthBiometricView authBiometricView2 = this.mBiometricView;
            PromptInfo promptInfo = this.mConfig.mPromptInfo;
            Objects.requireNonNull(authBiometricView2);
            authBiometricView2.mPromptInfo = promptInfo;
            AuthBiometricView authBiometricView3 = this.mBiometricView;
            BiometricCallback biometricCallback = this.mBiometricCallback;
            Objects.requireNonNull(authBiometricView3);
            authBiometricView3.mCallback = biometricCallback;
            AuthBiometricView authBiometricView4 = this.mBiometricView;
            Objects.requireNonNull(authBiometricView4);
            imageView.setOnClickListener(authBiometricView4.mBackgroundClickListener);
            AuthBiometricView authBiometricView5 = this.mBiometricView;
            int i7 = this.mConfig.mUserId;
            Objects.requireNonNull(authBiometricView5);
            AuthBiometricView authBiometricView6 = this.mBiometricView;
            int i8 = this.mEffectiveUserId;
            Objects.requireNonNull(authBiometricView6);
            authBiometricView6.mEffectiveUserId = i8;
        }
        setOnKeyListener(new View.OnKeyListener() { // from class: com.android.systemui.biometrics.AuthContainerView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i9, KeyEvent keyEvent) {
                AuthContainerView authContainerView = AuthContainerView.this;
                Objects.requireNonNull(authContainerView);
                if (i9 != 4) {
                    return false;
                }
                if (keyEvent.getAction() == 1) {
                    authContainerView.sendEarlyUserCanceled();
                    authContainerView.animateAway(1);
                }
                return true;
            }
        });
        setImportantForAccessibility(2);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @VisibleForTesting
    public static WindowManager.LayoutParams getLayoutParams(IBinder iBinder, CharSequence charSequence) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2017, 16785408, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(layoutParams.getFitInsetsTypes() & (~WindowInsets.Type.ime()));
        layoutParams.setTitle("BiometricPrompt");
        layoutParams.accessibilityTitle = charSequence;
        layoutParams.token = iBinder;
        return layoutParams;
    }

    public final void addCredentialView(boolean z, boolean z2) {
        int i;
        LayoutInflater from = LayoutInflater.from(((LinearLayout) this).mContext);
        Injector injector = this.mInjector;
        Context context = ((LinearLayout) this).mContext;
        int i2 = this.mEffectiveUserId;
        Objects.requireNonNull(injector);
        int keyguardStoredPasswordQuality = new LockPatternUtils(context).getKeyguardStoredPasswordQuality(i2);
        if (keyguardStoredPasswordQuality == 65536) {
            i = 2;
        } else if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
            i = 1;
        } else {
            i = 3;
        }
        if (i != 1) {
            if (i == 2) {
                this.mCredentialView = (AuthCredentialView) from.inflate(2131624009, (ViewGroup) null, false);
                this.mBackgroundView.setOnClickListener(null);
                this.mBackgroundView.setImportantForAccessibility(2);
                AuthCredentialView authCredentialView = this.mCredentialView;
                Objects.requireNonNull(authCredentialView);
                authCredentialView.mContainerView = this;
                AuthCredentialView authCredentialView2 = this.mCredentialView;
                int i3 = this.mConfig.mUserId;
                Objects.requireNonNull(authCredentialView2);
                authCredentialView2.mUserId = i3;
                AuthCredentialView authCredentialView3 = this.mCredentialView;
                long j = this.mConfig.mOperationId;
                Objects.requireNonNull(authCredentialView3);
                authCredentialView3.mOperationId = j;
                AuthCredentialView authCredentialView4 = this.mCredentialView;
                int i4 = this.mEffectiveUserId;
                Objects.requireNonNull(authCredentialView4);
                authCredentialView4.mEffectiveUserId = i4;
                AuthCredentialView authCredentialView5 = this.mCredentialView;
                Objects.requireNonNull(authCredentialView5);
                authCredentialView5.mCredentialType = i;
                AuthCredentialView authCredentialView6 = this.mCredentialView;
                CredentialCallback credentialCallback = this.mCredentialCallback;
                Objects.requireNonNull(authCredentialView6);
                authCredentialView6.mCallback = credentialCallback;
                AuthCredentialView authCredentialView7 = this.mCredentialView;
                PromptInfo promptInfo = this.mConfig.mPromptInfo;
                Objects.requireNonNull(authCredentialView7);
                authCredentialView7.mPromptInfo = promptInfo;
                AuthCredentialView authCredentialView8 = this.mCredentialView;
                AuthPanelController authPanelController = this.mPanelController;
                Objects.requireNonNull(authCredentialView8);
                authCredentialView8.mPanelController = authPanelController;
                authCredentialView8.mShouldAnimatePanel = z;
                AuthCredentialView authCredentialView9 = this.mCredentialView;
                Objects.requireNonNull(authCredentialView9);
                authCredentialView9.mShouldAnimateContents = z2;
                this.mFrameLayout.addView(this.mCredentialView);
            } else if (i != 3) {
                throw new IllegalStateException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown credential type: ", i));
            }
        }
        this.mCredentialView = (AuthCredentialView) from.inflate(2131624008, (ViewGroup) null, false);
        this.mBackgroundView.setOnClickListener(null);
        this.mBackgroundView.setImportantForAccessibility(2);
        AuthCredentialView authCredentialView10 = this.mCredentialView;
        Objects.requireNonNull(authCredentialView10);
        authCredentialView10.mContainerView = this;
        AuthCredentialView authCredentialView22 = this.mCredentialView;
        int i32 = this.mConfig.mUserId;
        Objects.requireNonNull(authCredentialView22);
        authCredentialView22.mUserId = i32;
        AuthCredentialView authCredentialView32 = this.mCredentialView;
        long j2 = this.mConfig.mOperationId;
        Objects.requireNonNull(authCredentialView32);
        authCredentialView32.mOperationId = j2;
        AuthCredentialView authCredentialView42 = this.mCredentialView;
        int i42 = this.mEffectiveUserId;
        Objects.requireNonNull(authCredentialView42);
        authCredentialView42.mEffectiveUserId = i42;
        AuthCredentialView authCredentialView52 = this.mCredentialView;
        Objects.requireNonNull(authCredentialView52);
        authCredentialView52.mCredentialType = i;
        AuthCredentialView authCredentialView62 = this.mCredentialView;
        CredentialCallback credentialCallback2 = this.mCredentialCallback;
        Objects.requireNonNull(authCredentialView62);
        authCredentialView62.mCallback = credentialCallback2;
        AuthCredentialView authCredentialView72 = this.mCredentialView;
        PromptInfo promptInfo2 = this.mConfig.mPromptInfo;
        Objects.requireNonNull(authCredentialView72);
        authCredentialView72.mPromptInfo = promptInfo2;
        AuthCredentialView authCredentialView82 = this.mCredentialView;
        AuthPanelController authPanelController2 = this.mPanelController;
        Objects.requireNonNull(authCredentialView82);
        authCredentialView82.mPanelController = authPanelController2;
        authCredentialView82.mShouldAnimatePanel = z;
        AuthCredentialView authCredentialView92 = this.mCredentialView;
        Objects.requireNonNull(authCredentialView92);
        authCredentialView92.mShouldAnimateContents = z2;
        this.mFrameLayout.addView(this.mCredentialView);
    }

    public final void animateAway(boolean z, int i) {
        int i2 = this.mContainerState;
        if (i2 == 1) {
            Log.w("BiometricPrompt/AuthContainerView", "startDismiss(): waiting for onDialogAnimatedIn");
            this.mContainerState = 2;
        } else if (i2 == 4) {
            Log.w("BiometricPrompt/AuthContainerView", "Already dismissing, sendReason: " + z + " reason: " + i);
        } else {
            this.mContainerState = 4;
            if (z) {
                this.mPendingCallbackReason = Integer.valueOf(i);
            } else {
                this.mPendingCallbackReason = null;
            }
            postOnAnimation(new CastTile$$ExternalSyntheticLambda1(this, new TaskView$$ExternalSyntheticLambda4(this, 4), 1));
        }
    }

    @VisibleForTesting
    public void onAttachedToWindowInternal() {
        boolean z;
        boolean z2;
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.add(this);
        if ((this.mConfig.mPromptInfo.getAuthenticators() & 255) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mBiometricScrollView.addView(this.mBiometricView);
        } else {
            if ((this.mConfig.mPromptInfo.getAuthenticators() & 32768) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                addCredentialView(true, false);
            } else {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown configuration: ");
                m.append(this.mConfig.mPromptInfo.getAuthenticators());
                throw new IllegalStateException(m.toString());
            }
        }
        maybeUpdatePositionForUdfps(false);
        if (this.mConfig.mSkipIntro) {
            this.mContainerState = 3;
            return;
        }
        this.mContainerState = 1;
        this.mPanelView.setY(this.mTranslationY);
        this.mBiometricScrollView.setY(this.mTranslationY);
        setAlpha(0.0f);
        postOnAnimation(new CarrierTextManager$$ExternalSyntheticLambda0(this, 3));
    }

    @VisibleForTesting
    public void onDialogAnimatedIn() {
        if (this.mContainerState == 2) {
            Log.d("BiometricPrompt/AuthContainerView", "onDialogAnimatedIn(): mPendingDismissDialog=true, dismissing now");
            animateAway(1);
            return;
        }
        this.mContainerState = 3;
        if (this.mBiometricView != null) {
            AuthController authController = (AuthController) this.mConfig.mCallback;
            Objects.requireNonNull(authController);
            IBiometricSysuiReceiver iBiometricSysuiReceiver = authController.mReceiver;
            if (iBiometricSysuiReceiver == null) {
                Log.e("AuthController", "onDialogAnimatedIn: Receiver is null");
            } else {
                try {
                    iBiometricSysuiReceiver.onDialogAnimatedIn();
                } catch (RemoteException e) {
                    Log.e("AuthController", "RemoteException when sending onDialogAnimatedIn", e);
                }
            }
            AuthBiometricView authBiometricView = this.mBiometricView;
            Objects.requireNonNull(authBiometricView);
            authBiometricView.updateState(2);
        }
    }

    public final void removeWindowIfAttached() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("pendingCallback: ");
        m.append(this.mPendingCallbackReason);
        Log.d("BiometricPrompt/AuthContainerView", m.toString());
        Integer num = this.mPendingCallbackReason;
        if (num != null) {
            AuthDialogCallback authDialogCallback = this.mConfig.mCallback;
            int intValue = num.intValue();
            byte[] bArr = this.mCredentialAttestation;
            AuthController authController = (AuthController) authDialogCallback;
            switch (intValue) {
                case 1:
                    authController.sendResultAndCleanUp(3, bArr);
                    break;
                case 2:
                    authController.sendResultAndCleanUp(2, bArr);
                    break;
                case 3:
                    authController.sendResultAndCleanUp(1, bArr);
                    break;
                case 4:
                    authController.sendResultAndCleanUp(4, bArr);
                    break;
                case 5:
                    authController.sendResultAndCleanUp(5, bArr);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    authController.sendResultAndCleanUp(6, bArr);
                    break;
                case 7:
                    authController.sendResultAndCleanUp(7, bArr);
                    break;
                default:
                    Objects.requireNonNull(authController);
                    Log.e("AuthController", "Unhandled reason: " + intValue);
                    break;
            }
            this.mPendingCallbackReason = null;
        }
        if (this.mContainerState != 5) {
            this.mContainerState = 5;
            this.mWindowManager.removeView(this);
        }
    }

    public final void sendEarlyUserCanceled() {
        AuthController authController = (AuthController) this.mConfig.mCallback;
        Objects.requireNonNull(authController);
        IBiometricSysuiReceiver iBiometricSysuiReceiver = authController.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onSystemEvent(1): Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onSystemEvent(1);
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending system event", e);
        }
    }

    public final void setScrollViewGravity(int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mBiometricScrollView.getLayoutParams();
        layoutParams.gravity = i;
        this.mBiometricScrollView.setLayoutParams(layoutParams);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0024, code lost:
        if (r2.mFingerprintSensorProps.isAnyUdfpsType() != false) goto L_0x0026;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x002b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean maybeUpdatePositionForUdfps(boolean r7) {
        /*
            r6 = this;
            android.view.Display r0 = r6.getDisplay()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            com.android.systemui.biometrics.AuthBiometricView r2 = r6.mBiometricView
            boolean r3 = r2 instanceof com.android.systemui.biometrics.AuthBiometricUdfpsView
            r4 = 2
            r5 = 1
            if (r3 == 0) goto L_0x0011
            goto L_0x0026
        L_0x0011:
            boolean r3 = r2 instanceof com.android.systemui.biometrics.AuthBiometricFaceToFingerprintView
            if (r3 == 0) goto L_0x0028
            com.android.systemui.biometrics.AuthBiometricFaceToFingerprintView r2 = (com.android.systemui.biometrics.AuthBiometricFaceToFingerprintView) r2
            java.util.Objects.requireNonNull(r2)
            int r3 = r2.mActiveSensorType
            if (r3 != r4) goto L_0x0028
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r2 = r2.mFingerprintSensorProps
            boolean r2 = r2.isAnyUdfpsType()
            if (r2 == 0) goto L_0x0028
        L_0x0026:
            r2 = r5
            goto L_0x0029
        L_0x0028:
            r2 = r1
        L_0x0029:
            if (r2 != 0) goto L_0x002c
            return r1
        L_0x002c:
            int r0 = r0.getRotation()
            r1 = 81
            if (r0 == 0) goto L_0x0065
            r2 = 3
            if (r0 == r5) goto L_0x0058
            if (r0 == r2) goto L_0x004b
            java.lang.String r2 = "Unsupported display rotation: "
            java.lang.String r3 = "BiometricPrompt/AuthContainerView"
            com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m(r2, r0, r3)
            com.android.systemui.biometrics.AuthPanelController r0 = r6.mPanelController
            java.util.Objects.requireNonNull(r0)
            r0.mPosition = r5
            r6.setScrollViewGravity(r1)
            goto L_0x006f
        L_0x004b:
            com.android.systemui.biometrics.AuthPanelController r0 = r6.mPanelController
            java.util.Objects.requireNonNull(r0)
            r0.mPosition = r4
            r0 = 19
            r6.setScrollViewGravity(r0)
            goto L_0x006f
        L_0x0058:
            com.android.systemui.biometrics.AuthPanelController r0 = r6.mPanelController
            java.util.Objects.requireNonNull(r0)
            r0.mPosition = r2
            r0 = 21
            r6.setScrollViewGravity(r0)
            goto L_0x006f
        L_0x0065:
            com.android.systemui.biometrics.AuthPanelController r0 = r6.mPanelController
            java.util.Objects.requireNonNull(r0)
            r0.mPosition = r5
            r6.setScrollViewGravity(r1)
        L_0x006f:
            if (r7 == 0) goto L_0x007b
            android.view.View r7 = r6.mPanelView
            r7.invalidateOutline()
            com.android.systemui.biometrics.AuthBiometricView r6 = r6.mBiometricView
            r6.requestLayout()
        L_0x007b:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthContainerView.maybeUpdatePositionForUdfps(boolean):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        onAttachedToWindowInternal();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.remove(this);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        AuthPanelController authPanelController = this.mPanelController;
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        Objects.requireNonNull(authPanelController);
        authPanelController.mContainerWidth = measuredWidth;
        authPanelController.mContainerHeight = measuredHeight;
    }
}
