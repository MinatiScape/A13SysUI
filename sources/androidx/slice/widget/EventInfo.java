package androidx.slice.widget;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.slice.widget.SliceView;
import com.android.systemui.plugins.FalsingManager;
/* loaded from: classes.dex */
public final class EventInfo {
    public int actionType;
    public int rowIndex;
    public int rowTemplateType;
    public int sliceMode = 2;
    public int actionPosition = 0;
    public int actionIndex = -1;
    public int actionCount = -1;
    public int state = -1;

    public final String toString() {
        String str;
        String str2;
        String str3;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mode=");
        int i = this.sliceMode;
        SliceView.AnonymousClass3 r2 = SliceView.SLICE_ACTION_PRIORITY_COMPARATOR;
        if (i == 1) {
            str = "MODE SMALL";
        } else if (i == 2) {
            str = "MODE LARGE";
        } else if (i != 3) {
            str = VendorAtomValue$$ExternalSyntheticOutline0.m("unknown mode: ", i);
        } else {
            str = "MODE SHORTCUT";
        }
        m.append(str);
        m.append(", actionType=");
        int i2 = this.actionType;
        String str4 = "TIME_PICK";
        switch (i2) {
            case 0:
                str2 = "TOGGLE";
                break;
            case 1:
                str2 = "BUTTON";
                break;
            case 2:
                str2 = "SLIDER";
                break;
            case 3:
                str2 = "CONTENT";
                break;
            case 4:
                str2 = "SEE MORE";
                break;
            case 5:
                str2 = "SELECTION";
                break;
            case FalsingManager.VERSION /* 6 */:
                str2 = "DATE_PICK";
                break;
            case 7:
                str2 = str4;
                break;
            default:
                str2 = VendorAtomValue$$ExternalSyntheticOutline0.m("unknown action: ", i2);
                break;
        }
        m.append(str2);
        m.append(", rowTemplateType=");
        int i3 = this.rowTemplateType;
        switch (i3) {
            case -1:
                str4 = "SHORTCUT";
                break;
            case 0:
                str4 = "LIST";
                break;
            case 1:
                str4 = "GRID";
                break;
            case 2:
                str4 = "MESSAGING";
                break;
            case 3:
                str4 = "TOGGLE";
                break;
            case 4:
                str4 = "SLIDER";
                break;
            case 5:
                str4 = "PROGRESS";
                break;
            case FalsingManager.VERSION /* 6 */:
                str4 = "SELECTION";
                break;
            case 7:
                str4 = "DATE_PICK";
                break;
            case 8:
                break;
            default:
                str4 = VendorAtomValue$$ExternalSyntheticOutline0.m("unknown row type: ", i3);
                break;
        }
        m.append(str4);
        m.append(", rowIndex=");
        m.append(this.rowIndex);
        m.append(", actionPosition=");
        int i4 = this.actionPosition;
        if (i4 == 0) {
            str3 = "START";
        } else if (i4 == 1) {
            str3 = "END";
        } else if (i4 != 2) {
            str3 = VendorAtomValue$$ExternalSyntheticOutline0.m("unknown position: ", i4);
        } else {
            str3 = "CELL";
        }
        m.append(str3);
        m.append(", actionIndex=");
        m.append(this.actionIndex);
        m.append(", actionCount=");
        m.append(this.actionCount);
        m.append(", state=");
        m.append(this.state);
        return m.toString();
    }

    public EventInfo(int i, int i2, int i3) {
        this.actionType = i;
        this.rowTemplateType = i2;
        this.rowIndex = i3;
    }
}
