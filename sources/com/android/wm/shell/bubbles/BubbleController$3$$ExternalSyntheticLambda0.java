package com.android.wm.shell.bubbles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.RemoteAnimationTarget;
import android.window.BackEvent;
import android.window.BackNavigationInfo;
import android.window.IOnBackInvokedCallback;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.back.BackAnimationController$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BubbleController$3$$ExternalSyntheticLambda0(Object obj, Object obj2, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        BackNavigationInfo backNavigationInfo;
        boolean z;
        IOnBackInvokedCallback iOnBackInvokedCallback;
        float f;
        IOnBackInvokedCallback iOnBackInvokedCallback2;
        int i;
        switch (this.$r8$classId) {
            case 0:
                BubbleController.AnonymousClass3 r1 = (BubbleController.AnonymousClass3) this.f$0;
                Boolean bool = (Boolean) this.f$1;
                int i2 = this.f$2;
                Objects.requireNonNull(r1);
                BubbleStackView bubbleStackView = BubbleController.this.mStackView;
                if (!(bubbleStackView == null || bubbleStackView.getExpandedBubble() == null || !BubbleController.this.isStackExpanded())) {
                    BubbleStackView bubbleStackView2 = BubbleController.this.mStackView;
                    Objects.requireNonNull(bubbleStackView2);
                    if (!bubbleStackView2.mIsExpansionAnimating && !bool.booleanValue()) {
                        i = BubbleController.this.mStackView.getExpandedBubble().getTaskId();
                        if (i != -1 && i != i2) {
                            BubbleController.this.mBubbleData.setExpanded(false);
                            return;
                        }
                        return;
                    }
                }
                i = -1;
                if (i != -1) {
                    return;
                }
                return;
            default:
                BackAnimationController.BackAnimationImpl backAnimationImpl = (BackAnimationController.BackAnimationImpl) this.f$0;
                MotionEvent motionEvent = (MotionEvent) this.f$1;
                int i3 = this.f$2;
                Objects.requireNonNull(backAnimationImpl);
                final BackAnimationController backAnimationController = BackAnimationController.this;
                Objects.requireNonNull(backAnimationController);
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                        ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, 1188911440, 3, "initAnimation mMotionStarted=%b", Boolean.valueOf(backAnimationController.mBackGestureStarted));
                    }
                    if (backAnimationController.mBackGestureStarted) {
                        Log.e("BackAnimationController", "Animation is being initialized but is already started.");
                        return;
                    }
                    if (backAnimationController.mBackNavigationInfo != null) {
                        backAnimationController.finishAnimation();
                    }
                    backAnimationController.mInitTouchLocation.set(motionEvent.getX(), motionEvent.getY());
                    backAnimationController.mBackGestureStarted = true;
                    try {
                        BackNavigationInfo startBackNavigation = backAnimationController.mActivityTaskManager.startBackNavigation();
                        backAnimationController.mBackNavigationInfo = startBackNavigation;
                        backAnimationController.onBackNavigationInfoReceived(startBackNavigation);
                        return;
                    } catch (RemoteException e) {
                        Log.e("BackAnimationController", "Failed to initAnimation", e);
                        backAnimationController.finishAnimation();
                        return;
                    }
                } else if (actionMasked == 2) {
                    if (backAnimationController.mBackGestureStarted && backAnimationController.mBackNavigationInfo != null) {
                        int round = Math.round(motionEvent.getX() - backAnimationController.mInitTouchLocation.x);
                        int round2 = Math.round(motionEvent.getY() - backAnimationController.mInitTouchLocation.y);
                        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -1295653292, 5, "Runner move: %d %d", Long.valueOf(round), Long.valueOf(round2));
                        }
                        int i4 = BackAnimationController.PROGRESS_THRESHOLD;
                        if (i4 >= 0) {
                            f = i4;
                        } else {
                            f = backAnimationController.mProgressThreshold;
                        }
                        float min = Math.min(Math.max(Math.abs(round) / f, 0.0f), 1.0f);
                        int type = backAnimationController.mBackNavigationInfo.getType();
                        RemoteAnimationTarget departingAnimationTarget = backAnimationController.mBackNavigationInfo.getDepartingAnimationTarget();
                        BackEvent backEvent = new BackEvent(0, 0, min, i3, departingAnimationTarget);
                        if (type == 1) {
                            iOnBackInvokedCallback2 = backAnimationController.mBackToLauncherCallback;
                        } else {
                            if (type == 3 || type == 2) {
                                if (departingAnimationTarget != null) {
                                    backAnimationController.mTransaction.setPosition(departingAnimationTarget.leash, round, round2);
                                    backAnimationController.mTouchEventDelta.set(round, round2);
                                    backAnimationController.mTransaction.apply();
                                }
                            } else if (type == 4) {
                                iOnBackInvokedCallback2 = backAnimationController.mBackNavigationInfo.getOnBackInvokedCallback();
                            }
                            iOnBackInvokedCallback2 = null;
                        }
                        if (iOnBackInvokedCallback2 != null) {
                            try {
                                iOnBackInvokedCallback2.onBackProgressed(backEvent);
                                return;
                            } catch (RemoteException e2) {
                                Log.e("BackAnimationController", "dispatchOnBackProgressed error: ", e2);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (actionMasked == 1 || actionMasked == 3) {
                    if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                        ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -14660627, 0, "onGestureFinished() mTriggerBack == %s", String.valueOf(backAnimationController.mTriggerBack));
                    }
                    if (backAnimationController.mBackGestureStarted && (backNavigationInfo = backAnimationController.mBackNavigationInfo) != null) {
                        int type2 = backNavigationInfo.getType();
                        if (type2 != 1 || backAnimationController.mBackToLauncherCallback == null) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if (z) {
                            iOnBackInvokedCallback = backAnimationController.mBackToLauncherCallback;
                        } else {
                            iOnBackInvokedCallback = backAnimationController.mBackNavigationInfo.getOnBackInvokedCallback();
                        }
                        if (backAnimationController.mTriggerBack) {
                            if (iOnBackInvokedCallback != null) {
                                try {
                                    iOnBackInvokedCallback.onBackInvoked();
                                } catch (RemoteException e3) {
                                    Log.e("BackAnimationController", "dispatchOnBackInvoked error: ", e3);
                                }
                            }
                        } else if (iOnBackInvokedCallback != null) {
                            try {
                                iOnBackInvokedCallback.onBackCancelled();
                            } catch (RemoteException e4) {
                                Log.e("BackAnimationController", "dispatchOnBackCancelled error: ", e4);
                            }
                        }
                        if (type2 == 4) {
                            backAnimationController.finishAnimation();
                            return;
                        } else if (type2 == 1 && !z) {
                            backAnimationController.finishAnimation();
                            return;
                        } else if (type2 != 2 && type2 != 3) {
                            return;
                        } else {
                            if (backAnimationController.mTriggerBack) {
                                if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, 1275042685, 0, "prepareTransition()", null);
                                }
                                backAnimationController.mTriggerBack = false;
                                backAnimationController.mBackGestureStarted = false;
                                return;
                            }
                            backAnimationController.mBackGestureStarted = false;
                            if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -742132644, 0, "Runner: Back not triggered, cancelling animation mLastPos=%s mInitTouch=%s", String.valueOf(backAnimationController.mTouchEventDelta), String.valueOf(backAnimationController.mInitTouchLocation));
                            }
                            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
                            duration.addUpdateListener(new BackAnimationController$$ExternalSyntheticLambda0(backAnimationController, 0));
                            duration.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.back.BackAnimationController.1
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                                        ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -1966544683, 0, "BackAnimationController: onAnimationEnd", null);
                                    }
                                    backAnimationController.finishAnimation();
                                }
                            });
                            duration.start();
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        }
    }
}
