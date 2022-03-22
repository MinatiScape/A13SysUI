package com.android.systemui.qs.customize;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSEditEvent;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tileimpl.QSIconViewImpl;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class TileAdapter extends RecyclerView.Adapter<Holder> implements TileQueryHelper.TileStateListener {
    public int mAccessibilityFromIndex;
    public List<TileQueryHelper.TileInfo> mAllTiles;
    public final AnonymousClass5 mCallbacks;
    public final Context mContext;
    public Holder mCurrentDrag;
    public List<String> mCurrentSpecs;
    public TileItemDecoration mDecoration;
    public int mEditIndex;
    public int mFocusIndex;
    public final QSTileHost mHost;
    public final ItemTouchHelper mItemTouchHelper;
    public final int mMinNumTiles;
    public boolean mNeedsFocus;
    public int mNumColumns;
    public ArrayList mOtherTiles;
    public RecyclerView mRecyclerView;
    public final AnonymousClass4 mSizeLookup;
    public int mTileDividerIndex;
    public final UiEventLogger mUiEventLogger;
    public final Handler mHandler = new Handler();
    public final ArrayList mTiles = new ArrayList();
    public int mAccessibilityAction = 0;
    public final MarginTileDecoration mMarginDecoration = new MarginTileDecoration(0);
    public final TileAdapterDelegate mAccessibilityDelegate = new TileAdapterDelegate();

    /* loaded from: classes.dex */
    public class Holder extends RecyclerView.ViewHolder {
        public QSTileViewImpl mTileView;

        public Holder(View view) {
            super(view);
            if (view instanceof FrameLayout) {
                QSTileViewImpl qSTileViewImpl = (QSTileViewImpl) ((FrameLayout) view).getChildAt(0);
                this.mTileView = qSTileViewImpl;
                Objects.requireNonNull(qSTileViewImpl);
                qSTileViewImpl._icon.disableAnimation();
                this.mTileView.setTag(this);
                ViewCompat.setAccessibilityDelegate(this.mTileView, TileAdapter.this.mAccessibilityDelegate);
            }
        }

        public final void stopDrag() {
            this.itemView.animate().setDuration(100L).scaleX(1.0f).scaleY(1.0f);
        }
    }

    /* loaded from: classes.dex */
    public class TileItemDecoration extends RecyclerView.ItemDecoration {
        public final Drawable mDrawable;

        public TileItemDecoration(Context context) {
            this.mDrawable = context.getDrawable(2131232581);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public final void onDraw(Canvas canvas, RecyclerView recyclerView) {
            int childCount = recyclerView.getChildCount();
            int width = recyclerView.getWidth();
            int bottom = recyclerView.getBottom();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childAt);
                if (childViewHolder != TileAdapter.this.mCurrentDrag) {
                    Objects.requireNonNull(childViewHolder);
                    if (childViewHolder.getBindingAdapterPosition() != 0 && (childViewHolder.getBindingAdapterPosition() >= TileAdapter.this.mEditIndex || (childAt instanceof TextView))) {
                        int top = childAt.getTop();
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        this.mDrawable.setBounds(0, Math.round(childAt.getTranslationY()) + top, width, bottom);
                        this.mDrawable.draw(canvas);
                        return;
                    }
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onDetachedFromRecyclerView() {
        this.mRecyclerView = null;
    }

    public final void updateDividerLocations() {
        this.mEditIndex = -1;
        this.mTileDividerIndex = this.mTiles.size();
        for (int i = 1; i < this.mTiles.size(); i++) {
            if (this.mTiles.get(i) == null) {
                if (this.mEditIndex == -1) {
                    this.mEditIndex = i;
                } else {
                    this.mTileDividerIndex = i;
                }
            }
        }
        int size = this.mTiles.size() - 1;
        int i2 = this.mTileDividerIndex;
        if (size == i2) {
            notifyItemChanged(i2);
        }
    }

    /* loaded from: classes.dex */
    public static class MarginTileDecoration extends RecyclerView.ItemDecoration {
        public int mHalfMargin;

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public final void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            Objects.requireNonNull(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.mLayout;
            if (layoutManager != null) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                Objects.requireNonNull(layoutParams);
                int i = layoutParams.mSpanIndex;
                if (view instanceof TextView) {
                    super.getItemOffsets(rect, view, recyclerView, state);
                } else if (i != 0 && i != gridLayoutManager.mSpanCount - 1) {
                    int i2 = this.mHalfMargin;
                    rect.left = i2;
                    rect.right = i2;
                } else if (recyclerView.isLayoutRtl()) {
                    if (i == 0) {
                        rect.left = this.mHalfMargin;
                        rect.right = 0;
                        return;
                    }
                    rect.left = 0;
                    rect.right = this.mHalfMargin;
                } else if (i == 0) {
                    rect.left = 0;
                    rect.right = this.mHalfMargin;
                } else {
                    rect.left = this.mHalfMargin;
                    rect.right = 0;
                }
            }
        }

        public MarginTileDecoration(int i) {
        }
    }

    public static String strip(TileQueryHelper.TileInfo tileInfo) {
        String str = tileInfo.spec;
        if (str.startsWith("custom(")) {
            return CustomTile.getComponentFromSpec(str).getPackageName();
        }
        return str;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.mTiles.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemViewType(int i) {
        if (i == 0) {
            return 3;
        }
        if (this.mAccessibilityAction == 1 && i == this.mEditIndex - 1) {
            return 2;
        }
        if (i == this.mTileDividerIndex) {
            return 4;
        }
        if (this.mTiles.get(i) == null) {
            return 1;
        }
        return 0;
    }

    public final void move(int i, int i2, boolean z) {
        if (i2 != i) {
            ArrayList arrayList = this.mTiles;
            arrayList.add(i2, arrayList.remove(i));
            if (z) {
                notifyItemMoved(i, i2);
            }
            updateDividerLocations();
            int i3 = this.mEditIndex;
            if (i2 >= i3) {
                this.mUiEventLogger.log(QSEditEvent.QS_EDIT_REMOVE, 0, strip((TileQueryHelper.TileInfo) this.mTiles.get(i2)));
            } else if (i >= i3) {
                this.mUiEventLogger.log(QSEditEvent.QS_EDIT_ADD, 0, strip((TileQueryHelper.TileInfo) this.mTiles.get(i2)));
            } else {
                this.mUiEventLogger.log(QSEditEvent.QS_EDIT_MOVE, 0, strip((TileQueryHelper.TileInfo) this.mTiles.get(i2)));
            }
            saveSpecs(this.mHost);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(Holder holder, int i) {
        boolean z;
        boolean z2;
        int i2;
        boolean z3;
        String str;
        boolean z4;
        final Holder holder2 = holder;
        int i3 = holder2.mItemViewType;
        int i4 = 4;
        boolean z5 = false;
        if (i3 == 3) {
            View view = holder2.itemView;
            if (this.mAccessibilityAction == 0) {
                z5 = true;
            }
            view.setFocusable(z5);
            if (z5) {
                i4 = 1;
            }
            view.setImportantForAccessibility(i4);
            view.setFocusableInTouchMode(z5);
        } else if (i3 == 4) {
            View view2 = holder2.itemView;
            if (this.mTileDividerIndex < this.mTiles.size() - 1) {
                i4 = 0;
            }
            view2.setVisibility(i4);
        } else if (i3 == 1) {
            Resources resources = this.mContext.getResources();
            if (this.mCurrentDrag == null) {
                str = resources.getString(2131952300);
            } else {
                if (this.mCurrentSpecs.size() > this.mMinNumTiles) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (!z4) {
                    Holder holder3 = this.mCurrentDrag;
                    Objects.requireNonNull(holder3);
                    if (holder3.getBindingAdapterPosition() < this.mEditIndex) {
                        str = resources.getString(2131952302, Integer.valueOf(this.mMinNumTiles));
                    }
                }
                str = resources.getString(2131952303);
            }
            ((TextView) holder2.itemView.findViewById(16908310)).setText(str);
            View view3 = holder2.itemView;
            if (this.mAccessibilityAction == 0) {
                z5 = true;
            }
            view3.setFocusable(z5);
            if (z5) {
                i4 = 1;
            }
            view3.setImportantForAccessibility(i4);
            view3.setFocusableInTouchMode(z5);
        } else if (i3 == 2) {
            holder2.mTileView.setClickable(true);
            holder2.mTileView.setFocusable(true);
            holder2.mTileView.setFocusableInTouchMode(true);
            holder2.mTileView.setVisibility(0);
            holder2.mTileView.setImportantForAccessibility(1);
            holder2.mTileView.setContentDescription(this.mContext.getString(2131951777, Integer.valueOf(i)));
            holder2.mTileView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.customize.TileAdapter.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    TileAdapter.m75$$Nest$mselectPosition(TileAdapter.this, holder2.getLayoutPosition());
                }
            });
            if (this.mNeedsFocus) {
                holder2.mTileView.requestLayout();
                holder2.mTileView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.customize.TileAdapter.3
                    @Override // android.view.View.OnLayoutChangeListener
                    public final void onLayoutChange(View view4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12) {
                        Holder.this.mTileView.removeOnLayoutChangeListener(this);
                        Holder.this.mTileView.requestAccessibilityFocus();
                    }
                });
                this.mNeedsFocus = false;
                this.mFocusIndex = -1;
            }
        } else {
            TileQueryHelper.TileInfo tileInfo = (TileQueryHelper.TileInfo) this.mTiles.get(i);
            if (i <= 0 || i >= this.mEditIndex) {
                z = false;
            } else {
                z = true;
            }
            if (z && this.mAccessibilityAction == 1) {
                tileInfo.state.contentDescription = this.mContext.getString(2131951777, Integer.valueOf(i));
            } else if (!z || this.mAccessibilityAction != 2) {
                QSTile.State state = tileInfo.state;
                state.contentDescription = state.label;
            } else {
                tileInfo.state.contentDescription = this.mContext.getString(2131951779, Integer.valueOf(i));
            }
            tileInfo.state.expandedAccessibilityClassName = "";
            CustomizeTileView customizeTileView = (CustomizeTileView) holder2.mTileView;
            Objects.requireNonNull(customizeTileView, "The holder must have a tileView");
            customizeTileView.handleStateChanged(tileInfo.state);
            if (i <= this.mEditIndex || tileInfo.isSystem) {
                z2 = false;
            } else {
                z2 = true;
            }
            customizeTileView.showAppLabel = z2;
            TextView secondaryLabel = customizeTileView.mo78getSecondaryLabel();
            CharSequence text = customizeTileView.mo78getSecondaryLabel().getText();
            if (!customizeTileView.showAppLabel || TextUtils.isEmpty(text)) {
                i2 = 8;
            } else {
                i2 = 0;
            }
            secondaryLabel.setVisibility(i2);
            if (i < this.mEditIndex || tileInfo.isSystem) {
                z3 = true;
            } else {
                z3 = false;
            }
            customizeTileView.showSideView = z3;
            if (!z3) {
                customizeTileView.getSideView().setVisibility(8);
            }
            holder2.mTileView.setSelected(true);
            holder2.mTileView.setImportantForAccessibility(1);
            holder2.mTileView.setClickable(true);
            holder2.mTileView.setOnClickListener(null);
            holder2.mTileView.setFocusable(true);
            holder2.mTileView.setFocusableInTouchMode(true);
            if (this.mAccessibilityAction != 0) {
                holder2.mTileView.setClickable(z);
                holder2.mTileView.setFocusable(z);
                holder2.mTileView.setFocusableInTouchMode(z);
                QSTileViewImpl qSTileViewImpl = holder2.mTileView;
                if (z) {
                    i4 = 1;
                }
                qSTileViewImpl.setImportantForAccessibility(i4);
                if (z) {
                    holder2.mTileView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.customize.TileAdapter.2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view4) {
                            int layoutPosition = holder2.getLayoutPosition();
                            if (layoutPosition != -1) {
                                TileAdapter tileAdapter = TileAdapter.this;
                                if (tileAdapter.mAccessibilityAction != 0) {
                                    TileAdapter.m75$$Nest$mselectPosition(tileAdapter, layoutPosition);
                                }
                            }
                        }
                    });
                }
            }
            if (i == this.mFocusIndex && this.mNeedsFocus) {
                holder2.mTileView.requestLayout();
                holder2.mTileView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.customize.TileAdapter.3
                    @Override // android.view.View.OnLayoutChangeListener
                    public final void onLayoutChange(View view4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12) {
                        Holder.this.mTileView.removeOnLayoutChangeListener(this);
                        Holder.this.mTileView.requestAccessibilityFocus();
                    }
                });
                this.mNeedsFocus = false;
                this.mFocusIndex = -1;
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final boolean onFailedToRecycleView(Holder holder) {
        Holder holder2 = holder;
        holder2.stopDrag();
        holder2.itemView.clearAnimation();
        holder2.itemView.setScaleX(1.0f);
        holder2.itemView.setScaleY(1.0f);
        return true;
    }

    public final void recalcSpecs() {
        TileQueryHelper.TileInfo tileInfo;
        if (!(this.mCurrentSpecs == null || this.mAllTiles == null)) {
            this.mOtherTiles = new ArrayList(this.mAllTiles);
            this.mTiles.clear();
            this.mTiles.add(null);
            int i = 0;
            for (int i2 = 0; i2 < this.mCurrentSpecs.size(); i2++) {
                String str = this.mCurrentSpecs.get(i2);
                int i3 = 0;
                while (true) {
                    if (i3 >= this.mOtherTiles.size()) {
                        tileInfo = null;
                        break;
                    } else if (((TileQueryHelper.TileInfo) this.mOtherTiles.get(i3)).spec.equals(str)) {
                        tileInfo = (TileQueryHelper.TileInfo) this.mOtherTiles.remove(i3);
                        break;
                    } else {
                        i3++;
                    }
                }
                if (tileInfo != null) {
                    this.mTiles.add(tileInfo);
                }
            }
            this.mTiles.add(null);
            while (i < this.mOtherTiles.size()) {
                TileQueryHelper.TileInfo tileInfo2 = (TileQueryHelper.TileInfo) this.mOtherTiles.get(i);
                if (tileInfo2.isSystem) {
                    this.mOtherTiles.remove(i);
                    this.mTiles.add(tileInfo2);
                    i--;
                }
                i++;
            }
            this.mTileDividerIndex = this.mTiles.size();
            this.mTiles.add(null);
            this.mTiles.addAll(this.mOtherTiles);
            updateDividerLocations();
            notifyDataSetChanged();
        }
    }

    public final void saveSpecs(QSTileHost qSTileHost) {
        ArrayList arrayList = new ArrayList();
        this.mNeedsFocus = false;
        if (this.mAccessibilityAction == 1) {
            ArrayList arrayList2 = this.mTiles;
            int i = this.mEditIndex - 1;
            this.mEditIndex = i;
            arrayList2.remove(i);
            notifyDataSetChanged();
        }
        this.mAccessibilityAction = 0;
        for (int i2 = 1; i2 < this.mTiles.size() && this.mTiles.get(i2) != null; i2++) {
            arrayList.add(((TileQueryHelper.TileInfo) this.mTiles.get(i2)).spec);
        }
        qSTileHost.changeTiles(this.mCurrentSpecs, arrayList);
        this.mCurrentSpecs = arrayList;
    }

    /* renamed from: -$$Nest$mselectPosition  reason: not valid java name */
    public static void m75$$Nest$mselectPosition(TileAdapter tileAdapter, int i) {
        Objects.requireNonNull(tileAdapter);
        if (tileAdapter.mAccessibilityAction == 1) {
            ArrayList arrayList = tileAdapter.mTiles;
            int i2 = tileAdapter.mEditIndex;
            tileAdapter.mEditIndex = i2 - 1;
            arrayList.remove(i2);
        }
        tileAdapter.mAccessibilityAction = 0;
        tileAdapter.move(tileAdapter.mAccessibilityFromIndex, i, false);
        tileAdapter.mFocusIndex = i;
        tileAdapter.mNeedsFocus = true;
        tileAdapter.notifyDataSetChanged();
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.qs.customize.TileAdapter$4, androidx.recyclerview.widget.GridLayoutManager$SpanSizeLookup] */
    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.recyclerview.widget.ItemTouchHelper$Callback, com.android.systemui.qs.customize.TileAdapter$5] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TileAdapter(android.content.Context r4, com.android.systemui.qs.QSTileHost r5, com.android.internal.logging.UiEventLogger r6) {
        /*
            r3 = this;
            r3.<init>()
            android.os.Handler r0 = new android.os.Handler
            r0.<init>()
            r3.mHandler = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r3.mTiles = r0
            r0 = 0
            r3.mAccessibilityAction = r0
            com.android.systemui.qs.customize.TileAdapter$4 r1 = new com.android.systemui.qs.customize.TileAdapter$4
            r1.<init>()
            r3.mSizeLookup = r1
            com.android.systemui.qs.customize.TileAdapter$5 r2 = new com.android.systemui.qs.customize.TileAdapter$5
            r2.<init>()
            r3.mCallbacks = r2
            r3.mContext = r4
            r3.mHost = r5
            r3.mUiEventLogger = r6
            androidx.recyclerview.widget.ItemTouchHelper r5 = new androidx.recyclerview.widget.ItemTouchHelper
            r5.<init>(r2)
            r3.mItemTouchHelper = r5
            com.android.systemui.qs.customize.TileAdapter$TileItemDecoration r5 = new com.android.systemui.qs.customize.TileAdapter$TileItemDecoration
            r5.<init>(r4)
            r3.mDecoration = r5
            com.android.systemui.qs.customize.TileAdapter$MarginTileDecoration r5 = new com.android.systemui.qs.customize.TileAdapter$MarginTileDecoration
            r5.<init>(r0)
            r3.mMarginDecoration = r5
            android.content.res.Resources r5 = r4.getResources()
            r6 = 2131493031(0x7f0c00a7, float:1.860953E38)
            int r5 = r5.getInteger(r6)
            r3.mMinNumTiles = r5
            android.content.res.Resources r4 = r4.getResources()
            r5 = 2131493032(0x7f0c00a8, float:1.8609533E38)
            int r4 = r4.getInteger(r5)
            r3.mNumColumns = r4
            com.android.systemui.qs.customize.TileAdapterDelegate r4 = new com.android.systemui.qs.customize.TileAdapterDelegate
            r4.<init>()
            r3.mAccessibilityDelegate = r4
            r3 = 1
            r1.mCacheSpanIndices = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.customize.TileAdapter.<init>(android.content.Context, com.android.systemui.qs.QSTileHost, com.android.internal.logging.UiEventLogger):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        Context context = recyclerView.getContext();
        LayoutInflater from = LayoutInflater.from(context);
        if (i == 3) {
            return new Holder(from.inflate(2131624422, (ViewGroup) recyclerView, false));
        }
        if (i == 4) {
            return new Holder(from.inflate(2131624425, (ViewGroup) recyclerView, false));
        }
        if (i == 1) {
            return new Holder(from.inflate(2131624421, (ViewGroup) recyclerView, false));
        }
        FrameLayout frameLayout = (FrameLayout) from.inflate(2131624426, (ViewGroup) recyclerView, false);
        frameLayout.addView(new CustomizeTileView(context, new QSIconViewImpl(context)));
        return new Holder(frameLayout);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }
}
