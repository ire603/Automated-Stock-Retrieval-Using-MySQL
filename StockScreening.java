package martin;

import java.math.BigDecimal;
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
                                "AND companydata.latestVolume BETWEEN 2700000 AND 3500000 "
//                                "AND companydata.avgTotalVolume BETWEEN 2500000 AND 3500000 "
                );
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {
                    System.out.printf("%n%d : Data : %s : Volume %f : ", i++,
                            resultSet1.getString(1), resultSet1.getBigDecimal(9));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void filterStocks1() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "SELECT * FROM symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = mysqlConnect.connect().prepareStatement(
                        "SELECT * FROM keystats WHERE symbols = ?" +
                                "AND keystats.month3ChangePercent BETWEEN 0.01 AND 1.00"
                );
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {

                    BigDecimal bigDecimal = resultSet1.getBigDecimal(8);
                    Double double1 = bigDecimal.doubleValue();
                    Double convertValue = (100 * double1);
                    PreparedStatement preparedStatement2 = mysqlConnect.connect().prepareStatement(
                            "SELECT * FROM companydata WHERE symbols = ?" +
                                    "AND companydata.latestVolume BETWEEN 0 AND 1"
                    );
                    preparedStatement2.setString(1, resultSet1.getString(1));
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    while (resultSet2.next()) {
                        System.out.printf("%n%d : Data %s : 3 Month Change Percent %f : Latest Volume %f : Latest Price %f", i++,
                                resultSet1.getString(1), convertValue, resultSet2.getBigDecimal(9), resultSet2.getBigDecimal(7));
                    }


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   public static void filterStocks2() {
        PreparedStatement preparedStatement;
        PreparedStatement preparedStatement1;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "SELECT * FROM symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                preparedStatement1 = mysqlConnect.connect().prepareStatement(
                        "SELECT * FROM companydata WHERE symbols = ?"
                );
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                while (resultSet1.next()) {
                    try {
                        BigDecimal week52High = resultSet1.getBigDecimal(13);
                        BigDecimal week52Low = resultSet1.getBigDecimal(14);
                        BigDecimal latestPrice = resultSet1.getBigDecimal(7);
                        Double latestPrice1 = latestPrice.doubleValue();
                        Double week52High1 = week52High.doubleValue();
                        Double week52Low1 = week52Low.doubleValue();
                        Double changeFrom52WeekHighAndLow = (((week52High1 - week52Low1)/(week52Low1))*100);
                        Double percentOff52WeekHigh  = (1 - (latestPrice1/week52High1));
                        Double percentOff52WeekLow  = ((latestPrice1/week52Low1) - 1);
                        BigDecimal volume = resultSet1.getBigDecimal(12);
                        Double latestVolume = volume.doubleValue();
                        if (changeFrom52WeekHighAndLow <= ####) {
                            if (latestPrice1 <= 25.00) {

                                    System.out.printf("%n %d : Symbol : %s : Price : %f : Average Volume : %f : Change From 52 Week High And Low : %f",
                                            i++, resultSet1.getString(1),latestPrice1, latestVolume, changeFrom52WeekHighAndLow);

            //                      System.out.printf("%n Data : %s : Change Percent From 52 Week High And Low : %f : Percent off -> 52 Week high : %f : Percent off -> 52 Week low : %f",
            //                                resultSet1.getString(1), changeFrom52WeekHighAndLow, percentOff52WeekHigh, percentOff52WeekLow);
//                                    System.out.printf("%n %d : Symbol : %s : Price : %f : Latest Volume : %f : Change From 52 Week High And Low : %f",
//                                            i++, resultSet1.getString(1),latestPrice1, resultSet1.getBigDecimal(9), changeFrom52WeekHighAndLow);


                            }
                            //                            findVolumeBasedOffSymbols(resultSet1.getString(1), changeFrom52WeekHighAndLow);
                        }


                    } catch (NullPointerException e) {
                        e.addSuppressed(new Throwable(e));
                    }
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }

    private static void findVolumeBasedOffSymbols(String string, Double changeFrom52WeekHighAndLow) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "SELECT * FROM companydata WHERE symbols = ?" +
                            "AND companydata.latestVolume BETWEEN 0 AND 1"
            );
            preparedStatement.setString(1, string);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.printf("%nSymbol : %s : Change From 52 Week High And Low : %f : And Volume : %f", string, changeFrom52WeekHighAndLow, resultSet.getBigDecimal(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
