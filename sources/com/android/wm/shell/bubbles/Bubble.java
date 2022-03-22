package com.android.wm.shell.bubbles;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ShortcutInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda1;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
@VisibleForTesting
/* loaded from: classes.dex */
public class Bubble implements BubbleViewProvider {
    public String mAppName;
    public int mAppUid;
    public Bitmap mBadgeBitmap;
    public Bitmap mBubbleBitmap;
    public String mChannelId;
    public PendingIntent mDeleteIntent;
    public int mDesiredHeight;
    public int mDesiredHeightResId;
    public int mDotColor;
    public Path mDotPath;
    public BubbleExpandedView mExpandedView;
    public int mFlags;
    public FlyoutMessage mFlyoutMessage;
    public final String mGroupKey;
    public Icon mIcon;
    public BadgedImageView mIconView;
    public boolean mInflateSynchronously;
    public BubbleViewInfoTask mInflationTask;
    public InstanceId mInstanceId;
    public PendingIntent mIntent;
    public boolean mIntentActive;
    public Bubble$$ExternalSyntheticLambda0 mIntentCancelListener;
    public boolean mIsBubble;
    public boolean mIsClearable;
    public boolean mIsImportantConversation;
    public boolean mIsTextChanged;
    public final String mKey;
    public long mLastAccessed;
    public long mLastUpdated;
    public final LocusId mLocusId;
    public final Executor mMainExecutor;
    public String mMetadataShortcutId;
    public int mNotificationId;
    public String mPackageName;
    public boolean mPendingIntentCanceled;
    public Bitmap mRawBadgeBitmap;
    public ShortcutInfo mShortcutInfo;
    public boolean mShouldSuppressNotificationDot;
    public boolean mShouldSuppressNotificationList;
    public boolean mShouldSuppressPeek;
    public boolean mShowBubbleUpdateDot;
    public boolean mSuppressFlyout;
    public Bubbles.SuppressionChangedListener mSuppressionListener;
    public int mTaskId;
    public String mTitle;
    public UserHandle mUser;

    /* loaded from: classes.dex */
    public static class FlyoutMessage {
        public boolean isGroupChat;
        public CharSequence message;
        public Drawable senderAvatar;
        public Icon senderIcon;
        public CharSequence senderName;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble(String str, ShortcutInfo shortcutInfo, int i, int i2, String str2, int i3, String str3, Executor executor) {
        this.mShowBubbleUpdateDot = true;
        this.mAppUid = -1;
        Objects.requireNonNull(str);
        Objects.requireNonNull(shortcutInfo);
        this.mMetadataShortcutId = shortcutInfo.getId();
        this.mShortcutInfo = shortcutInfo;
        this.mKey = str;
        LocusId locusId = null;
        this.mGroupKey = null;
        this.mLocusId = str3 != null ? new LocusId(str3) : locusId;
        this.mFlags = 0;
        this.mUser = shortcutInfo.getUserHandle();
        this.mPackageName = shortcutInfo.getPackage();
        this.mIcon = shortcutInfo.getIcon();
        this.mDesiredHeight = i;
        this.mDesiredHeightResId = i2;
        this.mTitle = str2;
        this.mShowBubbleUpdateDot = false;
        this.mMainExecutor = executor;
        this.mTaskId = i3;
    }

    public final int hashCode() {
        return Objects.hash(this.mKey);
    }

    public final void inflate(BubbleViewInfoTask.Callback callback, Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, BubbleBadgeIconFactory bubbleBadgeIconFactory, boolean z) {
        boolean z2;
        BubbleViewInfoTask bubbleViewInfoTask = this.mInflationTask;
        if (bubbleViewInfoTask == null || bubbleViewInfoTask.getStatus() == AsyncTask.Status.FINISHED) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            this.mInflationTask.cancel(true);
        }
        BubbleViewInfoTask bubbleViewInfoTask2 = new BubbleViewInfoTask(this, context, bubbleController, bubbleStackView, bubbleIconFactory, bubbleBadgeIconFactory, z, callback, this.mMainExecutor);
        this.mInflationTask = bubbleViewInfoTask2;
        if (this.mInflateSynchronously) {
            BubbleViewInfoTask.BubbleViewInfo doInBackground = bubbleViewInfoTask2.doInBackground();
            if (!bubbleViewInfoTask2.isCancelled() && doInBackground != null) {
                bubbleViewInfoTask2.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda1(bubbleViewInfoTask2, doInBackground, 5));
                return;
            }
            return;
        }
        bubbleViewInfoTask2.execute(new Void[0]);
    }

    public final boolean showInShade() {
        if (!isEnabled(2) || !this.mIsClearable) {
            return true;
        }
        return false;
    }

    public final void dump(PrintWriter printWriter) {
        boolean z;
        String str;
        printWriter.print("key: ");
        printWriter.println(this.mKey);
        printWriter.print("  showInShade:   ");
        printWriter.println(showInShade());
        printWriter.print("  showDot:       ");
        printWriter.println(showDot());
        printWriter.print("  showFlyout:    ");
        printWriter.println(showFlyout());
        printWriter.print("  lastActivity:  ");
        printWriter.println(Math.max(this.mLastUpdated, this.mLastAccessed));
        printWriter.print("  desiredHeight: ");
        int i = this.mDesiredHeightResId;
        if (i != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            str = String.valueOf(i);
        } else {
            str = String.valueOf(this.mDesiredHeight);
        }
        printWriter.println(str);
        printWriter.print("  suppressNotif: ");
        printWriter.println(isEnabled(2));
        printWriter.print("  autoExpand:    ");
        printWriter.println(isEnabled(1));
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            printWriter.print("BubbleExpandedView");
            printWriter.print("  taskId:               ");
            printWriter.println(bubbleExpandedView.mTaskId);
            printWriter.print("  stackView:            ");
            printWriter.println(bubbleExpandedView.mStackView);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Bubble)) {
            return false;
        }
        return Objects.equals(this.mKey, ((Bubble) obj).mKey);
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView == null) {
            return this.mTaskId;
        }
        Objects.requireNonNull(bubbleExpandedView);
        return bubbleExpandedView.mTaskId;
    }

    public final boolean hasMetadataShortcutId() {
        String str = this.mMetadataShortcutId;
        if (str == null || str.isEmpty()) {
            return false;
        }
        return true;
    }

    public final boolean isEnabled(int i) {
        if ((this.mFlags & i) != 0) {
            return true;
        }
        return false;
    }

    public final void setShowDot(boolean z) {
        this.mShowBubbleUpdateDot = z;
        BadgedImageView badgedImageView = this.mIconView;
        if (badgedImageView != null) {
            badgedImageView.updateDotVisibility(true);
        }
    }

    public final void setSuppressBubble(boolean z) {
        boolean z2;
        Bubbles.SuppressionChangedListener suppressionChangedListener;
        int i = this.mFlags;
        boolean z3 = true;
        if ((i & 4) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("calling setSuppressBubble on ");
            m.append(this.mKey);
            m.append(" when bubble not suppressable");
            Log.e("Bubble", m.toString());
            return;
        }
        if ((i & 8) == 0) {
            z3 = false;
        }
        if (z) {
            this.mFlags = i | 8;
        } else {
            this.mFlags = i & (-9);
        }
        if (z3 != z && (suppressionChangedListener = this.mSuppressionListener) != null) {
            ((BubbleController) ((PipController$$ExternalSyntheticLambda1) suppressionChangedListener).f$0).onBubbleNotificationSuppressionChanged(this);
        }
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final void setTaskViewVisibility() {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.setContentVisibility(false);
        }
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final boolean showDot() {
        if (!this.mShowBubbleUpdateDot || this.mShouldSuppressNotificationDot || isEnabled(2)) {
            return false;
        }
        return true;
    }

    @VisibleForTesting
    public boolean showFlyout() {
        if (this.mSuppressFlyout || this.mShouldSuppressPeek || isEnabled(2) || this.mShouldSuppressNotificationList) {
            return false;
        }
        return true;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Bubble{");
        m.append(this.mKey);
        m.append('}');
        return m.toString();
    }

    public final void setEntry(BubbleEntry bubbleEntry) {
        String str;
        CharSequence charSequence;
        Icon icon;
        Objects.requireNonNull(bubbleEntry);
        this.mLastUpdated = bubbleEntry.mSbn.getPostTime();
        this.mIsBubble = bubbleEntry.mSbn.getNotification().isBubbleNotification();
        this.mPackageName = bubbleEntry.mSbn.getPackageName();
        this.mUser = bubbleEntry.mSbn.getUser();
        CharSequence charSequence2 = bubbleEntry.mSbn.getNotification().extras.getCharSequence("android.title");
        if (charSequence2 == null) {
            str = null;
        } else {
            str = charSequence2.toString();
        }
        this.mTitle = str;
        this.mChannelId = bubbleEntry.mSbn.getNotification().getChannelId();
        this.mNotificationId = bubbleEntry.mSbn.getId();
        this.mAppUid = bubbleEntry.mSbn.getUid();
        this.mInstanceId = bubbleEntry.mSbn.getInstanceId();
        Notification notification = bubbleEntry.mSbn.getNotification();
        Class notificationStyle = notification.getNotificationStyle();
        FlyoutMessage flyoutMessage = new FlyoutMessage();
        flyoutMessage.isGroupChat = notification.extras.getBoolean("android.isGroupConversation");
        try {
            if (Notification.BigTextStyle.class.equals(notificationStyle)) {
                CharSequence charSequence3 = notification.extras.getCharSequence("android.bigText");
                if (TextUtils.isEmpty(charSequence3)) {
                    charSequence3 = notification.extras.getCharSequence("android.text");
                }
                flyoutMessage.message = charSequence3;
            } else if (Notification.MessagingStyle.class.equals(notificationStyle)) {
                Notification.MessagingStyle.Message findLatestIncomingMessage = Notification.MessagingStyle.findLatestIncomingMessage(Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[]) notification.extras.get("android.messages")));
                if (findLatestIncomingMessage != null) {
                    flyoutMessage.message = findLatestIncomingMessage.getText();
                    Person senderPerson = findLatestIncomingMessage.getSenderPerson();
                    if (senderPerson != null) {
                        charSequence = senderPerson.getName();
                    } else {
                        charSequence = null;
                    }
                    flyoutMessage.senderName = charSequence;
                    flyoutMessage.senderAvatar = null;
                    if (senderPerson != null) {
                        icon = senderPerson.getIcon();
                    } else {
                        icon = null;
                    }
                    flyoutMessage.senderIcon = icon;
                }
            } else if (Notification.InboxStyle.class.equals(notificationStyle)) {
                CharSequence[] charSequenceArray = notification.extras.getCharSequenceArray("android.textLines");
                if (charSequenceArray != null && charSequenceArray.length > 0) {
                    flyoutMessage.message = charSequenceArray[charSequenceArray.length - 1];
                }
            } else if (!Notification.MediaStyle.class.equals(notificationStyle)) {
                flyoutMessage.message = notification.extras.getCharSequence("android.text");
            }
        } catch (ArrayIndexOutOfBoundsException | ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }
        this.mFlyoutMessage = flyoutMessage;
        NotificationListenerService.Ranking ranking = bubbleEntry.mRanking;
        if (ranking != null) {
            this.mShortcutInfo = ranking.getConversationShortcutInfo();
            this.mIsTextChanged = bubbleEntry.mRanking.isTextChanged();
            if (bubbleEntry.mRanking.getChannel() != null) {
                this.mIsImportantConversation = bubbleEntry.mRanking.getChannel().isImportantConversation();
            }
        }
        if (bubbleEntry.getBubbleMetadata() != null) {
            this.mMetadataShortcutId = bubbleEntry.getBubbleMetadata().getShortcutId();
            this.mFlags = bubbleEntry.getBubbleMetadata().getFlags();
            this.mDesiredHeight = bubbleEntry.getBubbleMetadata().getDesiredHeight();
            this.mDesiredHeightResId = bubbleEntry.getBubbleMetadata().getDesiredHeightResId();
            this.mIcon = bubbleEntry.getBubbleMetadata().getIcon();
            if (!this.mIntentActive || this.mIntent == null) {
                PendingIntent pendingIntent = this.mIntent;
                if (pendingIntent != null) {
                    pendingIntent.unregisterCancelListener(this.mIntentCancelListener);
                }
                PendingIntent intent = bubbleEntry.getBubbleMetadata().getIntent();
                this.mIntent = intent;
                if (intent != null) {
                    intent.registerCancelListener(this.mIntentCancelListener);
                }
            } else if (bubbleEntry.getBubbleMetadata().getIntent() == null) {
                this.mIntent.unregisterCancelListener(this.mIntentCancelListener);
                this.mIntentActive = false;
                this.mIntent = null;
            }
            this.mDeleteIntent = bubbleEntry.getBubbleMetadata().getDeleteIntent();
        }
        this.mIsClearable = bubbleEntry.mIsClearable;
        this.mShouldSuppressNotificationDot = bubbleEntry.mShouldSuppressNotificationDot;
        this.mShouldSuppressNotificationList = bubbleEntry.mShouldSuppressNotificationList;
        this.mShouldSuppressPeek = bubbleEntry.mShouldSuppressPeek;
    }

    @VisibleForTesting
    public void setSuppressNotification(boolean z) {
        Bubbles.SuppressionChangedListener suppressionChangedListener;
        boolean showInShade = showInShade();
        if (z) {
            this.mFlags |= 2;
        } else {
            this.mFlags &= -3;
        }
        if (showInShade() != showInShade && (suppressionChangedListener = this.mSuppressionListener) != null) {
            ((BubbleController) ((PipController$$ExternalSyntheticLambda1) suppressionChangedListener).f$0).onBubbleNotificationSuppressionChanged(this);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble(BubbleEntry bubbleEntry, Bubbles.SuppressionChangedListener suppressionChangedListener, final Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener, final Executor executor) {
        this.mShowBubbleUpdateDot = true;
        this.mAppUid = -1;
        this.mKey = bubbleEntry.getKey();
        this.mGroupKey = bubbleEntry.mSbn.getGroupKey();
        this.mLocusId = bubbleEntry.mSbn.getNotification().getLocusId();
        this.mSuppressionListener = suppressionChangedListener;
        this.mIntentCancelListener = new PendingIntent.CancelListener() { // from class: com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda0
            public final void onCanceled(PendingIntent pendingIntent) {
                Bubble bubble = Bubble.this;
                Executor executor2 = executor;
                Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener2 = pendingIntentCanceledListener;
                Objects.requireNonNull(bubble);
                PendingIntent pendingIntent2 = bubble.mIntent;
                if (pendingIntent2 != null) {
                    pendingIntent2.unregisterCancelListener(bubble.mIntentCancelListener);
                }
                executor2.execute(new Bubble$$ExternalSyntheticLambda1(bubble, pendingIntentCanceledListener2, 0));
            }
        };
        this.mMainExecutor = executor;
        this.mTaskId = -1;
        setEntry(bubbleEntry);
    }

    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    @VisibleForTesting
    public void setTextChangedForTest(boolean z) {
        this.mIsTextChanged = z;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Bitmap getAppBadge() {
        return this.mBadgeBitmap;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Bitmap getBubbleIcon() {
        return this.mBubbleBitmap;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final int getDotColor() {
        return this.mDotColor;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final Path getDotPath() {
        return this.mDotPath;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final BubbleExpandedView getExpandedView() {
        return this.mExpandedView;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final BadgedImageView getIconView$1() {
        return this.mIconView;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public final String getKey() {
        return this.mKey;
    }
}
