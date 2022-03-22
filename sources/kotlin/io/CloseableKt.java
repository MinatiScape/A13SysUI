package kotlin.io;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.WithHint;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.io.Closeable;
import java.util.Objects;
import kotlin.ExceptionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Closeable.kt */
/* loaded from: classes.dex */
public final class CloseableKt {
    public static final int[] METRICS_GESTURE_TYPE_MAP = {0, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195};

    public static final void closeFinally(Closeable closeable, Throwable th) {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }

    public static InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo, View view) {
        if (inputConnection != null && editorInfo.hintText == null) {
            ViewParent parent = view.getParent();
            while (true) {
                if (!(parent instanceof View)) {
                    break;
                } else if (parent instanceof WithHint) {
                    editorInfo.hintText = ((WithHint) parent).getHint();
                    break;
                } else {
                    parent = parent.getParent();
                }
            }
        }
        return inputConnection;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final long systemProp(java.lang.String r24, long r25, long r27, long r29) {
        /*
            Method dump skipped, instructions count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.CloseableKt.systemProp(java.lang.String, long, long, long):long");
    }

    public static int systemProp$default(String str, int i, int i2, int i3, int i4) {
        if ((i4 & 4) != 0) {
            i2 = 1;
        }
        if ((i4 & 8) != 0) {
            i3 = Integer.MAX_VALUE;
        }
        return (int) systemProp(str, i, i2, i3);
    }

    public static final boolean access$isColorizedForegroundService(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        Notification notification = notificationEntry.mSbn.getNotification();
        if (!notification.isForegroundService() || !notification.isColorized() || notificationEntry.getImportance() <= 1) {
            return false;
        }
        return true;
    }

    public static final boolean access$isImportantCall(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        if (!notificationEntry.mSbn.getNotification().isStyle(Notification.CallStyle.class) || notificationEntry.getImportance() <= 1) {
            return false;
        }
        return true;
    }

    public static final boolean access$isSystemMax(NotificationEntry notificationEntry) {
        boolean z;
        if (notificationEntry.getImportance() >= 4) {
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            if (Intrinsics.areEqual(ThemeOverlayApplier.ANDROID_PACKAGE, statusBarNotification.getPackageName()) || Intrinsics.areEqual(ThemeOverlayApplier.SYSUI_PACKAGE, statusBarNotification.getPackageName())) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }
}
