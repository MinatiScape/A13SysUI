package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import com.android.wm.shell.animation.FloatProperties;
import com.android.wm.shell.animation.FloatProperties$Companion$RECT_X$1;
import com.android.wm.shell.animation.FloatProperties$Companion$RECT_Y$1;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget$updateLocationOnScreen$1;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function5;
/* loaded from: classes.dex */
public final class PipDismissTargetHandler implements ViewTreeObserver.OnPreDrawListener {
    public final Context mContext;
    public int mDismissAreaHeight;
    public boolean mEnableDismissDragToEdge;
    public MagnetizedObject.MagneticTarget mMagneticTarget;
    public PhysicsAnimator<View> mMagneticTargetAnimator;
    public PipMotionHelper.AnonymousClass3 mMagnetizedPip;
    public final ShellExecutor mMainExecutor;
    public final PipMotionHelper mMotionHelper;
    public final PipUiEventLogger mPipUiEventLogger;
    public int mTargetSize;
    public DismissCircleView mTargetView;
    public FrameLayout mTargetViewContainer;
    public SurfaceControl mTaskLeash;
    public WindowInsets mWindowInsets;
    public final WindowManager mWindowManager;
    public final PhysicsAnimator.SpringConfig mTargetSpringConfig = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    public float mMagneticFieldRadiusPercent = 1.0f;

    /* renamed from: com.android.wm.shell.pip.phone.PipDismissTargetHandler$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements MagnetizedObject.MagnetListener {
        public AnonymousClass1() {
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onReleasedInTarget() {
            PipDismissTargetHandler pipDismissTargetHandler = PipDismissTargetHandler.this;
            if (pipDismissTargetHandler.mEnableDismissDragToEdge) {
                pipDismissTargetHandler.mMainExecutor.executeDelayed(new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 8), 0L);
            }
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onStuckToTarget() {
            PipDismissTargetHandler pipDismissTargetHandler = PipDismissTargetHandler.this;
            if (pipDismissTargetHandler.mEnableDismissDragToEdge) {
                pipDismissTargetHandler.showDismissTargetMaybe();
            }
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onUnstuckFromTarget(float f, float f2, boolean z) {
            if (z) {
                PipMotionHelper pipMotionHelper = PipDismissTargetHandler.this.mMotionHelper;
                Objects.requireNonNull(pipMotionHelper);
                pipMotionHelper.movetoTarget(f, f2, null, false);
                PipDismissTargetHandler.this.hideDismissTargetMaybe();
                return;
            }
            PipMotionHelper pipMotionHelper2 = PipDismissTargetHandler.this.mMotionHelper;
            Objects.requireNonNull(pipMotionHelper2);
            pipMotionHelper2.mSpringingToTouch = true;
        }
    }

    public final void createOrUpdateDismissTarget() {
        if (!this.mTargetViewContainer.isAttachedToWindow()) {
            this.mMagneticTargetAnimator.cancel();
            this.mTargetViewContainer.setVisibility(4);
            this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
            try {
                this.mWindowManager.addView(this.mTargetViewContainer, getDismissTargetLayoutParams());
            } catch (IllegalStateException unused) {
                this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
            }
        } else {
            this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
        }
    }

    public final WindowManager.LayoutParams getDismissTargetLayoutParams() {
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getRealSize(point);
        int i = this.mDismissAreaHeight;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, i, 0, point.y - i, 2024, 280, -3);
        layoutParams.setTitle("pip-dismiss-overlay");
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        return layoutParams;
    }

    public final void hideDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            PhysicsAnimator<View> physicsAnimator = this.mMagneticTargetAnimator;
            PhysicsAnimator.SpringConfig springConfig = this.mTargetSpringConfig;
            Objects.requireNonNull(physicsAnimator);
            physicsAnimator.spring(DynamicAnimation.TRANSLATION_Y, this.mTargetViewContainer.getHeight(), 0.0f, springConfig);
            physicsAnimator.withEndActions(new AccessPoint$$ExternalSyntheticLambda0(this, 11));
            physicsAnimator.start();
            ((TransitionDrawable) this.mTargetViewContainer.getBackground()).reverseTransition(200);
        }
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [com.android.wm.shell.pip.phone.PipMotionHelper$3] */
    public final void init() {
        Resources resources = this.mContext.getResources();
        this.mEnableDismissDragToEdge = resources.getBoolean(2131034149);
        this.mDismissAreaHeight = resources.getDimensionPixelSize(2131165727);
        FrameLayout frameLayout = this.mTargetViewContainer;
        if (frameLayout != null && frameLayout.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(this.mTargetViewContainer);
        }
        this.mTargetView = new DismissCircleView(this.mContext);
        FrameLayout frameLayout2 = new FrameLayout(this.mContext);
        this.mTargetViewContainer = frameLayout2;
        frameLayout2.setBackgroundDrawable(this.mContext.getDrawable(2131231711));
        this.mTargetViewContainer.setClipChildren(false);
        this.mTargetViewContainer.addView(this.mTargetView);
        this.mTargetViewContainer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.wm.shell.pip.phone.PipDismissTargetHandler$$ExternalSyntheticLambda0
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                PipDismissTargetHandler pipDismissTargetHandler = PipDismissTargetHandler.this;
                Objects.requireNonNull(pipDismissTargetHandler);
                if (!windowInsets.equals(pipDismissTargetHandler.mWindowInsets)) {
                    pipDismissTargetHandler.mWindowInsets = windowInsets;
                    pipDismissTargetHandler.updateMagneticTargetSize();
                }
                return windowInsets;
            }
        });
        PipMotionHelper pipMotionHelper = this.mMotionHelper;
        Objects.requireNonNull(pipMotionHelper);
        if (pipMotionHelper.mMagnetizedPip == null) {
            Context context = pipMotionHelper.mContext;
            PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
            Objects.requireNonNull(motionBoundsState);
            pipMotionHelper.mMagnetizedPip = new MagnetizedObject<Rect>(context, motionBoundsState.mBoundsInMotion) { // from class: com.android.wm.shell.pip.phone.PipMotionHelper.3
                {
                    FloatProperties$Companion$RECT_X$1 floatProperties$Companion$RECT_X$1 = FloatProperties.RECT_X;
                    FloatProperties$Companion$RECT_Y$1 floatProperties$Companion$RECT_Y$1 = FloatProperties.RECT_Y;
                }

                @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
                public final float getHeight(Rect rect) {
                    return rect.height();
                }

                @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
                public final void getLocationOnScreen(Rect rect, int[] iArr) {
                    Rect rect2 = rect;
                    iArr[0] = rect2.left;
                    iArr[1] = rect2.top;
                }

                @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject
                public final float getWidth(Rect rect) {
                    return rect.width();
                }
            };
        }
        PipMotionHelper.AnonymousClass3 r0 = pipMotionHelper.mMagnetizedPip;
        this.mMagnetizedPip = r0;
        Objects.requireNonNull(r0);
        r0.associatedTargets.clear();
        PipMotionHelper.AnonymousClass3 r02 = this.mMagnetizedPip;
        DismissCircleView dismissCircleView = this.mTargetView;
        Objects.requireNonNull(r02);
        MagnetizedObject.MagneticTarget magneticTarget = new MagnetizedObject.MagneticTarget(dismissCircleView, 0);
        r02.associatedTargets.add(magneticTarget);
        dismissCircleView.post(new MagnetizedObject$MagneticTarget$updateLocationOnScreen$1(magneticTarget));
        this.mMagneticTarget = magneticTarget;
        updateMagneticTargetSize();
        PipMotionHelper.AnonymousClass3 r03 = this.mMagnetizedPip;
        Function5<? super MagnetizedObject.MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> pipDismissTargetHandler$$ExternalSyntheticLambda1 = new Function5() { // from class: com.android.wm.shell.pip.phone.PipDismissTargetHandler$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function5
            public final void invoke(MagnetizedObject.MagneticTarget magneticTarget2, Float f, Float f2, Boolean bool, Object obj) {
                PipDismissTargetHandler pipDismissTargetHandler = PipDismissTargetHandler.this;
                Function0 function0 = (Function0) obj;
                Objects.requireNonNull(pipDismissTargetHandler);
                if (pipDismissTargetHandler.mEnableDismissDragToEdge) {
                    PipMotionHelper pipMotionHelper2 = pipDismissTargetHandler.mMotionHelper;
                    float floatValue = f.floatValue();
                    float floatValue2 = f2.floatValue();
                    bool.booleanValue();
                    Objects.requireNonNull(pipMotionHelper2);
                    PointF pointF = magneticTarget2.centerOnScreen;
                    float dimensionPixelSize = pipMotionHelper2.mContext.getResources().getDimensionPixelSize(2131165666) * 0.85f;
                    float width = dimensionPixelSize / (pipMotionHelper2.getBounds().width() / pipMotionHelper2.getBounds().height());
                    float f3 = pointF.x - (dimensionPixelSize / 2.0f);
                    float f4 = pointF.y - (width / 2.0f);
                    PipBoundsState pipBoundsState2 = pipMotionHelper2.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState2);
                    PipBoundsState.MotionBoundsState motionBoundsState2 = pipBoundsState2.mMotionBoundsState;
                    Objects.requireNonNull(motionBoundsState2);
                    if (!(!motionBoundsState2.mBoundsInMotion.isEmpty())) {
                        PipBoundsState pipBoundsState3 = pipMotionHelper2.mPipBoundsState;
                        Objects.requireNonNull(pipBoundsState3);
                        PipBoundsState.MotionBoundsState motionBoundsState3 = pipBoundsState3.mMotionBoundsState;
                        Rect bounds = pipMotionHelper2.getBounds();
                        Objects.requireNonNull(motionBoundsState3);
                        motionBoundsState3.mBoundsInMotion.set(bounds);
                    }
                    PhysicsAnimator<Rect> physicsAnimator = pipMotionHelper2.mTemporaryBoundsPhysicsAnimator;
                    physicsAnimator.spring(FloatProperties.RECT_X, f3, floatValue, pipMotionHelper2.mAnimateToDismissSpringConfig);
                    physicsAnimator.spring(FloatProperties.RECT_Y, f4, floatValue2, pipMotionHelper2.mAnimateToDismissSpringConfig);
                    physicsAnimator.spring(FloatProperties.RECT_WIDTH, dimensionPixelSize, 0.0f, pipMotionHelper2.mAnimateToDismissSpringConfig);
                    physicsAnimator.spring(FloatProperties.RECT_HEIGHT, width, 0.0f, pipMotionHelper2.mAnimateToDismissSpringConfig);
                    physicsAnimator.endActions.addAll(ArraysKt___ArraysKt.filterNotNull(new Function0[]{function0}));
                    pipMotionHelper2.startBoundsAnimator(f3, f4, null);
                }
            }
        };
        Objects.requireNonNull(r03);
        r03.animateStuckToTarget = pipDismissTargetHandler$$ExternalSyntheticLambda1;
        PipMotionHelper.AnonymousClass3 r04 = this.mMagnetizedPip;
        AnonymousClass1 r1 = new AnonymousClass1();
        Objects.requireNonNull(r04);
        r04.magnetListener = r1;
        DismissCircleView dismissCircleView2 = this.mTargetView;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        this.mMagneticTargetAnimator = PhysicsAnimator.Companion.getInstance(dismissCircleView2);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public final boolean onPreDraw() {
        this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
        if (this.mTaskLeash == null) {
            return true;
        }
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setRelativeLayer(this.mTargetViewContainer.getViewRootImpl().getSurfaceControl(), this.mTaskLeash, -1);
        transaction.apply();
        return true;
    }

    public final void showDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            createOrUpdateDismissTarget();
            if (this.mTargetViewContainer.getVisibility() != 0) {
                this.mTargetView.setTranslationY(this.mTargetViewContainer.getHeight());
                this.mTargetViewContainer.setVisibility(0);
                this.mTargetViewContainer.getViewTreeObserver().addOnPreDrawListener(this);
                this.mMagneticTargetAnimator.cancel();
                PhysicsAnimator<View> physicsAnimator = this.mMagneticTargetAnimator;
                DynamicAnimation.AnonymousClass2 r1 = DynamicAnimation.TRANSLATION_Y;
                PhysicsAnimator.SpringConfig springConfig = this.mTargetSpringConfig;
                Objects.requireNonNull(physicsAnimator);
                physicsAnimator.spring(r1, 0.0f, 0.0f, springConfig);
                physicsAnimator.start();
                ((TransitionDrawable) this.mTargetViewContainer.getBackground()).startTransition(200);
            }
        }
    }

    public final void updateMagneticTargetSize() {
        if (this.mTargetView != null) {
            Resources resources = this.mContext.getResources();
            this.mTargetSize = resources.getDimensionPixelSize(2131165666);
            this.mDismissAreaHeight = resources.getDimensionPixelSize(2131165727);
            Insets insetsIgnoringVisibility = this.mWindowManager.getCurrentWindowMetrics().getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
            int i = this.mTargetSize;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i);
            layoutParams.gravity = 81;
            layoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(2131165726) + insetsIgnoringVisibility.bottom;
            this.mTargetView.setLayoutParams(layoutParams);
            float f = this.mMagneticFieldRadiusPercent;
            this.mMagneticFieldRadiusPercent = f;
            MagnetizedObject.MagneticTarget magneticTarget = this.mMagneticTarget;
            Objects.requireNonNull(magneticTarget);
            magneticTarget.magneticFieldRadiusPx = (int) (f * this.mTargetSize * 1.25f);
        }
    }

    public PipDismissTargetHandler(Context context, PipUiEventLogger pipUiEventLogger, PipMotionHelper pipMotionHelper, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mMotionHelper = pipMotionHelper;
        this.mMainExecutor = shellExecutor;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }
}
