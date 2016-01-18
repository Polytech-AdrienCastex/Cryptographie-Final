package cryptographie;

import cryptographie.systems.CryptoSystemPaillier;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientBob
{
    public ClientBob(String ip, int port) throws UnknownHostException
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

    public byte[] get(int index)
    {
        if(index < 1)
            throw new IllegalArgumentException("index must be >= 1.");
        
        try(SocketWrapper sw = new SocketWrapper(new Socket(addr, port), 100))
        {
            CryptoSystemPaillier csp = new CryptoSystemPaillier(512);
            csp.generateKeys();
            
            sw.write(csp.getPublicKey().toByteArray());
            if(!new String(sw.read()).equals("Ack"))
                return null;
            
            sw.write(csp.encrypt(BigInteger.valueOf(index)).toByteArray());
            
            byte[] data;
            int nb = 0;
            while((data = sw.read()).length > 0)
            {
                if(++nb == index)
                    return csp.decrypt(new BigInteger(data)).toByteArray();
                
                sw.write("Ack".getBytes());
            }
            
            throw new IndexOutOfBoundsException("index " + index + " is out of possible answers.");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }
}
