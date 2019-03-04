package ua.nike.project.spring.service.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHACryptographer {

//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String passwordToHash = "password";
//        String securePassword = encryptPasswordBySHA512(passwordToHash);
//        System.out.println(securePassword);
//        System.out.println(checkPassword(passwordToHash, encryptPasswordBySHA512("password")));
//
//    }

    public static String encryptPasswordBySHA512(String passwordToHash) throws NoSuchAlgorithmException {
        if (passwordToHash == null || passwordToHash.equals("") || passwordToHash.equals(" ")) {
            passwordToHash = getSalt();
        }
        String salt = getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());
        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static boolean checkPassword(String password, String shaHash) throws NoSuchAlgorithmException {
        return shaHash.equals(encryptPasswordBySHA512(password));
    }

    private static String getSalt() {
        String salt = "96BqJAw2n4J2p#S-G+W_E_%$53dwn-@Qz@^*p=S4hJn7e=m+6DE4VwYuvZhyxUp9eekNaVkd8-RDKYWCUP@XVWpQm!kGNp#gm^Qp_J*UfzLTBLU8*3r#!t=+P4EcR_rH";
        return salt;
    }
}
