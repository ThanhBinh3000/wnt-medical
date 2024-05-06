package vn.com.gsoft.medical.service;


import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;

public interface NoteMedicalsService extends BaseService<NoteMedicals, NoteMedicalsReq, Long> {


    Object searchPagePhieuKham(NoteMedicalsReq objReq);
}