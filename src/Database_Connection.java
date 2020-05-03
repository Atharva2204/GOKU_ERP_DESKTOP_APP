import javax.xml.crypto.Data;
import java.sql.Connection;

public class Database_Connection
{
    protected Connection con;

    private static final Database_Connection ourinstance = new Database_Connection();

    public static Database_Connection getInstance()
    {
        return ourinstance;
    }
    private Database_Connection()
    {

    }

    protected void  getConnection(Connection con)
    {
        this.con = con;
    }


}
