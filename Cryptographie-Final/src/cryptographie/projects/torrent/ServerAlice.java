package cryptographie.projects.torrent;

import cryptographie.Server;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ServerAlice extends Server
{
    public ServerAlice(int port, Map<BigInteger, String> values)
    {
        super(port, values.values()
                .stream()
                .map(v -> "D" + v)
                .map(String::getBytes)
                .map(BigInteger::new));
        
        this.indexToUID = new HashMap<>();
        
        int index = 0;
        for(BigInteger i : values.keySet())
            indexToUID.put(index++, i);
    }
    
    private final Map<Integer, BigInteger> indexToUID;
    
    @Override
    protected BigInteger getValue(int index)
    {
        return values[index];
    }
    @Override
    protected BigInteger getOffset(int index)
    {
        return indexToUID.get(index).negate();
    }
}
