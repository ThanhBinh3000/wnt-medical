package vn.com.gsoft.medical.util.system;

import lombok.extern.slf4j.Slf4j;
import vn.com.gsoft.medical.enums.EnumResponse;
import vn.com.gsoft.medical.model.system.BaseResponse;


@Slf4j
public class ResponseUtils {
    public static BaseResponse ok(Object data) {
        BaseResponse resp = new BaseResponse();
        resp.setData(data);
        resp.setStatus(EnumResponse.RESP_SUCC.getValue());
        resp.setMessage(EnumResponse.RESP_SUCC.getDescription());
        return resp;
    }
}
