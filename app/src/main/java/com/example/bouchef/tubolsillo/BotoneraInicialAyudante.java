package com.example.bouchef.tubolsillo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bouchef.tubolsillo.api.APIService;
import com.example.bouchef.tubolsillo.api.Api;
import com.example.bouchef.tubolsillo.api.model.CompraViewModelResponse;
import com.example.bouchef.tubolsillo.api.model.MensajeViewModelPOST;
import com.example.bouchef.tubolsillo.api.model.MensajeViewModelResponse;
import com.example.bouchef.tubolsillo.api.model.UsuarioViewModelPOST;
import com.example.bouchef.tubolsillo.api.model.UsuarioViewModelResponse;
import com.example.bouchef.tubolsillo.generics.ApplicationGlobal;
import com.example.bouchef.tubolsillo.utiles.Alerts;
import com.example.bouchef.tubolsillo.utiles.FechaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by World of UI/UX on 01/04/2019.
 */

public class BotoneraInicialAyudante extends AppCompatActivity {
    private Context mContext= BotoneraInicialAyudante.this;
    private APIService api;

//    @BindView(R.id.descripcion) TextView descripcion;
//    @BindView(R.id.fechaAlta) TextView fechaAlta;
//    @BindView(R.id.autorizarButton)
//    ImageButton autorizarButton;
//
//    @BindView(R.id.info)
//    ImageView imageInfo;


    private Integer idTipoEvento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botonera_inicial_ayudante);

        ButterKnife.bind(this);
        ApplicationGlobal applicationGlobal = ApplicationGlobal.getInstance();

        api = Api.getAPIService(getApplicationContext());

        UsuarioViewModelPOST usuarioViewModelPOST = new UsuarioViewModelPOST();
        usuarioViewModelPOST.setId(1);
        usuarioViewModelPOST.setNombre("");
        api.getUsuario(usuarioViewModelPOST).enqueue(new Callback<UsuarioViewModelResponse>() {
            @Override
            public void onResponse(Call<UsuarioViewModelResponse> call, Response<UsuarioViewModelResponse> response) {
                if(response.isSuccessful()){
                    cargarUsuarioGlobal(applicationGlobal, response.body());
                }else{
                    Alerts.newToastLarge(mContext, "ERR");
                }
            }

            @Override
            public void onFailure(Call<UsuarioViewModelResponse> call, Throwable t) {
                Alerts.newToastLarge(mContext, "ErrErr");
            }
        });

        api.getCompraVigente(applicationGlobal.getUsuario().getId()).enqueue(new Callback<CompraViewModelResponse>() {
            @Override
            public void onResponse(Call<CompraViewModelResponse> call, Response<CompraViewModelResponse> response) {
                if(response.isSuccessful()){
                    applicationGlobal.setCompra(response.body());
                }else{
                    if (response.code() != 404) {
                        Alerts.newToastLarge(mContext, "ERR");
                    }
                    else
                    {
                        //applicationGlobal.setCompra(null);
                    }
                }

            }

            @Override
            public void onFailure(Call<CompraViewModelResponse> call, Throwable t) {
                Alerts.newToastLarge(getApplicationContext(), "Err");

            }
        });

        MensajeViewModelPOST mensajeViewModelPOST = new MensajeViewModelPOST();
        mensajeViewModelPOST.setIdUsuario(1);
        mensajeViewModelPOST.setIdCompra(0);
        mensajeViewModelPOST.setIdTipoEvento(4);

        api.getUltimoMensaje(mensajeViewModelPOST.getIdCompra(),mensajeViewModelPOST.getIdUsuario(),mensajeViewModelPOST.getIdTipoEvento()).enqueue(new Callback<MensajeViewModelResponse>() {
            @Override
            public void onResponse(Call<MensajeViewModelResponse> call, Response<MensajeViewModelResponse> response) {
                if(response.isSuccessful()){
                    //*Alerts.newToastLarge(mContext, "OK");*/
                    //cargarUltimoMensaje(response.body());
                }else{
                    if (response.code() != 404) {
                        Alerts.newToastLarge(mContext, "ERR");
                    }
                    else
                    {
                        //cargarUltimoMensaje(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MensajeViewModelResponse> call, Throwable t) {
                Alerts.newToastLarge(mContext, "ErrErr");
            }
        });

        Button btn1 = (Button) findViewById(R.id.button_autorizar);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), AutorizarTutor.class);

                startActivityForResult(intent, 0);
            }
        });

        Button btn2 = (Button) findViewById(R.id.button_seguir_compra);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), SeguimientoCompra.class);
                startActivityForResult(intent, 0);
            }
        });
        /*Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), InicioPCD.class);
                startActivityForResult(intent, 0);
            }
        });*/
        Button btn6 = (Button) findViewById(R.id.button_bandeja_mensajes);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), HistorialTutor.class);
                startActivityForResult(intent, 0);
            }
        });

        Button btn5 = (Button) findViewById(R.id.button_control_de_gastos);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), ListaControlGastos.class);
                startActivityForResult(intent, 0);
            }
        });

        Button btn7 = (Button) findViewById(R.id.button_enviar_mensajes);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), NotificadorPCD.class);
                startActivityForResult(intent, 0);
            }
        });

        /*Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), LocationActivityAmigable.class);
                startActivityForResult(intent, 0);
            }
        });*/

        /*Button btn8 = (Button) findViewById(R.id.button8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), SplashScreen.class);
                startActivityForResult(intent, 0);
            }
        });*/

        Button btn9 = (Button) findViewById(R.id.button_perfil_usuario);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), BotoneraInicialPCD.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });

//        autorizarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), AutorizarTutor.class);
//                startActivityForResult(intent, 0);
//            }
//        });

    }
//    private void cargarUltimoMensaje(MensajeViewModelResponse mensaje){
//        String t = FechaUtils.fromStringToVerbose(mensaje.getFechaAlta());
//
//        descripcion.setText(mensaje.getDescripcion());
//        fechaAlta.setText(t);
//
//        idTipoEvento = mensaje.getOrdenImportancia();
//        if(idTipoEvento.equals(3)){
//            autorizarButton.setVisibility(View.VISIBLE);
//        }else {
//            imageInfo.setVisibility(View.VISIBLE);
//        }
//    }

    private void cargarUsuarioGlobal(ApplicationGlobal global, UsuarioViewModelResponse usuario) {
        global.setUsuario(usuario);
    }
}