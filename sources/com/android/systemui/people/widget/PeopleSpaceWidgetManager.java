package com.android.systemui.people.widget;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Person;
import android.app.backup.BackupManager;
import android.app.job.JobScheduler;
import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.app.people.PeopleManager;
import android.app.people.PeopleSpaceTile;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import androidx.recyclerview.R$dimen;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.MessagingMessage;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.people.NotificationHelper;
import com.android.systemui.people.NotificationHelper$$ExternalSyntheticLambda1;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda4;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda8;
import com.android.systemui.people.PeopleTileViewHelper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda4;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda11;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda7;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final class PeopleSpaceWidgetManager {
    @GuardedBy({"mLock"})
    public static HashMap mListeners = new HashMap();
    @GuardedBy({"mLock"})
    public static HashMap mTiles = new HashMap();
    public AppWidgetManager mAppWidgetManager;
    public BackupManager mBackupManager;
    public final AnonymousClass2 mBaseBroadcastReceiver;
    public Executor mBgExecutor;
    public BroadcastDispatcher mBroadcastDispatcher;
    public Optional<Bubbles> mBubblesOptional;
    public final Context mContext;
    public INotificationManager mINotificationManager;
    public IPeopleManager mIPeopleManager;
    public LauncherApps mLauncherApps;
    public final AnonymousClass1 mListener;
    public final Object mLock;
    public PeopleSpaceWidgetManager mManager;
    public CommonNotifCollection mNotifCollection;
    @GuardedBy({"mLock"})
    public HashMap mNotificationKeyToWidgetIdsMatchedByUri;
    public NotificationManager mNotificationManager;
    public PackageManager mPackageManager;
    public PeopleManager mPeopleManager;
    public boolean mRegisteredReceivers;
    public SharedPreferences mSharedPrefs;
    public UiEventLoggerImpl mUiEventLogger;
    public UserManager mUserManager;

    /* renamed from: com.android.systemui.people.widget.PeopleSpaceWidgetManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends BroadcastReceiver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new ExecutorUtils$$ExternalSyntheticLambda0(this, intent, 2));
        }
    }

    /* loaded from: classes.dex */
    public class TileConversationListener implements PeopleManager.ConversationListener {
        public TileConversationListener() {
        }

        public final void onConversationUpdate(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new ExecutorUtils$$ExternalSyntheticLambda1(this, conversationChannel, 2));
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.people.widget.PeopleSpaceWidgetManager$1] */
    public PeopleSpaceWidgetManager(Context context, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        this.mLock = new Object();
        this.mUiEventLogger = new UiEventLoggerImpl();
        this.mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
        this.mListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.1
            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationsInitialized() {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                PeopleSpaceUtils.NotificationAction notificationAction = PeopleSpaceUtils.NotificationAction.POSTED;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                peopleSpaceWidgetManager.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(peopleSpaceWidgetManager, statusBarNotification, notificationAction, 0));
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                PeopleSpaceUtils.NotificationAction notificationAction = PeopleSpaceUtils.NotificationAction.REMOVED;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                peopleSpaceWidgetManager.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(peopleSpaceWidgetManager, statusBarNotification, notificationAction, 0));
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                if (notificationChannel.isConversation()) {
                    PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                    peopleSpaceWidgetManager.mBgExecutor.execute(new ScreenRecordTile$$ExternalSyntheticLambda1(peopleSpaceWidgetManager, peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)), 1));
                }
            }
        };
        this.mBaseBroadcastReceiver = new AnonymousClass2();
        this.mContext = context;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mLauncherApps = launcherApps;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPeopleManager = (PeopleManager) context.getSystemService(PeopleManager.class);
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = new BackupManager(context);
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBgExecutor = executor;
    }

    public final void addNewWidget(final int i, PeopleTileKey peopleTileKey) {
        PeopleTileKey keyFromStorageByWidgetId;
        try {
            PeopleSpaceTile tileFromPersistentStorage = getTileFromPersistentStorage(peopleTileKey, i, false);
            if (tileFromPersistentStorage != null) {
                final PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tileFromPersistentStorage, Optional.of(Integer.valueOf(i)));
                synchronized (this.mLock) {
                    keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
                }
                if (PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
                    deleteWidgets(new int[]{i});
                } else {
                    this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_ADDED);
                }
                synchronized (this.mLock) {
                    PeopleSpaceUtils.setSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, augmentTileFromNotificationEntryManager.getContactUri(), this.mBackupManager);
                }
                registerConversationListenerIfNeeded(peopleTileKey);
                try {
                    this.mLauncherApps.cacheShortcuts(augmentTileFromNotificationEntryManager.getPackageName(), Collections.singletonList(augmentTileFromNotificationEntryManager.getId()), augmentTileFromNotificationEntryManager.getUserHandle(), 2);
                } catch (Exception e) {
                    Log.w("PeopleSpaceWidgetMgr", "Exception caching shortcut:" + e);
                }
                this.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                        int i2 = i;
                        PeopleSpaceTile peopleSpaceTile = augmentTileFromNotificationEntryManager;
                        Objects.requireNonNull(peopleSpaceWidgetManager);
                        peopleSpaceWidgetManager.updateAppWidgetOptionsAndView(i2, peopleSpaceTile);
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot add widget since app was uninstalled");
        }
    }

    public final void deleteWidgets(int[] iArr) {
        PeopleTileKey peopleTileKey;
        HashSet hashSet;
        String string;
        for (int i : iArr) {
            this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_DELETED);
            synchronized (this.mLock) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
                peopleTileKey = new PeopleTileKey(sharedPreferences.getString("shortcut_id", null), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", null));
                if (PeopleTileKey.isValid(peopleTileKey)) {
                    hashSet = new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
                    string = this.mSharedPrefs.getString(String.valueOf(i), null);
                } else {
                    return;
                }
            }
            synchronized (this.mLock) {
                PeopleSpaceUtils.removeSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, string);
            }
            if (hashSet.contains(String.valueOf(i)) && hashSet.size() == 1) {
                synchronized (mListeners) {
                    TileConversationListener tileConversationListener = (TileConversationListener) mListeners.get(peopleTileKey);
                    if (tileConversationListener != null) {
                        mListeners.remove(peopleTileKey);
                        this.mPeopleManager.unregisterConversationListener(tileConversationListener);
                    }
                }
                try {
                    this.mLauncherApps.uncacheShortcuts(peopleTileKey.mPackageName, Collections.singletonList(peopleTileKey.mShortcutId), UserHandle.of(peopleTileKey.mUserId), 2);
                } catch (Exception e) {
                    Log.d("PeopleSpaceWidgetMgr", "Exception uncaching shortcut:" + e);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.widget.RemoteViews getPreview(java.lang.String r4, android.os.UserHandle r5, java.lang.String r6, android.os.Bundle r7) {
        /*
            r3 = this;
            r0 = 0
            android.app.people.IPeopleManager r1 = r3.mIPeopleManager     // Catch: Exception -> 0x0051
            int r5 = r5.getIdentifier()     // Catch: Exception -> 0x0051
            android.app.people.ConversationChannel r4 = r1.getConversation(r6, r5, r4)     // Catch: Exception -> 0x0051
            android.content.pm.LauncherApps r5 = r3.mLauncherApps     // Catch: Exception -> 0x0051
            com.android.systemui.people.widget.PeopleTileKey r6 = com.android.systemui.people.PeopleSpaceUtils.EMPTY_KEY     // Catch: Exception -> 0x0051
            java.lang.String r6 = "PeopleSpaceUtils"
            r1 = 0
            if (r4 != 0) goto L_0x001a
            java.lang.String r4 = "ConversationChannel is null"
            android.util.Log.i(r6, r4)     // Catch: Exception -> 0x0051
            goto L_0x0039
        L_0x001a:
            android.app.people.PeopleSpaceTile$Builder r2 = new android.app.people.PeopleSpaceTile$Builder     // Catch: Exception -> 0x0051
            r2.<init>(r4, r5)     // Catch: Exception -> 0x0051
            android.app.people.PeopleSpaceTile r4 = r2.build()     // Catch: Exception -> 0x0051
            if (r4 == 0) goto L_0x0031
            java.lang.CharSequence r5 = r4.getUserName()     // Catch: Exception -> 0x0051
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch: Exception -> 0x0051
            if (r5 != 0) goto L_0x0031
            r5 = 1
            goto L_0x0032
        L_0x0031:
            r5 = r1
        L_0x0032:
            if (r5 != 0) goto L_0x003a
            java.lang.String r4 = "PeopleSpaceTile is not valid"
            android.util.Log.i(r6, r4)     // Catch: Exception -> 0x0051
        L_0x0039:
            r4 = r0
        L_0x003a:
            if (r4 != 0) goto L_0x003d
            return r0
        L_0x003d:
            java.util.Optional r5 = java.util.Optional.empty()
            android.app.people.PeopleSpaceTile r4 = r3.augmentTileFromNotificationEntryManager(r4, r5)
            android.content.Context r3 = r3.mContext
            com.android.systemui.people.widget.PeopleTileKey r5 = new com.android.systemui.people.widget.PeopleTileKey
            r5.<init>(r4)
            android.widget.RemoteViews r3 = com.android.systemui.people.PeopleTileViewHelper.createRemoteViews(r3, r4, r1, r7, r5)
            return r3
        L_0x0051:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Exception getting tiles: "
            r4.append(r5)
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            java.lang.String r4 = "PeopleSpaceWidgetMgr"
            android.util.Log.w(r4, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getPreview(java.lang.String, android.os.UserHandle, java.lang.String, android.os.Bundle):android.widget.RemoteViews");
    }

    public final PeopleSpaceTile augmentTileFromNotificationEntryManager(PeopleSpaceTile peopleSpaceTile, Optional<Integer> optional) {
        String str;
        PeopleTileKey peopleTileKey = new PeopleTileKey(peopleSpaceTile);
        Map<PeopleTileKey, Set<NotificationEntry>> groupedConversationNotifications = getGroupedConversationNotifications();
        if (peopleSpaceTile.getContactUri() != null) {
            str = peopleSpaceTile.getContactUri().toString();
        } else {
            str = null;
        }
        return augmentTileFromNotifications(peopleSpaceTile, peopleTileKey, str, groupedConversationNotifications, optional);
    }

    public final PeopleSpaceTile augmentTileFromNotifications(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, final String str, Map<PeopleTileKey, Set<NotificationEntry>> map, Optional<Integer> optional) {
        boolean z;
        NotificationEntry notificationEntry;
        Notification.MessagingStyle.Message message;
        CharSequence charSequence;
        Uri uri;
        Person senderPerson;
        List<Notification.MessagingStyle.Message> messagingStyleMessages;
        List list;
        boolean z2 = true;
        if (this.mPackageManager.checkPermission("android.permission.READ_CONTACTS", peopleSpaceTile.getPackageName()) == 0) {
            z = true;
        } else {
            z = false;
        }
        List arrayList = new ArrayList();
        if (z) {
            final PackageManager packageManager = this.mPackageManager;
            PeopleTileKey peopleTileKey2 = PeopleSpaceUtils.EMPTY_KEY;
            if (TextUtils.isEmpty(str)) {
                list = new ArrayList();
            } else {
                list = (List) map.entrySet().stream().flatMap(PeopleSpaceUtils$$ExternalSyntheticLambda4.INSTANCE).filter(new Predicate() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda5
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean z3;
                        boolean z4;
                        PackageManager packageManager2 = packageManager;
                        String str2 = str;
                        NotificationEntry notificationEntry2 = (NotificationEntry) obj;
                        Objects.requireNonNull(notificationEntry2);
                        if (packageManager2.checkPermission("android.permission.READ_CONTACTS", notificationEntry2.mSbn.getPackageName()) == 0) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (!z3) {
                            return false;
                        }
                        Notification notification = notificationEntry2.mSbn.getNotification();
                        if (notification == null) {
                            z4 = false;
                        } else {
                            z4 = NotificationHelper.isMissedCall(notification);
                        }
                        if (!z4 || !Objects.equals(str2, NotificationHelper.getContactUri(notificationEntry2.mSbn))) {
                            return false;
                        }
                        return true;
                    }
                }).collect(Collectors.toList());
            }
            arrayList = list;
            arrayList.isEmpty();
        }
        Set<NotificationEntry> set = map.get(peopleTileKey);
        if (set == null) {
            set = new HashSet<>();
        }
        if (set.isEmpty() && arrayList.isEmpty()) {
            return PeopleSpaceUtils.removeNotificationFields(peopleSpaceTile);
        }
        set.addAll(arrayList);
        PeopleTileKey peopleTileKey3 = PeopleSpaceUtils.EMPTY_KEY;
        int i = 0;
        for (NotificationEntry notificationEntry2 : set) {
            Objects.requireNonNull(notificationEntry2);
            Notification notification = notificationEntry2.mSbn.getNotification();
            if (!NotificationHelper.isMissedCall(notification) && (messagingStyleMessages = NotificationHelper.getMessagingStyleMessages(notification)) != null) {
                i += messagingStyleMessages.size();
            }
        }
        CharSequence charSequence2 = null;
        if (set.isEmpty()) {
            notificationEntry = null;
        } else {
            notificationEntry = set.stream().filter(NotificationHelper$$ExternalSyntheticLambda1.INSTANCE).sorted(NotificationHelper.notificationEntryComparator).findFirst().orElse(null);
        }
        Context context = this.mContext;
        BackupManager backupManager = this.mBackupManager;
        if (notificationEntry == null || notificationEntry.mSbn.getNotification() == null) {
            return PeopleSpaceUtils.removeNotificationFields(peopleSpaceTile);
        }
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        Notification notification2 = statusBarNotification.getNotification();
        PeopleSpaceTile.Builder builder = peopleSpaceTile.toBuilder();
        String contactUri = NotificationHelper.getContactUri(statusBarNotification);
        if (optional.isPresent() && peopleSpaceTile.getContactUri() == null && !TextUtils.isEmpty(contactUri)) {
            Uri parse = Uri.parse(contactUri);
            PeopleSpaceUtils.setSharedPreferencesStorageForTile(context, new PeopleTileKey(peopleSpaceTile), optional.get().intValue(), parse, backupManager);
            builder.setContactUri(parse);
        }
        boolean isMissedCall = NotificationHelper.isMissedCall(notification2);
        List<Notification.MessagingStyle.Message> messagingStyleMessages2 = NotificationHelper.getMessagingStyleMessages(notification2);
        if (!isMissedCall && ArrayUtils.isEmpty(messagingStyleMessages2)) {
            return PeopleSpaceUtils.removeNotificationFields(builder.build());
        }
        if (messagingStyleMessages2 != null) {
            message = messagingStyleMessages2.get(0);
        } else {
            message = null;
        }
        if (message == null || TextUtils.isEmpty(message.getText())) {
            z2 = false;
        }
        if (!isMissedCall || z2) {
            charSequence = message.getText();
        } else {
            charSequence = context.getString(2131952762);
        }
        if (message == null || !MessagingMessage.hasImage(message)) {
            uri = null;
        } else {
            uri = message.getDataUri();
        }
        if (notification2.extras.getBoolean("android.isGroupConversation", false) && (senderPerson = message.getSenderPerson()) != null) {
            charSequence2 = senderPerson.getName();
        }
        return builder.setLastInteractionTimestamp(statusBarNotification.getPostTime()).setNotificationKey(statusBarNotification.getKey()).setNotificationCategory(notification2.category).setNotificationContent(charSequence).setNotificationSender(charSequence2).setNotificationDataUri(uri).setMessagesCount(i).build();
    }

    public final Map<PeopleTileKey, Set<NotificationEntry>> getGroupedConversationNotifications() {
        return (Map) this.mNotifCollection.getAllNotifs().stream().filter(new Predicate() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda7
            /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
            @Override // java.util.function.Predicate
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final boolean test(java.lang.Object r4) {
                /*
                    r3 = this;
                    com.android.systemui.people.widget.PeopleSpaceWidgetManager r3 = com.android.systemui.people.widget.PeopleSpaceWidgetManager.this
                    com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r4
                    java.util.Objects.requireNonNull(r3)
                    r0 = 1
                    r1 = 0
                    if (r4 == 0) goto L_0x001f
                    android.service.notification.NotificationListenerService$Ranking r2 = r4.mRanking
                    if (r2 == 0) goto L_0x001f
                    android.content.pm.ShortcutInfo r2 = r2.getConversationShortcutInfo()
                    if (r2 == 0) goto L_0x001f
                    android.service.notification.StatusBarNotification r2 = r4.mSbn
                    android.app.Notification r2 = r2.getNotification()
                    if (r2 == 0) goto L_0x001f
                    r2 = r0
                    goto L_0x0020
                L_0x001f:
                    r2 = r1
                L_0x0020:
                    if (r2 == 0) goto L_0x0064
                    boolean r2 = com.android.systemui.people.NotificationHelper.isMissedCallOrHasContent(r4)
                    if (r2 == 0) goto L_0x0064
                    java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r3 = r3.mBubblesOptional
                    boolean r2 = r3.isPresent()     // Catch: Exception -> 0x0049
                    if (r2 == 0) goto L_0x0060
                    java.lang.Object r3 = r3.get()     // Catch: Exception -> 0x0049
                    com.android.wm.shell.bubbles.Bubbles r3 = (com.android.wm.shell.bubbles.Bubbles) r3     // Catch: Exception -> 0x0049
                    java.util.Objects.requireNonNull(r4)     // Catch: Exception -> 0x0049
                    java.lang.String r2 = r4.mKey     // Catch: Exception -> 0x0049
                    android.service.notification.StatusBarNotification r4 = r4.mSbn     // Catch: Exception -> 0x0049
                    java.lang.String r4 = r4.getGroupKey()     // Catch: Exception -> 0x0049
                    boolean r3 = r3.isBubbleNotificationSuppressedFromShade(r2, r4)     // Catch: Exception -> 0x0049
                    if (r3 == 0) goto L_0x0060
                    r3 = r0
                    goto L_0x0061
                L_0x0049:
                    r3 = move-exception
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder
                    r4.<init>()
                    java.lang.String r2 = "Exception checking if notification is suppressed: "
                    r4.append(r2)
                    r4.append(r3)
                    java.lang.String r3 = r4.toString()
                    java.lang.String r4 = "PeopleNotifHelper"
                    android.util.Log.e(r4, r3)
                L_0x0060:
                    r3 = r1
                L_0x0061:
                    if (r3 != 0) goto L_0x0064
                    goto L_0x0065
                L_0x0064:
                    r0 = r1
                L_0x0065:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda7.test(java.lang.Object):boolean");
            }
        }).collect(Collectors.groupingBy(PeopleSpaceWidgetManager$$ExternalSyntheticLambda6.INSTANCE, Collectors.mapping(Function.identity(), Collectors.toSet())));
    }

    public final PeopleTileKey getKeyFromStorageByWidgetId(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", ""));
    }

    public final Set<String> getMatchingUriWidgetIds(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        boolean z;
        String contactUri;
        if (notificationAction.equals(PeopleSpaceUtils.NotificationAction.POSTED)) {
            Notification notification = statusBarNotification.getNotification();
            if (notification == null) {
                z = false;
            } else {
                z = NotificationHelper.isMissedCall(notification);
            }
            HashSet hashSet = null;
            if (z && (contactUri = NotificationHelper.getContactUri(statusBarNotification)) != null) {
                HashSet hashSet2 = new HashSet(this.mSharedPrefs.getStringSet(contactUri, new HashSet()));
                if (!hashSet2.isEmpty()) {
                    hashSet = hashSet2;
                }
            }
            if (hashSet != null && !hashSet.isEmpty()) {
                this.mNotificationKeyToWidgetIdsMatchedByUri.put(statusBarNotification.getKey(), hashSet);
                return hashSet;
            }
        } else {
            Set<String> set = (Set) this.mNotificationKeyToWidgetIdsMatchedByUri.remove(statusBarNotification.getKey());
            if (set != null && !set.isEmpty()) {
                return set;
            }
        }
        return new HashSet();
    }

    public final int getNotificationPolicyState() {
        int currentInterruptionFilter;
        NotificationManager.Policy notificationPolicy = this.mNotificationManager.getNotificationPolicy();
        int i = 0;
        if (!NotificationManager.Policy.areAllVisualEffectsSuppressed(notificationPolicy.suppressedVisualEffects) || (currentInterruptionFilter = this.mNotificationManager.getCurrentInterruptionFilter()) == 1) {
            return 1;
        }
        if (currentInterruptionFilter == 2) {
            if (notificationPolicy.allowConversations()) {
                int i2 = notificationPolicy.priorityConversationSenders;
                if (i2 == 1) {
                    return 1;
                }
                if (i2 == 2) {
                    i = 4;
                }
            }
            if (notificationPolicy.allowMessages()) {
                int allowMessagesFrom = notificationPolicy.allowMessagesFrom();
                if (allowMessagesFrom == 1) {
                    return i | 16;
                }
                if (allowMessagesFrom != 2) {
                    return 1;
                }
                return i | 8;
            } else if (i != 0) {
                return i;
            }
        }
        return 2;
    }

    public final List<PeopleSpaceTile> getRecentTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, Stream.concat(this.mINotificationManager.getConversations(false).getList().stream().filter(PeopleSpaceUtils$$ExternalSyntheticLambda8.INSTANCE$1).map(WifiPickerTracker$$ExternalSyntheticLambda7.INSTANCE$1), this.mIPeopleManager.getRecentConversations().getList().stream().map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda5.INSTANCE)));
    }

    public final PeopleSpaceTile getTileForExistingWidgetThrowing(int i) throws PackageManager.NameNotFoundException {
        PeopleSpaceTile peopleSpaceTile;
        synchronized (mTiles) {
            peopleSpaceTile = (PeopleSpaceTile) mTiles.get(Integer.valueOf(i));
        }
        if (peopleSpaceTile != null) {
            return peopleSpaceTile;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return getTileFromPersistentStorage(new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", "")), i, true);
    }

    public final void updateAppWidgetOptionsAndView(int i, PeopleSpaceTile peopleSpaceTile) {
        synchronized (mTiles) {
            mTiles.put(Integer.valueOf(i), peopleSpaceTile);
        }
        Bundle appWidgetOptions = this.mAppWidgetManager.getAppWidgetOptions(i);
        PeopleTileKey keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
        if (!PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot update invalid widget");
            return;
        }
        this.mAppWidgetManager.updateAppWidget(i, PeopleTileViewHelper.createRemoteViews(this.mContext, peopleSpaceTile, i, appWidgetOptions, keyFromStorageByWidgetId));
    }

    public final void updateSingleConversationWidgets(final int[] iArr) {
        final HashMap hashMap = new HashMap();
        for (int i : iArr) {
            PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
            if (tileForExistingWidget == null) {
                Log.e("PeopleSpaceWidgetMgr", "Matching conversation not found for shortcut ID");
            }
            updateAppWidgetOptionsAndView(i, tileForExistingWidget);
            hashMap.put(Integer.valueOf(i), tileForExistingWidget);
            if (tileForExistingWidget != null) {
                registerConversationListenerIfNeeded(new PeopleTileKey(tileForExistingWidget));
            }
        }
        final Context context = this.mContext;
        final PeopleSpaceWidgetManager peopleSpaceWidgetManager = this.mManager;
        PeopleTileKey peopleTileKey = PeopleSpaceUtils.EMPTY_KEY;
        R$dimen.postOnBackgroundThread(new Runnable() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PeopleSpaceUtils.getDataFromContacts(context, peopleSpaceWidgetManager, hashMap, iArr);
            }
        });
    }

    @VisibleForTesting
    public void updateWidgetsFromBroadcastInBackground(String str) {
        int[] appWidgetIds = this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class));
        if (appWidgetIds != null) {
            for (int i : appWidgetIds) {
                try {
                    synchronized (this.mLock) {
                        PeopleSpaceTile tileForExistingWidgetThrowing = getTileForExistingWidgetThrowing(i);
                        if (tileForExistingWidgetThrowing == null) {
                            Log.e("PeopleSpaceWidgetMgr", "Matching conversation not found for shortcut ID");
                        } else {
                            updateAppWidgetOptionsAndView(i, getTileWithCurrentState(tileForExistingWidgetThrowing, str));
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("PeopleSpaceWidgetMgr", "Package no longer found for tile: " + e);
                    JobScheduler jobScheduler = (JobScheduler) this.mContext.getSystemService(JobScheduler.class);
                    if (jobScheduler == null || jobScheduler.getPendingJob(74823873) == null) {
                        synchronized (this.mLock) {
                            updateAppWidgetOptionsAndView(i, null);
                            deleteWidgets(new int[]{i});
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    public static Set getNewWidgets(Set set, HashMap hashMap) {
        return (Set) set.stream().map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda3(hashMap, 0)).filter(Monitor$$ExternalSyntheticLambda4.INSTANCE$1).collect(Collectors.toSet());
    }

    public final HashSet getMatchingKeyWidgetIds(PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return new HashSet();
        }
        return new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
    }

    public final boolean getPackageSuspended(PeopleSpaceTile peopleSpaceTile) throws PackageManager.NameNotFoundException {
        boolean z;
        if (TextUtils.isEmpty(peopleSpaceTile.getPackageName()) || !this.mPackageManager.isPackageSuspended(peopleSpaceTile.getPackageName())) {
            z = false;
        } else {
            z = true;
        }
        PackageManager packageManager = this.mPackageManager;
        String packageName = peopleSpaceTile.getPackageName();
        PeopleTileKey peopleTileKey = PeopleSpaceUtils.EMPTY_KEY;
        packageManager.getApplicationInfoAsUser(packageName, 128, peopleSpaceTile.getUserHandle().getIdentifier());
        return z;
    }

    public final PeopleSpaceTile getTileForExistingWidget(int i) {
        try {
            return getTileForExistingWidgetThrowing(i);
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "Failed to retrieve conversation for tile: " + e);
            return null;
        }
    }

    public final PeopleSpaceTile getTileFromPersistentStorage(PeopleTileKey peopleTileKey, int i, boolean z) throws PackageManager.NameNotFoundException {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("PeopleTileKey invalid: ");
            m.append(peopleTileKey.toString());
            Log.e("PeopleSpaceWidgetMgr", m.toString());
            return null;
        }
        IPeopleManager iPeopleManager = this.mIPeopleManager;
        if (iPeopleManager == null || this.mLauncherApps == null) {
            Log.d("PeopleSpaceWidgetMgr", "System services are null");
            return null;
        }
        try {
            Objects.requireNonNull(peopleTileKey);
            ConversationChannel conversation = iPeopleManager.getConversation(peopleTileKey.mPackageName, peopleTileKey.mUserId, peopleTileKey.mShortcutId);
            if (conversation == null) {
                return null;
            }
            PeopleSpaceTile.Builder builder = new PeopleSpaceTile.Builder(conversation, this.mLauncherApps);
            String string = this.mSharedPrefs.getString(String.valueOf(i), null);
            if (z && string != null && builder.build().getContactUri() == null) {
                builder.setContactUri(Uri.parse(string));
            }
            return getTileWithCurrentState(builder.build(), "android.intent.action.BOOT_COMPLETED");
        } catch (RemoteException e) {
            Log.e("PeopleSpaceWidgetMgr", "Could not retrieve data: " + e);
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final PeopleSpaceTile getTileWithCurrentState(PeopleSpaceTile peopleSpaceTile, String str) throws PackageManager.NameNotFoundException {
        char c;
        PeopleSpaceTile.Builder builder = peopleSpaceTile.toBuilder();
        boolean z = false;
        switch (str.hashCode()) {
            case -1238404651:
                if (str.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1001645458:
                if (str.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -864107122:
                if (str.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -19011148:
                if (str.equals("android.intent.action.LOCALE_CHANGED")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 798292259:
                if (str.equals("android.intent.action.BOOT_COMPLETED")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 833559602:
                if (str.equals("android.intent.action.USER_UNLOCKED")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1290767157:
                if (str.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2106958107:
                if (str.equals("android.app.action.INTERRUPTION_FILTER_CHANGED")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                builder.setNotificationPolicyState(getNotificationPolicyState());
                break;
            case 1:
            case 2:
                builder.setIsPackageSuspended(getPackageSuspended(peopleSpaceTile));
                break;
            case 3:
            case 4:
            case 5:
                if (peopleSpaceTile.getUserHandle() != null && this.mUserManager.isQuietModeEnabled(peopleSpaceTile.getUserHandle())) {
                    z = true;
                }
                builder.setIsUserQuieted(z);
                break;
            case FalsingManager.VERSION /* 6 */:
                break;
            default:
                if (peopleSpaceTile.getUserHandle() != null && this.mUserManager.isQuietModeEnabled(peopleSpaceTile.getUserHandle())) {
                    z = true;
                }
                builder.setIsUserQuieted(z).setIsPackageSuspended(getPackageSuspended(peopleSpaceTile)).setNotificationPolicyState(getNotificationPolicyState());
                break;
        }
        return builder.build();
    }

    public final void registerConversationListenerIfNeeded(PeopleTileKey peopleTileKey) {
        if (PeopleTileKey.isValid(peopleTileKey)) {
            TileConversationListener tileConversationListener = new TileConversationListener();
            synchronized (mListeners) {
                if (!mListeners.containsKey(peopleTileKey)) {
                    mListeners.put(peopleTileKey, tileConversationListener);
                    PeopleManager peopleManager = this.mPeopleManager;
                    Objects.requireNonNull(peopleTileKey);
                    peopleManager.registerConversationListener(peopleTileKey.mPackageName, peopleTileKey.mUserId, peopleTileKey.mShortcutId, tileConversationListener, this.mContext.getMainExecutor());
                }
            }
        }
    }

    public final void updateStorageAndViewWithConversationData(ConversationChannel conversationChannel, int i) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget != null) {
            PeopleSpaceTile.Builder builder = tileForExistingWidget.toBuilder();
            ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
            Uri uri = null;
            if (shortcutInfo.getPersons() != null && shortcutInfo.getPersons().length > 0) {
                Person person = shortcutInfo.getPersons()[0];
                if (person.getUri() != null) {
                    uri = Uri.parse(person.getUri());
                }
            }
            CharSequence label = shortcutInfo.getLabel();
            if (label != null) {
                builder.setUserName(label);
            }
            Icon convertDrawableToIcon = PeopleSpaceTile.convertDrawableToIcon(this.mLauncherApps.getShortcutIconDrawable(shortcutInfo, 0));
            if (convertDrawableToIcon != null) {
                builder.setUserIcon(convertDrawableToIcon);
            }
            NotificationChannel notificationChannel = conversationChannel.getNotificationChannel();
            if (notificationChannel != null) {
                builder.setIsImportantConversation(notificationChannel.isImportantConversation());
            }
            builder.setContactUri(uri).setStatuses(conversationChannel.getStatuses()).setLastInteractionTimestamp(conversationChannel.getLastEventTimestamp());
            updateAppWidgetOptionsAndView(i, builder.build());
        }
    }

    public final void updateWidgetIdsBasedOnNotifications(HashSet hashSet) {
        if (!hashSet.isEmpty()) {
            try {
                final Map<PeopleTileKey, Set<NotificationEntry>> groupedConversationNotifications = getGroupedConversationNotifications();
                ((Map) hashSet.stream().map(WifiPickerTracker$$ExternalSyntheticLambda11.INSTANCE$1).collect(Collectors.toMap(Function.identity(), new Function() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda4
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                        Map<PeopleTileKey, Set<NotificationEntry>> map = groupedConversationNotifications;
                        Objects.requireNonNull(peopleSpaceWidgetManager);
                        int intValue = ((Integer) obj).intValue();
                        PeopleSpaceTile tileForExistingWidget = peopleSpaceWidgetManager.getTileForExistingWidget(intValue);
                        if (tileForExistingWidget == null) {
                            return Optional.empty();
                        }
                        return Optional.ofNullable(peopleSpaceWidgetManager.augmentTileFromNotifications(tileForExistingWidget, new PeopleTileKey(tileForExistingWidget), peopleSpaceWidgetManager.mSharedPrefs.getString(String.valueOf(intValue), null), map, Optional.of(Integer.valueOf(intValue))));
                    }
                }))).forEach(new BiConsumer() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda2
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                        Optional optional = (Optional) obj2;
                        Objects.requireNonNull(peopleSpaceWidgetManager);
                        int intValue = ((Integer) obj).intValue();
                        if (optional.isPresent()) {
                            peopleSpaceWidgetManager.updateAppWidgetOptionsAndView(intValue, (PeopleSpaceTile) optional.get());
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("PeopleSpaceWidgetMgr", "Exception updating widgets: " + e);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.people.widget.PeopleSpaceWidgetManager$1] */
    @VisibleForTesting
    public PeopleSpaceWidgetManager(Context context, AppWidgetManager appWidgetManager, IPeopleManager iPeopleManager, PeopleManager peopleManager, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, BackupManager backupManager, INotificationManager iNotificationManager, NotificationManager notificationManager, Executor executor) {
        this.mLock = new Object();
        this.mUiEventLogger = new UiEventLoggerImpl();
        this.mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
        this.mListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.1
            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationsInitialized() {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                PeopleSpaceUtils.NotificationAction notificationAction = PeopleSpaceUtils.NotificationAction.POSTED;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                peopleSpaceWidgetManager.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(peopleSpaceWidgetManager, statusBarNotification, notificationAction, 0));
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                PeopleSpaceUtils.NotificationAction notificationAction = PeopleSpaceUtils.NotificationAction.REMOVED;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                peopleSpaceWidgetManager.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(peopleSpaceWidgetManager, statusBarNotification, notificationAction, 0));
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public final void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                if (notificationChannel.isConversation()) {
                    PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                    peopleSpaceWidgetManager.mBgExecutor.execute(new ScreenRecordTile$$ExternalSyntheticLambda1(peopleSpaceWidgetManager, peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)), 1));
                }
            }
        };
        this.mBaseBroadcastReceiver = new AnonymousClass2();
        this.mContext = context;
        this.mAppWidgetManager = appWidgetManager;
        this.mIPeopleManager = iPeopleManager;
        this.mPeopleManager = peopleManager;
        this.mLauncherApps = launcherApps;
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = backupManager;
        this.mINotificationManager = iNotificationManager;
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mBgExecutor = executor;
    }
}
