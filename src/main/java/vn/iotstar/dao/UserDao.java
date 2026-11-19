
package vn.iotstar.dao;

import java.util.Date;
import vn.iotstar.model.User;

public interface UserDao {

    User get(String username);

    User getById(int userId);

    boolean existsByEmail(String email);

    boolean existsByEmailForOtherUser(String email, int userId);

    boolean insert(User user);

    boolean updateProfile(int userId, String fullName, String phone);

    boolean updateAvatar(int userId, String avatar);

    boolean updateEmail(int userId, String email);

    boolean activateAccount(int userId);

    boolean saveResetOTP(int userId, String otpHash, Date expiry);

    boolean resetPassword(int userId, String passwordHash, String otpHash);
}
