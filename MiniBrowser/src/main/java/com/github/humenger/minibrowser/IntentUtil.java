package com.github.humenger.minibrowser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

class IntentUtil {

//分享.zip文件

    public static void openShareWithZip(Context con, String path) {

        openIntent(con, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK, path, "application/x-gzip");

    }


    public static void openQQPerson(Context con, String qqpersonnum) {

        openQQ(con, qqpersonnum, 0);

    }

    public static void openQQGroup(Context con, String qqgroupnum) {
        openQQ(con, qqgroupnum, 1);
    }

    static void openQQ(Context con, String qqnum, int type) {
        openIntent(con, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK, "mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + qqnum + "&card_type=" + (type == 0 ? "person" : "group") + "&source=external", "");
    }
//mqqapi://card/show_pslcard?src_type=internal&version=1&uin=2664487933&card_type=person&source=external
    public static void openUrl(Context con, String url) {
        openIntent(con, Intent.ACTION_VIEW, 0, url, "");
    }

    public static void openShare(Context con, String msg) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);

        sendIntent.setType("text/plain");

        con.startActivity(Intent.createChooser(sendIntent, "分享"));

    }

    public static void openMarket(Context con, String pkgname) {

        openIntent(con, Intent.ACTION_VIEW, 0, "market://details?id=" + pkgname, "");

    }

    public static boolean openCoolApkMarket(Context con, String pkgname) {

        return openIntentNotTips(con, Intent.ACTION_VIEW, 0, "coolmarket://apk/" + pkgname, "");

    }

    public static void openApp(Context con, String url) {

        openIntent(con, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK, url, "");

    }

    public static void openAlipay(Context con, String path) {
        openIntent(con, Intent.ACTION_VIEW, Intent.FLAG_ACTIVITY_NEW_TASK, "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + path, "");
    }

    public static void openBrowser(Context con, String path) {

        openIntent(con, Intent.ACTION_VIEW, 0, path, "");

    }

    public static void openVideoPlayer(Context con, String videopath) {

        openIntent(con, Intent.ACTION_VIEW, 0, videopath, "video/*");

    }


    public static void openIntent(Context con, String action, int flags, String path, String type) {

        Intent intent = new Intent(action);

        if (flags != 0) {

            intent.setFlags(flags);

        }

        if (type.equals("")) {

            intent.setData(Uri.parse(path));

        } else {

            intent.setDataAndType(Uri.parse(path), type);

        }

        try {

            con.startActivity(intent);
        } catch (Exception e) {

            Toast.makeText(con, "不存在可供跳转的应用！", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean openIntentNotTips(Context con, String action, int flags, String path, String type) {

        Intent intent = new Intent(action);

        if (flags != 0) {

            intent.setFlags(flags);

        }

        if (type.equals("")) {

            intent.setData(Uri.parse(path));

        } else {

            intent.setDataAndType(Uri.parse(path), type);

        }

        try {

            con.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

