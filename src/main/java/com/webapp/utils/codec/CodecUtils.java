/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.webapp.utils.string.Utils
 *  com.webapp.utils.string.Utils$Charsets
 *  org.apache.commons.codec.DecoderException
 *  org.apache.commons.codec.binary.Base64
 *  org.apache.commons.codec.binary.Hex
 *  org.apache.commons.codec.binary.StringUtils
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.commons.lang.StringEscapeUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.webapp.utils.codec;

import com.webapp.utils.string.Utils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CodecUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class)CodecUtils.class);
    private static final String UNICODE = "unicode";
    private static final String FIXED = "fixed_key";

    public static String bytesToString(byte[] data) {
        return StringUtils.newStringUtf8((byte[])data);
    }

    private static void checkIV(AlgoBuilder.AlgoBuild algo) {
        if (!algo.getKeyAlgo().contains(AlgoSymEnum.AlgoSym.DES.name()) && algo.getIv().length() != 16) {
            logger.error("The iv must be 16 bits");
        } else if (algo.getKeyAlgo().contains(AlgoSymEnum.AlgoSym.DES.name()) && algo.getIv().length() != 8) {
            logger.error("The iv must be 8 bits");
        }
    }

    public static class Signing {
        private byte[] data;

        private Signing(byte[] data) {
            this.data = data;
        }

        public static Signing of(byte[] data) {
            return new Signing(data);
        }

        public static Signing of(String data) {
            return new Signing(Decode.decodeBase64(data.getBytes()));
        }

        public String sign(PrivateKey privateKey) {
            byte[] result = null;
            try {
                Signature sign = Signature.getInstance(privateKey.getAlgorithm());
                sign.initSign(privateKey);
                sign.update(this.data);
                result = sign.sign();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return Encode.encodeBase64Str(result);
        }

        public boolean verify(String sign, PublicKey publicKey) {
            boolean result = false;
            try {
                Signature signa = Signature.getInstance(publicKey.getAlgorithm());
                signa.initVerify(publicKey);
                signa.update(this.data);
                result = signa.verify(Decode.decodeBase64(sign.getBytes()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class Decrypt {
        private byte[] data;

        private Decrypt(byte[] data) {
            this.data = data;
        }

        public static Decrypt of(byte[] data) {
            return new Decrypt(data);
        }

        public static Decrypt of(String data) {
            return new Decrypt(Decode.decodeBase64(data.getBytes()));
        }

        private static byte[] decrypt(byte[] data, AlgoBuilder.AlgoBuild algo, Key key) {
            byte[] result = null;
            try {
                Cipher cipher = Cipher.getInstance(algo.getCipherAlgo());
                if (algo.isCBC()) {
                    CodecUtils.checkIV(algo);
                    cipher.init(2, key, new IvParameterSpec(algo.getIv().getBytes()));
                } else {
                    cipher.init(2, key);
                }
                result = cipher.doFinal(data);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoSymBuild algoSym) {
            this.data = Decrypt.decrypt(this.data, algoSym, KeyUtils.genSecretKey(algoSym.getAlgoSym(), DigestUtils.sha256Hex((String)"fixed_key")));
            return this;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoSymBuild algoSym, byte[] key) {
            SecretKeySpec secretKey = new SecretKeySpec(key, algoSym.getKeyAlgo());
            this.data = Decrypt.decrypt(this.data, algoSym, secretKey);
            return this;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoSymBuild algoSym, String base64Key) {
            this.decrypt(algoSym, Decode.decodeBase64(base64Key.getBytes()));
            return this;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoBuild algoSym, Key key) {
            this.data = Decrypt.decrypt(this.data, algoSym, key);
            return this;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoAsymBuild algoAsym, byte[] key) {
            this.data = Decrypt.decrypt(this.data, algoAsym, KeyUtils.getPrivateKey(algoAsym.getAlgoAsym(), key));
            return this;
        }

        public Decrypt decrypt(AlgoBuilder.AlgoAsymBuild algoAsym, String base64Key) {
            this.decrypt(algoAsym, Decode.decodeBase64(base64Key.getBytes()));
            return this;
        }

        public byte[] toByte() {
            return this.data;
        }

        public String toStr() {
            return StringUtils.newStringUtf8((byte[])this.data).trim();
        }
    }

    public static class Encrypt {
        private byte[] data;

        private static byte[] cbcPadding(byte[] data) {
            int len = data.length;
            if (len % 16 == 0) {
                return data;
            }
            while (len % 16 != 0) {
                ++len;
            }
            byte[] result = new byte[len];
            for (int i = 0; i < len; ++i) {
                result[i] = i < data.length ? data[i] : 0;
            }
            return result;
        }

        private Encrypt(byte[] data) {
            this.data = data;
        }

        public static Encrypt of(byte[] data) {
            return new Encrypt(data);
        }

        public static Encrypt of(String data) {
            return new Encrypt(data.getBytes());
        }

        private static byte[] encrypt(byte[] data, AlgoBuilder.AlgoBuild algo, Key key) {
            byte[] result = null;
            try {
                Cipher cipher = Cipher.getInstance(algo.getCipherAlgo());
                if (algo.isCBC()) {
                    CodecUtils.checkIV(algo);
                    cipher.init(1, key, new IvParameterSpec(algo.getIv().getBytes()));
                } else {
                    cipher.init(1, key);
                }
                if (algo.isNoPadding()) {
                    data = Encrypt.cbcPadding(data);
                }
                result = cipher.doFinal(data);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoSymBuild algoSym) {
            this.data = Encrypt.encrypt(this.data, algoSym, KeyUtils.genSecretKey(algoSym.getAlgoSym(), DigestUtils.sha256Hex((String)"fixed_key")));
            return this;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoSymBuild algoSym, byte[] key) {
            this.data = Encrypt.encrypt(this.data, algoSym, KeyUtils.getSecretKey(algoSym.getAlgoSym(), key));
            return this;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoSymBuild algoSym, String base64Key) {
            this.encrypt(algoSym, Decode.decodeBase64(base64Key.getBytes()));
            return this;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoBuild algoSym, Key key) {
            this.data = Encrypt.encrypt(this.data, algoSym, key);
            return this;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoAsymBuild algoAsym, byte[] key) {
            this.data = Encrypt.encrypt(this.data, algoAsym, KeyUtils.getPublicKey(algoAsym.getAlgoAsym(), key));
            return this;
        }

        public Encrypt encrypt(AlgoBuilder.AlgoAsymBuild algoAsym, String base64Key) {
            this.encrypt(algoAsym, Decode.decodeBase64(base64Key.getBytes()));
            return this;
        }

        public byte[] toByte() {
            return this.data;
        }

        public byte[] toHex() {
            return this.toHexStr().getBytes();
        }

        public String toHexStr() {
            return Encode.encodeRadixStr(this.data, 16);
        }

        public byte[] toBase64() {
            return Encode.encodeBase64(this.data);
        }

        public String toBase64Str() {
            return Encode.encodeBase64Str(this.data);
        }
    }

    public static class CryptBase {
        protected byte[] data;

        protected CryptBase(byte[] data) {
            this.data = data;
        }
    }

    public static class AlgoAsymEnum {

        public static enum AlgoAsymPadding {
            PKCS1Padding,
            OAEPWithSHA1AndMGF1Padding,
            OAEPWithSHA256AndMGF1Padding;
            

            private AlgoAsymPadding() {
            }
        }

        public static enum AlgoAsymMode {
            ECB;
            

            private AlgoAsymMode() {
            }
        }

        public static enum AlgoAsym {
            RSA("RSA", 1024, false);
            
            private String name;
            private int keySize;
            private boolean isSym;

            private AlgoAsym(String name, int keySize, boolean isSym) {
                this.name = name;
                this.keySize = keySize;
                this.isSym = isSym;
            }

            public String getName() {
                return this.name;
            }

            public int getKeySize() {
                return this.keySize;
            }

            public boolean isSym() {
                return this.isSym;
            }
        }

    }

    public static class AlgoSymEnum {

        public static enum AlgoSymPadding {
            NoPadding,
            ZerosPadding,
            PKCS5Padding;
            

            private AlgoSymPadding() {
            }
        }

        public static enum AlgoSymMode {
            ECB,
            CBC;
            

            private AlgoSymMode() {
            }
        }

        public static enum AlgoSym {
            AES("AES", 128),
            DES("DES", 56),
            DESede("DESede", 168);
            
            private String name;
            private int keySize;

            private AlgoSym(String name, int keySize) {
                this.name = name;
                this.keySize = keySize;
            }

            public String getName() {
                return this.name;
            }

            public int getKeySize() {
                return this.keySize;
            }
        }

    }

    public static class AlgoBuilder {
        public static AlgoSymBuild build(AlgoSymEnum.AlgoSym algoSym) {
            return new AlgoSymBuild(algoSym);
        }

        public static AlgoSymBuild build(AlgoSymEnum.AlgoSym algoSym, AlgoSymEnum.AlgoSymMode mode, AlgoSymEnum.AlgoSymPadding padding) {
            return new AlgoSymBuild(algoSym, mode, padding);
        }

        public static AlgoSymBuild build(AlgoSymEnum.AlgoSym algoSym, AlgoSymEnum.AlgoSymPadding padding, String iv) {
            return new AlgoSymBuild(algoSym, padding, iv);
        }

        public static AlgoAsymBuild build(AlgoAsymEnum.AlgoAsym algoAsym) {
            return new AlgoAsymBuild(algoAsym);
        }

        public static AlgoAsymBuild build(AlgoAsymEnum.AlgoAsym algoAsym, AlgoAsymEnum.AlgoAsymMode mode, AlgoAsymEnum.AlgoAsymPadding padding) {
            return new AlgoAsymBuild(algoAsym, mode, padding);
        }

        public static AlgoAsymBuild build(AlgoAsymEnum.AlgoAsym algoAsym, AlgoAsymEnum.AlgoAsymPadding padding, String iv) {
            return new AlgoAsymBuild(algoAsym, padding, iv);
        }

        public static class AlgoAsymBuild
        extends AlgoBuild {
            private AlgoAsymEnum.AlgoAsym algoAsym;

            private AlgoAsymBuild(AlgoAsymEnum.AlgoAsym algoAsym) {
                super(algoAsym.getName(), algoAsym.getKeySize());
                this.algoAsym = algoAsym;
            }

            private AlgoAsymBuild(AlgoAsymEnum.AlgoAsym algoAsym, AlgoAsymEnum.AlgoAsymMode mode, AlgoAsymEnum.AlgoAsymPadding padding) {
                super(algoAsym.getName(), mode.name(), padding.name(), algoAsym.getKeySize());
                this.algoAsym = algoAsym;
            }

            private AlgoAsymBuild(AlgoAsymEnum.AlgoAsym algoAsym, AlgoAsymEnum.AlgoAsymPadding padding, String iv) {
                super(algoAsym.getName(), padding.name(), algoAsym.getKeySize(), iv);
                this.algoAsym = algoAsym;
            }

            public AlgoAsymEnum.AlgoAsym getAlgoAsym() {
                return this.algoAsym;
            }
        }

        public static class AlgoSymBuild
        extends AlgoBuild {
            private AlgoSymEnum.AlgoSym algoSym;

            private AlgoSymBuild(AlgoSymEnum.AlgoSym algoSym) {
                super(algoSym.getName(), algoSym.getKeySize());
                this.algoSym = algoSym;
            }

            private AlgoSymBuild(AlgoSymEnum.AlgoSym algoSym, AlgoSymEnum.AlgoSymMode mode, AlgoSymEnum.AlgoSymPadding padding) {
                super(algoSym.getName(), mode.name(), padding.name(), algoSym.getKeySize());
                this.algoSym = algoSym;
            }

            private AlgoSymBuild(AlgoSymEnum.AlgoSym algoSym, AlgoSymEnum.AlgoSymPadding padding, String iv) {
                super(algoSym.getName(), padding.name(), algoSym.getKeySize(), iv);
                this.algoSym = algoSym;
            }

            public AlgoSymEnum.AlgoSym getAlgoSym() {
                return this.algoSym;
            }
        }

        protected static class AlgoBuild {
            private static final String ivSpec = "1234567890123456";
            private static final String ivDESede = "12345678";
            protected String keyAlgo;
            protected String cipherAlgo;
            protected int keySize;
            protected boolean isCBC;
            protected boolean isNoPadding;
            protected String iv;

            private AlgoBuild(String algo, String cipherAlgo, int keySize, boolean isCBC, boolean isNoPadding, String iv) {
                this.keyAlgo = algo;
                this.cipherAlgo = cipherAlgo;
                this.keySize = keySize;
                this.isCBC = isCBC;
                this.isNoPadding = isNoPadding;
                this.iv = iv;
            }

            private AlgoBuild(String algo, int keySize) {
                this(algo, algo, keySize, false, false, null);
            }

            private AlgoBuild(String algo, String mode, String padding, int keySize) {
                this(algo, algo + "/" + mode + "/" + padding, keySize, mode.equals(AlgoSymEnum.AlgoSymMode.CBC.name()), padding.equals(AlgoSymEnum.AlgoSymPadding.NoPadding.name()), algo.contains(AlgoSymEnum.AlgoSym.DES.name()) ? ivDESede : ivSpec);
            }

            private AlgoBuild(String algo, String padding, int keySize, String iv) {
                this(algo, algo + "/" + AlgoSymEnum.AlgoSymMode.CBC.name() + "/" + padding, keySize, true, padding.equals(AlgoSymEnum.AlgoSymPadding.NoPadding.name()), iv);
            }

            public int getKeySize() {
                return this.keySize;
            }

            public String getKeyAlgo() {
                return this.keyAlgo;
            }

            public String getCipherAlgo() {
                return this.cipherAlgo;
            }

            public boolean isCBC() {
                return this.isCBC;
            }

            public boolean isNoPadding() {
                return this.isNoPadding;
            }

            public String getIv() {
                return this.iv;
            }
        }

    }

    public static class Hashing {
        public static String md5Hex(String data) {
            return DigestUtils.md5Hex((String)data);
        }

        public static String sha256Hex(String data) {
            return DigestUtils.sha256Hex((String)data);
        }

        public static String sha384Hex(String data) {
            return DigestUtils.sha384Hex((String)data);
        }

        public static String sha512Hex(String data) {
            return DigestUtils.sha512Hex((String)data);
        }
    }

    public static class Decode {
        public static byte[] decodeBase64(byte[] data) {
            return Base64.decodeBase64((byte[])data);
        }

        public static String decodeBase64Str(byte[] data) {
            return CodecUtils.bytesToString(Base64.decodeBase64((byte[])data));
        }

        public static byte[] decodeHex(String data) {
            try {
                return Hex.decodeHex((char[])data.toCharArray());
            }
            catch (DecoderException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static byte[] decodeRadix(String data, int radix) {
            return new BigInteger(data, radix).toByteArray();
        }

        public static String decodeRadixStr(String data, int radix) {
            return CodecUtils.bytesToString(Decode.decodeRadix(data, radix));
        }

        public static byte[] decodeRadix(byte[] data, int radix) {
            return new BigInteger(new String(data), radix).toByteArray();
        }

        public static String decodeRadixStr(byte[] data, int radix) {
            return CodecUtils.bytesToString(Decode.decodeRadix(data, radix));
        }

        public static String decodeURL(String url) {
            String result = null;
            try {
                result = URLDecoder.decode(url, "unicode");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        public static String unescapeUnicode(String data) {
            return StringEscapeUtils.unescapeJava((String)data);
        }
    }

    public static class Encode {
        public static byte[] encodeBase64(byte[] data) {
            return Base64.encodeBase64((byte[])data);
        }

        public static String encodeBase64Str(byte[] data) {
            return Base64.encodeBase64String((byte[])data);
        }

        public static byte[] encodeBase64(byte[] data, boolean urlSafe) {
            return Base64.encodeBase64((byte[])data, (boolean)false, (boolean)urlSafe);
        }

        public static String encodeBase64Str(byte[] data, boolean urlSafe) {
            return CodecUtils.bytesToString(Encode.encodeBase64(data, urlSafe));
        }

        public static String encodeHex(byte[] data) {
            return Hex.encodeHexString((byte[])data);
        }

        public static byte[] encodeRadix(byte[] data, int radix) {
            return Encode.encodeRadixStr(data, radix).getBytes();
        }

        public static String encodeRadixStr(byte[] data, int radix) {
            return new BigInteger(1, data).toString(radix);
        }

        public static String encodeURL(String url) {
            String result = null;
            try {
                result = URLEncoder.encode(url, "unicode");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        public static String encodeUTF8(byte[] data) {
            String result = null;
            try {
                result = new String(data, Utils.Charsets.uft8);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        public static String escapeUnicode(String data) {
            return StringEscapeUtils.escapeJava((String)data);
        }
    }

    public static class KeyUtils {
        public static SecretKey genSecretKey(AlgoSymEnum.AlgoSym algoSym) {
            return KeyUtils.genSecretKey(algoSym, null);
        }

        public static SecretKey genSecretKey(AlgoSymEnum.AlgoSym algoSym, String seed) {
            KeyGenerator instance = null;
            try {
                instance = KeyGenerator.getInstance(algoSym.getName());
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (seed != null) {
                instance.init(algoSym.getKeySize(), new SecureRandom(seed.getBytes()));
            } else {
                instance.init(algoSym.getKeySize());
            }
            SecretKey deskey = instance.generateKey();
            return deskey;
        }

        public static SecretKey getSecretKey(AlgoSymEnum.AlgoSym algoSym, byte[] key) {
            SecretKeySpec secretKey = new SecretKeySpec(key, algoSym.getName());
            return secretKey;
        }

        public static void genKeyPair(AlgoAsymEnum.AlgoAsym algoAsym) throws Exception {
            try {
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algoAsym.getName());
                keyPairGen.initialize(algoAsym.getKeySize());
                KeyPair keyPair = keyPairGen.genKeyPair();
                PublicKey pubKey = keyPair.getPublic();
                PrivateKey priKey = keyPair.getPrivate();
                String pubName = algoAsym.name() + "_public_key.cer";
                String priName = algoAsym.name() + "_private_key.pfx";
                KeyUtils.writeKey(pubName, pubKey);
                KeyUtils.writeKey(priName, priKey);
                if (algoAsym.getName().contains("RSA")) {
                    RSAPublicKey rsaPubKey = (RSAPublicKey)pubKey;
                    RSAPrivateKey rsaPriKey = (RSAPrivateKey)priKey;
                    System.out.println(pubKey.toString());
                    System.out.println("exponent 16 -> " + rsaPubKey.getPublicExponent().toString(16));
                    System.out.println("byte -> " + rsaPubKey.getEncoded());
                    System.out.println("10 -> " + rsaPubKey.getModulus().toString());
                    System.out.println("16 -> " + rsaPubKey.getModulus().toString(16));
                    System.out.println("base64 -> " + Encode.encodeBase64Str(rsaPubKey.getEncoded()));
                    System.out.println("file -> " + System.getProperty("user.dir") + "\\" + pubName);
                    System.out.println();
                    System.out.println(rsaPriKey.getAlgorithm() + " private key ");
                    System.out.println("byte -> " + rsaPriKey.getEncoded());
                    System.out.println("10 -> " + rsaPriKey.getModulus().toString());
                    System.out.println("16 -> " + rsaPriKey.getModulus().toString(16));
                    System.out.println("base64 -> " + Encode.encodeBase64Str(priKey.getEncoded()));
                    System.out.println("file -> " + System.getProperty("user.dir") + "\\" + priName);
                } else if (algoAsym.getName().contains("DSA")) {
                    DSAPublicKey dsaPubKey = (DSAPublicKey)pubKey;
                    DSAPrivateKey dsaPriKey = (DSAPrivateKey)priKey;
                    System.out.println(dsaPubKey.toString());
                    System.out.println("byte -> " + dsaPubKey.getEncoded());
                    System.out.println("base64 -> " + Encode.encodeBase64Str(dsaPubKey.getEncoded()));
                    System.out.println("file -> " + System.getProperty("user.dir") + "\\" + pubName);
                    System.out.println();
                    System.out.println(dsaPriKey.getAlgorithm() + " private key ");
                    System.out.println("byte -> " + dsaPriKey.getEncoded());
                    System.out.println("base64 -> " + Encode.encodeBase64Str(dsaPriKey.getEncoded()));
                    System.out.println("file -> " + System.getProperty("user.dir") + "\\" + priName);
                }
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public static PublicKey getPublicKey(AlgoAsymEnum.AlgoAsym algoAsym, byte[] key) {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            PublicKey publicKey = null;
            try {
                publicKey = KeyUtils.getKeyFactory(algoAsym).generatePublic(keySpec);
            }
            catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return publicKey;
        }

        public static PrivateKey getPrivateKey(AlgoAsymEnum.AlgoAsym algoAsym, byte[] key) {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            PrivateKey privateKey = null;
            try {
                privateKey = KeyUtils.getKeyFactory(algoAsym).generatePrivate(keySpec);
            }
            catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return privateKey;
        }

        private static KeyFactory getKeyFactory(AlgoAsymEnum.AlgoAsym algoAsym) {
            KeyFactory keyFactory = null;
            try {
                keyFactory = KeyFactory.getInstance(algoAsym.getName());
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return keyFactory;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static <T> T readKey(Class<T> clz, InputStream is) {
            Object key = null;
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(is);
                key = ois.readObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    ois.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return (T)key;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static void writeKey(String path, Object object) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(path));
                oos.writeObject(object);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    oos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
