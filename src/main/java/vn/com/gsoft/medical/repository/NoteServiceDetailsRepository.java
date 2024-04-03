package vn.com.gsoft.medical.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.gsoft.medical.entity.NoteServiceDetails;
import vn.com.gsoft.medical.model.dto.NoteServiceDetailsReq;

import java.util.List;

@Repository
public interface NoteServiceDetailsRepository extends BaseRepository<NoteServiceDetails, NoteServiceDetailsReq, Long> {
  @Query("SELECT c FROM NoteServiceDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.idNoteDetailService} IS NULL OR c.idNoteDetailService = :#{#param.idNoteDetailService}) "
          + " AND (:#{#param.idNoteService} IS NULL OR c.idNoteService = :#{#param.idNoteService}) "
          + " AND (:#{#param.drugId} IS NULL OR c.drugId = :#{#param.drugId}) "
          + " AND (:#{#param.amount} IS NULL OR c.amount = :#{#param.amount}) "
          + " AND (:#{#param.retailOutPrice} IS NULL OR c.retailOutPrice = :#{#param.retailOutPrice}) "
          + " AND (:#{#param.recordStatusID} IS NULL OR c.recordStatusID = :#{#param.recordStatusID}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.createdById} IS NULL OR c.createdById = :#{#param.createdById}) "
          + " AND (:#{#param.updatedById} IS NULL OR c.updatedById = :#{#param.updatedById}) "
          + " AND (:#{#param.implementationRoomCode} IS NULL OR c.implementationRoomCode = :#{#param.implementationRoomCode}) "
          + " AND (:#{#param.indexDescription} IS NULL OR lower(c.indexDescription) LIKE lower(concat('%',CONCAT(:#{#param.indexDescription},'%'))))"
          + " AND (:#{#param.result} IS NULL OR lower(c.result) LIKE lower(concat('%',CONCAT(:#{#param.result},'%'))))"
          + " AND (:#{#param.idNoteDetail} IS NULL OR c.idNoteDetail = :#{#param.idNoteDetail}) "
          + " AND (:#{#param.countNumbers} IS NULL OR c.countNumbers = :#{#param.countNumbers}) "
          + " AND (:#{#param.lastCountNumbers} IS NULL OR c.lastCountNumbers = :#{#param.lastCountNumbers}) "
          + " AND (:#{#param.textDocument} IS NULL OR lower(c.textDocument) LIKE lower(concat('%',CONCAT(:#{#param.textDocument},'%'))))"
          + " AND (:#{#param.resultImage1} IS NULL OR lower(c.resultImage1) LIKE lower(concat('%',CONCAT(:#{#param.resultImage1},'%'))))"
          + " AND (:#{#param.resultImage2} IS NULL OR lower(c.resultImage2) LIKE lower(concat('%',CONCAT(:#{#param.resultImage2},'%'))))"
          + " ORDER BY c.idNoteDetailService desc"
  )
  Page<NoteServiceDetails> searchPage(@Param("param") NoteServiceDetailsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM NoteServiceDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.idNoteDetailService} IS NULL OR c.idNoteDetailService = :#{#param.idNoteDetailService}) "
          + " AND (:#{#param.idNoteService} IS NULL OR c.idNoteService = :#{#param.idNoteService}) "
          + " AND (:#{#param.drugId} IS NULL OR c.drugId = :#{#param.drugId}) "
          + " AND (:#{#param.amount} IS NULL OR c.amount = :#{#param.amount}) "
          + " AND (:#{#param.retailOutPrice} IS NULL OR c.retailOutPrice = :#{#param.retailOutPrice}) "
          + " AND (:#{#param.recordStatusID} IS NULL OR c.recordStatusID = :#{#param.recordStatusID}) "
          + " AND (:#{#param.storeCode} IS NULL OR lower(c.storeCode) LIKE lower(concat('%',CONCAT(:#{#param.storeCode},'%'))))"
          + " AND (:#{#param.createdById} IS NULL OR c.createdById = :#{#param.createdById}) "
          + " AND (:#{#param.updatedById} IS NULL OR c.updatedById = :#{#param.updatedById}) "
          + " AND (:#{#param.implementationRoomCode} IS NULL OR c.implementationRoomCode = :#{#param.implementationRoomCode}) "
          + " AND (:#{#param.indexDescription} IS NULL OR lower(c.indexDescription) LIKE lower(concat('%',CONCAT(:#{#param.indexDescription},'%'))))"
          + " AND (:#{#param.result} IS NULL OR lower(c.result) LIKE lower(concat('%',CONCAT(:#{#param.result},'%'))))"
          + " AND (:#{#param.idNoteDetail} IS NULL OR c.idNoteDetail = :#{#param.idNoteDetail}) "
          + " AND (:#{#param.countNumbers} IS NULL OR c.countNumbers = :#{#param.countNumbers}) "
          + " AND (:#{#param.lastCountNumbers} IS NULL OR c.lastCountNumbers = :#{#param.lastCountNumbers}) "
          + " AND (:#{#param.textDocument} IS NULL OR lower(c.textDocument) LIKE lower(concat('%',CONCAT(:#{#param.textDocument},'%'))))"
          + " AND (:#{#param.resultImage1} IS NULL OR lower(c.resultImage1) LIKE lower(concat('%',CONCAT(:#{#param.resultImage1},'%'))))"
          + " AND (:#{#param.resultImage2} IS NULL OR lower(c.resultImage2) LIKE lower(concat('%',CONCAT(:#{#param.resultImage2},'%'))))"
          + " ORDER BY c.idNoteDetailService desc"
  )
  List<NoteServiceDetails> searchList(@Param("param") NoteServiceDetailsReq param);

}
