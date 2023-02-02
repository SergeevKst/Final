package utilsTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.ConnectionManagerUtil;
import java.sql.SQLException;

public class ConnectionManagerUtilTest {

    @Test
    void openConnectionWithDBTest(){

        try {
            Assertions.assertFalse(ConnectionManagerUtil.openConnectionWithDB().isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
