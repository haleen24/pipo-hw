package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationType {
    private Long id;
    private String code;
    private Long lengthCm;
    private Long widthCm;
    private Long heightCm;
    private Boolean unlimited;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
