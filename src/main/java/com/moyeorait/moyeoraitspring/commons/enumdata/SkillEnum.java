package com.moyeorait.moyeoraitspring.commons.enumdata;

import java.util.List;
import java.util.Objects;

public enum SkillEnum {
    Java,
    JavaScript,
    HTML_CSS,
    REACT,
    Vue,
    Kotlin,
    Spring;

    public static List<Integer> createIdxList(List<String> skills) {
        if(skills == null || skills.isEmpty()) return null;
        return skills.stream()
                .map(skillName -> SkillEnum.valueOf(skillName).ordinal())
                .toList();
    }

    public static List<String> createStringList(List<Integer> skills) {
        if(skills == null || skills.isEmpty()) return null;
        return skills.stream()
                .filter(Objects::nonNull)
                .map(index -> String.valueOf(SkillEnum.values()[index])).toList();
    }
}
