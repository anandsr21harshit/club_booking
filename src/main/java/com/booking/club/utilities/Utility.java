package com.booking.club.utilities;

import com.booking.club.models.Booking;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
public class Utility {

    public static String generateReceiptId(){
        String uuid = UUID.randomUUID().toString().substring(0,5);
        String timeStamp = LocalDate.now().toString().replace(":","");
        return uuid + "-" + timeStamp;
    }
    public static void convertJsonToCSV(List<Booking> allBookingList){
        try {
            StringBuffer content = new StringBuffer();
            String header [] = {"BookingId","UserName","StartDate","EndDate","Amenities","Amount"};
            boolean firstHeader = true;
            for(String headerItem: header){
                if(firstHeader) {
                    content.append(headerItem);
                    firstHeader = false;
                }else {
                    content.append("," + headerItem);
                }
            }
            for(Booking bookingItem: allBookingList){
                content.append("\n");
                content.append(bookingItem.getBookingId());
                content.append(",").append(bookingItem.getUserName());
                content.append(",").append(bookingItem.getStartDate());
                content.append(",").append(bookingItem.getEndDate());
                content.append(",").append(bookingItem.getAmenities());
                content.append(",").append(bookingItem.getAmount());
            }

            BufferedWriter bufferedWriter = null;
            FileWriter fileWriter = null;

            File file = new File("booking.csv");
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content.toString());
            bufferedWriter.close();
            fileWriter.close();

            log.info("File created");
        }catch (Exception ex){
            log.error("Exception occurred: " + ex.getMessage(),ex);
        }
    }
}
