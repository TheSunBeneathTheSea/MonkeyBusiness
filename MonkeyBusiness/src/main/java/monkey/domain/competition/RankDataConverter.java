package monkey.domain.competition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class RankDataConverter implements AttributeConverter<List<RankingData>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<RankingData> attribute) {
        String serialized = null;
        try {
            serialized = objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    @Override
    public List<RankingData> convertToEntityAttribute(String dbData) {
        List<RankingData> deserialized = null;
        try {
            deserialized = Arrays.asList(objectMapper.readValue(dbData, RankingData[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return deserialized;
    }
}
