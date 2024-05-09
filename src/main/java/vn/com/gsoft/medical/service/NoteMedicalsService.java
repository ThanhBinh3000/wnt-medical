package vn.com.gsoft.medical.service;


import org.springframework.transaction.annotation.Transactional;
import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;

import java.util.Date;

public interface NoteMedicalsService extends BaseService<NoteMedicals, NoteMedicalsReq, Long> {


    Object searchPagePhieuKham(NoteMedicalsReq objReq);

    @Transactional
    NoteMedicals cancel(Long id) throws Exception;

    Integer getNewNoteWaitNumber() throws Exception;

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    NoteMedicals createNoteWait(NoteMedicalsReq req) throws Exception;

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    NoteMedicals updateNoteWait(NoteMedicalsReq req) throws Exception;
}