package cryptographie;

import cryptographie.systems.CryptoSystemPaillier;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Client<T>
{
    public Client(String ip, int port) throws UnknownHostException
    {
        byte[] ipb = new byte[4];
        String[] ips = ip.trim().split("\\.");
        for(int i = 0; i < ips.length; ++i)
            ipb[i] = (byte)Integer.parseInt(ips[i]);
        this.addr = Inet4Address.getByAddress(ipb);
        
        this.port = port;
    }
    
    private final InetAddress addr;
    private final int port;
    
    protected abstract T receiveData(SocketWrapper sw, CryptoSystemPaillier csp, BigInteger input) throws IOException;
    
    protected void ack(SocketWrapper sw) throws IOException
    {
        sw.write("Ack".getBytes());
    }

    public T get(BigInteger input) throws IOException
    {
        try(SocketWrapper sw = new SocketWrapper(new Socket(addr, port), 100))
        {
            CryptoSystemPaillier csp = new CryptoSystemPaillier(512);
            csp.generateKeys();
            
            sw.write(csp.getPublicKey().toByteArray());
            if(!new String(sw.read()).equals("Ack"))
                return null;
            
            sw.write(csp.encrypt(input).toByteArray());
            
            return receiveData(sw, csp, input);
        }
    }
}
