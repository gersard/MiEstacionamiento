package com.example.gerardo.miestacionamiento.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gerardo.miestacionamiento.ui.dialog.DialogEscogerTipoUsuario;
import com.example.gerardo.miestacionamiento.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_usuario)
    TextInputEditText editUsuario;
    @Bind(R.id.edit_password)
    TextInputEditText editPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.txt_Registrar)
    TextView txtRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    @OnClick(R.id.btn_login)
    public void loguear() {

    }

    @OnClick(R.id.txt_Registrar)
    public void registrar() {
        new DialogEscogerTipoUsuario(this).show();
    }

}
