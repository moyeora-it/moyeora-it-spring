package com.moyeorait.moyeoraitspring.domain.group.repository.converter;

import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SkillListConverter implements AttributeConverter<List<Skill>, String[]> {

    @Override
    public String[] convertToDatabaseColumn(List<Skill> attribute) {
        if(attribute == null || attribute.isEmpty()) return new String[0];
        else return attribute.stream()
                .map(Skill::name) // enum → string
                .toArray(String[]::new);
    }

    @Override
    public List<Skill> convertToEntityAttribute(String[] dbData) {
        if(dbData == null || dbData.length == 0) return List.of();
        else return Arrays.stream(dbData)
                .map(Skill::valueOf) // string → enum
                .collect(Collectors.toList());
    }
}
