package hu.bme.aut.shed.model.Converter;

import hu.bme.aut.shed.model.PlayerCardState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PlayerCardStateConverter implements AttributeConverter<PlayerCardState, String> {

    @Override
    public String convertToDatabaseColumn(PlayerCardState playerCardState) {
        return playerCardState.getName();
    }

    @Override
    public PlayerCardState convertToEntityAttribute(String dbData) {
        return PlayerCardState.fromName(dbData);
    }
}
