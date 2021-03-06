package com.example.gerardo.miestacionamiento.view.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerardo.miestacionamiento.R;
import com.example.gerardo.miestacionamiento.controller.util.GlobalConstant;
import com.example.gerardo.miestacionamiento.controller.GlobalFunction;
import com.example.gerardo.miestacionamiento.controller.util.RunnableArgs;
import com.example.gerardo.miestacionamiento.model.Estacionamiento;
import com.example.gerardo.miestacionamiento.model.Evaluacion;
import com.example.gerardo.miestacionamiento.model.Usuario;
import com.example.gerardo.miestacionamiento.view.adapter.EvaluacionAdapter;
import com.example.gerardo.miestacionamiento.view.ui.InfoActivity;
import com.example.gerardo.miestacionamiento.view.ui.LoginActivity;
import com.example.gerardo.miestacionamiento.view.ui.MainActivity;
import com.example.gerardo.miestacionamiento.view.ui.dialog.EstanciaDialog;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 10/10/2016.
 */
public class DetalleFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {

    StreetViewPanorama mStreetView;
    SupportMapFragment fragmentStreetView;
    LatLng coordenadas;

    @Bind(R.id.txt_detalle_comuna)
    TextView mComuna;
    @Bind(R.id.txt_detalle_direccion)
    TextView mDireccion;
    @Bind(R.id.txt_detalle_tamaño)
    TextView mTamaño;
    @Bind(R.id.txt_detalle_numero)
    TextView mNumero;
    @Bind(R.id.txt_detalle_piso)
    TextView mPiso;
    @Bind(R.id.txt_detalle_camara_vigilancia)
    TextView mCamaraVigilancia;
    @Bind(R.id.txt_detalle_valor_hora)
    TextView mValorHora;
    @Bind(R.id.txt_detalle_nombre)
    TextView mNombre;
    @Bind(R.id.txt_detalle_apellido)
    TextView mApellido;
    @Bind(R.id.txt_detalle_correo)
    TextView mCorreo;
    @Bind(R.id.rating_detalle_valoracion)
    RatingBar mRatingBar;
    @Bind(R.id.btn_detalle_solicitar)
    TextView mBtnSolicitar;
    @Bind(R.id.detalle_scrollView)
    ScrollView mainScrollView;
    @Bind(R.id.transparent_image)
    ImageView transparentImageView;
    @Bind(R.id.fragment_detalle_rv)
    RecyclerView recyclerComentarios;
    @Bind(R.id.fragment_Detalle_nocoment)
    TextView txtNoComent;

    String rutUsuario;
    int idEstacio;
    int estadoEstacionamiento;

    public static DetalleFragment newInstance(LatLng coordenadas, String rutUsuario, Integer IdEst) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle b = new Bundle();
        b.putDouble("latitud", coordenadas.latitude);
        b.putDouble("longitud", coordenadas.longitude);
        if (rutUsuario != null) {
            b.putString(GlobalConstant.BUNDLE_RUT_USUARIO, rutUsuario);
        }
        if (IdEst != null) {
            b.putInt(GlobalConstant.BUNDLE_ID_ESTACIO, IdEst);
        }
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        Double la = args.getDouble("latitud");
        Double lo = args.getDouble("longitud");
        coordenadas = new LatLng(la, lo);
        rutUsuario = args.getString(GlobalConstant.BUNDLE_RUT_USUARIO, "");
        idEstacio = args.getInt(GlobalConstant.BUNDLE_ID_ESTACIO, 0);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
//        View fragmentView = super.onCreateView(layoutInflater, viewGroup, bundle);
        View root = layoutInflater.inflate(R.layout.fragment_detalle, viewGroup, false);
        ButterKnife.bind(this, root);
//        getStreetViewPanoramaAsync(this);
        setContentViews();


        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment) getChildFragmentManager()
                        .findFragmentById(R.id.street_view);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        setRecyclerItems();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Información");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTouchEvent();

    }

    @OnClick(R.id.btn_detalle_solicitar)
    public void solicitar() {
//        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                .replace(R.id.frame,EstanciaFragment.newInstance(rutUsuario,idEstacio))
//                .commitAllowingStateLoss();
        if (estadoEstacionamiento == 1) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            EstanciaDialog dialog = EstanciaDialog.newInstance(rutUsuario, idEstacio);
            dialog.show(ft, "ftEstancia");
        } else {
            Toast.makeText(getActivity(), "Estacionamiento no disponible", Toast.LENGTH_SHORT).show();
        }

    }

    private void setContentViews() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Usuario usuario = realm.where(Usuario.class).equalTo("rutUsuario", rutUsuario).findFirst();
        Estacionamiento est = realm.where(Estacionamiento.class).equalTo("idEstacionamiento", idEstacio).findFirst();
        realm.commitTransaction();


        if (est != null) {
            estadoEstacionamiento = est.getIdEstado();
            mComuna.setText(GlobalFunction.getComunaNombrebyID(est.getIdComuna()));
            mDireccion.setText(est.getDireccionEstacionamiento());
            mTamaño.setText("Normal");
            if (est.getNumeroEst() != 0 && est.getNumeroEst() != null) {
                mNumero.setText(String.valueOf(est.getNumeroEst()));
            } else {
                mNumero.setText("N/A");
            }
            if (est.getPisoUbicacion() != 0 && est.getPisoUbicacion() != null) {
                mPiso.setText(String.valueOf(est.getPisoUbicacion()));
            } else {
                mPiso.setText("N/A");
            }
            if (est.getCamaraVigilancia() == 1) {
                mCamaraVigilancia.setText("Sí");
            } else {
                mCamaraVigilancia.setText("No");
            }
            mValorHora.setText(String.valueOf(est.getCostoHora()));
        }
        if (usuario != null) {
            mNombre.setText(usuario.getNombre());
            mApellido.setText(getActivity().getResources().getString(R.string.resumenApellido,
                    usuario.getApellidoPaterno(), usuario.getApellidoMaterno()));
            mCorreo.setText(usuario.getCorreo());
        }

    }

    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama panorama) {

        panorama.setPosition(coordenadas);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (panorama.getLocation() == null) {
                    Toast.makeText(getActivity(), "Conexión lenta detectada, por favor espere mientras carga Street View", Toast.LENGTH_LONG).show();
                }
            }
        }, 3000);


    }

    private void setTouchEvent() {
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        SupportStreetViewPanoramaFragment fragment = (SupportStreetViewPanoramaFragment) getChildFragmentManager().findFragmentById(R.id.street_view);
//        if (fragment!=null){
//            FragmentTransaction ft = getActivity().getSupportFragmentManager()
//                    .beginTransaction();
//            ft.remove(fragment);
//            ft.commit();
//        }
        if (fragment.isResumed()) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }


    private void setRecyclerItems() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Evaluacion> evaluacionList = realm.where(Evaluacion.class).equalTo("idEstacionamiento", idEstacio).findAll();
        realm.commitTransaction();
        Integer[] notas = new Integer[evaluacionList.size()];
        for (int i = 0; i < evaluacionList.size(); i++) {
            notas[i] = evaluacionList.get(i).getCalificacion();
        }
        mRatingBar.setRating(GlobalFunction.getPromedio(notas));
        if (evaluacionList.size() == 0) {
            txtNoComent.setVisibility(View.VISIBLE);
            recyclerComentarios.setVisibility(View.GONE);
        }
        EvaluacionAdapter adapter = new EvaluacionAdapter(getContext(), evaluacionList);
        recyclerComentarios.setAdapter(adapter);
    }

}
