package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.entity.NhomBacSies;
import vn.com.gsoft.medical.model.dto.BacSiesReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.BacSiesRepository;
import vn.com.gsoft.medical.repository.NhomBacSiesRepository;
import vn.com.gsoft.medical.service.BacSiesService;

import java.util.Date;
import java.util.Optional;


@Service
@Log4j2
public class BacSiesServiceImpl extends BaseServiceImpl<BacSies, BacSiesReq,Long> implements BacSiesService {

	private BacSiesRepository hdrRepo;

	@Autowired
	public BacSiesServiceImpl(BacSiesRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}
	@Autowired
	private NhomBacSiesRepository nhomBacSiesRepository;
	@Override
	public Page<BacSies> searchPage(BacSiesReq req) throws Exception {
		Profile userInfo = this.getLoggedUser();
		if (userInfo == null)
			throw new Exception("Bad request.");
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
		req.setMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
		if(req.getDataDelete() != null){
			req.setRecordStatusId(req.getDataDelete() ? RecordStatusContains.DELETED : RecordStatusContains.ACTIVE);
		}
		Page<BacSies> bacSies = hdrRepo.searchPage(req, pageable);
		bacSies.getContent().forEach( item -> {
			if(item.getMaNhomBacSy()!=null){
				Optional<NhomBacSies> byId = nhomBacSiesRepository.findById(item.getMaNhomBacSy());
				byId.ifPresent(byIdI -> item.setTenNhomBacSy(byIdI.getTenNhomBacSy()));
			}
		});
		return bacSies;
	}

	@Override
	public BacSies create(BacSiesReq req) throws Exception {
		Profile userInfo = this.getLoggedUser();
		if (userInfo == null)
			throw new Exception("Bad request.");
		BacSies hdr = new BacSies();
		BeanUtils.copyProperties(req, hdr, "id");
		if(req.getRecordStatusId() == null){
			hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
		}
		hdr.setMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
		hdr.setCreatedByUserId(userInfo.getId());
		hdr.setCreated(new Date());
		hdr.setStoreId(userInfo.getNhaThuoc().getId());
		hdr.setMasterId(0);
		hdr.setMetadataHash(0);
		hdr.setPreMetadataHash(0);
		hdr.setModifiedByUserId(0L);

		return hdrRepo.save(hdr);
	}

	@Override
	public BacSies update(BacSiesReq req) throws Exception {
		Profile userInfo = this.getLoggedUser();
		if (userInfo == null)
			throw new Exception("Bad request.");
		Optional<BacSies> optional = hdrRepo.findById(req.getId());
		if (optional.isEmpty()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}
		BacSies hdr = optional.get();
		BeanUtils.copyProperties(req, hdr, "id");
		if(hdr.getRecordStatusId() == null){
			hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
		}
		hdr.setModifiedByUserId(userInfo.getId());
		hdr.setModified(new Date());
		hdr.setStoreId(userInfo.getNhaThuoc().getId());
		hdr.setMasterId(0);
		hdr.setMetadataHash(0);
		hdr.setPreMetadataHash(0);

		return hdrRepo.save(hdr);
	}
}
