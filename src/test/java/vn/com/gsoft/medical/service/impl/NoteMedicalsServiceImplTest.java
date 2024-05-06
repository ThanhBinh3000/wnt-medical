package vn.com.gsoft.medical.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.system.PaggingReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.service.NoteMedicalsService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class NoteMedicalsServiceImplTest {
    @Autowired
    private NoteMedicalsService noteMedicalsService;

    @BeforeAll
    static void beforeAll() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Profile p = new Profile();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(p, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void searchPage() throws Exception {
        NoteMedicalsReq noteMedicalsReq = new NoteMedicalsReq();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(10);
        noteMedicalsReq.setPaggingReq(paggingReq);
        Page<NoteMedicals> sampleNotes = noteMedicalsService.searchPage(noteMedicalsReq);
        assert sampleNotes != null;
    }

    @Test
    void detail() throws Exception {
        NoteMedicals detail = noteMedicalsService.detail(15022l);
        assert detail != null;
    }
}