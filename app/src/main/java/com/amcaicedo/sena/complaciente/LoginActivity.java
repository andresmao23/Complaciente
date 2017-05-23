package com.amcaicedo.sena.complaciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.models.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button register, login;
    TextInputLayout usr, pass;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(AppUtil.PREFERENCES_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        register = (Button) findViewById(R.id.btn_register);
        login = (Button) findViewById(R.id.btn_login);

        usr = (TextInputLayout) findViewById(R.id.edit_usr);
        pass = (TextInputLayout) findViewById(R.id.edit_pass);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

        Usuario.init(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                validateUsuario();
                break;
            case R.id.btn_register:
                Snackbar.make(v, "Elemento en construcción", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void validateUsuario() {
        String usrTxt = usr.getEditText().getText().toString();
        String passTxt = pass.getEditText().getText().toString();

        Usuario usuario = Usuario.findUsuarioByUsrAndPass(usrTxt, passTxt);

        if(usuario == null){
            pass.setErrorEnabled(true);
            pass.setError(getString(R.string.login_error));
        }else{

            editor.putBoolean(AppUtil.KEY_LOGIN, true);
            editor.putString(AppUtil.KEY_USR_NAME, usuario.getNombre());
            editor.putString(AppUtil.KEY_USR_IMG, usuario.getUrlusr());
            editor.putString(AppUtil.KEY_USR_IMG_BANNER, usuario.getUrlbanner());
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
