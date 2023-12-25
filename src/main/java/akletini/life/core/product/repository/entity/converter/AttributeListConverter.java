package akletini.life.core.product.repository.entity.converter;

import akletini.life.core.product.repository.entity.Attribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Converter
@Log4j2
public class AttributeListConverter implements AttributeConverter<List<Attribute>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<Attribute>> attributeMapTypeRef = new TypeReference<>() {
    };

    public AttributeListConverter() {
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convertToDatabaseColumn(List<Attribute> attributes) {
        if (CollectionUtils.isEmpty(attributes)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            log.error("Could not convert attributes list to JSON String", e);
            return null;
        }
    }

    @Override
    public List<Attribute> convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData)) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(dbData, attributeMapTypeRef);
        } catch (JsonProcessingException e) {
            log.error("Could not convert DB-String to attribute list", e);
            return new ArrayList<>();
        }
    }
}
