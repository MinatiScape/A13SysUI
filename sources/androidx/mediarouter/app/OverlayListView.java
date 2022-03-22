package androidx.mediarouter.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ListView;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.mediarouter.app.MediaRouteControllerDialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
final class OverlayListView extends ListView {
    public final ArrayList mOverlayObjects = new ArrayList();

    public OverlayListView(Context context) {
        super(context);
    }

    /* loaded from: classes.dex */
    public static class OverlayObject {
        public BitmapDrawable mBitmap;
        public Rect mCurrentBounds;
        public int mDeltaY;
        public long mDuration;
        public Interpolator mInterpolator;
        public boolean mIsAnimationEnded;
        public boolean mIsAnimationStarted;
        public OnAnimationEndListener mListener;
        public Rect mStartRect;
        public long mStartTime;
        public float mCurrentAlpha = 1.0f;
        public float mStartAlpha = 1.0f;
        public float mEndAlpha = 1.0f;

        /* loaded from: classes.dex */
        public interface OnAnimationEndListener {
        }

        public OverlayObject(BitmapDrawable bitmapDrawable, Rect rect) {
            this.mBitmap = bitmapDrawable;
            this.mStartRect = rect;
            this.mCurrentBounds = new Rect(rect);
            BitmapDrawable bitmapDrawable2 = this.mBitmap;
            if (bitmapDrawable2 != null) {
                bitmapDrawable2.setAlpha((int) (this.mCurrentAlpha * 255.0f));
                this.mBitmap.setBounds(this.mCurrentBounds);
            }
        }
    }

    public OverlayListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        boolean z;
        float f;
        super.onDraw(canvas);
        if (this.mOverlayObjects.size() > 0) {
            Iterator it = this.mOverlayObjects.iterator();
            while (it.hasNext()) {
                OverlayObject overlayObject = (OverlayObject) it.next();
                Objects.requireNonNull(overlayObject);
                BitmapDrawable bitmapDrawable = overlayObject.mBitmap;
                if (bitmapDrawable != null) {
                    bitmapDrawable.draw(canvas);
                }
                long drawingTime = getDrawingTime();
                if (overlayObject.mIsAnimationEnded) {
                    z = false;
                } else {
                    float f2 = 0.0f;
                    float max = Math.max(0.0f, Math.min(1.0f, ((float) (drawingTime - overlayObject.mStartTime)) / ((float) overlayObject.mDuration)));
                    if (overlayObject.mIsAnimationStarted) {
                        f2 = max;
                    }
                    Interpolator interpolator = overlayObject.mInterpolator;
                    if (interpolator == null) {
                        f = f2;
                    } else {
                        f = interpolator.getInterpolation(f2);
                    }
                    int i = (int) (overlayObject.mDeltaY * f);
                    Rect rect = overlayObject.mCurrentBounds;
                    Rect rect2 = overlayObject.mStartRect;
                    rect.top = rect2.top + i;
                    rect.bottom = rect2.bottom + i;
                    float f3 = overlayObject.mStartAlpha;
                    float m = MotionController$$ExternalSyntheticOutline0.m(overlayObject.mEndAlpha, f3, f, f3);
                    overlayObject.mCurrentAlpha = m;
                    BitmapDrawable bitmapDrawable2 = overlayObject.mBitmap;
                    if (bitmapDrawable2 != null) {
                        bitmapDrawable2.setAlpha((int) (m * 255.0f));
                        overlayObject.mBitmap.setBounds(overlayObject.mCurrentBounds);
                    }
                    if (overlayObject.mIsAnimationStarted && f2 >= 1.0f) {
                        overlayObject.mIsAnimationEnded = true;
                        OverlayObject.OnAnimationEndListener onAnimationEndListener = overlayObject.mListener;
                        if (onAnimationEndListener != null) {
                            MediaRouteControllerDialog.AnonymousClass10 r2 = (MediaRouteControllerDialog.AnonymousClass10) onAnimationEndListener;
                            MediaRouteControllerDialog.this.mGroupMemberRoutesAnimatingWithBitmap.remove(r2.val$route);
                            MediaRouteControllerDialog.this.mVolumeGroupAdapter.notifyDataSetChanged();
                        }
                    }
                    z = !overlayObject.mIsAnimationEnded;
                }
                if (!z) {
                    it.remove();
                }
            }
        }
    }

    public OverlayListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
