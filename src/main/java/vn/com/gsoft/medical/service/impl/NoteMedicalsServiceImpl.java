package vn.com.gsoft.medical.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.constant.StoreSettingKeys;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.system.ApplicationSetting;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.NoteMedicalsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class NoteMedicalsServiceImpl extends BaseServiceImpl<NoteMedicals, NoteMedicalsReq, Long> implements NoteMedicalsService {

    private NoteMedicalsRepository hdrRepo;
    private UserProfileRepository userProfileRepository;
    private KhachHangsRepository khachHangsRepository;
    private BacSiesRepository bacSiesRepository;
    private ESDiagnoseRepository diagnoseRepository;
    private BenhBoYTeRepository benhBoYTeRepository;

    @Autowired
    public NoteMedicalsServiceImpl(NoteMedicalsRepository hdrRepo, UserProfileRepository userProfileRepository,
                                   KhachHangsRepository khachHangsRepository,
                                   ESDiagnoseRepository diagnoseRepository,
                                   BacSiesRepository bacSiesRepository,
                                   BenhBoYTeRepository benhBoYTeRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.userProfileRepository = userProfileRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.bacSiesRepository = bacSiesRepository;
        this.diagnoseRepository = diagnoseRepository;
        this.benhBoYTeRepository = benhBoYTeRepository;
    }

    @Override
    public Page<NoteMedicals> searchPage(NoteMedicalsReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
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
                khachHangs.ifPresent(kk::setCustomer);
            }
            if (kk.getIdDoctor() != null && kk.getIdDoctor() > 0) {
                Optional<BacSies> bacSies = bacSiesRepository.findById(kk.getIdDoctor());
                bacSies.ifPresent(hangs -> kk.setDoctorName(bacSies.get().getTenBacSy()));
            }
            if(kk.getDiagnosticIds() != null && !kk.getDiagnosticIds().isEmpty()){
                String[] diagnosticIds = kk.getDiagnosticIds().split(",");
                List<Long> ids = Arrays.stream(diagnosticIds)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                List<BenhBoYTe> benhBoYTes = benhBoYTeRepository.findByIdIn(ids);
                kk.setDiagnostics(benhBoYTes);
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
            khachHangs.ifPresent(noteMedicals::setCustomer);
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
            List<BenhBoYTe> benhBoYTes = benhBoYTeRepository.findByIdIn(ids);
            noteMedicals.setDiagnostics(benhBoYTes);
        }

        return noteMedicals;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteMedicals> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        NoteMedicals detail = optional.get();
        if(detail.getIsLock()){
            throw new Exception("Bạn không được phép sửa phiếu trong quá khứ");
        }

        boolean normalUser = "User".equals(userInfo.getNhaThuoc().getRole());
        List<ApplicationSetting> settings = getLoggedUser().getApplicationSettings();
        Optional<ApplicationSetting> optEnableNotChangeNotesMedicalOfDoctorOther = settings.stream().filter(setting -> setting.getSettingKey().equals(StoreSettingKeys.EnableNotChangeNotesMedicalOfDoctorOther)).findFirst();
        if(optEnableNotChangeNotesMedicalOfDoctorOther.isPresent()){
            if(optEnableNotChangeNotesMedicalOfDoctorOther.get().getSettingValue().equals("true") && normalUser && (detail.getIdDoctor() != null && detail.getIdDoctor() > 0)){
                Optional<BacSies> bacSies = bacSiesRepository.findById(detail.getIdDoctor());
                if(bacSies.isPresent() && bacSies.get().getTenBacSy().equalsIgnoreCase(getLoggedUser().getFullName()))
                    if(!bacSies.get().getId().equals(getLoggedUser().getId())){
                        throw new Exception("Bạn không được phép sửa hay xóa phiếu của bác sỹ khác");
                    }
            }
        }
        detail.setRecordStatusId(RecordStatusContains.DELETED);
        detail.setModified(new Date());
        detail.setModifiedByUserId(getLoggedUser().getId());
        hdrRepo.save(detail);
        return true;
    }

    @Override
    @Transactional
    public NoteMedicals cancel(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteMedicals> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        NoteMedicals detail = optional.get();
        detail.setStatusNote(3);
        detail.setModified(new Date());
        detail.setModifiedByUserId(getLoggedUser().getId());
        hdrRepo.save(detail);
        return detail;
    }

    @Override
    public Integer getNewNoteWaitNumber() throws Exception {
        NoteMedicalsReq req = new NoteMedicalsReq();
        LocalDate today = LocalDate.now();
        req.setFromDateCreated(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        req.setToDateCreated(Date.from(today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
        req.setStoreCode(getLoggedUser().getNhaThuoc().getMaNhaThuoc());
        List<NoteMedicals> noteMedicals = hdrRepo.searchList(req);
        int noteNumber = 1;
        if(!noteMedicals.isEmpty()){
            noteNumber = noteMedicals.stream().map(NoteMedicals::getOrderWait).max(Integer::compareTo).get() + 1;
        }
        return noteNumber;
    }

    @Override
    @Transactional
    public NoteMedicals createNoteWait(NoteMedicalsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        req.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        req.setStatusNote(0);
        req.setIsLock(false);
        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        req.setIsDeb(true);
        req.setNoteDate(new Date());
        req.setOrderWait(getNewNoteWaitNumber());

        if (req.getIdPatient() == null) {
            throw new Exception("Khách hàng không được để trống");
        }

        // Kiểm tra khách hàng này đã tạo phiếu trong ngày hôm nay chưa
        NoteMedicalsReq noteMedicalsReq = new NoteMedicalsReq();
        LocalDate today = LocalDate.now();
        noteMedicalsReq.setFromDateCreated(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        noteMedicalsReq.setToDateCreated(Date.from(today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
        noteMedicalsReq.setStoreCode(getLoggedUser().getNhaThuoc().getMaNhaThuoc());
        noteMedicalsReq.setIdPatient(req.getIdPatient());
        List<NoteMedicals> noteMedicals = hdrRepo.searchList(noteMedicalsReq);
        if(!noteMedicals.isEmpty()) {
            throw new Exception("Lưu thất bại, bệnh nhân này đã được tạo phiếu chờ ngày hôm nay");
        }

        NoteMedicals e = new NoteMedicals();
        BeanUtils.copyProperties(req, e, "id");
        e.setCreated(new Date());
        e.setCreatedByUserId(getLoggedUser().getId());
        e = hdrRepo.save(e);
        return e;
    }

    @Override
    @Transactional
    public NoteMedicals updateNoteWait(NoteMedicalsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        req.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        req.setStatusNote(0);

        Optional<NoteMedicals> optional = hdrRepo.findById(req.getId());
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }

        if (req.getIdPatient() == null) {
            throw new Exception("Khách hàng không được để trống");
        }

        NoteMedicals e = new NoteMedicals();
        BeanUtils.copyProperties(req, e, "id", "created", "createdByUserId");
        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        e.setModified(new Date());
        e.setModifiedByUserId(getLoggedUser().getId());
        e = hdrRepo.save(e);
        return e;
    }


}
