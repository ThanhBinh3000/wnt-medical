package vn.com.gsoft.medical.repository;

import vn.com.gsoft.categories.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface NoteMedicalsRepository extends BaseRepository<NoteMedicals, NoteMedicalsReq, Long> {
  @Query("SELECT c FROM NoteMedicals c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.idPatient} IS NULL OR c.idPatient = :#{#param.idPatient}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.noteDate} IS NULL OR c.noteDate >= :#{#param.noteDateFrom}) "
          + " AND (:#{#param.noteDate} IS NULL OR c.noteDate <= :#{#param.noteDateTo}) "
          + " AND (:#{#param.includingDiseases} IS NULL OR lower(c.includingDiseases) LIKE lower(concat('%',CONCAT(:#{#param.includingDiseases},'%'))))"
          + " AND (:#{#param.drugAllergy} IS NULL OR lower(c.drugAllergy) LIKE lower(concat('%',CONCAT(:#{#param.drugAllergy},'%'))))"
          + " AND (:#{#param.diagnostic} IS NULL OR lower(c.diagnostic) LIKE lower(concat('%',CONCAT(:#{#param.diagnostic},'%'))))"
          + " AND (:#{#param.conclude} IS NULL OR lower(c.conclude) LIKE lower(concat('%',CONCAT(:#{#param.conclude},'%'))))"
          + " AND (:#{#param.created} IS NULL OR c.created >= :#{#param.createdFrom}) "
          + " AND (:#{#param.created} IS NULL OR c.created <= :#{#param.createdTo}) "
          + " AND (:#{#param.modified} IS NULL OR c.modified >= :#{#param.modifiedFrom}) "
          + " AND (:#{#param.modified} IS NULL OR c.modified <= :#{#param.modifiedTo}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.reexaminationDate} IS NULL OR c.reexaminationDate >= :#{#param.reexaminationDateFrom}) "
          + " AND (:#{#param.reexaminationDate} IS NULL OR c.reexaminationDate <= :#{#param.reexaminationDateTo}) "
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.sickCondition} IS NULL OR c.sickCondition = :#{#param.sickCondition}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.heartbeat} IS NULL OR lower(c.heartbeat) LIKE lower(concat('%',CONCAT(:#{#param.heartbeat},'%'))))"
          + " AND (:#{#param.temperature} IS NULL OR lower(c.temperature) LIKE lower(concat('%',CONCAT(:#{#param.temperature},'%'))))"
          + " AND (:#{#param.weight} IS NULL OR lower(c.weight) LIKE lower(concat('%',CONCAT(:#{#param.weight},'%'))))"
          + " AND (:#{#param.bloodPressure} IS NULL OR lower(c.bloodPressure) LIKE lower(concat('%',CONCAT(:#{#param.bloodPressure},'%'))))"
          + " AND (:#{#param.breathing} IS NULL OR lower(c.breathing) LIKE lower(concat('%',CONCAT(:#{#param.breathing},'%'))))"
          + " AND (:#{#param.height} IS NULL OR lower(c.height) LIKE lower(concat('%',CONCAT(:#{#param.height},'%'))))"
          + " AND (:#{#param.clinicCode} IS NULL OR c.clinicCode = :#{#param.clinicCode}) "
          + " AND (:#{#param.statusNote} IS NULL OR c.statusNote = :#{#param.statusNote}) "
          + " AND (:#{#param.orderWait} IS NULL OR c.orderWait = :#{#param.orderWait}) "
          + " AND (:#{#param.reasonExamination} IS NULL OR lower(c.reasonExamination) LIKE lower(concat('%',CONCAT(:#{#param.reasonExamination},'%'))))"
          + " AND (:#{#param.idServiceExam} IS NULL OR c.idServiceExam = :#{#param.idServiceExam}) "
          + " AND (:#{#param.idDiagnostic} IS NULL OR c.idDiagnostic = :#{#param.idDiagnostic}) "
          + " AND (:#{#param.testResults} IS NULL OR lower(c.testResults) LIKE lower(concat('%',CONCAT(:#{#param.testResults},'%'))))"
          + " AND (:#{#param.diagnosticIds} IS NULL OR lower(c.diagnosticIds) LIKE lower(concat('%',CONCAT(:#{#param.diagnosticIds},'%'))))"
          + " AND (:#{#param.diagnosticOther} IS NULL OR lower(c.diagnosticOther) LIKE lower(concat('%',CONCAT(:#{#param.diagnosticOther},'%'))))"
          + " ORDER BY c.id desc"
  )
  Page<NoteMedicals> searchPage(@Param("param") NoteMedicalsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM NoteMedicals c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.idPatient} IS NULL OR c.idPatient = :#{#param.idPatient}) "
          + " AND (:#{#param.idDoctor} IS NULL OR c.idDoctor = :#{#param.idDoctor}) "
          + " AND (:#{#param.noteDate} IS NULL OR c.noteDate >= :#{#param.noteDateFrom}) "
          + " AND (:#{#param.noteDate} IS NULL OR c.noteDate <= :#{#param.noteDateTo}) "
          + " AND (:#{#param.includingDiseases} IS NULL OR lower(c.includingDiseases) LIKE lower(concat('%',CONCAT(:#{#param.includingDiseases},'%'))))"
          + " AND (:#{#param.drugAllergy} IS NULL OR lower(c.drugAllergy) LIKE lower(concat('%',CONCAT(:#{#param.drugAllergy},'%'))))"
          + " AND (:#{#param.diagnostic} IS NULL OR lower(c.diagnostic) LIKE lower(concat('%',CONCAT(:#{#param.diagnostic},'%'))))"
          + " AND (:#{#param.conclude} IS NULL OR lower(c.conclude) LIKE lower(concat('%',CONCAT(:#{#param.conclude},'%'))))"
          + " AND (:#{#param.created} IS NULL OR c.created >= :#{#param.createdFrom}) "
          + " AND (:#{#param.created} IS NULL OR c.created <= :#{#param.createdTo}) "
          + " AND (:#{#param.modified} IS NULL OR c.modified >= :#{#param.modifiedFrom}) "
          + " AND (:#{#param.modified} IS NULL OR c.modified <= :#{#param.modifiedTo}) "
          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.reexaminationDate} IS NULL OR c.reexaminationDate >= :#{#param.reexaminationDateFrom}) "
          + " AND (:#{#param.reexaminationDate} IS NULL OR c.reexaminationDate <= :#{#param.reexaminationDateTo}) "
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.sickCondition} IS NULL OR c.sickCondition = :#{#param.sickCondition}) "
          + " AND (:#{#param.clinicalExamination} IS NULL OR lower(c.clinicalExamination) LIKE lower(concat('%',CONCAT(:#{#param.clinicalExamination},'%'))))"
          + " AND (:#{#param.heartbeat} IS NULL OR lower(c.heartbeat) LIKE lower(concat('%',CONCAT(:#{#param.heartbeat},'%'))))"
          + " AND (:#{#param.temperature} IS NULL OR lower(c.temperature) LIKE lower(concat('%',CONCAT(:#{#param.temperature},'%'))))"
          + " AND (:#{#param.weight} IS NULL OR lower(c.weight) LIKE lower(concat('%',CONCAT(:#{#param.weight},'%'))))"
          + " AND (:#{#param.bloodPressure} IS NULL OR lower(c.bloodPressure) LIKE lower(concat('%',CONCAT(:#{#param.bloodPressure},'%'))))"
          + " AND (:#{#param.breathing} IS NULL OR lower(c.breathing) LIKE lower(concat('%',CONCAT(:#{#param.breathing},'%'))))"
          + " AND (:#{#param.height} IS NULL OR lower(c.height) LIKE lower(concat('%',CONCAT(:#{#param.height},'%'))))"
          + " AND (:#{#param.clinicCode} IS NULL OR c.clinicCode = :#{#param.clinicCode}) "
          + " AND (:#{#param.statusNote} IS NULL OR c.statusNote = :#{#param.statusNote}) "
          + " AND (:#{#param.orderWait} IS NULL OR c.orderWait = :#{#param.orderWait}) "
          + " AND (:#{#param.reasonExamination} IS NULL OR lower(c.reasonExamination) LIKE lower(concat('%',CONCAT(:#{#param.reasonExamination},'%'))))"
          + " AND (:#{#param.idServiceExam} IS NULL OR c.idServiceExam = :#{#param.idServiceExam}) "
          + " AND (:#{#param.idDiagnostic} IS NULL OR c.idDiagnostic = :#{#param.idDiagnostic}) "
          + " AND (:#{#param.testResults} IS NULL OR lower(c.testResults) LIKE lower(concat('%',CONCAT(:#{#param.testResults},'%'))))"
          + " AND (:#{#param.diagnosticIds} IS NULL OR lower(c.diagnosticIds) LIKE lower(concat('%',CONCAT(:#{#param.diagnosticIds},'%'))))"
          + " AND (:#{#param.diagnosticOther} IS NULL OR lower(c.diagnosticOther) LIKE lower(concat('%',CONCAT(:#{#param.diagnosticOther},'%'))))"
          + " ORDER BY c.id desc"
  )
  List<NoteMedicals> searchList(@Param("param") NoteMedicalsReq param);

}
