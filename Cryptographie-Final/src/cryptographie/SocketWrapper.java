package cryptographie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class SocketWrapper implements AutoCloseable
{
    public SocketWrapper(Socket socket, int timeout) throws IOException
    {
        this.os = socket.getOutputStream();
        this.is = socket.getInputStream();
        this.socket = socket;
        
        this.socket.setSoTimeout(timeout);
    }
    
    private final OutputStream os;
    private final InputStream is;
    private final Socket socket;
    
    public void write(byte[] data) throws IOException
    {
        os.write(data);
    }
    
    public byte[] read() throws IOException
    {
        return read(10);
    }
    private byte[] read(int nbTry) throws IOException
    {
        if(nbTry == 0)
            return new byte[0];
        
        byte[] data = new byte[4028];
        int nb = 0;
        int tnb;
        
        try
        {
            while((tnb = is.read(data, nb, data.length - nb)) > 0)
            {
                nb += tnb;
            }
        }
        catch(SocketTimeoutException ex)
        { }
        
        if(nb == 0)
            return read(nbTry - 1);
        
        return Arrays.copyOf(data, nb);
    }
    
    @Override
    public void close()
    {
        try
        {
            socket.close();
        }
        catch(IOException ex)
        { }
    }
}
