package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArraySet;
import androidx.core.view.KeyEventDispatcher$Component;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class AppCompatDialog extends Dialog implements AppCompatCallback {
    public AppCompatDelegateImpl mDelegate;
    public final AnonymousClass1 mKeyDispatcher;

    /* renamed from: androidx.appcompat.app.AppCompatDialog$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements KeyEventDispatcher$Component {
        public AnonymousClass1() {
        }

        public final boolean superDispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDialog.this.superDispatchKeyEvent(keyEvent);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppCompatDialog(android.content.Context r5, int r6) {
        /*
            r4 = this;
            r0 = 1
            r1 = 2130968948(0x7f040174, float:1.7546564E38)
            if (r6 != 0) goto L_0x0015
            android.util.TypedValue r2 = new android.util.TypedValue
            r2.<init>()
            android.content.res.Resources$Theme r3 = r5.getTheme()
            r3.resolveAttribute(r1, r2, r0)
            int r2 = r2.resourceId
            goto L_0x0016
        L_0x0015:
            r2 = r6
        L_0x0016:
            r4.<init>(r5, r2)
            androidx.appcompat.app.AppCompatDialog$1 r2 = new androidx.appcompat.app.AppCompatDialog$1
            r2.<init>()
            r4.mKeyDispatcher = r2
            androidx.appcompat.app.AppCompatDelegate r4 = r4.getDelegate()
            if (r6 != 0) goto L_0x0034
            android.util.TypedValue r6 = new android.util.TypedValue
            r6.<init>()
            android.content.res.Resources$Theme r5 = r5.getTheme()
            r5.resolveAttribute(r1, r6, r0)
            int r6 = r6.resourceId
        L_0x0034:
            r5 = r4
            androidx.appcompat.app.AppCompatDelegateImpl r5 = (androidx.appcompat.app.AppCompatDelegateImpl) r5
            java.util.Objects.requireNonNull(r5)
            r5.mThemeResId = r6
            r4.onCreate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDialog.<init>(android.content.Context, int):void");
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public final void onSupportActionModeFinished() {
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public final void onSupportActionModeStarted() {
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public final void onWindowStartingSupportActionMode() {
    }

    @Override // android.app.Dialog
    public final void setContentView(int i) {
        getDelegate().setContentView(i);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        getDelegate().setTitle(charSequence);
    }

    public final AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            ArraySet<WeakReference<AppCompatDelegate>> arraySet = AppCompatDelegate.sActivityDelegates;
            this.mDelegate = new AppCompatDelegateImpl(getContext(), getWindow(), this, this);
        }
        return this.mDelegate;
    }

    @Override // android.app.Dialog
    public final void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override // android.app.Dialog
    public final void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        getDelegate().addContentView(view, layoutParams);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public final void dismiss() {
        super.dismiss();
        getDelegate().onDestroy();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        getWindow().getDecorView();
        AnonymousClass1 r1 = this.mKeyDispatcher;
        if (r1 == null) {
            return false;
        }
        return r1.superDispatchKeyEvent(keyEvent);
    }

    @Override // android.app.Dialog
    public final <T extends View> T findViewById(int i) {
        return (T) getDelegate().findViewById(i);
    }

    @Override // android.app.Dialog
    public final void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        getDelegate().installViewFactory();
        super.onCreate(bundle);
        getDelegate().onCreate();
    }

    @Override // android.app.Dialog
    public final void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override // android.app.Dialog
    public final void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        getDelegate().setContentView(view, layoutParams);
    }

    @Override // android.app.Dialog
    public void setTitle(int i) {
        super.setTitle(i);
        getDelegate().setTitle(getContext().getString(i));
    }

    public final boolean superDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
}
