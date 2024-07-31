package vn.com.gsoft.medical.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.ProcessDtl;

@Repository
public interface ProcessDtlRepository extends CrudRepository<ProcessDtl, Long> {

}
