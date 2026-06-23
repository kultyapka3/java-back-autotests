package com.company.models.wp;

import lombok.Data;

/** Вспомогательный DTO для rendered-полей */
@Data
public class RenderedField {
    private String raw;
    private String rendered;
}
