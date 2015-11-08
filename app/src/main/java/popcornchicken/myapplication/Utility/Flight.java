package popcornchicken.myapplication.Utility;

/**
 * Created by ianwind2 on 15/11/8.
 */
public class Flight {
    public String original;
    public String destination;
    public String date;        // 2015-11-27
    public int price;
    public String start_time;  // 00:00
    public String end_time;    // 00:00
    public int duration;
    public Boolean isDirect;

    public Flight(String original, String destination, String date, String QuoteDateTime, int price, Boolean isDirect){
        this.original = original;
        this.destination = destination;
        this.date = date.split("T")[0];
        this.price = price;
        this.isDirect = isDirect;

        // Fake.
        SetDuration(QuoteDateTime, price);
    }

    String formatTime(int num){
        String result = "";

        if (num < 10){
            result += "0";
        }

        result += num;

        return result;
    }

    String forwardTime(int hour, int minute, int duration){
        int total = hour*60 + minute + duration;
        minute = total % 60;
        hour = (total / 60) % 24;

        return formatTime(hour) + ":" + formatTime(minute);
    }

    void SetDuration(String time, int price){
        String[] ary = time.split("T")[1].split(":");
        this.start_time = ary[0] + ":" + ary[1];

        if (price < 300){
            duration = 90;
        }
        else if(price < 400){
            duration = 200;
        }
        else if(price < 500){
            duration = 300;
        }
        else if(price < 700){
            duration = 480;
        }
        else if(price < 900){
            duration = 600;
        }
        else{
            duration = 900;
        }

        this.end_time = forwardTime(Integer.valueOf(ary[0]), Integer.valueOf(ary[1]), duration);
    }
}
