package vn.com.gsoft.medical.service;

import vn.com.gsoft.medical.model.system.Profile;

import java.util.Optional;

public interface UserService  {
    Optional<Profile> findUserByToken(String token);

}
