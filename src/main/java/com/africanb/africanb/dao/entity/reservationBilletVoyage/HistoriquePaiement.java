package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.utils.Reference.Reference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "hsitoriquepaiment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoriquePaiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true , length = 50)
    @NotNull
    private String identifiantUnique;
    private String description;
    private String dateTimePayment;

    @ManyToOne
    private ModePaiement modePaiement;
}
