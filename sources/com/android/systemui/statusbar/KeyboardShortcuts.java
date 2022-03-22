package com.android.systemui.statusbar;

import android.app.AlertDialog;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyboardShortcutGroup;
import android.view.KeyboardShortcutInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.MetricsLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyboardShortcuts {
    public static KeyboardShortcuts sInstance;
    public static final Object sLock = new Object();
    public KeyCharacterMap mBackupKeyCharacterMap;
    public final ContextThemeWrapper mContext;
    public KeyCharacterMap mKeyCharacterMap;
    public AlertDialog mKeyboardShortcutsDialog;
    public final SparseArray<Drawable> mModifierDrawables;
    public final SparseArray<String> mModifierNames;
    public final SparseArray<Drawable> mSpecialCharacterDrawables;
    public final SparseArray<String> mSpecialCharacterNames;
    public final int[] mModifierList = {65536, 4096, 2, 1, 4, 8};
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final AnonymousClass1 mDialogCloseListener = new DialogInterface.OnClickListener() { // from class: com.android.systemui.statusbar.KeyboardShortcuts.1
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            KeyboardShortcuts keyboardShortcuts = KeyboardShortcuts.this;
            Objects.requireNonNull(keyboardShortcuts);
            AlertDialog alertDialog = keyboardShortcuts.mKeyboardShortcutsDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                keyboardShortcuts.mKeyboardShortcutsDialog = null;
            }
        }
    };
    public final AnonymousClass2 mApplicationItemsComparator = new Comparator<KeyboardShortcutInfo>() { // from class: com.android.systemui.statusbar.KeyboardShortcuts.2
        @Override // java.util.Comparator
        public final int compare(KeyboardShortcutInfo keyboardShortcutInfo, KeyboardShortcutInfo keyboardShortcutInfo2) {
            boolean z;
            boolean z2;
            KeyboardShortcutInfo keyboardShortcutInfo3 = keyboardShortcutInfo;
            KeyboardShortcutInfo keyboardShortcutInfo4 = keyboardShortcutInfo2;
            if (keyboardShortcutInfo3.getLabel() == null || keyboardShortcutInfo3.getLabel().toString().isEmpty()) {
                z = true;
            } else {
                z = false;
            }
            if (keyboardShortcutInfo4.getLabel() == null || keyboardShortcutInfo4.getLabel().toString().isEmpty()) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z && z2) {
                return 0;
            }
            if (z) {
                return 1;
            }
            if (z2) {
                return -1;
            }
            return keyboardShortcutInfo3.getLabel().toString().compareToIgnoreCase(keyboardShortcutInfo4.getLabel().toString());
        }
    };
    public final IPackageManager mPackageManager = AppGlobals.getPackageManager();

    /* loaded from: classes.dex */
    public final class ShortcutKeyAccessibilityDelegate extends View.AccessibilityDelegate {
        public String mContentDescription;

        public ShortcutKeyAccessibilityDelegate(String str) {
            this.mContentDescription = str;
        }

        @Override // android.view.View.AccessibilityDelegate
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            String str = this.mContentDescription;
            if (str != null) {
                accessibilityNodeInfo.setContentDescription(str.toLowerCase());
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class StringDrawableContainer {
        public Drawable mDrawable;
        public String mString;

        public StringDrawableContainer(String str, Drawable drawable) {
            this.mString = str;
            this.mDrawable = drawable;
        }
    }

    /* JADX WARN: Type inference failed for: r6v3, types: [com.android.systemui.statusbar.KeyboardShortcuts$1] */
    /* JADX WARN: Type inference failed for: r6v4, types: [com.android.systemui.statusbar.KeyboardShortcuts$2] */
    public KeyboardShortcuts(Context context) {
        SparseArray<String> sparseArray = new SparseArray<>();
        this.mSpecialCharacterNames = sparseArray;
        SparseArray<String> sparseArray2 = new SparseArray<>();
        this.mModifierNames = sparseArray2;
        SparseArray<Drawable> sparseArray3 = new SparseArray<>();
        this.mSpecialCharacterDrawables = sparseArray3;
        SparseArray<Drawable> sparseArray4 = new SparseArray<>();
        this.mModifierDrawables = sparseArray4;
        this.mContext = new ContextThemeWrapper(context, 16974371);
        sparseArray.put(3, context.getString(2131952493));
        sparseArray.put(4, context.getString(2131952483));
        sparseArray.put(19, context.getString(2131952490));
        sparseArray.put(20, context.getString(2131952487));
        sparseArray.put(21, context.getString(2131952488));
        sparseArray.put(22, context.getString(2131952489));
        sparseArray.put(23, context.getString(2131952486));
        sparseArray.put(56, ".");
        sparseArray.put(61, context.getString(2131952508));
        sparseArray.put(62, context.getString(2131952507));
        sparseArray.put(66, context.getString(2131952491));
        sparseArray.put(67, context.getString(2131952484));
        sparseArray.put(85, context.getString(2131952497));
        sparseArray.put(86, context.getString(2131952500));
        sparseArray.put(87, context.getString(2131952496));
        sparseArray.put(88, context.getString(2131952498));
        sparseArray.put(89, context.getString(2131952499));
        sparseArray.put(90, context.getString(2131952495));
        sparseArray.put(92, context.getString(2131952506));
        sparseArray.put(93, context.getString(2131952505));
        sparseArray.put(96, context.getString(2131952485, "A"));
        sparseArray.put(97, context.getString(2131952485, "B"));
        sparseArray.put(98, context.getString(2131952485, "C"));
        sparseArray.put(99, context.getString(2131952485, "X"));
        sparseArray.put(100, context.getString(2131952485, "Y"));
        sparseArray.put(101, context.getString(2131952485, "Z"));
        sparseArray.put(102, context.getString(2131952485, "L1"));
        sparseArray.put(103, context.getString(2131952485, "R1"));
        sparseArray.put(104, context.getString(2131952485, "L2"));
        sparseArray.put(105, context.getString(2131952485, "R2"));
        sparseArray.put(108, context.getString(2131952485, "Start"));
        sparseArray.put(109, context.getString(2131952485, "Select"));
        sparseArray.put(110, context.getString(2131952485, "Mode"));
        sparseArray.put(112, context.getString(2131952492));
        sparseArray.put(111, "Esc");
        sparseArray.put(120, "SysRq");
        sparseArray.put(121, "Break");
        sparseArray.put(116, "Scroll Lock");
        sparseArray.put(122, context.getString(2131952502));
        sparseArray.put(123, context.getString(2131952501));
        sparseArray.put(124, context.getString(2131952494));
        sparseArray.put(131, "F1");
        sparseArray.put(132, "F2");
        sparseArray.put(133, "F3");
        sparseArray.put(134, "F4");
        sparseArray.put(135, "F5");
        sparseArray.put(136, "F6");
        sparseArray.put(137, "F7");
        sparseArray.put(138, "F8");
        sparseArray.put(139, "F9");
        sparseArray.put(140, "F10");
        sparseArray.put(141, "F11");
        sparseArray.put(142, "F12");
        sparseArray.put(143, context.getString(2131952503));
        sparseArray.put(144, context.getString(2131952504, "0"));
        sparseArray.put(145, context.getString(2131952504, "1"));
        sparseArray.put(146, context.getString(2131952504, "2"));
        sparseArray.put(147, context.getString(2131952504, "3"));
        sparseArray.put(148, context.getString(2131952504, "4"));
        sparseArray.put(149, context.getString(2131952504, "5"));
        sparseArray.put(150, context.getString(2131952504, "6"));
        sparseArray.put(151, context.getString(2131952504, "7"));
        sparseArray.put(152, context.getString(2131952504, "8"));
        sparseArray.put(153, context.getString(2131952504, "9"));
        sparseArray.put(154, context.getString(2131952504, "/"));
        sparseArray.put(155, context.getString(2131952504, "*"));
        sparseArray.put(156, context.getString(2131952504, "-"));
        sparseArray.put(157, context.getString(2131952504, "+"));
        sparseArray.put(158, context.getString(2131952504, "."));
        sparseArray.put(159, context.getString(2131952504, ","));
        sparseArray.put(160, context.getString(2131952504, context.getString(2131952491)));
        sparseArray.put(161, context.getString(2131952504, "="));
        sparseArray.put(162, context.getString(2131952504, "("));
        sparseArray.put(163, context.getString(2131952504, ")"));
        sparseArray.put(211, "半角/全角");
        sparseArray.put(212, "英数");
        sparseArray.put(213, "無変換");
        sparseArray.put(214, "変換");
        sparseArray.put(215, "かな");
        sparseArray2.put(65536, "Meta");
        sparseArray2.put(4096, "Ctrl");
        sparseArray2.put(2, "Alt");
        sparseArray2.put(1, "Shift");
        sparseArray2.put(4, "Sym");
        sparseArray2.put(8, "Fn");
        sparseArray3.put(67, context.getDrawable(2131232018));
        sparseArray3.put(66, context.getDrawable(2131232020));
        sparseArray3.put(19, context.getDrawable(2131232024));
        sparseArray3.put(22, context.getDrawable(2131232023));
        sparseArray3.put(20, context.getDrawable(2131232019));
        sparseArray3.put(21, context.getDrawable(2131232021));
        sparseArray4.put(65536, context.getDrawable(2131232022));
    }

    public static void dismiss() {
        synchronized (sLock) {
            KeyboardShortcuts keyboardShortcuts = sInstance;
            if (keyboardShortcuts != null) {
                MetricsLogger.hidden(keyboardShortcuts.mContext, 500);
                KeyboardShortcuts keyboardShortcuts2 = sInstance;
                Objects.requireNonNull(keyboardShortcuts2);
                AlertDialog alertDialog = keyboardShortcuts2.mKeyboardShortcutsDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    keyboardShortcuts2.mKeyboardShortcutsDialog = null;
                }
                sInstance = null;
            }
        }
    }

    public static void show(Context context, int i) {
        MetricsLogger.visible(context, 500);
        synchronized (sLock) {
            KeyboardShortcuts keyboardShortcuts = sInstance;
            if (keyboardShortcuts != null && !keyboardShortcuts.mContext.equals(context)) {
                dismiss();
            }
            if (sInstance == null) {
                sInstance = new KeyboardShortcuts(context);
            }
            sInstance.showKeyboardShortcuts(i);
        }
    }

    public final Icon getIconForIntentCategory(String str, int i) {
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo;
        int i2;
        ResolveInfo resolveIntent;
        ActivityInfo activityInfo;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory(str);
        try {
            resolveIntent = this.mPackageManager.resolveIntent(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 0L, i);
        } catch (RemoteException e) {
            Log.e("KeyboardShortcuts", "PackageManagerService is dead", e);
        }
        if (!(resolveIntent == null || (activityInfo = resolveIntent.activityInfo) == null)) {
            packageInfo = this.mPackageManager.getPackageInfo(activityInfo.packageName, 0L, i);
            if (packageInfo != null || (i2 = (applicationInfo = packageInfo.applicationInfo).icon) == 0) {
                return null;
            }
            return Icon.createWithResource(applicationInfo.packageName, i2);
        }
        packageInfo = null;
        if (packageInfo != null) {
        }
        return null;
    }

    public final void showKeyboardShortcuts(int i) {
        InputDevice inputDevice;
        InputManager instance = InputManager.getInstance();
        this.mBackupKeyCharacterMap = instance.getInputDevice(-1).getKeyCharacterMap();
        if (i == -1 || (inputDevice = instance.getInputDevice(i)) == null) {
            int[] inputDeviceIds = instance.getInputDeviceIds();
            int i2 = 0;
            while (true) {
                if (i2 >= inputDeviceIds.length) {
                    this.mKeyCharacterMap = this.mBackupKeyCharacterMap;
                    break;
                }
                InputDevice inputDevice2 = instance.getInputDevice(inputDeviceIds[i2]);
                if (inputDevice2.getId() != -1 && inputDevice2.isFullKeyboard()) {
                    this.mKeyCharacterMap = inputDevice2.getKeyCharacterMap();
                    break;
                }
                i2++;
            }
        } else {
            this.mKeyCharacterMap = inputDevice.getKeyCharacterMap();
        }
        ((WindowManager) this.mContext.getSystemService("window")).requestAppKeyboardShortcuts(new WindowManager.KeyboardShortcutsReceiver() { // from class: com.android.systemui.statusbar.KeyboardShortcuts.3
            public final void onKeyboardShortcutsReceived(final List<KeyboardShortcutGroup> list) {
                PackageInfo packageInfo;
                KeyboardShortcuts keyboardShortcuts = KeyboardShortcuts.this;
                Objects.requireNonNull(keyboardShortcuts);
                KeyboardShortcutGroup keyboardShortcutGroup = new KeyboardShortcutGroup((CharSequence) keyboardShortcuts.mContext.getString(2131952517), true);
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952519), 66, 65536));
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952518), 67, 65536));
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952521), 61, 2));
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952520), 42, 65536));
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952522), 76, 65536));
                keyboardShortcutGroup.addItem(new KeyboardShortcutInfo(keyboardShortcuts.mContext.getString(2131952523), 62, 65536));
                list.add(keyboardShortcutGroup);
                KeyboardShortcuts keyboardShortcuts2 = KeyboardShortcuts.this;
                Objects.requireNonNull(keyboardShortcuts2);
                int userId = keyboardShortcuts2.mContext.getUserId();
                ArrayList arrayList = new ArrayList();
                ComponentName assistComponentForUser = new AssistUtils(keyboardShortcuts2.mContext).getAssistComponentForUser(userId);
                KeyboardShortcutGroup keyboardShortcutGroup2 = null;
                if (assistComponentForUser != null) {
                    try {
                        packageInfo = keyboardShortcuts2.mPackageManager.getPackageInfo(assistComponentForUser.getPackageName(), 0L, userId);
                    } catch (RemoteException unused) {
                        Log.e("KeyboardShortcuts", "PackageManagerService is dead");
                        packageInfo = null;
                    }
                    if (packageInfo != null) {
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952510), Icon.createWithResource(applicationInfo.packageName, applicationInfo.icon), 0, 65536));
                    }
                }
                Icon iconForIntentCategory = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_BROWSER", userId);
                if (iconForIntentCategory != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952511), iconForIntentCategory, 30, 65536));
                }
                Icon iconForIntentCategory2 = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_CONTACTS", userId);
                if (iconForIntentCategory2 != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952513), iconForIntentCategory2, 31, 65536));
                }
                Icon iconForIntentCategory3 = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_EMAIL", userId);
                if (iconForIntentCategory3 != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952514), iconForIntentCategory3, 33, 65536));
                }
                Icon iconForIntentCategory4 = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_MESSAGING", userId);
                if (iconForIntentCategory4 != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952516), iconForIntentCategory4, 47, 65536));
                }
                Icon iconForIntentCategory5 = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_MUSIC", userId);
                if (iconForIntentCategory5 != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952515), iconForIntentCategory5, 44, 65536));
                }
                Icon iconForIntentCategory6 = keyboardShortcuts2.getIconForIntentCategory("android.intent.category.APP_CALENDAR", userId);
                if (iconForIntentCategory6 != null) {
                    arrayList.add(new KeyboardShortcutInfo(keyboardShortcuts2.mContext.getString(2131952512), iconForIntentCategory6, 40, 65536));
                }
                if (arrayList.size() != 0) {
                    Collections.sort(arrayList, keyboardShortcuts2.mApplicationItemsComparator);
                    keyboardShortcutGroup2 = new KeyboardShortcutGroup(keyboardShortcuts2.mContext.getString(2131952509), arrayList, true);
                }
                if (keyboardShortcutGroup2 != null) {
                    list.add(keyboardShortcutGroup2);
                }
                final KeyboardShortcuts keyboardShortcuts3 = KeyboardShortcuts.this;
                Objects.requireNonNull(keyboardShortcuts3);
                keyboardShortcuts3.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.KeyboardShortcuts.4
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Removed duplicated region for block: B:45:0x018a  */
                    /* JADX WARN: Removed duplicated region for block: B:46:0x0193  */
                    /* JADX WARN: Removed duplicated region for block: B:49:0x019c  */
                    /* JADX WARN: Removed duplicated region for block: B:50:0x01ae  */
                    /* JADX WARN: Type inference failed for: r13v3, types: [android.view.ViewGroup] */
                    /* JADX WARN: Type inference failed for: r14v0 */
                    /* JADX WARN: Type inference failed for: r14v1, types: [int] */
                    /* JADX WARN: Type inference failed for: r6v1, types: [android.view.LayoutInflater] */
                    /* JADX WARN: Unknown variable types count: 1 */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void run() {
                        /*
                            Method dump skipped, instructions count: 815
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.KeyboardShortcuts.AnonymousClass4.run():void");
                    }
                });
            }
        }, i);
    }
}
