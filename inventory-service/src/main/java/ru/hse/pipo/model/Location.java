package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Long id;
    private String code;
    private LocationType locationType;
    private Zone zone;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
