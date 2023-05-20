package com.africanb.africanb.helper.dto.compagnie.ModePaiement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class ModePaiementOrangeMoneyDTO extends ModePaiementDTO{
    private String telephoneOrangeMoney;
}
