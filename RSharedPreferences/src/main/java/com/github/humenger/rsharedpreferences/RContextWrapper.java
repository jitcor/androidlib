package com.github.humenger.rsharedpreferences;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RContextWrapper extends Context {
    public static final String TAG = "RContextWrapper";
    private final Context base;
    public RContextWrapper(Context base){
        this.base=base;
    }

    @Override
    public AssetManager getAssets() {return this.base.getAssets();}

    @Override
    public Resources getResources() {return this.base.getResources();}

    @Override
    public PackageManager getPackageManager() {return this.base.getPackageManager();}

    @Override
    public ContentResolver getContentResolver() {return this.base.getContentResolver();}

    @Override
    public Looper getMainLooper() {return this.base.getMainLooper();}

    @Override
    public Context getApplicationContext() {return this.base.getApplicationContext();}

    @Override
    public void setTheme(int resid) { this.base.setTheme(resid);}

    @Override
    public Resources.Theme getTheme() {return this.base.getTheme();}

    @Override
    public ClassLoader getClassLoader() {return this.base.getClassLoader();}

    @Override
    public String getPackageName() {return this.base.getPackageName();}

    @Override
    public ApplicationInfo getApplicationInfo() {return this.base.getApplicationInfo();}

    @Override
    public String getPackageResourcePath() {return this.base.getPackageResourcePath();}

    @Override
    public String getPackageCodePath() {return this.base.getPackageCodePath();}

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return RSharedPreferences.getSharedPreferences(this,name,mode);
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {return this.base.moveSharedPreferencesFrom(sourceContext,name);}

    @Override
    public boolean deleteSharedPreferences(String name) {return this.base.deleteSharedPreferences(name);}

    @Override
    public FileInputStream openFileInput(String name)     throws FileNotFoundException{return this.base.openFileInput(name);}

    @Override
    public FileOutputStream openFileOutput(String name, int mode)     throws FileNotFoundException{return this.base.openFileOutput(name,mode);}

    @Override
    public boolean deleteFile(String name) {return this.base.deleteFile(name);}

    @Override
    public File getFileStreamPath(String name) {return this.base.getFileStreamPath(name);}

    @Override
    public File getDataDir() {return this.base.getDataDir();}

    @Override
    public File getFilesDir() {return this.base.getFilesDir();}

    @Override
    public File getNoBackupFilesDir() {return this.base.getNoBackupFilesDir();}

    @Override
    public File getExternalFilesDir(String type) {return this.base.getExternalFilesDir(type);}

    @Override
    public File[] getExternalFilesDirs(String type) {return this.base.getExternalFilesDirs(type);}

    @Override
    public File getObbDir() {return this.base.getObbDir();}

    @Override
    public File[] getObbDirs() {return this.base.getObbDirs();}

    @Override
    public File getCacheDir() {return this.base.getCacheDir();}

    @Override
    public File getCodeCacheDir() {return this.base.getCodeCacheDir();}

    @Override
    public File getExternalCacheDir() {return this.base.getExternalCacheDir();}

    @Override
    public File[] getExternalCacheDirs() {return this.base.getExternalCacheDirs();}

    @Override
    public File[] getExternalMediaDirs() {return this.base.getExternalMediaDirs();}

    @Override
    public String[] fileList() {return this.base.fileList();}

    @Override
    public File getDir(String name, int mode) {return this.base.getDir(name,mode);}

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {return this.base.openOrCreateDatabase(name,mode,factory);}

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {return this.base.openOrCreateDatabase(name,mode,factory,errorHandler);}

    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {return this.base.moveDatabaseFrom(sourceContext,name);}

    @Override
    public boolean deleteDatabase(String name) {return this.base.deleteDatabase(name);}

    @Override
    public File getDatabasePath(String name) {return this.base.getDatabasePath(name);}

    @Override
    public String[] databaseList() {return this.base.databaseList();}

    @Override
    public Drawable getWallpaper() {return this.base.getWallpaper();}

    @Override
    public Drawable peekWallpaper() {return this.base.peekWallpaper();}

    @Override
    public int getWallpaperDesiredMinimumWidth() {return this.base.getWallpaperDesiredMinimumWidth();}

    @Override
    public int getWallpaperDesiredMinimumHeight() {return this.base.getWallpaperDesiredMinimumHeight();}

    @Override
    public void setWallpaper(Bitmap bitmap)     throws IOException{ this.base.setWallpaper(bitmap);}

    @Override
    public void setWallpaper(InputStream data)     throws IOException{ this.base.setWallpaper(data);}

    @Override
    public void clearWallpaper()     throws IOException{ this.base.clearWallpaper();}

    @Override
    public void startActivity(Intent intent) { this.base.startActivity(intent);}

    @Override
    public void startActivity(Intent intent, Bundle options) { this.base.startActivity(intent,options);}

    @Override
    public void startActivities(Intent[] intents) { this.base.startActivities(intents);}

    @Override
    public void startActivities(Intent[] intents, Bundle options) { this.base.startActivities(intents,options);}

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)     throws IntentSender.SendIntentException{ this.base.startIntentSender(intent,fillInIntent,flagsMask,flagsValues,extraFlags);}

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options)     throws IntentSender.SendIntentException{ this.base.startIntentSender(intent,fillInIntent,flagsMask,flagsValues,extraFlags,options);}

    @Override
    public void sendBroadcast(Intent intent) { this.base.sendBroadcast(intent);}

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) { this.base.sendBroadcast(intent,receiverPermission);}

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) { this.base.sendOrderedBroadcast(intent,receiverPermission);}

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) { this.base.sendOrderedBroadcast(intent,receiverPermission,resultReceiver,scheduler,initialCode,initialData,initialExtras);}

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) { this.base.sendBroadcastAsUser(intent,user);}

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) { this.base.sendBroadcastAsUser(intent,user,receiverPermission);}

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) { this.base.sendOrderedBroadcastAsUser(intent,user,receiverPermission,resultReceiver,scheduler,initialCode,initialData,initialExtras);}

    @Override
    public void sendStickyBroadcast(Intent intent) { this.base.sendStickyBroadcast(intent);}

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) { this.base.sendStickyOrderedBroadcast(intent,resultReceiver,scheduler,initialCode,initialData,initialExtras);}

    @Override
    public void removeStickyBroadcast(Intent intent) { this.base.removeStickyBroadcast(intent);}

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) { this.base.sendStickyBroadcastAsUser(intent,user);}

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) { this.base.sendStickyOrderedBroadcastAsUser(intent,user,resultReceiver,scheduler,initialCode,initialData,initialExtras);}

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) { this.base.removeStickyBroadcastAsUser(intent,user);}

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {return this.base.registerReceiver(receiver,filter);}

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {return this.base.registerReceiver(receiver,filter,flags);}

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {return this.base.registerReceiver(receiver,filter,broadcastPermission,scheduler);}

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler, int flags) {return this.base.registerReceiver(receiver,filter,broadcastPermission,scheduler,flags);}

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) { this.base.unregisterReceiver(receiver);}

    @Override
    public ComponentName startService(Intent service) {return this.base.startService(service);}

    @Override
    public ComponentName startForegroundService(Intent service) {return this.base.startForegroundService(service);}

    @Override
    public boolean stopService(Intent service) {return this.base.stopService(service);}

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {return this.base.bindService(service,conn,flags);}

    @Override
    public void unbindService(ServiceConnection conn) { this.base.unbindService(conn);}

    @Override
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {return this.base.startInstrumentation(className,profileFile,arguments);}

    @Override
    public Object getSystemService(String name) {return this.base.getSystemService(name);}

    @Override
    public String getSystemServiceName(Class<?> serviceClass) {return this.base.getSystemServiceName(serviceClass);}

    @Override
    public int checkPermission(String permission, int pid, int uid) {return this.base.checkPermission(permission,pid,uid);}

    @Override
    public int checkCallingPermission(String permission) {return this.base.checkCallingPermission(permission);}

    @Override
    public int checkCallingOrSelfPermission(String permission) {return this.base.checkCallingOrSelfPermission(permission);}

    @Override
    public int checkSelfPermission(String permission) {return this.base.checkSelfPermission(permission);}

    @Override
    public void enforcePermission(String permission, int pid, int uid, String message) { this.base.enforcePermission(permission,pid,uid,message);}

    @Override
    public void enforceCallingPermission(String permission, String message) { this.base.enforceCallingPermission(permission,message);}

    @Override
    public void enforceCallingOrSelfPermission(String permission, String message) { this.base.enforceCallingOrSelfPermission(permission,message);}

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) { this.base.grantUriPermission(toPackage,uri,modeFlags);}

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) { this.base.revokeUriPermission(uri,modeFlags);}

    @Override
    public void revokeUriPermission(String toPackage, Uri uri, int modeFlags) { this.base.revokeUriPermission(toPackage,uri,modeFlags);}

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {return this.base.checkUriPermission(uri,pid,uid,modeFlags);}

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {return this.base.checkCallingUriPermission(uri,modeFlags);}

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {return this.base.checkCallingOrSelfUriPermission(uri,modeFlags);}

    @Override
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {return this.base.checkUriPermission(uri,readPermission,writePermission,pid,uid,modeFlags);}

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) { this.base.enforceUriPermission(uri,pid,uid,modeFlags,message);}

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) { this.base.enforceCallingUriPermission(uri,modeFlags,message);}

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) { this.base.enforceCallingOrSelfUriPermission(uri,modeFlags,message);}

    @Override
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) { this.base.enforceUriPermission(uri,readPermission,writePermission,pid,uid,modeFlags,message);}

    @Override
    public Context createPackageContext(String packageName, int flags)     throws PackageManager.NameNotFoundException{return this.base.createPackageContext(packageName,flags);}

    @Override
    public Context createContextForSplit(String splitName)     throws PackageManager.NameNotFoundException{return this.base.createContextForSplit(splitName);}

    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {return this.base.createConfigurationContext(overrideConfiguration);}

    @Override
    public Context createDisplayContext(Display display) {return this.base.createDisplayContext(display);}

    @Override
    public Context createDeviceProtectedStorageContext() {return this.base.createDeviceProtectedStorageContext();}

    @Override
    public boolean isDeviceProtectedStorage() {return this.base.isDeviceProtectedStorage();}
}
