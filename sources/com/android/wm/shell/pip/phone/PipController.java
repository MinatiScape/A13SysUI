package com.android.wm.shell.pip.phone;

import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Size;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda3;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda28;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.IPip;
import com.android.wm.shell.pip.IPipAnimationListener;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PipController;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class PipController implements PipTransitionController.PipTransitionCallback, RemoteCallable<PipController> {
    public PipAppOpsListener mAppOpsListener;
    public Context mContext;
    public DisplayController mDisplayController;
    public final PipImpl mImpl;
    public boolean mIsInFixedRotation;
    public ShellExecutor mMainExecutor;
    public PipMediaController mMediaController;
    public PhonePipMenuController mMenuController;
    public Optional<OneHandedController> mOneHandedController;
    public PipAnimationListener mPinnedStackAnimationRecentsCallback;
    public PipBoundsAlgorithm mPipBoundsAlgorithm;
    public PipBoundsState mPipBoundsState;
    public PipInputConsumer mPipInputConsumer;
    public PipTaskOrganizer mPipTaskOrganizer;
    public PipTransitionController mPipTransitionController;
    public TaskStackListenerImpl mTaskStackListener;
    public PipTouchHandler mTouchHandler;
    public WindowManagerShellWrapper mWindowManagerShellWrapper;
    public final Rect mTmpInsetBounds = new Rect();
    public PipControllerPinnedTaskListener mPinnedTaskListener = new PipControllerPinnedTaskListener();
    public final PipController$$ExternalSyntheticLambda2 mRotationController = new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda2
        /* JADX WARN: Removed duplicated region for block: B:30:0x0107  */
        /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
        @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onRotateDisplay(int r18, int r19, int r20, android.window.WindowContainerTransaction r21) {
            /*
                Method dump skipped, instructions count: 430
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda2.onRotateDisplay(int, int, int, android.window.WindowContainerTransaction):void");
        }
    };
    @VisibleForTesting
    public final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener = new DisplayController.OnDisplaysChangedListener() { // from class: com.android.wm.shell.pip.phone.PipController.1
        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public final void onDisplayAdded(int i) {
            PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            if (i == pipBoundsState.mDisplayId) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), false);
            }
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
            PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            if (i == pipBoundsState.mDisplayId) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), true);
            }
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public final void onFixedRotationFinished() {
            PipController.this.mIsInFixedRotation = false;
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public final void onFixedRotationStarted(int i) {
            PipController.this.mIsInFixedRotation = true;
        }
    };

    /* loaded from: classes.dex */
    public interface PipAnimationListener {
    }

    /* loaded from: classes.dex */
    public class PipControllerPinnedTaskListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        public PipControllerPinnedTaskListener() {
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
            PhonePipMenuController phonePipMenuController = PipController.this.mMenuController;
            Objects.requireNonNull(phonePipMenuController);
            phonePipMenuController.mAppActions = parceledListSlice;
            phonePipMenuController.updateMenuActions();
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onActivityHidden(ComponentName componentName) {
            PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            if (componentName.equals(pipBoundsState.mLastPipComponentName)) {
                PipController.this.mPipBoundsState.setLastPipComponentName(null);
            }
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onAspectRatioChanged(float f) {
            PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            pipBoundsState.mAspectRatio = f;
            PipTouchHandler pipTouchHandler = PipController.this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler);
            PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
            Objects.requireNonNull(pipResizeGestureHandler);
            pipResizeGestureHandler.mUserResizeBounds.setEmpty();
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onImeVisibilityChanged(boolean z, int i) {
            PipBoundsState pipBoundsState = PipController.this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            pipBoundsState.mIsImeShowing = z;
            pipBoundsState.mImeHeight = i;
            PipTouchHandler pipTouchHandler = PipController.this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler);
            pipTouchHandler.mIsImeShowing = z;
            pipTouchHandler.mImeHeight = i;
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onMovementBoundsChanged(boolean z) {
            PipController.this.updateMovementBounds(null, false, z, false, null);
        }
    }

    /* loaded from: classes.dex */
    public class PipImpl implements Pip {
        public IPipImpl mIPip;

        public PipImpl() {
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda0(this, consumer, 0));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final IPip createExternalInterface() {
            IPipImpl iPipImpl = this.mIPip;
            if (iPipImpl != null) {
                Objects.requireNonNull(iPipImpl);
                iPipImpl.mController = null;
            }
            IPipImpl iPipImpl2 = new IPipImpl(PipController.this);
            this.mIPip = iPipImpl2;
            return iPipImpl2;
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void dump(PrintWriter printWriter) {
            try {
                PipController.this.mMainExecutor.executeBlocking$1(new BubblesManager$5$$ExternalSyntheticLambda3(this, printWriter, 2));
            } catch (InterruptedException unused) {
                Slog.e("PipController", "Failed to dump PipController in 2s");
            }
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void hidePipMenu() {
            PipController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipController$PipImpl$$ExternalSyntheticLambda2
                public final /* synthetic */ Runnable f$1 = null;
                public final /* synthetic */ Runnable f$2 = null;

                @Override // java.lang.Runnable
                public final void run() {
                    PipController.PipImpl pipImpl = PipController.PipImpl.this;
                    Runnable runnable = this.f$1;
                    Runnable runnable2 = this.f$2;
                    Objects.requireNonNull(pipImpl);
                    PipController pipController = PipController.this;
                    Objects.requireNonNull(pipController);
                    PhonePipMenuController phonePipMenuController = pipController.mMenuController;
                    Objects.requireNonNull(phonePipMenuController);
                    if (phonePipMenuController.isMenuVisible()) {
                        if (runnable != null) {
                            runnable.run();
                        }
                        PipMenuView pipMenuView = phonePipMenuController.mPipMenuView;
                        Objects.requireNonNull(pipMenuView);
                        pipMenuView.hideMenu(runnable2, true, pipMenuView.mDidLastShowMenuResize, 1);
                    }
                }
            });
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void onConfigurationChanged(Configuration configuration) {
            PipController.this.mMainExecutor.execute(new Monitor$$ExternalSyntheticLambda0(this, configuration, 4));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void onDensityOrFontScaleChanged() {
            PipController.this.mMainExecutor.execute(new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this, 7));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void onOverlayChanged() {
            PipController.this.mMainExecutor.execute(new StatusBar$$ExternalSyntheticLambda19(this, 7));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void onSystemUiStateChanged(final boolean z, final int i) {
            PipController.this.mMainExecutor.execute(new Runnable(z, i) { // from class: com.android.wm.shell.pip.phone.PipController$PipImpl$$ExternalSyntheticLambda3
                public final /* synthetic */ boolean f$1;

                @Override // java.lang.Runnable
                public final void run() {
                    PipController.PipImpl pipImpl = PipController.PipImpl.this;
                    boolean z2 = this.f$1;
                    Objects.requireNonNull(pipImpl);
                    PipController pipController = PipController.this;
                    Objects.requireNonNull(pipController);
                    PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                    Objects.requireNonNull(pipTouchHandler);
                    PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
                    Objects.requireNonNull(pipResizeGestureHandler);
                    pipResizeGestureHandler.mIsSysUiStateValid = z2;
                }
            });
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void registerSessionListenerForCurrentUser() {
            PipController.this.mMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda0(this, 6));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda1(this, consumer, 5));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void setPinnedStackAnimationType() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda1(this, 1, 0));
        }

        @Override // com.android.wm.shell.pip.Pip
        public final void showPictureInPictureMenu() {
            PipController.this.mMainExecutor.execute(new CreateUserActivity$$ExternalSyntheticLambda2(this, 7));
        }
    }

    /* loaded from: classes.dex */
    public static class IPipImpl extends IPip.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;
        public PipController mController;
        public final SingleInstanceRemoteListener<PipController, IPipAnimationListener> mListener;
        public final AnonymousClass1 mPipAnimationListener = new AnonymousClass1();

        /* renamed from: com.android.wm.shell.pip.phone.PipController$IPipImpl$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 implements PipAnimationListener {
            public AnonymousClass1() {
            }
        }

        public IPipImpl(PipController pipController) {
            this.mController = pipController;
            this.mListener = new SingleInstanceRemoteListener<>(pipController, new Consumer() { // from class: com.android.wm.shell.pip.phone.PipController$IPipImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PipController.IPipImpl iPipImpl = PipController.IPipImpl.this;
                    PipController pipController2 = (PipController) obj;
                    Objects.requireNonNull(iPipImpl);
                    PipController.IPipImpl.AnonymousClass1 r0 = iPipImpl.mPipAnimationListener;
                    Objects.requireNonNull(pipController2);
                    pipController2.mPinnedStackAnimationRecentsCallback = r0;
                    pipController2.onPipCornerRadiusChanged();
                }
            }, BubbleStackView$$ExternalSyntheticLambda28.INSTANCE$2);
        }
    }

    public final void onDisplayChanged(DisplayLayout displayLayout, boolean z) {
        boolean z2;
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        DisplayLayout displayLayout2 = pipBoundsState.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        if (displayLayout2.mWidth == displayLayout.mWidth && displayLayout2.mHeight == displayLayout.mHeight && displayLayout2.mRotation == displayLayout.mRotation && displayLayout2.mDensityDpi == displayLayout.mDensityDpi && Objects.equals(displayLayout2.mCutout, displayLayout.mCutout)) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            PipController$$ExternalSyntheticLambda3 pipController$$ExternalSyntheticLambda3 = new PipController$$ExternalSyntheticLambda3(this, displayLayout, 0);
            if (!this.mPipTaskOrganizer.isInPip() || !z) {
                pipController$$ExternalSyntheticLambda3.run();
                return;
            }
            PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
            Objects.requireNonNull(pipBoundsAlgorithm);
            PipSnapAlgorithm pipSnapAlgorithm = pipBoundsAlgorithm.mSnapAlgorithm;
            Rect rect = new Rect(this.mPipBoundsState.getBounds());
            PipBoundsAlgorithm pipBoundsAlgorithm2 = this.mPipBoundsAlgorithm;
            Objects.requireNonNull(pipBoundsAlgorithm2);
            Rect movementBounds = pipBoundsAlgorithm2.getMovementBounds(rect, true);
            PipBoundsState pipBoundsState2 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState2);
            float snapFraction = pipSnapAlgorithm.getSnapFraction(rect, movementBounds, pipBoundsState2.mStashedState);
            pipController$$ExternalSyntheticLambda3.run();
            Rect movementBounds2 = this.mPipBoundsAlgorithm.getMovementBounds(rect, false);
            PipBoundsState pipBoundsState3 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState3);
            int i = pipBoundsState3.mStashedState;
            PipBoundsState pipBoundsState4 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState4);
            int i2 = pipBoundsState4.mStashOffset;
            Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
            PipBoundsState pipBoundsState5 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState5);
            DisplayLayout displayLayout3 = pipBoundsState5.mDisplayLayout;
            Objects.requireNonNull(displayLayout3);
            PipSnapAlgorithm.applySnapFraction(rect, movementBounds2, snapFraction, i, i2, displayBounds, displayLayout3.mStableInsets);
            PipTouchHandler pipTouchHandler = this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler);
            PipMotionHelper pipMotionHelper = pipTouchHandler.mMotionHelper;
            Objects.requireNonNull(pipMotionHelper);
            pipMotionHelper.movePip(rect, false);
        }
    }

    public final void onPipCornerRadiusChanged() {
        if (this.mPinnedStackAnimationRecentsCallback != null) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131166780);
            IPipImpl.AnonymousClass1 r2 = (IPipImpl.AnonymousClass1) this.mPinnedStackAnimationRecentsCallback;
            Objects.requireNonNull(r2);
            SingleInstanceRemoteListener<PipController, IPipAnimationListener> singleInstanceRemoteListener = IPipImpl.this.mListener;
            Objects.requireNonNull(singleInstanceRemoteListener);
            IPipAnimationListener iPipAnimationListener = singleInstanceRemoteListener.mListener;
            if (iPipAnimationListener == null) {
                Slog.e("SingleInstanceRemoteListener", "Failed remote call on null listener");
                return;
            }
            try {
                iPipAnimationListener.onPipCornerRadiusChanged(dimensionPixelSize);
            } catch (RemoteException e) {
                Slog.e("SingleInstanceRemoteListener", "Failed remote call", e);
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public final void onPipTransitionStarted(int i, Rect rect) {
        String str;
        Context context = this.mContext;
        PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
        Objects.requireNonNull(pipTaskOrganizer);
        InteractionJankMonitor.Configuration.Builder withSurface = InteractionJankMonitor.Configuration.Builder.withSurface(35, context, pipTaskOrganizer.mLeash);
        switch (i) {
            case 2:
                str = "TRANSITION_TO_PIP";
                break;
            case 3:
                str = "TRANSITION_LEAVE_PIP";
                break;
            case 4:
                str = "TRANSITION_LEAVE_PIP_TO_SPLIT_SCREEN";
                break;
            case 5:
                str = "TRANSITION_REMOVE_STACK";
                break;
            case FalsingManager.VERSION /* 6 */:
                str = "TRANSITION_SNAP_AFTER_RESIZE";
                break;
            case 7:
                str = "TRANSITION_USER_RESIZE";
                break;
            case 8:
                str = "TRANSITION_EXPAND_OR_UNEXPAND";
                break;
            default:
                str = "TRANSITION_LEAVE_UNKNOWN";
                break;
        }
        InteractionJankMonitor.getInstance().begin(withSurface.setTag(str).setTimeout(2000L));
        if (PipAnimationController.isOutPipDirection(i)) {
            PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
            Objects.requireNonNull(pipBoundsAlgorithm);
            Rect movementBounds = pipBoundsAlgorithm.getMovementBounds(rect, true);
            PipSnapAlgorithm pipSnapAlgorithm = pipBoundsAlgorithm.mSnapAlgorithm;
            Objects.requireNonNull(pipSnapAlgorithm);
            float snapFraction = pipSnapAlgorithm.getSnapFraction(rect, movementBounds, 0);
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            if (pipBoundsState.mHasUserResizedPip) {
                PipTouchHandler pipTouchHandler = this.mTouchHandler;
                Objects.requireNonNull(pipTouchHandler);
                PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
                Objects.requireNonNull(pipResizeGestureHandler);
                Rect rect2 = pipResizeGestureHandler.mUserResizeBounds;
                Size size = new Size(rect2.width(), rect2.height());
                PipBoundsState pipBoundsState2 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState2);
                pipBoundsState2.mPipReentryState = new PipBoundsState.PipReentryState(size, snapFraction);
            } else {
                PipBoundsState pipBoundsState3 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState3);
                pipBoundsState3.mPipReentryState = new PipBoundsState.PipReentryState(null, snapFraction);
            }
        }
        PipTouchHandler pipTouchHandler2 = this.mTouchHandler;
        Objects.requireNonNull(pipTouchHandler2);
        PipTouchState pipTouchState = pipTouchHandler2.mTouchState;
        Objects.requireNonNull(pipTouchState);
        pipTouchState.mAllowTouches = false;
        if (pipTouchState.mIsUserInteracting) {
            pipTouchState.reset();
        }
        PipAnimationListener pipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (pipAnimationListener != null) {
            SingleInstanceRemoteListener<PipController, IPipAnimationListener> singleInstanceRemoteListener = IPipImpl.this.mListener;
            Objects.requireNonNull(singleInstanceRemoteListener);
            IPipAnimationListener iPipAnimationListener = singleInstanceRemoteListener.mListener;
            if (iPipAnimationListener == null) {
                Slog.e("SingleInstanceRemoteListener", "Failed remote call on null listener");
                return;
            }
            try {
                iPipAnimationListener.onPipAnimationStarted();
            } catch (RemoteException e) {
                Slog.e("SingleInstanceRemoteListener", "Failed remote call", e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x02fd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:128:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateMovementBounds(android.graphics.Rect r18, boolean r19, boolean r20, boolean r21, android.window.WindowContainerTransaction r22) {
        /*
            Method dump skipped, instructions count: 1010
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipController.updateMovementBounds(android.graphics.Rect, boolean, boolean, boolean, android.window.WindowContainerTransaction):void");
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda2] */
    public PipController(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (UserManager.get(context).getProcessUserId() == 0) {
            this.mContext = context;
            this.mImpl = new PipImpl();
            this.mWindowManagerShellWrapper = windowManagerShellWrapper;
            this.mDisplayController = displayController;
            this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
            this.mPipBoundsState = pipBoundsState;
            this.mPipTaskOrganizer = pipTaskOrganizer;
            this.mMainExecutor = shellExecutor;
            this.mMediaController = pipMediaController;
            this.mMenuController = phonePipMenuController;
            this.mTouchHandler = pipTouchHandler;
            this.mAppOpsListener = pipAppOpsListener;
            this.mOneHandedController = optional;
            this.mPipTransitionController = pipTransitionController;
            this.mTaskStackListener = taskStackListenerImpl;
            shellExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 9));
            return;
        }
        throw new IllegalStateException("Non-primary Pip component not currently supported.");
    }

    public final void onPipTransitionFinishedOrCanceled(int i) {
        InteractionJankMonitor.getInstance().end(35);
        PipTouchHandler pipTouchHandler = this.mTouchHandler;
        Objects.requireNonNull(pipTouchHandler);
        PipTouchState pipTouchState = pipTouchHandler.mTouchState;
        Objects.requireNonNull(pipTouchState);
        pipTouchState.mAllowTouches = true;
        if (pipTouchState.mIsUserInteracting) {
            pipTouchState.reset();
        }
        PipTouchHandler pipTouchHandler2 = this.mTouchHandler;
        Objects.requireNonNull(pipTouchHandler2);
        pipTouchHandler2.mMotionHelper.synchronizePinnedStackBounds();
        pipTouchHandler2.updateMovementBounds();
        if (i == 2) {
            PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler2.mPipResizeGestureHandler;
            Rect bounds = pipTouchHandler2.mPipBoundsState.getBounds();
            Objects.requireNonNull(pipResizeGestureHandler);
            pipResizeGestureHandler.mUserResizeBounds.set(bounds);
        }
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public final void onPipTransitionCanceled(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public final void onPipTransitionFinished(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }
}
