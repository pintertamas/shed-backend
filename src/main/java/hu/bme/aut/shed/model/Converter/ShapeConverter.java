package hu.bme.aut.shed.model.Converter;

import hu.bme.aut.shed.model.Shape;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ShapeConverter implements AttributeConverter<Shape, String> {

    @Override
    public String convertToDatabaseColumn(Shape shape) {
        return shape.getName();
    }

    @Override
    public Shape convertToEntityAttribute(String dbData) {
        return Shape.fromName(dbData);
    }
}