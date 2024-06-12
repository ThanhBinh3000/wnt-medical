package vn.com.gsoft.medical.repository;

import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.gsoft.medical.entity.NoteServices;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteServicesRepository extends BaseRepository<NoteServices, NoteServicesReq, Long> {
  @Query("SELECT c FROM NoteServices c "
          + " WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.object} IS NULL OR lower(c.object) LIKE lower(concat('%',CONCAT(:#{#param.object},'%'))))"
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.barCode} IS NULL OR lower(c.barCode) LIKE lower(concat('%',CONCAT(:#{#param.barCode},'%'))))"
          + " AND (:#{#param.idNoteMedical} IS NULL OR c.idNoteMedical = :#{#param.idNoteMedical}) "
          + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
          + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
          + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
          + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
          + " AND (:#{#param.performerId} IS NULL OR c.performerId = :#{#param.performerId}) "
          + " AND (:#{#param.templateDocument} IS NULL OR lower(c.templateDocument) LIKE lower(concat('%',CONCAT(:#{#param.templateDocument},'%'))))"
          + " AND (:#{#param.fromDateCreated} IS NULL OR c.noteDate >= :#{#param.fromDateCreated}) "
          + " AND (:#{#param.toDateCreated} IS NULL OR c.noteDate <= :#{#param.toDateCreated}) "
          + " AND (:#{#param.maNhomKhachHang} IS NULL OR c.idCus IN (SELECT k.id FROM KhachHangs k WHERE k.maNhomKhachHang = :#{#param.maNhomKhachHang})) "
          + " ORDER BY c.id desc"
  )
  Page<NoteServices> searchPage(@Param("param") NoteServicesReq param, Pageable pageable);

  @Query(value = "SELECT c.id, c.storeCode, c.noteDate, c.idCus, d.drugId, d.countNumbers, d.amount, d.lastCountNumbers, d.retailOutPrice FROM NoteServices c "
          + " JOIN NoteServiceDetails d ON c.id = d.idNoteService"
          + " JOIN KhachHangs k ON c.idCus = k.id"
          + " WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.object} IS NULL OR lower(c.object) LIKE lower(concat('%',CONCAT(:#{#param.object},'%'))))"
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdBy_UserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedBy_UserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.barCode} IS NULL OR lower(c.barCode) LIKE lower(concat('%',CONCAT(:#{#param.barCode},'%'))))"
          + " AND (:#{#param.idNoteMedical} IS NULL OR c.idNoteMedical = :#{#param.idNoteMedical}) "
          + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
          + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
          + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
          + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
          + " AND (:#{#param.performerId} IS NULL OR c.performerId = :#{#param.performerId}) "
          + " AND (:#{#param.templateDocument} IS NULL OR lower(c.templateDocument) LIKE lower(concat('%',CONCAT(:#{#param.templateDocument},'%'))))"
          + " AND (:#{#param.fromDateCreated} IS NULL OR c.noteDate >= :#{#param.fromDateCreated}) "
          + " AND (:#{#param.toDateCreated} IS NULL OR c.noteDate <= :#{#param.toDateCreated}) "
          + " ORDER BY k.tenKhachHang", nativeQuery = true
  )
  Page<Tuple> searchPageLieuTrinh(@Param("param") NoteServicesReq param, Pageable pageable);

  @Query(value = "SELECT c.id, c.storeCode, c.noteDate, c.noteNumber, c.idCus, c.idDoctor, c.performerId, c.isDeb, d.idStatus, d.drugId FROM NoteServices c "
          + " JOIN NoteServiceDetails d ON c.id = d.idNoteService "
          + " WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.object} IS NULL OR lower(c.object) LIKE lower(concat('%',CONCAT(:#{#param.object},'%'))))"
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdBy_UserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedBy_UserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.barCode} IS NULL OR lower(c.barCode) LIKE lower(concat('%',CONCAT(:#{#param.barCode},'%'))))"
          + " AND (:#{#param.idNoteMedical} IS NULL OR c.idNoteMedical = :#{#param.idNoteMedical}) "
          + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
          + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
          + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
          + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
          + " AND (:#{#param.performerId} IS NULL OR c.performerId = :#{#param.performerId}) "
          + " AND (:#{#param.templateDocument} IS NULL OR lower(c.templateDocument) LIKE lower(concat('%',CONCAT(:#{#param.templateDocument},'%'))))"
          + " AND (:#{#param.fromDateCreated} IS NULL OR c.noteDate >= :#{#param.fromDateCreated}) "
          + " AND (:#{#param.toDateCreated} IS NULL OR c.noteDate <= :#{#param.toDateCreated}) "
          + " AND (:#{#param.idStatus} IS NULL OR d.idStatus = :#{#param.idStatus}) "
          + " AND (d.drugId IN (SELECT t.id FROM Thuocs t WHERE t.recordStatusId = 0 AND t.typeService = 1 AND (:#{#param.idClinic} IS NULL OR t.idClinic = :#{#param.idClinic})))"
          + " ORDER BY c.id desc", nativeQuery = true
  )
  Page<Tuple> searchPageChoThucHien(@Param("param") NoteServicesReq param, Pageable pageable);
  
  @Query("SELECT c FROM NoteServices c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.object} IS NULL OR lower(c.object) LIKE lower(concat('%',CONCAT(:#{#param.object},'%'))))"
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.barCode} IS NULL OR lower(c.barCode) LIKE lower(concat('%',CONCAT(:#{#param.barCode},'%'))))"
          + " AND (:#{#param.idNoteMedical} IS NULL OR c.idNoteMedical = :#{#param.idNoteMedical}) "
          + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
          + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
          + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
          + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
          + " AND (:#{#param.performerId} IS NULL OR c.performerId = :#{#param.performerId}) "
          + " AND (:#{#param.templateDocument} IS NULL OR lower(c.templateDocument) LIKE lower(concat('%',CONCAT(:#{#param.templateDocument},'%'))))"
          + " ORDER BY c.id desc"
  )
  List<NoteServices> searchList(@Param("param") NoteServicesReq param);

  List<NoteServices> findByStoreCodeAndRecordStatusIdAndIsDeb(String storeCode, Long recordStatusId, Boolean isDeb);

  @Query("SELECT t FROM NoteServices t WHERE t.barCode = :barCode AND t.storeCode = :maNhaThuoc  AND t.recordStatusId = :recordStatusId")
  Optional<NoteServices> findByBarCode(String barCode, String maNhaThuoc, Long recordStatusId);

  @Query("SELECT MAX(px.noteNumber) FROM NoteServices px where px.storeCode = ?1  ")
  Long findByNoteNumberMax(String nhaThuocMaNhaThuoc);

}
