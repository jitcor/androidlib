package com.github.humenger.hsystemhelpers;/*
created by humenger on 2022/10/13
*/

import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class SoLibrary {
    private static final Pattern soPathRegex=Pattern.compile("/data/(app|data)/[ \\.=a-zA-Z0-9-_/~!,|+;:'\"&%\\*\\?\\\\]*lib.*\\.so");

     public static List<File> readLoadedSoLibrary(boolean onlyApp) {
         List<File> soFiles=new ArrayList<>();
         int pid = Process.myPid();
         String path = "/proc/" + pid + "/maps";
         File file = new File(path);
         if(file.exists() && file.isFile()){
             try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                 String tempString = null;
                 // 一次读入一行，直到读入null为文件结束
                 while ((tempString = reader.readLine()) != null) {
                     if (tempString.endsWith(".so")) {
                         int index = tempString.indexOf("/");
                         if (index != -1) {
                             String str = tempString.substring(index);
                             // 所有so库（包括系统的，即包含/system/目录下的）
                             if (onlyApp) {
                                 if (soPathRegex.matcher(str).find()) {
                                     soFiles.add(new File(str));
                                 }
                             } else {
                                 soFiles.add(new File(str));
                             }

                         }
                     }
                 }
                 reader.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         return soFiles;
    }

}
