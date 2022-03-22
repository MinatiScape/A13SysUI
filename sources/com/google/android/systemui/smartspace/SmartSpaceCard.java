package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
/* loaded from: classes.dex */
public final class SmartSpaceCard {
    public static int sRequestCode;
    public final SmartspaceProto$SmartspaceUpdate.SmartspaceCard mCard;
    public final Context mContext;
    public Bitmap mIcon;
    public boolean mIconProcessed;
    public final Intent mIntent;
    public final long mPublishTime;
    public int mRequestCode;

    public static SmartSpaceCard fromWrapper(Context context, SmartspaceProto$CardWrapper smartspaceProto$CardWrapper, boolean z) {
        Intent intent;
        Bitmap bitmap;
        try {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.TapAction tapAction = smartspaceProto$CardWrapper.card.tapAction;
            if (tapAction == null || TextUtils.isEmpty(tapAction.intent)) {
                intent = null;
            } else {
                intent = Intent.parseUri(smartspaceProto$CardWrapper.card.tapAction.intent, 0);
            }
            byte[] bArr = smartspaceProto$CardWrapper.icon;
            if (bArr != null) {
                bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, null);
            } else {
                bitmap = null;
            }
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131165794);
            if (bitmap != null && bitmap.getHeight() > dimensionPixelSize) {
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) ((dimensionPixelSize / bitmap.getHeight()) * bitmap.getWidth()), dimensionPixelSize, true);
            }
            return new SmartSpaceCard(context, smartspaceProto$CardWrapper.card, intent, bitmap, smartspaceProto$CardWrapper.publishTime);
        } catch (Exception e) {
            Log.e("SmartspaceCard", "from proto", e);
            return null;
        }
    }

    public final String getDurationText(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        long j;
        if (formatParam.formatParamArgs == 2) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
            j = smartspaceCard.eventTimeMillis + smartspaceCard.eventDurationMillis;
        } else {
            j = this.mCard.eventTimeMillis;
        }
        int ceil = (int) Math.ceil(Math.abs(System.currentTimeMillis() - j) / 60000.0d);
        if (ceil < 60) {
            return this.mContext.getResources().getQuantityString(2131820557, ceil, Integer.valueOf(ceil));
        }
        int i = ceil / 60;
        int i2 = ceil % 60;
        String quantityString = this.mContext.getResources().getQuantityString(2131820556, i, Integer.valueOf(i));
        if (i2 <= 0) {
            return quantityString;
        }
        return this.mContext.getString(2131953296, quantityString, this.mContext.getResources().getQuantityString(2131820557, i2, Integer.valueOf(i2)));
    }

    public final long getExpiration() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.ExpiryCriteria expiryCriteria;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        if (smartspaceCard == null || (expiryCriteria = smartspaceCard.expiryCriteria) == null) {
            return 0L;
        }
        return expiryCriteria.expirationTimeMillis;
    }

    public final PendingIntent getPendingIntent() {
        if (this.mCard.tapAction == null) {
            return null;
        }
        Intent intent = new Intent(this.mIntent);
        int i = this.mCard.tapAction.actionType;
        if (i == 1) {
            intent.addFlags(268435456);
            intent.setPackage("com.google.android.googlequicksearchbox");
            return PendingIntent.getBroadcast(this.mContext, this.mRequestCode, intent, 0);
        } else if (i != 2) {
            return null;
        } else {
            return PendingIntent.getActivity(this.mContext, this.mRequestCode, intent, 67108864);
        }
    }

    public SmartSpaceCard(Context context, SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, Bitmap bitmap, long j) {
        this.mContext = context.getApplicationContext();
        this.mCard = smartspaceCard;
        this.mIntent = intent;
        this.mIcon = bitmap;
        this.mPublishTime = j;
        int i = sRequestCode + 1;
        sRequestCode = i;
        if (i > 2147483646) {
            sRequestCode = 0;
        }
        this.mRequestCode = sRequestCode;
    }

    public final String getFormattedTitle() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText;
        String str;
        boolean z;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr2;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message == null || (formattedText = message.title) == null || (str = formattedText.text) == null) {
            return "";
        }
        if (str == null || (formatParamArr2 = formattedText.formatParam) == null || formatParamArr2.length <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return str;
        }
        String str2 = null;
        int i = 0;
        String str3 = null;
        while (true) {
            formatParamArr = formattedText.formatParam;
            if (i >= formatParamArr.length) {
                break;
            }
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam = formatParamArr[i];
            if (formatParam != null) {
                int i2 = formatParam.formatParamArgs;
                if (i2 == 1 || i2 == 2) {
                    str3 = getDurationText(formatParam);
                } else if (i2 == 3) {
                    str2 = formatParam.text;
                }
            }
            i++;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        if (smartspaceCard.cardType == 3 && formatParamArr.length == 2) {
            str3 = formatParamArr[0].text;
            str2 = formatParamArr[1].text;
        }
        if (str2 == null) {
            return "";
        }
        if (str3 == null) {
            if (message != smartspaceCard.duringEvent) {
                return str;
            }
            str3 = this.mContext.getString(2131953298);
        }
        return this.mContext.getString(2131953299, str3, str2);
    }

    public final SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message getMessage() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message2;
        long currentTimeMillis = System.currentTimeMillis();
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        long j = smartspaceCard.eventTimeMillis;
        long j2 = smartspaceCard.eventDurationMillis + j;
        if (currentTimeMillis < j && (message2 = smartspaceCard.preEvent) != null) {
            return message2;
        }
        if (currentTimeMillis > j2 && (message = smartspaceCard.postEvent) != null) {
            return message;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message3 = smartspaceCard.duringEvent;
        if (message3 != null) {
            return message3;
        }
        return null;
    }

    public final boolean isExpired() {
        if (System.currentTimeMillis() > getExpiration()) {
            return true;
        }
        return false;
    }

    public final String substitute(boolean z) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText;
        String str;
        boolean z2;
        String str2;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message == null) {
            formattedText = null;
        } else if (z) {
            formattedText = message.title;
        } else {
            formattedText = message.subtitle;
        }
        if (formattedText == null || (str = formattedText.text) == null) {
            return "";
        }
        if (str == null || (formatParamArr = formattedText.formatParam) == null || formatParamArr.length <= 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (!z2) {
            return str;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr2 = formattedText.formatParam;
        int length = formatParamArr2.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            int i2 = formatParamArr2[i].formatParamArgs;
            if (i2 == 1 || i2 == 2) {
                strArr[i] = getDurationText(formatParamArr2[i]);
            } else if (i2 != 3) {
                strArr[i] = "";
            } else {
                if (formatParamArr2[i].text != null) {
                    str2 = formatParamArr2[i].text;
                } else {
                    str2 = "";
                }
                strArr[i] = str2;
            }
        }
        return String.format(str, strArr);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("title:");
        m.append(substitute(true));
        m.append(" subtitle:");
        m.append(substitute(false));
        m.append(" expires:");
        m.append(getExpiration());
        m.append(" published:");
        m.append(this.mPublishTime);
        return m.toString();
    }
}
