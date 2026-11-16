
package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

import vn.iotstar.connection.DBConnection;
import vn.iotstar.dao.UserDao;
import vn.iotstar.model.User;

public class UserDaoImpl implements UserDao {

    @Override
    public User get(String username) {
        String sql = "SELECT * FROM `User` WHERE username = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();

                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setUserName(rs.getString("username"));
                    user.setFullName(rs.getString("fullname"));
                    user.setPassWord(rs.getString("password"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setRoleid(rs.getInt("roleid"));
                    user.setPhone(rs.getString("phone"));
                    user.setCreatedDate(rs.getTimestamp("createdDate"));

                    user.setActive(rs.getBoolean("is_active"));
                    user.setOtpCode(rs.getString("otp_code"));
                    user.setOtpExpiry(rs.getTimestamp("otp_expiry"));

                    user.setResetOtpCode(rs.getString("reset_otp_code"));
                    user.setResetOtpExpiry(rs.getTimestamp("reset_otp_expiry"));

                    return user;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi truy van User", e);
        }

        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT id FROM `User` WHERE email = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi kiem tra email", e);
        }
    }

    @Override
    public boolean insert(User user) {
        String sql = """
            INSERT INTO `User`
            (username, email, fullname, password, roleid, phone,
             is_active, otp_code, otp_expiry)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getPassWord());
            ps.setInt(5, 2);
            ps.setString(6, user.getPhone());
            ps.setBoolean(7, user.isActive());
            ps.setString(8, user.getOtpCode());

            ps.setTimestamp(9, user.getOtpExpiry() == null
                    ? null
                    : new java.sql.Timestamp(user.getOtpExpiry().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Loi them User", e);
        }
    }

    @Override
    public boolean activateAccount(int userId) {
        String sql = """
            UPDATE `User`
            SET is_active = 1,
                otp_code = NULL,
                otp_expiry = NULL
            WHERE id = ? AND is_active = 0
            """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi kich hoat tai khoan", e);
        }
    }

    @Override
    public boolean saveResetOTP(int userId, String otpHash, Date expiry) {
        String sql = """
            UPDATE `User`
            SET reset_otp_code = ?,
                reset_otp_expiry = ?
            WHERE id = ? AND is_active = 1
            """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, otpHash);
            ps.setTimestamp(2,
                    new java.sql.Timestamp(expiry.getTime()));
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi luu OTP quen mat khau", e);
        }
    }

    @Override
    public boolean resetPassword(int userId, String passwordHash, String otpHash) {
        String sql = """
            UPDATE `User`
            SET password = ?,
                reset_otp_code = NULL,
                reset_otp_expiry = NULL
            WHERE id = ?
              AND is_active = 1
              AND reset_otp_code = ?
              AND reset_otp_expiry > CURRENT_TIMESTAMP
            """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setInt(2, userId);
            ps.setString(3, otpHash);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Loi dat lai mat khau", e);
        }
    }
}
