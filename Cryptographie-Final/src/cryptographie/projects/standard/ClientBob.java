package cryptographie.projects.standard;

import cryptographie.Client;
import cryptographie.SocketWrapper;
import cryptographie.systems.CryptoSystemPaillier;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;

public class ClientBob extends Client<byte[]>
{
    public ClientBob(String ip, int port) throws UnknownHostException
    {
        super(ip, port);
    }
    
    public byte[] get(int index) throws IOException
    {
        if(index < 1)
            throw new IllegalArgumentException("The index must be >= 1.");
        
        return get(BigInteger.valueOf(index));
    }

    @Override
    protected byte[] receiveData(SocketWrapper sw, CryptoSystemPaillier csp, BigInteger input) throws IOException
    {
        byte[] data;
        int nb = 0;
        int index = input.intValue();
        
        while((data = sw.read()).length > 0)
        {
            if(++nb == index)
                return csp.decrypt(new BigInteger(data)).toByteArray();

            ack(sw);
        }

        throw new IndexOutOfBoundsException("Index " + index + " is out of possible answers.");
    }
}
