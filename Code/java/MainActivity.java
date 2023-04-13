package com.example.winning_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class MainActivity extends AppCompatActivity {private FloatingActionButton mBtn;
    private DateAttr mDate;
    private MenuItem mItem;
    CalendarLayout mLayout;
    CalendarView mView;
    DayView mDayView;

    enum state{
        CALENDAER,
        ADDDATE,
        CHANGEDATE
    }

    state mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDate = new DateAttr(0);
        DateEventManager mngr = DateEventManager.getInstance();
        mngr.init(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mState = state.CALENDAER;

        mngr.LoadingC(mngr);

        mLayout = findViewById(R.id.CalendarLayout);
        mView = findViewById(R.id.CalendarView);
        mDayView = findViewById(R.id.DayView);

        mDayView.setListener(new DayView.OnItemClickListener() {
            @Override
            public void onClickedItem(DateEvent event) {
                mLayout.setEnabled(false);
                mState = state.CHANGEDATE;
                mBtn.setEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("일정 수정");
                getSupportFragmentManager().beginTransaction()
                        //.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, FragmentTodo.newInstance(event))
                        .commit();
            }
        });

        mBtn = (FloatingActionButton)findViewById(R.id.fab);
        mBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mLayout.setEnabled(false);
                mState = state.ADDDATE;
                mBtn.setEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("일정 추가");
                getSupportFragmentManager().beginTransaction()
                        //.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, FragmentTodo.newInstance(null))
                        .commit();
            }
        });

        CalendarView view = (CalendarView)findViewById(R.id.CalendarView);
        view.setListener(new CalendarView.CalendarMonthListener() {
            @Override
            public void changeMonth(DateAttr date) {
                mDate.copyTo(date);
                getSupportActionBar().setTitle(date.getYear() + "-" + (date.getMonth()));
            }
        });
    }

    public void disableFragment(){
        mState = state.CALENDAER;
        mLayout.setEnabled(true);
        mBtn.setEnabled(true);
        mItem.setTitle("오늘");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(mDate.getYear() + "-" + mDate.getMonth());
        mView.invalidate();
        mDayView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mItem = menu.findItem(R.id.action_btn01);
        if(mState == state.CALENDAER){
            mItem.setTitle("오늘");
        }
        else if (mState == state.ADDDATE){
            mItem.setTitle("추가");
        }
        else if (mState == state.CHANGEDATE){
            mItem.setTitle("수정");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (mState == state.CALENDAER){
            if (id == R.id.action_btn01){
                Date today = new Date();
                int year = today.getYear() + 1900;
                int month = today.getMonth() + 1;
                int date = today.getDate();
                mView.setDate(new DateAttr(year, month, date, 0 , 0));

                return true;
            }
        }

        return false;
    }
}