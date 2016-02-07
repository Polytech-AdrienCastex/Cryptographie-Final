package cryptographie.projects.standard;

public class Main
{
    public static void main(String[] args) throws Throwable
    {
        new Thread(new ServerAlice(1817, "Albator", "Bonobo", "Chocolat")).start();
        
        Thread.sleep(1000);
        
        test(1);
        test(2);
        test(3);
        test(4);
    }
    
    protected static void test(int index) throws Throwable
    {
        System.out.println(new String(new ClientBob("127.0.0.1", 1817).get(index)));
    }
}
