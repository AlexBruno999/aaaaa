package com.example.aplicativodehqs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class PesquisarHQ extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewHQs;
    public SearchView searchView;
    public ArrayAdapter<String> meuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_hq);

        listViewHQs = (ListView) findViewById(R.id.listViewHQs);
        searchView = findViewById(R.id.search_bar);
        criarBancoDados1();
        meuAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new ArrayList<>()
        );
        listViewHQs.setAdapter(meuAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                meuAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                meuAdapter.getFilter().filter(newText);
                return false;
            }
        });
        pesquisarDados();
    }
    public void pesquisarDados(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome, numero FROM HQ_new", null);
            ArrayList<String> linhas = new ArrayList<>();

            meuCursor.moveToFirst();
            while (!meuCursor.isAfterLast()) {
                String nome = (meuCursor.getString(1));
                String numero = meuCursor.getString(2);
                linhas.add(nome + " - " + numero);
                meuCursor.moveToNext();
            }

            meuAdapter.addAll(linhas);
            meuAdapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void criarBancoDados1(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS HQ_new(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR " +
                    ", ano VARCHAR " +
                    ", licenciador VARCHAR " +
                    ", genero VARCHAR " +
                    ", numero INTEGER" +
                    ", idColecao INTEGER," +
                    "FOREIGN KEY (idColecao) REFERENCES colecao(id))");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}