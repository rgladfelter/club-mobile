package clubs.mobile.radford.clubmobile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import clubs.mobile.radford.clubmobile.managers.UserManager;
import clubs.mobile.radford.clubmobile.models.Club;
import clubs.mobile.radford.clubmobile.networking.ClubService;
import clubs.mobile.radford.clubmobile.networking.ClubServiceProvider;
import clubs.mobile.radford.clubmobile.utils.AlertHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubActivity extends ToolbarActivity implements Callback<Void> {
    private Club club;
    @Override
    public int layoutId() {
        return R.layout.activity_club;
    }

    @Override
    public String title() {
        return ((Club) getIntent().getSerializableExtra("club")).getName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        club = (Club) getIntent().getSerializableExtra("club");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leave_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_leave) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to leave " + club.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ClubService clubService = ClubServiceProvider.getService();
                            clubService.leaveClub(UserManager.getSessionId(), club.getId()).enqueue(ClubActivity.this);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
            finish();
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to leave club").show();
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to leave club").show();
    }
}
