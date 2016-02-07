package cryptographie.projects.torrent;

import cryptographie.Client;
import cryptographie.SocketWrapper;
import cryptographie.systems.CryptoSystemPaillier;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.Arrays;

public abstract class SubClientBob<T> extends Client<T>
{
    public SubClientBob(String ip, int port) throws UnknownHostException
    {
        super(ip, port);
    }
    
    protected abstract T convert(byte[] data);
    protected abstract boolean isValid(byte[] data);

    @Override
    protected T receiveData(SocketWrapper sw, CryptoSystemPaillier csp, BigInteger input) throws IOException
    {
        byte[] data;
        
        while((data = sw.read()).length > 0)
        {
            byte[] value = csp.decrypt(new BigInteger(data)).toByteArray();
            
            if(isValid(value))
                return convert(value);

            ack(sw);
        }

        return null;
    }
}
