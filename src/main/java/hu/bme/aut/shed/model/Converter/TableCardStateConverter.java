package hu.bme.aut.shed.model.Converter;

import hu.bme.aut.shed.model.TableCardState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TableCardStateConverter implements AttributeConverter<TableCardState, String> {

    @Override
    public String convertToDatabaseColumn(TableCardState tableCardState) {
        return tableCardState.getName();
    }

    @Override
    public TableCardState convertToEntityAttribute(String dbData) {
        return TableCardState.fromName(dbData);
    }
}
