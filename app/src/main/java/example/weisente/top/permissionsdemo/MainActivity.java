package example.weisente.top.permissionsdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 打电话权限申请的请求码
    private static final int CALL_PHONE_REQUEST_CODE = 0x0011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = (Button) findViewById(R.id.bt);
    }

    public void phoneClick(View view) {
        PermissionHelper.with(this).requestCode(CALL_PHONE_REQUEST_CODE)
                .requestPermission(Manifest.permission.CALL_PHONE).request();
    }

    @PermissionSucceed(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:147****2514");
        intent.setData(data);
        startActivity(intent);
    }

    @PermissionFail(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhoneFail(){
        Toast.makeText(this,"您拒绝了拨打电话",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.requestPermissionsResult(this,CALL_PHONE_REQUEST_CODE,permissions);

    }
}
