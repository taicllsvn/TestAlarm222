package a20156410.chuthetai.com.testalarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    private PendingIntent pending_intent,pendingIntent111,pendingIntent12;
    SharedPreferences sharedPreferences;
    private AlarmReceiver alarm;
    Button btnChangeTime,btnHenGio,btnTatBT,btnSleepdialog,btnTatBTdialog;
    TextView txtTime,txtChiTietTB;
    TextView txtTenCV;
    TextView txtNdCV;
    long hour1,minute1;
    Calendar cal;
    MainActivity inst;
    Context context;
    boolean test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = false;
        btnHenGio       = (Button) findViewById(R.id.btnHenGio);
        txtTime         = (TextView) findViewById(R.id.txtTime);
        btnTatBT        = (Button) findViewById(R.id.btnTatBT);
        btnChangeTime   = (Button) findViewById(R.id.btnChangeTime);
        txtTenCV        = (TextView) findViewById(R.id.txtTenCV);
        txtNdCV         = (TextView) findViewById(R.id.txtNdCV);
        txtChiTietTB = (TextView) findViewById(R.id.txtHienThiTB);
        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        sharedPreferences = getSharedPreferences("ChiTietCV",MODE_PRIVATE);
        //Định dạng giờ phút am/pm
        dft = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String strTime = dft.format(cal.getTime());
        //đưa lên giao diện
        txtTime.setText("Bây giờ là "+strTime);
        //lấy giờ theo 24 để lập trình theo Tag
        dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txtTime.setTag(dft.format(cal.getTime()));

        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        //Xử lý lưu giờ và AM,PM
                        String s = hour + ":" + minute;
                        hour1 = hour;
                        minute1 = minute;
                        String string_phut=String.valueOf(minute);
                        if (minute < 10 )
                        {
                            string_phut= "0" + String.valueOf(minute);
                        }
                        int hourTam = hour;
                        if (hourTam > 12)
                            hourTam = hourTam - 12;
                        txtTime.setText("Đã chọn "+hourTam + ":" + string_phut + (hour > 12 ? " PM" : " AM"));
                        //lưu giờ thực vào tag
                        txtTime.setTag(s);
                        //lưu vết lại giờ
                        //cal.set(Calendar.HOUR_OF_DAY, hour);
                        //cal.set(Calendar.MINUTE, minute);
                        //date = cal.getTime();
                    }

                };

                String s = txtTime.getTag() + "";
                String strArr[] = s.split(":");
                int gio = Integer.parseInt(strArr[0]);
                int phut = Integer.parseInt(strArr[1]);
                TimePickerDialog time = new TimePickerDialog(
                        MainActivity.this,
                        callback, gio, phut, true);
                time.setTitle("Chọn giờ nhắc việc");
                time.show();

            }
        });


        this.context = this;

        //alarm = new AlarmReceiver();
       txtTime = (TextView) findViewById(R.id.txtTime);
        final Intent intent= new Intent(MainActivity.this,MainActivity.class);

        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();

        Button start_alarm= (Button) findViewById(R.id.btnHenGio);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                //calendar.add(Calendar.SECOND, 3);
                //setAlarmText("You clicked a button");

                test = true;



                myIntent.putExtra("extra", "yes");
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent pendingIntent1= PendingIntent.getActivity(MainActivity.this,00014,intent,0);
                long ThoiGianHenGio;
                long GioHen,PhutHen;

                //Toast.makeText(MainActivity.this,"Báo thức được đặt đổ chuông sau " +" giờ "+" phút " +" nữa tính từ bây giờ",Toast.LENGTH_LONG).show();
                if (hour1==calendar.get(Calendar.HOUR_OF_DAY)) {
                    GioHen = (hour1 - calendar.get(Calendar.HOUR_OF_DAY));
                    PhutHen = (minute1 - 1 - calendar.get(Calendar.MINUTE));   long PhutHenMili= TimeUnit.MINUTES.toMillis(PhutHen);
                    int GiayHen = (60 - calendar.get(Calendar.SECOND)); long GiayHenMili=TimeUnit.SECONDS.toMillis(GiayHen);
                    ThoiGianHenGio = PhutHenMili+GiayHenMili ;
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+  ThoiGianHenGio, pending_intent);
                   alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ThoiGianHenGio,pendingIntent1);



                    if (hour1==0 && minute1==0 || hour1==calendar.get(Calendar.HOUR_OF_DAY) && hour1==calendar.get(Calendar.MINUTE))
                    {

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Báo thức sẽ đổ chuông sau " + GioHen + " giờ " + (PhutHen + 1) + " phút nữa", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    GioHen = (hour1-1 - calendar.get(Calendar.HOUR_OF_DAY)); long GioHenMili=TimeUnit.HOURS.toMillis(GioHen);
                    PhutHen = (60 + minute1 - calendar.get(Calendar.MINUTE)); long PhutHenMili=TimeUnit.MINUTES.toMillis(PhutHen);
                    int GiayHen = (60 - calendar.get(Calendar.SECOND)); long GiayHenMili=TimeUnit.SECONDS.toMillis(GiayHen);
                    ThoiGianHenGio = GioHenMili+PhutHenMili+GiayHenMili  ;
                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ ThoiGianHenGio, pending_intent);
                   // alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ThoiGianHenGio,pendingIntent1);




                    if (PhutHen==60) {
                        GioHen=GioHen+1;
                    }
                    if (PhutHen>60){
                        GioHen=GioHen+1;
                    }

                    if (hour1==0 && minute1==0 || hour1==calendar.get(Calendar.HOUR_OF_DAY) && minute1==calendar.get(Calendar.MINUTE))
                    {

                    }
                    else if (PhutHen==60)
                    {
                        Toast.makeText(MainActivity.this,"Báo thức sẽ đổ chuông sau " + GioHen + " giờ "+ "nữa" , Toast.LENGTH_SHORT).show();
                    }


                    else  if(PhutHen>60)  {
                        PhutHen=PhutHen-60;
                        String string_chuoi= String.valueOf(PhutHen);
                        if (PhutHen<10)
                        {
                            string_chuoi="0"+String.valueOf(PhutHen);
                        }
                        Toast.makeText(MainActivity.this,"Báo thức sẽ đổ chuông sau " + GioHen + " giờ " + string_chuoi + " phút nữa" , Toast.LENGTH_SHORT).show();


                    }

                    else {
                        Toast.makeText(MainActivity.this, "Báo thức sẽ đổ chuông sau " + GioHen + " giờ " + (PhutHen) + " phút nữa", Toast.LENGTH_SHORT).show();
                    }
                }


                /*pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);*/


                // now you should change the set Alarm text so it says something nice



                //Toast.makeText(getApplicationContext(), "You set the alarm", Toast.LENGTH_SHORT).show();
            txtChiTietTB.setText(txtNdCV.getText().toString());
            }

        });

        Button stop_alarm= (Button) findViewById(R.id.btnTatBT);
            stop_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.custom_dialog);
                    btnSleepdialog = dialog.findViewById(R.id.btnSleepdialog);
                    btnTatBTdialog = dialog.findViewById(R.id.btnTatBTdialog);
                    btnTatBTdialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myIntent.putExtra("extra", "no");

                            sendBroadcast(myIntent);
                            alarmManager.cancel(pending_intent);
                            alarmManager.cancel(pendingIntent111);
                            alarmManager.cancel(pendingIntent12);
                            dialog.cancel();

                        }
                    });

                    btnSleepdialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cal = Calendar.getInstance();

                            myIntent.putExtra("extra", "no");
                            sendBroadcast(myIntent);
                            alarmManager.cancel(pending_intent);

                            Toast.makeText(MainActivity.this, "Báo thức sẽ reo trong 5 phút nữa", Toast.LENGTH_SHORT).show();

                            Intent intent1111 = new Intent(MainActivity.this,AlarmReceiver.class);
                            intent1111.putExtra("extra", "yes");
                            pendingIntent111=PendingIntent.getActivity(MainActivity.this,1212,intent,0);
                            pendingIntent12 = PendingIntent.getBroadcast(MainActivity.this, 10, intent1111, 0);

                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent12);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 60000 ,pendingIntent111);

                            dialog.cancel();
                        }

                    });
                    dialog.show();



                /*myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);

                alarmManager.cancel(pending_intent);*/

                }
            });


    }




    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //tạo đối tượng editor để lưu giá trị
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String NdCV= txtNdCV.getText().toString();
        editor.putBoolean("test",test);
        editor.putString("NdCv",NdCV);
        editor.putLong("Gio",hour1);
        editor.putLong("Phut",minute1);
        editor.commit();
    }



    @Override
    protected void onResume() {
        super.onResume();
        String NDCV =sharedPreferences.getString("NdCv",null);
        long GioPr =sharedPreferences.getLong("Gio",0);
        long PhutPr = sharedPreferences.getLong("Phut",0);
        String Phut = String.valueOf(PhutPr);
        String Gio = String.valueOf(GioPr);
            if (GioPr == 0 && PhutPr == 0 )
            {
                txtChiTietTB.setText("Chưa đặt công việc nào");
            }
            else {
                if (PhutPr <10)
                {

                    Phut = "0" + String.valueOf(PhutPr);
                }
                if (GioPr <10)
                {

                    Gio = "0" + String.valueOf(PhutPr);
                }

                txtChiTietTB.setText(" "+ NDCV + "\n " + Gio + ":" + Phut);
            }

    }
}
