package ru.diplom.fpd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourierStatus {

    ACTIVE("ACTIVE"),
    NOT_ACTIVE("NOT_ACTIVE");

    private final String value;

}
