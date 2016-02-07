package cryptographie;

import cryptographie.systems.CryptoSystemPaillier;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.Random;
import java.util.stream.Stream;
import javax.net.ServerSocketFactory;

public abstract class Server implements Runnable
{
    public Server(int port, BigInteger... values)
    {
        this.values = values;
        this.port = port;
    }
    public Server(int port, Stream<BigInteger> values)
    {
        this(port, values.toArray(BigInteger[]::new));
    }
    
    private final int port;
    
    protected final BigInteger[] values;
    
    protected abstract BigInteger getValue(int index);
    protected abstract BigInteger getOffset(int index);
    
    @Override
    public void run()
    {
        try
        {
            ServerSocket ss = ServerSocketFactory.getDefault()
                    .createServerSocket(port);
            
            while(true)
            {
                try(SocketWrapper sw = new SocketWrapper(ss.accept(), 100))
                {
                    BigInteger key = new BigInteger(sw.read());
                    sw.write("Ack".getBytes());
                    
                    BigInteger I = new BigInteger(sw.read());
                    
                    BigInteger[] biValues = new BigInteger[values.length];
                    for(int i = 0; i < biValues.length; i++)
                    {
                        biValues[i] = I
                                .multiply(CryptoSystemPaillier.encrypt(getOffset(i), key))
                                .modPow(new BigInteger(key.bitCount() - 1, new Random()), key.multiply(key))
                                .multiply(CryptoSystemPaillier.encrypt(getValue(i), key));
                    }
                    
                    for(BigInteger bi : biValues)
                    {
                        sw.write(bi.toByteArray());
                        sw.read();
                    }
                }
                catch(Exception ex)
                { }
            }
        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }
    }
}
