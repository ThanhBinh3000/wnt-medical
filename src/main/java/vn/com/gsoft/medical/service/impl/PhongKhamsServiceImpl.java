package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.entity.PhongKhams;
import vn.com.gsoft.medical.model.dto.BacSiesReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.repository.PhongKhamsRepository;
import vn.com.gsoft.medical.model.dto.PhongKhamsReq;
import vn.com.gsoft.medical.service.PhongKhamsService;

import java.util.Date;
import java.util.Optional;


@Service
@Log4j2
public class PhongKhamsServiceImpl extends BaseServiceImpl<PhongKhams, PhongKhamsReq,Long> implements PhongKhamsService {

	private PhongKhamsRepository hdrRepo;
	@Autowired
	public PhongKhamsServiceImpl(PhongKhamsRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}

	@Override
	public PhongKhams create(PhongKhamsReq req) throws Exception {
		Profile userInfo = this.getLoggedUser();
		if (userInfo == null)
			throw new Exception("Bad request.");
		PhongKhams hdr = new PhongKhams();
		BeanUtils.copyProperties(req, hdr, "id");
		if(req.getRecordStatusId() == null){
			hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
		}
		hdr.setMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
		hdr.setCreatedByUserId(userInfo.getId());
		hdr.setCreated(new Date());
		hdr.setModifiedByUserId(0L);

		return hdrRepo.save(hdr);
	}

	@Override
	public PhongKhams update(PhongKhamsReq req) throws Exception {
		Profile userInfo = this.getLoggedUser();
		if (userInfo == null)
			throw new Exception("Bad request.");
		Optional<PhongKhams> optional = hdrRepo.findById(req.getId());
		if (optional.isEmpty()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}
		PhongKhams hdr = optional.get();
		BeanUtils.copyProperties(req, hdr, "id");
		if(hdr.getRecordStatusId() == null){
			hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
		}
		hdr.setModifiedByUserId(userInfo.getId());
		hdr.setModified(new Date());

		return hdrRepo.save(hdr);
	}

}
