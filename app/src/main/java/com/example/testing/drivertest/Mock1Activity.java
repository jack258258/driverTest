package com.example.testing.drivertest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class Mock1Activity extends AppCompatActivity {
    private SharedPreferences settings; //讀取Sharedpreference 物件內容getString(key, "unknow")，讀取被寫入的資料
    private static final String data_token = "DATA";
    private static final String data_status = "STATUS";
    private SharedPreferences status_settings;
    private String SocketURL = "http://172.104.110.249:3000/";
    private Socket socket;
    private Button btngps, Whostation, Kairplan, Mstation, Tainin;
    private TextView txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock1);
        settings = getSharedPreferences(data_token,0);            //讀取Sharedpreference 物件內容getString(key, "unknow")，讀取被寫入的資料
        String token = settings.getString("token", "");
        status_settings = getSharedPreferences(data_status,0);
        btngps =(Button)findViewById(R.id.gpsbtn);
        txt= (TextView)findViewById(R.id.txt);
        Whostation =(Button)findViewById(R.id.whostation);
        Kairplan =(Button)findViewById(R.id.kairplan);
        Mstation =(Button)findViewById(R.id.mstation);
        Tainin =(Button)findViewById(R.id.tainan);

        Tainin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mockGPS4();
                    }
                }).start();
            }
        });
        Mstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mockGPS2();
                    }
                }).start();
            }
        });
        Kairplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mockGPS3();
                    }
                }).start();
            }
        });
        Whostation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                a=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mockGPS1();
                    }
                }).start();
            }
        });
        btngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mockGPS();
                    }
                }).start();
            }
        });

        Log.e("TOKEN-LOGO-Main", token);
        {
            try{
                IO.Options options = new IO.Options();
                options.forceNew=true;
                options.reconnection = false;
                options.query = "token="+token;  //token認證
                socket = IO.socket(SocketURL, options);
                Log.e("Token",options.query);
            }catch (Exception e){
                e.printStackTrace();
                Log.d("Error connecting","連線失敗");
            }
        }
        socket.connect();                       //連線
        socket.on("ConnectStatus", handling);
        socket.on("SendDriverGPS", SendDriverGPS);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Emitter.Listener handling = new Emitter.Listener() {  //後台發送，接收
        @Override
        public void call( final Object... args ) {
            JSONObject data = (JSONObject) args[0];
            if (data.equals("")){
                System.out.println("無資料");

            }else {
                String JSONdata = data.toString();
                //Json 字串解析'
                String json = JSONdata;
                try {
                    Log.d("handling:", json);
                    JSONObject parentObject = new JSONObject(json);
                    String success = parentObject.getString("success");
                    String message = parentObject.getString("message");
                    Log.d("JSON-connect-socket:", success);
                    Log.d("JSON-connect:", success);//顯示在電腦上
                    Log.d("JSON message:", success+" "+message);
                } catch (JSONException e) {
                    Log.d("JSONException", "有錯誤");
                    e.printStackTrace();
                }
            }
        }

    };
    private Emitter.Listener SendDriverGPS  = new Emitter.Listener() {  //後台發送，接收
        @Override
        public void call( final Object... args ) {
            JSONObject data = (JSONObject) args[0];
            try {
                Log.i("socket-SendDriverGPS", String.valueOf(data));
                String success = data.getString("success");
                String message = data.getString("message");
            } catch (JSONException e) {
                Log.d("JSONException", "SendDriverGPS error");
                e.printStackTrace();

            }
        }
    };
    public int a = 0;
    private String serlist;
    public void mockGPS(){

        int mocknumber = 0;
        try {
            Double lat1[][]={{22.639654, 22.63979, 22.639929, 22.640147, 22.640553, 22.64082, 22.641266, 22.641563
                    , 22.642068, 22.642434, 22.640028, 22.640295, 22.6408, 22.641196, 22.641651, 22.641913, 22.642116
                    , 22.642581, 22.642715, 22.643136, 22.639645, 22.639571, 22.639398, 22.639076, 22.639002, 22.638863,
                    22.638734, 22.638585, 22.638129, 22.638155},
                    {120.302727, 120.303433, 120.303959, 120.304538, 120.305203, 120.305857, 120.306511, 120.307058
                            , 120.307627, 120.308206, 120.301704, 120.3012, 120.300706, 120.300234, 120.29974, 120.299434, 120.299322
                            , 120.298689, 120.298442, 120.298345, 120.301676, 120.301456, 120.301118, 120.300812, 120.30063, 120.300437,
                            120.30026, 120.300056, 120.29937, 120.298861
                    },};
            for (int i=0;i<1; i++) {

                for (int j = 0; j < lat1[i].length; j++){
                    if(a<=6){
                        serlist = "1,2";
                        a ++;
                    }else if (6 < a && a<=12){
                        serlist = "2,3,4";
                        a ++;
                    }else if (12 < a && a<=18){
                        serlist = "5,6,7";
                        a ++;
                    }else if (18 < a && a<=24){
                        serlist = "4,5,6,7,8,9,10";
                        a ++;
                    }else if (24 < a && a<=30){
                        serlist = "1,2,3,4,5,6,7,8,9";
                        a ++;
                    }
                    Log.i("serlist","Int:"+String.valueOf(a)+" "+serlist);
                    int team_code = (int)(Math.random()*2+1);
                    int number1= mocknumber+1;
                    mocknumber = number1;
                    int status = (int)(Math.random()*10+1);
                    if(status>=5){
                        Log.i("status",String.valueOf(status));
                        status = 5;
                    }else if (status<5){
                        Log.i("status2",String.valueOf(status));
                        status = 1;
                    }
                    Double lat = lat1[0][j];
                    Double lng = lat1[1][j];
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("gps_Lat", lat);
                        data.put("gps_Lng", lng);
                        data.put("status", status);
                        data.put("account",number1);
                        data.put("callCarTeamID","00"+String.valueOf(team_code));
                        data.put("serviceList",serlist);
                        data.put("city", "kaohsiungcity");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i("GPStoService","Json");
                        //socket.emit("SendDriverGPS", data);
                    }
                    Log.i("getSendDriverGPS",String.valueOf(data));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(String.valueOf(data));
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void mockGPS1(){
        //大湖火車站(乘客點)	22.87818, 120.2537
        int mocknumber = 0;
        try {
            Double lat1[][]={{22.881809,22.882115,22.882313,22.882481,22.883351,22.885367
                    ,22.886682,22.888026,22.888688,22.88938,22.880121,22.878114
                    ,22.878519,22.879488,22.880249,22.880051,22.881306,22.879532
                    ,22.879453,22.879433,22.877337,22.877268,22.876675,22.876586
                    ,22.876467,22.876744,22.876774,22.87626,22.876013,22.875677
            },
                    {120.253298,120.252268,120.25098,120.249833,120.250455
                            ,120.250205,120.250205,120.250226,120.250355,120.250731
                            ,120.253548,120.252733,120.251714,120.250941,120.250384
                            ,120.249064,120.249429,120.247722,120.246789,120.245598
                            ,120.252529,120.252625,120.252786,120.252840,120.252464
                            ,120.251005,120.250501,120.250469,120.250458,120.250479
                    },};
            for (int i=0;i<1; i++) {

                for (int j = 0; j < lat1[i].length; j++){
                    if(a<=6){
                        serlist = "1,2";
                        a ++;
                    }else if (6 < a && a<=12){
                        serlist = "2,3,4";
                        a ++;
                    }else if (12 < a && a<=18){
                        serlist = "5,6,7";
                        a ++;
                    }else if (18 < a && a<=24){
                        serlist = "4,5,6,7,8,9,10";
                        a ++;
                    }else if (24 < a && a<=30){
                        serlist = "1,2,3,4,5,6,7,8,9";
                        a ++;
                    }
                    Log.i("serlist","Int:"+String.valueOf(a)+" "+serlist);
                    int team_code = (int)(Math.random()*2+1);
                    int number1= mocknumber+1;
                    mocknumber = number1;
                    int status = (int)(Math.random()*10+1);
                    if(status>=5){
                        Log.i("status",String.valueOf(status));
                        status = 5;
                    }else if (status<5){
                        Log.i("status2",String.valueOf(status));
                        status = 1;
                    }
                    Double lat = lat1[0][j];
                    Double lng = lat1[1][j];
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("gps_Lat", lat);
                        data.put("gps_Lng", lng);
                        data.put("status", status);
                        data.put("account",number1);
                        data.put("callCarTeamID","00"+String.valueOf(team_code));
                        data.put("serviceList",serlist);
                        data.put("city", "kaohsiungcity");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i("GPStoService","Json");
                        //socket.emit("SendDriverGPS", data);
                    }
                    Log.i("getSendDriverGPS",String.valueOf(data));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(String.valueOf(data));
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void mockGPS2(){
        //岡山	22.79221	, 120.300000
        int mocknumber = 0;
        try {
            Double lat1[][]={{22.794821,22.796453,22.797412,22.798203,22.798875
                    ,22.798995,22.799944,22.800765,22.801487,22.802278
                    ,22.792537,22.792705,22.792913,22.793872,22.793358
                    ,22.793635,22.793932,22.794278,22.794782,22.795425
                    ,22.791795,22.791508,22.791023,22.790706,22.790469
                    ,22.790024,22.789727,22.789153,22.788559,22.788194

            },
                    {120.298820,120.298734,120.298841,120.298659,120.299195
                            ,120.298229,120.298572,120.297982,120.297446,120.296995
                            ,120.298315,120.296782,120.295977,120.295837,120.294969
                            ,120.294304,120.293553,120.293156,120.292663,120.291793
                            ,120.299067,120.299110,120.299260,120.299292,120.299281
                            ,120.299324,120.299367,120.299453,120.299528,120.299453
                    },};
            for (int i=0;i<1; i++) {

                for (int j = 0; j < lat1[i].length; j++){
                    if(a<=6){
                        serlist = "1,2";
                        a ++;
                    }else if (6 < a && a<=12){
                        serlist = "2,3,4";
                        a ++;
                    }else if (12 < a && a<=18){
                        serlist = "5,6,7";
                        a ++;
                    }else if (18 < a && a<=24){
                        serlist = "4,5,6,7,8,9,10";
                        a ++;
                    }else if (24 < a && a<=30){
                        serlist = "1,2,3,4,5,6,7,8,9";
                        a ++;
                    }
                    Log.i("serlist","Int:"+String.valueOf(a)+" "+serlist);
                    int team_code = (int)(Math.random()*2+1);
                    int number1= mocknumber+1;
                    mocknumber = number1;
                    int status = (int)(Math.random()*10+1);
                    if(status>=5){
                        Log.i("status",String.valueOf(status));
                        status = 5;
                    }else if (status<5){
                        Log.i("status2",String.valueOf(status));
                        status = 1;
                    }
                    Double lat = lat1[0][j];
                    Double lng = lat1[1][j];
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("gps_Lat", lat);
                        data.put("gps_Lng", lng);
                        data.put("status", status);
                        data.put("account",number1);
                        data.put("callCarTeamID","00"+String.valueOf(team_code));
                        data.put("serviceList",serlist);
                        data.put("city", "kaohsiungcity");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i("GPStoService","Json");
                        //socket.emit("SendDriverGPS", data);
                    }
                    Log.i("getSendDriverGPS",String.valueOf(data));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(String.valueOf(data));
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void mockGPS3(){
        //高雄國際機場	22.577291, 120.347600
        int mocknumber = 0;
        try {
            Double lat1[][]={{22.581002,22.58178,22.582721,22.583692,22.584415
                    ,22.585564,22.586218,22.58704,22.587852,22.588883
                    ,22.58026,22.581448,22.582498,22.583241,22.584034
                    ,22.584875,22.58543,22.586173,22.586847,22.587402
                    ,22.577358,22.578428,22.579389,22.580399,22.581449
                    ,22.583153,22.584064,22.584718,22.586113,22.587866
            },
                    {120.346897,120.347154,120.347283,120.347390,120.347529
                            ,120.347465,120.347379,120.347036,120.346854,120.346875
                            ,120.343827,120.342915,120.341864,120.341081,120.340266
                            ,120.339772,120.339633,120.339451,120.339269,120.338786
                            ,120.339646,120.336352,120.334442,120.332972,120.331191
                            ,120.329721,120.328187,120.326824,120.327188,120.326748

                    },};
            for (int i=0;i<1; i++) {

                for (int j = 0; j < lat1[i].length; j++){
                    if(a<=6){
                        serlist = "1,2";
                        a ++;
                    }else if (6 < a && a<=12){
                        serlist = "2,3,4";
                        a ++;
                    }else if (12 < a && a<=18){
                        serlist = "5,6,7";
                        a ++;
                    }else if (18 < a && a<=24){
                        serlist = "4,5,6,7,8,9,10";
                        a ++;
                    }else if (24 < a && a<=30){
                        serlist = "1,2,3,4,5,6,7,8,9";
                        a ++;
                    }
                    Log.i("serlist","Int:"+String.valueOf(a)+" "+serlist);
                    int team_code = (int)(Math.random()*2+1);
                    int number1= mocknumber+1;
                    mocknumber = number1;
                    int status = (int)(Math.random()*10+1);
                    if(status>=5){
                        Log.i("status",String.valueOf(status));
                        status = 5;
                    }else if (status<5){
                        Log.i("status2",String.valueOf(status));
                        status = 1;
                    }
                    Double lat = lat1[0][j];
                    Double lng = lat1[1][j];
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("gps_Lat", lat);
                        data.put("gps_Lng", lng);
                        data.put("status", status);
                        data.put("account",number1);
                        data.put("callCarTeamID","00"+String.valueOf(team_code));
                        data.put("serviceList",serlist);
                        data.put("city", "kaohsiungcity");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i("GPStoService","Json");
                        //socket.emit("SendDriverGPS", data);
                    }
                    Log.i("getSendDriverGPS",String.valueOf(data));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(String.valueOf(data));
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void mockGPS4(){
        //崑山	22.587866,120.326748

        int mocknumber = 0;
        try {
            Double lat1[][]={{23.000602,23.001204,23.001895,23.002705,23.003317
                   ,23.004018,23.004689,23.005243,23.006625,23.005707
                    ,22.999653,23.000127,23.000542,23.001006,23.001549
                    ,23.002102,23.002467,23.003168,23.004027,23.004797
                    ,22.998476,22.998604,22.998485,22.998663,22.999206
                    ,22.999976,23.000776,23.001497,23.002485,23.003443

            },
                    {120.254297,120.254420,120.254495,120.254463,120.254388
                            ,120.254163,120.253755,120.253508,120.252410,120.253111
                            ,120.253411,120.254237,120.254902,120.255428,120.255782
                            ,120.256297,120.256672,120.257252,120.257509,120.257799
                            ,120.250825,120.249956,120.249527,120.248647,120.248250
                            ,120.248057,120.248143,120.248004,120.248090,120.248004
                    },};
            for (int i=0;i<1; i++) {

                for (int j = 0; j < lat1[i].length; j++){
                    if(a<=6){
                        serlist = "1,2";
                        a ++;
                    }else if (6 < a && a<=12){
                        serlist = "2,3,4";
                        a ++;
                    }else if (12 < a && a<=18){
                        serlist = "5,6,7";
                        a ++;
                    }else if (18 < a && a<=24){
                        serlist = "4,5,6,7,8,9,10";
                        a ++;
                    }else if (24 < a && a<=30){
                        serlist = "1,2,3,4,5,6,7,8,9";
                        a ++;
                    }
                    Log.i("serlist","Int:"+String.valueOf(a)+" "+serlist);
                    int team_code = (int)(Math.random()*2+1);
                    int number1= mocknumber+1;
                    mocknumber = number1;
                    int status = (int)(Math.random()*10+1);
                    if(status>=5){
                        Log.i("status",String.valueOf(status));
                        status = 5;
                    }else if (status<5){
                        Log.i("status2",String.valueOf(status));
                        status = 1;
                    }
                    Double lat = lat1[0][j];
                    Double lng = lat1[1][j];
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("gps_Lat", lat);
                        data.put("gps_Lng", lng);
                        data.put("status", status);
                        data.put("account",number1);
                        data.put("callCarTeamID","00"+String.valueOf(team_code));
                        data.put("serviceList",serlist);
                        data.put("city", "tainancity");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i("GPStoService","Json");
                        //socket.emit("SendDriverGPS", data);
                    }
                    Log.i("getSendDriverGPS",String.valueOf(data));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(String.valueOf(data));
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
