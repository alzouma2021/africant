package com.africanb.africanb.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Pays;
import com.africanb.africanb.dao.repository.compagnie.PaysRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Disabled
@Log
//@RunWith(SpringRunner.class)
@SpringBootTest
public class PaysTests {

    @Autowired
    private PaysRepository paysRepository;

    @BeforeEach
    void initialized(){
        Pays pays = new Pays();
        pays.setDesignation("Test1000");
        pays.setDescription("Test1000");
        pays.setIsDeleted(false);
        paysRepository.save(pays);
    }
    @Disabled
    @Test
    public void create(){
        Pays pays = new Pays();
        pays.setDesignation("Nigeria1222");
        pays.setDescription("Pays de l'afrique de l'ouest");
        Pays rtn=paysRepository.save(pays);
        assertNotNull(rtn);
        assertEquals(pays.getDesignation(),rtn.getDesignation());
    }

}
