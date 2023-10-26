package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Entity
@Table(name = "statusutilreservationbilletvoyage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusUtilReservationBilletVoyage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private ReservationBilletVoyage reservationBilletVoyage;
    @ManyToOne
    private StatusUtil statusUtil;
}
