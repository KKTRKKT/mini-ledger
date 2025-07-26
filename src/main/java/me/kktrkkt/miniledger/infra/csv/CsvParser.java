package me.kktrkkt.miniledger.infra.csv;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvParser implements Parser{

    @Override
    public List<Map<String, String>> parse(MultipartFile csvFile) {
        List<Map<String, String>> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return result; // 빈 파일
            }

            String[] headers = headerLine.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1); // 빈 문자열도 포함

                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i].trim();
                    String value = i < values.length ? values[i].trim() : "";
                    rowMap.put(key, value);
                }

                result.add(rowMap);
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV 파싱 중 오류 발생", e);
        }

        return result;
    }
}
