package com.booking.club.utilities;

import java.time.LocalDate;
import java.util.UUID;

public class Utility {

    public static String generateReceiptId(){
        String uuid = UUID.randomUUID().toString().substring(0,5);
        String timeStamp = LocalDate.now().toString().replace(":","");
        return uuid + "-" + timeStamp;
    }
}
