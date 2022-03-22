package androidx.slice.widget;

import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import okio.Okio;
/* loaded from: classes.dex */
public final class RowContent extends SliceContent {
    public boolean mIsHeader;
    public SliceItem mPrimaryAction;
    public SliceItem mRange;
    public SliceItem mSelection;
    public boolean mShowActionDivider;
    public boolean mShowBottomDivider;
    public boolean mShowTitleItems;
    public SliceItem mStartItem;
    public SliceItem mSubtitleItem;
    public SliceItem mSummaryItem;
    public SliceItem mTitleItem;
    public final ArrayList<SliceItem> mEndItems = new ArrayList<>();
    public final ArrayList<SliceAction> mToggleItems = new ArrayList<>();
    public int mLineCount = 0;

    public static boolean isValidRow(SliceItem sliceItem) {
        if (sliceItem == null) {
            return false;
        }
        if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
            List<SliceItem> items = sliceItem.getSlice().getItems();
            if (sliceItem.hasHint("see_more") && items.isEmpty()) {
                return true;
            }
            for (int i = 0; i < items.size(); i++) {
                if (isValidRowContent(sliceItem, items.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<SliceItem> filterInvalidItems(SliceItem sliceItem) {
        ArrayList<SliceItem> arrayList = new ArrayList<>();
        for (SliceItem sliceItem2 : sliceItem.getSlice().getItems()) {
            if (isValidRowContent(sliceItem, sliceItem2)) {
                arrayList.add(sliceItem2);
            }
        }
        return arrayList;
    }

    public static boolean isValidRowContent(SliceItem sliceItem, SliceItem sliceItem2) {
        if (sliceItem2.hasAnyHints("keywords", "ttl", "last_updated", "horizontal") || "content_description".equals(sliceItem2.mSubType) || "selection_option_key".equals(sliceItem2.mSubType) || "selection_option_value".equals(sliceItem2.mSubType)) {
            return false;
        }
        String str = sliceItem2.mFormat;
        if (!"image".equals(str) && !"text".equals(str) && !"long".equals(str) && !"action".equals(str) && !"input".equals(str) && !"slice".equals(str)) {
            if (!"int".equals(str)) {
                return false;
            }
            Objects.requireNonNull(sliceItem);
            if (!"range".equals(sliceItem.mSubType)) {
                return false;
            }
        }
        return true;
    }

    public final SliceItem getInputRangeThumb() {
        SliceItem sliceItem = this.mRange;
        if (sliceItem == null) {
            return null;
        }
        List<SliceItem> items = sliceItem.getSlice().getItems();
        for (int i = 0; i < items.size(); i++) {
            SliceItem sliceItem2 = items.get(i);
            Objects.requireNonNull(sliceItem2);
            if ("image".equals(sliceItem2.mFormat)) {
                return items.get(i);
            }
        }
        return null;
    }

    public final boolean isDefaultSeeMore() {
        SliceItem sliceItem = this.mSliceItem;
        Objects.requireNonNull(sliceItem);
        if ("action".equals(sliceItem.mFormat)) {
            Slice slice = this.mSliceItem.getSlice();
            Objects.requireNonNull(slice);
            if (Okio.contains(slice.mHints, "see_more") && this.mSliceItem.getSlice().getItems().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isValid() {
        boolean z;
        if (this.mSliceItem != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z || (this.mStartItem == null && this.mPrimaryAction == null && this.mTitleItem == null && this.mSubtitleItem == null && this.mEndItems.size() <= 0 && this.mRange == null && this.mSelection == null && !isDefaultSeeMore())) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x010f, code lost:
        if ("slice".equals(r8.mFormat) != false) goto L_0x0111;
     */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public RowContent(androidx.slice.SliceItem r11, int r12) {
        /*
            Method dump skipped, instructions count: 679
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowContent.<init>(androidx.slice.SliceItem, int):void");
    }

    @Override // androidx.slice.widget.SliceContent
    public final int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        int i;
        int i2;
        SliceItem sliceItem;
        Objects.requireNonNull(sliceStyle);
        Objects.requireNonNull(sliceViewPolicy);
        int i3 = sliceViewPolicy.mMaxSmallHeight;
        if (i3 <= 0) {
            i3 = sliceStyle.mRowMaxHeight;
        }
        SliceItem sliceItem2 = this.mRange;
        SliceItem sliceItem3 = this.mSelection;
        if (sliceItem2 != null) {
            if (!this.mIsHeader || this.mShowTitleItems) {
                sliceItem = this.mStartItem;
            } else {
                sliceItem = null;
            }
            if (sliceItem != null) {
                return sliceStyle.mRowInlineRangeHeight;
            }
            int i4 = this.mLineCount;
            if (i4 == 0) {
                i2 = 0;
            } else if (i4 > 1) {
                i2 = sliceStyle.mRowTextWithRangeHeight;
            } else {
                i2 = sliceStyle.mRowSingleTextWithRangeHeight;
            }
            i = sliceStyle.mRowRangeHeight;
        } else if (sliceItem3 != null) {
            if (this.mLineCount > 1) {
                i2 = sliceStyle.mRowTextWithSelectionHeight;
            } else {
                i2 = sliceStyle.mRowSingleTextWithSelectionHeight;
            }
            i = sliceStyle.mRowSelectionHeight;
        } else {
            if (this.mLineCount <= 1 && !this.mIsHeader) {
                i3 = sliceStyle.mRowMinHeight;
            }
            return i3;
        }
        return i2 + i;
    }
}
