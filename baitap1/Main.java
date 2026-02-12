package baitap1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap duong dan file nguon (source): ");
        String sourcePathStr = sc.nextLine().trim();

        System.out.print("Nhap duong dan file dich (target): ");
        String targetPathStr = sc.nextLine().trim();

        Path sourcePath = Paths.get(sourcePathStr);
        Path targetPath = Paths.get(targetPathStr);


        if (!Files.exists(sourcePath) || !Files.isRegularFile(sourcePath)) {
            System.out.println("Loi: File nguon khong ton tai hoac khong phai file!");
            return;
        }


        if (Files.exists(targetPath)) {
            System.out.print("Canh bao: File dich da ton tai. Ban co muon ghi de? (Y/N): ");
            String ans = sc.nextLine().trim();
            if (!ans.equalsIgnoreCase("Y")) {
                System.out.println("Da huy thao tac copy.");
                return;
            }
        } else {

            Path parent = targetPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                try {
                    Files.createDirectories(parent);
                } catch (IOException e) {
                    System.out.println("Loi: Khong tao duoc thu muc dich!");
                    return;
                }
            }
        }


        long charCount = 0;
        try (Reader reader = Files.newBufferedReader(sourcePath, StandardCharsets.UTF_8);
             Writer writer = Files.newBufferedWriter(
                     targetPath,
                     StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING,
                     StandardOpenOption.WRITE
             )) {

            char[] buffer = new char[4096];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
                charCount += n;
            }

            writer.flush();
            System.out.println("Copy thanh cong!");
            System.out.println("So ky tu da copy: " + charCount);

        } catch (IOException e) {
            System.out.println("Loi IO: " + e.getMessage());
        }
    }
}
