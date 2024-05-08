package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.NoteMedicalsService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
public class NoteMedicalsServiceImpl extends BaseServiceImpl<NoteMedicals, NoteMedicalsReq, Long> implements NoteMedicalsService {

    private NoteMedicalsRepository hdrRepo;
    private UserProfileRepository userProfileRepository;
    private KhachHangsRepository khachHangsRepository;
    private BacSiesRepository bacSiesRepository;
    private ESDiagnoseRepository diagnoseRepository;

    @Autowired
    public NoteMedicalsServiceImpl(NoteMedicalsRepository hdrRepo, UserProfileRepository userProfileRepository,
                                   KhachHangsRepository khachHangsRepository,
                                   ESDiagnoseRepository diagnoseRepository,
                                   BacSiesRepository bacSiesRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.userProfileRepository = userProfileRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.bacSiesRepository = bacSiesRepository;
        this.diagnoseRepository = diagnoseRepository;
    }

    @Override
    public Page<NoteMedicals> searchPage(NoteMedicalsReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setStatusNote(0);
        if (req.getRecordStatusId() == null) {
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteMedicals> noteMedicals = hdrRepo.searchPage(req, pageable);
        for (NoteMedicals kk : noteMedicals.getContent()) {
            if (kk.getCreatedByUserId() != null && kk.getCreatedByUserId() > 0) {
                Optional<UserProfile> userProfile = userProfileRepository.findById(kk.getCreatedByUserId());
                userProfile.ifPresent(profile -> kk.setCreatedByUseText(profile.getTenDayDu()));
            }
            if (kk.getIdPatient() != null && kk.getIdPatient() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(kk.getIdPatient());
                khachHangs.ifPresent(hangs -> kk.setPatientName(hangs.getTenKhachHang()));
            }
            if (kk.getIdDoctor() != null && kk.getIdDoctor() > 0) {
                Optional<BacSies> bacSies = bacSiesRepository.findById(kk.getIdDoctor());
                bacSies.ifPresent(hangs -> kk.setDoctorName(bacSies.get().getTenBacSy()));
            }
        }
        return noteMedicals;
    }

    @Override
    public Object searchPagePhieuKham(NoteMedicalsReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (req.getRecordStatusId() == null) {
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteMedicals> noteMedicals = hdrRepo.searchPagePhieuKham(req, pageable);
        for (NoteMedicals kk : noteMedicals.getContent()) {
            if (kk.getCreatedByUserId() != null && kk.getCreatedByUserId() > 0) {
                Optional<UserProfile> userProfile = userProfileRepository.findById(kk.getCreatedByUserId());
                userProfile.ifPresent(profile -> kk.setCreatedByUseText(profile.getTenDayDu()));
            }
            if (kk.getIdPatient() != null && kk.getIdPatient() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(kk.getIdPatient());
                khachHangs.ifPresent(hangs -> kk.setPatientName(hangs.getTenKhachHang()));
            }
            if (kk.getIdDoctor() != null && kk.getIdDoctor() > 0) {
                Optional<BacSies> bacSies = bacSiesRepository.findById(kk.getIdDoctor());
                bacSies.ifPresent(hangs -> kk.setDoctorName(bacSies.get().getTenBacSy()));
            }
        }
        return noteMedicals;
    }

    @Override
    public NoteMedicals detail(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteMedicals> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        NoteMedicals noteMedicals = optional.get();
        if (noteMedicals.getCreatedByUserId() != null && noteMedicals.getCreatedByUserId() > 0) {
            Optional<UserProfile> userProfile = userProfileRepository.findById(noteMedicals.getCreatedByUserId());
            userProfile.ifPresent(profile -> noteMedicals.setCreatedByUseText(profile.getTenDayDu()));
        }
        if (noteMedicals.getIdPatient() != null && noteMedicals.getIdPatient() > 0) {
            Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteMedicals.getIdPatient());
            khachHangs.ifPresent(hangs -> noteMedicals.setPatientName(hangs.getTenKhachHang()));
        }
        if (noteMedicals.getIdDoctor() != null && noteMedicals.getIdDoctor() > 0) {
            Optional<BacSies> bacSies = bacSiesRepository.findById(noteMedicals.getIdDoctor());
            bacSies.ifPresent(hangs -> noteMedicals.setDoctorName(bacSies.get().getTenBacSy()));
        }
        if (noteMedicals.getDiagnosticIds() != null) {
            String[] diagnosticIds = noteMedicals.getDiagnosticIds().split(",");
            List<Long> ids = Arrays.stream(diagnosticIds)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<ESDiagnose> diagnose = diagnoseRepository.findByIdIn(ids);
            noteMedicals.setDiagnostics(diagnose);
        }

        return noteMedicals;
    }

}
