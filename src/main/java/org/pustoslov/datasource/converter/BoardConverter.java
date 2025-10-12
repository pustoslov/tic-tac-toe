package org.pustoslov.datasource.converter;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardConverter implements AttributeConverter<List<List<Integer>>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<List<Integer>> board) {
    try {
      return objectMapper.writeValueAsString(board); // Конвертируем в JSON строку
    } catch (Exception e) {
      throw new RuntimeException("Error converting board to JSON", e);
    }
  }

  @Override
  public List<List<Integer>> convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData,
              objectMapper.getTypeFactory().constructCollectionType(List.class,
                      objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class)));
    } catch (Exception e) {
      throw new RuntimeException("Error converting JSON to board", e);
    }
  }
}
