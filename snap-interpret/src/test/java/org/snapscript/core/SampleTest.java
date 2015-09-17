package org.snapscript.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;

public class SampleTest extends TestCase {

   public void testSample() {
      System.out.println(generateTransactions("VOD,Vodafone,10|GOOG,Google,15|MSFT,Microsoft,12:VOD,Vodafone,16,2|GOOG,Google,10,15|MSFT,Microsoft,25,6"));
   }

   private static enum Side {
      BUY, SELL
   }

   public static class Transaction implements Comparable<Transaction> {

      private final String ticker;
      private final Side side;
      private final double count;

      public Transaction(String ticker, Side side, double count) {
         this.ticker = ticker;
         this.side = side;
         this.count = count;
      }

      public int compareTo(Transaction holding) {
         return ticker.compareTo(holding.ticker);
      }

      public String getTicker() {
         return ticker;
      }

      public Side getSide() {
         return side;
      }

      public double getCount() {
         return count;
      }

   }

   public static class BenchmarkHolding implements Comparable<BenchmarkHolding> {

      private final String ticker;
      private final String name;
      private final int count;
      private final double price;
      private final double holdingValue;
      private final double benchmarkValue;

      public BenchmarkHolding(String ticker, String name, int count, double price, double holdingValue, double benchmarkValue) {
         this.ticker = ticker;
         this.count = count;
         this.price = price;
         this.holdingValue = holdingValue;
         this.benchmarkValue = benchmarkValue;
         this.name = name;
      }

      public int compareTo(BenchmarkHolding holding) {
         return ticker.compareTo(holding.ticker);
      }

      public String getTicker() {
         return ticker;
      }

      public String getName() {
         return name;
      }

      public int getCount() {
         return count;
      }

      public double getPrice() {
         return price;
      }

      public double getHoldingValue() {
         return holdingValue;
      }
      
      public double getBenchmarkValue(){
         return benchmarkValue;
      }

   }

   public static class Portfolio {

      private final Map<String, Benchmark> benchmarks;
      private final Map<String, Holding> holdings;

      public Portfolio() {
         this.holdings = new TreeMap<String, Holding>();
         this.benchmarks = new TreeMap<String, Benchmark>();
      }

      public Set<String> getTickers() {
         return holdings.keySet();
      }

      public Benchmark getBenchmark(String ticker) {
         return benchmarks.get(ticker);
      }

      public void addBenchmark(Benchmark benchmark) {
         String ticker = benchmark.getTicker();
         benchmarks.put(ticker, benchmark);
      }

      public Holding getHolding(String ticker) {
         return holdings.get(ticker);
      }

      public void addHolding(Holding holding) {
         String ticker = holding.getTicker();
         holdings.put(ticker, holding);
      }
   }

   public static class Benchmark implements Comparable<Benchmark> {

      private final String ticker;
      private final String name;
      private final int count;
      private final double price;

      public Benchmark(String ticker, String name, int count, double price) {
         this.ticker = ticker;
         this.name = name;
         this.count = count;
         this.price = price;
      }

      public int compareTo(Benchmark holding) {
         return ticker.compareTo(holding.ticker);
      }

      public String getTicker() {
         return ticker;
      }

      public String getName() {
         return name;
      }

      public int getCount() {
         return count;
      }

      public double getPrice() {
         return price;
      }
   }

   public static class Holding implements Comparable<Holding> {

      private final String ticker;
      private final String name;
      private final int count;

      public Holding(String ticker, String name, int count) {
         this.ticker = ticker;
         this.name = name;
         this.count = count;
      }

      public int compareTo(Holding holding) {
         return ticker.compareTo(holding.ticker);
      }

      public String getTicker() {
         return ticker;
      }

      public String getName() {
         return name;
      }

      public int getCount() {
         return count;
      }
   }

   private static class BenchmarkHoldingBuilder {

      public static Set<BenchmarkHolding> create(Portfolio portfolio) {
         Set<BenchmarkHolding> transactions = new TreeSet<BenchmarkHolding>();
         Set<String> tickers = portfolio.getTickers();

         for (String ticker : tickers) {
            Holding holding = portfolio.getHolding(ticker);
            Benchmark benchmark = portfolio.getBenchmark(ticker);

            if (benchmark != null) {
               BenchmarkHolding transaction = calculate(holding, benchmark);

               if (transaction != null) {
                  transactions.add(transaction);
               }
            }
         }
         return transactions;

      }

      public static BenchmarkHolding calculate(Holding holding, Benchmark benchmark) {
         String ticker = holding.getTicker();
         String name = holding.getName();
         int holdingCount = holding.getCount();
         int benchmarkCount = benchmark.getCount();
         double price = benchmark.getPrice();
         double holdingValue = price * holdingCount;
         double benchmarkValue = price * benchmarkCount;

         return new BenchmarkHolding(ticker, name, holdingCount, price, holdingValue, benchmarkValue);
      }
   }

   public static class PortfolioParser {

      public static Portfolio parsePortfolio(String input) {
         Portfolio portfolio = new Portfolio();
         HoldingParser holdingParser = new HoldingParser();
         BenchmarkParser benchmarkParser = new BenchmarkParser();

         if (input != null && !input.isEmpty()) {
            String[] parts = input.split(":");
            Set<Holding> holdings = holdingParser.parseEntries(parts[0]);
            Set<Benchmark> benchmarks = benchmarkParser.parseEntries(parts[1]);

            for (Holding holding : holdings) {
               portfolio.addHolding(holding);
            }
            for (Benchmark benchmark : benchmarks) {
               portfolio.addBenchmark(benchmark);
            }
         }
         return portfolio;
      }
   }

   private static abstract class EntryParser<T extends Comparable> {

      public Set<T> parseEntries(String holdings) {
         Set<T> values = new TreeSet<T>();

         if (holdings != null && !holdings.isEmpty()) {
            String[] parts = holdings.split("\\|");

            for (String part : parts) {
               T holding = parseEntry(part);
               values.add(holding);
            }
         }
         return values;
      }

      public abstract T parseEntry(String value);
   }

   public static class BenchmarkParser extends EntryParser<Benchmark> {

      @Override
      public Benchmark parseEntry(String entry) {
         String[] parts = entry.split(",");

         if (parts.length != 4) {
            throw new IllegalArgumentException("Value '" + entry + "' is not a valid benchmark");
         }
         return new Benchmark(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
      }
   }

   public static class HoldingParser extends EntryParser<Holding> {

      @Override
      public Holding parseEntry(String entry) {
         String[] parts = entry.split(",");

         if (parts.length != 3) {
            throw new IllegalArgumentException("Value '" + entry + "' is not a valid holding");
         }
         return new Holding(parts[0], parts[1], Integer.parseInt(parts[2]));
      }
   }
   
   private static class BenchmarkTransactionCalculator {

      public static Set<Transaction> calculate(Portfolio portfolio) {
         Set<Transaction> transactions = new TreeSet<Transaction>();
         Set<BenchmarkHolding> holdings = BenchmarkHoldingBuilder.create(portfolio);

         if (!holdings.isEmpty()) {
            double benchmarkNetAssetValue = 0;
            double netAssetValue = 0;
            int totalQuantity = 0;

            for (BenchmarkHolding holding : holdings) {
               netAssetValue += holding.getHoldingValue();
               benchmarkNetAssetValue += holding.getBenchmarkValue();
               totalQuantity += holding.getCount();
            }
            for (BenchmarkHolding holding : holdings) {
               String ticker = holding.getTicker();
               Benchmark benchmark = portfolio.getBenchmark(ticker);
               Transaction transaction = calculate(holding, benchmark, netAssetValue, benchmarkNetAssetValue, totalQuantity);
               
               if(transaction != null) {
                  transactions.add(transaction);
               }
            }
         }
         return transactions;
      }

      public static Transaction calculate(BenchmarkHolding holding, Benchmark benchmark, double netAssetValue, double benchmarkNetAssetValue, int totalQuantity) {
         String ticker = holding.getTicker();
         int actual = holding.getCount();
         int require = benchmark.getCount();
         double holdingValue = holding.getHoldingValue();
         double benchmarkValue = holding.getBenchmarkValue();
         double percentageNetAssetValue = (holdingValue / netAssetValue) * 100.0;
         double percentageBenchmarkNetAssetValue = (benchmarkValue / netAssetValue) * 100.0;
         
         if (actual < require) {
            return new Transaction(ticker, Side.BUY, actual * ((percentageBenchmarkNetAssetValue - percentageNetAssetValue) / 100.0));
         }
         if (actual > require) {
            return new Transaction(ticker, Side.SELL, actual * ((percentageNetAssetValue - percentageBenchmarkNetAssetValue) / 100.0));
         }
         return null;
      }
   }

   static String generateTransactions(String input) {
      Portfolio portfolio = PortfolioParser.parsePortfolio(input);
      Set<Transaction> transactions = BenchmarkTransactionCalculator.calculate(portfolio);

      if (!transactions.isEmpty()) {
         DecimalFormat format = new DecimalFormat("0.00");
         StringBuilder builder = new StringBuilder();
         int count = 0;

         for (Transaction transaction : transactions) {
            if (count++ > 0) {
               builder.append(", ");
            }
            builder.append("[");
            builder.append(transaction.getSide());
            builder.append(", ");
            builder.append(transaction.getTicker());
            builder.append(", ");
            builder.append(format.format(transaction.getCount()));
            builder.append("]");
         }

         return builder.toString();
      }
      return null;

   }

   void readFile() throws Exception {
      InputStreamReader decoder = new InputStreamReader(System.in);
      BufferedReader reader = new BufferedReader(decoder);
   }

   void diff(String list) {
      List<Integer> values = new ArrayList<Integer>();
      String[] numbers = list.split("\\s+");

      for (String number : numbers) {
         values.add(Integer.parseInt(number));
      }
      int count = values.size();
      int diff = 0;
      int prev = 0;

      for (int i = 0; i < count; i++) {
         int next = values.get(i);

         if (i > 1 && next - prev != diff) {
            System.out.println(prev + diff);
         }
         if (i == 1) {
            diff = next - prev;
         }
         prev = next;
      }

   }

   static void findingDigits(int[] foo) {
      for (int i : foo) {
         String s = String.valueOf(i);
         int l = s.length();
         int v = 0;
         for (int j = 0; j < l; j++) {
            int c = s.charAt(j) - '0';
            if (c != 0 && i % c == 0) {
               v++;
            }
         }
         System.out.println(v);
      }

   }

}
