package cryptographie.projects.torrent;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    public static void main(String[] args) throws Throwable
    {
        BigInteger[] files = new BigInteger[]
        {
            BigInteger.valueOf(3),
            BigInteger.valueOf(10),
            BigInteger.valueOf(12),
            BigInteger.valueOf(45678992L)
        };
        
        // Full content of file :
        // [0] Bonjour les amis!
        // [1] Comment vont ces vacances?
        // [2] Chocolat
        // [3] J'aimerais manger du chocolat.
        
        Map<BigInteger, String> references = new HashMap<>();
        references.put(files[0], "127.0.0.1:1817;127.0.0.1:1819");
        references.put(files[1], "127.0.0.1:1819;127.0.0.1:1818;127.0.0.1:1817");
        references.put(files[2], "127.0.0.1:1819");
        references.put(files[3], "127.0.0.1:1817;127.0.0.1:1818;127.0.0.1:1819");
        new Thread(new ServerAlice(1816, references)).start(); // Referencer
        
        Map<BigInteger, String> storedOnServer1 = new HashMap<>();
        storedOnServer1.put(files[0], "Bonjour ");
        storedOnServer1.put(files[1], "vacances?");
        storedOnServer1.put(files[3], "J'aimera");
        new Thread(new ServerAlice(1817, storedOnServer1)).start();
        
        Map<BigInteger, String> storedOnServer2 = new HashMap<>();
        storedOnServer2.put(files[1], "vont ces ");
        storedOnServer2.put(files[3], "is manger");
        new Thread(new ServerAlice(1818, storedOnServer2)).start();
        
        Map<BigInteger, String> storedOnServer3 = new HashMap<>();
        storedOnServer3.put(files[0], "les amis!");
        storedOnServer3.put(files[1], "Comment ");
        storedOnServer3.put(files[2], "Chocolat!");
        storedOnServer3.put(files[3], " du chocolat.");
        new Thread(new ServerAlice(1819, storedOnServer3)).start();
        
        Thread.sleep(1000);
        
        test(files[0]);
        test(files[1]);
        test(files[2]);
        test(files[3]);
    }
    
    protected static void test(BigInteger id) throws Throwable
    {
        ClientBobReference scb = new ClientBobReference("127.0.0.1", 1816); // References
        
        String value = scb.get(id)
                .stream()
                .parallel()
                .map(pi ->
                {
                    try
                    {
                        return new ClientBobData(pi.ip, pi.port).get(id);
                    }
                    catch (Exception ex)
                    {
                        return null;
                    }
                })
                .map(String::new)
                .reduce("", String::concat);
        
        System.out.println(value);
    }
}
