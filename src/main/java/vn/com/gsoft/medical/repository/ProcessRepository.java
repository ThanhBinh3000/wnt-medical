package vn.com.gsoft.medical.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.Process;

import java.util.Optional;

@Repository
public interface ProcessRepository extends CrudRepository<Process, Long> {
  Optional<Process> findByBatchKey(String batchKey);
}
