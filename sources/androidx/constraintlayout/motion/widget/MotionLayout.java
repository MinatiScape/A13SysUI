package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.utils.StopLogic;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Flow;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.StateSet;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.widget.NestedScrollView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
    public static boolean IS_IN_EDIT_MODE;
    public DevModeDraw mDevModeDraw;
    public int mEndWrapHeight;
    public int mEndWrapWidth;
    public int mHeightMeasureMode;
    public Interpolator mInterpolator;
    public int mLastLayoutHeight;
    public int mLastLayoutWidth;
    public float mPostInterpolationPosition;
    public MotionScene mScene;
    public View mScrollTarget;
    public float mScrollTargetDT;
    public float mScrollTargetDX;
    public float mScrollTargetDY;
    public long mScrollTargetTime;
    public int mStartWrapHeight;
    public int mStartWrapWidth;
    public boolean mTransitionInstantly;
    public long mTransitionLastTime;
    public int mWidthMeasureMode;
    public float mLastVelocity = 0.0f;
    public int mBeginState = -1;
    public int mCurrentState = -1;
    public int mEndState = -1;
    public int mLastWidthMeasureSpec = 0;
    public int mLastHeightMeasureSpec = 0;
    public boolean mInteractionEnabled = true;
    public HashMap<View, MotionController> mFrameArrayList = new HashMap<>();
    public long mAnimationStartTime = 0;
    public float mTransitionDuration = 1.0f;
    public float mTransitionPosition = 0.0f;
    public float mTransitionLastPosition = 0.0f;
    public float mTransitionGoalPosition = 0.0f;
    public boolean mInTransition = false;
    public int mDebugPath = 0;
    public boolean mTemporalInterpolator = false;
    public StopLogic mStopLogic = new StopLogic();
    public DecelerateInterpolator mDecelerateLogic = new DecelerateInterpolator();
    public boolean mUndergoingMotion = false;
    public boolean mKeepAnimating = false;
    public ArrayList<MotionHelper> mOnShowHelpers = null;
    public ArrayList<MotionHelper> mOnHideHelpers = null;
    public ArrayList<TransitionListener> mTransitionListeners = null;
    public int mFrames = 0;
    public long mLastDrawTime = -1;
    public float mLastFps = 0.0f;
    public int mListenerState = 0;
    public float mListenerPosition = 0.0f;
    public boolean mIsAnimating = false;
    public boolean mMeasureDuringTransition = false;
    public KeyCache mKeyCache = new KeyCache();
    public Model mModel = new Model();
    public boolean mNeedsFireTransitionCompleted = false;
    public RectF mBoundsCheck = new RectF();
    public View mRegionView = null;
    public ArrayList<Integer> mTransitionCompleted = new ArrayList<>();

    /* loaded from: classes.dex */
    public class DecelerateInterpolator extends MotionInterpolator {
        public float maxA;
        public float initalV = 0.0f;
        public float currentP = 0.0f;

        public DecelerateInterpolator() {
        }

        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float f2 = this.initalV;
            if (f2 > 0.0f) {
                float f3 = this.maxA;
                if (f2 / f3 < f) {
                    f = f2 / f3;
                }
                MotionLayout.this.mLastVelocity = f2 - (f3 * f);
                return ((f2 * f) - (((f3 * f) * f) / 2.0f)) + this.currentP;
            }
            float f4 = this.maxA;
            if ((-f2) / f4 < f) {
                f = (-f2) / f4;
            }
            MotionLayout.this.mLastVelocity = (f4 * f) + f2;
            return (((f4 * f) * f) / 2.0f) + (f2 * f) + this.currentP;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionInterpolator
        public final float getVelocity$1() {
            return MotionLayout.this.mLastVelocity;
        }
    }

    /* loaded from: classes.dex */
    public class DevModeDraw {
        public Paint mFillPaint;
        public int mKeyFrameCount;
        public Paint mPaint;
        public Paint mPaintGraph;
        public Paint mPaintKeyframes;
        public Path mPath;
        public float[] mPoints;
        public Paint mTextPaint;
        public Rect mBounds = new Rect();
        public int mShadowTranslate = 1;
        public float[] mRectangle = new float[8];
        public float[] mKeyFramePoints = new float[100];
        public int[] mPathMode = new int[50];

        public final void drawPathRelativeTicks(Canvas canvas, float f, float f2) {
            float[] fArr = this.mPoints;
            float f3 = fArr[0];
            float f4 = fArr[1];
            float f5 = fArr[fArr.length - 2];
            float f6 = fArr[fArr.length - 1];
            float hypot = (float) Math.hypot(f3 - f5, f4 - f6);
            float f7 = f5 - f3;
            float f8 = f6 - f4;
            float f9 = (((f2 - f4) * f8) + ((f - f3) * f7)) / (hypot * hypot);
            float f10 = f3 + (f7 * f9);
            float f11 = f4 + (f9 * f8);
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f10, f11);
            float hypot2 = (float) Math.hypot(f10 - f, f11 - f2);
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("");
            m.append(((int) ((hypot2 * 100.0f) / hypot)) / 100.0f);
            String sb = m.toString();
            getTextBounds(sb, this.mTextPaint);
            canvas.drawTextOnPath(sb, path, (hypot2 / 2.0f) - (this.mBounds.width() / 2), -20.0f, this.mTextPaint);
            canvas.drawLine(f, f2, f10, f11, this.mPaintGraph);
        }

        public final void drawPathScreenTicks(Canvas canvas, float f, float f2, int i, int i2) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("");
            m.append(((int) ((((f - (i / 2)) * 100.0f) / (MotionLayout.this.getWidth() - i)) + 0.5d)) / 100.0f);
            String sb = m.toString();
            getTextBounds(sb, this.mTextPaint);
            canvas.drawText(sb, ((f / 2.0f) - (this.mBounds.width() / 2)) + 0.0f, f2 - 20.0f, this.mTextPaint);
            canvas.drawLine(f, f2, Math.min(0.0f, 1.0f), f2, this.mPaintGraph);
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("");
            m2.append(((int) ((((f2 - (i2 / 2)) * 100.0f) / (MotionLayout.this.getHeight() - i2)) + 0.5d)) / 100.0f);
            String sb2 = m2.toString();
            getTextBounds(sb2, this.mTextPaint);
            canvas.drawText(sb2, f + 5.0f, 0.0f - ((f2 / 2.0f) - (this.mBounds.height() / 2)), this.mTextPaint);
            canvas.drawLine(f, f2, f, Math.max(0.0f, 1.0f), this.mPaintGraph);
        }

        public DevModeDraw() {
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(true);
            this.mPaint.setColor(-21965);
            this.mPaint.setStrokeWidth(2.0f);
            this.mPaint.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint();
            this.mPaintKeyframes = paint2;
            paint2.setAntiAlias(true);
            this.mPaintKeyframes.setColor(-2067046);
            this.mPaintKeyframes.setStrokeWidth(2.0f);
            this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
            Paint paint3 = new Paint();
            this.mPaintGraph = paint3;
            paint3.setAntiAlias(true);
            this.mPaintGraph.setColor(-13391360);
            this.mPaintGraph.setStrokeWidth(2.0f);
            this.mPaintGraph.setStyle(Paint.Style.STROKE);
            Paint paint4 = new Paint();
            this.mTextPaint = paint4;
            paint4.setAntiAlias(true);
            this.mTextPaint.setColor(-13391360);
            this.mTextPaint.setTextSize(MotionLayout.this.getContext().getResources().getDisplayMetrics().density * 12.0f);
            Paint paint5 = new Paint();
            this.mFillPaint = paint5;
            paint5.setAntiAlias(true);
            this.mPaintGraph.setPathEffect(new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f));
        }

        public final void drawAll(Canvas canvas, int i, int i2, MotionController motionController) {
            int i3;
            int i4;
            int i5;
            float f;
            float f2;
            if (i == 4) {
                boolean z = false;
                boolean z2 = false;
                for (int i6 = 0; i6 < this.mKeyFrameCount; i6++) {
                    int[] iArr = this.mPathMode;
                    if (iArr[i6] == 1) {
                        z = true;
                    }
                    if (iArr[i6] == 2) {
                        z2 = true;
                    }
                }
                if (z) {
                    float[] fArr = this.mPoints;
                    canvas.drawLine(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1], this.mPaintGraph);
                }
                if (z2) {
                    drawPathCartesian(canvas);
                }
            }
            if (i == 2) {
                float[] fArr2 = this.mPoints;
                canvas.drawLine(fArr2[0], fArr2[1], fArr2[fArr2.length - 2], fArr2[fArr2.length - 1], this.mPaintGraph);
            }
            if (i == 3) {
                drawPathCartesian(canvas);
            }
            canvas.drawLines(this.mPoints, this.mPaint);
            View view = motionController.mView;
            if (view != null) {
                i4 = view.getWidth();
                i3 = motionController.mView.getHeight();
            } else {
                i4 = 0;
                i3 = 0;
            }
            int i7 = 1;
            while (i7 < i2 - 1) {
                if (i == 4 && this.mPathMode[i7 - 1] == 0) {
                    i5 = i7;
                } else {
                    float[] fArr3 = this.mKeyFramePoints;
                    int i8 = i7 * 2;
                    float f3 = fArr3[i8];
                    float f4 = fArr3[i8 + 1];
                    this.mPath.reset();
                    this.mPath.moveTo(f3, f4 + 10.0f);
                    this.mPath.lineTo(f3 + 10.0f, f4);
                    this.mPath.lineTo(f3, f4 - 10.0f);
                    this.mPath.lineTo(f3 - 10.0f, f4);
                    this.mPath.close();
                    int i9 = i7 - 1;
                    motionController.mMotionPaths.get(i9);
                    if (i == 4) {
                        int[] iArr2 = this.mPathMode;
                        if (iArr2[i9] == 1) {
                            drawPathRelativeTicks(canvas, f3 - 0.0f, f4 - 0.0f);
                        } else if (iArr2[i9] == 2) {
                            drawPathCartesianTicks(canvas, f3 - 0.0f, f4 - 0.0f);
                        } else if (iArr2[i9] == 3) {
                            f = f4;
                            f2 = f3;
                            i5 = i7;
                            drawPathScreenTicks(canvas, f3 - 0.0f, f4 - 0.0f, i4, i3);
                            canvas.drawPath(this.mPath, this.mFillPaint);
                        }
                        f = f4;
                        f2 = f3;
                        i5 = i7;
                        canvas.drawPath(this.mPath, this.mFillPaint);
                    } else {
                        f = f4;
                        f2 = f3;
                        i5 = i7;
                    }
                    if (i == 2) {
                        drawPathRelativeTicks(canvas, f2 - 0.0f, f - 0.0f);
                    }
                    if (i == 3) {
                        drawPathCartesianTicks(canvas, f2 - 0.0f, f - 0.0f);
                    }
                    if (i == 6) {
                        drawPathScreenTicks(canvas, f2 - 0.0f, f - 0.0f, i4, i3);
                    }
                    canvas.drawPath(this.mPath, this.mFillPaint);
                }
                i7 = i5 + 1;
            }
            float[] fArr4 = this.mPoints;
            if (fArr4.length > 1) {
                canvas.drawCircle(fArr4[0], fArr4[1], 8.0f, this.mPaintKeyframes);
                float[] fArr5 = this.mPoints;
                canvas.drawCircle(fArr5[fArr5.length - 2], fArr5[fArr5.length - 1], 8.0f, this.mPaintKeyframes);
            }
        }

        public final void drawPathCartesian(Canvas canvas) {
            float[] fArr = this.mPoints;
            float f = fArr[0];
            float f2 = fArr[1];
            float f3 = fArr[fArr.length - 2];
            float f4 = fArr[fArr.length - 1];
            canvas.drawLine(Math.min(f, f3), Math.max(f2, f4), Math.max(f, f3), Math.max(f2, f4), this.mPaintGraph);
            canvas.drawLine(Math.min(f, f3), Math.min(f2, f4), Math.min(f, f3), Math.max(f2, f4), this.mPaintGraph);
        }

        public final void drawPathCartesianTicks(Canvas canvas, float f, float f2) {
            float[] fArr = this.mPoints;
            float f3 = fArr[0];
            float f4 = fArr[1];
            float f5 = fArr[fArr.length - 2];
            float f6 = fArr[fArr.length - 1];
            float min = Math.min(f3, f5);
            float max = Math.max(f4, f6);
            float min2 = f - Math.min(f3, f5);
            float max2 = Math.max(f4, f6) - f2;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("");
            m.append(((int) (((min2 * 100.0f) / Math.abs(f5 - f3)) + 0.5d)) / 100.0f);
            String sb = m.toString();
            getTextBounds(sb, this.mTextPaint);
            canvas.drawText(sb, ((min2 / 2.0f) - (this.mBounds.width() / 2)) + min, f2 - 20.0f, this.mTextPaint);
            canvas.drawLine(f, f2, Math.min(f3, f5), f2, this.mPaintGraph);
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("");
            m2.append(((int) (((max2 * 100.0f) / Math.abs(f6 - f4)) + 0.5d)) / 100.0f);
            String sb2 = m2.toString();
            getTextBounds(sb2, this.mTextPaint);
            canvas.drawText(sb2, f + 5.0f, max - ((max2 / 2.0f) - (this.mBounds.height() / 2)), this.mTextPaint);
            canvas.drawLine(f, f2, f, Math.max(f4, f6), this.mPaintGraph);
        }

        public final void getTextBounds(String str, Paint paint) {
            paint.getTextBounds(str, 0, str.length(), this.mBounds);
        }
    }

    /* loaded from: classes.dex */
    public class Model {
        public int mEndId;
        public int mStartId;
        public ConstraintWidgetContainer mLayoutStart = new ConstraintWidgetContainer();
        public ConstraintWidgetContainer mLayoutEnd = new ConstraintWidgetContainer();
        public ConstraintSet mStart = null;
        public ConstraintSet mEnd = null;

        public Model() {
        }

        public final void build() {
            int childCount = MotionLayout.this.getChildCount();
            MotionLayout.this.mFrameArrayList.clear();
            for (int i = 0; i < childCount; i++) {
                View childAt = MotionLayout.this.getChildAt(i);
                MotionLayout.this.mFrameArrayList.put(childAt, new MotionController(childAt));
            }
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt2 = MotionLayout.this.getChildAt(i2);
                MotionController motionController = MotionLayout.this.mFrameArrayList.get(childAt2);
                if (motionController != null) {
                    if (this.mStart != null) {
                        ConstraintWidget widget = getWidget(this.mLayoutStart, childAt2);
                        if (widget != null) {
                            ConstraintSet constraintSet = this.mStart;
                            MotionPaths motionPaths = motionController.mStartMotionPath;
                            motionPaths.time = 0.0f;
                            motionPaths.position = 0.0f;
                            motionController.readView(motionPaths);
                            motionController.mStartMotionPath.setBounds(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
                            int i3 = motionController.mId;
                            Objects.requireNonNull(constraintSet);
                            ConstraintSet.Constraint constraint = constraintSet.get(i3);
                            motionController.mStartMotionPath.applyParameters(constraint);
                            motionController.mMotionStagger = constraint.motion.mMotionStagger;
                            motionController.mStartPoint.setState(widget, constraintSet, motionController.mId);
                        } else {
                            Log.e("MotionLayout", Debug.getLocation() + "no widget for  " + Debug.getName(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                    if (this.mEnd != null) {
                        ConstraintWidget widget2 = getWidget(this.mLayoutEnd, childAt2);
                        if (widget2 != null) {
                            ConstraintSet constraintSet2 = this.mEnd;
                            MotionPaths motionPaths2 = motionController.mEndMotionPath;
                            motionPaths2.time = 1.0f;
                            motionPaths2.position = 1.0f;
                            motionController.readView(motionPaths2);
                            motionController.mEndMotionPath.setBounds(widget2.getX(), widget2.getY(), widget2.getWidth(), widget2.getHeight());
                            MotionPaths motionPaths3 = motionController.mEndMotionPath;
                            int i4 = motionController.mId;
                            Objects.requireNonNull(constraintSet2);
                            motionPaths3.applyParameters(constraintSet2.get(i4));
                            motionController.mEndPoint.setState(widget2, constraintSet2, motionController.mId);
                        } else {
                            Log.e("MotionLayout", Debug.getLocation() + "no widget for  " + Debug.getName(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                }
            }
        }

        public final void initFrom(ConstraintSet constraintSet, ConstraintSet constraintSet2) {
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            this.mStart = constraintSet;
            this.mEnd = constraintSet2;
            ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutStart;
            MotionLayout motionLayout = MotionLayout.this;
            boolean z = MotionLayout.IS_IN_EDIT_MODE;
            ConstraintWidgetContainer constraintWidgetContainer2 = motionLayout.mLayoutWidget;
            Objects.requireNonNull(constraintWidgetContainer2);
            BasicMeasure.Measurer measurer = constraintWidgetContainer2.mMeasurer;
            Objects.requireNonNull(constraintWidgetContainer);
            constraintWidgetContainer.mMeasurer = measurer;
            DependencyGraph dependencyGraph = constraintWidgetContainer.mDependencyGraph;
            Objects.requireNonNull(dependencyGraph);
            dependencyGraph.mMeasurer = measurer;
            ConstraintWidgetContainer constraintWidgetContainer3 = this.mLayoutEnd;
            ConstraintWidgetContainer constraintWidgetContainer4 = MotionLayout.this.mLayoutWidget;
            Objects.requireNonNull(constraintWidgetContainer4);
            BasicMeasure.Measurer measurer2 = constraintWidgetContainer4.mMeasurer;
            Objects.requireNonNull(constraintWidgetContainer3);
            constraintWidgetContainer3.mMeasurer = measurer2;
            DependencyGraph dependencyGraph2 = constraintWidgetContainer3.mDependencyGraph;
            Objects.requireNonNull(dependencyGraph2);
            dependencyGraph2.mMeasurer = measurer2;
            ConstraintWidgetContainer constraintWidgetContainer5 = this.mLayoutStart;
            Objects.requireNonNull(constraintWidgetContainer5);
            constraintWidgetContainer5.mChildren.clear();
            ConstraintWidgetContainer constraintWidgetContainer6 = this.mLayoutEnd;
            Objects.requireNonNull(constraintWidgetContainer6);
            constraintWidgetContainer6.mChildren.clear();
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
            if (constraintSet != null) {
                setupConstraintWidget(this.mLayoutStart, constraintSet);
            }
            setupConstraintWidget(this.mLayoutEnd, constraintSet2);
            ConstraintWidgetContainer constraintWidgetContainer7 = this.mLayoutStart;
            boolean isRtl = MotionLayout.this.isRtl();
            Objects.requireNonNull(constraintWidgetContainer7);
            constraintWidgetContainer7.mIsRtl = isRtl;
            this.mLayoutStart.updateHierarchy();
            ConstraintWidgetContainer constraintWidgetContainer8 = this.mLayoutEnd;
            boolean isRtl2 = MotionLayout.this.isRtl();
            Objects.requireNonNull(constraintWidgetContainer8);
            constraintWidgetContainer8.mIsRtl = isRtl2;
            this.mLayoutEnd.updateHierarchy();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    this.mLayoutStart.setHorizontalDimensionBehaviour(dimensionBehaviour);
                    this.mLayoutEnd.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (layoutParams.height == -2) {
                    this.mLayoutStart.setVerticalDimensionBehaviour(dimensionBehaviour);
                    this.mLayoutEnd.setVerticalDimensionBehaviour(dimensionBehaviour);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x00c1  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00f5  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00f8  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00fb  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0114  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x013e  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x0143  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0219 A[ORIG_RETURN, RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void reEvaluateState() {
            /*
                Method dump skipped, instructions count: 538
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.Model.reEvaluateState():void");
        }

        public final void setupConstraintWidget(ConstraintWidgetContainer constraintWidgetContainer, ConstraintSet constraintSet) {
            SparseArray<ConstraintWidget> sparseArray = new SparseArray<>();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            sparseArray.clear();
            sparseArray.put(0, constraintWidgetContainer);
            sparseArray.put(MotionLayout.this.getId(), constraintWidgetContainer);
            Objects.requireNonNull(constraintWidgetContainer);
            Iterator<ConstraintWidget> it = constraintWidgetContainer.mChildren.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                Objects.requireNonNull(next);
                sparseArray.put(((View) next.mCompanionWidget).getId(), next);
            }
            Iterator<ConstraintWidget> it2 = constraintWidgetContainer.mChildren.iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                Objects.requireNonNull(next2);
                View view = (View) next2.mCompanionWidget;
                int id = view.getId();
                Objects.requireNonNull(constraintSet);
                if (constraintSet.mConstraints.containsKey(Integer.valueOf(id))) {
                    constraintSet.mConstraints.get(Integer.valueOf(id)).applyTo(layoutParams);
                }
                next2.setWidth(constraintSet.get(view.getId()).layout.mWidth);
                next2.setHeight(constraintSet.get(view.getId()).layout.mHeight);
                if (view instanceof ConstraintHelper) {
                    ConstraintHelper constraintHelper = (ConstraintHelper) view;
                    int id2 = constraintHelper.getId();
                    if (constraintSet.mConstraints.containsKey(Integer.valueOf(id2))) {
                        ConstraintSet.Constraint constraint = constraintSet.mConstraints.get(Integer.valueOf(id2));
                        if (next2 instanceof HelperWidget) {
                            constraintHelper.loadParameters(constraint, (HelperWidget) next2, layoutParams, sparseArray);
                        }
                    }
                    if (view instanceof Barrier) {
                        ((Barrier) view).validateParams();
                    }
                }
                layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                MotionLayout motionLayout = MotionLayout.this;
                boolean z = MotionLayout.IS_IN_EDIT_MODE;
                motionLayout.applyConstraintsFromLayoutParams(false, view, next2, layoutParams, sparseArray);
                if (constraintSet.get(view.getId()).propertySet.mVisibilityMode == 1) {
                    next2.mVisibility = view.getVisibility();
                } else {
                    next2.mVisibility = constraintSet.get(view.getId()).propertySet.visibility;
                }
            }
            Iterator<ConstraintWidget> it3 = constraintWidgetContainer.mChildren.iterator();
            while (it3.hasNext()) {
                ConstraintWidget next3 = it3.next();
                if (next3 instanceof Helper) {
                    Helper helper = (Helper) next3;
                    helper.removeAllIds();
                    Objects.requireNonNull(next3);
                    ConstraintHelper constraintHelper2 = (ConstraintHelper) next3.mCompanionWidget;
                    Objects.requireNonNull(constraintHelper2);
                    helper.removeAllIds();
                    for (int i = 0; i < constraintHelper2.mCount; i++) {
                        helper.add(sparseArray.get(constraintHelper2.mIds[i]));
                    }
                    if (helper instanceof VirtualLayout) {
                        VirtualLayout virtualLayout = (VirtualLayout) helper;
                        for (int i2 = 0; i2 < virtualLayout.mWidgetsCount; i2++) {
                            ConstraintWidget constraintWidget = virtualLayout.mWidgets[i2];
                        }
                    }
                }
            }
        }

        public static void copy(ConstraintWidgetContainer constraintWidgetContainer, ConstraintWidgetContainer constraintWidgetContainer2) {
            ConstraintWidget constraintWidget;
            Objects.requireNonNull(constraintWidgetContainer);
            ArrayList<ConstraintWidget> arrayList = constraintWidgetContainer.mChildren;
            HashMap<ConstraintWidget, ConstraintWidget> hashMap = new HashMap<>();
            hashMap.put(constraintWidgetContainer, constraintWidgetContainer2);
            Objects.requireNonNull(constraintWidgetContainer2);
            constraintWidgetContainer2.mChildren.clear();
            constraintWidgetContainer2.copy(constraintWidgetContainer, hashMap);
            Iterator<ConstraintWidget> it = arrayList.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                if (next instanceof androidx.constraintlayout.solver.widgets.Barrier) {
                    constraintWidget = new androidx.constraintlayout.solver.widgets.Barrier();
                } else if (next instanceof Guideline) {
                    constraintWidget = new Guideline();
                } else if (next instanceof Flow) {
                    constraintWidget = new Flow();
                } else if (next instanceof Helper) {
                    constraintWidget = new HelperWidget();
                } else {
                    constraintWidget = new ConstraintWidget();
                }
                constraintWidgetContainer2.mChildren.add(constraintWidget);
                ConstraintWidget constraintWidget2 = constraintWidget.mParent;
                if (constraintWidget2 != null) {
                    ((WidgetContainer) constraintWidget2).mChildren.remove(constraintWidget);
                    constraintWidget.mParent = null;
                }
                constraintWidget.mParent = constraintWidgetContainer2;
                hashMap.put(next, constraintWidget);
            }
            Iterator<ConstraintWidget> it2 = arrayList.iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                hashMap.get(next2).copy(next2, hashMap);
            }
        }

        public static ConstraintWidget getWidget(ConstraintWidgetContainer constraintWidgetContainer, View view) {
            Objects.requireNonNull(constraintWidgetContainer);
            if (constraintWidgetContainer.mCompanionWidget == view) {
                return constraintWidgetContainer;
            }
            ArrayList<ConstraintWidget> arrayList = constraintWidgetContainer.mChildren;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ConstraintWidget constraintWidget = arrayList.get(i);
                Objects.requireNonNull(constraintWidget);
                if (constraintWidget.mCompanionWidget == view) {
                    return constraintWidget;
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class MyTracker {
        public static MyTracker me = new MyTracker();
        public VelocityTracker tracker;
    }

    /* loaded from: classes.dex */
    public interface TransitionListener {
        void onTransitionChange();

        void onTransitionCompleted();

        void onTransitionStarted();

        void onTransitionTrigger();
    }

    public MotionLayout(Context context) {
        super(context);
        init(null);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedFling(View view, float f, float f2, boolean z) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedPreFling(View view, float f, float f2) {
        return false;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScrollAccepted(View view, View view2, int i, int i2) {
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onStopNestedScroll(View view, int i) {
        TouchResponse touchResponse;
        float f;
        boolean z;
        this.mScrollTarget = null;
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            float f2 = this.mScrollTargetDX;
            float f3 = this.mScrollTargetDT;
            float f4 = f2 / f3;
            float f5 = this.mScrollTargetDY / f3;
            Objects.requireNonNull(motionScene);
            MotionScene.Transition transition = motionScene.mCurrentTransition;
            if (transition != null && (touchResponse = transition.mTouchResponse) != null) {
                boolean z2 = false;
                touchResponse.mDragStarted = false;
                MotionLayout motionLayout = touchResponse.mMotionLayout;
                Objects.requireNonNull(motionLayout);
                float f6 = motionLayout.mTransitionLastPosition;
                touchResponse.mMotionLayout.getAnchorDpDt(touchResponse.mTouchAnchorId, f6, touchResponse.mTouchAnchorX, touchResponse.mTouchAnchorY, touchResponse.mAnchorDpDt);
                float f7 = touchResponse.mTouchDirectionX;
                float[] fArr = touchResponse.mAnchorDpDt;
                float f8 = fArr[0];
                float f9 = touchResponse.mTouchDirectionY;
                float f10 = fArr[1];
                float f11 = 0.0f;
                if (f7 != 0.0f) {
                    f = (f4 * f7) / fArr[0];
                } else {
                    f = (f5 * f9) / fArr[1];
                }
                if (!Float.isNaN(f)) {
                    f6 += f / 3.0f;
                }
                if (f6 != 0.0f) {
                    if (f6 != 1.0f) {
                        z = true;
                    } else {
                        z = false;
                    }
                    int i2 = touchResponse.mOnTouchUp;
                    if (i2 != 3) {
                        z2 = true;
                    }
                    if (z2 && z) {
                        MotionLayout motionLayout2 = touchResponse.mMotionLayout;
                        if (f6 >= 0.5d) {
                            f11 = 1.0f;
                        }
                        motionLayout2.touchAnimateTo(i2, f11, f);
                    }
                }
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    public final void parseLayoutDescription(int i) {
    }

    public final void setProgress(float f) {
        if (f <= 0.0f) {
            this.mCurrentState = this.mBeginState;
        } else if (f >= 1.0f) {
            this.mCurrentState = this.mEndState;
        } else {
            this.mCurrentState = -1;
        }
        if (this.mScene != null) {
            this.mTransitionInstantly = true;
            this.mTransitionGoalPosition = f;
            this.mTransitionPosition = f;
            this.mTransitionLastTime = -1L;
            this.mAnimationStartTime = -1L;
            this.mInterpolator = null;
            this.mInTransition = true;
            invalidate();
        }
    }

    public final void setTransition(int i) {
        MotionScene.Transition transition;
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            Iterator<MotionScene.Transition> it = motionScene.mTransitionList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    transition = null;
                    break;
                }
                transition = it.next();
                if (transition.mId == i) {
                    break;
                }
            }
            Objects.requireNonNull(transition);
            int i2 = transition.mConstraintSetStart;
            this.mBeginState = i2;
            int i3 = transition.mConstraintSetEnd;
            this.mEndState = i3;
            float f = Float.NaN;
            int i4 = this.mCurrentState;
            if (i4 == i2) {
                f = 0.0f;
            } else if (i4 == i3) {
                f = 1.0f;
            }
            MotionScene motionScene2 = this.mScene;
            Objects.requireNonNull(motionScene2);
            motionScene2.mCurrentTransition = transition;
            TouchResponse touchResponse = transition.mTouchResponse;
            if (touchResponse != null) {
                touchResponse.setRTL(motionScene2.mRtl);
            }
            this.mModel.initFrom(this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            rebuildScene();
            this.mTransitionLastPosition = Float.isNaN(f) ? 0.0f : f;
            if (Float.isNaN(f)) {
                Log.v("MotionLayout", Debug.getLocation() + " transitionToStart ");
                animateTo(0.0f);
                return;
            }
            setProgress(f);
        }
    }

    public final void animateTo(float f) {
        int i;
        Interpolator interpolator;
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            float f2 = this.mTransitionLastPosition;
            float f3 = this.mTransitionPosition;
            if (f2 != f3 && this.mTransitionInstantly) {
                this.mTransitionLastPosition = f3;
            }
            float f4 = this.mTransitionLastPosition;
            if (f4 != f) {
                this.mTemporalInterpolator = false;
                this.mTransitionGoalPosition = f;
                MotionScene.Transition transition = motionScene.mCurrentTransition;
                if (transition != null) {
                    i = transition.mDuration;
                } else {
                    i = motionScene.mDefaultDuration;
                }
                this.mTransitionDuration = i / 1000.0f;
                setProgress(f);
                MotionScene motionScene2 = this.mScene;
                Objects.requireNonNull(motionScene2);
                MotionScene.Transition transition2 = motionScene2.mCurrentTransition;
                int i2 = transition2.mDefaultInterpolator;
                if (i2 == -2) {
                    interpolator = AnimationUtils.loadInterpolator(motionScene2.mMotionLayout.getContext(), motionScene2.mCurrentTransition.mDefaultInterpolatorID);
                } else if (i2 == -1) {
                    final Easing interpolator2 = Easing.getInterpolator(transition2.mDefaultInterpolatorString);
                    interpolator = new Interpolator() { // from class: androidx.constraintlayout.motion.widget.MotionScene.1
                        @Override // android.animation.TimeInterpolator
                        public final float getInterpolation(float f5) {
                            return (float) Easing.this.get(f5);
                        }
                    };
                } else if (i2 == 0) {
                    interpolator = new AccelerateDecelerateInterpolator();
                } else if (i2 == 1) {
                    interpolator = new AccelerateInterpolator();
                } else if (i2 == 2) {
                    interpolator = new android.view.animation.DecelerateInterpolator();
                } else if (i2 == 4) {
                    interpolator = new AnticipateInterpolator();
                } else if (i2 != 5) {
                    interpolator = null;
                } else {
                    interpolator = new BounceInterpolator();
                }
                this.mInterpolator = interpolator;
                this.mTransitionInstantly = false;
                this.mAnimationStartTime = System.nanoTime();
                this.mInTransition = true;
                this.mTransitionPosition = f4;
                this.mTransitionLastPosition = f4;
                invalidate();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02e3  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0328  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0332  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x034b  */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dispatchDraw(android.graphics.Canvas r29) {
        /*
            Method dump skipped, instructions count: 1169
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.dispatchDraw(android.graphics.Canvas):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:129:0x01b6, code lost:
        if (r20.mListenerState == r20.mEndState) goto L_0x01ba;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void evaluate(boolean r21) {
        /*
            Method dump skipped, instructions count: 477
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.evaluate(boolean):void");
    }

    public final void fireTransitionChange() {
        ArrayList<TransitionListener> arrayList = this.mTransitionListeners;
        if (arrayList != null && !arrayList.isEmpty() && this.mListenerPosition != this.mTransitionPosition) {
            if (this.mListenerState != -1) {
                ArrayList<TransitionListener> arrayList2 = this.mTransitionListeners;
                if (arrayList2 != null) {
                    Iterator<TransitionListener> it = arrayList2.iterator();
                    while (it.hasNext()) {
                        it.next().onTransitionStarted();
                    }
                }
                this.mIsAnimating = true;
            }
            this.mListenerState = -1;
            this.mListenerPosition = this.mTransitionPosition;
            ArrayList<TransitionListener> arrayList3 = this.mTransitionListeners;
            if (arrayList3 != null) {
                Iterator<TransitionListener> it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    it2.next().onTransitionChange();
                }
            }
            this.mIsAnimating = true;
        }
    }

    public final void fireTransitionCompleted() {
        int i;
        ArrayList<Integer> arrayList;
        ArrayList<TransitionListener> arrayList2 = this.mTransitionListeners;
        if (arrayList2 != null && !arrayList2.isEmpty() && this.mListenerState == -1) {
            this.mListenerState = this.mCurrentState;
            if (!this.mTransitionCompleted.isEmpty()) {
                i = this.mTransitionCompleted.get(arrayList.size() - 1).intValue();
            } else {
                i = -1;
            }
            int i2 = this.mCurrentState;
            if (i != i2 && i2 != -1) {
                this.mTransitionCompleted.add(Integer.valueOf(i2));
            }
        }
    }

    public final void getAnchorDpDt(int i, float f, float f2, float f3, float[] fArr) {
        String str;
        HashMap<View, MotionController> hashMap = this.mFrameArrayList;
        View viewById = getViewById(i);
        MotionController motionController = hashMap.get(viewById);
        if (motionController != null) {
            motionController.getDpDt(f, f2, f3, fArr);
            viewById.getY();
            return;
        }
        if (viewById == null) {
            str = VendorAtomValue$$ExternalSyntheticOutline0.m("", i);
        } else {
            str = viewById.getContext().getResources().getResourceName(i);
        }
        MotionLayout$$ExternalSyntheticOutline0.m("WARNING could not find view id ", str, "MotionLayout");
    }

    public final boolean handlesTouchEvent(float f, float f2, View view, MotionEvent motionEvent) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (handlesTouchEvent(view.getLeft() + f, view.getTop() + f2, viewGroup.getChildAt(i), motionEvent)) {
                    return true;
                }
            }
        }
        this.mBoundsCheck.set(view.getLeft() + f, view.getTop() + f2, f + view.getRight(), f2 + view.getBottom());
        if (motionEvent.getAction() == 0) {
            if (this.mBoundsCheck.contains(motionEvent.getX(), motionEvent.getY()) && view.onTouchEvent(motionEvent)) {
                return true;
            }
        } else if (view.onTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        int i;
        super.onAttachedToWindow();
        MotionScene motionScene = this.mScene;
        if (!(motionScene == null || (i = this.mCurrentState) == -1)) {
            ConstraintSet constraintSet = motionScene.getConstraintSet(i);
            MotionScene motionScene2 = this.mScene;
            Objects.requireNonNull(motionScene2);
            int i2 = 0;
            while (true) {
                boolean z = true;
                if (i2 < motionScene2.mConstraintSetMap.size()) {
                    int keyAt = motionScene2.mConstraintSetMap.keyAt(i2);
                    int i3 = motionScene2.mDeriveMap.get(keyAt);
                    int size = motionScene2.mDeriveMap.size();
                    while (true) {
                        if (i3 <= 0) {
                            z = false;
                            break;
                        } else if (i3 == keyAt) {
                            break;
                        } else {
                            int i4 = size - 1;
                            if (size < 0) {
                                break;
                            }
                            i3 = motionScene2.mDeriveMap.get(i3);
                            size = i4;
                        }
                    }
                    if (z) {
                        Log.e("MotionScene", "Cannot be derived from yourself");
                        break;
                    } else {
                        motionScene2.readConstraintChain(keyAt);
                        i2++;
                    }
                } else {
                    for (int i5 = 0; i5 < motionScene2.mConstraintSetMap.size(); i5++) {
                        ConstraintSet valueAt = motionScene2.mConstraintSetMap.valueAt(i5);
                        Objects.requireNonNull(valueAt);
                        int childCount = getChildCount();
                        for (int i6 = 0; i6 < childCount; i6++) {
                            View childAt = getChildAt(i6);
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
                            int id = childAt.getId();
                            if (!valueAt.mForceId || id != -1) {
                                if (!valueAt.mConstraints.containsKey(Integer.valueOf(id))) {
                                    valueAt.mConstraints.put(Integer.valueOf(id), new ConstraintSet.Constraint());
                                }
                                ConstraintSet.Constraint constraint = valueAt.mConstraints.get(Integer.valueOf(id));
                                if (!constraint.layout.mApply) {
                                    constraint.fillFrom(id, layoutParams);
                                    if (childAt instanceof ConstraintHelper) {
                                        ConstraintHelper constraintHelper = (ConstraintHelper) childAt;
                                        constraint.layout.mReferenceIds = Arrays.copyOf(constraintHelper.mIds, constraintHelper.mCount);
                                        if (childAt instanceof Barrier) {
                                            Barrier barrier = (Barrier) childAt;
                                            ConstraintSet.Layout layout = constraint.layout;
                                            androidx.constraintlayout.solver.widgets.Barrier barrier2 = barrier.mBarrier;
                                            Objects.requireNonNull(barrier2);
                                            layout.mBarrierAllowsGoneWidgets = barrier2.mAllowsGoneWidget;
                                            ConstraintSet.Layout layout2 = constraint.layout;
                                            layout2.mBarrierDirection = barrier.mIndicatedType;
                                            androidx.constraintlayout.solver.widgets.Barrier barrier3 = barrier.mBarrier;
                                            Objects.requireNonNull(barrier3);
                                            layout2.mBarrierMargin = barrier3.mMargin;
                                        }
                                    }
                                    constraint.layout.mApply = true;
                                }
                                ConstraintSet.PropertySet propertySet = constraint.propertySet;
                                if (!propertySet.mApply) {
                                    propertySet.visibility = childAt.getVisibility();
                                    constraint.propertySet.alpha = childAt.getAlpha();
                                    constraint.propertySet.mApply = true;
                                }
                                ConstraintSet.Transform transform = constraint.transform;
                                if (!transform.mApply) {
                                    transform.mApply = true;
                                    transform.rotation = childAt.getRotation();
                                    constraint.transform.rotationX = childAt.getRotationX();
                                    constraint.transform.rotationY = childAt.getRotationY();
                                    constraint.transform.scaleX = childAt.getScaleX();
                                    constraint.transform.scaleY = childAt.getScaleY();
                                    float pivotX = childAt.getPivotX();
                                    float pivotY = childAt.getPivotY();
                                    if (!(pivotX == 0.0d && pivotY == 0.0d)) {
                                        ConstraintSet.Transform transform2 = constraint.transform;
                                        transform2.transformPivotX = pivotX;
                                        transform2.transformPivotY = pivotY;
                                    }
                                    constraint.transform.translationX = childAt.getTranslationX();
                                    constraint.transform.translationY = childAt.getTranslationY();
                                    constraint.transform.translationZ = childAt.getTranslationZ();
                                    ConstraintSet.Transform transform3 = constraint.transform;
                                    if (transform3.applyElevation) {
                                        transform3.elevation = childAt.getElevation();
                                    }
                                }
                            } else {
                                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
                            }
                        }
                    }
                }
            }
            if (constraintSet != null) {
                constraintSet.applyTo(this);
            }
            this.mBeginState = this.mCurrentState;
        }
        onNewStateAttachHandlers();
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionScene.Transition transition;
        TouchResponse touchResponse;
        int i;
        RectF touchRegion;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && this.mInteractionEnabled && (transition = motionScene.mCurrentTransition) != null && (!transition.mDisable) && (touchResponse = transition.mTouchResponse) != null && ((motionEvent.getAction() != 0 || (touchRegion = touchResponse.getTouchRegion(this, new RectF())) == null || touchRegion.contains(motionEvent.getX(), motionEvent.getY())) && (i = touchResponse.mTouchRegionId) != -1)) {
            View view = this.mRegionView;
            if (view == null || view.getId() != i) {
                this.mRegionView = findViewById(i);
            }
            View view2 = this.mRegionView;
            if (view2 != null) {
                this.mBoundsCheck.set(view2.getLeft(), this.mRegionView.getTop(), this.mRegionView.getRight(), this.mRegionView.getBottom());
                if (this.mBoundsCheck.contains(motionEvent.getX(), motionEvent.getY()) && !handlesTouchEvent(0.0f, 0.0f, this.mRegionView, motionEvent)) {
                    return onTouchEvent(motionEvent);
                }
            }
        }
        return false;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mScene == null) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int i5 = i3 - i;
        int i6 = i4 - i2;
        if (!(this.mLastLayoutWidth == i5 && this.mLastLayoutHeight == i6)) {
            rebuildScene();
            evaluate(true);
        }
        this.mLastLayoutWidth = i5;
        this.mLastLayoutHeight = i6;
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0099, code lost:
        if (r3 != false) goto L_0x009b;
     */
    /* JADX WARN: Removed duplicated region for block: B:105:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01a6  */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r18, int r19) {
        /*
            Method dump skipped, instructions count: 426
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.onMeasure(int, int):void");
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        MotionScene.Transition transition;
        TouchResponse touchResponse;
        float f;
        float f2;
        TouchResponse touchResponse2;
        boolean z;
        TouchResponse touchResponse3;
        TouchResponse touchResponse4;
        int i4;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && (transition = motionScene.mCurrentTransition) != null) {
            boolean z2 = transition.mDisable;
            if (!z2) {
                if (transition == null || !(!z2) || (touchResponse4 = transition.mTouchResponse) == null || (i4 = touchResponse4.mTouchRegionId) == -1 || this.mScrollTarget.getId() == i4) {
                    MotionScene motionScene2 = this.mScene;
                    if (motionScene2 != null) {
                        MotionScene.Transition transition2 = motionScene2.mCurrentTransition;
                        if (transition2 == null || (touchResponse3 = transition2.mTouchResponse) == null) {
                            z = false;
                        } else {
                            z = touchResponse3.mMoveWhenScrollAtTop;
                        }
                        if (z) {
                            float f3 = this.mTransitionPosition;
                            if ((f3 == 1.0f || f3 == 0.0f) && view.canScrollVertically(-1)) {
                                return;
                            }
                        }
                    }
                    Objects.requireNonNull(transition);
                    if (transition.mTouchResponse != null) {
                        MotionScene.Transition transition3 = this.mScene.mCurrentTransition;
                        Objects.requireNonNull(transition3);
                        TouchResponse touchResponse5 = transition3.mTouchResponse;
                        Objects.requireNonNull(touchResponse5);
                        if ((touchResponse5.mFlags & 1) != 0) {
                            MotionScene motionScene3 = this.mScene;
                            float f4 = i;
                            float f5 = i2;
                            Objects.requireNonNull(motionScene3);
                            MotionScene.Transition transition4 = motionScene3.mCurrentTransition;
                            if (transition4 == null || (touchResponse2 = transition4.mTouchResponse) == null) {
                                f2 = 0.0f;
                            } else {
                                MotionLayout motionLayout = touchResponse2.mMotionLayout;
                                Objects.requireNonNull(motionLayout);
                                touchResponse2.mMotionLayout.getAnchorDpDt(touchResponse2.mTouchAnchorId, motionLayout.mTransitionLastPosition, touchResponse2.mTouchAnchorX, touchResponse2.mTouchAnchorY, touchResponse2.mAnchorDpDt);
                                float f6 = touchResponse2.mTouchDirectionX;
                                if (f6 != 0.0f) {
                                    float[] fArr = touchResponse2.mAnchorDpDt;
                                    if (fArr[0] == 0.0f) {
                                        fArr[0] = 1.0E-7f;
                                    }
                                    f2 = (f4 * f6) / fArr[0];
                                } else {
                                    float[] fArr2 = touchResponse2.mAnchorDpDt;
                                    if (fArr2[1] == 0.0f) {
                                        fArr2[1] = 1.0E-7f;
                                    }
                                    f2 = (f5 * touchResponse2.mTouchDirectionY) / fArr2[1];
                                }
                            }
                            float f7 = this.mTransitionLastPosition;
                            if ((f7 <= 0.0f && f2 < 0.0f) || (f7 >= 1.0f && f2 > 0.0f)) {
                                this.mScrollTarget.setNestedScrollingEnabled(false);
                                this.mScrollTarget.post(new Runnable() { // from class: androidx.constraintlayout.motion.widget.MotionLayout.1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MotionLayout.this.mScrollTarget.setNestedScrollingEnabled(true);
                                    }
                                });
                                return;
                            }
                        }
                    }
                    float f8 = this.mTransitionPosition;
                    long nanoTime = System.nanoTime();
                    float f9 = i;
                    this.mScrollTargetDX = f9;
                    float f10 = i2;
                    this.mScrollTargetDY = f10;
                    this.mScrollTargetDT = (float) ((nanoTime - this.mScrollTargetTime) * 1.0E-9d);
                    this.mScrollTargetTime = nanoTime;
                    MotionScene motionScene4 = this.mScene;
                    Objects.requireNonNull(motionScene4);
                    MotionScene.Transition transition5 = motionScene4.mCurrentTransition;
                    if (!(transition5 == null || (touchResponse = transition5.mTouchResponse) == null)) {
                        MotionLayout motionLayout2 = touchResponse.mMotionLayout;
                        Objects.requireNonNull(motionLayout2);
                        float f11 = motionLayout2.mTransitionLastPosition;
                        if (!touchResponse.mDragStarted) {
                            touchResponse.mDragStarted = true;
                            touchResponse.mMotionLayout.setProgress(f11);
                        }
                        touchResponse.mMotionLayout.getAnchorDpDt(touchResponse.mTouchAnchorId, f11, touchResponse.mTouchAnchorX, touchResponse.mTouchAnchorY, touchResponse.mAnchorDpDt);
                        float f12 = touchResponse.mTouchDirectionX;
                        float[] fArr3 = touchResponse.mAnchorDpDt;
                        if (Math.abs((touchResponse.mTouchDirectionY * fArr3[1]) + (f12 * fArr3[0])) < 0.01d) {
                            float[] fArr4 = touchResponse.mAnchorDpDt;
                            fArr4[0] = 0.01f;
                            fArr4[1] = 0.01f;
                        }
                        float f13 = touchResponse.mTouchDirectionX;
                        if (f13 != 0.0f) {
                            f = (f9 * f13) / touchResponse.mAnchorDpDt[0];
                        } else {
                            f = (f10 * touchResponse.mTouchDirectionY) / touchResponse.mAnchorDpDt[1];
                        }
                        float max = Math.max(Math.min(f11 + f, 1.0f), 0.0f);
                        MotionLayout motionLayout3 = touchResponse.mMotionLayout;
                        Objects.requireNonNull(motionLayout3);
                        if (max != motionLayout3.mTransitionLastPosition) {
                            touchResponse.mMotionLayout.setProgress(max);
                        }
                    }
                    if (f8 != this.mTransitionPosition) {
                        iArr[0] = i;
                        iArr[1] = i2;
                    }
                    evaluate(false);
                    if (iArr[0] != 0 || iArr[1] != 0) {
                        this.mUndergoingMotion = true;
                    }
                }
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        if (!(!this.mUndergoingMotion && i == 0 && i2 == 0)) {
            iArr[0] = iArr[0] + i3;
            iArr[1] = iArr[1] + i4;
        }
        this.mUndergoingMotion = false;
    }

    public final void onNewStateAttachHandlers() {
        TouchResponse touchResponse;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && !motionScene.autoTransition(this, this.mCurrentState)) {
            int i = this.mCurrentState;
            if (i != -1) {
                MotionScene motionScene2 = this.mScene;
                Objects.requireNonNull(motionScene2);
                Iterator<MotionScene.Transition> it = motionScene2.mTransitionList.iterator();
                while (it.hasNext()) {
                    MotionScene.Transition next = it.next();
                    if (next.mOnClicks.size() > 0) {
                        Iterator<MotionScene.Transition.TransitionOnClick> it2 = next.mOnClicks.iterator();
                        while (it2.hasNext()) {
                            it2.next().removeOnClickListeners(this);
                        }
                    }
                }
                Iterator<MotionScene.Transition> it3 = motionScene2.mAbstractTransitionList.iterator();
                while (it3.hasNext()) {
                    MotionScene.Transition next2 = it3.next();
                    if (next2.mOnClicks.size() > 0) {
                        Iterator<MotionScene.Transition.TransitionOnClick> it4 = next2.mOnClicks.iterator();
                        while (it4.hasNext()) {
                            it4.next().removeOnClickListeners(this);
                        }
                    }
                }
                Iterator<MotionScene.Transition> it5 = motionScene2.mTransitionList.iterator();
                while (it5.hasNext()) {
                    MotionScene.Transition next3 = it5.next();
                    if (next3.mOnClicks.size() > 0) {
                        Iterator<MotionScene.Transition.TransitionOnClick> it6 = next3.mOnClicks.iterator();
                        while (it6.hasNext()) {
                            it6.next().addOnClickListeners(this, i, next3);
                        }
                    }
                }
                Iterator<MotionScene.Transition> it7 = motionScene2.mAbstractTransitionList.iterator();
                while (it7.hasNext()) {
                    MotionScene.Transition next4 = it7.next();
                    if (next4.mOnClicks.size() > 0) {
                        Iterator<MotionScene.Transition.TransitionOnClick> it8 = next4.mOnClicks.iterator();
                        while (it8.hasNext()) {
                            it8.next().addOnClickListeners(this, i, next4);
                        }
                    }
                }
            }
            if (this.mScene.supportTouch()) {
                MotionScene motionScene3 = this.mScene;
                Objects.requireNonNull(motionScene3);
                MotionScene.Transition transition = motionScene3.mCurrentTransition;
                if (transition != null && (touchResponse = transition.mTouchResponse) != null) {
                    View findViewById = touchResponse.mMotionLayout.findViewById(touchResponse.mTouchAnchorId);
                    if (findViewById == null) {
                        Log.w("TouchResponse", " cannot find view to handle touch");
                    }
                    if (findViewById instanceof NestedScrollView) {
                        NestedScrollView nestedScrollView = (NestedScrollView) findViewById;
                        nestedScrollView.setOnTouchListener(new View.OnTouchListener() { // from class: androidx.constraintlayout.motion.widget.TouchResponse.1
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view, MotionEvent motionEvent) {
                                return false;
                            }
                        });
                        nestedScrollView.mOnScrollChangeListener = new NestedScrollView.OnScrollChangeListener() { // from class: androidx.constraintlayout.motion.widget.TouchResponse.2
                        };
                    }
                }
            }
        }
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
        TouchResponse touchResponse;
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            boolean isRtl = isRtl();
            Objects.requireNonNull(motionScene);
            motionScene.mRtl = isRtl;
            MotionScene.Transition transition = motionScene.mCurrentTransition;
            if (transition != null && (touchResponse = transition.mTouchResponse) != null) {
                touchResponse.setRTL(isRtl);
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        MotionScene.Transition transition;
        this.mScrollTarget = view2;
        MotionScene motionScene = this.mScene;
        if (motionScene == null || (transition = motionScene.mCurrentTransition) == null) {
            return false;
        }
        Objects.requireNonNull(transition);
        if (transition.mTouchResponse == null) {
            return false;
        }
        MotionScene.Transition transition2 = this.mScene.mCurrentTransition;
        Objects.requireNonNull(transition2);
        TouchResponse touchResponse = transition2.mTouchResponse;
        Objects.requireNonNull(touchResponse);
        if ((touchResponse.mFlags & 2) != 0) {
            return false;
        }
        return true;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        MotionScene.Transition transition;
        MyTracker myTracker;
        TouchResponse touchResponse;
        char c;
        char c2;
        float f;
        int i;
        float f2;
        char c3;
        char c4;
        char c5;
        char c6;
        float f3;
        float f4;
        RectF rectF;
        View findViewById;
        MotionEvent motionEvent2;
        MotionScene.Transition transition2;
        boolean z;
        int i2;
        TouchResponse touchResponse2;
        RectF touchRegion;
        float f5;
        MotionScene motionScene = this.mScene;
        if (motionScene == null || !this.mInteractionEnabled || !motionScene.supportTouch()) {
            return super.onTouchEvent(motionEvent);
        }
        MotionScene motionScene2 = this.mScene;
        if (motionScene2.mCurrentTransition != null && !(!transition.mDisable)) {
            return super.onTouchEvent(motionEvent);
        }
        int i3 = this.mCurrentState;
        RectF rectF2 = new RectF();
        if (motionScene2.mVelocityTracker == null) {
            Objects.requireNonNull(motionScene2.mMotionLayout);
            MyTracker myTracker2 = MyTracker.me;
            myTracker2.tracker = VelocityTracker.obtain();
            motionScene2.mVelocityTracker = myTracker2;
        }
        MyTracker myTracker3 = motionScene2.mVelocityTracker;
        Objects.requireNonNull(myTracker3);
        VelocityTracker velocityTracker = myTracker3.tracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
        if (i3 != -1) {
            int action = motionEvent.getAction();
            if (action == 0) {
                motionScene2.mLastTouchX = motionEvent.getRawX();
                motionScene2.mLastTouchY = motionEvent.getRawY();
                motionScene2.mLastTouchDown = motionEvent;
                TouchResponse touchResponse3 = motionScene2.mCurrentTransition.mTouchResponse;
                if (touchResponse3 != null) {
                    MotionLayout motionLayout = motionScene2.mMotionLayout;
                    int i4 = touchResponse3.mLimitBoundsTo;
                    if (i4 == -1 || (findViewById = motionLayout.findViewById(i4)) == null) {
                        rectF = null;
                    } else {
                        rectF2.set(findViewById.getLeft(), findViewById.getTop(), findViewById.getRight(), findViewById.getBottom());
                        rectF = rectF2;
                    }
                    if (rectF == null || rectF.contains(motionScene2.mLastTouchDown.getX(), motionScene2.mLastTouchDown.getY())) {
                        RectF touchRegion2 = motionScene2.mCurrentTransition.mTouchResponse.getTouchRegion(motionScene2.mMotionLayout, rectF2);
                        if (touchRegion2 == null || touchRegion2.contains(motionScene2.mLastTouchDown.getX(), motionScene2.mLastTouchDown.getY())) {
                            motionScene2.mMotionOutsideRegion = false;
                        } else {
                            motionScene2.mMotionOutsideRegion = true;
                        }
                        TouchResponse touchResponse4 = motionScene2.mCurrentTransition.mTouchResponse;
                        float f6 = motionScene2.mLastTouchX;
                        float f7 = motionScene2.mLastTouchY;
                        Objects.requireNonNull(touchResponse4);
                        touchResponse4.mLastTouchX = f6;
                        touchResponse4.mLastTouchY = f7;
                    } else {
                        motionScene2.mLastTouchDown = null;
                    }
                }
                return true;
            } else if (action == 2) {
                float rawY = motionEvent.getRawY() - motionScene2.mLastTouchY;
                float rawX = motionEvent.getRawX() - motionScene2.mLastTouchX;
                if ((rawX == 0.0d && rawY == 0.0d) || (motionEvent2 = motionScene2.mLastTouchDown) == null) {
                    return true;
                }
                if (i3 != -1) {
                    StateSet stateSet = motionScene2.mStateSet;
                    if (stateSet == null || (i2 = stateSet.stateGetConstraintID(i3)) == -1) {
                        i2 = i3;
                    }
                    ArrayList arrayList = new ArrayList();
                    Iterator<MotionScene.Transition> it = motionScene2.mTransitionList.iterator();
                    while (it.hasNext()) {
                        MotionScene.Transition next = it.next();
                        if (next.mConstraintSetStart == i2 || next.mConstraintSetEnd == i2) {
                            arrayList.add(next);
                        }
                    }
                    RectF rectF3 = new RectF();
                    Iterator it2 = arrayList.iterator();
                    float f8 = 0.0f;
                    transition2 = null;
                    while (it2.hasNext()) {
                        MotionScene.Transition transition3 = (MotionScene.Transition) it2.next();
                        if (!transition3.mDisable && (touchResponse2 = transition3.mTouchResponse) != null) {
                            touchResponse2.setRTL(motionScene2.mRtl);
                            RectF touchRegion3 = transition3.mTouchResponse.getTouchRegion(motionScene2.mMotionLayout, rectF3);
                            if ((touchRegion3 == null || touchRegion3.contains(motionEvent2.getX(), motionEvent2.getY())) && ((touchRegion = transition3.mTouchResponse.getTouchRegion(motionScene2.mMotionLayout, rectF3)) == null || touchRegion.contains(motionEvent2.getX(), motionEvent2.getY()))) {
                                TouchResponse touchResponse5 = transition3.mTouchResponse;
                                Objects.requireNonNull(touchResponse5);
                                float f9 = (touchResponse5.mTouchDirectionY * rawY) + (touchResponse5.mTouchDirectionX * rawX);
                                if (transition3.mConstraintSetEnd == i3) {
                                    f5 = -1.0f;
                                } else {
                                    f5 = 1.1f;
                                }
                                float f10 = f9 * f5;
                                if (f10 > f8) {
                                    f8 = f10;
                                    transition2 = transition3;
                                }
                            }
                        }
                    }
                } else {
                    transition2 = motionScene2.mCurrentTransition;
                }
                if (transition2 != null) {
                    setTransition(transition2);
                    RectF touchRegion4 = motionScene2.mCurrentTransition.mTouchResponse.getTouchRegion(motionScene2.mMotionLayout, rectF2);
                    if (touchRegion4 == null || touchRegion4.contains(motionScene2.mLastTouchDown.getX(), motionScene2.mLastTouchDown.getY())) {
                        z = false;
                    } else {
                        z = true;
                    }
                    motionScene2.mMotionOutsideRegion = z;
                    TouchResponse touchResponse6 = motionScene2.mCurrentTransition.mTouchResponse;
                    float f11 = motionScene2.mLastTouchX;
                    float f12 = motionScene2.mLastTouchY;
                    Objects.requireNonNull(touchResponse6);
                    touchResponse6.mLastTouchX = f11;
                    touchResponse6.mLastTouchY = f12;
                    touchResponse6.mDragStarted = false;
                }
            }
        }
        MotionScene.Transition transition4 = motionScene2.mCurrentTransition;
        if (!(transition4 == null || (touchResponse = transition4.mTouchResponse) == null || motionScene2.mMotionOutsideRegion)) {
            MyTracker myTracker4 = motionScene2.mVelocityTracker;
            Objects.requireNonNull(myTracker4);
            VelocityTracker velocityTracker2 = myTracker4.tracker;
            if (velocityTracker2 != null) {
                velocityTracker2.addMovement(motionEvent);
            }
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                touchResponse.mLastTouchX = motionEvent.getRawX();
                touchResponse.mLastTouchY = motionEvent.getRawY();
                touchResponse.mDragStarted = false;
            } else if (action2 == 1) {
                touchResponse.mDragStarted = false;
                myTracker4.tracker.computeCurrentVelocity(1000);
                float xVelocity = myTracker4.tracker.getXVelocity();
                float yVelocity = myTracker4.tracker.getYVelocity();
                MotionLayout motionLayout2 = touchResponse.mMotionLayout;
                Objects.requireNonNull(motionLayout2);
                float f13 = motionLayout2.mTransitionLastPosition;
                int i5 = touchResponse.mTouchAnchorId;
                if (i5 != -1) {
                    touchResponse.mMotionLayout.getAnchorDpDt(i5, f13, touchResponse.mTouchAnchorX, touchResponse.mTouchAnchorY, touchResponse.mAnchorDpDt);
                    c2 = 0;
                    c = 1;
                } else {
                    float min = Math.min(touchResponse.mMotionLayout.getWidth(), touchResponse.mMotionLayout.getHeight());
                    float[] fArr = touchResponse.mAnchorDpDt;
                    c = 1;
                    fArr[1] = touchResponse.mTouchDirectionY * min;
                    c2 = 0;
                    fArr[0] = min * touchResponse.mTouchDirectionX;
                }
                float f14 = touchResponse.mTouchDirectionX;
                float[] fArr2 = touchResponse.mAnchorDpDt;
                float f15 = fArr2[c2];
                float f16 = fArr2[c];
                if (f14 != 0.0f) {
                    f = xVelocity / fArr2[c2];
                } else {
                    f = yVelocity / fArr2[c];
                }
                if (!Float.isNaN(f)) {
                    f13 += f / 3.0f;
                }
                if (!(f13 == 0.0f || f13 == 1.0f || (i = touchResponse.mOnTouchUp) == 3)) {
                    MotionLayout motionLayout3 = touchResponse.mMotionLayout;
                    if (f13 < 0.5d) {
                        f2 = 0.0f;
                    } else {
                        f2 = 1.0f;
                    }
                    motionLayout3.touchAnimateTo(i, f2, f);
                }
            } else if (action2 == 2) {
                float rawY2 = motionEvent.getRawY() - touchResponse.mLastTouchY;
                float rawX2 = motionEvent.getRawX() - touchResponse.mLastTouchX;
                if (Math.abs((touchResponse.mTouchDirectionY * rawY2) + (touchResponse.mTouchDirectionX * rawX2)) > 10.0f || touchResponse.mDragStarted) {
                    MotionLayout motionLayout4 = touchResponse.mMotionLayout;
                    Objects.requireNonNull(motionLayout4);
                    float f17 = motionLayout4.mTransitionLastPosition;
                    if (!touchResponse.mDragStarted) {
                        touchResponse.mDragStarted = true;
                        touchResponse.mMotionLayout.setProgress(f17);
                    }
                    int i6 = touchResponse.mTouchAnchorId;
                    if (i6 != -1) {
                        touchResponse.mMotionLayout.getAnchorDpDt(i6, f17, touchResponse.mTouchAnchorX, touchResponse.mTouchAnchorY, touchResponse.mAnchorDpDt);
                        c4 = 0;
                        c3 = 1;
                    } else {
                        float min2 = Math.min(touchResponse.mMotionLayout.getWidth(), touchResponse.mMotionLayout.getHeight());
                        float[] fArr3 = touchResponse.mAnchorDpDt;
                        c3 = 1;
                        fArr3[1] = touchResponse.mTouchDirectionY * min2;
                        c4 = 0;
                        fArr3[0] = min2 * touchResponse.mTouchDirectionX;
                    }
                    float f18 = touchResponse.mTouchDirectionX;
                    float[] fArr4 = touchResponse.mAnchorDpDt;
                    if (Math.abs(((touchResponse.mTouchDirectionY * fArr4[c3]) + (f18 * fArr4[c4])) * touchResponse.mDragScale) < 0.01d) {
                        float[] fArr5 = touchResponse.mAnchorDpDt;
                        c6 = 0;
                        fArr5[0] = 0.01f;
                        c5 = 1;
                        fArr5[1] = 0.01f;
                    } else {
                        c6 = 0;
                        c5 = 1;
                    }
                    if (touchResponse.mTouchDirectionX != 0.0f) {
                        f3 = rawX2 / touchResponse.mAnchorDpDt[c6];
                    } else {
                        f3 = rawY2 / touchResponse.mAnchorDpDt[c5];
                    }
                    float max = Math.max(Math.min(f17 + f3, 1.0f), 0.0f);
                    MotionLayout motionLayout5 = touchResponse.mMotionLayout;
                    Objects.requireNonNull(motionLayout5);
                    if (max != motionLayout5.mTransitionLastPosition) {
                        touchResponse.mMotionLayout.setProgress(max);
                        myTracker4.tracker.computeCurrentVelocity(1000);
                        float xVelocity2 = myTracker4.tracker.getXVelocity();
                        float yVelocity2 = myTracker4.tracker.getYVelocity();
                        if (touchResponse.mTouchDirectionX != 0.0f) {
                            f4 = xVelocity2 / touchResponse.mAnchorDpDt[0];
                        } else {
                            f4 = yVelocity2 / touchResponse.mAnchorDpDt[1];
                        }
                        touchResponse.mMotionLayout.mLastVelocity = f4;
                    } else {
                        touchResponse.mMotionLayout.mLastVelocity = 0.0f;
                    }
                    touchResponse.mLastTouchX = motionEvent.getRawX();
                    touchResponse.mLastTouchY = motionEvent.getRawY();
                }
            }
        }
        motionScene2.mLastTouchX = motionEvent.getRawX();
        motionScene2.mLastTouchY = motionEvent.getRawY();
        if (motionEvent.getAction() == 1 && (myTracker = motionScene2.mVelocityTracker) != null) {
            myTracker.tracker.recycle();
            myTracker.tracker = null;
            motionScene2.mVelocityTracker = null;
            int i7 = this.mCurrentState;
            if (i7 != -1) {
                motionScene2.autoTransition(this, i7);
            }
        }
        return true;
    }

    public final void rebuildScene() {
        this.mModel.reEvaluateState();
        invalidate();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View, android.view.ViewParent
    public final void requestLayout() {
        MotionScene motionScene;
        MotionScene.Transition transition;
        if (!this.mMeasureDuringTransition && this.mCurrentState == -1 && (motionScene = this.mScene) != null && (transition = motionScene.mCurrentTransition) != null) {
            Objects.requireNonNull(transition);
            if (transition.mLayoutDuringTransition == 0) {
                return;
            }
        }
        super.requestLayout();
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x005a, code lost:
        if ((((r15 * r3) - (((r2 * r3) * r3) / 2.0f)) + r13) > 1.0f) goto L_0x006c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0068, code lost:
        if ((((((r2 * r3) * r3) / 2.0f) + (r15 * r3)) + r13) < 0.0f) goto L_0x006c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x006b, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x006c, code lost:
        if (r0 == false) goto L_0x0087;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x006e, code lost:
        r13 = r12.mDecelerateLogic;
        r14 = r12.mTransitionLastPosition;
        r0 = r12.mScene.getMaxAcceleration();
        java.util.Objects.requireNonNull(r13);
        r13.initalV = r15;
        r13.currentP = r14;
        r13.maxA = r0;
        r12.mInterpolator = r12.mDecelerateLogic;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0087, code lost:
        r2 = r12.mStopLogic;
        r3 = r12.mTransitionLastPosition;
        r6 = r12.mTransitionDuration;
        r7 = r12.mScene.getMaxAcceleration();
        r13 = r12.mScene;
        java.util.Objects.requireNonNull(r13);
        r13 = r13.mCurrentTransition;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x009a, code lost:
        if (r13 == null) goto L_0x00a4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x009c, code lost:
        r13 = r13.mTouchResponse;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x009e, code lost:
        if (r13 == null) goto L_0x00a4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a0, code lost:
        r8 = r13.mMaxVelocity;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a4, code lost:
        r8 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00a5, code lost:
        r2.config(r3, r14, r15, r6, r7, r8);
        r12.mLastVelocity = 0.0f;
        r13 = r12.mCurrentState;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b0, code lost:
        if (r14 != 0.0f) goto L_0x00b3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b3, code lost:
        r10 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b4, code lost:
        setProgress(r10);
        r12.mCurrentState = r13;
        r12.mInterpolator = r12.mStopLogic;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void touchAnimateTo(int r13, float r14, float r15) {
        /*
            Method dump skipped, instructions count: 283
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.touchAnimateTo(int, float, float):void");
    }

    public final void init(AttributeSet attributeSet) {
        MotionScene motionScene;
        String str;
        String str2;
        IS_IN_EDIT_MODE = isInEditMode();
        int i = -1;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.MotionLayout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            boolean z = true;
            for (int i2 = 0; i2 < indexCount; i2++) {
                int index = obtainStyledAttributes.getIndex(i2);
                int i3 = 2;
                if (index == 2) {
                    this.mScene = new MotionScene(getContext(), this, obtainStyledAttributes.getResourceId(index, -1));
                } else if (index == 1) {
                    this.mCurrentState = obtainStyledAttributes.getResourceId(index, -1);
                } else if (index == 4) {
                    this.mTransitionGoalPosition = obtainStyledAttributes.getFloat(index, 0.0f);
                    this.mInTransition = true;
                } else if (index == 0) {
                    z = obtainStyledAttributes.getBoolean(index, z);
                } else if (index == 5) {
                    if (this.mDebugPath == 0) {
                        if (!obtainStyledAttributes.getBoolean(index, false)) {
                            i3 = 0;
                        }
                        this.mDebugPath = i3;
                    }
                } else if (index == 3) {
                    this.mDebugPath = obtainStyledAttributes.getInt(index, 0);
                }
            }
            obtainStyledAttributes.recycle();
            if (this.mScene == null) {
                Log.e("MotionLayout", "WARNING NO app:layoutDescription tag");
            }
            if (!z) {
                this.mScene = null;
            }
        }
        if (this.mDebugPath != 0) {
            MotionScene motionScene2 = this.mScene;
            if (motionScene2 == null) {
                Log.e("MotionLayout", "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            } else {
                int startId = motionScene2.getStartId();
                MotionScene motionScene3 = this.mScene;
                ConstraintSet constraintSet = motionScene3.getConstraintSet(motionScene3.getStartId());
                String name = Debug.getName(getContext(), startId);
                int childCount = getChildCount();
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = getChildAt(i4);
                    int id = childAt.getId();
                    if (id == -1) {
                        StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("CHECK: ", name, " ALL VIEWS SHOULD HAVE ID's ");
                        m.append(childAt.getClass().getName());
                        m.append(" does not!");
                        Log.w("MotionLayout", m.toString());
                    }
                    if (constraintSet.getConstraint(id) == null) {
                        StringBuilder m2 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("CHECK: ", name, " NO CONSTRAINTS for ");
                        m2.append(Debug.getName(childAt));
                        Log.w("MotionLayout", m2.toString());
                    }
                }
                Objects.requireNonNull(constraintSet);
                Integer[] numArr = (Integer[]) constraintSet.mConstraints.keySet().toArray(new Integer[0]);
                int length = numArr.length;
                int[] iArr = new int[length];
                for (int i5 = 0; i5 < length; i5++) {
                    iArr[i5] = numArr[i5].intValue();
                }
                for (int i6 = 0; i6 < length; i6++) {
                    int i7 = iArr[i6];
                    String name2 = Debug.getName(getContext(), i7);
                    if (findViewById(iArr[i6]) == null) {
                        Log.w("MotionLayout", "CHECK: " + name + " NO View matches id " + name2);
                    }
                    if (constraintSet.get(i7).layout.mHeight == -1) {
                        Log.w("MotionLayout", "CHECK: " + name + "(" + name2 + ") no LAYOUT_HEIGHT");
                    }
                    if (constraintSet.get(i7).layout.mWidth == -1) {
                        Log.w("MotionLayout", "CHECK: " + name + "(" + name2 + ") no LAYOUT_HEIGHT");
                    }
                }
                SparseIntArray sparseIntArray = new SparseIntArray();
                SparseIntArray sparseIntArray2 = new SparseIntArray();
                MotionScene motionScene4 = this.mScene;
                Objects.requireNonNull(motionScene4);
                Iterator<MotionScene.Transition> it = motionScene4.mTransitionList.iterator();
                while (it.hasNext()) {
                    MotionScene.Transition next = it.next();
                    if (next == this.mScene.mCurrentTransition) {
                        Log.v("MotionLayout", "CHECK: CURRENT");
                    }
                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("CHECK: transition = ");
                    Context context = getContext();
                    Objects.requireNonNull(next);
                    if (next.mConstraintSetStart == -1) {
                        str = "null";
                    } else {
                        str = context.getResources().getResourceEntryName(next.mConstraintSetStart);
                    }
                    if (next.mConstraintSetEnd == -1) {
                        str2 = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, " -> null");
                    } else {
                        StringBuilder m4 = DebugInfo$$ExternalSyntheticOutline0.m(str, " -> ");
                        m4.append(context.getResources().getResourceEntryName(next.mConstraintSetEnd));
                        str2 = m4.toString();
                    }
                    m3.append(str2);
                    Log.v("MotionLayout", m3.toString());
                    Log.v("MotionLayout", "CHECK: transition.setDuration = " + next.mDuration);
                    if (next.mConstraintSetStart == next.mConstraintSetEnd) {
                        Log.e("MotionLayout", "CHECK: start and end constraint set should not be the same!");
                    }
                    int i8 = next.mConstraintSetStart;
                    int i9 = next.mConstraintSetEnd;
                    String name3 = Debug.getName(getContext(), i8);
                    String name4 = Debug.getName(getContext(), i9);
                    if (sparseIntArray.get(i8) == i9) {
                        Log.e("MotionLayout", "CHECK: two transitions with the same start and end " + name3 + "->" + name4);
                    }
                    if (sparseIntArray2.get(i9) == i8) {
                        Log.e("MotionLayout", "CHECK: you can't have reverse transitions" + name3 + "->" + name4);
                    }
                    sparseIntArray.put(i8, i9);
                    sparseIntArray2.put(i9, i8);
                    if (this.mScene.getConstraintSet(i8) == null) {
                        Log.e("MotionLayout", " no such constraintSetStart " + name3);
                    }
                    if (this.mScene.getConstraintSet(i9) == null) {
                        Log.e("MotionLayout", " no such constraintSetEnd " + name3);
                    }
                }
            }
        }
        if (this.mCurrentState == -1 && (motionScene = this.mScene) != null) {
            this.mCurrentState = motionScene.getStartId();
            this.mBeginState = this.mScene.getStartId();
            MotionScene motionScene5 = this.mScene;
            Objects.requireNonNull(motionScene5);
            MotionScene.Transition transition = motionScene5.mCurrentTransition;
            if (transition != null) {
                i = transition.mConstraintSetEnd;
            }
            this.mEndState = i;
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper motionHelper = (MotionHelper) view;
            if (this.mTransitionListeners == null) {
                this.mTransitionListeners = new ArrayList<>();
            }
            this.mTransitionListeners.add(motionHelper);
            if (motionHelper.mUseOnShow) {
                if (this.mOnShowHelpers == null) {
                    this.mOnShowHelpers = new ArrayList<>();
                }
                this.mOnShowHelpers.add(motionHelper);
            }
            if (motionHelper.mUseOnHide) {
                if (this.mOnHideHelpers == null) {
                    this.mOnHideHelpers = new ArrayList<>();
                }
                this.mOnHideHelpers.add(motionHelper);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        ArrayList<MotionHelper> arrayList2 = this.mOnHideHelpers;
        if (arrayList2 != null) {
            arrayList2.remove(view);
        }
        if (this.mScrollTarget == view) {
            this.mScrollTarget = null;
        }
    }

    public final void setTransition(MotionScene.Transition transition) {
        TouchResponse touchResponse;
        MotionScene motionScene = this.mScene;
        Objects.requireNonNull(motionScene);
        motionScene.mCurrentTransition = transition;
        if (!(transition == null || (touchResponse = transition.mTouchResponse) == null)) {
            touchResponse.setRTL(motionScene.mRtl);
        }
        int i = this.mCurrentState;
        MotionScene motionScene2 = this.mScene;
        Objects.requireNonNull(motionScene2);
        MotionScene.Transition transition2 = motionScene2.mCurrentTransition;
        int i2 = -1;
        if (i == (transition2 == null ? -1 : transition2.mConstraintSetEnd)) {
            this.mTransitionLastPosition = 1.0f;
            this.mTransitionPosition = 1.0f;
            this.mTransitionGoalPosition = 1.0f;
        } else {
            this.mTransitionLastPosition = 0.0f;
            this.mTransitionPosition = 0.0f;
            this.mTransitionGoalPosition = 0.0f;
        }
        this.mTransitionLastTime = -1L;
        int startId = this.mScene.getStartId();
        MotionScene motionScene3 = this.mScene;
        Objects.requireNonNull(motionScene3);
        MotionScene.Transition transition3 = motionScene3.mCurrentTransition;
        if (transition3 != null) {
            i2 = transition3.mConstraintSetEnd;
        }
        if (startId != this.mBeginState || i2 != this.mEndState) {
            this.mBeginState = startId;
            this.mEndState = i2;
            this.mScene.setTransition(startId, i2);
            this.mModel.initFrom(this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            Model model = this.mModel;
            int i3 = this.mBeginState;
            int i4 = this.mEndState;
            Objects.requireNonNull(model);
            model.mStartId = i3;
            model.mEndId = i4;
            this.mModel.reEvaluateState();
            rebuildScene();
            ArrayList<TransitionListener> arrayList = this.mTransitionListeners;
            if (arrayList != null) {
                Iterator<TransitionListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().onTransitionStarted();
                }
            }
        }
    }

    public MotionLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public MotionLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }
}
