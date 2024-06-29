package com.africanb.africanb.helper.dto.compagnie.ModeAbonnement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class AbonnementPeriodiqueDTO extends ModeAbonnementDTO {
    private long redevance;
    private long redevancePublicite;
}
