package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.SampleNoteDetail;
import vn.com.gsoft.medical.model.dto.SampleNoteDetailReq;

import java.util.List;

@Repository
public interface SampleNoteDetailRepository extends BaseRepository<SampleNoteDetail, SampleNoteDetailReq, Long> {
    @Query("SELECT c FROM SampleNoteDetail c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
            + " AND (:#{#param.noteID} IS NULL OR c.noteID = :#{#param.noteID}) "
            + " AND (:#{#param.drugID} IS NULL OR c.drugID = :#{#param.drugID}) "
            + " AND (:#{#param.drugUnitID} IS NULL OR c.drugUnitID = :#{#param.drugUnitID}) "
            + " AND (:#{#param.comment} IS NULL OR lower(c.comment) LIKE lower(concat('%',CONCAT(:#{#param.comment},'%'))))"
            + " AND (:#{#param.drugStoreID} IS NULL OR lower(c.drugStoreID) LIKE lower(concat('%',CONCAT(:#{#param.drugStoreID},'%'))))"
            + " AND (:#{#param.quantity} IS NULL OR c.quantity = :#{#param.quantity}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " AND (:#{#param.batch} IS NULL OR c.batch = :#{#param.batch}) "
            + " AND (:#{#param.numberOfPotionBars} IS NULL OR lower(c.numberOfPotionBars) LIKE lower(concat('%',CONCAT(:#{#param.numberOfPotionBars},'%'))))"
            + " ORDER BY c.id desc"
    )
    Page<SampleNoteDetail> searchPage(@Param("param") SampleNoteDetailReq param, Pageable pageable);


    @Query("SELECT c FROM SampleNoteDetail c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
            + " AND (:#{#param.noteID} IS NULL OR c.noteID = :#{#param.noteID}) "
            + " AND (:#{#param.drugID} IS NULL OR c.drugID = :#{#param.drugID}) "
            + " AND (:#{#param.drugUnitID} IS NULL OR c.drugUnitID = :#{#param.drugUnitID}) "
            + " AND (:#{#param.comment} IS NULL OR lower(c.comment) LIKE lower(concat('%',CONCAT(:#{#param.comment},'%'))))"
            + " AND (:#{#param.drugStoreID} IS NULL OR lower(c.drugStoreID) LIKE lower(concat('%',CONCAT(:#{#param.drugStoreID},'%'))))"
            + " AND (:#{#param.quantity} IS NULL OR c.quantity = :#{#param.quantity}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " AND (:#{#param.batch} IS NULL OR c.batch = :#{#param.batch}) "
            + " AND (:#{#param.numberOfPotionBars} IS NULL OR lower(c.numberOfPotionBars) LIKE lower(concat('%',CONCAT(:#{#param.numberOfPotionBars},'%'))))"
            + " ORDER BY c.id desc"
    )
    List<SampleNoteDetail> searchList(@Param("param") SampleNoteDetailReq param);

    @Query("SELECT COUNT(DISTINCT p.drugID) FROM SampleNoteDetail p where p.noteID = ?1")
    Long countByNoteID(Long noteID);

    List<SampleNoteDetail> findByNoteID(Long noteId);

    void deleteAllByNoteID(Long noteID);

    @Query("SELECT c FROM SampleNoteDetail c " +
            "JOIN SampleNote d on c.noteID = d.id " +
            "WHERE 1=1 and d.patientId =?1 "
            + " ORDER BY c.id desc"
    )
    List<SampleNoteDetail> findByPatientId(Long idPatient);
}
