package com.moyeorait.moyeoraitspring.commons.enumdata;

import java.util.List;

public enum PositionEnum {
    PM,
    PL,
    AA,
    TA,
    DA,
    QA,
    FE,
    BE,
    FS;

    public static List<Integer> createIdxList(List<String> positions) {
        if(positions == null || positions.isEmpty()) return null;
        return positions.stream()
                .map(positionName -> PositionEnum.valueOf(positionName).ordinal())
                .toList();
    }

    public static List<String> createStringList(List<Integer> position) {
        if(position == null || position.isEmpty()) return null;
        return position.stream()
                .map(index -> String.valueOf(PositionEnum.values()[index])).toList();
    }
}
