
package vn.iotstar.dao;

import java.util.Date;
import vn.iotstar.model.User;

public interface UserDao {

    User get(String username);

    boolean existsByEmail(String email);

    boolean insert(User user);

    boolean activateAccount(int userId);

    boolean saveResetOTP(int userId, String otpHash, Date expiry);

    boolean resetPassword(int userId, String passwordHash, String otpHash);
}
