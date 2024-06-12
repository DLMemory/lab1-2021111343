package lab1;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MyUtils {
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static String[] readFile(String fileName) {
        ArrayList<String> wordList = new ArrayList<>();
        List<String> safeDirectories = Arrays.asList("Output", ".");
        File temp = new File(fileName);
        String parent = temp.getParent();
        if (safeDirectories.contains(parent)) {
            try {
                File file = new File(fileName);
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                Scanner scanner = new Scanner(reader);

                // 使用正则表达式替换非字母字符为空格，并且分割成单词
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().toLowerCase();
                    line = line.replaceAll("[^a-zA-Z]", " ");
                    String[] words = line.split("\\s+");
                    wordList.addAll(Arrays.asList(words));
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            throw new IllegalArgumentException("The specified file is not in a safe directory.");
        }
        return wordList.toArray(new String[0]);

    }

    @SuppressFBWarnings({"PATH_TRAVERSAL_IN", "PATH_TRAVERSAL_OUT"})
    public static void writeWalkToFile(String walk, String path) {

        List<String> safeDirectories = Arrays.asList(".\\output");
        File file = new File(path);
        String parent = file.getParent();
        if (safeDirectories.contains(parent)) {
            // 写入文件
            try {
                OutputStream outputStream = new FileOutputStream(path);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
                java.io.PrintWriter output = new java.io.PrintWriter(writer);
                output.print(walk);
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(parent);
            throw new IllegalArgumentException("The specified file is not in a safe directory.");
        }
    }

    // 检查文件夹是否存在，若不存在则创建。
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static void checkAndCreateFolder(String path) {
        List<String> safeDirectories = Arrays.asList("Output", ".");
        File temp = new File(path);
        String parent = temp.getParent();
        if (safeDirectories.contains(parent)) {
            File folder = new File(path);
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                System.out.println(created ? "文件夹\"" + path + "\"创建成功。" : "文件夹\"" + path + "\"创建失败。");
            } else {
                System.out.println("文件夹\"" + path + "\"已存在。");
            }
        }else {
            throw new IllegalArgumentException("The specified file is not in a safe directory.");
        }
    }
}