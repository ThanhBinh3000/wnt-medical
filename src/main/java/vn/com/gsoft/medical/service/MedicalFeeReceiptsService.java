package vn.com.gsoft.medical.service;


import vn.com.gsoft.medical.entity.MedicalFeeReceiptDetails;
import vn.com.gsoft.medical.entity.MedicalFeeReceipts;
import vn.com.gsoft.medical.entity.ReportTemplateResponse;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsCustomerDebtRes;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;

import java.util.HashMap;
import java.util.List;

public interface MedicalFeeReceiptsService extends BaseService<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> {
    Integer getNewNoteNumber() throws Exception;

    List<MedicalFeeReceiptsCustomerDebtRes> getListCustomerDebt(Long customerId, Boolean isDisplayByNote) throws Exception;

    ReportTemplateResponse preview(HashMap<String, Object> hashMap) throws Exception;
    //Thanh toán cho một phiếu
    Long paymentMedicalNote(MedicalFeeReceiptsReq req) throws Exception;
}