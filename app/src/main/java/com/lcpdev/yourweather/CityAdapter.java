package com.lcpdev.yourweather;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林楚鹏 on 2017/1/20.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mDataList;
    public CityAdapter(List<String> dataList){
        mDataList=dataList;
    }
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view =LayoutInflater.from(mContext).inflate(R.layout.item_city,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
            View weatherView;
            CardView cardView;
            TextView itemCity;



        public ViewHolder(View view) {
            super(view);
            weatherView=view;
            cardView=(CardView) view;
            itemCity= (TextView) view.findViewById(R.id.item_city);
        }


    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public void onBindViewHolder(final CityAdapter.ViewHolder holder,final int position) {
        holder.itemCity.setText(mDataList.get(position));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }
    }



