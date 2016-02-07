package cryptographie.projects.authentification;

public class Main
{
    public static void main(String[] args) throws Throwable
    {
        new Thread(new ServerAlice(1817, "albator@123", "bonobo@Chocolat", "chocolat@PoloMarco")).start();
        
        Thread.sleep(1000);
        
        test("albator", "123"); // Valid test
        test("albator", "321"); // Wrong test
        test("BONOBO", "Chocolat"); // Login case test
        test("chocolat", "poloMarco"); // Pasword case test
    }
    
    protected static void test(String login, String password) throws Throwable
    {
        System.out.println(login + "@" + password + " : " + new ClientBob("127.0.0.1", 1817).get(login, password));
    }
}
