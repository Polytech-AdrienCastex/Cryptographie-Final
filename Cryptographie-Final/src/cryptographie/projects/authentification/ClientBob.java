package cryptographie.projects.authentification;

import cryptographie.Client;
import cryptographie.SocketWrapper;
import cryptographie.systems.CryptoSystemPaillier;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;

public class ClientBob extends Client<Boolean>
{
    public ClientBob(String ip, int port) throws UnknownHostException
    {
        super(ip, port);
    }
    
    public Boolean get(String login, String password) throws IOException
    {
        return get(new BigInteger((login.trim().toLowerCase() + "@" + password).getBytes()));
    }

    @Override
    protected Boolean receiveData(SocketWrapper sw, CryptoSystemPaillier csp, BigInteger input) throws IOException
    {
        byte[] data;
        
        while((data = sw.read()).length > 0)
        {
            if(csp.decrypt(new BigInteger(data)).compareTo(input) == 0)
                return true;

            ack(sw);
        }

        return false;
    }
}
