package androidx.core.widget;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TextViewCompat {

    /* loaded from: classes.dex */
    public static class OreoCallback implements ActionMode.Callback {
        public final ActionMode.Callback mCallback;
        public boolean mCanUseMenuBuilderReferences;
        public boolean mInitializedMenuBuilderReferences;
        public Class<?> mMenuBuilderClass;
        public Method mMenuBuilderRemoveItemAtMethod;
        public final TextView mTextView;

        @Override // android.view.ActionMode.Callback
        public final boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mCallback.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public final boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mCallback.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public final void onDestroyActionMode(ActionMode actionMode) {
            this.mCallback.onDestroyActionMode(actionMode);
        }

        /* JADX WARN: Removed duplicated region for block: B:64:0x00d3 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:66:0x00a1 A[SYNTHETIC] */
        @Override // android.view.ActionMode.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean onPrepareActionMode(android.view.ActionMode r13, android.view.Menu r14) {
            /*
                Method dump skipped, instructions count: 306
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.TextViewCompat.OreoCallback.onPrepareActionMode(android.view.ActionMode, android.view.Menu):boolean");
        }
    }

    public static ActionMode.Callback unwrapCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        if (!(callback instanceof OreoCallback)) {
            return callback;
        }
        OreoCallback oreoCallback = (OreoCallback) callback;
        Objects.requireNonNull(oreoCallback);
        return oreoCallback.mCallback;
    }
}
