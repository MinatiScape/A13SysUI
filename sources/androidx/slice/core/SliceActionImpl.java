package androidx.slice.core;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
/* loaded from: classes.dex */
public final class SliceActionImpl implements SliceAction {
    public PendingIntent mAction;
    public SliceItem mActionItem;
    public String mActionKey;
    public ActionType mActionType;
    public CharSequence mContentDescription;
    public long mDateTimeMillis;
    public IconCompat mIcon;
    public int mImageMode;
    public boolean mIsActivity;
    public boolean mIsChecked;
    public int mPriority;
    public SliceItem mSliceItem;
    public CharSequence mTitle;

    /* loaded from: classes.dex */
    public enum ActionType {
        DEFAULT,
        TOGGLE,
        DATE_PICKER,
        TIME_PICKER
    }

    public SliceActionImpl(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        this.mActionType = ActionType.DEFAULT;
        this.mPriority = -1;
        this.mDateTimeMillis = -1L;
        this.mAction = pendingIntent;
        this.mIcon = iconCompat;
        this.mTitle = charSequence;
        this.mImageMode = i;
    }

    public final Slice.Builder buildSliceContent(Slice.Builder builder) {
        String[] strArr;
        Slice.Builder builder2 = new Slice.Builder(builder);
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            int i = this.mImageMode;
            if (i == 6) {
                strArr = new String[]{"show_label"};
            } else if (i == 0) {
                strArr = new String[0];
            } else {
                strArr = new String[]{"no_tint"};
            }
            builder2.addIcon(iconCompat, null, strArr);
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder2.addText(charSequence, null, "title");
        }
        CharSequence charSequence2 = this.mContentDescription;
        if (charSequence2 != null) {
            builder2.addText(charSequence2, "content_description", new String[0]);
        }
        long j = this.mDateTimeMillis;
        if (j != -1) {
            builder2.addLong(j, "millis", new String[0]);
        }
        if (this.mActionType == ActionType.TOGGLE && this.mIsChecked) {
            builder2.addHints("selected");
        }
        int i2 = this.mPriority;
        if (i2 != -1) {
            builder2.addInt(i2, "priority", new String[0]);
        }
        String str = this.mActionKey;
        if (str != null) {
            builder2.addText(str, "action_key", new String[0]);
        }
        if (this.mIsActivity) {
            builder.addHints("activity");
        }
        return builder2;
    }

    public final String getSubtype() {
        int ordinal = this.mActionType.ordinal();
        if (ordinal == 1) {
            return "toggle";
        }
        if (ordinal == 2) {
            return "date_picker";
        }
        if (ordinal != 3) {
            return null;
        }
        return "time_picker";
    }

    public final boolean isDefaultToggle() {
        if (this.mActionType == ActionType.TOGGLE && this.mIcon == null) {
            return true;
        }
        return false;
    }

    @Override // androidx.slice.core.SliceAction
    public final boolean isToggle() {
        if (this.mActionType == ActionType.TOGGLE) {
            return true;
        }
        return false;
    }

    public static int parseImageMode(SliceItem sliceItem) {
        if (sliceItem.hasHint("show_label")) {
            return 6;
        }
        if (!sliceItem.hasHint("no_tint")) {
            return 0;
        }
        if (sliceItem.hasHint("raw")) {
            if (sliceItem.hasHint("large")) {
                return 4;
            }
            return 3;
        } else if (sliceItem.hasHint("large")) {
            return 2;
        } else {
            return 1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d5  */
    @android.annotation.SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SliceActionImpl(androidx.slice.SliceItem r9) {
        /*
            Method dump skipped, instructions count: 279
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.core.SliceActionImpl.<init>(androidx.slice.SliceItem):void");
    }

    @Override // androidx.slice.core.SliceAction
    public final int getPriority() {
        return this.mPriority;
    }
}
