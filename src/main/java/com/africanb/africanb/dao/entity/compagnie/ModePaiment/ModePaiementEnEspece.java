package com.africanb.africanb.dao.entity.compagnie.ModePaiment;

import lombok.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;


@Entity
@Table(name="modepaiementenespece")
@DiscriminatorValue("enespece")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ModePaiementEnEspece extends ModePaiement implements Serializable {
}
