package vn.com.gsoft.medical.service;


import vn.com.gsoft.medical.entity.NoteServices;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;

public interface NoteServicesService extends BaseService<NoteServices, NoteServicesReq, Long> {

    Object searchPageLieuTrinh(NoteServicesReq objReq);
    Object searchPageChoThucHien(NoteServicesReq objReq);
    void lockNoteService (NoteServicesReq objReq) throws Exception;
}