package com.example.aplicativodehqs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlterarColecaoActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public Button btnAlterar;
    public EditText edtNome;
    public EditText edtDesc;
    public Button btnExcluir;

    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_colecao);

        btnAlterar = (Button) findViewById(R.id.btnAlterar);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        Intent intent = getIntent();
        id = intent.getIntExtra("colecao_id", 0);

        carregarDados();

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });


        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluir();
            }
        });


    }

    public void carregarDados(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, nome, descricao FROM colecao WHERE id = " +id.toString(), null);
            cursor.moveToFirst();
            edtNome.setText(cursor.getString(1));
            edtDesc.setText(cursor.getString(2));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void alterar(){
        String valueNome;
        String valueDesc;

        valueNome = edtNome.getText().toString();
        valueDesc = edtDesc.getText().toString();

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE colecao SET nome=?, autor=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueNome);
            stmt.bindString(2, valueDesc);
            stmt.bindLong(3, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    public void excluir() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sqlDelete = "DELETE FROM colecao WHERE id = ?";
            SQLiteStatement stmtDelete = bancoDados.compileStatement(sqlDelete);
            stmtDelete.bindLong(1, id);
            stmtDelete.executeUpdateDelete();

            String sqlDelete2 = "DELETE FROM HQ WHERE idColecao = ?";
            SQLiteStatement stmtDelete2 = bancoDados.compileStatement(sqlDelete2);
            stmtDelete2.bindLong(1, id);
            stmtDelete2.executeUpdateDelete();

            String sqlUpdate = "UPDATE colecao SET id = id - 1 WHERE id > ?";
            SQLiteStatement stmtUpdate = bancoDados.compileStatement(sqlUpdate);
            stmtUpdate.bindLong(1, id);
            stmtUpdate.executeUpdateDelete();

            String sqlUpdate2 = "UPDATE sqlite_sequence SET SEQ = ? - 1 WHERE NAME='colecao'";
            SQLiteStatement stmtUpdate2 = bancoDados.compileStatement(sqlUpdate2);
            stmtUpdate2.bindLong(1, id);
            stmtUpdate2.executeUpdateDelete();

            String sqlUpdate3 = "UPDATE HQ SET idColecao = idColecao - 1 WHERE idColecao > ?";
            SQLiteStatement stmtUpdate3 = bancoDados.compileStatement(sqlUpdate3);
            stmtUpdate3.bindLong(1, id);
            stmtUpdate3.executeUpdateDelete();



            bancoDados.close();

            Toast.makeText(this, "Coleção excluída com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}