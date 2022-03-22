package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import androidx.constraintlayout.widget.R$styleable;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: classes.dex */
public final class KeyTrigger extends Key {
    public Method mFireCross;
    public float mFireLastPos;
    public Method mFireNegativeCross;
    public Method mFirePositiveCross;
    public String mCross = null;
    public int mTriggerReceiver = -1;
    public String mNegativeCross = null;
    public String mPositiveCross = null;
    public int mTriggerID = -1;
    public int mTriggerCollisionId = -1;
    public View mTriggerCollisionView = null;
    public float mTriggerSlack = 0.1f;
    public boolean mFireCrossReset = true;
    public boolean mFireNegativeReset = true;
    public boolean mFirePositiveReset = true;
    public float mFireThreshold = Float.NaN;
    public boolean mPostLayout = false;
    public RectF mCollisionRect = new RectF();
    public RectF mTargetRect = new RectF();

    /* loaded from: classes.dex */
    public static class Loader {
        public static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(0, 8);
            mAttrMap.append(4, 4);
            mAttrMap.append(5, 1);
            mAttrMap.append(6, 2);
            mAttrMap.append(1, 7);
            mAttrMap.append(7, 6);
            mAttrMap.append(9, 5);
            mAttrMap.append(3, 9);
            mAttrMap.append(2, 10);
            mAttrMap.append(8, 11);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void addValues(HashMap<String, SplineSet> hashMap) {
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void getAttributeNames(HashSet<String> hashSet) {
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void load(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyTrigger);
        SparseIntArray sparseIntArray = Loader.mAttrMap;
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            switch (Loader.mAttrMap.get(index)) {
                case 1:
                    this.mNegativeCross = obtainStyledAttributes.getString(index);
                    continue;
                case 2:
                    this.mPositiveCross = obtainStyledAttributes.getString(index);
                    continue;
                case 4:
                    this.mCross = obtainStyledAttributes.getString(index);
                    continue;
                case 5:
                    this.mTriggerSlack = obtainStyledAttributes.getFloat(index, this.mTriggerSlack);
                    continue;
                case FalsingManager.VERSION /* 6 */:
                    this.mTriggerID = obtainStyledAttributes.getResourceId(index, this.mTriggerID);
                    continue;
                case 7:
                    if (MotionLayout.IS_IN_EDIT_MODE) {
                        int resourceId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                        this.mTargetId = resourceId;
                        if (resourceId == -1) {
                            this.mTargetString = obtainStyledAttributes.getString(index);
                        } else {
                            continue;
                        }
                    } else if (obtainStyledAttributes.peekValue(index).type == 3) {
                        this.mTargetString = obtainStyledAttributes.getString(index);
                    } else {
                        this.mTargetId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                    }
                case 8:
                    int integer = obtainStyledAttributes.getInteger(index, this.mFramePosition);
                    this.mFramePosition = integer;
                    this.mFireThreshold = (integer + 0.5f) / 100.0f;
                    continue;
                case 9:
                    this.mTriggerCollisionId = obtainStyledAttributes.getResourceId(index, this.mTriggerCollisionId);
                    continue;
                case 10:
                    this.mPostLayout = obtainStyledAttributes.getBoolean(index, this.mPostLayout);
                    continue;
                case QSTileImpl.H.STALE /* 11 */:
                    this.mTriggerReceiver = obtainStyledAttributes.getResourceId(index, this.mTriggerReceiver);
                    break;
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("unused attribute 0x");
            m.append(Integer.toHexString(index));
            m.append("   ");
            m.append(Loader.mAttrMap.get(index));
            Log.e("KeyTrigger", m.toString());
        }
    }

    public KeyTrigger() {
        this.mCustomConstraints = new HashMap<>();
    }

    public static void setUpRect(RectF rectF, View view, boolean z) {
        rectF.top = view.getTop();
        rectF.bottom = view.getBottom();
        rectF.left = view.getLeft();
        rectF.right = view.getRight();
        if (z) {
            view.getMatrix().mapRect(rectF);
        }
    }
}
