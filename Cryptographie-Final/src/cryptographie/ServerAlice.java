package cryptographie;

import cryptographie.systems.CryptoSystemPaillier;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.Random;
import javax.net.ServerSocketFactory;

public class ServerAlice implements Runnable
{
    public ServerAlice(int port, String... values)
    {
        this.values = values;
        this.port = port;
    }
    
    private final int port;
    private final String[] values;
    
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
                                .multiply(CryptoSystemPaillier.encrypt(BigInteger.valueOf(-(i + 1)), key))
                                .modPow(new BigInteger(key.bitCount() - 1, new Random()), key.multiply(key))
                                .multiply(CryptoSystemPaillier.encrypt(new BigInteger(values[i].getBytes()), key));
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
