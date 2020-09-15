package tools;

import com.google.gson.Gson;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ReadFile {
    /*按流读文件，把所有的字节都读到一个字节数组里，
    然后每两个字节转换成一个int，将Int数组return*/
    public static List<Integer> readDataFile(String filePath){
        List<Integer> x = new LinkedList<>();//定义链表x;
        try {
            InputStream reader = new FileInputStream(filePath);
            byte[] b = new byte[reader.available()];//创建一个数组b，数组长度为文件的总byte数

            //从reader中读取字节到b数组中，返回类型为int,指的是读取的总长度
            int len = reader.read(b);

            //将两个byte合并为一个int
            for (int i = 0; i < len; i+=2) {
                if(i==len-1){
                    x.add(b[i] << 8);
                }
                else{
                    x.add((b[i] << 8) + b[i + 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    //读取配置文件（输入绝对路径）
    //按字符读，读完全部的文件，然后用gson包将这个配置文件映射成setting类
    public static Setting readSettingFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            char[] buffer = new char[1024];
            int len = 1;

            String ss = "";
            while ((len = fileReader.read(buffer)) != -1) { //文件未读完
                String s = new String(buffer, 0, len);
                ss = ss + s;
            }

            return new Gson().fromJson(ss,Setting.class);//用gson包将这个配置文件映射成setting类
        } catch (IOException e) {
            return new Setting();
        }
    }
}


