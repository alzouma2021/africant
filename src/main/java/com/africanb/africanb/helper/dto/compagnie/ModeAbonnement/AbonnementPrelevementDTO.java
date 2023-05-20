package com.africanb.africanb.helper.dto.compagnie.ModeAbonnement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class AbonnementPrelevementDTO extends ModeAbonnementDTO {
    private long taux;
}
