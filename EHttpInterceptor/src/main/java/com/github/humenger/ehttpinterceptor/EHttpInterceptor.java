package com.github.humenger.ehttpinterceptor;
/*
created by humenger on 2022/9/22
*/

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EHttpInterceptor {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(2, new SecretKeySpec(new byte[0], "AES"), new GCMParameterSpec(128, new byte[0]));
        CipherInputStream cipherInputStream2 = new CipherInputStream(null, cipher);

    }
}
