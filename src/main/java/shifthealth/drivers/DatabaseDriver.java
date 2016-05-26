package shifthealth.drivers;

import shifthealth.Global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Database Driver for MySQL or SQL Server.
 *
 *
 * Created by John Simoes on 2016-05-26.
 */
public class DatabaseDriver {

    public static boolean testDatabase() {

        Global.log.info("Testing database connection...");

        boolean result = false;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            Connection connection =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/tickit_staging?user=root&password=&useSSL=false&useTimezone=true&serverTimezone=UTC");

            Statement statement = connection.createStatement();

            if (statement.execute("SELECT company_name FROM accounts;")) {

                result = true;
            }

            result = true;
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }

        return result;
    }

// IF NOT EXISTS (select * from sys.tables t where t.name = 'nextgen_tickit_ref')
//  CREATE TABLE nextgen_tickit_ref(  
//   request_id UNIQUEIDENTIFIER, 
//   appt_id UNIQUEIDENTIFIER, 
//   status VARCHAR(30), create_timestamp DATETIME  );
}
