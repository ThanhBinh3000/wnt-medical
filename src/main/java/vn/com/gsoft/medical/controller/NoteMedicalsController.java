package vn.com.gsoft.medical.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.medical.constant.PathContains;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.model.system.BaseResponse;
import vn.com.gsoft.medical.service.NoteMedicalsService;
import vn.com.gsoft.medical.util.system.ResponseUtils;

import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping(PathContains.URL_PHIEU_KHAM)
public class NoteMedicalsController {

    @Autowired
    NoteMedicalsService service;


    @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
    }

    @PostMapping(value = PathContains.URL_SEARCH_PAGE+"-phieu-kham", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colectionPhieuKham(@RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPagePhieuKham(objReq)));
    }


    @PostMapping(value = PathContains.URL_SEARCH_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colectionList(@RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchList(objReq)));
    }


    @PostMapping(value = PathContains.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
    }


    @PostMapping(value = PathContains.URL_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.update(objReq)));
    }

    @PostMapping(value = PathContains.URL_LOCK, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> lock(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.lock(objReq)));
    }

    @PostMapping(value = PathContains.URL_INIT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> init(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.init(objReq)));
    }

    @GetMapping(value = PathContains.URL_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.detail(id)));
    }


    @PostMapping(value = PathContains.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody NoteMedicalsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.delete(idSearchReq.getId())));
    }

    @PostMapping(value = PathContains.URL_DELETE_DATABASE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteDatabase(@Valid @RequestBody NoteMedicalsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.deleteForever(idSearchReq.getId())));
    }

    @PostMapping(value = PathContains.URL_UPDATE_STATUS_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> updStatusMulti(@Valid @RequestBody NoteMedicalsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.updateMultiple(idSearchReq)));
    }

    @PostMapping(value = PathContains.URL_RESTORE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> restore(@Valid @RequestBody NoteMedicalsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.restore(idSearchReq.getId())));
    }

    @PostMapping(value = PathContains.URL_CANCEL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> cancel(@Valid @RequestBody NoteMedicalsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.cancel(idSearchReq.getId())));
    }

    @GetMapping(value = "get-new-note-wait-number", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getNewNoteWaitNumber() throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.getNewNoteWaitNumber()));
    }

    @PostMapping(value = PathContains.URL_CREATE + "-note-wait", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insertNoteWait(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.createNoteWait(objReq)));
    }


    @PostMapping(value = PathContains.URL_UPDATE + "-note-wait", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> updateNoteWait(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.updateNoteWait(objReq)));
    }

    @PostMapping(value = PathContains.URL_CHANGESTATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> changeStatusExam(@Valid @RequestBody NoteMedicalsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.changeStatusExam(objReq)));
    }

    @PostMapping(value = PathContains.URL_PREVIEW, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HashMap<String, Object> body) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.preview(body)));
    }
}
