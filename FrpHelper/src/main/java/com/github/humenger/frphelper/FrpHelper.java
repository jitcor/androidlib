package com.github.humenger.frphelper;

import android.content.Context;

import androidx.annotation.StringDef;

import com.github.humenger.loglite.LogLite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import libfrp.Libfrp;

public final class FrpHelper {

     public static final String TAG = FrpHelper.class.getSimpleName();

     @FrpVersion
     private final String frpVer;

     private FrpHelper(@FrpVersion String version) {
          this.frpVer = version;
     }

     public static FrpHelper with(@FrpVersion String version) {
          return new FrpHelper(version);
     }

     public Throwable startClientFormTextConfig(Context context, String frpConfig) {
          try {
               File file = writeFiles(context, frpConfig.getBytes(StandardCharsets.UTF_8));
               if (file != null) {
                    LogLite.tag(TAG).i("frpc start...");
                    Libfrp.runFrpc(file.getAbsolutePath(), frpVer);
                    LogLite.tag(TAG).i("frpc stop...");
               }
          } catch (Throwable e) {
               return e;
          }
          return null;
     }

     public Throwable startClientFormAssetsConfig(Context context, String fileName) {
          try {
               File file = writeFiles(context, assets(context, fileName));
               if (file != null) {
                    LogLite.tag(TAG).i("frpc start...");
                    Libfrp.runFrpc(file.getAbsolutePath(), frpVer);
                    LogLite.tag(TAG).i("frpc stop...");
               }
          } catch (Throwable e) {
               return e;
          }
          return null;
     }

     public Throwable startServerFormTextConfig(Context context, String frpConfig) {
          try {
               File file = writeFiles(context, frpConfig.getBytes(StandardCharsets.UTF_8));
               if (file != null) {
                    LogLite.tag(TAG).i("frps start...");
                    Libfrp.runFrps(file.getAbsolutePath(), frpVer);
                    LogLite.tag(TAG).i("frps stop...");
               }
          } catch (Throwable e) {
               return e;
          }
          return null;
     }

     public Throwable startServerFormAssetsConfig(Context context, String fileName) {
          try {
               File file = writeFiles(context, assets(context, fileName));
               if (file != null) {
                    LogLite.tag(TAG).i("frps start...");
                    Libfrp.runFrps(file.getAbsolutePath(), frpVer);
                    LogLite.tag(TAG).i("frps stop...");
               }
          } catch (Throwable e) {
               return e;
          }
          return null;
     }

     static byte[] assets(Context context, String fileName) throws IOException {
          try (InputStream inputStream = context.getAssets().open(fileName)) {
               ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
               byte[] buff = new byte[100];
               int rc = 0;
               while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
               }
               return swapStream.toByteArray();
          }
     }

     static File writeFiles(Context context, byte[] data) throws IOException {
          try (FileOutputStream fileOutputStream = new FileOutputStream(context.getFileStreamPath("frpc.ini"), false)) {
               ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
               byte[] buffer = new byte[1024];
               int len = 0;
               while ((len = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                    fileOutputStream.flush();
               }
               fileOutputStream.close();
               inputStream.close();
               return context.getFileStreamPath("frpc.ini");
          }
     }


     public static final String VERSION_0_28_2 = "0.28.2";
     public static final String VERSION_0_29_0 = "0.29.0";
     public static final String VERSION_0_30_0 = "0.30.0";
     public static final String VERSION_0_31_2 = "0.31.2";
     public static final String VERSION_0_32_1 = "0.32.1";
     public static final String VERSION_0_35_1 = "0.35.1";
     public static final String VERSION_0_36_2 = "0.36.2";
     public static final String VERSION_0_38_0 = "0.38.0";
     public static final String VERSION_0_39_1 = "0.39.1";
     public static final String VERSION_0_42_0 = "0.42.0";

     @StringDef({VERSION_0_28_2, VERSION_0_29_0, VERSION_0_30_0, VERSION_0_31_2, VERSION_0_32_1, VERSION_0_35_1, VERSION_0_36_2, VERSION_0_38_0, VERSION_0_39_1, VERSION_0_42_0})
     public @interface FrpVersion {
     }
}
