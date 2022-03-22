package androidx.slice.builders;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.core.SliceActionImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceAction implements androidx.slice.core.SliceAction {
    public final SliceActionImpl mSliceAction;

    public final IconCompat getIcon() {
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        Objects.requireNonNull(sliceActionImpl);
        return sliceActionImpl.mIcon;
    }

    @Override // androidx.slice.core.SliceAction
    public final int getPriority() {
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        Objects.requireNonNull(sliceActionImpl);
        return sliceActionImpl.mPriority;
    }

    @Override // androidx.slice.core.SliceAction
    public final boolean isToggle() {
        return this.mSliceAction.isToggle();
    }

    public final void setPrimaryAction(Slice.Builder builder) {
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        Objects.requireNonNull(sliceActionImpl);
        PendingIntent pendingIntent = sliceActionImpl.mAction;
        if (pendingIntent == null) {
            pendingIntent = sliceActionImpl.mActionItem.getAction();
        }
        SliceActionImpl sliceActionImpl2 = this.mSliceAction;
        Objects.requireNonNull(sliceActionImpl2);
        Slice.Builder buildSliceContent = sliceActionImpl2.buildSliceContent(builder);
        buildSliceContent.addHints("shortcut", "title");
        builder.addAction(pendingIntent, buildSliceContent.build(), this.mSliceAction.getSubtype());
    }

    public SliceAction(PendingIntent pendingIntent, IconCompat iconCompat, int i, String str) {
        this.mSliceAction = new SliceActionImpl(pendingIntent, iconCompat, i, str);
    }
}
