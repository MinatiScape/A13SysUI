package androidx.slice.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class SliceAdapter extends RecyclerView.Adapter<SliceViewHolder> implements SliceActionView.SliceActionLoadingListener {
    public boolean mAllowTwoLines;
    public final Context mContext;
    public int mInsetBottom;
    public int mInsetEnd;
    public int mInsetStart;
    public int mInsetTop;
    public long mLastUpdated;
    public SliceView mParent;
    public SliceViewPolicy mPolicy;
    public boolean mShowLastUpdated;
    public List<SliceAction> mSliceActions;
    public SliceView.OnSliceActionListener mSliceObserver;
    public SliceStyle mSliceStyle;
    public TemplateView mTemplateView;
    public final IdGenerator mIdGen = new IdGenerator();
    public ArrayList mSlices = new ArrayList();
    public Set<SliceItem> mLoadingActions = new HashSet();

    /* loaded from: classes.dex */
    public static class IdGenerator {
        public long mNextLong = 0;
        public final ArrayMap<String, Long> mCurrentIds = new ArrayMap<>();
        public final ArrayMap<String, Integer> mUsedIds = new ArrayMap<>();
    }

    /* loaded from: classes.dex */
    public class SliceViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {
        public final SliceChildView mSliceChildView;

        public SliceViewHolder(View view) {
            super(view);
            SliceChildView sliceChildView;
            if (view instanceof SliceChildView) {
                sliceChildView = (SliceChildView) view;
            } else {
                sliceChildView = null;
            }
            this.mSliceChildView = sliceChildView;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (SliceAdapter.this.mParent != null) {
                int[] iArr = (int[]) view.getTag();
                SliceAdapter.this.mParent.performClick();
            }
        }

        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            boolean z;
            ListContent listContent;
            TemplateView templateView = SliceAdapter.this.mTemplateView;
            if (templateView != null) {
                Objects.requireNonNull(templateView);
                SliceView sliceView = templateView.mParent;
                if (sliceView != null) {
                    if (sliceView.mOnClickListener == null && ((listContent = sliceView.mListContent) == null || listContent.getShortcut(sliceView.getContext()) == null)) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (!z) {
                        templateView.mForeground.setPressed(false);
                    }
                }
                templateView.mForeground.getLocationOnScreen(templateView.mLoc);
                templateView.mForeground.getBackground().setHotspot((int) (motionEvent.getRawX() - templateView.mLoc[0]), (int) (motionEvent.getRawY() - templateView.mLoc[1]));
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    templateView.mForeground.setPressed(true);
                } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
                    templateView.mForeground.setPressed(false);
                }
            }
            return false;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        View view;
        if (i == 3) {
            View inflate = LayoutInflater.from(this.mContext).inflate(2131623963, (ViewGroup) null);
            if (inflate instanceof GridRowView) {
                view = (GridRowView) inflate;
            } else {
                view = new GridRowView(this.mContext, null);
            }
        } else if (i == 4) {
            view = LayoutInflater.from(this.mContext).inflate(2131623967, (ViewGroup) null);
        } else if (i != 5) {
            view = new RowView(this.mContext);
        } else {
            view = LayoutInflater.from(this.mContext).inflate(2131623968, (ViewGroup) null);
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SliceViewHolder(view);
    }

    /* loaded from: classes.dex */
    public static class SliceWrapper {
        public final long mId;
        public final SliceContent mItem;
        public final int mType;

        public SliceWrapper(SliceContent sliceContent, IdGenerator idGenerator) {
            int i;
            String str;
            int i2;
            this.mItem = sliceContent;
            Objects.requireNonNull(sliceContent);
            SliceItem sliceItem = sliceContent.mSliceItem;
            Objects.requireNonNull(sliceItem);
            if ("message".equals(sliceItem.mSubType)) {
                if (SliceQuery.findSubtype(sliceItem, (String) null, "source") != null) {
                    i = 4;
                } else {
                    i = 5;
                }
            } else if (sliceItem.hasHint("horizontal")) {
                i = 3;
            } else if (!sliceItem.hasHint("list_item")) {
                i = 2;
            } else {
                i = 1;
            }
            this.mType = i;
            SliceItem sliceItem2 = sliceContent.mSliceItem;
            Objects.requireNonNull(idGenerator);
            Objects.requireNonNull(sliceItem2);
            if ("slice".equals(sliceItem2.mFormat) || "action".equals(sliceItem2.mFormat)) {
                str = String.valueOf(sliceItem2.getSlice().getItems().size());
            } else {
                str = sliceItem2.toString();
            }
            if (!idGenerator.mCurrentIds.containsKey(str)) {
                ArrayMap<String, Long> arrayMap = idGenerator.mCurrentIds;
                long j = idGenerator.mNextLong;
                idGenerator.mNextLong = 1 + j;
                arrayMap.put(str, Long.valueOf(j));
            }
            ArrayMap<String, Long> arrayMap2 = idGenerator.mCurrentIds;
            Objects.requireNonNull(arrayMap2);
            long longValue = arrayMap2.getOrDefault(str, null).longValue();
            ArrayMap<String, Integer> arrayMap3 = idGenerator.mUsedIds;
            Objects.requireNonNull(arrayMap3);
            Integer orDefault = arrayMap3.getOrDefault(str, null);
            if (orDefault != null) {
                i2 = orDefault.intValue();
            } else {
                i2 = 0;
            }
            idGenerator.mUsedIds.put(str, Integer.valueOf(i2 + 1));
            this.mId = longValue + (i2 * 10000);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.mSlices.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final long getItemId(int i) {
        return ((SliceWrapper) this.mSlices.get(i)).mId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemViewType(int i) {
        return ((SliceWrapper) this.mSlices.get(i)).mType;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(SliceViewHolder sliceViewHolder, int i) {
        boolean z;
        boolean z2;
        long j;
        int i2;
        int i3;
        List<SliceAction> list;
        SliceViewHolder sliceViewHolder2 = sliceViewHolder;
        SliceContent sliceContent = ((SliceWrapper) this.mSlices.get(i)).mItem;
        if (sliceViewHolder2.mSliceChildView != null && sliceContent != null) {
            RowStyle rowStyle = SliceAdapter.this.mSliceStyle.getRowStyle(sliceContent.mSliceItem);
            sliceViewHolder2.mSliceChildView.setOnClickListener(sliceViewHolder2);
            sliceViewHolder2.mSliceChildView.setOnTouchListener(sliceViewHolder2);
            SliceChildView sliceChildView = sliceViewHolder2.mSliceChildView;
            SliceAdapter sliceAdapter = SliceAdapter.this;
            Objects.requireNonNull(sliceChildView);
            sliceChildView.mLoadingListener = sliceAdapter;
            if (sliceContent instanceof RowContent) {
                z = ((RowContent) sliceContent).mIsHeader;
            } else if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            sliceViewHolder2.mSliceChildView.setLoadingActions(SliceAdapter.this.mLoadingActions);
            sliceViewHolder2.mSliceChildView.setPolicy(SliceAdapter.this.mPolicy);
            sliceViewHolder2.mSliceChildView.setTint(rowStyle.getTintColor());
            sliceViewHolder2.mSliceChildView.setStyle(SliceAdapter.this.mSliceStyle, rowStyle);
            SliceChildView sliceChildView2 = sliceViewHolder2.mSliceChildView;
            if (!z || !SliceAdapter.this.mShowLastUpdated) {
                z2 = false;
            } else {
                z2 = true;
            }
            sliceChildView2.setShowLastUpdated(z2);
            SliceChildView sliceChildView3 = sliceViewHolder2.mSliceChildView;
            if (z) {
                j = SliceAdapter.this.mLastUpdated;
            } else {
                j = -1;
            }
            sliceChildView3.setLastUpdated(j);
            if (i == 0) {
                i2 = SliceAdapter.this.mInsetTop;
            } else {
                i2 = 0;
            }
            if (i == SliceAdapter.this.getItemCount() - 1) {
                i3 = SliceAdapter.this.mInsetBottom;
            } else {
                i3 = 0;
            }
            SliceChildView sliceChildView4 = sliceViewHolder2.mSliceChildView;
            SliceAdapter sliceAdapter2 = SliceAdapter.this;
            sliceChildView4.setInsets(sliceAdapter2.mInsetStart, i2, sliceAdapter2.mInsetEnd, i3);
            sliceViewHolder2.mSliceChildView.setAllowTwoLines(SliceAdapter.this.mAllowTwoLines);
            SliceChildView sliceChildView5 = sliceViewHolder2.mSliceChildView;
            if (z) {
                list = SliceAdapter.this.mSliceActions;
            } else {
                list = null;
            }
            sliceChildView5.setSliceActions(list);
            sliceViewHolder2.mSliceChildView.setSliceItem(sliceContent, z, i, SliceAdapter.this.getItemCount(), SliceAdapter.this.mSliceObserver);
            sliceViewHolder2.mSliceChildView.setTag(new int[]{ListContent.getRowType(sliceContent, z, SliceAdapter.this.mSliceActions), i});
        }
    }

    public final void onSliceActionLoading(SliceItem sliceItem, int i) {
        this.mLoadingActions.add(sliceItem);
        if (getItemCount() > i) {
            notifyItemChanged(i);
        } else {
            notifyDataSetChanged();
        }
    }

    public final void setSliceItems(List list) {
        if (list == null) {
            this.mLoadingActions.clear();
            this.mSlices.clear();
        } else {
            IdGenerator idGenerator = this.mIdGen;
            Objects.requireNonNull(idGenerator);
            idGenerator.mUsedIds.clear();
            this.mSlices = new ArrayList(list.size());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                this.mSlices.add(new SliceWrapper((SliceContent) it.next(), this.mIdGen));
            }
        }
        notifyDataSetChanged();
    }

    public SliceAdapter(Context context) {
        this.mContext = context;
        setHasStableIds(true);
    }

    public final void notifyHeaderChanged() {
        if (getItemCount() > 0) {
            notifyItemChanged(0);
        }
    }
}
