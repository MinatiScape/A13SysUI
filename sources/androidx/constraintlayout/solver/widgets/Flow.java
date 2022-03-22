package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Flow extends VirtualLayout {
    public ConstraintWidget[] mDisplayedWidgets;
    public int mHorizontalStyle = -1;
    public int mVerticalStyle = -1;
    public int mFirstHorizontalStyle = -1;
    public int mFirstVerticalStyle = -1;
    public int mLastHorizontalStyle = -1;
    public int mLastVerticalStyle = -1;
    public float mHorizontalBias = 0.5f;
    public float mVerticalBias = 0.5f;
    public float mFirstHorizontalBias = 0.5f;
    public float mFirstVerticalBias = 0.5f;
    public float mLastHorizontalBias = 0.5f;
    public float mLastVerticalBias = 0.5f;
    public int mHorizontalGap = 0;
    public int mVerticalGap = 0;
    public int mHorizontalAlign = 2;
    public int mVerticalAlign = 2;
    public int mWrapMode = 0;
    public int mMaxElementsWrap = -1;
    public int mOrientation = 0;
    public ArrayList<WidgetsList> mChainList = new ArrayList<>();
    public ConstraintWidget[] mAlignedBiggestElementsInRows = null;
    public ConstraintWidget[] mAlignedBiggestElementsInCols = null;
    public int[] mAlignedDimensions = null;
    public int mDisplayedWidgetsCount = 0;

    /* loaded from: classes.dex */
    public class WidgetsList {
        public ConstraintAnchor mBottom;
        public ConstraintAnchor mLeft;
        public int mMax;
        public int mOrientation;
        public int mPaddingBottom;
        public int mPaddingLeft;
        public int mPaddingRight;
        public int mPaddingTop;
        public ConstraintAnchor mRight;
        public ConstraintAnchor mTop;
        public ConstraintWidget biggest = null;
        public int biggestDimension = 0;
        public int mWidth = 0;
        public int mHeight = 0;
        public int mStartIndex = 0;
        public int mCount = 0;
        public int mNbMatchConstraintsWidgets = 0;

        public WidgetsList(int i, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i2) {
            this.mPaddingLeft = 0;
            this.mPaddingTop = 0;
            this.mPaddingRight = 0;
            this.mPaddingBottom = 0;
            this.mMax = 0;
            this.mOrientation = i;
            this.mLeft = constraintAnchor;
            this.mTop = constraintAnchor2;
            this.mRight = constraintAnchor3;
            this.mBottom = constraintAnchor4;
            Objects.requireNonNull(Flow.this);
            this.mPaddingLeft = Flow.this.mResolvedPaddingLeft;
            this.mPaddingTop = Flow.this.mPaddingTop;
            this.mPaddingRight = Flow.this.mResolvedPaddingRight;
            this.mPaddingBottom = Flow.this.mPaddingBottom;
            this.mMax = i2;
        }

        public final void add(ConstraintWidget constraintWidget) {
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            int i = 0;
            if (this.mOrientation == 0) {
                Objects.requireNonNull(constraintWidget);
                if (constraintWidget.mListDimensionBehaviors[0] == dimensionBehaviour) {
                    this.mNbMatchConstraintsWidgets++;
                }
                int widgetWidth = Flow.this.getWidgetWidth(constraintWidget, this.mMax);
                Flow flow = Flow.this;
                int i2 = flow.mHorizontalGap;
                if (constraintWidget.mVisibility != 8) {
                    i = i2;
                }
                this.mWidth = widgetWidth + i + this.mWidth;
                int widgetHeight = flow.getWidgetHeight(constraintWidget, this.mMax);
                if (this.biggest == null || this.biggestDimension < widgetHeight) {
                    this.biggest = constraintWidget;
                    this.biggestDimension = widgetHeight;
                    this.mHeight = widgetHeight;
                }
            } else {
                Objects.requireNonNull(constraintWidget);
                if (constraintWidget.mListDimensionBehaviors[1] == dimensionBehaviour) {
                    this.mNbMatchConstraintsWidgets++;
                }
                int widgetWidth2 = Flow.this.getWidgetWidth(constraintWidget, this.mMax);
                int widgetHeight2 = Flow.this.getWidgetHeight(constraintWidget, this.mMax);
                int i3 = Flow.this.mVerticalGap;
                if (constraintWidget.mVisibility != 8) {
                    i = i3;
                }
                this.mHeight = widgetHeight2 + i + this.mHeight;
                if (this.biggest == null || this.biggestDimension < widgetWidth2) {
                    this.biggest = constraintWidget;
                    this.biggestDimension = widgetWidth2;
                    this.mWidth = widgetWidth2;
                }
            }
            this.mCount++;
        }

        public final void createConstraints(boolean z, int i, boolean z2) {
            boolean z3;
            int i2;
            float f;
            ConstraintWidget constraintWidget;
            int i3;
            char c;
            int i4;
            float f2;
            int i5;
            int i6;
            int i7 = this.mCount;
            for (int i8 = 0; i8 < i7; i8++) {
                ConstraintWidget constraintWidget2 = Flow.this.mDisplayedWidgets[this.mStartIndex + i8];
                if (constraintWidget2 != null) {
                    constraintWidget2.resetAnchors();
                }
            }
            if (!(i7 == 0 || this.biggest == null)) {
                if (!z2 || i != 0) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                int i9 = -1;
                int i10 = -1;
                for (int i11 = 0; i11 < i7; i11++) {
                    if (z) {
                        i6 = (i7 - 1) - i11;
                    } else {
                        i6 = i11;
                    }
                    ConstraintWidget constraintWidget3 = Flow.this.mDisplayedWidgets[this.mStartIndex + i6];
                    Objects.requireNonNull(constraintWidget3);
                    if (constraintWidget3.mVisibility == 0) {
                        if (i9 == -1) {
                            i9 = i11;
                        }
                        i10 = i11;
                    }
                }
                ConstraintWidget constraintWidget4 = null;
                if (this.mOrientation == 0) {
                    ConstraintWidget constraintWidget5 = this.biggest;
                    int i12 = Flow.this.mVerticalStyle;
                    Objects.requireNonNull(constraintWidget5);
                    constraintWidget5.mVerticalChainStyle = i12;
                    int i13 = this.mPaddingTop;
                    if (i > 0) {
                        i13 += Flow.this.mVerticalGap;
                    }
                    constraintWidget5.mTop.connect(this.mTop, i13);
                    if (z2) {
                        constraintWidget5.mBottom.connect(this.mBottom, this.mPaddingBottom);
                    }
                    if (i > 0) {
                        this.mTop.mOwner.mBottom.connect(constraintWidget5.mTop, 0);
                    }
                    if (Flow.this.mVerticalAlign == 3 && !constraintWidget5.hasBaseline) {
                        for (int i14 = 0; i14 < i7; i14++) {
                            if (z) {
                                i5 = (i7 - 1) - i14;
                            } else {
                                i5 = i14;
                            }
                            constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + i5];
                            Objects.requireNonNull(constraintWidget);
                            if (constraintWidget.hasBaseline) {
                                break;
                            }
                        }
                    }
                    constraintWidget = constraintWidget5;
                    int i15 = 0;
                    while (i15 < i7) {
                        if (z) {
                            i3 = (i7 - 1) - i15;
                        } else {
                            i3 = i15;
                        }
                        ConstraintWidget constraintWidget6 = Flow.this.mDisplayedWidgets[this.mStartIndex + i3];
                        if (i15 == 0) {
                            constraintWidget6.connect(constraintWidget6.mLeft, this.mLeft, this.mPaddingLeft);
                        }
                        if (i3 == 0) {
                            Flow flow = Flow.this;
                            int i16 = flow.mHorizontalStyle;
                            float f3 = flow.mHorizontalBias;
                            if (this.mStartIndex != 0 || (i4 = flow.mFirstHorizontalStyle) == -1) {
                                if (z2 && (i4 = flow.mLastHorizontalStyle) != -1) {
                                    f2 = flow.mLastHorizontalBias;
                                }
                                Objects.requireNonNull(constraintWidget6);
                                constraintWidget6.mHorizontalChainStyle = i16;
                                constraintWidget6.mHorizontalBiasPercent = f3;
                            } else {
                                f2 = flow.mFirstHorizontalBias;
                            }
                            f3 = f2;
                            i16 = i4;
                            Objects.requireNonNull(constraintWidget6);
                            constraintWidget6.mHorizontalChainStyle = i16;
                            constraintWidget6.mHorizontalBiasPercent = f3;
                        }
                        if (i15 == i7 - 1) {
                            constraintWidget6.connect(constraintWidget6.mRight, this.mRight, this.mPaddingRight);
                        }
                        if (constraintWidget4 != null) {
                            constraintWidget6.mLeft.connect(constraintWidget4.mRight, Flow.this.mHorizontalGap);
                            if (i15 == i9) {
                                ConstraintAnchor constraintAnchor = constraintWidget6.mLeft;
                                int i17 = this.mPaddingLeft;
                                Objects.requireNonNull(constraintAnchor);
                                if (constraintAnchor.isConnected()) {
                                    constraintAnchor.mGoneMargin = i17;
                                }
                            }
                            constraintWidget4.mRight.connect(constraintWidget6.mLeft, 0);
                            if (i15 == i10 + 1) {
                                ConstraintAnchor constraintAnchor2 = constraintWidget4.mRight;
                                int i18 = this.mPaddingRight;
                                Objects.requireNonNull(constraintAnchor2);
                                if (constraintAnchor2.isConnected()) {
                                    constraintAnchor2.mGoneMargin = i18;
                                }
                            }
                        }
                        if (constraintWidget6 != constraintWidget5) {
                            c = 3;
                            if (Flow.this.mVerticalAlign == 3 && constraintWidget.hasBaseline && constraintWidget6 != constraintWidget) {
                                Objects.requireNonNull(constraintWidget6);
                                if (constraintWidget6.hasBaseline) {
                                    constraintWidget6.mBaseline.connect(constraintWidget.mBaseline, 0);
                                }
                            }
                            int i19 = Flow.this.mVerticalAlign;
                            if (i19 == 0) {
                                constraintWidget6.mTop.connect(constraintWidget5.mTop, 0);
                            } else if (i19 == 1) {
                                constraintWidget6.mBottom.connect(constraintWidget5.mBottom, 0);
                            } else if (z3) {
                                constraintWidget6.mTop.connect(this.mTop, this.mPaddingTop);
                                constraintWidget6.mBottom.connect(this.mBottom, this.mPaddingBottom);
                            } else {
                                constraintWidget6.mTop.connect(constraintWidget5.mTop, 0);
                                constraintWidget6.mBottom.connect(constraintWidget5.mBottom, 0);
                            }
                        } else {
                            c = 3;
                        }
                        i15++;
                        constraintWidget4 = constraintWidget6;
                    }
                    return;
                }
                ConstraintWidget constraintWidget7 = this.biggest;
                int i20 = Flow.this.mHorizontalStyle;
                Objects.requireNonNull(constraintWidget7);
                constraintWidget7.mHorizontalChainStyle = i20;
                int i21 = this.mPaddingLeft;
                if (i > 0) {
                    i21 += Flow.this.mHorizontalGap;
                }
                if (z) {
                    constraintWidget7.mRight.connect(this.mRight, i21);
                    if (z2) {
                        constraintWidget7.mLeft.connect(this.mLeft, this.mPaddingRight);
                    }
                    if (i > 0) {
                        this.mRight.mOwner.mLeft.connect(constraintWidget7.mRight, 0);
                    }
                } else {
                    constraintWidget7.mLeft.connect(this.mLeft, i21);
                    if (z2) {
                        constraintWidget7.mRight.connect(this.mRight, this.mPaddingRight);
                    }
                    if (i > 0) {
                        this.mLeft.mOwner.mRight.connect(constraintWidget7.mLeft, 0);
                    }
                }
                int i22 = 0;
                while (i22 < i7) {
                    ConstraintWidget constraintWidget8 = Flow.this.mDisplayedWidgets[this.mStartIndex + i22];
                    if (i22 == 0) {
                        constraintWidget8.connect(constraintWidget8.mTop, this.mTop, this.mPaddingTop);
                        Flow flow2 = Flow.this;
                        int i23 = flow2.mVerticalStyle;
                        float f4 = flow2.mVerticalBias;
                        if (this.mStartIndex != 0 || (i2 = flow2.mFirstVerticalStyle) == -1) {
                            if (z2 && (i2 = flow2.mLastVerticalStyle) != -1) {
                                f = flow2.mLastVerticalBias;
                            }
                            constraintWidget8.mVerticalChainStyle = i23;
                            constraintWidget8.mVerticalBiasPercent = f4;
                        } else {
                            f = flow2.mFirstVerticalBias;
                        }
                        f4 = f;
                        i23 = i2;
                        constraintWidget8.mVerticalChainStyle = i23;
                        constraintWidget8.mVerticalBiasPercent = f4;
                    }
                    if (i22 == i7 - 1) {
                        constraintWidget8.connect(constraintWidget8.mBottom, this.mBottom, this.mPaddingBottom);
                    }
                    if (constraintWidget4 != null) {
                        constraintWidget8.mTop.connect(constraintWidget4.mBottom, Flow.this.mVerticalGap);
                        if (i22 == i9) {
                            ConstraintAnchor constraintAnchor3 = constraintWidget8.mTop;
                            int i24 = this.mPaddingTop;
                            Objects.requireNonNull(constraintAnchor3);
                            if (constraintAnchor3.isConnected()) {
                                constraintAnchor3.mGoneMargin = i24;
                            }
                        }
                        constraintWidget4.mBottom.connect(constraintWidget8.mTop, 0);
                        if (i22 == i10 + 1) {
                            ConstraintAnchor constraintAnchor4 = constraintWidget4.mBottom;
                            int i25 = this.mPaddingBottom;
                            Objects.requireNonNull(constraintAnchor4);
                            if (constraintAnchor4.isConnected()) {
                                constraintAnchor4.mGoneMargin = i25;
                            }
                        }
                    }
                    if (constraintWidget8 != constraintWidget7) {
                        if (z) {
                            int i26 = Flow.this.mHorizontalAlign;
                            if (i26 == 0) {
                                constraintWidget8.mRight.connect(constraintWidget7.mRight, 0);
                            } else if (i26 == 1) {
                                constraintWidget8.mLeft.connect(constraintWidget7.mLeft, 0);
                            } else if (i26 == 2) {
                                constraintWidget8.mLeft.connect(constraintWidget7.mLeft, 0);
                                constraintWidget8.mRight.connect(constraintWidget7.mRight, 0);
                            }
                        } else {
                            int i27 = Flow.this.mHorizontalAlign;
                            if (i27 == 0) {
                                constraintWidget8.mLeft.connect(constraintWidget7.mLeft, 0);
                            } else if (i27 == 1) {
                                constraintWidget8.mRight.connect(constraintWidget7.mRight, 0);
                            } else if (i27 == 2) {
                                if (z3) {
                                    constraintWidget8.mLeft.connect(this.mLeft, this.mPaddingLeft);
                                    constraintWidget8.mRight.connect(this.mRight, this.mPaddingRight);
                                } else {
                                    constraintWidget8.mLeft.connect(constraintWidget7.mLeft, 0);
                                    constraintWidget8.mRight.connect(constraintWidget7.mRight, 0);
                                }
                            }
                            i22++;
                            constraintWidget4 = constraintWidget8;
                        }
                    }
                    i22++;
                    constraintWidget4 = constraintWidget8;
                }
            }
        }

        public final int getHeight() {
            if (this.mOrientation == 1) {
                return this.mHeight - Flow.this.mVerticalGap;
            }
            return this.mHeight;
        }

        public final int getWidth() {
            if (this.mOrientation == 0) {
                return this.mWidth - Flow.this.mHorizontalGap;
            }
            return this.mWidth;
        }

        public final void measureMatchConstraints(int i) {
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            int i2 = this.mNbMatchConstraintsWidgets;
            if (i2 != 0) {
                int i3 = this.mCount;
                int i4 = i / i2;
                for (int i5 = 0; i5 < i3; i5++) {
                    Flow flow = Flow.this;
                    ConstraintWidget constraintWidget = flow.mDisplayedWidgets[this.mStartIndex + i5];
                    if (this.mOrientation == 0) {
                        if (constraintWidget != null) {
                            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.mListDimensionBehaviors;
                            if (dimensionBehaviourArr[0] == dimensionBehaviour2) {
                                flow.measure(constraintWidget, dimensionBehaviour, i4, dimensionBehaviourArr[1], constraintWidget.getHeight());
                            }
                        }
                    } else if (constraintWidget != null) {
                        ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr2 = constraintWidget.mListDimensionBehaviors;
                        if (dimensionBehaviourArr2[1] == dimensionBehaviour2) {
                            flow.measure(constraintWidget, dimensionBehaviourArr2[0], constraintWidget.getWidth(), dimensionBehaviour, i4);
                        }
                    }
                }
                this.mWidth = 0;
                this.mHeight = 0;
                this.biggest = null;
                this.biggestDimension = 0;
                int i6 = this.mCount;
                for (int i7 = 0; i7 < i6; i7++) {
                    Flow flow2 = Flow.this;
                    ConstraintWidget constraintWidget2 = flow2.mDisplayedWidgets[this.mStartIndex + i7];
                    if (this.mOrientation == 0) {
                        int width = constraintWidget2.getWidth();
                        Flow flow3 = Flow.this;
                        int i8 = flow3.mHorizontalGap;
                        if (constraintWidget2.mVisibility == 8) {
                            i8 = 0;
                        }
                        this.mWidth = width + i8 + this.mWidth;
                        int widgetHeight = flow3.getWidgetHeight(constraintWidget2, this.mMax);
                        if (this.biggest == null || this.biggestDimension < widgetHeight) {
                            this.biggest = constraintWidget2;
                            this.biggestDimension = widgetHeight;
                            this.mHeight = widgetHeight;
                        }
                    } else {
                        int widgetWidth = flow2.getWidgetWidth(constraintWidget2, this.mMax);
                        int widgetHeight2 = Flow.this.getWidgetHeight(constraintWidget2, this.mMax);
                        int i9 = Flow.this.mVerticalGap;
                        Objects.requireNonNull(constraintWidget2);
                        if (constraintWidget2.mVisibility == 8) {
                            i9 = 0;
                        }
                        this.mHeight = widgetHeight2 + i9 + this.mHeight;
                        if (this.biggest == null || this.biggestDimension < widgetWidth) {
                            this.biggest = constraintWidget2;
                            this.biggestDimension = widgetWidth;
                            this.mWidth = widgetWidth;
                        }
                    }
                }
            }
        }

        public final void setup(int i, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, ConstraintAnchor constraintAnchor3, ConstraintAnchor constraintAnchor4, int i2, int i3, int i4, int i5, int i6) {
            this.mOrientation = i;
            this.mLeft = constraintAnchor;
            this.mTop = constraintAnchor2;
            this.mRight = constraintAnchor3;
            this.mBottom = constraintAnchor4;
            this.mPaddingLeft = i2;
            this.mPaddingTop = i3;
            this.mPaddingRight = i4;
            this.mPaddingBottom = i5;
            this.mMax = i6;
        }
    }

    public final int getWidgetHeight(ConstraintWidget constraintWidget, int i) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int i2 = constraintWidget.mMatchConstraintDefaultHeight;
            if (i2 == 0) {
                return 0;
            }
            if (i2 == 2) {
                int i3 = (int) (constraintWidget.mMatchConstraintPercentHeight * i);
                if (i3 != constraintWidget.getHeight()) {
                    measure(constraintWidget, constraintWidget.mListDimensionBehaviors[0], constraintWidget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, i3);
                }
                return i3;
            }
        }
        return constraintWidget.getHeight();
    }

    public final int getWidgetWidth(ConstraintWidget constraintWidget, int i) {
        if (constraintWidget == null) {
            return 0;
        }
        if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            int i2 = constraintWidget.mMatchConstraintDefaultWidth;
            if (i2 == 0) {
                return 0;
            }
            if (i2 == 2) {
                int i3 = (int) (constraintWidget.mMatchConstraintPercentWidth * i);
                if (i3 != constraintWidget.getWidth()) {
                    measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i3, constraintWidget.mListDimensionBehaviors[1], constraintWidget.getHeight());
                }
                return i3;
            }
        }
        return constraintWidget.getWidth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:120:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x05f6  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0604  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0620  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0622  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:184:0x029a -> B:185:0x02a9). Please submit an issue!!! */
    @Override // androidx.constraintlayout.solver.widgets.VirtualLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void measure(int r34, int r35, int r36, int r37) {
        /*
            Method dump skipped, instructions count: 1574
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Flow.measure(int, int, int, int):void");
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void addToSolver(LinearSystem linearSystem) {
        boolean z;
        boolean z2;
        ConstraintWidget constraintWidget;
        int i;
        super.addToSolver(linearSystem);
        ConstraintWidget constraintWidget2 = this.mParent;
        if (constraintWidget2 != null) {
            ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) constraintWidget2;
            Objects.requireNonNull(constraintWidgetContainer);
            z = constraintWidgetContainer.mIsRtl;
        } else {
            z = false;
        }
        int i2 = this.mWrapMode;
        if (i2 != 0) {
            if (i2 == 1) {
                int size = this.mChainList.size();
                for (int i3 = 0; i3 < size; i3++) {
                    WidgetsList widgetsList = this.mChainList.get(i3);
                    if (i3 == size - 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    widgetsList.createConstraints(z, i3, z2);
                }
            } else if (!(i2 != 2 || this.mAlignedDimensions == null || this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInRows == null)) {
                for (int i4 = 0; i4 < this.mDisplayedWidgetsCount; i4++) {
                    this.mDisplayedWidgets[i4].resetAnchors();
                }
                int[] iArr = this.mAlignedDimensions;
                int i5 = iArr[0];
                int i6 = iArr[1];
                ConstraintWidget constraintWidget3 = null;
                for (int i7 = 0; i7 < i5; i7++) {
                    if (z) {
                        i = (i5 - i7) - 1;
                    } else {
                        i = i7;
                    }
                    ConstraintWidget constraintWidget4 = this.mAlignedBiggestElementsInCols[i];
                    if (!(constraintWidget4 == null || constraintWidget4.mVisibility == 8)) {
                        if (i7 == 0) {
                            constraintWidget4.connect(constraintWidget4.mLeft, this.mLeft, this.mResolvedPaddingLeft);
                            constraintWidget4.mHorizontalChainStyle = this.mHorizontalStyle;
                            constraintWidget4.mHorizontalBiasPercent = this.mHorizontalBias;
                        }
                        if (i7 == i5 - 1) {
                            constraintWidget4.connect(constraintWidget4.mRight, this.mRight, this.mResolvedPaddingRight);
                        }
                        if (i7 > 0) {
                            constraintWidget4.connect(constraintWidget4.mLeft, constraintWidget3.mRight, this.mHorizontalGap);
                            constraintWidget3.connect(constraintWidget3.mRight, constraintWidget4.mLeft, 0);
                        }
                        constraintWidget3 = constraintWidget4;
                    }
                }
                for (int i8 = 0; i8 < i6; i8++) {
                    ConstraintWidget constraintWidget5 = this.mAlignedBiggestElementsInRows[i8];
                    if (!(constraintWidget5 == null || constraintWidget5.mVisibility == 8)) {
                        if (i8 == 0) {
                            constraintWidget5.connect(constraintWidget5.mTop, this.mTop, this.mPaddingTop);
                            constraintWidget5.mVerticalChainStyle = this.mVerticalStyle;
                            constraintWidget5.mVerticalBiasPercent = this.mVerticalBias;
                        }
                        if (i8 == i6 - 1) {
                            constraintWidget5.connect(constraintWidget5.mBottom, this.mBottom, this.mPaddingBottom);
                        }
                        if (i8 > 0) {
                            constraintWidget5.connect(constraintWidget5.mTop, constraintWidget3.mBottom, this.mVerticalGap);
                            constraintWidget3.connect(constraintWidget3.mBottom, constraintWidget5.mTop, 0);
                        }
                        constraintWidget3 = constraintWidget5;
                    }
                }
                for (int i9 = 0; i9 < i5; i9++) {
                    for (int i10 = 0; i10 < i6; i10++) {
                        int i11 = (i10 * i5) + i9;
                        if (this.mOrientation == 1) {
                            i11 = (i9 * i6) + i10;
                        }
                        ConstraintWidget[] constraintWidgetArr = this.mDisplayedWidgets;
                        if (!(i11 >= constraintWidgetArr.length || (constraintWidget = constraintWidgetArr[i11]) == null || constraintWidget.mVisibility == 8)) {
                            ConstraintWidget constraintWidget6 = this.mAlignedBiggestElementsInCols[i9];
                            ConstraintWidget constraintWidget7 = this.mAlignedBiggestElementsInRows[i10];
                            if (constraintWidget != constraintWidget6) {
                                constraintWidget.connect(constraintWidget.mLeft, constraintWidget6.mLeft, 0);
                                constraintWidget.connect(constraintWidget.mRight, constraintWidget6.mRight, 0);
                            }
                            if (constraintWidget != constraintWidget7) {
                                constraintWidget.connect(constraintWidget.mTop, constraintWidget7.mTop, 0);
                                constraintWidget.connect(constraintWidget.mBottom, constraintWidget7.mBottom, 0);
                            }
                        }
                    }
                }
            }
        } else if (this.mChainList.size() > 0) {
            this.mChainList.get(0).createConstraints(z, 0, true);
        }
        this.mNeedsCallFromSolver = false;
    }

    @Override // androidx.constraintlayout.solver.widgets.HelperWidget, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public final void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(constraintWidget, hashMap);
        Flow flow = (Flow) constraintWidget;
        this.mHorizontalStyle = flow.mHorizontalStyle;
        this.mVerticalStyle = flow.mVerticalStyle;
        this.mFirstHorizontalStyle = flow.mFirstHorizontalStyle;
        this.mFirstVerticalStyle = flow.mFirstVerticalStyle;
        this.mLastHorizontalStyle = flow.mLastHorizontalStyle;
        this.mLastVerticalStyle = flow.mLastVerticalStyle;
        this.mHorizontalBias = flow.mHorizontalBias;
        this.mVerticalBias = flow.mVerticalBias;
        this.mFirstHorizontalBias = flow.mFirstHorizontalBias;
        this.mFirstVerticalBias = flow.mFirstVerticalBias;
        this.mLastHorizontalBias = flow.mLastHorizontalBias;
        this.mLastVerticalBias = flow.mLastVerticalBias;
        this.mHorizontalGap = flow.mHorizontalGap;
        this.mVerticalGap = flow.mVerticalGap;
        this.mHorizontalAlign = flow.mHorizontalAlign;
        this.mVerticalAlign = flow.mVerticalAlign;
        this.mWrapMode = flow.mWrapMode;
        this.mMaxElementsWrap = flow.mMaxElementsWrap;
        this.mOrientation = flow.mOrientation;
    }
}
