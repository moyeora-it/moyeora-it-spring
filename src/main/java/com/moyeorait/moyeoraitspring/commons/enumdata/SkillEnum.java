package com.moyeorait.moyeoraitspring.commons.enumdata;

import java.util.List;

public enum SkillEnum {
    Java,
    JavaScript,
    HTML_CSS,
    REACT,
    Vue,
    Kotlin,
    Spring;

    public static List<Integer> createIdxList(List<String> skills) {
        return skills.stream()
                .map(skillName -> SkillEnum.valueOf(skillName).ordinal())
                .toList();
    }

    public static List<String> createStringList(List<Integer> skills) {
        return skills.stream()
                .map(index -> String.valueOf(SkillEnum.values()[index])).toList();
    }
}
