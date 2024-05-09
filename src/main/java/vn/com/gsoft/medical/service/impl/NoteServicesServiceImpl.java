package vn.com.gsoft.medical.service.impl;

import jakarta.persistence.Tuple;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.dto.NoteServicesLieuTrinhRes;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.NoteServicesService;
import vn.com.gsoft.medical.util.system.DataUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;


@Service
@Log4j2
public class NoteServicesServiceImpl extends BaseServiceImpl<NoteServices, NoteServicesReq, Long> implements NoteServicesService {

    private NoteServicesRepository hdrRepo;
    private NoteServiceDetailsRepository noteServiceDetailsRepository;
    private UserProfileRepository userProfileRepository;
    private KhachHangsRepository khachHangsRepository;
	private BacSiesRepository bacSiesRepository;

    @Autowired
    public NoteServicesServiceImpl(NoteServicesRepository hdrRepo, UserProfileRepository userProfileRepository,
                                   KhachHangsRepository khachHangsRepository,
                                   BacSiesRepository bacSiesRepository,
                                   NoteServiceDetailsRepository noteServiceDetailsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.userProfileRepository = userProfileRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.bacSiesRepository =bacSiesRepository;
        this.noteServiceDetailsRepository = noteServiceDetailsRepository;
    }

    @Override
    public Page<NoteServices> searchPage(NoteServicesReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServices> noteServices = hdrRepo.searchPage(req, pageable);
        for (NoteServices kk : noteServices.getContent()) {
            if (kk.getCreatedByUserId() != null && kk.getCreatedByUserId() > 0) {
                Optional<UserProfile> userProfile = userProfileRepository.findById(kk.getCreatedByUserId());
                userProfile.ifPresent(profile -> kk.setCreatedByUseText(profile.getTenDayDu()));
            }
            if (kk.getIdCus() != null && kk.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(kk.getIdCus());
                khachHangs.ifPresent(khachHang -> kk.setCustomerName(khachHang.getTenKhachHang()));
                khachHangs.ifPresent(kk::setCustomer);
            }
            if (kk.getIdDoctor() != null && kk.getIdDoctor() > 0) {
                Optional<BacSies> bacSies = bacSiesRepository.findById(kk.getIdDoctor());
                bacSies.ifPresent(sies -> kk.setDoctorName(sies.getTenBacSy()));
            }
        }
        return noteServices;
    }

    @Override
    public Object searchPageLieuTrinh(NoteServicesReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServicesLieuTrinhRes> results =   DataUtils.convertPage(hdrRepo.searchPageLieuTrinh(req, pageable), NoteServicesLieuTrinhRes.class);
        Page<NoteServices> noteServices = results.map(result -> {
            final NoteServices res = new NoteServices();
            BeanUtils.copyProperties(result, res);
            if (res.getIdCus() != null && res.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(res.getIdCus());
                khachHangs.ifPresent(khachHang -> res.setCustomerName(khachHang.getTenKhachHang()));
                khachHangs.ifPresent(res::setCustomer);
            }
            return res;
        });
        return noteServices;
    }

    @Override
    public NoteServices detail(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteServices> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        NoteServices noteServices = optional.get();
        if (noteServices.getCreatedByUserId() != null && noteServices.getCreatedByUserId() > 0) {
            Optional<UserProfile> userProfile = userProfileRepository.findById(noteServices.getCreatedByUserId());
            userProfile.ifPresent(profile -> noteServices.setCreatedByUseText(profile.getTenDayDu()));
        }
        if (noteServices.getIdCus() != null && noteServices.getIdCus() > 0) {
            Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteServices.getIdCus());
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerName(khachHang.getTenKhachHang()));
            khachHangs.ifPresent(noteServices::setCustomer);
        }
        if (noteServices.getIdDoctor() != null && noteServices.getIdDoctor() > 0) {
            Optional<BacSies> bacSies = bacSiesRepository.findById(noteServices.getIdDoctor());
            bacSies.ifPresent(sies -> noteServices.setDoctorName(sies.getTenBacSy()));
        }
        noteServices.setChiTiets(noteServiceDetailsRepository.findByIdNoteService(noteServices.getId()));
        return noteServices;
    }
}
