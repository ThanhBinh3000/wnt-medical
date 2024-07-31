package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.ENoteType;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.constant.StatusExaminationConstant;
import vn.com.gsoft.medical.constant.StoreSettingKeys;
import vn.com.gsoft.medical.entity.*;
import vn.com.gsoft.medical.model.dto.*;
import vn.com.gsoft.medical.model.system.ApplicationSetting;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.MedicalFeeReceiptsService;
import vn.com.gsoft.medical.util.system.FileUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@Log4j2
public class MedicalFeeReceiptsServiceImpl extends BaseServiceImpl<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> implements MedicalFeeReceiptsService {

    private MedicalFeeReceiptsRepository hdrRepo;
    private KhachHangsRepository khachHangsRepository;
    private MedicalFeeReceiptDetailsRepository medicalFeeReceiptDetailsRepository;
    private NoteMedicalsRepository noteMedicalsRepository;
    private NoteServicesRepository noteServicesRepository;
    private ThuocsRepository thuocsRepository;
    private PhongKhamsRepository phongKhamsRepository;
    private NoteServiceDetailsRepository noteServiceDetailsRepository;
    private ConfigTemplateRepository configTemplateRepository;

    @Autowired
    public MedicalFeeReceiptsServiceImpl(MedicalFeeReceiptsRepository hdrRepo,
                                         KhachHangsRepository khachHangsRepository,
                                         MedicalFeeReceiptDetailsRepository medicalFeeReceiptDetailsRepository,
                                         NoteMedicalsRepository noteMedicalsRepository,
                                         NoteServicesRepository noteServicesRepository,
                                         ThuocsRepository thuocsRepository,
                                         PhongKhamsRepository phongKhamsRepository,
                                         NoteServiceDetailsRepository noteServiceDetailsRepository,
                                         ConfigTemplateRepository configTemplateRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.khachHangsRepository = khachHangsRepository;
        this.medicalFeeReceiptDetailsRepository = medicalFeeReceiptDetailsRepository;
        this.noteMedicalsRepository = noteMedicalsRepository;
        this.noteServicesRepository = noteServicesRepository;
        this.thuocsRepository = thuocsRepository;
        this.phongKhamsRepository = phongKhamsRepository;
        this.noteServiceDetailsRepository = noteServiceDetailsRepository;
        this.configTemplateRepository = configTemplateRepository;
    }

    @Override
    public Page<MedicalFeeReceipts> searchPage(MedicalFeeReceiptsReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (req.getRecordStatusId() == null) {
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
    public MedicalFeeReceipts create(MedicalFeeReceiptsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        MedicalFeeReceipts e = new MedicalFeeReceipts();
        BeanUtils.copyProperties(req, e, "id");

        // Apply current time cho noteDate
        LocalDateTime noteDate = LocalDateTime.ofInstant(e.getNoteDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime noteDateWithCurrentTime = LocalDateTime.of(noteDate.toLocalDate(), LocalTime.now());
        e.setNoteDate(Date.from(noteDateWithCurrentTime.atZone(ZoneId.systemDefault()).toInstant()));
        e.setRecordStatusId(RecordStatusContains.ACTIVE);
        e.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        e.setCreated(new Date());
        e.setCreatedByUserId(userInfo.getId());
        StringBuilder descriptionNotePay = new StringBuilder("Đã thanh toán cho phiếu");
        e.getChiTiets().forEach(chiTiet -> {
            if (Objects.equals(chiTiet.getTypeNote(), ENoteType.ExaminationCard))
                descriptionNotePay.append(" khám số ").append(chiTiet.getNoteNumber()).append(",");
            if (Objects.equals(chiTiet.getTypeNote(), ENoteType.NoteService))
                descriptionNotePay.append(" dịch vụ số ").append(chiTiet.getNoteNumber()).append(",");
        });
        e.setDescriptNotePay(descriptionNotePay.toString());
        e.setNoteNumber(getNewNoteNumber());
        e = hdrRepo.save(e);
        // Save chi tiết
        for (MedicalFeeReceiptDetails chiTiet : e.getChiTiets()) {
            chiTiet.setIdBill(e.getId());
            chiTiet.setRecordStatusId(RecordStatusContains.ACTIVE);
            chiTiet.setCreated(new Date());
            chiTiet.setCreatedByUserId(userInfo.getId());
            chiTiet.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
            medicalFeeReceiptDetailsRepository.save(chiTiet);

            // Update lại trạng thái của phiếu khám bệnh hoặc phiếu dịch vụ
            if (Objects.equals(chiTiet.getTypeNote(), ENoteType.ExaminationCard)) {
                Optional<NoteMedicals> optional = noteMedicalsRepository.findById(chiTiet.getNoteId());
                optional.ifPresent(noteMedicals -> {
                    if (Objects.equals(noteMedicals.getStoreCode(), userInfo.getNhaThuoc().getMaNhaThuoc())) {
                        noteMedicals.setIsDeb(false);
                        noteMedicalsRepository.save(noteMedicals);
                    }
                });
            }
            if (Objects.equals(chiTiet.getTypeNote(), ENoteType.NoteService)) {
                Optional<NoteServices> optional = noteServicesRepository.findById(chiTiet.getNoteId());
                optional.ifPresent(noteServices -> {
                    if (Objects.equals(noteServices.getStoreCode(), userInfo.getNhaThuoc().getMaNhaThuoc())) {
                        noteServices.setIsDeb(false);
                        noteServicesRepository.save(noteServices);
                    }
                });
            }
        }

        return super.create(req);
    }

    @Override
    public MedicalFeeReceipts update(MedicalFeeReceiptsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<MedicalFeeReceipts> optional = hdrRepo.findById(req.getId());
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }

        MedicalFeeReceipts e = optional.get();
        BeanUtils.copyProperties(req, e, "id", "created", "createdByUserId");
        // Apply current time cho noteDate
        LocalDateTime noteDate = LocalDateTime.ofInstant(e.getNoteDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime noteDateWithCurrentTime = LocalDateTime.of(noteDate.toLocalDate(), LocalTime.now());
        e.setNoteDate(Date.from(noteDateWithCurrentTime.atZone(ZoneId.systemDefault()).toInstant()));
        e.setModified(new Date());
        e.setModifiedByUserId(userInfo.getId());

        return hdrRepo.save(e);
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
        MedicalFeeReceipts medicalFeeReceipts = optional.get();
        if (medicalFeeReceipts.getIdCus() != null && medicalFeeReceipts.getIdCus() > 0) {
            Optional<KhachHangs> khachHangs = khachHangsRepository.findById(medicalFeeReceipts.getIdCus());
            khachHangs.ifPresent(hangs -> medicalFeeReceipts.setCustomerName(hangs.getTenKhachHang()));
        }
        List<MedicalFeeReceiptDetails> medicalFeeReceiptDetails = medicalFeeReceiptDetailsRepository.findByStoreCodeAndIdBill(userInfo.getNhaThuoc().getMaNhaThuoc(), medicalFeeReceipts.getId());
        medicalFeeReceipts.setChiTiets(medicalFeeReceiptDetails);
        return medicalFeeReceipts;
    }

    @Override
    public Integer getNewNoteNumber() throws Exception {
        List<MedicalFeeReceipts> medicalFeeReceipts = hdrRepo.findByStoreCode(getLoggedUser().getNhaThuoc().getMaNhaThuoc());
        int noteNumber = 1;
        if (!medicalFeeReceipts.isEmpty()) {
            noteNumber = medicalFeeReceipts.stream().map(MedicalFeeReceipts::getNoteNumber).max(Integer::compareTo).get() + 1;
        }
        return noteNumber;
    }

    @Override
    public List<MedicalFeeReceiptsCustomerDebtRes> getListCustomerDebt(Long customerId, Boolean isDisplayByNote) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        List<MedicalFeeReceiptsCustomerDebtRes> result = new ArrayList<>();
        List<MedicalFeeReceiptsCustomerDebtRes> listDebtNote = new ArrayList<>();

        List<NoteMedicals> noteMedicals = noteMedicalsRepository.findByStoreCodeAndRecordStatusIdAndIsDebAndStatusNoteInAndTotalMoneyIsGreaterThan(userInfo.getNhaThuoc().getMaNhaThuoc(), RecordStatusContains.ACTIVE, true, List.of(StatusExaminationConstant.BeingExamination, StatusExaminationConstant.Examined), BigDecimal.ZERO);
        List<NoteServices> noteServices = noteServicesRepository.findByStoreCodeAndRecordStatusIdAndIsDeb(userInfo.getNhaThuoc().getMaNhaThuoc(), RecordStatusContains.ACTIVE, true);
        if (customerId != null) {
            noteMedicals = noteMedicals.stream().filter(x -> x.getIdPatient().equals(customerId)).toList();
            noteServices = noteServices.stream().filter(x -> x.getIdCus().equals(customerId)).toList();
        }

        ApplicationSetting useCustomerCommon = getSetting(StoreSettingKeys.UseCustomerCommon);
        // Danh sách khách nợ phiếu khám bệnh
        for (NoteMedicals noteMedical : noteMedicals) {
            MedicalFeeReceiptsCustomerDebtRes item = new MedicalFeeReceiptsCustomerDebtRes();
            if (noteMedical.getIdPatient() != null) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteMedical.getIdPatient())
                        .filter(x -> x.getRecordStatusId() == RecordStatusContains.ACTIVE)
                        .filter(x -> x.getMaNhaThuoc().equals(
                                useCustomerCommon.getActivated() ?
                                        userInfo.getNhaThuoc().getMaNhaThuocCha() :
                                        userInfo.getNhaThuoc().getMaNhaThuoc()
                        ));
                if (khachHangs.isPresent()) {
                    item.setKhachHang(khachHangs.get());
                    item.setNoteId(noteMedical.getId());
                    item.setNoteNumber(noteMedical.getNoteNumber());
                    item.setNoteDate(noteMedical.getNoteDate());
                    item.setTypeNote(ENoteType.ExaminationCard);
                    item.setTypeNoteText("Khám bệnh");
                    item.setSoLan(BigDecimal.ONE);
                    item.setGia(noteMedical.getTotalMoney());
                    if (noteMedical.getIdServiceExam() != null) {
                        Optional<Thuocs> thuoc = thuocsRepository.findById(noteMedical.getIdServiceExam())
                                .filter(x -> x.getRecordStatusId() == RecordStatusContains.ACTIVE);
                        item.setNoteServiceText(thuoc.map(Thuocs::getTenThuoc).orElse("Khám và tư vấn kê đơn"));
                    }
                    listDebtNote.add(item);
                }
            }
        }

        // Danh sách khách nợ phiếu dịch vụ
        for (NoteServices noteService : noteServices) {
            MedicalFeeReceiptsCustomerDebtRes item = new MedicalFeeReceiptsCustomerDebtRes();
            if (noteService.getIdCus() != null) {
                Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteService.getIdCus())
                        .filter(x -> x.getRecordStatusId() == RecordStatusContains.ACTIVE)
                        .filter(x -> x.getMaNhaThuoc().equals(
                                useCustomerCommon.getActivated() ?
                                        userInfo.getNhaThuoc().getMaNhaThuocCha() :
                                        userInfo.getNhaThuoc().getMaNhaThuoc()
                        ));
                if (khachHangs.isPresent()) {
                    item.setKhachHang(khachHangs.get());
                    item.setNoteId(noteService.getId());
                    item.setNoteNumber(noteService.getNoteNumber());
                    item.setNoteDate(noteService.getNoteDate());
                    item.setTypeNote(ENoteType.NoteService);
                    item.setTypeNoteText("Dịch vụ");
                    item.setSoLan(noteService.getAmount());
                    item.setGia(noteService.getRetailOutPrice());
                    listDebtNote.add(item);
                }
            }
        }

        if (isDisplayByNote) {
            listDebtNote = listDebtNote
                    .stream()
                    .sorted(Comparator.comparing(MedicalFeeReceiptsCustomerDebtRes::getNoteDate).reversed())
                    .toList();
            Map<Long, List<MedicalFeeReceiptsCustomerDebtRes>> groupByNoteNumber = listDebtNote
                    .stream()
                    .collect(Collectors.groupingBy(MedicalFeeReceiptsCustomerDebtRes::getNoteNumber));

            for (Map.Entry<Long, List<MedicalFeeReceiptsCustomerDebtRes>> entry : groupByNoteNumber.entrySet()) {
                List<MedicalFeeReceiptsCustomerDebtRes> subItems = entry.getValue();
                BigDecimal thanhTienGroup = subItems.stream().map(MedicalFeeReceiptsCustomerDebtRes::getThanhTien).reduce(BigDecimal.ZERO, BigDecimal::add);
                for (int i = 0; i < subItems.size(); i++) {
                    MedicalFeeReceiptsCustomerDebtRes item = new MedicalFeeReceiptsCustomerDebtRes();
                    BeanUtils.copyProperties(subItems.get(i), item);
                    // Set thành tiền phiếu và danh sách chi tiết cho item đầu tiên của phiếu
                    if (i == 0) {
                        item.setIsFirstItem(true);
                        item.setThanhTienGroup(thanhTienGroup);
                        item.setChiTiets(subItems);
                    } else {
                        item.setNoteId(null);
                        item.setNoteNumber(null);
                        item.setNoteDate(null);
                        item.setKhachHang(null);
                    }
                    result.add(item);
                }
            }
        } else {
            listDebtNote = listDebtNote
                    .stream()
                    .sorted(Comparator.comparing(x -> x.getKhachHang().getTenKhachHang()))
                    .toList();
            Map<Long, List<MedicalFeeReceiptsCustomerDebtRes>> groupByCusId = listDebtNote
                    .stream()
                    .collect(Collectors.groupingBy(x -> x.getKhachHang().getId()));

            for (Map.Entry<Long, List<MedicalFeeReceiptsCustomerDebtRes>> entry : groupByCusId.entrySet()) {
                List<MedicalFeeReceiptsCustomerDebtRes> subItems = entry.getValue();
                BigDecimal thanhTienGroup = subItems.stream().map(MedicalFeeReceiptsCustomerDebtRes::getThanhTien).reduce(BigDecimal.ZERO, BigDecimal::add);
                for (int i = 0; i < subItems.size(); i++) {
                    MedicalFeeReceiptsCustomerDebtRes item = new MedicalFeeReceiptsCustomerDebtRes();
                    BeanUtils.copyProperties(subItems.get(i), item);
                    // Set thành tiền khách hàng và danh sách chi tiết cho item đầu tiên của khách hàng
                    if (i == 0) {
                        item.setIsFirstItem(true);
                        item.setThanhTienGroup(thanhTienGroup);
                        item.setChiTiets(subItems);
                    } else {
                        item.setNoteId(null);
                        item.setNoteNumber(null);
                        item.setNoteDate(null);
                        item.setKhachHang(null);
                    }
                    result.add(item);
                }
            }
        }
        return result;
    }

    @Override
    public ReportTemplateResponse preview(HashMap<String, Object> hashMap) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        try {
            String loai = FileUtils.safeToString(hashMap.get("loai"));
            MedicalFeeReceipts medicalFeeReceipts = this.detail(FileUtils.safeToLong(hashMap.get("id")));
            String templatePath = "/tienKham/";
            Integer checkType = 0;
            Optional<ConfigTemplate> configTemplates = null;
            configTemplates = configTemplateRepository.findByMaNhaThuocAndPrintTypeAndMaLoaiAndType(medicalFeeReceipts.getStoreCode(), loai, Long.valueOf(ENoteType.ReceiptMedicalFee), checkType);
            if (!configTemplates.isPresent()) {
                configTemplates = configTemplateRepository.findByPrintTypeAndMaLoaiAndType(loai, Long.valueOf(ENoteType.ReceiptMedicalFee), checkType);
            }
            if (configTemplates.isPresent()) {
                templatePath += configTemplates.get().getTemplateFileName();
            }

            ExampleClass(userInfo, medicalFeeReceipts);
            List<ReportImage> reportImage = new ArrayList<>();
//            reportImage.add(new ReportImage("imageLogoPK", "src/main/resources/template/imageLogoPK.png"));
            if ("10324".equals(medicalFeeReceipts.getStoreCode())) {
                reportImage.add(new ReportImage("imageLogo_10324", "src/main/resources/template/imageLogo_10324.png"));
            }
            InputStream templateInputStream = FileUtils.getInputStreamByFileName(templatePath);
            return FileUtils.convertDocxToPdf(templateInputStream, medicalFeeReceipts, reportImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void ExampleClass(Profile userInfo, MedicalFeeReceipts medicalFeeReceipts) {
        List<MedicalFeeReceiptDetails> medicalFeeReceiptDetails = medicalFeeReceiptDetailsRepository.findByStoreCodeAndIdBill(userInfo.getNhaThuoc().getMaNhaThuoc(), medicalFeeReceipts.getId());
        for (MedicalFeeReceiptDetails detail : medicalFeeReceiptDetails) {
            if (Objects.equals(detail.getTypeNote(), ENoteType.ExaminationCard)) {
                Optional<NoteMedicals> noteMedicals = noteMedicalsRepository.findById(detail.getNoteId());
                if (noteMedicals.isPresent()) {
                    if (noteMedicals.get().getIdServiceExam() != null) {
                        Optional<Thuocs> thuoc = thuocsRepository.findById(noteMedicals.get().getIdServiceExam()).filter(item -> item.getRecordStatusId() == RecordStatusContains.ACTIVE);
                        detail.setExaminationContent(thuoc.map(Thuocs::getTenThuoc).orElse("Khám và tưu vấn"));
                        detail.setExtraInfo("Khám bệnh");
                        detail.setUnit("Lần");
                        detail.setAmount(BigDecimal.ONE);
                        detail.setPrice(noteMedicals.get().getTotalMoney());
                        detail.setTotalMoney(noteMedicals.get().getTotalMoney());
                    }
                }
            }
            if (Objects.equals(detail.getTypeNote(), ENoteType.NoteService)) {
                Optional<NoteServices> noteServices = noteServicesRepository.findById(detail.getNoteId());
                System.out.println(noteServices + "Trần Thanh Bình");
                if (noteServices.isPresent()) {
                    List<NoteServiceDetails> noteServiceDetails = noteServiceDetailsRepository.findByIdNoteService(noteServices.get().getId())
                            .stream().filter(item -> item.getRecordStatusId() == RecordStatusContains.ACTIVE).collect(Collectors.toList());
                    for (NoteServiceDetails noteServiceDetai : noteServiceDetails) {
                        thuocsRepository.findById(noteServiceDetai.getDrugId()).ifPresent(item -> detail.setExaminationContent(item.getTenThuoc()));
                        detail.setExtraInfo("Dịch vụ");
                        detail.setUnit("Lần");
                        detail.setAmount(noteServiceDetai.getAmount());
                        detail.setPrice(noteServiceDetai.getRetailOutPrice());
                        detail.setTotalMoney(noteServices.get().getTotalMoney());
                    }
                }
            }
        }
        khachHangsRepository.findById(medicalFeeReceipts.getIdCus()).ifPresent(item -> {
            medicalFeeReceipts.setCustomerAddress(item.getDiaChi());
            medicalFeeReceipts.setCustomerPhoneNumber(item.getSoDienThoai());
            medicalFeeReceipts.setCustomerAge(Period.between(item.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
            medicalFeeReceipts.setCustomerGender(item.getSexId() == 1 ? "Nữ" : "Nam");
            medicalFeeReceipts.setCustomerEmail(item.getEmail());
        });
        medicalFeeReceipts.setPaymentMethods(medicalFeeReceipts.getTypePayment() == 1 ? "Chuyển khoản" : "Tiền mặt");
        medicalFeeReceipts.setText(FileUtils.convertToWords(medicalFeeReceipts.getTotalMoney()));
        medicalFeeReceipts.setPharmacyName(userInfo.getNhaThuoc().getTenNhaThuoc());
        medicalFeeReceipts.setPharmacyAddress(userInfo.getNhaThuoc().getDiaChi());
        medicalFeeReceipts.setPharmacyPhoneNumber(userInfo.getNhaThuoc().getDienThoai());
    }

    @Override
    public Long paymentMedicalNote(MedicalFeeReceiptsReq req) throws Exception {
        Long result = 0L;
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NoteMedicals> optional = noteMedicalsRepository.findById(req.getIdMedical());
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        var medicalFeeReceipts = new MedicalFeeReceipts();
        medicalFeeReceipts.setNoteNumber(getNewNoteNumber());
        medicalFeeReceipts.setTotalMoney(req.getTotalMoney());
        medicalFeeReceipts.setDiscount(req.getDiscount());
        medicalFeeReceipts.setTotalMoney(req.getTotalMoney());
        medicalFeeReceipts.setCreated(new Date());
        medicalFeeReceipts.setNoteDate(new Date());
        medicalFeeReceipts.setCreatedByUserId(userInfo.getId());
        medicalFeeReceipts.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        medicalFeeReceipts.setDescriptNotePay("Đã thanh toán cho phiếu khám số: " + req.getNoteNumber());
        medicalFeeReceipts.setRecordStatusId(RecordStatusContains.ACTIVE);
        medicalFeeReceipts.setIdCus(req.getIdCus());
        medicalFeeReceipts = hdrRepo.save(medicalFeeReceipts);

        var itemDetail = new MedicalFeeReceiptDetails();
        itemDetail.setIdBill(medicalFeeReceipts.getId());
        itemDetail.setNoteId(req.getIdMedical());
        itemDetail.setNoteNumber(req.getNoteNumber().longValue());
        itemDetail.setTypeNote(ENoteType.ExaminationCard);
        itemDetail.setStoreCode(userInfo.getNhaThuoc().getMaNhaThuoc());
        itemDetail.setCreated(new Date());
        itemDetail.setCreatedByUserId(userInfo.getId());
        itemDetail.setRecordStatusId(RecordStatusContains.ACTIVE);
        medicalFeeReceiptDetailsRepository.save(itemDetail);

        var medicalNote = optional.get();
        medicalNote.setIsDeb(false);
        noteMedicalsRepository.save(medicalNote);
        result = medicalFeeReceipts.getId();
        return result;
    }
}
