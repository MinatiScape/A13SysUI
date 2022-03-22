package androidx.viewpager2.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R$styleable;
import androidx.viewpager2.adapter.StatefulAdapter;
import androidx.viewpager2.widget.ScrollEventAdapter;
import com.android.systemui.controls.management.StructureAdapter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ViewPager2 extends ViewGroup {
    public static final WindowInsetsCompat EMPTY_INSETS = new WindowInsetsCompat.BuilderImpl30().build();
    public PageAwareAccessibilityProvider mAccessibilityProvider;
    public int mCurrentItem;
    public FakeDrag mFakeDragger;
    public LinearLayoutManagerImpl mLayoutManager;
    public CompositeOnPageChangeCallback mPageChangeEventDispatcher;
    public PagerSnapHelperImpl mPagerSnapHelper;
    public Parcelable mPendingAdapterState;
    public RecyclerViewImpl mRecyclerView;
    public ScrollEventAdapter mScrollEventAdapter;
    public final Rect mTmpContainerRect = new Rect();
    public final Rect mTmpChildRect = new Rect();
    public CompositeOnPageChangeCallback mExternalPageChangeCallbacks = new CompositeOnPageChangeCallback();
    public boolean mCurrentItemDirty = false;
    public AnonymousClass1 mCurrentItemDataSetChangeObserver = new DataSetChangeObserver() { // from class: androidx.viewpager2.widget.ViewPager2.1
        @Override // androidx.viewpager2.widget.ViewPager2.DataSetChangeObserver, androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onChanged() {
            ViewPager2 viewPager2 = ViewPager2.this;
            viewPager2.mCurrentItemDirty = true;
            ScrollEventAdapter scrollEventAdapter = viewPager2.mScrollEventAdapter;
            Objects.requireNonNull(scrollEventAdapter);
            scrollEventAdapter.mDataSetChangeHappened = true;
        }
    };
    public int mPendingCurrentItem = -1;
    public boolean mUserInputEnabled = true;
    public int mOffscreenPageLimit = -1;

    /* loaded from: classes.dex */
    public abstract class AccessibilityProvider {
    }

    /* loaded from: classes.dex */
    public static abstract class DataSetChangeObserver extends RecyclerView.AdapterDataObserver {
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public abstract void onChanged();

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onItemRangeChanged(int i, int i2) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onItemRangeChanged(int i, int i2, Object obj) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onItemRangeInserted(int i, int i2) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onItemRangeMoved(int i, int i2) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public final void onItemRangeRemoved(int i, int i2) {
            onChanged();
        }
    }

    /* loaded from: classes.dex */
    public class LinearLayoutManagerImpl extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public final boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean z, boolean z2) {
            return false;
        }

        public LinearLayoutManagerImpl() {
            super(1);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public final void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
            int i;
            int i2;
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            int i3 = viewPager2.mOffscreenPageLimit;
            if (i3 == -1) {
                super.calculateExtraLayoutSpace(state, iArr);
                return;
            }
            ViewPager2 viewPager22 = ViewPager2.this;
            Objects.requireNonNull(viewPager22);
            RecyclerViewImpl recyclerViewImpl = viewPager22.mRecyclerView;
            LinearLayoutManagerImpl linearLayoutManagerImpl = viewPager22.mLayoutManager;
            Objects.requireNonNull(linearLayoutManagerImpl);
            if (linearLayoutManagerImpl.mOrientation == 0) {
                i2 = recyclerViewImpl.getWidth() - recyclerViewImpl.getPaddingLeft();
                i = recyclerViewImpl.getPaddingRight();
            } else {
                i2 = recyclerViewImpl.getHeight() - recyclerViewImpl.getPaddingTop();
                i = recyclerViewImpl.getPaddingBottom();
            }
            int i4 = (i2 - i) * i3;
            iArr[0] = i4;
            iArr[1] = i4;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public final void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int i;
            int i2;
            PageAwareAccessibilityProvider pageAwareAccessibilityProvider = ViewPager2.this.mAccessibilityProvider;
            Objects.requireNonNull(pageAwareAccessibilityProvider);
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            LinearLayoutManagerImpl linearLayoutManagerImpl = viewPager2.mLayoutManager;
            Objects.requireNonNull(linearLayoutManagerImpl);
            if (linearLayoutManagerImpl.mOrientation == 1) {
                Objects.requireNonNull(ViewPager2.this.mLayoutManager);
                i = RecyclerView.LayoutManager.getPosition(view);
            } else {
                i = 0;
            }
            ViewPager2 viewPager22 = ViewPager2.this;
            Objects.requireNonNull(viewPager22);
            LinearLayoutManagerImpl linearLayoutManagerImpl2 = viewPager22.mLayoutManager;
            Objects.requireNonNull(linearLayoutManagerImpl2);
            if (linearLayoutManagerImpl2.mOrientation == 0) {
                Objects.requireNonNull(ViewPager2.this.mLayoutManager);
                i2 = RecyclerView.LayoutManager.getPosition(view);
            } else {
                i2 = 0;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, i2, 1, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public final boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int i, Bundle bundle) {
            Objects.requireNonNull(ViewPager2.this.mAccessibilityProvider);
            return super.performAccessibilityAction(recycler, state, i, bundle);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public final void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(recycler, state, accessibilityNodeInfoCompat);
            Objects.requireNonNull(ViewPager2.this.mAccessibilityProvider);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OnPageChangeCallback {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }
    }

    /* loaded from: classes.dex */
    public class PageAwareAccessibilityProvider extends AccessibilityProvider {
        public AnonymousClass3 mAdapterDataObserver;
        public final AnonymousClass1 mActionPageForward = new AccessibilityViewCommand() { // from class: androidx.viewpager2.widget.ViewPager2.PageAwareAccessibilityProvider.1
            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public final boolean perform(View view) {
                ViewPager2 viewPager2 = (ViewPager2) view;
                PageAwareAccessibilityProvider pageAwareAccessibilityProvider = PageAwareAccessibilityProvider.this;
                Objects.requireNonNull(viewPager2);
                int i = viewPager2.mCurrentItem + 1;
                Objects.requireNonNull(pageAwareAccessibilityProvider);
                ViewPager2 viewPager22 = ViewPager2.this;
                Objects.requireNonNull(viewPager22);
                if (viewPager22.mUserInputEnabled) {
                    ViewPager2.this.setCurrentItemInternal(i);
                }
                return true;
            }
        };
        public final AnonymousClass2 mActionPageBackward = new AccessibilityViewCommand() { // from class: androidx.viewpager2.widget.ViewPager2.PageAwareAccessibilityProvider.2
            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public final boolean perform(View view) {
                ViewPager2 viewPager2 = (ViewPager2) view;
                PageAwareAccessibilityProvider pageAwareAccessibilityProvider = PageAwareAccessibilityProvider.this;
                Objects.requireNonNull(viewPager2);
                int i = viewPager2.mCurrentItem - 1;
                Objects.requireNonNull(pageAwareAccessibilityProvider);
                ViewPager2 viewPager22 = ViewPager2.this;
                Objects.requireNonNull(viewPager22);
                if (viewPager22.mUserInputEnabled) {
                    ViewPager2.this.setCurrentItemInternal(i);
                }
                return true;
            }
        };

        /* JADX WARN: Type inference failed for: r1v1, types: [androidx.viewpager2.widget.ViewPager2$PageAwareAccessibilityProvider$1] */
        /* JADX WARN: Type inference failed for: r1v2, types: [androidx.viewpager2.widget.ViewPager2$PageAwareAccessibilityProvider$2] */
        public PageAwareAccessibilityProvider() {
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [androidx.viewpager2.widget.ViewPager2$PageAwareAccessibilityProvider$3] */
        public final void onInitialize(RecyclerView recyclerView) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.setImportantForAccessibility(recyclerView, 2);
            this.mAdapterDataObserver = new DataSetChangeObserver() { // from class: androidx.viewpager2.widget.ViewPager2.PageAwareAccessibilityProvider.3
                @Override // androidx.viewpager2.widget.ViewPager2.DataSetChangeObserver, androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
                public final void onChanged() {
                    PageAwareAccessibilityProvider.this.updatePageAccessibilityActions();
                }
            };
            if (ViewCompat.Api16Impl.getImportantForAccessibility(ViewPager2.this) == 0) {
                ViewCompat.Api16Impl.setImportantForAccessibility(ViewPager2.this, 1);
            }
        }

        public final void updatePageAccessibilityActions() {
            int itemCount;
            int i;
            ViewPager2 viewPager2 = ViewPager2.this;
            int i2 = 16908360;
            ViewCompat.removeActionWithId(16908360, viewPager2);
            boolean z = false;
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(viewPager2, 0);
            ViewCompat.removeActionWithId(16908361, viewPager2);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(viewPager2, 0);
            ViewCompat.removeActionWithId(16908358, viewPager2);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(viewPager2, 0);
            ViewCompat.removeActionWithId(16908359, viewPager2);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(viewPager2, 0);
            if (ViewPager2.this.getAdapter() != null && (itemCount = ViewPager2.this.getAdapter().getItemCount()) != 0) {
                ViewPager2 viewPager22 = ViewPager2.this;
                Objects.requireNonNull(viewPager22);
                if (viewPager22.mUserInputEnabled) {
                    ViewPager2 viewPager23 = ViewPager2.this;
                    Objects.requireNonNull(viewPager23);
                    LinearLayoutManagerImpl linearLayoutManagerImpl = viewPager23.mLayoutManager;
                    Objects.requireNonNull(linearLayoutManagerImpl);
                    if (linearLayoutManagerImpl.mOrientation == 0) {
                        ViewPager2 viewPager24 = ViewPager2.this;
                        Objects.requireNonNull(viewPager24);
                        if (viewPager24.mLayoutManager.getLayoutDirection() == 1) {
                            z = true;
                        }
                        if (z) {
                            i = 16908360;
                        } else {
                            i = 16908361;
                        }
                        if (z) {
                            i2 = 16908361;
                        }
                        if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                            ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, (String) null), null, this.mActionPageForward);
                        }
                        if (ViewPager2.this.mCurrentItem > 0) {
                            ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i2, (String) null), null, this.mActionPageBackward);
                            return;
                        }
                        return;
                    }
                    if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                        ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908359, (String) null), null, this.mActionPageForward);
                    }
                    if (ViewPager2.this.mCurrentItem > 0) {
                        ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908358, (String) null), null, this.mActionPageBackward);
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class PagerSnapHelperImpl extends PagerSnapHelper {
        public PagerSnapHelperImpl() {
        }

        @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
        public final View findSnapView(RecyclerView.LayoutManager layoutManager) {
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            FakeDrag fakeDrag = viewPager2.mFakeDragger;
            Objects.requireNonNull(fakeDrag);
            ScrollEventAdapter scrollEventAdapter = fakeDrag.mScrollEventAdapter;
            Objects.requireNonNull(scrollEventAdapter);
            if (scrollEventAdapter.mFakeDragging) {
                return null;
            }
            return super.findSnapView(layoutManager);
        }
    }

    /* loaded from: classes.dex */
    public class RecyclerViewImpl extends RecyclerView {
        public RecyclerViewImpl(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public final CharSequence getAccessibilityClassName() {
            Objects.requireNonNull(ViewPager2.this.mAccessibilityProvider);
            return "androidx.recyclerview.widget.RecyclerView";
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            if (!viewPager2.mUserInputEnabled || !super.onInterceptTouchEvent(motionEvent)) {
                return false;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            if (!viewPager2.mUserInputEnabled || !super.onTouchEvent(motionEvent)) {
                return false;
            }
            return true;
        }

        @Override // android.view.View
        public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setFromIndex(ViewPager2.this.mCurrentItem);
            accessibilityEvent.setToIndex(ViewPager2.this.mCurrentItem);
            PageAwareAccessibilityProvider pageAwareAccessibilityProvider = ViewPager2.this.mAccessibilityProvider;
            Objects.requireNonNull(pageAwareAccessibilityProvider);
            accessibilityEvent.setSource(ViewPager2.this);
            accessibilityEvent.setClassName("androidx.viewpager.widget.ViewPager");
        }
    }

    /* loaded from: classes.dex */
    public static class SmoothScrollToPosition implements Runnable {
        public final int mPosition;
        public final RecyclerView mRecyclerView;

        @Override // java.lang.Runnable
        public final void run() {
            this.mRecyclerView.smoothScrollToPosition(this.mPosition);
        }

        public SmoothScrollToPosition(int i, RecyclerViewImpl recyclerViewImpl) {
            this.mPosition = i;
            this.mRecyclerView = recyclerViewImpl;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.viewpager2.widget.ViewPager2$1] */
    public ViewPager2(Context context) {
        super(context);
        initialize(context, null);
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.viewpager2.widget.ViewPager2.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Parcelable mAdapterState;
        public int mCurrentItem;
        public int mRecyclerViewId;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.mRecyclerViewId = parcel.readInt();
            this.mCurrentItem = parcel.readInt();
            this.mAdapterState = parcel.readParcelable(classLoader);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mRecyclerViewId);
            parcel.writeInt(this.mCurrentItem);
            parcel.writeParcelable(this.mAdapterState, i);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    @Override // android.view.View
    public final boolean canScrollHorizontally(int i) {
        return this.mRecyclerView.canScrollHorizontally(i);
    }

    @Override // android.view.View
    public final boolean canScrollVertically(int i) {
        return this.mRecyclerView.canScrollVertically(i);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final CharSequence getAccessibilityClassName() {
        Objects.requireNonNull(this.mAccessibilityProvider);
        Objects.requireNonNull(this.mAccessibilityProvider);
        return "androidx.viewpager.widget.ViewPager";
    }

    public final RecyclerView.Adapter getAdapter() {
        RecyclerViewImpl recyclerViewImpl = this.mRecyclerView;
        Objects.requireNonNull(recyclerViewImpl);
        return recyclerViewImpl.mAdapter;
    }

    /* JADX WARN: Finally extract failed */
    public final void initialize(Context context, AttributeSet attributeSet) {
        this.mAccessibilityProvider = new PageAwareAccessibilityProvider();
        RecyclerViewImpl recyclerViewImpl = new RecyclerViewImpl(context);
        this.mRecyclerView = recyclerViewImpl;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        recyclerViewImpl.setId(ViewCompat.Api17Impl.generateViewId());
        this.mRecyclerView.setDescendantFocusability(131072);
        LinearLayoutManagerImpl linearLayoutManagerImpl = new LinearLayoutManagerImpl();
        this.mLayoutManager = linearLayoutManagerImpl;
        this.mRecyclerView.setLayoutManager(linearLayoutManagerImpl);
        RecyclerViewImpl recyclerViewImpl2 = this.mRecyclerView;
        Objects.requireNonNull(recyclerViewImpl2);
        recyclerViewImpl2.mTouchSlop = ViewConfiguration.get(recyclerViewImpl2.getContext()).getScaledPagingTouchSlop();
        int[] iArr = R$styleable.ViewPager2;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        try {
            this.mLayoutManager.setOrientation(obtainStyledAttributes.getInt(0, 0));
            PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
            Objects.requireNonNull(pageAwareAccessibilityProvider);
            pageAwareAccessibilityProvider.updatePageAccessibilityActions();
            obtainStyledAttributes.recycle();
            this.mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            RecyclerViewImpl recyclerViewImpl3 = this.mRecyclerView;
            RecyclerView.OnChildAttachStateChangeListener onChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: androidx.viewpager2.widget.ViewPager2.4
                @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
                public final void onChildViewDetachedFromWindow(View view) {
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
                public final void onChildViewAttachedToWindow(View view) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    if (((ViewGroup.MarginLayoutParams) layoutParams).width != -1 || ((ViewGroup.MarginLayoutParams) layoutParams).height != -1) {
                        throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
                    }
                }
            };
            Objects.requireNonNull(recyclerViewImpl3);
            if (recyclerViewImpl3.mOnChildAttachStateListeners == null) {
                recyclerViewImpl3.mOnChildAttachStateListeners = new ArrayList();
            }
            recyclerViewImpl3.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
            ScrollEventAdapter scrollEventAdapter = new ScrollEventAdapter(this);
            this.mScrollEventAdapter = scrollEventAdapter;
            this.mFakeDragger = new FakeDrag(scrollEventAdapter);
            PagerSnapHelperImpl pagerSnapHelperImpl = new PagerSnapHelperImpl();
            this.mPagerSnapHelper = pagerSnapHelperImpl;
            pagerSnapHelperImpl.attachToRecyclerView(this.mRecyclerView);
            this.mRecyclerView.addOnScrollListener(this.mScrollEventAdapter);
            CompositeOnPageChangeCallback compositeOnPageChangeCallback = new CompositeOnPageChangeCallback();
            this.mPageChangeEventDispatcher = compositeOnPageChangeCallback;
            ScrollEventAdapter scrollEventAdapter2 = this.mScrollEventAdapter;
            Objects.requireNonNull(scrollEventAdapter2);
            scrollEventAdapter2.mCallback = compositeOnPageChangeCallback;
            OnPageChangeCallback onPageChangeCallback = new OnPageChangeCallback() { // from class: androidx.viewpager2.widget.ViewPager2.2
                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public final void onPageScrollStateChanged(int i) {
                    if (i == 0) {
                        ViewPager2.this.updateCurrentItem();
                    }
                }

                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public final void onPageSelected(int i) {
                    ViewPager2 viewPager2 = ViewPager2.this;
                    if (viewPager2.mCurrentItem != i) {
                        viewPager2.mCurrentItem = i;
                        PageAwareAccessibilityProvider pageAwareAccessibilityProvider2 = viewPager2.mAccessibilityProvider;
                        Objects.requireNonNull(pageAwareAccessibilityProvider2);
                        pageAwareAccessibilityProvider2.updatePageAccessibilityActions();
                    }
                }
            };
            OnPageChangeCallback onPageChangeCallback2 = new OnPageChangeCallback() { // from class: androidx.viewpager2.widget.ViewPager2.3
                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public final void onPageSelected(int i) {
                    ViewPager2.this.clearFocus();
                    if (ViewPager2.this.hasFocus()) {
                        ViewPager2.this.mRecyclerView.requestFocus(2);
                    }
                }
            };
            CompositeOnPageChangeCallback compositeOnPageChangeCallback2 = this.mPageChangeEventDispatcher;
            Objects.requireNonNull(compositeOnPageChangeCallback2);
            compositeOnPageChangeCallback2.mCallbacks.add(onPageChangeCallback);
            CompositeOnPageChangeCallback compositeOnPageChangeCallback3 = this.mPageChangeEventDispatcher;
            Objects.requireNonNull(compositeOnPageChangeCallback3);
            compositeOnPageChangeCallback3.mCallbacks.add(onPageChangeCallback2);
            this.mAccessibilityProvider.onInitialize(this.mRecyclerView);
            CompositeOnPageChangeCallback compositeOnPageChangeCallback4 = this.mPageChangeEventDispatcher;
            CompositeOnPageChangeCallback compositeOnPageChangeCallback5 = this.mExternalPageChangeCallbacks;
            Objects.requireNonNull(compositeOnPageChangeCallback4);
            compositeOnPageChangeCallback4.mCallbacks.add(compositeOnPageChangeCallback5);
            PageTransformerAdapter pageTransformerAdapter = new PageTransformerAdapter(this.mLayoutManager);
            CompositeOnPageChangeCallback compositeOnPageChangeCallback6 = this.mPageChangeEventDispatcher;
            Objects.requireNonNull(compositeOnPageChangeCallback6);
            compositeOnPageChangeCallback6.mCallbacks.add(pageTransformerAdapter);
            RecyclerViewImpl recyclerViewImpl4 = this.mRecyclerView;
            attachViewToParent(recyclerViewImpl4, 0, recyclerViewImpl4.getLayoutParams());
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = this.mRecyclerView.getMeasuredWidth();
        int measuredHeight = this.mRecyclerView.getMeasuredHeight();
        this.mTmpContainerRect.left = getPaddingLeft();
        this.mTmpContainerRect.right = (i3 - i) - getPaddingRight();
        this.mTmpContainerRect.top = getPaddingTop();
        this.mTmpContainerRect.bottom = (i4 - i2) - getPaddingBottom();
        Gravity.apply(8388659, measuredWidth, measuredHeight, this.mTmpContainerRect, this.mTmpChildRect);
        RecyclerViewImpl recyclerViewImpl = this.mRecyclerView;
        Rect rect = this.mTmpChildRect;
        recyclerViewImpl.layout(rect.left, rect.top, rect.right, rect.bottom);
        if (this.mCurrentItemDirty) {
            updateCurrentItem();
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        measureChild(this.mRecyclerView, i, i2);
        int measuredWidth = this.mRecyclerView.getMeasuredWidth();
        int measuredHeight = this.mRecyclerView.getMeasuredHeight();
        int measuredState = this.mRecyclerView.getMeasuredState();
        int paddingRight = getPaddingRight() + getPaddingLeft() + measuredWidth;
        int paddingTop = getPaddingTop();
        setMeasuredDimension(View.resolveSizeAndState(Math.max(paddingRight, getSuggestedMinimumWidth()), i, measuredState), View.resolveSizeAndState(Math.max(getPaddingBottom() + paddingTop + measuredHeight, getSuggestedMinimumHeight()), i2, measuredState << 16));
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mPendingCurrentItem = savedState.mCurrentItem;
        this.mPendingAdapterState = savedState.mAdapterState;
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(View view) {
        throw new IllegalStateException("ViewPager2 does not support direct child views");
    }

    @Override // android.view.View
    public final boolean performAccessibilityAction(int i, Bundle bundle) {
        boolean z;
        int i2;
        Objects.requireNonNull(this.mAccessibilityProvider);
        boolean z2 = false;
        if (i == 8192 || i == 4096) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return super.performAccessibilityAction(i, bundle);
        }
        PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
        Objects.requireNonNull(pageAwareAccessibilityProvider);
        if (i == 8192 || i == 4096) {
            z2 = true;
        }
        if (z2) {
            if (i == 8192) {
                ViewPager2 viewPager2 = ViewPager2.this;
                Objects.requireNonNull(viewPager2);
                i2 = viewPager2.mCurrentItem - 1;
            } else {
                ViewPager2 viewPager22 = ViewPager2.this;
                Objects.requireNonNull(viewPager22);
                i2 = viewPager22.mCurrentItem + 1;
            }
            ViewPager2 viewPager23 = ViewPager2.this;
            Objects.requireNonNull(viewPager23);
            if (viewPager23.mUserInputEnabled) {
                ViewPager2.this.setCurrentItemInternal(i2);
            }
            return true;
        }
        throw new IllegalStateException();
    }

    public final void restorePendingState() {
        RecyclerView.Adapter adapter;
        if (this.mPendingCurrentItem != -1 && (adapter = getAdapter()) != null) {
            if (this.mPendingAdapterState != null) {
                if (adapter instanceof StatefulAdapter) {
                    ((StatefulAdapter) adapter).restoreState();
                }
                this.mPendingAdapterState = null;
            }
            int max = Math.max(0, Math.min(this.mPendingCurrentItem, adapter.getItemCount() - 1));
            this.mCurrentItem = max;
            this.mPendingCurrentItem = -1;
            this.mRecyclerView.scrollToPosition(max);
            PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
            Objects.requireNonNull(pageAwareAccessibilityProvider);
            pageAwareAccessibilityProvider.updatePageAccessibilityActions();
        }
    }

    public final void setAdapter(StructureAdapter structureAdapter) {
        RecyclerViewImpl recyclerViewImpl = this.mRecyclerView;
        Objects.requireNonNull(recyclerViewImpl);
        RecyclerView.Adapter adapter = recyclerViewImpl.mAdapter;
        PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
        if (adapter != null) {
            adapter.mObservable.unregisterObserver(pageAwareAccessibilityProvider.mAdapterDataObserver);
        } else {
            Objects.requireNonNull(pageAwareAccessibilityProvider);
        }
        if (adapter != null) {
            adapter.mObservable.unregisterObserver(this.mCurrentItemDataSetChangeObserver);
        }
        this.mRecyclerView.setAdapter(structureAdapter);
        this.mCurrentItem = 0;
        restorePendingState();
        PageAwareAccessibilityProvider pageAwareAccessibilityProvider2 = this.mAccessibilityProvider;
        Objects.requireNonNull(pageAwareAccessibilityProvider2);
        pageAwareAccessibilityProvider2.updatePageAccessibilityActions();
        structureAdapter.registerAdapterDataObserver(pageAwareAccessibilityProvider2.mAdapterDataObserver);
        structureAdapter.registerAdapterDataObserver(this.mCurrentItemDataSetChangeObserver);
    }

    public final void updateCurrentItem() {
        PagerSnapHelperImpl pagerSnapHelperImpl = this.mPagerSnapHelper;
        if (pagerSnapHelperImpl != null) {
            View findSnapView = pagerSnapHelperImpl.findSnapView(this.mLayoutManager);
            if (findSnapView != null) {
                Objects.requireNonNull(this.mLayoutManager);
                int position = RecyclerView.LayoutManager.getPosition(findSnapView);
                if (position != this.mCurrentItem) {
                    ScrollEventAdapter scrollEventAdapter = this.mScrollEventAdapter;
                    Objects.requireNonNull(scrollEventAdapter);
                    if (scrollEventAdapter.mScrollState == 0) {
                        this.mPageChangeEventDispatcher.onPageSelected(position);
                    }
                }
                this.mCurrentItemDirty = false;
                return;
            }
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = sparseArray.get(getId());
        if (parcelable instanceof SavedState) {
            int i = ((SavedState) parcelable).mRecyclerViewId;
            sparseArray.put(this.mRecyclerView.getId(), sparseArray.get(i));
            sparseArray.remove(i);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        restorePendingState();
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        WindowInsets onApplyWindowInsets = super.onApplyWindowInsets(windowInsets);
        if (onApplyWindowInsets.isConsumed()) {
            return onApplyWindowInsets;
        }
        int childCount = this.mRecyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mRecyclerView.getChildAt(i).dispatchApplyWindowInsets(new WindowInsets(onApplyWindowInsets));
        }
        WindowInsetsCompat windowInsetsCompat = EMPTY_INSETS;
        if (windowInsetsCompat.toWindowInsets() != null) {
            return windowInsetsCompat.toWindowInsets();
        }
        return windowInsets.consumeSystemWindowInsets().consumeStableInsets();
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        int i2;
        int itemCount;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
        Objects.requireNonNull(pageAwareAccessibilityProvider);
        if (ViewPager2.this.getAdapter() != null) {
            ViewPager2 viewPager2 = ViewPager2.this;
            Objects.requireNonNull(viewPager2);
            LinearLayoutManagerImpl linearLayoutManagerImpl = viewPager2.mLayoutManager;
            Objects.requireNonNull(linearLayoutManagerImpl);
            if (linearLayoutManagerImpl.mOrientation == 1) {
                i2 = ViewPager2.this.getAdapter().getItemCount();
                i = 1;
            } else {
                i = ViewPager2.this.getAdapter().getItemCount();
                i2 = 1;
            }
        } else {
            i2 = 0;
            i = 0;
        }
        accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo) AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(i2, i, 0).mInfo);
        RecyclerView.Adapter adapter = ViewPager2.this.getAdapter();
        if (adapter != null && (itemCount = adapter.getItemCount()) != 0) {
            ViewPager2 viewPager22 = ViewPager2.this;
            Objects.requireNonNull(viewPager22);
            if (viewPager22.mUserInputEnabled) {
                if (ViewPager2.this.mCurrentItem > 0) {
                    accessibilityNodeInfo.addAction(8192);
                }
                if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                    accessibilityNodeInfo.addAction(4096);
                }
                accessibilityNodeInfo.setScrollable(true);
            }
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mRecyclerViewId = this.mRecyclerView.getId();
        int i = this.mPendingCurrentItem;
        if (i == -1) {
            i = this.mCurrentItem;
        }
        savedState.mCurrentItem = i;
        Parcelable parcelable = this.mPendingAdapterState;
        if (parcelable != null) {
            savedState.mAdapterState = parcelable;
        } else {
            RecyclerViewImpl recyclerViewImpl = this.mRecyclerView;
            Objects.requireNonNull(recyclerViewImpl);
            RecyclerView.Adapter adapter = recyclerViewImpl.mAdapter;
            if (adapter instanceof StatefulAdapter) {
                savedState.mAdapterState = ((StatefulAdapter) adapter).saveState();
            }
        }
        return savedState;
    }

    public final void setCurrentItemInternal(int i) {
        boolean z;
        int i2;
        OnPageChangeCallback onPageChangeCallback;
        boolean z2;
        RecyclerView.Adapter adapter = getAdapter();
        boolean z3 = false;
        if (adapter == null) {
            if (this.mPendingCurrentItem != -1) {
                this.mPendingCurrentItem = Math.max(i, 0);
            }
        } else if (adapter.getItemCount() > 0) {
            int min = Math.min(Math.max(i, 0), adapter.getItemCount() - 1);
            if (min == this.mCurrentItem) {
                ScrollEventAdapter scrollEventAdapter = this.mScrollEventAdapter;
                Objects.requireNonNull(scrollEventAdapter);
                if (scrollEventAdapter.mScrollState == 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    return;
                }
            }
            int i3 = this.mCurrentItem;
            if (min != i3) {
                double d = i3;
                this.mCurrentItem = min;
                PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
                Objects.requireNonNull(pageAwareAccessibilityProvider);
                pageAwareAccessibilityProvider.updatePageAccessibilityActions();
                ScrollEventAdapter scrollEventAdapter2 = this.mScrollEventAdapter;
                Objects.requireNonNull(scrollEventAdapter2);
                if (scrollEventAdapter2.mScrollState == 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    ScrollEventAdapter scrollEventAdapter3 = this.mScrollEventAdapter;
                    Objects.requireNonNull(scrollEventAdapter3);
                    scrollEventAdapter3.updateScrollEventValues();
                    ScrollEventAdapter.ScrollEventValues scrollEventValues = scrollEventAdapter3.mScrollValues;
                    d = scrollEventValues.mPosition + scrollEventValues.mOffset;
                }
                ScrollEventAdapter scrollEventAdapter4 = this.mScrollEventAdapter;
                Objects.requireNonNull(scrollEventAdapter4);
                scrollEventAdapter4.mAdapterState = 2;
                scrollEventAdapter4.mFakeDragging = false;
                if (scrollEventAdapter4.mTarget != min) {
                    z3 = true;
                }
                scrollEventAdapter4.mTarget = min;
                scrollEventAdapter4.dispatchStateChanged(2);
                if (z3 && (onPageChangeCallback = scrollEventAdapter4.mCallback) != null) {
                    onPageChangeCallback.onPageSelected(min);
                }
                double d2 = min;
                if (Math.abs(d2 - d) > 3.0d) {
                    RecyclerViewImpl recyclerViewImpl = this.mRecyclerView;
                    if (d2 > d) {
                        i2 = min - 3;
                    } else {
                        i2 = min + 3;
                    }
                    recyclerViewImpl.scrollToPosition(i2);
                    RecyclerViewImpl recyclerViewImpl2 = this.mRecyclerView;
                    recyclerViewImpl2.post(new SmoothScrollToPosition(min, recyclerViewImpl2));
                    return;
                }
                this.mRecyclerView.smoothScrollToPosition(min);
            }
        }
    }

    @Override // android.view.View
    public final void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        PageAwareAccessibilityProvider pageAwareAccessibilityProvider = this.mAccessibilityProvider;
        Objects.requireNonNull(pageAwareAccessibilityProvider);
        pageAwareAccessibilityProvider.updatePageAccessibilityActions();
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.viewpager2.widget.ViewPager2$1] */
    public ViewPager2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context, attributeSet);
    }

    /* JADX WARN: Type inference failed for: r4v5, types: [androidx.viewpager2.widget.ViewPager2$1] */
    public ViewPager2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context, attributeSet);
    }

    /* JADX WARN: Type inference failed for: r3v5, types: [androidx.viewpager2.widget.ViewPager2$1] */
    public ViewPager2(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context, attributeSet);
    }
}
