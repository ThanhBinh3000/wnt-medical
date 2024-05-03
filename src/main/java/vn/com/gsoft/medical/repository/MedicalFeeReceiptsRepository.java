package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.MedicalFeeReceipts;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;

import java.util.List;

@Repository
public interface MedicalFeeReceiptsRepository extends BaseRepository<MedicalFeeReceipts, MedicalFeeReceiptsReq, Long> {
  @Query("SELECT c FROM MedicalFeeReceipts c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.discount} IS NULL OR c.discount = :#{#param.discount}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.description} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.description},'%'))))"
          + " AND (:#{#param.descriptNotePay} IS NULL OR lower(c.descriptNotePay) LIKE lower(concat('%',CONCAT(:#{#param.descriptNotePay},'%'))))"
          + " AND (:#{#param.debtAmount} IS NULL OR c.debtAmount = :#{#param.debtAmount}) "
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.typePayment} IS NULL OR c.typePayment = :#{#param.typePayment}) "
          + " ORDER BY c.id desc"
  )
  Page<MedicalFeeReceipts> searchPage(@Param("param") MedicalFeeReceiptsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM MedicalFeeReceipts c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.totalMoney} IS NULL OR c.totalMoney = :#{#param.totalMoney}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.discount} IS NULL OR c.discount = :#{#param.discount}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " AND (:#{#param.description} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.description},'%'))))"
          + " AND (:#{#param.descriptNotePay} IS NULL OR lower(c.descriptNotePay) LIKE lower(concat('%',CONCAT(:#{#param.descriptNotePay},'%'))))"
          + " AND (:#{#param.debtAmount} IS NULL OR c.debtAmount = :#{#param.debtAmount}) "
          + " AND (:#{#param.idCus} IS NULL OR c.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.typePayment} IS NULL OR c.typePayment = :#{#param.typePayment}) "
          + " ORDER BY c.id desc"
  )
  List<MedicalFeeReceipts> searchList(@Param("param") MedicalFeeReceiptsReq param);

}
