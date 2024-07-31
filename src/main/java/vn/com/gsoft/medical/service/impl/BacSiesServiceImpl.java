package vn.com.gsoft.medical.service.impl;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.gsoft.medical.constant.ImportConstant;
import vn.com.gsoft.medical.constant.RecordStatusContains;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.entity.NhomBacSies;
import vn.com.gsoft.medical.entity.Process;
import vn.com.gsoft.medical.model.dto.BacSiesReq;
import vn.com.gsoft.medical.model.system.Profile;
import vn.com.gsoft.medical.model.system.WrapData;
import vn.com.gsoft.medical.repository.BacSiesRepository;
import vn.com.gsoft.medical.repository.NhomBacSiesRepository;
import vn.com.gsoft.medical.service.BacSiesService;
import vn.com.gsoft.medical.service.KafkaProducer;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;


@Service
@Log4j2
public class BacSiesServiceImpl extends BaseServiceImpl<BacSies, BacSiesReq, Long> implements BacSiesService {

    private BacSiesRepository hdrRepo;

    @Autowired
    public BacSiesServiceImpl(BacSiesRepository hdrRepo) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
    }

    @Autowired
    private NhomBacSiesRepository nhomBacSiesRepository;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Value("${wnt.kafka.internal.consumer.topic.import-master}")
	private String topicName;

    @Override
    public Page<BacSies> searchPage(BacSiesReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
        if (req.getDataDelete() != null) {
            req.setRecordStatusId(req.getDataDelete() ? RecordStatusContains.DELETED : RecordStatusContains.ACTIVE);
        }
        Page<BacSies> bacSies = hdrRepo.searchPage(req, pageable);
        bacSies.getContent().forEach(item -> {
            if (item.getMaNhomBacSy() != null) {
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
        if (req.getRecordStatusId() == null) {
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
        if (hdr.getRecordStatusId() == null) {
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

    @Override
    public Process importExcel(MultipartFile file) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Supplier<BacSies> bacSiesSupplier = BacSies::new;
        InputStream inputStream = file.getInputStream();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            List<String> propertyNames = Arrays.asList("code", "tenBacSy", "diaChi", "dienThoai"
                    , "email", "maNhaThuoc");
            List<BacSies> bacSies = new ArrayList<>(handleImportExcel(workbook, propertyNames,bacSiesSupplier));
            bacSies.forEach(item -> {
                item.setActive(true);
                item.setMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
                item.setStoreId(0L);
                item.setMasterId(0);
                item.setMetadataHash(0);
                item.setPreMetadataHash(0);
                item.setConnectCode("");
                item.setConnectPassword("");
                item.setIsConnectivity(true);
                item.setResultConnect("");
                item.setMaNhomBacSy(0L);
                item.setRecordStatusId(0L);
            });
            return pushToKafka(bacSies);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Process pushToKafka(List<BacSies> bacSies) throws Exception {
        int size = bacSies.size();
        int index = 1;
        UUID uuid = UUID.randomUUID();
        String batchKey = uuid.toString();
        Profile userInfo = this.getLoggedUser();
        Process process = kafkaProducer.createProcess(batchKey, userInfo.getNhaThuoc().getMaNhaThuoc(), new Gson().toJson(bacSies), new Date(),size, userInfo.getId());
        for(BacSies bs :bacSies){
			String key = bs.getMaNhaThuoc();
			WrapData<BacSies> data = new WrapData<>();
            data.setBatchKey(batchKey);
			data.setCode(ImportConstant.BAC_SI);
			data.setSendDate(new Date());
			data.setData(bs);
            data.setTotal(size);
            data.setIndex(index++);
            kafkaProducer.createProcessDtl(process, data);
			kafkaProducer.sendInternal(topicName, key, new Gson().toJson(data));
		}
        return process;
    }
}
