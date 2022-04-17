package hu.bme.aut.shed.model.Converter;

import hu.bme.aut.shed.model.Rule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RuleConverter implements AttributeConverter<Rule, String> {

    @Override
    public String convertToDatabaseColumn(Rule rule) {
        return rule.getName();
    }

    @Override
    public Rule convertToEntityAttribute(String dbData) {
        return Rule.fromName(dbData);
    }
}


