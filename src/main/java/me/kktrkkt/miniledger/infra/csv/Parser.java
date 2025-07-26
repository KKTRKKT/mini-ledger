package me.kktrkkt.miniledger.infra.csv;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface Parser {

    List<Map<String, String>> parse(MultipartFile csvFile);
}
