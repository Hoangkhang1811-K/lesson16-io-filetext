package baitap2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap duong dan file CSV: ");
        String pathStr = sc.nextLine().trim();
        Path path = Paths.get(pathStr);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            System.out.println("Loi: File khong ton tai!");
            return;
        }

        List<Country> countries = readCountries(path);

        System.out.println("Danh sach quoc gia:");
        for (Country c : countries) {
            System.out.println(c);
        }
    }

    private static List<Country> readCountries(Path path) {
        List<Country> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // tách CSV thành 3 cột: id, code, name
                List<String> cols = parseCsvLine(line);
                if (cols.size() != 3) continue; // hoặc báo lỗi

                int id = Integer.parseInt(stripQuotes(cols.get(0)).trim());
                String code = stripQuotes(cols.get(1)).trim();
                String name = stripQuotes(cols.get(2)).trim();

                list.add(new Country(id, code, name));
            }
        } catch (IOException e) {
            System.out.println("Loi doc file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Loi du lieu ID khong hop le!");
        }

        return list;
    }

    // Parse 1 dòng CSV đơn giản có hỗ trợ dấu ngoặc kép
    // Ví dụ: 1,"AU","Australia"
    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
                cur.append(ch); // giữ lại dấu " để lát strip
            } else if (ch == ',' && !inQuotes) {
                result.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        result.add(cur.toString());
        return result;
    }

    private static String stripQuotes(String s) {
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}

