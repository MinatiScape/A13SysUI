package com.google.android.systemui.columbus;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.settings.UserTracker;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import com.google.android.systemui.columbus.ColumbusTargetRequestService;
import java.security.MessageDigest;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.json.JSONObject;
/* compiled from: ColumbusTargetRequestService.kt */
/* loaded from: classes.dex */
public final class ColumbusTargetRequestService extends Service {
    public static final long PACKAGE_DENY_COOLDOWN_MS = TimeUnit.DAYS.toMillis(5);
    public final Set<String> allowCertList;
    public final Set<String> allowPackageList;
    public final ColumbusContext columbusContext;
    public final ColumbusSettings columbusSettings;
    public final ColumbusStructuredDataManager columbusStructuredDataManager;
    public LauncherApps launcherApps;
    public final Handler mainHandler;
    public final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    public final Messenger messenger;
    public final Context sysUIContext;
    public final UiEventLogger uiEventLogger;
    public final UserTracker userTracker;

    /* compiled from: ColumbusTargetRequestService.kt */
    /* loaded from: classes.dex */
    public final class IncomingMessageHandler extends Handler {
        public static final /* synthetic */ int $r8$clinit = 0;

        public IncomingMessageHandler(Looper looper) {
            super(looper);
        }

        public static void replyToMessenger(Messenger messenger, int i, int i2) {
            if (messenger != null) {
                Message what = Message.obtain().setWhat(i2);
                what.arg1 = i;
                messenger.send(what);
            }
        }

        public final LauncherActivityInfo getAppInfoForPackage(String str) {
            List<LauncherActivityInfo> activityList;
            PendingIntent pendingIntent;
            ColumbusTargetRequestService columbusTargetRequestService = ColumbusTargetRequestService.this;
            LauncherApps launcherApps = columbusTargetRequestService.launcherApps;
            Object obj = null;
            if (launcherApps == null || (activityList = launcherApps.getActivityList(str, columbusTargetRequestService.userTracker.getUserHandle())) == null) {
                return null;
            }
            ColumbusTargetRequestService columbusTargetRequestService2 = ColumbusTargetRequestService.this;
            Iterator<T> it = activityList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                LauncherActivityInfo launcherActivityInfo = (LauncherActivityInfo) next;
                boolean z = false;
                try {
                    LauncherApps launcherApps2 = columbusTargetRequestService2.launcherApps;
                    if (launcherApps2 == null) {
                        pendingIntent = null;
                    } else {
                        pendingIntent = launcherApps2.getMainActivityLaunchIntent(launcherActivityInfo.getComponentName(), null, columbusTargetRequestService2.userTracker.getUserHandle());
                    }
                    if (pendingIntent != null) {
                        z = true;
                        continue;
                    } else {
                        continue;
                    }
                } catch (RuntimeException unused) {
                }
                if (z) {
                    obj = next;
                    break;
                }
            }
            return (LauncherActivityInfo) obj;
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            String str;
            boolean z;
            String[] packagesForUid = ColumbusTargetRequestService.this.getPackageManager().getPackagesForUid(message.sendingUid);
            boolean z2 = false;
            if (packagesForUid == null) {
                str = null;
            } else {
                str = packagesForUid[0];
            }
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    Log.w("Columbus/TargetRequest", Intrinsics.stringPlus("Invalid request type: ", Integer.valueOf(i)));
                    return;
                }
                if (str != null && !packageIsNotAllowed(str)) {
                    if (ColumbusTargetRequestService.this.columbusStructuredDataManager.getPackageShownCount(str) >= 3) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z && getAppInfoForPackage(str) != null) {
                        if (packageIsTarget(str)) {
                            replyToMessenger(message.replyTo, message.what, 0);
                            return;
                        } else if (packageNeedsToCoolDown(str)) {
                            replyToMessenger(message.replyTo, message.what, 3);
                            return;
                        } else {
                            replyToMessenger(message.replyTo, message.what, 1);
                            return;
                        }
                    }
                }
                replyToMessenger(message.replyTo, message.what, 2);
            } else if (str == null || packageIsNotAllowed(str)) {
                replyToMessenger(message.replyTo, message.what, 1);
                Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Unsupported caller: ", str));
            } else if (packageIsTarget(str)) {
                replyToMessenger(message.replyTo, message.what, 0);
                Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Caller already target: ", str));
            } else if (packageNeedsToCoolDown(str)) {
                replyToMessenger(message.replyTo, message.what, 2);
                Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Caller throttled: ", str));
            } else {
                if (ColumbusTargetRequestService.this.columbusStructuredDataManager.getPackageShownCount(str) >= 3) {
                    z2 = true;
                }
                if (z2) {
                    replyToMessenger(message.replyTo, message.what, 3);
                    Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Caller already shown max times: ", str));
                    return;
                }
                final LauncherActivityInfo appInfoForPackage = getAppInfoForPackage(str);
                if (appInfoForPackage == null) {
                    replyToMessenger(message.replyTo, message.what, 4);
                    Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Caller not launchable: ", str));
                    return;
                }
                final Messenger messenger = message.replyTo;
                final int i2 = message.what;
                final ColumbusTargetRequestService columbusTargetRequestService = ColumbusTargetRequestService.this;
                columbusTargetRequestService.mainHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        final int packageShownCount = ColumbusTargetRequestService.this.columbusStructuredDataManager.getPackageShownCount(appInfoForPackage.getComponentName().getPackageName());
                        int i3 = 0;
                        ColumbusTargetRequestService.this.uiEventLogger.log(ColumbusEvent.COLUMBUS_RETARGET_DIALOG_SHOWN, 0, appInfoForPackage.getComponentName().getPackageName());
                        ColumbusTargetRequestDialog columbusTargetRequestDialog = new ColumbusTargetRequestDialog(ColumbusTargetRequestService.this.sysUIContext);
                        columbusTargetRequestDialog.show();
                        final LauncherActivityInfo launcherActivityInfo = appInfoForPackage;
                        final ColumbusTargetRequestService columbusTargetRequestService2 = ColumbusTargetRequestService.this;
                        final ColumbusTargetRequestService.IncomingMessageHandler incomingMessageHandler = this;
                        final Messenger messenger2 = messenger;
                        final int i4 = i2;
                        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.google.android.systemui.columbus.ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                long currentTimeMillis;
                                ColumbusEvent columbusEvent;
                                ColumbusEvent columbusEvent2;
                                if (i5 == -2) {
                                    ColumbusStructuredDataManager columbusStructuredDataManager = ColumbusTargetRequestService.this.columbusStructuredDataManager;
                                    String packageName = launcherActivityInfo.getComponentName().getPackageName();
                                    Objects.requireNonNull(columbusStructuredDataManager);
                                    synchronized (columbusStructuredDataManager.lock) {
                                        try {
                                            currentTimeMillis = SystemClock.currentNetworkTimeMillis();
                                        } catch (DateTimeException unused) {
                                            currentTimeMillis = System.currentTimeMillis();
                                        }
                                        int length = columbusStructuredDataManager.packageStats.length();
                                        int i6 = 0;
                                        while (true) {
                                            if (i6 >= length) {
                                                columbusStructuredDataManager.packageStats.put(ColumbusStructuredDataManager.makeJSONObject$default(columbusStructuredDataManager, packageName, 0, currentTimeMillis, 2));
                                                columbusStructuredDataManager.storePackageStats();
                                                break;
                                            }
                                            int i7 = i6 + 1;
                                            JSONObject jSONObject = columbusStructuredDataManager.packageStats.getJSONObject(i6);
                                            if (Intrinsics.areEqual(packageName, jSONObject.getString(ResourceEntry.KEY_PACKAGE_NAME))) {
                                                jSONObject.put("lastDeny", currentTimeMillis);
                                                columbusStructuredDataManager.packageStats.put(i6, jSONObject);
                                                columbusStructuredDataManager.storePackageStats();
                                                break;
                                            }
                                            i6 = i7;
                                        }
                                    }
                                    ColumbusTargetRequestService.IncomingMessageHandler incomingMessageHandler2 = incomingMessageHandler;
                                    Messenger messenger3 = messenger2;
                                    int i8 = i4;
                                    int i9 = ColumbusTargetRequestService.IncomingMessageHandler.$r8$clinit;
                                    Objects.requireNonNull(incomingMessageHandler2);
                                    ColumbusTargetRequestService.IncomingMessageHandler.replyToMessenger(messenger3, i8, 5);
                                    Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Target change denied by user: ", launcherActivityInfo.getComponentName().flattenToString()));
                                    if (packageShownCount == 0) {
                                        columbusEvent = ColumbusEvent.COLUMBUS_RETARGET_NOT_APPROVED;
                                    } else {
                                        columbusEvent = ColumbusEvent.COLUMBUS_RETARGET_FOLLOW_ON_NOT_APPROVED;
                                    }
                                    ColumbusTargetRequestService.this.uiEventLogger.log(columbusEvent, 0, launcherActivityInfo.getComponentName().flattenToString());
                                } else if (i5 != -1) {
                                    Log.e("Columbus/TargetRequest", Intrinsics.stringPlus("Invalid dialog option: ", Integer.valueOf(i5)));
                                } else {
                                    Settings.Secure.putIntForUser(ColumbusTargetRequestService.this.getContentResolver(), "columbus_enabled", 1, ColumbusTargetRequestService.this.userTracker.getUserId());
                                    Settings.Secure.putStringForUser(ColumbusTargetRequestService.this.getContentResolver(), "columbus_action", "launch", ColumbusTargetRequestService.this.userTracker.getUserId());
                                    Settings.Secure.putStringForUser(ColumbusTargetRequestService.this.getContentResolver(), "columbus_launch_app", launcherActivityInfo.getComponentName().flattenToString(), ColumbusTargetRequestService.this.userTracker.getUserId());
                                    Settings.Secure.putStringForUser(ColumbusTargetRequestService.this.getContentResolver(), "columbus_launch_app_shortcut", launcherActivityInfo.getComponentName().flattenToString(), ColumbusTargetRequestService.this.userTracker.getUserId());
                                    ColumbusTargetRequestService.IncomingMessageHandler incomingMessageHandler3 = incomingMessageHandler;
                                    Messenger messenger4 = messenger2;
                                    int i10 = i4;
                                    int i11 = ColumbusTargetRequestService.IncomingMessageHandler.$r8$clinit;
                                    Objects.requireNonNull(incomingMessageHandler3);
                                    ColumbusTargetRequestService.IncomingMessageHandler.replyToMessenger(messenger4, i10, 0);
                                    Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Target changed to ", launcherActivityInfo.getComponentName().flattenToString()));
                                    if (packageShownCount == 0) {
                                        columbusEvent2 = ColumbusEvent.COLUMBUS_RETARGET_APPROVED;
                                    } else {
                                        columbusEvent2 = ColumbusEvent.COLUMBUS_RETARGET_FOLLOW_ON_APPROVED;
                                    }
                                    ColumbusTargetRequestService.this.uiEventLogger.log(columbusEvent2, 0, launcherActivityInfo.getComponentName().flattenToString());
                                }
                            }
                        };
                        DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() { // from class: com.google.android.systemui.columbus.ColumbusTargetRequestService$IncomingMessageHandler$displayDialog$1.2
                            @Override // android.content.DialogInterface.OnCancelListener
                            public final void onCancel(DialogInterface dialogInterface) {
                                ColumbusEvent columbusEvent;
                                ColumbusTargetRequestService.IncomingMessageHandler incomingMessageHandler2 = ColumbusTargetRequestService.IncomingMessageHandler.this;
                                Messenger messenger3 = messenger2;
                                int i5 = i4;
                                int i6 = ColumbusTargetRequestService.IncomingMessageHandler.$r8$clinit;
                                Objects.requireNonNull(incomingMessageHandler2);
                                ColumbusTargetRequestService.IncomingMessageHandler.replyToMessenger(messenger3, i5, 6);
                                Log.d("Columbus/TargetRequest", Intrinsics.stringPlus("Target change dismissed by user: ", launcherActivityInfo.getComponentName().flattenToString()));
                                if (packageShownCount == 0) {
                                    columbusEvent = ColumbusEvent.COLUMBUS_RETARGET_NOT_APPROVED;
                                } else {
                                    columbusEvent = ColumbusEvent.COLUMBUS_RETARGET_FOLLOW_ON_NOT_APPROVED;
                                }
                                columbusTargetRequestService2.uiEventLogger.log(columbusEvent, 0, launcherActivityInfo.getComponentName().flattenToString());
                            }
                        };
                        columbusTargetRequestDialog.setTitle(columbusTargetRequestDialog.getContext().getString(2131952129, launcherActivityInfo.getLabel()));
                        columbusTargetRequestDialog.setMessage(columbusTargetRequestDialog.getContext().getString(2131952128, launcherActivityInfo.getLabel()));
                        columbusTargetRequestDialog.setPositiveButton(2131952126, onClickListener);
                        columbusTargetRequestDialog.setNegativeButton(2131952127, onClickListener);
                        columbusTargetRequestDialog.setOnCancelListener(onCancelListener);
                        columbusTargetRequestDialog.setCanceledOnTouchOutside(true);
                        ColumbusTargetRequestService.IncomingMessageHandler incomingMessageHandler2 = this;
                        String packageName = appInfoForPackage.getComponentName().getPackageName();
                        Objects.requireNonNull(incomingMessageHandler2);
                        ColumbusStructuredDataManager columbusStructuredDataManager = ColumbusTargetRequestService.this.columbusStructuredDataManager;
                        Objects.requireNonNull(columbusStructuredDataManager);
                        synchronized (columbusStructuredDataManager.lock) {
                            int length = columbusStructuredDataManager.packageStats.length();
                            while (i3 < length) {
                                int i5 = i3 + 1;
                                JSONObject jSONObject = columbusStructuredDataManager.packageStats.getJSONObject(i3);
                                if (Intrinsics.areEqual(packageName, jSONObject.getString(ResourceEntry.KEY_PACKAGE_NAME))) {
                                    jSONObject.put("shownCount", jSONObject.getInt("shownCount") + 1);
                                    columbusStructuredDataManager.packageStats.put(i3, jSONObject);
                                    columbusStructuredDataManager.storePackageStats();
                                    return;
                                }
                                i3 = i5;
                            }
                            columbusStructuredDataManager.packageStats.put(ColumbusStructuredDataManager.makeJSONObject$default(columbusStructuredDataManager, packageName, 1, 0L, 4));
                            columbusStructuredDataManager.storePackageStats();
                        }
                    }
                });
            }
        }

        public final boolean packageIsNotAllowed(String str) {
            Signature[] signatureArr;
            if (!ColumbusTargetRequestService.this.allowPackageList.contains(str)) {
                return true;
            }
            PackageInfo packageInfo = ColumbusTargetRequestService.this.sysUIContext.getPackageManager().getPackageInfo(str, 134217728);
            if (packageInfo.signingInfo.hasMultipleSigners()) {
                signatureArr = packageInfo.signingInfo.getApkContentsSigners();
            } else {
                signatureArr = packageInfo.signingInfo.getSigningCertificateHistory();
            }
            ColumbusTargetRequestService columbusTargetRequestService = ColumbusTargetRequestService.this;
            ArrayList arrayList = new ArrayList(signatureArr.length);
            int length = signatureArr.length;
            boolean z = false;
            int i = 0;
            while (i < length) {
                Signature signature = signatureArr[i];
                i++;
                arrayList.add(new String(columbusTargetRequestService.messageDigest.digest(signature.toByteArray()), Charsets.UTF_16));
            }
            ColumbusTargetRequestService columbusTargetRequestService2 = ColumbusTargetRequestService.this;
            if (!arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (columbusTargetRequestService2.allowCertList.contains((String) it.next())) {
                        z = true;
                        break;
                    }
                }
            }
            return !z;
        }

        public final boolean packageIsTarget(String str) {
            String str2;
            boolean isColumbusEnabled = ColumbusTargetRequestService.this.columbusSettings.isColumbusEnabled();
            boolean areEqual = Intrinsics.areEqual("launch", ColumbusTargetRequestService.this.columbusSettings.selectedAction());
            ComponentName unflattenFromString = ComponentName.unflattenFromString(ColumbusTargetRequestService.this.columbusSettings.selectedApp());
            if (unflattenFromString == null) {
                str2 = null;
            } else {
                str2 = unflattenFromString.getPackageName();
            }
            boolean areEqual2 = Intrinsics.areEqual(str, str2);
            if (!isColumbusEnabled || !areEqual || !areEqual2) {
                return false;
            }
            return true;
        }

        public final boolean packageNeedsToCoolDown(String str) {
            long j;
            long lastDenyTimestamp;
            ColumbusStructuredDataManager columbusStructuredDataManager = ColumbusTargetRequestService.this.columbusStructuredDataManager;
            Objects.requireNonNull(columbusStructuredDataManager);
            synchronized (columbusStructuredDataManager.lock) {
                try {
                    j = SystemClock.currentNetworkTimeMillis();
                } catch (DateTimeException unused) {
                    j = System.currentTimeMillis();
                }
                lastDenyTimestamp = j - columbusStructuredDataManager.getLastDenyTimestamp(str);
            }
            if (lastDenyTimestamp < ColumbusTargetRequestService.PACKAGE_DENY_COOLDOWN_MS) {
                return true;
            }
            return false;
        }
    }

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        ColumbusContext columbusContext = this.columbusContext;
        Objects.requireNonNull(columbusContext);
        if (columbusContext.packageManager.hasSystemFeature("com.google.android.feature.QUICK_TAP")) {
            return this.messenger.getBinder();
        }
        return null;
    }

    public ColumbusTargetRequestService(Context context, UserTracker userTracker, ColumbusSettings columbusSettings, ColumbusStructuredDataManager columbusStructuredDataManager, UiEventLogger uiEventLogger, Handler handler, Looper looper) {
        this.sysUIContext = context;
        this.userTracker = userTracker;
        this.columbusSettings = columbusSettings;
        this.columbusStructuredDataManager = columbusStructuredDataManager;
        this.uiEventLogger = uiEventLogger;
        this.mainHandler = handler;
        this.columbusContext = new ColumbusContext(context);
        this.messenger = new Messenger(new IncomingMessageHandler(looper));
        String[] stringArray = context.getResources().getStringArray(2130903087);
        this.allowPackageList = SetsKt__SetsKt.setOf(Arrays.copyOf(stringArray, stringArray.length));
        String[] stringArray2 = context.getResources().getStringArray(2130903086);
        this.allowCertList = SetsKt__SetsKt.setOf(Arrays.copyOf(stringArray2, stringArray2.length));
    }

    @Override // android.app.Service
    public final void onCreate() {
        LauncherApps launcherApps;
        super.onCreate();
        Object systemService = getSystemService("launcherapps");
        if (systemService instanceof LauncherApps) {
            launcherApps = (LauncherApps) systemService;
        } else {
            launcherApps = null;
        }
        this.launcherApps = launcherApps;
    }
}
