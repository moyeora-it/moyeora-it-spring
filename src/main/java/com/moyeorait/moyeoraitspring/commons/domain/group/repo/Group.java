package com.moyeorait.moyeoraitspring.commons.domain.group.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "\"group\"") // 예약어 및 소문자 보존을 위해 따옴표 포함
@Getter
@Setter
public class Group {

}
