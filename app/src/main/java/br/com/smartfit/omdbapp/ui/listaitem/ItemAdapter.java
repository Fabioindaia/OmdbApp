package br.com.smartfit.omdbapp.ui.listaitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.com.smartfit.omdbapp.R;
import br.com.smartfit.omdbapp.model.Item;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> listaItem;
    private Context context;
    private final int VIEW_TYPE_LOADING = 0;
    private int totalRegistro;

    ItemAdapter(List<Item> listaItem, Context context) {
        this.listaItem = listaItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_carregando, parent, false));
        }else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_ITEM = 1;
        return position == listaItem.size() - 1 && position < totalRegistro - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listaItem.size();
    }

    void setTotalRegistro(int totalRegistro){
        this.totalRegistro = totalRegistro;
    }

    class ViewHolder extends ItemViewHolder {

        private SimpleDraweeView imgFoto;
        private TextView txtTitulo;

        ViewHolder(View view) {
            super(view);
            imgFoto = view.findViewById(R.id.imgFoto);
            txtTitulo = view.findViewById(R.id.txtTitulo);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Item item = listaItem.get(position);
            if (item.getImagem().equals("N/A")){
                imgFoto.setImageResource(R.drawable.img_sem_foto);
            }else {
                imgFoto.setImageURI(item.getImagem());
            }
            txtTitulo.setText(item.getTitulo());

            itemView.setOnClickListener((View v) -> {
                MainContrato.View view = (MainContrato.View) context;
                view.abrirDetalhesItemActivity(item.getId());
            });
        }
    }

    public class LoadingViewHolder extends ItemViewHolder {

        @BindView(R.id.pbCarregando)
        ProgressBar pbCarregando;

        LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        protected void clear() {

        }
    }

    public abstract class ItemViewHolder extends RecyclerView.ViewHolder{

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void clear();

        public void onBind(int position) {
            clear();
        }

    }
}
