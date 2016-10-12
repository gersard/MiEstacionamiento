package com.example.gerardo.miestacionamiento.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerardo.miestacionamiento.R;
import com.example.gerardo.miestacionamiento.ui.fragment.MapFragment;
import com.example.gerardo.miestacionamiento.ui.fragment.MiCuentaFragment;
import com.example.gerardo.miestacionamiento.ui.fragment.PreferenciasFragment;
import com.example.gerardo.miestacionamiento.util.GlobalFunction;
import com.google.android.gms.maps.GoogleMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleMap mGoogleMap;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @Bind(R.id.image_header)
    ImageView imageHeader;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    Menu menu;
    Fragment fragment = null;
    @Bind(R.id.image_circle_header)
    CircleImageView imageCircleHeader;
    @Bind(R.id.txt_header_nombre)
    TextView txtHeaderNombre;
    @Bind(R.id.txt_header_tipo)
    TextView txtHeaderTipo;

    TextView txtToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        txtToolbar = (TextView) toolbar.findViewById(R.id.txt_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        displayView(R.id.nav_home);
        navView.setNavigationItemSelectedListener(this);


        ImageView iv = (ImageView) navView.getHeaderView(0).findViewById(R.id.navHeader_image);


        iv.setImageBitmap(setImageBlurEffect(R.drawable.logo_last));
        imageHeader.setImageBitmap(setImageBlurEffect(R.drawable.logo_last));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        disableCollapse();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        syncFrags();
    }

    private void syncFrags() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (fragment instanceof MiCuentaFragment) {
            enableCollapse();
        } else {
            disableCollapse();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        hideOption(R.id.action_estacionamiento);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_estacionamiento:
                break;
            case R.id.action_vehiculo:
                break;
            case R.id.action_tarjeta:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayView(int viewId) {
        String title = null;

        switch (viewId) {
            case R.id.nav_home:
                if (!(fragment instanceof MapFragment)) {
                    fragment = new MapFragment().newInstance();
                    title = "Inicio";
                    disableCollapse();
                }
                break;
            case R.id.nav_profile:
                fragment = new MiCuentaFragment();
                title = "Mi Cuenta";
                enableCollapse();
                break;
            case R.id.nav_historial:
                title = "Historial";
                disableCollapse();
                break;
            case R.id.nav_prefs:
                fragment = new PreferenciasFragment();
                title = "Preferencias";
                disableCollapse();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = GlobalFunction.crearDialogYesNot(this, "Salir", "¿Desea salir de Mi Estacionamiento?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtToolbar.setText(title);
        }
        drawerLayout.closeDrawers();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
        invalidateOptionsMenu();
    }

    public void disableCollapse() {
        imageHeader.setVisibility(View.GONE);
        imageCircleHeader.setVisibility(View.GONE);
        txtHeaderNombre.setVisibility(View.GONE);
        txtHeaderTipo.setVisibility(View.GONE);
        collapsingToolbarLayout.setTitleEnabled(false);

    }

    public void enableCollapse() {
        imageHeader.setVisibility(View.VISIBLE);
        imageCircleHeader.setVisibility(View.VISIBLE);
        txtHeaderNombre.setVisibility(View.VISIBLE);
        txtHeaderTipo.setVisibility(View.VISIBLE);
        collapsingToolbarLayout.setTitleEnabled(true);
    }

    public Bitmap setImageBlurEffect(int drawable) {
        Bitmap image = BitmapFactory.decodeResource(getResources(),
                drawable);

        Bitmap blur = GlobalFunction.blurRenderScript(this, image, 25);

        return blur;
    }

}
