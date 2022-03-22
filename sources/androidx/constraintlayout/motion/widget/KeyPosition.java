package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.widget.R$styleable;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class KeyPosition extends KeyPositionBase {
    public String mTransitionEasing = null;
    public int mPathMotionArc = -1;
    public int mDrawPath = 0;
    public float mPercentWidth = Float.NaN;
    public float mPercentHeight = Float.NaN;
    public float mPercentX = Float.NaN;
    public float mPercentY = Float.NaN;
    public int mPositionType = 0;

    /* loaded from: classes.dex */
    public static class Loader {
        public static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(4, 1);
            mAttrMap.append(2, 2);
            mAttrMap.append(11, 3);
            mAttrMap.append(0, 4);
            mAttrMap.append(1, 5);
            mAttrMap.append(8, 6);
            mAttrMap.append(9, 7);
            mAttrMap.append(3, 9);
            mAttrMap.append(10, 8);
            mAttrMap.append(7, 11);
            mAttrMap.append(6, 12);
            mAttrMap.append(5, 10);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void addValues(HashMap<String, SplineSet> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void load(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyPosition);
        SparseIntArray sparseIntArray = Loader.mAttrMap;
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            switch (Loader.mAttrMap.get(index)) {
                case 1:
                    if (!MotionLayout.IS_IN_EDIT_MODE) {
                        if (obtainStyledAttributes.peekValue(index).type == 3) {
                            this.mTargetString = obtainStyledAttributes.getString(index);
                            break;
                        } else {
                            this.mTargetId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                            break;
                        }
                    } else {
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                        this.mTargetId = resourceId;
                        if (resourceId == -1) {
                            this.mTargetString = obtainStyledAttributes.getString(index);
                            break;
                        } else {
                            break;
                        }
                    }
                case 2:
                    this.mFramePosition = obtainStyledAttributes.getInt(index, this.mFramePosition);
                    break;
                case 3:
                    if (obtainStyledAttributes.peekValue(index).type == 3) {
                        this.mTransitionEasing = obtainStyledAttributes.getString(index);
                        break;
                    } else {
                        this.mTransitionEasing = Easing.NAMED_EASING[obtainStyledAttributes.getInteger(index, 0)];
                        break;
                    }
                case 4:
                    this.mCurveFit = obtainStyledAttributes.getInteger(index, this.mCurveFit);
                    break;
                case 5:
                    this.mDrawPath = obtainStyledAttributes.getInt(index, this.mDrawPath);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    this.mPercentX = obtainStyledAttributes.getFloat(index, this.mPercentX);
                    break;
                case 7:
                    this.mPercentY = obtainStyledAttributes.getFloat(index, this.mPercentY);
                    break;
                case 8:
                    float f = obtainStyledAttributes.getFloat(index, this.mPercentHeight);
                    this.mPercentWidth = f;
                    this.mPercentHeight = f;
                    break;
                case 9:
                    this.mPositionType = obtainStyledAttributes.getInt(index, this.mPositionType);
                    break;
                case 10:
                    this.mPathMotionArc = obtainStyledAttributes.getInt(index, this.mPathMotionArc);
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    this.mPercentWidth = obtainStyledAttributes.getFloat(index, this.mPercentWidth);
                    break;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    this.mPercentHeight = obtainStyledAttributes.getFloat(index, this.mPercentHeight);
                    break;
                default:
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("unused attribute 0x");
                    m.append(Integer.toHexString(index));
                    m.append("   ");
                    m.append(Loader.mAttrMap.get(index));
                    Log.e("KeyPosition", m.toString());
                    break;
            }
        }
        if (this.mFramePosition == -1) {
            Log.e("KeyPosition", "no frame position");
        }
    }
}
