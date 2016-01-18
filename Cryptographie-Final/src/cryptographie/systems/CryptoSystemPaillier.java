package cryptographie.systems;

import java.math.BigInteger;
import java.util.Random;

public class CryptoSystemPaillier implements ICryptoSystem
{
    public CryptoSystemPaillier(int nbBitGen)
    {
        this.nbBitGen = nbBitGen;
    }
    
    private final int nbBitGen;
    private BigInteger pk;
    private BigInteger sk;
    public BigInteger n;
    private BigInteger n2;
    private BigInteger phin;
    
    @Override
    public void generateKeys()
    {
        BigInteger p = BigInteger.probablePrime(nbBitGen, new Random());
        BigInteger q = BigInteger.probablePrime(nbBitGen, new Random());
        n = p.multiply(q);
        n2 = n.multiply(n);
        phin = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        pk = n;
        sk = phin;
    }
    
    public BigInteger getPublicKey()
    {
        return pk;
    }
    public BigInteger getPrivateKey()
    {
        return sk;
    }
    
    public static BigInteger encrypt(BigInteger msg, BigInteger n)
    {
        BigInteger n2 = n.multiply(n);
        return n.add(BigInteger.ONE)
                .modPow(msg, n2)
                .multiply(getRandom(n).modPow(n, n2))
                .mod(n2);
    }
    
    public static BigInteger getRandom(BigInteger n)
    {
        Random rnd = new Random();
        BigInteger r;
        do
        {
            r = new BigInteger(n.bitLength(), rnd);
        } while(r.compareTo(BigInteger.ZERO) == -1 || r.compareTo(n) == 1);
        
        return r;
    }
    public BigInteger getRandom()
    {
        Random rnd = new Random();
        BigInteger r;
        do
        {
            r = new BigInteger(n.bitLength(), rnd);
        } while(r.compareTo(BigInteger.ZERO) == -1 || r.compareTo(n) == 1);
        
        return r;
    }
    
    @Override
    public BigInteger encrypt(BigInteger msg)
    {
        return n.add(BigInteger.ONE)
                .modPow(msg, n2)
                .multiply(getRandom().modPow(n, n2))
                .mod(n2);
    }
    
    @Override
    public BigInteger decrypt(BigInteger c)
    {
        return c.modPow(phin, n2)
                .subtract(BigInteger.ONE)
                .multiply(phin.modInverse(n))
                .divide(n)
                .mod(n);
    }
}
