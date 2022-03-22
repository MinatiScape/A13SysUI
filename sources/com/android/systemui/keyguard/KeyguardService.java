package com.android.systemui.keyguard;

import android.app.ActivityTaskManager;
import android.app.Service;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.WindowManagerPolicyConstants;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardService;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardService extends Service {
    public static boolean sEnableRemoteKeyguardGoingAwayAnimation;
    public static boolean sEnableRemoteKeyguardOccludeAnimation;
    public final KeyguardLifecyclesDispatcher mKeyguardLifecyclesDispatcher;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public final ShellTransitions mShellTransitions;
    public final AnonymousClass2 mExitAnimationRunner = new IRemoteAnimationRunner.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.2
        public final void onAnimationCancelled() {
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            Trace.beginSection("KeyguardViewMediator#cancelKeyguardExitAnimation");
            keyguardViewMediator.mHandler.sendMessage(keyguardViewMediator.mHandler.obtainMessage(19));
            Trace.endSection();
        }

        public final void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            Trace.beginSection("mExitAnimationRunner.onAnimationStart#startKeyguardExitAnimation");
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.startKeyguardExitAnimation(i, 0L, 0L, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
            Trace.endSection();
        }
    };
    public final AnonymousClass3 mOccludeAnimation = new IRemoteTransition.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.3
        public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        }

        public final void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
            transaction.apply();
            KeyguardService.this.mBinder.setOccluded(true, true);
            iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
        }
    };
    public final AnonymousClass4 mUnoccludeAnimation = new IRemoteTransition.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.4
        public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        }

        public final void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
            transaction.apply();
            KeyguardService.this.mBinder.setOccluded(false, true);
            iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
        }
    };
    public final AnonymousClass5 mBinder = new AnonymousClass5();

    /* renamed from: com.android.systemui.keyguard.KeyguardService$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 extends IKeyguardService.Stub {
        public AnonymousClass5() {
        }

        public final void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            synchronized (keyguardViewMediator) {
                keyguardViewMediator.mKeyguardStateCallbacks.add(iKeyguardStateCallback);
                try {
                    iKeyguardStateCallback.onSimSecureStateChanged(keyguardViewMediator.mUpdateMonitor.isSimPinSecure());
                    iKeyguardStateCallback.onShowingStateChanged(keyguardViewMediator.mShowing, KeyguardUpdateMonitor.getCurrentUser());
                    iKeyguardStateCallback.onInputRestrictedStateChanged(keyguardViewMediator.mInputRestricted);
                    iKeyguardStateCallback.onTrustedChanged(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser()));
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call to IKeyguardStateCallback", e);
                }
            }
        }

        public final void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.dismiss(iKeyguardDismissCallback, charSequence);
        }

        public final void dismissKeyguardToLaunch(Intent intent) {
            KeyguardService.this.checkPermission();
            Objects.requireNonNull(KeyguardService.this.mKeyguardViewMediator);
        }

        public final void doKeyguardTimeout(Bundle bundle) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.mHandler.removeMessages(10);
            keyguardViewMediator.mHandler.sendMessageAtFrontOfQueue(keyguardViewMediator.mHandler.obtainMessage(10, bundle));
        }

        public final void onBootCompleted() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onBootCompleted();
        }

        public final void onDreamingStarted() {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            KeyguardUpdateMonitor.AnonymousClass14 r0 = keyguardUpdateMonitor.mHandler;
            r0.sendMessage(r0.obtainMessage(333, 1, 0));
            synchronized (keyguardViewMediator) {
                if (keyguardViewMediator.mDeviceInteractive && keyguardViewMediator.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser())) {
                    long lockTimeout = keyguardViewMediator.getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
                    if (lockTimeout == 0) {
                        keyguardViewMediator.doKeyguardLocked(null);
                    } else {
                        keyguardViewMediator.doKeyguardLaterLocked(lockTimeout);
                    }
                }
            }
        }

        public final void onDreamingStopped() {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            KeyguardUpdateMonitor.AnonymousClass14 r0 = keyguardUpdateMonitor.mHandler;
            r0.sendMessage(r0.obtainMessage(333, 0, 0));
            synchronized (keyguardViewMediator) {
                if (keyguardViewMediator.mDeviceInteractive) {
                    keyguardViewMediator.mDelayedShowingSequence++;
                }
            }
        }

        public final void onFinishedGoingToSleep(int i, boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            int translateSleepReasonToOffReason = WindowManagerPolicyConstants.translateSleepReasonToOffReason(i);
            if (KeyguardViewMediator.DEBUG) {
                Objects.requireNonNull(keyguardViewMediator);
                Log.d("KeyguardViewMediator", "onFinishedGoingToSleep(" + translateSleepReasonToOffReason + ")");
            }
            synchronized (keyguardViewMediator) {
                try {
                    keyguardViewMediator.mDeviceInteractive = false;
                    keyguardViewMediator.mGoingToSleep = false;
                    keyguardViewMediator.mScreenOnCoordinator.setWakeAndUnlocking(false);
                    DozeParameters dozeParameters = keyguardViewMediator.mDozeParameters;
                    Objects.requireNonNull(dozeParameters);
                    ScreenOffAnimationController screenOffAnimationController = dozeParameters.mScreenOffAnimationController;
                    Objects.requireNonNull(screenOffAnimationController);
                    ArrayList arrayList = screenOffAnimationController.animations;
                    boolean z2 = true;
                    if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                        Iterator it = arrayList.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (!((ScreenOffAnimation) it.next()).shouldAnimateDozingChange()) {
                                    z2 = false;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    keyguardViewMediator.mAnimatingScreenOff = z2;
                    keyguardViewMediator.resetKeyguardDonePendingLocked();
                    keyguardViewMediator.mHideAnimationRun = false;
                    if (KeyguardViewMediator.DEBUG) {
                        Log.d("KeyguardViewMediator", "notifyFinishedGoingToSleep");
                    }
                    keyguardViewMediator.mHandler.sendEmptyMessage(5);
                    if (z) {
                        ((PowerManager) keyguardViewMediator.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE_PREVENT_LOCK");
                        keyguardViewMediator.mPendingLock = false;
                        keyguardViewMediator.mPendingReset = false;
                    }
                    if (keyguardViewMediator.mPendingReset) {
                        keyguardViewMediator.resetStateLocked();
                        keyguardViewMediator.mPendingReset = false;
                    }
                    if (keyguardViewMediator.mPendingLock && !keyguardViewMediator.mScreenOffAnimationController.isKeyguardShowDelayed()) {
                        keyguardViewMediator.doKeyguardLocked(null);
                        keyguardViewMediator.mPendingLock = false;
                    }
                    if (!keyguardViewMediator.mLockLater && !z) {
                        keyguardViewMediator.doKeyguardForChildProfilesLocked();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            synchronized (keyguardUpdateMonitor) {
                keyguardUpdateMonitor.mDeviceInteractive = false;
            }
            KeyguardUpdateMonitor.AnonymousClass14 r9 = keyguardUpdateMonitor.mHandler;
            r9.sendMessage(r9.obtainMessage(320, translateSleepReasonToOffReason, 0));
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(7).sendToTarget();
        }

        public final void onFinishedWakingUp() {
            Trace.beginSection("KeyguardService.mBinder#onFinishedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(5).sendToTarget();
            Trace.endSection();
        }

        public final void onScreenTurnedOff() {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.mHandler.sendEmptyMessage(332);
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(3).sendToTarget();
        }

        public final void onScreenTurnedOn() {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurnedOn");
            KeyguardService.this.checkPermission();
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(1).sendToTarget();
            Trace.endSection();
        }

        public final void onScreenTurningOff() {
            KeyguardService.this.checkPermission();
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(2).sendToTarget();
        }

        public final void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurningOn");
            KeyguardService.this.checkPermission();
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            keyguardLifecyclesDispatcher.mHandler.obtainMessage(0, iKeyguardDrawnCallback).sendToTarget();
            Trace.endSection();
        }

        public final void onShortPowerPressedGoHome() {
            KeyguardService.this.checkPermission();
            Objects.requireNonNull(KeyguardService.this.mKeyguardViewMediator);
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0083 A[Catch: all -> 0x008f, TryCatch #0 {all -> 0x008f, blocks: (B:7:0x0031, B:9:0x0042, B:14:0x004e, B:16:0x005c, B:18:0x0064, B:26:0x0075, B:27:0x007b, B:29:0x0083, B:30:0x0085, B:32:0x0089, B:35:0x0091), top: B:43:0x0031 }] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0089 A[Catch: all -> 0x008f, TryCatch #0 {all -> 0x008f, blocks: (B:7:0x0031, B:9:0x0042, B:14:0x004e, B:16:0x005c, B:18:0x0064, B:26:0x0075, B:27:0x007b, B:29:0x0083, B:30:0x0085, B:32:0x0089, B:35:0x0091), top: B:43:0x0031 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onStartedGoingToSleep(int r12) {
            /*
                r11 = this;
                com.android.systemui.keyguard.KeyguardService r0 = com.android.systemui.keyguard.KeyguardService.this
                r0.checkPermission()
                com.android.systemui.keyguard.KeyguardService r0 = com.android.systemui.keyguard.KeyguardService.this
                com.android.systemui.keyguard.KeyguardViewMediator r0 = r0.mKeyguardViewMediator
                int r1 = android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(r12)
                boolean r2 = com.android.systemui.keyguard.KeyguardViewMediator.DEBUG
                if (r2 == 0) goto L_0x002f
                java.util.Objects.requireNonNull(r0)
                java.lang.String r3 = "KeyguardViewMediator"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "onStartedGoingToSleep("
                r4.append(r5)
                r4.append(r1)
                java.lang.String r5 = ")"
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                android.util.Log.d(r3, r4)
            L_0x002f:
                monitor-enter(r0)
                r3 = 0
                r0.mDeviceInteractive = r3     // Catch: all -> 0x008f
                r4 = 1
                r0.mGoingToSleep = r4     // Catch: all -> 0x008f
                int r5 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch: all -> 0x008f
                com.android.internal.widget.LockPatternUtils r6 = r0.mLockPatternUtils     // Catch: all -> 0x008f
                boolean r6 = r6.getPowerButtonInstantlyLocks(r5)     // Catch: all -> 0x008f
                if (r6 != 0) goto L_0x004d
                com.android.internal.widget.LockPatternUtils r6 = r0.mLockPatternUtils     // Catch: all -> 0x008f
                boolean r6 = r6.isSecure(r5)     // Catch: all -> 0x008f
                if (r6 != 0) goto L_0x004b
                goto L_0x004d
            L_0x004b:
                r6 = r3
                goto L_0x004e
            L_0x004d:
                r6 = r4
            L_0x004e:
                int r7 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch: all -> 0x008f
                long r7 = r0.getLockTimeout(r7)     // Catch: all -> 0x008f
                r0.mLockLater = r3     // Catch: all -> 0x008f
                boolean r9 = r0.mShowing     // Catch: all -> 0x008f
                if (r9 == 0) goto L_0x0067
                com.android.systemui.statusbar.policy.KeyguardStateController r9 = r0.mKeyguardStateController     // Catch: all -> 0x008f
                boolean r9 = r9.isKeyguardGoingAway()     // Catch: all -> 0x008f
                if (r9 != 0) goto L_0x0067
                r0.mPendingReset = r4     // Catch: all -> 0x008f
                goto L_0x0085
            L_0x0067:
                r9 = 3
                if (r1 != r9) goto L_0x0070
                r9 = 0
                int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
                if (r9 > 0) goto L_0x0075
            L_0x0070:
                r9 = 2
                if (r1 != r9) goto L_0x007b
                if (r6 != 0) goto L_0x007b
            L_0x0075:
                r0.doKeyguardLaterLocked(r7)     // Catch: all -> 0x008f
                r0.mLockLater = r4     // Catch: all -> 0x008f
                goto L_0x0085
            L_0x007b:
                com.android.internal.widget.LockPatternUtils r6 = r0.mLockPatternUtils     // Catch: all -> 0x008f
                boolean r5 = r6.isLockScreenDisabled(r5)     // Catch: all -> 0x008f
                if (r5 != 0) goto L_0x0085
                r0.mPendingLock = r4     // Catch: all -> 0x008f
            L_0x0085:
                boolean r4 = r0.mPendingLock     // Catch: all -> 0x008f
                if (r4 == 0) goto L_0x0091
                int r4 = r0.mLockSoundId     // Catch: all -> 0x008f
                r0.playSound(r4)     // Catch: all -> 0x008f
                goto L_0x0091
            L_0x008f:
                r11 = move-exception
                goto L_0x00d8
            L_0x0091:
                monitor-exit(r0)     // Catch: all -> 0x008f
                com.android.keyguard.KeyguardUpdateMonitor r4 = r0.mUpdateMonitor
                java.util.Objects.requireNonNull(r4)
                com.android.keyguard.KeyguardUpdateMonitor$14 r4 = r4.mHandler
                r5 = 321(0x141, float:4.5E-43)
                android.os.Message r1 = r4.obtainMessage(r5, r1, r3)
                r4.sendMessage(r1)
                com.android.keyguard.KeyguardUpdateMonitor r1 = r0.mUpdateMonitor
                java.util.Objects.requireNonNull(r1)
                com.android.keyguard.KeyguardUpdateMonitor$14 r1 = r1.mHandler
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                r4 = 342(0x156, float:4.79E-43)
                android.os.Message r3 = r1.obtainMessage(r4, r3)
                r1.sendMessage(r3)
                if (r2 == 0) goto L_0x00bd
                java.lang.String r1 = "KeyguardViewMediator"
                java.lang.String r2 = "notifyStartedGoingToSleep"
                android.util.Log.d(r1, r2)
            L_0x00bd:
                com.android.systemui.keyguard.KeyguardViewMediator$9 r0 = r0.mHandler
                r1 = 17
                r0.sendEmptyMessage(r1)
                com.android.systemui.keyguard.KeyguardService r11 = com.android.systemui.keyguard.KeyguardService.this
                com.android.systemui.keyguard.KeyguardLifecyclesDispatcher r11 = r11.mKeyguardLifecyclesDispatcher
                r0 = 6
                java.util.Objects.requireNonNull(r11)
                com.android.systemui.keyguard.KeyguardLifecyclesDispatcher$1 r11 = r11.mHandler
                android.os.Message r11 = r11.obtainMessage(r0)
                r11.arg1 = r12
                r11.sendToTarget()
                return
            L_0x00d8:
                monitor-exit(r0)     // Catch: all -> 0x008f
                throw r11
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardService.AnonymousClass5.onStartedGoingToSleep(int):void");
        }

        public final void onStartedWakingUp(int i, boolean z) {
            Trace.beginSection("KeyguardService.mBinder#onStartedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            Trace.beginSection("KeyguardViewMediator#onStartedWakingUp");
            synchronized (keyguardViewMediator) {
                keyguardViewMediator.mDeviceInteractive = true;
                if (keyguardViewMediator.mPendingLock && !z) {
                    keyguardViewMediator.doKeyguardLocked(null);
                }
                keyguardViewMediator.mAnimatingScreenOff = false;
                keyguardViewMediator.mDelayedShowingSequence++;
                keyguardViewMediator.mDelayedProfileShowingSequence++;
                boolean z2 = KeyguardViewMediator.DEBUG;
                if (z2) {
                    Log.d("KeyguardViewMediator", "onStartedWakingUp, seq = " + keyguardViewMediator.mDelayedShowingSequence);
                }
                if (z2) {
                    Log.d("KeyguardViewMediator", "notifyStartedWakingUp");
                }
                keyguardViewMediator.mHandler.sendEmptyMessage(14);
            }
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            synchronized (keyguardUpdateMonitor) {
                keyguardUpdateMonitor.mDeviceInteractive = true;
            }
            keyguardUpdateMonitor.mHandler.sendEmptyMessage(319);
            keyguardViewMediator.maybeSendUserPresentBroadcast();
            Trace.endSection();
            KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher = KeyguardService.this.mKeyguardLifecyclesDispatcher;
            Objects.requireNonNull(keyguardLifecyclesDispatcher);
            Message obtainMessage = keyguardLifecyclesDispatcher.mHandler.obtainMessage(4);
            obtainMessage.arg1 = i;
            obtainMessage.sendToTarget();
            Trace.endSection();
        }

        public final void onSystemKeyPressed(int i) {
            KeyguardService.this.checkPermission();
            Objects.requireNonNull(KeyguardService.this.mKeyguardViewMediator);
        }

        public final void onSystemReady() {
            Trace.beginSection("KeyguardService.mBinder#onSystemReady");
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.mHandler.obtainMessage(18).sendToTarget();
            Trace.endSection();
        }

        public final void setCurrentUser(int i) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            boolean z = KeyguardUpdateMonitor.DEBUG;
            synchronized (KeyguardUpdateMonitor.class) {
                KeyguardUpdateMonitor.sCurrentUser = i;
            }
            synchronized (keyguardViewMediator) {
                keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
            }
        }

        public final void setKeyguardEnabled(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            synchronized (keyguardViewMediator) {
                boolean z2 = KeyguardViewMediator.DEBUG;
                if (z2) {
                    Log.d("KeyguardViewMediator", "setKeyguardEnabled(" + z + ")");
                }
                keyguardViewMediator.mExternallyEnabled = z;
                if (!z && keyguardViewMediator.mShowing) {
                    if (z2) {
                        Log.d("KeyguardViewMediator", "remembering to reshow, hiding keyguard, disabling status bar expansion");
                    }
                    keyguardViewMediator.mNeedToReshowWhenReenabled = true;
                    keyguardViewMediator.updateInputRestrictedLocked();
                    keyguardViewMediator.hideLocked();
                } else if (z && keyguardViewMediator.mNeedToReshowWhenReenabled) {
                    if (z2) {
                        Log.d("KeyguardViewMediator", "previously hidden, reshowing, reenabling status bar expansion");
                    }
                    keyguardViewMediator.mNeedToReshowWhenReenabled = false;
                    keyguardViewMediator.updateInputRestrictedLocked();
                    keyguardViewMediator.showLocked(null);
                    keyguardViewMediator.mWaitingUntilKeyguardVisible = true;
                    keyguardViewMediator.mHandler.sendEmptyMessageDelayed(8, 2000L);
                    if (z2) {
                        Log.d("KeyguardViewMediator", "waiting until mWaitingUntilKeyguardVisible is false");
                    }
                    while (keyguardViewMediator.mWaitingUntilKeyguardVisible) {
                        try {
                            keyguardViewMediator.wait();
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (KeyguardViewMediator.DEBUG) {
                        Log.d("KeyguardViewMediator", "done waiting for mWaitingUntilKeyguardVisible");
                    }
                }
            }
        }

        public final void setOccluded(boolean z, boolean z2) {
            Trace.beginSection("KeyguardService.mBinder#setOccluded");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setOccluded(z, z2);
            Trace.endSection();
        }

        public final void setSwitchingUser(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.mSwitchingUser = z;
            keyguardUpdateMonitor.mHandler.post(new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(keyguardUpdateMonitor, 0));
        }

        @Deprecated
        public final void startKeyguardExitAnimation(long j, long j2) {
            Trace.beginSection("KeyguardService.mBinder#startKeyguardExitAnimation");
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.startKeyguardExitAnimation(0, j, j2, null, null, null, null);
            Trace.endSection();
        }

        public final void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
            Trace.beginSection("KeyguardService.mBinder#verifyUnlock");
            KeyguardService.this.checkPermission();
            KeyguardViewMediator keyguardViewMediator = KeyguardService.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            Trace.beginSection("KeyguardViewMediator#verifyUnlock");
            synchronized (keyguardViewMediator) {
                boolean z = KeyguardViewMediator.DEBUG;
                if (z) {
                    Log.d("KeyguardViewMediator", "verifyUnlock");
                }
                if (keyguardViewMediator.shouldWaitForProvisioning()) {
                    if (z) {
                        Log.d("KeyguardViewMediator", "ignoring because device isn't provisioned");
                    }
                    try {
                        iKeyguardExitCallback.onKeyguardExitResult(false);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                    }
                } else if (keyguardViewMediator.mExternallyEnabled) {
                    Log.w("KeyguardViewMediator", "verifyUnlock called when not externally disabled");
                    try {
                        iKeyguardExitCallback.onKeyguardExitResult(false);
                    } catch (RemoteException e2) {
                        Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e2);
                    }
                } else if (!keyguardViewMediator.isSecure()) {
                    keyguardViewMediator.mExternallyEnabled = true;
                    keyguardViewMediator.mNeedToReshowWhenReenabled = false;
                    synchronized (keyguardViewMediator) {
                        keyguardViewMediator.updateInputRestrictedLocked();
                        try {
                            iKeyguardExitCallback.onKeyguardExitResult(true);
                        } catch (RemoteException e3) {
                            Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e3);
                        }
                    }
                } else {
                    try {
                        iKeyguardExitCallback.onKeyguardExitResult(false);
                    } catch (RemoteException e4) {
                        Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e4);
                    }
                }
            }
            Trace.endSection();
            Trace.endSection();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0091, code lost:
        if (r7 != 4) goto L_0x0097;
     */
    /* renamed from: -$$Nest$smwrap  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.view.RemoteAnimationTarget[] m62$$Nest$smwrap(android.window.TransitionInfo r24, boolean r25) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardService.m62$$Nest$smwrap(android.window.TransitionInfo, boolean):android.view.RemoteAnimationTarget[]");
    }

    static {
        boolean z;
        boolean z2 = true;
        int i = SystemProperties.getInt("persist.wm.enable_remote_keyguard_animation", 1);
        if (i >= 1) {
            z = true;
        } else {
            z = false;
        }
        sEnableRemoteKeyguardGoingAwayAnimation = z;
        if (i < 2) {
            z2 = false;
        }
        sEnableRemoteKeyguardOccludeAnimation = z2;
    }

    public KeyguardService(KeyguardViewMediator keyguardViewMediator, KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher, ShellTransitions shellTransitions) {
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mKeyguardLifecyclesDispatcher = keyguardLifecyclesDispatcher;
        this.mShellTransitions = shellTransitions;
    }

    public final void checkPermission() {
        if (Binder.getCallingUid() != 1000 && getBaseContext().checkCallingOrSelfPermission("android.permission.CONTROL_KEYGUARD") != 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Caller needs permission 'android.permission.CONTROL_KEYGUARD' to call ");
            m.append(Debug.getCaller());
            Log.w("KeyguardService", m.toString());
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Access denied to process: ");
            m2.append(Binder.getCallingPid());
            m2.append(", must have permission ");
            m2.append("android.permission.CONTROL_KEYGUARD");
            throw new SecurityException(m2.toString());
        }
    }

    @Override // android.app.Service
    public final void onCreate() {
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
        if (this.mShellTransitions == null || !Transitions.ENABLE_SHELL_TRANSITIONS) {
            RemoteAnimationDefinition remoteAnimationDefinition = new RemoteAnimationDefinition();
            if (sEnableRemoteKeyguardGoingAwayAnimation) {
                RemoteAnimationAdapter remoteAnimationAdapter = new RemoteAnimationAdapter(this.mExitAnimationRunner, 0L, 0L);
                remoteAnimationDefinition.addRemoteAnimation(20, remoteAnimationAdapter);
                remoteAnimationDefinition.addRemoteAnimation(21, remoteAnimationAdapter);
            }
            if (sEnableRemoteKeyguardOccludeAnimation) {
                KeyguardViewMediator keyguardViewMediator = this.mKeyguardViewMediator;
                Objects.requireNonNull(keyguardViewMediator);
                remoteAnimationDefinition.addRemoteAnimation(22, new RemoteAnimationAdapter(keyguardViewMediator.mOccludeAnimationRunner, 0L, 0L));
                KeyguardViewMediator keyguardViewMediator2 = this.mKeyguardViewMediator;
                Objects.requireNonNull(keyguardViewMediator2);
                remoteAnimationDefinition.addRemoteAnimation(23, new RemoteAnimationAdapter(keyguardViewMediator2.mUnoccludeAnimationRunner, 0L, 0L));
            }
            ActivityTaskManager.getInstance().registerRemoteAnimationsForDisplay(0, remoteAnimationDefinition);
            return;
        }
        if (sEnableRemoteKeyguardGoingAwayAnimation) {
            Slog.d("KeyguardService", "KeyguardService registerRemote: TRANSIT_KEYGUARD_GOING_AWAY");
            TransitionFilter transitionFilter = new TransitionFilter();
            transitionFilter.mFlags = 256;
            ShellTransitions shellTransitions = this.mShellTransitions;
            final AnonymousClass2 r5 = this.mExitAnimationRunner;
            shellTransitions.registerRemote(transitionFilter, new RemoteTransition(new IRemoteTransition.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.1
                public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                }

                public final void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
                    Slog.d("KeyguardService", "Starts IRemoteAnimationRunner: info=" + transitionInfo);
                    int i = 0;
                    RemoteAnimationTarget[] remoteAnimationTargetArr = KeyguardService.m62$$Nest$smwrap(transitionInfo, false);
                    RemoteAnimationTarget[] remoteAnimationTargetArr2 = KeyguardService.m62$$Nest$smwrap(transitionInfo, true);
                    RemoteAnimationTarget[] remoteAnimationTargetArr3 = new RemoteAnimationTarget[0];
                    for (TransitionInfo.Change change : transitionInfo.getChanges()) {
                        transaction.setAlpha(change.getLeash(), 1.0f);
                    }
                    transaction.apply();
                    IRemoteAnimationRunner iRemoteAnimationRunner = r5;
                    int type = transitionInfo.getType();
                    int flags = transitionInfo.getFlags();
                    if (type == 7 || (flags & 256) != 0) {
                        if (remoteAnimationTargetArr.length == 0) {
                            i = 21;
                        } else {
                            i = 20;
                        }
                    } else if (type == 8) {
                        i = 22;
                    } else if (type == 9) {
                        i = 23;
                    } else {
                        Slog.d("KeyguardService", "Unexpected transit type: " + type);
                    }
                    iRemoteAnimationRunner.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, new IRemoteAnimationFinishedCallback.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.1.1
                        public final void onAnimationFinished() throws RemoteException {
                            Slog.d("KeyguardService", "Finish IRemoteAnimationRunner.");
                            iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
                        }
                    });
                }
            }, getIApplicationThread()));
        }
        if (sEnableRemoteKeyguardOccludeAnimation) {
            Slog.d("KeyguardService", "KeyguardService registerRemote: TRANSIT_KEYGUARD_(UN)OCCLUDE");
            TransitionFilter transitionFilter2 = new TransitionFilter();
            transitionFilter2.mFlags = 64;
            TransitionFilter.Requirement[] requirementArr = {new TransitionFilter.Requirement(), new TransitionFilter.Requirement()};
            transitionFilter2.mRequirements = requirementArr;
            requirementArr[0].mMustBeIndependent = false;
            requirementArr[0].mFlags = 64;
            requirementArr[0].mModes = new int[]{1, 3};
            requirementArr[1].mNot = true;
            requirementArr[1].mMustBeIndependent = false;
            requirementArr[1].mFlags = 64;
            requirementArr[1].mModes = new int[]{2, 4};
            this.mShellTransitions.registerRemote(transitionFilter2, new RemoteTransition(this.mOccludeAnimation, getIApplicationThread()));
            TransitionFilter transitionFilter3 = new TransitionFilter();
            transitionFilter3.mFlags = 64;
            TransitionFilter.Requirement[] requirementArr2 = {new TransitionFilter.Requirement(), new TransitionFilter.Requirement()};
            transitionFilter3.mRequirements = requirementArr2;
            requirementArr2[1].mMustBeIndependent = false;
            requirementArr2[1].mModes = new int[]{2, 4};
            requirementArr2[1].mMustBeTask = true;
            requirementArr2[0].mNot = true;
            requirementArr2[0].mMustBeIndependent = false;
            requirementArr2[0].mFlags = 64;
            requirementArr2[0].mModes = new int[]{1, 3};
            this.mShellTransitions.registerRemote(transitionFilter3, new RemoteTransition(this.mUnoccludeAnimation, getIApplicationThread()));
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.keyguard.KeyguardService$5, android.os.IBinder] */
    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
