package com.zhufucdev.motion_emulator.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Plugin.kt */
/* JADX INFO: loaded from: classes17.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0014\u0010\u0016\u001a\u00020\u0011*\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0003H\u0002R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b¨\u0006\u0018"}, d2 = {"Lcom/zhufucdev/motion_emulator/plugin/Plugin;", "", "packageName", "", "name", "description", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDescription", "()Ljava/lang/String;", "getName", "getPackageName", "equals", "", "other", "hashCode", "", "notifySettingsChanged", "", "context", "Landroid/content/Context;", "notifyStart", "notifyStop", "broadcast", "message", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Plugin {
    public static final int $stable = 0;
    private final String description;
    private final String name;
    private final String packageName;

    public Plugin(String packageName, String name, String description) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        this.packageName = packageName;
        this.name = name;
        this.description = description;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    private final void broadcast(Context $this$broadcast, String message) {
        String target = this.packageName;
        Intent $this$broadcast_u24lambda_u240 = new Intent("com.zhufucdev.broadcast." + message);
        $this$broadcast_u24lambda_u240.setComponent(new ComponentName(target, target + ".ControllerReceiver"));
        $this$broadcast_u24lambda_u240.addFlags(32);
        $this$broadcast.sendBroadcast($this$broadcast_u24lambda_u240);
    }

    public final void notifyStart(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        broadcast(context, "EMULATION_START");
    }

    public final void notifyStop(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        broadcast(context, "EMULATION_STOP");
    }

    public final void notifySettingsChanged(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        broadcast(context, "SETTINGS_CHANGED");
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!Intrinsics.areEqual(getClass(), other != null ? other.getClass() : null)) {
            return false;
        }
        Intrinsics.checkNotNull(other, "null cannot be cast to non-null type com.zhufucdev.motion_emulator.plugin.Plugin");
        if (Intrinsics.areEqual(this.packageName, ((Plugin) other).packageName) && Intrinsics.areEqual(this.name, ((Plugin) other).name)) {
            return Intrinsics.areEqual(this.description, ((Plugin) other).description);
        }
        return false;
    }

    public int hashCode() {
        int result = this.packageName.hashCode();
        return (((result * 31) + this.name.hashCode()) * 31) + this.description.hashCode();
    }
}
