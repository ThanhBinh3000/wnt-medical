package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.entity.NhomBacSies;
import vn.com.gsoft.medical.model.dto.BacSiesReq;

import java.util.List;

@Repository
public interface NhomBacSiesRepository extends CrudRepository<NhomBacSies, Long> {

}

