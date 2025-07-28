package me.kktrkkt.miniledger.infra.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ParserTest {

    private static Parser parser;

    @BeforeAll
    static void setUp() {
        parser = new CsvParser();
    }

    @DisplayName("CSV 파일이 null일때 예외 발생")
    @Test
    void parse_nullInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }

    @DisplayName("CSV 파일이 비어있을 때 빈 리스트 반환")
    @Test
    void parse_emptyFile_returnsEmptyList() {
        var emptyFile = new MockMultipartFile("file", "empty.csv", "text/csv", "".getBytes());
        assertThrows(IllegalArgumentException.class, () -> parser.parse(emptyFile));
    }

    @DisplayName("CSV 파일이 올바르게 파싱되는지 확인")
    @Test
    void parse_validCsv_returnsParsedData() {
        String csvContent = "name,age,city\nAlice,30,New York\nBob,25,Los Angeles";
        var csvFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        var result = parser.parse(csvFile);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).get("name"));
        assertEquals("30", result.get(0).get("age"));
        assertEquals("New York", result.get(0).get("city"));
        assertEquals("Bob", result.get(1).get("name"));
        assertEquals("25", result.get(1).get("age"));
        assertEquals("Los Angeles", result.get(1).get("city"));
    }

    @DisplayName("CSV 파일의 헤더와 값의 개수가 달라도 정상 파싱")
    @Test
    void parse_differentHeaderAndValueCount_returnsParsedData() {
        String csvContent = "name,age,city\nAlice,30,New York,extra\nBob,25";
        var csvFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        var result = parser.parse(csvFile);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).get("name"));
        assertEquals("30", result.get(0).get("age"));
        assertEquals("New York", result.get(0).get("city"));
        assertEquals("Bob", result.get(1).get("name"));
        assertEquals("25", result.get(1).get("age"));
        assertEquals("", result.get(1).get("city")); // city 컬럼은 비어있음
        //extra 컬럼은 무시됨
        assertFalse(result.get(0).containsKey("extra")); // extra 컬럼은 존재하지 않음
    }
}