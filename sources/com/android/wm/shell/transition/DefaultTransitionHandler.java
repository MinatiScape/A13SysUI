package com.android.wm.shell.transition;

import android.animation.Animator;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.hardware.HardwareBuffer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.AttributeCache;
import com.android.internal.policy.TransitionAnimation;
import com.android.internal.protolog.BaseProtoLogImpl;
import com.android.systemui.qs.QSSecurityFooter$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DefaultTransitionHandler implements Transitions.TransitionHandler {
    public static boolean sDisableCustomTaskAnimationProperty = SystemProperties.getBoolean("persist.wm.disable_custom_task_animation", true);
    public final ShellExecutor mAnimExecutor;
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DisplayController mDisplayController;
    public Drawable mEnterpriseThumbnailDrawable;
    public final ShellExecutor mMainExecutor;
    public ScreenRotationAnimation mRotationAnimation;
    public final TransactionPool mTransactionPool;
    public final TransitionAnimation mTransitionAnimation;
    public final SurfaceSession mSurfaceSession = new SurfaceSession();
    public final ArrayMap<IBinder, ArrayList<Animator>> mAnimations = new ArrayMap<>();
    public final CounterRotatorHelper mRotator = new CounterRotatorHelper();
    public final Rect mInsets = new Rect(0, 0, 0, 0);
    public float mTransitionAnimationScaleSetting = 1.0f;
    public AnonymousClass1 mEnterpriseResourceUpdatedReceiver = new BroadcastReceiver() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("android.app.extra.RESOURCE_TYPE_DRAWABLE", false)) {
                DefaultTransitionHandler defaultTransitionHandler = DefaultTransitionHandler.this;
                Objects.requireNonNull(defaultTransitionHandler);
                defaultTransitionHandler.mEnterpriseThumbnailDrawable = defaultTransitionHandler.mDevicePolicyManager.getDrawable("WORK_PROFILE_ICON", "OUTLINE", "PROFILE_SWITCH_ANIMATION", new QSSecurityFooter$$ExternalSyntheticLambda0(defaultTransitionHandler, 1));
            }
        }
    };
    public final int mCurrentUserId = UserHandle.myUserId();

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    public static SurfaceControl createExtensionSurface(SurfaceControl surfaceControl, Rect rect, Rect rect2, int i, int i2, String str, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        SurfaceControl build = new SurfaceControl.Builder().setName(str).setParent(surfaceControl).setHidden(true).setCallsite("DefaultTransitionHandler#startAnimation").setOpaque(true).setBufferSize(rect2.width(), rect2.height()).build();
        SurfaceControl.ScreenshotHardwareBuffer captureLayers = SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect).setFrameScale(1.0f).setPixelFormat(1).setChildrenOnly(true).setAllowProtected(true).build());
        if (captureLayers == null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.getSingleInstance().log(BaseProtoLogImpl.LogLevel.ERROR, ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 457420030, 0, "Failed to capture edge of window.", (Object[]) null);
            }
            return null;
        }
        Bitmap asBitmap = captureLayers.asBitmap();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(asBitmap, tileMode, tileMode);
        Paint paint = new Paint();
        paint.setShader(bitmapShader);
        Surface surface = new Surface(build);
        Canvas lockHardwareCanvas = surface.lockHardwareCanvas();
        lockHardwareCanvas.drawRect(rect2, paint);
        surface.unlockCanvasAndPost(lockHardwareCanvas);
        surface.release();
        transaction.setLayer(build, Integer.MIN_VALUE);
        transaction.setPosition(build, i, i2);
        transaction.setVisibility(build, true);
        transaction2.remove(build);
        return build;
    }

    public static void edgeExtendWindow(TransitionInfo.Change change, Animation animation, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        Transformation transformation = new Transformation();
        animation.getTransformationAt(0.0f, transformation);
        Transformation transformation2 = new Transformation();
        animation.getTransformationAt(1.0f, transformation2);
        Insets min = Insets.min(transformation.getInsets(), transformation2.getInsets());
        int max = Math.max(change.getStartAbsBounds().height(), change.getEndAbsBounds().height());
        int max2 = Math.max(change.getStartAbsBounds().width(), change.getEndAbsBounds().width());
        if (min.left < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, 0, 1, max), new Rect(0, 0, -min.left, max), min.left, 0, "Left Edge Extension", transaction, transaction2);
        }
        if (min.top < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, 0, max2, 1), new Rect(0, 0, max2, -min.top), 0, min.top, "Top Edge Extension", transaction, transaction2);
        }
        if (min.right < 0) {
            createExtensionSurface(change.getLeash(), new Rect(max2 - 1, 0, max2, max), new Rect(0, 0, -min.right, max), max2, 0, "Right Edge Extension", transaction, transaction2);
        }
        if (min.bottom < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, max - 1, max2, max), new Rect(0, 0, max2, -min.bottom), min.left, max, "Bottom Edge Extension", transaction, transaction2);
        }
    }

    @VisibleForTesting
    public static boolean isRotationSeamless(TransitionInfo transitionInfo, DisplayController displayController) {
        boolean z;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 649960056, 0, "Display is changing, check if it should be seamless.", null);
        }
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getMode() == 6 && change.getEndRotation() != change.getStartRotation()) {
                int i = 3;
                if ((change.getFlags() & 32) == 0) {
                    int i2 = 2;
                    if ((change.getFlags() & 2) != 0) {
                        if (change.getRotationAnimation() != 3) {
                            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1015274864, 0, "  wallpaper is participating but isn't seamless.", null);
                            }
                            return false;
                        }
                    } else if (change.getTaskInfo() == null) {
                        continue;
                    } else if (change.getRotationAnimation() != 3) {
                        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1915000700, 0, "  task %s isn't requesting seamless, so not seamless.", String.valueOf(change.getTaskInfo().taskId));
                        }
                        return false;
                    } else if (!z4) {
                        DisplayLayout displayLayout = displayController.getDisplayLayout(change.getTaskInfo().displayId);
                        Objects.requireNonNull(displayLayout);
                        if (displayLayout.mWidth > displayLayout.mHeight) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (displayLayout.mRotation % 2 != 0) {
                            z = !z;
                        }
                        if (z) {
                            if (!displayLayout.mReverseDefaultRotation) {
                                i = 1;
                            }
                            i2 = i;
                        }
                        if (change.getStartRotation() == i2 || change.getEndRotation() == i2) {
                            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1167817788, 0, "  rotation involves upside-down portrait, so not seamless.", null);
                            }
                            return false;
                        } else if (displayLayout.mAllowSeamlessRotationDespiteNavBarMoving || (displayLayout.mNavigationBarCanMove && change.getStartAbsBounds().width() != change.getStartAbsBounds().height())) {
                            z2 = true;
                            z4 = true;
                        } else {
                            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1167654715, 0, "  nav bar changes sides, so not seamless.", null);
                            }
                            return false;
                        }
                    } else {
                        z2 = true;
                    }
                } else if ((change.getFlags() & 128) != 0) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 42311280, 0, "  display has system alert windows, so not seamless.", null);
                    }
                    return false;
                } else if (change.getRotationAnimation() == 3) {
                    z3 = true;
                } else {
                    z3 = false;
                }
            }
        }
        if (!(z2 || z3)) {
            return false;
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1215677233, 0, "  Rotation IS seamless.", null);
        }
        return true;
    }

    public final void attachThumbnailAnimation(ArrayList arrayList, final DefaultTransitionHandler$$ExternalSyntheticLambda1 defaultTransitionHandler$$ExternalSyntheticLambda1, TransitionInfo.Change change, TransitionInfo.AnimationOptions animationOptions, float f) {
        boolean z;
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final WindowThumbnail createAndAttach = WindowThumbnail.createAndAttach(this.mSurfaceSession, change.getLeash(), animationOptions.getThumbnail(), acquire);
        Rect endAbsBounds = change.getEndAbsBounds();
        int i = this.mContext.getResources().getConfiguration().orientation;
        TransitionAnimation transitionAnimation = this.mTransitionAnimation;
        Rect rect = this.mInsets;
        HardwareBuffer thumbnail = animationOptions.getThumbnail();
        Rect transitionBounds = animationOptions.getTransitionBounds();
        if (animationOptions.getType() == 3) {
            z = true;
        } else {
            z = false;
        }
        Animation createThumbnailAspectScaleAnimationLocked = transitionAnimation.createThumbnailAspectScaleAnimationLocked(endAbsBounds, rect, thumbnail, i, (Rect) null, transitionBounds, z);
        Runnable defaultTransitionHandler$$ExternalSyntheticLambda4 = new Runnable() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler defaultTransitionHandler = DefaultTransitionHandler.this;
                WindowThumbnail windowThumbnail = createAndAttach;
                SurfaceControl.Transaction transaction = acquire;
                Runnable runnable = defaultTransitionHandler$$ExternalSyntheticLambda1;
                Objects.requireNonNull(defaultTransitionHandler);
                Objects.requireNonNull(windowThumbnail);
                SurfaceControl surfaceControl = windowThumbnail.mSurfaceControl;
                if (surfaceControl != null) {
                    transaction.remove(surfaceControl);
                    transaction.apply();
                    windowThumbnail.mSurfaceControl.release();
                    windowThumbnail.mSurfaceControl = null;
                }
                defaultTransitionHandler.mTransactionPool.release(transaction);
                runnable.run();
            }
        };
        createThumbnailAspectScaleAnimationLocked.restrictDuration(3000L);
        createThumbnailAspectScaleAnimationLocked.scaleCurrentDuration(this.mTransitionAnimationScaleSetting);
        startSurfaceAnimation(arrayList, createThumbnailAspectScaleAnimationLocked, createAndAttach.mSurfaceControl, defaultTransitionHandler$$ExternalSyntheticLambda4, this.mTransactionPool, this.mMainExecutor, this.mAnimExecutor, null, f, change.getEndAbsBounds());
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0128, code lost:
        if (r0 != 3) goto L_0x0130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x012f, code lost:
        r0 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x02f6  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0385  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x057a  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x059b  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x05b6  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0647  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0667  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x066f  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0691  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x078e  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02a5  */
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean startAnimation(android.os.IBinder r34, android.window.TransitionInfo r35, android.view.SurfaceControl.Transaction r36, android.view.SurfaceControl.Transaction r37, com.android.wm.shell.transition.Transitions.TransitionFinishCallback r38) {
        /*
            Method dump skipped, instructions count: 2140
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.DefaultTransitionHandler.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.wm.shell.transition.DefaultTransitionHandler$1] */
    public DefaultTransitionHandler(DisplayController displayController, TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        this.mDisplayController = displayController;
        this.mTransactionPool = transactionPool;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mTransitionAnimation = new TransitionAnimation(context, false, "ShellTransitions");
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mDevicePolicyManager = devicePolicyManager;
        this.mEnterpriseThumbnailDrawable = devicePolicyManager.getDrawable("WORK_PROFILE_ICON", "OUTLINE", "PROFILE_SWITCH_ANIMATION", new QSSecurityFooter$$ExternalSyntheticLambda0(this, 1));
        context.registerReceiver(this.mEnterpriseResourceUpdatedReceiver, new IntentFilter("android.app.action.DEVICE_POLICY_RESOURCE_UPDATED"), null, handler);
        AttributeCache.init(context);
    }

    public static void applyTransformation(long j, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, Point point, float f, Rect rect) {
        animation.getTransformation(j, transformation);
        if (point != null) {
            transformation.getMatrix().postTranslate(point.x, point.y);
        }
        transaction.setMatrix(surfaceControl, transformation.getMatrix(), fArr);
        transaction.setAlpha(surfaceControl, transformation.getAlpha());
        Insets min = Insets.min(transformation.getInsets(), Insets.NONE);
        if (!min.equals(Insets.NONE) && rect != null && !rect.isEmpty()) {
            rect.inset(min);
            transaction.setCrop(surfaceControl, rect);
        }
        if (animation.hasRoundedCorners() && f > 0.0f && rect != null) {
            transaction.setCrop(surfaceControl, rect);
            transaction.setCornerRadius(surfaceControl, f);
        }
        transaction.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        transaction.apply();
    }

    /* JADX WARN: Type inference failed for: r13v1, types: [com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void startSurfaceAnimation(final java.util.ArrayList<android.animation.Animator> r16, final android.view.animation.Animation r17, final android.view.SurfaceControl r18, final java.lang.Runnable r19, final com.android.wm.shell.common.TransactionPool r20, final com.android.wm.shell.common.ShellExecutor r21, com.android.wm.shell.common.ShellExecutor r22, final android.graphics.Point r23, final float r24, final android.graphics.Rect r25) {
        /*
            android.view.SurfaceControl$Transaction r10 = r20.acquire()
            r0 = 2
            float[] r0 = new float[r0]
            r0 = {x0064: FILL_ARRAY_DATA  , data: [0, 1065353216} // fill-array
            android.animation.ValueAnimator r14 = android.animation.ValueAnimator.ofFloat(r0)
            android.view.animation.Transformation r11 = new android.view.animation.Transformation
            r11.<init>()
            r15 = 9
            float[] r12 = new float[r15]
            r0 = 1065353216(0x3f800000, float:1.0)
            r14.overrideDurationScale(r0)
            long r0 = r17.computeDurationHint()
            r14.setDuration(r0)
            com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0 r13 = new com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0
            r0 = r13
            r1 = r14
            r2 = r10
            r3 = r18
            r4 = r17
            r5 = r11
            r6 = r12
            r7 = r23
            r8 = r24
            r9 = r25
            r0.<init>()
            r14.addUpdateListener(r13)
            com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2 r13 = new com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2
            r0 = r13
            r10 = r20
            r11 = r21
            r12 = r16
            r15 = r13
            r13 = r19
            r0.<init>()
            com.android.wm.shell.transition.DefaultTransitionHandler$2 r0 = new com.android.wm.shell.transition.DefaultTransitionHandler$2
            r0.<init>()
            r14.addListener(r0)
            r0 = r16
            r0.add(r14)
            com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0 r0 = new com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0
            r1 = 9
            r0.<init>(r14, r1)
            r1 = r22
            r1.execute(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.DefaultTransitionHandler.startSurfaceAnimation(java.util.ArrayList, android.view.animation.Animation, android.view.SurfaceControl, java.lang.Runnable, com.android.wm.shell.common.TransactionPool, com.android.wm.shell.common.ShellExecutor, com.android.wm.shell.common.ShellExecutor, android.graphics.Point, float, android.graphics.Rect):void");
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final void setAnimScaleSetting(float f) {
        this.mTransitionAnimationScaleSetting = f;
    }
}
