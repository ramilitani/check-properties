package br.com.rmsystems.utils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtils {

    public static File loadResource(String name)
    {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Must have a resource file");
        }

        try {
            ClassLoader classLoader = FileUtils.class.getClassLoader();
            URL resource = classLoader.getResource(name);
            if (resource != null) {
                String path = URLDecoder.decode(resource.getPath(), "utf-8");
                return new File(path);
            } else {
                throw new Exception("resource file: " + name + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInFile(File file, String text)
    {
        try (FileWriter writer = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(writer)){
            bw.write(text);
            bw.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("error writing on log file");
        }
    }

    public static void cleanLogFile(String file)
    {
        File logFile = FileUtils.loadResource(file);
        if (logFile.exists()) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(logFile, "rw");
                raf.setLength(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
