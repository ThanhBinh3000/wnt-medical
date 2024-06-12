package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.MedicalFeeReceiptDetails;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptDetailsReq;

import java.util.List;

@Repository
public interface MedicalFeeReceiptDetailsRepository extends BaseRepository<MedicalFeeReceiptDetails, MedicalFeeReceiptDetailsReq, Long> {
  @Query("SELECT c FROM MedicalFeeReceiptDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " ORDER BY c.id desc"
  )
  Page<MedicalFeeReceiptDetails> searchPage(@Param("param") MedicalFeeReceiptDetailsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM MedicalFeeReceiptDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.noteNumber} IS NULL OR c.noteNumber = :#{#param.noteNumber}) "
          + " ORDER BY c.id desc"
  )
  List<MedicalFeeReceiptDetails> searchList(@Param("param") MedicalFeeReceiptDetailsReq param);

  List<MedicalFeeReceiptDetails> findByStoreCodeAndIdBill(String storeCode, Long idBill);
}
