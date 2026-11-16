
package vn.iotstar.service.impl;

import java.security.SecureRandom;
import java.util.Date;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PasswordUtil;
import vn.iotstar.util.EmailUtil;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = userDao.get(username);

        if (user != null
                && user.isActive()
                && PasswordUtil.verify(password, user.getPassWord())) {
            return user;
        }

        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.get(username) != null;
    }

    @Override
    public boolean register(String username, String password,
            String email, String fullname, String phone) {

        if (checkExistUsername(username) || checkExistEmail(email)) {
            return false;
        }

        String otp = String.format("%06d",
                new SecureRandom().nextInt(1000000));

        User user = new User();
        user.setUserName(username);
        user.setPassWord(PasswordUtil.hash(password));
        user.setEmail(email);
        user.setFullName(fullname);
        user.setPhone(phone);
        user.setRoleid(2);
        user.setActive(false);

        user.setOtpCode(PasswordUtil.hash(otp));
        user.setOtpExpiry(new Date(
                System.currentTimeMillis() + 5 * 60 * 1000));

        boolean success = userDao.insert(user);
        if (!success) {
            return false;
        }

        try {
            EmailUtil.sendOTP(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean verifyOTP(String username, String otp) {
        User user = userDao.get(username);

        if (user == null || user.isActive()) {
            return false;
        }

        if (otp == null || user.getOtpCode() == null
                || user.getOtpExpiry() == null) {
            return false;
        }

        if (new Date().after(user.getOtpExpiry())) {
            return false;
        }

        if (!PasswordUtil.verify(otp, user.getOtpCode())) {
            return false;
        }

        return userDao.activateAccount(user.getId());
    }

    // BAI 3: Gui OTP quen mat khau
    @Override
    public boolean sendResetOTP(String username) {
        User user = userDao.get(username);

        if (user == null || !user.isActive()) {
            return false;
        }

        String otp = String.format("%06d",
                new SecureRandom().nextInt(1000000));

        String otpHash = PasswordUtil.hash(otp);
        Date expiry = new Date(
                System.currentTimeMillis() + 5 * 60 * 1000);

        if (!userDao.saveResetOTP(user.getId(), otpHash, expiry)) {
            return false;
        }

        try {
            EmailUtil.sendOTP(user.getEmail(), otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // BAI 3: Xac minh OTP va doi mat khau
    @Override
    public boolean resetPassword(String username,
            String otp, String newPassword) {

        User user = userDao.get(username);

        if (user == null || !user.isActive()) {
            return false;
        }

        if (otp == null || !otp.matches("\\d{6}")
                || newPassword == null || newPassword.length() < 8) {
            return false;
        }

        if (user.getResetOtpCode() == null
                || user.getResetOtpExpiry() == null) {
            return false;
        }

        if (!new Date().before(user.getResetOtpExpiry())) {
            return false;
        }

        if (!PasswordUtil.verify(otp, user.getResetOtpCode())) {
            return false;
        }

        String passwordHash = PasswordUtil.hash(newPassword);

        return userDao.resetPassword(
                user.getId(), passwordHash, user.getResetOtpCode());
    }
}
