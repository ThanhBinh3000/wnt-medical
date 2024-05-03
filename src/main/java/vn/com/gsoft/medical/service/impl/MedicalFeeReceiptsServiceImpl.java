package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.MedicalFeeReceipts;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;
import vn.com.gsoft.medical.repository.MedicalFeeReceiptsRepository;
import vn.com.gsoft.medical.service.MedicalFeeReceiptsService;


@Service
@Log4j2
public class MedicalFeeReceiptsServiceImpl extends BaseServiceImpl<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> implements MedicalFeeReceiptsService {

    private MedicalFeeReceiptsRepository hdrRepo;

    @Autowired
    public MedicalFeeReceiptsServiceImpl(MedicalFeeReceiptsRepository hdrRepo) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
    }

}
