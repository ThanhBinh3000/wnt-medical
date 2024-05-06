package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.KhachHangs;
import vn.com.gsoft.medical.entity.MedicalFeeReceipts;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.KhachHangsRepository;
import vn.com.gsoft.medical.repository.MedicalFeeReceiptsRepository;
import vn.com.gsoft.medical.service.MedicalFeeReceiptsService;

import java.util.Optional;


@Service
@Log4j2
public class MedicalFeeReceiptsServiceImpl extends BaseServiceImpl<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> implements MedicalFeeReceiptsService {

    private MedicalFeeReceiptsRepository hdrRepo;
    private KhachHangsRepository khachHangsRepository;
    @Autowired
    public MedicalFeeReceiptsServiceImpl(MedicalFeeReceiptsRepository hdrRepo,KhachHangsRepository khachHangsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.khachHangsRepository =khachHangsRepository;
    }
    @Override
    public Page<MedicalFeeReceipts> searchPage(MedicalFeeReceiptsReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<MedicalFeeReceipts> medicalFeeReceipts = hdrRepo.searchPage(req, pageable);
        for (MedicalFeeReceipts kk : medicalFeeReceipts.getContent()) {
            if (kk.getIdCus() != null && kk.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(kk.getIdCus());
                khachHangs.ifPresent(hangs -> kk.setCustomerName(hangs.getTenKhachHang()));
            }
        }
        return medicalFeeReceipts;
    }

    @Override
    public MedicalFeeReceipts detail(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<MedicalFeeReceipts> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        MedicalFeeReceipts noteMedicals = optional.get();
        if (noteMedicals.getIdCus() != null && noteMedicals.getIdCus() > 0) {
            Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteMedicals.getIdCus());
            khachHangs.ifPresent(hangs -> noteMedicals.setCustomerName(hangs.getTenKhachHang()));
        }
        return noteMedicals;
    }
}
