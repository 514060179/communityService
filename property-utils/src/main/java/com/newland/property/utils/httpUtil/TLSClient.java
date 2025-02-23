package com.newland.property.utils.httpUtil;

import org.bouncycastle.crypto.tls.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class TLSClient extends DefaultTlsClient {

    protected String host = "";

    protected int port;

    protected TLSSocket tlsSocket;

    protected TLSAuthentication tlsAuthentication;

    public TLSClient(String host, int port, TLSSocket sock) {
        super();
        this.host = host;
        this.port = port;
        this.tlsSocket = sock;
        this.tlsAuthentication = new TLSAuthentication(this);
    }

    public TLSSocket getTlsSocket() {
        return tlsSocket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TlsSession getSession() {
        return context.getResumableSession();
    }

    public boolean isTLSv12() {
        return TlsUtils.isTLSv12(context);
    }

    public int getSelectedCipherSuite() {
        return selectedCipherSuite;
    }

    public ProtocolVersion getProtocol() {
        return context.getServerVersion();
    }

    @Override
    public Hashtable<Integer, byte[]> getClientExtensions() throws IOException {

        Hashtable<Integer, byte[]> clientExtensions = super.getClientExtensions();
        if (clientExtensions == null) {
            clientExtensions = new Hashtable<Integer, byte[]>();
        }

        byte[] hostname = host.getBytes("UTF-8");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(hostname.length + 3);
        dos.writeByte(0);
        dos.writeShort(hostname.length);
        dos.write(hostname);
        dos.close();

        clientExtensions.put(ExtensionType.server_name, baos.toByteArray());
        return clientExtensions;
    }

    @Override
    protected boolean allowUnexpectedServerExtension(Integer extensionType, byte[] extensionData) throws IOException {

        switch (extensionType.intValue()) {
            case ExtensionType.ec_point_formats:

                TlsECCUtils.readSupportedPointFormatsExtension(extensionData);
                return true;
            default:
                return super.allowUnexpectedServerExtension(extensionType, extensionData);
        }
    }

    @Override
    public TlsAuthentication getAuthentication() throws IOException {

        return this.tlsAuthentication;
    }

}
