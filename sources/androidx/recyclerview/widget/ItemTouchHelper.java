package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.controls.management.FavoritesModel$itemTouchHelperCallback$1;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    public Callback mCallback;
    public ArrayList mDistances;
    public long mDragScrollStartTimeInMs;
    public float mDx;
    public float mDy;
    public GestureDetectorCompat mGestureDetector;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
    public float mMaxSwipeVelocity;
    public RecyclerView mRecyclerView;
    public int mSelectedFlags;
    public float mSelectedStartX;
    public float mSelectedStartY;
    public int mSlop;
    public ArrayList mSwapTargets;
    public float mSwipeEscapeVelocity;
    public Rect mTmpRect;
    public VelocityTracker mVelocityTracker;
    public final ArrayList mPendingCleanup = new ArrayList();
    public final float[] mTmpPosition = new float[2];
    public RecyclerView.ViewHolder mSelected = null;
    public int mActivePointerId = -1;
    public int mActionState = 0;
    public List<RecoverAnimation> mRecoverAnimations = new ArrayList();
    public final AnonymousClass1 mScrollRunnable = new AnonymousClass1();
    public View mOverdrawChild = null;
    public final AnonymousClass2 mOnItemTouchListener = new RecyclerView.OnItemTouchListener() { // from class: androidx.recyclerview.widget.ItemTouchHelper.2
        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public final boolean onInterceptTouchEvent$1(MotionEvent motionEvent) {
            int findPointerIndex;
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            RecoverAnimation recoverAnimation = null;
            if (actionMasked == 0) {
                ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(0);
                ItemTouchHelper.this.mInitialTouchX = motionEvent.getX();
                ItemTouchHelper.this.mInitialTouchY = motionEvent.getY();
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                Objects.requireNonNull(itemTouchHelper);
                VelocityTracker velocityTracker = itemTouchHelper.mVelocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                }
                itemTouchHelper.mVelocityTracker = VelocityTracker.obtain();
                ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
                if (itemTouchHelper2.mSelected == null) {
                    if (!itemTouchHelper2.mRecoverAnimations.isEmpty()) {
                        View findChildView = itemTouchHelper2.findChildView(motionEvent);
                        int size = itemTouchHelper2.mRecoverAnimations.size() - 1;
                        while (true) {
                            if (size < 0) {
                                break;
                            }
                            RecoverAnimation recoverAnimation2 = itemTouchHelper2.mRecoverAnimations.get(size);
                            if (recoverAnimation2.mViewHolder.itemView == findChildView) {
                                recoverAnimation = recoverAnimation2;
                                break;
                            }
                            size--;
                        }
                    }
                    if (recoverAnimation != null) {
                        ItemTouchHelper itemTouchHelper3 = ItemTouchHelper.this;
                        itemTouchHelper3.mInitialTouchX -= recoverAnimation.mX;
                        itemTouchHelper3.mInitialTouchY -= recoverAnimation.mY;
                        itemTouchHelper3.endRecoverAnimation(recoverAnimation.mViewHolder, true);
                        if (ItemTouchHelper.this.mPendingCleanup.remove(recoverAnimation.mViewHolder.itemView)) {
                            ItemTouchHelper itemTouchHelper4 = ItemTouchHelper.this;
                            itemTouchHelper4.mCallback.clearView(itemTouchHelper4.mRecyclerView, recoverAnimation.mViewHolder);
                        }
                        ItemTouchHelper.this.select(recoverAnimation.mViewHolder, recoverAnimation.mActionState);
                        ItemTouchHelper itemTouchHelper5 = ItemTouchHelper.this;
                        itemTouchHelper5.updateDxDy(motionEvent, itemTouchHelper5.mSelectedFlags, 0);
                    }
                }
            } else if (actionMasked == 3 || actionMasked == 1) {
                ItemTouchHelper itemTouchHelper6 = ItemTouchHelper.this;
                itemTouchHelper6.mActivePointerId = -1;
                itemTouchHelper6.select(null, 0);
            } else {
                int i = ItemTouchHelper.this.mActivePointerId;
                if (i != -1 && (findPointerIndex = motionEvent.findPointerIndex(i)) >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, findPointerIndex);
                }
            }
            VelocityTracker velocityTracker2 = ItemTouchHelper.this.mVelocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.addMovement(motionEvent);
            }
            if (ItemTouchHelper.this.mSelected != null) {
                return true;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public final void onRequestDisallowInterceptTouchEvent(boolean z) {
            if (z) {
                ItemTouchHelper.this.select(null, 0);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public final void onTouchEvent(MotionEvent motionEvent) {
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
            VelocityTracker velocityTracker = ItemTouchHelper.this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
            if (ItemTouchHelper.this.mActivePointerId != -1) {
                int actionMasked = motionEvent.getActionMasked();
                int findPointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                if (findPointerIndex >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, findPointerIndex);
                }
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                RecyclerView.ViewHolder viewHolder = itemTouchHelper.mSelected;
                if (viewHolder != null) {
                    int i = 0;
                    if (actionMasked != 1) {
                        if (actionMasked != 2) {
                            if (actionMasked == 3) {
                                VelocityTracker velocityTracker2 = itemTouchHelper.mVelocityTracker;
                                if (velocityTracker2 != null) {
                                    velocityTracker2.clear();
                                }
                            } else if (actionMasked == 6) {
                                int actionIndex = motionEvent.getActionIndex();
                                int pointerId = motionEvent.getPointerId(actionIndex);
                                ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
                                if (pointerId == itemTouchHelper2.mActivePointerId) {
                                    if (actionIndex == 0) {
                                        i = 1;
                                    }
                                    itemTouchHelper2.mActivePointerId = motionEvent.getPointerId(i);
                                    ItemTouchHelper itemTouchHelper3 = ItemTouchHelper.this;
                                    itemTouchHelper3.updateDxDy(motionEvent, itemTouchHelper3.mSelectedFlags, actionIndex);
                                    return;
                                }
                                return;
                            } else {
                                return;
                            }
                        } else if (findPointerIndex >= 0) {
                            itemTouchHelper.updateDxDy(motionEvent, itemTouchHelper.mSelectedFlags, findPointerIndex);
                            ItemTouchHelper.this.moveIfNecessary(viewHolder);
                            ItemTouchHelper itemTouchHelper4 = ItemTouchHelper.this;
                            itemTouchHelper4.mRecyclerView.removeCallbacks(itemTouchHelper4.mScrollRunnable);
                            ItemTouchHelper.this.mScrollRunnable.run();
                            ItemTouchHelper.this.mRecyclerView.invalidate();
                            return;
                        } else {
                            return;
                        }
                    }
                    ItemTouchHelper.this.select(null, 0);
                    ItemTouchHelper.this.mActivePointerId = -1;
                }
            }
        }
    };

    /* renamed from: androidx.recyclerview.widget.ItemTouchHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x0089  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00cc  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00e5  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0100  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x0103 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:47:0x010f  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 309
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.ItemTouchHelper.AnonymousClass1.run():void");
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public static final AnonymousClass1 sDragScrollInterpolator = new Interpolator() { // from class: androidx.recyclerview.widget.ItemTouchHelper.Callback.1
            @Override // android.animation.TimeInterpolator
            public final float getInterpolation(float f) {
                return f * f * f * f * f;
            }
        };
        public static final AnonymousClass2 sDragViewScrollCapInterpolator = new AnonymousClass2();
        public int mCachedMaxScrollSpeed = -1;

        /* renamed from: androidx.recyclerview.widget.ItemTouchHelper$Callback$2  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass2 implements Interpolator {
            @Override // android.animation.TimeInterpolator
            public final float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return (f2 * f2 * f2 * f2 * f2) + 1.0f;
            }
        }

        public static int makeMovementFlags(int i, int i2) {
            return (i << 16) | (i2 << 8) | ((i2 | i) << 0);
        }

        public abstract boolean canDropOver(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2);

        public abstract int getMovementFlags(RecyclerView.ViewHolder viewHolder);

        public boolean isItemViewSwipeEnabled() {
            return !(this instanceof FavoritesModel$itemTouchHelperCallback$1);
        }

        public abstract boolean onMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2);

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public abstract void onSwiped();

        public static void onChildDraw(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, boolean z) {
            View view = viewHolder.itemView;
            if (z && view.getTag(2131428145) == null) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                Float valueOf = Float.valueOf(ViewCompat.Api21Impl.getElevation(view));
                int childCount = recyclerView.getChildCount();
                float f3 = 0.0f;
                for (int i = 0; i < childCount; i++) {
                    View childAt = recyclerView.getChildAt(i);
                    if (childAt != view) {
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        float elevation = ViewCompat.Api21Impl.getElevation(childAt);
                        if (elevation > f3) {
                            f3 = elevation;
                        }
                    }
                }
                ViewCompat.Api21Impl.setElevation(view, f3 + 1.0f);
                view.setTag(2131428145, valueOf);
            }
            view.setTranslationX(f);
            view.setTranslationY(f2);
        }

        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            Object tag = view.getTag(2131428145);
            if (tag instanceof Float) {
                float floatValue = ((Float) tag).floatValue();
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api21Impl.setElevation(view, floatValue);
            }
            view.setTag(2131428145, null);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
        }

        public final int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int i, int i2, long j) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(2131165820);
            }
            int i3 = this.mCachedMaxScrollSpeed;
            float f = 1.0f;
            int interpolation = (int) (sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (Math.abs(i2) * 1.0f) / i)) * ((int) Math.signum(i2)) * i3);
            if (j <= 2000) {
                f = ((float) j) / 2000.0f;
            }
            int i4 = (int) (f * f * f * f * f * interpolation);
            if (i4 != 0) {
                return i4;
            }
            if (i2 > 0) {
                return 1;
            }
            return -1;
        }
    }

    /* loaded from: classes.dex */
    public class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean mShouldReactToLongPress = true;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public ItemTouchHelperGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final void onLongPress(MotionEvent motionEvent) {
            View findChildView;
            RecyclerView.ViewHolder childViewHolder;
            int i;
            if (this.mShouldReactToLongPress && (findChildView = ItemTouchHelper.this.findChildView(motionEvent)) != null && (childViewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(findChildView)) != null) {
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                Callback callback = itemTouchHelper.mCallback;
                RecyclerView recyclerView = itemTouchHelper.mRecyclerView;
                Objects.requireNonNull(callback);
                int movementFlags = callback.getMovementFlags(childViewHolder);
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(recyclerView);
                int i2 = movementFlags & 3158064;
                boolean z = true;
                if (i2 != 0) {
                    int i3 = movementFlags & (~i2);
                    if (layoutDirection == 0) {
                        i = i2 >> 2;
                    } else {
                        int i4 = i2 >> 1;
                        i3 |= (-3158065) & i4;
                        i = (i4 & 3158064) >> 2;
                    }
                    movementFlags = i3 | i;
                }
                if ((16711680 & movementFlags) == 0) {
                    z = false;
                }
                if (z) {
                    int pointerId = motionEvent.getPointerId(0);
                    int i5 = ItemTouchHelper.this.mActivePointerId;
                    if (pointerId == i5) {
                        int findPointerIndex = motionEvent.findPointerIndex(i5);
                        float x = motionEvent.getX(findPointerIndex);
                        float y = motionEvent.getY(findPointerIndex);
                        ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
                        itemTouchHelper2.mInitialTouchX = x;
                        itemTouchHelper2.mInitialTouchY = y;
                        itemTouchHelper2.mDy = 0.0f;
                        itemTouchHelper2.mDx = 0.0f;
                        Objects.requireNonNull(itemTouchHelper2.mCallback);
                        ItemTouchHelper.this.select(childViewHolder, 2);
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class RecoverAnimation implements Animator.AnimatorListener {
        public final int mActionState;
        public boolean mIsPendingCleanup;
        public final float mStartDx;
        public final float mStartDy;
        public final float mTargetX;
        public final float mTargetY;
        public final ValueAnimator mValueAnimator;
        public final RecyclerView.ViewHolder mViewHolder;
        public float mX;
        public float mY;
        public boolean mOverridden = false;
        public boolean mEnded = false;
        public float mFraction = 0.0f;

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mFraction = 1.0f;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        public RecoverAnimation(RecyclerView.ViewHolder viewHolder, int i, float f, float f2, float f3, float f4) {
            this.mActionState = i;
            this.mViewHolder = viewHolder;
            this.mStartDx = f;
            this.mStartDy = f2;
            this.mTargetX = f3;
            this.mTargetY = f4;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mValueAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ItemTouchHelper.RecoverAnimation.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RecoverAnimation recoverAnimation = RecoverAnimation.this;
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    Objects.requireNonNull(recoverAnimation);
                    recoverAnimation.mFraction = animatedFraction;
                }
            });
            ofFloat.setTarget(viewHolder.itemView);
            ofFloat.addListener(this);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class SimpleCallback extends Callback {
        public int mDefaultSwipeDirs = 0;
        public int mDefaultDragDirs = 0;
    }

    /* loaded from: classes.dex */
    public interface ViewDropHandler {
        void prepareForDrop(View view, View view2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
    public final void onChildViewAttachedToWindow(View view) {
    }

    public static boolean hitTest(View view, float f, float f2, float f3, float f4) {
        if (f < f3 || f > f3 + view.getWidth() || f2 < f4 || f2 > f4 + view.getHeight()) {
            return false;
        }
        return true;
    }

    public final void attachToRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                recyclerView2.removeItemDecoration(this);
                RecyclerView recyclerView3 = this.mRecyclerView;
                AnonymousClass2 r1 = this.mOnItemTouchListener;
                Objects.requireNonNull(recyclerView3);
                recyclerView3.mOnItemTouchListeners.remove(r1);
                if (recyclerView3.mInterceptingOnItemTouchListener == r1) {
                    recyclerView3.mInterceptingOnItemTouchListener = null;
                }
                RecyclerView recyclerView4 = this.mRecyclerView;
                Objects.requireNonNull(recyclerView4);
                ArrayList arrayList = recyclerView4.mOnChildAttachStateListeners;
                if (arrayList != null) {
                    arrayList.remove(this);
                }
                int size = this.mRecoverAnimations.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
                    Objects.requireNonNull(recoverAnimation);
                    recoverAnimation.mValueAnimator.cancel();
                    this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
                }
                this.mRecoverAnimations.clear();
                this.mOverdrawChild = null;
                VelocityTracker velocityTracker = this.mVelocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                ItemTouchHelperGestureListener itemTouchHelperGestureListener = this.mItemTouchHelperGestureListener;
                if (itemTouchHelperGestureListener != null) {
                    itemTouchHelperGestureListener.mShouldReactToLongPress = false;
                    this.mItemTouchHelperGestureListener = null;
                }
                if (this.mGestureDetector != null) {
                    this.mGestureDetector = null;
                }
            }
            this.mRecyclerView = recyclerView;
            Resources resources = recyclerView.getResources();
            this.mSwipeEscapeVelocity = resources.getDimension(2131165822);
            this.mMaxSwipeVelocity = resources.getDimension(2131165821);
            this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
            this.mRecyclerView.addItemDecoration(this);
            RecyclerView recyclerView5 = this.mRecyclerView;
            AnonymousClass2 r0 = this.mOnItemTouchListener;
            Objects.requireNonNull(recyclerView5);
            recyclerView5.mOnItemTouchListeners.add(r0);
            RecyclerView recyclerView6 = this.mRecyclerView;
            Objects.requireNonNull(recyclerView6);
            if (recyclerView6.mOnChildAttachStateListeners == null) {
                recyclerView6.mOnChildAttachStateListeners = new ArrayList();
            }
            recyclerView6.mOnChildAttachStateListeners.add(this);
            this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
            this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), this.mItemTouchHelperGestureListener);
        }
    }

    public final int checkHorizontalSwipe(int i) {
        int i2;
        if ((i & 12) == 0) {
            return 0;
        }
        int i3 = 8;
        if (this.mDx > 0.0f) {
            i2 = 8;
        } else {
            i2 = 4;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null && this.mActivePointerId > -1) {
            Callback callback = this.mCallback;
            float f = this.mMaxSwipeVelocity;
            Objects.requireNonNull(callback);
            velocityTracker.computeCurrentVelocity(1000, f);
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (xVelocity <= 0.0f) {
                i3 = 4;
            }
            float abs = Math.abs(xVelocity);
            if ((i3 & i) != 0 && i2 == i3) {
                Callback callback2 = this.mCallback;
                float f2 = this.mSwipeEscapeVelocity;
                Objects.requireNonNull(callback2);
                if (abs >= f2 && abs > Math.abs(yVelocity)) {
                    return i3;
                }
            }
        }
        Objects.requireNonNull(this.mCallback);
        float width = this.mRecyclerView.getWidth() * 0.5f;
        if ((i & i2) == 0 || Math.abs(this.mDx) <= width) {
            return 0;
        }
        return i2;
    }

    public final void checkSelectForSwipe(int i, MotionEvent motionEvent, int i2) {
        int i3;
        View findChildView;
        if (this.mSelected == null && i == 2 && this.mActionState != 2 && this.mCallback.isItemViewSwipeEnabled()) {
            RecyclerView recyclerView = this.mRecyclerView;
            Objects.requireNonNull(recyclerView);
            if (recyclerView.mScrollState != 1) {
                RecyclerView recyclerView2 = this.mRecyclerView;
                Objects.requireNonNull(recyclerView2);
                RecyclerView.LayoutManager layoutManager = recyclerView2.mLayout;
                int i4 = this.mActivePointerId;
                RecyclerView.ViewHolder viewHolder = null;
                if (i4 != -1) {
                    int findPointerIndex = motionEvent.findPointerIndex(i4);
                    float abs = Math.abs(motionEvent.getX(findPointerIndex) - this.mInitialTouchX);
                    float abs2 = Math.abs(motionEvent.getY(findPointerIndex) - this.mInitialTouchY);
                    float f = this.mSlop;
                    if ((abs >= f || abs2 >= f) && ((abs <= abs2 || !layoutManager.canScrollHorizontally()) && ((abs2 <= abs || !layoutManager.canScrollVertically()) && (findChildView = findChildView(motionEvent)) != null))) {
                        viewHolder = this.mRecyclerView.getChildViewHolder(findChildView);
                    }
                }
                if (viewHolder != null) {
                    Callback callback = this.mCallback;
                    RecyclerView recyclerView3 = this.mRecyclerView;
                    Objects.requireNonNull(callback);
                    int movementFlags = callback.getMovementFlags(viewHolder);
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(recyclerView3);
                    int i5 = movementFlags & 3158064;
                    if (i5 != 0) {
                        int i6 = movementFlags & (~i5);
                        if (layoutDirection == 0) {
                            i3 = i5 >> 2;
                        } else {
                            int i7 = i5 >> 1;
                            i6 |= (-3158065) & i7;
                            i3 = (i7 & 3158064) >> 2;
                        }
                        movementFlags = i6 | i3;
                    }
                    int i8 = (movementFlags & 65280) >> 8;
                    if (i8 != 0) {
                        float x = motionEvent.getX(i2);
                        float y = motionEvent.getY(i2);
                        float f2 = x - this.mInitialTouchX;
                        float f3 = y - this.mInitialTouchY;
                        float abs3 = Math.abs(f2);
                        float abs4 = Math.abs(f3);
                        float f4 = this.mSlop;
                        if (abs3 >= f4 || abs4 >= f4) {
                            if (abs3 > abs4) {
                                if (f2 < 0.0f && (i8 & 4) == 0) {
                                    return;
                                }
                                if (f2 > 0.0f && (i8 & 8) == 0) {
                                    return;
                                }
                            } else if (f3 < 0.0f && (i8 & 1) == 0) {
                                return;
                            } else {
                                if (f3 > 0.0f && (i8 & 2) == 0) {
                                    return;
                                }
                            }
                            this.mDy = 0.0f;
                            this.mDx = 0.0f;
                            this.mActivePointerId = motionEvent.getPointerId(0);
                            select(viewHolder, 1);
                        }
                    }
                }
            }
        }
    }

    public final int checkVerticalSwipe(int i) {
        int i2;
        if ((i & 3) == 0) {
            return 0;
        }
        int i3 = 2;
        if (this.mDy > 0.0f) {
            i2 = 2;
        } else {
            i2 = 1;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null && this.mActivePointerId > -1) {
            Callback callback = this.mCallback;
            float f = this.mMaxSwipeVelocity;
            Objects.requireNonNull(callback);
            velocityTracker.computeCurrentVelocity(1000, f);
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (yVelocity <= 0.0f) {
                i3 = 1;
            }
            float abs = Math.abs(yVelocity);
            if ((i3 & i) != 0 && i3 == i2) {
                Callback callback2 = this.mCallback;
                float f2 = this.mSwipeEscapeVelocity;
                Objects.requireNonNull(callback2);
                if (abs >= f2 && abs > Math.abs(xVelocity)) {
                    return i3;
                }
            }
        }
        Objects.requireNonNull(this.mCallback);
        float height = this.mRecyclerView.getHeight() * 0.5f;
        if ((i & i2) == 0 || Math.abs(this.mDy) <= height) {
            return 0;
        }
        return i2;
    }

    public final void endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean z) {
        RecoverAnimation recoverAnimation;
        int size = this.mRecoverAnimations.size();
        do {
            size--;
            if (size >= 0) {
                recoverAnimation = this.mRecoverAnimations.get(size);
            } else {
                return;
            }
        } while (recoverAnimation.mViewHolder != viewHolder);
        recoverAnimation.mOverridden |= z;
        if (!recoverAnimation.mEnded) {
            recoverAnimation.mValueAnimator.cancel();
        }
        this.mRecoverAnimations.remove(size);
    }

    public final void getSelectedDxDy(float[] fArr) {
        if ((this.mSelectedFlags & 12) != 0) {
            fArr[0] = (this.mSelectedStartX + this.mDx) - this.mSelected.itemView.getLeft();
        } else {
            fArr[0] = this.mSelected.itemView.getTranslationX();
        }
        if ((this.mSelectedFlags & 3) != 0) {
            fArr[1] = (this.mSelectedStartY + this.mDy) - this.mSelected.itemView.getTop();
        } else {
            fArr[1] = this.mSelected.itemView.getTranslationY();
        }
    }

    public final void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        ArrayList arrayList;
        int bottom;
        int abs;
        int top;
        int abs2;
        int left;
        int abs3;
        int right;
        int abs4;
        int i;
        int i2;
        if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == 2) {
            Objects.requireNonNull(this.mCallback);
            int i3 = (int) (this.mSelectedStartX + this.mDx);
            int i4 = (int) (this.mSelectedStartY + this.mDy);
            if (Math.abs(i4 - viewHolder.itemView.getTop()) >= viewHolder.itemView.getHeight() * 0.5f || Math.abs(i3 - viewHolder.itemView.getLeft()) >= viewHolder.itemView.getWidth() * 0.5f) {
                ArrayList arrayList2 = this.mSwapTargets;
                if (arrayList2 == null) {
                    this.mSwapTargets = new ArrayList();
                    this.mDistances = new ArrayList();
                } else {
                    arrayList2.clear();
                    this.mDistances.clear();
                }
                Objects.requireNonNull(this.mCallback);
                int round = Math.round(this.mSelectedStartX + this.mDx) - 0;
                int round2 = Math.round(this.mSelectedStartY + this.mDy) - 0;
                int width = viewHolder.itemView.getWidth() + round + 0;
                int height = viewHolder.itemView.getHeight() + round2 + 0;
                int i5 = (round + width) / 2;
                int i6 = (round2 + height) / 2;
                RecyclerView recyclerView = this.mRecyclerView;
                Objects.requireNonNull(recyclerView);
                RecyclerView.LayoutManager layoutManager = recyclerView.mLayout;
                int childCount = layoutManager.getChildCount();
                int i7 = 0;
                while (i7 < childCount) {
                    View childAt = layoutManager.getChildAt(i7);
                    if (childAt != viewHolder.itemView && childAt.getBottom() >= round2 && childAt.getTop() <= height && childAt.getRight() >= round && childAt.getLeft() <= width) {
                        RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(childAt);
                        i2 = round;
                        if (this.mCallback.canDropOver(this.mSelected, childViewHolder)) {
                            int abs5 = Math.abs(i5 - ((childAt.getRight() + childAt.getLeft()) / 2));
                            int abs6 = Math.abs(i6 - ((childAt.getBottom() + childAt.getTop()) / 2));
                            int i8 = (abs6 * abs6) + (abs5 * abs5);
                            i = round2;
                            int i9 = 0;
                            int i10 = 0;
                            for (int size = this.mSwapTargets.size(); i9 < size && i8 > ((Integer) this.mDistances.get(i9)).intValue(); size = size) {
                                i10++;
                                i9++;
                            }
                            this.mSwapTargets.add(i10, childViewHolder);
                            this.mDistances.add(i10, Integer.valueOf(i8));
                            i7++;
                            round = i2;
                            round2 = i;
                        }
                    } else {
                        i2 = round;
                    }
                    i = round2;
                    i7++;
                    round = i2;
                    round2 = i;
                }
                ArrayList arrayList3 = this.mSwapTargets;
                if (arrayList3.size() != 0) {
                    Objects.requireNonNull(this.mCallback);
                    int width2 = viewHolder.itemView.getWidth() + i3;
                    int height2 = viewHolder.itemView.getHeight() + i4;
                    int left2 = i3 - viewHolder.itemView.getLeft();
                    int top2 = i4 - viewHolder.itemView.getTop();
                    int size2 = arrayList3.size();
                    int i11 = -1;
                    RecyclerView.ViewHolder viewHolder2 = null;
                    int i12 = 0;
                    while (i12 < size2) {
                        RecyclerView.ViewHolder viewHolder3 = (RecyclerView.ViewHolder) arrayList3.get(i12);
                        if (left2 <= 0 || (right = viewHolder3.itemView.getRight() - width2) >= 0) {
                            arrayList = arrayList3;
                        } else {
                            arrayList = arrayList3;
                            if (viewHolder3.itemView.getRight() > viewHolder.itemView.getRight() && (abs4 = Math.abs(right)) > i11) {
                                i11 = abs4;
                                viewHolder2 = viewHolder3;
                            }
                        }
                        if (left2 < 0 && (left = viewHolder3.itemView.getLeft() - i3) > 0 && viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft() && (abs3 = Math.abs(left)) > i11) {
                            i11 = abs3;
                            viewHolder2 = viewHolder3;
                        }
                        if (top2 < 0 && (top = viewHolder3.itemView.getTop() - i4) > 0 && viewHolder3.itemView.getTop() < viewHolder.itemView.getTop() && (abs2 = Math.abs(top)) > i11) {
                            i11 = abs2;
                            viewHolder2 = viewHolder3;
                        }
                        if (top2 > 0 && (bottom = viewHolder3.itemView.getBottom() - height2) < 0 && viewHolder3.itemView.getBottom() > viewHolder.itemView.getBottom() && (abs = Math.abs(bottom)) > i11) {
                            i11 = abs;
                            viewHolder2 = viewHolder3;
                        }
                        i12++;
                        arrayList3 = arrayList;
                    }
                    if (viewHolder2 == null) {
                        this.mSwapTargets.clear();
                        this.mDistances.clear();
                        return;
                    }
                    int absoluteAdapterPosition = viewHolder2.getAbsoluteAdapterPosition();
                    viewHolder.getAbsoluteAdapterPosition();
                    if (this.mCallback.onMove(viewHolder, viewHolder2)) {
                        Callback callback = this.mCallback;
                        RecyclerView recyclerView2 = this.mRecyclerView;
                        Objects.requireNonNull(callback);
                        Objects.requireNonNull(recyclerView2);
                        RecyclerView.LayoutManager layoutManager2 = recyclerView2.mLayout;
                        if (layoutManager2 instanceof ViewDropHandler) {
                            ((ViewDropHandler) layoutManager2).prepareForDrop(viewHolder.itemView, viewHolder2.itemView);
                            return;
                        }
                        if (layoutManager2.canScrollHorizontally()) {
                            if (layoutManager2.getDecoratedLeft(viewHolder2.itemView) <= recyclerView2.getPaddingLeft()) {
                                recyclerView2.scrollToPosition(absoluteAdapterPosition);
                            }
                            if (layoutManager2.getDecoratedRight(viewHolder2.itemView) >= recyclerView2.getWidth() - recyclerView2.getPaddingRight()) {
                                recyclerView2.scrollToPosition(absoluteAdapterPosition);
                            }
                        }
                        if (layoutManager2.canScrollVertically()) {
                            if (layoutManager2.getDecoratedTop(viewHolder2.itemView) <= recyclerView2.getPaddingTop()) {
                                recyclerView2.scrollToPosition(absoluteAdapterPosition);
                            }
                            if (layoutManager2.getDecoratedBottom(viewHolder2.itemView) >= recyclerView2.getHeight() - recyclerView2.getPaddingBottom()) {
                                recyclerView2.scrollToPosition(absoluteAdapterPosition);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void onDraw(Canvas canvas, RecyclerView recyclerView) {
        float f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float[] fArr = this.mTmpPosition;
            float f3 = fArr[0];
            f2 = fArr[1];
            f = f3;
        } else {
            f = 0.0f;
        }
        Callback callback = this.mCallback;
        RecyclerView.ViewHolder viewHolder = this.mSelected;
        List<RecoverAnimation> list = this.mRecoverAnimations;
        Objects.requireNonNull(callback);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            RecoverAnimation recoverAnimation = list.get(i);
            Objects.requireNonNull(recoverAnimation);
            float f4 = recoverAnimation.mStartDx;
            float f5 = recoverAnimation.mTargetX;
            if (f4 == f5) {
                recoverAnimation.mX = recoverAnimation.mViewHolder.itemView.getTranslationX();
            } else {
                recoverAnimation.mX = MotionController$$ExternalSyntheticOutline0.m(f5, f4, recoverAnimation.mFraction, f4);
            }
            float f6 = recoverAnimation.mStartDy;
            float f7 = recoverAnimation.mTargetY;
            if (f6 == f7) {
                recoverAnimation.mY = recoverAnimation.mViewHolder.itemView.getTranslationY();
            } else {
                recoverAnimation.mY = MotionController$$ExternalSyntheticOutline0.m(f7, f6, recoverAnimation.mFraction, f6);
            }
            int save = canvas.save();
            Callback.onChildDraw(recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, false);
            canvas.restoreToCount(save);
        }
        if (viewHolder != null) {
            int save2 = canvas.save();
            Callback.onChildDraw(recyclerView, viewHolder, f, f2, true);
            canvas.restoreToCount(save2);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
        boolean z = false;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float[] fArr = this.mTmpPosition;
            float f = fArr[0];
            float f2 = fArr[1];
        }
        Callback callback = this.mCallback;
        RecyclerView.ViewHolder viewHolder = this.mSelected;
        List<RecoverAnimation> list = this.mRecoverAnimations;
        Objects.requireNonNull(callback);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int save = canvas.save();
            View view = list.get(i).mViewHolder.itemView;
            canvas.restoreToCount(save);
        }
        if (viewHolder != null) {
            canvas.restoreToCount(canvas.save());
        }
        for (int i2 = size - 1; i2 >= 0; i2--) {
            RecoverAnimation recoverAnimation = list.get(i2);
            boolean z2 = recoverAnimation.mEnded;
            if (z2 && !recoverAnimation.mIsPendingCleanup) {
                list.remove(i2);
            } else if (!z2) {
                z = true;
            }
        }
        if (z) {
            recyclerView.invalidate();
        }
    }

    public final void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00b2, code lost:
        if (r0 == 0) goto L_0x00b4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b4, code lost:
        r0 = r1 << 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b7, code lost:
        r0 = r1 << 1;
        r2 = r2 | (r0 & (-789517));
        r0 = (r0 & 789516) << 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00be, code lost:
        r2 = r0 | r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c5, code lost:
        if (r2 > 0) goto L_0x00e9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00e5, code lost:
        if (r0 == 0) goto L_0x00b4;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x020f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void select(androidx.recyclerview.widget.RecyclerView.ViewHolder r24, int r25) {
        /*
            Method dump skipped, instructions count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.ItemTouchHelper.select(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.recyclerview.widget.ItemTouchHelper$2] */
    public ItemTouchHelper(Callback callback) {
        this.mCallback = callback;
    }

    public final View findChildView(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        RecyclerView.ViewHolder viewHolder = this.mSelected;
        if (viewHolder != null) {
            View view = viewHolder.itemView;
            if (hitTest(view, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return view;
            }
        }
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            View view2 = recoverAnimation.mViewHolder.itemView;
            if (hitTest(view2, x, y, recoverAnimation.mX, recoverAnimation.mY)) {
                return view2;
            }
        }
        RecyclerView recyclerView = this.mRecyclerView;
        Objects.requireNonNull(recyclerView);
        int childCount = recyclerView.mChildHelper.getChildCount();
        while (true) {
            childCount--;
            if (childCount < 0) {
                return null;
            }
            View childAt = recyclerView.mChildHelper.getChildAt(childCount);
            float translationX = childAt.getTranslationX();
            float translationY = childAt.getTranslationY();
            if (x >= childAt.getLeft() + translationX && x <= childAt.getRight() + translationX && y >= childAt.getTop() + translationY && y <= childAt.getBottom() + translationY) {
                return childAt;
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
    public final void onChildViewDetachedFromWindow(View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (childViewHolder != null) {
            RecyclerView.ViewHolder viewHolder = this.mSelected;
            if (viewHolder == null || childViewHolder != viewHolder) {
                endRecoverAnimation(childViewHolder, false);
                if (this.mPendingCleanup.remove(childViewHolder.itemView)) {
                    this.mCallback.clearView(this.mRecyclerView, childViewHolder);
                    return;
                }
                return;
            }
            select(null, 0);
        }
    }

    public final void updateDxDy(MotionEvent motionEvent, int i, int i2) {
        float x = motionEvent.getX(i2);
        float y = motionEvent.getY(i2);
        float f = x - this.mInitialTouchX;
        this.mDx = f;
        this.mDy = y - this.mInitialTouchY;
        if ((i & 4) == 0) {
            this.mDx = Math.max(0.0f, f);
        }
        if ((i & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((i & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((i & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        rect.setEmpty();
    }
}
