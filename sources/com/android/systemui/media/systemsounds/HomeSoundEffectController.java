package com.android.systemui.media.systemsounds;

import android.app.WindowConfiguration;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import com.android.systemui.CoreStartable;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
/* loaded from: classes.dex */
public class HomeSoundEffectController extends CoreStartable {
    public final ActivityManagerWrapper mActivityManagerWrapper;
    public final AudioManager mAudioManager;
    public boolean mIsLastTaskHome = true;
    public boolean mLastActivityHasNoHomeSound = false;
    @WindowConfiguration.ActivityType
    public int mLastActivityType;
    public String mLastHomePackageName;
    public int mLastTaskId;
    public final boolean mPlayHomeSoundAfterAssistant;
    public final boolean mPlayHomeSoundAfterDream;
    public final PackageManager mPm;
    public final TaskStackChangeListeners mTaskStackChangeListeners;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        if (this.mAudioManager.isHomeSoundEffectEnabled()) {
            this.mTaskStackChangeListeners.registerTaskStackListener(new TaskStackChangeListener() { // from class: com.android.systemui.media.systemsounds.HomeSoundEffectController.1
                /* JADX WARN: Removed duplicated region for block: B:38:0x007b  */
                /* JADX WARN: Removed duplicated region for block: B:39:0x007d  */
                @Override // com.android.systemui.shared.system.TaskStackChangeListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void onTaskStackChanged() {
                    /*
                        r7 = this;
                        com.android.systemui.media.systemsounds.HomeSoundEffectController r0 = com.android.systemui.media.systemsounds.HomeSoundEffectController.this
                        com.android.systemui.shared.system.ActivityManagerWrapper r0 = r0.mActivityManagerWrapper
                        android.app.ActivityManager$RunningTaskInfo r0 = r0.getRunningTask()
                        if (r0 == 0) goto L_0x0099
                        android.content.pm.ActivityInfo r1 = r0.topActivityInfo
                        if (r1 != 0) goto L_0x0010
                        goto L_0x0099
                    L_0x0010:
                        com.android.systemui.media.systemsounds.HomeSoundEffectController r7 = com.android.systemui.media.systemsounds.HomeSoundEffectController.this
                        java.util.Objects.requireNonNull(r7)
                        int r1 = r0.topActivityType
                        r2 = 2
                        r3 = 0
                        r4 = 1
                        if (r1 != r2) goto L_0x001e
                        r1 = r4
                        goto L_0x001f
                    L_0x001e:
                        r1 = r3
                    L_0x001f:
                        int r5 = r0.taskId
                        int r6 = r7.mLastTaskId
                        if (r5 != r6) goto L_0x0026
                        goto L_0x0046
                    L_0x0026:
                        boolean r5 = r7.mIsLastTaskHome
                        if (r5 != 0) goto L_0x0046
                        if (r1 != 0) goto L_0x002d
                        goto L_0x0046
                    L_0x002d:
                        boolean r1 = r7.mLastActivityHasNoHomeSound
                        if (r1 == 0) goto L_0x0032
                        goto L_0x0046
                    L_0x0032:
                        int r1 = r7.mLastActivityType
                        r5 = 4
                        if (r1 != r5) goto L_0x003c
                        boolean r5 = r7.mPlayHomeSoundAfterAssistant
                        if (r5 != 0) goto L_0x003c
                        goto L_0x0046
                    L_0x003c:
                        r5 = 5
                        if (r1 != r5) goto L_0x0044
                        boolean r1 = r7.mPlayHomeSoundAfterDream
                        if (r1 != 0) goto L_0x0044
                        goto L_0x0046
                    L_0x0044:
                        r1 = r4
                        goto L_0x0047
                    L_0x0046:
                        r1 = r3
                    L_0x0047:
                        if (r1 == 0) goto L_0x0050
                        android.media.AudioManager r1 = r7.mAudioManager
                        r5 = 11
                        r1.playSoundEffect(r5)
                    L_0x0050:
                        int r1 = r0.taskId
                        r7.mLastTaskId = r1
                        int r1 = r0.topActivityType
                        r7.mLastActivityType = r1
                        android.content.pm.ActivityInfo r1 = r0.topActivityInfo
                        int r5 = r1.privateFlags
                        r5 = r5 & r2
                        if (r5 != 0) goto L_0x0074
                        android.content.pm.PackageManager r5 = r7.mPm
                        java.lang.String r1 = r1.packageName
                        java.lang.String r6 = "android.permission.DISABLE_SYSTEM_SOUND_EFFECTS"
                        int r1 = r5.checkPermission(r6, r1)
                        if (r1 != 0) goto L_0x006d
                        r1 = r4
                        goto L_0x0075
                    L_0x006d:
                        java.lang.String r1 = "HomeSoundEffectController"
                        java.lang.String r5 = "Activity has flag playHomeTransition set to false but doesn't hold required permission android.permission.DISABLE_SYSTEM_SOUND_EFFECTS"
                        android.util.Slog.w(r1, r5)
                    L_0x0074:
                        r1 = r3
                    L_0x0075:
                        r7.mLastActivityHasNoHomeSound = r1
                        int r1 = r0.topActivityType
                        if (r1 != r2) goto L_0x007d
                        r1 = r4
                        goto L_0x007e
                    L_0x007d:
                        r1 = r3
                    L_0x007e:
                        android.content.pm.ActivityInfo r2 = r0.topActivityInfo
                        java.lang.String r2 = r2.packageName
                        java.lang.String r5 = r7.mLastHomePackageName
                        boolean r2 = r2.equals(r5)
                        if (r1 != 0) goto L_0x008c
                        if (r2 == 0) goto L_0x008d
                    L_0x008c:
                        r3 = r4
                    L_0x008d:
                        r7.mIsLastTaskHome = r3
                        if (r1 == 0) goto L_0x0099
                        if (r2 != 0) goto L_0x0099
                        android.content.pm.ActivityInfo r0 = r0.topActivityInfo
                        java.lang.String r0 = r0.packageName
                        r7.mLastHomePackageName = r0
                    L_0x0099:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.systemsounds.HomeSoundEffectController.AnonymousClass1.onTaskStackChanged():void");
                }
            });
        }
    }

    public HomeSoundEffectController(Context context, AudioManager audioManager, TaskStackChangeListeners taskStackChangeListeners, ActivityManagerWrapper activityManagerWrapper, PackageManager packageManager) {
        super(context);
        this.mAudioManager = audioManager;
        this.mTaskStackChangeListeners = taskStackChangeListeners;
        this.mActivityManagerWrapper = activityManagerWrapper;
        this.mPm = packageManager;
        this.mPlayHomeSoundAfterAssistant = context.getResources().getBoolean(2131034153);
        this.mPlayHomeSoundAfterDream = context.getResources().getBoolean(2131034154);
    }
}
