package com.example.aplicacionrealm;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aplicacionrealm.Model.Empleat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListBusquedaFragment extends MyFragment {
    private String dadeAbuscar;

    public ListBusquedaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_busqueda, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView empleatsRecyclerView = view.findViewById(R.id.itemList2);

        appViewModel.dato.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dadeAbuscar = s;
                System.out.println("la ossssssssssssssssssssssssssssssssssss...........................................ssssssssssssssssssssss  " + s );
            }
        });
        System.out.println("dades a buscar list Busqueda... .................................   "+ dadeAbuscar);
        RealmResults<Empleat> resultat = realm.where(Empleat.class)
                .equalTo("nom", dadeAbuscar)
                .findAll();

        System.out.println("resultatt: .............................................    "+ resultat.toString());

        empleatsRecyclerView.setAdapter(new ListarAdapter(resultat, true));
    }
    public class ListarAdapter extends RealmRecyclerViewAdapter<Empleat, ListarAdapter.MyHolder> {

        public ListarAdapter(@Nullable OrderedRealmCollection<Empleat> data, boolean autoUpdate) {
            super(data, autoUpdate);
        }
        @NonNull
        @Override
        public ListarAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_empleats, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListarAdapter.MyHolder holder, final int position) {

            holder.id.setText(String.valueOf(getData().get(position).getId()));
            holder.nom.setText(getData().get(position).getNom());
            holder.cognoms.setText(getData().get(position).getCognoms());
            holder.categoria.setText(getData().get(position).getGetCategoria());
            holder.edad.setText(String.valueOf(getData().get(position).getEdad()));
            holder.antiguetat.setText(String.valueOf(getData().get(position).getAntiguetat()));

            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    eliminarDades(position);
                    new AlertDialog.Builder(requireContext()).setTitle("\t\tEl·liminat")
                            .setMessage("\t      ")
                            .setMessage("\t      ")
                            .setCancelable(true)
                            .create()
                            .show();
                }
            });
            appViewModel.modificar.setValue(false);
            holder.btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.idSeleccion.postValue(getData().get(position).getId());
                    appViewModel.modificar.setValue(true);
                    System.out.println("aquii mas lejos -..........................................................  " + appViewModel.modificar.getValue().toString());
                    navController.navigate(R.id.insertarFragment);
                }
            });
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView id, nom, cognoms, categoria, edad, antiguetat;
            Button btnEliminar, btnModificar;

            public MyHolder(@NonNull View view) {
                super(view);
                id = view.findViewById(R.id.textid);
                nom = view.findViewById(R.id.textnom);
                cognoms = view.findViewById(R.id.textcognoms);
                categoria = view.findViewById(R.id.textcategoria);
                edad = view.findViewById(R.id.textedad);
                antiguetat = view.findViewById(R.id.textantiguetat);
                btnEliminar = view.findViewById(R.id.eliminarButton);
                btnModificar = view.findViewById(R.id.modificarButton);
            }
        }
        void eliminarDades(Integer pos){
            RealmResults<Empleat> empleat = realm.where(Empleat.class).findAll();
            realm.beginTransaction();
            empleat.get(pos).deleteFromRealm();
            realm.commitTransaction();
        }
    }
}