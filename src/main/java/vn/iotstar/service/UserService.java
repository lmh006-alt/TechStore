package vn.iotstar.service;

import vn.iotstar.model.User;

public interface UserService {
    User login(String username, String password);

    User get(String username);

    User getById(int userId);

    boolean register(String username, String password, String email, String fullname, String phone);

    boolean updateProfile(int userId, String fullName, String phone);

    boolean updateAvatar(int userId, String avatar);

    boolean checkEmailUsedByOtherUser(String email, int userId);

    boolean updateEmail(int userId, String email);
    
    boolean verifyOTP(String username, String otp);
    
    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);
    boolean sendResetOTP(String username);

    boolean resetPassword(String username, String otp, String newPassword);
}
