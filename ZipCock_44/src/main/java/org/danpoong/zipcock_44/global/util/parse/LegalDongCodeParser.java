package org.danpoong.zipcock_44.global.util.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LegalDongCodeParser {
    public static Map<String, String> parseLegalDongCode() {
        Map<String, String> bupjeongdongCodeMap = new HashMap<>();
        try (InputStream inputStream = LegalDongCodeParser.class.getResourceAsStream("/static/legaldongcode.md");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String code = parts[1].trim();
                    bupjeongdongCodeMap.put(name, code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bupjeongdongCodeMap;
    }
}
