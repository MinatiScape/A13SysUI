package androidx.slice;

import android.os.Parcelable;
import android.text.Html;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.core.util.Pair;
import androidx.slice.SliceItemHolder;
import androidx.slice.compat.SliceProviderCompat;
import androidx.versionedparcelable.VersionedParcel;
import com.android.systemui.plugins.FalsingManager;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceItemParcelizer {
    public static SliceItem read(VersionedParcel versionedParcel) {
        Object obj;
        SliceItem sliceItem = new SliceItem();
        sliceItem.mHints = (String[]) versionedParcel.readArray(sliceItem.mHints, 1);
        sliceItem.mFormat = versionedParcel.readString(sliceItem.mFormat, 2);
        sliceItem.mSubType = versionedParcel.readString(sliceItem.mSubType, 3);
        SliceItemHolder sliceItemHolder = (SliceItemHolder) versionedParcel.readVersionedParcelable(sliceItem.mHolder, 4);
        sliceItem.mHolder = sliceItemHolder;
        if (sliceItemHolder != null) {
            String str = sliceItem.mFormat;
            SliceProviderCompat.AnonymousClass2 r3 = SliceItemHolder.sHandler;
            if (r3 != null) {
                r3.handle(sliceItemHolder);
            }
            Objects.requireNonNull(str);
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1422950858:
                    if (str.equals("action")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1377881982:
                    if (str.equals("bundle")) {
                        c = 1;
                        break;
                    }
                    break;
                case 104431:
                    if (str.equals("int")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3327612:
                    if (str.equals("long")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3556653:
                    if (str.equals("text")) {
                        c = 4;
                        break;
                    }
                    break;
                case 100313435:
                    if (str.equals("image")) {
                        c = 5;
                        break;
                    }
                    break;
                case 100358090:
                    if (str.equals("input")) {
                        c = 6;
                        break;
                    }
                    break;
                case 109526418:
                    if (str.equals("slice")) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    Parcelable parcelable = sliceItemHolder.mParcelable;
                    if (parcelable != null || sliceItemHolder.mVersionedParcelable != null) {
                        if (parcelable == null) {
                            parcelable = null;
                        }
                        obj = new Pair(parcelable, (Slice) sliceItemHolder.mVersionedParcelable);
                        break;
                    } else {
                        obj = null;
                        break;
                    }
                    break;
                case 1:
                    obj = sliceItemHolder.mBundle;
                    break;
                case 2:
                    obj = Integer.valueOf(sliceItemHolder.mInt);
                    break;
                case 3:
                    obj = Long.valueOf(sliceItemHolder.mLong);
                    break;
                case 4:
                    String str2 = sliceItemHolder.mStr;
                    if (str2 != null && str2.length() != 0) {
                        obj = Html.fromHtml(sliceItemHolder.mStr, 0);
                        break;
                    } else {
                        obj = "";
                        break;
                    }
                    break;
                case 5:
                case 7:
                    obj = sliceItemHolder.mVersionedParcelable;
                    break;
                case FalsingManager.VERSION /* 6 */:
                    obj = sliceItemHolder.mParcelable;
                    break;
                default:
                    throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Unrecognized format ", str));
            }
            sliceItem.mObj = obj;
            SliceItemHolder sliceItemHolder2 = sliceItem.mHolder;
            Objects.requireNonNull(sliceItemHolder2);
            SliceItemHolder.SliceItemPool sliceItemPool = sliceItemHolder2.mPool;
            if (sliceItemPool != null) {
                sliceItemHolder2.mParcelable = null;
                sliceItemHolder2.mVersionedParcelable = null;
                sliceItemHolder2.mInt = 0;
                sliceItemHolder2.mLong = 0L;
                sliceItemHolder2.mStr = null;
                sliceItemPool.mCached.add(sliceItemHolder2);
            }
        } else {
            sliceItem.mObj = null;
        }
        sliceItem.mHolder = null;
        return sliceItem;
    }

    public static void write(SliceItem sliceItem, VersionedParcel versionedParcel) {
        Objects.requireNonNull(versionedParcel);
        Objects.requireNonNull(sliceItem);
        sliceItem.mHolder = new SliceItemHolder(sliceItem.mFormat, sliceItem.mObj);
        if (!Arrays.equals(Slice.NO_HINTS, sliceItem.mHints)) {
            versionedParcel.writeArray(sliceItem.mHints, 1);
        }
        if (!"text".equals(sliceItem.mFormat)) {
            versionedParcel.writeString(sliceItem.mFormat, 2);
        }
        String str = sliceItem.mSubType;
        if (str != null) {
            versionedParcel.writeString(str, 3);
        }
        SliceItemHolder sliceItemHolder = sliceItem.mHolder;
        versionedParcel.setOutputField(4);
        versionedParcel.writeVersionedParcelable(sliceItemHolder);
    }
}
