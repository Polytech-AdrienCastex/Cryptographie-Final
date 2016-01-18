package cryptographie.systems;

import java.math.BigInteger;

public interface ICryptoSystem
{
    public void generateKeys();
    public BigInteger decrypt(BigInteger c);
    public BigInteger encrypt(BigInteger m);
}
