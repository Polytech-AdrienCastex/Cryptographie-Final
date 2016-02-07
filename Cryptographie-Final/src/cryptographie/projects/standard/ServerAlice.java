package cryptographie.projects.standard;

import cryptographie.Server;
import java.math.BigInteger;
import java.util.stream.Stream;

public class ServerAlice extends Server
{
    public ServerAlice(int port, String... values)
    {
        super(port, Stream.of(values)
                .map(String::getBytes)
                .map(BigInteger::new));
    }
    
    @Override
    protected BigInteger getValue(int index)
    {
        return values[index];
    }
    @Override
    protected BigInteger getOffset(int index)
    {
        return BigInteger.valueOf(-(index + 1));
    }
}
