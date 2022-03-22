package com.android.keyguard;

import android.app.admin.IKeyguardCallback;
import android.app.admin.IKeyguardClient;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.power.PowerUI$$ExternalSyntheticLambda0;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AdminSecondaryLockScreenController {
    public IKeyguardClient mClient;
    public final Context mContext;
    public Handler mHandler;
    public KeyguardSecurityCallback mKeyguardCallback;
    public final KeyguardSecurityContainer mParent;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public AdminSecurityView mView;
    public final AnonymousClass1 mConnection = new ServiceConnection() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.1
        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AdminSecondaryLockScreenController.this.mClient = IKeyguardClient.Stub.asInterface(iBinder);
            if (AdminSecondaryLockScreenController.this.mView.isAttachedToWindow()) {
                AdminSecondaryLockScreenController adminSecondaryLockScreenController = AdminSecondaryLockScreenController.this;
                if (adminSecondaryLockScreenController.mClient != null) {
                    AdminSecondaryLockScreenController.m17$$Nest$monSurfaceReady(adminSecondaryLockScreenController);
                    try {
                        iBinder.linkToDeath(AdminSecondaryLockScreenController.this.mKeyguardClientDeathRecipient, 0);
                    } catch (RemoteException e) {
                        Log.e("AdminSecondaryLockScreenController", "Lost connection to secondary lockscreen service", e);
                        AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
                    }
                }
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            AdminSecondaryLockScreenController.this.mClient = null;
        }
    };
    public final AdminSecondaryLockScreenController$$ExternalSyntheticLambda0 mKeyguardClientDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.keyguard.AdminSecondaryLockScreenController$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            AdminSecondaryLockScreenController adminSecondaryLockScreenController = AdminSecondaryLockScreenController.this;
            Objects.requireNonNull(adminSecondaryLockScreenController);
            adminSecondaryLockScreenController.hide();
            Log.d("AdminSecondaryLockScreenController", "KeyguardClient service died");
        }
    };
    public final AnonymousClass2 mCallback = new AnonymousClass2();
    public final AnonymousClass3 mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onSecondaryLockscreenRequirementChanged(int i) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = AdminSecondaryLockScreenController.this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (((Intent) keyguardUpdateMonitor.mSecondaryLockscreenRequirement.get(Integer.valueOf(i))) == null) {
                AdminSecondaryLockScreenController.this.dismiss(i);
            }
        }
    };
    @VisibleForTesting
    public SurfaceHolder.Callback mSurfaceHolderCallback = new AnonymousClass4();

    /* renamed from: com.android.keyguard.AdminSecondaryLockScreenController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends IKeyguardCallback.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass2() {
        }

        public final void onDismiss() {
            AdminSecondaryLockScreenController.this.mHandler.post(new PowerUI$$ExternalSyntheticLambda0(this, 1));
        }

        public final void onRemoteContentReady(SurfaceControlViewHost.SurfacePackage surfacePackage) {
            Handler handler = AdminSecondaryLockScreenController.this.mHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if (surfacePackage != null) {
                AdminSecondaryLockScreenController.this.mView.setChildSurfacePackage(surfacePackage);
            } else {
                AdminSecondaryLockScreenController.this.mHandler.post(new ScreenDecorations$$ExternalSyntheticLambda1(this, 1));
            }
        }
    }

    /* renamed from: com.android.keyguard.AdminSecondaryLockScreenController$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements SurfaceHolder.Callback {
        @Override // android.view.SurfaceHolder.Callback
        public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        public AnonymousClass4() {
        }

        @Override // android.view.SurfaceHolder.Callback
        public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            AdminSecondaryLockScreenController adminSecondaryLockScreenController = AdminSecondaryLockScreenController.this;
            adminSecondaryLockScreenController.mUpdateMonitor.removeCallback(adminSecondaryLockScreenController.mUpdateCallback);
        }

        @Override // android.view.SurfaceHolder.Callback
        public final void surfaceCreated(SurfaceHolder surfaceHolder) {
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            AdminSecondaryLockScreenController adminSecondaryLockScreenController = AdminSecondaryLockScreenController.this;
            adminSecondaryLockScreenController.mUpdateMonitor.registerCallback(adminSecondaryLockScreenController.mUpdateCallback);
            AdminSecondaryLockScreenController adminSecondaryLockScreenController2 = AdminSecondaryLockScreenController.this;
            if (adminSecondaryLockScreenController2.mClient != null) {
                AdminSecondaryLockScreenController.m17$$Nest$monSurfaceReady(adminSecondaryLockScreenController2);
            }
            AdminSecondaryLockScreenController.this.mHandler.postDelayed(new Runnable() { // from class: com.android.keyguard.AdminSecondaryLockScreenController$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AdminSecondaryLockScreenController.AnonymousClass4 r0 = AdminSecondaryLockScreenController.AnonymousClass4.this;
                    int i = currentUser;
                    Objects.requireNonNull(r0);
                    AdminSecondaryLockScreenController.this.dismiss(i);
                    Log.w("AdminSecondaryLockScreenController", "Timed out waiting for secondary lockscreen content.");
                }
            }, 500L);
        }
    }

    /* loaded from: classes.dex */
    public class AdminSecurityView extends SurfaceView {
        public SurfaceHolder.Callback mSurfaceHolderCallback;

        public AdminSecurityView(Context context, SurfaceHolder.Callback callback) {
            super(context);
            this.mSurfaceHolderCallback = callback;
            setZOrderOnTop(true);
        }

        @Override // android.view.SurfaceView, android.view.View
        public final void onAttachedToWindow() {
            super.onAttachedToWindow();
            getHolder().addCallback(this.mSurfaceHolderCallback);
        }

        @Override // android.view.SurfaceView, android.view.View
        public final void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            getHolder().removeCallback(this.mSurfaceHolderCallback);
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final Context mContext;
        public final Handler mHandler;
        public final KeyguardSecurityContainer mParent;
        public final KeyguardUpdateMonitor mUpdateMonitor;

        public Factory(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler) {
            this.mContext = context;
            this.mParent = keyguardSecurityContainer;
            this.mUpdateMonitor = keyguardUpdateMonitor;
            this.mHandler = handler;
        }
    }

    public final void dismiss(int i) {
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mView.isAttachedToWindow() && i == KeyguardUpdateMonitor.getCurrentUser()) {
            hide();
            KeyguardSecurityCallback keyguardSecurityCallback = this.mKeyguardCallback;
            if (keyguardSecurityCallback != null) {
                keyguardSecurityCallback.dismiss(i, true);
            }
        }
    }

    public final void hide() {
        if (this.mView.isAttachedToWindow()) {
            this.mParent.removeView(this.mView);
        }
        IKeyguardClient iKeyguardClient = this.mClient;
        if (iKeyguardClient != null) {
            try {
                iKeyguardClient.asBinder().unlinkToDeath(this.mKeyguardClientDeathRecipient, 0);
            } catch (NoSuchElementException unused) {
                Log.w("AdminSecondaryLockScreenController", "IKeyguardClient death recipient already released");
            }
            this.mContext.unbindService(this.mConnection);
            this.mClient = null;
        }
    }

    /* renamed from: -$$Nest$monSurfaceReady  reason: not valid java name */
    public static void m17$$Nest$monSurfaceReady(AdminSecondaryLockScreenController adminSecondaryLockScreenController) {
        Objects.requireNonNull(adminSecondaryLockScreenController);
        try {
            IBinder hostToken = adminSecondaryLockScreenController.mView.getHostToken();
            if (hostToken != null) {
                adminSecondaryLockScreenController.mClient.onCreateKeyguardSurface(hostToken, adminSecondaryLockScreenController.mCallback);
            } else {
                adminSecondaryLockScreenController.hide();
            }
        } catch (RemoteException e) {
            Log.e("AdminSecondaryLockScreenController", "Error in onCreateKeyguardSurface", e);
            adminSecondaryLockScreenController.dismiss(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.AdminSecondaryLockScreenController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.AdminSecondaryLockScreenController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.keyguard.AdminSecondaryLockScreenController$3] */
    public AdminSecondaryLockScreenController(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityContainerController.AnonymousClass2 r5, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mParent = keyguardSecurityContainer;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardCallback = r5;
        this.mView = new AdminSecurityView(context, this.mSurfaceHolderCallback);
    }
}
