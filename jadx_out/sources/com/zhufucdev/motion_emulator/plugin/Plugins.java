package com.zhufucdev.motion_emulator.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import androidx.compose.runtime.IntState;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.SnapshotIntStateKt;
import androidx.compose.runtime.SnapshotStateKt__SnapshotStateKt;
import androidx.compose.runtime.State;
import com.zhufucdev.motion_emulator.extension.PreferencesKt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.fusesource.jansi.AnsiRenderer;

/* JADX INFO: compiled from: Plugins.kt */
/* JADX INFO: loaded from: classes17.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010\"\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010#\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010$\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u0014\u0010%\u001a\u00020\u001e2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R7\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048F@BX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR+\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r8F@BX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\bR\u001e\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0003\u001a\u00020\u0017@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lcom/zhufucdev/motion_emulator/plugin/Plugins;", "", "()V", "<set-?>", "", "Lcom/zhufucdev/motion_emulator/plugin/Plugin;", "available", "getAvailable", "()Ljava/util/List;", "setAvailable", "(Ljava/util/List;)V", "available$delegate", "Landroidx/compose/runtime/MutableState;", "", "countEnabled", "getCountEnabled", "()I", "setCountEnabled", "(I)V", "countEnabled$delegate", "Landroidx/compose/runtime/MutableIntState;", "enabled", "getEnabled", "", "initialized", "getInitialized", "()Z", "prefs", "Landroid/content/SharedPreferences;", "init", "", "context", "Landroid/content/Context;", "loadAvailablePlugins", "notifySettingsChanged", "notifyStart", "notifyStop", "setPriorities", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Plugins {
    private static boolean initialized;
    private static SharedPreferences prefs;
    public static final Plugins INSTANCE = new Plugins();

    /* JADX INFO: renamed from: available$delegate, reason: from kotlin metadata */
    private static final MutableState available = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.emptyList(), null, 2, null);

    /* JADX INFO: renamed from: countEnabled$delegate, reason: from kotlin metadata */
    private static final MutableIntState countEnabled = SnapshotIntStateKt.mutableIntStateOf(0);
    public static final int $stable = 8;

    private Plugins() {
    }

    private final void setAvailable(List<Plugin> list) {
        MutableState $this$setValue$iv = available;
        $this$setValue$iv.setValue(list);
    }

    public final List<Plugin> getAvailable() {
        State $this$getValue$iv = available;
        return (List) $this$getValue$iv.getValue();
    }

    public final boolean getInitialized() {
        return initialized;
    }

    public final void init(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        SharedPreferences sharedPreferences = PreferencesKt.sharedPreferences(context);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "sharedPreferences(...)");
        prefs = sharedPreferences;
        loadAvailablePlugins(context);
        SharedPreferences sharedPreferences2 = prefs;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences2 = null;
        }
        String it = sharedPreferences2.getString("plugins_enabled", "");
        Intrinsics.checkNotNull(it);
        Intrinsics.checkNotNull(it);
        setCountEnabled(StringsKt.isBlank(it) ^ true ? StringsKt.split$default((CharSequence) it, new String[]{AnsiRenderer.CODE_LIST_SEPARATOR}, false, 0, 6, (Object) null).size() : 0);
        initialized = true;
    }

    public final void loadAvailablePlugins(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Iterable installedApplications = context.getPackageManager().getInstalledApplications(128);
        Intrinsics.checkNotNullExpressionValue(installedApplications, "getInstalledApplications(...)");
        Iterable $this$filter$iv = installedApplications;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            ApplicationInfo it = (ApplicationInfo) element$iv$iv;
            boolean z = false;
            if (it.enabled) {
                Bundle bundle = it.metaData;
                if (bundle != null && bundle.getBoolean("me_plugin")) {
                    z = true;
                }
            }
            if (z) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable $this$map$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            ApplicationInfo it2 = (ApplicationInfo) item$iv$iv;
            String packageName = it2.packageName;
            Intrinsics.checkNotNullExpressionValue(packageName, "packageName");
            String string = context.getPackageManager().getApplicationLabel(it2).toString();
            String string2 = it2.metaData.getString("me_description", "");
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            destination$iv$iv2.add(new Plugin(packageName, string, string2));
        }
        setAvailable((List) destination$iv$iv2);
    }

    public final List<Plugin> getEnabled() {
        Object element$iv;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            sharedPreferences = null;
        }
        String string = sharedPreferences.getString("plugins_enabled", "");
        Intrinsics.checkNotNull(string);
        Iterable $this$mapNotNull$iv = StringsKt.split$default((CharSequence) string, new String[]{AnsiRenderer.CODE_LIST_SEPARATOR}, false, 0, 6, (Object) null);
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
            String saved = (String) element$iv$iv$iv;
            Iterable $this$firstOrNull$iv = INSTANCE.getAvailable();
            Iterator it = $this$firstOrNull$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    element$iv = null;
                    break;
                }
                element$iv = it.next();
                Plugin it2 = (Plugin) element$iv;
                if (Intrinsics.areEqual(it2.getPackageName(), saved)) {
                    break;
                }
            }
            Plugin plugin = (Plugin) element$iv;
            if (plugin != null) {
                destination$iv$iv.add(plugin);
            }
        }
        return (List) destination$iv$iv;
    }

    private final void setCountEnabled(int i) {
        MutableIntState $this$setValue$iv = countEnabled;
        $this$setValue$iv.setIntValue(i);
    }

    public final int getCountEnabled() {
        IntState $this$getValue$iv = countEnabled;
        return $this$getValue$iv.getIntValue();
    }

    public final void setPriorities(List<Plugin> enabled) {
        Intrinsics.checkNotNullParameter(enabled, "enabled");
        SharedPreferences $this$edit_u24default$iv = prefs;
        if ($this$edit_u24default$iv == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
            $this$edit_u24default$iv = null;
        }
        SharedPreferences.Editor editor$iv = $this$edit_u24default$iv.edit();
        editor$iv.putString("plugins_enabled", CollectionsKt.joinToString$default(enabled, AnsiRenderer.CODE_LIST_SEPARATOR, null, null, 0, null, new Function1<Plugin, CharSequence>() { // from class: com.zhufucdev.motion_emulator.plugin.Plugins$setPriorities$1$1
            @Override // kotlin.jvm.functions.Function1
            public final CharSequence invoke(Plugin it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return it.getPackageName();
            }
        }, 30, null));
        editor$iv.apply();
        setCountEnabled(enabled.size());
    }

    public final void notifyStart(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Iterable $this$forEach$iv = getEnabled();
        for (Object element$iv : $this$forEach$iv) {
            Plugin it = (Plugin) element$iv;
            it.notifyStart(context);
        }
    }

    public final void notifyStop(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Iterable $this$forEach$iv = getEnabled();
        for (Object element$iv : $this$forEach$iv) {
            Plugin it = (Plugin) element$iv;
            it.notifyStop(context);
        }
    }

    public final void notifySettingsChanged(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Iterable $this$forEach$iv = getAvailable();
        for (Object element$iv : $this$forEach$iv) {
            Plugin it = (Plugin) element$iv;
            it.notifySettingsChanged(context);
        }
    }
}
