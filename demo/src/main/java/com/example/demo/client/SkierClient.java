package com.example.demo.client;



import ch.qos.logback.core.net.SyslogOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.Executors;

public class SkierClient {
    
    private static final int MAX_SKIER_ID = 100000;
    private static final int MAX_RESORT_ID = 10;
    private static final int MAX_LIFT_ID = 40;
    private static final int SEASON_ID = 2022;
    private static final int DAY_ID = 1;
    private static final int MAX_TIME = 360;
    private static final int NUM_THREADS = 32;
    private static final int REQ_PER_THREAD = 1000;
    private static final int NUM_REQUESTS = 100;
    private static final Random RANDOM = new Random();
    private static long successfulRequests;
    private static long unsucessfulRequests;
    private static List<Object> latencies = Collections.synchronizedList(new ArrayList<>());
//    private static ArrayList<long> latencies = new ArrayList<>();
    private static long minLatency = Long.MAX_VALUE;
    private static long maxLatency = Long.MIN_VALUE;

    static int count=NUM_REQUESTS;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        long start = System.currentTimeMillis();
        for (int i = 0; i <= NUM_THREADS; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 1; j <= REQ_PER_THREAD; j++) {

                           synchronized (this) {
                                if (count - NUM_THREADS >  0) {
                                    String requestBody = generateRequestBody();
                                    sendPostRequest(requestBody);
                                    count--;
                                    System.out.println("count :" + count);
                                    System.out.println("1st" +latencies  );
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("2nd"+ latencies);
                }
            });


        }
        System.out.println("3rd"+latencies);

//        System.out.println("Successful Requests:" + NUM_REQUESTS);
//        System.out.println("Unsuccessful Requests:"+ unsucessfulRequests);
//        long end = System.currentTimeMillis();
//        long total_time = end-start;
//        double meanResponseTime = latencies.stream().mapToLong(Long::longValue).average().orElse(0);;
//        double medianResponseTime = calculateMedian(latencies);
//        double throughput = (double) (successfulRequests + unsucessfulRequests) / ((double) total_time / 1000);
//        double p99ResponseTime = calculatePercentile(latencies, 0.99);
//        for (long latency : latencies) {
//            maxLatency = Math.max(maxLatency, latency);
//            minLatency = Math.min(minLatency, latency);
//        }

//        System.out.println("Mean Response Time: " + meanResponseTime + " ms");
//        System.out.println("Median Response Time: " + medianResponseTime + " ms");
//        System.out.println("Throughput: " + throughput + " requests/second");
//        System.out.println("99th Percentile Response Time: " + p99ResponseTime + " ms");
//        System.out.println("Min Response Time: " + minLatency + " ms");
//        System.out.println("Max Response Time: " + maxLatency + " ms");
        executor.shutdown();
    }


    //            calculate Median
    private static double calculateMedian(List<Long> latencies) {
        int size = latencies.size();
        if (size % 2 == 0) {
            return (latencies.get(size / 2 - 1) + latencies.get(size / 2)) / 2.0;
        } else {
            return latencies.get(size / 2);
        }
    }
    //           calculate    Percentile
    private static double calculatePercentile(List<Long> latencies, double percentile) {
        int index = (int) Math.ceil(percentile * latencies.size());
        return latencies.get(index - 1);
    }
        private static synchronized void sendPostRequest(String requestBody) throws Exception {
            long startTime = System.currentTimeMillis(); // take a timestamp before sending the POST
        	int skierID = RANDOM.nextInt(MAX_SKIER_ID) + 1;
            int resortID = RANDOM.nextInt(MAX_RESORT_ID) + 1;
            //String base_url = "http://localhost:8080/api/skiers/3/seasons/4/days/2/skiers/5";
        	String base_url = "http://localhost:8080/api/skiers/"+String.valueOf(resortID) +"/seasons/" + String.valueOf(SEASON_ID) + "/days/"+ String.valueOf(DAY_ID) +"/skiers/"+String.valueOf(skierID);
            URL url = new URL(base_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
//            System.out.println("at the post request");
            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());
            //System.out.println("request body" + requestBody);
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            System.out.println(responseCode);

            long endTime = System.currentTimeMillis(); // take another timestamp when the response is received
            long latency = endTime - startTime; // calculate the latency
            latencies.add(latency);
            // Construct the record string
            String record = String.format("%s,POST,%d,%d", startTime, latency, responseCode);
            FileWriter writer = new FileWriter("records.csv", true);
            writer.write(record + "\n");
            writer.close();

            if (responseCode != 201) {
                throw new Exception("Failed to send POST request with response code: " + responseCode);
            }
            else {
            	System.out.println("Executed post");
            }
        }

        private static synchronized @NotNull String generateRequestBody() {
            
        	int liftID = RANDOM.nextInt(MAX_LIFT_ID) + 1;
        	int time = RANDOM.nextInt(MAX_TIME) + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                    .append("\"liftID\":").append(String.valueOf(liftID)).append(",")
                    .append("\"time\":").append(String.valueOf(time))
                    .append("}");
            return sb.toString();
        }
        
//        public static void main(String[] args) {
//        	SkierClient client = new SkierClient();
//            int numThreads = 32;
//            int requestsPerThread = 1000;
//            int numRequests = numThreads * requestsPerThread;
//            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//            for (int i = 0; i < numThreads; i++) {
//                executor.submit(() -> {
//                    for (int j = 0; j < requestsPerThread; j++) {
//                        client.sendPOSTRequest();
//                    }
//                });
//            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {
//                try {
//                    executor.awaitTermination(1, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    System.err.println("Thread interrupted: " + e.getMessage());
//                }
//            }
//            int numRemainingRequests = numRequests - client.getNumSuccessfulRequests();
//            for (int i = 0; i < numRemainingRequests; i++) {
//                client.sendPOSTRequest();
//            }
//            System.out.println("All requests sent.");
//        }
    }


