package col10dejunio.lldm.asistenciaslldm.view.adapters;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import col10dejunio.lldm.asistenciaslldm.R;
import col10dejunio.lldm.asistenciaslldm.model.ContratoAsistencia;
import col10dejunio.lldm.asistenciaslldm.view.MemberFragment.OnListFragmentInteractionListener;

import col10dejunio.lldm.asistenciaslldm.view.items.MemberItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MemberItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private final static String TAG = MemberAdapter.class.getSimpleName();
    private final static int VIEW_TYPE_ITEM = 1;
    private final static int VIEW_TYPE_HEADER = 2;
    private HashMap<Integer, String> initials;
    private int mPosition = 0;
    private Cursor mCursor;


    public MemberAdapter(Cursor items, OnListFragmentInteractionListener listener) {
        initials = new HashMap<>();
        mCursor = items;
        mListener = listener;

    }

    private void fillHolders() {
        initials.clear();
        mCursor.moveToPosition(-1);
        while (mCursor.moveToNext()) {
            MemberItem memberItem = cursorToMember(mCursor);
            String inicial = memberItem.getNombre().substring(0, 1).toUpperCase();
            if (!initials.containsValue(inicial))
                initials.put(mPosition++, inicial);
            initials.put(mPosition++, mCursor.getPosition() + "");
        }
    }

    @Nullable
    public Cursor swapCursor(Cursor cursor) {
        Log.i(TAG,"swapCursor");
        if (this.mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = cursor;
        this.mCursor = cursor;
        if (cursor != null) {
            fillHolders();
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.member_item, parent, false);
                return new ItemViewHolder(view);
            case VIEW_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_item, parent, false);
                return new ItemHeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "Position : " + position);
        if (holder instanceof ItemViewHolder) {
            mCursor.moveToPosition(Integer.valueOf(initials.get(position)));
            MemberItem memberItem = cursorToMember(mCursor);
            final ItemViewHolder mHolder = (ItemViewHolder) holder;
            mHolder.mItem = memberItem;
            Log.i(TAG, "Is ItemViewHolder -- Id : " + memberItem.getId());
            mHolder.mNombreView.setText(memberItem.Nombre + " " + memberItem.APaterno + " " + memberItem.AMaterno);
            mHolder.mGrupoView.setText(memberItem.getGrupo());
            mHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(mHolder.mItem);
                    }
                }
            });
            //Generamos la inicial que pondremos en la imagen circular
            String inicial = memberItem.getNombre().substring(0, 1).toUpperCase();
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(inicial.toUpperCase(), color);
            //seteamos la imagen de nuestro item actual
            mHolder.mInicialView.setImageDrawable(drawable);
        }
        if (holder instanceof ItemHeaderViewHolder) {
            Log.i(TAG, "Is ItemHeaderViewHolder");
            final ItemHeaderViewHolder mHolder = (ItemHeaderViewHolder) holder;
            mHolder.mTitleView.setText(initials.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (initials.get(position).matches("[A-Z]"))
            return VIEW_TYPE_HEADER;
        else
            return VIEW_TYPE_ITEM;
    }


    private MemberItem cursorToMember(Cursor cursor) {
        return new MemberItem(
                cursor.getLong(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro._ID)),
                cursor.getString(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro.NOMBRE)),
                cursor.getString(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro.A_PATERNO)),
                cursor.getString(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro.A_MATERNO)),
                cursor.getString(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro.GRUPO)),
                cursor.getLong(cursor.getColumnIndex(ContratoAsistencia.ColumnasMiembro.ID_LISTA))
        );
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return initials.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreView;
        public final TextView mGrupoView;
        public final ImageView mInicialView;
        public MemberItem mItem;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            mInicialView = (ImageView) view.findViewById(R.id.foto_perfil);
            mNombreView = (TextView) view.findViewById(R.id.nombre_txv);
            mGrupoView = (TextView) view.findViewById(R.id.grupo_txv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreView.getText() + "'";
        }
    }

    public class ItemHeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;

        public ItemHeaderViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title_header);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
