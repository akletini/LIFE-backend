package akletini.life.core.product.validation.rule;

import akletini.life.core.product.repository.api.attributeType.AttributeTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static akletini.life.core.shared.validation.Errors.ATTRIBUTE_TYPE.ILLEGAL_TYPE_MIGRATION;

@Component
@AllArgsConstructor
public class BasicTypeMigrationRule implements ValidationRule<AttributeType> {

    private final AttributeTypeRepository attributeTypeRepository;

    @Override
    public Optional<BusinessException> validate(AttributeType validatable) {
        Optional<AttributeType> attributeTypeOptional =
                attributeTypeRepository.findById(validatable.getId());
        if (attributeTypeOptional.isPresent()) {
            AttributeType attributeType = attributeTypeOptional.get();
            BasicType oldBasicType = attributeType.getBasicType();
            BasicType newBasicType = validatable.getBasicType();
            Map<BasicType, List<BasicType>> allowedTypeMigrations = getAllowedTypeMigrations();
            for (Map.Entry<BasicType, List<BasicType>> entry : allowedTypeMigrations.entrySet()) {
                if (entry.getKey().equals(oldBasicType)) {
                    if (!entry.getValue().contains(newBasicType)) {
                        return Optional.of(new InvalidDataException(Errors.getError(ILLEGAL_TYPE_MIGRATION, oldBasicType, entry.getValue())));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Map<BasicType, List<BasicType>> getAllowedTypeMigrations() {
        Map<BasicType, List<BasicType>> allowedTypeMigrations = new HashMap<>();
        allowedTypeMigrations.put(BasicType.String, List.of(BasicType.String));
        allowedTypeMigrations.put(BasicType.Date, List.of(BasicType.String, BasicType.Date,
                BasicType.DateTime));
        allowedTypeMigrations.put(BasicType.DateTime, List.of(BasicType.String, BasicType.Date,
                BasicType.DateTime));
        allowedTypeMigrations.put(BasicType.Integer, List.of(BasicType.String, BasicType.Number));
        allowedTypeMigrations.put(BasicType.Number, List.of(BasicType.String, BasicType.Integer));
        return allowedTypeMigrations;
    }
}
