package br.com.smartfit.omdbapp.ui.listafavorito;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Favorito;
import br.com.smartfit.omdbapp.model.Item;
import br.com.smartfit.omdbapp.sqlite.ItemDao;

public class FavoritoAdapter extends ExpandableRecyclerAdapter<Favorito, Item, FavoritoAdapter.FavoritoViewHolder, FavoritoAdapter.FavoritoItemViewHolder> {

    private LayoutInflater mInflater;
    private List<Favorito> listaFavorito;
    private Context context;
    private ItemDao itemDao;

    FavoritoAdapter(@NonNull List<Favorito> listaFavorito, Context context, ItemDao itemDao) {
        super(listaFavorito);
        mInflater = LayoutInflater.from(context);
        this.listaFavorito = listaFavorito;
        this.context = context;
        this.itemDao = itemDao;
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View favoritoView = mInflater.inflate(R.layout.item_rv_favorito,parentViewGroup,false);
        return new FavoritoViewHolder(favoritoView);
    }

    @NonNull
    @Override
    public FavoritoItemViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View favoritoItemView = mInflater.inflate(R.layout.item_rv_item,childViewGroup,false);
        return new FavoritoItemViewHolder(favoritoItemView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull FavoritoViewHolder FavoritoViewHolder, final int parentPosition, @NonNull Favorito favorito) {
        FavoritoViewHolder.txtTipo.setText(favorito.getTipo());
    }

    @Override
    public void onBindChildViewHolder(@NonNull FavoritoItemViewHolder FavoritoItemViewHolder, int parentPosition, int childPosition, @NonNull Item item) {
        if (item.getImagem().equals("N/A")){
            FavoritoItemViewHolder.imgFoto.setImageResource(R.drawable.img_sem_foto);
        }else {
            FavoritoItemViewHolder.imgFoto.setImageURI(item.getImagem());
        }
        FavoritoItemViewHolder.txtTitulo.setText(item.getTitulo());

        FavoritoItemViewHolder.itemView.setOnClickListener((View v) -> {
            ListaFavoritoContrato.View view = (ListaFavoritoContrato.View) context;
            view.abrirDetalhesItemActivity(item.getId());
        });

        FavoritoItemViewHolder.imgRemover.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmação")
                    .setMessage("Tem certeza que deseja remover este item da lista de favoritos?")
                    .setPositiveButton("Remover", (DialogInterface dialog, int which) -> {
                        itemDao.removerFavorito(item.getId());
                        List<Item> listaItem = listaFavorito.get(parentPosition).getChildList();
                        listaItem.remove(childPosition);
                        notifyChildRemoved(parentPosition, childPosition);
                    })
                    .setNegativeButton("Cancelar", null)
                    .create()
                    .show();
        });
    }

    @Override
    public void setExpandCollapseListener(@Nullable ExpandCollapseListener expandCollapseListener) {
        super.setExpandCollapseListener(expandCollapseListener);

    }

//    @Override
//    public void notifyChildChanged(int parentPosition, int childPosition) {
//        super.notifyChildChanged(parentPosition, childPosition);
//    }

    class FavoritoViewHolder extends ParentViewHolder {

        private ImageView imgExpandir;
        private TextView txtTipo;

        FavoritoViewHolder(View view) {
            super(view);
            imgExpandir = view.findViewById(R.id.imgExpandir);
            txtTipo = view.findViewById(R.id.txtTipo);

            imgExpandir.setOnClickListener((View v) -> {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            });
        }

        @Override
        public void setExpanded(boolean isExpanded) {
            super.setExpanded(isExpanded);
            if (isExpanded) {
                imgExpandir.setBackgroundResource(R.drawable.ic_esconder);
            }else{
                imgExpandir.setBackgroundResource(R.drawable.ic_mostrar);
            }
        }
    }

    class FavoritoItemViewHolder extends ChildViewHolder {

        private SimpleDraweeView imgFoto;
        private TextView txtTitulo;
        private ImageView imgRemover;

        FavoritoItemViewHolder(final View view) {
            super(view);
            imgFoto = view.findViewById(R.id.imgFoto);
            txtTitulo = view.findViewById(R.id.txtTitulo);
            imgRemover = view.findViewById(R.id.imgRemover);
            imgRemover.setVisibility(View.VISIBLE);
        }
    }
}