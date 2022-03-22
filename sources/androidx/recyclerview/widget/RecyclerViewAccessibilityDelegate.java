package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat {
    public final ItemDelegate mItemDelegate;
    public final RecyclerView mRecyclerView;

    /* loaded from: classes.dex */
    public static class ItemDelegate extends AccessibilityDelegateCompat {
        public WeakHashMap mOriginalItemDelegates = new WeakHashMap();
        public final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.getAccessibilityNodeProvider(view);
            }
            return super.getAccessibilityNodeProvider(view);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
            } else {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean z;
            RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate = this.mRecyclerViewDelegate;
            Objects.requireNonNull(recyclerViewAccessibilityDelegate);
            RecyclerView recyclerView = recyclerViewAccessibilityDelegate.mRecyclerView;
            Objects.requireNonNull(recyclerView);
            if (!recyclerView.mFirstLayoutComplete || recyclerView.mDataSetHasChangedAfterLayout || recyclerView.mAdapterHelper.hasPendingUpdates()) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                RecyclerView recyclerView2 = this.mRecyclerViewDelegate.mRecyclerView;
                Objects.requireNonNull(recyclerView2);
                if (recyclerView2.mLayout != null) {
                    RecyclerView recyclerView3 = this.mRecyclerViewDelegate.mRecyclerView;
                    Objects.requireNonNull(recyclerView3);
                    recyclerView3.mLayout.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
                    AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
                    if (accessibilityDelegateCompat != null) {
                        accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                        return;
                    } else {
                        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                        return;
                    }
                }
            }
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
            } else {
                super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(viewGroup);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            boolean z;
            RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate = this.mRecyclerViewDelegate;
            Objects.requireNonNull(recyclerViewAccessibilityDelegate);
            RecyclerView recyclerView = recyclerViewAccessibilityDelegate.mRecyclerView;
            Objects.requireNonNull(recyclerView);
            if (!recyclerView.mFirstLayoutComplete || recyclerView.mDataSetHasChangedAfterLayout || recyclerView.mAdapterHelper.hasPendingUpdates()) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                RecyclerView recyclerView2 = this.mRecyclerViewDelegate.mRecyclerView;
                Objects.requireNonNull(recyclerView2);
                if (recyclerView2.mLayout != null) {
                    AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
                    if (accessibilityDelegateCompat != null) {
                        if (accessibilityDelegateCompat.performAccessibilityAction(view, i, bundle)) {
                            return true;
                        }
                    } else if (super.performAccessibilityAction(view, i, bundle)) {
                        return true;
                    }
                    RecyclerView recyclerView3 = this.mRecyclerViewDelegate.mRecyclerView;
                    Objects.requireNonNull(recyclerView3);
                    RecyclerView.LayoutManager layoutManager = recyclerView3.mLayout;
                    Objects.requireNonNull(layoutManager);
                    RecyclerView.Recycler recycler = layoutManager.mRecyclerView.mRecycler;
                    return false;
                }
            }
            return super.performAccessibilityAction(view, i, bundle);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void sendAccessibilityEvent(View view, int i) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEvent(view, i);
            } else {
                super.sendAccessibilityEvent(view, i);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = (AccessibilityDelegateCompat) this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
            } else {
                super.sendAccessibilityEventUnchecked(view, accessibilityEvent);
            }
        }

        public ItemDelegate(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate) {
            this.mRecyclerViewDelegate = recyclerViewAccessibilityDelegate;
        }
    }

    public RecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        AccessibilityDelegateCompat itemDelegate = getItemDelegate();
        if (itemDelegate == null || !(itemDelegate instanceof ItemDelegate)) {
            this.mItemDelegate = new ItemDelegate(this);
        } else {
            this.mItemDelegate = (ItemDelegate) itemDelegate;
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        boolean z;
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = this.mRecyclerView;
            Objects.requireNonNull(recyclerView);
            if (!recyclerView.mFirstLayoutComplete || recyclerView.mDataSetHasChangedAfterLayout || recyclerView.mAdapterHelper.hasPendingUpdates()) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                RecyclerView recyclerView2 = (RecyclerView) view;
                Objects.requireNonNull(recyclerView2);
                RecyclerView.LayoutManager layoutManager = recyclerView2.mLayout;
                if (layoutManager != null) {
                    layoutManager.onInitializeAccessibilityEvent(accessibilityEvent);
                }
            }
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean z;
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        RecyclerView recyclerView = this.mRecyclerView;
        Objects.requireNonNull(recyclerView);
        if (!recyclerView.mFirstLayoutComplete || recyclerView.mDataSetHasChangedAfterLayout || recyclerView.mAdapterHelper.hasPendingUpdates()) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            RecyclerView recyclerView2 = this.mRecyclerView;
            Objects.requireNonNull(recyclerView2);
            if (recyclerView2.mLayout != null) {
                RecyclerView recyclerView3 = this.mRecyclerView;
                Objects.requireNonNull(recyclerView3);
                RecyclerView.LayoutManager layoutManager = recyclerView3.mLayout;
                Objects.requireNonNull(layoutManager);
                RecyclerView recyclerView4 = layoutManager.mRecyclerView;
                layoutManager.onInitializeAccessibilityNodeInfo(recyclerView4.mRecycler, recyclerView4.mState, accessibilityNodeInfoCompat);
            }
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        boolean z = true;
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        RecyclerView recyclerView = this.mRecyclerView;
        Objects.requireNonNull(recyclerView);
        if (recyclerView.mFirstLayoutComplete && !recyclerView.mDataSetHasChangedAfterLayout && !recyclerView.mAdapterHelper.hasPendingUpdates()) {
            z = false;
        }
        if (!z) {
            RecyclerView recyclerView2 = this.mRecyclerView;
            Objects.requireNonNull(recyclerView2);
            if (recyclerView2.mLayout != null) {
                RecyclerView recyclerView3 = this.mRecyclerView;
                Objects.requireNonNull(recyclerView3);
                RecyclerView.LayoutManager layoutManager = recyclerView3.mLayout;
                Objects.requireNonNull(layoutManager);
                RecyclerView recyclerView4 = layoutManager.mRecyclerView;
                return layoutManager.performAccessibilityAction(recyclerView4.mRecycler, recyclerView4.mState, i, bundle);
            }
        }
        return false;
    }

    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }
}
