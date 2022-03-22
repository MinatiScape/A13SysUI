package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.util.Log;
import android.util.PathParser;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1;
import com.android.launcher3.icons.BitmapInfo;
import com.android.wm.shell.bubbles.Bubble;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class BubbleViewInfoTask extends AsyncTask<Void, Void, BubbleViewInfo> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BubbleBadgeIconFactory mBadgeIconFactory;
    public Bubble mBubble;
    public Callback mCallback;
    public WeakReference<Context> mContext;
    public WeakReference<BubbleController> mController;
    public BubbleIconFactory mIconFactory;
    public Executor mMainExecutor;
    public boolean mSkipInflation;
    public WeakReference<BubbleStackView> mStackView;

    /* loaded from: classes.dex */
    public static class BubbleViewInfo {
        public String appName;
        public Bitmap badgeBitmap;
        public Bitmap bubbleBitmap;
        public int dotColor;
        public Path dotPath;
        public BubbleExpandedView expandedView;
        public Bubble.FlyoutMessage flyoutMessage;
        public BadgedImageView imageView;
        public Bitmap mRawBadgeBitmap;
        public ShortcutInfo shortcutInfo;

        public static BubbleViewInfo populate(Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, BubbleBadgeIconFactory bubbleBadgeIconFactory, Bubble bubble, boolean z) {
            Drawable drawable;
            boolean z2;
            BubbleViewInfo bubbleViewInfo = new BubbleViewInfo();
            if (!z) {
                Objects.requireNonNull(bubble);
                if (bubble.mIconView == null || bubble.mExpandedView == null) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (!z2) {
                    LayoutInflater from = LayoutInflater.from(context);
                    BadgedImageView badgedImageView = (BadgedImageView) from.inflate(2131624026, (ViewGroup) bubbleStackView, false);
                    bubbleViewInfo.imageView = badgedImageView;
                    badgedImageView.initialize(bubbleController.getPositioner());
                    BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) from.inflate(2131624018, (ViewGroup) bubbleStackView, false);
                    bubbleViewInfo.expandedView = bubbleExpandedView;
                    bubbleExpandedView.initialize(bubbleController, bubbleStackView, false);
                }
            }
            Objects.requireNonNull(bubble);
            ShortcutInfo shortcutInfo = bubble.mShortcutInfo;
            if (shortcutInfo != null) {
                bubbleViewInfo.shortcutInfo = shortcutInfo;
            }
            PackageManager packageManagerForUser = BubbleController.getPackageManagerForUser(context, bubble.mUser.getIdentifier());
            Drawable drawable2 = null;
            try {
                ApplicationInfo applicationInfo = packageManagerForUser.getApplicationInfo(bubble.mPackageName, 795136);
                if (applicationInfo != null) {
                    bubbleViewInfo.appName = String.valueOf(packageManagerForUser.getApplicationLabel(applicationInfo));
                }
                Drawable applicationIcon = packageManagerForUser.getApplicationIcon(bubble.mPackageName);
                Drawable userBadgedIcon = packageManagerForUser.getUserBadgedIcon(applicationIcon, bubble.mUser);
                ShortcutInfo shortcutInfo2 = bubbleViewInfo.shortcutInfo;
                Icon icon = bubble.mIcon;
                Objects.requireNonNull(bubbleIconFactory);
                if (shortcutInfo2 != null) {
                    drawable = ((LauncherApps) context.getSystemService("launcherapps")).getShortcutIconDrawable(shortcutInfo2, context.getResources().getConfiguration().densityDpi);
                } else if (icon != null) {
                    if (icon.getType() == 4 || icon.getType() == 6) {
                        context.grantUriPermission(context.getPackageName(), icon.getUri(), 1);
                    }
                    drawable = icon.loadDrawable(context);
                } else {
                    drawable = null;
                }
                if (drawable != null) {
                    applicationIcon = drawable;
                }
                BitmapInfo badgeBitmap = bubbleBadgeIconFactory.getBadgeBitmap(userBadgedIcon, bubble.mIsImportantConversation);
                bubbleViewInfo.badgeBitmap = badgeBitmap.icon;
                bubbleViewInfo.mRawBadgeBitmap = bubbleBadgeIconFactory.getBadgeBitmap(userBadgedIcon, false).icon;
                bubbleViewInfo.bubbleBitmap = bubbleIconFactory.createBadgedIconBitmap(applicationIcon, null).icon;
                Path createPathFromPathData = PathParser.createPathFromPathData(context.getResources().getString(17039972));
                Matrix matrix = new Matrix();
                float scale = bubbleIconFactory.getNormalizer().getScale(applicationIcon, null, null, null);
                matrix.setScale(scale, scale, 50.0f, 50.0f);
                createPathFromPathData.transform(matrix);
                bubbleViewInfo.dotPath = createPathFromPathData;
                bubbleViewInfo.dotColor = ColorUtils.blendARGB(badgeBitmap.color, -1, 0.54f);
                Bubble.FlyoutMessage flyoutMessage = bubble.mFlyoutMessage;
                bubbleViewInfo.flyoutMessage = flyoutMessage;
                if (flyoutMessage != null) {
                    Icon icon2 = flyoutMessage.senderIcon;
                    int i = BubbleViewInfoTask.$r8$clinit;
                    if (icon2 != null) {
                        try {
                            if (icon2.getType() == 4 || icon2.getType() == 6) {
                                context.grantUriPermission(context.getPackageName(), icon2.getUri(), 1);
                            }
                            drawable2 = icon2.loadDrawable(context);
                        } catch (Exception e) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("loadSenderAvatar failed: ");
                            m.append(e.getMessage());
                            Log.w("Bubbles", m.toString());
                        }
                    }
                    flyoutMessage.senderAvatar = drawable2;
                }
                return bubbleViewInfo;
            } catch (PackageManager.NameNotFoundException unused) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to find package: ");
                m2.append(bubble.mPackageName);
                Log.w("Bubbles", m2.toString());
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        void onBubbleViewsReady(Bubble bubble);
    }

    @Override // android.os.AsyncTask
    public final /* bridge */ /* synthetic */ BubbleViewInfo doInBackground(Void[] voidArr) {
        return doInBackground();
    }

    public final BubbleViewInfo doInBackground() {
        return BubbleViewInfo.populate(this.mContext.get(), this.mController.get(), this.mStackView.get(), this.mIconFactory, this.mBadgeIconFactory, this.mBubble, this.mSkipInflation);
    }

    @Override // android.os.AsyncTask
    public final void onPostExecute(BubbleViewInfo bubbleViewInfo) {
        BubbleViewInfo bubbleViewInfo2 = bubbleViewInfo;
        if (!isCancelled() && bubbleViewInfo2 != null) {
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda1(this, bubbleViewInfo2, 5));
        }
    }

    public BubbleViewInfoTask(Bubble bubble, Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, BubbleBadgeIconFactory bubbleBadgeIconFactory, boolean z, Callback callback, Executor executor) {
        this.mBubble = bubble;
        this.mContext = new WeakReference<>(context);
        this.mController = new WeakReference<>(bubbleController);
        this.mStackView = new WeakReference<>(bubbleStackView);
        this.mIconFactory = bubbleIconFactory;
        this.mBadgeIconFactory = bubbleBadgeIconFactory;
        this.mSkipInflation = z;
        this.mCallback = callback;
        this.mMainExecutor = executor;
    }
}
