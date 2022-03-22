package androidx.dynamicanimation.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import androidx.dynamicanimation.animation.AnimationHandler;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AnimationHandler {
    public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
    public FrameCallbackScheduler mScheduler;
    public final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap<>();
    public final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
    public final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    public final ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 mRunnable = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 1);
    public long mCurrentFrameTime = 0;
    public boolean mListDirty = false;

    /* loaded from: classes.dex */
    public class AnimationCallbackDispatcher {
        public AnimationCallbackDispatcher() {
        }
    }

    /* loaded from: classes.dex */
    public interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    /* loaded from: classes.dex */
    public interface FrameCallbackScheduler {
        boolean isCurrentThread();

        void postFrameCallback(ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 imageWallpaper$GLEngine$$ExternalSyntheticLambda0);
    }

    /* loaded from: classes.dex */
    public static final class FrameCallbackScheduler16 implements FrameCallbackScheduler {
        public final Choreographer mChoreographer = Choreographer.getInstance();
        public final Looper mLooper = Looper.myLooper();

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public final void postFrameCallback(final ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 imageWallpaper$GLEngine$$ExternalSyntheticLambda0) {
            this.mChoreographer.postFrameCallback(new Choreographer.FrameCallback() { // from class: androidx.dynamicanimation.animation.AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0
                @Override // android.view.Choreographer.FrameCallback
                public final void doFrame(long j) {
                    imageWallpaper$GLEngine$$ExternalSyntheticLambda0.run();
                }
            });
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public final boolean isCurrentThread() {
            if (Thread.currentThread() == this.mLooper.getThread()) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class FrameCallbackScheduler14 implements FrameCallbackScheduler {
        public final Handler mHandler = new Handler(Looper.myLooper());
        public long mLastFrameTime;

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public final boolean isCurrentThread() {
            if (Thread.currentThread() == this.mHandler.getLooper().getThread()) {
                return true;
            }
            return false;
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public final void postFrameCallback(final ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 imageWallpaper$GLEngine$$ExternalSyntheticLambda0) {
            this.mHandler.postDelayed(new Runnable() { // from class: androidx.dynamicanimation.animation.AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimationHandler.FrameCallbackScheduler14 frameCallbackScheduler14 = AnimationHandler.FrameCallbackScheduler14.this;
                    Runnable runnable = imageWallpaper$GLEngine$$ExternalSyntheticLambda0;
                    Objects.requireNonNull(frameCallbackScheduler14);
                    frameCallbackScheduler14.mLastFrameTime = SystemClock.uptimeMillis();
                    runnable.run();
                }
            }, Math.max(10 - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0L));
        }
    }

    public AnimationHandler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    public void setScheduler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    public FrameCallbackScheduler getScheduler() {
        return this.mScheduler;
    }
}
