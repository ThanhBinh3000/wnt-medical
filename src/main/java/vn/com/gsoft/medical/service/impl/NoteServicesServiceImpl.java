package vn.com.gsoft.medical.service.impl;

import jakarta.persistence.Tuple;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.ETypeService;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.dto.NoteServicesChoThucHienRes;
import vn.com.gsoft.medical.model.dto.NoteServicesLieuTrinhRes;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.NoteServicesService;
import vn.com.gsoft.medical.util.system.DataUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Log4j2
public class NoteServicesServiceImpl extends BaseServiceImpl<NoteServices, NoteServicesReq, Long> implements NoteServicesService {

    private NoteServicesRepository hdrRepo;
    private NoteServiceDetailsRepository noteServiceDetailsRepository;
    private UserProfileRepository userProfileRepository;
    private KhachHangsRepository khachHangsRepository;
	private BacSiesRepository bacSiesRepository;
    private ThuocsRepository thuocsRepository;
    private NhomThuocsRepository nhomThuocsRepository;
    @Autowired
    public NoteServicesServiceImpl(NoteServicesRepository hdrRepo, UserProfileRepository userProfileRepository,
                                   KhachHangsRepository khachHangsRepository,
                                   BacSiesRepository bacSiesRepository,
                                   NoteServiceDetailsRepository noteServiceDetailsRepository,
                                   ThuocsRepository thuocsRepository,
                                   NhomThuocsRepository nhomThuocsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.userProfileRepository = userProfileRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.bacSiesRepository =bacSiesRepository;
        this.noteServiceDetailsRepository = noteServiceDetailsRepository;
        this.thuocsRepository = thuocsRepository;
        this.nhomThuocsRepository = nhomThuocsRepository;
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
    public NoteServices create (NoteServicesReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        NoteServices hdr = new NoteServices();
        BeanUtils.copyProperties(req, hdr, "id");
        if(req.getRecordStatusId() == null){
            hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        hdr.setCreatedByUserId(userInfo.getId());
        hdr.setCreated(new Date());
        hdr.setIsModified(true);
        hdr.setPaymentScore(BigDecimal.ZERO);
        hdr.setPaymentScoreAmount(BigDecimal.ZERO);
        hdr.setPerformerId(0L);
        hdr.setPreScore(BigDecimal.ZERO);
        hdr.setScore(BigDecimal.ZERO);
        hdr.setTotalMoney(BigDecimal.ZERO);
        NoteServices save = hdrRepo.save(hdr);
        List<NoteServiceDetails> noteServiceDetails = saveDetail(req, hdr.getId());
        save.setChiTiets(noteServiceDetails);
        return save;
    }

    private List<NoteServiceDetails> saveDetail(NoteServicesReq req, Long idHdr){
        noteServiceDetailsRepository.deleteAllByIdNoteService(idHdr);
        List<NoteServiceDetails> list = req.getChiTiets();
        list.forEach(item -> {
            item.setIdNoteService(idHdr);
            item.setIdNoteDetail(0);
            item.setIdStatus(false);
            item.setLastCountNumbers(0);
            item.setRecordStatusId(RecordStatusContains.ACTIVE);
        });
        return (List<NoteServiceDetails>) noteServiceDetailsRepository.saveAll(list);
    }

    @Override
    public Object searchPageLieuTrinh(NoteServicesReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServicesLieuTrinhRes> results =   DataUtils.convertPage(hdrRepo.searchPageLieuTrinh(req, pageable), NoteServicesLieuTrinhRes.class);
        return results.map(result -> {
            final NoteServices res = new NoteServices();
            BeanUtils.copyProperties(result, res);
            if (res.getIdCus() != null && res.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(res.getIdCus());
                khachHangs.ifPresent(khachHang -> res.setCustomerName(khachHang.getTenKhachHang()));
                khachHangs.ifPresent(res::setCustomer);
            }
            if(res.getDrugId() != null && res.getDrugId() > 0){
                Optional<Thuocs> dichVus = thuocsRepository.findById(res.getDrugId());
                dichVus.ifPresent(res::setDichVu);
            }
            return res;
        });
    }

    @Override
    public Object searchPageChoThucHien(NoteServicesReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if(req.getRecordStatusId() == null){
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServicesChoThucHienRes> results =   DataUtils.convertPage(hdrRepo.searchPageChoThucHien(req, pageable), NoteServicesChoThucHienRes.class);
        return results.map(result -> {
            final NoteServices res = new NoteServices();
            BeanUtils.copyProperties(result, res);
            if (res.getPerformerId() != null && res.getPerformerId() > 0) {
                Optional<UserProfile> userProfile = userProfileRepository.findById(res.getPerformerId());
                userProfile.ifPresent(profile -> res.setPerformerText(profile.getTenDayDu()));
            }
            if (res.getIdCus() != null && res.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(res.getIdCus());
                khachHangs.ifPresent(khachHang -> res.setCustomerName(khachHang.getTenKhachHang()));
                khachHangs.ifPresent(res::setCustomer);
            }
            if (res.getIdDoctor() != null && res.getIdDoctor() > 0) {
                Optional<BacSies> bacSies = bacSiesRepository.findById(res.getIdDoctor());
                bacSies.ifPresent(sies -> res.setDoctorName(sies.getTenBacSy()));
            }
            if(res.getDrugId() != null && res.getDrugId() > 0){
                Optional<Thuocs> dichVus = thuocsRepository.findById(res.getDrugId());
                dichVus.ifPresent(res::setDichVu);
                if(res.getDichVu() != null){
                    Optional<NhomThuocs> nhomThuocs = nhomThuocsRepository.findById(res.getDichVu().getNhomThuocMaNhomThuoc());
                    nhomThuocs.ifPresent(nhomThuoc -> res.getDichVu().setTenNhomThuoc(nhomThuoc.getTenNhomThuoc()));
                }
            }
            return res;
        });
    }

    @Override
    public void lockNoteService(NoteServicesReq objReq) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteServices> optional = hdrRepo.findById(objReq.getId());
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        optional.get().setIsLock(objReq.getIsLock());
        hdrRepo.save(optional.get());
    }

    @Override
    public NoteServices init(NoteServicesReq objReq) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        NoteServices noteServices = new NoteServices();
        Long soPhieuNhap = hdrRepo.findByNoteNumberMax(userInfo.getNhaThuoc().getMaNhaThuoc());
        if (soPhieuNhap == null) {
            soPhieuNhap = 1L;
        } else {
            soPhieuNhap += 1;
        }
        noteServices.setNoteNumber(soPhieuNhap);
        noteServices.setNoteDate(new Date());
        noteServices.setBarCode(generateBarCode());
        return noteServices;
    }

    @Override
    public String generateBarCode() throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        var storeCode = userInfo.getNhaThuoc().getMaNhaThuoc();
        NoteServicesReq noteServicesReq = new NoteServicesReq();
        noteServicesReq.setStoreCode(storeCode);
        List<NoteServices> lst = hdrRepo.searchList(noteServicesReq);
        String temp = UUID.randomUUID().toString().replace("-", "");
        String barcode = temp.replaceAll("[a-zA-Z]", "").substring(0, 12);

        while (hdrRepo.findByBarCode(barcode, storeCode, RecordStatusContains.ACTIVE).isPresent()) {
            temp = UUID.randomUUID().toString().replace("-", "");
            barcode = temp.replaceAll("[a-zA-Z]", "").substring(0, 12);
        }

        // Check if storeCode contains only digits
        try {
            int number = Integer.parseInt(storeCode);
            int order = lst.isEmpty() ? 1 : lst.size() + 1;
            String storeCodeWithOrder = storeCode + order;

            if (barcode.length() > storeCodeWithOrder.length()) {
                barcode = barcode.substring(storeCodeWithOrder.length());
                // Replace the beginning of the barcode with storeCode + sequence number
                barcode = storeCodeWithOrder + barcode;
            }
        } catch (NumberFormatException e) {
            // storeCode contains non-numeric characters, ignore the numeric check
        }

        return barcode;
    }

    @Override
    public List<NoteServiceDetails> searchByCustomer(NoteServicesReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        req.setIdTypeService(ETypeService.ServiceTherapy);
        List<NoteServiceDetails> noteServices = noteServiceDetailsRepository.searchListByCusId(req);
        noteServices.forEach(item -> {
            Optional<NoteServices> hdrOpt = hdrRepo.findById(item.getIdNoteService());
            NoteServices note = hdrOpt.get();
            int i = item.getCountNumbers() * item.getAmount().intValue() - item.getLastCountNumbers();
            item.setCountNumbers(i);
            Optional<Thuocs> byId = thuocsRepository.findById(item.getDrugId());
            byId.ifPresent(thuocs -> item.setTenThuoc(thuocs.getTenThuoc()));
            Optional<BacSies> byId1 = bacSiesRepository.findById(note.getIdDoctor());
            byId1.ifPresent(item::setBacSies);
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
        for (NoteServiceDetails kk : noteServices.getChiTiets()) {
            if (kk.getDrugId() != null && kk.getDrugId() > 0) {
                Optional<Thuocs> thuocs = thuocsRepository.findById(kk.getDrugId());
                thuocs.ifPresent(profile -> kk.setTenThuoc(thuocs.get().getTenThuoc()));
            }
        }
        return noteServices;
    }
}
