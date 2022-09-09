package com.unl.ute.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unl.ute.R;
import com.unl.ute.sw.modelos.PersonaJS;

import java.util.List;

public class ListaAdaptadorPersona extends ArrayAdapter<PersonaJS> {

    private Context context;
    private List<PersonaJS> lista;
    public ListaAdaptadorPersona(Context context, List<PersonaJS> lista) {
        super(context, R.layout.persona_item, lista);
        this.context = context;
        this.lista=lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.persona_item, null);
        final PersonaJS pPersonaJS = lista.get(position);
        TextView txtNombres = item.findViewById(R.id.nombres);
        TextView txtApellidos = item.findViewById(R.id.apellidos);
        TextView txtCelular = item.findViewById(R.id.telefono);
        TextView txtDNI = item.findViewById(R.id.DNI);
        TextView txtDireccion = item.findViewById(R.id.direccion);
        TextView txtExtId = item.findViewById(R.id.extId);


        txtNombres.setText(pPersonaJS.getNombres());
        txtApellidos.setText(pPersonaJS.getApellidos());
        txtCelular.setText(pPersonaJS.getCelular());
        txtDNI.setText(pPersonaJS.getIdentificacion());
        txtDireccion.setText(pPersonaJS.getDireccion());
        txtExtId.setText(pPersonaJS.getExternal_id());
        return item;
    }
    
}
