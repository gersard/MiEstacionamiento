package com.example.gerardo.miestacionamiento.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerardo.miestacionamiento.model.ResponseLogin;
import com.example.gerardo.miestacionamiento.model.Usuario;
import com.example.gerardo.miestacionamiento.rest.ApiAdapter;
import com.example.gerardo.miestacionamiento.R;
import com.example.gerardo.miestacionamiento.util.GlobalConstant;
import com.example.gerardo.miestacionamiento.util.GlobalFunction;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_usuario)
    TextInputEditText editUsuario;
    @Bind(R.id.edit_password)
    TextInputEditText editPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.txt_Registrar)
    TextView txtRegistrar;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        prefs = getSharedPreferences(GlobalConstant.PREFS_NAME,MODE_PRIVATE);


    }

    @OnClick(R.id.btn_login)
    public void loguear() {
        if (!GlobalFunction.isEmpty(editUsuario.getText().toString().trim()) &&
                !GlobalFunction.isEmpty(editPassword.getText().toString().trim())){

//            SharedPreferences prefs = this.getSharedPreferences(GlobalConstant.PREFS_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString(GlobalConstant.PREFS_USER,editUsuario.getText().toString().trim());
//            editor.putString(GlobalConstant.PREFS_PASS,editPassword.getText().toString().trim());
//            editor.apply();

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Login");
            dialog.setMessage("Cargando...");
            dialog.setCancelable(false);
            dialog.show();

            Usuario user = new Usuario();
            user.setCorreo(editUsuario.getText().toString().trim());
            user.setContraseña(editPassword.getText().toString().trim());

            //LLAMADA AL WEB SERVICE
            Call<ResponseLogin> retroCall = ApiAdapter.getApiService().login(user);
            retroCall.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    dialog.dismiss();
                    if (response.body().getMensaje().equals("true")){

                        Usuario usuario = response.body().getUsuario();

                        saveUserInfo(usuario);


                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Problemas al conectar, reintentelo en unos minutos", Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            Toast.makeText(LoginActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserInfo(Usuario usuario){
        editor = prefs.edit();

        editor.putString(GlobalConstant.PREFS_RUT,usuario.getRut());
        editor.putString(GlobalConstant.PREFS_NOMBRE,usuario.getNombre());
        editor.putString(GlobalConstant.PREFS_APELLIDO_P,usuario.getApellidoPaterno());
        editor.putString(GlobalConstant.PREFS_APELLIDO_M,usuario.getApellidoMaterno());
        editor.putString(GlobalConstant.PREFS_CORREO,usuario.getCorreo());
        editor.putInt(GlobalConstant.PREFS_TELEFONO,usuario.getTelefono());
        editor.putString(GlobalConstant.PREFS_CLAVE,usuario.getContraseña());

        editor.apply();
    }

    @OnClick(R.id.txt_Registrar)
    public void registrar() {
//        new DialogTipoUsuario(this).show();
        startActivity(new Intent(this,RegistroActivity.class));
    }

}
