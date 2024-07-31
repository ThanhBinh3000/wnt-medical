package vn.com.gsoft.medical.service.impl;

import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.AppConstants;
import vn.com.gsoft.medical.constant.ENoteType;
import vn.com.gsoft.medical.constant.ETypeService;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.dto.NoteServicesChoThucHienRes;
import vn.com.gsoft.medical.model.dto.NoteServicesLieuTrinhRes;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;
import vn.com.gsoft.medical.model.system.ApplicationSetting;
import vn.com.gsoft.medical.model.system.PaggingReq;
import vn.com.gsoft.medical.model.dto.*;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.model.system.Settings;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.NoteServicesService;
import vn.com.gsoft.medical.util.system.DataUtils;
import vn.com.gsoft.medical.util.system.ExportExcel;
import vn.com.gsoft.medical.util.system.FileUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;


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
    private PhongKhamsRepository phongKhamsRepository;
    private ConfigTemplateRepository configTemplateRepository;

    @Autowired
    public NoteServicesServiceImpl(NoteServicesRepository hdrRepo, UserProfileRepository userProfileRepository,
                                   KhachHangsRepository khachHangsRepository,
                                   BacSiesRepository bacSiesRepository,
                                   NoteServiceDetailsRepository noteServiceDetailsRepository,
                                   ThuocsRepository thuocsRepository,
                                   NhomThuocsRepository nhomThuocsRepository,
                                   PhongKhamsRepository phongKhamsRepository,
                                   ConfigTemplateRepository configTemplateRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.userProfileRepository = userProfileRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.bacSiesRepository = bacSiesRepository;
        this.noteServiceDetailsRepository = noteServiceDetailsRepository;
        this.thuocsRepository = thuocsRepository;
        this.nhomThuocsRepository = nhomThuocsRepository;
        this.phongKhamsRepository = phongKhamsRepository;
        this.configTemplateRepository = configTemplateRepository;
    }

    @Override
    public Page<NoteServices> searchPage(NoteServicesReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (req.getRecordStatusId() == null) {
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
    public NoteServices create(NoteServicesReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        NoteServices hdr = new NoteServices();
        BeanUtils.copyProperties(req, hdr, "id");
        if (req.getRecordStatusId() == null) {
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
        hdr.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        this.updateScores(hdr,userInfo);
        NoteServices save = hdrRepo.save(hdr);
        List<NoteServiceDetails> noteServiceDetails = saveDetail(req, hdr.getId());
        save.setChiTiets(noteServiceDetails);
        return hdr;
    }

    private void updateScores(NoteServices noteModel, Profile userInfo)
    {
        List<ApplicationSetting> settings = userInfo.getApplicationSettings();

        Optional<ApplicationSetting> scoreRate = settings.stream().filter(item -> item.getSettingKey().equals("SCORE_RATE")).findFirst();

        if(scoreRate.isPresent()){
            Boolean active = scoreRate.get().getActivated();
            BigDecimal settingScore = BigDecimal.valueOf(Double.valueOf(scoreRate.get().getSettingValue()));
            if( active && settingScore.compareTo(AppConstants.EspAmount) > 0){
                BigDecimal newScore = BigDecimal.ZERO;
                for (NoteServiceDetails dtl : noteModel.getChiTiets()) {
                    if(dtl.getDrugId() != null && dtl.getDrugId() > 0L){
                        Optional<Thuocs> thuocOpt = thuocsRepository.findById(dtl.getDrugId());

                        var scopreRate = (thuocOpt.get().getMoneyToOneScoreRate() != null && thuocOpt.get().getMoneyToOneScoreRate().compareTo(AppConstants.EspAmount) > 0) ? thuocOpt.get().getMoneyToOneScoreRate() : settingScore;
                        if (scopreRate.compareTo(AppConstants.EspAmount) > 0)
                        {
                            BigDecimal score = dtl.getAmount().multiply(dtl.getRetailOutPrice()).divide(scopreRate,2, RoundingMode.DOWN);
                            newScore = newScore.add(score);
                        }
                    }
                }
                noteModel.setPreScore(noteModel.getScore());
                noteModel.setScore(newScore);
            }
        }

//        var hasScoreConfig = setting.ScoreRate_Activated && setting.ScoreRate > (decimal)AppConstants.Esp;
//        if (!hasScoreConfig) return;
//        var noteItems = noteModel.ListDetailNote;
//        noteItems.ForEach(i =>
//                {
//        if (i.DrugId > 0)
//        {
//            var scoreRate = i.MoneyToOneScoreRate > (decimal)AppConstants.EspAmount ? i.MoneyToOneScoreRate : setting.ScoreRate;
//            if (scoreRate > (decimal)AppConstants.EspAmount)
//            {
//                var score = (decimal)(i.Amount * i.RetailOutPrice) / scoreRate;
//                i.Score = score;
//            }
//        }
//            });
//        noteModel.PreScore = noteModel.Score;
//        noteModel.Score = noteItems.Where(i => i.Scorable).Sum(i => i.Score);
    }

    private List<NoteServiceDetails> saveDetail(NoteServicesReq req, Long idHdr) {
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
        if (req.getRecordStatusId() == null) {
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServicesLieuTrinhRes> results = DataUtils.convertPage(hdrRepo.searchPageLieuTrinh(req, pageable), NoteServicesLieuTrinhRes.class);
        return results.map(result -> {
            final NoteServices res = new NoteServices();
            BeanUtils.copyProperties(result, res);
            if (res.getIdCus() != null && res.getIdCus() > 0) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(res.getIdCus());
                khachHangs.ifPresent(khachHang -> res.setCustomerName(khachHang.getTenKhachHang()));
                khachHangs.ifPresent(res::setCustomer);
            }
            if (res.getDrugId() != null && res.getDrugId() > 0) {
                Optional<Thuocs> dichVus = thuocsRepository.findById(res.getDrugId());
                dichVus.ifPresent(res::setDichVu);
            }
            return res;
        });
    }

    @Override
    public Object searchPageChoThucHien(NoteServicesReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (req.getRecordStatusId() == null) {
            req.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        Page<NoteServicesChoThucHienRes> results = DataUtils.convertPage(hdrRepo.searchPageChoThucHien(req, pageable), NoteServicesChoThucHienRes.class);
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
            if (res.getDrugId() != null && res.getDrugId() > 0) {
                Optional<Thuocs> dichVus = thuocsRepository.findById(res.getDrugId());
                dichVus.ifPresent(res::setDichVu);
                if (res.getDichVu() != null) {
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
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerAddress(khachHang.getDiaChi()));
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerPhoneNumber(khachHang.getSoDienThoai()));
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerGender(khachHang.getSexId() == 1 ? "Nữ" : "Nam"));
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerEmail(khachHang.getEmail()));
            khachHangs.ifPresent(khachHang -> noteServices.setCustomeBirthDate(khachHang.getBirthDate()));
            khachHangs.ifPresent(khachHang -> noteServices.setCustomerAge(Period.between(khachHang.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears()));
            khachHangs.ifPresent(noteServices::setCustomer);
        }
        if (noteServices.getIdDoctor() != null && noteServices.getIdDoctor() > 0) {
            Optional<BacSies> bacSies = bacSiesRepository.findById(noteServices.getIdDoctor());
            bacSies.ifPresent(sies -> noteServices.setDoctorName(sies.getTenBacSy()));
            bacSies.ifPresent(sies -> noteServices.setDoctorPhoneNumber(sies.getDienThoai()));
        }
        noteServices.setChiTiets(noteServiceDetailsRepository.findByIdNoteService(noteServices.getId()));
        for (NoteServiceDetails kk : noteServices.getChiTiets()) {
            if (kk.getDrugId() != null && kk.getDrugId() > 0) {
                Optional<Thuocs> thuocs = thuocsRepository.findById(kk.getDrugId());
                thuocs.ifPresent(profile -> kk.setTenThuoc(thuocs.get().getTenThuoc()));
                thuocs.ifPresent(profile -> kk.setMaThuoc(thuocs.get().getMaThuoc()));
                thuocs.ifPresent(profile -> kk.setTenNhomThuoc(thuocs.get().getTenNhomThuoc()));
            }
            if (kk.getImplementationRoomCode() != null && kk.getImplementationRoomCode() > 0) {
                Optional<PhongKhams> phongKhams = phongKhamsRepository.findById(Long.valueOf(kk.getImplementationRoomCode()));
                phongKhams.ifPresent(phongKhams1 -> kk.setNameClinic(phongKhams.get().getTenPhongKham()));
            }
        }
        return noteServices;
    }

    @Override
    public void export(NoteServicesReq objReq, HttpServletResponse response) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        if (userInfo.getNhaThuoc().getMaNhaThuoc().equals(AppConstants.DictionaryStoreCode) || userInfo.getNhaThuoc().getMaNhaThuocCha().equals(AppConstants.DictionaryStoreCode))
            throw new Exception("Bạn không được phép xuất dữ liệu của cơ sở này.");
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<NoteServices> noteMedicals = this.searchPage(objReq);
        List<NoteServices> data = noteMedicals.getContent();
        String title = "DANH SÁCH PHIẾU DỊCH VỤ";
        String fileName = "danh-sach-phieu-dich-vu.xlsx";
        String[] rowsName = new String[]{
                "STT",
                "Số phiếu",
                "Ngày khám",
                "Tên bệnh nhân",
                "Tuổi",
                "Địa chỉ",
                "Bác sỹ khám",
        };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < data.size(); i++) {
            NoteServices medicals = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = medicals.getNoteNumber();
            objs[2] = medicals.getNoteDate() != null ? dateFormat.format(medicals.getNoteDate()) : "";
            objs[3] = medicals.getCustomer() != null ? medicals.getCustomer().getTenKhachHang() : "";
            objs[4] = medicals.getCustomer() != null ? medicals.getCustomer().getBirthDate() : "";
            objs[5] = medicals.getCustomer() != null ? medicals.getCustomer().getDiaChi() : "";
            objs[6] = medicals.getDoctorName();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HashMap<String, Object> hashMap) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        try {
            String loai = FileUtils.safeToString(hashMap.get("loai"));
            NoteServices noteServices = this.detail(FileUtils.safeToLong(hashMap.get("id")));
            Object value = hashMap.get("amountPrint");
            String templatePath = "/dichVu/";
            Integer checkType = 0;
            if (value != null) {
                checkType = 1;
            }
            Optional<ConfigTemplate> configTemplates = null;
            configTemplates = configTemplateRepository.findByMaNhaThuocAndPrintTypeAndMaLoaiAndType(userInfo.getNhaThuoc().getMaNhaThuoc(), loai, Long.valueOf(ENoteType.NoteService), checkType);
            if (!configTemplates.isPresent()) {
                configTemplates = configTemplateRepository.findByPrintTypeAndMaLoaiAndType(loai, Long.valueOf(ENoteType.NoteService), checkType);
            }
            if (configTemplates.isPresent()) {
                templatePath += configTemplates.get().getTemplateFileName();
            }
            List<ReportImage> reportImage = new ArrayList<>();
            if ("11440".equals(userInfo.getNhaThuoc().getMaNhaThuoc())) {
                reportImage.add(new ReportImage("imageLogo_11440", "src/main/resources/template/imageLogo_11440.png"));
            }
            noteServices.setPharmacyName(userInfo.getNhaThuoc().getTenNhaThuoc());
            noteServices.setPharmacyAddress(userInfo.getNhaThuoc().getDiaChi());
            noteServices.setPharmacyPhoneNumber(userInfo.getNhaThuoc().getDienThoai());
            InputStream templateInputStream = FileUtils.getInputStreamByFileName(templatePath);
            return FileUtils.convertDocxToPdf(templateInputStream, noteServices, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
