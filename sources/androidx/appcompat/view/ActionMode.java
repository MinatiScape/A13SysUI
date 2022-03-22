package androidx.appcompat.view;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.menu.MenuBuilder;
/* loaded from: classes.dex */
public abstract class ActionMode {
    public Object mTag;
    public boolean mTitleOptionalHint;

    /* loaded from: classes.dex */
    public interface Callback {
        boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem);

        boolean onCreateActionMode(ActionMode actionMode, MenuBuilder menuBuilder);

        void onDestroyActionMode(ActionMode actionMode);

        boolean onPrepareActionMode(ActionMode actionMode, MenuBuilder menuBuilder);
    }

    public abstract void finish();

    public abstract View getCustomView();

    public abstract MenuBuilder getMenu();

    public abstract MenuInflater getMenuInflater();

    public abstract CharSequence getSubtitle();

    public abstract CharSequence getTitle();

    public abstract void invalidate();

    public abstract boolean isTitleOptional();

    public abstract void setCustomView(View view);

    public abstract void setSubtitle(int i);

    public abstract void setSubtitle(CharSequence charSequence);

    public abstract void setTitle(int i);

    public abstract void setTitle(CharSequence charSequence);

    public abstract void setTitleOptionalHint(boolean z);
}
