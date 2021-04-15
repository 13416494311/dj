package com.ruoyi.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipPackUtil {

    public static void main(String[] args) throws Exception {
        /*String text = "http://www.baidu.com";  //这里设置自定义网站url
        String logoPath = "D:\\test\\test.png";
        String destPath = "D:\\test\\";
        System.out.println(QRCodeUtils.encode(text, logoPath, destPath, true));*/

        Map<String, List<File>> map = new HashMap<>();


        File source = new File("D:\\test\\test.png");
        List<File> fileList = new ArrayList<File>();
        for (int i = 0; i < 2; i++) {
            File dest = new File(i + ".png");
            FileUtils.copyFile(source, dest);
            fileList.add(dest);
        }
        map.put("123", fileList);
        map.put(ACTIVITY_PLAN_FILE, fileList);
        zip(map,"zipTest");
    }


    public static final String ACTIVITY_PLAN_FILE = "ACTIVITY_PLAN_FILE";
    public static final String ACTIVITY_DETAILED_PIC_FILE = "ACTIVITY_DETAILED_PIC_FILE";
    public static final String ACTIVITY_DETAILED_FILE = "ACTIVITY_DETAILED_FILE";
    public static final String ROOT = "ROOT";

    public static File zip(Map<String, List<File>> fileListMap,String zipName) throws IOException {
        File zipFile = null;

        zipFile = File.createTempFile(zipName, ".zip");
        FileOutputStream f = new FileOutputStream(zipFile);
        /**
         * 作用是为任何OutputStream产生校验和
         * 第一个参数是制定产生校验和的输出流，第二个参数是指定Checksum的类型 （Adler32（较快）和CRC32两种）
         */
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(f, new Adler32());
        // 用于将数据压缩成Zip文件格式
        ZipOutputStream zos = new ZipOutputStream(checkedOutputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zos);

        fileListMap.keySet().forEach(key -> {
            String dir = "";
            switch (key) {
                case ROOT:
                    dir = "";
                    break;
                case ACTIVITY_PLAN_FILE:
                    dir = "活动计划\\";
                    break;
                case ACTIVITY_DETAILED_PIC_FILE:
                    dir = "活动详情\\活动图片\\";
                    break;
                case ACTIVITY_DETAILED_FILE:
                    dir = "活动详情\\活动资料\\";
                    break;
                default:
                    dir = "其他\\";
                    break;
            }
            if (StringUtils.isNotNull(fileListMap.get(key))) {
                String finalDir = dir;
                fileListMap.get(key).stream().forEach(file -> {
                    try {
                        zos.putNextEntry(new ZipEntry(finalDir  + file.getName()));

                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        int bytesRead = 0;
                        // 向压缩文件中输出数据
                        while ((bytesRead = bufferedInputStream.read()) != -1) {
                            bufferedOutputStream.write(bytesRead);
                        }
                        bufferedInputStream.close();
                        zos.closeEntry();
                        //文件删除
                        if(file.exists()){
                            file.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        zos.close();

        return zipFile;
    }
}
