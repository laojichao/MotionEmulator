package com.zhufucdev.motion_emulator.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.gms.actions.SearchIntents;
import com.zhufucdev.motion_emulator.extension.PreferencesKt;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: SettingsProvider.kt */
/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J1\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0015\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u0016H\u0016¢\u0006\u0002\u0010\u0017J\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u000eH\u0016JO\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u0012\u001a\u00020\u00132\u0010\u0010\u001f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u00162\b\u0010\u0014\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0015\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u00162\b\u0010 \u001a\u0004\u0018\u00010\u0004H\u0016¢\u0006\u0002\u0010!J;\u0010\"\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00042\u0010\u0010\u0015\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0004\u0018\u00010\u0016H\u0016¢\u0006\u0002\u0010#R\u0016\u0010\u0003\u001a\u0004\u0018\u00010\u00048BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006$"}, d2 = {"Lcom/zhufucdev/motion_emulator/provider/SettingsProvider;", "Landroid/content/ContentProvider;", "()V", "method", "", "getMethod", "()Ljava/lang/String;", RtspHeaders.Values.PORT, "", "getPort", "()I", "preferences", "Landroid/content/SharedPreferences;", "tls", "", "getTls", "()Z", "delete", "uri", "Landroid/net/Uri;", "selection", "selectionArgs", "", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "getType", "insert", "values", "Landroid/content/ContentValues;", "onCreate", SearchIntents.EXTRA_QUERY, "Landroid/database/Cursor;", "projection", "sortOrder", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SettingsProvider extends ContentProvider {
    public static final int $stable = 8;
    private SharedPreferences preferences;

    private final int getPort() {
        Integer intOrNull;
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferences");
            sharedPreferences = null;
        }
        String string = sharedPreferences.getString("provider_port", "");
        if (string == null || (intOrNull = StringsKt.toIntOrNull(string)) == null) {
            return 20230;
        }
        return intOrNull.intValue();
    }

    private final boolean getTls() {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferences");
            sharedPreferences = null;
        }
        return sharedPreferences.getBoolean("provider_tls", true);
    }

    private final String getMethod() {
        SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferences");
            sharedPreferences = null;
        }
        String lowerCase = "XPOSED_ONLY".toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return sharedPreferences.getString("method", lowerCase);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Context context = getContext();
        SharedPreferences sharedPreferences = context != null ? PreferencesKt.sharedPreferences(context) : null;
        if (sharedPreferences == null) {
            return false;
        }
        this.preferences = sharedPreferences;
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        switch (SettingsProviderKt.uriMatcher.match(uri)) {
            case 1:
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{RtspHeaders.Values.PORT, "tls"}, 1);
                matrixCursor.addRow(new Integer[]{Integer.valueOf(getPort()), Integer.valueOf(getTls() ? 1 : 0)});
                return matrixCursor;
            case 2:
                MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"method"}, 1);
                matrixCursor2.addRow(new String[]{getMethod()});
                return matrixCursor2;
            default:
                return null;
        }
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return 0;
    }
}
