package cryptographie;

public class Main
{
    public static void main(String[] args) throws Throwable
    {
        new Thread(new ServerAlice(1817, "Albator", "Bonobo", "Chocolat")).start();
        
        Thread.sleep(1000);
        
        System.out.println(new String(new ClientBob("127.0.0.1", 1817).get(1)));
        System.out.println(new String(new ClientBob("127.0.0.1", 1817).get(2)));
        System.out.println(new String(new ClientBob("127.0.0.1", 1817).get(3)));
    }
}
