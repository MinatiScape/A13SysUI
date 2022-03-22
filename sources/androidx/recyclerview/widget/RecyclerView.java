package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat$Api31Impl;
import androidx.customview.view.AbsSavedState;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.leanback.widget.GridLayoutManager;
import androidx.recyclerview.R$styleable;
import androidx.recyclerview.widget.AdapterHelper;
import androidx.recyclerview.widget.ChildHelper;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import androidx.recyclerview.widget.ViewInfoStore;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class RecyclerView extends ViewGroup implements NestedScrollingChild {
    public static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    public RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    public final AccessibilityManager mAccessibilityManager;
    public Adapter mAdapter;
    public AdapterHelper mAdapterHelper;
    public boolean mAdapterUpdateDuringMeasure;
    public EdgeEffect mBottomGlow;
    public ChildDrawingOrderCallback mChildDrawingOrderCallback;
    public ChildHelper mChildHelper;
    public boolean mClipToPadding;
    public boolean mDataSetHasChangedAfterLayout;
    public boolean mDispatchItemsChangedEvent;
    public int mDispatchScrollCounter;
    public int mEatenAccessibilityChangeFlags;
    public StretchEdgeEffectFactory mEdgeEffectFactory;
    public boolean mFirstLayoutComplete;
    public GapWorker mGapWorker;
    public boolean mHasFixedSize;
    public boolean mIgnoreMotionEventTillDown;
    public int mInitialTouchX;
    public int mInitialTouchY;
    public int mInterceptRequestLayoutDepth;
    public OnItemTouchListener mInterceptingOnItemTouchListener;
    public boolean mIsAttached;
    public ItemAnimator mItemAnimator;
    public ItemAnimatorRestoreListener mItemAnimatorListener;
    public AnonymousClass2 mItemAnimatorRunner;
    public final ArrayList<ItemDecoration> mItemDecorations;
    public boolean mItemsAddedOrRemoved;
    public boolean mItemsChanged;
    public int mLastAutoMeasureNonExactMeasuredHeight;
    public int mLastAutoMeasureNonExactMeasuredWidth;
    public boolean mLastAutoMeasureSkippedDueToExact;
    public int mLastTouchX;
    public int mLastTouchY;
    public LayoutManager mLayout;
    public int mLayoutOrScrollCounter;
    public boolean mLayoutSuppressed;
    public boolean mLayoutWasDefered;
    public EdgeEffect mLeftGlow;
    public final int mMaxFlingVelocity;
    public final int mMinFlingVelocity;
    public final int[] mMinMaxLayoutPositions;
    public final int[] mNestedOffsets;
    public final RecyclerViewDataObserver mObserver;
    public ArrayList mOnChildAttachStateListeners;
    public OnFlingListener mOnFlingListener;
    public final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    public final List<ViewHolder> mPendingAccessibilityImportanceChange;
    public SavedState mPendingSavedState;
    public boolean mPostedAnimatorRunner;
    public GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
    public boolean mPreserveFocusAfterLayout;
    public final Recycler mRecycler;
    public final ArrayList mRecyclerListeners;
    public final int[] mReusableIntPair;
    public EdgeEffect mRightGlow;
    public float mScaledHorizontalScrollFactor;
    public float mScaledVerticalScrollFactor;
    public ArrayList mScrollListeners;
    public final int[] mScrollOffset;
    public int mScrollPointerId;
    public int mScrollState;
    public NestedScrollingChildHelper mScrollingChildHelper;
    public final State mState;
    public final Rect mTempRect;
    public final Rect mTempRect2;
    public final RectF mTempRectF;
    public EdgeEffect mTopGlow;
    public int mTouchSlop;
    public final AnonymousClass1 mUpdateChildViewsRunnable;
    public VelocityTracker mVelocityTracker;
    public final ViewFlinger mViewFlinger;
    public final AnonymousClass4 mViewInfoProcessCallback;
    public final ViewInfoStore mViewInfoStore;
    public static final int[] NESTED_SCROLLING_ATTRS = {16843830};
    public static final AnonymousClass3 sQuinticInterpolator = new Interpolator() { // from class: androidx.recyclerview.widget.RecyclerView.3
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2 * f2 * f2) + 1.0f;
        }
    };
    public static final StretchEdgeEffectFactory sDefaultEdgeEffectFactory = new StretchEdgeEffectFactory();

    /* renamed from: androidx.recyclerview.widget.RecyclerView$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 {
        public AnonymousClass4() {
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x002f  */
        /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void processAppeared(androidx.recyclerview.widget.RecyclerView.ViewHolder r8, androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemHolderInfo r9, androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemHolderInfo r10) {
            /*
                r7 = this;
                androidx.recyclerview.widget.RecyclerView r7 = androidx.recyclerview.widget.RecyclerView.this
                java.util.Objects.requireNonNull(r7)
                r0 = 0
                r8.setIsRecyclable(r0)
                androidx.recyclerview.widget.RecyclerView$ItemAnimator r0 = r7.mItemAnimator
                r1 = r0
                androidx.recyclerview.widget.SimpleItemAnimator r1 = (androidx.recyclerview.widget.SimpleItemAnimator) r1
                if (r9 == 0) goto L_0x0029
                java.util.Objects.requireNonNull(r1)
                int r3 = r9.left
                int r5 = r10.left
                if (r3 != r5) goto L_0x001f
                int r0 = r9.top
                int r2 = r10.top
                if (r0 == r2) goto L_0x0029
            L_0x001f:
                int r4 = r9.top
                int r6 = r10.top
                r2 = r8
                boolean r8 = r1.animateMove(r2, r3, r4, r5, r6)
                goto L_0x002d
            L_0x0029:
                r1.animateAdd(r8)
                r8 = 1
            L_0x002d:
                if (r8 == 0) goto L_0x0032
                r7.postAnimationRunner()
            L_0x0032:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.AnonymousClass4.processAppeared(androidx.recyclerview.widget.RecyclerView$ViewHolder, androidx.recyclerview.widget.RecyclerView$ItemAnimator$ItemHolderInfo, androidx.recyclerview.widget.RecyclerView$ItemAnimator$ItemHolderInfo):void");
        }

        public final void processDisappeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo, ItemAnimator.ItemHolderInfo itemHolderInfo2) {
            int i;
            int i2;
            boolean z;
            RecyclerView.this.mRecycler.unscrapView(viewHolder);
            RecyclerView recyclerView = RecyclerView.this;
            Objects.requireNonNull(recyclerView);
            recyclerView.addAnimatingView(viewHolder);
            viewHolder.setIsRecyclable(false);
            SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.mItemAnimator;
            Objects.requireNonNull(simpleItemAnimator);
            int i3 = itemHolderInfo.left;
            int i4 = itemHolderInfo.top;
            View view = viewHolder.itemView;
            if (itemHolderInfo2 == null) {
                i = view.getLeft();
            } else {
                i = itemHolderInfo2.left;
            }
            if (itemHolderInfo2 == null) {
                i2 = view.getTop();
            } else {
                i2 = itemHolderInfo2.top;
            }
            if (viewHolder.isRemoved() || (i3 == i && i4 == i2)) {
                simpleItemAnimator.animateRemove(viewHolder);
                z = true;
            } else {
                view.layout(i, i2, view.getWidth() + i, view.getHeight() + i2);
                z = simpleItemAnimator.animateMove(viewHolder, i3, i4, i, i2);
            }
            if (z) {
                recyclerView.postAnimationRunner();
            }
        }
    }

    /* renamed from: androidx.recyclerview.widget.RecyclerView$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 implements ChildHelper.Callback {
        public AnonymousClass5() {
        }

        public final int getChildCount() {
            return RecyclerView.this.getChildCount();
        }

        public final void removeViewAt(int i) {
            View childAt = RecyclerView.this.getChildAt(i);
            if (childAt != null) {
                RecyclerView.this.dispatchChildDetached(childAt);
                childAt.clearAnimation();
            }
            RecyclerView.this.removeViewAt(i);
        }
    }

    /* renamed from: androidx.recyclerview.widget.RecyclerView$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass6 implements AdapterHelper.Callback {
        public AnonymousClass6() {
        }

        public final void dispatchUpdate(AdapterHelper.UpdateOp updateOp) {
            int i = updateOp.cmd;
            if (i == 1) {
                RecyclerView.this.mLayout.onItemsAdded(updateOp.positionStart, updateOp.itemCount);
            } else if (i == 2) {
                RecyclerView.this.mLayout.onItemsRemoved(updateOp.positionStart, updateOp.itemCount);
            } else if (i == 4) {
                RecyclerView recyclerView = RecyclerView.this;
                recyclerView.mLayout.onItemsUpdated(recyclerView, updateOp.positionStart, updateOp.itemCount);
            } else if (i == 8) {
                RecyclerView.this.mLayout.onItemsMoved(updateOp.positionStart, updateOp.itemCount);
            }
        }

        public final void markViewHoldersUpdated(int i, int i2, Object obj) {
            int i3;
            int i4;
            RecyclerView recyclerView = RecyclerView.this;
            Objects.requireNonNull(recyclerView);
            int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount();
            int i5 = i2 + i;
            for (int i6 = 0; i6 < unfilteredChildCount; i6++) {
                View unfilteredChildAt = recyclerView.mChildHelper.getUnfilteredChildAt(i6);
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(unfilteredChildAt);
                if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && (i4 = childViewHolderInt.mPosition) >= i && i4 < i5) {
                    childViewHolderInt.addFlags(2);
                    childViewHolderInt.addChangePayload(obj);
                    ((LayoutParams) unfilteredChildAt.getLayoutParams()).mInsetsDirty = true;
                }
            }
            Recycler recycler = recyclerView.mRecycler;
            Objects.requireNonNull(recycler);
            int size = recycler.mCachedViews.size();
            while (true) {
                size--;
                if (size >= 0) {
                    ViewHolder viewHolder = recycler.mCachedViews.get(size);
                    if (viewHolder != null && (i3 = viewHolder.mPosition) >= i && i3 < i5) {
                        viewHolder.addFlags(2);
                        recycler.recycleCachedViewAt(size);
                    }
                } else {
                    RecyclerView.this.mItemsChanged = true;
                    return;
                }
            }
        }

        public final void offsetPositionsForAdd(int i, int i2) {
            RecyclerView recyclerView = RecyclerView.this;
            Objects.requireNonNull(recyclerView);
            int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount();
            for (int i3 = 0; i3 < unfilteredChildCount; i3++) {
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i3));
                if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= i) {
                    childViewHolderInt.offsetPosition(i2, false);
                    recyclerView.mState.mStructureChanged = true;
                }
            }
            Recycler recycler = recyclerView.mRecycler;
            Objects.requireNonNull(recycler);
            int size = recycler.mCachedViews.size();
            for (int i4 = 0; i4 < size; i4++) {
                ViewHolder viewHolder = recycler.mCachedViews.get(i4);
                if (viewHolder != null && viewHolder.mPosition >= i) {
                    viewHolder.offsetPosition(i2, false);
                }
            }
            recyclerView.requestLayout();
            RecyclerView.this.mItemsAddedOrRemoved = true;
        }

        public final void offsetPositionsForMove(int i, int i2) {
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            RecyclerView recyclerView = RecyclerView.this;
            Objects.requireNonNull(recyclerView);
            int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount();
            int i10 = -1;
            if (i < i2) {
                i5 = i;
                i4 = i2;
                i3 = -1;
            } else {
                i4 = i;
                i5 = i2;
                i3 = 1;
            }
            for (int i11 = 0; i11 < unfilteredChildCount; i11++) {
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i11));
                if (childViewHolderInt != null && (i9 = childViewHolderInt.mPosition) >= i5 && i9 <= i4) {
                    if (i9 == i) {
                        childViewHolderInt.offsetPosition(i2 - i, false);
                    } else {
                        childViewHolderInt.offsetPosition(i3, false);
                    }
                    recyclerView.mState.mStructureChanged = true;
                }
            }
            Recycler recycler = recyclerView.mRecycler;
            Objects.requireNonNull(recycler);
            if (i < i2) {
                i7 = i;
                i6 = i2;
            } else {
                i6 = i;
                i7 = i2;
                i10 = 1;
            }
            int size = recycler.mCachedViews.size();
            for (int i12 = 0; i12 < size; i12++) {
                ViewHolder viewHolder = recycler.mCachedViews.get(i12);
                if (viewHolder != null && (i8 = viewHolder.mPosition) >= i7 && i8 <= i6) {
                    if (i8 == i) {
                        viewHolder.offsetPosition(i2 - i, false);
                    } else {
                        viewHolder.offsetPosition(i10, false);
                    }
                }
            }
            recyclerView.requestLayout();
            RecyclerView.this.mItemsAddedOrRemoved = true;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Adapter<VH extends ViewHolder> {
        public final AdapterDataObservable mObservable = new AdapterDataObservable();
        public boolean mHasStableIds = false;
        public StateRestorationPolicy mStateRestorationPolicy = StateRestorationPolicy.ALLOW;

        /* loaded from: classes.dex */
        public enum StateRestorationPolicy {
            ALLOW,
            /* JADX INFO: Fake field, exist only in values array */
            PREVENT_WHEN_EMPTY,
            /* JADX INFO: Fake field, exist only in values array */
            PREVENT
        }

        public abstract int getItemCount();

        public long getItemId(int i) {
            return -1L;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i);

        public void onDetachedFromRecyclerView() {
        }

        public boolean onFailedToRecycleView(VH vh) {
            return false;
        }

        public void onViewAttachedToWindow(VH vh) {
        }

        public void onViewRecycled(VH vh) {
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int i) {
            AdapterDataObservable adapterDataObservable = this.mObservable;
            Objects.requireNonNull(adapterDataObservable);
            adapterDataObservable.notifyItemRangeChanged(i, 1, null);
        }

        public final void notifyItemMoved(int i, int i2) {
            this.mObservable.notifyItemMoved(i, i2);
        }

        public final void notifyItemRangeInserted(int i, int i2) {
            this.mObservable.notifyItemRangeInserted(i, i2);
        }

        public final void notifyItemRangeRemoved(int i, int i2) {
            this.mObservable.notifyItemRangeRemoved(i, i2);
        }

        public final void registerAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver(adapterDataObserver);
        }

        public final void setHasStableIds(boolean z) {
            if (!this.mObservable.hasObservers()) {
                this.mHasStableIds = z;
                return;
            }
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        }

        public void onBindViewHolder(VH vh, int i, List<Object> list) {
            onBindViewHolder(vh, i);
        }
    }

    /* loaded from: classes.dex */
    public static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public final boolean hasObservers() {
            return !((Observable) this).mObservers.isEmpty();
        }

        public final void notifyChanged() {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) ((Observable) this).mObservers.get(size)).onChanged();
            }
        }

        public final void notifyItemMoved(int i, int i2) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) ((Observable) this).mObservers.get(size)).onItemRangeMoved(i, i2);
            }
        }

        public final void notifyItemRangeChanged(int i, int i2, Object obj) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) ((Observable) this).mObservers.get(size)).onItemRangeChanged(i, i2, obj);
            }
        }

        public final void notifyItemRangeInserted(int i, int i2) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) ((Observable) this).mObservers.get(size)).onItemRangeInserted(i, i2);
            }
        }

        public final void notifyItemRangeRemoved(int i, int i2) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) ((Observable) this).mObservers.get(size)).onItemRangeRemoved(i, i2);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder();
    }

    /* loaded from: classes.dex */
    public static class EdgeEffectFactory {
    }

    /* loaded from: classes.dex */
    public static abstract class ItemAnimator {
        public ItemAnimatorListener mListener = null;
        public ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
        public long mAddDuration = 120;
        public long mRemoveDuration = 120;
        public long mMoveDuration = 250;
        public long mChangeDuration = 250;

        /* loaded from: classes.dex */
        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        /* loaded from: classes.dex */
        public interface ItemAnimatorListener {
        }

        /* loaded from: classes.dex */
        public static class ItemHolderInfo {
            public int left;
            public int top;

            public final ItemHolderInfo setFrom(ViewHolder viewHolder) {
                View view = viewHolder.itemView;
                this.left = view.getLeft();
                this.top = view.getTop();
                view.getRight();
                view.getBottom();
                return this;
            }
        }

        public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public abstract boolean isRunning();

        public abstract void runPendingAnimations();

        public static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int i = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((i & 4) != 0) {
                return i;
            }
            int i2 = viewHolder.mOldPosition;
            int absoluteAdapterPosition = viewHolder.getAbsoluteAdapterPosition();
            if (i2 == -1 || absoluteAdapterPosition == -1 || i2 == absoluteAdapterPosition) {
                return i;
            }
            return i | 2048;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder, List<Object> list) {
            if (!((SimpleItemAnimator) this).mSupportsChangeAnimations || viewHolder.isInvalid()) {
                return true;
            }
            return false;
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            boolean z;
            ItemAnimatorListener itemAnimatorListener = this.mListener;
            if (itemAnimatorListener != null) {
                ItemAnimatorRestoreListener itemAnimatorRestoreListener = (ItemAnimatorRestoreListener) itemAnimatorListener;
                boolean z2 = true;
                viewHolder.setIsRecyclable(true);
                if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                    viewHolder.mShadowedHolder = null;
                }
                viewHolder.mShadowingHolder = null;
                if ((viewHolder.mFlags & 16) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    RecyclerView recyclerView = RecyclerView.this;
                    View view = viewHolder.itemView;
                    Objects.requireNonNull(recyclerView);
                    recyclerView.startInterceptRequestLayout();
                    ChildHelper childHelper = recyclerView.mChildHelper;
                    Objects.requireNonNull(childHelper);
                    AnonymousClass5 r5 = (AnonymousClass5) childHelper.mCallback;
                    Objects.requireNonNull(r5);
                    int indexOfChild = RecyclerView.this.indexOfChild(view);
                    if (indexOfChild == -1) {
                        childHelper.unhideViewInternal(view);
                    } else if (childHelper.mBucket.get(indexOfChild)) {
                        childHelper.mBucket.remove(indexOfChild);
                        childHelper.unhideViewInternal(view);
                        ((AnonymousClass5) childHelper.mCallback).removeViewAt(indexOfChild);
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                        recyclerView.mRecycler.unscrapView(childViewHolderInt);
                        recyclerView.mRecycler.recycleViewHolderInternal(childViewHolderInt);
                    }
                    recyclerView.stopInterceptRequestLayout(!z2);
                    if (!z2 && viewHolder.isTmpDetached()) {
                        RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                    }
                }
            }
        }

        public final void dispatchAnimationsFinished() {
            int size = this.mFinishedListeners.size();
            for (int i = 0; i < size; i++) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }
    }

    /* loaded from: classes.dex */
    public class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        public ItemAnimatorRestoreListener() {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class LayoutManager {
        public ChildHelper mChildHelper;
        public int mHeight;
        public int mHeightMode;
        public ViewBoundsCheck mHorizontalBoundCheck;
        public final AnonymousClass1 mHorizontalBoundCheckCallback;
        public int mPrefetchMaxCountObserved;
        public boolean mPrefetchMaxObservedInInitialPrefetch;
        public RecyclerView mRecyclerView;
        public SmoothScroller mSmoothScroller;
        public ViewBoundsCheck mVerticalBoundCheck;
        public final AnonymousClass2 mVerticalBoundCheckCallback;
        public int mWidth;
        public int mWidthMode;
        public boolean mRequestedSimpleAnimations = false;
        public boolean mIsAttachedToWindow = false;
        public boolean mMeasurementCacheEnabled = true;
        public boolean mItemPrefetchEnabled = true;

        /* loaded from: classes.dex */
        public interface LayoutPrefetchRegistry {
        }

        /* loaded from: classes.dex */
        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
            if (r5 == 1073741824) goto L_0x0020;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static int getChildMeasureSpec(int r4, int r5, int r6, int r7, boolean r8) {
            /*
                int r4 = r4 - r6
                r6 = 0
                int r4 = java.lang.Math.max(r6, r4)
                r0 = -2
                r1 = -1
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r3 = 1073741824(0x40000000, float:2.0)
                if (r8 == 0) goto L_0x001a
                if (r7 < 0) goto L_0x0011
                goto L_0x001c
            L_0x0011:
                if (r7 != r1) goto L_0x002f
                if (r5 == r2) goto L_0x0020
                if (r5 == 0) goto L_0x002f
                if (r5 == r3) goto L_0x0020
                goto L_0x002f
            L_0x001a:
                if (r7 < 0) goto L_0x001e
            L_0x001c:
                r5 = r3
                goto L_0x0031
            L_0x001e:
                if (r7 != r1) goto L_0x0022
            L_0x0020:
                r7 = r4
                goto L_0x0031
            L_0x0022:
                if (r7 != r0) goto L_0x002f
                if (r5 == r2) goto L_0x002c
                if (r5 != r3) goto L_0x0029
                goto L_0x002c
            L_0x0029:
                r7 = r4
                r5 = r6
                goto L_0x0031
            L_0x002c:
                r7 = r4
                r5 = r2
                goto L_0x0031
            L_0x002f:
                r5 = r6
                r7 = r5
            L_0x0031:
                int r4 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r5)
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.LayoutManager.getChildMeasureSpec(int, int, int, int, boolean):int");
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public boolean checkLayoutParams(LayoutParams layoutParams) {
            return layoutParams != null;
        }

        public void collectAdjacentPrefetchPositions(int i, int i2, State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public void collectInitialPrefetchPositions(int i, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            if (layoutParams instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) layoutParams);
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
            }
            return new LayoutParams(layoutParams);
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            return -1;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            return -1;
        }

        public boolean isAutoMeasureEnabled() {
            return false;
        }

        public void onAdapterChanged(Adapter adapter, Adapter adapter2) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int i, int i2) {
            return false;
        }

        public void onDetachedFromWindow(RecyclerView recyclerView) {
        }

        public View onFocusSearchFailed(View view, int i, Recycler recycler, State state) {
            return null;
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        }

        public View onInterceptFocusSearch(View view, int i) {
            return null;
        }

        public void onItemsAdded(int i, int i2) {
        }

        public void onItemsChanged() {
        }

        public void onItemsMoved(int i, int i2) {
        }

        public void onItemsRemoved(int i, int i2) {
        }

        public void onItemsUpdated(int i, int i2) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int i, int i2) {
            onItemsUpdated(i, i2);
        }

        public void onLayoutCompleted(State state) {
        }

        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onScrollStateChanged(int i) {
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x00b3, code lost:
            if (r9 == false) goto L_0x00ba;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean requestChildRectangleOnScreen(androidx.recyclerview.widget.RecyclerView r10, android.view.View r11, android.graphics.Rect r12, boolean r13, boolean r14) {
            /*
                r9 = this;
                r0 = 2
                int[] r0 = new int[r0]
                int r1 = r9.getPaddingLeft()
                int r2 = r9.getPaddingTop()
                int r3 = r9.mWidth
                int r4 = r9.getPaddingRight()
                int r3 = r3 - r4
                int r4 = r9.mHeight
                int r5 = r9.getPaddingBottom()
                int r4 = r4 - r5
                int r5 = r11.getLeft()
                int r6 = r12.left
                int r5 = r5 + r6
                int r6 = r11.getScrollX()
                int r5 = r5 - r6
                int r6 = r11.getTop()
                int r7 = r12.top
                int r6 = r6 + r7
                int r11 = r11.getScrollY()
                int r6 = r6 - r11
                int r11 = r12.width()
                int r11 = r11 + r5
                int r12 = r12.height()
                int r12 = r12 + r6
                int r5 = r5 - r1
                r1 = 0
                int r7 = java.lang.Math.min(r1, r5)
                int r6 = r6 - r2
                int r2 = java.lang.Math.min(r1, r6)
                int r11 = r11 - r3
                int r3 = java.lang.Math.max(r1, r11)
                int r12 = r12 - r4
                int r12 = java.lang.Math.max(r1, r12)
                int r4 = r9.getLayoutDirection()
                r8 = 1
                if (r4 != r8) goto L_0x005f
                if (r3 == 0) goto L_0x005a
                goto L_0x0067
            L_0x005a:
                int r3 = java.lang.Math.max(r7, r11)
                goto L_0x0067
            L_0x005f:
                if (r7 == 0) goto L_0x0062
                goto L_0x0066
            L_0x0062:
                int r7 = java.lang.Math.min(r5, r3)
            L_0x0066:
                r3 = r7
            L_0x0067:
                if (r2 == 0) goto L_0x006a
                goto L_0x006e
            L_0x006a:
                int r2 = java.lang.Math.min(r6, r12)
            L_0x006e:
                r0[r1] = r3
                r0[r8] = r2
                r11 = r0[r1]
                r12 = r0[r8]
                if (r14 == 0) goto L_0x00b5
                android.view.View r14 = r10.getFocusedChild()
                if (r14 != 0) goto L_0x0080
            L_0x007e:
                r9 = r1
                goto L_0x00b3
            L_0x0080:
                int r0 = r9.getPaddingLeft()
                int r2 = r9.getPaddingTop()
                int r3 = r9.mWidth
                int r4 = r9.getPaddingRight()
                int r3 = r3 - r4
                int r4 = r9.mHeight
                int r5 = r9.getPaddingBottom()
                int r4 = r4 - r5
                androidx.recyclerview.widget.RecyclerView r5 = r9.mRecyclerView
                android.graphics.Rect r5 = r5.mTempRect
                r9.getDecoratedBoundsWithMargins(r14, r5)
                int r9 = r5.left
                int r9 = r9 - r11
                if (r9 >= r3) goto L_0x007e
                int r9 = r5.right
                int r9 = r9 - r11
                if (r9 <= r0) goto L_0x007e
                int r9 = r5.top
                int r9 = r9 - r12
                if (r9 >= r4) goto L_0x007e
                int r9 = r5.bottom
                int r9 = r9 - r12
                if (r9 > r2) goto L_0x00b2
                goto L_0x007e
            L_0x00b2:
                r9 = r8
            L_0x00b3:
                if (r9 == 0) goto L_0x00ba
            L_0x00b5:
                if (r11 != 0) goto L_0x00bb
                if (r12 == 0) goto L_0x00ba
                goto L_0x00bb
            L_0x00ba:
                return r1
            L_0x00bb:
                if (r13 == 0) goto L_0x00c1
                r10.scrollBy(r11, r12)
                goto L_0x00c4
            L_0x00c1:
                r10.smoothScrollBy(r11, r12)
            L_0x00c4:
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.LayoutManager.requestChildRectangleOnScreen(androidx.recyclerview.widget.RecyclerView, android.view.View, android.graphics.Rect, boolean, boolean):boolean");
        }

        public int scrollHorizontallyBy(int i, Recycler recycler, State state) {
            return 0;
        }

        public void scrollToPosition(int i) {
        }

        public int scrollVerticallyBy(int i, Recycler recycler, State state) {
            return 0;
        }

        public boolean shouldMeasureTwice() {
            return false;
        }

        public boolean supportsPredictiveItemAnimations() {
            return this instanceof GridLayoutManager;
        }

        public static Properties getProperties(Context context, AttributeSet attributeSet, int i, int i2) {
            Properties properties = new Properties();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RecyclerView, i, i2);
            properties.orientation = obtainStyledAttributes.getInt(0, 1);
            properties.spanCount = obtainStyledAttributes.getInt(10, 1);
            properties.reverseLayout = obtainStyledAttributes.getBoolean(9, false);
            properties.stackFromEnd = obtainStyledAttributes.getBoolean(11, false);
            obtainStyledAttributes.recycle();
            return properties;
        }

        public void assertNotInLayoutOrScroll(String str) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.assertNotInLayoutOrScroll(str);
            }
        }

        public void calculateItemDecorationsForChild(View view, Rect rect) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                rect.set(0, 0, 0, 0);
            } else {
                rect.set(recyclerView.getItemDecorInsetsForChild(view));
            }
        }

        public final View findContainingItemView(View view) {
            View findContainingItemView;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || (findContainingItemView = recyclerView.findContainingItemView(view)) == null || this.mChildHelper.isHidden(findContainingItemView)) {
                return null;
            }
            return findContainingItemView;
        }

        public final View getChildAt(int i) {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildAt(i);
            }
            return null;
        }

        public final int getChildCount() {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildCount();
            }
            return 0;
        }

        public void getDecoratedBoundsWithMargins(View view, Rect rect) {
            int[] iArr = RecyclerView.NESTED_SCROLLING_ATTRS;
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Rect rect2 = layoutParams.mDecorInsets;
            rect.set((view.getLeft() - rect2.left) - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, (view.getTop() - rect2.top) - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, view.getRight() + rect2.right + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, view.getBottom() + rect2.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        }

        public final int getItemCount() {
            Adapter adapter;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                Objects.requireNonNull(recyclerView);
                adapter = recyclerView.mAdapter;
            } else {
                adapter = null;
            }
            if (adapter != null) {
                return adapter.getItemCount();
            }
            return 0;
        }

        public final int getLayoutDirection() {
            RecyclerView recyclerView = this.mRecyclerView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            return ViewCompat.Api17Impl.getLayoutDirection(recyclerView);
        }

        public final int getPaddingBottom() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingBottom();
            }
            return 0;
        }

        public final int getPaddingLeft() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingLeft();
            }
            return 0;
        }

        public final int getPaddingRight() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingRight();
            }
            return 0;
        }

        public final int getPaddingTop() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingTop();
            }
            return 0;
        }

        public final boolean hasFocus() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || !recyclerView.hasFocus()) {
                return false;
            }
            return true;
        }

        public final boolean isSmoothScrolling() {
            SmoothScroller smoothScroller = this.mSmoothScroller;
            if (smoothScroller != null) {
                Objects.requireNonNull(smoothScroller);
                if (smoothScroller.mRunning) {
                    return true;
                }
            }
            return false;
        }

        public void offsetChildrenHorizontal(int i) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                Objects.requireNonNull(recyclerView);
                int childCount = recyclerView.mChildHelper.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    recyclerView.mChildHelper.getChildAt(i2).offsetLeftAndRight(i);
                }
            }
        }

        public void offsetChildrenVertical(int i) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                Objects.requireNonNull(recyclerView);
                int childCount = recyclerView.mChildHelper.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    recyclerView.mChildHelper.getChildAt(i2).offsetTopAndBottom(i);
                }
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            RecyclerView recyclerView = this.mRecyclerView;
            Recycler recycler = recyclerView.mRecycler;
            State state = recyclerView.mState;
            if (recyclerView != null && accessibilityEvent != null) {
                boolean z = true;
                if (!recyclerView.canScrollVertically(1) && !this.mRecyclerView.canScrollVertically(-1) && !this.mRecyclerView.canScrollHorizontally(-1) && !this.mRecyclerView.canScrollHorizontally(1)) {
                    z = false;
                }
                accessibilityEvent.setScrollable(z);
                Adapter adapter = this.mRecyclerView.mAdapter;
                if (adapter != null) {
                    accessibilityEvent.setItemCount(adapter.getItemCount());
                }
            }
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), 0));
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onMeasure(Recycler recycler, State state, int i, int i2) {
            this.mRecyclerView.defaultOnMeasure(i, i2);
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x006a A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean performAccessibilityAction(androidx.recyclerview.widget.RecyclerView.Recycler r2, androidx.recyclerview.widget.RecyclerView.State r3, int r4, android.os.Bundle r5) {
            /*
                r1 = this;
                androidx.recyclerview.widget.RecyclerView r2 = r1.mRecyclerView
                r3 = 0
                if (r2 != 0) goto L_0x0006
                return r3
            L_0x0006:
                r5 = 4096(0x1000, float:5.74E-42)
                r0 = 1
                if (r4 == r5) goto L_0x003e
                r5 = 8192(0x2000, float:1.14794E-41)
                if (r4 == r5) goto L_0x0012
                r2 = r3
                r4 = r2
                goto L_0x0068
            L_0x0012:
                r4 = -1
                boolean r2 = r2.canScrollVertically(r4)
                if (r2 == 0) goto L_0x0027
                int r2 = r1.mHeight
                int r5 = r1.getPaddingTop()
                int r2 = r2 - r5
                int r5 = r1.getPaddingBottom()
                int r2 = r2 - r5
                int r2 = -r2
                goto L_0x0028
            L_0x0027:
                r2 = r3
            L_0x0028:
                androidx.recyclerview.widget.RecyclerView r5 = r1.mRecyclerView
                boolean r4 = r5.canScrollHorizontally(r4)
                if (r4 == 0) goto L_0x0067
                int r4 = r1.mWidth
                int r5 = r1.getPaddingLeft()
                int r4 = r4 - r5
                int r5 = r1.getPaddingRight()
                int r4 = r4 - r5
                int r4 = -r4
                goto L_0x0068
            L_0x003e:
                boolean r2 = r2.canScrollVertically(r0)
                if (r2 == 0) goto L_0x0051
                int r2 = r1.mHeight
                int r4 = r1.getPaddingTop()
                int r2 = r2 - r4
                int r4 = r1.getPaddingBottom()
                int r2 = r2 - r4
                goto L_0x0052
            L_0x0051:
                r2 = r3
            L_0x0052:
                androidx.recyclerview.widget.RecyclerView r4 = r1.mRecyclerView
                boolean r4 = r4.canScrollHorizontally(r0)
                if (r4 == 0) goto L_0x0067
                int r4 = r1.mWidth
                int r5 = r1.getPaddingLeft()
                int r4 = r4 - r5
                int r5 = r1.getPaddingRight()
                int r4 = r4 - r5
                goto L_0x0068
            L_0x0067:
                r4 = r3
            L_0x0068:
                if (r2 != 0) goto L_0x006d
                if (r4 != 0) goto L_0x006d
                return r3
            L_0x006d:
                androidx.recyclerview.widget.RecyclerView r1 = r1.mRecyclerView
                r1.smoothScrollBy$1(r4, r2, r0)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.LayoutManager.performAccessibilityAction(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, int, android.os.Bundle):boolean");
        }

        public final void removeAndRecycleView(View view, Recycler recycler) {
            ChildHelper childHelper = this.mChildHelper;
            Objects.requireNonNull(childHelper);
            AnonymousClass5 r0 = (AnonymousClass5) childHelper.mCallback;
            Objects.requireNonNull(r0);
            int indexOfChild = RecyclerView.this.indexOfChild(view);
            if (indexOfChild >= 0) {
                if (childHelper.mBucket.remove(indexOfChild)) {
                    childHelper.unhideViewInternal(view);
                }
                ((AnonymousClass5) childHelper.mCallback).removeViewAt(indexOfChild);
            }
            recycler.recycleView(view);
        }

        public final void requestLayout() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.requestLayout();
            }
        }

        public final void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            } else {
                this.mRecyclerView = recyclerView;
                this.mChildHelper = recyclerView.mChildHelper;
                this.mWidth = recyclerView.getWidth();
                this.mHeight = recyclerView.getHeight();
            }
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
        }

        public final boolean shouldReMeasureChild(View view, int i, int i2, LayoutParams layoutParams) {
            if (!this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getMeasuredWidth(), i, ((ViewGroup.MarginLayoutParams) layoutParams).width) || !isMeasurementUpToDate(view.getMeasuredHeight(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height)) {
                return true;
            }
            return false;
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, int i) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            SmoothScroller smoothScroller2 = this.mSmoothScroller;
            if (!(smoothScroller2 == null || smoothScroller == smoothScroller2 || !smoothScroller2.mRunning)) {
                smoothScroller2.stop();
            }
            this.mSmoothScroller = smoothScroller;
            RecyclerView recyclerView = this.mRecyclerView;
            ViewFlinger viewFlinger = recyclerView.mViewFlinger;
            Objects.requireNonNull(viewFlinger);
            RecyclerView.this.removeCallbacks(viewFlinger);
            viewFlinger.mOverScroller.abortAnimation();
            if (smoothScroller.mStarted) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("An instance of ");
                m.append(smoothScroller.getClass().getSimpleName());
                m.append(" was started more than once. Each instance of");
                m.append(smoothScroller.getClass().getSimpleName());
                m.append(" is intended to only be used once. You should create a new instance for each use.");
                Log.w("RecyclerView", m.toString());
            }
            smoothScroller.mRecyclerView = recyclerView;
            smoothScroller.mLayoutManager = this;
            int i = smoothScroller.mTargetPosition;
            if (i != -1) {
                recyclerView.mState.mTargetPosition = i;
                smoothScroller.mRunning = true;
                smoothScroller.mPendingInitialRun = true;
                smoothScroller.mTargetView = recyclerView.mLayout.findViewByPosition(i);
                smoothScroller.mRecyclerView.mViewFlinger.postOnAnimation();
                smoothScroller.mStarted = true;
                return;
            }
            throw new IllegalArgumentException("Invalid target position");
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [androidx.recyclerview.widget.RecyclerView$LayoutManager$1, androidx.recyclerview.widget.ViewBoundsCheck$Callback] */
        /* JADX WARN: Type inference failed for: r1v0, types: [androidx.recyclerview.widget.RecyclerView$LayoutManager$2, androidx.recyclerview.widget.ViewBoundsCheck$Callback] */
        /* JADX WARN: Unknown variable types count: 2 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public LayoutManager() {
            /*
                r3 = this;
                r3.<init>()
                androidx.recyclerview.widget.RecyclerView$LayoutManager$1 r0 = new androidx.recyclerview.widget.RecyclerView$LayoutManager$1
                r0.<init>()
                r3.mHorizontalBoundCheckCallback = r0
                androidx.recyclerview.widget.RecyclerView$LayoutManager$2 r1 = new androidx.recyclerview.widget.RecyclerView$LayoutManager$2
                r1.<init>()
                r3.mVerticalBoundCheckCallback = r1
                androidx.recyclerview.widget.ViewBoundsCheck r2 = new androidx.recyclerview.widget.ViewBoundsCheck
                r2.<init>(r0)
                r3.mHorizontalBoundCheck = r2
                androidx.recyclerview.widget.ViewBoundsCheck r0 = new androidx.recyclerview.widget.ViewBoundsCheck
                r0.<init>(r1)
                r3.mVerticalBoundCheck = r0
                r0 = 0
                r3.mRequestedSimpleAnimations = r0
                r3.mIsAttachedToWindow = r0
                r0 = 1
                r3.mMeasurementCacheEnabled = r0
                r3.mItemPrefetchEnabled = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.LayoutManager.<init>():void");
        }

        public static int chooseSize(int i, int i2, int i3) {
            int mode = View.MeasureSpec.getMode(i);
            int size = View.MeasureSpec.getSize(i);
            if (mode == Integer.MIN_VALUE) {
                return Math.min(size, Math.max(i2, i3));
            }
            if (mode != 1073741824) {
                return Math.max(i2, i3);
            }
            return size;
        }

        public static int getDecoratedMeasuredHeight(View view) {
            Rect rect = ((LayoutParams) view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + rect.top + rect.bottom;
        }

        public static int getDecoratedMeasuredWidth(View view) {
            Rect rect = ((LayoutParams) view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + rect.left + rect.right;
        }

        public static int getPosition(View view) {
            return ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        }

        public static boolean isMeasurementUpToDate(int i, int i2, int i3) {
            int mode = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i2);
            if (i3 > 0 && i != i3) {
                return false;
            }
            if (mode != Integer.MIN_VALUE) {
                if (mode == 0) {
                    return true;
                }
                if (mode == 1073741824 && size == i) {
                    return true;
                }
                return false;
            } else if (size >= i) {
                return true;
            } else {
                return false;
            }
        }

        public static void layoutDecoratedWithMargins(View view, int i, int i2, int i3, int i4) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Rect rect = layoutParams.mDecorInsets;
            view.layout(i + rect.left + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, i2 + rect.top + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, (i3 - rect.right) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, (i4 - rect.bottom) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        }

        public final void addViewInt(View view, int i, boolean z) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (z || childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (childViewHolderInt.wasReturnedFromScrap() || childViewHolderInt.isScrap()) {
                if (childViewHolderInt.isScrap()) {
                    childViewHolderInt.mScrapContainer.unscrapView(childViewHolderInt);
                } else {
                    childViewHolderInt.mFlags &= -33;
                }
                this.mChildHelper.attachViewToParent(view, i, view.getLayoutParams(), false);
            } else {
                int i2 = -1;
                if (view.getParent() == this.mRecyclerView) {
                    int indexOfChild = this.mChildHelper.indexOfChild(view);
                    if (i == -1) {
                        i = this.mChildHelper.getChildCount();
                    }
                    if (indexOfChild == -1) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                        m.append(this.mRecyclerView.indexOfChild(view));
                        throw new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this.mRecyclerView, m));
                    } else if (indexOfChild != i) {
                        LayoutManager layoutManager = this.mRecyclerView.mLayout;
                        Objects.requireNonNull(layoutManager);
                        View childAt = layoutManager.getChildAt(indexOfChild);
                        if (childAt != null) {
                            layoutManager.getChildAt(indexOfChild);
                            layoutManager.mChildHelper.detachViewFromParent(indexOfChild);
                            LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
                            ViewHolder childViewHolderInt2 = RecyclerView.getChildViewHolderInt(childAt);
                            if (childViewHolderInt2.isRemoved()) {
                                layoutManager.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt2);
                            } else {
                                layoutManager.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt2);
                            }
                            layoutManager.mChildHelper.attachViewToParent(childAt, i, layoutParams2, childViewHolderInt2.isRemoved());
                        } else {
                            throw new IllegalArgumentException("Cannot move a child from non-existing index:" + indexOfChild + layoutManager.mRecyclerView.toString());
                        }
                    }
                } else {
                    this.mChildHelper.addView(view, i, false);
                    layoutParams.mInsetsDirty = true;
                    SmoothScroller smoothScroller = this.mSmoothScroller;
                    if (smoothScroller != null && smoothScroller.mRunning) {
                        Objects.requireNonNull(smoothScroller);
                        Objects.requireNonNull(smoothScroller.mRecyclerView);
                        ViewHolder childViewHolderInt3 = RecyclerView.getChildViewHolderInt(view);
                        if (childViewHolderInt3 != null) {
                            i2 = childViewHolderInt3.getLayoutPosition();
                        }
                        if (i2 == smoothScroller.mTargetPosition) {
                            smoothScroller.mTargetView = view;
                        }
                    }
                }
            }
            if (layoutParams.mPendingInvalidate) {
                childViewHolderInt.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }

        public final void detachAndScrapAttachedViews(Recycler recycler) {
            int childCount = getChildCount();
            while (true) {
                childCount--;
                if (childCount >= 0) {
                    scrapOrRecycleView(recycler, childCount, getChildAt(childCount));
                } else {
                    return;
                }
            }
        }

        public View findViewByPosition(int i) {
            int childCount = getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(childAt);
                if (childViewHolderInt != null && childViewHolderInt.getLayoutPosition() == i && !childViewHolderInt.shouldIgnore()) {
                    State state = this.mRecyclerView.mState;
                    Objects.requireNonNull(state);
                    if (state.mInPreLayout || !childViewHolderInt.isRemoved()) {
                        return childAt;
                    }
                }
            }
            return null;
        }

        public int getDecoratedBottom(View view) {
            return ((LayoutParams) view.getLayoutParams()).mDecorInsets.bottom + view.getBottom();
        }

        public int getDecoratedLeft(View view) {
            return view.getLeft() - ((LayoutParams) view.getLayoutParams()).mDecorInsets.left;
        }

        public int getDecoratedRight(View view) {
            return ((LayoutParams) view.getLayoutParams()).mDecorInsets.right + view.getRight();
        }

        public int getDecoratedTop(View view) {
            return view.getTop() - ((LayoutParams) view.getLayoutParams()).mDecorInsets.top;
        }

        public final void getTransformedBoundingBox(View view, Rect rect) {
            Matrix matrix;
            Rect rect2 = ((LayoutParams) view.getLayoutParams()).mDecorInsets;
            rect.set(-rect2.left, -rect2.top, view.getWidth() + rect2.right, view.getHeight() + rect2.bottom);
            if (!(this.mRecyclerView == null || (matrix = view.getMatrix()) == null || matrix.isIdentity())) {
                RectF rectF = this.mRecyclerView.mTempRectF;
                rectF.set(rect);
                matrix.mapRect(rectF);
                rect.set((int) Math.floor(rectF.left), (int) Math.floor(rectF.top), (int) Math.ceil(rectF.right), (int) Math.ceil(rectF.bottom));
            }
            rect.offset(view.getLeft(), view.getTop());
        }

        public final void onInitializeAccessibilityNodeInfoForItem(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && !this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                RecyclerView recyclerView = this.mRecyclerView;
                onInitializeAccessibilityNodeInfoForItem(recyclerView.mRecycler, recyclerView.mState, view, accessibilityNodeInfoCompat);
            }
        }

        public boolean onRequestChildFocus(RecyclerView recyclerView, View view, View view2) {
            if (isSmoothScrolling() || recyclerView.isComputingLayout()) {
                return true;
            }
            return false;
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            int childCount = getChildCount();
            while (true) {
                childCount--;
                if (childCount < 0) {
                    return;
                }
                if (!RecyclerView.getChildViewHolderInt(getChildAt(childCount)).shouldIgnore()) {
                    View childAt = getChildAt(childCount);
                    removeViewAt(childCount);
                    recycler.recycleView(childAt);
                }
            }
        }

        public final void removeAndRecycleScrapInt(Recycler recycler) {
            Objects.requireNonNull(recycler);
            int size = recycler.mAttachedScrap.size();
            for (int i = size - 1; i >= 0; i--) {
                View view = recycler.mAttachedScrap.get(i).itemView;
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (!childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.setIsRecyclable(false);
                    if (childViewHolderInt.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(view, false);
                    }
                    ItemAnimator itemAnimator = this.mRecyclerView.mItemAnimator;
                    if (itemAnimator != null) {
                        itemAnimator.endAnimation(childViewHolderInt);
                    }
                    childViewHolderInt.setIsRecyclable(true);
                    ViewHolder childViewHolderInt2 = RecyclerView.getChildViewHolderInt(view);
                    childViewHolderInt2.mScrapContainer = null;
                    childViewHolderInt2.mInChangeScrap = false;
                    childViewHolderInt2.mFlags &= -33;
                    recycler.recycleViewHolderInternal(childViewHolderInt2);
                }
            }
            recycler.mAttachedScrap.clear();
            ArrayList<ViewHolder> arrayList = recycler.mChangedScrap;
            if (arrayList != null) {
                arrayList.clear();
            }
            if (size > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        public final void removeViewAt(int i) {
            if (getChildAt(i) != null) {
                ChildHelper childHelper = this.mChildHelper;
                Objects.requireNonNull(childHelper);
                int offset = childHelper.getOffset(i);
                AnonymousClass5 r0 = (AnonymousClass5) childHelper.mCallback;
                Objects.requireNonNull(r0);
                View childAt = RecyclerView.this.getChildAt(offset);
                if (childAt != null) {
                    if (childHelper.mBucket.remove(offset)) {
                        childHelper.unhideViewInternal(childAt);
                    }
                    ((AnonymousClass5) childHelper.mCallback).removeViewAt(offset);
                }
            }
        }

        public final void scrapOrRecycleView(Recycler recycler, int i, View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved()) {
                    Adapter adapter = this.mRecyclerView.mAdapter;
                    Objects.requireNonNull(adapter);
                    if (!adapter.mHasStableIds) {
                        removeViewAt(i);
                        recycler.recycleViewHolderInternal(childViewHolderInt);
                        return;
                    }
                }
                getChildAt(i);
                this.mChildHelper.detachViewFromParent(i);
                recycler.scrapView(view);
                ViewInfoStore viewInfoStore = this.mRecyclerView.mViewInfoStore;
                Objects.requireNonNull(viewInfoStore);
                viewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
        }

        public final void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
            setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }

        public final void setMeasureSpecs(int i, int i2) {
            this.mWidth = View.MeasureSpec.getSize(i);
            int mode = View.MeasureSpec.getMode(i);
            this.mWidthMode = mode;
            if (mode == 0) {
                int[] iArr = RecyclerView.NESTED_SCROLLING_ATTRS;
            }
            this.mHeight = View.MeasureSpec.getSize(i2);
            int mode2 = View.MeasureSpec.getMode(i2);
            this.mHeightMode = mode2;
            if (mode2 == 0) {
                int[] iArr2 = RecyclerView.NESTED_SCROLLING_ATTRS;
            }
        }

        public void setMeasuredDimension(Rect rect, int i, int i2) {
            int paddingRight = getPaddingRight() + getPaddingLeft() + rect.width();
            int paddingBottom = getPaddingBottom() + getPaddingTop() + rect.height();
            RecyclerView recyclerView = this.mRecyclerView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            this.mRecyclerView.setMeasuredDimension(chooseSize(i, paddingRight, ViewCompat.Api16Impl.getMinimumWidth(recyclerView)), chooseSize(i2, paddingBottom, ViewCompat.Api16Impl.getMinimumHeight(this.mRecyclerView)));
        }

        public final void setMeasuredDimensionFromChildren(int i, int i2) {
            int childCount = getChildCount();
            if (childCount == 0) {
                this.mRecyclerView.defaultOnMeasure(i, i2);
                return;
            }
            int i3 = Integer.MIN_VALUE;
            int i4 = Integer.MAX_VALUE;
            int i5 = Integer.MAX_VALUE;
            int i6 = Integer.MIN_VALUE;
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt = getChildAt(i7);
                Rect rect = this.mRecyclerView.mTempRect;
                getDecoratedBoundsWithMargins(childAt, rect);
                int i8 = rect.left;
                if (i8 < i4) {
                    i4 = i8;
                }
                int i9 = rect.right;
                if (i9 > i3) {
                    i3 = i9;
                }
                int i10 = rect.top;
                if (i10 < i5) {
                    i5 = i10;
                }
                int i11 = rect.bottom;
                if (i11 > i6) {
                    i6 = i11;
                }
            }
            this.mRecyclerView.mTempRect.set(i4, i5, i3, i6);
            setMeasuredDimension(this.mRecyclerView.mTempRect, i, i2);
        }

        public final boolean shouldMeasureChild(View view, int i, int i2, LayoutParams layoutParams) {
            if (view.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getWidth(), i, ((ViewGroup.MarginLayoutParams) layoutParams).width) || !isMeasurementUpToDate(view.getHeight(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height)) {
                return true;
            }
            return false;
        }

        public LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
            return new LayoutParams(context, attributeSet);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean z) {
            return requestChildRectangleOnScreen(recyclerView, view, rect, z, false);
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public final Rect mDecorInsets = new Rect();
        public boolean mInsetsDirty = true;
        public boolean mPendingInvalidate = false;
        public ViewHolder mViewHolder;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public final int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        public final boolean isItemChanged() {
            ViewHolder viewHolder = this.mViewHolder;
            Objects.requireNonNull(viewHolder);
            if ((viewHolder.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams) layoutParams);
        }
    }

    /* loaded from: classes.dex */
    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View view);

        void onChildViewDetachedFromWindow(View view);
    }

    /* loaded from: classes.dex */
    public static abstract class OnFlingListener {
    }

    /* loaded from: classes.dex */
    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent$1(MotionEvent motionEvent);

        void onRequestDisallowInterceptTouchEvent(boolean z);

        void onTouchEvent(MotionEvent motionEvent);
    }

    /* loaded from: classes.dex */
    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        }
    }

    /* loaded from: classes.dex */
    public static class RecycledViewPool {
        public SparseArray<ScrapData> mScrap = new SparseArray<>();
        public int mAttachCount = 0;

        /* loaded from: classes.dex */
        public static class ScrapData {
            public final ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();
            public int mMaxScrap = 5;
            public long mCreateRunningAverageNs = 0;
            public long mBindRunningAverageNs = 0;
        }

        public final ScrapData getScrapDataForType(int i) {
            ScrapData scrapData = this.mScrap.get(i);
            if (scrapData != null) {
                return scrapData;
            }
            ScrapData scrapData2 = new ScrapData();
            this.mScrap.put(i, scrapData2);
            return scrapData2;
        }
    }

    /* loaded from: classes.dex */
    public final class Recycler {
        public final ArrayList<ViewHolder> mAttachedScrap;
        public RecycledViewPool mRecyclerPool;
        public final List<ViewHolder> mUnmodifiableAttachedScrap;
        public ArrayList<ViewHolder> mChangedScrap = null;
        public final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();
        public int mRequestedCacheMax = 2;
        public int mViewCacheMax = 2;

        public Recycler() {
            ArrayList<ViewHolder> arrayList = new ArrayList<>();
            this.mAttachedScrap = arrayList;
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(arrayList);
        }

        public final int convertPreLayoutPositionToPostLayout(int i) {
            if (i < 0 || i >= RecyclerView.this.mState.getItemCount()) {
                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("invalid position ", i, ". State item count is ");
                m.append(RecyclerView.this.mState.getItemCount());
                throw new IndexOutOfBoundsException(ChildHelper$$ExternalSyntheticOutline0.m(RecyclerView.this, m));
            }
            State state = RecyclerView.this.mState;
            Objects.requireNonNull(state);
            if (!state.mInPreLayout) {
                return i;
            }
            AdapterHelper adapterHelper = RecyclerView.this.mAdapterHelper;
            Objects.requireNonNull(adapterHelper);
            return adapterHelper.findPositionOffset(i, 0);
        }

        public final void recycleAndClearCachedViews() {
            for (int size = this.mCachedViews.size() - 1; size >= 0; size--) {
                recycleCachedViewAt(size);
            }
            this.mCachedViews.clear();
            int[] iArr = RecyclerView.NESTED_SCROLLING_ATTRS;
            GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = RecyclerView.this.mPrefetchRegistry;
            Objects.requireNonNull(layoutPrefetchRegistryImpl);
            int[] iArr2 = layoutPrefetchRegistryImpl.mPrefetchArray;
            if (iArr2 != null) {
                Arrays.fill(iArr2, -1);
            }
            layoutPrefetchRegistryImpl.mCount = 0;
        }

        public final void recycleCachedViewAt(int i) {
            addViewHolderToRecycledViewPool(this.mCachedViews.get(i), true);
            this.mCachedViews.remove(i);
        }

        /* JADX WARN: Code restructure failed: missing block: B:151:0x032c, code lost:
            r7 = r10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:228:0x04a4, code lost:
            if (r7.isInvalid() == false) goto L_0x04e5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:237:0x04e3, code lost:
            if (r6 == false) goto L_0x04e5;
         */
        /* JADX WARN: Removed duplicated region for block: B:121:0x026d  */
        /* JADX WARN: Removed duplicated region for block: B:205:0x043e  */
        /* JADX WARN: Removed duplicated region for block: B:222:0x0495  */
        /* JADX WARN: Removed duplicated region for block: B:231:0x04c9  */
        /* JADX WARN: Removed duplicated region for block: B:241:0x04f4  */
        /* JADX WARN: Removed duplicated region for block: B:242:0x04f6  */
        /* JADX WARN: Removed duplicated region for block: B:244:0x04f9  */
        /* JADX WARN: Removed duplicated region for block: B:250:0x051d  */
        /* JADX WARN: Removed duplicated region for block: B:259:0x055a  */
        /* JADX WARN: Removed duplicated region for block: B:267:0x0578  */
        /* JADX WARN: Removed duplicated region for block: B:285:0x05be  */
        /* JADX WARN: Removed duplicated region for block: B:288:0x05ca  */
        /* JADX WARN: Removed duplicated region for block: B:292:0x05d5  */
        /* JADX WARN: Removed duplicated region for block: B:293:0x05e3  */
        /* JADX WARN: Removed duplicated region for block: B:299:0x0600 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0095  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x009c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final androidx.recyclerview.widget.RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int r18, long r19) {
            /*
                Method dump skipped, instructions count: 1575
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.Recycler.tryGetViewHolderForPositionByDeadline(int, long):androidx.recyclerview.widget.RecyclerView$ViewHolder");
        }

        public final void unscrapView(ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            } else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.mFlags &= -33;
        }

        public final void updateViewCacheSize() {
            int i;
            LayoutManager layoutManager = RecyclerView.this.mLayout;
            if (layoutManager != null) {
                i = layoutManager.mPrefetchMaxCountObserved;
            } else {
                i = 0;
            }
            this.mViewCacheMax = this.mRequestedCacheMax + i;
            for (int size = this.mCachedViews.size() - 1; size >= 0 && this.mCachedViews.size() > this.mViewCacheMax; size--) {
                recycleCachedViewAt(size);
            }
        }

        public final void addViewHolderToRecycledViewPool(ViewHolder viewHolder, boolean z) {
            AccessibilityDelegateCompat accessibilityDelegateCompat;
            RecyclerView.clearNestedRecyclerViewIfNotNested(viewHolder);
            View view = viewHolder.itemView;
            RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate = RecyclerView.this.mAccessibilityDelegate;
            if (recyclerViewAccessibilityDelegate != null) {
                AccessibilityDelegateCompat itemDelegate = recyclerViewAccessibilityDelegate.getItemDelegate();
                if (itemDelegate instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
                    RecyclerViewAccessibilityDelegate.ItemDelegate itemDelegate2 = (RecyclerViewAccessibilityDelegate.ItemDelegate) itemDelegate;
                    Objects.requireNonNull(itemDelegate2);
                    accessibilityDelegateCompat = (AccessibilityDelegateCompat) itemDelegate2.mOriginalItemDelegates.remove(view);
                } else {
                    accessibilityDelegateCompat = null;
                }
                ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
            }
            if (z) {
                Objects.requireNonNull(RecyclerView.this);
                int size = RecyclerView.this.mRecyclerListeners.size();
                for (int i = 0; i < size; i++) {
                    ((RecyclerListener) RecyclerView.this.mRecyclerListeners.get(i)).onViewRecycled(viewHolder);
                }
                Adapter adapter = RecyclerView.this.mAdapter;
                if (adapter != null) {
                    adapter.onViewRecycled(viewHolder);
                }
                RecyclerView recyclerView = RecyclerView.this;
                if (recyclerView.mState != null) {
                    recyclerView.mViewInfoStore.removeViewHolder(viewHolder);
                }
            }
            viewHolder.mBindingAdapter = null;
            viewHolder.mOwnerRecyclerView = null;
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            RecycledViewPool recycledViewPool = this.mRecyclerPool;
            Objects.requireNonNull(recycledViewPool);
            int i2 = viewHolder.mItemViewType;
            ArrayList<ViewHolder> arrayList = recycledViewPool.getScrapDataForType(i2).mScrapHeap;
            if (recycledViewPool.mScrap.get(i2).mMaxScrap > arrayList.size()) {
                viewHolder.resetInternal();
                arrayList.add(viewHolder);
            }
        }

        public final void recycleView(View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (childViewHolderInt.isScrap()) {
                childViewHolderInt.mScrapContainer.unscrapView(childViewHolderInt);
            } else if (childViewHolderInt.wasReturnedFromScrap()) {
                childViewHolderInt.mFlags &= -33;
            }
            recycleViewHolderInternal(childViewHolderInt);
            if (RecyclerView.this.mItemAnimator != null && !childViewHolderInt.isRecyclable()) {
                RecyclerView.this.mItemAnimator.endAnimation(childViewHolderInt);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0052  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x00cd  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x00d1  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void recycleViewHolderInternal(androidx.recyclerview.widget.RecyclerView.ViewHolder r10) {
            /*
                Method dump skipped, instructions count: 316
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.Recycler.recycleViewHolderInternal(androidx.recyclerview.widget.RecyclerView$ViewHolder):void");
        }

        public final void scrapView(View view) {
            boolean z;
            boolean z2;
            boolean z3;
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            Objects.requireNonNull(childViewHolderInt);
            int i = childViewHolderInt.mFlags;
            if ((i & 12) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                if ((i & 2) != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    RecyclerView recyclerView = RecyclerView.this;
                    Objects.requireNonNull(recyclerView);
                    ItemAnimator itemAnimator = recyclerView.mItemAnimator;
                    if (itemAnimator == null || itemAnimator.canReuseUpdatedViewHolder(childViewHolderInt, childViewHolderInt.getUnmodifiedPayloads())) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (!z3) {
                        if (this.mChangedScrap == null) {
                            this.mChangedScrap = new ArrayList<>();
                        }
                        childViewHolderInt.mScrapContainer = this;
                        childViewHolderInt.mInChangeScrap = true;
                        this.mChangedScrap.add(childViewHolderInt);
                        return;
                    }
                }
            }
            if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved()) {
                Adapter adapter = RecyclerView.this.mAdapter;
                Objects.requireNonNull(adapter);
                if (!adapter.mHasStableIds) {
                    throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(RecyclerView.this, VendorAtomValue$$ExternalSyntheticOutline1.m("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.")));
                }
            }
            childViewHolderInt.mScrapContainer = this;
            childViewHolderInt.mInChangeScrap = false;
            this.mAttachedScrap.add(childViewHolderInt);
        }

        public final View getViewForPosition(int i) {
            return tryGetViewHolderForPositionByDeadline(i, Long.MAX_VALUE).itemView;
        }
    }

    /* loaded from: classes.dex */
    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    /* loaded from: classes.dex */
    public class RecyclerViewDataObserver extends AdapterDataObserver {
        public RecyclerViewDataObserver() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.mState.mStructureChanged = true;
            recyclerView.processDataSetCompletelyChanged(true);
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0026, code lost:
            if (r0.mPendingUpdates.size() == 1) goto L_0x002a;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onItemRangeChanged(int r5, int r6, java.lang.Object r7) {
            /*
                r4 = this;
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                r1 = 0
                r0.assertNotInLayoutOrScroll(r1)
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                androidx.recyclerview.widget.AdapterHelper r0 = r0.mAdapterHelper
                r1 = 1
                if (r6 >= r1) goto L_0x0011
                java.util.Objects.requireNonNull(r0)
                goto L_0x0029
            L_0x0011:
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r2 = r0.mPendingUpdates
                r3 = 4
                androidx.recyclerview.widget.AdapterHelper$UpdateOp r5 = r0.obtainUpdateOp(r3, r5, r6, r7)
                r2.add(r5)
                int r5 = r0.mExistingUpdateTypes
                r5 = r5 | r3
                r0.mExistingUpdateTypes = r5
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r5 = r0.mPendingUpdates
                int r5 = r5.size()
                if (r5 != r1) goto L_0x0029
                goto L_0x002a
            L_0x0029:
                r1 = 0
            L_0x002a:
                if (r1 == 0) goto L_0x002f
                r4.triggerUpdateProcessor()
            L_0x002f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.RecyclerViewDataObserver.onItemRangeChanged(int, int, java.lang.Object):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0025, code lost:
            if (r0.mPendingUpdates.size() == 1) goto L_0x0029;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onItemRangeInserted(int r5, int r6) {
            /*
                r4 = this;
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                r1 = 0
                r0.assertNotInLayoutOrScroll(r1)
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                androidx.recyclerview.widget.AdapterHelper r0 = r0.mAdapterHelper
                r2 = 1
                if (r6 >= r2) goto L_0x0011
                java.util.Objects.requireNonNull(r0)
                goto L_0x0028
            L_0x0011:
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r3 = r0.mPendingUpdates
                androidx.recyclerview.widget.AdapterHelper$UpdateOp r5 = r0.obtainUpdateOp(r2, r5, r6, r1)
                r3.add(r5)
                int r5 = r0.mExistingUpdateTypes
                r5 = r5 | r2
                r0.mExistingUpdateTypes = r5
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r5 = r0.mPendingUpdates
                int r5 = r5.size()
                if (r5 != r2) goto L_0x0028
                goto L_0x0029
            L_0x0028:
                r2 = 0
            L_0x0029:
                if (r2 == 0) goto L_0x002e
                r4.triggerUpdateProcessor()
            L_0x002e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.RecyclerViewDataObserver.onItemRangeInserted(int, int):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:5:0x0027, code lost:
            if (r0.mPendingUpdates.size() == 1) goto L_0x002b;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onItemRangeMoved(int r6, int r7) {
            /*
                r5 = this;
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                r1 = 0
                r0.assertNotInLayoutOrScroll(r1)
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                androidx.recyclerview.widget.AdapterHelper r0 = r0.mAdapterHelper
                java.util.Objects.requireNonNull(r0)
                if (r6 != r7) goto L_0x0010
                goto L_0x002a
            L_0x0010:
                r2 = 1
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r3 = r0.mPendingUpdates
                r4 = 8
                androidx.recyclerview.widget.AdapterHelper$UpdateOp r6 = r0.obtainUpdateOp(r4, r6, r7, r1)
                r3.add(r6)
                int r6 = r0.mExistingUpdateTypes
                r6 = r6 | r4
                r0.mExistingUpdateTypes = r6
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r6 = r0.mPendingUpdates
                int r6 = r6.size()
                if (r6 != r2) goto L_0x002a
                goto L_0x002b
            L_0x002a:
                r2 = 0
            L_0x002b:
                if (r2 == 0) goto L_0x0030
                r5.triggerUpdateProcessor()
            L_0x0030:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.RecyclerViewDataObserver.onItemRangeMoved(int, int):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0026, code lost:
            if (r0.mPendingUpdates.size() == 1) goto L_0x002a;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onItemRangeRemoved(int r6, int r7) {
            /*
                r5 = this;
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                r1 = 0
                r0.assertNotInLayoutOrScroll(r1)
                androidx.recyclerview.widget.RecyclerView r0 = androidx.recyclerview.widget.RecyclerView.this
                androidx.recyclerview.widget.AdapterHelper r0 = r0.mAdapterHelper
                r2 = 1
                if (r7 >= r2) goto L_0x0011
                java.util.Objects.requireNonNull(r0)
                goto L_0x0029
            L_0x0011:
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r3 = r0.mPendingUpdates
                r4 = 2
                androidx.recyclerview.widget.AdapterHelper$UpdateOp r6 = r0.obtainUpdateOp(r4, r6, r7, r1)
                r3.add(r6)
                int r6 = r0.mExistingUpdateTypes
                r6 = r6 | r4
                r0.mExistingUpdateTypes = r6
                java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r6 = r0.mPendingUpdates
                int r6 = r6.size()
                if (r6 != r2) goto L_0x0029
                goto L_0x002a
            L_0x0029:
                r2 = 0
            L_0x002a:
                if (r2 == 0) goto L_0x002f
                r5.triggerUpdateProcessor()
            L_0x002f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.RecyclerViewDataObserver.onItemRangeRemoved(int, int):void");
        }

        public final void triggerUpdateProcessor() {
            int[] iArr = RecyclerView.NESTED_SCROLLING_ATTRS;
            RecyclerView recyclerView = RecyclerView.this;
            if (!recyclerView.mHasFixedSize || !recyclerView.mIsAttached) {
                recyclerView.mAdapterUpdateDuringMeasure = true;
                recyclerView.requestLayout();
                return;
            }
            AnonymousClass1 r0 = recyclerView.mUpdateChildViewsRunnable;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postOnAnimation(recyclerView, r0);
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.recyclerview.widget.RecyclerView.SavedState.1
            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Parcelable mLayoutState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.mLayoutState = parcel.readParcelable(classLoader == null ? LayoutManager.class.getClassLoader() : classLoader);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeParcelable(this.mLayoutState, 0);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class SmoothScroller {
        public LayoutManager mLayoutManager;
        public boolean mPendingInitialRun;
        public RecyclerView mRecyclerView;
        public boolean mRunning;
        public boolean mStarted;
        public View mTargetView;
        public int mTargetPosition = -1;
        public final Action mRecyclingAction = new Action();

        /* loaded from: classes.dex */
        public static class Action {
            public int mJumpToPosition = -1;
            public boolean mChanged = false;
            public int mConsecutiveUpdates = 0;
            public int mDx = 0;
            public int mDy = 0;
            public int mDuration = Integer.MIN_VALUE;
            public Interpolator mInterpolator = null;

            public final void runIfNecessary(RecyclerView recyclerView) {
                int i = this.mJumpToPosition;
                if (i >= 0) {
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(i);
                    this.mChanged = false;
                } else if (this.mChanged) {
                    Interpolator interpolator = this.mInterpolator;
                    if (interpolator == null || this.mDuration >= 1) {
                        int i2 = this.mDuration;
                        if (i2 >= 1) {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, i2, interpolator);
                            int i3 = this.mConsecutiveUpdates + 1;
                            this.mConsecutiveUpdates = i3;
                            if (i3 > 10) {
                                Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                            }
                            this.mChanged = false;
                            return;
                        }
                        throw new IllegalStateException("Scroll duration must be a positive number");
                    }
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                } else {
                    this.mConsecutiveUpdates = 0;
                }
            }

            public final void update(int i, int i2, int i3, BaseInterpolator baseInterpolator) {
                this.mDx = i;
                this.mDy = i2;
                this.mDuration = i3;
                this.mInterpolator = baseInterpolator;
                this.mChanged = true;
            }
        }

        /* loaded from: classes.dex */
        public interface ScrollVectorProvider {
            PointF computeScrollVectorForPosition(int i);
        }

        public abstract void onStop();

        public abstract void onTargetFound(View view, Action action);

        public PointF computeScrollVectorForPosition(int i) {
            LayoutManager layoutManager = this.mLayoutManager;
            if (layoutManager instanceof ScrollVectorProvider) {
                return ((ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(i);
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
            m.append(ScrollVectorProvider.class.getCanonicalName());
            Log.w("RecyclerView", m.toString());
            return null;
        }

        public final void onAnimation(int i, int i2) {
            float f;
            PointF computeScrollVectorForPosition;
            RecyclerView recyclerView = this.mRecyclerView;
            int i3 = -1;
            if (this.mTargetPosition == -1 || recyclerView == null) {
                stop();
            }
            if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null && (computeScrollVectorForPosition = computeScrollVectorForPosition(this.mTargetPosition)) != null) {
                float f2 = computeScrollVectorForPosition.x;
                if (!(f2 == 0.0f && computeScrollVectorForPosition.y == 0.0f)) {
                    recyclerView.scrollStep((int) Math.signum(f2), (int) Math.signum(computeScrollVectorForPosition.y), null);
                }
            }
            boolean z = false;
            this.mPendingInitialRun = false;
            View view = this.mTargetView;
            if (view != null) {
                Objects.requireNonNull(this.mRecyclerView);
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    i3 = childViewHolderInt.getLayoutPosition();
                }
                if (i3 == this.mTargetPosition) {
                    View view2 = this.mTargetView;
                    State state = recyclerView.mState;
                    onTargetFound(view2, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    stop();
                } else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                State state2 = recyclerView.mState;
                Action action = this.mRecyclingAction;
                LinearSmoothScroller linearSmoothScroller = (LinearSmoothScroller) this;
                if (linearSmoothScroller.mRecyclerView.mLayout.getChildCount() == 0) {
                    linearSmoothScroller.stop();
                } else {
                    int i4 = linearSmoothScroller.mInterimTargetDx;
                    int i5 = i4 - i;
                    if (i4 * i5 <= 0) {
                        i5 = 0;
                    }
                    linearSmoothScroller.mInterimTargetDx = i5;
                    int i6 = linearSmoothScroller.mInterimTargetDy;
                    int i7 = i6 - i2;
                    if (i6 * i7 <= 0) {
                        i7 = 0;
                    }
                    linearSmoothScroller.mInterimTargetDy = i7;
                    if (i5 == 0 && i7 == 0) {
                        PointF computeScrollVectorForPosition2 = linearSmoothScroller.computeScrollVectorForPosition(linearSmoothScroller.mTargetPosition);
                        if (computeScrollVectorForPosition2 != null) {
                            if (!(computeScrollVectorForPosition2.x == 0.0f && computeScrollVectorForPosition2.y == 0.0f)) {
                                float f3 = computeScrollVectorForPosition2.y;
                                float sqrt = (float) Math.sqrt((f3 * f3) + (f * f));
                                float f4 = computeScrollVectorForPosition2.x / sqrt;
                                computeScrollVectorForPosition2.x = f4;
                                float f5 = computeScrollVectorForPosition2.y / sqrt;
                                computeScrollVectorForPosition2.y = f5;
                                linearSmoothScroller.mTargetVector = computeScrollVectorForPosition2;
                                linearSmoothScroller.mInterimTargetDx = (int) (f4 * 10000.0f);
                                linearSmoothScroller.mInterimTargetDy = (int) (f5 * 10000.0f);
                                action.update((int) (linearSmoothScroller.mInterimTargetDx * 1.2f), (int) (linearSmoothScroller.mInterimTargetDy * 1.2f), (int) (linearSmoothScroller.calculateTimeForScrolling(10000) * 1.2f), linearSmoothScroller.mLinearInterpolator);
                            }
                        }
                        int i8 = linearSmoothScroller.mTargetPosition;
                        Objects.requireNonNull(action);
                        action.mJumpToPosition = i8;
                        linearSmoothScroller.stop();
                    }
                }
                Action action2 = this.mRecyclingAction;
                Objects.requireNonNull(action2);
                if (action2.mJumpToPosition >= 0) {
                    z = true;
                }
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (z && this.mRunning) {
                    this.mPendingInitialRun = true;
                    recyclerView.mViewFlinger.postOnAnimation();
                }
            }
        }

        public final void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                LayoutManager layoutManager = this.mLayoutManager;
                Objects.requireNonNull(layoutManager);
                if (layoutManager.mSmoothScroller == this) {
                    layoutManager.mSmoothScroller = null;
                }
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class State {
        public long mFocusedItemId;
        public int mFocusedItemPosition;
        public int mFocusedSubChildId;
        public int mRemainingScrollHorizontal;
        public int mRemainingScrollVertical;
        public int mTargetPosition = -1;
        public int mPreviousLayoutItemCount = 0;
        public int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        public int mLayoutStep = 1;
        public int mItemCount = 0;
        public boolean mStructureChanged = false;
        public boolean mInPreLayout = false;
        public boolean mTrackOldChangeHolders = false;
        public boolean mIsMeasuring = false;
        public boolean mRunSimpleAnimations = false;
        public boolean mRunPredictiveAnimations = false;

        public final void assertLayoutStep(int i) {
            if ((this.mLayoutStep & i) == 0) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Layout state should be one of ");
                m.append(Integer.toBinaryString(i));
                m.append(" but it is ");
                m.append(Integer.toBinaryString(this.mLayoutStep));
                throw new IllegalStateException(m.toString());
            }
        }

        public final int getItemCount() {
            if (this.mInPreLayout) {
                return this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
            }
            return this.mItemCount;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("State{mTargetPosition=");
            m.append(this.mTargetPosition);
            m.append(", mData=");
            m.append((Object) null);
            m.append(", mItemCount=");
            m.append(this.mItemCount);
            m.append(", mIsMeasuring=");
            m.append(this.mIsMeasuring);
            m.append(", mPreviousLayoutItemCount=");
            m.append(this.mPreviousLayoutItemCount);
            m.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
            m.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
            m.append(", mStructureChanged=");
            m.append(this.mStructureChanged);
            m.append(", mInPreLayout=");
            m.append(this.mInPreLayout);
            m.append(", mRunSimpleAnimations=");
            m.append(this.mRunSimpleAnimations);
            m.append(", mRunPredictiveAnimations=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.mRunPredictiveAnimations, '}');
        }
    }

    /* loaded from: classes.dex */
    public static class StretchEdgeEffectFactory extends EdgeEffectFactory {
    }

    /* loaded from: classes.dex */
    public class ViewFlinger implements Runnable {
        public Interpolator mInterpolator;
        public int mLastFlingX;
        public int mLastFlingY;
        public OverScroller mOverScroller;
        public boolean mEatRunOnAnimationRequest = false;
        public boolean mReSchedulePostAnimationCallback = false;

        public final void smoothScrollBy(int i, int i2, int i3, Interpolator interpolator) {
            boolean z;
            int i4;
            if (i3 == Integer.MIN_VALUE) {
                int abs = Math.abs(i);
                int abs2 = Math.abs(i2);
                if (abs > abs2) {
                    z = true;
                } else {
                    z = false;
                }
                RecyclerView recyclerView = RecyclerView.this;
                if (z) {
                    i4 = recyclerView.getWidth();
                } else {
                    i4 = recyclerView.getHeight();
                }
                if (!z) {
                    abs = abs2;
                }
                i3 = Math.min((int) (((abs / i4) + 1.0f) * 300.0f), 2000);
            }
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), interpolator);
            }
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            RecyclerView.this.setScrollState(2);
            this.mOverScroller.startScroll(0, 0, i, i2, i3);
            postOnAnimation();
        }

        public ViewFlinger() {
            AnonymousClass3 r0 = RecyclerView.sQuinticInterpolator;
            this.mInterpolator = r0;
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), r0);
        }

        public final void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
                return;
            }
            RecyclerView.this.removeCallbacks(this);
            RecyclerView recyclerView = RecyclerView.this;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postOnAnimation(recyclerView, this);
        }

        @Override // java.lang.Runnable
        public final void run() {
            int i;
            int i2;
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            int i3;
            RecyclerView recyclerView = RecyclerView.this;
            if (recyclerView.mLayout == null) {
                recyclerView.removeCallbacks(this);
                this.mOverScroller.abortAnimation();
                return;
            }
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            recyclerView.consumePendingUpdateOperations();
            OverScroller overScroller = this.mOverScroller;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i4 = currX - this.mLastFlingX;
                int i5 = currY - this.mLastFlingY;
                this.mLastFlingX = currX;
                this.mLastFlingY = currY;
                RecyclerView recyclerView2 = RecyclerView.this;
                int[] iArr = recyclerView2.mReusableIntPair;
                iArr[0] = 0;
                iArr[1] = 0;
                if (recyclerView2.getScrollingChildHelper().dispatchNestedPreScroll(i4, i5, iArr, null, 1)) {
                    int[] iArr2 = RecyclerView.this.mReusableIntPair;
                    i4 -= iArr2[0];
                    i5 -= iArr2[1];
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(i4, i5);
                }
                RecyclerView recyclerView3 = RecyclerView.this;
                if (recyclerView3.mAdapter != null) {
                    int[] iArr3 = recyclerView3.mReusableIntPair;
                    iArr3[0] = 0;
                    iArr3[1] = 0;
                    recyclerView3.scrollStep(i4, i5, iArr3);
                    RecyclerView recyclerView4 = RecyclerView.this;
                    int[] iArr4 = recyclerView4.mReusableIntPair;
                    i = iArr4[0];
                    i2 = iArr4[1];
                    i4 -= i;
                    i5 -= i2;
                    SmoothScroller smoothScroller = recyclerView4.mLayout.mSmoothScroller;
                    if (smoothScroller != null && !smoothScroller.mPendingInitialRun && smoothScroller.mRunning) {
                        int itemCount = recyclerView4.mState.getItemCount();
                        if (itemCount == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.mTargetPosition >= itemCount) {
                            smoothScroller.mTargetPosition = itemCount - 1;
                            smoothScroller.onAnimation(i, i2);
                        } else {
                            smoothScroller.onAnimation(i, i2);
                        }
                    }
                } else {
                    i2 = 0;
                    i = 0;
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                RecyclerView recyclerView5 = RecyclerView.this;
                int[] iArr5 = recyclerView5.mReusableIntPair;
                iArr5[0] = 0;
                iArr5[1] = 0;
                NestedScrollingChildHelper scrollingChildHelper = recyclerView5.getScrollingChildHelper();
                Objects.requireNonNull(scrollingChildHelper);
                scrollingChildHelper.dispatchNestedScrollInternal(i, i2, i4, i5, null, 1, iArr5);
                RecyclerView recyclerView6 = RecyclerView.this;
                int[] iArr6 = recyclerView6.mReusableIntPair;
                int i6 = i4 - iArr6[0];
                int i7 = i5 - iArr6[1];
                if (!(i == 0 && i2 == 0)) {
                    recyclerView6.dispatchOnScrolled(i, i2);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                if (overScroller.getCurrX() == overScroller.getFinalX()) {
                    z = true;
                } else {
                    z = false;
                }
                if (overScroller.getCurrY() == overScroller.getFinalY()) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (overScroller.isFinished() || ((z || i6 != 0) && (z2 || i7 != 0))) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                RecyclerView recyclerView7 = RecyclerView.this;
                SmoothScroller smoothScroller2 = recyclerView7.mLayout.mSmoothScroller;
                if (smoothScroller2 == null || !smoothScroller2.mPendingInitialRun) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                if (z4 || !z3) {
                    postOnAnimation();
                    RecyclerView recyclerView8 = RecyclerView.this;
                    GapWorker gapWorker = recyclerView8.mGapWorker;
                    if (gapWorker != null) {
                        gapWorker.postFromTraversal(recyclerView8, i, i2);
                    }
                } else {
                    if (recyclerView7.getOverScrollMode() != 2) {
                        int currVelocity = (int) overScroller.getCurrVelocity();
                        if (i6 < 0) {
                            i3 = -currVelocity;
                        } else if (i6 > 0) {
                            i3 = currVelocity;
                        } else {
                            i3 = 0;
                        }
                        if (i7 < 0) {
                            currVelocity = -currVelocity;
                        } else if (i7 <= 0) {
                            currVelocity = 0;
                        }
                        RecyclerView recyclerView9 = RecyclerView.this;
                        if (i3 < 0) {
                            recyclerView9.ensureLeftGlow();
                            recyclerView9.mLeftGlow.onAbsorb(-i3);
                        } else if (i3 > 0) {
                            recyclerView9.ensureRightGlow();
                            recyclerView9.mRightGlow.onAbsorb(i3);
                        }
                        if (currVelocity < 0) {
                            recyclerView9.ensureTopGlow();
                            recyclerView9.mTopGlow.onAbsorb(-currVelocity);
                        } else if (currVelocity > 0) {
                            recyclerView9.ensureBottomGlow();
                            recyclerView9.mBottomGlow.onAbsorb(currVelocity);
                        } else {
                            Objects.requireNonNull(recyclerView9);
                        }
                        if (!(i3 == 0 && currVelocity == 0)) {
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                            ViewCompat.Api16Impl.postInvalidateOnAnimation(recyclerView9);
                        }
                    }
                    GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = RecyclerView.this.mPrefetchRegistry;
                    Objects.requireNonNull(layoutPrefetchRegistryImpl);
                    int[] iArr7 = layoutPrefetchRegistryImpl.mPrefetchArray;
                    if (iArr7 != null) {
                        Arrays.fill(iArr7, -1);
                    }
                    layoutPrefetchRegistryImpl.mCount = 0;
                }
            }
            SmoothScroller smoothScroller3 = RecyclerView.this.mLayout.mSmoothScroller;
            if (smoothScroller3 != null && smoothScroller3.mPendingInitialRun) {
                smoothScroller3.onAnimation(0, 0);
            }
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                RecyclerView.this.removeCallbacks(this);
                RecyclerView recyclerView10 = RecyclerView.this;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.postOnAnimation(recyclerView10, this);
                return;
            }
            RecyclerView.this.setScrollState(0);
            RecyclerView recyclerView11 = RecyclerView.this;
            Objects.requireNonNull(recyclerView11);
            recyclerView11.getScrollingChildHelper().stopNestedScroll(1);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ViewHolder {
        public static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
        public final View itemView;
        public Adapter<? extends ViewHolder> mBindingAdapter;
        public int mFlags;
        public WeakReference<RecyclerView> mNestedRecyclerView;
        public RecyclerView mOwnerRecyclerView;
        public int mPosition = -1;
        public int mOldPosition = -1;
        public long mItemId = -1;
        public int mItemViewType = -1;
        public int mPreLayoutPosition = -1;
        public ViewHolder mShadowedHolder = null;
        public ViewHolder mShadowingHolder = null;
        public ArrayList mPayloads = null;
        public List<Object> mUnmodifiedPayloads = null;
        public int mIsRecyclableCount = 0;
        public Recycler mScrapContainer = null;
        public boolean mInChangeScrap = false;
        public int mWasImportantForAccessibilityBeforeHidden = 0;
        public int mPendingAccessibilityState = -1;

        public final void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            ArrayList arrayList = this.mPayloads;
            if (arrayList != null) {
                arrayList.clear();
            }
            this.mFlags &= -1025;
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }

        public final void setIsRecyclable(boolean z) {
            int i;
            int i2 = this.mIsRecyclableCount;
            if (z) {
                i = i2 - 1;
            } else {
                i = i2 + 1;
            }
            this.mIsRecyclableCount = i;
            if (i < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            } else if (!z && i == 1) {
                this.mFlags |= 16;
            } else if (z && i == 0) {
                this.mFlags &= -17;
            }
        }

        public final void addChangePayload(Object obj) {
            if (obj == null) {
                addFlags(1024);
            } else if ((1024 & this.mFlags) == 0) {
                if (this.mPayloads == null) {
                    ArrayList arrayList = new ArrayList();
                    this.mPayloads = arrayList;
                    this.mUnmodifiedPayloads = Collections.unmodifiableList(arrayList);
                }
                this.mPayloads.add(obj);
            }
        }

        public final void addFlags(int i) {
            this.mFlags = i | this.mFlags;
        }

        public final int getAbsoluteAdapterPosition() {
            RecyclerView recyclerView = this.mOwnerRecyclerView;
            if (recyclerView == null) {
                return -1;
            }
            return recyclerView.getAdapterPositionInRecyclerView(this);
        }

        public final int getBindingAdapterPosition() {
            RecyclerView recyclerView;
            int adapterPositionInRecyclerView;
            if (this.mBindingAdapter == null || (recyclerView = this.mOwnerRecyclerView) == null) {
                return -1;
            }
            Objects.requireNonNull(recyclerView);
            Adapter<? extends ViewHolder> adapter = recyclerView.mAdapter;
            if (adapter == null || (adapterPositionInRecyclerView = this.mOwnerRecyclerView.getAdapterPositionInRecyclerView(this)) == -1 || this.mBindingAdapter != adapter) {
                return -1;
            }
            return adapterPositionInRecyclerView;
        }

        public final int getLayoutPosition() {
            int i = this.mPreLayoutPosition;
            if (i == -1) {
                return this.mPosition;
            }
            return i;
        }

        public final List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) != 0) {
                return FULLUPDATE_PAYLOADS;
            }
            ArrayList arrayList = this.mPayloads;
            if (arrayList == null || arrayList.size() == 0) {
                return FULLUPDATE_PAYLOADS;
            }
            return this.mUnmodifiedPayloads;
        }

        public final boolean isAttachedToTransitionOverlay() {
            if (this.itemView.getParent() == null || this.itemView.getParent() == this.mOwnerRecyclerView) {
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean isBound() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isInvalid() {
            if ((this.mFlags & 4) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isRecyclable() {
            if ((this.mFlags & 16) == 0) {
                View view = this.itemView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!ViewCompat.Api16Impl.hasTransientState(view)) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean isRemoved() {
            if ((this.mFlags & 8) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isScrap() {
            if (this.mScrapContainer != null) {
                return true;
            }
            return false;
        }

        public final boolean isTmpDetached() {
            if ((this.mFlags & 256) != 0) {
                return true;
            }
            return false;
        }

        public final void offsetPosition(int i, boolean z) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (z) {
                this.mPreLayoutPosition += i;
            }
            this.mPosition += i;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        public final boolean shouldIgnore() {
            if ((this.mFlags & 128) != 0) {
                return true;
            }
            return false;
        }

        public final boolean wasReturnedFromScrap() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }

        public ViewHolder(View view) {
            if (view != null) {
                this.itemView = view;
                return;
            }
            throw new IllegalArgumentException("itemView may not be null");
        }

        public final String toString() {
            String str;
            boolean z;
            String str2;
            if (getClass().isAnonymousClass()) {
                str = "ViewHolder";
            } else {
                str = getClass().getSimpleName();
            }
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "{");
            m.append(Integer.toHexString(hashCode()));
            m.append(" position=");
            m.append(this.mPosition);
            m.append(" id=");
            m.append(this.mItemId);
            m.append(", oldPos=");
            m.append(this.mOldPosition);
            m.append(", pLpos:");
            m.append(this.mPreLayoutPosition);
            StringBuilder sb = new StringBuilder(m.toString());
            if (isScrap()) {
                sb.append(" scrap ");
                if (this.mInChangeScrap) {
                    str2 = "[changeScrap]";
                } else {
                    str2 = "[attachedScrap]";
                }
                sb.append(str2);
            }
            if (isInvalid()) {
                sb.append(" invalid");
            }
            if (!isBound()) {
                sb.append(" unbound");
            }
            boolean z2 = true;
            if ((this.mFlags & 2) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                sb.append(" update");
            }
            if (isRemoved()) {
                sb.append(" removed");
            }
            if (shouldIgnore()) {
                sb.append(" ignored");
            }
            if (isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!isRecyclable()) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" not recyclable(");
                m2.append(this.mIsRecyclableCount);
                m2.append(")");
                sb.append(m2.toString());
            }
            if ((this.mFlags & 512) == 0 && !isInvalid()) {
                z2 = false;
            }
            if (z2) {
                sb.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [androidx.recyclerview.widget.RecyclerView$3] */
    static {
        Class<?> cls = Integer.TYPE;
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, cls, cls};
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams(getContext(), attributeSet);
        }
        throw new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m("RecyclerView has no LayoutManager")));
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return "androidx.recyclerview.widget.RecyclerView";
    }

    public void setAdapter(Adapter adapter) {
        suppressLayout(false);
        Adapter adapter2 = this.mAdapter;
        if (adapter2 != null) {
            adapter2.mObservable.unregisterObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView();
        }
        removeAndRecycleViews();
        AdapterHelper adapterHelper = this.mAdapterHelper;
        Objects.requireNonNull(adapterHelper);
        adapterHelper.recycleUpdateOpsAndClearList(adapterHelper.mPendingUpdates);
        adapterHelper.recycleUpdateOpsAndClearList(adapterHelper.mPostponedList);
        adapterHelper.mExistingUpdateTypes = 0;
        Adapter adapter3 = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.onAdapterChanged(adapter3, this.mAdapter);
        }
        Recycler recycler = this.mRecycler;
        Adapter adapter4 = this.mAdapter;
        Objects.requireNonNull(recycler);
        recycler.mAttachedScrap.clear();
        recycler.recycleAndClearCachedViews();
        if (recycler.mRecyclerPool == null) {
            recycler.mRecyclerPool = new RecycledViewPool();
        }
        RecycledViewPool recycledViewPool = recycler.mRecyclerPool;
        if (adapter3 != null) {
            recycledViewPool.mAttachCount--;
        }
        if (recycledViewPool.mAttachCount == 0) {
            for (int i = 0; i < recycledViewPool.mScrap.size(); i++) {
                recycledViewPool.mScrap.valueAt(i).mScrapHeap.clear();
            }
        }
        if (adapter4 != null) {
            recycledViewPool.mAttachCount++;
        }
        this.mState.mStructureChanged = true;
        processDataSetCompletelyChanged(false);
        requestLayout();
    }

    public void smoothScrollBy$1(int i, int i2) {
        smoothScrollBy$1(i, i2, false);
    }

    public final void stopScroll() {
        SmoothScroller smoothScroller;
        setScrollState(0);
        ViewFlinger viewFlinger = this.mViewFlinger;
        Objects.requireNonNull(viewFlinger);
        RecyclerView.this.removeCallbacks(viewFlinger);
        viewFlinger.mOverScroller.abortAnimation();
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && (smoothScroller = layoutManager.mSmoothScroller) != null) {
            smoothScroller.stop();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ItemDecoration {
        public void onDraw(Canvas canvas, RecyclerView recyclerView) {
        }

        public void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            rect.set(0, 0, 0, 0);
        }
    }

    public RecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969647);
    }

    public static void clearNestedRecyclerViewIfNotNested(ViewHolder viewHolder) {
        WeakReference<RecyclerView> weakReference = viewHolder.mNestedRecyclerView;
        if (weakReference != null) {
            RecyclerView recyclerView = weakReference.get();
            while (recyclerView != null) {
                if (recyclerView != viewHolder.itemView) {
                    ViewParent parent = recyclerView.getParent();
                    if (parent instanceof View) {
                        recyclerView = (View) parent;
                    } else {
                        recyclerView = null;
                    }
                } else {
                    return;
                }
            }
            viewHolder.mNestedRecyclerView = null;
        }
    }

    public static RecyclerView findNestedRecyclerView(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RecyclerView findNestedRecyclerView = findNestedRecyclerView(viewGroup.getChildAt(i));
            if (findNestedRecyclerView != null) {
                return findNestedRecyclerView;
            }
        }
        return null;
    }

    public static ViewHolder getChildViewHolderInt(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).mViewHolder;
    }

    public final void addAnimatingView(ViewHolder viewHolder) {
        boolean z;
        View view = viewHolder.itemView;
        if (view.getParent() == this) {
            z = true;
        } else {
            z = false;
        }
        this.mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (!z) {
            ChildHelper childHelper = this.mChildHelper;
            Objects.requireNonNull(childHelper);
            childHelper.addView(view, -1, true);
        } else {
            ChildHelper childHelper2 = this.mChildHelper;
            Objects.requireNonNull(childHelper2);
            AnonymousClass5 r6 = (AnonymousClass5) childHelper2.mCallback;
            Objects.requireNonNull(r6);
            int indexOfChild = RecyclerView.this.indexOfChild(view);
            if (indexOfChild >= 0) {
                childHelper2.mBucket.set(indexOfChild);
                childHelper2.hideViewInternal(view);
                return;
            }
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null || !layoutManager.onAddFocusables(this, arrayList, i, i2)) {
            super.addFocusables(arrayList, i, i2);
        }
    }

    public final void addItemDecoration(ItemDecoration itemDecoration) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        this.mItemDecorations.add(itemDecoration);
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public final void addOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList();
        }
        this.mScrollListeners.add(onScrollListener);
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (!(layoutParams instanceof LayoutParams) || !this.mLayout.checkLayoutParams((LayoutParams) layoutParams)) {
            return false;
        }
        return true;
    }

    public final void clearOldPositions() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.mOldPosition = -1;
                childViewHolderInt.mPreLayoutPosition = -1;
            }
        }
        Recycler recycler = this.mRecycler;
        Objects.requireNonNull(recycler);
        int size = recycler.mCachedViews.size();
        for (int i2 = 0; i2 < size; i2++) {
            ViewHolder viewHolder = recycler.mCachedViews.get(i2);
            Objects.requireNonNull(viewHolder);
            viewHolder.mOldPosition = -1;
            viewHolder.mPreLayoutPosition = -1;
        }
        int size2 = recycler.mAttachedScrap.size();
        for (int i3 = 0; i3 < size2; i3++) {
            ViewHolder viewHolder2 = recycler.mAttachedScrap.get(i3);
            Objects.requireNonNull(viewHolder2);
            viewHolder2.mOldPosition = -1;
            viewHolder2.mPreLayoutPosition = -1;
        }
        ArrayList<ViewHolder> arrayList = recycler.mChangedScrap;
        if (arrayList != null) {
            int size3 = arrayList.size();
            for (int i4 = 0; i4 < size3; i4++) {
                ViewHolder viewHolder3 = recycler.mChangedScrap.get(i4);
                Objects.requireNonNull(viewHolder3);
                viewHolder3.mOldPosition = -1;
                viewHolder3.mPreLayoutPosition = -1;
            }
        }
    }

    @Override // android.view.View
    public final int computeHorizontalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    public final int computeHorizontalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    public final int computeHorizontalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    public final int computeVerticalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    public final int computeVerticalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    public final int computeVerticalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    public final void considerReleasingGlowsOnScroll(int i, int i2) {
        boolean z;
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect == null || edgeEffect.isFinished() || i <= 0) {
            z = false;
        } else {
            this.mLeftGlow.onRelease();
            z = this.mLeftGlow.isFinished();
        }
        EdgeEffect edgeEffect2 = this.mRightGlow;
        if (edgeEffect2 != null && !edgeEffect2.isFinished() && i < 0) {
            this.mRightGlow.onRelease();
            z |= this.mRightGlow.isFinished();
        }
        EdgeEffect edgeEffect3 = this.mTopGlow;
        if (edgeEffect3 != null && !edgeEffect3.isFinished() && i2 > 0) {
            this.mTopGlow.onRelease();
            z |= this.mTopGlow.isFinished();
        }
        EdgeEffect edgeEffect4 = this.mBottomGlow;
        if (edgeEffect4 != null && !edgeEffect4.isFinished() && i2 < 0) {
            this.mBottomGlow.onRelease();
            z |= this.mBottomGlow.isFinished();
        }
        if (z) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this);
        }
    }

    public final void consumePendingUpdateOperations() {
        boolean z;
        boolean z2;
        boolean z3;
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
            Trace.beginSection("RV FullInvalidate");
            dispatchLayout();
            Trace.endSection();
        } else if (this.mAdapterHelper.hasPendingUpdates()) {
            AdapterHelper adapterHelper = this.mAdapterHelper;
            Objects.requireNonNull(adapterHelper);
            boolean z4 = false;
            if ((adapterHelper.mExistingUpdateTypes & 4) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                AdapterHelper adapterHelper2 = this.mAdapterHelper;
                Objects.requireNonNull(adapterHelper2);
                if ((adapterHelper2.mExistingUpdateTypes & 11) != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    Trace.beginSection("RV PartialInvalidate");
                    startInterceptRequestLayout();
                    onEnterLayoutOrScroll();
                    this.mAdapterHelper.preProcess();
                    if (!this.mLayoutWasDefered) {
                        int childCount = this.mChildHelper.getChildCount();
                        int i = 0;
                        while (true) {
                            if (i >= childCount) {
                                break;
                            }
                            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                                if ((childViewHolderInt.mFlags & 2) != 0) {
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                                if (z3) {
                                    z4 = true;
                                    break;
                                }
                            }
                            i++;
                        }
                        if (z4) {
                            dispatchLayout();
                        } else {
                            this.mAdapterHelper.consumePostponedUpdates();
                        }
                    }
                    stopInterceptRequestLayout(true);
                    onExitLayoutOrScroll(true);
                    Trace.endSection();
                    return;
                }
            }
            if (this.mAdapterHelper.hasPendingUpdates()) {
                Trace.beginSection("RV FullInvalidate");
                dispatchLayout();
                Trace.endSection();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:155:0x037b, code lost:
        if (r15.mChildHelper.isHidden(getFocusedChild()) == false) goto L_0x044b;
     */
    /* JADX WARN: Removed duplicated region for block: B:187:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0432  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dispatchLayout() {
        /*
            Method dump skipped, instructions count: 1109
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.dispatchLayout():void");
    }

    public final void dispatchLayoutStep1() {
        View view;
        ViewHolder viewHolder;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i;
        View findContainingItemView;
        this.mState.assertLayoutStep(1);
        fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        startInterceptRequestLayout();
        ViewInfoStore viewInfoStore = this.mViewInfoStore;
        Objects.requireNonNull(viewInfoStore);
        viewInfoStore.mLayoutHolderMap.clear();
        viewInfoStore.mOldChangedHolders.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        if (!this.mPreserveFocusAfterLayout || !hasFocus() || this.mAdapter == null) {
            view = null;
        } else {
            view = getFocusedChild();
        }
        if (view == null || (findContainingItemView = findContainingItemView(view)) == null) {
            viewHolder = null;
        } else {
            viewHolder = getChildViewHolder(findContainingItemView);
        }
        long j = -1;
        if (viewHolder == null) {
            State state = this.mState;
            state.mFocusedItemId = -1L;
            state.mFocusedItemPosition = -1;
            state.mFocusedSubChildId = -1;
        } else {
            State state2 = this.mState;
            Adapter adapter = this.mAdapter;
            Objects.requireNonNull(adapter);
            if (adapter.mHasStableIds) {
                j = viewHolder.mItemId;
            }
            state2.mFocusedItemId = j;
            State state3 = this.mState;
            if (this.mDataSetHasChangedAfterLayout) {
                i = -1;
            } else if (viewHolder.isRemoved()) {
                i = viewHolder.mOldPosition;
            } else {
                i = viewHolder.getAbsoluteAdapterPosition();
            }
            state3.mFocusedItemPosition = i;
            State state4 = this.mState;
            View view2 = viewHolder.itemView;
            int id = view2.getId();
            while (!view2.isFocused() && (view2 instanceof ViewGroup) && view2.hasFocus()) {
                view2 = ((ViewGroup) view2).getFocusedChild();
                if (view2.getId() != -1) {
                    id = view2.getId();
                }
            }
            state4.mFocusedSubChildId = id;
        }
        State state5 = this.mState;
        if (!state5.mRunSimpleAnimations || !this.mItemsChanged) {
            z = false;
        } else {
            z = true;
        }
        state5.mTrackOldChangeHolders = z;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        state5.mInPreLayout = state5.mRunPredictiveAnimations;
        state5.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            int childCount = this.mChildHelper.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i2));
                if (!childViewHolderInt.shouldIgnore()) {
                    if (childViewHolderInt.isInvalid()) {
                        Adapter adapter2 = this.mAdapter;
                        Objects.requireNonNull(adapter2);
                        if (!adapter2.mHasStableIds) {
                        }
                    }
                    ItemAnimator itemAnimator = this.mItemAnimator;
                    ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt);
                    childViewHolderInt.getUnmodifiedPayloads();
                    Objects.requireNonNull(itemAnimator);
                    ItemAnimator.ItemHolderInfo itemHolderInfo = new ItemAnimator.ItemHolderInfo();
                    itemHolderInfo.setFrom(childViewHolderInt);
                    this.mViewInfoStore.addToPreLayout(childViewHolderInt, itemHolderInfo);
                    if (this.mState.mTrackOldChangeHolders) {
                        if ((childViewHolderInt.mFlags & 2) != 0) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        if (z4 && !childViewHolderInt.isRemoved() && !childViewHolderInt.shouldIgnore() && !childViewHolderInt.isInvalid()) {
                            long changedHolderKey = getChangedHolderKey(childViewHolderInt);
                            ViewInfoStore viewInfoStore2 = this.mViewInfoStore;
                            Objects.requireNonNull(viewInfoStore2);
                            viewInfoStore2.mOldChangedHolders.put(changedHolderKey, childViewHolderInt);
                        }
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
            for (int i3 = 0; i3 < unfilteredChildCount; i3++) {
                ViewHolder childViewHolderInt2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i3));
                if (!childViewHolderInt2.shouldIgnore() && childViewHolderInt2.mOldPosition == -1) {
                    childViewHolderInt2.mOldPosition = childViewHolderInt2.mPosition;
                }
            }
            State state6 = this.mState;
            boolean z5 = state6.mStructureChanged;
            state6.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, state6);
            this.mState.mStructureChanged = z5;
            for (int i4 = 0; i4 < this.mChildHelper.getChildCount(); i4++) {
                ViewHolder childViewHolderInt3 = getChildViewHolderInt(this.mChildHelper.getChildAt(i4));
                if (!childViewHolderInt3.shouldIgnore()) {
                    ViewInfoStore viewInfoStore3 = this.mViewInfoStore;
                    Objects.requireNonNull(viewInfoStore3);
                    SimpleArrayMap<ViewHolder, ViewInfoStore.InfoRecord> simpleArrayMap = viewInfoStore3.mLayoutHolderMap;
                    Objects.requireNonNull(simpleArrayMap);
                    ViewInfoStore.InfoRecord orDefault = simpleArrayMap.getOrDefault(childViewHolderInt3, null);
                    if (orDefault == null || (orDefault.flags & 4) == 0) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (!z2) {
                        ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt3);
                        if ((childViewHolderInt3.mFlags & 8192) != 0) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        ItemAnimator itemAnimator2 = this.mItemAnimator;
                        childViewHolderInt3.getUnmodifiedPayloads();
                        Objects.requireNonNull(itemAnimator2);
                        ItemAnimator.ItemHolderInfo itemHolderInfo2 = new ItemAnimator.ItemHolderInfo();
                        itemHolderInfo2.setFrom(childViewHolderInt3);
                        if (z3) {
                            recordAnimationInfoIfBouncedHiddenView(childViewHolderInt3, itemHolderInfo2);
                        } else {
                            ViewInfoStore viewInfoStore4 = this.mViewInfoStore;
                            Objects.requireNonNull(viewInfoStore4);
                            SimpleArrayMap<ViewHolder, ViewInfoStore.InfoRecord> simpleArrayMap2 = viewInfoStore4.mLayoutHolderMap;
                            Objects.requireNonNull(simpleArrayMap2);
                            ViewInfoStore.InfoRecord orDefault2 = simpleArrayMap2.getOrDefault(childViewHolderInt3, null);
                            if (orDefault2 == null) {
                                orDefault2 = ViewInfoStore.InfoRecord.obtain();
                                viewInfoStore4.mLayoutHolderMap.put(childViewHolderInt3, orDefault2);
                            }
                            orDefault2.flags |= 2;
                            orDefault2.preInfo = itemHolderInfo2;
                        }
                    }
                }
            }
            clearOldPositions();
        } else {
            clearOldPositions();
        }
        onExitLayoutOrScroll(true);
        stopInterceptRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }

    public final void dispatchOnScrolled(int i, int i2) {
        this.mDispatchScrollCounter++;
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX - i, scrollY - i2);
        ArrayList arrayList = this.mScrollListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                ((OnScrollListener) this.mScrollListeners.get(size)).onScrolled(this, i, i2);
            }
        }
        this.mDispatchScrollCounter--;
    }

    public final void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            Objects.requireNonNull(this.mEdgeEffectFactory);
            EdgeEffect edgeEffect = new EdgeEffect(getContext());
            this.mBottomGlow = edgeEffect;
            if (this.mClipToPadding) {
                edgeEffect.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                edgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    public final void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            Objects.requireNonNull(this.mEdgeEffectFactory);
            EdgeEffect edgeEffect = new EdgeEffect(getContext());
            this.mLeftGlow = edgeEffect;
            if (this.mClipToPadding) {
                edgeEffect.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                edgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    public final void ensureRightGlow() {
        if (this.mRightGlow == null) {
            Objects.requireNonNull(this.mEdgeEffectFactory);
            EdgeEffect edgeEffect = new EdgeEffect(getContext());
            this.mRightGlow = edgeEffect;
            if (this.mClipToPadding) {
                edgeEffect.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                edgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    public final void ensureTopGlow() {
        if (this.mTopGlow == null) {
            Objects.requireNonNull(this.mEdgeEffectFactory);
            EdgeEffect edgeEffect = new EdgeEffect(getContext());
            this.mTopGlow = edgeEffect;
            if (this.mClipToPadding) {
                edgeEffect.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                edgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    public final String exceptionLabel() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" ");
        m.append(super.toString());
        m.append(", adapter:");
        m.append(this.mAdapter);
        m.append(", layout:");
        m.append(this.mLayout);
        m.append(", context:");
        m.append(getContext());
        return m.toString();
    }

    public final void fillRemainingScrollValues(State state) {
        if (this.mScrollState == 2) {
            OverScroller overScroller = this.mViewFlinger.mOverScroller;
            state.mRemainingScrollHorizontal = overScroller.getFinalX() - overScroller.getCurrX();
            state.mRemainingScrollVertical = overScroller.getFinalY() - overScroller.getCurrY();
            return;
        }
        state.mRemainingScrollHorizontal = 0;
        state.mRemainingScrollVertical = 0;
    }

    public final void findMinMaxChildLayoutPositions(int[] iArr) {
        int childCount = this.mChildHelper.getChildCount();
        if (childCount == 0) {
            iArr[0] = -1;
            iArr[1] = -1;
            return;
        }
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        for (int i3 = 0; i3 < childCount; i3++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i3));
            if (!childViewHolderInt.shouldIgnore()) {
                int layoutPosition = childViewHolderInt.getLayoutPosition();
                if (layoutPosition < i) {
                    i = layoutPosition;
                }
                if (layoutPosition > i2) {
                    i2 = layoutPosition;
                }
            }
        }
        iArr[0] = i;
        iArr[1] = i2;
    }

    public final ViewHolder findViewHolderForAdapterPosition(int i) {
        ViewHolder viewHolder = null;
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i2 = 0; i2 < unfilteredChildCount; i2++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i2));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && getAdapterPositionInRecyclerView(childViewHolderInt) == i) {
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                viewHolder = childViewHolderInt;
            }
        }
        return viewHolder;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0036 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.recyclerview.widget.RecyclerView.ViewHolder findViewHolderForPosition(int r6, boolean r7) {
        /*
            r5 = this;
            androidx.recyclerview.widget.ChildHelper r0 = r5.mChildHelper
            int r0 = r0.getUnfilteredChildCount()
            r1 = 0
            r2 = 0
        L_0x0008:
            if (r2 >= r0) goto L_0x003a
            androidx.recyclerview.widget.ChildHelper r3 = r5.mChildHelper
            android.view.View r3 = r3.getUnfilteredChildAt(r2)
            androidx.recyclerview.widget.RecyclerView$ViewHolder r3 = getChildViewHolderInt(r3)
            if (r3 == 0) goto L_0x0037
            boolean r4 = r3.isRemoved()
            if (r4 != 0) goto L_0x0037
            if (r7 == 0) goto L_0x0023
            int r4 = r3.mPosition
            if (r4 == r6) goto L_0x002a
            goto L_0x0037
        L_0x0023:
            int r4 = r3.getLayoutPosition()
            if (r4 == r6) goto L_0x002a
            goto L_0x0037
        L_0x002a:
            androidx.recyclerview.widget.ChildHelper r1 = r5.mChildHelper
            android.view.View r4 = r3.itemView
            boolean r1 = r1.isHidden(r4)
            if (r1 == 0) goto L_0x0036
            r1 = r3
            goto L_0x0037
        L_0x0036:
            return r3
        L_0x0037:
            int r2 = r2 + 1
            goto L_0x0008
        L_0x003a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.findViewHolderForPosition(int, boolean):androidx.recyclerview.widget.RecyclerView$ViewHolder");
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x016a, code lost:
        if (r3 > 0) goto L_0x019e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0184, code lost:
        if (r6 > 0) goto L_0x019e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0187, code lost:
        if (r3 < 0) goto L_0x019e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x018a, code lost:
        if (r6 < 0) goto L_0x019e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0192, code lost:
        if ((r6 * r2) <= 0) goto L_0x019d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x019a, code lost:
        if ((r6 * r2) >= 0) goto L_0x019d;
     */
    /* JADX WARN: Removed duplicated region for block: B:132:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:134:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0073  */
    @Override // android.view.ViewGroup, android.view.ViewParent
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View focusSearch(android.view.View r14, int r15) {
        /*
            Method dump skipped, instructions count: 422
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.focusSearch(android.view.View, int):android.view.View");
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateDefaultLayoutParams();
        }
        throw new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m("RecyclerView has no LayoutManager")));
    }

    @Override // android.view.View
    public final int getBaseline() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return super.getBaseline();
        }
        Objects.requireNonNull(layoutManager);
        return -1;
    }

    public final long getChangedHolderKey(ViewHolder viewHolder) {
        Adapter adapter = this.mAdapter;
        Objects.requireNonNull(adapter);
        if (!adapter.mHasStableIds) {
            return viewHolder.mPosition;
        }
        Objects.requireNonNull(viewHolder);
        return viewHolder.mItemId;
    }

    @Override // android.view.ViewGroup
    public int getChildDrawingOrder(int i, int i2) {
        ChildDrawingOrderCallback childDrawingOrderCallback = this.mChildDrawingOrderCallback;
        if (childDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(i, i2);
        }
        return childDrawingOrderCallback.onGetChildDrawingOrder();
    }

    public final NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return this.mScrollingChildHelper;
    }

    public void initFastScroller(StateListDrawable stateListDrawable, Drawable drawable, StateListDrawable stateListDrawable2, Drawable drawable2) {
        if (stateListDrawable == null || drawable == null || stateListDrawable2 == null || drawable2 == null) {
            throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m("Trying to set fast scroller without both required drawables.")));
        }
        Resources resources = getContext().getResources();
        new FastScroller(this, stateListDrawable, drawable, stateListDrawable2, drawable2, resources.getDimensionPixelSize(2131165722), resources.getDimensionPixelSize(2131165724), resources.getDimensionPixelOffset(2131165723));
    }

    public final boolean isComputingLayout() {
        if (this.mLayoutOrScrollCounter > 0) {
            return true;
        }
        return false;
    }

    public final void jumpToPositionForSmoothScroller(int i) {
        if (this.mLayout != null) {
            setScrollState(2);
            this.mLayout.scrollToPosition(i);
            awakenScrollBars();
        }
    }

    public final void markItemDecorInsetsDirty() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ((LayoutParams) this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        Recycler recycler = this.mRecycler;
        Objects.requireNonNull(recycler);
        int size = recycler.mCachedViews.size();
        for (int i2 = 0; i2 < size; i2++) {
            LayoutParams layoutParams = (LayoutParams) recycler.mCachedViews.get(i2).itemView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.mInsetsDirty = true;
            }
        }
    }

    public final void offsetPositionRecordsForRemove(int i, int i2, boolean z) {
        int i3 = i + i2;
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i4 = 0; i4 < unfilteredChildCount; i4++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i4));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                int i5 = childViewHolderInt.mPosition;
                if (i5 >= i3) {
                    childViewHolderInt.offsetPosition(-i2, z);
                    this.mState.mStructureChanged = true;
                } else if (i5 >= i) {
                    childViewHolderInt.addFlags(8);
                    childViewHolderInt.offsetPosition(-i2, z);
                    childViewHolderInt.mPosition = i - 1;
                    this.mState.mStructureChanged = true;
                }
            }
        }
        Recycler recycler = this.mRecycler;
        Objects.requireNonNull(recycler);
        int size = recycler.mCachedViews.size();
        while (true) {
            size--;
            if (size >= 0) {
                ViewHolder viewHolder = recycler.mCachedViews.get(size);
                if (viewHolder != null) {
                    int i6 = viewHolder.mPosition;
                    if (i6 >= i3) {
                        viewHolder.offsetPosition(-i2, z);
                    } else if (i6 >= i) {
                        viewHolder.addFlags(8);
                        recycler.recycleCachedViewAt(size);
                    }
                }
            } else {
                requestLayout();
                return;
            }
        }
    }

    public final void onEnterLayoutOrScroll() {
        this.mLayoutOrScrollCounter++;
    }

    public final void onExitLayoutOrScroll(boolean z) {
        int i;
        boolean z2 = true;
        int i2 = this.mLayoutOrScrollCounter - 1;
        this.mLayoutOrScrollCounter = i2;
        if (i2 < 1) {
            this.mLayoutOrScrollCounter = 0;
            if (z) {
                int i3 = this.mEatenAccessibilityChangeFlags;
                this.mEatenAccessibilityChangeFlags = 0;
                if (i3 != 0) {
                    AccessibilityManager accessibilityManager = this.mAccessibilityManager;
                    if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
                        z2 = false;
                    }
                    if (z2) {
                        AccessibilityEvent obtain = AccessibilityEvent.obtain();
                        obtain.setEventType(2048);
                        obtain.setContentChangeTypes(i3);
                        sendAccessibilityEventUnchecked(obtain);
                    }
                }
                for (int size = this.mPendingAccessibilityImportanceChange.size() - 1; size >= 0; size--) {
                    ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(size);
                    if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore() && (i = viewHolder.mPendingAccessibilityState) != -1) {
                        View view = viewHolder.itemView;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        ViewCompat.Api16Impl.setImportantForAccessibility(view, i);
                        viewHolder.mPendingAccessibilityState = -1;
                    }
                }
                this.mPendingAccessibilityImportanceChange.clear();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x007f  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onGenericMotionEvent(android.view.MotionEvent r15) {
        /*
            Method dump skipped, instructions count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.onGenericMotionEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        if (this.mLayoutSuppressed) {
            return false;
        }
        this.mInterceptingOnItemTouchListener = null;
        if (findInterceptingOnItemTouchListener(motionEvent)) {
            resetScroll();
            setScrollState(0);
            return true;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return false;
        }
        boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            if (this.mIgnoreMotionEventTillDown) {
                this.mIgnoreMotionEventTillDown = false;
            }
            this.mScrollPointerId = motionEvent.getPointerId(0);
            int x = (int) (motionEvent.getX() + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            int y = (int) (motionEvent.getY() + 0.5f);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
            EdgeEffect edgeEffect = this.mLeftGlow;
            if (edgeEffect == null || EdgeEffectCompat$Api31Impl.getDistance(edgeEffect) == 0.0f) {
                z = false;
            } else {
                EdgeEffectCompat$Api31Impl.onPullDistance(this.mLeftGlow, 0.0f, 1.0f - (motionEvent.getY() / getHeight()));
                z = true;
            }
            EdgeEffect edgeEffect2 = this.mRightGlow;
            boolean z3 = z;
            if (edgeEffect2 != null) {
                z3 = z;
                if (EdgeEffectCompat$Api31Impl.getDistance(edgeEffect2) != 0.0f) {
                    EdgeEffectCompat$Api31Impl.onPullDistance(this.mRightGlow, 0.0f, motionEvent.getY() / getHeight());
                    z3 = true;
                }
            }
            EdgeEffect edgeEffect3 = this.mTopGlow;
            boolean z4 = z3;
            if (edgeEffect3 != null) {
                z4 = z3;
                if (EdgeEffectCompat$Api31Impl.getDistance(edgeEffect3) != 0.0f) {
                    EdgeEffectCompat$Api31Impl.onPullDistance(this.mTopGlow, 0.0f, motionEvent.getX() / getWidth());
                    z4 = true;
                }
            }
            EdgeEffect edgeEffect4 = this.mBottomGlow;
            boolean z5 = z4;
            if (edgeEffect4 != null) {
                z5 = z4;
                if (EdgeEffectCompat$Api31Impl.getDistance(edgeEffect4) != 0.0f) {
                    EdgeEffectCompat$Api31Impl.onPullDistance(this.mBottomGlow, 0.0f, 1.0f - (motionEvent.getX() / getWidth()));
                    z5 = true;
                }
            }
            if (z5 || this.mScrollState == 2) {
                getParent().requestDisallowInterceptTouchEvent(true);
                setScrollState(1);
                getScrollingChildHelper().stopNestedScroll(1);
            }
            int[] iArr = this.mNestedOffsets;
            iArr[1] = 0;
            iArr[0] = 0;
            if (canScrollVertically) {
                boolean z6 = canScrollHorizontally ? 1 : 0;
                boolean z7 = canScrollHorizontally ? 1 : 0;
                canScrollHorizontally = z6 | true;
            }
            NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
            int i = canScrollHorizontally ? 1 : 0;
            int i2 = canScrollHorizontally ? 1 : 0;
            scrollingChildHelper.startNestedScroll(i, 0);
        } else if (actionMasked == 1) {
            this.mVelocityTracker.clear();
            getScrollingChildHelper().stopNestedScroll(0);
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
            if (findPointerIndex < 0) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error processing scroll; pointer index for id ");
                m.append(this.mScrollPointerId);
                m.append(" not found. Did any MotionEvents get skipped?");
                Log.e("RecyclerView", m.toString());
                return false;
            }
            int x2 = (int) (motionEvent.getX(findPointerIndex) + 0.5f);
            int y2 = (int) (motionEvent.getY(findPointerIndex) + 0.5f);
            if (this.mScrollState != 1) {
                int i3 = x2 - this.mInitialTouchX;
                int i4 = y2 - this.mInitialTouchY;
                if (!canScrollHorizontally || Math.abs(i3) <= this.mTouchSlop) {
                    z2 = false;
                } else {
                    this.mLastTouchX = x2;
                    z2 = true;
                }
                if (canScrollVertically && Math.abs(i4) > this.mTouchSlop) {
                    this.mLastTouchY = y2;
                    z2 = true;
                }
                if (z2) {
                    setScrollState(1);
                }
            }
        } else if (actionMasked == 3) {
            resetScroll();
            setScrollState(0);
        } else if (actionMasked == 5) {
            this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
            int x3 = (int) (motionEvent.getX(actionIndex) + 0.5f);
            this.mLastTouchX = x3;
            this.mInitialTouchX = x3;
            int y3 = (int) (motionEvent.getY(actionIndex) + 0.5f);
            this.mLastTouchY = y3;
            this.mInitialTouchY = y3;
        } else if (actionMasked == 6) {
            onPointerUp(motionEvent);
        }
        if (this.mScrollState == 1) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Trace.beginSection("RV OnLayout");
        dispatchLayout();
        Trace.endSection();
        this.mFirstLayoutComplete = true;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            defaultOnMeasure(i, i2);
            return;
        }
        boolean z = false;
        if (layoutManager.isAutoMeasureEnabled()) {
            int mode = View.MeasureSpec.getMode(i);
            int mode2 = View.MeasureSpec.getMode(i2);
            this.mLayout.onMeasure(this.mRecycler, this.mState, i, i2);
            if (mode == 1073741824 && mode2 == 1073741824) {
                z = true;
            }
            this.mLastAutoMeasureSkippedDueToExact = z;
            if (!z && this.mAdapter != null) {
                if (this.mState.mLayoutStep == 1) {
                    dispatchLayoutStep1();
                }
                this.mLayout.setMeasureSpecs(i, i2);
                this.mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(i, i2);
                if (this.mLayout.shouldMeasureTwice()) {
                    this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
                    this.mState.mIsMeasuring = true;
                    dispatchLayoutStep2();
                    this.mLayout.setMeasuredDimensionFromChildren(i, i2);
                }
                this.mLastAutoMeasureNonExactMeasuredWidth = getMeasuredWidth();
                this.mLastAutoMeasureNonExactMeasuredHeight = getMeasuredHeight();
            }
        } else if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, i, i2);
        } else {
            if (this.mAdapterUpdateDuringMeasure) {
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                processAdapterUpdatesAndSetAnimationFlags();
                onExitLayoutOrScroll(true);
                State state = this.mState;
                if (state.mRunPredictiveAnimations) {
                    state.mInPreLayout = true;
                } else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.mAdapterUpdateDuringMeasure = false;
                stopInterceptRequestLayout(false);
            } else if (this.mState.mRunPredictiveAnimations) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
                return;
            }
            Adapter adapter = this.mAdapter;
            if (adapter != null) {
                this.mState.mItemCount = adapter.getItemCount();
            } else {
                this.mState.mItemCount = 0;
            }
            startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, i, i2);
            stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mPendingSavedState = savedState;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        requestLayout();
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SavedState savedState2 = this.mPendingSavedState;
        if (savedState2 != null) {
            savedState.mLayoutState = savedState2.mLayoutState;
        } else {
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager != null) {
                savedState.mLayoutState = layoutManager.onSaveInstanceState();
            } else {
                savedState.mLayoutState = null;
            }
        }
        return savedState;
    }

    /* JADX WARN: Code restructure failed: missing block: B:222:0x036e, code lost:
        if (r1 < r3) goto L_0x0371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0381, code lost:
        if (r3 != 0) goto L_0x03e3;
     */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0376  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x03ea  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x041e  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0426  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0116  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r20) {
        /*
            Method dump skipped, instructions count: 1070
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            AnonymousClass2 r0 = this.mItemAnimatorRunner;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postOnAnimation(this, r0);
            this.mPostedAnimatorRunner = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0067, code lost:
        if (r4.mHasStableIds != false) goto L_0x0069;
     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void processAdapterUpdatesAndSetAnimationFlags() {
        /*
            r6 = this;
            boolean r0 = r6.mDataSetHasChangedAfterLayout
            r1 = 0
            if (r0 == 0) goto L_0x001f
            androidx.recyclerview.widget.AdapterHelper r0 = r6.mAdapterHelper
            java.util.Objects.requireNonNull(r0)
            java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r2 = r0.mPendingUpdates
            r0.recycleUpdateOpsAndClearList(r2)
            java.util.ArrayList<androidx.recyclerview.widget.AdapterHelper$UpdateOp> r2 = r0.mPostponedList
            r0.recycleUpdateOpsAndClearList(r2)
            r0.mExistingUpdateTypes = r1
            boolean r0 = r6.mDispatchItemsChangedEvent
            if (r0 == 0) goto L_0x001f
            androidx.recyclerview.widget.RecyclerView$LayoutManager r0 = r6.mLayout
            r0.onItemsChanged()
        L_0x001f:
            androidx.recyclerview.widget.RecyclerView$ItemAnimator r0 = r6.mItemAnimator
            r2 = 1
            if (r0 == 0) goto L_0x002e
            androidx.recyclerview.widget.RecyclerView$LayoutManager r0 = r6.mLayout
            boolean r0 = r0.supportsPredictiveItemAnimations()
            if (r0 == 0) goto L_0x002e
            r0 = r2
            goto L_0x002f
        L_0x002e:
            r0 = r1
        L_0x002f:
            if (r0 == 0) goto L_0x0037
            androidx.recyclerview.widget.AdapterHelper r0 = r6.mAdapterHelper
            r0.preProcess()
            goto L_0x003c
        L_0x0037:
            androidx.recyclerview.widget.AdapterHelper r0 = r6.mAdapterHelper
            r0.consumeUpdatesInOnePass()
        L_0x003c:
            boolean r0 = r6.mItemsAddedOrRemoved
            if (r0 != 0) goto L_0x0047
            boolean r0 = r6.mItemsChanged
            if (r0 == 0) goto L_0x0045
            goto L_0x0047
        L_0x0045:
            r0 = r1
            goto L_0x0048
        L_0x0047:
            r0 = r2
        L_0x0048:
            androidx.recyclerview.widget.RecyclerView$State r3 = r6.mState
            boolean r4 = r6.mFirstLayoutComplete
            if (r4 == 0) goto L_0x006b
            androidx.recyclerview.widget.RecyclerView$ItemAnimator r4 = r6.mItemAnimator
            if (r4 == 0) goto L_0x006b
            boolean r4 = r6.mDataSetHasChangedAfterLayout
            if (r4 != 0) goto L_0x005e
            if (r0 != 0) goto L_0x005e
            androidx.recyclerview.widget.RecyclerView$LayoutManager r5 = r6.mLayout
            boolean r5 = r5.mRequestedSimpleAnimations
            if (r5 == 0) goto L_0x006b
        L_0x005e:
            if (r4 == 0) goto L_0x0069
            androidx.recyclerview.widget.RecyclerView$Adapter r4 = r6.mAdapter
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mHasStableIds
            if (r4 == 0) goto L_0x006b
        L_0x0069:
            r4 = r2
            goto L_0x006c
        L_0x006b:
            r4 = r1
        L_0x006c:
            r3.mRunSimpleAnimations = r4
            androidx.recyclerview.widget.RecyclerView$State r3 = r6.mState
            boolean r4 = r3.mRunSimpleAnimations
            if (r4 == 0) goto L_0x008c
            if (r0 == 0) goto L_0x008c
            boolean r0 = r6.mDataSetHasChangedAfterLayout
            if (r0 != 0) goto L_0x008c
            androidx.recyclerview.widget.RecyclerView$ItemAnimator r0 = r6.mItemAnimator
            if (r0 == 0) goto L_0x0088
            androidx.recyclerview.widget.RecyclerView$LayoutManager r6 = r6.mLayout
            boolean r6 = r6.supportsPredictiveItemAnimations()
            if (r6 == 0) goto L_0x0088
            r6 = r2
            goto L_0x0089
        L_0x0088:
            r6 = r1
        L_0x0089:
            if (r6 == 0) goto L_0x008c
            r1 = r2
        L_0x008c:
            r3.mRunPredictiveAnimations = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.processAdapterUpdatesAndSetAnimationFlags():void");
    }

    public final void processDataSetCompletelyChanged(boolean z) {
        this.mDispatchItemsChangedEvent = z | this.mDispatchItemsChangedEvent;
        this.mDataSetHasChangedAfterLayout = true;
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(6);
            }
        }
        markItemDecorInsetsDirty();
        Recycler recycler = this.mRecycler;
        Objects.requireNonNull(recycler);
        int size = recycler.mCachedViews.size();
        for (int i2 = 0; i2 < size; i2++) {
            ViewHolder viewHolder = recycler.mCachedViews.get(i2);
            if (viewHolder != null) {
                viewHolder.addFlags(6);
                viewHolder.addChangePayload(null);
            }
        }
        Adapter adapter = RecyclerView.this.mAdapter;
        if (adapter == null || !adapter.mHasStableIds) {
            recycler.recycleAndClearCachedViews();
        }
    }

    public final void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo) {
        boolean z = false;
        int i = (viewHolder.mFlags & (-8193)) | 0;
        viewHolder.mFlags = i;
        if (this.mState.mTrackOldChangeHolders) {
            if ((i & 2) != 0) {
                z = true;
            }
            if (z && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
                long changedHolderKey = getChangedHolderKey(viewHolder);
                ViewInfoStore viewInfoStore = this.mViewInfoStore;
                Objects.requireNonNull(viewInfoStore);
                viewInfoStore.mOldChangedHolders.put(changedHolderKey, viewHolder);
            }
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }

    public final void removeAndRecycleViews() {
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        Recycler recycler = this.mRecycler;
        Objects.requireNonNull(recycler);
        recycler.mAttachedScrap.clear();
        recycler.recycleAndClearCachedViews();
    }

    public final void removeItemDecoration(ItemDecoration itemDecoration) {
        boolean z;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(itemDecoration);
        if (this.mItemDecorations.isEmpty()) {
            if (getOverScrollMode() == 2) {
                z = true;
            } else {
                z = false;
            }
            setWillNotDraw(z);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestChildFocus(View view, View view2) {
        if (!this.mLayout.onRequestChildFocus(this, view, view2) && view2 != null) {
            requestChildOnScreen(view, view2);
        }
        super.requestChildFocus(view, view2);
    }

    public final void requestChildOnScreen(View view, View view2) {
        View view3;
        boolean z;
        if (view2 != null) {
            view3 = view2;
        } else {
            view3 = view;
        }
        this.mTempRect.set(0, 0, view3.getWidth(), view3.getHeight());
        ViewGroup.LayoutParams layoutParams = view3.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            if (!layoutParams2.mInsetsDirty) {
                Rect rect = layoutParams2.mDecorInsets;
                Rect rect2 = this.mTempRect;
                rect2.left -= rect.left;
                rect2.right += rect.right;
                rect2.top -= rect.top;
                rect2.bottom += rect.bottom;
            }
        }
        if (view2 != null) {
            offsetDescendantRectToMyCoords(view2, this.mTempRect);
            offsetRectIntoDescendantCoords(view, this.mTempRect);
        }
        LayoutManager layoutManager = this.mLayout;
        Rect rect3 = this.mTempRect;
        boolean z2 = !this.mFirstLayoutComplete;
        if (view2 == null) {
            z = true;
        } else {
            z = false;
        }
        layoutManager.requestChildRectangleOnScreen(this, view, rect3, z2, z);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, z);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        int size = this.mOnItemTouchListeners.size();
        for (int i = 0; i < size; i++) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(z);
        }
        super.requestDisallowInterceptTouchEvent(z);
    }

    @Override // android.view.View, android.view.ViewParent
    public final void requestLayout() {
        if (this.mInterceptRequestLayoutDepth != 0 || this.mLayoutSuppressed) {
            this.mLayoutWasDefered = true;
        } else {
            super.requestLayout();
        }
    }

    public final void resetScroll() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.clear();
        }
        boolean z = false;
        getScrollingChildHelper().stopNestedScroll(0);
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            z = this.mLeftGlow.isFinished();
        }
        EdgeEffect edgeEffect2 = this.mTopGlow;
        if (edgeEffect2 != null) {
            edgeEffect2.onRelease();
            z |= this.mTopGlow.isFinished();
        }
        EdgeEffect edgeEffect3 = this.mRightGlow;
        if (edgeEffect3 != null) {
            edgeEffect3.onRelease();
            z |= this.mRightGlow.isFinished();
        }
        EdgeEffect edgeEffect4 = this.mBottomGlow;
        if (edgeEffect4 != null) {
            edgeEffect4.onRelease();
            z |= this.mBottomGlow.isFinished();
        }
        if (z) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.View
    public final void scrollBy(int i, int i2) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutSuppressed) {
            boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
            boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (canScrollHorizontally || canScrollVertically) {
                if (!canScrollHorizontally) {
                    i = 0;
                }
                if (!canScrollVertically) {
                    i2 = 0;
                }
                scrollByInternal(i, i2, null, 0);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean scrollByInternal(int r18, int r19, android.view.MotionEvent r20, int r21) {
        /*
            Method dump skipped, instructions count: 318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.scrollByInternal(int, int, android.view.MotionEvent, int):boolean");
    }

    @Override // android.view.View
    public final void scrollTo(int i, int i2) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollToPosition(int i) {
        if (!this.mLayoutSuppressed) {
            stopScroll();
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager == null) {
                Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
                return;
            }
            layoutManager.scrollToPosition(i);
            awakenScrollBars();
        }
    }

    @Override // android.view.ViewGroup
    public final void setClipToPadding(boolean z) {
        if (z != this.mClipToPadding) {
            this.mBottomGlow = null;
            this.mTopGlow = null;
            this.mRightGlow = null;
            this.mLeftGlow = null;
        }
        this.mClipToPadding = z;
        super.setClipToPadding(z);
        if (this.mFirstLayoutComplete) {
            requestLayout();
        }
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager != this.mLayout) {
            stopScroll();
            if (this.mLayout != null) {
                ItemAnimator itemAnimator = this.mItemAnimator;
                if (itemAnimator != null) {
                    itemAnimator.endAnimations();
                }
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
                Recycler recycler = this.mRecycler;
                Objects.requireNonNull(recycler);
                recycler.mAttachedScrap.clear();
                recycler.recycleAndClearCachedViews();
                if (this.mIsAttached) {
                    LayoutManager layoutManager2 = this.mLayout;
                    Objects.requireNonNull(layoutManager2);
                    layoutManager2.mIsAttachedToWindow = false;
                    layoutManager2.onDetachedFromWindow(this);
                }
                this.mLayout.setRecyclerView(null);
                this.mLayout = null;
            } else {
                Recycler recycler2 = this.mRecycler;
                Objects.requireNonNull(recycler2);
                recycler2.mAttachedScrap.clear();
                recycler2.recycleAndClearCachedViews();
            }
            ChildHelper childHelper = this.mChildHelper;
            Objects.requireNonNull(childHelper);
            childHelper.mBucket.reset();
            int size = childHelper.mHiddenViews.size();
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                AnonymousClass5 r3 = (AnonymousClass5) childHelper.mCallback;
                Objects.requireNonNull(r3);
                ViewHolder childViewHolderInt = getChildViewHolderInt((View) childHelper.mHiddenViews.get(size));
                if (childViewHolderInt != null) {
                    RecyclerView.this.setChildImportantForAccessibilityInternal(childViewHolderInt, childViewHolderInt.mWasImportantForAccessibilityBeforeHidden);
                    childViewHolderInt.mWasImportantForAccessibilityBeforeHidden = 0;
                }
                childHelper.mHiddenViews.remove(size);
            }
            AnonymousClass5 r0 = (AnonymousClass5) childHelper.mCallback;
            Objects.requireNonNull(r0);
            int childCount = r0.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = RecyclerView.this.getChildAt(i);
                RecyclerView.this.dispatchChildDetached(childAt);
                childAt.clearAnimation();
            }
            RecyclerView.this.removeAllViews();
            this.mLayout = layoutManager;
            if (layoutManager != null) {
                if (layoutManager.mRecyclerView == null) {
                    layoutManager.setRecyclerView(this);
                    if (this.mIsAttached) {
                        LayoutManager layoutManager3 = this.mLayout;
                        Objects.requireNonNull(layoutManager3);
                        layoutManager3.mIsAttachedToWindow = true;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("LayoutManager ");
                    sb.append(layoutManager);
                    sb.append(" is already attached to a RecyclerView:");
                    throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(layoutManager.mRecyclerView, sb));
                }
            }
            this.mRecycler.updateViewCacheSize();
            requestLayout();
        }
    }

    @Override // android.view.ViewGroup
    @Deprecated
    public final void setLayoutTransition(LayoutTransition layoutTransition) {
        if (layoutTransition == null) {
            super.setLayoutTransition(null);
            return;
        }
        throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
    }

    final void setScrollState(int i) {
        SmoothScroller smoothScroller;
        if (i != this.mScrollState) {
            this.mScrollState = i;
            if (i != 2) {
                ViewFlinger viewFlinger = this.mViewFlinger;
                Objects.requireNonNull(viewFlinger);
                RecyclerView.this.removeCallbacks(viewFlinger);
                viewFlinger.mOverScroller.abortAnimation();
                LayoutManager layoutManager = this.mLayout;
                if (!(layoutManager == null || (smoothScroller = layoutManager.mSmoothScroller) == null)) {
                    smoothScroller.stop();
                }
            }
            LayoutManager layoutManager2 = this.mLayout;
            if (layoutManager2 != null) {
                layoutManager2.onScrollStateChanged(i);
            }
            ArrayList arrayList = this.mScrollListeners;
            if (arrayList != null) {
                int size = arrayList.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        ((OnScrollListener) this.mScrollListeners.get(size)).onScrollStateChanged(this, i);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public final void smoothScrollBy$1(int i, int i2, boolean z) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutSuppressed) {
            int i3 = 0;
            if (!layoutManager.canScrollHorizontally()) {
                i = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                i2 = 0;
            }
            if (i != 0 || i2 != 0) {
                if (z) {
                    if (i != 0) {
                        i3 = 1;
                    }
                    if (i2 != 0) {
                        i3 |= 2;
                    }
                    getScrollingChildHelper().startNestedScroll(i3, 1);
                }
                this.mViewFlinger.smoothScrollBy(i, i2, Integer.MIN_VALUE, null);
            }
        }
    }

    public void smoothScrollToPosition(int i) {
        if (!this.mLayoutSuppressed) {
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager == null) {
                Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            } else {
                layoutManager.smoothScrollToPosition(this, i);
            }
        }
    }

    public final void startInterceptRequestLayout() {
        int i = this.mInterceptRequestLayoutDepth + 1;
        this.mInterceptRequestLayoutDepth = i;
        if (i == 1 && !this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
        }
    }

    public final void stopInterceptRequestLayout(boolean z) {
        if (this.mInterceptRequestLayoutDepth < 1) {
            this.mInterceptRequestLayoutDepth = 1;
        }
        if (!z && !this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
        }
        if (this.mInterceptRequestLayoutDepth == 1) {
            if (z && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null) {
                dispatchLayout();
            }
            if (!this.mLayoutSuppressed) {
                this.mLayoutWasDefered = false;
            }
        }
        this.mInterceptRequestLayoutDepth--;
    }

    @Override // android.view.ViewGroup
    public final void suppressLayout(boolean z) {
        if (z != this.mLayoutSuppressed) {
            assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
            if (!z) {
                this.mLayoutSuppressed = false;
                if (!(!this.mLayoutWasDefered || this.mLayout == null || this.mAdapter == null)) {
                    requestLayout();
                }
                this.mLayoutWasDefered = false;
                return;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
            this.mLayoutSuppressed = true;
            this.mIgnoreMotionEventTillDown = true;
            stopScroll();
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [androidx.recyclerview.widget.RecyclerView$1] */
    /* JADX WARN: Type inference failed for: r1v10, types: [androidx.recyclerview.widget.RecyclerView$2] */
    public RecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ClassLoader classLoader;
        Constructor constructor;
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() { // from class: androidx.recyclerview.widget.RecyclerView.1
            @Override // java.lang.Runnable
            public final void run() {
                RecyclerView recyclerView = RecyclerView.this;
                if (recyclerView.mFirstLayoutComplete && !recyclerView.isLayoutRequested()) {
                    RecyclerView recyclerView2 = RecyclerView.this;
                    if (!recyclerView2.mIsAttached) {
                        recyclerView2.requestLayout();
                    } else if (recyclerView2.mLayoutSuppressed) {
                        recyclerView2.mLayoutWasDefered = true;
                    } else {
                        recyclerView2.consumePendingUpdateOperations();
                    }
                }
            }
        };
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mRecyclerListeners = new ArrayList();
        this.mItemDecorations = new ArrayList<>();
        this.mOnItemTouchListeners = new ArrayList<>();
        this.mInterceptRequestLayoutDepth = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mEdgeEffectFactory = sDefaultEdgeEffectFactory;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        this.mPrefetchRegistry = new GapWorker.LayoutPrefetchRegistryImpl();
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mNestedOffsets = new int[2];
        this.mReusableIntPair = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList();
        this.mItemAnimatorRunner = new Runnable() { // from class: androidx.recyclerview.widget.RecyclerView.2
            @Override // java.lang.Runnable
            public final void run() {
                ItemAnimator itemAnimator = RecyclerView.this.mItemAnimator;
                if (itemAnimator != null) {
                    itemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mLastAutoMeasureNonExactMeasuredWidth = 0;
        this.mLastAutoMeasureNonExactMeasuredHeight = 0;
        this.mViewInfoProcessCallback = new AnonymousClass4();
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = viewConfiguration.getScaledHorizontalScrollFactor();
        this.mScaledVerticalScrollFactor = viewConfiguration.getScaledVerticalScrollFactor();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setWillNotDraw(getOverScrollMode() == 2);
        ItemAnimator itemAnimator = this.mItemAnimator;
        ItemAnimatorRestoreListener itemAnimatorRestoreListener = this.mItemAnimatorListener;
        Objects.requireNonNull(itemAnimator);
        itemAnimator.mListener = itemAnimatorRestoreListener;
        this.mAdapterHelper = new AdapterHelper(new AnonymousClass6());
        this.mChildHelper = new ChildHelper(new AnonymousClass5());
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api26Impl.getImportantForAutofill(this) == 0) {
            ViewCompat.Api26Impl.setImportantForAutofill(this, 8);
        }
        if (ViewCompat.Api16Impl.getImportantForAccessibility(this) == 0) {
            ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate = new RecyclerViewAccessibilityDelegate(this);
        this.mAccessibilityDelegate = recyclerViewAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, recyclerViewAccessibilityDelegate);
        int[] iArr = R$styleable.RecyclerView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, 0);
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, 0);
        String string = obtainStyledAttributes.getString(8);
        if (obtainStyledAttributes.getInt(2, -1) == -1) {
            setDescendantFocusability(262144);
        }
        this.mClipToPadding = obtainStyledAttributes.getBoolean(1, true);
        if (obtainStyledAttributes.getBoolean(3, false)) {
            initFastScroller((StateListDrawable) obtainStyledAttributes.getDrawable(6), obtainStyledAttributes.getDrawable(7), (StateListDrawable) obtainStyledAttributes.getDrawable(4), obtainStyledAttributes.getDrawable(5));
        }
        obtainStyledAttributes.recycle();
        if (string != null) {
            String trim = string.trim();
            if (!trim.isEmpty()) {
                if (trim.charAt(0) == '.') {
                    trim = context.getPackageName() + trim;
                } else if (!trim.contains(".")) {
                    trim = RecyclerView.class.getPackage().getName() + '.' + trim;
                }
                try {
                    if (isInEditMode()) {
                        classLoader = getClass().getClassLoader();
                    } else {
                        classLoader = context.getClassLoader();
                    }
                    Class<? extends U> asSubclass = Class.forName(trim, false, classLoader).asSubclass(LayoutManager.class);
                    Object[] objArr = null;
                    try {
                        constructor = asSubclass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        objArr = new Object[]{context, attributeSet, Integer.valueOf(i), 0};
                    } catch (NoSuchMethodException e) {
                        try {
                            constructor = asSubclass.getConstructor(new Class[0]);
                        } catch (NoSuchMethodException e2) {
                            e2.initCause(e);
                            throw new IllegalStateException(attributeSet.getPositionDescription() + ": Error creating LayoutManager " + trim, e2);
                        }
                    }
                    constructor.setAccessible(true);
                    setLayoutManager((LayoutManager) constructor.newInstance(objArr));
                } catch (ClassCastException e3) {
                    throw new IllegalStateException(attributeSet.getPositionDescription() + ": Class is not a LayoutManager " + trim, e3);
                } catch (ClassNotFoundException e4) {
                    throw new IllegalStateException(attributeSet.getPositionDescription() + ": Unable to find LayoutManager " + trim, e4);
                } catch (IllegalAccessException e5) {
                    throw new IllegalStateException(attributeSet.getPositionDescription() + ": Cannot access non-public constructor " + trim, e5);
                } catch (InstantiationException e6) {
                    throw new IllegalStateException(attributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + trim, e6);
                } catch (InvocationTargetException e7) {
                    throw new IllegalStateException(attributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + trim, e7);
                }
            }
        }
        int[] iArr2 = NESTED_SCROLLING_ATTRS;
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, iArr2, i, 0);
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr2, attributeSet, obtainStyledAttributes2, i, 0);
        boolean z = obtainStyledAttributes2.getBoolean(0, true);
        obtainStyledAttributes2.recycle();
        setNestedScrollingEnabled(z);
    }

    public static int getChildAdapterPosition(View view) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            return childViewHolderInt.getAbsoluteAdapterPosition();
        }
        return -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int releaseVerticalGlow(int r4, float r5) {
        /*
            r3 = this;
            int r0 = r3.getWidth()
            float r0 = (float) r0
            float r5 = r5 / r0
            float r4 = (float) r4
            int r0 = r3.getHeight()
            float r0 = (float) r0
            float r4 = r4 / r0
            android.widget.EdgeEffect r0 = r3.mTopGlow
            r1 = 0
            if (r0 == 0) goto L_0x0033
            float r0 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L_0x0033
            android.widget.EdgeEffect r0 = r3.mTopGlow
            float r4 = -r4
            float r4 = androidx.core.widget.EdgeEffectCompat$Api31Impl.onPullDistance(r0, r4, r5)
            float r4 = -r4
            android.widget.EdgeEffect r5 = r3.mTopGlow
            float r5 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L_0x0031
            android.widget.EdgeEffect r5 = r3.mTopGlow
            r5.onRelease()
        L_0x0031:
            r1 = r4
            goto L_0x0058
        L_0x0033:
            android.widget.EdgeEffect r0 = r3.mBottomGlow
            if (r0 == 0) goto L_0x0058
            float r0 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L_0x0058
            android.widget.EdgeEffect r0 = r3.mBottomGlow
            r2 = 1065353216(0x3f800000, float:1.0)
            float r2 = r2 - r5
            float r4 = androidx.core.widget.EdgeEffectCompat$Api31Impl.onPullDistance(r0, r4, r2)
            android.widget.EdgeEffect r5 = r3.mBottomGlow
            float r5 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L_0x0031
            android.widget.EdgeEffect r5 = r3.mBottomGlow
            r5.onRelease()
            goto L_0x0031
        L_0x0058:
            int r4 = r3.getHeight()
            float r4 = (float) r4
            float r1 = r1 * r4
            int r4 = java.lang.Math.round(r1)
            if (r4 == 0) goto L_0x0067
            r3.invalidate()
        L_0x0067:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.releaseVerticalGlow(int, float):int");
    }

    public final void assertNotInLayoutOrScroll(String str) {
        if (isComputingLayout()) {
            if (str == null) {
                throw new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot call this method while RecyclerView is computing a layout or scrolling")));
            }
            throw new IllegalStateException(str);
        } else if (this.mDispatchScrollCounter > 0) {
            Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m(""))));
        }
    }

    public final void defaultOnMeasure(int i, int i2) {
        int paddingRight = getPaddingRight() + getPaddingLeft();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        setMeasuredDimension(LayoutManager.chooseSize(i, paddingRight, ViewCompat.Api16Impl.getMinimumWidth(this)), LayoutManager.chooseSize(i2, getPaddingBottom() + getPaddingTop(), ViewCompat.Api16Impl.getMinimumHeight(this)));
    }

    public final void dispatchChildDetached(View view) {
        getChildViewHolderInt(view);
        Adapter adapter = this.mAdapter;
        ArrayList arrayList = this.mOnChildAttachStateListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            while (true) {
                size--;
                if (size >= 0) {
                    ((OnChildAttachStateChangeListener) this.mOnChildAttachStateListeners.get(size)).onChildViewDetachedFromWindow(view);
                } else {
                    return;
                }
            }
        }
    }

    public final void dispatchLayoutStep2() {
        boolean z;
        boolean z2;
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        if (this.mPendingSavedState != null) {
            Adapter adapter = this.mAdapter;
            Objects.requireNonNull(adapter);
            int ordinal = adapter.mStateRestorationPolicy.ordinal();
            if (ordinal == 1 ? adapter.getItemCount() <= 0 : ordinal == 2) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                Parcelable parcelable = this.mPendingSavedState.mLayoutState;
                if (parcelable != null) {
                    this.mLayout.onRestoreInstanceState(parcelable);
                }
                this.mPendingSavedState = null;
            }
        }
        State state = this.mState;
        state.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, state);
        State state2 = this.mState;
        state2.mStructureChanged = false;
        if (!state2.mRunSimpleAnimations || this.mItemAnimator == null) {
            z = false;
        } else {
            z = true;
        }
        state2.mRunSimpleAnimations = z;
        state2.mLayoutStep = 4;
        onExitLayoutOrScroll(true);
        stopInterceptRequestLayout(false);
    }

    @Override // android.view.View
    public final boolean dispatchNestedFling(float f, float f2, boolean z) {
        return getScrollingChildHelper().dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.View
    public final boolean dispatchNestedPreFling(float f, float f2) {
        return getScrollingChildHelper().dispatchNestedPreFling(f, f2);
    }

    @Override // android.view.View
    public final boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        return scrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, 0);
    }

    @Override // android.view.View
    public final boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        return scrollingChildHelper.dispatchNestedScrollInternal(i, i2, i3, i4, iArr, 0, null);
    }

    @Override // android.view.View
    public final boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        boolean z;
        int i;
        boolean z2;
        boolean z3;
        int i2;
        super.draw(canvas);
        int size = this.mItemDecorations.size();
        boolean z4 = false;
        for (int i3 = 0; i3 < size; i3++) {
            this.mItemDecorations.get(i3).onDrawOver(canvas, this);
        }
        EdgeEffect edgeEffect = this.mLeftGlow;
        boolean z5 = true;
        if (edgeEffect == null || edgeEffect.isFinished()) {
            z = false;
        } else {
            int save = canvas.save();
            if (this.mClipToPadding) {
                i2 = getPaddingBottom();
            } else {
                i2 = 0;
            }
            canvas.rotate(270.0f);
            canvas.translate((-getHeight()) + i2, 0.0f);
            EdgeEffect edgeEffect2 = this.mLeftGlow;
            if (edgeEffect2 == null || !edgeEffect2.draw(canvas)) {
                z = false;
            } else {
                z = true;
            }
            canvas.restoreToCount(save);
        }
        EdgeEffect edgeEffect3 = this.mTopGlow;
        if (edgeEffect3 != null && !edgeEffect3.isFinished()) {
            int save2 = canvas.save();
            if (this.mClipToPadding) {
                canvas.translate(getPaddingLeft(), getPaddingTop());
            }
            EdgeEffect edgeEffect4 = this.mTopGlow;
            if (edgeEffect4 == null || !edgeEffect4.draw(canvas)) {
                z3 = false;
            } else {
                z3 = true;
            }
            z |= z3;
            canvas.restoreToCount(save2);
        }
        EdgeEffect edgeEffect5 = this.mRightGlow;
        if (edgeEffect5 != null && !edgeEffect5.isFinished()) {
            int save3 = canvas.save();
            int width = getWidth();
            if (this.mClipToPadding) {
                i = getPaddingTop();
            } else {
                i = 0;
            }
            canvas.rotate(90.0f);
            canvas.translate(i, -width);
            EdgeEffect edgeEffect6 = this.mRightGlow;
            if (edgeEffect6 == null || !edgeEffect6.draw(canvas)) {
                z2 = false;
            } else {
                z2 = true;
            }
            z |= z2;
            canvas.restoreToCount(save3);
        }
        EdgeEffect edgeEffect7 = this.mBottomGlow;
        if (edgeEffect7 != null && !edgeEffect7.isFinished()) {
            int save4 = canvas.save();
            canvas.rotate(180.0f);
            if (this.mClipToPadding) {
                canvas.translate(getPaddingRight() + (-getWidth()), getPaddingBottom() + (-getHeight()));
            } else {
                canvas.translate(-getWidth(), -getHeight());
            }
            EdgeEffect edgeEffect8 = this.mBottomGlow;
            if (edgeEffect8 != null && edgeEffect8.draw(canvas)) {
                z4 = true;
            }
            z |= z4;
            canvas.restoreToCount(save4);
        }
        if (z || this.mItemAnimator == null || this.mItemDecorations.size() <= 0 || !this.mItemAnimator.isRunning()) {
            z5 = z;
        }
        if (z5) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        return super.drawChild(canvas, view, j);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:?, code lost:
        return r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View findContainingItemView(android.view.View r3) {
        /*
            r2 = this;
            android.view.ViewParent r0 = r3.getParent()
        L_0x0004:
            if (r0 == 0) goto L_0x0014
            if (r0 == r2) goto L_0x0014
            boolean r1 = r0 instanceof android.view.View
            if (r1 == 0) goto L_0x0014
            r3 = r0
            android.view.View r3 = (android.view.View) r3
            android.view.ViewParent r0 = r3.getParent()
            goto L_0x0004
        L_0x0014:
            if (r0 != r2) goto L_0x0017
            goto L_0x0018
        L_0x0017:
            r3 = 0
        L_0x0018:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.findContainingItemView(android.view.View):android.view.View");
    }

    public final boolean findInterceptingOnItemTouchListener(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int size = this.mOnItemTouchListeners.size();
        for (int i = 0; i < size; i++) {
            OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (onItemTouchListener.onInterceptTouchEvent$1(motionEvent) && action != 3) {
                this.mInterceptingOnItemTouchListener = onItemTouchListener;
                return true;
            }
        }
        return false;
    }

    public final int getAdapterPositionInRecyclerView(ViewHolder viewHolder) {
        boolean z;
        Objects.requireNonNull(viewHolder);
        if ((viewHolder.mFlags & 524) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z && viewHolder.isBound()) {
            AdapterHelper adapterHelper = this.mAdapterHelper;
            int i = viewHolder.mPosition;
            Objects.requireNonNull(adapterHelper);
            int size = adapterHelper.mPendingUpdates.size();
            for (int i2 = 0; i2 < size; i2++) {
                AdapterHelper.UpdateOp updateOp = adapterHelper.mPendingUpdates.get(i2);
                int i3 = updateOp.cmd;
                if (i3 != 1) {
                    if (i3 == 2) {
                        int i4 = updateOp.positionStart;
                        if (i4 <= i) {
                            int i5 = updateOp.itemCount;
                            if (i4 + i5 <= i) {
                                i -= i5;
                            }
                        } else {
                            continue;
                        }
                    } else if (i3 == 8) {
                        int i6 = updateOp.positionStart;
                        if (i6 == i) {
                            i = updateOp.itemCount;
                        } else {
                            if (i6 < i) {
                                i--;
                            }
                            if (updateOp.itemCount <= i) {
                                i++;
                            }
                        }
                    }
                } else if (updateOp.positionStart <= i) {
                    i += updateOp.itemCount;
                }
            }
            return i;
        }
        return -1;
    }

    public final ViewHolder getChildViewHolder(View view) {
        ViewParent parent = view.getParent();
        if (parent == null || parent == this) {
            return getChildViewHolderInt(view);
        }
        throw new IllegalArgumentException("View " + view + " is not a direct child of " + this);
    }

    public final Rect getItemDecorInsetsForChild(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        State state = this.mState;
        Objects.requireNonNull(state);
        if (state.mInPreLayout && (layoutParams.isItemChanged() || layoutParams.mViewHolder.isInvalid())) {
            return layoutParams.mDecorInsets;
        }
        Rect rect = layoutParams.mDecorInsets;
        rect.set(0, 0, 0, 0);
        int size = this.mItemDecorations.size();
        for (int i = 0; i < size; i++) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
            int i2 = rect.left;
            Rect rect2 = this.mTempRect;
            rect.left = i2 + rect2.left;
            rect.top += rect2.top;
            rect.right += rect2.right;
            rect.bottom += rect2.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return rect;
    }

    @Override // android.view.View
    public final boolean hasNestedScrollingParent() {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        if (scrollingChildHelper.getNestedScrollingParentForType(0) != null) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final boolean isNestedScrollingEnabled() {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        return scrollingChildHelper.mIsNestedScrollingEnabled;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        boolean z;
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        this.mIsAttached = true;
        if (!this.mFirstLayoutComplete || isLayoutRequested()) {
            z = false;
        } else {
            z = true;
        }
        this.mFirstLayoutComplete = z;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            Objects.requireNonNull(layoutManager);
            layoutManager.mIsAttachedToWindow = true;
        }
        this.mPostedAnimatorRunner = false;
        ThreadLocal<GapWorker> threadLocal = GapWorker.sGapWorker;
        GapWorker gapWorker = threadLocal.get();
        this.mGapWorker = gapWorker;
        if (gapWorker == null) {
            this.mGapWorker = new GapWorker();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            Display display = ViewCompat.Api17Impl.getDisplay(this);
            float f = 60.0f;
            if (!isInEditMode() && display != null) {
                float refreshRate = display.getRefreshRate();
                if (refreshRate >= 30.0f) {
                    f = refreshRate;
                }
            }
            GapWorker gapWorker2 = this.mGapWorker;
            gapWorker2.mFrameIntervalNs = 1.0E9f / f;
            threadLocal.set(gapWorker2);
        }
        GapWorker gapWorker3 = this.mGapWorker;
        Objects.requireNonNull(gapWorker3);
        gapWorker3.mRecyclerViews.add(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
        }
        stopScroll();
        this.mIsAttached = false;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.mIsAttachedToWindow = false;
            layoutManager.onDetachedFromWindow(this);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        removeCallbacks(this.mItemAnimatorRunner);
        Objects.requireNonNull(this.mViewInfoStore);
        do {
        } while (ViewInfoStore.InfoRecord.sPool.acquire() != null);
        GapWorker gapWorker = this.mGapWorker;
        if (gapWorker != null) {
            gapWorker.mRecyclerViews.remove(this);
            this.mGapWorker = null;
        }
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = this.mItemDecorations.size();
        for (int i = 0; i < size; i++) {
            this.mItemDecorations.get(i).onDraw(canvas, this);
        }
    }

    public final void onPointerUp(MotionEvent motionEvent) {
        int i;
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mScrollPointerId) {
            if (actionIndex == 0) {
                i = 1;
            } else {
                i = 0;
            }
            this.mScrollPointerId = motionEvent.getPointerId(i);
            int x = (int) (motionEvent.getX(i) + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            int y = (int) (motionEvent.getY(i) + 0.5f);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        if (isComputingLayout()) {
            return false;
        }
        return super.onRequestFocusInDescendants(i, rect);
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 || i2 != i4) {
            this.mBottomGlow = null;
            this.mTopGlow = null;
            this.mRightGlow = null;
            this.mLeftGlow = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int releaseHorizontalGlow(int r4, float r5) {
        /*
            r3 = this;
            int r0 = r3.getHeight()
            float r0 = (float) r0
            float r5 = r5 / r0
            float r4 = (float) r4
            int r0 = r3.getWidth()
            float r0 = (float) r0
            float r4 = r4 / r0
            android.widget.EdgeEffect r0 = r3.mLeftGlow
            r1 = 0
            if (r0 == 0) goto L_0x0036
            float r0 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L_0x0036
            android.widget.EdgeEffect r0 = r3.mLeftGlow
            float r4 = -r4
            r2 = 1065353216(0x3f800000, float:1.0)
            float r2 = r2 - r5
            float r4 = androidx.core.widget.EdgeEffectCompat$Api31Impl.onPullDistance(r0, r4, r2)
            float r4 = -r4
            android.widget.EdgeEffect r5 = r3.mLeftGlow
            float r5 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L_0x0034
            android.widget.EdgeEffect r5 = r3.mLeftGlow
            r5.onRelease()
        L_0x0034:
            r1 = r4
            goto L_0x0058
        L_0x0036:
            android.widget.EdgeEffect r0 = r3.mRightGlow
            if (r0 == 0) goto L_0x0058
            float r0 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L_0x0058
            android.widget.EdgeEffect r0 = r3.mRightGlow
            float r4 = androidx.core.widget.EdgeEffectCompat$Api31Impl.onPullDistance(r0, r4, r5)
            android.widget.EdgeEffect r5 = r3.mRightGlow
            float r5 = androidx.core.widget.EdgeEffectCompat$Api31Impl.getDistance(r5)
            int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r5 != 0) goto L_0x0034
            android.widget.EdgeEffect r5 = r3.mRightGlow
            r5.onRelease()
            goto L_0x0034
        L_0x0058:
            int r4 = r3.getWidth()
            float r4 = (float) r4
            float r1 = r1 * r4
            int r4 = java.lang.Math.round(r1)
            if (r4 == 0) goto L_0x0067
            r3.invalidate()
        L_0x0067:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.releaseHorizontalGlow(int, float):int");
    }

    @Override // android.view.ViewGroup
    public final void removeDetachedView(View view, boolean z) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached()) {
                childViewHolderInt.mFlags &= -257;
            } else if (!childViewHolderInt.shouldIgnore()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
                sb.append(childViewHolderInt);
                throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(this, sb));
            }
        }
        view.clearAnimation();
        dispatchChildDetached(view);
        super.removeDetachedView(view, z);
    }

    public final void scrollStep(int i, int i2, int[] iArr) {
        int i3;
        int i4;
        ViewHolder viewHolder;
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        Trace.beginSection("RV Scroll");
        fillRemainingScrollValues(this.mState);
        if (i != 0) {
            i3 = this.mLayout.scrollHorizontallyBy(i, this.mRecycler, this.mState);
        } else {
            i3 = 0;
        }
        if (i2 != 0) {
            i4 = this.mLayout.scrollVerticallyBy(i2, this.mRecycler, this.mState);
        } else {
            i4 = 0;
        }
        Trace.endSection();
        int childCount = this.mChildHelper.getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = this.mChildHelper.getChildAt(i5);
            ViewHolder childViewHolder = getChildViewHolder(childAt);
            if (!(childViewHolder == null || (viewHolder = childViewHolder.mShadowingHolder) == null)) {
                View view = viewHolder.itemView;
                int left = childAt.getLeft();
                int top = childAt.getTop();
                if (left != view.getLeft() || top != view.getTop()) {
                    view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
                }
            }
        }
        onExitLayoutOrScroll(true);
        stopInterceptRequestLayout(false);
        if (iArr != null) {
            iArr[0] = i3;
            iArr[1] = i4;
        }
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public final void sendAccessibilityEventUnchecked(AccessibilityEvent accessibilityEvent) {
        int i;
        int i2 = 0;
        if (isComputingLayout()) {
            if (accessibilityEvent != null) {
                i = accessibilityEvent.getContentChangeTypes();
            } else {
                i = 0;
            }
            if (i != 0) {
                i2 = i;
            }
            this.mEatenAccessibilityChangeFlags |= i2;
            i2 = 1;
        }
        if (i2 == 0) {
            super.sendAccessibilityEventUnchecked(accessibilityEvent);
        }
    }

    public boolean setChildImportantForAccessibilityInternal(ViewHolder viewHolder, int i) {
        if (isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = i;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        View view = viewHolder.itemView;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(view, i);
        return true;
    }

    @Override // android.view.View
    public final void setNestedScrollingEnabled(boolean z) {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        if (scrollingChildHelper.mIsNestedScrollingEnabled) {
            View view = scrollingChildHelper.mView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api21Impl.stopNestedScroll(view);
        }
        scrollingChildHelper.mIsNestedScrollingEnabled = z;
    }

    @Override // android.view.View
    public final boolean startNestedScroll(int i) {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        return scrollingChildHelper.startNestedScroll(i, 0);
    }

    @Override // android.view.View
    public final void stopNestedScroll() {
        NestedScrollingChildHelper scrollingChildHelper = getScrollingChildHelper();
        Objects.requireNonNull(scrollingChildHelper);
        scrollingChildHelper.stopNestedScroll(0);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams(layoutParams);
        }
        throw new IllegalStateException(ChildHelper$$ExternalSyntheticOutline0.m(this, VendorAtomValue$$ExternalSyntheticOutline1.m("RecyclerView has no LayoutManager")));
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchFreezeSelfOnly(sparseArray);
    }

    public void smoothScrollBy(int i, int i2) {
        smoothScrollBy$1(i, i2);
    }

    @Override // android.view.ViewGroup
    public final boolean getClipToPadding() {
        return this.mClipToPadding;
    }

    @Override // android.view.View
    public final boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    @Override // android.view.ViewGroup
    public final boolean isLayoutSuppressed() {
        return this.mLayoutSuppressed;
    }

    /* loaded from: classes.dex */
    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int i, int i2) {
        }

        public void onItemRangeInserted(int i, int i2) {
        }

        public void onItemRangeMoved(int i, int i2) {
        }

        public void onItemRangeRemoved(int i, int i2) {
        }

        public void onItemRangeChanged(int i, int i2, Object obj) {
            onItemRangeChanged(i, i2);
        }
    }
}
