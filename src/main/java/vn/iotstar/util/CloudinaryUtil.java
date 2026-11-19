package vn.iotstar.util;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public final class CloudinaryUtil {
    public static final long MAX_AVATAR_SIZE = 5L * 1024 * 1024;

    private CloudinaryUtil() {
    }

    public static String uploadAvatar(byte[] image, String filename)
            throws IOException {
        String format = validateImage(image, filename);
        Cloudinary cloudinary = createCloudinary();
        Map<?, ?> result = cloudinary.uploader().upload(image,
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "techstore/avatars"));

        Object secureUrl = result.get("secure_url");
        if (!(secureUrl instanceof String)
                || !((String) secureUrl).startsWith("https://")
                || ((String) secureUrl).length() > 255) {
            throw new IOException("Cloudinary returned an invalid avatar URL.");
        }
        Object uploadedFormat = result.get("format");
        if (!(uploadedFormat instanceof String)
                || !format.equalsIgnoreCase((String) uploadedFormat)) {
            throw new IOException("Cloudinary returned an unexpected image format.");
        }
        return (String) secureUrl;
    }

    private static Cloudinary createCloudinary() {
        String cloudName = requiredEnvironmentVariable("CLOUDINARY_CLOUD_NAME");
        String apiKey = requiredEnvironmentVariable("CLOUDINARY_API_KEY");
        String apiSecret = requiredEnvironmentVariable("CLOUDINARY_API_SECRET");
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is not configured: " + name);
        }
        return value;
    }

    private static String validateImage(byte[] image, String filename) {
        if (image == null || image.length == 0) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh đại diện.");
        }
        if (image.length > MAX_AVATAR_SIZE) {
            throw new IllegalArgumentException(
                    "Ảnh đại diện không được vượt quá 5 MB.");
        }
        if (filename == null) {
            throw new IllegalArgumentException("Tên tệp ảnh không hợp lệ.");
        }

        int extensionSeparator = filename.lastIndexOf('.');
        String extension = extensionSeparator < 0
                ? "" : filename.substring(extensionSeparator + 1)
                        .toLowerCase(Locale.ROOT);
        String format = switch (extension) {
            case "jpg", "jpeg" -> "jpg";
            case "png" -> "png";
            case "webp" -> "webp";
            default -> "";
        };
        if (format.isEmpty() || !hasImageSignature(image, format)) {
            throw new IllegalArgumentException(
                    "Chỉ chấp nhận ảnh JPG, JPEG, PNG hoặc WEBP hợp lệ.");
        }
        return format;
    }

    private static boolean hasImageSignature(byte[] image, String format) {
        if ("jpg".equals(format)) {
            return image.length >= 4
                    && (image[0] & 0xff) == 0xff
                    && (image[1] & 0xff) == 0xd8
                    && (image[2] & 0xff) == 0xff
                    && (image[image.length - 2] & 0xff) == 0xff
                    && (image[image.length - 1] & 0xff) == 0xd9;
        }
        if ("png".equals(format)) {
            return image.length >= 33
                    && (image[0] & 0xff) == 0x89
                    && image[1] == 0x50 && image[2] == 0x4e
                    && image[3] == 0x47 && image[4] == 0x0d
                    && image[5] == 0x0a && image[6] == 0x1a
                    && image[7] == 0x0a
                    && image[12] == 0x49 && image[13] == 0x48
                    && image[14] == 0x44 && image[15] == 0x52
                    && image[image.length - 8] == 0x49
                    && image[image.length - 7] == 0x45
                    && image[image.length - 6] == 0x4e
                    && image[image.length - 5] == 0x44;
        }
        return "webp".equals(format)
                && image.length >= 20
                && image[0] == 0x52 && image[1] == 0x49
                && image[2] == 0x46 && image[3] == 0x46
                && image[8] == 0x57 && image[9] == 0x45
                && image[10] == 0x42 && image[11] == 0x50
                && ((image[12] == 0x56 && image[13] == 0x50
                        && image[14] == 0x38 && image[15] == 0x20)
                    || (image[12] == 0x56 && image[13] == 0x50
                        && image[14] == 0x38 && image[15] == 0x4c)
                    || (image[12] == 0x56 && image[13] == 0x50
                        && image[14] == 0x38 && image[15] == 0x58));
    }
}
