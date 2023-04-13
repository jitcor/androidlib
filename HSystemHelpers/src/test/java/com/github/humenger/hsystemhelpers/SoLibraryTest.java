package com.github.humenger.hsystemhelpers;

import junit.framework.TestCase;

import java.util.regex.Pattern;

public class SoLibraryTest extends TestCase {
    private static final Pattern soPathRegex=Pattern.compile("/data/(app|data)/[ \\.=a-zA-Z0-9-_/~!,|+;:'\"&%\\*\\?\\\\]*lib.*\\.so");

    public void testReadLoadedSoLibrary() {
        System.out.println(soPathRegex.matcher(".coolapk.market 13930    u0_a180   62w      REG              252,1         0     401550 /data/data/com.coolapk.market/app_bugly/jni_log_1565756287068.txt").find());
        System.out.println(soPathRegex.matcher(".coolapk.market 13930    u0_a180   63r      REG              252,1  85957853    1139166 /data/app/com.coolapk.market-Eq3eSk27CGcPN3ExnkK3Ow==/base.apk").find());
        System.out.println(soPathRegex.matcher(".coolapk.market 13930    u0_a180  mem       REG               7,56    245024        128 /apex/com.android.runtime/lib64/libprofile.so").find());
        System.out.println(soPathRegex.matcher(".coolapk.market 13930    u0_a180  mem       REG                8,7    125624       2248 /system/lib64/android.hidl.memory@1.0.so").find());
        System.out.println(soPathRegex.matcher(".coolapk.market 13930    u0_a180   84u      REG              252,1     81920     402714 /data/data/com.coolapk.market/databases/pangle_com.byted.pangle_ttopensdk.db").find());
        System.out.println(soPathRegex.matcher("xg_vip_service 13993    u0_a180  mem       REG              252,1     51120    1139572 /data/data/com.coolapk.ma \\?ket-Eq3eSk27CGcPN3ExnkK3Ow==/lib/arm64/libxgVip Security.so (delete)").find());
    }
}