package com.warner.common.app.utils;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by warner on 2018/1/10.
 */

public class StreamUtil {

    /**
     * Copy文件
     * @param in 文件
     * @param outputStream 输出流
     * @return 是否Copy成功
     */
    public static boolean copy(File in, OutputStream outputStream) {
        if (!in.exists()) {
            return false;
        }

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, outputStream);
    }

    /**
     * Copy 文件
     * @param in 源文件
     * @param out 目标文件
     * @return
     */
    public static boolean copy(File in, File out) {
        if (!in.exists()) {
            return false;
        }

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, out);
    }

    /**
     * 输入流输出到文件
     * @param inputStream
     * @param out
     * @return
     */
    public static boolean copy (InputStream inputStream, File out) {
        if (!out.exists()) {
            File fileParentDir = out.getParentFile();
            if (!fileParentDir.exists()) {
                if (!fileParentDir.mkdirs()) {
                    return false;
                }
            }

            try {
                if (!out.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(inputStream, outputStream);
    }

    /**
     * 将输入流转换为输出流
     * @param inputStream
     * @param outputStream
     * @return
     */
    private static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024];
            int realLength;
            while ((realLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, realLength);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream);
            close(outputStream);
        }

    }

    /**
     * 对流进行closs
     * @param closeables
     */
    public static void close (Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对文件进行删除
     * @param path
     * @return
     */
    public static boolean delete(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
