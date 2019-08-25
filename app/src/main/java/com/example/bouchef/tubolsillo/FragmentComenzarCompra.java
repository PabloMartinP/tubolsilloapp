package com.example.bouchef.tubolsillo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchef.tubolsillo.adapter.DashboardAdapter;
import com.example.bouchef.tubolsillo.adapter.LenguajeListAdapter;
import com.example.bouchef.tubolsillo.api.APIService;
import com.example.bouchef.tubolsillo.api.Api;
import com.example.bouchef.tubolsillo.api.model.ComercioViewModelPOST;
import com.example.bouchef.tubolsillo.api.model.ComercioViewModelResponse;
import com.example.bouchef.tubolsillo.api.model.CompraViewModelPOST;
import com.example.bouchef.tubolsillo.api.model.CompraViewModelResponse;
import com.example.bouchef.tubolsillo.api.model.IdResponse;
import com.example.bouchef.tubolsillo.api.model.MensajeViewModelPOST;
import com.example.bouchef.tubolsillo.api.model.MensajeViewModelResponse;
import com.example.bouchef.tubolsillo.generics.ApplicationGlobal;
import com.example.bouchef.tubolsillo.generics.Progress;
import com.example.bouchef.tubolsillo.model.dashboard;
import com.example.bouchef.tubolsillo.tests.ComerciosRecyclerViewAdapter;
import com.example.bouchef.tubolsillo.tests.HistorialMensajesRecyclerViewAdapter;
import com.example.bouchef.tubolsillo.tests.ItemBasico;
import com.example.bouchef.tubolsillo.tests.MensajesRecyclerViewAdapter;
import com.example.bouchef.tubolsillo.utiles.Alerts;
import com.example.bouchef.tubolsillo.utiles.FechaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bouchef.tubolsillo.R.layout.activity_negocios_favoritos;

public class FragmentComenzarCompra extends Fragment {
    private Context mContext = getContext();
    FragmentActivity activity = (FragmentActivity) mContext;


    private APIService api;

    ApplicationGlobal applicationGlobal = ApplicationGlobal.getInstance();

    @BindView(R.id.mi_lista) ListView list;
    @BindView(R.id.empty_state_container) LinearLayout lista_vacia;
    RecyclerView recyclerListaMensajes;
    ComerciosRecyclerViewAdapter adapter;
    List<ItemBasico> items;

    boolean fragmentTransaction = false;
    Fragment fragment = null;

    private ListView lista;

    private String lenguajeProgramacion[]=new String[]{"FAVORITO 1","FAVORITO 2","FAVORITO 3","FAVORITO 4","FAVORITO 5"};
    private Integer[] imgid={
            R.drawable.restaurant,
            R.drawable.restaurant,
            R.drawable.restaurant,
            R.drawable.restaurant,
            R.drawable.restaurant
    };

    public FragmentComenzarCompra() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View vista = inflater.inflate(R.layout.activity_lista_negocios_favoritos, container, false);
        api = Api.getAPIService(mContext);

        lista_vacia = (LinearLayout) vista.findViewById(R.id.empty_state_container);
        lista_vacia.setVisibility((View.GONE));

        //items=new ArrayList<>();
        //recyclerListaMensajes = (RecyclerView) vista.findViewById(R.id.mi_lista);
        //recyclerListaMensajes.setLayoutManager(new LinearLayoutManager(getContext()));



        LenguajeListAdapter adapter=new LenguajeListAdapter((Activity) getContext(),lenguajeProgramacion,imgid);
        lista=(ListView) vista.findViewById(R.id.mi_lista);

        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Slecteditem= lenguajeProgramacion[+position];
                //enviar mensaje ("inicio de compra")
                //enviar mensaje ("Me difirijo al comercio XXXXXX")
                //Me guardo el Comercio para las etapas siguientes
                ComercioViewModelPOST comercioViewModelPOST = new ComercioViewModelPOST();
                comercioViewModelPOST.setId(position + 1);
                comercioViewModelPOST.setIdUsuario(0);
                comercioViewModelPOST.setNombre("");
                api.getComercios(comercioViewModelPOST).enqueue(new Callback<List<ComercioViewModelResponse>>() {
                    @Override
                    public void onResponse(Call<List<ComercioViewModelResponse>> call, Response<List<ComercioViewModelResponse>> response) {
                        if(response.isSuccessful()){
                            cargarComercioGlobal(response.body().get(0),applicationGlobal);
                        }else{
                            Alerts.newToastLarge(mContext, "ERR");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComercioViewModelResponse>> call, Throwable t) {
                        Alerts.newToastLarge(mContext, "ErrErr");
                    }
                });

                CompraViewModelPOST compraViewModelPOST = new CompraViewModelPOST();
                compraViewModelPOST.setIdUsuario(applicationGlobal.getUsuario().getId());
                compraViewModelPOST.setIdComercio(position + 1);
                compraViewModelPOST.setCompraReal(false);

                api.nuevaCompra(compraViewModelPOST).enqueue(new Callback<IdResponse>() {
                    @Override
                    public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                        if(response.isSuccessful()){
                            cargarCompra((response.body().getId()),applicationGlobal);
                        }else{
                            Alerts.newToastLarge(getContext(), "Err");
                        }

                    }

                    @Override
                    public void onFailure(Call<IdResponse> call, Throwable t) {
                        Alerts.newToastLarge(getContext(), "Err");

                    }
                });

                Toast.makeText(getContext(), "ME DIRIJO A " + Slecteditem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (view.getContext(), NotificadorPCD.class);
                startActivityForResult(intent, 0);

            }
        });

        //ComerciosRecyclerViewAdapter adapter = new ComerciosRecyclerViewAdapter(getContext(), items);

        //recyclerListaMensajes.setAdapter(adapter);

        return vista;
    }

    private void cargarComercioGlobal(ComercioViewModelResponse comercio, ApplicationGlobal applicationGlobal) {
        applicationGlobal.setComercio(comercio);
    }
    private void cargarCompraGlobal(CompraViewModelResponse compra, ApplicationGlobal applicationGlobal) {
        applicationGlobal.setCompra(compra);
    }

    private void cargarCompra(Integer idCompra, ApplicationGlobal global) {
        //Consultar nueva compra
        CompraViewModelPOST nuevaCompra = new CompraViewModelPOST();
        nuevaCompra.setId(idCompra);
        nuevaCompra.setIdUsuario(global.getUsuario().getId());

        //Cargarla en Global
        api.getCompras(nuevaCompra).enqueue(new Callback<List<CompraViewModelResponse>>() {
            @Override
            public void onResponse(Call<List<CompraViewModelResponse>> call, Response<List<CompraViewModelResponse>> response) {
                if(response.isSuccessful()){
                    cargarCompraGlobal(response.body().get(0),global);
                }else{
                    Alerts.newToastLarge(mContext, "ERR");
                }
            }

            @Override
            public void onFailure(Call<List<CompraViewModelResponse>> call, Throwable t) {
                Alerts.newToastLarge(mContext, "ErrErr");
            }
        });
    }

    private void populateList(Context context){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerListaMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
        HistorialMensajesRecyclerViewAdapter adapter = new HistorialMensajesRecyclerViewAdapter(context, items);

        // oculto segun resultado de la lista
        if(items.isEmpty()){
            lista_vacia.setVisibility(View.VISIBLE);
            recyclerListaMensajes.setVisibility((View.GONE));
        }else{
            lista_vacia.setVisibility((View.GONE));
            recyclerListaMensajes.setVisibility(View.VISIBLE);
        }
        adapter.setClickListener((HistorialMensajesRecyclerViewAdapter.DatoBasicoItemClickListener) this);
        recyclerListaMensajes.setAdapter(adapter);

    }



}
