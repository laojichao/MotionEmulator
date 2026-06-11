package com.zhufucdev.motion_emulator.data;

import android.content.Context;
import com.autonavi.base.amap.mapcore.AeUtil;
import com.zhufucdev.me.stub.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JvmStreamsKt;

/* JADX INFO: compiled from: DataStore.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\b\b'\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u001b\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00020\u0015¢\u0006\u0002\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\u0018\u0010\u001a\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u001b\u001a\u00020\u0007H\u0086\u0002¢\u0006\u0002\u0010\u001cJ\u0010\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u0002H\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\f\u0010 \u001a\b\u0012\u0004\u0012\u00028\u00000!J\u001d\u0010\"\u001a\u00028\u00002\u0006\u0010#\u001a\u00020\u00072\b\b\u0002\u0010$\u001a\u00020\u0018¢\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u001d\u0010'\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00028\u00002\b\b\u0002\u0010$\u001a\u00020\u0018¢\u0006\u0002\u0010(R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tX¤\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082.¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006)"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/DataStore;", "T", "Lcom/zhufucdev/me/stub/Data;", "", "()V", AeUtil.ROOT_DATA_PATH_OLD_NAME, "Ljava/util/SortedMap;", "", "dataSerializer", "Lkotlinx/serialization/KSerializer;", "getDataSerializer", "()Lkotlinx/serialization/KSerializer;", "rootDir", "Ljava/io/File;", "typeName", "getTypeName", "()Ljava/lang/String;", "delete", "", "record", "context", "Landroid/content/Context;", "(Lcom/zhufucdev/me/stub/Data;Landroid/content/Context;)V", "equals", "", "other", "get", "id", "(Ljava/lang/String;)Lcom/zhufucdev/me/stub/Data;", "getStoreName", "hashCode", "", "list", "", "parseAndStore", "json", "overwrite", "(Ljava/lang/String;Z)Lcom/zhufucdev/me/stub/Data;", "require", "store", "(Lcom/zhufucdev/me/stub/Data;Z)V", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class DataStore<T extends Data> {
    public static final int $stable = 8;
    private final SortedMap<String, T> data = MapsKt.sortedMapOf(new Pair[0]);
    private File rootDir;

    protected abstract KSerializer<T> getDataSerializer();

    public abstract String getTypeName();

    private final String getStoreName(Data data) {
        return getTypeName() + "_" + data.getId() + ".json";
    }

    public final void require(Context context) {
        File file;
        Intrinsics.checkNotNullParameter(context, "context");
        File filesDir = context.getFilesDir();
        Intrinsics.checkNotNullExpressionValue(filesDir, "getFilesDir(...)");
        this.rootDir = filesDir;
        File file2 = this.rootDir;
        File file3 = null;
        if (file2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootDir");
            file2 = null;
        }
        String[] list = file2.list();
        if (list == null) {
            this.data.clear();
            return;
        }
        ArrayList arrayList = new ArrayList();
        int length = list.length;
        boolean z = false;
        int i = 0;
        while (i < length) {
            String str = list[i];
            File file4 = this.rootDir;
            if (file4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rootDir");
                file4 = file3;
            }
            File file5 = new File(file4, str);
            Intrinsics.checkNotNull(str);
            if (!StringsKt.endsWith$default(str, "json", z, 2, (Object) file3)) {
                file = file3;
            } else if (StringsKt.startsWith$default(str, getTypeName(), z, 2, (Object) file3)) {
                String strRemovePrefix = StringsKt.removePrefix(FilesKt.getNameWithoutExtension(file5), (CharSequence) (getTypeName() + "_"));
                arrayList.add(strRemovePrefix);
                if (this.data.containsKey(strRemovePrefix)) {
                    file = file3;
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file5);
                        try {
                            try {
                                Data data = (Data) JvmStreamsKt.decodeFromStream(Json.INSTANCE, getDataSerializer(), fileInputStream);
                                file = null;
                                try {
                                    CloseableKt.closeFinally(fileInputStream, null);
                                    this.data.put(data.getId(), data);
                                } catch (Exception e) {
                                }
                            } catch (Throwable th) {
                                th = th;
                                file = null;
                                Throwable th2 = th;
                                try {
                                    throw th2;
                                } catch (Throwable th3) {
                                    CloseableKt.closeFinally(fileInputStream, th2);
                                    throw th3;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            file = file3;
                        }
                    } catch (Exception e2) {
                        file = file3;
                    }
                }
            } else {
                file = file3;
            }
            i++;
            file3 = file;
            z = false;
        }
        Set<String> setKeySet = this.data.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : setKeySet) {
            if (!arrayList.contains((String) obj)) {
                arrayList2.add(obj);
            }
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            this.data.remove((String) it.next());
        }
    }

    public static /* synthetic */ void store$default(DataStore dataStore, Data data, boolean z, int i, Object obj) throws IOException {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: store");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        dataStore.store(data, z);
    }

    public final void store(T record, boolean overwrite) throws IOException {
        Intrinsics.checkNotNullParameter(record, "record");
        String id = record.getId();
        if (this.data.containsKey(id) && !overwrite) {
            return;
        }
        File file = this.rootDir;
        if (file == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootDir");
            file = null;
        }
        File file2 = new File(file, getStoreName(record));
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        try {
            FileOutputStream stream = fileOutputStream;
            record.writeTo(stream);
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(fileOutputStream, null);
            this.data.put(id, record);
        } finally {
        }
    }

    public static /* synthetic */ Data parseAndStore$default(DataStore dataStore, String str, boolean z, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: parseAndStore");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        return dataStore.parseAndStore(str, z);
    }

    public final T parseAndStore(String json, boolean overwrite) {
        Intrinsics.checkNotNullParameter(json, "json");
        T t = (T) Json.INSTANCE.decodeFromString(getDataSerializer(), json);
        String id = t.getId();
        if (this.data.containsKey(id) && !overwrite) {
            return t;
        }
        File file = this.rootDir;
        if (file == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootDir");
            file = null;
        }
        File file2 = new File(file, getStoreName(t));
        FilesKt.writeText$default(file2, json, null, 2, null);
        this.data.put(id, t);
        return t;
    }

    public final void delete(T record, Context context) {
        Intrinsics.checkNotNullParameter(record, "record");
        Intrinsics.checkNotNullParameter(context, "context");
        context.deleteFile(getStoreName(record));
        this.data.remove(record.getId());
    }

    public final List<T> list() {
        Collection<T> collectionValues = this.data.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues);
    }

    public final T get(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        return this.data.get(id);
    }

    public boolean equals(Object other) {
        return (other instanceof DataStore) && Intrinsics.areEqual(other.getClass(), getClass()) && Intrinsics.areEqual(((DataStore) other).getTypeName(), getTypeName());
    }

    public int hashCode() {
        int result = this.data.hashCode();
        int i = result * 31;
        File file = this.rootDir;
        if (file == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootDir");
            file = null;
        }
        int result2 = i + file.hashCode();
        return (((result2 * 31) + getTypeName().hashCode()) * 31) + getDataSerializer().hashCode();
    }
}
