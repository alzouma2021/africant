package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import lombok.*;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "statusutilreservationbilletvoyage")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class StatusUtilReservationBilletVoyage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private ReservationBilletVoyage reservationBilletVoyage;
    @ManyToOne
    private StatusUtil statusUtil;
}
