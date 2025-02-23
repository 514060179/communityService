package com.newland.property.utils.httpUtil;

import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class TLSAuthentication implements TlsAuthentication {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    protected TLSClient tlsClient;

    protected static KeyStore keyStore = null;

    public TLSAuthentication(TLSClient client) {
        super();
        tlsClient = client;
    }

    @Override
    public void notifyServerCertificate(Certificate serverCertificate) throws IOException {
        try {
            if (keyStore == null) {
                try {
                    keyStore = loadKeyStore();
                } catch (Exception e) {
                    throw new CertificateException("KeyStore loading failed.");
                }
            }
            KeyStore ks = keyStore;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List<java.security.cert.Certificate> certs = new LinkedList<java.security.cert.Certificate>();
            boolean trustedCertificate = false;
            for (org.bouncycastle.asn1.x509.Certificate c : serverCertificate.getCertificateList()) {
                java.security.cert.Certificate cert = cf.generateCertificate(new ByteArrayInputStream(c.getEncoded()));
                certs.add(cert);

                if (cert instanceof java.security.cert.X509Certificate) {
                    java.security.cert.X509Certificate x509cert = (java.security.cert.X509Certificate) cert;
                    String subjectDN = x509cert.getSubjectDN().getName();
                    String issuerDN = x509cert.getIssuerDN().getName();

                    try {
                        x509cert.checkValidity();
                    } catch (Exception e) {
                        throw e;
                    }

                    if (subjectDN.equals(issuerDN)) {

                        try {
                            x509cert.verify(x509cert.getPublicKey());
                        } catch (Exception e) {
                            logger.info("Self signed certificate verification failed.:" + x509cert.getSubjectDN());
                            throw e;
                        }

                        if (this.tlsClient.getTlsSocket().isSelfSignPass()) {
                            trustedCertificate = true;
                        } else {
                            logger.info("Self signed certificate cannot pass.:" + x509cert.getSubjectDN());
                        }
                    } else {

                        Enumeration en = ks.aliases();
                        String alias = "";
                        java.security.cert.X509Certificate signCert = null;

                        while (en.hasMoreElements()) {
                            java.security.cert.X509Certificate storeCert = null;
                            alias = (String) en.nextElement();

                            if (ks.isCertificateEntry(alias)) {
                                storeCert = (java.security.cert.X509Certificate) ks.getCertificate(alias);
                                if (storeCert.getIssuerDN().getName().equals(issuerDN)) {
                                    try {
                                        x509cert.verify(storeCert.getPublicKey());
                                        signCert = storeCert;
                                        break;
                                    } catch (Exception e) {
                                        logger.info("X509 keystore certificate verification failed.:" + storeCert.getSubjectDN());
                                    }

                                }
                            }
                        }
                        if (signCert != null) {
                            trustedCertificate = true;
                        }
                    }
                }

            }
            if (!trustedCertificate) {

                logger.info("Not trusted certificate detected.");
                throw new CertificateException("Not trusted certificate detected.:" + this.tlsClient.getHost());
            }

            TLSSession tlsSession = (TLSSession) this.tlsClient.getTlsSocket().getSession();
            tlsSession.setPeerCertArray(certs.toArray(new java.security.cert.Certificate[0]));

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException(ex);
        }
    }

    @Override
    public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
        return null;
    }

    private KeyStore loadKeyStore() throws Exception {
        FileInputStream trustStoreFis = null;
        try {

            String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
            trustStoreFis = new FileInputStream(filename);

            KeyStore localKeyStore = null;

            String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType") != null ? System.getProperty("javax.net.ssl.trustStoreType") : KeyStore.getDefaultType();
            String trustStoreProvider = System.getProperty("javax.net.ssl.trustStoreProvider") != null ? System.getProperty("javax.net.ssl.trustStoreProvider") : "";

            if (trustStoreType.length() != 0) {
                if (trustStoreProvider.length() == 0) {
                    localKeyStore = KeyStore.getInstance(trustStoreType);
                } else {
                    localKeyStore = KeyStore.getInstance(trustStoreType, trustStoreProvider);
                }

                char[] keyStorePass = null;
                String str5 = System.getProperty("javax.net.ssl.trustStorePassword") != null ? System.getProperty("javax.net.ssl.trustStorePassword") : "";
                if (str5.length() <= 0) {
                    str5 = "changeit";
                }
                if (str5.length() != 0) {
                    keyStorePass = str5.toCharArray();
                }

                localKeyStore.load(trustStoreFis, keyStorePass);

                if (keyStorePass != null) {
                    for (int i = 0; i < keyStorePass.length; i++) {
                        keyStorePass[i] = 0;
                    }
                }
            }
            return localKeyStore;
        } finally {
            if (trustStoreFis != null) {
                trustStoreFis.close();
            }
        }
    }

}

