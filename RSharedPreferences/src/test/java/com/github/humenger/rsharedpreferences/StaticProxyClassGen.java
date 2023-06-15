package com.github.humenger.rsharedpreferences;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticProxyClassGen {
    public static final String TAG = "StaticProxyClassGen";


    @Test
    public void testGen(){
        System.out.println(new File("./").getAbsolutePath());
        String javaCode=readFile("src/main/java/com/github/humenger/rsharedpreferences/RContextWrapper.java");
       Pattern pattern= Pattern.compile("(public|protected) *?([a-zA-Z0-9_<>?\\.\\[\\]]*?) *?([a-zA-Z0-9_]+?)\\((.*?)\\) *?(.*?) *?\\{([\\s\\S]*?)}");
       Matcher matcher=pattern.matcher(javaCode);
       while (matcher.find()){
           String originalMethod=matcher.group(0);
           String xiushifu=matcher.group(1);
           String returnType=matcher.group(2);
           String methodName=matcher.group(3);
           String params=matcher.group(4);
           String exception=matcher.group(5);
           String body=matcher.group(6);
           System.out.println(xiushifu+" "+returnType+" "+methodName+"("+params+") "+exception+"{"+body+"}");
           if(returnType!=null&&!returnType.trim().isEmpty()){
               System.out.println(returnType);
               StringBuilder newBody= new StringBuilder(" this.base." + methodName + "(");
               if(params!=null&&!params.trim().isEmpty()){
                   String[] paramArray=params.split(",");
                   for (int i=0;i<paramArray.length;i++){
                       String paramName=paramArray[i].trim().split(" ")[paramArray[i].trim().split(" ").length-1];
                       newBody.append(paramName);
                       if(i!=paramArray.length-1){
                           newBody.append(",");
                       }
                   }
               }
               newBody.append(");");
               if(!returnType.equals("void")){
                   newBody.insert(0,"return");
               }
               javaCode=javaCode.replace(originalMethod,xiushifu+" "+returnType+" "+methodName+"("+params+") "+exception+"{"+newBody+"}");
           }
       }
       writeFile("src/main/java/com/github/humenger/rsharedpreferences/RContextWrapper.java",javaCode);
    }
    void writeFile(String path,String text){
        try {
            FileOutputStream target=new FileOutputStream(path);
            ByteArrayInputStream src=new ByteArrayInputStream(text.getBytes("utf-8"));
            byte[] buf=new byte[1024];
            int n=0;
            while ((n=src.read(buf))>0){
                target.write(buf,0,n);
                target.flush();
            }
            target.close();
            src.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String readFile(String path){
        try {
            FileInputStream inputStream=new FileInputStream(new File(path));
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] buf=new byte[1024];
            int n=0;
            while ((n=inputStream.read(buf))>0){
                outputStream.write(buf,0,n);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
            return new String(outputStream.toByteArray(),"utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
