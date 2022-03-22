package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.RectF;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.R$styleable;
/* loaded from: classes.dex */
public final class TouchResponse {
    public float mDragScale;
    public int mFlags;
    public float mLastTouchX;
    public float mLastTouchY;
    public int mLimitBoundsTo;
    public float mMaxAcceleration;
    public float mMaxVelocity;
    public final MotionLayout mMotionLayout;
    public boolean mMoveWhenScrollAtTop;
    public int mOnTouchUp;
    public int mTouchAnchorId;
    public int mTouchAnchorSide;
    public float mTouchAnchorX;
    public float mTouchAnchorY;
    public float mTouchDirectionX;
    public float mTouchDirectionY;
    public int mTouchRegionId;
    public int mTouchSide;
    public static final float[][] TOUCH_SIDES = {new float[]{0.5f, 0.0f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}, new float[]{0.5f, 1.0f}, new float[]{0.5f, 0.5f}, new float[]{0.0f, 0.5f}, new float[]{1.0f, 0.5f}};
    public static final float[][] TOUCH_DIRECTION = {new float[]{0.0f, -1.0f}, new float[]{0.0f, 1.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}, new float[]{-1.0f, 0.0f}, new float[]{1.0f, 0.0f}};
    public boolean mDragStarted = false;
    public float[] mAnchorDpDt = new float[2];

    public final void setRTL(boolean z) {
        if (z) {
            float[][] fArr = TOUCH_DIRECTION;
            fArr[4] = fArr[3];
            fArr[5] = fArr[2];
            float[][] fArr2 = TOUCH_SIDES;
            fArr2[5] = fArr2[2];
            fArr2[6] = fArr2[1];
        } else {
            float[][] fArr3 = TOUCH_DIRECTION;
            fArr3[4] = fArr3[2];
            fArr3[5] = fArr3[3];
            float[][] fArr4 = TOUCH_SIDES;
            fArr4[5] = fArr4[1];
            fArr4[6] = fArr4[2];
        }
        float[][] fArr5 = TOUCH_SIDES;
        int i = this.mTouchAnchorSide;
        this.mTouchAnchorX = fArr5[i][0];
        this.mTouchAnchorY = fArr5[i][1];
        float[][] fArr6 = TOUCH_DIRECTION;
        int i2 = this.mTouchSide;
        this.mTouchDirectionX = fArr6[i2][0];
        this.mTouchDirectionY = fArr6[i2][1];
    }

    public final RectF getTouchRegion(ViewGroup viewGroup, RectF rectF) {
        View findViewById;
        int i = this.mTouchRegionId;
        if (i == -1 || (findViewById = viewGroup.findViewById(i)) == null) {
            return null;
        }
        rectF.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
        return rectF;
    }

    public final String toString() {
        return this.mTouchDirectionX + " , " + this.mTouchDirectionY;
    }

    public TouchResponse(Context context, MotionLayout motionLayout, XmlResourceParser xmlResourceParser) {
        this.mTouchAnchorSide = 0;
        this.mTouchSide = 0;
        this.mOnTouchUp = 0;
        this.mTouchAnchorId = -1;
        this.mTouchRegionId = -1;
        this.mLimitBoundsTo = -1;
        this.mTouchAnchorY = 0.5f;
        this.mTouchAnchorX = 0.5f;
        this.mTouchDirectionX = 0.0f;
        this.mTouchDirectionY = 1.0f;
        this.mMaxVelocity = 4.0f;
        this.mMaxAcceleration = 1.2f;
        this.mMoveWhenScrollAtTop = true;
        this.mDragScale = 1.0f;
        this.mFlags = 0;
        this.mMotionLayout = motionLayout;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlResourceParser), R$styleable.OnSwipe);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == 8) {
                this.mTouchAnchorId = obtainStyledAttributes.getResourceId(index, this.mTouchAnchorId);
            } else if (index == 9) {
                int i2 = obtainStyledAttributes.getInt(index, this.mTouchAnchorSide);
                this.mTouchAnchorSide = i2;
                float[][] fArr = TOUCH_SIDES;
                this.mTouchAnchorX = fArr[i2][0];
                this.mTouchAnchorY = fArr[i2][1];
            } else if (index == 0) {
                int i3 = obtainStyledAttributes.getInt(index, this.mTouchSide);
                this.mTouchSide = i3;
                float[][] fArr2 = TOUCH_DIRECTION;
                this.mTouchDirectionX = fArr2[i3][0];
                this.mTouchDirectionY = fArr2[i3][1];
            } else if (index == 4) {
                this.mMaxVelocity = obtainStyledAttributes.getFloat(index, this.mMaxVelocity);
            } else if (index == 3) {
                this.mMaxAcceleration = obtainStyledAttributes.getFloat(index, this.mMaxAcceleration);
            } else if (index == 5) {
                this.mMoveWhenScrollAtTop = obtainStyledAttributes.getBoolean(index, this.mMoveWhenScrollAtTop);
            } else if (index == 1) {
                this.mDragScale = obtainStyledAttributes.getFloat(index, this.mDragScale);
            } else if (index == 10) {
                this.mTouchRegionId = obtainStyledAttributes.getResourceId(index, this.mTouchRegionId);
            } else if (index == 7) {
                this.mOnTouchUp = obtainStyledAttributes.getInt(index, this.mOnTouchUp);
            } else if (index == 6) {
                this.mFlags = obtainStyledAttributes.getInteger(index, 0);
            } else if (index == 2) {
                this.mLimitBoundsTo = obtainStyledAttributes.getResourceId(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }
}
