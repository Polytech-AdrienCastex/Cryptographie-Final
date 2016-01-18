package cryptographie.systems;

import java.math.BigInteger;
import java.util.Random;

public class CryptoSystemRSA implements ICryptoSystem
{
    public CryptoSystemRSA(int nbBitPKey, int nbBitGen)
    {
        this.nbBitPKey = nbBitPKey;
        this.nbBitGen = nbBitGen;
    }
    
    private final int nbBitPKey;
    private final int nbBitGen;
    private BigInteger pk;
    private BigInteger sk;
    private BigInteger n;
    
    protected static boolean arePrimal(BigInteger e, BigInteger y)
    {
        if(e.compareTo(y) == -1)
            return arePrimal(y, e);
        
        BigInteger bn = y;
        BigInteger bnmo = bn.subtract(BigInteger.ONE);
        
        return y.equals(e)
                || e
                        .modPow(bnmo, bn)
                        .equals(BigInteger.ONE);
    }
    protected static BigInteger generatePrimal(int nbBits, BigInteger p)
    {
        Random rnd = new Random();
        BigInteger e;
        do
        {
            e = BigInteger.probablePrime(nbBits, rnd);
        } while(!arePrimal(e, p));
        return e;
    }
    @Override
    public void generateKeys()
    {
        BigInteger p = BigInteger.probablePrime(nbBitGen, new Random());
        BigInteger q = BigInteger.probablePrime(nbBitGen, new Random());
        n = p.multiply(q);
        BigInteger phin = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        pk = generatePrimal(nbBitPKey, phin);
        sk = pk.modInverse(phin);
    }
    
    public BigInteger getPublicKey()
    {
        return new BigInteger(n.toString() + pk.toString());
    }
    public BigInteger getPrivateKey()
    {
        return sk;
    }
    
    
    public static boolean isMessageValid(BigInteger msg, BigInteger n)
    {
        return msg.signum() == 1 && msg.compareTo(n) == -1;
    }
    public boolean isMessageValid(BigInteger msg)
    {
        return isMessageValid(msg, n);
    }
    
    public static BigInteger encrypt(BigInteger msg, BigInteger pk, BigInteger n)
    {
        return msg.modPow(pk, n);
    }
    public BigInteger encrypt(BigInteger msg)
    {
        return encrypt(msg, pk, n);
    }
    public static BigInteger encrypt(boolean value, BigInteger pk, BigInteger n)
    {
        Random rnd = new Random();
        BigInteger x;
        do
        {
            x = new BigInteger(n.bitLength(), rnd);

            if(x.testBit(0) != value)
                x = x.add(BigInteger.ONE);
        } while(!isMessageValid(x, n));
        
        return encrypt(x, pk, n);
    }
    public BigInteger encrypt(boolean value)
    {
        return encrypt(value, pk, n);
    }
    
    public static BigInteger decrypt(BigInteger msg, BigInteger sk, BigInteger n)
    {
        return msg.modPow(sk, n);
    }
    public BigInteger decrypt(BigInteger msg)
    {
        return decrypt(msg, sk, n);
    }
    public boolean decryptBool(BigInteger msg)
    {
        return decrypt(msg).testBit(0);
    }
}
