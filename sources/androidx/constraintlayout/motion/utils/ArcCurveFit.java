package androidx.constraintlayout.motion.utils;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ArcCurveFit extends CurveFit {
    public Arc[] mArcs;
    public final double[] mTime;

    /* loaded from: classes.dex */
    public static class Arc {
        public static double[] ourPercent = new double[91];
        public boolean linear;
        public double mArcDistance;
        public double mArcVelocity;
        public double mEllipseA;
        public double mEllipseB;
        public double mEllipseCenterX;
        public double mEllipseCenterY;
        public double[] mLut;
        public double mOneOverDeltaTime;
        public double mTime1;
        public double mTime2;
        public double mTmpCosAngle;
        public double mTmpSinAngle;
        public boolean mVertical;
        public double mX1;
        public double mX2;
        public double mY1;
        public double mY2;

        public Arc(int i, double d, double d2, double d3, double d4, double d5, double d6) {
            int i2;
            double d7;
            double[] dArr;
            double d8 = d3;
            boolean z = false;
            this.linear = false;
            int i3 = 1;
            this.mVertical = i == 1 ? true : z;
            this.mTime1 = d;
            this.mTime2 = d2;
            this.mOneOverDeltaTime = 1.0d / (d2 - d);
            if (3 == i) {
                this.linear = true;
            }
            double d9 = d5 - d8;
            double d10 = d6 - d4;
            if (this.linear || Math.abs(d9) < 0.001d || Math.abs(d10) < 0.001d) {
                this.linear = true;
                this.mX1 = d8;
                this.mX2 = d5;
                this.mY1 = d4;
                this.mY2 = d6;
                double hypot = Math.hypot(d10, d9);
                this.mArcDistance = hypot;
                this.mArcVelocity = hypot * this.mOneOverDeltaTime;
                double d11 = this.mTime2;
                double d12 = this.mTime1;
                this.mEllipseCenterX = d9 / (d11 - d12);
                this.mEllipseCenterY = d10 / (d11 - d12);
                return;
            }
            this.mLut = new double[101];
            boolean z2 = this.mVertical;
            this.mEllipseA = (z2 ? -1 : i3) * d9;
            if (z2) {
                i2 = 1;
            } else {
                i2 = -1;
            }
            this.mEllipseB = d10 * i2;
            this.mEllipseCenterX = z2 ? d5 : d8;
            if (z2) {
                d7 = d4;
            } else {
                d7 = d6;
            }
            this.mEllipseCenterY = d7;
            double d13 = d4 - d6;
            int i4 = 0;
            double d14 = 0.0d;
            double d15 = 0.0d;
            double d16 = 0.0d;
            while (true) {
                double[] dArr2 = ourPercent;
                if (i4 >= 91) {
                    break;
                }
                double radians = Math.toRadians((i4 * 90.0d) / 90);
                double sin = Math.sin(radians) * d9;
                double cos = Math.cos(radians) * d13;
                if (i4 > 0) {
                    d14 += Math.hypot(sin - d15, cos - d16);
                    dArr2[i4] = d14;
                }
                i4++;
                d16 = cos;
                d15 = sin;
            }
            this.mArcDistance = d14;
            int i5 = 0;
            while (true) {
                double[] dArr3 = ourPercent;
                if (i5 >= 91) {
                    break;
                }
                dArr3[i5] = dArr3[i5] / d14;
                i5++;
            }
            int i6 = 0;
            while (true) {
                if (i6 < this.mLut.length) {
                    double length = i6 / (dArr.length - 1);
                    double[] dArr4 = ourPercent;
                    int binarySearch = Arrays.binarySearch(dArr4, length);
                    if (binarySearch >= 0) {
                        this.mLut[i6] = binarySearch / 90;
                    } else if (binarySearch == -1) {
                        this.mLut[i6] = 0.0d;
                    } else {
                        int i7 = -binarySearch;
                        int i8 = i7 - 2;
                        this.mLut[i6] = (((length - dArr4[i8]) / (dArr4[i7 - 1] - dArr4[i8])) + i8) / 90;
                    }
                    i6++;
                } else {
                    this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
                    return;
                }
            }
        }

        public final void setPoint(double d) {
            double d2;
            if (this.mVertical) {
                d2 = this.mTime2 - d;
            } else {
                d2 = d - this.mTime1;
            }
            double d3 = d2 * this.mOneOverDeltaTime;
            double d4 = 0.0d;
            if (d3 > 0.0d) {
                d4 = 1.0d;
                if (d3 < 1.0d) {
                    double[] dArr = this.mLut;
                    double length = d3 * (dArr.length - 1);
                    int i = (int) length;
                    double d5 = dArr[i];
                    d4 = ((dArr[i + 1] - dArr[i]) * (length - i)) + d5;
                }
            }
            double d6 = d4 * 1.5707963267948966d;
            this.mTmpSinAngle = Math.sin(d6);
            this.mTmpCosAngle = Math.cos(d6);
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final void getPos(double d, double[] dArr) {
        Arc[] arcArr = this.mArcs;
        if (d < arcArr[0].mTime1) {
            d = arcArr[0].mTime1;
        }
        if (d > arcArr[arcArr.length - 1].mTime2) {
            d = arcArr[arcArr.length - 1].mTime2;
        }
        int i = 0;
        while (true) {
            Arc[] arcArr2 = this.mArcs;
            if (i >= arcArr2.length) {
                return;
            }
            if (d > arcArr2[i].mTime2) {
                i++;
            } else if (arcArr2[i].linear) {
                Arc arc = arcArr2[i];
                Objects.requireNonNull(arc);
                double d2 = (d - arc.mTime1) * arc.mOneOverDeltaTime;
                double d3 = arc.mX1;
                dArr[0] = ((arc.mX2 - d3) * d2) + d3;
                Arc arc2 = this.mArcs[i];
                Objects.requireNonNull(arc2);
                double d4 = (d - arc2.mTime1) * arc2.mOneOverDeltaTime;
                double d5 = arc2.mY1;
                dArr[1] = ((arc2.mY2 - d5) * d4) + d5;
                return;
            } else {
                arcArr2[i].setPoint(d);
                Arc arc3 = this.mArcs[i];
                Objects.requireNonNull(arc3);
                dArr[0] = (arc3.mEllipseA * arc3.mTmpSinAngle) + arc3.mEllipseCenterX;
                Arc arc4 = this.mArcs[i];
                Objects.requireNonNull(arc4);
                dArr[1] = (arc4.mEllipseB * arc4.mTmpCosAngle) + arc4.mEllipseCenterY;
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final void getSlope(double d, double[] dArr) {
        Arc[] arcArr = this.mArcs;
        if (d < arcArr[0].mTime1) {
            d = arcArr[0].mTime1;
        } else if (d > arcArr[arcArr.length - 1].mTime2) {
            d = arcArr[arcArr.length - 1].mTime2;
        }
        int i = 0;
        while (true) {
            Arc[] arcArr2 = this.mArcs;
            if (i >= arcArr2.length) {
                return;
            }
            if (d > arcArr2[i].mTime2) {
                i++;
            } else if (arcArr2[i].linear) {
                Arc arc = arcArr2[i];
                Objects.requireNonNull(arc);
                dArr[0] = arc.mEllipseCenterX;
                Arc arc2 = this.mArcs[i];
                Objects.requireNonNull(arc2);
                dArr[1] = arc2.mEllipseCenterY;
                return;
            } else {
                arcArr2[i].setPoint(d);
                Arc arc3 = this.mArcs[i];
                Objects.requireNonNull(arc3);
                double d2 = arc3.mEllipseA * arc3.mTmpCosAngle;
                double hypot = arc3.mArcVelocity / Math.hypot(d2, (-arc3.mEllipseB) * arc3.mTmpSinAngle);
                if (arc3.mVertical) {
                    d2 = -d2;
                }
                dArr[0] = d2 * hypot;
                Arc arc4 = this.mArcs[i];
                Objects.requireNonNull(arc4);
                double d3 = arc4.mEllipseA * arc4.mTmpCosAngle;
                double d4 = (-arc4.mEllipseB) * arc4.mTmpSinAngle;
                double hypot2 = arc4.mArcVelocity / Math.hypot(d3, d4);
                dArr[1] = arc4.mVertical ? (-d4) * hypot2 : d4 * hypot2;
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0026, code lost:
        if (r5 == 1) goto L_0x0028;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ArcCurveFit(int[] r25, double[] r26, double[][] r27) {
        /*
            r24 = this;
            r0 = r24
            r1 = r26
            r24.<init>()
            r0.mTime = r1
            int r2 = r1.length
            r3 = 1
            int r2 = r2 - r3
            androidx.constraintlayout.motion.utils.ArcCurveFit$Arc[] r2 = new androidx.constraintlayout.motion.utils.ArcCurveFit.Arc[r2]
            r0.mArcs = r2
            r2 = 0
            r4 = r2
            r5 = r3
            r6 = r5
        L_0x0014:
            androidx.constraintlayout.motion.utils.ArcCurveFit$Arc[] r7 = r0.mArcs
            int r8 = r7.length
            if (r4 >= r8) goto L_0x0051
            r8 = r25[r4]
            r9 = 3
            r10 = 2
            if (r8 == 0) goto L_0x002d
            if (r8 == r3) goto L_0x002a
            if (r8 == r10) goto L_0x0028
            if (r8 == r9) goto L_0x0026
            goto L_0x002e
        L_0x0026:
            if (r5 != r3) goto L_0x002a
        L_0x0028:
            r5 = r10
            goto L_0x002b
        L_0x002a:
            r5 = r3
        L_0x002b:
            r6 = r5
            goto L_0x002e
        L_0x002d:
            r6 = r9
        L_0x002e:
            androidx.constraintlayout.motion.utils.ArcCurveFit$Arc r22 = new androidx.constraintlayout.motion.utils.ArcCurveFit$Arc
            r10 = r1[r4]
            int r23 = r4 + 1
            r12 = r1[r23]
            r8 = r27[r4]
            r14 = r8[r2]
            r8 = r27[r4]
            r16 = r8[r3]
            r8 = r27[r23]
            r18 = r8[r2]
            r8 = r27[r23]
            r20 = r8[r3]
            r8 = r22
            r9 = r6
            r8.<init>(r9, r10, r12, r14, r16, r18, r20)
            r7[r4] = r22
            r4 = r23
            goto L_0x0014
        L_0x0051:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.utils.ArcCurveFit.<init>(int[], double[], double[][]):void");
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final void getPos(double d, float[] fArr) {
        Arc[] arcArr = this.mArcs;
        if (d < arcArr[0].mTime1) {
            d = arcArr[0].mTime1;
        } else if (d > arcArr[arcArr.length - 1].mTime2) {
            d = arcArr[arcArr.length - 1].mTime2;
        }
        int i = 0;
        while (true) {
            Arc[] arcArr2 = this.mArcs;
            if (i >= arcArr2.length) {
                return;
            }
            if (d > arcArr2[i].mTime2) {
                i++;
            } else if (arcArr2[i].linear) {
                Arc arc = arcArr2[i];
                Objects.requireNonNull(arc);
                double d2 = (d - arc.mTime1) * arc.mOneOverDeltaTime;
                double d3 = arc.mX1;
                fArr[0] = (float) (((arc.mX2 - d3) * d2) + d3);
                Arc arc2 = this.mArcs[i];
                Objects.requireNonNull(arc2);
                double d4 = (d - arc2.mTime1) * arc2.mOneOverDeltaTime;
                double d5 = arc2.mY1;
                fArr[1] = (float) (((arc2.mY2 - d5) * d4) + d5);
                return;
            } else {
                arcArr2[i].setPoint(d);
                Arc arc3 = this.mArcs[i];
                Objects.requireNonNull(arc3);
                fArr[0] = (float) ((arc3.mEllipseA * arc3.mTmpSinAngle) + arc3.mEllipseCenterX);
                Arc arc4 = this.mArcs[i];
                Objects.requireNonNull(arc4);
                fArr[1] = (float) ((arc4.mEllipseB * arc4.mTmpCosAngle) + arc4.mEllipseCenterY);
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final double getSlope(double d) {
        Arc[] arcArr = this.mArcs;
        int i = 0;
        if (d < arcArr[0].mTime1) {
            d = arcArr[0].mTime1;
        }
        if (d > arcArr[arcArr.length - 1].mTime2) {
            d = arcArr[arcArr.length - 1].mTime2;
        }
        while (true) {
            Arc[] arcArr2 = this.mArcs;
            if (i >= arcArr2.length) {
                return Double.NaN;
            }
            if (d > arcArr2[i].mTime2) {
                i++;
            } else if (arcArr2[i].linear) {
                Arc arc = arcArr2[i];
                Objects.requireNonNull(arc);
                return arc.mEllipseCenterX;
            } else {
                arcArr2[i].setPoint(d);
                Arc arc2 = this.mArcs[i];
                Objects.requireNonNull(arc2);
                double d2 = arc2.mEllipseA * arc2.mTmpCosAngle;
                double hypot = arc2.mArcVelocity / Math.hypot(d2, (-arc2.mEllipseB) * arc2.mTmpSinAngle);
                if (arc2.mVertical) {
                    d2 = -d2;
                }
                return d2 * hypot;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final double getPos(double d) {
        Arc[] arcArr = this.mArcs;
        int i = 0;
        if (d < arcArr[0].mTime1) {
            d = arcArr[0].mTime1;
        } else if (d > arcArr[arcArr.length - 1].mTime2) {
            d = arcArr[arcArr.length - 1].mTime2;
        }
        while (true) {
            Arc[] arcArr2 = this.mArcs;
            if (i >= arcArr2.length) {
                return Double.NaN;
            }
            if (d > arcArr2[i].mTime2) {
                i++;
            } else if (arcArr2[i].linear) {
                Arc arc = arcArr2[i];
                Objects.requireNonNull(arc);
                double d2 = (d - arc.mTime1) * arc.mOneOverDeltaTime;
                double d3 = arc.mX1;
                return ((arc.mX2 - d3) * d2) + d3;
            } else {
                arcArr2[i].setPoint(d);
                Arc arc2 = this.mArcs[i];
                Objects.requireNonNull(arc2);
                return (arc2.mEllipseA * arc2.mTmpSinAngle) + arc2.mEllipseCenterX;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public final double[] getTimePoints() {
        return this.mTime;
    }
}
