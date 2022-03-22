package androidx.constraintlayout.motion.widget;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.utils.ArcCurveFit;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MotionController {
    public ArcCurveFit mArcSpline;
    public int[] mAttributeInterpCount;
    public String[] mAttributeNames;
    public HashMap<String, SplineSet> mAttributesMap;
    public HashMap<String, KeyCycleOscillator> mCycleMap;
    public int mId;
    public double[] mInterpolateData;
    public int[] mInterpolateVariables;
    public double[] mInterpolateVelocity;
    public KeyTrigger[] mKeyTriggers;
    public CurveFit[] mSpline;
    public HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
    public View mView;
    public int mCurveFitType = -1;
    public MotionPaths mStartMotionPath = new MotionPaths();
    public MotionPaths mEndMotionPath = new MotionPaths();
    public MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
    public MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
    public float mMotionStagger = Float.NaN;
    public float mStaggerOffset = 0.0f;
    public float mStaggerScale = 1.0f;
    public float[] mValuesBuff = new float[4];
    public ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
    public float[] mVelocity = new float[1];
    public ArrayList<Key> mKeyList = new ArrayList<>();
    public int mPathMotionArc = -1;

    public final float getAdjustedPosition(float f, float[] fArr) {
        float f2 = 0.0f;
        float f3 = 1.0f;
        if (fArr != null) {
            fArr[0] = 1.0f;
        } else {
            float f4 = this.mStaggerScale;
            if (f4 != 1.0d) {
                float f5 = this.mStaggerOffset;
                if (f < f5) {
                    f = 0.0f;
                }
                if (f > f5 && f < 1.0d) {
                    f = (f - f5) * f4;
                }
            }
        }
        Easing easing = this.mStartMotionPath.mKeyFrameEasing;
        float f6 = Float.NaN;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            MotionPaths next = it.next();
            Easing easing2 = next.mKeyFrameEasing;
            if (easing2 != null) {
                float f7 = next.time;
                if (f7 < f) {
                    easing = easing2;
                    f2 = f7;
                } else if (Float.isNaN(f6)) {
                    f6 = next.time;
                }
            }
        }
        if (easing == null) {
            return f;
        }
        if (!Float.isNaN(f6)) {
            f3 = f6;
        }
        float f8 = f3 - f2;
        double d = (f - f2) / f8;
        float f9 = f2 + (((float) easing.get(d)) * f8);
        if (fArr != null) {
            fArr[0] = (float) easing.getDiff(d);
        }
        return f9;
    }

    public final void getDpDt(float f, float f2, float f3, float[] fArr) {
        double[] dArr;
        float adjustedPosition = getAdjustedPosition(f, this.mVelocity);
        CurveFit[] curveFitArr = this.mSpline;
        int i = 0;
        if (curveFitArr != null) {
            double d = adjustedPosition;
            curveFitArr[0].getSlope(d, this.mInterpolateVelocity);
            this.mSpline[0].getPos(d, this.mInterpolateData);
            float f4 = this.mVelocity[0];
            while (true) {
                dArr = this.mInterpolateVelocity;
                if (i >= dArr.length) {
                    break;
                }
                dArr[i] = dArr[i] * f4;
                i++;
            }
            ArcCurveFit arcCurveFit = this.mArcSpline;
            if (arcCurveFit != null) {
                double[] dArr2 = this.mInterpolateData;
                if (dArr2.length > 0) {
                    arcCurveFit.getPos(d, dArr2);
                    this.mArcSpline.getSlope(d, this.mInterpolateVelocity);
                    MotionPaths motionPaths = this.mStartMotionPath;
                    int[] iArr = this.mInterpolateVariables;
                    double[] dArr3 = this.mInterpolateVelocity;
                    double[] dArr4 = this.mInterpolateData;
                    Objects.requireNonNull(motionPaths);
                    MotionPaths.setDpDt(f2, f3, fArr, iArr, dArr3, dArr4);
                    return;
                }
                return;
            }
            MotionPaths motionPaths2 = this.mStartMotionPath;
            int[] iArr2 = this.mInterpolateVariables;
            double[] dArr5 = this.mInterpolateData;
            Objects.requireNonNull(motionPaths2);
            MotionPaths.setDpDt(f2, f3, fArr, iArr2, dArr, dArr5);
            return;
        }
        MotionPaths motionPaths3 = this.mEndMotionPath;
        float f5 = motionPaths3.x;
        MotionPaths motionPaths4 = this.mStartMotionPath;
        float f6 = f5 - motionPaths4.x;
        float f7 = motionPaths3.y - motionPaths4.y;
        fArr[0] = (((motionPaths3.width - motionPaths4.width) + f6) * f2) + ((1.0f - f2) * f6);
        fArr[1] = (((motionPaths3.height - motionPaths4.height) + f7) * f3) + ((1.0f - f3) * f7);
    }

    /* JADX WARN: Removed duplicated region for block: B:149:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x031d  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x035f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0370  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0389  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x03a3  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0493  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0428 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x049b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0504 A[ADDED_TO_REGION, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean interpolate(android.view.View r24, float r25, long r26, androidx.constraintlayout.motion.widget.KeyCache r28) {
        /*
            Method dump skipped, instructions count: 1433
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionController.interpolate(android.view.View, float, long, androidx.constraintlayout.motion.widget.KeyCache):boolean");
    }

    public final void readView(MotionPaths motionPaths) {
        motionPaths.setBounds((int) this.mView.getX(), (int) this.mView.getY(), this.mView.getWidth(), this.mView.getHeight());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void setup(int i, int i2, long j) {
        String str;
        String str2;
        String str3;
        String str4;
        ArrayList arrayList;
        HashSet<String> hashSet;
        String str5;
        HashSet<String> hashSet2;
        String str6;
        String str7;
        Object obj;
        Object obj2;
        Object obj3;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        Object obj4;
        Object obj5;
        MotionController motionController;
        Object obj6;
        String str14;
        boolean z;
        Iterator<Key> it;
        String str15;
        String str16;
        Iterator<String> it2;
        char c;
        Object obj7;
        Object obj8;
        Object obj9;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        float f;
        char c2;
        char c3;
        char c4;
        char c5;
        ConstraintAttribute constraintAttribute;
        Iterator<String> it3;
        String str26;
        String str27;
        String str28;
        String str29;
        String str30;
        KeyCycleOscillator.CustomSet customSet;
        String str31;
        String str32;
        String str33;
        Object obj10;
        String str34;
        String str35;
        String str36;
        String str37;
        String str38;
        boolean z2;
        String str39;
        String str40;
        double d;
        int i3;
        String str41;
        char c6;
        float f2;
        char c7;
        Object obj11;
        KeyCycleOscillator rotationXset;
        String str42;
        char c8;
        String str43;
        char c9;
        char c10;
        String str44;
        int i4;
        double[] dArr;
        double[][] dArr2;
        Object obj12;
        HashSet<String> hashSet3;
        int i5;
        char c11;
        Object obj13;
        Object obj14;
        String str45;
        Object obj15;
        KeyTimeCycle keyTimeCycle;
        char c12;
        char c13;
        char c14;
        Iterator<String> it4;
        HashMap<String, Integer> hashMap;
        String str46;
        Object obj16;
        String str47;
        TimeCycleSplineSet.CustomSet customSet2;
        Object obj17;
        Object obj18;
        char c15;
        Object obj19;
        char c16;
        ConstraintAttribute constraintAttribute2;
        int i6;
        Iterator<String> it5;
        HashSet<String> hashSet4;
        String str48;
        HashSet<String> hashSet5;
        String str49;
        String str50;
        Object obj20;
        Object obj21;
        SplineSet.CustomSet customSet3;
        String str51;
        Object obj22;
        String str52;
        String str53;
        String str54;
        char c17;
        Object obj23;
        SplineSet splineSet;
        char c18;
        ConstraintAttribute constraintAttribute3;
        String str55;
        String str56;
        String str57;
        int binarySearch;
        MotionController motionController2 = this;
        new HashSet();
        HashSet<String> hashSet6 = new HashSet<>();
        HashSet<String> hashSet7 = new HashSet<>();
        HashSet<String> hashSet8 = new HashSet<>();
        HashMap<String, Integer> hashMap2 = new HashMap<>();
        int i7 = motionController2.mPathMotionArc;
        if (i7 != -1) {
            motionController2.mStartMotionPath.mPathMotionArc = i7;
        }
        MotionConstrainedPoint motionConstrainedPoint = motionController2.mStartPoint;
        MotionConstrainedPoint motionConstrainedPoint2 = motionController2.mEndPoint;
        Objects.requireNonNull(motionConstrainedPoint);
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.alpha, motionConstrainedPoint2.alpha)) {
            hashSet7.add("alpha");
        }
        String str58 = "elevation";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.elevation, motionConstrainedPoint2.elevation)) {
            hashSet7.add(str58);
        }
        int i8 = motionConstrainedPoint.visibility;
        int i9 = motionConstrainedPoint2.visibility;
        if (i8 != i9 && motionConstrainedPoint.mVisibilityMode == 0 && (i8 == 0 || i9 == 0)) {
            hashSet7.add("alpha");
        }
        String str59 = "rotation";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.rotation, motionConstrainedPoint2.rotation)) {
            hashSet7.add(str59);
        }
        if (!Float.isNaN(motionConstrainedPoint.mPathRotate) || !Float.isNaN(motionConstrainedPoint2.mPathRotate)) {
            hashSet7.add("transitionPathRotate");
        }
        String str60 = "progress";
        if (!Float.isNaN(motionConstrainedPoint.mProgress) || !Float.isNaN(motionConstrainedPoint2.mProgress)) {
            hashSet7.add(str60);
        }
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.rotationX, motionConstrainedPoint2.rotationX)) {
            hashSet7.add("rotationX");
        }
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.rotationY, motionConstrainedPoint2.rotationY)) {
            hashSet7.add("rotationY");
        }
        String str61 = "scaleX";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.scaleX, motionConstrainedPoint2.scaleX)) {
            hashSet7.add(str61);
        }
        Object obj24 = "rotationX";
        String str62 = "scaleY";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.scaleY, motionConstrainedPoint2.scaleY)) {
            hashSet7.add(str62);
        }
        Object obj25 = "rotationY";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.translationX, motionConstrainedPoint2.translationX)) {
            hashSet7.add("translationX");
        }
        Object obj26 = "translationX";
        String str63 = "translationY";
        if (MotionConstrainedPoint.diff(motionConstrainedPoint.translationY, motionConstrainedPoint2.translationY)) {
            hashSet7.add(str63);
        }
        boolean diff = MotionConstrainedPoint.diff(motionConstrainedPoint.translationZ, motionConstrainedPoint2.translationZ);
        String str64 = "translationZ";
        if (diff) {
            hashSet7.add(str64);
        }
        ArrayList<Key> arrayList2 = motionController2.mKeyList;
        if (arrayList2 != null) {
            Iterator<Key> it6 = arrayList2.iterator();
            arrayList = null;
            while (it6.hasNext()) {
                Key next = it6.next();
                if (next instanceof KeyPosition) {
                    KeyPosition keyPosition = (KeyPosition) next;
                    str56 = str64;
                    str55 = str60;
                    MotionPaths motionPaths = new MotionPaths(i, i2, keyPosition, motionController2.mStartMotionPath, motionController2.mEndMotionPath);
                    if (Collections.binarySearch(motionController2.mMotionPaths, motionPaths) == 0) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" KeyPath positon \"");
                        str57 = str61;
                        m.append(motionPaths.position);
                        m.append("\" outside of range");
                        Log.e("MotionController", m.toString());
                    } else {
                        str57 = str61;
                    }
                    motionController2.mMotionPaths.add((-binarySearch) - 1, motionPaths);
                    int i10 = keyPosition.mCurveFit;
                    if (i10 != -1) {
                        motionController2.mCurveFitType = i10;
                    }
                } else {
                    str56 = str64;
                    str57 = str61;
                    str55 = str60;
                    if (next instanceof KeyCycle) {
                        next.getAttributeNames(hashSet8);
                    } else if (next instanceof KeyTimeCycle) {
                        next.getAttributeNames(hashSet6);
                    } else if (next instanceof KeyTrigger) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add((KeyTrigger) next);
                    } else {
                        next.setInterpolation(hashMap2);
                        next.getAttributeNames(hashSet7);
                    }
                }
                str63 = str63;
                it6 = it6;
                str61 = str57;
                str64 = str56;
                str60 = str55;
            }
            str2 = str64;
            str3 = str61;
            str = str60;
            str4 = str63;
        } else {
            str2 = str64;
            str3 = str61;
            str = str60;
            str4 = str63;
            arrayList = null;
        }
        if (arrayList != null) {
            motionController2.mKeyTriggers = (KeyTrigger[]) arrayList.toArray(new KeyTrigger[0]);
        }
        String str65 = "waveVariesBy";
        String str66 = ",";
        String str67 = "waveOffset";
        String str68 = "CUSTOM,";
        if (!hashSet7.isEmpty()) {
            motionController2.mAttributesMap = new HashMap<>();
            Iterator<String> it7 = hashSet7.iterator();
            while (it7.hasNext()) {
                String next2 = it7.next();
                if (next2.startsWith(str68)) {
                    it5 = it7;
                    SparseArray sparseArray = new SparseArray();
                    hashSet4 = hashSet8;
                    String str69 = next2.split(str66)[1];
                    hashSet5 = hashSet7;
                    for (Iterator<Key> it8 = motionController2.mKeyList.iterator(); it8.hasNext(); it8 = it8) {
                        Key next3 = it8.next();
                        HashMap<String, ConstraintAttribute> hashMap3 = next3.mCustomConstraints;
                        if (!(hashMap3 == null || (constraintAttribute3 = hashMap3.get(str69)) == null)) {
                            sparseArray.append(next3.mFramePosition, constraintAttribute3);
                        }
                        str66 = str66;
                    }
                    str48 = str66;
                    SplineSet.CustomSet customSet4 = new SplineSet.CustomSet(next2, sparseArray);
                    str51 = str4;
                    str54 = str3;
                    str52 = str2;
                    str49 = str65;
                    str50 = str67;
                    obj22 = obj26;
                    customSet3 = customSet4;
                    obj20 = obj25;
                    str53 = str;
                    obj21 = obj24;
                } else {
                    it5 = it7;
                    hashSet5 = hashSet7;
                    hashSet4 = hashSet8;
                    str48 = str66;
                    switch (next2.hashCode()) {
                        case -1249320806:
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            if (next2.equals(obj23)) {
                                c17 = 0;
                                break;
                            }
                            c17 = 65535;
                            break;
                        case -1249320805:
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj22 = obj26;
                            if (!next2.equals(obj25)) {
                                obj20 = obj25;
                                obj23 = obj24;
                                c17 = 65535;
                                break;
                            } else {
                                obj20 = obj25;
                                obj23 = obj24;
                                c17 = 1;
                                break;
                            }
                        case -1225497657:
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            obj22 = obj26;
                            if (next2.equals(obj22)) {
                                str50 = str67;
                                obj23 = obj24;
                                c17 = 2;
                                obj20 = obj25;
                                break;
                            }
                            str50 = str67;
                            obj23 = obj24;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case -1225497656:
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            if (next2.equals(str51)) {
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = 3;
                                break;
                            }
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case -1225497655:
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            if (!next2.equals(str52)) {
                                str49 = str65;
                                obj22 = obj26;
                                str51 = str4;
                                str50 = str67;
                                obj23 = obj24;
                                obj20 = obj25;
                                c17 = 65535;
                                break;
                            } else {
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                c17 = 4;
                                str51 = str4;
                                str50 = str67;
                                obj23 = obj24;
                                break;
                            }
                        case -1001078227:
                            str54 = str3;
                            str53 = str;
                            str49 = str65;
                            obj22 = obj26;
                            str51 = str4;
                            if (!next2.equals(str53)) {
                                str52 = str2;
                                str50 = str67;
                                obj23 = obj24;
                                obj20 = obj25;
                                c17 = 65535;
                                break;
                            } else {
                                str52 = str2;
                                str50 = str67;
                                obj23 = obj24;
                                obj20 = obj25;
                                c17 = 5;
                                break;
                            }
                        case -908189618:
                            str54 = str3;
                            if (!next2.equals(str54)) {
                                str49 = str65;
                                obj22 = obj26;
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                obj20 = obj25;
                                c17 = 65535;
                                break;
                            } else {
                                str49 = str65;
                                obj22 = obj26;
                                str51 = str4;
                                str52 = str2;
                                str50 = str67;
                                obj23 = obj24;
                                obj20 = obj25;
                                c17 = 6;
                                str53 = str;
                                break;
                            }
                        case -908189617:
                            if (next2.equals(str62)) {
                                c18 = 7;
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case -797520672:
                            if (next2.equals(str65)) {
                                c18 = '\b';
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case -40300674:
                            if (next2.equals(str59)) {
                                c18 = '\t';
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case -4379043:
                            if (next2.equals(str58)) {
                                c18 = '\n';
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case 37232917:
                            if (next2.equals("transitionPathRotate")) {
                                c18 = 11;
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case 92909918:
                            if (next2.equals("alpha")) {
                                c18 = '\f';
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        case 156108012:
                            if (next2.equals(str67)) {
                                c18 = '\r';
                                str51 = str4;
                                str52 = str2;
                                str53 = str;
                                str50 = str67;
                                obj23 = obj24;
                                c17 = c18;
                                str54 = str3;
                                str49 = str65;
                                obj22 = obj26;
                                obj20 = obj25;
                                break;
                            }
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                        default:
                            str51 = str4;
                            str54 = str3;
                            str52 = str2;
                            str53 = str;
                            str49 = str65;
                            str50 = str67;
                            obj23 = obj24;
                            obj22 = obj26;
                            obj20 = obj25;
                            c17 = 65535;
                            break;
                    }
                    switch (c17) {
                        case 0:
                            splineSet = new SplineSet.RotationXset();
                            break;
                        case 1:
                            splineSet = new SplineSet.RotationYset();
                            break;
                        case 2:
                            splineSet = new SplineSet.TranslationXset();
                            break;
                        case 3:
                            splineSet = new SplineSet.TranslationYset();
                            break;
                        case 4:
                            splineSet = new SplineSet.TranslationZset();
                            break;
                        case 5:
                            splineSet = new SplineSet.ProgressSet();
                            break;
                        case FalsingManager.VERSION /* 6 */:
                            splineSet = new SplineSet.ScaleXset();
                            break;
                        case 7:
                            splineSet = new SplineSet.ScaleYset();
                            break;
                        case '\b':
                            splineSet = new SplineSet.AlphaSet();
                            break;
                        case '\t':
                            splineSet = new SplineSet.RotationSet();
                            break;
                        case '\n':
                            splineSet = new SplineSet.ElevationSet();
                            break;
                        case QSTileImpl.H.STALE /* 11 */:
                            splineSet = new SplineSet.PathRotate();
                            break;
                        case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                            splineSet = new SplineSet.AlphaSet();
                            break;
                        case QS.VERSION /* 13 */:
                            splineSet = new SplineSet.AlphaSet();
                            break;
                        default:
                            splineSet = null;
                            break;
                    }
                    obj21 = obj23;
                    customSet3 = splineSet;
                }
                if (customSet3 == null) {
                    str = str53;
                    str2 = str52;
                    obj24 = obj21;
                    obj25 = obj20;
                    str67 = str50;
                    hashSet7 = hashSet5;
                    hashSet8 = hashSet4;
                    obj26 = obj22;
                    str4 = str51;
                    str65 = str49;
                    str66 = str48;
                    str3 = str54;
                    it7 = it5;
                } else {
                    customSet3.mType = next2;
                    motionController2.mAttributesMap.put(next2, customSet3);
                    str = str53;
                    str2 = str52;
                    str67 = str50;
                    str65 = str49;
                    hashSet7 = hashSet5;
                    hashSet8 = hashSet4;
                    str3 = str54;
                    str4 = str51;
                    str66 = str48;
                    it7 = it5;
                    obj26 = obj22;
                    obj24 = obj21;
                    obj25 = obj20;
                }
            }
            hashSet2 = hashSet7;
            hashSet = hashSet8;
            str5 = str66;
            str8 = str4;
            str11 = str3;
            str9 = str2;
            str10 = str;
            str6 = str65;
            str7 = str67;
            obj2 = obj24;
            obj3 = obj26;
            obj = obj25;
            ArrayList<Key> arrayList3 = motionController2.mKeyList;
            if (arrayList3 != null) {
                Iterator<Key> it9 = arrayList3.iterator();
                while (it9.hasNext()) {
                    Key next4 = it9.next();
                    if (next4 instanceof KeyAttributes) {
                        next4.addValues(motionController2.mAttributesMap);
                    }
                }
            }
            motionController2.mStartPoint.addValues(motionController2.mAttributesMap, 0);
            motionController2.mEndPoint.addValues(motionController2.mAttributesMap, 100);
            for (Iterator<String> it10 = motionController2.mAttributesMap.keySet().iterator(); it10.hasNext(); it10 = it10) {
                String next5 = it10.next();
                if (hashMap2.containsKey(next5)) {
                    i6 = hashMap2.get(next5).intValue();
                } else {
                    i6 = 0;
                }
                motionController2.mAttributesMap.get(next5).setup(i6);
            }
        } else {
            hashSet2 = hashSet7;
            hashSet = hashSet8;
            str5 = str66;
            str8 = str4;
            str11 = str3;
            str9 = str2;
            str10 = str;
            str6 = str65;
            str7 = str67;
            obj2 = obj24;
            obj3 = obj26;
            obj = obj25;
        }
        String str70 = "CUSTOM";
        if (!hashSet6.isEmpty()) {
            if (motionController2.mTimeCycleAttributesMap == null) {
                motionController2.mTimeCycleAttributesMap = new HashMap<>();
            }
            Iterator<String> it11 = hashSet6.iterator();
            while (it11.hasNext()) {
                String next6 = it11.next();
                if (!motionController2.mTimeCycleAttributesMap.containsKey(next6)) {
                    if (!next6.startsWith(str68)) {
                        it4 = it11;
                        hashMap = hashMap2;
                        str46 = str68;
                        switch (next6.hashCode()) {
                            case -1249320806:
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                if (next6.equals(obj17)) {
                                    c15 = 0;
                                    break;
                                }
                                c15 = 65535;
                                break;
                            case -1249320805:
                                obj18 = obj3;
                                obj19 = obj;
                                if (!next6.equals(obj19)) {
                                    obj17 = obj2;
                                    c15 = 65535;
                                    break;
                                } else {
                                    c15 = 1;
                                    obj17 = obj2;
                                    break;
                                }
                            case -1225497657:
                                obj18 = obj3;
                                if (next6.equals(obj18)) {
                                    c15 = 2;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -1225497656:
                                if (next6.equals(str8)) {
                                    c16 = 3;
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -1225497655:
                                if (next6.equals(str9)) {
                                    c16 = 4;
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -1001078227:
                                if (next6.equals(str10)) {
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    c15 = 5;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -908189618:
                                if (next6.equals(str11)) {
                                    c16 = 6;
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -908189617:
                                if (next6.equals(str62)) {
                                    c16 = 7;
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -40300674:
                                if (next6.equals(str59)) {
                                    c16 = '\b';
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case -4379043:
                                if (next6.equals(str58)) {
                                    c16 = '\t';
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case 37232917:
                                if (next6.equals("transitionPathRotate")) {
                                    c16 = '\n';
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            case 92909918:
                                if (next6.equals("alpha")) {
                                    c16 = 11;
                                    c15 = c16;
                                    obj18 = obj3;
                                    obj17 = obj2;
                                    obj19 = obj;
                                    break;
                                }
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                            default:
                                obj18 = obj3;
                                obj17 = obj2;
                                obj19 = obj;
                                c15 = 65535;
                                break;
                        }
                        switch (c15) {
                            case 0:
                                customSet2 = new TimeCycleSplineSet.RotationXset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 1:
                                customSet2 = new TimeCycleSplineSet.RotationYset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 2:
                                customSet2 = new TimeCycleSplineSet.TranslationXset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 3:
                                customSet2 = new TimeCycleSplineSet.TranslationYset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 4:
                                customSet2 = new TimeCycleSplineSet.TranslationZset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 5:
                                customSet2 = new TimeCycleSplineSet.ProgressSet();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case FalsingManager.VERSION /* 6 */:
                                customSet2 = new TimeCycleSplineSet.ScaleXset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case 7:
                                customSet2 = new TimeCycleSplineSet.ScaleYset();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case '\b':
                                customSet2 = new TimeCycleSplineSet.RotationSet();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case '\t':
                                customSet2 = new TimeCycleSplineSet.ElevationSet();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case '\n':
                                customSet2 = new TimeCycleSplineSet.PathRotate();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            case QSTileImpl.H.STALE /* 11 */:
                                customSet2 = new TimeCycleSplineSet.AlphaSet();
                                str47 = str9;
                                obj16 = obj19;
                                customSet2.last_time = j;
                                break;
                            default:
                                str47 = str9;
                                obj16 = obj19;
                                customSet2 = null;
                                break;
                        }
                    } else {
                        SparseArray sparseArray2 = new SparseArray();
                        it4 = it11;
                        String str71 = next6.split(str5)[1];
                        str46 = str68;
                        for (Iterator<Key> it12 = motionController2.mKeyList.iterator(); it12.hasNext(); it12 = it12) {
                            Key next7 = it12.next();
                            HashMap<String, ConstraintAttribute> hashMap4 = next7.mCustomConstraints;
                            if (!(hashMap4 == null || (constraintAttribute2 = hashMap4.get(str71)) == null)) {
                                sparseArray2.append(next7.mFramePosition, constraintAttribute2);
                            }
                            hashMap2 = hashMap2;
                        }
                        hashMap = hashMap2;
                        customSet2 = new TimeCycleSplineSet.CustomSet(next6, sparseArray2);
                        obj18 = obj3;
                        obj17 = obj2;
                        obj16 = obj;
                        str47 = str9;
                    }
                    if (customSet2 != null) {
                        customSet2.mType = next6;
                        motionController2.mTimeCycleAttributesMap.put(next6, customSet2);
                    }
                    str9 = str47;
                    obj = obj16;
                    str68 = str46;
                    hashMap2 = hashMap;
                    obj3 = obj18;
                    obj2 = obj17;
                    it11 = it4;
                }
            }
            HashMap<String, Integer> hashMap5 = hashMap2;
            str13 = str68;
            obj6 = obj3;
            Object obj27 = obj2;
            obj4 = obj;
            String str72 = str9;
            ArrayList<Key> arrayList4 = motionController2.mKeyList;
            if (arrayList4 != null) {
                Iterator<Key> it13 = arrayList4.iterator();
                while (it13.hasNext()) {
                    Key next8 = it13.next();
                    if (next8 instanceof KeyTimeCycle) {
                        KeyTimeCycle keyTimeCycle2 = (KeyTimeCycle) next8;
                        HashMap<String, TimeCycleSplineSet> hashMap6 = motionController2.mTimeCycleAttributesMap;
                        Objects.requireNonNull(keyTimeCycle2);
                        Iterator<String> it14 = hashMap6.keySet().iterator();
                        while (it14.hasNext()) {
                            String next9 = it14.next();
                            TimeCycleSplineSet timeCycleSplineSet = hashMap6.get(next9);
                            if (next9.startsWith(str70)) {
                                ConstraintAttribute constraintAttribute4 = keyTimeCycle2.mCustomConstraints.get(next9.substring(7));
                                if (constraintAttribute4 != null) {
                                    TimeCycleSplineSet.CustomSet customSet5 = (TimeCycleSplineSet.CustomSet) timeCycleSplineSet;
                                    int i11 = keyTimeCycle2.mFramePosition;
                                    float f3 = keyTimeCycle2.mWavePeriod;
                                    int i12 = keyTimeCycle2.mWaveShape;
                                    float f4 = keyTimeCycle2.mWaveOffset;
                                    Objects.requireNonNull(customSet5);
                                    customSet5.mConstraintAttributeList.append(i11, constraintAttribute4);
                                    customSet5.mWaveProperties.append(i11, new float[]{f3, f4});
                                    customSet5.mWaveShape = Math.max(customSet5.mWaveShape, i12);
                                    it13 = it13;
                                    it14 = it14;
                                    keyTimeCycle2 = keyTimeCycle2;
                                    hashMap6 = hashMap6;
                                    str70 = str70;
                                    obj27 = obj27;
                                } else {
                                    it13 = it13;
                                    hashMap6 = hashMap6;
                                }
                            } else {
                                switch (next9.hashCode()) {
                                    case -1249320806:
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        if (next9.equals(obj13)) {
                                            c11 = 0;
                                            break;
                                        }
                                        c11 = 65535;
                                        break;
                                    case -1249320805:
                                        str45 = str72;
                                        obj14 = obj4;
                                        if (next9.equals(obj14)) {
                                            c11 = 1;
                                            obj13 = obj27;
                                            break;
                                        }
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -1225497657:
                                        str45 = str72;
                                        if (next9.equals(obj6)) {
                                            c12 = 2;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -1225497656:
                                        str45 = str72;
                                        if (next9.equals(str8)) {
                                            c12 = 3;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -1225497655:
                                        str45 = str72;
                                        if (next9.equals(str45)) {
                                            c12 = 4;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -1001078227:
                                        if (next9.equals(str10)) {
                                            str45 = str72;
                                            obj14 = obj4;
                                            c11 = 5;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -908189618:
                                        if (next9.equals(str11)) {
                                            c13 = 6;
                                            c11 = c13;
                                            str45 = str72;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -908189617:
                                        if (next9.equals(str62)) {
                                            c13 = 7;
                                            c11 = c13;
                                            str45 = str72;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -40300674:
                                        if (next9.equals(str59)) {
                                            c14 = '\b';
                                            c12 = c14;
                                            str45 = str72;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case -4379043:
                                        if (next9.equals(str58)) {
                                            c14 = '\t';
                                            c12 = c14;
                                            str45 = str72;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case 37232917:
                                        if (next9.equals("transitionPathRotate")) {
                                            c14 = '\n';
                                            c12 = c14;
                                            str45 = str72;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    case 92909918:
                                        if (next9.equals("alpha")) {
                                            c14 = 11;
                                            c12 = c14;
                                            str45 = str72;
                                            c11 = c12;
                                            obj14 = obj4;
                                            obj13 = obj27;
                                            break;
                                        }
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                    default:
                                        str45 = str72;
                                        obj14 = obj4;
                                        obj13 = obj27;
                                        c11 = 65535;
                                        break;
                                }
                                switch (c11) {
                                    case 0:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mRotationX)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mRotationX, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 1:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mRotationY)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mRotationY, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 2:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mTranslationX)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mTranslationX, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 3:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mTranslationY)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mTranslationY, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 4:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mTranslationZ)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mTranslationZ, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 5:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mProgress)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mProgress, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case FalsingManager.VERSION /* 6 */:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mScaleX)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mScaleX, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case 7:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mScaleY)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mScaleY, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case '\b':
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mRotation)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mRotation, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case '\t':
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mElevation)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mElevation, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case '\n':
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        if (!Float.isNaN(keyTimeCycle.mTransitionPathRotate)) {
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mTransitionPathRotate, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        }
                                        break;
                                    case QSTileImpl.H.STALE /* 11 */:
                                        keyTimeCycle = keyTimeCycle2;
                                        if (!Float.isNaN(keyTimeCycle.mAlpha)) {
                                            obj15 = obj13;
                                            obj4 = obj14;
                                            timeCycleSplineSet.setPoint(keyTimeCycle.mFramePosition, keyTimeCycle.mAlpha, keyTimeCycle.mWavePeriod, keyTimeCycle.mWaveShape, keyTimeCycle.mWaveOffset);
                                            break;
                                        } else {
                                            obj4 = obj14;
                                            obj15 = obj13;
                                            break;
                                        }
                                    default:
                                        keyTimeCycle = keyTimeCycle2;
                                        obj4 = obj14;
                                        obj15 = obj13;
                                        Log.e("KeyTimeCycles", "UNKNOWN addValues \"" + next9 + "\"");
                                        break;
                                }
                                it13 = it13;
                                it14 = it14;
                                keyTimeCycle2 = keyTimeCycle;
                                obj27 = obj15;
                                hashMap6 = hashMap6;
                                str70 = str70;
                                str72 = str45;
                            }
                        }
                    }
                    it13 = it13;
                    obj27 = obj27;
                    str70 = str70;
                    str72 = str72;
                    motionController2 = this;
                }
            }
            str12 = str70;
            str14 = str72;
            obj5 = obj27;
            motionController = this;
            for (String str73 : motionController.mTimeCycleAttributesMap.keySet()) {
                if (hashMap5.containsKey(str73)) {
                    i5 = hashMap5.get(str73).intValue();
                } else {
                    i5 = 0;
                }
                motionController.mTimeCycleAttributesMap.get(str73).setup(i5);
                hashMap5 = hashMap5;
            }
        } else {
            str12 = str70;
            str13 = str68;
            obj6 = obj3;
            obj5 = obj2;
            obj4 = obj;
            motionController = motionController2;
            str14 = str9;
        }
        int size = motionController.mMotionPaths.size() + 2;
        MotionPaths[] motionPathsArr = new MotionPaths[size];
        motionPathsArr[0] = motionController.mStartMotionPath;
        motionPathsArr[size - 1] = motionController.mEndMotionPath;
        if (motionController.mMotionPaths.size() > 0 && motionController.mCurveFitType == -1) {
            motionController.mCurveFitType = 0;
        }
        Iterator<MotionPaths> it15 = motionController.mMotionPaths.iterator();
        int i13 = 1;
        while (it15.hasNext()) {
            motionPathsArr[i13] = it15.next();
            i13++;
        }
        HashSet hashSet9 = new HashSet();
        Iterator<String> it16 = motionController.mEndMotionPath.attributes.keySet().iterator();
        while (it16.hasNext()) {
            String next10 = it16.next();
            if (motionController.mStartMotionPath.attributes.containsKey(next10)) {
                StringBuilder sb = new StringBuilder();
                obj12 = obj6;
                sb.append(str13);
                sb.append(next10);
                hashSet3 = hashSet2;
                if (!hashSet3.contains(sb.toString())) {
                    hashSet9.add(next10);
                }
            } else {
                obj12 = obj6;
                hashSet3 = hashSet2;
            }
            it16 = it16;
            hashSet2 = hashSet3;
            obj6 = obj12;
        }
        Object obj28 = obj6;
        String[] strArr = (String[]) hashSet9.toArray(new String[0]);
        motionController.mAttributeNames = strArr;
        motionController.mAttributeInterpCount = new int[strArr.length];
        int i14 = 0;
        while (true) {
            String[] strArr2 = motionController.mAttributeNames;
            if (i14 < strArr2.length) {
                String str74 = strArr2[i14];
                motionController.mAttributeInterpCount[i14] = 0;
                int i15 = 0;
                while (true) {
                    if (i15 >= size) {
                        break;
                    } else if (motionPathsArr[i15].attributes.containsKey(str74)) {
                        int[] iArr = motionController.mAttributeInterpCount;
                        iArr[i14] = motionPathsArr[i15].attributes.get(str74).noOfInterpValues() + iArr[i14];
                    } else {
                        i15++;
                    }
                }
                i14++;
            } else {
                if (motionPathsArr[0].mPathMotionArc != -1) {
                    z = true;
                } else {
                    z = false;
                }
                int length = strArr2.length + 18;
                boolean[] zArr = new boolean[length];
                int i16 = 1;
                while (i16 < size) {
                    MotionPaths motionPaths2 = motionPathsArr[i16];
                    MotionPaths motionPaths3 = motionPathsArr[i16 - 1];
                    Objects.requireNonNull(motionPaths2);
                    zArr[0] = zArr[0] | MotionPaths.diff(motionPaths2.position, motionPaths3.position);
                    zArr[1] = zArr[1] | MotionPaths.diff(motionPaths2.x, motionPaths3.x) | z;
                    zArr[2] = zArr[2] | MotionPaths.diff(motionPaths2.y, motionPaths3.y) | z;
                    zArr[3] = zArr[3] | MotionPaths.diff(motionPaths2.width, motionPaths3.width);
                    zArr[4] = MotionPaths.diff(motionPaths2.height, motionPaths3.height) | zArr[4];
                    i16++;
                    str8 = str8;
                    str62 = str62;
                    str14 = str14;
                    str10 = str10;
                    str11 = str11;
                }
                String str75 = str14;
                String str76 = str11;
                String str77 = str10;
                String str78 = str8;
                String str79 = str62;
                int i17 = 0;
                for (int i18 = 1; i18 < length; i18++) {
                    if (zArr[i18]) {
                        i17++;
                    }
                }
                motionController.mInterpolateVariables = new int[i17];
                motionController.mInterpolateData = new double[i17];
                motionController.mInterpolateVelocity = new double[i17];
                int i19 = 0;
                for (int i20 = 1; i20 < length; i20++) {
                    if (zArr[i20]) {
                        motionController.mInterpolateVariables[i19] = i20;
                        i19++;
                    }
                }
                double[][] dArr3 = (double[][]) Array.newInstance(double.class, size, motionController.mInterpolateVariables.length);
                double[] dArr4 = new double[size];
                int i21 = 0;
                while (i21 < size) {
                    MotionPaths motionPaths4 = motionPathsArr[i21];
                    double[] dArr5 = dArr3[i21];
                    int[] iArr2 = motionController.mInterpolateVariables;
                    Objects.requireNonNull(motionPaths4);
                    float[] fArr = {motionPaths4.position, motionPaths4.x, motionPaths4.y, motionPaths4.width, motionPaths4.height, motionPaths4.mPathRotate};
                    int i22 = 0;
                    int i23 = 0;
                    while (i22 < iArr2.length) {
                        if (iArr2[i22] < 6) {
                            dArr5[i23] = fArr[iArr2[i22]];
                            i23++;
                        }
                        i22++;
                        str58 = str58;
                    }
                    dArr4[i21] = motionPathsArr[i21].time;
                    i21++;
                    str59 = str59;
                }
                String str80 = str58;
                String str81 = str59;
                int i24 = 0;
                while (true) {
                    int[] iArr3 = motionController.mInterpolateVariables;
                    if (i24 < iArr3.length) {
                        int i25 = iArr3[i24];
                        String[] strArr3 = MotionPaths.names;
                        if (i25 < 6) {
                            String m2 = MotionController$$ExternalSyntheticOutline1.m(new StringBuilder(), strArr3[motionController.mInterpolateVariables[i24]], " [");
                            for (int i26 = 0; i26 < size; i26++) {
                                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m(m2);
                                m3.append(dArr3[i26][i24]);
                                m2 = m3.toString();
                            }
                        }
                        i24++;
                    } else {
                        motionController.mSpline = new CurveFit[motionController.mAttributeNames.length + 1];
                        int i27 = 0;
                        while (true) {
                            String[] strArr4 = motionController.mAttributeNames;
                            if (i27 < strArr4.length) {
                                String str82 = strArr4[i27];
                                int i28 = 0;
                                int i29 = 0;
                                double[] dArr6 = null;
                                double[][] dArr7 = null;
                                while (i28 < size) {
                                    MotionPaths motionPaths5 = motionPathsArr[i28];
                                    Objects.requireNonNull(motionPaths5);
                                    if (motionPaths5.attributes.containsKey(str82)) {
                                        if (dArr7 == null) {
                                            dArr6 = new double[size];
                                            MotionPaths motionPaths6 = motionPathsArr[i28];
                                            Objects.requireNonNull(motionPaths6);
                                            dArr7 = (double[][]) Array.newInstance(double.class, size, motionPaths6.attributes.get(str82).noOfInterpValues());
                                        }
                                        i4 = i27;
                                        dArr6[i29] = motionPathsArr[i28].time;
                                        MotionPaths motionPaths7 = motionPathsArr[i28];
                                        double[] dArr8 = dArr7[i29];
                                        Objects.requireNonNull(motionPaths7);
                                        ConstraintAttribute constraintAttribute5 = motionPaths7.attributes.get(str82);
                                        str44 = str82;
                                        if (constraintAttribute5.noOfInterpValues() == 1) {
                                            dArr = dArr6;
                                            dArr2 = dArr7;
                                            dArr8[0] = constraintAttribute5.getValueToInterpolate();
                                        } else {
                                            dArr = dArr6;
                                            dArr2 = dArr7;
                                            int noOfInterpValues = constraintAttribute5.noOfInterpValues();
                                            float[] fArr2 = new float[noOfInterpValues];
                                            constraintAttribute5.getValuesToInterpolate(fArr2);
                                            int i30 = 0;
                                            int i31 = 0;
                                            while (i30 < noOfInterpValues) {
                                                dArr8[i31] = fArr2[i30];
                                                i30++;
                                                i31++;
                                                dArr = dArr;
                                                noOfInterpValues = noOfInterpValues;
                                                fArr2 = fArr2;
                                            }
                                        }
                                        i29++;
                                        dArr7 = dArr2;
                                        dArr6 = dArr;
                                    } else {
                                        i4 = i27;
                                        str44 = str82;
                                    }
                                    i28++;
                                    str82 = str44;
                                    i27 = i4;
                                }
                                int i32 = i27 + 1;
                                motionController.mSpline[i32] = CurveFit.get(motionController.mCurveFitType, Arrays.copyOf(dArr6, i29), (double[][]) Arrays.copyOf(dArr7, i29));
                                i27 = i32;
                            } else {
                                motionController.mSpline[0] = CurveFit.get(motionController.mCurveFitType, dArr4, dArr3);
                                if (motionPathsArr[0].mPathMotionArc != -1) {
                                    int[] iArr4 = new int[size];
                                    double[] dArr9 = new double[size];
                                    double[][] dArr10 = (double[][]) Array.newInstance(double.class, size, 2);
                                    for (int i33 = 0; i33 < size; i33++) {
                                        iArr4[i33] = motionPathsArr[i33].mPathMotionArc;
                                        dArr9[i33] = motionPathsArr[i33].time;
                                        dArr10[i33][0] = motionPathsArr[i33].x;
                                        dArr10[i33][1] = motionPathsArr[i33].y;
                                    }
                                    motionController.mArcSpline = new ArcCurveFit(iArr4, dArr9, dArr10);
                                }
                                motionController.mCycleMap = new HashMap<>();
                                if (motionController.mKeyList != null) {
                                    Iterator<String> it17 = hashSet.iterator();
                                    float f5 = Float.NaN;
                                    while (it17.hasNext()) {
                                        String next11 = it17.next();
                                        if (next11.startsWith(str12)) {
                                            it3 = it17;
                                            customSet = new KeyCycleOscillator.CustomSet();
                                            str31 = str81;
                                            obj10 = obj28;
                                            str33 = str7;
                                            str26 = str78;
                                            str30 = str79;
                                            str27 = str75;
                                            str32 = str80;
                                            str28 = str77;
                                            str29 = str76;
                                        } else {
                                            switch (next11.hashCode()) {
                                                case -1249320806:
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str33 = str7;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (next11.equals(obj11)) {
                                                        c7 = 0;
                                                        break;
                                                    }
                                                    c7 = 65535;
                                                    break;
                                                case -1249320805:
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj10 = obj28;
                                                    str33 = str7;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (!next11.equals(obj4)) {
                                                        obj4 = obj4;
                                                        obj11 = obj5;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        c7 = 1;
                                                        obj4 = obj4;
                                                        obj11 = obj5;
                                                        break;
                                                    }
                                                case -1225497657:
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj10 = obj28;
                                                    str33 = str7;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (!next11.equals(obj10)) {
                                                        str6 = str6;
                                                        obj11 = obj5;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        c7 = 2;
                                                        str6 = str6;
                                                        obj11 = obj5;
                                                        break;
                                                    }
                                                case -1225497656:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str42 = str6;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (!next11.equals(str26)) {
                                                        it3 = it17;
                                                        str6 = str42;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        c7 = 3;
                                                        it3 = it17;
                                                        str6 = str42;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        break;
                                                    }
                                                case -1225497655:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str42 = str6;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (next11.equals(str27)) {
                                                        str26 = str78;
                                                        c7 = 4;
                                                        it3 = it17;
                                                        str6 = str42;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        break;
                                                    }
                                                    it3 = it17;
                                                    str6 = str42;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    c7 = 65535;
                                                    break;
                                                case -1001078227:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str42 = str6;
                                                    str30 = str79;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    if (!next11.equals(str28)) {
                                                        str27 = str75;
                                                        it3 = it17;
                                                        str6 = str42;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        c8 = 5;
                                                        it3 = it17;
                                                        str6 = str42;
                                                        c7 = c8;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        break;
                                                    }
                                                case -908189618:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str30 = str79;
                                                    str32 = str80;
                                                    str29 = str76;
                                                    if (!next11.equals(str29)) {
                                                        it3 = it17;
                                                        str6 = str6;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        it3 = it17;
                                                        str6 = str6;
                                                        c7 = 6;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        break;
                                                    }
                                                case -908189617:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str43 = str6;
                                                    str30 = str79;
                                                    str32 = str80;
                                                    if (next11.equals(str30)) {
                                                        it3 = it17;
                                                        str6 = str43;
                                                        c7 = 7;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        break;
                                                    }
                                                    it3 = it17;
                                                    str6 = str43;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str27 = str75;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                case -797520672:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str43 = str6;
                                                    str32 = str80;
                                                    if (next11.equals(str43)) {
                                                        c9 = '\b';
                                                        it3 = it17;
                                                        str6 = str43;
                                                        c7 = c9;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str30 = str79;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        break;
                                                    }
                                                    str30 = str79;
                                                    it3 = it17;
                                                    str6 = str43;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str27 = str75;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                case -40300674:
                                                    str31 = str81;
                                                    str33 = str7;
                                                    str32 = str80;
                                                    if (next11.equals(str31)) {
                                                        str43 = str6;
                                                        c9 = '\t';
                                                        it3 = it17;
                                                        str6 = str43;
                                                        c7 = c9;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str30 = str79;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        break;
                                                    }
                                                    str43 = str6;
                                                    str30 = str79;
                                                    it3 = it17;
                                                    str6 = str43;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str27 = str75;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                case -4379043:
                                                    str33 = str7;
                                                    str32 = str80;
                                                    if (next11.equals(str32)) {
                                                        c10 = '\n';
                                                        it3 = it17;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str30 = str79;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        c7 = c10;
                                                        str31 = str81;
                                                        break;
                                                    }
                                                    str31 = str81;
                                                    str43 = str6;
                                                    str30 = str79;
                                                    it3 = it17;
                                                    str6 = str43;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str27 = str75;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                case 37232917:
                                                    str33 = str7;
                                                    if (!next11.equals("transitionPathRotate")) {
                                                        str32 = str80;
                                                        str31 = str81;
                                                        str43 = str6;
                                                        str30 = str79;
                                                        it3 = it17;
                                                        str6 = str43;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        c7 = 65535;
                                                        break;
                                                    } else {
                                                        c10 = 11;
                                                        str32 = str80;
                                                        it3 = it17;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str30 = str79;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        c7 = c10;
                                                        str31 = str81;
                                                        break;
                                                    }
                                                case 92909918:
                                                    str33 = str7;
                                                    if (next11.equals("alpha")) {
                                                        str31 = str81;
                                                        str42 = str6;
                                                        str30 = str79;
                                                        str32 = str80;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        c8 = '\f';
                                                        it3 = it17;
                                                        str6 = str42;
                                                        c7 = c8;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str27 = str75;
                                                        break;
                                                    }
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                case 156108012:
                                                    str33 = str7;
                                                    if (next11.equals(str33)) {
                                                        it3 = it17;
                                                        str31 = str81;
                                                        obj11 = obj5;
                                                        obj10 = obj28;
                                                        str26 = str78;
                                                        str30 = str79;
                                                        str27 = str75;
                                                        str28 = str77;
                                                        str29 = str76;
                                                        c7 = '\r';
                                                        str32 = str80;
                                                        break;
                                                    }
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                                default:
                                                    it3 = it17;
                                                    str31 = str81;
                                                    obj11 = obj5;
                                                    obj10 = obj28;
                                                    str33 = str7;
                                                    str26 = str78;
                                                    str30 = str79;
                                                    str27 = str75;
                                                    str32 = str80;
                                                    str28 = str77;
                                                    str29 = str76;
                                                    c7 = 65535;
                                                    break;
                                            }
                                            switch (c7) {
                                                case 0:
                                                    rotationXset = new KeyCycleOscillator.RotationXset();
                                                    break;
                                                case 1:
                                                    rotationXset = new KeyCycleOscillator.RotationYset();
                                                    break;
                                                case 2:
                                                    rotationXset = new KeyCycleOscillator.TranslationXset();
                                                    break;
                                                case 3:
                                                    rotationXset = new KeyCycleOscillator.TranslationYset();
                                                    break;
                                                case 4:
                                                    rotationXset = new KeyCycleOscillator.TranslationZset();
                                                    break;
                                                case 5:
                                                    rotationXset = new KeyCycleOscillator.ProgressSet();
                                                    break;
                                                case FalsingManager.VERSION /* 6 */:
                                                    rotationXset = new KeyCycleOscillator.ScaleXset();
                                                    break;
                                                case 7:
                                                    rotationXset = new KeyCycleOscillator.ScaleYset();
                                                    break;
                                                case '\b':
                                                    rotationXset = new KeyCycleOscillator.AlphaSet();
                                                    break;
                                                case '\t':
                                                    rotationXset = new KeyCycleOscillator.RotationSet();
                                                    break;
                                                case '\n':
                                                    rotationXset = new KeyCycleOscillator.ElevationSet();
                                                    break;
                                                case QSTileImpl.H.STALE /* 11 */:
                                                    rotationXset = new KeyCycleOscillator.PathRotateSet();
                                                    break;
                                                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                                                    rotationXset = new KeyCycleOscillator.AlphaSet();
                                                    break;
                                                case QS.VERSION /* 13 */:
                                                    rotationXset = new KeyCycleOscillator.AlphaSet();
                                                    break;
                                                default:
                                                    rotationXset = null;
                                                    break;
                                            }
                                            obj5 = obj11;
                                            customSet = rotationXset;
                                        }
                                        if (customSet == null) {
                                            obj28 = obj10;
                                            str35 = str32;
                                            str37 = str30;
                                            str76 = str29;
                                            str77 = str28;
                                            str34 = str27;
                                            str36 = str26;
                                            str38 = str31;
                                        } else {
                                            obj28 = obj10;
                                            str36 = str26;
                                            if (customSet.mVariesBy == 1) {
                                                z2 = true;
                                            } else {
                                                z2 = false;
                                            }
                                            if (!z2 || !Float.isNaN(f5)) {
                                                str35 = str32;
                                                str38 = str31;
                                                str37 = str30;
                                                str76 = str29;
                                                str77 = str28;
                                                str34 = str27;
                                            } else {
                                                float[] fArr3 = new float[2];
                                                float f6 = 1.0f / 99;
                                                f5 = 0.0f;
                                                str77 = str28;
                                                double d2 = 0.0d;
                                                double d3 = 0.0d;
                                                str34 = str27;
                                                int i34 = 0;
                                                while (i34 < 100) {
                                                    float f7 = i34 * f6;
                                                    double d4 = f7;
                                                    Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
                                                    float f8 = 0.0f;
                                                    float f9 = Float.NaN;
                                                    for (Iterator<MotionPaths> it18 = motionController.mMotionPaths.iterator(); it18.hasNext(); it18 = it18) {
                                                        MotionPaths next12 = it18.next();
                                                        Easing easing2 = next12.mKeyFrameEasing;
                                                        if (easing2 != null) {
                                                            float f10 = next12.time;
                                                            if (f10 < f7) {
                                                                f8 = f10;
                                                                easing = easing2;
                                                            } else if (Float.isNaN(f9)) {
                                                                f9 = next12.time;
                                                            }
                                                        }
                                                        f6 = f6;
                                                    }
                                                    if (easing != null) {
                                                        if (Float.isNaN(f9)) {
                                                            f9 = 1.0f;
                                                        }
                                                        str40 = str32;
                                                        str39 = str31;
                                                        d = (((float) easing.get((f7 - f8) / f2)) * (f9 - f8)) + f8;
                                                    } else {
                                                        str40 = str32;
                                                        str39 = str31;
                                                        d = d4;
                                                    }
                                                    motionController.mSpline[0].getPos(d, motionController.mInterpolateData);
                                                    motionController.mStartMotionPath.getCenter(motionController.mInterpolateVariables, motionController.mInterpolateData, fArr3, 0);
                                                    if (i34 > 0) {
                                                        str41 = str40;
                                                        double d5 = d2 - fArr3[1];
                                                        i3 = i34;
                                                        f5 = (float) (Math.hypot(d5, d3 - fArr3[0]) + f5);
                                                        c6 = 0;
                                                    } else {
                                                        str41 = str40;
                                                        i3 = i34;
                                                        c6 = 0;
                                                    }
                                                    i34 = i3 + 1;
                                                    d3 = fArr3[c6];
                                                    d2 = fArr3[1];
                                                    str31 = str39;
                                                    str30 = str30;
                                                    str32 = str41;
                                                    str29 = str29;
                                                    f6 = f6;
                                                }
                                                str35 = str32;
                                                str38 = str31;
                                                str37 = str30;
                                                str76 = str29;
                                            }
                                            customSet.mType = next11;
                                            motionController.mCycleMap.put(next11, customSet);
                                        }
                                        it17 = it3;
                                        str12 = str12;
                                        str80 = str35;
                                        str75 = str34;
                                        str79 = str37;
                                        str78 = str36;
                                        str7 = str33;
                                        str81 = str38;
                                    }
                                    String str83 = str81;
                                    String str84 = str7;
                                    String str85 = str78;
                                    String str86 = str79;
                                    String str87 = str75;
                                    String str88 = str12;
                                    String str89 = str80;
                                    Iterator<Key> it19 = motionController.mKeyList.iterator();
                                    while (it19.hasNext()) {
                                        Key next13 = it19.next();
                                        if (next13 instanceof KeyCycle) {
                                            KeyCycle keyCycle = (KeyCycle) next13;
                                            HashMap<String, KeyCycleOscillator> hashMap7 = motionController.mCycleMap;
                                            Objects.requireNonNull(keyCycle);
                                            Iterator<String> it20 = hashMap7.keySet().iterator();
                                            while (it20.hasNext()) {
                                                String next14 = it20.next();
                                                if (!next14.startsWith(str88) || (constraintAttribute = keyCycle.mCustomConstraints.get(next14.substring(7))) == null || constraintAttribute.mType != ConstraintAttribute.AttributeType.FLOAT_TYPE) {
                                                    it = it19;
                                                    str15 = str88;
                                                    it2 = it20;
                                                    str16 = str83;
                                                } else {
                                                    KeyCycleOscillator keyCycleOscillator = hashMap7.get(next14);
                                                    int i35 = keyCycle.mFramePosition;
                                                    int i36 = keyCycle.mWaveShape;
                                                    int i37 = keyCycle.mWaveVariesBy;
                                                    it = it19;
                                                    float f11 = keyCycle.mWavePeriod;
                                                    str15 = str88;
                                                    float f12 = keyCycle.mWaveOffset;
                                                    it2 = it20;
                                                    float valueToInterpolate = constraintAttribute.getValueToInterpolate();
                                                    Objects.requireNonNull(keyCycleOscillator);
                                                    str16 = str83;
                                                    keyCycleOscillator.mWavePoints.add(new KeyCycleOscillator.WavePoint(i35, f11, f12, valueToInterpolate));
                                                    if (i37 != -1) {
                                                        keyCycleOscillator.mVariesBy = i37;
                                                    }
                                                    keyCycleOscillator.mWaveShape = i36;
                                                    keyCycleOscillator.mCustom = constraintAttribute;
                                                }
                                                switch (next14.hashCode()) {
                                                    case -1249320806:
                                                        str21 = str86;
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (next14.equals(obj7)) {
                                                            c = 0;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case -1249320805:
                                                        str21 = str86;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (!next14.equals(obj8)) {
                                                            obj7 = obj5;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            c = 1;
                                                            obj7 = obj5;
                                                            break;
                                                        }
                                                    case -1225497657:
                                                        str21 = str86;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (!next14.equals(obj9)) {
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            c = 2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            break;
                                                        }
                                                    case -1225497656:
                                                        str21 = str86;
                                                        str17 = str85;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (!next14.equals(str17)) {
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            c2 = 3;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                    case -1225497655:
                                                        str21 = str86;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (next14.equals(str18)) {
                                                            str17 = str85;
                                                            c2 = 4;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        c = 65535;
                                                        break;
                                                    case -1001078227:
                                                        str21 = str86;
                                                        str23 = str89;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        if (!next14.equals(str19)) {
                                                            str18 = str87;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            str17 = str85;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            str17 = str85;
                                                            c2 = 5;
                                                            str18 = str87;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                    case -908189618:
                                                        str21 = str86;
                                                        str23 = str89;
                                                        str22 = str16;
                                                        str20 = str76;
                                                        if (next14.equals(str20)) {
                                                            c = 6;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            break;
                                                        }
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str18 = str87;
                                                        str19 = str77;
                                                        c = 65535;
                                                        break;
                                                    case -908189617:
                                                        str21 = str86;
                                                        str23 = str89;
                                                        str22 = str16;
                                                        if (!next14.equals(str21)) {
                                                            str20 = str76;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            c = 7;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            break;
                                                        }
                                                    case -40300674:
                                                        str23 = str89;
                                                        str22 = str16;
                                                        if (!next14.equals(str22)) {
                                                            str21 = str86;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = 65535;
                                                            break;
                                                        } else {
                                                            c3 = '\b';
                                                            c2 = c3;
                                                            str21 = str86;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                    case -4379043:
                                                        str23 = str89;
                                                        if (next14.equals(str23)) {
                                                            c4 = '\t';
                                                            c3 = c4;
                                                            str22 = str16;
                                                            c2 = c3;
                                                            str21 = str86;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                        str21 = str86;
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        c = 65535;
                                                        break;
                                                    case 37232917:
                                                        if (next14.equals("transitionPathRotate")) {
                                                            c4 = '\n';
                                                            str23 = str89;
                                                            c3 = c4;
                                                            str22 = str16;
                                                            c2 = c3;
                                                            str21 = str86;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                        str23 = str89;
                                                        str21 = str86;
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        c = 65535;
                                                        break;
                                                    case 92909918:
                                                        if (next14.equals("alpha")) {
                                                            c5 = 11;
                                                            c3 = c5;
                                                            str23 = str89;
                                                            str22 = str16;
                                                            c2 = c3;
                                                            str21 = str86;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                        str23 = str89;
                                                        str21 = str86;
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        c = 65535;
                                                        break;
                                                    case 156108012:
                                                        if (next14.equals(str84)) {
                                                            c5 = '\f';
                                                            c3 = c5;
                                                            str23 = str89;
                                                            str22 = str16;
                                                            c2 = c3;
                                                            str21 = str86;
                                                            str17 = str85;
                                                            str18 = str87;
                                                            str19 = str77;
                                                            str20 = str76;
                                                            c = c2;
                                                            obj7 = obj5;
                                                            obj8 = obj4;
                                                            obj9 = obj28;
                                                            break;
                                                        }
                                                    default:
                                                        str21 = str86;
                                                        obj7 = obj5;
                                                        obj8 = obj4;
                                                        obj9 = obj28;
                                                        str17 = str85;
                                                        str23 = str89;
                                                        str18 = str87;
                                                        str22 = str16;
                                                        str19 = str77;
                                                        str20 = str76;
                                                        c = 65535;
                                                        break;
                                                }
                                                switch (c) {
                                                    case 0:
                                                        str24 = str23;
                                                        f = keyCycle.mRotationX;
                                                        str25 = str22;
                                                        break;
                                                    case 1:
                                                        str24 = str23;
                                                        f = keyCycle.mRotationY;
                                                        str25 = str22;
                                                        break;
                                                    case 2:
                                                        str24 = str23;
                                                        f = keyCycle.mTranslationX;
                                                        str25 = str22;
                                                        break;
                                                    case 3:
                                                        str24 = str23;
                                                        f = keyCycle.mTranslationY;
                                                        str25 = str22;
                                                        break;
                                                    case 4:
                                                        str24 = str23;
                                                        f = keyCycle.mTranslationZ;
                                                        str25 = str22;
                                                        break;
                                                    case 5:
                                                        str24 = str23;
                                                        f = keyCycle.mProgress;
                                                        str25 = str22;
                                                        break;
                                                    case FalsingManager.VERSION /* 6 */:
                                                        str24 = str23;
                                                        f = keyCycle.mScaleX;
                                                        str25 = str22;
                                                        break;
                                                    case 7:
                                                        str24 = str23;
                                                        f = keyCycle.mScaleY;
                                                        str25 = str22;
                                                        break;
                                                    case '\b':
                                                        str24 = str23;
                                                        f = keyCycle.mRotation;
                                                        str25 = str22;
                                                        break;
                                                    case '\t':
                                                        str24 = str23;
                                                        f = keyCycle.mElevation;
                                                        str25 = str22;
                                                        break;
                                                    case '\n':
                                                        str24 = str23;
                                                        f = keyCycle.mTransitionPathRotate;
                                                        str25 = str22;
                                                        break;
                                                    case QSTileImpl.H.STALE /* 11 */:
                                                        str24 = str23;
                                                        f = keyCycle.mAlpha;
                                                        str25 = str22;
                                                        break;
                                                    case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                                                        str24 = str23;
                                                        f = keyCycle.mWaveOffset;
                                                        str25 = str22;
                                                        break;
                                                    default:
                                                        str24 = str23;
                                                        StringBuilder sb2 = new StringBuilder();
                                                        str25 = str22;
                                                        sb2.append("  UNKNOWN  ");
                                                        sb2.append(next14);
                                                        Log.v("WARNING! KeyCycle", sb2.toString());
                                                        f = Float.NaN;
                                                        break;
                                                }
                                                if (!Float.isNaN(f)) {
                                                    KeyCycleOscillator keyCycleOscillator2 = hashMap7.get(next14);
                                                    int i38 = keyCycle.mFramePosition;
                                                    int i39 = keyCycle.mWaveShape;
                                                    int i40 = keyCycle.mWaveVariesBy;
                                                    float f13 = keyCycle.mWavePeriod;
                                                    str76 = str20;
                                                    float f14 = keyCycle.mWaveOffset;
                                                    Objects.requireNonNull(keyCycleOscillator2);
                                                    keyCycleOscillator2.mWavePoints.add(new KeyCycleOscillator.WavePoint(i38, f13, f14, f));
                                                    if (i40 != -1) {
                                                        keyCycleOscillator2.mVariesBy = i40;
                                                    }
                                                    keyCycleOscillator2.mWaveShape = i39;
                                                    it19 = it;
                                                    obj28 = obj9;
                                                    hashMap7 = hashMap7;
                                                    keyCycle = keyCycle;
                                                    str84 = str84;
                                                    it20 = it2;
                                                    str77 = str19;
                                                    str88 = str15;
                                                    str89 = str24;
                                                    str85 = str17;
                                                    obj4 = obj8;
                                                    obj5 = obj7;
                                                    str83 = str25;
                                                    str86 = str21;
                                                    str87 = str18;
                                                } else {
                                                    it19 = it;
                                                    str76 = str20;
                                                    str77 = str19;
                                                    str87 = str18;
                                                    str85 = str17;
                                                    obj28 = obj9;
                                                    obj4 = obj8;
                                                    obj5 = obj7;
                                                    str83 = str25;
                                                    it20 = it2;
                                                    str88 = str15;
                                                    str89 = str24;
                                                    str86 = str21;
                                                }
                                            }
                                        }
                                        motionController = this;
                                        it19 = it19;
                                        obj28 = obj28;
                                        obj4 = obj4;
                                        obj5 = obj5;
                                        str83 = str83;
                                        str84 = str84;
                                        str86 = str86;
                                        str77 = str77;
                                        str88 = str88;
                                        str89 = str89;
                                        str87 = str87;
                                        str85 = str85;
                                    }
                                    for (KeyCycleOscillator keyCycleOscillator3 : motionController.mCycleMap.values()) {
                                        keyCycleOscillator3.setup();
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" start: x: ");
        m.append(this.mStartMotionPath.x);
        m.append(" y: ");
        m.append(this.mStartMotionPath.y);
        m.append(" end: x: ");
        m.append(this.mEndMotionPath.x);
        m.append(" y: ");
        m.append(this.mEndMotionPath.y);
        return m.toString();
    }

    public MotionController(View view) {
        this.mView = view;
        this.mId = view.getId();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            Objects.requireNonNull((ConstraintLayout.LayoutParams) layoutParams);
        }
    }
}
