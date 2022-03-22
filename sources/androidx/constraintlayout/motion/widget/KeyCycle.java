package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.widget.R$styleable;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyCycle extends Key {
    public int mCurveFit = 0;
    public int mWaveShape = -1;
    public float mWavePeriod = Float.NaN;
    public float mWaveOffset = 0.0f;
    public float mProgress = Float.NaN;
    public int mWaveVariesBy = -1;
    public float mAlpha = Float.NaN;
    public float mElevation = Float.NaN;
    public float mRotation = Float.NaN;
    public float mTransitionPathRotate = Float.NaN;
    public float mRotationX = Float.NaN;
    public float mRotationY = Float.NaN;
    public float mScaleX = Float.NaN;
    public float mScaleY = Float.NaN;
    public float mTranslationX = Float.NaN;
    public float mTranslationY = Float.NaN;
    public float mTranslationZ = Float.NaN;

    /* loaded from: classes.dex */
    public static class Loader {
        public static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(13, 1);
            mAttrMap.append(11, 2);
            mAttrMap.append(14, 3);
            mAttrMap.append(10, 4);
            mAttrMap.append(18, 5);
            mAttrMap.append(17, 6);
            mAttrMap.append(16, 7);
            mAttrMap.append(19, 8);
            mAttrMap.append(0, 9);
            mAttrMap.append(9, 10);
            mAttrMap.append(5, 11);
            mAttrMap.append(6, 12);
            mAttrMap.append(7, 13);
            mAttrMap.append(15, 14);
            mAttrMap.append(3, 15);
            mAttrMap.append(4, 16);
            mAttrMap.append(1, 17);
            mAttrMap.append(2, 18);
            mAttrMap.append(8, 19);
            mAttrMap.append(12, 20);
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void addValues(HashMap<String, SplineSet> hashMap) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("add ");
        m.append(hashMap.size());
        m.append(" values");
        String sb = m.toString();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        int min = Math.min(2, stackTrace.length - 1);
        String str = " ";
        for (int i = 1; i <= min; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(".(");
            m2.append(stackTrace[i].getFileName());
            m2.append(":");
            m2.append(stackTrace[i].getLineNumber());
            m2.append(") ");
            m2.append(stackTrace[i].getMethodName());
            String sb2 = m2.toString();
            str = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, " ");
            Log.v("KeyCycle", sb + str + sb2 + str);
        }
        for (String str2 : hashMap.keySet()) {
            SplineSet splineSet = hashMap.get(str2);
            Objects.requireNonNull(str2);
            str2.hashCode();
            char c = 65535;
            switch (str2.hashCode()) {
                case -1249320806:
                    if (str2.equals("rotationX")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1249320805:
                    if (str2.equals("rotationY")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1225497657:
                    if (str2.equals("translationX")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1225497656:
                    if (str2.equals("translationY")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1225497655:
                    if (str2.equals("translationZ")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1001078227:
                    if (str2.equals("progress")) {
                        c = 5;
                        break;
                    }
                    break;
                case -908189618:
                    if (str2.equals("scaleX")) {
                        c = 6;
                        break;
                    }
                    break;
                case -908189617:
                    if (str2.equals("scaleY")) {
                        c = 7;
                        break;
                    }
                    break;
                case -40300674:
                    if (str2.equals("rotation")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -4379043:
                    if (str2.equals("elevation")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 37232917:
                    if (str2.equals("transitionPathRotate")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 92909918:
                    if (str2.equals("alpha")) {
                        c = 11;
                        break;
                    }
                    break;
                case 156108012:
                    if (str2.equals("waveOffset")) {
                        c = '\f';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    splineSet.setPoint(this.mFramePosition, this.mRotationX);
                    break;
                case 1:
                    splineSet.setPoint(this.mFramePosition, this.mRotationY);
                    break;
                case 2:
                    splineSet.setPoint(this.mFramePosition, this.mTranslationX);
                    break;
                case 3:
                    splineSet.setPoint(this.mFramePosition, this.mTranslationY);
                    break;
                case 4:
                    splineSet.setPoint(this.mFramePosition, this.mTranslationZ);
                    break;
                case 5:
                    splineSet.setPoint(this.mFramePosition, this.mProgress);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    splineSet.setPoint(this.mFramePosition, this.mScaleX);
                    break;
                case 7:
                    splineSet.setPoint(this.mFramePosition, this.mScaleY);
                    break;
                case '\b':
                    splineSet.setPoint(this.mFramePosition, this.mRotation);
                    break;
                case '\t':
                    splineSet.setPoint(this.mFramePosition, this.mElevation);
                    break;
                case '\n':
                    splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate);
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    splineSet.setPoint(this.mFramePosition, this.mAlpha);
                    break;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    splineSet.setPoint(this.mFramePosition, this.mWaveOffset);
                    break;
                default:
                    Log.v("WARNING KeyCycle", "  UNKNOWN  " + str2);
                    break;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void getAttributeNames(HashSet<String> hashSet) {
        if (!Float.isNaN(this.mAlpha)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashSet.add("translationZ");
        }
        if (this.mCustomConstraints.size() > 0) {
            Iterator<String> it = this.mCustomConstraints.keySet().iterator();
            while (it.hasNext()) {
                hashSet.add("CUSTOM," + it.next());
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public final void load(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyCycle);
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
                    obtainStyledAttributes.getString(index);
                    break;
                case 4:
                    this.mCurveFit = obtainStyledAttributes.getInteger(index, this.mCurveFit);
                    break;
                case 5:
                    this.mWaveShape = obtainStyledAttributes.getInt(index, this.mWaveShape);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    this.mWavePeriod = obtainStyledAttributes.getFloat(index, this.mWavePeriod);
                    break;
                case 7:
                    if (obtainStyledAttributes.peekValue(index).type == 5) {
                        this.mWaveOffset = obtainStyledAttributes.getDimension(index, this.mWaveOffset);
                        break;
                    } else {
                        this.mWaveOffset = obtainStyledAttributes.getFloat(index, this.mWaveOffset);
                        break;
                    }
                case 8:
                    this.mWaveVariesBy = obtainStyledAttributes.getInt(index, this.mWaveVariesBy);
                    break;
                case 9:
                    this.mAlpha = obtainStyledAttributes.getFloat(index, this.mAlpha);
                    break;
                case 10:
                    this.mElevation = obtainStyledAttributes.getDimension(index, this.mElevation);
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    this.mRotation = obtainStyledAttributes.getFloat(index, this.mRotation);
                    break;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    this.mRotationX = obtainStyledAttributes.getFloat(index, this.mRotationX);
                    break;
                case QS.VERSION /* 13 */:
                    this.mRotationY = obtainStyledAttributes.getFloat(index, this.mRotationY);
                    break;
                case 14:
                    this.mTransitionPathRotate = obtainStyledAttributes.getFloat(index, this.mTransitionPathRotate);
                    break;
                case 15:
                    this.mScaleX = obtainStyledAttributes.getFloat(index, this.mScaleX);
                    break;
                case 16:
                    this.mScaleY = obtainStyledAttributes.getFloat(index, this.mScaleY);
                    break;
                case 17:
                    this.mTranslationX = obtainStyledAttributes.getDimension(index, this.mTranslationX);
                    break;
                case 18:
                    this.mTranslationY = obtainStyledAttributes.getDimension(index, this.mTranslationY);
                    break;
                case 19:
                    this.mTranslationZ = obtainStyledAttributes.getDimension(index, this.mTranslationZ);
                    break;
                case 20:
                    this.mProgress = obtainStyledAttributes.getFloat(index, this.mProgress);
                    break;
                default:
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("unused attribute 0x");
                    m.append(Integer.toHexString(index));
                    m.append("   ");
                    m.append(Loader.mAttrMap.get(index));
                    Log.e("KeyCycle", m.toString());
                    break;
            }
        }
    }

    public KeyCycle() {
        this.mCustomConstraints = new HashMap<>();
    }
}
