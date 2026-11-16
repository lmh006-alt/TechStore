package vn.iotstar.service;

import vn.iotstar.model.User;

public interface UserService {
    User login(String username, String password);

    User get(String username);

    boolean register(String username, String password, String email, String fullname, String phone);
    
    boolean verifyOTP(String username, String otp);
    
    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);
    boolean sendResetOTP(String username);

    boolean resetPassword(String username, String otp, String newPassword);
}
