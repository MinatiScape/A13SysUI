package androidx.slice.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GridContent extends SliceContent {
    public boolean mAllImages;
    public boolean mIsLastIndex;
    public int mMaxCellLineCount;
    public SliceItem mPrimaryAction;
    public SliceItem mSeeMoreItem;
    public SliceItem mTitleItem;
    public final ArrayList<CellContent> mGridContent = new ArrayList<>();
    public int mLargestImageMode = 5;
    public IconCompat mFirstImage = null;
    public Point mFirstImageSize = null;

    /* loaded from: classes.dex */
    public static class CellContent {
        public final ArrayList<SliceItem> mCellItems;
        public SliceItem mContentDescr;
        public SliceItem mContentIntent;
        public IconCompat mImage;
        public int mImageCount;
        public int mImageMode = -1;
        public SliceItem mOverlayItem;
        public SliceItem mPicker;
        public int mTextCount;
        public SliceItem mTitleItem;
        public SliceItem mToggleItem;

        public final void fillCellItems(List<SliceItem> list) {
            for (int i = 0; i < list.size(); i++) {
                SliceItem sliceItem = list.get(i);
                Objects.requireNonNull(sliceItem);
                String str = sliceItem.mFormat;
                if (this.mPicker == null && ("date_picker".equals(sliceItem.mSubType) || "time_picker".equals(sliceItem.mSubType))) {
                    this.mPicker = sliceItem;
                } else if ("content_description".equals(sliceItem.mSubType)) {
                    this.mContentDescr = sliceItem;
                } else if (this.mTextCount < 2 && ("text".equals(str) || "long".equals(str))) {
                    SliceItem sliceItem2 = this.mTitleItem;
                    if (sliceItem2 == null || (!sliceItem2.hasHint("title") && sliceItem.hasHint("title"))) {
                        this.mTitleItem = sliceItem;
                    }
                    if (!sliceItem.hasHint("overlay")) {
                        this.mTextCount++;
                        this.mCellItems.add(sliceItem);
                    } else if (this.mOverlayItem == null) {
                        this.mOverlayItem = sliceItem;
                    }
                } else if (this.mImageCount < 1 && "image".equals(sliceItem.mFormat)) {
                    this.mImageMode = SliceActionImpl.parseImageMode(sliceItem);
                    this.mImageCount++;
                    this.mImage = (IconCompat) sliceItem.mObj;
                    this.mCellItems.add(sliceItem);
                }
            }
        }

        public CellContent(SliceItem sliceItem) {
            boolean z;
            ArrayList<SliceItem> arrayList = new ArrayList<>();
            this.mCellItems = arrayList;
            String str = sliceItem.mFormat;
            boolean z2 = false;
            if (sliceItem.hasHint("shortcut") || (!"slice".equals(str) && !"action".equals(str))) {
                String str2 = sliceItem.mFormat;
                if ("content_description".equals(sliceItem.mSubType) || sliceItem.hasAnyHints("keywords", "ttl", "last_updated")) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z && ("text".equals(str2) || "long".equals(str2) || "image".equals(str2))) {
                    z2 = true;
                }
                if (z2) {
                    arrayList.add(sliceItem);
                }
            } else {
                List<SliceItem> items = sliceItem.getSlice().getItems();
                List<SliceItem> list = null;
                Iterator<SliceItem> it = items.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SliceItem next = it.next();
                    Objects.requireNonNull(next);
                    if ("action".equals(next.mFormat) || "slice".equals(next.mFormat)) {
                        if (!"date_picker".equals(next.mSubType) && !"time_picker".equals(next.mSubType)) {
                            list = next.getSlice().getItems();
                            if (new SliceActionImpl(next).isToggle()) {
                                this.mToggleItem = next;
                            } else {
                                this.mContentIntent = items.get(0);
                            }
                        }
                    }
                }
                if ("action".equals(str)) {
                    this.mContentIntent = sliceItem;
                }
                this.mTextCount = 0;
                this.mImageCount = 0;
                fillCellItems(items);
                if (this.mTextCount == 0 && this.mImageCount == 0 && list != null) {
                    fillCellItems(list);
                }
            }
            if (this.mPicker == null && this.mCellItems.size() > 0) {
                this.mCellItems.size();
            }
        }
    }

    public GridContent(SliceItem sliceItem, int i) {
        super(sliceItem, i);
        boolean z;
        boolean z2;
        List<SliceItem> items;
        SliceItem find = SliceQuery.find(sliceItem, (String) null, "see_more");
        this.mSeeMoreItem = find;
        if (find != null && "slice".equals(find.mFormat) && (items = this.mSeeMoreItem.getSlice().getItems()) != null && items.size() > 0) {
            this.mSeeMoreItem = items.get(0);
        }
        this.mPrimaryAction = SliceQuery.find(sliceItem, "slice", new String[]{"shortcut", "title"}, new String[]{"actions"});
        this.mAllImages = true;
        if ("slice".equals(sliceItem.mFormat)) {
            List<SliceItem> items2 = sliceItem.getSlice().getItems();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < items2.size(); i2++) {
                SliceItem sliceItem2 = items2.get(i2);
                if (SliceQuery.find(sliceItem2, (String) null, "see_more") != null) {
                    z = true;
                } else {
                    z = false;
                }
                if (z || sliceItem2.hasAnyHints("shortcut", "see_more", "keywords", "ttl", "last_updated", "overlay")) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                Objects.requireNonNull(sliceItem2);
                if ("content_description".equals(sliceItem2.mSubType)) {
                    this.mContentDescr = sliceItem2;
                } else if (!z2) {
                    arrayList.add(sliceItem2);
                }
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                SliceItem sliceItem3 = (SliceItem) arrayList.get(i3);
                Objects.requireNonNull(sliceItem3);
                if (!"content_description".equals(sliceItem3.mSubType)) {
                    processContent(new CellContent(sliceItem3));
                }
            }
        } else {
            processContent(new CellContent(sliceItem));
        }
        isValid();
    }

    public final Point getFirstImageSize(Context context) {
        IconCompat iconCompat = this.mFirstImage;
        if (iconCompat == null) {
            return new Point(-1, -1);
        }
        if (this.mFirstImageSize == null) {
            Drawable loadDrawable = iconCompat.loadDrawable(context);
            this.mFirstImageSize = new Point(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
        }
        return this.mFirstImageSize;
    }

    public final boolean isValid() {
        boolean z;
        if (this.mSliceItem != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z || this.mGridContent.size() <= 0) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void processContent(androidx.slice.widget.GridContent.CellContent r5) {
        /*
            r4 = this;
            androidx.slice.SliceItem r0 = r5.mPicker
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x001a
            java.util.ArrayList<androidx.slice.SliceItem> r0 = r5.mCellItems
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0018
            java.util.ArrayList<androidx.slice.SliceItem> r0 = r5.mCellItems
            int r0 = r0.size()
            r3 = 3
            if (r0 > r3) goto L_0x0018
            goto L_0x001a
        L_0x0018:
            r0 = r1
            goto L_0x001b
        L_0x001a:
            r0 = r2
        L_0x001b:
            if (r0 == 0) goto L_0x0077
            androidx.slice.SliceItem r0 = r4.mTitleItem
            if (r0 != 0) goto L_0x0027
            androidx.slice.SliceItem r0 = r5.mTitleItem
            if (r0 == 0) goto L_0x0027
            r4.mTitleItem = r0
        L_0x0027:
            java.util.ArrayList<androidx.slice.widget.GridContent$CellContent> r0 = r4.mGridContent
            r0.add(r5)
            java.util.ArrayList<androidx.slice.SliceItem> r0 = r5.mCellItems
            int r0 = r0.size()
            if (r0 != r2) goto L_0x004b
            java.util.ArrayList<androidx.slice.SliceItem> r0 = r5.mCellItems
            java.lang.Object r0 = r0.get(r1)
            androidx.slice.SliceItem r0 = (androidx.slice.SliceItem) r0
            java.util.Objects.requireNonNull(r0)
            java.lang.String r0 = r0.mFormat
            java.lang.String r3 = "image"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x004b
            r0 = r2
            goto L_0x004c
        L_0x004b:
            r0 = r1
        L_0x004c:
            if (r0 != 0) goto L_0x0050
            r4.mAllImages = r1
        L_0x0050:
            int r0 = r4.mMaxCellLineCount
            int r3 = r5.mTextCount
            int r0 = java.lang.Math.max(r0, r3)
            r4.mMaxCellLineCount = r0
            androidx.core.graphics.drawable.IconCompat r0 = r4.mFirstImage
            if (r0 != 0) goto L_0x0067
            androidx.core.graphics.drawable.IconCompat r0 = r5.mImage
            if (r0 == 0) goto L_0x0063
            r1 = r2
        L_0x0063:
            if (r1 == 0) goto L_0x0067
            r4.mFirstImage = r0
        L_0x0067:
            int r0 = r4.mLargestImageMode
            r1 = 5
            if (r0 != r1) goto L_0x006f
            int r5 = r5.mImageMode
            goto L_0x0075
        L_0x006f:
            int r5 = r5.mImageMode
            int r5 = java.lang.Math.max(r0, r5)
        L_0x0075:
            r4.mLargestImageMode = r5
        L_0x0077:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridContent.processContent(androidx.slice.widget.GridContent$CellContent):void");
    }

    @Override // androidx.slice.widget.SliceContent
    public final int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        int i;
        int i2;
        boolean z;
        boolean z2;
        boolean z3;
        Objects.requireNonNull(sliceStyle);
        Objects.requireNonNull(sliceViewPolicy);
        int i3 = 0;
        if (!isValid()) {
            return 0;
        }
        int i4 = this.mLargestImageMode;
        int i5 = 1;
        if (!this.mAllImages) {
            if (this.mMaxCellLineCount > 1) {
                z = true;
            } else {
                z = false;
            }
            if (this.mFirstImage != null) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (i4 == 0 || i4 == 5) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (i4 == 4) {
                int i6 = getFirstImageSize(sliceStyle.mContext).y;
                if (z) {
                    i5 = 2;
                }
                i = i6 + (i5 * sliceStyle.mGridRawImageTextHeight);
            } else if (z) {
                if (z2) {
                    i = sliceStyle.mGridMaxHeight;
                } else {
                    i = sliceStyle.mGridMinHeight;
                }
            } else if (z3) {
                i = sliceStyle.mGridMinHeight;
            } else {
                i = sliceStyle.mGridImageTextHeight;
            }
        } else if (this.mGridContent.size() == 1) {
            i = sliceStyle.mGridBigPicMaxHeight;
        } else if (i4 == 0) {
            i = sliceStyle.mGridMinHeight;
        } else if (i4 == 4) {
            i = getFirstImageSize(sliceStyle.mContext).y;
        } else {
            i = sliceStyle.mGridAllImagesHeight;
        }
        boolean z4 = this.mAllImages;
        if (!z4 || this.mRowIndex != 0) {
            i2 = 0;
        } else {
            i2 = sliceStyle.mGridTopPadding;
        }
        if (z4 && this.mIsLastIndex) {
            i3 = sliceStyle.mGridBottomPadding;
        }
        return i3 + i + i2;
    }
}
