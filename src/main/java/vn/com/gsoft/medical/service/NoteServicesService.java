package vn.com.gsoft.medical.service;


import jakarta.servlet.http.HttpServletResponse;
import vn.com.gsoft.medical.entity.NoteServiceDetails;
import vn.com.gsoft.medical.entity.NoteServices;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.entity.ReportTemplateResponse;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;

import java.util.HashMap;
import java.util.List;

public interface NoteServicesService extends BaseService<NoteServices, NoteServicesReq, Long> {

    Object searchPageLieuTrinh(NoteServicesReq objReq);
    Object searchPageChoThucHien(NoteServicesReq objReq);
    void lockNoteService (NoteServicesReq objReq) throws Exception;

    NoteServices init (NoteServicesReq objReq) throws Exception;
    String generateBarCode () throws Exception;

    List<NoteServiceDetails> searchByCustomer(NoteServicesReq req) throws Exception;
    void export(NoteServicesReq req, HttpServletResponse response) throws Exception;


    ReportTemplateResponse preview(HashMap<String, Object> hashMap) throws Exception;
}