package cryptographie.projects.torrent;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class ClientBobReference extends SubClientBob<Collection<ClientBobReference.IpPort>>
{
    public ClientBobReference(String ip, int port) throws UnknownHostException
    {
        super(ip, port);
    }
    
    public static class IpPort
    {
        public IpPort(String ip, int port)
        {
            this.ip = ip;
            this.port = port;
        }
        
        public final String ip;
        public final int port;
    }

    @Override
    protected Collection<IpPort> convert(byte[] data)
    {
        LinkedList<IpPort> result = new LinkedList<>();
        String value = new String(Arrays.copyOfRange(data, 1, data.length));
        
        for(String ipPort : value.split(";"))
        {
            String[] splitted = ipPort.split(":");
            
            if(splitted.length == 2)
                result.add(new IpPort(splitted[0], Integer.parseInt(splitted[1])));
        }
        
        return result;
    }

    @Override
    protected boolean isValid(byte[] data)
    {
        String value = new String(data);
        
        return value.matches("D[0-9\\.:;]+");
    }
}
