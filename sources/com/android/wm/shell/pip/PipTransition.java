package com.android.wm.shell.pip;

import android.app.ActivityManager;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.util.RotationUtils;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.systemui.plugins.qs.QS;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.CounterRotatorHelper;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda1;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class PipTransition extends PipTransitionController {
    public final Context mContext;
    public WindowContainerToken mCurrentPipTaskToken;
    public final int mEnterExitAnimationDuration;
    public IBinder mExitTransition;
    public Transitions.TransitionFinishCallback mFinishCallback;
    public int mFixedRotation;
    public boolean mHasFadeOut;
    public boolean mInFixedRotation;
    public final PipTransitionState mPipTransitionState;
    public final Optional<SplitScreenController> mSplitScreenOptional;
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;
    public int mOneShotAnimationType = 0;
    public final Rect mExitDestinationBounds = new Rect();

    public PipTransition(Context context, PipBoundsState pipBoundsState, PipTransitionState pipTransitionState, PhonePipMenuController phonePipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional optional) {
        super(pipBoundsState, phonePipMenuController, pipBoundsAlgorithm, pipAnimationController, transitions);
        this.mContext = context;
        this.mPipTransitionState = pipTransitionState;
        this.mEnterExitAnimationDuration = context.getResources().getInteger(2131492902);
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        this.mSplitScreenOptional = optional;
    }

    public final void fadeExistingPip(boolean z) {
        float f;
        float f2;
        PipTaskOrganizer pipTaskOrganizer = this.mPipOrganizer;
        Objects.requireNonNull(pipTaskOrganizer);
        SurfaceControl surfaceControl = pipTaskOrganizer.mLeash;
        PipTaskOrganizer pipTaskOrganizer2 = this.mPipOrganizer;
        Objects.requireNonNull(pipTaskOrganizer2);
        ActivityManager.RunningTaskInfo runningTaskInfo = pipTaskOrganizer2.mTaskInfo;
        if (surfaceControl == null || !surfaceControl.isValid() || runningTaskInfo == null) {
            Log.w("PipTransition", "Invalid leash on fadeExistingPip: " + surfaceControl);
            return;
        }
        if (z) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        if (z) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        this.mPipAnimationController.getAnimator(runningTaskInfo, surfaceControl, this.mPipBoundsState.getBounds(), f, f2).setTransitionDirection(1).setPipAnimationCallback(this.mPipAnimationCallback).setDuration(this.mEnterExitAnimationDuration).start();
        this.mHasFadeOut = !z;
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void forceFinishTransition() {
        Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
        if (transitionFinishCallback != null) {
            transitionFinishCallback.onTransitionFinished(null);
            this.mFinishCallback = null;
        }
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void onFixedRotationStarted() {
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        if (pipTransitionState.mState == 4 && !this.mHasFadeOut) {
            fadeExistingPip(false);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final void onTransitionMerged(IBinder iBinder) {
        if (iBinder == this.mExitTransition) {
            boolean z = false;
            PipAnimationController pipAnimationController = this.mPipAnimationController;
            Objects.requireNonNull(pipAnimationController);
            if (pipAnimationController.mCurrentAnimator != null) {
                PipAnimationController pipAnimationController2 = this.mPipAnimationController;
                Objects.requireNonNull(pipAnimationController2);
                pipAnimationController2.mCurrentAnimator.cancel();
                z = true;
            }
            this.mExitTransition = null;
            if (z) {
                PipTaskOrganizer pipTaskOrganizer = this.mPipOrganizer;
                Objects.requireNonNull(pipTaskOrganizer);
                ActivityManager.RunningTaskInfo runningTaskInfo = pipTaskOrganizer.mTaskInfo;
                if (runningTaskInfo != null) {
                    PipTaskOrganizer pipTaskOrganizer2 = this.mPipOrganizer;
                    Objects.requireNonNull(pipTaskOrganizer2);
                    startExpandAnimation(runningTaskInfo, pipTaskOrganizer2.mLeash, new Rect(this.mExitDestinationBounds));
                }
                this.mExitDestinationBounds.setEmpty();
                this.mCurrentPipTaskToken = null;
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void setIsFullAnimation(boolean z) {
        this.mOneShotAnimationType = !z ? 1 : 0;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        final TransitionInfo.Change change;
        TransitionInfo.Change change2;
        boolean z;
        int i;
        int i2;
        boolean z2;
        int i3;
        PipAnimationController.PipTransitionAnimator pipTransitionAnimator;
        Rect rect;
        int i4;
        int i5;
        int i6;
        boolean z3;
        TransitionInfo.Change change3 = null;
        if (this.mCurrentPipTaskToken != null) {
            for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
                TransitionInfo.Change change4 = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                if (this.mCurrentPipTaskToken.equals(change4.getContainer())) {
                    change = change4;
                    break;
                }
            }
        }
        change = null;
        int size2 = transitionInfo.getChanges().size() - 1;
        while (true) {
            if (size2 < 0) {
                change2 = null;
                break;
            }
            change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            if (change2.getEndFixedRotation() != -1) {
                break;
            }
            size2--;
        }
        if (change2 != null) {
            z = true;
        } else {
            z = false;
        }
        this.mInFixedRotation = z;
        if (z) {
            i = change2.getEndFixedRotation();
        } else {
            i = -1;
        }
        this.mFixedRotation = i;
        int type = transitionInfo.getType();
        if (iBinder.equals(this.mExitTransition)) {
            this.mExitDestinationBounds.setEmpty();
            this.mExitTransition = null;
            this.mHasFadeOut = false;
            Transitions.TransitionFinishCallback transitionFinishCallback2 = this.mFinishCallback;
            if (transitionFinishCallback2 != null) {
                transitionFinishCallback2.onTransitionFinished(null);
                this.mFinishCallback = null;
                throw new RuntimeException("Previous callback not called, aborting exit PIP.");
            } else if (change != null) {
                switch (type) {
                    case QS.VERSION /* 13 */:
                        int size3 = transitionInfo.getChanges().size() - 1;
                        while (true) {
                            if (size3 >= 0) {
                                TransitionInfo.Change change5 = (TransitionInfo.Change) transitionInfo.getChanges().get(size3);
                                if (change5.getMode() != 6 || (change5.getFlags() & 32) == 0 || change5.getStartRotation() == change5.getEndRotation()) {
                                    size3--;
                                } else {
                                    change3 = change5;
                                }
                            }
                        }
                        if (change3 != null) {
                            int deltaRotation = RotationUtils.deltaRotation(change3.getStartRotation(), change3.getEndRotation());
                            CounterRotatorHelper counterRotatorHelper = new CounterRotatorHelper();
                            counterRotatorHelper.handleClosingChanges(transitionInfo, transaction, change3);
                            final Transitions$$ExternalSyntheticLambda1 transitions$$ExternalSyntheticLambda1 = (Transitions$$ExternalSyntheticLambda1) transitionFinishCallback;
                            this.mFinishCallback = new Transitions.TransitionFinishCallback() { // from class: com.android.wm.shell.pip.PipTransition$$ExternalSyntheticLambda1
                                @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
                                public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                                    PipTransition pipTransition = PipTransition.this;
                                    TransitionInfo.Change change6 = change;
                                    Transitions.TransitionFinishCallback transitionFinishCallback3 = transitions$$ExternalSyntheticLambda1;
                                    Objects.requireNonNull(pipTransition);
                                    pipTransition.mPipOrganizer.onExitPipFinished(change6.getTaskInfo());
                                    transitionFinishCallback3.onTransitionFinished(windowContainerTransaction);
                                }
                            };
                            Rect rect2 = new Rect(change.getStartAbsBounds());
                            RotationUtils.rotateBounds(rect2, change3.getStartAbsBounds(), deltaRotation);
                            Rect rect3 = new Rect(change.getEndAbsBounds());
                            Point endRelOffset = change.getEndRelOffset();
                            rect2.offset(-endRelOffset.x, -endRelOffset.y);
                            rect3.offset(-endRelOffset.x, -endRelOffset.y);
                            int deltaRotation2 = RotationUtils.deltaRotation(deltaRotation, 0);
                            if (deltaRotation2 == 1) {
                                i6 = 90;
                                i5 = rect2.right;
                                i4 = rect2.top;
                            } else {
                                i6 = -90;
                                i5 = rect2.left;
                                i4 = rect2.bottom;
                            }
                            PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                            SurfaceControl leash = change.getLeash();
                            Rect rect4 = new Rect();
                            float f = i6;
                            float f2 = i5;
                            float f3 = i4;
                            if (deltaRotation2 == 3) {
                                z3 = true;
                            } else {
                                z3 = false;
                            }
                            pipSurfaceTransactionHelper.rotateAndScaleWithCrop(transaction, leash, rect3, rect2, rect4, f, f2, f3, true, z3);
                            transaction.apply();
                            counterRotatorHelper.cleanUp(transaction2);
                            this.mPipAnimationController.getAnimator(change.getTaskInfo(), change.getLeash(), rect2, rect2, rect3, null, 3, 0.0f, deltaRotation2).setTransitionDirection(3).setPipAnimationCallback(this.mPipAnimationCallback).setDuration(this.mEnterExitAnimationDuration).start();
                        } else {
                            final Transitions$$ExternalSyntheticLambda1 transitions$$ExternalSyntheticLambda12 = (Transitions$$ExternalSyntheticLambda1) transitionFinishCallback;
                            this.mFinishCallback = new Transitions.TransitionFinishCallback() { // from class: com.android.wm.shell.pip.PipTransition$$ExternalSyntheticLambda0
                                @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
                                public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                                    PipTransition pipTransition = PipTransition.this;
                                    TransitionInfo.Change change6 = change;
                                    Transitions.TransitionFinishCallback transitionFinishCallback3 = transitions$$ExternalSyntheticLambda12;
                                    Objects.requireNonNull(pipTransition);
                                    pipTransition.mPipOrganizer.onExitPipFinished(change6.getTaskInfo());
                                    transitionFinishCallback3.onTransitionFinished(windowContainerTransaction);
                                }
                            };
                            Rect rect5 = new Rect(change.getEndAbsBounds());
                            Point endRelOffset2 = change.getEndRelOffset();
                            rect5.offset(-endRelOffset2.x, -endRelOffset2.y);
                            transaction.setWindowCrop(change.getLeash(), rect5);
                            PipSurfaceTransactionHelper pipSurfaceTransactionHelper2 = this.mSurfaceTransactionHelper;
                            SurfaceControl leash2 = change.getLeash();
                            Rect bounds = this.mPipBoundsState.getBounds();
                            Objects.requireNonNull(pipSurfaceTransactionHelper2);
                            pipSurfaceTransactionHelper2.scale(transaction, leash2, rect5, bounds, 0.0f);
                            transaction.apply();
                            startExpandAnimation(change.getTaskInfo(), change.getLeash(), rect5);
                        }
                        change3 = null;
                        break;
                    case 14:
                        int size4 = transitionInfo.getChanges().size();
                        if (size4 >= 4) {
                            for (int i7 = size4 - 1; i7 >= 0; i7--) {
                                TransitionInfo.Change change6 = (TransitionInfo.Change) transitionInfo.getChanges().get(i7);
                                int mode = change6.getMode();
                                if ((mode != 6 || change6.getParent() == null) && Transitions.isOpeningType(mode) && change6.getParent() == null) {
                                    SurfaceControl leash3 = change6.getLeash();
                                    Rect endAbsBounds = change6.getEndAbsBounds();
                                    transaction.show(leash3).setAlpha(leash3, 1.0f).setPosition(leash3, endAbsBounds.left, endAbsBounds.top).setWindowCrop(leash3, endAbsBounds.width(), endAbsBounds.height());
                                }
                            }
                            SplitScreenController splitScreenController = this.mSplitScreenOptional.get();
                            Objects.requireNonNull(splitScreenController);
                            splitScreenController.mStageCoordinator.finishEnterSplitScreen(transaction);
                            transaction.apply();
                            this.mPipOrganizer.onExitPipFinished(change.getTaskInfo());
                            ((Transitions$$ExternalSyntheticLambda1) transitionFinishCallback).onTransitionFinished(null);
                            break;
                        } else {
                            throw new RuntimeException("Got an exit-pip-to-split transition with unexpected change-list");
                        }
                    case 15:
                        transaction.apply();
                        transaction2.setWindowCrop(((TransitionInfo.Change) transitionInfo.getChanges().get(0)).getLeash(), this.mPipBoundsState.getDisplayBounds());
                        this.mPipOrganizer.onExitPipFinished(change.getTaskInfo());
                        ((Transitions$$ExternalSyntheticLambda1) transitionFinishCallback).onTransitionFinished(null);
                        break;
                    default:
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mExitTransition with unexpected transit type=");
                        m.append(WindowManager.transitTypeToString(type));
                        throw new IllegalStateException(m.toString());
                }
                this.mCurrentPipTaskToken = change3;
                return true;
            } else {
                throw new RuntimeException("Cannot find the pip window for exit-pip transition.");
            }
        } else {
            if (!(change == null || change.getTaskInfo().getWindowingMode() == 2)) {
                SurfaceControl leash4 = change.getLeash();
                Rect endAbsBounds2 = change.getEndAbsBounds();
                Point endRelOffset3 = change.getEndRelOffset();
                endAbsBounds2.offset(-endRelOffset3.x, -endRelOffset3.y);
                transaction.setWindowCrop(leash4, null);
                transaction.setMatrix(leash4, 1.0f, 0.0f, 0.0f, 1.0f);
                transaction.setCornerRadius(leash4, 0.0f);
                transaction.setPosition(leash4, endAbsBounds2.left, endAbsBounds2.top);
                if (this.mHasFadeOut && change.getTaskInfo().isVisible()) {
                    PipAnimationController pipAnimationController = this.mPipAnimationController;
                    Objects.requireNonNull(pipAnimationController);
                    if (pipAnimationController.mCurrentAnimator != null) {
                        PipAnimationController pipAnimationController2 = this.mPipAnimationController;
                        Objects.requireNonNull(pipAnimationController2);
                        pipAnimationController2.mCurrentAnimator.cancel();
                    }
                    transaction.setAlpha(leash4, 1.0f);
                }
                this.mHasFadeOut = false;
                this.mCurrentPipTaskToken = null;
                this.mPipOrganizer.onExitPipFinished(change.getTaskInfo());
            }
            WindowContainerToken windowContainerToken = this.mCurrentPipTaskToken;
            int size5 = transitionInfo.getChanges().size() - 1;
            while (true) {
                if (size5 < 0) {
                    i2 = 2;
                    z2 = false;
                    break;
                }
                TransitionInfo.Change change7 = (TransitionInfo.Change) transitionInfo.getChanges().get(size5);
                if (change7.getTaskInfo() != null) {
                    i2 = 2;
                    if (change7.getTaskInfo().getWindowingMode() == 2 && !change7.getContainer().equals(windowContainerToken)) {
                        if (transitionInfo.getType() == 10 || transitionInfo.getType() == 1 || transitionInfo.getType() == 6) {
                            z2 = true;
                        } else {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Entering PIP with unexpected transition type=");
                            m2.append(WindowManager.transitTypeToString(transitionInfo.getType()));
                            throw new IllegalStateException(m2.toString());
                        }
                    }
                }
                size5--;
            }
            if (z2) {
                TransitionInfo.Change change8 = null;
                TransitionInfo.Change change9 = null;
                for (int size6 = transitionInfo.getChanges().size() - 1; size6 >= 0; size6--) {
                    TransitionInfo.Change change10 = (TransitionInfo.Change) transitionInfo.getChanges().get(size6);
                    if (change10.getTaskInfo() != null && change10.getTaskInfo().getWindowingMode() == i2) {
                        change8 = change10;
                    } else if ((change10.getFlags() & i2) != 0) {
                        change9 = change10;
                    }
                }
                if (change8 == null) {
                    return false;
                }
                this.mCurrentPipTaskToken = change8.getContainer();
                this.mHasFadeOut = false;
                Transitions.TransitionFinishCallback transitionFinishCallback3 = this.mFinishCallback;
                if (transitionFinishCallback3 == null) {
                    if (change9 != null) {
                        transaction.show(change9.getLeash());
                        transaction.setAlpha(change9.getLeash(), 1.0f);
                    }
                    for (int size7 = transitionInfo.getChanges().size() - 1; size7 >= 0; size7--) {
                        TransitionInfo.Change change11 = (TransitionInfo.Change) transitionInfo.getChanges().get(size7);
                        if (!(change11 == change8 || change11 == change9 || !Transitions.isOpeningType(change11.getMode()))) {
                            SurfaceControl leash5 = change11.getLeash();
                            transaction.show(leash5).setAlpha(leash5, 1.0f);
                        }
                    }
                    PipTransitionState pipTransitionState = this.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState);
                    pipTransitionState.mState = 3;
                    this.mFinishCallback = transitionFinishCallback;
                    if (this.mInFixedRotation) {
                        i3 = this.mFixedRotation;
                    } else {
                        i3 = change8.getEndRotation();
                    }
                    ActivityManager.RunningTaskInfo taskInfo = change8.getTaskInfo();
                    SurfaceControl leash6 = change8.getLeash();
                    int startRotation = change8.getStartRotation();
                    this.mPipBoundsState.setBoundsStateForEntry(((TaskInfo) taskInfo).topActivity, ((TaskInfo) taskInfo).topActivityInfo, ((TaskInfo) taskInfo).pictureInPictureParams, this.mPipBoundsAlgorithm);
                    Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
                    Rect bounds2 = ((TaskInfo) taskInfo).configuration.windowConfiguration.getBounds();
                    int deltaRotation3 = RotationUtils.deltaRotation(startRotation, i3);
                    Rect validSourceHintRect = PipBoundsAlgorithm.getValidSourceHintRect(((TaskInfo) taskInfo).pictureInPictureParams, bounds2);
                    if (deltaRotation3 != 0 && this.mInFixedRotation) {
                        PipBoundsState pipBoundsState = this.mPipBoundsState;
                        Objects.requireNonNull(pipBoundsState);
                        pipBoundsState.mDisplayLayout.rotateTo(this.mContext.getResources(), i3);
                        Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
                        entryDestinationBounds.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
                        RotationUtils.rotateBounds(entryDestinationBounds, displayBounds, i3, startRotation);
                        if (!(validSourceHintRect == null || (rect = ((TaskInfo) taskInfo).displayCutoutInsets) == null || deltaRotation3 != 3)) {
                            validSourceHintRect.offset(rect.left, rect.top);
                        }
                    }
                    PipSurfaceTransactionHelper pipSurfaceTransactionHelper3 = this.mSurfaceTransactionHelper;
                    pipSurfaceTransactionHelper3.crop(transaction2, leash6, entryDestinationBounds);
                    pipSurfaceTransactionHelper3.round(transaction2, leash6, true);
                    this.mPipMenuController.attach(leash6);
                    PictureInPictureParams pictureInPictureParams = ((TaskInfo) taskInfo).pictureInPictureParams;
                    if (pictureInPictureParams != null && pictureInPictureParams.isAutoEnterEnabled()) {
                        PipTransitionState pipTransitionState2 = this.mPipTransitionState;
                        Objects.requireNonNull(pipTransitionState2);
                        if (pipTransitionState2.mInSwipePipToHomeTransition) {
                            this.mOneShotAnimationType = 0;
                            SurfaceControl.Transaction transaction3 = new SurfaceControl.Transaction();
                            transaction3.setMatrix(leash6, Matrix.IDENTITY_MATRIX, new float[9]).setPosition(leash6, entryDestinationBounds.left, entryDestinationBounds.top).setWindowCrop(leash6, entryDestinationBounds.width(), entryDestinationBounds.height());
                            transaction.merge(transaction3);
                            transaction.apply();
                            if (deltaRotation3 != 0 && this.mInFixedRotation) {
                                entryDestinationBounds.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
                            }
                            this.mPipBoundsState.setBounds(entryDestinationBounds);
                            onFinishResize(taskInfo, entryDestinationBounds, i2, null);
                            sendOnPipTransitionFinished(i2);
                            PipTransitionState pipTransitionState3 = this.mPipTransitionState;
                            Objects.requireNonNull(pipTransitionState3);
                            pipTransitionState3.mInSwipePipToHomeTransition = false;
                            return true;
                        }
                    }
                    if (deltaRotation3 != 0) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(deltaRotation3);
                        transaction.setMatrix(leash6, matrix, new float[9]);
                    }
                    int i8 = this.mOneShotAnimationType;
                    if (i8 == 0) {
                        deltaRotation3 = RotationUtils.deltaRotation(deltaRotation3, 0);
                        pipTransitionAnimator = this.mPipAnimationController.getAnimator(taskInfo, leash6, bounds2, bounds2, entryDestinationBounds, validSourceHintRect, 2, 0.0f, deltaRotation3);
                        if (validSourceHintRect == null) {
                            pipTransitionAnimator.setUseContentOverlay(this.mContext);
                        }
                    } else if (i8 == 1) {
                        transaction.setAlpha(leash6, 0.0f);
                        pipTransitionAnimator = this.mPipAnimationController.getAnimator(taskInfo, leash6, entryDestinationBounds, 0.0f, 1.0f);
                        this.mOneShotAnimationType = 0;
                    } else {
                        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unrecognized animation type: ");
                        m3.append(this.mOneShotAnimationType);
                        throw new RuntimeException(m3.toString());
                    }
                    transaction.apply();
                    pipTransitionAnimator.setTransitionDirection(i2).setPipAnimationCallback(this.mPipAnimationCallback).setDuration(this.mEnterExitAnimationDuration);
                    if (deltaRotation3 != 0 && this.mInFixedRotation) {
                        pipTransitionAnimator.setDestinationBounds(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
                    }
                    pipTransitionAnimator.start();
                    return true;
                }
                transitionFinishCallback3.onTransitionFinished(null);
                this.mFinishCallback = null;
                throw new RuntimeException("Previous callback not called, aborting entering PIP.");
            }
            if (change != null) {
                SurfaceControl leash7 = change.getLeash();
                Rect bounds3 = this.mPipBoundsState.getBounds();
                boolean isInPip = this.mPipTransitionState.isInPip();
                PipSurfaceTransactionHelper pipSurfaceTransactionHelper4 = this.mSurfaceTransactionHelper;
                pipSurfaceTransactionHelper4.crop(transaction, leash7, bounds3);
                pipSurfaceTransactionHelper4.round(transaction, leash7, isInPip);
                PipSurfaceTransactionHelper pipSurfaceTransactionHelper5 = this.mSurfaceTransactionHelper;
                pipSurfaceTransactionHelper5.crop(transaction2, leash7, bounds3);
                pipSurfaceTransactionHelper5.round(transaction2, leash7, isInPip);
            }
            if (!this.mPipTransitionState.isInPip() || this.mInFixedRotation || !this.mHasFadeOut) {
                return false;
            }
            fadeExistingPip(true);
            return false;
        }
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void startExitTransition(int i, WindowContainerTransaction windowContainerTransaction, Rect rect) {
        if (rect != null) {
            this.mExitDestinationBounds.set(rect);
        }
        this.mExitTransition = this.mTransitions.startTransition(i, windowContainerTransaction, this);
    }

    public final void startExpandAnimation(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect) {
        this.mPipAnimationController.getAnimator(runningTaskInfo, surfaceControl, this.mPipBoundsState.getBounds(), this.mPipBoundsState.getBounds(), rect, null, 3, 0.0f, 0).setTransitionDirection(3).setPipAnimationCallback(this.mPipAnimationCallback).setDuration(this.mEnterExitAnimationDuration).start();
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (transitionRequestInfo.getType() != 10) {
            return null;
        }
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (this.mOneShotAnimationType == 1) {
            windowContainerTransaction.setActivityWindowingMode(transitionRequestInfo.getTriggerTask().token, 0);
            windowContainerTransaction.setBounds(transitionRequestInfo.getTriggerTask().token, this.mPipBoundsAlgorithm.getEntryDestinationBounds());
        }
        return windowContainerTransaction;
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
        Rect rect2;
        if (PipAnimationController.isInPipDirection(i)) {
            PipTransitionState pipTransitionState = this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState);
            pipTransitionState.mState = 4;
        }
        if (this.mExitTransition == null && this.mFinishCallback != null) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            if (PipAnimationController.isInPipDirection(i)) {
                windowContainerTransaction.setActivityWindowingMode(taskInfo.token, 0);
                windowContainerTransaction.scheduleFinishEnterPip(taskInfo.token, rect);
                rect2 = rect;
            } else if (PipAnimationController.isOutPipDirection(i)) {
                if (i == 3) {
                    rect2 = null;
                } else {
                    rect2 = rect;
                }
                windowContainerTransaction.setWindowingMode(taskInfo.token, 0);
                windowContainerTransaction.setActivityWindowingMode(taskInfo.token, 0);
            } else {
                rect2 = null;
            }
            windowContainerTransaction.setBounds(taskInfo.token, rect2);
            if (transaction != null) {
                windowContainerTransaction.setBoundsChangeTransaction(taskInfo.token, transaction);
            }
            this.mFinishCallback.onTransitionFinished(windowContainerTransaction);
            this.mFinishCallback = null;
        }
        this.mPipMenuController.movePipMenu(null, null, rect);
        this.mPipMenuController.updateMenuBounds(rect);
    }
}
