package com.github.humenger.android13lib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.github.humenger.rsharedpreferences.RSharedPreferences;
import com.tonyodev.fetch2.AbstractFetchListener;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processPermissions();
//        RSharedPreferences.getSharedPreferences(this,"www", Context.MODE_WORLD_READABLE);
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setHttpDownloader(new OkHttpDownloader(getOkHttpClientInstance()))
                .setDownloadConcurrentLimit(3)
                .build();

        Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        String url = "https://gh.wget.cool/https://github.com/frida/frida/releases/download/14.2.8/frida-gadget-14.2.8-android-arm64.so.xz";
        String file = new File(getExternalCacheDir(),"/test.txt").getAbsolutePath();


        final Request request = new Request(url, file);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
//        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
        fetch.addListener(new AbstractFetchListener() {
            @Override
            public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {
                super.onError(download, error, throwable);
                error.getThrowable().printStackTrace();
            }
        });
        fetch.enqueue(request, updatedRequest -> {
            //Request was successfully enqueued for download.
            Log.d(TAG, "onCreate: "+request);
            Log.d(TAG, "onCreate: "+updatedRequest);
        }, error -> {
            //An error occurred enqueuing the request.
            Objects.requireNonNull(error.getThrowable()).printStackTrace();
        });

    }
    //2017-12-5：更新添加6.0的动态请求权限
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;

    protected void processPermissions() {
        //动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS);
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
    //客户端不对服务器证书做任何验证
    static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }
    //获得无需验证任何证书的OkHttpClient实例对象
    public static OkHttpClient getOkHttpClientInstance(){
        try{
            return new okhttp3.OkHttpClient.Builder()
                    .sslSocketFactory(getSSLSocketFactory())
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
        }catch (Exception e){
            Log.e("OkHttpClientError", e.getMessage());
        }
        return null;
    }


}