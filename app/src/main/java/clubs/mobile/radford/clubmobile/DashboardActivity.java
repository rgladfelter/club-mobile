package clubs.mobile.radford.clubmobile;

import android.os.Bundle;

public class DashboardActivity extends NavigationDrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public int layoutId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public String title() {
        return "Dashboard";
    }
}
