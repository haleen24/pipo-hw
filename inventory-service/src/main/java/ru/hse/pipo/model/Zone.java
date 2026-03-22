package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    private String code;
    private ZoneType type;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
