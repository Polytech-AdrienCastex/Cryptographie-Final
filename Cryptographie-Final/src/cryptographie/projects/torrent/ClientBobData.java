package cryptographie.projects.torrent;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientBobData extends SubClientBob<byte[]>
{
    public ClientBobData(String ip, int port) throws UnknownHostException
    {
        super(ip, port);
    }

    @Override
    protected byte[] convert(byte[] data)
    {
        return Arrays.copyOfRange(data, 1, data.length);
    }

    @Override
    protected boolean isValid(byte[] data)
    {
        return data[0] == 'D';
    }
}
