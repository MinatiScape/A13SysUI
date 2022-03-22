package androidx.appcompat.app;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArraySet;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class AppCompatDelegate {
    public static final ArraySet<WeakReference<AppCompatDelegate>> sActivityDelegates = new ArraySet<>(0);
    public static final Object sActivityDelegatesLock = new Object();

    public abstract void addContentView(View view, ViewGroup.LayoutParams layoutParams);

    public Context attachBaseContext2(Context context) {
        return context;
    }

    public abstract <T extends View> T findViewById(int i);

    public int getLocalNightMode() {
        return -100;
    }

    public abstract MenuInflater getMenuInflater();

    public abstract void installViewFactory();

    public abstract void invalidateOptionsMenu();

    public abstract void onConfigurationChanged();

    public abstract void onCreate();

    public abstract void onDestroy();

    public abstract void onPostResume();

    public abstract void onStart();

    public abstract void onStop();

    public abstract boolean requestWindowFeature(int i);

    public abstract void setContentView(int i);

    public abstract void setContentView(View view);

    public abstract void setContentView(View view, ViewGroup.LayoutParams layoutParams);

    public void setTheme(int i) {
    }

    public abstract void setTitle(CharSequence charSequence);

    public static void removeDelegateFromActives(AppCompatDelegate appCompatDelegate) {
        synchronized (sActivityDelegatesLock) {
            ArraySet<WeakReference<AppCompatDelegate>> arraySet = sActivityDelegates;
            Objects.requireNonNull(arraySet);
            ArraySet.ElementIterator elementIterator = new ArraySet.ElementIterator();
            while (elementIterator.hasNext()) {
                AppCompatDelegate appCompatDelegate2 = (AppCompatDelegate) ((WeakReference) elementIterator.next()).get();
                if (appCompatDelegate2 == appCompatDelegate || appCompatDelegate2 == null) {
                    elementIterator.remove();
                }
            }
        }
    }
}
