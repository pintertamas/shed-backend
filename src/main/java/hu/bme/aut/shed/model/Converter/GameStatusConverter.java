package hu.bme.aut.shed.model.Converter;

import hu.bme.aut.shed.model.GameStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GameStatusConverter implements AttributeConverter<GameStatus, String> {

    @Override
    public String convertToDatabaseColumn(GameStatus status) {
        return status.getShortName();
    }

    @Override
    public GameStatus convertToEntityAttribute(String dbData) {
        return GameStatus.fromShortName(dbData);
    }
}
