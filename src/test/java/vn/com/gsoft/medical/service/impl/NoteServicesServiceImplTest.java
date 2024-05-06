package vn.com.gsoft.medical.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.entity.NoteServices;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;
import vn.com.gsoft.medical.model.system.PaggingReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.service.NoteServicesService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class NoteServicesServiceImplTest {
    @Autowired
    private NoteServicesService noteServicesService;

    @BeforeAll
    static void beforeAll() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Profile p = new Profile();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(p, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void searchPage() throws Exception {
        NoteServicesReq noteServicesReq = new NoteServicesReq();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(10);
        noteServicesReq.setPaggingReq(paggingReq);
        Page<NoteServices> noteServices = noteServicesService.searchPage(noteServicesReq);
        assert noteServices != null;
    }

    @Test
    void detail() throws Exception {
        NoteServices detail = noteServicesService.detail(15571l);
        assert detail != null;
    }
}