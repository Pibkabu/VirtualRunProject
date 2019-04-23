package com.example.quynh.virtualrunproject.functionscreen.race;
import com.bumptech.glide.Glide;
import com.example.quynh.virtualrunproject.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.quynh.virtualrunproject.customGUI.PlayerIconAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.PlayerListDAO;
import com.example.quynh.virtualrunproject.dao.UserProfileDAO;
import com.example.quynh.virtualrunproject.entity.DonateAccount;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserAndRaceMaped;
import com.example.quynh.virtualrunproject.functionscreen.hosting.RaceDonationScreen;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.UserProfileListScreen;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.helper.PictureResizeHandler;
import com.example.quynh.virtualrunproject.services.DonateAccountServices;
import com.example.quynh.virtualrunproject.services.PlayerServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RaceDetailScreen extends AppCompatActivity implements View.OnClickListener{

    private TextView toolbarTitle;
    private ImageView raceImage;
    private RecyclerView playerIcons;
    private TextView title;
    private TextView numberOfPlayer;
    private TextView countDownDays, countDownHours, countDownMins, countDownSecs;
    private TextView raceTime, txtDonation;
    private ReadMoreTextView description, regulation;
    private Button joinRaceBtn, cancelRaceBtn;
    private ImageView backBtn;
    private CountDownTimer countDownTimer = null;
    private List<Player> players;
    private Player individual;
    private Race race;
    private DonateAccount donateAccount;
    private PlayerIconAdapter adapter;
    private TextView raceDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_detail);

        setupView();
        setupRaceInfo();
        setupAction();
    }

    private void setupRaceInfo() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        UserAccountPrefs prefs = new UserAccountPrefs(this);
        UserAccount account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
        race = gson.fromJson(intent.getStringExtra("raceString"), Race.class);
        individual = new Player();
        individual.setUserAndRaceMaped(new UserAndRaceMaped(account.getUserId(), race.getRaceId()));
        toolbarTitle.setText(race.getName());
        title.setText(race.getName());
        numberOfPlayer.setText(race.getTotalPlayer() + " Người tham gia cuộc đua");

        Log.d("TestImage", "setupRaceInfo: " + race.getRaceImage());
        Glide.with(this).load(race.getRaceImage()).into(raceImage);

        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", race.getStartTime().toString());
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", race.getEndTime().toString());
        String startTime = DateFormatHandler.dateToString("dd MM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Giờ Việt Nam";
        String endTime = DateFormatHandler.dateToString("dd MM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Giờ Việt Nam";
        raceTime.setText(startTime + " đến " + endTime);
        description.setText(race.getDescription());
        regulation.setText(race.getRegulation());
        raceDistance.setText("Quãng Đường: " + race.getDistance() + " (km)");

        Calendar calendar = Calendar.getInstance();
        long countDownTime = startDate.getTime() - calendar.getTimeInMillis();
        if(countDownTime > 0){
            countDownTimer = new CountDownTimer(countDownTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
                    countDownDays.setText(String.valueOf(days));

                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                    countDownHours.setText(String.valueOf(hours));

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                    countDownMins.setText(String.valueOf(minutes));

                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    countDownSecs.setText(String.valueOf(seconds));
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
        }
        getRaceParticipants(race.getRaceId());
        getRaceDonateAccount();
    }

    private void setupView() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        raceImage = (ImageView) findViewById(R.id.race_head_image);
        playerIcons = (RecyclerView) findViewById(R.id.players_icon);
        title = (TextView) findViewById(R.id.race_detail_title);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        numberOfPlayer = (TextView) findViewById(R.id.number_of_players_detail);
        countDownDays = (TextView) findViewById(R.id.countdown_days);
        countDownHours = (TextView) findViewById(R.id.countdown_hours);
        countDownMins = (TextView) findViewById(R.id.countdown_mins);
        countDownSecs = (TextView) findViewById(R.id.countdown_secs);
        raceTime = (TextView) findViewById(R.id.race_time);
        txtDonation = (TextView) findViewById(R.id.txt_donation);
        raceDistance = (TextView) findViewById(R.id.race_distance);
        description = (ReadMoreTextView) findViewById(R.id.race_description);
        regulation = (ReadMoreTextView) findViewById(R.id.race_regulation);
        joinRaceBtn = (Button) findViewById(R.id.race_join_btn);
        cancelRaceBtn = (Button) findViewById(R.id.race_cancel_btn);


        //raceImage.setImageDrawable(PictureResizeHandler.resizeImage(R.drawable.dummy_picture, this));

        PushDownAnim.setPushDownAnimTo(joinRaceBtn);
        PushDownAnim.setPushDownAnimTo(cancelRaceBtn);
        PushDownAnim.setPushDownAnimTo(txtDonation);
    }

    private void setupAction() {
        backBtn.setOnClickListener(this);
        joinRaceBtn.setOnClickListener(this);
        cancelRaceBtn.setOnClickListener(this);
        txtDonation.setOnClickListener(this);
        playerIcons.setOnClickListener(this);
    }

    private void getRaceParticipants(int raceId){
        players = new ArrayList<>();
        PlayerServices.getRaceParticipants(raceId, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                PlayerListDAO dao = gson.fromJson(response.toString(), PlayerListDAO.class);
                UserAccountPrefs prefs = new UserAccountPrefs(RaceDetailScreen.this);
                UserAccount account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
                players = dao.getPlayers();
                adapter = new PlayerIconAdapter(players, RaceDetailScreen.this);
                playerIcons.setLayoutManager(new LinearLayoutManager(RaceDetailScreen.this, LinearLayoutManager.HORIZONTAL, false));
                playerIcons.setAdapter(adapter);
                playerIcons.setNestedScrollingEnabled(false);
                if(!players.isEmpty()){
                    for (Player player : players){
                        if(player.getUserAndRaceMaped().getUserId() == account.getUserId()){
                            joinRaceBtn.setVisibility(View.GONE);
                            cancelRaceBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void getUsersProfile(){
        PlayerServices.getParticipateProfile(race.getRaceId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Intent intent = new Intent(RaceDetailScreen.this, UserProfileListScreen.class);
                intent.putExtra("profiles", response.toString());
                startActivity(intent);
            }
        });
    }

    private void inputPassword(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        TextView labelText = (TextView) promptsView.findViewById(R.id.label_text);
        labelText.setText("Mật Khẩu: ");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input
                                if(userInput.getText().toString().equals(race.getRacePassword())){
                                    registerForRace();
                                }else{
                                    Toast.makeText(RaceDetailScreen.this, "Mật khẩu không chính xác", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .setNegativeButton("Hủy",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void getRaceDonateAccount(){
        DonateAccountServices.getRaceDonationRecord(race.getRaceId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                donateAccount = gson.fromJson(response.toString(), DonateAccount.class);
                if(donateAccount.getRaceId() != 0){
                    txtDonation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void registerForRace(){
        PlayerServices.playerRegister(individual, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                Player player = gson.fromJson(response.toString(), Player.class);
                if(player != null){
                    joinRaceBtn.setVisibility(View.GONE);
                    cancelRaceBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(RaceDetailScreen.this, "Bạn đã tham gia cuộc đua", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RaceDetailScreen.this, "There are some error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cancelRegister(){
        PlayerServices.cancelRegister(individual, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                Player player = gson.fromJson(response.toString(), Player.class);
                if(player != null){
                    joinRaceBtn.setVisibility(View.VISIBLE);
                    cancelRaceBtn.setVisibility(View.GONE);
                    Toast.makeText(RaceDetailScreen.this, "Bạn đã hủy đăng ký tham gia cuộc đua", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RaceDetailScreen.this, "There are some error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.race_join_btn:
                if(!race.getRacePassword().trim().equalsIgnoreCase("")){
                    inputPassword();
                }else{
                    registerForRace();
                }
                break;
            case R.id.race_cancel_btn:

                android.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.app.AlertDialog.Builder(this);
                }
                builder.setTitle("Hủy đăng ký")
                        .setMessage("Bạn có chắc chắn muốn hủy đăng ký tham gia đường chạy này không ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cancelRegister();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.txt_donation:
                Intent intent = new Intent(this, RaceDonationScreen.class);
                Gson gson = new Gson();
                intent.putExtra("donateAccount", gson.toJson(donateAccount));
                startActivity(intent);
                break;
            case R.id.players_icon:
                getUsersProfile();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
