package com.africanb.africanb.integration.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.reservationBilletVoyage.ClientDetails;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class ReservationBilletVoyageTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void testReservationBilletVoyageWithoutOtherPersonal(){

    }

    @Test
    void testReservationBilletVoyageWithOtherPersonal() throws Exception {

        Request<ReservationBilletVoyageDTO> request = createReservationRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/reservationBilletVoyages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.programmeDesignation").value(request.getData().getProgrammeDesignation()))
                .andExpect(jsonPath("$.data.clientDetails.nom").value(request.getData().getClientDetails().getNom()))
                .andExpect(jsonPath("$.data.offreVoyageDesignation").value(request.getData().getOffreVoyageDesignation()));
    }

    private Request<ReservationBilletVoyageDTO> createReservationRequest() {

        Request<ReservationBilletVoyageDTO> request = new Request<ReservationBilletVoyageDTO>();

        ReservationBilletVoyageDTO data = new ReservationBilletVoyageDTO();
        data.setProgrammeDesignation("programme600");
        data.setNombrePlace(2);
        data.setCategorieVoyageur("Adulte");
        data.setGareDesignation("gare001");
        data.setIsOtherPerson(true);

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setNom("Alzouma Moussa");
        clientDetails.setPrenoms("Mahamadou");
        clientDetails.setEmail("alzoumamoussa@gmail.com");
        clientDetails.setTelephone("0565585746");

        data.setClientDetails(clientDetails);
        data.setOffreVoyageDesignation("AbidjanYamoussoukroCompagnieTransport");

        request.setData(data);

        return request;
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
