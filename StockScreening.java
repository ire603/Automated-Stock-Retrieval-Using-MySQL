package martin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockScreening {
    private static MysqlConnect mysqlConnect;

    public StockScreening(MysqlConnect mysqlConnect) {

        this.mysqlConnect = mysqlConnect;
    }
    public static void filterStocks() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "SELECT * FROM symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = mysqlConnect.connect().prepareStatement(
                        "SELECT * FROM companydata WHERE companydata.symbols = ? " +
                                "AND companydata.latestVolume BETWEEN 0 AND 1 " +
                                "AND companydata.avgTotalVolume BETWEEN 0 AND 1 "
                );
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {
                    System.out.printf("%n%d : Data : %s : Volume %f : Average Total Volume %f", i++,
                            resultSet1.getString(1), resultSet1.getBigDecimal(9),
                            resultSet1.getBigDecimal(12));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
