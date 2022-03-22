package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.functions.Function1;
/* compiled from: AppAdapter.kt */
/* loaded from: classes.dex */
public final class AppAdapter extends RecyclerView.Adapter<Holder> {
    public final FavoritesRenderer favoritesRenderer;
    public final LayoutInflater layoutInflater;
    public List<ControlsServiceInfo> listOfServices = EmptyList.INSTANCE;
    public final Function1<ComponentName, Unit> onAppSelected;
    public final Resources resources;

    /* compiled from: AppAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Holder extends RecyclerView.ViewHolder {
        public final FavoritesRenderer favRenderer;
        public final TextView favorites;
        public final ImageView icon;
        public final TextView title;

        public Holder(View view, FavoritesRenderer favoritesRenderer) {
            super(view);
            this.favRenderer = favoritesRenderer;
            this.icon = (ImageView) view.requireViewById(16908294);
            this.title = (TextView) view.requireViewById(16908310);
            this.favorites = (TextView) view.requireViewById(2131427959);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.listOfServices.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(Holder holder, final int i) {
        String str;
        Holder holder2 = holder;
        ControlsServiceInfo controlsServiceInfo = this.listOfServices.get(i);
        holder2.icon.setImageDrawable(controlsServiceInfo.loadIcon());
        holder2.title.setText(controlsServiceInfo.loadLabel());
        FavoritesRenderer favoritesRenderer = holder2.favRenderer;
        ComponentName componentName = controlsServiceInfo.componentName;
        Objects.requireNonNull(favoritesRenderer);
        int intValue = favoritesRenderer.favoriteFunction.invoke(componentName).intValue();
        int i2 = 0;
        if (intValue != 0) {
            str = favoritesRenderer.resources.getQuantityString(2131820547, intValue, Integer.valueOf(intValue));
        } else {
            str = null;
        }
        holder2.favorites.setText(str);
        TextView textView = holder2.favorites;
        if (str == null) {
            i2 = 8;
        }
        textView.setVisibility(i2);
        holder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.AppAdapter$onBindViewHolder$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                String str2;
                AppAdapter appAdapter = AppAdapter.this;
                Function1<ComponentName, Unit> function1 = appAdapter.onAppSelected;
                ControlsServiceInfo controlsServiceInfo2 = appAdapter.listOfServices.get(i);
                Objects.requireNonNull(controlsServiceInfo2);
                ComponentName componentName2 = controlsServiceInfo2.componentName;
                if (componentName2 != null) {
                    str2 = componentName2.flattenToString();
                } else {
                    str2 = null;
                }
                function1.invoke(ComponentName.unflattenFromString(str2));
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        return new Holder(this.layoutInflater.inflate(2131624043, (ViewGroup) recyclerView, false), this.favoritesRenderer);
    }

    public AppAdapter(final Executor executor, final Executor executor2, Lifecycle lifecycle, ControlsListingController controlsListingController, LayoutInflater layoutInflater, Function1 function1, FavoritesRenderer favoritesRenderer, Resources resources) {
        this.layoutInflater = layoutInflater;
        this.onAppSelected = function1;
        this.favoritesRenderer = favoritesRenderer;
        this.resources = resources;
        controlsListingController.observe((androidx.lifecycle.Lifecycle) lifecycle, (Lifecycle) new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1
            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public final void onServicesUpdated(final ArrayList arrayList) {
                Executor executor3 = executor;
                final AppAdapter appAdapter = this;
                final Executor executor4 = executor2;
                executor3.execute(new Runnable() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        final Collator instance = Collator.getInstance(AppAdapter.this.resources.getConfiguration().getLocales().get(0));
                        Comparator appAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1 = new Comparator() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1
                            @Override // java.util.Comparator
                            public final int compare(T t, T t2) {
                                return instance.compare(((ControlsServiceInfo) t).loadLabel(), ((ControlsServiceInfo) t2).loadLabel());
                            }
                        };
                        AppAdapter.this.listOfServices = CollectionsKt___CollectionsKt.sortedWith(arrayList, appAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1);
                        Executor executor5 = executor4;
                        final AppAdapter appAdapter2 = AppAdapter.this;
                        executor5.execute(new Runnable() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                AppAdapter.this.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
    }
}
