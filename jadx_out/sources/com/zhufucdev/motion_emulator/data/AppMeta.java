package com.zhufucdev.motion_emulator.data;

import android.graphics.drawable.Drawable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppMeta.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B!\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J+\u0010\u0010\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b¨\u0006\u0018"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/AppMeta;", "", "name", "", "icon", "Landroid/graphics/drawable/Drawable;", "packageName", "(Ljava/lang/String;Landroid/graphics/drawable/Drawable;Ljava/lang/String;)V", "getIcon", "()Landroid/graphics/drawable/Drawable;", "getName", "()Ljava/lang/String;", "getPackageName", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class AppMeta {
    private final Drawable icon;
    private final String name;
    private final String packageName;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public static /* synthetic */ AppMeta copy$default(AppMeta appMeta, String str, Drawable drawable, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = appMeta.name;
        }
        if ((i & 2) != 0) {
            drawable = appMeta.icon;
        }
        if ((i & 4) != 0) {
            str2 = appMeta.packageName;
        }
        return appMeta.copy(str, drawable, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Drawable getIcon() {
        return this.icon;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    public final AppMeta copy(String name, Drawable icon, String packageName) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        return new AppMeta(name, icon, packageName);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AppMeta)) {
            return false;
        }
        AppMeta appMeta = (AppMeta) other;
        return Intrinsics.areEqual(this.name, appMeta.name) && Intrinsics.areEqual(this.icon, appMeta.icon) && Intrinsics.areEqual(this.packageName, appMeta.packageName);
    }

    public int hashCode() {
        return ((((this.name == null ? 0 : this.name.hashCode()) * 31) + (this.icon != null ? this.icon.hashCode() : 0)) * 31) + this.packageName.hashCode();
    }

    public String toString() {
        return "AppMeta(name=" + this.name + ", icon=" + this.icon + ", packageName=" + this.packageName + ")";
    }

    /* JADX INFO: compiled from: AppMeta.kt */
    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\t"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/AppMeta$Companion;", "", "()V", "of", "Lcom/zhufucdev/motion_emulator/data/AppMeta;", "app", "Landroid/content/pm/ApplicationInfo;", "pm", "Landroid/content/pm/PackageManager;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0039  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final com.zhufucdev.motion_emulator.data.AppMeta of(android.content.pm.ApplicationInfo r5, android.content.pm.PackageManager r6) {
            /*
                r4 = this;
                java.lang.String r0 = "app"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                java.lang.String r0 = "pm"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
                int r0 = r5.labelRes
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
                r1 = r0
                java.lang.Number r1 = (java.lang.Number) r1
                int r1 = r1.intValue()
                r2 = 0
                if (r1 == 0) goto L1d
                r3 = 1
                goto L1e
            L1d:
                r3 = 0
            L1e:
                r1 = 0
                if (r3 == 0) goto L22
                goto L23
            L22:
                r0 = r1
            L23:
                if (r0 == 0) goto L39
                java.lang.Number r0 = (java.lang.Number) r0
                int r0 = r0.intValue()
                r2 = 0
                java.lang.String r3 = r5.packageName
                java.lang.CharSequence r0 = r6.getText(r3, r0, r5)
                if (r0 == 0) goto L39
                java.lang.String r0 = r0.toString()
                goto L3a
            L39:
                r0 = r1
            L3a:
                android.graphics.drawable.Drawable r1 = r6.getApplicationIcon(r5)     // Catch: android.content.res.Resources.NotFoundException -> L40
                goto L42
            L40:
                r2 = move-exception
            L42:
                java.lang.String r2 = r5.packageName
                java.lang.String r3 = "packageName"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)
                com.zhufucdev.motion_emulator.data.AppMeta r3 = new com.zhufucdev.motion_emulator.data.AppMeta
                r3.<init>(r0, r1, r2)
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zhufucdev.motion_emulator.data.AppMeta.Companion.of(android.content.pm.ApplicationInfo, android.content.pm.PackageManager):com.zhufucdev.motion_emulator.data.AppMeta");
        }
    }

    public AppMeta(String name, Drawable icon, String packageName) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPackageName() {
        return this.packageName;
    }
}
