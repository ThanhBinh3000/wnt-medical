package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.PhongKhams;
import vn.com.gsoft.medical.repository.PhongKhamsRepository;
import vn.com.gsoft.medical.model.dto.PhongKhamsReq;
import vn.com.gsoft.medical.service.PhongKhamsService;


@Service
@Log4j2
public class PhongKhamsServiceImpl extends BaseServiceImpl<PhongKhams, PhongKhamsReq,Long> implements PhongKhamsService {

	private PhongKhamsRepository hdrRepo;
	@Autowired
	public PhongKhamsServiceImpl(PhongKhamsRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}

}
