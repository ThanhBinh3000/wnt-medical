package vn.com.gsoft.medical.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.gsoft.medical.entity.NoteServiceDetails;
import vn.com.gsoft.medical.entity.NoteServices;
import vn.com.gsoft.medical.model.dto.NoteServiceDetailsReq;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;

import java.util.List;

@Repository
public interface NoteServiceDetailsRepository extends CrudRepository<NoteServiceDetails, Long> {
  @Query("SELECT c FROM NoteServiceDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.idNoteService} IS NULL OR c.idNoteService = :#{#param.idNoteService}) "
          + " AND (:#{#param.drugId} IS NULL OR c.drugId = :#{#param.drugId}) "
          + " AND (:#{#param.amount} IS NULL OR c.amount = :#{#param.amount}) "
          + " AND (:#{#param.retailOutPrice} IS NULL OR c.retailOutPrice = :#{#param.retailOutPrice}) "
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
          + " ORDER BY c.id desc"
  )
  Page<NoteServiceDetails> searchPage(@Param("param") NoteServiceDetailsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM NoteServiceDetails c " +
         "WHERE 1=1 "
          + " AND (:#{#param.idNoteService} IS NULL OR c.idNoteService = :#{#param.idNoteService}) "
          + " AND (:#{#param.drugId} IS NULL OR c.drugId = :#{#param.drugId}) "
          + " AND (:#{#param.amount} IS NULL OR c.amount = :#{#param.amount}) "
          + " AND (:#{#param.retailOutPrice} IS NULL OR c.retailOutPrice = :#{#param.retailOutPrice}) "
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
          + " ORDER BY c.id desc"
  )
  List<NoteServiceDetails> searchList(@Param("param") NoteServiceDetailsReq param);

  @Query("SELECT c FROM NoteServiceDetails c " +
          " JOIN NoteServices hdr on c.idNoteService = hdr.id" +
          " JOIN Thuocs t on t.id = c.drugId" +
          " JOIN BacSies d on d.id = hdr.idDoctor " +
          " WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR hdr.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.idCus} IS NULL OR hdr.idCus = :#{#param.idCus}) "
          + " AND (:#{#param.idTypeService} IS NULL OR t.idTypeService = :#{#param.idTypeService}) "
          + " ORDER BY c.id desc"
  )
  List<NoteServiceDetails> searchListByCusId(@Param("param") NoteServicesReq param);

  List<NoteServiceDetails> findByIdNoteService(Long idNoteDetailService);

  void deleteAllByIdNoteService(Long idNoteService);
}
