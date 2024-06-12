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
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsCustomerDebtRes;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.system.ApplicationSetting;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.*;
import vn.com.gsoft.medical.service.MedicalFeeReceiptsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class MedicalFeeReceiptsServiceImpl extends BaseServiceImpl<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> implements MedicalFeeReceiptsService {

    private MedicalFeeReceiptsRepository hdrRepo;
    private KhachHangsRepository khachHangsRepository;
    private MedicalFeeReceiptDetailsRepository medicalFeeReceiptDetailsRepository;
    private NoteMedicalsRepository noteMedicalsRepository;
    private NoteServicesRepository noteServicesRepository;
    private ThuocsRepository thuocsRepository;

    @Autowired
    public MedicalFeeReceiptsServiceImpl(MedicalFeeReceiptsRepository hdrRepo, KhachHangsRepository khachHangsRepository, MedicalFeeReceiptDetailsRepository medicalFeeReceiptDetailsRepository, NoteMedicalsRepository noteMedicalsRepository, NoteServicesRepository noteServicesRepository, ThuocsRepository thuocsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.khachHangsRepository = khachHangsRepository;
        this.medicalFeeReceiptDetailsRepository = medicalFeeReceiptDetailsRepository;
        this.noteMedicalsRepository = noteMedicalsRepository;
        this.noteServicesRepository = noteServicesRepository;
        this.thuocsRepository = thuocsRepository;
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

        MedicalFeeReceipts noteMedicals = optional.get();
        if (noteMedicals.getIdCus() != null && noteMedicals.getIdCus() > 0) {
            Optional<KhachHangs> khachHangs = khachHangsRepository.findById(noteMedicals.getIdCus());
            khachHangs.ifPresent(hangs -> noteMedicals.setCustomerName(hangs.getTenKhachHang()));
        }
        noteMedicals.setChiTiets(medicalFeeReceiptDetailsRepository.findByStoreCodeAndIdBill(userInfo.getNhaThuoc().getMaNhaThuoc(), id));
        return noteMedicals;
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
                for(int i = 0; i < subItems.size(); i++) {
                    MedicalFeeReceiptsCustomerDebtRes item = subItems.get(i);
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
                for(int i = 0; i < subItems.size(); i++) {
                    MedicalFeeReceiptsCustomerDebtRes item = subItems.get(i);
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
}
