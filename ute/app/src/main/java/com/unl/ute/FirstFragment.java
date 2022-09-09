package com.unl.ute;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unl.ute.adaptadores.ListaAdaptadorPersona;
import com.unl.ute.databinding.FragmentFirstBinding;
import com.unl.ute.sw.Peticiones;
import com.unl.ute.sw.VolleyRequest;
import com.unl.ute.sw.modelos.ListPersonaJS;
import com.unl.ute.sw.modelos.PersonaJS;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ListaAdaptadorPersona listaAdaptadorPersona;
    private ListView listaPersona;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        listaAdaptadorPersona = new ListaAdaptadorPersona(getContext(), new ArrayList<PersonaJS>());
        listaPersona = view.findViewById(R.id.listapersona);
        listaPersona.setEmptyView(view.findViewById(R.id.listaVaciaPersona));
        cargarPersona();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void cargarPersona(){
        VolleyRequest<ListPersonaJS> peticion = Peticiones.getListaPersonas(getContext(), new Response.Listener<ListPersonaJS>() {
            @Override
            public void onResponse(ListPersonaJS response) {
                if(response.getCode() == 200){
                    listaAdaptadorPersona = new ListaAdaptadorPersona(getContext(), response.getData());
                    listaPersona.setAdapter(listaAdaptadorPersona);
                }
            }
        },
        new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){
                System.out.println("Error: "+ error);
            }
        }) ;
        requestQueue.add(peticion);
    }
}