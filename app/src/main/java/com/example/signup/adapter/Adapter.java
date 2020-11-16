package com.example.signup.adapter;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.signup.Model.model1;
import com.example.signup.R;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
int flag=0;
    private Context context;
    private ArrayList<model1> modelsAll = new ArrayList<>();

    public Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final model1 model = modelsAll.get(position);
        holder.textView.setText(model.name);

        holder.itemView.setTag(R.string.MODEL, model);
        holder.itemView.setTag(R.string.position, position);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.rlContent.getLayoutParams();
        layoutParams.setMargins(((int) convertDpToPixel(20, context)) * model.level, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

        switch (model.state) {

            case CLOSED:
                holder.imgArrow.setImageResource(R.drawable.right);
                break;
            case OPENED:
                holder.imgArrow.setImageResource(R.drawable.down);
                break;
        }

        if (model.models.isEmpty()) {
            holder.imgArrow.setVisibility(View.INVISIBLE);
            holder.viewDashed.setVisibility(View.VISIBLE);
        } else {
            holder.imgArrow.setVisibility(View.VISIBLE);
            holder.viewDashed.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (int) v.getTag(R.string.position);
                model1 rootModel = (model1) v.getTag(R.string.MODEL);
                if (rootModel.models.isEmpty()) {
for(int i=0;i<modelsAll.size();i++){
    if(modelsAll.get(i).models.isEmpty())
        modelsAll.get(i).setSelected(false);
}
                    model.setSelected(true);


                    return;
                }
                switch (rootModel.state) {

                    case CLOSED:

                        modelsAll.addAll(position + 1, rootModel.models);
                        notifyItemRangeInserted(position + 1, rootModel.models.size());
                        notifyItemRangeChanged(position + rootModel.models.size(), modelsAll.size() - (position + rootModel.models.size()));
                        notifyItemRangeChanged(position, modelsAll.size() - position);
                        if(modelsAll.indexOf(rootModel)==0) {
                            for(int i=0;i<modelsAll.size();i++){
                                if(modelsAll.get(i).models.isEmpty())
                                    modelsAll.get(i).setSelected(false);
                            }
                            modelsAll.get(position + 1).setSelected(false);
                        }
                        else{
                            for(int i=0;i<modelsAll.size();i++){
                                if(modelsAll.get(i).models.isEmpty())
                                    modelsAll.get(i).setSelected(false);
                            }

                           modelsAll.get(position + 1).setSelected(false);

                        }

                        rootModel.state = model1.STATE.OPENED;
                        model.setSelected(true);

                        break;

                    case OPENED:

                        int start = position + 1;
                        int end = modelsAll.size();
                        for (int i = start; i < modelsAll.size(); i++) {
                            model1 model1 = modelsAll.get(i);
                            if (model1.level <= rootModel.level) {
                                end = i;
                                break;
                            } else {
                                if (model1.state == com.example.signup.Model.model1.STATE.OPENED) {
                                    model1.state = com.example.signup.Model.model1.STATE.CLOSED;
                                }
                            }
                        }

                        if (end != -1) {
                            modelsAll.subList(start, end).clear();
                            notifyItemRangeRemoved(start, end - start);
                            notifyItemRangeChanged(start, end - start);
                            notifyItemRangeChanged(position, modelsAll.size() - position);
                        }

                        rootModel.state = model1.STATE.CLOSED;
                        model.setSelected(false);

                        break;
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return modelsAll.size();
    }

    public void setData(ArrayList<model1> list) {
        modelsAll = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlContent;
        TextView textView;
        ImageView imgArrow;
        View viewDashed;
String cat;
        public ViewHolder(View itemView) {

            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvName);
            imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.rlContent);
            viewDashed = itemView.findViewById(R.id.viewDashed);
            cat="";
        }
    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public model1 getItem(int position) {

        return modelsAll.get(position);
    }
    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
