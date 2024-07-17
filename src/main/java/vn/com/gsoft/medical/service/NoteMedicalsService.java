package vn.com.gsoft.medical.service;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;

import java.util.Date;

public interface NoteMedicalsService extends BaseService<NoteMedicals, NoteMedicalsReq, Long> {


    Object searchPagePhieuKham(NoteMedicalsReq objReq);

    NoteMedicals lock(NoteMedicalsReq objReq) throws Exception;
    NoteMedicals init(NoteMedicalsReq objReq) throws Exception;
    NoteMedicals changeStatusExam(NoteMedicalsReq objReq) throws Exception;

    @Transactional
    NoteMedicals cancel(Long id) throws Exception;

    Integer getNewNoteWaitNumber() throws Exception;

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    NoteMedicals createNoteWait(NoteMedicalsReq req) throws Exception;

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    NoteMedicals updateNoteWait(NoteMedicalsReq req) throws Exception;

    void export(NoteMedicalsReq req, HttpServletResponse response) throws Exception;

}