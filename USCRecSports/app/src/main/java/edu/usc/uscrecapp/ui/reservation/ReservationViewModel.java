package edu.usc.uscrecapp.ui.reservation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Locale;

import edu.usc.uscrecapp.R;

public class ReservationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReservationViewModel() {
        mText = new MutableLiveData<>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        int day = c.get(Calendar.DATE);
        c.add(Calendar.DATE, 2);
        if (month == c.get(Calendar.MONTH))
            mText.setValue(monthName + " " + day + " - " + c.get(Calendar.DATE) + ", " + year);
        else
            mText.setValue(monthName + " " + day + " - " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DATE) + ", " + year);
    }

    public LiveData<String> getText() {
        return mText;
    }
}