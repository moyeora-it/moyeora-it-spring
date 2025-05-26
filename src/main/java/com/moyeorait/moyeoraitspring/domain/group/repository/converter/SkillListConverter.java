package com.moyeorait.moyeoraitspring.domain.group.repository.converter;

import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SkillListConverter implements AttributeConverter<List<Skill>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Skill> attribute) {
        if(attribute == null || attribute.isEmpty()) return null;
        else return attribute.stream().map(Enum::name).collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Skill> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) return List.of();
        else return Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .map(Skill::valueOf)
                .collect(Collectors.toList());
    }
}
