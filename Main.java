package martin;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.refdata.ExchangeSymbol;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.refdata.SymbolsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final IEXTradingClient iexTradingClient = IEXTradingClient.create();
        final List<ExchangeSymbol> exchangeSymbols = iexTradingClient.executeRequest(
                new SymbolsRequestBuilder().build());

        MysqlConnect mysqlConnect = new MysqlConnect();
        mysqlConnect.connect();
        System.out.printf("%n%s",exchangeSymbols.get(0));
//        for (int i = 0; i < 8710; i++) {
//
//            String symbols = exchangeSymbols.get(i).getSymbol();
//            mysqlConnect.connect().setAutoCommit(false);
//            PreparedStatement preparedStatement = mysqlConnect.connect().prepareStatement(
//                    "insert into symbols(symbol) values (?)");
//            preparedStatement.setString(1, symbols);
////            preparedStatement.execute();
//        }
//        mysqlConnect.connect().commit();
//        getResultSet(mysqlConnect, iexTradingClient);
//        updateDataInCompanyData(mysqlConnect, iexTradingClient);
//        updateDataInCompanyDataSingleStock(mysqlConnect, iexTradingClient);
        insertingDataThatsNotThere(mysqlConnect, iexTradingClient);
    }

    private static void getResultSet(MysqlConnect mysqlConnect, IEXTradingClient iexTradingClient) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "select * from symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Thread.sleep(50);
                System.out.printf("%n%s: ", resultSet.getString(1));
                Quote quotes = iexTradingClient.executeRequest(
                        new QuoteRequestBuilder().withSymbol(resultSet.getString(1)).build());
//                System.out.printf("%n%f", quotes.getAvgTotalVolume());
                enterIntoDB(mysqlConnect, quotes);

            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void enterIntoDB(MysqlConnect mysqlConnect, Quote quotes) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "INSERT INTO companyData(symbols,companyName, primaryExchange, sector, " +
                            "open, close, latestPrice, latestTime, latestVolume, " +
                            "changeInPrice, changePercent, avgTotalVolume, week52High, week52Low) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            enterValuesIntoPreparedStatement(preparedStatement, quotes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void enterValuesIntoPreparedStatement(PreparedStatement preparedStatement, Quote quotes) {
        try {
            preparedStatement.setString(    1, quotes.getSymbol());
            preparedStatement.setString(    2, quotes.getCompanyName());
            preparedStatement.setString(    3, quotes.getPrimaryExchange());
            preparedStatement.setString(    4, quotes.getSector());
            preparedStatement.setBigDecimal(5, quotes.getOpen());
            preparedStatement.setBigDecimal(6, quotes.getClose());
            preparedStatement.setBigDecimal(7, quotes.getLatestPrice());
            preparedStatement.setString(    8, quotes.getLatestTime());
            preparedStatement.setBigDecimal(9, quotes.getLatestVolume());
            preparedStatement.setBigDecimal(10, quotes.getChange());
            preparedStatement.setBigDecimal(11, quotes.getChangePercent());
            preparedStatement.setBigDecimal(12, quotes.getAvgTotalVolume());
            preparedStatement.setBigDecimal(13, quotes.getWeek52High());
            preparedStatement.setBigDecimal(14, quotes.getWeek52Low());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateDataInCompanyData(MysqlConnect mysqlConnect, IEXTradingClient iexTradingClient) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "select * from symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Thread.sleep(0);
                System.out.printf("%n%s: ", resultSet.getString(1));
                Quote quotes = iexTradingClient.executeRequest(
                        new QuoteRequestBuilder().withSymbol(resultSet.getString(1)).build());
//            Quote quotes = iexTradingClient.executeRequest(new QuoteRequestBuilder().withSymbol("AEE").build());

//                System.out.printf("%n%f", quotes.getAvgTotalVolume());
//                enterIntoDB(mysqlConnect, quotes);
            updateIntoDB(mysqlConnect, quotes);

            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void updateDataInCompanyDataSingleStock(MysqlConnect mysqlConnect, IEXTradingClient iexTradingClient)
    {
        Quote quotes = iexTradingClient.executeRequest(new QuoteRequestBuilder().withSymbol("A").build());
        updateIntoDB(mysqlConnect, quotes);
    }

    private static void updateIntoDB(MysqlConnect mysqlConnect, Quote quotes) {
        PreparedStatement preparedStatement1;
        try {
            preparedStatement1 = mysqlConnect.connect().prepareStatement(
                    "UPDATE companydata SET companyName = ?, primaryExchange = ?, " +
                            "sector = ?, open = ?, close = ?," +
                            "latestPrice = ?, latestTime = ?, latestVolume = ?, " +
                            "changeInPrice = ?, changePercent = ?, avgTotalVolume = ?," +
                            "week52High = ?, week52Low = ? WHERE symbols = ? "
            );
            enterUpdatedValuesIntoPreparedStatement(preparedStatement1, quotes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void enterUpdatedValuesIntoPreparedStatement(PreparedStatement preparedStatement, Quote quotes) {
        try {
            preparedStatement.setString(    1, quotes.getCompanyName());
            preparedStatement.setString(    2, quotes.getPrimaryExchange());
            preparedStatement.setString(    3, quotes.getSector());
            preparedStatement.setBigDecimal(4, quotes.getOpen());
            preparedStatement.setBigDecimal(5, quotes.getClose());
            preparedStatement.setBigDecimal(6, quotes.getLatestPrice());
            preparedStatement.setString(    7 , quotes.getLatestTime());
            preparedStatement.setBigDecimal(8, quotes.getLatestVolume());
            preparedStatement.setBigDecimal(9, quotes.getChange());
            preparedStatement.setBigDecimal(10, quotes.getChangePercent());
            preparedStatement.setBigDecimal(11, quotes.getAvgTotalVolume());
            preparedStatement.setBigDecimal(12, quotes.getWeek52High());
            preparedStatement.setBigDecimal(13, quotes.getWeek52Low());
            preparedStatement.setString(14, quotes.getSymbol());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void insertingDataThatsNotThere(MysqlConnect mysqlConnect, IEXTradingClient client) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = mysqlConnect.connect().prepareStatement(
                    "select * from symbols"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Thread.sleep(0);

                System.out.printf("%n%s: ", resultSet.getString(1));
                PreparedStatement ps = mysqlConnect.connect().prepareStatement(
                        "SELECT * FROM companydata WHERE companydata.symbols = ?"
                );
                ps.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = ps.executeQuery();
                if (!resultSet1.next()) {
                    try {
                        Quote quote = client.executeRequest(
                                new QuoteRequestBuilder().withSymbol(resultSet.getString(1)).build());
                        PreparedStatement ps1 = mysqlConnect.connect().prepareStatement(
                                "INSERT INTO companydata(symbols, companyName, primaryExchange, " +
                                        "sector, open, close, latestPrice, " +
                                        "latestTime, latestVolume, changeInPrice, " +
                                        "changePercent, avgTotalVolume, week52High, week52Low) " +
                                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
                        );
                        ps1.setString(    1, quote.getSymbol());
                        ps1.setString(    2, quote.getCompanyName());
                        ps1.setString(    3, quote.getPrimaryExchange());
                        ps1.setString(    4, quote.getSector());
                        ps1.setBigDecimal(5, quote.getOpen());
                        ps1.setBigDecimal(6, quote.getClose());
                        ps1.setBigDecimal(7, quote.getLatestPrice());
                        ps1.setString(    8, quote.getLatestTime());
                        ps1.setBigDecimal(9, quote.getLatestVolume());
                        ps1.setBigDecimal(10, quote.getChange());
                        ps1.setBigDecimal(11, quote.getChangePercent());
                        ps1.setBigDecimal(12, quote.getAvgTotalVolume());
                        ps1.setBigDecimal(13, quote.getWeek52High());
                        ps1.setBigDecimal(14, quote.getWeek52Low());
                        ps1.execute();
                        System.out.printf("%n New Symbol and Data Entered %s: ", quote.getSymbol());
                    } catch (IEXTradingException e) {
                        e.addSuppressed(new Throwable(e));
                    }
                } else {
                    System.out.printf("%n No Symbol and Data Entered");
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
