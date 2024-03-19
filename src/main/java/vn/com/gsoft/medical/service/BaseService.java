package vn.com.gsoft.medical.service;

import vn.com.gsoft.medical.model.system.Profile;

public interface BaseService {
    Profile getLoggedUser() throws Exception;

}
