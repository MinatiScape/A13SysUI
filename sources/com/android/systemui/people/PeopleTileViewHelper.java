package com.android.systemui.people;

import android.app.people.ConversationStatus;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.Pair;
import android.util.SizeF;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable21;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.FastBitmapDrawable;
import com.android.settingslib.Utils;
import com.android.systemui.people.PeopleStoryIconFactory;
import com.android.systemui.people.widget.PeopleTileKey;
import com.android.systemui.plugins.FalsingManager;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public final class PeopleTileViewHelper {
    public int mAppWidgetId;
    public Context mContext;
    public float mDensity;
    public int mHeight;
    public NumberFormat mIntegerFormat;
    public boolean mIsLeftToRight;
    public PeopleTileKey mKey;
    public int mLayoutSize;
    public Locale mLocale;
    public int mMediumVerticalPadding;
    public PeopleSpaceTile mTile;
    public int mWidth;
    public static final Pattern DOUBLE_EXCLAMATION_PATTERN = Pattern.compile("[!][!]+");
    public static final Pattern DOUBLE_QUESTION_PATTERN = Pattern.compile("[?][?]+");
    public static final Pattern ANY_DOUBLE_MARK_PATTERN = Pattern.compile("[!?][!?]+");
    public static final Pattern MIXED_MARK_PATTERN = Pattern.compile("![?].*|.*[?]!");
    public static final Pattern EMOJI_PATTERN = Pattern.compile("\\p{RI}\\p{RI}|(\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})|[\\p{Emoji}&&\\p{So}])(\\x{200D}\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})?)*");

    public static Bitmap getPersonIconBitmap(Context context, PeopleSpaceTile peopleSpaceTile, int i, boolean z) {
        Drawable drawable;
        Icon userIcon = peopleSpaceTile.getUserIcon();
        if (userIcon == null) {
            Drawable mutate = context.getDrawable(2131231763).mutate();
            mutate.setColorFilter(FastBitmapDrawable.getDisabledColorFilter(1.0f));
            return PeopleSpaceUtils.convertDrawableToBitmap(mutate);
        }
        PackageManager packageManager = context.getPackageManager();
        IconDrawableFactory.newInstance(context, false);
        PeopleStoryIconFactory peopleStoryIconFactory = new PeopleStoryIconFactory(context, packageManager, i);
        RoundedBitmapDrawable21 roundedBitmapDrawable21 = new RoundedBitmapDrawable21(context.getResources(), userIcon.getBitmap());
        String packageName = peopleSpaceTile.getPackageName();
        PeopleTileKey peopleTileKey = PeopleSpaceUtils.EMPTY_KEY;
        int identifier = peopleSpaceTile.getUserHandle().getIdentifier();
        boolean isImportantConversation = peopleSpaceTile.isImportantConversation();
        try {
            drawable = Utils.getBadgedIcon(peopleStoryIconFactory.mContext, peopleStoryIconFactory.mPackageManager.getApplicationInfoAsUser(packageName, 128, identifier));
        } catch (PackageManager.NameNotFoundException unused) {
            drawable = peopleStoryIconFactory.mPackageManager.getDefaultActivityIcon();
        }
        PeopleStoryIconFactory.PeopleStoryIconDrawable peopleStoryIconDrawable = new PeopleStoryIconFactory.PeopleStoryIconDrawable(roundedBitmapDrawable21, drawable, peopleStoryIconFactory.mIconBitmapSize, peopleStoryIconFactory.mImportantConversationColor, isImportantConversation, peopleStoryIconFactory.mIconSize, peopleStoryIconFactory.mDensity, peopleStoryIconFactory.mAccentColor, z);
        if (isDndBlockingTileData(peopleSpaceTile)) {
            peopleStoryIconDrawable.setColorFilter(FastBitmapDrawable.getDisabledColorFilter(1.0f));
        }
        return PeopleSpaceUtils.convertDrawableToBitmap(peopleStoryIconDrawable);
    }

    public static boolean isDndBlockingTileData(PeopleSpaceTile peopleSpaceTile) {
        if (peopleSpaceTile == null) {
            return false;
        }
        int notificationPolicyState = peopleSpaceTile.getNotificationPolicyState();
        if ((notificationPolicyState & 1) != 0) {
            return false;
        }
        if ((notificationPolicyState & 4) != 0 && peopleSpaceTile.isImportantConversation()) {
            return false;
        }
        if ((notificationPolicyState & 8) != 0 && peopleSpaceTile.getContactAffinity() == 1.0f) {
            return false;
        }
        if ((notificationPolicyState & 16) == 0 || (peopleSpaceTile.getContactAffinity() != 0.5f && peopleSpaceTile.getContactAffinity() != 1.0f)) {
            return !peopleSpaceTile.canBypassDnd();
        }
        return false;
    }

    /* loaded from: classes.dex */
    public static final class RemoteViewsAndSizes {
        public final int mAvatarSize;
        public final RemoteViews mRemoteViews;

        public RemoteViewsAndSizes(RemoteViews remoteViews, int i) {
            this.mRemoteViews = remoteViews;
            this.mAvatarSize = i;
        }
    }

    public final RemoteViewsAndSizes createDndRemoteViews() {
        int i;
        int i2;
        int i3;
        StaticLayout staticLayout;
        int i4;
        String packageName = this.mContext.getPackageName();
        int i5 = this.mLayoutSize;
        if (i5 == 1) {
            i = 2131624375;
        } else if (i5 != 2) {
            i = getLayoutSmallByHeight();
        } else {
            i = 2131624376;
        }
        RemoteViews remoteViews = new RemoteViews(packageName, i);
        int sizeInDp = getSizeInDp(2131165354);
        int sizeInDp2 = getSizeInDp(2131166336);
        String string = this.mContext.getString(2131952965);
        remoteViews.setTextViewText(2131429032, string);
        if (this.mLayoutSize == 2) {
            i2 = 2131165527;
        } else {
            i2 = 2131165528;
        }
        remoteViews.setTextViewTextSize(2131429032, 0, this.mContext.getResources().getDimension(i2));
        int lineHeightFromResource = getLineHeightFromResource(i2);
        int i6 = this.mLayoutSize;
        if (i6 == 1) {
            remoteViews.setInt(2131429032, "setMaxLines", (this.mHeight - 16) / lineHeightFromResource);
        } else {
            float f = this.mDensity;
            int i7 = (int) (16 * f);
            int i8 = (int) (14 * f);
            if (i6 == 0) {
                i3 = 2131166926;
            } else {
                i3 = 2131165876;
            }
            int sizeInDp3 = getSizeInDp(i3);
            int i9 = (this.mHeight - 32) - sizeInDp3;
            int sizeInDp4 = getSizeInDp(2131166752);
            int i10 = this.mWidth - 32;
            int i11 = sizeInDp4 * 2;
            int i12 = (i9 - sizeInDp) - i11;
            try {
                TextView textView = new TextView(this.mContext);
                textView.setTextSize(0, this.mContext.getResources().getDimension(i2));
                textView.setTextAppearance(16974253);
                staticLayout = StaticLayout.Builder.obtain(string, 0, string.length(), textView.getPaint(), (int) (i10 * this.mDensity)).setBreakStrategy(0).build();
            } catch (Exception e) {
                Log.e("PeopleTileView", "Could not create static layout: " + e);
                staticLayout = null;
            }
            if (staticLayout == null) {
                i4 = Integer.MAX_VALUE;
            } else {
                i4 = (int) (staticLayout.getHeight() / this.mDensity);
            }
            if (i4 > i12 || this.mLayoutSize != 2) {
                if (this.mLayoutSize != 0) {
                    remoteViews = new RemoteViews(this.mContext.getPackageName(), 2131624372);
                }
                sizeInDp = getMaxAvatarSize(remoteViews);
                remoteViews.setViewVisibility(2131428367, 8);
                remoteViews.setViewVisibility(2131428475, 8);
                remoteViews.setContentDescription(2131428605, string);
            } else {
                remoteViews.setViewVisibility(2131429032, 0);
                remoteViews.setInt(2131429032, "setMaxLines", i12 / lineHeightFromResource);
                remoteViews.setContentDescription(2131428605, null);
                sizeInDp = AtomicFU.clamp(Math.min(this.mWidth - 32, (i9 - i4) - i11), (int) (10.0f * this.mDensity), sizeInDp2);
                remoteViews.setViewPadding(16908288, i7, i8, i7, i7);
                float f2 = sizeInDp3;
                remoteViews.setViewLayoutWidth(2131428605, f2, 1);
                remoteViews.setViewLayoutHeight(2131428605, f2, 1);
            }
            remoteViews.setViewVisibility(2131428605, 0);
            remoteViews.setImageViewResource(2131428605, 2131232224);
        }
        return new RemoteViewsAndSizes(remoteViews, sizeInDp);
    }

    public final RemoteViews createStatusRemoteViews(ConversationStatus conversationStatus) {
        int i;
        int i2;
        String packageName = this.mContext.getPackageName();
        int i3 = this.mLayoutSize;
        if (i3 == 1) {
            i = 2131624369;
        } else if (i3 != 2) {
            i = getLayoutSmallByHeight();
        } else {
            i = 2131624367;
        }
        RemoteViews remoteViews = new RemoteViews(packageName, i);
        setViewForContentLayout(remoteViews);
        CharSequence description = conversationStatus.getDescription();
        CharSequence charSequence = "";
        if (TextUtils.isEmpty(description)) {
            switch (conversationStatus.getActivity()) {
                case 1:
                    description = this.mContext.getString(2131951955);
                    break;
                case 2:
                    description = this.mContext.getString(2131951887);
                    break;
                case 3:
                    description = this.mContext.getString(2131952868);
                    break;
                case 4:
                    description = this.mContext.getString(2131951897);
                    break;
                case 5:
                    description = this.mContext.getString(2131953495);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    description = this.mContext.getString(2131952403);
                    break;
                case 7:
                    description = this.mContext.getString(2131952667);
                    break;
                case 8:
                    description = this.mContext.getString(2131953438);
                    break;
                default:
                    description = charSequence;
                    break;
            }
        }
        setPredefinedIconVisible(remoteViews);
        int i4 = 2131429032;
        remoteViews.setTextViewText(2131429032, description);
        if (conversationStatus.getActivity() == 1 || conversationStatus.getActivity() == 8) {
            setEmojiBackground(remoteViews, "🎂");
        }
        Icon icon = conversationStatus.getIcon();
        if (icon != null) {
            remoteViews.setViewVisibility(2131428790, 0);
            remoteViews.setImageViewIcon(2131428930, icon);
            int i5 = this.mLayoutSize;
            if (i5 == 2) {
                remoteViews.setInt(2131427738, "setGravity", 80);
                remoteViews.setViewVisibility(2131428475, 8);
                remoteViews.setColorAttr(2131429032, "setTextColor", 16842806);
            } else if (i5 == 1) {
                remoteViews.setViewVisibility(2131429032, 8);
                remoteViews.setTextViewText(2131428475, description);
            }
        } else {
            remoteViews.setColorAttr(2131429032, "setTextColor", 16842808);
            setMaxLines(remoteViews, false);
        }
        setAvailabilityDotPadding(remoteViews, 2131165349);
        switch (conversationStatus.getActivity()) {
            case 1:
                i2 = 2131231775;
                break;
            case 2:
                i2 = 2131231781;
                break;
            case 3:
                i2 = 2131232210;
                break;
            case 4:
                i2 = 2131232196;
                break;
            case 5:
                i2 = 2131232299;
                break;
            case FalsingManager.VERSION /* 6 */:
                i2 = 2131232213;
                break;
            case 7:
                i2 = 2131232026;
                break;
            case 8:
                i2 = 2131231966;
                break;
            default:
                i2 = 2131232211;
                break;
        }
        remoteViews.setImageViewResource(2131428605, i2);
        CharSequence userName = this.mTile.getUserName();
        if (TextUtils.isEmpty(conversationStatus.getDescription())) {
            switch (conversationStatus.getActivity()) {
                case 1:
                    charSequence = this.mContext.getString(2131951956, userName);
                    break;
                case 2:
                    charSequence = this.mContext.getString(2131951888, userName);
                    break;
                case 3:
                    charSequence = this.mContext.getString(2131952869, userName);
                    break;
                case 4:
                    charSequence = this.mContext.getString(2131951897);
                    break;
                case 5:
                    charSequence = this.mContext.getString(2131953495);
                    break;
                case FalsingManager.VERSION /* 6 */:
                    charSequence = this.mContext.getString(2131952403);
                    break;
                case 7:
                    charSequence = this.mContext.getString(2131952668, userName);
                    break;
                case 8:
                    charSequence = this.mContext.getString(2131953439, userName);
                    break;
            }
        } else {
            charSequence = conversationStatus.getDescription();
        }
        String string = this.mContext.getString(2131952867, this.mTile.getUserName(), charSequence);
        int i6 = this.mLayoutSize;
        if (i6 == 0) {
            remoteViews.setContentDescription(2131428605, string);
        } else if (i6 == 1) {
            if (icon != null) {
                i4 = 2131428475;
            }
            remoteViews.setContentDescription(i4, string);
        } else if (i6 == 2) {
            remoteViews.setContentDescription(2131429032, string);
        }
        return remoteViews;
    }

    @VisibleForTesting
    public CharSequence getDoubleEmoji(CharSequence charSequence) {
        Matcher matcher = EMOJI_PATTERN.matcher(charSequence);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            arrayList.add(new Pair(Integer.valueOf(start), Integer.valueOf(end)));
            arrayList2.add(charSequence.subSequence(start, end));
        }
        if (arrayList.size() < 2) {
            return null;
        }
        for (int i = 1; i < arrayList.size(); i++) {
            int i2 = i - 1;
            if (((Pair) arrayList.get(i)).first == ((Pair) arrayList.get(i2)).second && Objects.equals(arrayList2.get(i), arrayList2.get(i2))) {
                return (CharSequence) arrayList2.get(i);
            }
        }
        return null;
    }

    @VisibleForTesting
    public CharSequence getDoublePunctuation(CharSequence charSequence) {
        if (!ANY_DOUBLE_MARK_PATTERN.matcher(charSequence).find()) {
            return null;
        }
        if (MIXED_MARK_PATTERN.matcher(charSequence).find()) {
            return "!?";
        }
        Matcher matcher = DOUBLE_QUESTION_PATTERN.matcher(charSequence);
        if (!matcher.find()) {
            return "!";
        }
        Matcher matcher2 = DOUBLE_EXCLAMATION_PATTERN.matcher(charSequence);
        if (matcher2.find() && matcher.start() >= matcher2.start()) {
            return "!";
        }
        return "?";
    }

    public final int getLayoutSmallByHeight() {
        if (this.mHeight >= getSizeInDp(2131166930)) {
            return 2131624372;
        }
        return 2131624373;
    }

    public final int getLineHeightFromResource(int i) {
        try {
            TextView textView = new TextView(this.mContext);
            textView.setTextSize(0, this.mContext.getResources().getDimension(i));
            textView.setTextAppearance(16974253);
            return (int) (textView.getLineHeight() / this.mDensity);
        } catch (Exception e) {
            Log.e("PeopleTileView", "Could not create text view: " + e);
            return this.getSizeInDp(2131165528);
        }
    }

    public final int getSizeInDp(int i) {
        Context context = this.mContext;
        return (int) (context.getResources().getDimension(i) / this.mDensity);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:2|(3:126|(1:131)(1:130)|132)(2:8|(1:10)(2:11|(5:13|(1:(1:16)(1:17))|18|(1:20)|21)(2:22|(16:24|(1:(1:27)(1:28))|29|(4:31|184|32|35)(5:36|(1:38)(1:39)|40|(1:42)(1:43)|44)|45|(6:47|(5:49|(1:51)(1:52)|53|(1:55)(1:56)|57)|58|(1:60)(3:61|(1:63)|64)|65|(1:67))|68|(1:70)(1:71)|72|134|183|135|(14:138|(1:143)(1:142)|144|(1:146)(1:147)|148|(1:150)(1:151)|152|(1:154)(1:155)|156|(1:158)(1:159)|160|(1:165)(1:164)|166|(1:168)(1:169))(1:137)|172|(4:186|176|(1:178)|179)|182)(5:73|(1:75)(1:76)|77|(1:79)(2:80|(1:82)(1:83))|(1:85)(2:86|(1:88)(11:89|(1:(1:92)(1:93))(1:94)|95|(1:97)|98|(1:100)|101|(1:103)(2:104|(4:107|(1:109)(2:110|(1:112)(2:113|(1:115)(2:116|(1:118)(1:119))))|120|(1:122)(2:123|(1:125))))|106|120|(0)(0)))))))|133|134|183|135|(0)(0)|172|(5:174|186|176|(0)|179)|182) */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x04ae, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x04af, code lost:
        android.util.Log.e("PeopleTileView", "Failed to set common fields: " + r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x039c  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x03fa A[Catch: Exception -> 0x04ae, TryCatch #0 {Exception -> 0x04ae, blocks: (B:135:0x03f4, B:138:0x03fa, B:140:0x0400, B:146:0x041a, B:147:0x0437, B:148:0x0447, B:160:0x0462, B:162:0x046f, B:166:0x0482, B:168:0x0492, B:169:0x04a9), top: B:183:0x03f4 }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x050c A[Catch: Exception -> 0x0525, TryCatch #2 {Exception -> 0x0525, blocks: (B:176:0x04d0, B:178:0x050c, B:179:0x0515), top: B:186:0x04d0 }] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.widget.RemoteViews getViews() {
        /*
            Method dump skipped, instructions count: 1339
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.PeopleTileViewHelper.getViews():android.widget.RemoteViews");
    }

    public final void setAvailabilityDotPadding(RemoteViews remoteViews, int i) {
        int i2;
        int i3;
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(i);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(2131166361);
        boolean z = this.mIsLeftToRight;
        if (z) {
            i2 = dimensionPixelSize;
        } else {
            i2 = 0;
        }
        if (z) {
            i3 = 0;
        } else {
            i3 = dimensionPixelSize;
        }
        remoteViews.setViewPadding(2131428363, i2, 0, i3, dimensionPixelSize2);
    }

    public final void setContentDescriptionForNotificationTextContent(RemoteViews remoteViews, CharSequence charSequence, CharSequence charSequence2) {
        int i;
        String string = this.mContext.getString(2131952866, charSequence2, charSequence);
        if (this.mLayoutSize == 0) {
            i = 2131428605;
        } else {
            i = 2131429032;
        }
        remoteViews.setContentDescription(i, string);
    }

    public final void setMaxLines(RemoteViews remoteViews, boolean z) {
        int i;
        int i2;
        boolean z2;
        int i3;
        int i4;
        if (this.mLayoutSize == 2) {
            i2 = 2131165527;
            i = getLineHeightFromResource(2131166583);
        } else {
            i2 = 2131165528;
            i = getLineHeightFromResource(2131166584);
        }
        if (remoteViews.getLayoutId() == 2131624367) {
            z2 = true;
        } else {
            z2 = false;
        }
        int i5 = this.mLayoutSize;
        if (i5 == 1) {
            i3 = this.mHeight - ((this.mMediumVerticalPadding * 2) + (i + 12));
        } else if (i5 != 2) {
            i3 = -1;
        } else {
            if (z2) {
                i4 = 76;
            } else {
                i4 = 62;
            }
            i3 = this.mHeight - ((getSizeInDp(2131166337) + i) + i4);
        }
        int max = Math.max(2, Math.floorDiv(i3, getLineHeightFromResource(i2)));
        if (z) {
            max--;
        }
        remoteViews.setInt(2131429032, "setMaxLines", max);
    }

    public final RemoteViews setViewForContentLayout(RemoteViews remoteViews) {
        decorateBackground(remoteViews, "");
        remoteViews.setContentDescription(2131428605, null);
        remoteViews.setContentDescription(2131429032, null);
        remoteViews.setContentDescription(2131428475, null);
        remoteViews.setContentDescription(2131428115, null);
        remoteViews.setAccessibilityTraversalAfter(2131429032, 2131428475);
        if (this.mLayoutSize == 0) {
            remoteViews.setViewVisibility(2131428605, 0);
            remoteViews.setViewVisibility(2131428475, 8);
        } else {
            remoteViews.setViewVisibility(2131428605, 8);
            remoteViews.setViewVisibility(2131428475, 0);
            remoteViews.setViewVisibility(2131429032, 0);
            remoteViews.setViewVisibility(2131428946, 8);
            remoteViews.setViewVisibility(2131428115, 8);
            remoteViews.setViewVisibility(2131428790, 8);
        }
        if (this.mLayoutSize == 1) {
            int floor = (int) Math.floor(this.mDensity * 16.0f);
            int floor2 = (int) Math.floor(this.mMediumVerticalPadding * this.mDensity);
            remoteViews.setViewPadding(2131427738, floor, floor2, floor, floor2);
            remoteViews.setViewPadding(2131428475, 0, 0, 0, 0);
            if (this.mHeight > ((int) (this.mContext.getResources().getDimension(2131166362) / this.mDensity))) {
                remoteViews.setTextViewTextSize(2131428475, 0, (int) this.mContext.getResources().getDimension(2131166335));
            }
        }
        if (this.mLayoutSize == 2) {
            remoteViews.setViewPadding(2131428475, 0, 0, 0, this.mContext.getResources().getDimensionPixelSize(2131165366));
            remoteViews.setInt(2131427738, "setGravity", 48);
        }
        remoteViews.setViewLayoutHeightDimen(2131428605, 2131166926);
        remoteViews.setViewLayoutWidthDimen(2131428605, 2131166926);
        remoteViews.setViewVisibility(2131428367, 8);
        if (this.mTile.getUserName() != null) {
            remoteViews.setTextViewText(2131428475, this.mTile.getUserName());
        }
        return remoteViews;
    }

    public PeopleTileViewHelper(Context context, PeopleSpaceTile peopleSpaceTile, int i, int i2, int i3, PeopleTileKey peopleTileKey) {
        this.mContext = context;
        this.mTile = peopleSpaceTile;
        this.mKey = peopleTileKey;
        this.mAppWidgetId = i;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mWidth = i2;
        this.mHeight = i3;
        int i4 = 2;
        boolean z = true;
        if (i3 < getSizeInDp(2131166929) || this.mWidth < getSizeInDp(2131166931)) {
            if (this.mHeight < getSizeInDp(2131166930) || this.mWidth < getSizeInDp(2131166932)) {
                i4 = 0;
            } else {
                this.mMediumVerticalPadding = Math.max(4, Math.min(Math.floorDiv(this.mHeight - (getLineHeightFromResource(2131166584) + (getSizeInDp(2131165353) + 4)), 2), 16));
                i4 = 1;
            }
        }
        this.mLayoutSize = i4;
        this.mIsLeftToRight = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) != 0 ? false : z;
    }

    public static RemoteViews createRemoteViews(final Context context, final PeopleSpaceTile peopleSpaceTile, final int i, Bundle bundle, final PeopleTileKey peopleTileKey) {
        float f = context.getResources().getDisplayMetrics().density;
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("appWidgetSizes");
        if (parcelableArrayList == null || parcelableArrayList.isEmpty()) {
            int dimension = (int) (context.getResources().getDimension(2131165606) / f);
            int dimension2 = (int) (context.getResources().getDimension(2131165604) / f);
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(new SizeF(bundle.getInt("appWidgetMinWidth", dimension), bundle.getInt("appWidgetMaxHeight", dimension2)));
            arrayList.add(new SizeF(bundle.getInt("appWidgetMaxWidth", dimension), bundle.getInt("appWidgetMinHeight", dimension2)));
            parcelableArrayList = arrayList;
        }
        return new RemoteViews((Map) parcelableArrayList.stream().distinct().collect(Collectors.toMap(Function.identity(), new Function() { // from class: com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SizeF sizeF = (SizeF) obj;
                return new PeopleTileViewHelper(context, peopleSpaceTile, i, (int) sizeF.getWidth(), (int) sizeF.getHeight(), peopleTileKey).getViews();
            }
        })));
    }

    public static RemoteViews setEmojiBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(2131427911, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(2131427908, charSequence);
        remoteViews.setTextViewText(2131427909, charSequence);
        remoteViews.setTextViewText(2131427910, charSequence);
        remoteViews.setViewVisibility(2131427911, 0);
        return remoteViews;
    }

    public static RemoteViews setPunctuationBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(2131428640, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(2131428634, charSequence);
        remoteViews.setTextViewText(2131428635, charSequence);
        remoteViews.setTextViewText(2131428636, charSequence);
        remoteViews.setTextViewText(2131428637, charSequence);
        remoteViews.setTextViewText(2131428638, charSequence);
        remoteViews.setTextViewText(2131428639, charSequence);
        remoteViews.setViewVisibility(2131428640, 0);
        return remoteViews;
    }

    public final RemoteViews decorateBackground(RemoteViews remoteViews, CharSequence charSequence) {
        CharSequence doubleEmoji = getDoubleEmoji(charSequence);
        if (!TextUtils.isEmpty(doubleEmoji)) {
            setEmojiBackground(remoteViews, doubleEmoji);
            setPunctuationBackground(remoteViews, null);
            return remoteViews;
        }
        CharSequence doublePunctuation = getDoublePunctuation(charSequence);
        setEmojiBackground(remoteViews, null);
        setPunctuationBackground(remoteViews, doublePunctuation);
        return remoteViews;
    }

    public final int getMaxAvatarSize(RemoteViews remoteViews) {
        int layoutId = remoteViews.getLayoutId();
        int sizeInDp = getSizeInDp(2131165353);
        if (layoutId == 2131624368) {
            return getSizeInDp(2131166337);
        }
        if (layoutId == 2131624369) {
            return getSizeInDp(2131165353);
        }
        if (layoutId == 2131624372) {
            sizeInDp = Math.min(this.mHeight - (Math.max(18, getLineHeightFromResource(2131166585)) + 18), this.mWidth - 8);
        }
        if (layoutId == 2131624373) {
            sizeInDp = Math.min(this.mHeight - 10, this.mWidth - 16);
        }
        if (layoutId == 2131624366) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(2131165527) * 3) + 62), getSizeInDp(2131166337));
        }
        if (layoutId == 2131624367) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(2131165527) * 3) + 76), getSizeInDp(2131166337));
        }
        if (layoutId == 2131624364) {
            sizeInDp = Math.min(this.mHeight - ((((getLineHeightFromResource(2131165527) + (getLineHeightFromResource(2131166582) + 28)) + 16) + 10) + 16), this.mWidth - 28);
        }
        if (isDndBlockingTileData(this.mTile) && this.mLayoutSize != 0) {
            sizeInDp = createDndRemoteViews().mAvatarSize;
        }
        return Math.min(sizeInDp, getSizeInDp(2131166336));
    }

    public final void setPredefinedIconVisible(RemoteViews remoteViews) {
        int i;
        int i2;
        remoteViews.setViewVisibility(2131428605, 0);
        if (this.mLayoutSize == 1) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131165362);
            boolean z = this.mIsLeftToRight;
            if (z) {
                i = 0;
            } else {
                i = dimensionPixelSize;
            }
            if (z) {
                i2 = dimensionPixelSize;
            } else {
                i2 = 0;
            }
            remoteViews.setViewPadding(2131428475, i, 0, i2, 0);
        }
    }
}
