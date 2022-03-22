package com.android.systemui.statusbar.notification.row;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowDragController {
    public final Context mContext;
    public final HeadsUpManager mHeadsUpManager;
    public int mIconSize;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v17 */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v5, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v6, types: [java.lang.StringBuilder] */
    public void startDragAndDrop(View view) {
        ExpandableNotificationRow expandableNotificationRow;
        if (view instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) view;
        } else {
            expandableNotificationRow = null;
        }
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        Notification notification = notificationEntry.mSbn.getNotification();
        PendingIntent pendingIntent = notification.contentIntent;
        if (pendingIntent == null) {
            pendingIntent = notification.fullScreenIntent;
        }
        NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry2);
        Drawable packageName = notificationEntry2.mSbn.getPackageName();
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 795136);
            if (applicationInfo != null) {
                packageName = packageManager.getApplicationIcon(applicationInfo);
            } else {
                Log.d("ExpandableNotificationRowDragController", " application info is null ");
                packageName = packageManager.getDefaultActivityIcon();
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d("ExpandableNotificationRowDragController", "can not find package with : " + packageName);
            packageName = packageManager.getDefaultActivityIcon();
        }
        Bitmap createBitmap = Bitmap.createBitmap(packageName.getIntrinsicWidth(), packageName.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        packageName.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        packageName.draw(canvas);
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageBitmap(createBitmap);
        int i = this.mIconSize;
        imageView.layout(0, 0, i, i);
        ClipDescription clipDescription = new ClipDescription("Drag And Drop", new String[]{"application/vnd.android.activity"});
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.PENDING_INTENT", pendingIntent);
        intent.putExtra("android.intent.extra.USER", Process.myUserHandle());
        ClipData clipData = new ClipData(clipDescription, new ClipData.Item(intent));
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(imageView);
        view.setOnDragListener(new View.OnDragListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController$$ExternalSyntheticLambda0
            /* JADX WARN: Code restructure failed: missing block: B:32:0x0086, code lost:
                if (r9 != false) goto L_0x0088;
             */
            @Override // android.view.View.OnDragListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final boolean onDrag(android.view.View r8, android.view.DragEvent r9) {
                /*
                    r7 = this;
                    com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController r7 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController.this
                    java.util.Objects.requireNonNull(r7)
                    int r0 = r9.getAction()
                    r1 = 1
                    r2 = 0
                    if (r0 == r1) goto L_0x0095
                    r7 = 4
                    if (r0 == r7) goto L_0x0013
                    r1 = r2
                    goto L_0x00b3
                L_0x0013:
                    boolean r7 = r9.getResult()
                    if (r7 == 0) goto L_0x00b3
                    boolean r7 = r8 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
                    if (r7 == 0) goto L_0x00b3
                    com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r8 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r8
                    java.util.Objects.requireNonNull(r8)
                    com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$OnDragSuccessListener r7 = r8.mOnDragSuccessListener
                    if (r7 == 0) goto L_0x00b3
                    com.android.systemui.statusbar.notification.collection.NotificationEntry r8 = r8.mEntry
                    com.android.systemui.statusbar.notification.NotificationClicker$1 r7 = (com.android.systemui.statusbar.notification.NotificationClicker.AnonymousClass1) r7
                    com.android.systemui.statusbar.notification.NotificationClicker r7 = com.android.systemui.statusbar.notification.NotificationClicker.this
                    com.android.systemui.statusbar.notification.NotificationActivityStarter r7 = r7.mNotificationActivityStarter
                    com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter r7 = (com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter) r7
                    java.util.Objects.requireNonNull(r7)
                    com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider r9 = r7.mVisibilityProvider
                    com.android.internal.statusbar.NotificationVisibility r9 = r9.obtain(r8, r1)
                    java.util.Objects.requireNonNull(r8)
                    android.service.notification.StatusBarNotification r0 = r8.mSbn
                    android.app.Notification r0 = r0.getNotification()
                    int r0 = r0.flags
                    r3 = r0 & 16
                    r4 = 16
                    if (r3 == r4) goto L_0x004c
                L_0x004a:
                    r0 = r2
                    goto L_0x0052
                L_0x004c:
                    r0 = r0 & 64
                    if (r0 == 0) goto L_0x0051
                    goto L_0x004a
                L_0x0051:
                    r0 = r1
                L_0x0052:
                    if (r0 == 0) goto L_0x005b
                    com.android.systemui.statusbar.notification.row.OnUserInteractionCallback r3 = r7.mOnUserInteractionCallback
                    com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.getGroupSummaryToDismiss(r8)
                    goto L_0x005c
                L_0x005b:
                    r3 = 0
                L_0x005c:
                    java.lang.String r4 = r8.mKey
                    com.android.systemui.statusbar.NotificationClickNotifier r5 = r7.mClickNotifier
                    java.util.Objects.requireNonNull(r5)
                    com.android.internal.statusbar.IStatusBarService r6 = r5.barService     // Catch: RemoteException -> 0x0068
                    r6.onNotificationClick(r4, r9)     // Catch: RemoteException -> 0x0068
                L_0x0068:
                    java.util.concurrent.Executor r9 = r5.mainExecutor
                    com.android.systemui.statusbar.NotificationClickNotifier$onNotificationClick$1 r6 = new com.android.systemui.statusbar.NotificationClickNotifier$onNotificationClick$1
                    r6.<init>(r5, r4)
                    r9.execute(r6)
                    if (r0 != 0) goto L_0x0088
                    com.android.systemui.statusbar.NotificationRemoteInputManager r9 = r7.mRemoteInputManager
                    java.util.Objects.requireNonNull(r9)
                    com.android.systemui.statusbar.NotificationRemoteInputManager$RemoteInputListener r9 = r9.mRemoteInputListener
                    if (r9 == 0) goto L_0x0085
                    boolean r9 = r9.isNotificationKeptForRemoteInputHistory(r4)
                    if (r9 == 0) goto L_0x0085
                    r9 = r1
                    goto L_0x0086
                L_0x0085:
                    r9 = r2
                L_0x0086:
                    if (r9 == 0) goto L_0x0092
                L_0x0088:
                    android.os.Handler r9 = r7.mMainThreadHandler
                    com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda2 r0 = new com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda2
                    r0.<init>()
                    r9.post(r0)
                L_0x0092:
                    r7.mIsCollapsingToShowActivityOverLockscreen = r2
                    goto L_0x00b3
                L_0x0095:
                    r8.performHapticFeedback(r2)
                    boolean r9 = r8 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
                    if (r9 == 0) goto L_0x00b3
                    com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r8 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r8
                    boolean r8 = r8.mIsPinned
                    if (r8 == 0) goto L_0x00a8
                    com.android.systemui.statusbar.policy.HeadsUpManager r7 = r7.mHeadsUpManager
                    r7.releaseAllImmediately()
                    goto L_0x00b3
                L_0x00a8:
                    java.lang.Class<com.android.systemui.statusbar.phone.ShadeController> r7 = com.android.systemui.statusbar.phone.ShadeController.class
                    java.lang.Object r7 = com.android.systemui.Dependency.get(r7)
                    com.android.systemui.statusbar.phone.ShadeController r7 = (com.android.systemui.statusbar.phone.ShadeController) r7
                    r7.animateCollapsePanels$1(r2)
                L_0x00b3:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController$$ExternalSyntheticLambda0.onDrag(android.view.View, android.view.DragEvent):boolean");
            }
        });
        view.startDragAndDrop(clipData, dragShadowBuilder, null, 256);
    }

    public ExpandableNotificationRowDragController(Context context, HeadsUpManager headsUpManager) {
        this.mContext = context;
        this.mHeadsUpManager = headsUpManager;
        this.mIconSize = context.getResources().getDimensionPixelSize(2131165679);
    }
}
