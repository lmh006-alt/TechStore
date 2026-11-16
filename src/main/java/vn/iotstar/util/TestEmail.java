
package vn.iotstar.util;

public class TestEmail {

    public static void main(String[] args) {
        try {
            String email = System.getenv("MAIL_USERNAME");

            if (email == null || email.isBlank()) {
                System.out.println("Chua cau hinh MAIL_USERNAME");
                return;
            }

            EmailUtil.sendOTP(email, "123456");

            System.out.println("Gui email thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
